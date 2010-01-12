/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996,2009 The Regents of the University of California. All
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

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.ui.UndoableAction;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.argouml.uml.diagram.activity.layout.ActivityDiagramLayouter;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.layout.Layouter;
import org.argouml.uml.diagram.static_structure.layout.ClassdiagramLayouter;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;

/**
 * Action to automatically lay out a diagram.
 *
 */
public class ActionLayout extends UndoableAction {

    /**
     * The constructor.
     */
    public ActionLayout() {
        super(Translator.localize("action.layout"), null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("action.layout"));
    }

    /**
     * Check whether we deal with a supported diagram type (currently only Class
     * and Activity diagrams).
     * <p>
     * NOTE: This is only called at initialization time by Swing, so the 
     * application is responsible for checking when the current diagram changes.
     * Currently done in 
     * {@link org.argouml.ui.cmd.GenericArgoMenuBar#setTarget()}.
     * 
     * @return true if the action is enabled
     */
    @Override
    public boolean isEnabled() {
        ArgoDiagram d;
        Object target = TargetManager.getInstance().getTarget();
        if (target instanceof ArgoDiagram) {
            d = (ArgoDiagram) target;
        } else {
            d = DiagramUtils.getActiveDiagram(); 
        }
        if (d instanceof UMLClassDiagram 
                || d instanceof UMLActivityDiagram) {
            return true;
        }
        return false;
    }

    /*
     * This action performs the layout and triggers a redraw of the editor pane.
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
    	super.actionPerformed(ae);
        ArgoDiagram diagram = DiagramUtils.getActiveDiagram();
        Layouter layouter;
        if (diagram instanceof UMLClassDiagram) {
            layouter = new ClassdiagramLayouter(diagram);
        } else if (diagram instanceof UMLActivityDiagram) {
            layouter = 
                 new ActivityDiagramLayouter(diagram);
        } else {
            return;
        }

        // Rearrange the diagram layout
        layouter.layout();
        diagram.damage();
    }
}
