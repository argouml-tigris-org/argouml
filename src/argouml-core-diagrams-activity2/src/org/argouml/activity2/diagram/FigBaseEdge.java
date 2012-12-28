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

import java.awt.Color;

import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.PathItemPlacement;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdgePoly;

/**
 * The Fig for all edge diagram elements. All specialist edge diagram elements
 * decorate this to get specialist behaviour 
 * @author Bob Tarling
 */
class FigBaseEdge extends FigEdgePoly {

    private final DiagramSettings settings;
    private DiagramElement nameDiagramElement;
    
    FigBaseEdge(Object owner, DiagramSettings settings) {
        super();
        _useNearest = true;
        setOwner(owner);
        this.settings = settings;
        if (nameDiagramElement != null) {
            addPathItem((Fig) nameDiagramElement,
                    new PathItemPlacement(this, (Fig) nameDiagramElement, 50, 10));
        }
        getFig().setLineColor(Color.black);
    }
    

    public DiagramElement getNameDiagramElement() {
        return nameDiagramElement;
    }
    
}
