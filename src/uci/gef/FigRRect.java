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

// File: FigRRect.java
// Classes: FigRRect
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;
import uci.util.*;
import uci.ui.*;

/** Primitive Fig to paint rounded rectangles on a LayerDiagram.
 *  <A HREF="../features.html#basic_shapes_rounded_rect">
 *  <TT>FEATURE: basic_shapes_rounded_rect</TT></A>
 */

public class FigRRect extends FigRect {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected int _radius = 16;

  ////////////////////////////////////////////////////////////////
  // static initializer
  static {
    PropCategoryManager.categorizeProperty("Geometry", "cornerRadius");
  }

  ////////////////////////////////////////////////////////////////
  // constructors

//   public FigRRect(int x, int y, int w, int h, Color lineColor, int r) {
//     super(x, y, w, h, lineColor);
//   }

  /** Construct a new FigRRect w/ the given position, size, and attributes */
  public FigRRect(int x, int y, int w, int h, Hashtable gAttrs) {
    super(x, y, w, h, gAttrs);
  }

  /** Construct a new FigRRect w/ the given position, size, line color,
   * and fill color*/
  public FigRRect(int x, int y, int w, int h,Color lineColor,Color fillColor) {
    super(x, y, w, h, lineColor, fillColor);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setCornerRadius(int r) { _radius = r; }
  public int getCornerRadius() { return _radius; }


  ////////////////////////////////////////////////////////////////
  /// painting methods

  /** Paint this FigRRect */
  public void paint(Graphics g) {

    if (_filled && _fillColor != null) {
      g.setColor(_fillColor);
      g.fillRoundRect(_x, _y, _w, _h, _radius, _radius);
    }
    if (_lineWidth > 0  && _lineColor != null) {
      g.setColor(_lineColor);
      g.drawRoundRect(_x, _y, _w - _lineWidth, _h - _lineWidth,
		      _radius, _radius);
    }
  }

} /* end class FigRRect */

