// $Id: FigStereotype.java 11516 2006-11-25 04:30:15Z tfmorris $
// Copyright (c) 2006 The Regents of the University of California. All
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
import java.util.Iterator;

import org.argouml.model.IllegalModelElementConnectionException;
import org.argouml.model.Model;
import org.argouml.uml.diagram.static_structure.ui.FigClassifierBox;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * A Mode to interpret user input while drawing a binary association.
 * The association can connect two existing classifiers.
 * 
 * @author Bob Tarling
 */
public class ModeCreateAssociation extends ModeCreateGraphEdge {

    public Object getMetaType() {
        return Model.getMetaTypes().getAssociation();
    }
    
    /**
     * Create an edge of the given type and connect it to the
     * given nodes.
     *
     * @param graphModel     the graph model in which to create the connection
     *                       element
     * @param edgeType       the UML object type of the connection
     * @param sourceFigNode      the FigNode for the source element
     * @param destFigNode        the FigNode for the destination element
     * @return The FigEdge representing the newly created model element
     */
    @Override
    protected FigEdge buildConnection(
            MutableGraphModel graphModel,
            Object edgeType,
            Fig sourceFigNode,
            Fig destFigNode) {
        Object association = graphModel.connect(
                sourceFigNode.getOwner(), 
                destFigNode.getOwner(), 
                edgeType);
        
        setNewEdge(association);

        // Calling connect() will add the edge to the GraphModel and
        // any LayerPersectives on that GraphModel will get a
        // edgeAdded event and will add an appropriate FigEdge
        // (determined by the GraphEdgeRenderer).

        if (getNewEdge() != null) {
            
            getSourceFigNode().damage();
            destFigNode.damage();
            Layer lay = editor.getLayerManager().getActiveLayer();
            FigAssociation fe = (FigAssociation) lay.presentationFor(getNewEdge());
            _newItem.setLineColor(Color.black);
            fe.setFig(_newItem);
            
//            boolean flip = false;
//            
//            final Iterator iter = 
//                Model.getFacade().getConnections(association).iterator();
//            
//            Object sourceAssociationEnd = iter.next();
//            Object destAssociationEnd = iter.next();
//            
//            if (getArg("unidirectional").equals(Boolean.TRUE)) {
//                if (!Model.getFacade().isNavigable(destAssociationEnd)) {
//                    flip = true;
//                }
//            } else if (!getArg("aggregation").equals(Model.getAggregationKind().getNone())) {
//                if (Model.getFacade().getAggregation(destAssociationEnd).equals(
//                        getArg("aggregation"))) {
//                    flip = true;
//                }
//            }
//            if (flip) {
//                fe.setDestPortFig(getStartPortFig());
//                fe.setDestFigNode(getSourceFigNode());
//                fe.setSourcePortFig(destFigNode);
//                fe.setSourceFigNode((FigNode) destFigNode);
//            } else {
//                fe.setSourcePortFig(getStartPortFig());
//                fe.setSourceFigNode(getSourceFigNode());
//                fe.setDestPortFig(destFigNode);
//                fe.setDestFigNode((FigNode) destFigNode);
//            }
            return fe;

        } else {
            return null;
        }
    }
    
}
