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

// File: FigInk.java
// Classes: FigInk
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/** Primitive Fig to paint Ink on a LayerDiagram. Ink is like an open
 *  polygon with no fill. The main difference between FigInk and
 *  FigPoly is in the way that they are created.
 *  <A HREF="../features.html#basic_shapes_ink">
 *  <TT>FEATURE: basic_shapes_ink</TT></A>
 *
 * @see FigPoly
 * @see ModeCreateFigInk */

public class FigInk extends FigPoly {

//   /** Construct a new FigInk w/ the given line color,
//    *  and fill color. */
//   public FigInk(Color lineColor, Color fillColor) {
//     super(lineColor, fillColor);
//     _filled = false;
//   }

//   /** Construct a new FigInk w/ the given line color. */
//   public FigInk(Color lineColor) {
//     super(lineColor);
//     _filled = false;
//   }

  /** Construct a new FigInk w/ the given attributes. */
  public FigInk(Hashtable gAttrs) { super(gAttrs); _filled = false;}

  /** Construct a new FigInk w/ the given point and attributes. */
  public FigInk(int x, int y, Hashtable gAttrs) {
    super(x, y, gAttrs);
    _filled = false;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Line width of ink must be at least 1. Since Java does not support
   *  thicker lines, it will always be 1. */
  public void setLineWidth(int w) { _lineWidth = 1; }

  /** FigInks can never be filled, do nothing. */
  public void setFilled(boolean f) { _filled = false; }

  /** FigInks can never be rectilinear, do nothing. */
  public void rectilinear(boolean r) { _rectilinear = false; }

  ////////////////////////////////////////////////////////////////
  // painting methods

  // needs-more-work: could this be eliminated? see FigPoly.paint()
  public void paint(Graphics g) {
    // FigInk's are never filled
    if (_lineWidth > 0) {
      g.setColor(_lineColor);
      g.drawPolyline(_xpoints, _ypoints, _npoints);
    }
  }


  public boolean contains(int x, int y) {
    return super.findHandle(x, y) != -1;
  }

} /* end class FigInk */

