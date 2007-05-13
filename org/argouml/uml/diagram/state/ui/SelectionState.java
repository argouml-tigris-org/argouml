// $Id$
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

package org.argouml.uml.diagram.state.ui;

import javax.swing.Icon;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.presentation.Fig;

/**
 * @author jrobbins@ics.uci.edu
 */
public class SelectionState extends SelectionNodeClarifiers2 {

    private static Icon trans =
	ResourceLoaderWrapper.lookupIconResource("Transition");

    private static Icon icons[] = 
    {null,
     null,
     trans,
     trans,
     null,
    };
    
    // TODO: I18N required
    private static String instructions[] = 
    {null,
     null,
     "Add an outgoing transition",
     "Add an incoming transition",
     null,
     "Move object(s)",
    };
    private boolean showIncoming = true;

    private boolean showOutgoing = true;

    /**
     * Construct a new SelectionState for the given Fig.
     *
     * @param f The given Fig.
     */
    public SelectionState(Fig f) {
	super(f);
    }

    /**
     * @param b true if the buton is enabled
     */
    public void setIncomingButtonEnabled(boolean b) {
	showIncoming = b;
    }

    /**
     * @param b true if the buton is enabled
     */
    public void setOutgoingButtonEnabled(boolean b) {
	showOutgoing = b;
    }

    @Override
    protected Icon[] getIcons() {
        Icon workingIcons[] = new Icon[icons.length];
        System.arraycopy(icons, 0, workingIcons, 0, icons.length);

        if (!showOutgoing) {
            workingIcons[LEFT] = null;
        }
        if (!showIncoming) {
            workingIcons[RIGHT] = null;
        }
        
        return workingIcons;
    }

    @Override
    protected String getInstructions(int index) {
        return instructions[index - BASE];
    }

    @Override
    protected Object getNewEdgeType(int index) {
        return Model.getMetaTypes().getTransition();
    }

    @Override
    protected Object getNewNode(int index) {
        return Model.getStateMachinesFactory().createSimpleState();
    }
    
    @Override
    protected Object getNewNodeType(int index) {
        return Model.getMetaTypes().getSimpleState();
    }

    @Override
    protected boolean isReverseEdge(int index) {
        if (index == 13) {
            return true;
        }
        return false;
    }

}
