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

// File: FigClass.java
// Classes: FigClass
// Original Author: abonner
// $Id$

package uci.uml.visual;

import java.awt.*;
import java.util.*;
import uci.util.*;
import uci.ui.*;
import uci.gef.*;
import uci.uml.*;

/** Class to display graphics for a UML Class in a diagram.
 *  <A HREF="../features.html#graph_visualization_nodes">
 *  <TT>FEATURE: graph_visualization_nodes</TT></A>
 *  <A HREF="../features.html#graph_visualization_ports">
 *  <TT>FEATURE: graph_visualization_ports</TT></A>
 */

public class FigClass extends FigNode {

  public FigClass()
  {
    super(new uci.gef.demo.SampleNode());
    setLocation(50,50);
    setSize(100,50);
    FigText foo = new FigText(0,0,20,10, Color.blue, "Times", 10);
    foo.setText("Class data");
    FigText xyzzy = new FigText(0,10,20,10, Color.blue, "Times", 10);
    xyzzy.setText("Attrib data");
    FigText bar = new FigText(0,20,20,10, Color.blue, "Times", 10);
    bar.setText("Function data");
    addFig(foo);
    addFig(xyzzy);
    addFig(bar);
  }

  public FigClass(NetNode nn, Vector figs) {
    super(nn, figs);
    setLocation(50,50);
    setSize(100,50);
    FigText foo = new FigText(0,0,20,10, Color.blue, "Times", 10);
    foo.setText("Class data");
    FigText xyzzy = new FigText(0,10,20,10, Color.blue, "Times", 10);
    xyzzy.setText("Attrib data");
    FigText bar = new FigText(0,20,20,10, Color.blue, "Times", 10);
    bar.setText("Function data");
    addFig(foo);
    addFig(xyzzy);
    addFig(bar);
    setFigs(figs);
  }


  /** Paints the FigClass to the given Graphics. */
  public void paint(Graphics g) {
    super.paint(g);
    g.drawRect(10,10,10,10);
    if (_highlight) {
      g.setColor(Globals.getPrefs().getHighlightColor()); /* needs-more-work */
      g.drawRect(_x - 3, _y - 3, _w + 6 - 1, _h + 6 - 1);
      g.drawRect(_x - 2, _y - 2, _w + 4 - 1, _h + 4 - 1);
    }
  }

} /* end class FigClass */

