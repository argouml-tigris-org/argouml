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




// File: SampleEdge.java
// Classes: SampleEdge
// Original Author: ics125b spring 1996
// $Id$

package uci.gef.demo;

import uci.gef.*;
import java.awt.*;

/** A sample NetEdge subclass for use in the demos.  This edge is
 *  drawn with an arrowhead. */

public class SampleEdge extends NetEdge {
  /** Construct a new SampleEdge. */
  public SampleEdge() { } /* needs-more-work */

  public FigEdge makePresentation(Layer lay) {
    FigEdge foo = new FigEdgeLine();
    foo.setSourceArrowHead(new ArrowHeadTriangle());


    FigText mid = new FigText(10, 30, 90, 20);
    mid.setText("Midpoint");
    mid.setTextColor(Color.black);
    mid.setTextFilled(false);
    mid.setFilled(false);
    mid.setLineWidth(0);
    foo.addPathItem(mid, new PathConvPercent(foo, 50, 10));

    FigText start = new FigText(10, 30, 90, 20);
    start.setText("Start");
    start.setTextColor(Color.black);
    start.setTextFilled(false);
    start.setFilled(false);
    start.setLineWidth(0);
    foo.addPathItem(start,
		    new PathConvPercentPlusConst(foo, 0, 10, 10));

    return foo;
  }

  static final long serialVersionUID = -3351193870220408015L;

} /* end class SampleEdge */
