// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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
import java.awt.event.MouseEvent;

import javax.swing.Icon;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.diagram.deployment.DeploymentDiagramGraphModel;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ModeCreateEdgeAndNode;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.base.ModeModify;
import org.tigris.gef.base.SelectionManager;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.Handle;

/**
 * The buttons on selection for a DataType.
 * 
 * @author Michiel
 */
class SelectionDataType extends SelectionNodeClarifiers {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(SelectionDataType.class);

    private static Icon inherit =
        ResourceLoaderWrapper.lookupIconResource("Generalization");

    private boolean useComposite;

    /**
     * Construct a new SelectionClass for the given Fig.
     *
     * @param f The given Fig.
     */
    public SelectionDataType(Fig f) { 
        super(f); 
    }

    /**
     * Return a handle ID for the handle under the mouse, or -1 if
     * none. TODO: in the future, return a Handle instance or
     * null. <p>
     *  <pre>
     *   0-------1-------2
     *   |               |
     *   3               4
     *   |               |
     *   5-------6-------7
     * </pre>
     *
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
        int iw = inherit.getIconWidth();
        int ih = inherit.getIconHeight();

        if (hitAbove(cx + cw / 2, cy, iw, ih, r)) {
            h.index = 10;
            h.instructions = "Add a superdatatype";
        } else if (hitBelow(cx + cw / 2, cy + ch, iw, ih, r)) {
            h.index = 11;
            h.instructions = "Add a subdatatype";
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

        // The next two lines are necessary to get the GraphModel,
        // in the DeploymentDiagram there are no Generalizations
        Editor ce = Globals.curEditor();
        GraphModel gm = ce.getGraphModel();

        if (!(gm instanceof DeploymentDiagramGraphModel)) {
            paintButtonAbove(inherit, g, cx + cw / 2, cy, 10);
            paintButtonBelow(inherit, g, cx + cw / 2, cy + ch + 2, 11);
        }
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
        Object nodeType = getNewNodeType(hand.index);
        int bx = mX, by = mY;
        boolean reverse = false;
        switch (hand.index) {
        case 10: //add superdatatype
            edgeType = Model.getMetaTypes().getGeneralization();
            by = cy;
            bx = cx + cw / 2;
            break;
        case 11: //add subdatatype
            edgeType = Model.getMetaTypes().getGeneralization();
            reverse = true;
            by = cy + ch;
            bx = cx + cw / 2;
            break;
        default:
            LOG.warn("invalid handle number");
            break;
        }
        if (edgeType != null && nodeType != null) {
            Editor ce = Globals.curEditor();
            ModeCreateEdgeAndNode m =
                new ModeCreateEdgeAndNode(ce,
                        edgeType, useComposite, this);
            m.setup((FigNode) getContent(), getContent().getOwner(),
                    bx, by, reverse);
            ce.pushMode(m);
        }

    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent me) {
        super.mouseEntered(me);
        useComposite = me.isShiftDown();
    }

    /**
     * @see org.tigris.gef.base.SelectionButtons#getNewNode(int)
     */
    protected Object getNewNode(int buttonCode) {
        Object ns = Model.getFacade().getNamespace(getContent().getOwner());
        return Model.getCoreFactory().buildDataType("", ns);
    }

    protected Object getNewNodeType(int buttonCode) {
        return Model.getMetaTypes().getDataType();
    }

    /**
     * @see org.tigris.gef.base.SelectionButtons#createEdgeAbove(
     *         org.tigris.gef.graph.MutableGraphModel, java.lang.Object)
     */
    protected Object createEdgeAbove(MutableGraphModel mgm, Object newNode) {
        return mgm.connect(getContent().getOwner(), newNode,
                           (Class) Model.getMetaTypes().getGeneralization());
    }

    /**
     * @see org.tigris.gef.base.SelectionButtons#createEdgeUnder(
     *         org.tigris.gef.graph.MutableGraphModel, java.lang.Object)
     */
    protected Object createEdgeUnder(MutableGraphModel mgm, Object newNode) {
        return mgm.connect(newNode, getContent().getOwner(),
                           (Class) Model.getMetaTypes().getGeneralization());
    }

}
