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



// File: PathConv.java
// Classes: PathConv
// Original Author: abonner@ics.uci.edu
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/** Abstract class that defines a common interface to all of
 *  path-to-coord mapping objects.  These objects store some
 *  information about a point that is defined relative to a path
 *  (e.g., along a FigEdge) and provide methods to get the
 *  coordinates (x, y) for that point.  This allows us to place labels
 *  along a FigEdge and have the label stay in the right place, even
 *  if the FigEdge moves. */

public abstract class PathConv implements java.io.Serializable {
  protected Fig _pathFigure; 	// The intermediate path figure

  public PathConv(Fig theFig) {
    _pathFigure = theFig;
  }

  public Point getPoint() {
    Point res = new Point();
    stuffPoint(res);
    return res;
  }
  abstract public void stuffPoint(Point res);
  abstract public void setClosestPoint(Point newPoint);

  protected Point getOffsetAmount(Point p1, Point p2, int offset) {
    Point res = new Point(0, 0);
    applyOffsetAmount(p1, p2, offset, res);
    return res;
  }

  protected void applyOffsetAmount(Point p1, Point p2, int offset,
				   Point res) {
    // slope of the line we're finding the normal to
    // is slope, and the normal is the negative reciprocal
    // slope is (p1.y - p2.y) / (p1.x - p2.x)
    // so recip is - (p1.x - p2.x) / (p1.y - p2.y)
    int recipnumerator = (p1.x - p2.x) * -1;
    int recipdenominator = (p1.y - p2.y);

    if (recipdenominator == 0 && recipnumerator == 0) return;
    // find the point offset on the line that gives a 
    // correct offset

    double len = Math.sqrt(recipnumerator * recipnumerator +
			   recipdenominator * recipdenominator);
    int dx = (int) ((recipdenominator * offset) / len);
    int dy = (int) ((recipnumerator * offset) / len);
//     if (dx > 10000 || dy > 10000) {
//       System.out.println("p1=" + p1 + " p2=" + p2);
//       System.out.println("offset=" + offset);
//       System.out.println("dx=" + dx + " dy=" + dy);
//     }
    res.x += dx;
    res.y += dy;

  }

  static final long serialVersionUID = -2090491388402219290L;
} /* end class PathConv */
