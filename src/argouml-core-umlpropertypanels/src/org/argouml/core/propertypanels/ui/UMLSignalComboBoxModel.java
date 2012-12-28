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
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Action;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeEvent;
import org.argouml.ui.ActionCreateContainedModelElement;
import org.argouml.ui.UndoableAction;


/**
 * The model for the signal combobox on the reception proppanel.
 */
public class UMLSignalComboBoxModel extends UMLComboBoxModel {

    /**
     * Constructor for UMLReceptionSignalComboBoxModel.
     */
    public UMLSignalComboBoxModel(
        final String propertyName,
        final Object target) {
        super(target, propertyName, false);
        Model.getPump().addClassModelEventListener(this,
                Model.getMetaTypes().getNamespace(), propertyName);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        Object target = getTarget();
        if (Model.getFacade().isAReception(target) || Model.getFacade().isASignalEvent(target)) {
            removeAllElements();
            Project p = ProjectManager.getManager().getCurrentProject();
            Object model = p.getRoot();
            setElements(Model.getModelManagementHelper()
                    .getAllModelElementsOfKindWithModel(
                            model,
                            Model.getMetaTypes().getSignal()));
            setSelectedItem(Model.getFacade().getSignal(target));
        } else {
        	throw new IllegalStateException(
        			"Expected a Reception or SignalEvent - got a " + target);
        }

    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object m) {
        return Model.getFacade().isASignal(m);
    }

    /*
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
     * @param evt the event describing the property change
     */
    public void modelChanged(UmlChangeEvent evt) {
        if (evt instanceof RemoveAssociationEvent) {
            Object o = getChangedElement(evt);
            if (contains(o)) {
                if (o instanceof Collection) {
                    removeAll((Collection) o);
                } else {
                    removeElement(o);
                }
            }
        } else {
            super.propertyChange(evt);
        }
    }
    
    public List<Action> getActions() {
        final ArrayList<Action> actions = new ArrayList<Action>();
        actions.add(new ActionCreateContainedModelElement(
                Model.getMetaTypes().getSignal(), getTarget()));
        return actions;
    }
    
    public Action getAction() {
        
        return new SetAction();
    }
    
    class SetAction extends UndoableAction {
        
        /**
         * The class uid
         */
        private static final long serialVersionUID = 6281434994800778660L;

        /**
         * Constructor for ActionSetModelElementNamespace.
         */
        public SetAction() {
            super();
        }

        /*
         * @see java.awt.event.ActionListener#actionPerformed(
         * java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            UMLComboBox box = (UMLComboBox) source;
            Object o = getTarget();
            o = box.getSelectedItem();
            Object signal = o;
            if (signal != Model.getFacade().getSignal(getTarget())) {
                super.actionPerformed(e);
                Model.getCommonBehaviorHelper().setSignal(getTarget(), signal);
            }
        }
    }
}
