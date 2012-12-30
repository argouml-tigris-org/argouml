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
import java.awt.Polygon;
import java.awt.Rectangle;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.FigPoly;

class FigNamedPentagon extends FigBasePresentation
        implements StereotypeDisplayer, NameDisplayer {

    private static final int PADDING = 8;
    
    public FigNamedPentagon(final Object owner, Rectangle rect, Color lineColor,
            Color fillColor, Object modelElement, DiagramSettings settings) {
        super(owner, rect, lineColor, fillColor, modelElement, settings);
        createBorder(rect, lineColor, fillColor);
    }

    protected DiagramElement createBorder(
            final Rectangle rect,
            final Color lineColor,
            final Color fillColor) {
        final Poly poly = new Poly();
        final int[] xs = new int[6];
        final int[] ys = new int[6];
        final int x = rect.x;
        final int y = rect.y;
        final int w = rect.width;
        final int h = rect.height;
        xs[0] = x;             ys[0] = y;
        xs[1] = x + w - h / 2; ys[1] = y;
        xs[2] = x + w;         ys[2] = y + h / 2;
        xs[3] = x + w - h / 2; ys[3] = y + h;
        xs[4] = x;             ys[4] = y + h;
        xs[5] = x;             ys[5] = y;
        final Polygon p = new Polygon(xs, ys, 6);
        poly.setPolygon(p);
        
        return poly;
    }
    
    protected int getLeftMargin() {
        return getBounds().height / 2;
    }
    
    private class Poly extends FigPoly implements DiagramElement {
        Poly() {
            super();
        }
    }
}
