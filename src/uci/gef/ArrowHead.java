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

// File: ArrowHead.java
// Classes: ArrowHead
// Original Author: abonner@ics.uci.edu
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;


/** Abstract class to draw arrow heads on the ends of FigEdges. */

public abstract class ArrowHead implements java.io.Serializable {
  protected int arrow_width = 7, arrow_height = 12;
  protected Color arrowLineColor = Color.black;
  protected Color arrowFillColor = Color.black;

  public ArrowHead() { }
  public ArrowHead(Color line, Color fill) {
    setLineColor(line);
    setFillColor(fill);
  }

  public Color getLineColor() {
    return arrowLineColor;
  }

  public void setLineColor(Color newColor) {
    arrowLineColor = newColor;
  }

  public Color getFillColor() {
    return arrowFillColor;
  }

  public void setFillColor(Color newColor) {
    arrowFillColor = newColor;
  }

  public abstract void paint(Graphics g, Point start, Point end);

  public void paintAtHead(Graphics g, Fig path) {
    paint(g, path.pointAlongPerimeter(5), path.pointAlongPerimeter(0));
  }

  public void paintAtTail(Graphics g, Fig path) {
    int len = path.getPerimeterLength();
    paint(g, path.pointAlongPerimeter(len - 6),
	  path.pointAlongPerimeter(len - 1));
  }

  /** return the approximate arc length of the path in pixel units */
  public int getLineLength(Point one, Point two) {
    int dxdx = (two.x - one.x) * (two.x - one.x);
    int dydy = (two.y - one.y) * (two.y - one.y);
    //System.out.println("    ! pall dx, dy = " + dxdx + " , " + dydy);
    return (int) Math.sqrt(dxdx + dydy);
  }

  /** return a point that is dist pixels along the path */
  public Point pointAlongLine(Point one, Point two, int dist) {
    int len = getLineLength(one, two);
    int p = dist;
    if (len == 0) return one;
    //System.out.println("    ! pall dist, len = " + dist + " , " + len);
    return new Point(one.x + ((two.x - one.x) * p) / len,
		     one.y + ((two.y - one.y) * p) / len);
  }

  public double dist(int x0, int y0, int x1, int y1) {
    double dx, dy;
    dx = (double)(x0-x1);
    dy = (double)(y0-y1);
    return Math.sqrt(dx*dx+dy*dy);
  }

  public double dist(double dx, double dy) {
    return Math.sqrt(dx*dx+dy*dy);
  }

  static final long serialVersionUID = 4150958987048733172L;
} /* end class ArrowHead */
