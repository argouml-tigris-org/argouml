// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.Icon;

import org.apache.log4j.Logger;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ModeCreateEdgeAndNode;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.base.ModeModify;
import org.tigris.gef.base.SelectionButtons;
import org.tigris.gef.base.SelectionManager;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.Handle;

/**
 * Enhanced version of SelectionNodeClarifiers with the new methods
 * necessary for the enhanced support marked as abstract so that implementors
 * are forced to implement them.  SelectionNodeClarifiers is simple
 * extension of this which implements null versions of the required 
 * methods for backward compatibility with.
 * <p>
 * To upgrade subtypes of SelectionNodeClarifiers, change them to 
 * extend this class instead and implement the required abstract methods.
 *
 * @author jrobbins
 * @author Tom Morris
 */
public abstract class SelectionNodeClarifiers2 extends SelectionButtons {
    
    private static final Logger LOG =
            Logger.getLogger(SelectionNodeClarifiers2.class);


    /**
     * Construct a new SelectionNodeClarifiers for the given Fig
     * 
     * @param f
     *            the given Fig
     */
    public SelectionNodeClarifiers2(Fig f) {
        super(f);
    }
    
    /*
     * @see org.tigris.gef.base.SelectionButtons#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        ((FigNodeModelElement) getContent()).paintClarifiers(g);
        super.paint(g);
    }

    /*
     * @see org.tigris.gef.base.SelectionButtons#paintButtons(Graphics)
     */
    public void paintButtons(Graphics g) {
        Icon[] icons = getIcons();
        if (icons == null) {
            return;
        }
        int cx = getContent().getX();
        int cy = getContent().getY();
        int cw = getContent().getWidth();
        int ch = getContent().getHeight();
        
        if (icons[10] != null) {
            paintButtonAbove(icons[10], g, cx + cw / 2, cy, 10);
        }
        if (icons[11] != null) {
            paintButtonBelow(icons[11], g, cx + cw / 2, cy + ch + 2, 11);
        }
        if (icons[12] != null) {
            paintButtonLeft(icons[12], g, cx + cw + 2, cy + ch / 2, 12);
        }
        if (icons[13] != null) {
            paintButtonRight(icons[13], g, cx, cy + ch / 2, 13);
        }
        if (icons[14] != null) {
            paintButtonRight(icons[14], g, cx, cy + ch, 14);
        }
    }

    /*
     * @see org.tigris.gef.base.SelectionButtons#getNewNode(int)
     */
    protected Object getNewNode(int arg0) {
        return null;
    }
    
    /*
     * @see org.tigris.gef.base.Selection#hitHandle(java.awt.Rectangle,
     * org.tigris.gef.presentation.Handle)
     */
    public void hitHandle(Rectangle r, Handle h) {
        super.hitHandle(r, h);
        if (h.index != -1) {
            // super implementation found a hit
            return;
        }
        if (!isPaintButtons()) {
            return;
        }
        Icon[] icons = getIcons();
        if (icons == null) {
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

        /*
         * Crazy numbering scheme at work here.  Here's how the handle numbers
         * are laid out.  Values 0-7 are defined by GEF and go left to
         * right, top to bottom (ie not clockwise or counterclockwise).
         * Values 10-14 zigzag North, South, West, East, Southwest.
         * If you can correctly guess where 15 will go, you should buy
         * a lottery ticket immediately.
         *  <pre>
         *            10
         *     0-------1-------2
         *     |               |
         *  12 3               4 13
         *     |               |
         *  14 5-------6-------7
         *            11
         * </pre>
         */
        if (icons[10] != null && hitAbove(cx + cw / 2, cy, 
                icons[10].getIconWidth(), icons[10].getIconHeight(), 
                r)) {
            h.index = 10;
            h.instructions = getInstructions(h.index);
        } else if (icons[11] != null && hitBelow(cx + cw / 2, cy + ch, 
                icons[11].getIconWidth(), icons[11].getIconHeight(), 
                r)) {
            h.index = 11;
            h.instructions = getInstructions(h.index);
        } else if (icons[12] != null && hitLeft(cx + cw, cy + ch / 2, 
                icons[12].getIconWidth(), icons[12].getIconHeight(), 
                r)) {
            h.index = 12;
            h.instructions = getInstructions(h.index);
        } else if (icons[13] != null && hitRight(cx, cy + ch / 2, 
                icons[13].getIconWidth(), icons[13].getIconHeight(), 
                r)) {
            h.index = 13;
            h.instructions = getInstructions(h.index);
        } else if (icons[14] != null && hitRight(cx, cy + ch - 10, 
                icons[14].getIconWidth(), icons[14].getIconHeight(), 
                r)) {
            h.index = 14;
            h.instructions = getInstructions(h.index);
        } else {
            h.index = -1;
            h.instructions = getInstructions(15);
        }
    }

    /*
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

        int bx = mX, by = mY;

        switch (hand.index) {
        case 10:
            by = cy;
            bx = cx + cw / 2;
            break;
        case 11:
            by = cy + ch;
            bx = cx + cw / 2;
            break;
        case 12:
            by = cy + ch / 2;
            bx = cx + cw;
            break;
        case 13:
            by = cy + ch / 2;
            bx = cx;
            break;
        case 14:
            break;
        default:
            LOG.warn("invalid handle number");
            break;
        }
        
        Object nodeType = getNewNodeType(hand.index);
        Object edgeType = getNewEdgeType(hand.index);
        boolean reverse = isDragEdgeReverse(hand.index);
        
        if (edgeType != null && nodeType != null) {
            Editor ce = Globals.curEditor();
            ModeCreateEdgeAndNode m =
                new ModeCreateEdgeAndNode(ce,
                      edgeType, isEdgePostProcessRequested(), this);
            m.setup((FigNode) getContent(), getContent().getOwner(),
                    bx, by, reverse);
            ce.pushMode(m);
        }
    }

    /**
     * Get array of icons to use when drawing handles.
     * @return icon or null
     */
    abstract protected Icon[] getIcons();

    /**
     * Get the "instructoins" string to pass to GEF for the given handle number.
     * 
     * @param index
     *            handle number that is being dragged from
     * @return string or null
     */
    abstract protected String getInstructions(int index);
    
    /**
     * Get the node type to create when dragging from the given handle number.
     * 
     * @param index
     *            handle number that is being dragged from
     * @return metatype for model element. Null to disallow drag.
     */
    abstract protected Object getNewNodeType(int index);

    /**
     * Get the edge type to create when dragging from the given handle number.
     * 
     * @param index
     *            handle number that is being dragged from
     * @return metatype for model element. Null to disallow drag.
     */
    abstract protected Object getNewEdgeType(int index);
    
    /**
     * Get the node type to create when dragging from the given handle number.
     * 
     * @param index
     *            handle number that is being dragged from
     * @return true to reverse direction of assocation from direction of drag.
     *         eg. specialization instead of generalization
     */
    abstract protected boolean isDragEdgeReverse(int index);
    
    /**
     * Request post processing of edge by GEF after it is created using
     * {@link ModeCreateEdgeAndNode#ModeCreateEdgeAndNode(Editor, Object, Object, boolean)
     * 
     * @return true if postprocessing requested
     */
    abstract protected boolean isEdgePostProcessRequested();
    
} 

