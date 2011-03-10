/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *    Thomas Neustupny
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.UndoableAction;
import org.argouml.uml.util.PathComparator;

/**
 * The combobox model for the type belonging to some attribute.
 * 
 * @since Nov 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
class UMLStructuralFeatureTypeComboBoxModel extends UMLComboBoxModel {

    /**
     * The class uid
     */
    private static final long serialVersionUID = 2283910742530930285L;

    /**
     * Constructor for UMLStructuralFeatureTypeComboBoxModel.
     */
    public UMLStructuralFeatureTypeComboBoxModel(final String propertyName,
	    final Object target) {
	super(target, propertyName, true); // Allow null
    }

    /*
     * This is explained by WFR 2 of a StructuralFeature: The type of a
     * StructuralFeature must be a Class, DataType, or Interface.
     * 
     * @see org.argouml.uml.ui.UMLComboBoxModel#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
	return Model.getFacade().isAClass(element)
		|| Model.getFacade().isAInterface(element)
		|| Model.getFacade().isADataType(element);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel#buildModelList()
     */
    @SuppressWarnings("unchecked")
    protected void buildModelList() {
	Set<Object> elements = new TreeSet<Object>(new PathComparator());

	Project p = ProjectManager.getManager().getCurrentProject();
	if (p == null) {
	    return;
	}

	if (Model.getFacade().getUmlVersion().charAt(0) != '1'
		&& Model.getFacade().isAStereotype(
			Model.getFacade().getOwner(getTarget()))) {
	    // restricting types for tagged values
	    elements.addAll(Model.getExtensionMechanismsHelper()
		    .getCommonTaggedValueTypes());
	} else {
	    for (Object model : p.getUserDefinedModelList()) {
		elements.addAll(Model.getModelManagementHelper()
			.getAllModelElementsOfKind(model,
				Model.getMetaTypes().getUMLClass()));
		elements.addAll(Model.getModelManagementHelper()
			.getAllModelElementsOfKind(model,
				Model.getMetaTypes().getInterface()));
		elements.addAll(Model.getModelManagementHelper()
			.getAllModelElementsOfKind(model,
				Model.getMetaTypes().getDataType()));
	    }
	    if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
                elements.addAll(p.getProfileConfiguration().findByMetaType(
                        Model.getMetaTypes().getClassifier()));
	    } else {
		// classifier is way too much in UML 2.x
                elements.addAll(p.getProfileConfiguration().findByMetaType(
                        Model.getMetaTypes().getDataType()));
                // the minimum set of standard types
                elements.addAll(Model.getExtensionMechanismsHelper()
                        .getCommonTaggedValueTypes());
	    }
	}

	setElements(elements);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void buildMinimalModelList() {
	Collection list = new ArrayList(1);
	Object element = getSelectedModelElement();
	if (element != null) {
	    list.add(element);
	}
	setElements(list);
    }

    @Override
    protected boolean isLazy() {
	return true;
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
	Object o = null;
	if (getTarget() != null) {
	    o = Model.getFacade().getType(getTarget());
	}
	return o;
    }

    @Override
    protected void addOtherModelEventListeners(Object newTarget) {
	super.addOtherModelEventListeners(newTarget);

	Model.getPump().addClassModelEventListener(this,
		Model.getMetaTypes().getNamespace(), "ownedElement");
	Model.getPump().addClassModelEventListener(this,
		Model.getMetaTypes().getUMLClass(), "name");
	Model.getPump().addClassModelEventListener(this,
		Model.getMetaTypes().getInterface(), "name");
	Model.getPump().addClassModelEventListener(this,
		Model.getMetaTypes().getDataType(), "name");
    }

    @Override
    protected void removeOtherModelEventListeners(Object oldTarget) {
	super.removeOtherModelEventListeners(oldTarget);

	Model.getPump().removeClassModelEventListener(this,
		Model.getMetaTypes().getNamespace(), "ownedElement");
	Model.getPump().removeClassModelEventListener(this,
		Model.getMetaTypes().getUMLClass(), "name");
	Model.getPump().removeClassModelEventListener(this,
		Model.getMetaTypes().getInterface(), "name");
	Model.getPump().removeClassModelEventListener(this,
		Model.getMetaTypes().getDataType(), "name");
    }

    public Action getAction() {
	return new ActionSetStructuralFeatureType();
    }

    /**
     * @since Nov 3, 2002
     * @author jaap.branderhorst@xs4all.nl
     */
    class ActionSetStructuralFeatureType extends UndoableAction {

	/**
	 * The class uid
	 */
	private static final long serialVersionUID = 8227201276430122294L;

	/**
	 * Constructor for ActionSetStructuralFeatureType.
	 */
	protected ActionSetStructuralFeatureType() {
	    super(Translator.localize("Set"), null);
	    // Set the tooltip string:
	    putValue(Action.SHORT_DESCRIPTION, Translator.localize("Set"));
	}

	/*
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
	 * )
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	    super.actionPerformed(e);
	    assert (e.getSource() instanceof UMLComboBox);
	    Object oldClassifier = null;
	    final UMLComboBox box = (UMLComboBox) e.getSource();
	    final Object feature = box.getTarget();
	    assert (feature != null);

	    oldClassifier = Model.getFacade().getType(feature);

	    Object selectedClassifier = box.getSelectedItem();

	    final Object newClassifier;
	    if (Model.getFacade().isAElement(selectedClassifier)) {
		newClassifier = selectedClassifier;
	    } else {
		newClassifier = null;
	    }

	    if (newClassifier != oldClassifier) {
		Model.getCoreHelper().setType(feature, newClassifier);
	    }
	}
    }
}
