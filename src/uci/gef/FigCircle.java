// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: FigCircle.java
// Classes: FigCircle
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/** Primitive Fig for displaying circles and ovals.
 *
 *  <A HREF="../features.html#basic_shapes_circle">
 *  <TT>FEATURE: basic_shapes_circle</TT></A>.
 */

public class FigCircle extends Fig {

  ////////////////////////////////////////////////////////////////
  // constants

  /** Used as a percentage tolerance for making it easier for the user
   *  to select a hollow circle with the mouse. Needs-More-Work: This
   *  is bad design that needs to be changed. Should use just
   *  GRIP_FACTOR. */
  public static final double CIRCLE_ADJUST_RADIUS = 0.1;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new FigCircle with the given position, size, and
   *  attributes. */
  public FigCircle(int x, int y, int w, int h, Hashtable gAttrs) {
    super(x, y, w, h);
    //put(gAttrs);
  }

  /** Construct a new FigCircle with the given position, size, line
   *  color, and fill color */

  public FigCircle(int x, int y, int w, int h, Color lColor, Color fColor) {
    super(x, y, w, h, lColor, fColor);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  ////////////////////////////////////////////////////////////////
  // display methods

  /** Draw this FigCircle. */
  public void paint(Graphics g) {

    /* See if this is a filled object */
    if (_filled && _fillColor != null) {
      g.setColor(_fillColor);
      g.fillOval(_x, _y, _w, _h);
    }
    if (_lineWidth > 0 && _lineColor != null) {
      g.setColor(_lineColor);
      g.drawOval(_x, _y, _w - _lineWidth, _h - _lineWidth);
    }
  }


  /** Reply true if the given mouse coordinates are "near" the
   * circle. Needs-More-Work: there should be separate contains() and
   * hit() functions. */
  public boolean contains(int x, int y) {
    if (!super.contains(x, y)) return false;
    double dx = ((double)(_x + _w/2 - x)) * 2 / _w;
    double dy = ((double)(_y + _h/2 - y)) * 2 / _h;
    double pnt_dist = Math.sqrt(dx * dx + dy * dy); // avoidable?
    double arc_dist_out = 1 + CIRCLE_ADJUST_RADIUS;
    return  ((pnt_dist < arc_dist_out) &&
	     (_filled || pnt_dist > arc_dist_out - CIRCLE_ADJUST_RADIUS));
  }


} /* end class FigCircle */


