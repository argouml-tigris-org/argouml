/* $Id$
 *****************************************************************************
 * Copyright (c) 2011-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 */

package org.argouml.state2.diagram;

import java.awt.Rectangle;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.di.GraphNode;
import org.tigris.gef.presentation.Connector;
import org.tigris.gef.presentation.Fig;

class FigEntryPoint extends FigCircleState implements Connector {

    FigVertex node;
    
    public FigEntryPoint(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
    }

    public GraphNode getGraphNode() {
        return node;
    }

    public void setGraphNode(GraphNode node) {
        this.node = (FigVertex) node;
    }
    

    @Override
    public void setEnclosingFig(Fig encloser) {
        // TODO: Note copy/paste code in FigExitPoint - where is this code
        // better placed in GEF?
        LayerPerspective lp = (LayerPerspective) getLayer();
        if (lp == null) {
            return;
        }
        
        super.setEnclosingFig(encloser);
        FigVertex vertex = ((FigVertex) encloser);
        
        if (vertex != null) {
            vertex.addConnector(this);
        } else {
            FigVertex previousVertex = (FigVertex) getGraphNode();
            if (previousVertex != null)
                previousVertex.removeConnector(this);
        }
    }
}
