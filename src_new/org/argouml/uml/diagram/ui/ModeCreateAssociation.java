// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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
import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;

/**
 * A Mode to interpret user input while creating an association,
 * transforming a binary association into a N-ary association and
 * a N-ary association in a binary one as well.  Basically mouse down
 * starts creating an edge from a source port Fig or from a binary association.
 * If it is a binary association, it creates the proper figNode (a rhomb)
 * to transform the binary association into a N-ary association. Mouse
 * motion paints a rubberband line, mouse up finds the destination port
 * and finishes creating the edge if it is a binary association or several
 * edges if several association ends must be created.
 * So if the connection is successful, it deletes the previous simple edge
 * and connect association ends' edges to the recently created node.
 * If it fails, everything must be undone.
 *
 * The argument "edgeClass" determines the type if edge to suggest
 * that the Editor's GraphModel construct, normally an association or
 * an association end.  The GraphModel is responsible for acutally
 * making edges in the underlying model and connecting it to other
 * model elements.
 *
 * @author pepargouml@yahoo.es
 */
public class ModeCreateAssociation extends ModeCreatePolyEdge {
    private FigNode newFigNodeAssociation;
    private FigEdge oldFigAssociation;
    private Object association;
    private Collection associationEnds;


    /**
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
        if (underMouse instanceof FigAssociation  && _npoints == 0) {
            oldFigAssociation = (FigEdge) underMouse;
            association = oldFigAssociation.getOwner();
            associationEnds = ModelFacade.getConnections(association);
            oldFigAssociation.setOwner(null);
            newFigNodeAssociation = placeTempNode(me);
            underMouse = newFigNodeAssociation;
            setSourceFigNode(newFigNodeAssociation);
            setStartPort(newFigNodeAssociation.getOwner());
            setStartPortFig(newFigNodeAssociation);
            setArg("edgeClass", Model.getMetaTypes().getAssociationEnd());
        } else {
            if (!(underMouse instanceof FigNode) && _npoints == 0) {
                done();
                me.consume();
                return;
            }
            if (underMouse instanceof FigNodeAssociation) {
                setArg("edgeClass", Model.getMetaTypes().getAssociationEnd());
            } else {
                setArg("edgeClass", Model.getMetaTypes().getAssociation());
            }
            if (getSourceFigNode() == null) { //_npoints == 0) {
                setSourceFigNode((FigNode) underMouse);
                setStartPort(getSourceFigNode().deepHitPort(x, y));
            }
            if (getStartPort() == null) {
                done();
                me.consume();
                return;
            }
            setStartPortFig(getSourceFigNode().getPortFig(getStartPort()));
        }

        if (_npoints == 0) {
            createFig(me);
        }
        me.consume();
    }

    /**
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
        Fig f = editor.hit(x, y);
        if (f == null) {
            f = editor.hit(x - 16, y - 16, 32, 32);
        }
        GraphModel graphModel = editor.getGraphModel();
        if (!(graphModel instanceof MutableGraphModel)) {
            f = null;
        }
        MutableGraphModel mutableGraphModel = (MutableGraphModel) graphModel;
        // TODO: potential class cast exception
        if (f instanceof FigAssociation)  {
            Object assoc = f.getOwner();
            Object edgeType = getArg("edgeClass");
            if (Model.getMetaTypes().getAssociation().equals(edgeType)) {
                boolean isValid =
                    Model.getUmlFactory().isConnectionValid(
                        Model.getMetaTypes().getAssociationEnd(),
                        getStartPort(),
                        assoc);
                if (isValid) {
                    GraphModel gm = editor.getGraphModel();
                    GraphNodeRenderer renderer = editor.getGraphNodeRenderer();
                    Layer lay = editor.getLayerManager().getActiveLayer();
                    mutableGraphModel.removeEdge(assoc);
                    f.setOwner(null);
                    f.removeFromDiagram();
                    mutableGraphModel.addNode(assoc);
                    FigNode figNode = (FigNode) lay.presentationFor(assoc);
                    figNode.setX(x - figNode.getWidth() / 2);
                    figNode.setY(y - figNode.getHeight() / 2);
                    editor.add(figNode);
                    associationEnds = ModelFacade.getConnections(assoc);
                    Iterator it = associationEnds.iterator();
                    mutableGraphModel.addEdge(it.next());
                    mutableGraphModel.addEdge(it.next());
                    f = figNode;
                }
            }
        }

        if (f instanceof FigNode) {
            FigNode destFigNode = (FigNode) f;
            // If its a FigNode, then check within the
            // FigNode to see if a port exists
            Object foundPort = destFigNode.deepHitPort(x, y);

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
                p._isComplete = true;

                Object edgeType = getArg("edgeClass");
                if (edgeType.equals(Model.getMetaTypes().getAssociation())
                        && ModelFacade.isAAssociation(foundPort)) {
                    edgeType = Model.getMetaTypes().getAssociationEnd();
                }
                if (edgeType != null) {
                    setNewEdge(mutableGraphModel.connect(
                                    getStartPort(), foundPort, (Class) edgeType));
                } else {
                    setNewEdge(mutableGraphModel.connect(
                                    getStartPort(), foundPort));
                }

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
                    endAttached();
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

        /* If a FigNodeAssociation has been created and placed but
         * the connection fails, it must be undone.
         */
        if (getNewEdge() == null
                && newFigNodeAssociation != null
                && newFigNodeAssociation instanceof FigNodeAssociation) {
            Editor editor = Globals.curEditor();
            editor.remove(newFigNodeAssociation);
            newFigNodeAssociation.removeFromDiagram();
            oldFigAssociation.setOwner(association);
        }
    }

    /**
     * This will be called when the edge is successfully connected.
     * What we do in this class is to determine if we are creating
     * an n-ary association. If so then FigNode representing the n-ary
     * association is made visible. The FigEdge representing the old
     * binary association is removed and replaced with edges representing
     * the 2 association ends of that original fig.
     */
    protected void endAttached() {
        if (newFigNodeAssociation != null) {
            newFigNodeAssociation.setVisible(true);
            oldFigAssociation.removeFromDiagram();

            Editor editor = Globals.curEditor();

            GraphModel gm = editor.getGraphModel();
            if (gm instanceof MutableGraphModel) {
                MutableGraphModel mutableGraphModel = (MutableGraphModel) gm;
                Iterator it = associationEnds.iterator();
                mutableGraphModel.addEdge(it.next());
                mutableGraphModel.addEdge(it.next());
                editor.getSelectionManager().deselectAll();
                mutableGraphModel.addNode(association);
            }
        }
    }

    private FigNode placeTempNode(MouseEvent me) {
        Editor editor = Globals.curEditor();
        FigNode figNode = null;
        GraphModel gm = editor.getGraphModel();

        GraphNodeRenderer renderer = editor.getGraphNodeRenderer();
        Layer lay = editor.getLayerManager().getActiveLayer();
        figNode = renderer.getFigNodeFor(gm, lay, association, null);
        figNode.setX(me.getX() - figNode.getWidth() / 2);
        figNode.setY(me.getY() - figNode.getHeight() / 2);
        figNode.setVisible(false);
        editor.add(figNode);
        editor.getSelectionManager().deselectAll();

        return figNode;
    }

} /* end class ModeCreateAssociation */
