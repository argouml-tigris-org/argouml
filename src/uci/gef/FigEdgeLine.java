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

  protected Fig makeEdgeFig() { return new FigLine(0, 0, 0, 0, Color.black); }

  /** Compute the shape of the line that presents the Edge */
  protected void computeRoute() {
    Point srcPt = _sourcePortFig.center();
    Point dstPt = _destPortFig.center();
    ((FigLine) _fig).setShape(srcPt, dstPt);
    calcBounds();
  }

} /* end class FigEdgeLine */

