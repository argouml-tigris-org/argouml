// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.Point;

import org.tigris.gef.base.PathConv;
import org.tigris.gef.presentation.Fig;

/**
 * Used to place labels as specific positions along a FigEdge. For example, a
 * label can be placed in the middle of a FigEdge by using 50%. This version
 * changes the behavior as it tries to avoid that the itemFig cuts through the
 * pathFig.
 *
 * @author abonner@ics.uci.edu
 */
public class PathConvPercent2 extends PathConv {

    /**
     * The item Fig.
     */
    private Fig itemFig;

    /**
     * The percent.
     */
    private int percent;

    /**
     * The offset.
     */
    private int offset;

    /**
     * Constructor.
     *
     * @param theFig The Fig.
     * @param theItemFig The item Fig.
     * @param newPercent The percent.
     * @param newOffset The offset.
     */
    public PathConvPercent2(Fig theFig, Fig theItemFig, int newPercent,
            int newOffset) {
        super(theFig);
        itemFig = theItemFig;
        setPercentOffset(newPercent, newOffset);
    }

    /*
     * @see org.tigris.gef.base.PathConv#stuffPoint(java.awt.Point)
     */
    public void stuffPoint(Point res) {
        int figLength = _pathFigure.getPerimeterLength();
        if (figLength < 10) {
            res.setLocation(_pathFigure.getCenter());
            return;
        }
        int pointToGet = (figLength * percent) / 100;

        _pathFigure.stuffPointAlongPerimeter(pointToGet, res);

        applyOffsetAmount(_pathFigure.pointAlongPerimeter(pointToGet + 5),
                _pathFigure.pointAlongPerimeter(pointToGet - 5), offset, res);
    }

    /**
     * Set the percent and offset again.
     *
     * @param newPercent The new percent.
     * @param newOffset The new offset.
     */
    public void setPercentOffset(int newPercent, int newOffset) {
        percent = newPercent;
        offset = newOffset;
    }

    /*
     * @see org.tigris.gef.base.PathConv#setClosestPoint(java.awt.Point)
     */
    public void setClosestPoint(Point newPoint) {
    }

    /*
     * @see org.tigris.gef.base.PathConv#applyOffsetAmount(java.awt.Point, java.awt.Point, int, java.awt.Point)
     */
    protected void applyOffsetAmount(
            Point p1, Point p2,
            int theOffset, Point res) {
        // slope of the line we're finding the normal to
        // is slope, and the normal is the negative reciprocal
        // slope is (p1.y - p2.y) / (p1.x - p2.x)
        // so recip is - (p1.x - p2.x) / (p1.y - p2.y)
        int recipnumerator = (p1.x - p2.x) * -1;
        int recipdenominator = (p1.y - p2.y);

        if (recipdenominator == 0 && recipnumerator == 0) {
            return;
        }


        // find the point offset on the line that gives a
        // correct offset

        double len =
            Math.sqrt(recipnumerator * recipnumerator
                + recipdenominator * recipdenominator);
        int dx = (int) ((recipdenominator * theOffset) / len);
        int dy = (int) ((recipnumerator * theOffset) / len);

        res.x += Math.abs(dx);
        res.y -= Math.abs(dy);

        int width = itemFig.getWidth() / 2;

        if (recipnumerator != 0) {
            double slope = (double) recipdenominator / (double) recipnumerator;

            double factor = tanh(slope);
            res.x += (Math.abs(factor) * width);
        } else {
            res.x += width;
        }
    }

    /**
     * Calculate the tangens hyperbolicus.
     *
     * @param x The argument.
     * @return tangens hyberbolicus
     */
    private double tanh(double x) {
        return ((Math.exp(x) - Math.exp(-x)) / 2)
            / ((Math.exp(x) + Math.exp(-x)) / 2);
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -8079350336685789199L;
}
