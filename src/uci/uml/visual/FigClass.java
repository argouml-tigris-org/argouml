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

import uci.gef.*;
import uci.graph.*;
import uci.uml.generate.*;
import uci.uml.Foundation.Core.*;

/** Class to display graphics for a UML Class in a diagram.
 *  <A HREF="../features.html#graph_visualization_nodes">
 *  <TT>FEATURE: graph_visualization_nodes</TT></A>
 *  <A HREF="../features.html#graph_visualization_ports">
 *  <TT>FEATURE: graph_visualization_ports</TT></A>
 */

public class FigClass extends FigNode {

  public final int MARGIN = 2;
  
  FigText _clss;
  FigText _attr;
  FigText _oper;
  FigRect _bigPort;
  
  public FigClass(GraphModel gm, Object node) {
    super(node);
    Color handleColor = Globals.getPrefs().getHandleColor();
    _bigPort = new FigRect(8, 8, 92, 62, handleColor, Color.lightGray);
    _clss = new FigText(10,10,90,20, Color.blue, "Times", 10);
    _clss.setExpandOnly(true);
    //_clss.setText((new GeneratorDisplay()).generateClassifierRef((Classifier)node));
    _clss.setText("class foo");
    _attr = new FigText(10,30,90,20, Color.blue, "Times", 10);
    _attr.setExpandOnly(true);
    _attr.setText("int bob;\nint bar;\nint adfy;");
    _attr.setAlignment("Left");
    _oper = new FigText(10,50,90,20, Color.blue, "Times", 10);
    _oper.setExpandOnly(true);
    _oper.setText("int test(double xyzyzyxyzyyyxzyxyzyxyzyyzx, double big2);\n");
    _oper.setAlignment("Left");
    addFig(_bigPort);
    addFig(_clss);
    addFig(_attr);
    addFig(_oper);
    //Vector ports = gm.getPorts(node);
    //Object onlyPort = ports.firstElement();
    Object onlyPort = node; //this kngl should be in the GraphModel
    bindPort(onlyPort, _bigPort);
    setBlinkPorts(true); 
    Rectangle r = getBounds();  
    setBounds(r.x, r.y, r.width, r.height);
  }

  /* Override setBounds to keep shapes looking right */
  public void setBounds(int x, int y, int w, int h) {
    if (_clss == null) return;
    int leftSide = x;
    int widthP = w;
    int topSide = y;
    int heightP = h;

    Rectangle _clss_pref = _clss.getBounds();
    Rectangle _attr_pref = _attr.getBounds();
    Rectangle _oper_pref = _oper.getBounds();

    int combined_height = _clss_pref.height + _attr_pref.height + _oper_pref.height;

    widthP = Math.max(widthP, Math.max(_clss_pref.width, Math.max(_attr_pref.width, _oper_pref.width)));
    heightP = Math.max(heightP, combined_height);
    
    int leftover_height = heightP - combined_height;

    Rectangle _clss_rect = new Rectangle(leftSide, topSide, widthP, _clss_pref.height + leftover_height/3);
    Rectangle _attr_rect = new Rectangle(leftSide, topSide + _clss_rect.height, widthP, _attr_pref.height + leftover_height/3);
    Rectangle _oper_rect = new Rectangle(leftSide, topSide + _clss_rect.height + _attr_rect.height, widthP, _oper_pref.height + leftover_height/3);

    _clss.setBounds(_clss_rect);
    _attr.setBounds(_attr_rect);
    _oper.setBounds(_oper_rect);
    _bigPort.setBounds(leftSide, topSide, widthP, heightP);

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

