// $Id: svn:keywords $
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.Action;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.undo.UndoableAction;

/**
 * Action to move an Attribute or an Opetation up or down in a class.
 *
 * @author bszanto
 */
public class ActionMoveUpDown extends UndoableAction 
    implements TargetListener {

    /**
     * The UP direction.
     */
    public static final int MOVE_UP = 1;
    /**
     * The DOWN direction.
     */
    public static final int MOVE_DOWN = 2;

    /**
     * The direction in which the attribute should be moved.
     */
    private int dir;

    /**
     * The contructor.
     * @param direction in which the attribute should be moved.
     */
    public ActionMoveUpDown(int direction) {
        dir = direction;
        if (dir == MOVE_UP) {
            putValue(Action.SMALL_ICON, 
                    ResourceLoaderWrapper.lookupIconResource("NudgeUp"));
            putValue(Action.SHORT_DESCRIPTION, 
                    Translator.localize("button.move-up"));
        } else if (dir == MOVE_DOWN) {
            putValue(Action.SMALL_ICON, 
                    ResourceLoaderWrapper.lookupIconResource("NudgeDown"));
            putValue(Action.SHORT_DESCRIPTION, 
                    Translator.localize("button.move-down"));
        }
    }
    
    /*
     * @see javax.swing.Action#isEnabled()
     */
    public boolean isEnabled() {
        Object target = TargetManager.getInstance().getModelTarget();
        Object parent = Model.getFacade().getModelElementContainer(target);
        List family = null;
        if (Model.getFacade().isAAttribute(target)) {
            family = Model.getFacade().getAttributes(parent);
        } else if(Model.getFacade().isAOperation(target)) {
            family = Model.getFacade().getOperations(parent);
        }      
        if (family.size() >= 2) {
            int index = family.indexOf(target);
            if ((dir == MOVE_UP && index >= 1) 
                    || (dir == MOVE_DOWN && index < family.size() - 1)) {
                return true;
            } else {
                return false;
            }
        } else { 
            return false;
        }
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object target = TargetManager.getInstance().getModelTarget();
        Object parent = Model.getFacade().getModelElementContainer(target);
        List family = null;
        if (Model.getFacade().isAAttribute(target)) {
            family = Model.getFacade().getAttributes(parent);
        } else if(Model.getFacade().isAOperation(target)) {
            family = Model.getFacade().getOperations(parent);
        }

        if (family.size() >= 2) {
            int index = family.indexOf(target);
            if (dir == MOVE_UP && index >= 1) {
                index--;
            } else if (dir == MOVE_DOWN && index < family.size() - 1) {
                index++;
            }
            Model.getCoreHelper().removeFeature(parent, target);
            Model.getCoreHelper().addFeature(parent, index, target);
        }
    }
    
    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        setEnabled(isEnabled());
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        setEnabled(isEnabled());
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setEnabled(isEnabled());
    }
}
