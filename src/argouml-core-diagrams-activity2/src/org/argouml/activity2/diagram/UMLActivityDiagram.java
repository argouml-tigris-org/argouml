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

package org.argouml.activity2.diagram;

import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.Collections;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.ActivityDiagram;
import org.argouml.model.Model;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.DiagramElement;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.ui.ActionSetMode;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.FigNode;

public class UMLActivityDiagram extends UMLDiagram implements ActivityDiagram {
    
    private static final Logger LOG = Logger
        .getLogger(UMLActivityDiagram.class);
    
    public UMLActivityDiagram(Object activity) {
        super();
        MutableGraphModel gm = new ActivityDiagramGraphModel(); 
        setGraphModel(gm);
        
        // Create the layer
        LayerPerspective lay = new
            LayerPerspectiveMutable(this.getName(), gm);
        setLayer(lay);
        
        // Create the renderer
        ActivityDiagramRenderer renderer = new ActivityDiagramRenderer();
        lay.setGraphNodeRenderer(renderer);
        lay.setGraphEdgeRenderer(renderer);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Constructing Activity Diagram for activity "
                    + activity);
        }
        try {
            this.setName(getNewDiagramName());
        } catch (PropertyVetoException e) {
            LOG.error("Exception", e);
        }
        ((ActivityDiagramGraphModel) getGraphModel()).setOwner(activity);
        setNamespace(activity);
    }
    
    
    @Override
    public void initialize(Object owner) {
        super.initialize(owner);
        ActivityDiagramGraphModel gm =
            (ActivityDiagramGraphModel) getGraphModel();
        gm.setOwner(owner);
    }

    @Override
    protected Object[] getUmlActions() {
        Object[] actions = {
            getCreateEdgeAction(Model.getMetaTypes().getControlFlow(), "button.new-controlflow"),
            getCreateEdgeAction(Model.getMetaTypes().getObjectFlow(), "button.new-objectflow"),
            getCreateNodeAction(Model.getMetaTypes().getCallBehaviorAction(), "button.new-callbehavioraction"),
            getCreateNodeAction(Model.getMetaTypes().getCreateObjectAction(), "button.new-createobjectaction"),
            getCreateNodeAction(Model.getMetaTypes().getDestroyObjectAction(), "button.new-destroyobjectaction"),
            getCreateNodeAction(Model.getMetaTypes().getAcceptEventAction(), "button.new-accepteventaction"),
            getCreateNodeAction(Model.getMetaTypes().getSendSignalAction(), "button.new-sendsignalaction"),
            getCreateNodeAction(Model.getMetaTypes().getDestroyObjectAction(), "button.new-destroyobjectaction"),
        };
        return actions;
    }

    /**
     * @return Returns a diagram tool creation action.
     */
    private Action getCreateNodeAction(Object metaType, String label) {
        return new RadioAction(
                new CmdCreateNode(metaType, label));
    }
    
    protected Action getCreateEdgeAction(Object metaType, String label) {
        return new RadioAction(
                new ActionSetMode(
                        ModeCreatePolyEdge.class,
                        "edgeClass",
                        metaType,
                        label));
    }    
    
    @Override
    public String getLabelName() {
        return Translator.localize("label.activity-diagram");
    }
    
    @Override
    public void encloserChanged(FigNode enclosed, FigNode oldEncloser,
            FigNode newEncloser) {
    	// Do nothing.        
    }
    
    @Override
    public boolean doesAccept(Object objectToAccept) {
        if (Model.getFacade().isAComment(objectToAccept)
                || Model.getFacade().isAActivityEdge(objectToAccept) 
                || Model.getFacade().isAAction(objectToAccept) ) {
            return true;
        }
        return false;
    }
    
    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#isRelocationAllowed(java.lang.Object)
     */
    public boolean isRelocationAllowed(Object base)  {
        // An activity diagram is always attached to a single activity and
        // can't be relocated/
        return false;
    }
    
    public Collection getRelocationCandidates(Object root) {
        // An activity diagram is always attached to a single activity and
        // can't be relocated/
        return Collections.EMPTY_LIST;
    }
    
    public boolean relocate(Object base) {
        // An activity diagram is always attached to a single activity and
        // can't be relocated
        return false;
    }
    
    public DiagramElement createDiagramElement(
            final Object modelElement,
            final Rectangle bounds) {
        
        FigNodeModelElement figNode = null;
        
        DiagramSettings settings = getDiagramSettings();
        
        if (Model.getFacade().isAAction(modelElement)) {
            figNode = new FigAction(modelElement, bounds, settings);
        } else if (Model.getFacade().isAComment(modelElement)) {
            figNode = new FigComment(modelElement, bounds, settings);
        }
        
        return figNode;
    }
}
