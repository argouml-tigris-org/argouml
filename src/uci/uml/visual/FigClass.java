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
import uci.ui.*;
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
    _clss.setText("class foo");
    _attr = new FigText(10,30,90,20, Color.blue, "Times", 10);
    _attr.setExpandOnly(true);
    _attr.setText("int bob;\nint bar;\nint adfy;");
    _attr.setJustification("Left");
    _oper = new FigText(10,50,90,20, Color.blue, "Times", 10);
    _oper.setExpandOnly(true);
    _oper.setText("int test(double xyzyzyxyzyyyxzyxyzyxyzyyzx, double big2);\n");
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



  
} /* end class FigClass */
