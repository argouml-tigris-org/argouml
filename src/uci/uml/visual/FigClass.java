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

  FigText _class, _attrib, _oper;

  public FigClass()
  {
    super(new uci.gef.demo.SampleNode());
    setLocation(50,50);
    setSize(100,70);
  }

  public FigClass(NetNode nn, Vector figs) {
    super(nn, figs);
    _class = new FigText(10,10,90,20, Color.blue, "Times", 10);
    _class.setExpandOnly(true);
    _class.setText("Class data");
    _attrib = new FigText(10,30,90,20, Color.blue, "Times", 10);
    _attrib.setExpandOnly(true);
    _attrib.setText("Attrib data");
    _attrib.setAlignment("Left");
    _oper = new FigText(10,50,90,20, Color.blue, "Times", 10);
    _oper.setExpandOnly(true);
    _oper.setText("Function data");
    _oper.setAlignment("Left");
    addFig(_class);
    addFig(_attrib);
    addFig(_oper);
    setBlinkPorts(true); 
  }


  /** Paints the FigClass to the given Graphics. */
  public void paint(Graphics g) {
    super.paint(g);
    if (_highlight) {
      g.setColor(Globals.getPrefs().getHighlightColor()); /* needs-more-work */
      g.drawRect(_x - 3, _y - 3, _w + 6 - 1, _h + 6 - 1);
      g.drawRect(_x - 2, _y - 2, _w + 4 - 1, _h + 4 - 1);
    }
  }

  /* Override setBounds to keep shapes looking right */
  public void setBounds(int x, int y, int w, int h) {
    Fig bigPort = (Fig) getPortFigs().firstElement();

    int leftSide = x+10;
    int widthP = w-10;
    int topSide = y+10;
    int heightP = h-10;

    _class.setBounds(leftSide, topSide, widthP, (heightP/3));
    _attrib.setBounds(leftSide, topSide + (heightP/3), widthP, (heightP/3));
    _oper.setBounds(leftSide, topSide + (heightP/3) * 2, widthP, (heightP/3));
    bigPort.setBounds(x,y,w+10,h+10);

    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
  }

 
  /*
  public NetPort hitPort(int x, int y) {
    return (NetPort) getPortFigs().firstElement().getOwner();
  }
  */


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

