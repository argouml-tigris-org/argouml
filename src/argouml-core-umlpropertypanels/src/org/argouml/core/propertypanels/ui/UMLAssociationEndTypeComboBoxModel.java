/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
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

// $header$
package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.UndoableAction;

/**
 * @since Nov 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
class UMLAssociationEndTypeComboBoxModel
    extends UMLStructuralFeatureTypeComboBoxModel {

    /**
     * The class uid
     */
    private static final long serialVersionUID = 61755481049159333L;

    /**
     * Constructor for UMLAssociationEndTypeComboBoxModel.
     */
    public UMLAssociationEndTypeComboBoxModel(
            final String propertyName,
            final Object target) {
        super(propertyName, target);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
	if (getTarget() != null) {
            return Model.getFacade().getType(getTarget());
        }
        return null;
    }
    
    public Action getAction() {
        return new ActionSetAssociationEndType();
    }

    private class ActionSetAssociationEndType extends UndoableAction {

        /**
         * The class uid
         */
        private static final long serialVersionUID = 5055557403240849587L;


        /**
         * Constructor for ActionSetStructuralFeatureType.
         */
        public ActionSetAssociationEndType() {
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
            Object end = null;
            UMLComboBox box = (UMLComboBox) source;
            Object o = getTarget();
            if (Model.getFacade().isAAssociationEnd(o)) {
                end = o;
                oldClassifier = Model.getFacade().getType(end);
            }
            o = box.getSelectedItem();
            if (Model.getFacade().isAClassifier(o)) {
                newClassifier = o;
            }
            if (newClassifier != oldClassifier && end != null
                    && newClassifier != null) {
                Model.getCoreHelper().setType(end, newClassifier);
                super.actionPerformed(e);
            }
        }
    }
}
