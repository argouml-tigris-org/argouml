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

// File: PathConvPercent.java
// Classes: PathConvPercent
// Original Author: abonner@ics.uci.edu
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/** Used to place labels as specific positions along a FigEdge.  For
 *  example, a label can be placed in the middle of a FigEdge by using 50%. */

public class PathConvPercent extends PathConv {
  int percent = 0;
  int offset = 0;

  public PathConvPercent(Fig theFig, int newPercent, int newOffset) {
    super(theFig);
    setPercentOffset(newPercent, newOffset);
  }

  public void stuffPoint(Point res) {
    int figLength = _pathFigure.getPerimeterLength();
    if (figLength < 10) { res.setLocation(_pathFigure.getLocation()); return; }
    int pointToGet = (figLength * percent) / 100;

    _pathFigure.stuffPointAlongPerimeter(pointToGet, res);

    //System.out.println("lP=" + linePoint + " ptG=" + pointToGet +
    //" figLen=" + figLength);

    applyOffsetAmount(_pathFigure.pointAlongPerimeter(pointToGet + 5),
		      _pathFigure.pointAlongPerimeter(pointToGet - 5),
		      offset,
		      res);
  }

  public void setPercentOffset(int newPercent, int newOffset) {
    percent = newPercent;
    offset = newOffset;
  }

  public void setClosestPoint(Point newPoint) { }

}/* end class PathConvPercent */
