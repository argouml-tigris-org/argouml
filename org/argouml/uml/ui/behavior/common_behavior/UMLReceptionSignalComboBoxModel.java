// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.common_behavior;

import java.beans.PropertyChangeEvent;
import java.util.Collection;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.uml.ui.UMLComboBoxModel2;


/**
 * The model for the signal combobox on the reception proppanel.
 */
public class UMLReceptionSignalComboBoxModel extends UMLComboBoxModel2 {

    /**
     * Constructor for UMLReceptionSignalComboBoxModel.
     */
    public UMLReceptionSignalComboBoxModel() {
        super("signal", false);
        Model.getPump().addClassModelEventListener(this,
                Model.getMetaTypes().getNamespace(), "ownedElement");
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        Object target = getTarget();
        if (Model.getFacade().isAReception(target)) {
            Object rec = /*(MReception)*/ target;
            removeAllElements();
            Project p = ProjectManager.getManager().getCurrentProject();
            Object model = p.getRoot();
            setElements(Model.getModelManagementHelper()
                    .getAllModelElementsOfKindWithModel(
                            model,
                            Model.getMetaTypes().getSignal()));
            setSelectedItem(Model.getFacade().getSignal(rec));
        }

    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object m) {
        return Model.getFacade().isASignal(m);
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        if (getTarget() != null) {
            return Model.getFacade().getSignal(getTarget());
        }
        return null;
    }

    /**
     * Override UMLComboBoxModel2's default handling of RemoveAssociation. We
     * get this from MDR for the previous signal when a different signal is
     * selected. Don't let that remove it from the combo box. Only remove it if
     * the signal was removed from the namespace.
     * <p>
     *
     * @see org.argouml.uml.ui.UMLComboBoxModel2#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt instanceof RemoveAssociationEvent) {
            if ("ownedElement".equals(evt.getPropertyName())) {
                Object o = getChangedElement(evt);
                if (contains(o)) {
                    buildingModel = true;
                    if (o instanceof Collection) {
                        removeAll((Collection) o);
                    } else {
                        removeElement(o);
                    }
                    buildingModel = false;
                }
            }
        } else {
            super.propertyChange(evt);
        }
    }

}
