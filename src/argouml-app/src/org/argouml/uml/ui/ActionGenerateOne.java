/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.UndoableAction;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.generator.ui.ClassGenerationDialog;
import org.tigris.gef.presentation.Fig;

/**
 * Action to trigger generation of source
 * for all selected classes and interfaces.
 *
 * @stereotype singleton
 */
public class ActionGenerateOne extends UndoableAction {

    /**
     * The constructor.
     */
    public ActionGenerateOne() {
        super(Translator.localize("action.generate-selected-classes"), null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("action.generate-selected-classes"));
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
    	super.actionPerformed(ae);
        List classes = getCandidates();
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
    @Override
    public boolean isEnabled() {
        // TODO: this seems to be called at startup only so no check so far
        return true;
        //List classes = getCandidates();
        //return classes.size() > 0;
    }

    /**
     * @return the candidates for generation
     */
    private List getCandidates() {
        List classes = new ArrayList();
        Collection targets = TargetManager.getInstance().getTargets();
        for (Object target : targets) {
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

}
