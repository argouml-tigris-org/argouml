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

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/** Draws a triangluar arrow head at the end of a FigEdge */

public class ArrowHeadHalfTriangle extends ArrowHead {

  public void paint(Graphics g, Point start, Point end) {
    int    xFrom, xTo, yFrom, yTo;
    double denom, x, y, dx, dy, cos, sin;
    Polygon triangle;

    xFrom  = start.x;
    xTo   = end.x;
    yFrom  = start.y;
    yTo   = end.y;

    dx   	= (double)(xTo - xFrom);
    dy   	= (double)(yTo - yFrom);
    denom 	= dist(dx, dy);
    if (denom == 0) return;

    cos = arrow_height/denom;
    sin = arrow_width /denom;
    x   = xTo - cos*dx;
    y   = yTo - cos*dy;
    int x1  = (int)(x - sin*dy);
    int y1  = (int)(y + sin*dx);
    int x2  = (int)(x + sin*dy);
    int y2  = (int)(y - sin*dx);

    triangle = new Polygon();
    triangle.addPoint(xTo, yTo);
    triangle.addPoint(xFrom, yFrom);
    triangle.addPoint(x2, y2);

    g.setColor(arrowFillColor);
    g.fillPolygon(triangle);
    g.setColor(arrowLineColor);
    g.drawPolygon(triangle);
  }

  static final long serialVersionUID = -7257932581787201038L;

} /* end class ArrowHeadHalfTriangle */
