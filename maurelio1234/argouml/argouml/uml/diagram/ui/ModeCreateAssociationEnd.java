// $Id: ModeCreateAssociationEnd.java 12738 2007-06-01 17:14:39Z mvw $
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.argouml.model.IllegalModelElementConnectionException;
import org.argouml.model.Model;
import org.argouml.uml.diagram.static_structure.ui.FigClassifierBox;
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
 * TODO: Investigate if this can extend ModeCreateGraphEdge
 *
 * @author pepargouml@yahoo.es
 */
public class ModeCreateAssociationEnd extends ModeCreatePolyEdge {

    /**
     * The UID.
     */
    private static final long serialVersionUID = -7249069222789301797L;
    
    private static final Logger LOG =
	Logger.getLogger(ModeCreateAssociationEnd.class);
    
    private FigNode newFigNodeAssociation;
    private Object association;
    private Collection associationEnds;

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

        Object modelElement = underMouse.getOwner();

        if (!Model.getFacade().isAAssociationClass(modelElement)) {
            if (Model.getFacade().isAAssociation(underMouse.getOwner())) {
                association = underMouse.getOwner();
            }
            if (underMouse instanceof FigAssociation) {
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
    
    /*
     * @see org.tigris.gef.base.ModeCreatePolyEdge#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(final MouseEvent me) {
        if (me.isConsumed()) {
            return;
        }
        if (getSourceFigNode() == null) {
            done();
            me.consume();
            return;
        }
        final int x = me.getX();
        final int y = me.getY();
        Fig destFig = editor.hit(x, y);
        if (destFig == null) {
            destFig = editor.hit(x - 16, y - 16, 32, 32);
        }
        
        if (destFig != null) {
            Object source = getSourceFigNode().getOwner();
            Object dest = destFig.getOwner();
            
            if (Model.getFacade().isAAssociationClass(source)
            	|| Model.getFacade().isAAssociationClass(dest)) {
                // TODO: http://argouml.tigris.org/issues/show_bug.cgi?id=2991
            } else if ((Model.getFacade().isAAssociation(source)
                || Model.getFacade().isAClassifier(dest))
                && !Model.getFacade().isAClassifier(source)) {
                mouseReleasedOnClassifier(me, destFig);
                return;
            } else if ((Model.getFacade().isAClassifier(dest)
                || Model.getFacade().isAAssociation(dest))
                && !Model.getFacade().isAClassifier(source)) {
                mouseReleasedOnAssociation(me, destFig);
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

    /*
     * @see org.tigris.gef.base.ModeCreatePolyEdge#mouseReleased(java.awt.event.MouseEvent)
     */
    private void mouseReleasedOnClassifier(
	    final MouseEvent me,
	    final Fig destFig) {
        MutableGraphModel graphModel =
            (MutableGraphModel) editor.getGraphModel();

        // Order here is very important!
        // 1. Remove the old association FigEdge first
        graphModel.removeEdge(association);
        
        try {
	    Model.getUmlFactory().buildConnection(
	    	Model.getMetaTypes().getAssociationEnd(),
	    	getSourceFigNode().getOwner(), 
	    	null, 
	    	destFig.getOwner(), 
	    	null, 
	    	null, 
	    	null);
	} catch (IllegalModelElementConnectionException e) {
	    LOG.error("Exception", e);
	}
	
        // 3. Make sure the association is a node in the graph model
        graphModel.addNode(association);
        
        associationEnds =
            Model.getFacade().getConnections(association);
        
        endAttached(null);
        me.consume();
    }


    /*
     * @see org.tigris.gef.base.ModeCreatePolyEdge#mouseReleased(java.awt.event.MouseEvent)
     */
    private void mouseReleasedOnAssociation(
	    final MouseEvent me,
	    final Fig destFig) {
        int x = me.getX();
        int y = me.getY();
        
        MutableGraphModel graphModel =
            (MutableGraphModel) editor.getGraphModel();

        Object destAssociation = destFig.getOwner();
        
        // Order here is very important!
        // 1. Remove the old association FigEdge first
        graphModel.removeEdge(destAssociation);
        destFig.removeFromDiagram();
        
        // 2. Add a new association end to the association
        graphModel.connect(
                getStartPort(),
                destAssociation,
                Model.getMetaTypes().getAssociationEnd());
        
        // 3. Create a new FigNode representing the n-ary assoc
        graphModel.addNode(destAssociation);
        
        // 4. Create a new FigNode representing the n-ary assoc
        Layer lay = editor.getLayerManager().getActiveLayer();
        FigNode figNode = (FigNode) lay.presentationFor(destAssociation);
        figNode.setLocation(
                x - figNode.getWidth() / 2,
                y - figNode.getHeight() / 2);
        editor.add(figNode);
        
        associationEnds =
            Model.getFacade().getConnections(destAssociation);
        
        endAttached(null);
        me.consume();
    }

    /**
     * This will be called when the edge is successfully connected.
     * This method is extended to make sure that all edges are in the
     * graph model
     * @param fe the FigEdge drawn
     */
    protected void endAttached(FigEdge fe) {
        MutableGraphModel graphModel =
            (MutableGraphModel) editor.getGraphModel();
        for (Iterator it = associationEnds.iterator(); it.hasNext(); ) {
            graphModel.addEdge(it.next());
        }
        super.endAttached(fe);
        done();
    }

    private FigNode placeTempNode(MouseEvent me) {
        FigNode figNode = null;
        GraphModel gm = editor.getGraphModel();

        GraphNodeRenderer renderer = editor.getGraphNodeRenderer();
        Layer lay = editor.getLayerManager().getActiveLayer();
        figNode = renderer.getFigNodeFor(gm, lay, association, null);
        figNode.setLocation(
                me.getX() - figNode.getWidth() / 2,
                me.getY() - figNode.getHeight() / 2);
        //figNode.setVisible(false);
        editor.add(figNode);
        editor.getSelectionManager().deselectAll();

        return figNode;
    }
    
    public void keyTyped(KeyEvent ke) {
        if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) {
            LOG.debug("Esc pressed");
            abort();
            done();
            ke.consume();
        }
    }

    /**
     * This method must be called if the edge drawing is aborted for
     * any reason.
     * It removes any FigNodeAssociation that may have been created
     * when drawing started from a FigAssociation edge.
     */
    private void abort() {
	LOG.info("Drawing association end aborted");
        if (newFigNodeAssociation != null) {
            editor.remove(newFigNodeAssociation);
            newFigNodeAssociation.removeFromDiagram();
        }
    }

    /*
     * @see org.tigris.gef.base.ModeImpl#leave()
     */
    public void leave() {
        abort();
        super.leave();
    }
} /* end class ModeCreateAssociation */
