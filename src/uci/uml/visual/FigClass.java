// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
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



// File: FigClass.java
// Classes: FigClass
// Original Author: abonner
// $Id$

package uci.uml.visual;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import com.sun.java.swing.*;
import com.sun.java.swing.plaf.metal.MetalLookAndFeel;

import uci.gef.*;
import uci.graph.*;
import uci.uml.ui.*;
import uci.uml.generate.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** Class to display graphics for a UML Class in a diagram. */

public class FigClass extends FigNode
implements VetoableChangeListener, DelayedVetoableChangeListener,
  MouseListener, KeyListener, PropertyChangeListener  {

  public final int MARGIN = 2;
  
  protected FigText _clss;
  protected FigText _attr;
  protected FigText _oper;
  protected FigRect _bigPort;
  
  public FigClass(GraphModel gm, Object node) {
    super(node);
    if (node instanceof ElementImpl)
      ((ElementImpl)node).addVetoableChangeListener(this);

    Font labelFont = MetalLookAndFeel.getSubTextFont();

    Color handleColor = Globals.getPrefs().getHandleColor();
    _bigPort = new FigRect(8, 8, 91, 61, handleColor, Color.lightGray);
    _clss = new FigText(10, 10, 90, 20);
    _clss.setFont(labelFont);
    _clss.setTextColor(Color.black);
    _clss.setExpandOnly(true);
    _clss.setMultiLine(false);
    //_clss.setText((new GeneratorDisplay()).generateClassifierRef((Classifier)node));

    _attr = new FigText(10, 30, 90, 20);
    _attr.setFont(labelFont);
    _attr.setTextColor(Color.black);
    _attr.setExpandOnly(true);
    _attr.setJustification("Left");

    _oper = new FigText(10, 50, 90, 20);
    _oper.setFont(labelFont);
    _oper.setTextColor(Color.black);
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

    _clss.addPropertyChangeListener(this);
    _attr.addPropertyChangeListener(this);
    _oper.addPropertyChangeListener(this);
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
    //System.out.println("FigClass got a change notification!");
    Object src = pce.getSource();
    if (src == getOwner()) {
      DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
      SwingUtilities.invokeLater(delayedNotify);
    }
  }

  public void delayedVetoableChange(PropertyChangeEvent pce) {
    // throws PropertyVetoException 
    //System.out.println("FigClass got a delayed change notification!");
    Object src = pce.getSource();
    updateText();
  }
    
  public void propertyChange(PropertyChangeEvent pve) {
    Object src = pve.getSource();
    String pName = pve.getPropertyName();
    if (pName.equals("editing") && Boolean.FALSE.equals(pve.getNewValue())) {
      //System.out.println("finished editing");
      Classifier cls = (Classifier) getOwner();
      try {
	if (src == _clss) { cls.setName(new Name(_clss.getText())); }
	if (src == _attr) { System.out.println("attr changed " + pName);}
	if (src == _oper) { System.out.println("oper changed " + pName);}
      }
      catch (PropertyVetoException ex) {
	System.out.println("could not parse and use the text you entered");
      }
    }
    else super.propertyChange(pve);
  }

  ////////////////////////////////////////////////////////////////
  // event handlers - MouseListener implementation

  public void mouseClicked(MouseEvent me) {
    if (me.isConsumed()) return;
    if (me.getClickCount() >= 2) {
      if (_clss.contains(me.getX(), me.getY())) _clss.mouseClicked(me);
      if (_attr.contains(me.getX(), me.getY())) _attr.mouseClicked(me);
      if (_oper.contains(me.getX(), me.getY())) _oper.mouseClicked(me);
    }
    me.consume();
  }

  public void keyPressed(KeyEvent ke) { }

  public void keyReleased(KeyEvent ke) { }

  public void keyTyped(KeyEvent ke) {
    if (!ke.isConsumed()) {
      _clss.keyTyped(ke);
      ke.consume();
      Classifier cls = (Classifier) getOwner();
      try {
	cls.setName(new Name(_clss.getText()));
      }
      catch (PropertyVetoException pve) { }
    }
  }
    
  ////////////////////////////////////////////////////////////////
  // internal methods
  
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

    if (cls.getIsAbstract()) {
        Font italicFont = MetalLookAndFeel.getSubTextFont();
        italicFont = new Font(italicFont.getFamily(),
			      Font.ITALIC, italicFont.getSize());
        _clss.setFont(italicFont);
    }
    else {
        Font labelFont = MetalLookAndFeel.getSubTextFont();
        _clss.setFont(labelFont);
    }
    
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


  
} /* end class FigClass */
