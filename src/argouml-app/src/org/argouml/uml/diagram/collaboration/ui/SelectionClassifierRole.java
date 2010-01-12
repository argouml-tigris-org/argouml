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

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram.collaboration.ui;

import javax.swing.Icon;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Mode;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.Handle;

/**
 * The selection buttons for a classifier role. <p>
 * 
 * The AssociationRoles created shall be unidirectional.
 */
public class SelectionClassifierRole extends SelectionNodeClarifiers2 {

    private static Icon assocrole =
	ResourceLoaderWrapper
	    .lookupIconResource("AssociationRole");

    private static Icon selfassoc =
        ResourceLoaderWrapper
	    .lookupIconResource("SelfAssociation");

    private static Icon icons[] = 
    {null,
     null,
     assocrole,
     assocrole,
     selfassoc,
    };
    
    // TODO: I18N required
    private static String instructions[] = 
    {null,
     null,
     "Add an outgoing classifierrole",
     "Add an incoming classifierrole",
     "Add a associationrole to this",
     "Move object(s)",
    };

    private boolean showIncoming = true;

    private boolean showOutgoing = true;

    /**
     * Construct a new SelectionClassifierRole for the given Fig.
     *
     * @param f The given Fig.
     */
    public SelectionClassifierRole(Fig f) {
	super(f);
    }

    /**
     * @param b true if the incoming button is enabled
     */
    public void setIncomingButtonEnabled(boolean b) {
	showIncoming = b;
    }

    /**
     * @param b true if the outgoing button is enabled
     */
    public void setOutgoingButtonEnabled(boolean b) {
	showOutgoing = b;
    }

    @Override
    protected Icon[] getIcons() {
        Icon workingIcons[] = new Icon[icons.length];
        System.arraycopy(icons, 0, workingIcons, 0, icons.length);

        if (!showIncoming) {
            workingIcons[BASE - LEFT] = null;
        }
        if (!showOutgoing) {
            workingIcons[BASE - RIGHT] = null;
        }
        if (!showOutgoing && !showIncoming) {
            workingIcons[BASE - LOWER_LEFT] = null;
        }
        return workingIcons;
    }

    @Override
    protected String getInstructions(int index) {
        return instructions[index - BASE];
    }

    @Override
    protected Object getNewEdgeType(int index) {
        /* The next 4 lines fix the first half of issue 5638.
         * Is there no better way? */
        Editor curEditor = Globals.curEditor();
        ModeManager modeManager = curEditor.getModeManager();
        Mode mode = modeManager.top();
        mode.setArg("unidirectional", true);

        return Model.getMetaTypes().getAssociationRole();
    }

    @Override
    protected Object getNewNodeType(int index) {
        return Model.getMetaTypes().getClassifierRole();
    }

    @Override
    protected Object getNewNode(int index) {
        return Model.getCollaborationsFactory().createClassifierRole();
    }

    @Override
    protected boolean isReverseEdge(int index) {
        if (index == LEFT) {
            return true;
        }
        return false;
    }

    @Override
    public void dragHandle(int mx, int my, int anX, int anY, Handle hand) {
        super.dragHandle(mx, my, anX, anY, hand);

        /* The next 4 lines fix the 2nd half of issue 5638.
         * Is there no better way? */
        Editor curEditor = Globals.curEditor();
        ModeManager modeManager = curEditor.getModeManager();
        Mode mode = modeManager.top();
        mode.setArg("unidirectional", true);
    }

} 
