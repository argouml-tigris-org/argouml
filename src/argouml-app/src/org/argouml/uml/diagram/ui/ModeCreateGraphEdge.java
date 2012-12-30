/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
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

// Copyright (c) 2005-2008 The Regents of the University of California. All
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

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.Model;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;

/**
 * A Mode to interpret user input while creating an edge.
 * The edge can connect any two model element prooviding isConnectionValid
 * return true.
 */
public abstract class ModeCreateGraphEdge extends ModeCreatePolyEdge {

    private static final Logger LOG =
        Logger.getLogger(ModeCreateGraphEdge.class.getName());

    /**
     * The Fig from which drawing starts, either a FigNode or a FigEdge
     */
    private Fig sourceFig;

    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent me) {
        int x = me.getX(), y = me.getY();
        Fig underMouse = editor.hit(x, y);
        if (underMouse == null) {
            underMouse = editor.hit(x - 16, y - 16, 32, 32);
        }

        if (underMouse == null && _npoints == 0) {
            done();
            me.consume();
            return;
        }

        if (_npoints > 0) {
            me.consume();
            return;
        }

        sourceFig = underMouse;

        if (underMouse instanceof FigEdgeModelElement
                && !(underMouse instanceof FigEdgeNote)) {
            // If we're drawing from an edge

            FigEdgeModelElement sourceEdge = (FigEdgeModelElement) underMouse;
            sourceEdge.makeEdgePort();
            FigEdgePort edgePort = sourceEdge.getEdgePort();
            sourceEdge.computeRoute();

            underMouse = edgePort;
            setSourceFigNode(edgePort);
            setStartPort(sourceFig.getOwner());
            setStartPortFig(edgePort);

        } else if (underMouse instanceof FigNodeModelElement) {
            if (getSourceFigNode() == null) {
                setSourceFigNode((FigNode) underMouse);
                setStartPort(getSourceFigNode().deepHitPort(x, y));
            }
            if (getStartPort() == null) {
                done();
                me.consume();
                return;
            }
            setStartPortFig(
                    getSourceFigNode().getPortFig(getStartPort()));
        } else {
            done();
            me.consume();
            return;
        }

        createFig(me);
        me.consume();
    }

    /*
     * @see org.tigris.gef.base.ModeCreatePolyEdge#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent me) {
        if (me.isConsumed()) {
            return;
        }
        if (getSourceFigNode() == null) {
            done();
            me.consume();
            return;
        }
        int x = me.getX(), y = me.getY();
        Fig destFig = editor.hit(x, y);
        if (destFig == null) {
            destFig = editor.hit(x - 16, y - 16, 32, 32);
        }
        MutableGraphModel graphModel =
            (MutableGraphModel) editor.getGraphModel();

        if (!isConnectionValid(sourceFig, destFig)) {
            destFig = null;
        } else {
            LOG.log(Level.INFO, "Connection valid");
        }

        if (destFig instanceof FigEdgeModelElement
                && !(destFig instanceof FigEdgeNote)) {
            FigEdgeModelElement destEdge = (FigEdgeModelElement) destFig;
            destEdge.makeEdgePort();
            destFig = destEdge.getEdgePort();
            destEdge.computeRoute();
        }

        if (destFig instanceof FigNodeModelElement) {
            FigNode destFigNode = (FigNode) destFig;
            Object foundPort = destFigNode.getOwner();

            if (foundPort == getStartPort() && _npoints < 4) {
                // user made a false start
                done();
                me.consume();
                return;
            }
            if (foundPort != null) {
                FigPoly p = (FigPoly) _newItem;
                if (foundPort == getStartPort() && _npoints >= 4) {
                    p.setSelfLoop(true);
                }
                editor.damageAll();
                p.setComplete(true);

                LOG.log(Level.INFO, "Connecting");
                FigEdge fe = buildConnection(
                        graphModel,
                        getMetaType(),
                        sourceFig,
                        destFig);

                if (fe != null) {
                    editor.getSelectionManager().select(fe);
                }
                editor.damageAll();

                // if the new edge implements the MouseListener
                // interface it has to receive the mouseReleased() event
                if (fe instanceof MouseListener) {
                    ((MouseListener) fe).mouseReleased(me);
                }

                endAttached(fe);

                done();
                me.consume();
                return;
            }
        }
        if (!nearLast(x, y)) {
            editor.damageAll();
            Point snapPt = new Point(x, y);
            editor.snap(snapPt);
            ((FigPoly) _newItem).addPoint(snapPt.x, snapPt.y);
            _npoints++;
            editor.damageAll();
        }
        _lastX = x;
        _lastY = y;
        me.consume();
    }

    /**
     * Return the meta type of the element that this mode is designed to
     * create.
     * @return the meta type of the connection required.
     */
    protected abstract Object getMetaType();

    /**
     * Called after the edge has been drawn from a source and dropped to a
     * destination. Return true if this drop is valid.
     * The default is true, subclasses should override this with their own
     * logic.
     * @param source the source fig
     * @param dest the dest fig
     * @return true if drop on an edge is valid.
     */
    protected boolean isConnectionValid(Fig source, Fig dest) {
	return Model.getUmlFactory().isConnectionValid(
		getMetaType(),
		source == null ? null : source.getOwner(),
		dest == null ? null : dest.getOwner(),
                true);
    }

    /**
     * Create an edge of the given type and connect it to the
     * given nodes.
     *
     * @param graphModel the GraphModel containing the objects
     * @param edgeType       the UML object type of the connection
     * @param fromElement    the Fig for the "from" element
     * @param destFigNode    the Fig for the "to" element
     * @return a newly created FigEdge
     */
    protected FigEdge buildConnection(
            MutableGraphModel graphModel,
            Object edgeType,
            Fig fromElement,
            Fig destFigNode) {
        Object modelElement = graphModel.connect(
                fromElement.getOwner(),
                destFigNode.getOwner(),
                edgeType);

        setNewEdge(modelElement);

        // Calling connect() will add the edge to the GraphModel and
        // any LayerPersectives on that GraphModel will get a
        // edgeAdded event and will add an appropriate FigEdge
        // (determined by the GraphEdgeRenderer).

        if (getNewEdge() != null) {
            getSourceFigNode().damage();
            destFigNode.damage();
            Layer lay = editor.getLayerManager().getActiveLayer();
            FigEdge fe = (FigEdge) lay.presentationFor(getNewEdge());
            _newItem.setLineColor(Color.black);
            fe.setFig(_newItem);
            fe.setSourcePortFig(getStartPortFig());
            fe.setSourceFigNode(getSourceFigNode());
            fe.setDestPortFig(destFigNode);
            fe.setDestFigNode((FigNode) destFigNode);
            return fe;

        } else {
            return null;
        }

    }
}
