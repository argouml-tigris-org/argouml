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



// File: FigCircle.java
// Classes: FigCircle
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/** Primitive Fig for displaying circles and ovals. */

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
  public FigCircle(int x, int y, int w, int h) {
    super(x, y, w, h);
  }

  /** Construct a new FigCircle with the given position, size, line
   *  color, and fill color */

  public FigCircle(int x, int y, int w, int h, Color lColor, Color fColor) {
    super(x, y, w, h, lColor, fColor);
  }

  ////////////////////////////////////////////////////////////////
  // display methods

  /** Draw this FigCircle. */
  public void paint(Graphics g) {
    if (_filled && _fillColor != null) {
      g.setColor(_fillColor);
      g.fillOval(_x, _y, _w, _h);
    }
    if (_lineWidth > 0 && _lineColor != null) {
      g.setColor(_lineColor);
      g.drawOval(_x, _y, _w - _lineWidth, _h - _lineWidth);
    }
  }

  /** Reply true if the given coordinates are inside the circle. */
  public boolean contains(int x, int y) {
    if (!super.contains(x, y)) return false;
    double dx = ((double)(_x + _w/2 - x)) * 2 / _w;
    double dy = ((double)(_y + _h/2 - y)) * 2 / _h;
    double distSquared = dx * dx + dy * dy;
    return distSquared <= 1.01;
  }

  static final long serialVersionUID = 4192370690690777913L;

} /* end class FigCircle */


