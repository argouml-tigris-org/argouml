/* $Id: $
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
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
import java.awt.Point;
import java.awt.Polygon;
import java.util.List;

import org.tigris.gef.base.Geometry;
import org.tigris.gef.presentation.FigPoly;

/**
 * A FigPoly to which edges gravitate to the perimeter instead of the points
 * @author Bob Tarling
 *
 */
public class FigAcceptEventPoly extends FigGravityPoly {
    
    FigAcceptEventPoly(int x, int y, int w, int h, Color lineColor, Color fillColor) {
        super();
        final int[] xs = new int[6];
        final int[] ys = new int[6];
        xs[0] = x;         ys[0] = y;
        xs[1] = x + w;     ys[1] = y;
        xs[2] = x + w;     ys[2] = y + h;
        xs[3] = x;         ys[3] = y + h;
        xs[4] = x + h / 2; ys[4] = y + h / 2;
        xs[5] = x;         ys[5] = y;
        final Polygon p = new Polygon(xs, ys, 6);
        setPolygon(p);
    }
    
    FigAcceptEventPoly(Polygon p) {
        super(p);
    }

    @Override
    public Point getClosestPoint(Point anotherPt) {
        if (anotherPt.x < getX() && anotherPt.y >= getY() && anotherPt.y < getY() + getHeight()) {
            if (anotherPt.y < getY() + getHeight() / 2) {
                return new Point(getX() + (anotherPt.y - getY()), anotherPt.y); 
            } else {
                return new Point(getX() + ((getY() + getHeight()) - anotherPt.y), anotherPt.y); 
            }
        } else {
            return super.getClosestPoint(anotherPt);
        }
    }

    public List getGravityPoints() {
        return null;
    }
}
