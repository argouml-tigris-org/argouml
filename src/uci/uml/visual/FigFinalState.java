// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
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

// File: FigFinalState.java
// Classes: FigFinalState
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

public class FigFinalState extends FigNode
implements VetoableChangeListener, DelayedVetoableChangeListener {

  ////////////////////////////////////////////////////////////////
  // constants

  public final int MARGIN = 2;
  public int x = 0;
  public int y = 0;
  public int width = 20;
  public int height = 20;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The main label on this icon. */
  //  FigText _name;

  /** UML does not really use ports, so just define one big one so
   *  that users can drag edges to or from any point in the icon. */

  FigCircle _bigPort;

  // add other Figs here as needed

  FigCircle _inCircle;
  FigCircle _outCircle;
  ////////////////////////////////////////////////////////////////
  // constructors

  public FigFinalState() {
    Color handleColor = Globals.getPrefs().getHandleColor();
    _bigPort = new FigCircle(x,y,width,height, handleColor, Color.cyan);
    _outCircle = new FigCircle(x,y,width,height, handleColor, Color.white);
    _inCircle = new FigCircle(x+5,y+5,width-10,height-10, handleColor, Color.black);
    // initialize any other Figs here
    // add Figs to the FigNode in back-to-front order
    addFig(_bigPort);
    addFig(_outCircle);
    addFig(_inCircle);

    setBlinkPorts(false); //make port invisble unless mouse enters
    Rectangle r = getBounds();
  }

  public FigFinalState(GraphModel gm, Object node) {
    this();
    setOwner(node);
  }

  public Object clone() {
    FigFinalState figClone = (FigFinalState) super.clone();
    Vector v = figClone.getFigs();
    figClone._bigPort = (FigCircle) v.elementAt(0);
    figClone._outCircle = (FigCircle) v.elementAt(1);
    figClone._inCircle = (FigCircle) v.elementAt(2);
    return figClone;
  }

  public void setOwner(Object node) {
    super.setOwner(node);
    bindPort(node, _bigPort);
    // if it is a UML meta-model object, register interest in any change events
    if (node instanceof ElementImpl)
      ((ElementImpl)node).addVetoableChangeListener(this);
  }

  /** Final states are fixed size. */
  public boolean isResizable() { return false; }

  /** If the UML meta-model object changes state. Update the Fig.  But
   *  we need to do it as a "DelayedVetoableChangeListener", so that
   *  model changes complete before we update the screen. */
  public void vetoableChange(PropertyChangeEvent pce) {
    // throws PropertyVetoException
    //System.out.println("FigFinalState got a change notification!");
    Object src = pce.getSource();
    if (src == getOwner()) {
      DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
      SwingUtilities.invokeLater(delayedNotify);
    }
  }

  /** The UML meta-model object changed. Now update the Fig to show
   *  its current  state. */
  public void delayedVetoableChange(PropertyChangeEvent pce) {
    // throws PropertyVetoException
    //System.out.println("FigFinalState got a delayed change notification!");
    Object src = pce.getSource();
    if (src == getOwner()) {
     // updateText();
      // you may have to update more than just the text
    }
  }

  /** Update the text labels */
  protected void updateText() {
//     Element elmt = (Element) getOwner();
//     String nameStr = GeneratorDisplay.Generate(elmt.getName());

//     startTrans();
// //    _name.setText(nameStr);
//     Rectangle bbox = getBounds();
//     setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
//     endTrans();
  }


  public void dispose() {
    //System.out.println("disposing FigFinalState");
    Element elmt = (Element) getOwner();
    if (elmt == null) return;
    Project p = ProjectBrowser.TheInstance.getProject();
    p.moveToTrash(elmt);
    StateVertex sv = (StateVertex) getOwner();
    try { sv.setParent(null); }
    catch (PropertyVetoException pve) { }
    super.dispose();
  }
  static final long serialVersionUID = -3506578343969467480L;

} /* end class FigFinalState */


