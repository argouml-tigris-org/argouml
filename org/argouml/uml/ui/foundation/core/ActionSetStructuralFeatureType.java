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

package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;
/**
 * @since Nov 3, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class ActionSetStructuralFeatureType extends UndoableAction {

    private static final ActionSetStructuralFeatureType SINGLETON =
        new ActionSetStructuralFeatureType();

    /**
     * Constructor for ActionSetStructuralFeatureType.
     */
    protected ActionSetStructuralFeatureType() {
        super(Translator.localize("Set"), null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("Set"));
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object source = e.getSource();
        Object oldClassifier = null;
        Object newClassifier = null;
        Object attr = null;
        if (source instanceof UMLComboBox2) {
            UMLComboBox2 box = (UMLComboBox2) source;
            Object o = box.getTarget();
            if (Model.getFacade().isAStructuralFeature(o)) {
                attr = /*(MAttribute)*/ o;
                oldClassifier = Model.getFacade().getType(attr);
            }
            o = box.getSelectedItem();
            if (Model.getFacade().isAClassifier(o)) {
                newClassifier = /*(MClassifier)*/ o;
            }
        }
        if (newClassifier != oldClassifier && attr != null) {
            if (newClassifier != null) {
                newClassifier = /*(MClassifier)*/ Model
                    .getModelManagementHelper().getCorrespondingElement(
                                    newClassifier,
                                    Model.getFacade().getModel(attr));
            }

            Model.getCoreHelper().setType(attr, newClassifier);
        }
    }

    /**
     * @return Returns the sINGLETON.
     */
    public static ActionSetStructuralFeatureType getInstance() {
        return SINGLETON;
    }

}
