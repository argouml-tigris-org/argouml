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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import org.argouml.notation2.NotationType;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigStereotypesGroup;
import org.tigris.gef.presentation.Fig;

abstract class BaseDisplayState implements StereotypeDisplayer, NameDisplayer {

    private final Fig bigPort;
    private final Fig stereotypeFig;
    private final Fig nameFig;
    
    public BaseDisplayState(
            final Rectangle rect,
            final Color lineColor,
            final Color fillColor,
            final Object modelElement,
            final DiagramSettings settings) {
        stereotypeFig = new FigStereotypesGroup(modelElement, new Rectangle(0, 0, 0, 0), settings);
        nameFig = new FigNotation(
                modelElement,
                new Rectangle(0, 0, 0, 0),
                settings,
                NotationType.NAME);
        bigPort = createBigPort(rect, lineColor, fillColor);
    }
    
    public Fig getStereotypeDisplay() {
        return stereotypeFig;
    }

    public Fig getNameDisplay() {
        return nameFig;
    }

    abstract Dimension getMinimumSize();
    abstract Fig createBigPort(Rectangle rect, Color lineColor, Color fillColor);
    
    Fig getBigPort() {
        return bigPort;
    }
}
