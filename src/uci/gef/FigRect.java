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




// File: FigRect.java
// Classes: FigRect
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/** Primitive Fig to paint rectangles on a LayerDiagram. */

public class FigRect extends Fig implements Serializable {

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new FigRect w/ the given position and size. */
  public FigRect(int x, int y, int w, int h){
    super(x, y, w, h);
  }

  /** Construct a new FigRect w/ the given position, size, line color,
   *  and fill color. */
  public FigRect(int x, int y, int w, int h, Color lColor, Color fColor) {
    super(x, y, w, h, lColor, fColor);
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
      if (!getDashed()) g.drawRect(_x, _y, _w - _lineWidth, _h - _lineWidth);
      else {
	int phase = 0;
	phase = drawDashedLine(g, phase, _x, _y, _x + _w, _y);
	phase = drawDashedLine(g, phase, _x + _w, _y, _x + _w, _y + _h);
	phase = drawDashedLine(g, phase, _x + _w, _y + _h, _x, _y + _h);
	phase = drawDashedLine(g, phase, _x, _y + _h, _x, _y);
      }
    }
  }

  static final long serialVersionUID = 769067862802923166L;

} /* end class FigRect */

