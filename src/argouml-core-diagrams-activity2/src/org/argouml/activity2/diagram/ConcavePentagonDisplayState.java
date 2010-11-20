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
import java.awt.Polygon;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigPoly;

public class ConcavePentagonDisplayState extends BaseDisplayState implements StereotypeDisplayer, NameDisplayer {

    private static final int PADDING = 8;
    
    public ConcavePentagonDisplayState(int x, int y, int w, int h, Color lineColor,
            Color fillColor, Object modelElement, DiagramSettings settings) {
        super(x, y, w, h, lineColor, fillColor, modelElement, settings);
        createBigPort(x, y, w, h, lineColor, fillColor);
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
    
    protected Fig createBigPort(int x, int y, int w, int h, Color lineColor, Color fillColor) {
        final FigPoly polyFig = new FigPoly();
        final int[] xs = new int[6];
        final int[] ys = new int[6];
        
        xs[0] = x;              ys[0] = y;
        xs[1] = x + w;      ys[1] = y;
        xs[2] = x + w;      ys[2] = y + h;
        xs[3] = x;              ys[3] = y + h;
        xs[4] = x + h / 2; ys[4] = y + h / 2;
        xs[5] = x;              ys[5] = y;
        final Polygon p = new Polygon(xs, ys, 6);
        polyFig.setPolygon(p);
        
        return polyFig;
    }
}
