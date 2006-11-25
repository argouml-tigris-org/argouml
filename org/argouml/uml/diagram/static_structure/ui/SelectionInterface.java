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

package org.argouml.uml.diagram.static_structure.ui;

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
 * @author jrobbins@ics.uci.edu
 */
public class SelectionInterface extends SelectionNodeClarifiers {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(SelectionInterface.class);

    /**
     * Remember the pressed button, 
     * for the case where the mouse is released not above a fig.
     */
    private int code;

    private static Icon realiz =
        ResourceLoaderWrapper.lookupIconResource("Realization");

    private static Icon inherit =
        ResourceLoaderWrapper.lookupIconResource("Generalization");


    /**
     * Construct a new SelectionInterface for the given Fig.
     *
     * @param f
     *            The given Fig.
     */
    public SelectionInterface(Fig f) {
        super(f);
    }

    /*
     * @see org.tigris.gef.base.Selection#hitHandle(java.awt.Rectangle,
     *      org.tigris.gef.presentation.Handle)
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
        int iw = realiz.getIconWidth();
        int ih = realiz.getIconHeight();
        int gw = inherit.getIconWidth();
        int gh = inherit.getIconHeight();
        if (hitAbove(cx + cw / 2, cy, gw, gh, r)) {
            h.index = 10;
            h.instructions = "Add an interface";
        } else if (hitBelow(cx + cw / 2, cy + ch, iw, ih, r)) {
            h.index = 11;
            h.instructions = "Add a realization";
        } else {
            h.index = -1;
            h.instructions = "Move object(s)";
        }
    }

    /*
     * @see org.tigris.gef.base.SelectionButtons#paintButtons(
     *      java.awt.Graphics)
     */
    public void paintButtons(Graphics g) {
        int cx = getContent().getX();
        int cy = getContent().getY();
        int cw = getContent().getWidth();
        int ch = getContent().getHeight();
        paintButtonAbove(inherit, g, cx + cw / 2, cy, 10);
        paintButtonBelow(realiz, g, cx + cw / 2, cy + ch, 11);
    }

    /*
     * @see org.tigris.gef.base.Selection#dragHandle(int, int, int, int,
     *      org.tigris.gef.presentation.Handle)
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
        Object nodeType = null;
        int bx = mX, by = mY;
        boolean reverse = false;
        switch (hand.index) {
        case 10: //add superclass
            edgeType = Model.getMetaTypes().getGeneralization();
            nodeType = Model.getMetaTypes().getInterface();
            by = cy;
            bx = cx + cw / 2;
            reverse = false;
            break;
        case 11: // add realization
            edgeType = Model.getMetaTypes().getAbstraction();
            nodeType = Model.getMetaTypes().getUMLClass();
            reverse = true;
            by = cy + ch;
            bx = cx + cw / 2;
            break;
        default:
            LOG.warn("invalid handle number");
            break;
        }
        code = hand.index;
        if (edgeType != null && nodeType != null) {
            Editor ce = Globals.curEditor();
            ModeCreateEdgeAndNode m =
                new ModeCreateEdgeAndNode(ce, edgeType, false, this);
            m.setup((FigNode) getContent(), getContent().getOwner(), bx, by,
                    reverse);
            ce.pushMode(m);
        }

    }

    /*
     * @see org.tigris.gef.base.SelectionButtons#createEdgeAbove(
     *         org.tigris.gef.graph.MutableGraphModel, java.lang.Object)
     */
    protected Object createEdgeAbove(MutableGraphModel gm, Object newNode) {
        return gm.connect(getContent().getOwner(), newNode,
               (Class) Model.getMetaTypes().getGeneralization());
    }

    /*
     * @see org.tigris.gef.base.SelectionButtons#createEdgeUnder(
     *      org.tigris.gef.graph.MutableGraphModel, java.lang.Object)
     */
    protected Object createEdgeUnder(MutableGraphModel gm, Object newNode) {
        return gm.connect(newNode, getContent().getOwner(), (Class) Model
                .getMetaTypes().getAbstraction());
    }

    /*
     * @see org.tigris.gef.base.SelectionButtons#getNewNode(int)
     */
    protected Object getNewNode(int buttonCode) {
        if (buttonCode < 10) {
            buttonCode = code;
        }
        if (buttonCode == 10) {
            return Model.getCoreFactory().buildInterface();
        } else {
            return Model.getCoreFactory().buildClass();
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 7209387830978444644L;
} /* end class SelectionInterface */
