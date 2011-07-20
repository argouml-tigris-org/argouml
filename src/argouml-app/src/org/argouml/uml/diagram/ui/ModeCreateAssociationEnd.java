/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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
import java.util.List;

import org.argouml.model.IllegalModelElementConnectionException;
import org.argouml.model.Model;
import org.argouml.uml.diagram.static_structure.ui.FigClassifierBox;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
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
 *
 * @author Bob Tarling
 */
public class ModeCreateAssociationEnd extends ModeCreateGraphEdge {

    /**
     * The UID.
     */
    private static final long serialVersionUID = -7249069222789301797L;
    
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
     * @param sourceFig      the FigNode for the source element
     * @param destFig        the FigNode for the destination element
     * @return The FigEdge representing the newly created model element
     */
    @Override
    protected FigEdge buildConnection(
            MutableGraphModel graphModel,
            Object edgeType,
            Fig sourceFig,
            Fig destFig) {
        try {
            // The source of an association end should not
            // be the classifier. If it is the user has drawn the wrong way
            // round so we swap here.
            if (sourceFig instanceof FigClassifierBox) {
                final Fig tempFig = sourceFig;
                sourceFig = destFig;
                destFig = tempFig;
            }
            
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
                fe.setSourceFigNode(sourceFigNode);
                fe.setDestPortFig(destFigNode);
                fe.setDestFigNode(destFigNode);
                
                fe.computeRoute();

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
        final FigNode originalEdgePort = figAssociation.getEdgePort();
        
        FigClassAssociationClass associationClassBox = null;
        FigEdgeAssociationClass associationClassLink = null;
        
        final LayerPerspective lay = 
            (LayerPerspective) editor.getLayerManager().getActiveLayer();
        
        // Detach any edges (such as comment edges) already attached
        // to the FigAssociation before the FigAssociation is removed.
        // They'll later be re-attached to the new FigNodeAssociation
        final Collection<FigEdge> existingEdges =
            originalEdgePort.getFigEdges();
        for (FigEdge edge : existingEdges) {
            if (edge instanceof FigEdgeAssociationClass) {
                // If there are bits of an association class then
                // remember their location and path.
                associationClassLink = (FigEdgeAssociationClass) edge;
                FigNode figNode = edge.getSourceFigNode();
                if (figNode instanceof FigEdgePort) {
                    figNode = edge.getDestFigNode();
                }
                associationClassBox = (FigClassAssociationClass) figNode;
                originalEdgePort.removeFigEdge(edge);
                lay.remove(edge);
                lay.remove(associationClassBox);
            } else {
                originalEdgePort.removeFigEdge(edge);
            }
        }
        
        List associationFigs = lay.presentationsFor(association);
        
        figAssociation.removeFromDiagram();
        associationFigs = lay.presentationsFor(association);
        
        // Create the new FigNodeAssociation and locate it.
        final MutableGraphModel gm =
            (MutableGraphModel) editor.getGraphModel();
        gm.addNode(association);
        associationFigs = lay.presentationsFor(association);
        associationFigs.remove(figAssociation);
        associationFigs = lay.presentationsFor(association);
        
        final FigNodeAssociation figNode = 
            (FigNodeAssociation) associationFigs.get(0);
        
        figNode.setLocation(
                x - figNode.getWidth() / 2,
                y - figNode.getHeight() / 2);
        editor.add(figNode);
        editor.getSelectionManager().deselectAll();
        
        // Add the association ends to the graph model
        final Collection<Object> associationEnds =
            Model.getFacade().getConnections(association);
        
        for (Object associationEnd : associationEnds) {
            gm.addEdge(associationEnd);
        }
        
        // Add the edges (such as comment edges) that were on the old
        // FigAssociation to our new FigNodeAssociation and make sure they are
        // positioned correctly.
        for (FigEdge edge : existingEdges) {
            if (edge.getDestFigNode() == originalEdgePort) {
                edge.setDestFigNode(figNode);
                edge.setDestPortFig(figNode);
            }
            if (edge.getSourceFigNode() == originalEdgePort) {
                edge.setSourceFigNode(figNode);
                edge.setSourcePortFig(figNode);
            }
        }
        figNode.updateEdges();
        
        if (associationClassBox != null) {
            associationFigs = lay.presentationsFor(association);
            
            lay.add(associationClassBox);
            associationClassLink.setSourceFigNode(figNode);
            lay.add(associationClassLink);
            
            associationFigs = lay.presentationsFor(association);
        }

        return figNode;
    }
    
    /*
     * If we're drawing to an edge then only allow if the start is a comment
     * @see org.argouml.uml.diagram.ui.ModeCreateGraphEdge#isConnectionValid(org.tigris.gef.presentation.Fig, org.tigris.gef.presentation.Fig)
     */
    @Override
    protected final boolean isConnectionValid(Fig source, Fig dest) {
        return super.isConnectionValid(source, dest);
    }
    
    
} /* end class ModeCreateAssociation */
