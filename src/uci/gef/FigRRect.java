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



// File: FigRRect.java
// Classes: FigRRect
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;
import uci.util.*;
import uci.ui.*;

/** Primitive Fig to paint rounded rectangles on a LayerDiagram. */

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


  /** Construct a new FigRRect w/ the given position and size */
  public FigRRect(int x, int y, int w, int h) {
    super(x, y, w, h);
  }

  /** Construct a new FigRRect w/ the given position, size, line color,
   * and fill color*/
  public FigRRect(int x, int y, int w, int h,Color lineColor,Color fillColor) {
    super(x, y, w, h, lineColor, fillColor);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** get and set the "roundness" of the corners. */
  public int getCornerRadius() { return _radius; }
  public void setCornerRadius(int r) { _radius = r; }


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

  static final long serialVersionUID = 5242299525923672421L;

} /* end class FigRRect */

