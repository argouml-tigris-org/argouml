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
import uci.argo.kernel.*;
import uci.uml.ui.*;
import uci.uml.generate.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** Class to display graphics for a UML Class in a diagram. */

public class FigClass extends FigNodeModelElement  {

  protected FigText _attr;
  protected FigText _oper;
  protected FigRect _bigPort;

  public FigClass(GraphModel gm, Object node) {
    super(gm, node);

    _bigPort = new FigRect(10, 10, 90, 60, Color.cyan, Color.cyan);

    _attr = new FigText(10, 30, 90, 20);
    _attr.setFont(LABEL_FONT);
    _attr.setExpandOnly(true);
    _attr.setTextColor(Color.black);
    _attr.setJustification(FigText.JUSTIFY_LEFT);

    _oper = new FigText(10, 50, 90, 20);
    _oper.setFont(LABEL_FONT);
    _oper.setExpandOnly(true);
    _oper.setTextColor(Color.black);
    _oper.setJustification(FigText.JUSTIFY_LEFT);

    addFig(_bigPort);
    addFig(_name);
    addFig(_attr);
    addFig(_oper);

    //Vector ports = gm.getPorts(node);
    //Object onlyPort = ports.firstElement();
    Object onlyPort = node; //this kngl should be in the GraphModel
    bindPort(onlyPort, _bigPort);
    Rectangle r = getBounds();
    setBounds(r.x, r.y, r.width, r.height);

//     _attr.addPropertyChangeListener(this);
//     _oper.addPropertyChangeListener(this);
  }

  public FigText getOperationFig() { return _oper; }
  public FigText getAttributeFig() { return _attr; }

  public Dimension getMinimumSize() {
    Dimension nameMin = _name.getMinimumSize();
    Dimension attrMin = _attr.getMinimumSize();
    Dimension operMin = _oper.getMinimumSize();
    int h = nameMin.height + attrMin.height + operMin.height;
    int w = Math.max(Math.max(nameMin.width, attrMin.width), operMin.width);
    return new Dimension(w, h);
  }
  
  /* Override setBounds to keep shapes looking right */
  public void setBounds(int x, int y, int w, int h) {
    if (_name == null) return;
//     int leftSide = x;
//     int widthP = w;
//     int topSide = y;
//     int heightP = h;

    Dimension nameMin = _name.getMinimumSize();
    Dimension attrMin = _attr.getMinimumSize();
    Dimension operMin = _oper.getMinimumSize();

    int extra_each = (h - nameMin.height - attrMin.height - operMin.height) / 2;

    _name.setBounds(x, y, w, nameMin.height);
    _attr.setBounds(x, y + _name.getBounds().height,
		    w, attrMin.height + extra_each);
    _oper.setBounds(x, y + _attr.getBounds().height + _name.getBounds().height,
		    w, operMin.height + extra_each);
    _bigPort.setBounds(x+1, y+1, w-2, h-2);

    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
    updateEdges();
  }

  ////////////////////////////////////////////////////////////////
  // internal methods

  protected void textEdited(FigText ft) throws PropertyVetoException {
    super.textEdited(ft);
    Classifier cls = (Classifier) getOwner();
    if (ft == _attr) { 
      //System.out.println("\n\n\n Edited Attr");
      String s = ft.getText();
      ParserDisplay.SINGLETON.parseAttributeCompartment(cls, s);
    }
    if (ft == _oper) {
      String s = ft.getText();
      ParserDisplay.SINGLETON.parseOperationCompartment(cls, s);
    }
  }

  
  
  protected void modelChanged() {
    super.modelChanged();
    Classifier cls = (Classifier) getOwner();
    if (cls == null) return;
    //    String clsNameStr = GeneratorDisplay.Generate(cls.getName());
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

    _attr.setText(attrStr);
    _oper.setText(operStr);

    if (cls.getIsAbstract()) {
        _name.setFont(ITALIC_LABEL_FONT);
    }
    else {
        _name.setFont(LABEL_FONT);
    }

  }



//   public void dispose() {
//     if (!(getOwner() instanceof Classifier)) return;
//     Classifier cls = (Classifier) getOwner();
//     Project p = ProjectBrowser.TheInstance.getProject();
//     p.moveToTrash(cls);
//     super.dispose();
//   }


  
} /* end class FigClass */


