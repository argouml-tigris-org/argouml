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

// File: FigRect.java
// Classes: FigRect
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/** Primitive Fig to paint rectangles on a LayerDiagram.
 *
 *  <A HREF="../features.html#basic_shapes_rect">
 *  <TT>FEATURE: basic_shapes_rect</TT></A>.
 */

public class FigRect extends Fig {

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new FigRect w/ the given position, size, and attributes. */
  public FigRect(int x, int y, int w, int h, Hashtable gAttrs){
    super(x, y, w, h);
    //put(gAttrs);
  }

  /** Construct a new FigRect w/ the given position, size, line color,
   *  and fill color.*/
  public FigRect(int x, int y, int w, int h, Color lColor, Color fColor) {
    super(x, y, w, h, lColor, fColor);
  }

  ////////////////////////////////////////////////////////////////
  // Fig methods

  public Point connectionPoint(Point anotherPt) {
    return Geometry.ptClosestTo(getBounds(), anotherPt);
  }

  
  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Paint this FigRect */
  public void paint(Graphics g) {
    if (_filled && _fillColor != null) {
      g.setColor(_fillColor);
      g.fillRect(_x, _y, _w, _h);
    }
    if (_lineWidth > 0 && _lineColor != null) {
      g.setColor(_lineColor);
      g.drawRect(_x, _y, _w - _lineWidth, _h - _lineWidth);
    }
  }

} /* end class FigRect */

