// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

import org.argouml.model.Model;
import org.argouml.uml.diagram.static_structure.ui.CommentEdge;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;

/**
 * A Mode to interpret user input while creating a comment edge.
 * The comment can connect an existing comment node to any other existing
 * If the association is an n-ary association (diamond shape node) then
 * the edge is simply added.
 * If the association is a binary association edge then that edge is
 * transformed into a n-ary association.
 *
 * @author pepargouml@yahoo.es
 */
public class ModeCreateCommentEdge extends ModeCreatePolyEdge {

    private Object sourceModelElement;

    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
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

        sourceModelElement = underMouse.getOwner();

        if (underMouse instanceof FigEdgeModelElement
                && !(underMouse instanceof FigEdgeNote)) {
            // If we're drawing from an edge

            FigEdgeModelElement sourceEdge = (FigEdgeModelElement) underMouse;
            sourceEdge.makeEdgePort();
            FigEdgePort commentPort = sourceEdge.getEdgePort();
            sourceEdge.computeRoute();

            underMouse = commentPort;
            setSourceFigNode(commentPort);
            setStartPort(sourceModelElement);
            setStartPortFig(commentPort);
            
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

        if (destFig instanceof FigEdgeModelElement
                && Model.getFacade().isAComment(sourceModelElement)
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
                Fig destPortFig = destFigNode.getPortFig(foundPort);
                FigPoly p = (FigPoly) _newItem;
                if (foundPort == getStartPort() && _npoints >= 4) {
                    p.setSelfLoop(true);
                }
                editor.damageAll();
                p.setComplete(true);

                setNewEdge(graphModel.connect(
                       getStartPort(), foundPort, (Object) CommentEdge.class));

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
                    fe.setDestPortFig(destPortFig);
                    fe.setDestFigNode(destFigNode);

                    if (fe != null) {
                        editor.getSelectionManager().select(fe);
                    }
                    editor.damageAll();

                    // if the new edge implements the MouseListener
                    // interface it has to receive the mouseReleased() event
                    if (fe instanceof MouseListener) {
                        ((MouseListener) fe).mouseReleased(me);
                    }

                    // set the new edge in place
                    if (getSourceFigNode() != null) {
                        getSourceFigNode().updateEdges();
                    }
                    if (destFigNode != null) {
                        destFigNode.updateEdges();
                    }
                    endAttached(fe);
                }
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
} /* end class ModeCreateAssociation */
