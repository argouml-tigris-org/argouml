// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.

// File: FigEdgeLine.java
// Classes: FigEdgeLine
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;
import java.io.*;

/** An FigEdge that shows a straight line from the source port
 *  to the destination port. */

public class FigEdgeLine extends FigEdge {
  ////////////////////////////////////////////////////////////////
  // FigEdge API

  /** Instanciate a new FigLine as the contained Fig. By default it is
   *  black and the FigEdge has no ArrowHeads. */
  protected Fig makeEdgeFig() { return new FigLine(0, 0, 0, 0, Color.black); }

  /** Compute the shape of the line that presents an Edge. */
  protected void computeRoute() {
    Point srcPt = _sourcePortFig.center();
    Point dstPt = _destPortFig.center();

    if (_useNearest) {
      //? two iterations of refinement, maybe should be a for-loop
      srcPt = _sourcePortFig.connectionPoint(dstPt);
      dstPt = _destPortFig.connectionPoint(srcPt);      
      srcPt = _sourcePortFig.connectionPoint(dstPt);
      dstPt = _destPortFig.connectionPoint(srcPt);
    }

    ((FigLine) _fig).setShape(srcPt, dstPt);
    calcBounds();
  }


  public void paint(Graphics g) {
    super.paint(g);
    if (_highlight) {
      g.setColor(Globals.getPrefs().getHighlightColor()); /* needs-more-work */
      FigLine f = (FigLine) getFig();
      int x1 = f.getX1();
      int y1 = f.getY1();
      int x2 = f.getX2();
      int y2 = f.getY2();

      double dx = (double)(x2 - x1);
      double dy = (double)(y2 - y1);
      double denom = Math.sqrt(dx*dx+dy*dy);
      if (denom == 0) return;
      double orthoX =  dy / denom;
      double orthoY = -dx / denom;

      // needs-more-work: is 0.27 a good stepping rate?
      for (double i = 2.0; i < 5.0; i += 0.27) {
	int hx1  = (int)(x1 + i * orthoX);
	int hy1  = (int)(y1 + i * orthoY);
	int hx2  = (int)(x2 + i * orthoX);
	int hy2  = (int)(y2 + i * orthoY);
	g.drawLine(hx1, hy1, hx2, hy2);
      }

    }
  }
  
} /* end class FigEdgeLine */

