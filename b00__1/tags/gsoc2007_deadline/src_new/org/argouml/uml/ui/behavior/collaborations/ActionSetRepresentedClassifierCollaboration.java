// $Id:ActionSetRepresentedClassifierCollaboration.java 11516 2006-11-25 04:30:15Z tfmorris $
// Copyright (c) 2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.ui.behavior.collaborations;

import java.awt.event.ActionEvent;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;

/**
 * This Action sets the represented classifier 
 * of a collaboration.
 * 
 * @author michiel
 */
class ActionSetRepresentedClassifierCollaboration extends UndoableAction {

    /**
     * Constructor for ActionSetCompositeStateConcurrent.
     */
    ActionSetRepresentedClassifierCollaboration() {
        super(Translator.localize("action.set"),
                ResourceLoaderWrapper.lookupIcon("action.set"));
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() instanceof UMLComboBox2) {
            UMLComboBox2 source = (UMLComboBox2) e.getSource();
            Object target = source.getTarget();
            Object newValue = source.getSelectedItem();
            /* The selected value may be "" to 
             * clear the represented classifier. */
            if (!Model.getFacade().isAClassifier(newValue)) {
                newValue = null;
            }
            if (Model.getFacade().getRepresentedClassifier(target)
                    != newValue) {
                Model.getCollaborationsHelper().setRepresentedClassifier(
                        target, newValue);
            }
        }
    }

}