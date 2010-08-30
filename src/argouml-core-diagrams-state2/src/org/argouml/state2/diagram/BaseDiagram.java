/* $Id: $
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

import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.Collections;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.UmlDiagramRenderer;
import org.argouml.uml.diagram.ui.ActionSetMode;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.FigNode;

abstract class BaseDiagram extends UMLDiagram {
    
    private static final Logger LOG = Logger
        .getLogger(BaseDiagram.class);
    
    public BaseDiagram(Object owner) {
        super();
        MutableGraphModel gm = createGraphModel();
        setGraphModel(gm);
        
        // Create the layer
        LayerPerspective lay = new
            LayerPerspectiveMutable(this.getName(), gm);
        setLayer(lay);
        
        // Create the renderer
        UmlDiagramRenderer renderer = createDiagramRenderer();
        lay.setGraphNodeRenderer(renderer);
        lay.setGraphEdgeRenderer(renderer);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Constructing diagram for " + owner);
        }
        try {
            this.setName(getNewDiagramName());
        } catch (PropertyVetoException e) {
            LOG.error("Exception", e);
        }
    }
    
    abstract UmlDiagramRenderer createDiagramRenderer();
    abstract UMLMutableGraphSupport createGraphModel();
    
    @Override
    protected Object[] getUmlActions() {
        
        final Object[] edgeTools = getNewEdgeTypes();
        final Object[] nodeTools = getNewNodeTypes();
        final Object[] actions =
            new Object[edgeTools.length + nodeTools.length];
        int i = 0;
        for (Object meta : edgeTools) {
            if (meta instanceof Object[]) {
                Object[] childEdgeTools = (Object[]) meta;
                final Object[] childActions =
                    new Object[childEdgeTools.length];
                int j = 0;
                for (Object childMeta : childEdgeTools) {
                    childActions[j++] = getCreateEdgeAction(childMeta);
                }
                actions[i++] = childActions;
            } else {
                actions[i++] = getCreateEdgeAction(meta);
            }
        }
        for (Object meta : nodeTools) {
            if (meta instanceof Object[]) {
                Object[] childNodeTools = (Object[]) meta;
                final Object[] childActions =
                    new Object[childNodeTools.length];
                int j = 0;
                for (Object childMeta : childNodeTools) {
                    childActions[j++] = getCreateNodeAction(childMeta);
                }
                actions[i++] = childActions;
            } else {
                actions[i++] = getCreateNodeAction(meta);
            }
        }
        return actions;
    }
    
    abstract Object[] getNewNodeTypes();
    abstract Object[] getNewEdgeTypes();

    /**
     * @return Returns a diagram tool creation action.
     */
    private Action getCreateNodeAction(Object metaType) {
        String label = Model.getMetaTypes().getName(metaType);
        return new RadioAction(
                new CmdCreateNode(metaType, label));
    }
    
    protected Action getCreateEdgeAction(Object metaType) {
        String label = Model.getMetaTypes().getName(metaType);
        return new RadioAction(
                new ActionSetMode(
                        ModeCreatePolyEdge.class,
                        "edgeClass",
                        metaType,
                        label));
    }    
    
    @Override
    public void encloserChanged(FigNode enclosed, FigNode oldEncloser,
            FigNode newEncloser) {
    	// Do nothing.        
    }
    
    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#isRelocationAllowed(java.lang.Object)
     */
    public boolean isRelocationAllowed(Object base)  {
        return false;
    }
    
    public Collection getRelocationCandidates(Object root) {
        return Collections.EMPTY_LIST;
    }
    
    public boolean relocate(Object base) {
        return false;
    }
}
