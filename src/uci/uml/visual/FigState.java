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



// File: FigState.java
// Classes: FigState
// Original Author: your email address here
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

public class FigState extends FigNodeModelElement {

  ////////////////////////////////////////////////////////////////
  // constants

  public final int MARGIN = 2;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The main label on this icon. */
  //FigText _name;

  /** UML does not really use ports, so just define one big one so
   *  that users can drag edges to or from any point in the icon. */

  FigRect _bigPort;
  FigRect _cover;

  // add other Figs here aes needed


  ////////////////////////////////////////////////////////////////
  // constructors

  public FigState(GraphModel gm, Object node) {
    super(gm, node);
    _bigPort = new FigRRect(10+1, 10+1, 90-2, 70-2, Color.cyan, Color.cyan);
    _cover = new FigRRect(10, 10, 90, 70, Color.black, Color.white);

    _bigPort.setLineWidth(0);
    _name.setLineWidth(0);
    // initialize any other Figs here

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

/* Override setBounds to keep shapes looking right */
  public void setBounds(int x, int y, int w, int h) {
    if (_name == null) return;
    int leftSide = x;
    int widthP = w;
    int topSide = y;
    int heightP = h;

    Rectangle _name_pref = _name.getBounds();


    int total_height = _name_pref.height;

    widthP = Math.max(widthP,_name_pref.width);
    heightP = Math.max(heightP, total_height);

   int extra_each = (heightP - total_height) / 3;

    _name.setBounds(leftSide+10, topSide+(heightP-total_height)/2,
		    widthP-20, _name_pref.height);
    _bigPort.setBounds(leftSide+1, topSide+1, widthP-2, heightP-2);

    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
  }


  /** Update the text labels */
  protected void modelChanged() {
    super.modelChanged();
    //needs-more-work: text fields other than name
  }



  public void dispose() {
    System.out.println("disposing FigState");
    State s = (State) getOwner();
    try {
      s.setParent(null);
      s.setStateMachine(null);
    }
    catch (PropertyVetoException pve) { }
    super.dispose();
  }



} /* end class FigState */
