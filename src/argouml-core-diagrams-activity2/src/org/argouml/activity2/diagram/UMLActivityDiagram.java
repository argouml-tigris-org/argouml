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

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.ActivityDiagram;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramElement;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.UmlDiagramRenderer;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.ui.FigNodeModelElement;

public class UMLActivityDiagram extends BaseDiagram implements ActivityDiagram {
    
    private static final Logger LOG = Logger
        .getLogger(UMLActivityDiagram.class);
    
    public UMLActivityDiagram(Object activity) {
        super(activity);
    }
    
    @Override
    UmlDiagramRenderer createDiagramRenderer() {
        return new ActivityDiagramRenderer();
    }

    @Override
    UMLMutableGraphSupport createGraphModel() {
        return new ActivityDiagramGraphModel(); 
    }

    @Override
    Object[] getNewEdgeTypes() {
        return new Object[] {
            Model.getMetaTypes().getControlFlow(),
            Model.getMetaTypes().getObjectFlow()
        };
    }

    @Override
    Object[] getNewNodeTypes() {
        return new Object[] {
            Model.getMetaTypes().getCallBehaviorAction(),
            Model.getMetaTypes().getCreateObjectAction(),
            Model.getMetaTypes().getDestroyObjectAction(),
            Model.getMetaTypes().getAcceptEventAction(),
            Model.getMetaTypes().getSendSignalAction(),
            Model.getMetaTypes().getDestroyObjectAction(),
            Model.getMetaTypes().getActivityParameterNode(),
            Model.getMetaTypes().getCentralBufferNode(),
            Model.getMetaTypes().getDataStoreNode(),
        };
    }


    @Override
    public void initialize(Object owner) {
        super.initialize(owner);
        ActivityDiagramGraphModel gm =
            (ActivityDiagramGraphModel) getGraphModel();
    }

    @Override
    public String getLabelName() {
        return Translator.localize("label.activity-diagram");
    }
    
    @Override
    public boolean doesAccept(Object objectToAccept) {
        if (Model.getFacade().isAComment(objectToAccept)
                || Model.getFacade().isAActivityEdge(objectToAccept) 
                || Model.getFacade().isAActivityNode(objectToAccept) ) {
            return true;
        }
        return false;
    }
    
    public DiagramElement createDiagramElement(
            final Object modelElement,
            final Rectangle bounds) {
        
        FigNodeModelElement figNode = null;
        
        DiagramSettings settings = getDiagramSettings();
        
        if (Model.getFacade().isAActivityNode(modelElement)) {
            figNode = new FigActivityNode(modelElement, bounds, settings);
        } else if (Model.getFacade().isAComment(modelElement)) {
            figNode = new FigComment(modelElement, bounds, settings);
        }
        
        return figNode;
    }
}
