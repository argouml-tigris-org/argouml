// $Id: UMLAssociationEndTargetScopeCheckbox.java 15920 2008-10-14 18:03:33Z tfmorris $
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

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.UndoableAction;

/**
 *
 * @author jaap.branderhorst@xs4all.nl
 * @since Jan 4, 2003
 */
class UMLAssociationEndTargetScopeCheckbox extends UMLCheckBox {

    /**
     * The class uid
     */
    private static final long serialVersionUID = -8161016926703486344L;

    /**
     * Constructor for UMLAssociationEndTargetScopeCheckbox.
     */
    public UMLAssociationEndTargetScopeCheckbox(final String propertyName, final Object target) {
        super(Translator.localize("label.static"),
                new ActionSetAssociationEndTargetScope(), 
                propertyName,
                target);
    }

    /*
     * @see org.argouml.uml.ui.UMLCheckBox#buildModel()
     */
    public void buildModel() {
        if (getTarget() != null) {
            Object associationEnd = getTarget();
            setSelected(Model.getFacade().isStatic(associationEnd));
        }
    }
    
    /**
     *
     * @author jaap.branderhorst@xs4all.nl
     * @since Jan 4, 2003
     */
    private static class ActionSetAssociationEndTargetScope extends UndoableAction {

        /**
         * 
         */
        private static final long serialVersionUID = 5012290492852358140L;

        /**
         * Constructor for ActionSetElementOwnershipSpecification.
         */
        protected ActionSetAssociationEndTargetScope() {
            super(Translator.localize("Set"), null);
            // Set the tooltip string:
            putValue(Action.SHORT_DESCRIPTION, 
                    Translator.localize("Set"));
        }
 
        /*
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            UMLCheckBox source = (UMLCheckBox) e.getSource();
            Object target = source.getTarget();
            if (Model.getFacade().isAAssociationEnd(target)) {
                Object m = target;
                Model.getCoreHelper().setStatic(m, source.isSelected());
            }
        }
    }
}
