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



// File: FigUseCase.java
// Classes: FigUseCase
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
import uci.uml.Behavioral_Elements.Use_Cases.*;

/** Class to display graphics for a UML State in a diagram. */

public class FigUseCase extends FigNodeModelElement {
  //implements VetoableChangeListener, DelayedVetoableChangeListener {

  ////////////////////////////////////////////////////////////////
  // constants

  public int PADDING = 10;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** UML does not really use ports, so just define one big one so
   *  that users can drag edges to or from any point in the icon. */

  FigCircle _bigPort;

  // add other Figs here aes needed


  FigCircle _cover;
  ////////////////////////////////////////////////////////////////
  // constructors

  public FigUseCase(GraphModel gm, Object node) {
    super(gm, node);

    _bigPort = new FigCircle(0, 0, 100, 40, Color.black, Color.white);
    _cover = new FigCircle(0, 0, 100, 40, Color.black, Color.white);
    _name.setBounds(10, 10, 80, 20);
    _name.setTextFilled(false);
    _name.setFilled(false);
    _name.setLineWidth(0);
    _name.setMultiLine(true);

    // add Figs to the FigNode in back-to-front order
    addFig(_bigPort);
    addFig(_cover);
    addFig(_name);

    Object onlyPort = node;
    bindPort(onlyPort, _bigPort);
    Rectangle r = getBounds();
    setBounds(r.x, r.y, r.width, r.height);
  }




  /** Update the text labels */
  protected void modelChanged() {
    super.modelChanged();
    // needs-more-work: update extension points?
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

  public void dispose() {
    if (!(getOwner() instanceof Element)) return;
    Element elmt = (Element) getOwner();
    Project p = ProjectBrowser.TheInstance.getProject();
    p.moveToTrash(elmt);
    super.dispose();
  }




} /* end class FigUseCase */
