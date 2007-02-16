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

package org.argouml.uml.diagram.ui;

import org.argouml.uml.diagram.UMLMutableGraphSupport;

import java.awt.Rectangle;

import java.awt.event.MouseEvent;

import java.util.Enumeration;

import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerManager;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.base.FigModifyingMode;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;

/**
 * A general class for rerouting edges, achieved by delegating
 * the re-routing logic to the graphmodels; extends
 * functionality in SelectionEdgeClarifiers.
 *
 * <p>If a graphmodel does not override canChangeConnectedNode()
 * then rerouting is not possible and ArgoUML should behave as if
 * rerouting had never been implemented.
 *
 * @author  alexb
 * @since 0.13.2
 */
public class SelectionRerouteEdge extends SelectionEdgeClarifiers {

    /**
     * Used to determine if the association is now to self,
     * in which case The association needs automatic layout.
     */
    private FigNodeModelElement sourceFig;

    /**
     * Used to determine if the association is now to self,
     * in which case The association needs automatic layout.
     */
    private FigNodeModelElement destFig;

    /**
     * The re-routing capability it armed if the mouse was previously
     * dragged.
     * <p>prevents just selecting the message then clicking somewhere
     * else on the diagram,
     */
    private boolean armed;

    /**
     * The index of the point on the line of the message.
     * <p>0 = sender end
     * <p>1..* = receiver end
     */
    private int pointIndex;

    /** 
     * Creates a new instance of SelectionRerouteEdge
     *
     * @param feme the given Fig
     */
    public SelectionRerouteEdge(FigEdgeModelElement feme) {

        super(feme);

        // set it to an invalid number by default
        // to make sure it is set correctly.
        pointIndex = -1;
    }

    /**
     * Set up for re-routing.
     *
     * {@inheritDoc}
     */
    public void mousePressed(MouseEvent me) {

        // calculate the source and dest figs for to self assoc
        sourceFig =
	    (FigNodeModelElement) ((FigEdge) getContent()).getSourceFigNode();
        destFig = 
            (FigNodeModelElement) ((FigEdge) getContent()).getDestFigNode();

        Rectangle mousePosition =
	    new Rectangle(me.getX() - 5, me.getY() - 5, 10, 10);
        //reset the pointIndex
        pointIndex = -1;
        int npoints = getContent().getNumPoints();
        int[] xs = getContent().getXs();
        int[] ys = getContent().getYs();
        for (int i = 0; i < npoints; ++i) {
            if (mousePosition.contains(xs[i], ys[i])) {
                pointIndex = i;
                super.mousePressed(me);
                return;
            }
        }

        super.mousePressed(me);
    }

    /**
     * Need to 'arm' the rerouting capability with mouseDragged().
     * <p>
     * Don't arm if the edtior's current mode is a figedge create mode,
     * because once a new edge has been created it is not deselected,
     * therefore on the next create an unwanted reroute is performed.
     *
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent me) {

        Editor editor = Globals.curEditor();
        ModeManager modeMgr = editor.getModeManager();
        FigModifyingMode fMode = modeMgr.top();

        if (!(fMode instanceof ModeCreatePolyEdge)) {
            armed = true;
        }
        super.mouseDragged(me);
    }

    /**
     * Perform re-routing if src/dest nodes have changed.
     *
     * <p>This method needs to be 'armed' by a previous mouseDragged()
     * to avoid the situation where the user just clicks on the message
     * then clicks on some unrelated Fig, without moving the association...
     *
     * <p>TODO: improve the fig finding algorithm to find the top most fig
     * in the layer. will be useful for nested states in a statechart.
     *
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent me) {
        // check pre-conds
        if (me.isConsumed() || !armed || pointIndex == -1) {
            armed = false;
            super.mouseReleased(me);
            return;
        }

        //Set-up:
        int x = me.getX(), y = me.getY();
        // the fig that was under the mouse when it was released
        FigNodeModelElement newFig = null;
        //make a nice little target area:
        Rectangle mousePoint = new Rectangle(x - 5, y - 5, 5, 5);
        // and find the Fig:
        Editor editor = Globals.curEditor();
        LayerManager lm = editor.getLayerManager();
        Layer active = lm.getActiveLayer();
        Enumeration figs = active.elementsIn(mousePoint);
        // last is the top fig.
        while (figs.hasMoreElements()) {
            Fig candidateFig = (Fig) figs.nextElement();
            if (candidateFig instanceof FigNodeModelElement
                    && candidateFig.isSelectable()) {
                newFig = (FigNodeModelElement) candidateFig;
            }
        }
        // check intermediate post-condition.
        if (newFig == null) {
            armed = false;
            super.mouseReleased(me);
            return;
        }

        UMLMutableGraphSupport mgm =
            (UMLMutableGraphSupport) editor.getGraphModel();
        FigNodeModelElement oldFig = null;
        boolean isSource = false;
        if (pointIndex == 0) {
            oldFig = sourceFig;
            isSource = true;
        }
        else {
            oldFig = destFig;
        }

        // delegate the re-routing to graphmodels.
        if (mgm.canChangeConnectedNode(newFig.getOwner(),
				       oldFig.getOwner(),
				       this.getContent().getOwner())) {
	    mgm.changeConnectedNode(newFig.getOwner(),
				    oldFig.getOwner(),
				    this.getContent().getOwner(),
				    isSource);
	}

        editor.getSelectionManager().deselect(getContent());
        armed = false;
        FigEdgeModelElement figEdge = (FigEdgeModelElement) getContent();
        figEdge.determineFigNodes();
        figEdge.computeRoute();
        super.mouseReleased(me);
        return;
    }


}
