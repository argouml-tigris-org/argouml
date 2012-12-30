/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
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

// $header$
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
 * TODO: For UML 2.x, powertypes are accessed indirectly through the 
 * GeneralizationSets that contain a Generalization.
 * 
 * @since Nov 3, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
class UMLGeneralizationPowertypeComboBoxModel
    extends UMLComboBoxModel {

    /**
     * The class uid
     */
    private static final long serialVersionUID = -7164447610238810497L;

    /**
     * Constructor for UMLGeneralizationPowertypeComboBoxModel.
     */
    public UMLGeneralizationPowertypeComboBoxModel(
            final String propertyName,
            final Object target) {
        super(target, propertyName, true);
        Model.getPump().addClassModelEventListener(this,
                Model.getMetaTypes().getNamespace(), "ownedElement");
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        if (getTarget() != null) {
            return Model.getFacade().getPowertype(getTarget());
        }
        return null;
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel#buildModelList()
     */
    protected void buildModelList() {
        Set<Object> elements = new TreeSet<Object>(new PathComparator());
        Project p = ProjectManager.getManager().getCurrentProject();
        for (Object model : p.getUserDefinedModelList()) {
	    elements.addAll(Model.getModelManagementHelper()
                .getAllModelElementsOfKind(model,
                        Model.getMetaTypes().getClassifier()));
        }

        elements.addAll(p.getProfileConfiguration().findByMetaType(
                Model.getMetaTypes().getClassifier()));
        removeAllElements();
        addAll(elements);
    }
    
    @Override
    protected void buildMinimalModelList() {
        Collection list = new ArrayList(1);
        Object element = getSelectedModelElement();
        if (element == null) {
            element = " ";
        }
        list.add(element);
        setElements(list);
    }
    
    @Override
    protected boolean isLazy() {
        return true;
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAClassifier(element);
    }
    
    public Action getAction() {
        return new ActionSetGeneralizationPowertype();
    }

    private class ActionSetGeneralizationPowertype extends UndoableAction {

        /**
         * The class uid
         */
        private static final long serialVersionUID = -6222244327700907236L;

        /**
         * Constructor for ActionSetStructuralFeatureType.
         */
        protected ActionSetGeneralizationPowertype() {
            super(Translator.localize("Set"), null);
            // Set the tooltip string:
            putValue(Action.SHORT_DESCRIPTION, 
                    Translator.localize("Set"));
        }


        /*
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            Object source = e.getSource();
            Object oldClassifier = null;
            Object newClassifier = null;
            Object gen = null;
            UMLComboBox box = (UMLComboBox) source;
            Object o = box.getTarget();
            if (Model.getFacade().isAGeneralization(o)) {
                gen = o;
                oldClassifier = Model.getFacade().getPowertype(gen);
            }
            o = box.getSelectedItem();
            if (Model.getFacade().isAClassifier(o)) {
                newClassifier = o;
            }
            if (newClassifier != oldClassifier && gen != null) {
                Model.getCoreHelper().setPowertype(gen, newClassifier);
            }
        }
    }
}
