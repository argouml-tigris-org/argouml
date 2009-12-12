//$Id$
//Copyright (c) 2008 The Regents of the University of California. All
//Rights Reserved. Permission to use, copy, modify, and distribute this
//software and its documentation without fee, and without a written
//agreement is hereby granted, provided that the above copyright notice
//and this paragraph appear in all copies. This software program and
//documentation are copyrighted by The Regents of the University of
//California. The software program and documentation are supplied "AS
//IS", without any accompanying services from The Regents. The Regents
//does not warrant that the operation of the program will be
//uninterrupted or error-free. The end-user understands that the program
//was developed for research purposes and is advised not to rely
//exclusively on the program for any reason. IN NO EVENT SHALL THE
//UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
//SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
//ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
//THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
//WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
//MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
//PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
//CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
//UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLSearchableComboBox;
import org.tigris.gef.undo.UndoableAction;

class UMLCallActionOperationComboBox
    extends UMLComboBox {
    /**
     * The constructor.
     *
     * @param arg0 the model
     */
    public UMLCallActionOperationComboBox(UMLComboBoxModel arg0) {
        super(arg0, new SetActionOperationAction());
        setEditable(false);
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 1453984990567492914L;
}

class SetActionOperationAction extends UndoableAction {

    /**
     * The constructor.
     */
    public SetActionOperationAction() {
        super("");
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object source = e.getSource();
        if (source instanceof UMLComboBox2) {
            Object selected = ((UMLComboBox2) source).getSelectedItem();
            Object target = ((UMLComboBox2) source).getTarget();
            if (Model.getFacade().isACallAction(target)
                    && Model.getFacade().isAOperation(selected)) {
                if (Model.getFacade().getOperation(target) != selected) {
                    Model.getCommonBehaviorHelper()
                    .setOperation(target, selected);
                }
            }
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -3574312020866131632L;
}