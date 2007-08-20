// $Id:UMLReceptionSignalComboBox.java 11516 2006-11-25 04:30:15Z tfmorris $
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

import java.awt.event.ActionEvent;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLListCellRenderer2;
import org.argouml.uml.ui.UMLUserInterfaceContainer;

/**
 * Combobox for signals on the reception proppanel.
 */
public class UMLReceptionSignalComboBox extends UMLComboBox2 {

    /**
     * Constructor for UMLSignalComboBox.
     * @param container the containing UI element
     * @param arg0 the model
     */
    public UMLReceptionSignalComboBox(
            UMLUserInterfaceContainer container,
            UMLComboBoxModel2 arg0) {
        // TODO: This super constructor has been deprecated
        super(arg0);
        setRenderer(new UMLListCellRenderer2(true));
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBox2#doIt(ActionEvent)
     */
    protected void doIt(ActionEvent event) {
        Object o = getModel().getElementAt(getSelectedIndex());
        Object signal = o;
        Object reception = getTarget();
        if (signal != Model.getFacade().getSignal(reception)) {
            Model.getCommonBehaviorHelper().setSignal(reception, signal);
        }
    }

}
