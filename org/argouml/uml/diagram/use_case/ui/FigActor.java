// Copyright (c) 1996-2001 The Regents of the University of California. All
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

// File: FigActor.java
// Classes: FigActor
// Original Author: abonner@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.use_case.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Vector;

import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigText;

/** Class to display graphics for a UML MState in a diagram. */

public class FigActor extends FigNodeModelElement {


  ////////////////////////////////////////////////////////////////
  // instance variables

  /** UML does not really use ports, so just define one big one so
   *  that users can drag edges to or from any point in the icon. */
  FigCircle _bigPort;

  /* Put in the things for the "person" in the FigActor */
  FigCircle _head;
  FigLine _body;
  FigLine _arms;
  FigLine _leftLeg;
  FigLine _rightLeg;

  // add other Figs here aes needed


  ////////////////////////////////////////////////////////////////
  // constructors

  public FigActor() {
    // Put this rectangle behind the rest, so it goes first
    _bigPort = new FigCircle(10, 30, 15, 15, Color.gray, Color.gray);
    _head = new FigCircle(10, 30, 15, 15, Color.black, Color.white);
    _body = new FigLine(20, 45, 20, 60, Color.black);
    _arms = new FigLine(10, 50, 30, 50, Color.black);
    _leftLeg = new FigLine(20, 60, 15, 75, Color.black );
    _rightLeg = new FigLine(20, 60, 25, 75, Color.black );
    _name.setBounds(5, 75, 35, 20);

    _name.setTextFilled(false);
    _name.setFilled(false);
    _name.setLineWidth(0);
    // initialize any other Figs here

    // add Figs to the FigNode in back-to-front order
    addFig(_bigPort);
    addFig(_head);
    addFig(_body);
    addFig(_arms);
    addFig(_leftLeg);
    addFig(_rightLeg);
    addFig(_name);

  }

  public FigActor(GraphModel gm, Object node) {
    this();
    setOwner(node);
  }

  public String placeString() { return "new MActor"; }

  public Object clone() {
    FigActor figClone = (FigActor) super.clone();
    Vector v = figClone.getFigs();
    figClone._bigPort = (FigCircle) v.elementAt(0);
    figClone._head = (FigCircle) v.elementAt(1);
    figClone._body = (FigLine) v.elementAt(2);
    figClone._arms = (FigLine) v.elementAt(3);
    figClone._leftLeg = (FigLine) v.elementAt(4);
    figClone._rightLeg = (FigLine) v.elementAt(5);
    figClone._name = (FigText) v.elementAt(6);
    return figClone;
  }

  ////////////////////////////////////////////////////////////////
  // Fig accessors

  public Selection makeSelection() {
    return new SelectionActor(this);
  }

  public void setOwner(Object node) {  
    super.setOwner(node);
    bindPort(node, _bigPort);
  }

  /** Returns true if this Fig can be resized by the user. */
  public boolean isResizable() { return false; }

//   public Selection makeSelection() {
//     return new SelectionMoveClarifiers(this);
//   }

  public void setLineColor(Color col) {
    _head.setLineColor(col);
    _body.setLineColor(col);
    _arms.setLineColor(col);
    _leftLeg.setLineColor(col);
    _rightLeg.setLineColor(col);
  }
  public Color getLineColor() { return _head.getLineColor(); }

  public void setFillColor(Color col) { _head.setFillColor(col); }
  public Color getFillColor() { return _head.getFillColor(); }

  public void setFilled(boolean f) { _head.setFilled(f); }
  public boolean getFilled() { return _head.getFilled(); }

  public void setLineWidth(int w) {
    _head.setLineWidth(w);
    _body.setLineWidth(w);
    _arms.setLineWidth(w);
    _leftLeg.setLineWidth(w);
    _rightLeg.setLineWidth(w);
  }
  public int getLineWidth() { return _head.getLineWidth(); }

  public Dimension getMinimumSize() {
    Dimension nameDim = _name.getMinimumSize();
    int w = nameDim.width;
    int h = nameDim.height + 65;
    return new Dimension(w, h);
  }

  public void setBounds(int x, int y, int w, int h) {
    int middle = w/2;
    h = _h;
    Rectangle oldBounds = getBounds();
    _bigPort.setLocation(x + middle - _bigPort.getWidth()/2, y + h - 65);
    _head.setLocation(x + middle - _head.getWidth()/2, y + h - 65);
    _body.setLocation(x + middle, y + h - 50);
    _arms.setLocation(x + middle - _arms.getWidth()/2, y + h - 45);
    _leftLeg.setLocation(x + middle - _leftLeg.getWidth(), y + h - 35);
    _rightLeg.setLocation(x + middle,  y + h - 35);

    Dimension minTextSize = _name.getMinimumSize();
    _name.setBounds(x + middle - minTextSize.width/2,
		    y + h - minTextSize.height,
		    minTextSize.width, minTextSize.height);

    updateEdges();
    _x = x;
    _y = y;
    _w = w;
    // do not set height
    firePropChange("bounds", oldBounds, getBounds());
  }

	/**
	 * @see org.tigris.gef.presentation.FigNode#deepHitPort(int, int)
	 */
	public Object deepHitPort(int x, int y) {
		Object o = super.deepHitPort(x, y);
		if (o != null) return o;
		if (hit(new Rectangle(new Dimension(x,y)))) return getOwner();
		return null;
	}

} /* end class FigActor */
