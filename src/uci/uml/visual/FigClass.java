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
import java.beans.*;
import com.sun.java.swing.*;

import uci.gef.*;
import uci.graph.*;
import uci.uml.ui.*;
import uci.uml.generate.*;
import uci.uml.Foundation.Core.*;

/** Class to display graphics for a UML Class in a diagram.
 *  <A HREF="../features.html#graph_visualization_nodes">
 *  <TT>FEATURE: graph_visualization_nodes</TT></A>
 *  <A HREF="../features.html#graph_visualization_ports">
 *  <TT>FEATURE: graph_visualization_ports</TT></A>
 */

public class FigClass extends FigNode
implements VetoableChangeListener, DelayedVetoableChangeListener {

  public final int MARGIN = 2;
  
  FigText _clss;
  FigText _attr;
  FigText _oper;
  FigRect _bigPort;
  
  public FigClass(GraphModel gm, Object node) {
    super(node);
    if (node instanceof ElementImpl)
      ((ElementImpl)node).addVetoableChangeListener(this);

    Color handleColor = Globals.getPrefs().getHandleColor();
    _bigPort = new FigRect(8, 8, 92, 62, handleColor, Color.lightGray);
    _clss = new FigText(10,10,90,20, Color.blue, "Times", 10);
    _clss.setExpandOnly(true);
    //_clss.setText((new GeneratorDisplay()).generateClassifierRef((Classifier)node));
    _attr = new FigText(10,30,90,20, Color.blue, "Times", 10);
    _attr.setExpandOnly(true);
    _attr.setJustification("Left");
    _oper = new FigText(10,50,90,20, Color.blue, "Times", 10);
    _oper.setExpandOnly(true);
    _oper.setJustification("Left");
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

    int total_height = _clss_pref.height + _attr_pref.height + _oper_pref.height;

    widthP = Math.max(widthP, Math.max(_clss_pref.width, Math.max(_attr_pref.width, _oper_pref.width)));
    heightP = Math.max(heightP, total_height);

    int extra_each = (heightP - total_height) / 3;

    _clss.setBounds(leftSide, topSide, widthP, _clss_pref.height + extra_each);
    _attr.setBounds(leftSide, topSide + _clss.getBounds().height, widthP, _attr_pref.height + extra_each);
    _oper.setBounds(leftSide, topSide + _attr.getBounds().height + _clss.getBounds().height, widthP, _oper_pref.height + extra_each);
    _bigPort.setBounds(leftSide, topSide, widthP, heightP);

    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
  }

 
  public void vetoableChange(PropertyChangeEvent pce) {
    // throws PropertyVetoException 
    System.out.println("FigClass got a change notification!");
    Object src = pce.getSource();
    if (src == getOwner()) {
      DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
      SwingUtilities.invokeLater(delayedNotify);
    }
  }

  public void delayedVetoableChange(PropertyChangeEvent pce) {
    // throws PropertyVetoException 
    System.out.println("FigClass got a delayed change notification!");
    Object src = pce.getSource();
    if (src == getOwner()) updateText();
  }


  protected void updateText() {
    Classifier cls = (Classifier) getOwner();
    String clsNameStr = GeneratorDisplay.Generate(cls.getName());
    Vector strs = cls.getStructuralFeature();
    String attrStr = "";
    if (strs != null) {
      java.util.Enumeration enum = strs.elements();
      while (enum.hasMoreElements()) {
	StructuralFeature sf = (StructuralFeature) enum.nextElement();
	attrStr += GeneratorDisplay.Generate(sf);
	if (enum.hasMoreElements())
	  attrStr += "\n";
      }
    }
    Vector behs = cls.getBehavioralFeature();
    String operStr = "";
    if (behs != null) {
      java.util.Enumeration enum = behs.elements();
      while (enum.hasMoreElements()) {
	BehavioralFeature bf = (BehavioralFeature) enum.nextElement();
	operStr += GeneratorDisplay.Generate(bf);
	if (enum.hasMoreElements())
	  operStr += "\n";
      }
    }

    startTrans();
    _clss.setText(clsNameStr);
    _attr.setText(attrStr);
    _oper.setText(operStr);
    
    Rectangle bbox = getBounds();
    setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
    endTrans();
  }

  

  public void dispose() {
    if (!(getOwner() instanceof Classifier)) return;
    Classifier cls = (Classifier) getOwner();
    Project p = ProjectBrowser.TheInstance.getProject();
    p.moveToTrash(cls);
    super.dispose();
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
