/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2011 Contributors - see below
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

/**
 * A FigPoly to which edges gravitate to the perimeter instead of the points
 * @author Bob Tarling
 *
 */
class FigAcceptEventPoly extends FigGravityPoly {
    
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

    /**
     * This overrides the behaviour of FigGravityPoly in that the closest point 
     * on the left of the fig is where a horizontal line from anotherPt would
     * intersect this Fig. This prevents an edge from attaching to a point
     * on a convex polygon where it would look better connecting to the edge.
     */
    @Override
    public Point getClosestPoint(Point anotherPt) {
        if (anotherPt.x < getX()
                && anotherPt.y >= getY()
                && anotherPt.y < getY() + getHeight()) {
            final int x;
            if (anotherPt.y < getY() + getHeight() / 2) {
                x = getX() + (anotherPt.y - getY());
            } else {
                x = getX() + ((getY() + getHeight()) - anotherPt.y);
            }
            return new Point(x, anotherPt.y); 
        } else {
            return super.getClosestPoint(anotherPt);
        }
    }

    public List getGravityPoints() {
        return null;
    }
}
