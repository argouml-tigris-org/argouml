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

// File: FigUseCase.java
// Classes: FigUseCase
// Original Author: your email address here
// $Id$

package org.argouml.uml.diagram.use_case.ui;

import java.awt.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.use_cases.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;

import org.argouml.uml.diagram.ui.*;

/** Class to display graphics for a UML MState in a diagram. */

public class FigUseCase extends FigNodeModelElement {
  //implements VetoableChangeListener, DelayedVChangeListener {

  ////////////////////////////////////////////////////////////////
  // constants

  public int PADDING = 10;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** UML does not really use ports, so just define one big one so
   *  that users can drag edges to or from any point in the icon. */

  FigMyCircle _bigPort;

  // add other Figs here aes needed


  FigMyCircle _cover;
  ////////////////////////////////////////////////////////////////
  // constructors

  public FigUseCase() {
    _bigPort = new FigMyCircle(0, 0, 100, 40, Color.black, Color.white);
    _cover = new FigMyCircle(0, 0, 100, 40, Color.black, Color.white);
    _name.setBounds(10, 10, 80, 20);
    _name.setTextFilled(false);
    _name.setFilled(false);
    _name.setLineWidth(0);
    _name.setMultiLine(true);

    // add Figs to the FigNode in back-to-front order
    addFig(_bigPort);
    addFig(_cover);
    addFig(_name);

    Rectangle r = getBounds();
    setBounds(r.x, r.y, r.width, r.height);
  }

  public FigUseCase(GraphModel gm, Object node) {
    this();
    setOwner(node);
  }

  public String placeString() { return "new MUseCase"; }

  public Object clone() {
    FigUseCase figClone = (FigUseCase) super.clone();
    Vector v = figClone.getFigs();
    figClone._bigPort = (FigMyCircle) v.elementAt(0);
    figClone._cover = (FigMyCircle) v.elementAt(1);
    figClone._name = (FigText) v.elementAt(2);
    return figClone;
  }

  ////////////////////////////////////////////////////////////////
  // Fig accessors

  public Selection makeSelection() {
    return new SelectionUseCase(this);
  }

  public void setOwner(Object node) {
    super.setOwner(node);
    bindPort(node, _bigPort);
  }

  public Dimension getMinimumSize() {
    Dimension nameDim = _name.getMinimumSize();
    int w = nameDim.width + PADDING*2;
    int h = nameDim.height + PADDING*2;
    return new Dimension(w, h);
  }

  public void setBounds(int x, int y, int w, int h) {
    Rectangle oldBounds = getBounds();
    _bigPort.setBounds(x, y, w, h);
    _cover.setBounds(x, y, w, h);
    Dimension nameDim = _name.getMinimumSize();
    _name.setBounds(x + (w - nameDim.width)/2, y + (h - nameDim.height)/2,
		    nameDim.width, nameDim.height);
    _x = x; _y = y; _w = w; _h = h;
    firePropChange("bounds", oldBounds, getBounds());
    updateEdges();
  }

  public void setLineColor(Color col) { _cover.setLineColor(col); }
  public Color getLineColor() { return _cover.getLineColor(); }

  public void setFillColor(Color col) { _cover.setFillColor(col); }
  public Color getFillColor() { return _cover.getFillColor(); }

  public void setFilled(boolean f) { _cover.setFilled(f); }
  public boolean getFilled() { return _cover.getFilled(); }

  public void setLineWidth(int w) { _cover.setLineWidth(w); }
  public int getLineWidth() { return _cover.getLineWidth(); }

  /**
   *  FigMyCircle is a FigCircle with corrected connectionPoint method:
   *  this methods calculates where a connected edge ends.
   */
  public class FigMyCircle extends FigCircle {
    public FigMyCircle(int x, int y, int w, int h, Color lColor, Color fColor) {
      super(x, y, w, h, lColor, fColor);
    }
    public Point connectionPoint(Point anotherPt) {
      //calculate border point of elypse, that is on the edge between (_x,_y) and anotherPt
      double rx = _w/2;
      double ry = _h/2;
      double dx = anotherPt.x - _x;
      double dy = anotherPt.y - _y;
      double dd = ry*ry*dx*dx + rx*rx*dy*dy;
      double mu = rx*ry/Math.sqrt(dd);
      Point res = new Point((int)(mu*dx+_x+rx),(int)(mu*dy+_y+ry));
      //System.out.println("    returns "+res.x+','+res.y+')');
      return res;
    }
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** Update the text labels */
//   protected void modelChanged() {
//     super.modelChanged();
//     // needs-more-work: update extension points?
//   }

} /* end class FigUseCase */
