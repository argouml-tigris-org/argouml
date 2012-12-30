/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    dthompson
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008 Tom Morris and other contributors. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Contributors.
// The software program and documentation are supplied "AS
// IS", without any accompanying services from The Contributors. They
// do not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// CONTRIBUTORS BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE CONTRIBUTORS HAVE BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE CONTRIBUTORS SPECIFICALLY DISCLAIM ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE CONTRIBUTORS
// HAVE NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tigris.gef.base.Globals;
import org.tigris.gef.base.PathConv;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;

/**
 * This class implements the coordinate generation required for GEF's
 * FigEdge.addPathItem. It can be used to place labels at an offset relative to
 * an anchor position along the path described by a FigEdge. For example, a
 * label can be placed in the middle of a FigEdge by using 50% or near an end by
 * using 0% width an offset of +5 or 100% with an offset of -5.
 * <p>
 * The calculated anchor position along the path is then used as a base to which
 * additional offsets are added. This can be either in the form of a
 * displacement vector and distance specified using an angle relative to the
 * angle of the edge at that point or a fixed x,y offset.
 * <p>
 * This class tries to avoid placing the itemFig so that it intersects the
 * pathFig. Note that:<ul>
 * <li>itemFig must return correct size information for this to work properly,
 * which is not currently true of all GEF figs (eg. text figs).
 * <li>Only the path is considered, so you can still get overlaps with the
 * connected nodes on the ends of the edges or other labels on the same edge or
 * other figs in the diagram. Using a displacement angle of 135 or -135 degrees
 * is a good way to help avoid the connected nodes.
 * </ul>
 *
 * @author Tom Morris <tfmorris@gmail.com>
 * @since 0.27.3
 */
public class PathItemPlacement extends PathConv {

    private static final Logger LOG =
        Logger.getLogger(PathItemPlacement.class.getName());

    private boolean useCollisionCheck = true;

    private boolean useAngle = true;

    private double angle = 90; // default angle is 90 deg.

    /**
     * the fig to be placed.
     */
    private Fig itemFig;

    /**
     * Percentage of the way along the path to place anchor.
     */
    private int percent;

    /**
     * Fixed delta offset from the computed percentage location.
     */
    private int pathOffset;

    /**
     * Distance along the displacement vector (ie distance from the edge)
     */
    private int vectorOffset;

    /**
     * Fixed offset to use for manual positioning.  Coordinates are interpreted
     * as an XY offset.
     */
    private Point offset;

    /**
     * Set true to keep items on same side (top or bottom) of path as
     * it rotates through vertical.
     */
    private final boolean swap = true;

    /**
     * Construct a new path to coordinate conversion object which positions at a
     * percentage along a path with a given distance perpendicular to the path
     * at the anchor point.
     *
     * @param pathFig fig representing the edge which will be used for
     *            positioning.
     * @param theItemFig the fig to be placed.
     * @param pathPercent distance in integer percentages along path for anchor
     *            point from which the offset is computed.. Beginning of path is
     *            0 and end of path is 100.
     * @param displacement distance from the edge to place the fig. This is
     *            computed along the normal.
     */
    public PathItemPlacement(FigEdge pathFig, Fig theItemFig, int pathPercent,
            int displacement) {

        this(pathFig, theItemFig, pathPercent, 0, 90, displacement);
    }


    /**
     * Construct a new path to coordinate conversion object which positions
     * an anchor point on the path at a percentage along a path with an offset,
     * and from the anchor point at a distance measured at a given angle.
     *
     * @param pathFig fig representing the edge which will be used for
     *            positioning.
     * @param theItemFig the fig to be placed.
     * @param pathPercent distance in integer percentages along path for anchor
     *            point from which the offset is computed. Beginning of path is
     *            0 and end of path is 100.
     * @param pathDelta delta distance in coordinate space units to add to the
     *            computed percentage position
     * @param displacementAngle angle to add to computed line slope when
     *            computing the displacement vector
     * @param displacementDistance distance from the edge to place the fig. This
     *            is computed along the normal from the anchor position using
     *            pathPercent & pathDelta.
     */
    public PathItemPlacement(FigEdge pathFig, Fig theItemFig, int pathPercent,
            int pathDelta,
            int displacementAngle,
            int displacementDistance) {
        super(pathFig);
        itemFig = theItemFig;
        setAnchor(pathPercent, pathDelta);
        setDisplacementVector(displacementAngle + 180, displacementDistance);
    }

    /**
     * Construct a new path to coordinate conversion object which positions
     * an anchor point on the path at a percentage along a path with an offset,
     * and from the anchor point at a distance measured in X, Y coordinates.
     *
     * @param pathFig fig representing the edge which will be used for
     *            positioning.
     * @param theItemFig the fig to be placed.
     * @param pathPercent distance in integer percentages along path for anchor
     *            point from which the offset is computed. Beginning of path is
     *            0 and end of path is 100.
     * @param pathDelta delta distance in coordinate space units to add to the
     *            computed percentage position
     * @param absoluteOffset point representing XY offset from anchor to use for
     *            positioning.
     */
    public PathItemPlacement(FigEdge pathFig, Fig theItemFig, int pathPercent,
            int pathDelta, Point absoluteOffset) {
        super(pathFig);
        itemFig = theItemFig;
        setAnchor(pathPercent, pathDelta);
        setAbsoluteOffset(absoluteOffset);
    }

    /**
     * Returns the Fig that this PathItemPlacement places.
     * To get the Fig of the Edge which owns this fig, use use getPathFig()
     * @see org.tigris.gef.base.PathConv#getPathFig()
     * @note Used by PGML.tee.
     * @return The fig that this path item places.
     */
    public Fig getItemFig() {
        return itemFig;
    }

    /**
     * Compute a position.  This strangely named method computes a
     * position using the current set of parameters and returns the result
     * by updating the provided Point.
     *
     * @param result Point in which to return result.  Not read as input.
     *
     * @see org.tigris.gef.base.PathConv#stuffPoint(java.awt.Point)
     */
    public void stuffPoint(Point result) {
        result = getPosition(result);
    }

    /**
     * Get the computed target position based on the current set of parameters.
     *
     * @return the computed position
     */
    public Point getPosition() {
        return getPosition(new Point());
    }

    @Override
    public Point getPoint() {
        return getPosition();
    }

    /**
     * Get the anchor position.  The represents the point along the path that
     * is used as the starting point for all other calculations.
     *
     * @return the anchor position represented by the current percentage and
     *         path offset parameters
     */
    public Point getAnchorPosition() {
        int pathDistance = getPathDistance();
        Point anchor = new Point();
        _pathFigure.stuffPointAlongPerimeter(pathDistance, anchor);
        return anchor;
    }


    /**
     * Compute distance along the path based on percentage and offset, clamped
     * to the length of the path.
     *
     * @return the distance
     */
    private int getPathDistance() {
        int length = _pathFigure.getPerimeterLength();
        int distance = Math.max(0, (length * percent) / 100 + pathOffset);
        // Boundary condition in GEF, make sure this is LESS THAN, not equal
        if (distance >= length) {
            distance = length - 1;
        }
        return distance;
    }


    /**
     * Get the computed position based on the current set of parameters.
     *
     * @param result Point in which to return result.  Not read as input, but it
     * <em>is</em> modified.
     * @return the updated point
     */
    private Point getPosition(Point result) {

        Point anchor = getAnchorPosition();
        result.setLocation(anchor);

        // If we're using a fixed offset, just add it and return
        // No collision detection is done in this case
        if (!useAngle) {
            result.translate(offset.x, offset.y);
            return result;
        }

        double slope = getSlope();
        result.setLocation(applyOffset(slope, vectorOffset, anchor));

        // Check for a collision between our computed position and the edge
        if (useCollisionCheck) {
            int increment = 2; // increase offset by 2px at a time

            // TODO: The size of text figs, which is what we care about most,
            // isn't computed correctly by GEF. If we got ambitious, we could
            // recompute a proper size ourselves.
            Dimension size = new Dimension(itemFig.getWidth(), itemFig
                    .getHeight());

            // Get the points representing the poly line for our edge
            FigEdge fp = (FigEdge) _pathFigure;
            Point[] points = fp.getPoints();
            if (intersects(points, result, size)) {

                // increase offset by increments until we're clear
                int scaledOffset = vectorOffset + increment;

                int limit = 20;
                int count = 0;
                // limit our retries in case its too hard to get free
                while (intersects(points, result, size) && count++ < limit) {
                    result.setLocation(
                            applyOffset(slope, scaledOffset, anchor));
                    scaledOffset += increment;
                }
                // If we timed out, give it one more try on the other side
                if (false /* count >= limit */) {
                    LOG.log(Level.FINE, "Retry limit exceeded.  Trying other side");
                    result.setLocation(anchor);
                    // TODO: This works for 90 degree angles, but is suboptimal
                    // for other angles. It should reflect the angle, rather
                    // than just using a negative offset along the same vector
                    result.setLocation(
                            applyOffset(slope, -vectorOffset, anchor));
                    count = 0;
                    scaledOffset = -scaledOffset;
                    while (intersects(points, result, size)
                            && count++ < limit) {
                        result.setLocation(
                                applyOffset(slope, scaledOffset, anchor));
                        scaledOffset += increment;
                    }
                }
//                LOG.log(Level.FINE, "Final point #" + count + " " + result
//                        + " offset of " + scaledOffset);
            }
        }
        return result;
    }

    /**
     * Check for intersection between the segments of a poly line and a
     * rectangle.  Unlike FigEdge.intersects(), this only checks the main
     * path, not any associated path items (like ourselves).
     *
     * @param points set of points representing line segments
     * @param center position of center
     * @param size size of bounding box
     * @return true if they intersect
     */
    private boolean intersects(Point[] points, Point center, Dimension size) {
        // Convert to bounding box
        // Very screwy!  GEF sometimes uses center and sometimes upper left
        // TODO: GEF also positions text at the nominal baseline which is
        // well inside the bounding box and gives the overall size incorrectly
        Rectangle r = new Rectangle(center.x - (size.width / 2),
                center.y - (size.height / 2),
                size.width, size.height);
        Line2D line = new Line2D.Double();
        for (int i = 0; i < points.length - 1; i++) {
            line.setLine(points[i], points[i + 1]);
            if (r.intersectsLine(line)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Convenience method to set anchor percentage distance and offset.
     *
     * @param newPercent distance as a percent of total path 0<=percent<=100
     * @param newOffset offset in drawing coordinate system
     */
    public void setAnchor(int newPercent, int newOffset) {
        setAnchorPercent(newPercent);
        setAnchorOffset(newOffset);
    }

    /**
     * Set distance along path of anchor in integer percentages.
     * @param newPercent distance as a percent of total path 0<=percent<=100
     */
    public void setAnchorPercent(int newPercent) {
        percent = newPercent;
    }

    /**
     * Set offset along path to be applied to anchor after percentage based
     * location is calculated. Specified in units of the drawing coordinate
     * system.
     *
     * @param newOffset offset in drawing coordinate system
     */
    public void setAnchorOffset(int newOffset) {
        pathOffset = newOffset;
    }

    /**
     * Set a fixed offset from the anchor point.
     * @param newOffset a Point who's x & y coordinates will be used as a
     * displacement from anchor point
     */
    public void setAbsoluteOffset(Point newOffset) {
        offset = newOffset;
        useAngle = false;
    }

    /**
     * Attempts to set a new location for the fig being controlled
     * by this path item.  Takes the given Point which represents an x,y
     * position, and calculates the most appropriate angle and displacement
     * to achieve this new position.  Used when the user drags a label
     * on the diagram.
     * @override
     * @param newPoint The new target location for the PathItem fig.
     * @see org.tigris.gef.base.PathConv#setPoint(java.awt.Point)
     */
    public void setPoint(Point newPoint) {
        int vect[] = computeVector(newPoint);
        setDisplacementAngle(vect[0]);
        setDisplacementDistance(vect[1]);
    }


    /**
     * Compute an angle and distance which is equivalent to the given point.
     * This is a convenience method to help callers get coordinates in a form
     * that can be passed back in using {@link #setDisplacementVector(int, int)}
     *
     * @param point the desired target point
     * @return an array of two integers containing the angle and distance
     */
    public int[] computeVector(Point point) {
        Point anchor = getAnchorPosition();
        int distance = (int) anchor.distance(point);
        int angl = 0;
        double pathSlope = getSlope();
        double offsetSlope = getSlope(anchor, point);

        if (swap && pathSlope > Math.PI / 2 && pathSlope < Math.PI * 3 / 2) {
            angl = -(int) ((offsetSlope - pathSlope) / Math.PI * 180);
        }
        else {
            angl = (int) ((offsetSlope - pathSlope) / Math.PI * 180);
        }

        int[] result = new int[] {angl, distance};
        return result;
    }

    /**
     * Set the displacement vector to the given angle and distance.
     *
     * @param vectorAngle angle in degrees relative to the edge at the anchor
     *            point.
     * @param vectorDistance distance along vector in drawing coordinate units
     */
    public void setDisplacementVector(int vectorAngle, int vectorDistance) {
        setDisplacementAngle(vectorAngle);
        setDisplacementDistance(vectorDistance);
    }

    /**
     * Set the displacement vector to the given angle and distance.
     *
     * @param vectorAngle angle in degrees relative to the edge at the anchor
     *            point.
     * @param vectorDistance distance along vector in drawing coordinate units
     */
    public void setDisplacementVector(double vectorAngle,
            int vectorDistance) {
        setDisplacementAngle(vectorAngle);
        setDisplacementDistance(vectorDistance);
    }

    /**
     * @param offsetAngle the new angle for the displacement vector,
     * specified in degrees relative to the edge at the anchor.
     */
    public void setDisplacementAngle(int offsetAngle) {
        angle = offsetAngle * Math.PI / 180.0;
        useAngle = true;
    }

    /**
     * @param offsetAngle the new angle for the displacement vector,
     * specified in degrees relative to the edge at the anchor.
     */
    public void setDisplacementAngle(double offsetAngle) {
        angle = offsetAngle * Math.PI / 180.0;
        useAngle = true;
    }

    /**
     * Set distance along displacement vector to place the figure.
     * @param newDistance distance in units of the drawing coordinate system
     */
    public void setDisplacementDistance(int newDistance) {
        vectorOffset = newDistance;
        useAngle = true;
    }


    /**
     * Don't know what this is supposed to do since GEF has no API spec for it,
     * but we don't implement it and it'll throw an
     * UnsupportedOperationException if you try to use it.
     *
     * @param newPoint ignored
     * @see org.tigris.gef.base.PathConv#setClosestPoint(java.awt.Point)
     */
    public void setClosestPoint(Point newPoint) {
        throw new UnsupportedOperationException();
    }


    /**
     * Compute slope of path at the anchor point.  Slope is computed using a
     * short segment instead of using the instantaneous slope, so it will give
     * unusual results near discontinuities in the path (ie bends).
     * @return the slope radians in the range 0 < slope < 2PI
     */
    private double getSlope() {

        final int slopeSegLen = 40; // segment size for computing slope

        int pathLength = _pathFigure.getPerimeterLength();
        int pathDistance = getPathDistance();

        // Two points for line segment used to compute slope of path here
        // NOTE that this is the average slope, not instantaneous, so it will
        // give screwy results near bends in the path
        int d1 = Math.max(0, pathDistance - slopeSegLen / 2);
        // If our position was clamped, try to make it up on the other end
        int d2 = Math.min(pathLength - 1, d1 + slopeSegLen);
        // Can't get the slope of a point.  Just return an arbitrary point.
        if (d1 == d2) {
            return 0;
        }
        Point p1 = _pathFigure.pointAlongPerimeter(d1);
        Point p2 = _pathFigure.pointAlongPerimeter(d2);

        double theta = getSlope(p1, p2);
        return theta;
    }


    /**
     * Compute the slope in radians of the line between two points.
     * @param p1 first point
     * @param p2 second point
     * @return slope in radians in the range 0<=slope<=2PI
     */
    private static double getSlope(Point p1, Point p2) {
        // Our angle theta is arctan(opposite/adjacent)
        // Because y increases going down the screen, positive angles are
        // clockwise rather than counterclockwise
        int opposite = p2.y - p1.y;
        int adjacent = p2.x - p1.x;
        double theta;
        if (adjacent == 0) {
            // This shouldn't happen, because of our line segment size check
            if (opposite == 0) {
                return 0;
            }
            // "We're going vertical!" - Goose in "Top Gun"
            if (opposite < 0) {
                theta = Math.PI * 3 / 2;
            } else {
                theta = Math.PI / 2;
            }
        } else {
            // Arctan only returns -PI/2 to PI/2
            // Handle the other two quadrants and normalize to 0 - 2PI
            theta = Math.atan((double) opposite / (double) adjacent);
            // Quadrant II & III
            if (adjacent < 0) {
                theta += Math.PI;
            }
            // Quadrant IV
            if (theta < 0) {
                theta += Math.PI * 2;
            }
        }
        return theta;
    }

    /**
     * Apply an offset for a given distance along the normal vector computed
     * to the line specified by the two points.
     *
     * @param p1 point one of line to use in computing normal vector
     * @param p2 point two of line to use in computing normal vector
     * @param theOffset distance to displace fig along normal vector
     * @param anchor The start point to apply the offset from.  Not modified.
     * @return A new computed point describing the location after the offset
     * has been applied to the anchor.
     */
    private Point applyOffset(double theta, int theOffset,
            Point anchor) {

        Point result = new Point(anchor);

        // Set the following for some backward compatibility with old algorithm
        final boolean aboveAndRight = false;

//        LOG.log(Level.FINE, "Slope = " + theta / Math.PI + "PI "
//                + theta / Math.PI * 180.0);

        // Add displacement angle to slope
        if (swap && theta > Math.PI / 2 && theta < Math.PI * 3 / 2) {
            theta = theta - angle;
        } else {
            theta = theta + angle;
        }

        // Transform to 0 - 2PI range if we've gone all the way around circle
        if (theta > Math.PI * 2) {
            theta -= Math.PI * 2;
        }
        if (theta < 0) {
            theta += Math.PI * 2;
        }

        // Compute our deltas
        int dx = (int) (theOffset * Math.cos(theta));
        int dy = (int) (theOffset * Math.sin(theta));

        // For backward compatibility everything is above and right
        // TODO: Do in polar domain?
        if (aboveAndRight) {
            dx = Math.abs(dx);
            dy = -Math.abs(dy);
        }

        result.x += dx;
        result.y += dy;

//        LOG.log(Level.FINE,result.x + ", " + result.y
//                + " theta = " + theta * 180 / Math.PI
//                + " dx = " + dx + " dy = " + dy);

        return result;
    }

    /**
     * Paint the virtual connection from the edge to where the path item
     * is placed according to this path item placement algorithm.
     *
     * @param g the Graphics object
     * @see org.tigris.gef.base.PathConv#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        final Point p1 = getAnchorPosition();
        Point p2 = getPoint();
        Rectangle r = itemFig.getBounds();
        // Load the standard colour, just add an alpha channel.
        Color c = Globals.getPrefs().handleColorFor(itemFig);
        c = new Color(c.getRed(), c.getGreen(), c.getBlue(), 100);
        g.setColor(c);
        r.grow(2, 2);
        g.fillRoundRect(r.x, r.y, r.width, r.height, 8, 8);
        if (r.contains(p2)) {
            p2 = getRectLineIntersection(r, p1, p2);
        }
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    /**
     * Finds the point where a rectangle and line intersect.
     * Finds the intersection point between the border of a Rectangle r and
     * a line drawn between two Points pOut (outside the rectangle) and pIn
     * (inside the rectangle).
     * If the pIn is not inside the rectangle, or if any other problem occurs,
     * pIn is returned.
     * @param r Rectangle to find the intersection of.
     * @param pOut Point outside the rectangle.
     * @param pIn Point inside the rectangle.
     * @return The intersection between Line(pOut, pIn) and Rectangle r.
     */
    private Point getRectLineIntersection(Rectangle r, Point pOut, Point pIn) {
        Line2D.Double m, n;
        m = new Line2D.Double(pOut, pIn);
        n = new Line2D.Double(r.x, r.y, r.x + r.width, r.y);
        if (m.intersectsLine(n)) {
            return intersection(m, n);
        }
        n = new Line2D.Double(r.x + r.width, r.y, r.x + r.width,
                r.y + r.height);
        if (m.intersectsLine(n)) {
            return intersection(m, n);
        }
        n = new Line2D.Double(r.x, r.y + r.height, r.x + r.width,
                r.y + r.height);
        if (m.intersectsLine(n)) {
            return intersection(m, n);
        }
        n = new Line2D.Double(r.x, r.y, r.x, r.y + r.width);
        if (m.intersectsLine(n)) {
            return intersection(m, n);
        }
        // Should never get here.  If we do, return the inner point.
        LOG.log(Level.WARNING, "Could not find rectangle intersection, using inner point.");
        return pIn;
    }

    /**
     * Finds the intersection point of two lines.
     * It is surprising that this method isn't already available in the base
     * Line2D class of Java.  If a stock method exists or is implemented in
     * future, feel free replace this code with it.
     * @param m First line.
     * @param n Second line.
     * @return Intersection point of first and second line.
     */
    private Point intersection(Line2D m, Line2D n) {
        double d = (n.getY2() - n.getY1()) * (m.getX2() - m.getX1())
                - (n.getX2() - n.getX1()) * (m.getY2() - m.getY1());
        double a = (n.getX2() - n.getX1()) * (m.getY1() - n.getY1())
                - (n.getY2() - n.getY1()) * (m.getX1() - n.getX1());

        double as = a / d;

        double x = m.getX1() + as * (m.getX2() - m.getX1());
        double y = m.getY1() + as * (m.getY2() - m.getY1());
        return new Point((int) x, (int) y);
    }

    /**
     * Returns the value of the percent field - the position of the anchor
     * point as a percentage of the edge.
     * @important Used by PGML.tee.
     * @return The value of the percent field.
     */
    public int getPercent() {
        return percent;
    }

    /**
     * Returns the value of the angle field converted to degrees.
     * The angle of the path item relative to the edge.
     * @important Used by PGML.tee.
     * @return The value of the angle field in degrees.
     */
    public double getAngle() {
        return angle * 180 / Math.PI;
    }

    /**
     * Returns the value of the vectorOffset field.
     * The vectorOffset field is the distance away from the edge, along the
     * path vector that the item Fig is placed.
     * @important Used by PGML.tee.
     * @return The value of the vectorOffset field.
     */
    public int getVectorOffset() {
        return vectorOffset;
    }
    /** End of methods used by PGML.tee */

}
