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

package org.argouml.uml.diagram.deployment.ui;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.Icon;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ModeCreateEdgeAndNode;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.base.ModeModify;
import org.tigris.gef.base.SelectionManager;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.Handle;

/**
 * @author 5eichler@informatik.uni-hamburg.de
 */
public class SelectionNodeInstance extends SelectionNodeClarifiers {

    private static final Logger LOG =
        Logger.getLogger(SelectionNodeInstance.class);
    ////////////////////////////////////////////////////////////////
    // constants
    private static Icon dep =
	ResourceLoaderWrapper.lookupIconResource("Link");



    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Construct a new SelectionNodeInstance for the given Fig.
     *
     * @param f The given Fig.
     */
    public SelectionNodeInstance(Fig f) { super(f); }

    /**
     * @see org.tigris.gef.base.Selection#hitHandle(java.awt.Rectangle,
     * org.tigris.gef.presentation.Handle)
     */
    public void hitHandle(Rectangle r, Handle h) {
	super.hitHandle(r, h);
	if (h.index != -1) {
	    return;
	}
	if (!isPaintButtons()) {
	    return;
	}
	Editor ce = Globals.curEditor();
	SelectionManager sm = ce.getSelectionManager();
	if (sm.size() != 1) {
	    return;
	}
	ModeManager mm = ce.getModeManager();
	if (mm.includes(ModeModify.class) && getPressedButton() == -1) {
	    return;
	}
	int cx = getContent().getX();
	int cy = getContent().getY();
	int cw = getContent().getWidth();
	int ch = getContent().getHeight();
	int aw = dep.getIconWidth();
	int ah = dep.getIconHeight();
	if (hitAbove(cx + cw / 2, cy, aw, ah, r)) {
	    h.index = 10;
	    h.instructions = "Add a component";
	} else if (hitBelow(cx + cw / 2, cy + ch, aw, ah, r)) {
	    h.index = 11;
	    h.instructions = "Add a component";
	} else if (hitLeft(cx + cw, cy + ch / 2, aw, ah, r)) {
	    h.index = 12;
	    h.instructions = "Add a component";
	} else if (hitRight(cx, cy + ch / 2, aw, ah, r)) {
	    h.index = 13;
	    h.instructions = "Add a component";
	} else {
	    h.index = -1;
	    h.instructions = "Move object(s)";
	}
    }


    /**
     * @see org.tigris.gef.base.SelectionButtons#paintButtons(Graphics)
     */
    public void paintButtons(Graphics g) {
	int cx = getContent().getX();
	int cy = getContent().getY();
	int cw = getContent().getWidth();
	int ch = getContent().getHeight();
	paintButtonAbove(dep, g, cx + cw / 2, cy, 10);
	paintButtonBelow(dep, g, cx + cw / 2, cy + ch, 11);
	paintButtonLeft(dep, g, cx + cw, cy + ch / 2, 12);
	paintButtonRight(dep, g, cx, cy + ch / 2, 13);
    }


    /**
     * @see org.tigris.gef.base.Selection#dragHandle(int, int, int, int,
     * org.tigris.gef.presentation.Handle)
     */
    public void dragHandle(int mX, int mY, int anX, int anY, Handle hand) {
	if (hand.index < 10) {
	    setPaintButtons(false);
	    super.dragHandle(mX, mY, anX, anY, hand);
	    return;
	}
	int cx = getContent().getX(), cy = getContent().getY();
	int cw = getContent().getWidth(), ch = getContent().getHeight();
	Object edgeType = null;
	Object nodeType = Model.getMetaTypes().getNodeInstance();
	int bx = mX, by = mY;
	boolean reverse = false;
	switch (hand.index) {
	case 10: //add dep
	    edgeType = Model.getMetaTypes().getLink();
	    by = cy;
	    bx = cx + cw / 2;
	    break;
	case 11: //add dep
	    edgeType = Model.getMetaTypes().getLink();
	    reverse = true;
	    by = cy + ch;
	    bx = cx + cw / 2;
	    break;
	case 12: //add dep
	    edgeType = Model.getMetaTypes().getLink();
	    by = cy + ch / 2;
	    bx = cx + cw;
	    break;
	case 13: // add dep
	    edgeType = Model.getMetaTypes().getLink();
	    reverse = true;
	    by = cy + ch / 2;
	    bx = cx;
	    break;
	default:
	    LOG.warn("invalid handle number");
	    break;
	}
	if (edgeType != null && nodeType != null) {
	    Editor ce = Globals.curEditor();
	    ModeCreateEdgeAndNode m =
	        new ModeCreateEdgeAndNode(ce, edgeType, false, this);
	    m.setup((FigNode) getContent(), getContent().getOwner(),
	            bx, by, reverse);
	    ce.pushMode(m);
	}

    }


    /**
     * @see org.tigris.gef.base.SelectionButtons#createEdgeAbove(
     *         org.tigris.gef.graph.MutableGraphModel, java.lang.Object)
     */
    protected Object createEdgeAbove(MutableGraphModel gm, Object newNode) {
        return gm.connect(getContent().getOwner(), newNode,
			  (Class) Model.getMetaTypes().getLink());
    }

    /**
     * @see org.tigris.gef.base.SelectionButtons#createEdgeLeft(
     *         org.tigris.gef.graph.MutableGraphModel, java.lang.Object)
     */
    protected Object createEdgeLeft(MutableGraphModel gm, Object newNode) {
        return gm.connect(newNode, getContent().getOwner(),
			  (Class) Model.getMetaTypes().getLink());
    }

    /**
     * @see org.tigris.gef.base.SelectionButtons#createEdgeRight(
     *         org.tigris.gef.graph.MutableGraphModel, java.lang.Object)
     */
    protected Object createEdgeRight(MutableGraphModel gm, Object newNode) {
        return gm.connect(getContent().getOwner(), newNode,
			  (Class) Model.getMetaTypes().getLink());
    }

    /**
     * @see org.tigris.gef.base.SelectionButtons#createEdgeUnder(
     *         org.tigris.gef.graph.MutableGraphModel, java.lang.Object)
     */
    protected Object createEdgeUnder(MutableGraphModel gm, Object newNode) {
        return gm.connect(newNode, getContent().getOwner(),
			  (Class) Model.getMetaTypes().getLink());
    }

    /**
     * @see org.tigris.gef.base.SelectionButtons#getNewNode(int)
     */
    protected Object getNewNode(int buttonCode) {
        return Model.getCommonBehaviorFactory().createNodeInstance();
    }


} /* end class SelectionNodeInstance */

