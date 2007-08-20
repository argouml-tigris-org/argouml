// $Id:ActionSetElementOwnershipSpecification.java 12335 2007-04-04 21:15:14Z mvw $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import org.argouml.uml.ui.UMLCheckBox2;
import org.tigris.gef.undo.UndoableAction;

/**
 * This class sets the "isSpecification" for a ElementOwnership 
 * (incorporated in ModelElement for MDR) or a ElementImport.
 * 
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 * @stereotype singleton
 */
public class ActionSetElementOwnershipSpecification extends UndoableAction {

    private static final ActionSetElementOwnershipSpecification SINGLETON =
        new ActionSetElementOwnershipSpecification();

    /**
     * Constructor for ActionSetElementOwnershipSpecification.
     */
    protected ActionSetElementOwnershipSpecification() {
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
        if (e.getSource() instanceof UMLCheckBox2) {
            UMLCheckBox2 source = (UMLCheckBox2) e.getSource();
            Object target = source.getTarget();
            if (Model.getFacade().isAModelElement(target)
                    || Model.getFacade().isAElementImport(target)) {
                Object m = target;
                Model.getModelManagementHelper().setSpecification(m,
                        !Model.getFacade().isSpecification(m));
            }
        }
    }

    /**
     * @return Returns the SINGLETON.
     */
    public static ActionSetElementOwnershipSpecification getInstance() {
        return SINGLETON;
    }

}
