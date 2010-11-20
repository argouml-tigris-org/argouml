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

import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigSingleLineText;
import org.argouml.uml.diagram.ui.FigStereotypesGroup;
import org.tigris.gef.base.CreateNodeAction;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRRect;

abstract class BaseDisplayState implements StereotypeDisplayer, NameDisplayer {

    private Fig bigPort;
    private Fig stereotypeFig;
    private Fig nameFig;
    
    public BaseDisplayState(int x, int y, int w, int h, Color lineColor,
            Color fillColor, Object modelElement, DiagramSettings settings) {
        stereotypeFig = new FigStereotypesGroup(modelElement, new Rectangle(0, 0, 0, 0), settings);
        nameFig = new FigSingleLineText(modelElement, new Rectangle(0, 0, 0, 0), settings, true);
        bigPort = createBigPort(x, y, w, h, lineColor, fillColor);
    }
    
    public Fig getStereotypeDisplay() {
        return stereotypeFig;
    }

    public Fig getNameDisplay() {
        return nameFig;
    }

    abstract Dimension getMinimumSize();
    abstract Fig createBigPort(int x, int y, int w, int h, Color lineColor, Color fillColor);
    
    Fig getBigPort() {
        return bigPort;
    }
}
