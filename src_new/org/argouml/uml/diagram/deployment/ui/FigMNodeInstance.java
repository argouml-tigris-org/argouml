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

// File: FigMNodeInstance.java
// Classes: FigMNodeInstance
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.diagram.deployment.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.*; 
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;

import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.generator.*;

/** Class to display graphics for a UML NodeInstance in a diagram. */

public class FigMNodeInstance extends FigNodeModelElement {


  ////////////////////////////////////////////////////////////////
  // instance variables

  protected FigRect _bigPort;
  protected FigCube _cover;
  protected FigRect _test;

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigMNodeInstance() {
    _bigPort = new FigRect(10, 10, 200, 180);
    _cover = new FigCube(10, 10, 200, 180, Color.black, Color.white);
    _test = new FigRect(10,10,1,1, Color.black, Color.white);

    _name.setLineWidth(0);
    _name.setFilled(false);
    _name.setJustification(0);
    _name.setUnderline(true);

    addFig(_bigPort);
    addFig(_cover);
    addFig(_name);
    addFig(_test);

    Rectangle r = getBounds();
    setBounds(r.x, r.y, r.width, r.height);
  }

  public FigMNodeInstance(GraphModel gm, Object node) {
    this();
    setOwner(node);
  }

  public String placeString() { return "new NodeInstance"; }

  public Object clone() {
    FigMNodeInstance figClone = (FigMNodeInstance) super.clone();
    Vector v = figClone.getFigs();
    figClone._bigPort = (FigRect) v.elementAt(0);
    figClone._cover = (FigCube) v.elementAt(1);
    figClone._name = (FigText) v.elementAt(2);
    figClone._test = (FigRect) v.elementAt(3);
    return figClone;
  }	

  ////////////////////////////////////////////////////////////////
  // acessors

  public void setLineColor(Color c) {
//     super.setLineColor(c);
     _cover.setLineColor(c);
     _name.setFilled(false);
     _name.setLineWidth(0);
     _test.setLineColor(c);
  }

  public Selection makeSelection() {
      return new SelectionNodeInstance(this);
  }

  public void setOwner(Object node) {
    super.setOwner(node);
    bindPort(node, _bigPort);
  }

  public Dimension getMinimumSize() {
    Dimension nameDim = _name.getMinimumSize();
    int w = nameDim.width + 20;
    int h = nameDim.height + 20;
    return new Dimension(w, h);
  }

  public void setBounds(int x, int y, int w, int h) {
    if (_name == null) return;

    Rectangle oldBounds = getBounds();
    _bigPort.setBounds(x, y, w, h);
    _cover.setBounds(x, y, w, h);
    Dimension nameDim = _name.getMinimumSize();
    _name.setBounds(x, y, w, nameDim.height);
    _x = x; _y = y; _w = w; _h = h;
    firePropChange("bounds", oldBounds, getBounds());
    updateEdges();
  }

  ////////////////////////////////////////////////////////////////
  // user interaction methods

  public void mouseClicked(MouseEvent me) {
    super.mouseClicked(me);
    setLineColor(Color.black);
  }


  public void setEnclosingFig(Fig encloser) {
    super.setEnclosingFig(encloser);
    Vector figures = getEnclosedFigs();

    if (getLayer() != null) {
      elementOrdering(figures);
      Vector contents = getLayer().getContents();
      int contentsSize = contents.size();
      for (int j=0; j<contentsSize; j++) {
        Object o = contents.elementAt(j);
        if (o instanceof FigEdgeModelElement) {
          FigEdgeModelElement figedge = (FigEdgeModelElement) o;
          figedge.getLayer().bringToFront(figedge);
        }
      }
    }  
  }

  protected void textEdited(FigText ft) throws PropertyVetoException { 
      // super.textEdited(ft); 
    MNodeInstance noi = (MNodeInstance) getOwner(); 
    if (ft == _name) { 
      String s = ft.getText().trim();
      // why ever...
//       if (s.length()>0) {
//         s = s.substring(0, (s.length() - 1)); 
//      }
      ParserDisplay.SINGLETON.parseNodeInstance(noi, s); 
    } 
  } 
 
  protected void modelChanged() { 
//    super.modelChanged(); 
    MNodeInstance noi = (MNodeInstance) getOwner(); 
    if (noi == null) return; 
    String nameStr = ""; 
    if (noi.getName() != null) { 
      nameStr = noi.getName().trim(); 
    } 
    // construct bases string (comma separated)
    String baseStr = "";
    Collection col = noi.getClassifiers(); 
    if (col != null && col.size() > 0){
	Iterator it = col.iterator();
	baseStr = ((MClassifier)it.next()).getName(); 
	while (it.hasNext()) { 
	    baseStr += ", "+((MClassifier)it.next()).getName(); 
	} 
    }

    if (_readyToEdit) { 
      if( nameStr == "" && baseStr == "") 
	_name.setText(""); 
      else 
	_name.setText(nameStr.trim() + " : " + baseStr); 
    } 
    Dimension nameMin = _name.getMinimumSize(); 
    Rectangle r = getBounds(); 
    setBounds(r.x, r.y, r.width, r.height); 
  } 

  public boolean getUseTrapRect() { return true; }
	
  static final long serialVersionUID = 8822005566372687713L;

} /* end class FigMNodeInstance */

