// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

// Original author: jaap.branderhorst@xs4all.nl

package org.argouml.uml.diagram.sequence.ui;

import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.sequence.LinkPort;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.FigPoly;

/**
 * The fig for a link in a sequence diagram.
 * @author : jaap.branderhorst@xs4all.nl
 */
public abstract class FigLink
    extends FigEdgeModelElement {

    private int computeRouteNumbersOfTime = 0;

    /**
     * Contructs a new figlink and sets the owner of the figlink.
     * @param owner
     */
    public FigLink(Object owner) {
        super();
        setOwner(owner);
    }

    /**
     * Constructor here for saving and loading purposes.
     *
     */
    public FigLink() {
        this(null);
    }

    /**
     * Returns the action attached to this link if any.<p>
     *
     * @return the action attached to this link or null if there isn't any.
     */
    public Object getAction() {
        Object owner = getOwner();
        if (owner != null && ModelFacade.isALink(owner)) {
            Iterator it = ModelFacade.getStimuli(owner).iterator();
            Object stimulus = it.next();
            if (stimulus != null) {
                return ModelFacade.getDispatchAction(stimulus);
            }
        }
        return null;
    }

    /**
     * Computes the route of this {@link FigLink} and computes the
     * connectionpoints of the figlink to the ports.  This depends on
     * the action attached to the owner of the {@link FigLink}.  Also
     * adds FigActivations etc or moves the {@link FigObject}s
     * if necessary.<p>
     *
     * @see org.tigris.gef.presentation.FigEdge#computeRoute()
     */
    public void computeRoute() {
        if (computeRouteNumbersOfTime == 0) {
            computeRouteNumbersOfTime++;
        } else if (computeRouteNumbersOfTime == 1) {
            FigPoly p = ((FigPoly) _fig);
            Point srcPt = getSourcePortFig().center();
            Point dstPt = getSourcePortFig().center();
            Point centerPnt =
                new Point(
                    (int) (srcPt.getX() > dstPt.getX()
                        ? (dstPt.getX() + (srcPt.getX() - dstPt.getX()))
                        : (srcPt.getX() + (dstPt.getX() - srcPt.getX()))),
                    (int) srcPt.getY());
            srcPt = getSourcePortFig().connectionPoint(centerPnt);
            dstPt = getDestPortFig().connectionPoint(centerPnt);
            setEndPoints(srcPt, dstPt);
            layoutEdge();
            computeRouteNumbersOfTime++;
            calcBounds();
        } else if (computeRouteNumbersOfTime > 1) {
            FigPoly p = ((FigPoly) _fig);

            Point srcPt = getSourcePortFig().center();
            Point dstPt = getDestPortFig().center();

            if (_useNearest) {
                if (p.getNumPoints() == 2) {
                    //? two iterations of refinement, maybe should be a for-loop
                    srcPt = getSourcePortFig().connectionPoint(p.getPoints(1));
                    dstPt =
                        getDestPortFig().connectionPoint(
                            p.getPoints(p.getNumPoints() - 2));
                    srcPt = getSourcePortFig().connectionPoint(dstPt);
                    dstPt = getDestPortFig().connectionPoint(srcPt);
                } else {
                    srcPt = getSourcePortFig().connectionPoint(p.getPoints(1));
                    dstPt =
                        getDestPortFig().connectionPoint(
                            p.getPoints(p.getNumPoints() - 2));
                }
            }
            setEndPoints(srcPt, dstPt);
            calcBounds();
        }
    }

    /**
     * Returns the message belonging to this link if there is one
     * (otherwise null).<p>
     *
     * @return the message.
     */
    public Object getMessage() {
        Object action = getAction();
        if (action != null) {
            Collection col = ModelFacade.getMessages(action);
            if (!col.isEmpty()) {
                return col.iterator().next();
            }
        }
        return null;
    }

    /**
     * @see org.tigris.gef.presentation.FigEdgePoly#layoutEdge()
     */
    protected void layoutEdge() {
        layoutActivations();
        ((SequenceDiagramLayout) getLayer()).updateActivations();
        Globals.curEditor().damageAll();
    }

    /**
     * Lays out the activations to which the links is attached. Only
     * does this at creation time.
     */
    protected abstract void layoutActivations();

    public LinkPort getDestLinkPort() {
        return (LinkPort) getDestPortFig().getOwner();
    }

    public LinkPort getSrcLinkPort() {
        return (LinkPort) getSourcePortFig().getOwner();
    }

    public FigObject getSrcFigObject() {
        return (FigObject) getSourceFigNode();
    }

    public FigObject getDestFigObject() {
        return (FigObject) getDestFigNode();
    }

}
