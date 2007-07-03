// $Id: ActionSetParameterDirectionKind.java 12831 2007-06-14 18:44:38Z tfmorris $
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
import javax.swing.JRadioButton;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLRadioButtonPanel;
import org.tigris.gef.undo.UndoableAction;

/**
 * An action to set the direction of a parameter.
 *
 * @author mkl
 *
 */
public class ActionSetParameterDirectionKind extends UndoableAction {

    private static final ActionSetParameterDirectionKind SINGLETON =
        new ActionSetParameterDirectionKind();

    /**
     * IN_COMMAND determines the kind of direction.
     */
    public static final String IN_COMMAND = "in";

    /**
     * OUT_COMMAND determines the kind of direction.
     */
    public static final String OUT_COMMAND = "out";

    /**
     * INOUT_COMMAND determines the kind of direction.
     */
    public static final String INOUT_COMMAND = "inout";

    /**
     * RETURN_COMMAND determines the kind of direction.
     */
    public static final String RETURN_COMMAND = "return";
    
    /**
     * Constructor for ActionSetElementOwnershipSpecification.
     */
    protected ActionSetParameterDirectionKind() {
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
        if (e.getSource() instanceof JRadioButton) {
            JRadioButton source = (JRadioButton) e.getSource();
            String actionCommand = source.getActionCommand();
            Object target = ((UMLRadioButtonPanel) source.getParent())
                    .getTarget();
            if (Model.getFacade().isAParameter(target)) {
                Object kind = null;
                if (actionCommand == null) {
                    kind = null;
                } else if (actionCommand.equals(IN_COMMAND)) {
                    kind = Model.getDirectionKind().getInParameter();
                } else if (actionCommand.equals(OUT_COMMAND)) {
                    kind = Model.getDirectionKind().getOutParameter();
                } else if (actionCommand.equals(INOUT_COMMAND)) {
                    kind = Model.getDirectionKind().getInOutParameter();
                } else if (actionCommand.equals(RETURN_COMMAND)) {
                    kind = Model.getDirectionKind().getReturnParameter();
                }
                Model.getCoreHelper().setKind(target, kind);
            }
        }
    }

    /**
     * @return Returns the sINGLETON.
     */
    public static ActionSetParameterDirectionKind getInstance() {
        return SINGLETON;
    }
}
