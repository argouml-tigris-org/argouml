// $Id:ActionGenerateOne.java 11516 2006-11-25 04:30:15Z tfmorris $
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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.generator.ui.ClassGenerationDialog;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.undo.UndoableAction;

/**
 * Action to trigger generation of source
 * for all selected classes and interfaces.
 *
 * @stereotype singleton
 */
public class ActionGenerateOne extends UndoableAction {

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    public ActionGenerateOne() {
        super(Translator.localize("action.generate-selected-classes"), null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("action.generate-selected-classes"));
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
    	super.actionPerformed(ae);
        Vector classes = getCandidates();
        // There is no need to test if classes is empty because
        // the shouldBeEnabled mechanism blanks out the possibility to
        // choose this alternative in this case.
        ClassGenerationDialog cgd = new ClassGenerationDialog(classes);
        cgd.setVisible(true);
    }

    /**
     * @return true if the action is enabled and there is at least a 
     * candidate class
     * @see org.tigris.gef.undo.UndoableAction#isEnabled()
     */
    public boolean isEnabled() {
        if (!super.isEnabled()) {
            return false;
	}
        Vector classes = getCandidates();
        return classes.size() > 0;
    }

    /**
     * @return the candidates for generation
     */
    private Vector getCandidates() {
        Vector classes = new Vector();
        Collection targets = TargetManager.getInstance().getTargets();
        Iterator it = targets.iterator();
        while (it.hasNext()) {
            Object target = it.next();
            if (target instanceof Fig) {
                target = ((Fig) target).getOwner();
            }
            if (Model.getFacade().isAClass(target)
                || Model.getFacade().isAInterface(target)) {
                classes.add(target);
            }
        }
        return classes;
    }

} /* end class ActionGenerateOne */
