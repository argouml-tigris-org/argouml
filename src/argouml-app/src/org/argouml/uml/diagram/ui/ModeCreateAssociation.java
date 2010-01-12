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
import org.argouml.model.Model;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;

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
            final MutableGraphModel graphModel,
            final Object edgeType,
            final Fig sourceFigNode,
            final Fig destFigNode) {
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
            final Layer lay = editor.getLayerManager().getActiveLayer();
            final FigEdge fe = (FigEdge) lay.presentationFor(getNewEdge());
            _newItem.setLineColor(Color.black);
            fe.setFig(_newItem);
            fe.computeRoute();
            return fe;

        } else {
            return null;
        }
    }
}
