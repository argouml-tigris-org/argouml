/* $Id$
 *****************************************************************************
 * Copyright (c) 2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 */

package org.argouml.deployment2.diagram;

import java.awt.Point;
import java.awt.Rectangle;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.di.GraphNode;
import org.tigris.gef.presentation.Connector;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;

class FigPort extends FigNodeModelElement implements Connector {

    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;

    FigNode node;
    
    public FigPort(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initialize();
    }
    
    public GraphNode getGraphNode() {
        return node;
    }

    public void setGraphNode(GraphNode node) {
        this.node = (FigNode) node;
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
        FigNode node = ((FigNode) encloser);
        
        if (node != null) {
            node.addConnector(this);
        } else {
            FigNode previousNode = (FigNode) getGraphNode();
            if (previousNode != null)
                previousNode.removeConnector(this);
        }
    }
    
    @Override
    protected Fig createBigPortFig() {
        FigRect fr = new FigRect(0, 0, WIDTH, HEIGHT);
        fr.setLineWidth(1);
        return fr;
    }

    private void initialize() {
        setEditable(false);
        addFig(getBigPort());
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    @Override
    public Point connectionPoint(Point anotherPt) {
        return getBigPort().connectionPoint(anotherPt);
    }
    
    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
        getBigPort().setBounds(x, y, w, h);
        calcBounds();
        updateEdges();
    }
}
