// $Id: UMLAssociationRoleBaseComboBoxModel.java 13324 2007-08-14 02:35:33Z tfmorris $
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

import javax.swing.Action;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;

/**
 * The combo box model for the base of an association-role.
 * The base is clearable, since the UML standard indicates multiplicity 0..1.
 * 
 * @since Oct 4, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLAssociationRoleBaseComboBoxModel extends UMLComboBoxModel {

    /**
     * The class uid
     */
    private static final long serialVersionUID = -7060017054488071743L;
    
    private Collection others = new ArrayList();

    /**
     * Constructor for UMLAssociationRoleBaseComboBoxModel.
     */
    public UMLAssociationRoleBaseComboBoxModel(
            final String propertyName,
            final Object target) {
        super(propertyName, true);
        setTarget(target);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    @Override
    protected void buildModelList() {
        removeAllElements();
        Object ar = getTarget();
        Object base = Model.getFacade().getBase(ar);
        if (Model.getFacade().isAAssociationRole(ar)) {
            setElements(
                    Model.getCollaborationsHelper().getAllPossibleBases(ar));
        }
        if (base != null) {
            addElement(base);
        }
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    @Override
    protected Object getSelectedModelElement() {
        Object ar = getTarget();
        if (Model.getFacade().isAAssociationRole(ar)) {
            Object base = Model.getFacade().getBase(ar);
            if (base != null) {
                return base;
            }
        }
        return null;
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    @Override
    protected boolean isValidElement(Object element) {
        Object ar = getTarget();
        if (Model.getFacade().isAAssociationRole(ar)) {
            Object base = Model.getFacade().getBase(ar);
            if (element == base) {
                return true;
            }
            Collection b = 
                Model.getCollaborationsHelper().getAllPossibleBases(ar);
            return b.contains(element);
        }
        return false;
    }

    /*
     * TODO: Prove that this works. 
     * The TestUMLAssociationRoleBaseComboBoxModel does not cut it. 
     * 
     * @see org.argouml.uml.ui.UMLComboBoxModel2#addOtherModelEventListeners(java.lang.Object)
     */
    @Override
    protected void addOtherModelEventListeners(Object newTarget) {
        super.addOtherModelEventListeners(newTarget);
        Collection connections = Model.getFacade().getConnections(newTarget);
        Collection types = new ArrayList();
        for (Object conn : connections) {
            types.add(Model.getFacade().getType(conn));
        }
        for (Object classifierRole : types) {
            others.addAll(Model.getFacade().getBases(classifierRole));
        }
        for (Object classifier : others) {
            Model.getPump().addModelEventListener(this, 
                    classifier, "feature");
        }
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#removeOtherModelEventListeners(java.lang.Object)
     */
    @Override
    protected void removeOtherModelEventListeners(Object oldTarget) {
        super.removeOtherModelEventListeners(oldTarget);
        for (Object classifier : others) {
            Model.getPump().removeModelEventListener(this, 
                    classifier, "feature");
        }
        others.clear();
    }
    
    public Action getAction() {
        return new ActionSetAssociationRoleBase();
    }
    
    private class ActionSetAssociationRoleBase extends UndoableAction {

        /**
         * The class uid
         */
        private static final long serialVersionUID = -3966106395848112765L;

        /**
         * Constructor for ActionSetAssociationRoleBase.
         */
        public ActionSetAssociationRoleBase() {
            super();
        }

        /*
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            if (e.getSource() instanceof UMLComboBox2) {
                UMLComboBox2 source = (UMLComboBox2) e.getSource();
                Object assoc = source.getSelectedItem();
                Object ar = source.getTarget();
                if (Model.getFacade().getBase(ar) == assoc) {
                    return; // base is already set to this assoc...
                    /* This check is needed, otherwise the setbase()
                     *  below gives an exception.*/
                }
                if (Model.getFacade().isAAssociation(assoc)
                        && Model.getFacade().isAAssociationRole(ar)) {
                    Model.getCollaborationsHelper().setBase(ar, assoc);
                }
            }
        }

    }
}
