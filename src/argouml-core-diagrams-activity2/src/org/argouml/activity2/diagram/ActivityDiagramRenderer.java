/* $Id$
 *****************************************************************************
 * Copyright (c) 2010-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 */

package org.argouml.activity2.diagram;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.UmlDiagramRenderer;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

class ActivityDiagramRenderer extends UmlDiagramRenderer {

    private static final Logger LOG =
        Logger.getLogger(ActivityDiagramRenderer.class.getName());

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
            LOG.log(Level.WARNING,
                    "ActivityDiagramRenderer getFigNodeFor unexpected node "
                    + node);
            return null;
        }
        LOG.log(Level.FINE, "ActivityDiagramRenderer getFigNodeFor {0}", result);
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

        if (Model.getFacade().isAActivityEdge(edge)) {
            newEdge = new FigActivityEdge(edge, settings);
        } else if (edge instanceof CommentEdge) {
            newEdge = new FigEdgeNote(edge, settings);
        }
        addEdge(lay, newEdge, edge);
        return newEdge;
    }
}
