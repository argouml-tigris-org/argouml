// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

// File: PathConvPercent.java
// Classes: PathConvPercent
// Original Author: abonner@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.ui;

import java.awt.Point;

import org.tigris.gef.base.PathConv;
import org.tigris.gef.presentation.Fig;

/**
 * Used to place labels as specific positions along a FigEdge. For example, a
 * label can be placed in the middle of a FigEdge by using 50%. This version
 * changes the behavior as it tries to avoid that the itemFig cuts through the pathFig.
 */

public class PathConvPercent2 extends PathConv {
    
    private Fig itemFig;
    
    private int percent = 0;

    private int offset = 0;

    public PathConvPercent2(Fig theFig, Fig itemFig, int newPercent, int newOffset) {
        super(theFig);
        this.itemFig = itemFig;
        setPercentOffset(newPercent, newOffset);
    }
    
    public void stuffPoint(Point res) {
        int figLength = _pathFigure.getPerimeterLength();
        if (figLength < 10) {
            res.setLocation(_pathFigure.center());
            return;
        }
        int pointToGet = (figLength * percent) / 100;

        _pathFigure.stuffPointAlongPerimeter(pointToGet, res);

        applyOffsetAmount(_pathFigure.pointAlongPerimeter(pointToGet + 5),
                _pathFigure.pointAlongPerimeter(pointToGet - 5), offset, res);
    }

    public void setPercentOffset(int newPercent, int newOffset) {
        percent = newPercent;
        offset = newOffset;
    }

    public void setClosestPoint(Point newPoint) {
    }

    protected void applyOffsetAmount(Point p1, Point p2, int offset, Point res) {
        // slope of the line we're finding the normal to
        // is slope, and the normal is the negative reciprocal
        // slope is (p1.y - p2.y) / (p1.x - p2.x)
        // so recip is - (p1.x - p2.x) / (p1.y - p2.y)
        int recipnumerator = (p1.x - p2.x) * -1;
        int recipdenominator = (p1.y - p2.y);

        if (recipdenominator == 0 && recipnumerator == 0)
            return;
        
        
        // find the point offset on the line that gives a
        // correct offset

        double len = Math.sqrt(recipnumerator * recipnumerator
                + recipdenominator * recipdenominator);
        int dx = (int) ((recipdenominator * offset) / len);
        int dy = (int) ((recipnumerator * offset) / len);

        res.x += Math.abs(dx);
        res.y -= Math.abs(dy);
        
        int width = itemFig.getHalfWidth();
        
        if (recipnumerator != 0) {
            double factor = (Math.tanh(((double)recipdenominator / (double)recipnumerator)));
            res.x += (Math.abs(factor) * width);
        }
        else res.x += width;
    }
}