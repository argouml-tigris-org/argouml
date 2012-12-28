/* $Id$
 *****************************************************************************
 * Copyright (c) 2010-2011 Contributors - see below
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

import org.argouml.i18n.Translator;
import org.argouml.model.ActivityDiagram;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.UmlDiagramRenderer;
import org.tigris.gef.presentation.FigNode;

/**
 * Diagram class for UML2 Activity Diagram
 * @author Bob Tarling
 */
public class UMLActivityDiagram extends BaseDiagram implements ActivityDiagram {
    
    /**
     * A UML2 activity diagram is owned by an activity
     * @param activity
     */
    UMLActivityDiagram(Object activity) {
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
    
    protected String getDiagramXmlFile() {
        return "org/argouml/activity2/diagram/diagram.xml";
    }
    
    public DiagramElement createDiagramElement(
            final Object modelElement,
            final Rectangle bounds) {
        
        DiagramElement figNode = null;
        
        DiagramSettings settings = getDiagramSettings();
        
        if (Model.getFacade().isAActivityNode(modelElement)) {
            figNode = new FigBaseNode(modelElement, bounds, settings);
            final String style;
            if (Model.getFacade().isAObjectNode(modelElement)) {
                style = "rect";
            } else if (Model.getFacade().isASendSignalAction(modelElement)) {
                style = "pentagon";
            } else if (Model.getFacade().isAAcceptEventAction(modelElement)) {
                style = "concave-pentagon";
            } else {
                style = "rrect";
            }
            DiagramElementBuilder.buildDiagramElement(
                    (FigBaseNode) figNode, style, modelElement, settings);
        }
        
        return figNode;
    }

    @Override
    public void encloserChanged(FigNode enclosed, FigNode oldEncloser,
            FigNode newEncloser) {
        // TODO Auto-generated method stub
        
    }
}
