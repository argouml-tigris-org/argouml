// $Id$
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

import java.awt.Color;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.argouml.model.IllegalModelElementConnectionException;
import org.argouml.model.Model;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

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
 * @author Bob Tarling
 */
public class ModeCreateAssociationEnd extends ModeCreateGraphEdge {

    /**
     * The UID.
     */
    private static final long serialVersionUID = -7249069222789301797L;
    
    private static final Logger LOG =
	Logger.getLogger(ModeCreateAssociationEnd.class);

    public Object getMetaType() {
        return Model.getMetaTypes().getAssociationEnd();
    }
    
    /**
     * Create an edge of the given type and connect it to the
     * given nodes.
     *
     * @param graphModel     the graph model in which to create the connection
     *                       element
     * @param edgeType       the UML object type of the connection
     * @param sourceFigNode  the FigNode for the source element
     * @param destFigNode    the FigNode for the destination element
     */
    @Override
    protected FigEdge buildConnection(
            MutableGraphModel graphModel,
            Object edgeType,
            Fig sourceFig,
            Fig destFig) {
        try {
            Object associationEnd = 
                Model.getUmlFactory().buildConnection(
                    edgeType,
                    sourceFig.getOwner(),
                    null,
                    destFig.getOwner(),
                    null,
                    null,
                    null);
            
            final FigNode sourceFigNode = convertToFigNode(sourceFig);
            final FigNode destFigNode = convertToFigNode(destFig);
            
            graphModel.addEdge(associationEnd);
            
            setNewEdge(associationEnd);

            // Calling connect() will add the edge to the GraphModel and
            // any LayerPersectives on that GraphModel will get a
            // edgeAdded event and will add an appropriate FigEdge
            // (determined by the GraphEdgeRenderer).

            if (getNewEdge() != null) {
                sourceFigNode.damage();
                destFigNode.damage();
                Layer lay = editor.getLayerManager().getActiveLayer();
                FigEdge fe = (FigEdge) lay.presentationFor(getNewEdge());
                _newItem.setLineColor(Color.black);
                fe.setFig(_newItem);
                fe.setSourcePortFig(sourceFigNode);
                fe.setSourceFigNode((FigNode) sourceFigNode);
                fe.setDestPortFig(destFigNode);
                fe.setDestFigNode((FigNode) destFigNode);
                return fe;
            } else {
                return null;
            }
        } catch (IllegalModelElementConnectionException e) {
            // We have already confirmed the connection is valid
            return null;
        }
    }
    
    /**
     * If the selected Fig is a FigAssociation (an edge) then
     * convert it to a FigNodeAssociation.
     * @param fig the select end Fig
     * @return the fig converted to a FigNode
     */
    private FigNode convertToFigNode(Fig fig) {
        if (fig instanceof FigEdgePort) {
            fig = fig.getGroup();
        }
        if (!(fig instanceof FigAssociation)) {
            return (FigNode) fig;
        }
        final FigAssociation figAssociation = (FigAssociation) fig;
        final int x = figAssociation.getEdgePort().getX();
        final int y = figAssociation.getEdgePort().getY();
        final Object association = fig.getOwner();
        figAssociation.removeFromDiagram();
        
        final MutableGraphModel gm =
            (MutableGraphModel) editor.getGraphModel();
        gm.addNode(association);
        final Layer lay = editor.getLayerManager().getActiveLayer();
        final FigNode figNode = (FigNode) lay.presentationFor(association);
        
        figNode.setLocation(
                x - figNode.getWidth() / 2,
                y - figNode.getHeight() / 2);
        //figNode.setVisible(false);
        editor.add(figNode);
        editor.getSelectionManager().deselectAll();
        Collection<Object> associationEnds =
            Model.getFacade().getConnections(association);
        for (Object associationEnd : associationEnds) {
            gm.addEdge(associationEnd);
        }
        figNode.updateEdges();

        return figNode;
    }
    
} /* end class ModeCreateAssociation */
