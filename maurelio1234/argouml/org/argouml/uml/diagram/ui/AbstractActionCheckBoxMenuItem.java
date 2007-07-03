// $Id: AbstractActionCheckBoxMenuItem.java 10733 2006-06-11 14:56:02Z mvw $
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.undo.UndoableAction;

/**
 * This class adds the common algorithms
 * for handling multiple targets
 * for a checkbox menuitem to the UMLAction.
 *
 * @author mvw@tigris.org
 */
abstract class AbstractActionCheckBoxMenuItem extends UndoableAction {

    /**
     * @param key the name (to be localized)
     */
    public AbstractActionCheckBoxMenuItem(String key) {
        super(Translator.localize(key), null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize(key));
    }

    /**
     * This action should be enabled when: <ul>
     * <li>all targets are modelelements that support this checkmark and
     * <li>all targets have the checkmark either on or off
     *     (mixed is not yet supported, but could be if a tri-state
     *     checkmark is implemented).
     * </ul>
     */
    public boolean isEnabled() {
        boolean result = true;
        boolean commonValue = true; // only initialized to prevent warning
        boolean first = true;
        Iterator i = TargetManager.getInstance().getTargets().iterator();
        while (i.hasNext() && result) {
            Object t = i.next();
            try {
                boolean value = valueOfTarget(t);
                if (first) {
                    commonValue = value;
                    first = false;
                }
                result &= (commonValue == value);
            } catch (IllegalArgumentException e) {
                result = false; //not supported for this target
            }
        }
        return result;
    }

    /**
     * @param t the target modelelement
     * @return the value of the checkmark for this modelelement
     */
    abstract boolean valueOfTarget(Object t);

    /**
     * This action is performed on ALL targets.
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public final void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Iterator i = TargetManager.getInstance().getTargets().iterator();
        while (i.hasNext()) {
            Object t = i.next();
            toggleValueOfTarget(t);
        }
    }

    /**
     * @param t the target modelelement
     */
    abstract void toggleValueOfTarget(Object t);
}


