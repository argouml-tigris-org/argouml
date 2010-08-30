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

import java.awt.Rectangle;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.model.StateDiagram;
import org.argouml.uml.diagram.DiagramElement;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.UmlDiagramRenderer;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.ui.FigNodeModelElement;

public class UMLStateDiagram extends BaseDiagram implements StateDiagram {
    
    private static final Logger LOG = Logger
        .getLogger(UMLStateDiagram.class);
    
    public UMLStateDiagram(Object stateMachine) {
        super(stateMachine);
    }
    
    @Override
    UmlDiagramRenderer createDiagramRenderer() {
        return new StateDiagramRenderer();
    }

    @Override
    UMLMutableGraphSupport createGraphModel() {
        return new StateDiagramGraphModel(); 
    }

    @Override
    Object[] getNewEdgeTypes() {
        return new Object[] {
        };
    }

    @Override
    Object[] getNewNodeTypes() {
        return new Object[] {
        };
    }


    @Override
    public void initialize(Object owner) {
        super.initialize(owner);
        StateDiagramGraphModel gm =
            (StateDiagramGraphModel) getGraphModel();
    }

    @Override
    public String getLabelName() {
        return Translator.localize("label.state-diagram");
    }
    
    @Override
    public boolean doesAccept(Object objectToAccept) {
        if (Model.getFacade().isAComment(objectToAccept) ) {
            return true;
        }
        return false;
    }
    
    public DiagramElement createDiagramElement(
            final Object modelElement,
            final Rectangle bounds) {
        
        FigNodeModelElement figNode = null;
        
        DiagramSettings settings = getDiagramSettings();
        
        return figNode;
    }
}
