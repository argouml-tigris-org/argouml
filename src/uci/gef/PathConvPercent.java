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
    if (figLength < 10) { res.setLocation(_pathFigure.center()); return; }
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

  static final long serialVersionUID = -4842616124882977313L;
}/* end class PathConvPercent */
