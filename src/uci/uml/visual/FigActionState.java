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



// File: FigActionState.java
// Classes: FigActionState
// Original Author: ics 125b silverbullet team
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
import uci.uml.Behavioral_Elements.State_Machines.*;

/** Class to display graphics for a UML State in a diagram. */

public class FigActionState extends FigNodeModelElement {

  ////////////////////////////////////////////////////////////////
  // constants

  public final int MARGIN = 2;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** UML does not really use ports, so just define one big one so
   *  that users can drag edges to or from any point in the icon. */

  FigRRect _bigPort;
  FigRRect _cover;


  ////////////////////////////////////////////////////////////////
  // constructors

  public FigActionState(GraphModel gm, Object node) {
    super(gm, node);
    _bigPort = new FigRRect(10+1, 10+1, 90-2, 25-2, Color.cyan, Color.cyan);
    _cover = new FigRRect(10, 10, 90, 25, Color.black, Color.white);

    _bigPort.setLineWidth(0);
    _name.setLineWidth(0);
    _name.setBounds(10+5, 10+5, 90-5*2, 25-5*2);
    _name.setFilled(false);

    // add Figs to the FigNode in back-to-front order
    addFig(_bigPort);
    addFig(_cover);
    addFig(_name);

    Object onlyPort = node;
    bindPort(onlyPort, _bigPort);
    //setBlinkPorts(false); //make port invisble unless mouse enters
    Rectangle r = getBounds();
    setBounds(r.x, r.y, r.width, r.height);
  }

  public Object clone() {
    FigActionState figClone = (FigActionState) super.clone();
    Vector v = figClone.getFigs();
    figClone._bigPort = (FigRRect) v.elementAt(0);
    figClone._cover = (FigRRect) v.elementAt(1);
    figClone._name = (FigText) v.elementAt(2);
    return figClone;
  }

  public Dimension getMinimumSize() {
    return new Dimension(90, 25);
  }

  /* Override setBounds to keep shapes looking right */
  public void setBounds(int x, int y, int w, int h) {
    if (_name == null) return;

    _name.setBounds(x+5, y+5, w-5*2, h-5*2);
    _bigPort.setBounds(x, y, w, h);
    _cover.setBounds(x, y, w, h);
    _bigPort.setCornerRadius(h);
    _cover.setCornerRadius(h);

    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
    updateEdges();
  }


  public void dispose() {
    //System.out.println("disposing FigActionState");
    State s = (State) getOwner();
    try {
      s.setParent(null);
      s.setStateMachine(null);
    }
    catch (PropertyVetoException pve) { }
    super.dispose();
  }




} /* end class FigActionState */
