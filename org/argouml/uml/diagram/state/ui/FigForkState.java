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

// File: FigForkState.java
// Classes: FigForkState
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.state.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.state_machines.*;

import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;

/** Class to display graphics for a UML MState in a diagram. */

public class FigForkState extends FigStateVertex {

  ////////////////////////////////////////////////////////////////
  // constants

  public static final int MARGIN = 2;
  public static final int X = 10;
  public static final int Y = 10;
  public static final int WIDTH = 80;
  public static final int HEIGHT = 9;

  ////////////////////////////////////////////////////////////////
  // instance variables

  FigRect _bigPort;
  FigRect _head;

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigForkState() {
    _bigPort = new FigRect(X,Y,WIDTH,HEIGHT, Color.cyan, Color.cyan);
    _head = new FigRect(X,Y,WIDTH,HEIGHT, Color.black, Color.black);
    // add Figs to the FigNode in back-to-front order
    addFig(_bigPort);
    addFig(_head);

    setBlinkPorts(false); //make port invisble unless mouse enters
    Rectangle r = getBounds();
  }

  public FigForkState(GraphModel gm, Object node) {
    this();
    setOwner(node);
  }

  public Object clone() {
    FigForkState figClone = (FigForkState) super.clone();
    Vector v = figClone.getFigs();
    figClone._bigPort = (FigRect) v.elementAt(0);
    figClone._head = (FigRect) v.elementAt(1);
    return figClone;
  }

  ////////////////////////////////////////////////////////////////
  // Fig accessors

  public void setOwner(Object node) {
    super.setOwner(node);
    bindPort(node, _bigPort);
    // if it is a UML meta-model object, register interest in any change events
    if (node instanceof MElement)
      ((MElement)node).addMElementListener(this);
  }

  /** Initial states are fixed size. */
  //public boolean isResizable() { return false; }

  /* Override setBounds to keep shapes looking right */
  public void setBounds(int x, int y, int w, int h) {
    Rectangle oldBounds = getBounds();
    if (w > h) h = 9; else w = 9;
    _bigPort.setBounds(x, y, w, h);
    _head.setBounds(x, y, w, h);

    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
    updateEdges();
    firePropChange("bounds", oldBounds, getBounds());
  }


  public void setLineColor(Color col) { _head.setLineColor(col); }
  public Color getLineColor() { return _head.getLineColor(); }

  public void setFillColor(Color col) { _head.setFillColor(col); }
  public Color getFillColor() { return _head.getFillColor(); }

  public void setFilled(boolean f) { }
  public boolean getFilled() { return true; }

  public void setLineWidth(int w) { _head.setLineWidth(w); }
  public int getLineWidth() { return _head.getLineWidth(); }

  ////////////////////////////////////////////////////////////////
  // Event handlers
  
  public void mouseClicked(MouseEvent me) { }
  public void keyPressed(KeyEvent ke) { }


  static final long serialVersionUID = 6702818473439087473L;


} /* end class FigForkState */
