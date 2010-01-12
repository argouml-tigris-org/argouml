/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007-2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.UndoableAction;
import org.argouml.uml.diagram.PathContainer;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;


/**
 * This action shows or hides the path in the name.
 *
 * @author Michiel
 */
class ActionSetPath extends UndoableAction {

    private static final UndoableAction SHOW_PATH =
        new ActionSetPath(false);
    private static final UndoableAction HIDE_PATH =
        new ActionSetPath(true);

    private boolean isPathVisible;

    /**
     * Constructor.
     *
     * @param isVisible <tt>true</tt> if the path is visible.
     */
    public ActionSetPath(boolean isVisible) {
        super();
        isPathVisible = isVisible;
        String name;
        if (isVisible) {
            name = Translator.localize("menu.popup.hide.path");
        } else {
            name = Translator.localize("menu.popup.show.path");
        }
        putValue(Action.NAME, name);
    }
    
    /**
     * Static function to return the path show and/or hide actions 
     * needed for the selected Figs.
     * 
     * @return Only returns the actions for the menu-items that make sense for
     *         the current selection.
     */
    public static Collection<UndoableAction> getActions() {
        Collection<UndoableAction> actions = new ArrayList<UndoableAction>();
        Editor ce = Globals.curEditor();
        List<Fig> figs = ce.getSelectionManager().getFigs();
        for (Fig f : figs) {
            if (f instanceof PathContainer) {
                Object owner = f.getOwner();
                if (Model.getFacade().isAModelElement(owner)) {
                    Object ns = Model.getFacade().getNamespace(owner);
                    if (ns != null) {
                        /* Only show the path item when there is 
                         * an owning namespace. */
                        if (((PathContainer) f).isPathVisible()) {
                            actions.add(HIDE_PATH);
                            break;
                        }
                    }
                }
            }
        }
        for (Fig f : figs) {
            if (f instanceof PathContainer) {
                Object owner = f.getOwner();
                if (Model.getFacade().isAModelElement(owner)) {
                    Object ns = Model.getFacade().getNamespace(owner);
                    if (ns != null) {
                        /* Only show the path item when there is 
                         * an owning namespace. */
                        if (!((PathContainer) f).isPathVisible()) {
                            actions.add(SHOW_PATH);
                            break;
                        }
                    }
                }
            }
        }
        return actions;
    }

    /**
     * @see org.tigris.gef.undo.UndoableAction#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);

        // enumerate all selected figures and update their path accordingly  
        Iterator< ? > i =
            Globals.curEditor().getSelectionManager().selections().iterator();
        while (i.hasNext()) {
            Selection sel = (Selection) i.next();
            Fig f = sel.getContent();
		        
            if (f instanceof PathContainer) {
                ((PathContainer) f).setPathVisible(!isPathVisible);
            }
        }
    }
    
}
