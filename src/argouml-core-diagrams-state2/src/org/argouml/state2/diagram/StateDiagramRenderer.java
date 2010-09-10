/* $Id:$
 *****************************************************************************
 * Copyright (c) 2010 Contributors - see below
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

import java.util.Map;

import org.apache.log4j.Logger;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.UmlDiagramRenderer;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

class StateDiagramRenderer extends UmlDiagramRenderer {
    
    private static final Logger LOG =
        Logger.getLogger(StateDiagramRenderer.class);

    public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node,
                                 Map styleAttributes) {
        FigNode result = null;
        // Although not generally true for GEF, for Argo we know that the layer
        // is a LayerPerspective which knows the associated diagram
        Diagram diag = ((LayerPerspective) lay).getDiagram(); 
        if (diag instanceof UMLDiagram
                && ((UMLDiagram) diag).doesAccept(node)) {
            result = (FigNode) ((UMLDiagram) diag).drop(node, null);
        } else {
            LOG.warn("StateDiagramRenderer getFigNodeFor unexpected node " 
                    + node);
            return null;
        }
        LOG.debug("StateDiagramRenderer getFigNodeFor " + result);
        lay.add(result);
        return result;       
    }

    public FigEdge getFigEdgeFor(GraphModel gm, Layer lay, Object edge,
            Map styleAttributes) {
        assert edge != null;
        assert lay instanceof LayerPerspective;

        ArgoDiagram diag = (ArgoDiagram) ((LayerPerspective) lay).getDiagram();
        DiagramSettings settings = diag.getDiagramSettings();
        FigEdge newEdge = null;

        addEdge(lay, newEdge, edge);
        return newEdge;
    }
}
