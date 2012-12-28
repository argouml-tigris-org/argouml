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
class FigGravityPoly extends FigPoly {
    
    FigGravityPoly() {
        super();
    }
    
    FigGravityPoly(Polygon p) {
        super();
        setPolygon(p);
    }

    @Override
    public Point getClosestPoint(Point anotherPt) {
        return Geometry.ptClosestTo(
                getXs(), getYs(), getYs().length, anotherPt);
    }

    public List getGravityPoints() {
        return null;
    }
}
