/* $Id: $
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
import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Rectangle;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.FigPoly;

public class ConcavePentagonDisplayState extends BaseDisplayState
        implements StereotypeDisplayer, NameDisplayer {

    private static final int PADDING = 8;
    
    public ConcavePentagonDisplayState(Rectangle rect, Color lineColor,
            Color fillColor, Object modelElement, DiagramSettings settings) {
        super(rect, lineColor, fillColor, modelElement, settings);
        createBigPort(rect, lineColor, fillColor);
    }

    @Override
    public Dimension getMinimumSize() {
        final Dimension stereoDim = getStereotypeDisplay().getMinimumSize();
        final Dimension nameDim = getNameDisplay().getMinimumSize();

        int w = Math.max(stereoDim.width, nameDim.width) + PADDING * 2;
        /* The stereoDim has height=2, even if it is empty, 
         * hence the -2 below: */
        final int h = stereoDim.height - 2 + nameDim.height + PADDING;
        w = Math.max(w, h + 44); // the width needs to be > the height
        return new Dimension(w, h);
    }
    
    protected DiagramElement createBigPort(
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
        xs[0] = x;         ys[0] = y;
        xs[1] = x + w;     ys[1] = y;
        xs[2] = x + w;     ys[2] = y + h;
        xs[3] = x;         ys[3] = y + h;
        xs[4] = x + h / 2; ys[4] = y + h / 2;
        xs[5] = x;         ys[5] = y;
        final Polygon p = new Polygon(xs, ys, 6);
        poly.setPolygon(p);
        
        return poly;
    }
    
    
    private class Poly extends FigPoly implements DiagramElement {
        Poly() {
            super();
        }
    }
}
