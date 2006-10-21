// $Id$
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Action;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;

/**
 * Action to set the baseclass of a stereotype.
 * 
 * @author mkl
 */
public class ActionSetMetaClass extends UndoableAction {

    /**
     * The Singleton.
     */
    public static final ActionSetMetaClass SINGLETON =
	new ActionSetMetaClass();

    /**
     * Constructor.
     */
    public ActionSetMetaClass() {
        super(Translator.localize("Set"),
                ResourceLoaderWrapper.lookupIcon("Set"));
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
        Object newBase = null;
        Object oldBase = null;
        Object stereo = null;
        if (source instanceof UMLComboBox2) {
            UMLComboBox2 combo = (UMLComboBox2) source;
            stereo = combo.getTarget();
            if (Model.getFacade().isAStereotype(stereo)) {
                Collection baseClasses = Model.getFacade().getBaseClasses(stereo);
                Iterator iter = baseClasses != null ? baseClasses.iterator() : null;
                oldBase = iter != null ? iter.next() : null;
                newBase = combo.getSelectedItem();
                if (newBase != null) { // TODO: How come this happens?
                    if (newBase != oldBase) {
                        Model.getFacade().getBaseClasses(stereo).clear(); // TODO: this works?
                        Model.getExtensionMechanismsHelper().addBaseClass(
                                stereo,
                                newBase);
                    } else {
                        if (newBase != null && newBase.equals("")) {
                            Model.getFacade().getBaseClasses(stereo).clear(); // TODO: this works?
                            Model.getExtensionMechanismsHelper().addBaseClass(
                                    stereo,
                                    "ModelElement");
                        }
                    }
                }
            }
        }
    }
}
