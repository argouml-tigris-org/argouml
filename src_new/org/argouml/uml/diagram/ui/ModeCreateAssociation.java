/**
 *  A Mode to interpret user input while creating an association,
 *  transforming a binary association into a N-ary association and
 *  a N-ary association in a binary one as well.  Basically mouse down
 *  starts creating an edge from a source port Fig or from a binary association.
 *  If it is a binary association, it creates the proper figNode (a rhomb)
 *  to transform the binary association into a N-ary association. Mouse
 *  motion paints a rubberband line, mouse up finds the destination port
 *  and finishes creating the edge if it is a binary association or several
 *  edges if several association ends must be created.
 *  So if the connection is successful, it deletes the previous simple edge
 *  and connect association ends' edges to the recently created node.
 *  If it fails, everything must be undone.
 *
 *  The argument "edgeClass" determines the type if edge to suggest
 *  that the Editor's GraphModel construct, normally an association or
 *  an association end.  The GraphModel is responsible for acutally
 *  making edges in the underlying model and connecting it to other
 *  model elements.
 *
 * @author pepargouml@yahoo.es
 */

package org.argouml.uml.diagram.ui;

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

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.Iterator;


public class ModeCreateAssociation extends ModeCreatePolyEdge{
    private FigNode newFigNodeAssociation;
    private FigEdge oldFigAssociation;
    private Object association;
    private Collection associationEnds;


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
            oldFigAssociation = (FigEdge)underMouse;
            association = oldFigAssociation.getOwner();
            associationEnds = ModelFacade.getConnections(association);
            oldFigAssociation.setOwner(null);
            newFigNodeAssociation = placeTempNode(me);
            underMouse = newFigNodeAssociation;
            setSourceFigNode(newFigNodeAssociation);
            setStartPort(newFigNodeAssociation.getOwner());
            setStartPortFig(newFigNodeAssociation);
            setArg("edgeClass", ModelFacade.ASSOCIATION_END);
        } else {
            if (!(underMouse instanceof FigNode) && _npoints == 0) {
                done();
                me.consume();
                return;
            }
            if (underMouse instanceof FigNodeAssociation) {
                setArg("edgeClass", ModelFacade.ASSOCIATION_END);
            }
            else {
                setArg("edgeClass", ModelFacade.ASSOCIATION);
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
        // needs-more-work: potential class cast exception

        if (f instanceof FigAssociation)  {
            Object association = f.getOwner();
            Class edgeClass = (Class) getArg("edgeClass");
            if (edgeClass != null && edgeClass.equals(ModelFacade.ASSOCIATION)) {
                boolean isValid =
                        Model.getUmlFactory()
                        .isConnectionValid((Class)ModelFacade.ASSOCIATION_END, getStartPort(), association);
                if (isValid) {
                    GraphModel gm = (GraphModel)editor.getGraphModel();
                    GraphNodeRenderer renderer = editor.getGraphNodeRenderer();
                    Layer lay = editor.getLayerManager().getActiveLayer();
                    mutableGraphModel.removeEdge(association);
                    f.setOwner(null);
                    f.removeFromDiagram();
                    FigNode figNode = renderer.getFigNodeFor(gm, lay, association);
                    figNode.setX(x - figNode.getWidth()/2);
                    figNode.setY(y - figNode.getHeight()/2);
                    mutableGraphModel.addNode(association);
                    editor.add(figNode);
                    associationEnds = ModelFacade.getConnections(association);
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

                Class edgeClass = (Class) getArg("edgeClass");
                if (edgeClass.equals(ModelFacade.ASSOCIATION)
                        && ModelFacade.isAAssociation(foundPort))
                {
                    edgeClass = (Class) ModelFacade.ASSOCIATION_END;
                }
                if (edgeClass != null)
                    setNewEdge(mutableGraphModel.connect(getStartPort(), foundPort, edgeClass));
                else
                    setNewEdge(mutableGraphModel.connect(getStartPort(), foundPort));

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

                    // if the new edge implements the MouseListener interface it has to receive the mouseReleased() event
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

        /*If a FigNodeAssociation has been created and placed but the connection fails,
        it must be undone*/
        if (getNewEdge() == null
                && newFigNodeAssociation != null
                && newFigNodeAssociation instanceof FigNodeAssociation)
        {
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

            GraphModel gm = (GraphModel)editor.getGraphModel();
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
        GraphModel gm = (GraphModel)editor.getGraphModel();

        GraphNodeRenderer renderer = editor.getGraphNodeRenderer();
        Layer lay = editor.getLayerManager().getActiveLayer();
        figNode = renderer.getFigNodeFor(gm, lay, association);
        figNode.setX(me.getX() - figNode.getWidth()/2);
        figNode.setY(me.getY() - figNode.getHeight()/2);
        figNode.setVisible(false);
        editor.add(figNode);
        editor.getSelectionManager().deselectAll();

        return figNode;
    }

} /* end class ModeCreateAssociation */
