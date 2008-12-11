// $Id$
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

package org.argouml.uml.ui.foundation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.util.PathComparator;

/**
 * The combobox model for the type belonging to some attribute.
 *
 * @since Nov 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLStructuralFeatureTypeComboBoxModel extends UMLComboBoxModel2 {

    /**
     * Constructor for UMLStructuralFeatureTypeComboBoxModel.
     */
    public UMLStructuralFeatureTypeComboBoxModel() {
        super("type", false);
    }

    /*
     * This is explained by WFR 2 of a StructuralFeature: 
     * The type of a StructuralFeature must be a Class, DataType, or Interface.
     * 
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAClass(element)
                || Model.getFacade().isAInterface(element)
                || Model.getFacade().isADataType(element);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    @SuppressWarnings("unchecked")
    protected void buildModelList() {
        Set<Object> elements = new TreeSet<Object>(new PathComparator());

        Project p = ProjectManager.getManager().getCurrentProject();
        if (p == null) {
            return;
        }
        
        for (Object model : p.getUserDefinedModelList()) {
            elements.addAll(Model.getModelManagementHelper()
                    .getAllModelElementsOfKind(
                            model, Model.getMetaTypes().getUMLClass()));
            elements.addAll(Model.getModelManagementHelper()
                    .getAllModelElementsOfKind(
                            model, Model.getMetaTypes().getInterface()));
            elements.addAll(Model.getModelManagementHelper()
                    .getAllModelElementsOfKind(
                            model, Model.getMetaTypes().getDataType()));
        }

        elements.addAll(p.getProfileConfiguration().findByMetaType(
                        Model.getMetaTypes().getClassifier()));

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
        setModelInvalid();
    }
    
    @Override
    protected boolean isLazy() {
        return true;
    }
    
    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        Object o = null;
        if (getTarget() != null) {
            o = Model.getFacade().getType(getTarget());
        }
        if (o == null) {
            o = "";
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

    @Override
    public void modelChanged(UmlChangeEvent evt) {
        /*
         * The default behavior for super implementation is
         * to add/remove elements from the list, but it isn't
         * that complex here, because there is no need to
         * change the list on a simple type change.
         */
        Object newSelection = getSelectedModelElement();
        if (getSelectedItem() != newSelection) {
            /* The combo is not showing the correct UML element
             * as the selected item. 
             * So, let's empty the combo model, 
             * and set the right selected UML element: */
            buildMinimalModelList();
            setSelectedItem(newSelection);
        }
        if (evt.getSource() != getTarget()) {
            /* The target model element is not changed, 
             * but something else that may be shown in the list.
             * Since this is a "lazy" model, we can simply reset the flag to
             * indicate that the model has to be built again: */
            setModelInvalid();
        }
    }

}

