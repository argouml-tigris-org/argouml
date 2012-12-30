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

import java.awt.Color;
import java.awt.Rectangle;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.FigRRect;

/**
 * The state for displaying a node as a rounded rectangle
 * @author Bob Tarling
 */
class FigNamedRRect extends FigBasePresentation
            implements StereotypeDisplayer, NameDisplayer {

    private static int RADIUS = 16;
    
    public FigNamedRRect(
            final Object owner,
            final Rectangle rect,
            final Color lineColor,
            final Color fillColor,
            final Object modelElement,
            final DiagramSettings settings) {
        super(owner, rect, lineColor, fillColor, modelElement, settings);
        createBorder(rect, lineColor, fillColor);
    }

    DiagramElement createBorder(Rectangle rect, Color lineColor, Color fillColor) {
        return new RRect(
                rect.x, rect.y, rect.width, rect.height, lineColor, fillColor);
    }
    
    private class RRect extends FigRRect implements DiagramElement {
        RRect(
                final int x, 
                final int y, 
                final int w, 
                final int h, 
                final Color lineColor, 
                final Color fillColor) {
            super(x, y, w, h, lineColor, fillColor);
            this.setCornerRadius(RADIUS);
        }
    }
    
    
    protected int getRightMargin() {
        return RADIUS;
    }
    
    protected int getLeftMargin() {
        return RADIUS;
    }
}
