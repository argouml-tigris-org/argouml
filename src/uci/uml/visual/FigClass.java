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
    setSize(100,70);
    FigText foo = new FigText(5,5,100,20, Color.blue, "Times", 10);
    foo.setText("Class data");
    FigText xyzzy = new FigText(5,25,100,20, Color.blue, "Times", 10);
    xyzzy.setText("Attrib data");
    FigText bar = new FigText(5,45,100,20, Color.blue, "Times", 10);
    bar.setText("Function data");
    addFig(foo);
    addFig(xyzzy);
    addFig(bar);
  }

  public FigClass(NetNode nn, Vector figs) {
    super(nn, figs);
    FigText foo = new FigText(5,5,100,20, Color.blue, "Times", 10);
    foo.setText("Class data");
    FigText xyzzy = new FigText(5,25,100,20, Color.blue, "Times", 10);
    xyzzy.setText("Attrib data");
    FigText bar = new FigText(5,45,100,20, Color.blue, "Times", 10);
    bar.setText("Function data");
    addFig(foo);
    addFig(xyzzy);
    addFig(bar);
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

  ////////////////////////////////////////////////////////////////
  // notifications and updates

//   public void update(Observable o, Object arg) {
//     super.update(o, arg);
//     Boolean abs = (Boolean) ((NodeUML)getOwner()).get(UML.pABSTRACT);
//     String attrs = (String) ((NodeUML)getOwner()).get(UML.pATTRS);
//     boolean showAttrs = (attrs.length() > 0 && _showAttrs);
//     startTrans();
//     if (arg.equals(UML.pABSTRACT)) {
//       _nameFig.setItalic(abs.booleanValue());
//     }
//     if (arg.equals(UML.pATTRS) || arg.equals(UML.pSHOW_ATTRS)) {
//       _attrsFig.setText(attrs);
//       Vector figs = getFigs();
//       if (!showAttrs) figs.removeElement(_attrsFig);
//       else if (!figs.contains(_attrsFig))
// 	figs.insertElementAt(_attrsFig, 1);
//     }
//     //GEF v06: _nameFig.measure();
//     //GEF v06: _attrsFig.measure();

//     Rectangle nameBBox = _nameFig.getBounds();
//     int origX = nameBBox.x;
//     int origY = nameBBox.y;
//     int nameWidth = nameBBox.width;
//     int nameHeight = nameBBox.height;
//     int attrsWidth = _attrsFig.getBounds().width;
//     int maxWidth = Math.max(nameWidth, attrsWidth);
//     if (showAttrs) {
//       _nameFig.setWidth(maxWidth);
//       _nameFig.setLocation(origX, origY);
//       _attrsFig.setWidth(maxWidth);
//       _attrsFig.setLocation(origX, origY + nameHeight);
//     }
//     Rectangle attrBBox = null;
//     if (showAttrs) attrBBox = _attrsFig.getBounds();
//     else attrBBox = _nameFig.getBounds();
//     Rectangle botArrowBBox = _botArrow.getBounds();
//     Rectangle topArrowBBox = _topArrow.getBounds();
//     _botArrow.translate(attrBBox.x + (attrBBox.width -
// 			 botArrowBBox.width) / 2 - botArrowBBox.x,
// 			attrBBox.y + attrBBox.height - botArrowBBox.y-
// 			botArrowBBox.height + 4);
//     _topArrow.translate(attrBBox.x + (attrBBox.width -
// 			 topArrowBBox.width) / 2 - topArrowBBox.x,
// 			0);
//     endTrans();
//   }

  
} /* end class FigClass */

