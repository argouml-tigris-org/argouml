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
import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.Model;
import org.argouml.uml.diagram.static_structure.ui.FigClassifierBox;
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
 * A Mode to interpret user input while creating an association end.
 * The association end can connect an existing association to an existing
 * classifier.
 * If the association is an n-ary association (diamond shape node) then
 * the edge is simply added.
 * If the association is a binary association edge then that edge is
 * transformed into a n-ary association.
 *
 * @author pepargouml@yahoo.es
 */
public class ModeCreateAssociationEnd extends ModeCreatePolyEdge {
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

        if (_npoints > 0) {
            me.consume();
            return;
        }

        Object modelElement = underMouse.getOwner();

        if (!Model.getFacade().isAAssociationClass(modelElement)) {
            if (underMouse instanceof FigAssociation) {
                oldFigAssociation = (FigEdge) underMouse;
                association = oldFigAssociation.getOwner();
                associationEnds =
                    Model.getFacade().getConnections(association);
                newFigNodeAssociation = placeTempNode(me);
                underMouse = newFigNodeAssociation;
                setSourceFigNode(newFigNodeAssociation);
                setStartPort(newFigNodeAssociation.getOwner());
                setStartPortFig(newFigNodeAssociation);
            } else if (underMouse instanceof FigNodeAssociation
                    || underMouse instanceof FigClassifierBox) {
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
        }

        createFig(me);
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
        Fig destFig = editor.hit(x, y);
        if (destFig == null) {
            destFig = editor.hit(x - 16, y - 16, 32, 32);
        }
        GraphModel graphModel = editor.getGraphModel();
        if (!(graphModel instanceof MutableGraphModel)) {
            destFig = null;
        }

        Object newAssociationEnd = null;

        MutableGraphModel mutableGraphModel = (MutableGraphModel) graphModel;
        // TODO: potential class cast exception
        if (destFig instanceof FigAssociation
                && !(destFig instanceof FigAssociationClass)) {
            Object association = destFig.getOwner();
            boolean isValid =
                Model.getUmlFactory().isConnectionValid(
                    Model.getMetaTypes().getAssociationEnd(),
                    getStartPort(),
                    association);
            if (isValid) {
                // Order here is very important!
                // 1. Remove the old association FigEdge first
                Layer lay = editor.getLayerManager().getActiveLayer();
                mutableGraphModel.removeEdge(association);
                destFig.removeFromDiagram();
                
                // 2. Add a new association end to the association
                Object edgeType = getArg("edgeClass");
                newAssociationEnd = mutableGraphModel.connect(
                        getStartPort(), association, edgeType);
                
                // 2. Create a new FigNode representing the n-ary assoc
                mutableGraphModel.addNode(association);
                FigNode figNode = (FigNode) lay.presentationFor(association);
                figNode.setLocation(
                        x - figNode.getWidth() / 2,
                        y - figNode.getHeight() / 2);
                editor.add(figNode);
                associationEnds =
                    Model.getFacade().getConnections(association);
                Iterator it = associationEnds.iterator();
                mutableGraphModel.addEdge(it.next());
                mutableGraphModel.addEdge(it.next());
                mutableGraphModel.addEdge(it.next());
                endAttached();
                done();
                me.consume();
                return;
            }
        }

        if (destFig instanceof FigNode) {
            if (!(destFig instanceof FigClassAssociationClass)
                    && !Model.getFacade().isANaryAssociation(
                            destFig.getOwner())) {
                FigNode destFigNode = (FigNode) destFig;
                // If its a FigNode, then check within the
                // FigNode to see if a port exists
                Object foundPort = destFigNode.deepHitPort(x, y);

                if (foundPort == getStartPort() && _npoints < 4) {
                    // user made a false start
                    abort();
                    done();
                    me.consume();
                    return;
                }
                if (foundPort != null) {
                    Fig destPortFig = destFigNode.getPortFig(foundPort);
                    FigPoly p = (FigPoly) _newItem;
                    editor.damageAll();
                    p.setComplete(true);

                    Object edgeType = getArg("edgeClass");
                    if (newAssociationEnd == null
                            && !mutableGraphModel.canConnect(
                           getStartPort(), foundPort, edgeType)) {
                        abort();
                    } else {
                        if (newAssociationEnd == null) {
                            newAssociationEnd = mutableGraphModel.connect(
                                    getStartPort(), foundPort, edgeType);
                        }
                        setNewEdge(newAssociationEnd);

                        // Calling connect() will add the edge to the
                        // GraphModel and any LayerPersectives on that
                        // GraphModel will get a edgeAdded event and
                        // will add an appropriate FigEdge
                        // (determined by the GraphEdgeRenderer).

                        if (getNewEdge() != null) {
                            getSourceFigNode().damage();
                            destFigNode.damage();
                            Layer lay =
                                editor.getLayerManager().getActiveLayer();
                            FigEdge fe =
                                (FigEdge) lay.presentationFor(getNewEdge());
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
                            // interface it has to receive the mouseReleased()
                            // event
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
                        } else {
                            // The user must have release on some FigNode
                            // that is not valid
                            abort();
                        }
                    }
                    done();
                    me.consume();
                    return;
                }
            } else {
                // The user must have release on some FigNode that is not valid
                abort();
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
                mutableGraphModel.addEdge(it.next());
                editor.getSelectionManager().deselectAll();
                mutableGraphModel.addNode(association);
            }
        }
        super.endAttached();
    }

    private FigNode placeTempNode(MouseEvent me) {
        Editor editor = Globals.curEditor();
        FigNode figNode = null;
        GraphModel gm = editor.getGraphModel();

        GraphNodeRenderer renderer = editor.getGraphNodeRenderer();
        Layer lay = editor.getLayerManager().getActiveLayer();
        figNode = renderer.getFigNodeFor(gm, lay, association, null);
        figNode.setLocation(
                me.getX() - figNode.getWidth() / 2,
                me.getY() - figNode.getHeight() / 2);
        figNode.setVisible(false);
        editor.add(figNode);
        editor.getSelectionManager().deselectAll();

        return figNode;
    }

    /**
     * This method must be called if the edge drawing is aborted for
     * any reason.
     * It removes any FigNodeAssociation that may have been created
     * when drawing started from a FigAssociation edge.
     */
    private void abort() {
        if (newFigNodeAssociation != null) {
            Editor editor = Globals.curEditor();
            editor.remove(newFigNodeAssociation);
            newFigNodeAssociation.removeFromDiagram();
            oldFigAssociation.setOwner(association);
        }
    }

    /**
     * @see org.tigris.gef.base.ModeImpl#leave()
     */
    public void leave() {
        abort();
        super.leave();
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -7249069222789301797L;
} /* end class ModeCreateAssociation */
