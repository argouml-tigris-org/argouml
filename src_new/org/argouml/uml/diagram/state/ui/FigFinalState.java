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

// File: FigFinalState.java
// Classes: FigFinalState
// Original Author: ics125b spring 98
// $Id$

package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Vector;

import org.argouml.uml.diagram.activity.ui.SelectionActionState;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigCircle;
import ru.novosoft.uml.behavior.activity_graphs.MActivityGraph;
import ru.novosoft.uml.behavior.state_machines.MFinalState;

/** Class to display graphics for a UML MState in a diagram. */

public class FigFinalState extends FigStateVertex {

  ////////////////////////////////////////////////////////////////
  // constants

  public final int MARGIN = 2;
  public int x = 0;
  public int y = 0;
  public int width = 20;
  public int height = 20;

  ////////////////////////////////////////////////////////////////
  // instance variables

  FigCircle _bigPort;
  FigCircle _inCircle;
  FigCircle _outCircle;

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigFinalState() {
    Color handleColor = Globals.getPrefs().getHandleColor();
    _bigPort = new FigCircle(x,y,width,height, handleColor, Color.cyan);
    _outCircle = new FigCircle(x,y,width,height, Color.black, Color.white);
    _inCircle = new FigCircle(x+5,y+5,width-10,height-10, handleColor, Color.black);

    _outCircle.setLineWidth(1);
    _inCircle.setLineWidth(0);

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

  ////////////////////////////////////////////////////////////////
  // Fig accessors

  public Selection makeSelection() {
      MFinalState pstate = null;
      Selection sel = null;
      if (getOwner() != null) {
	  pstate = (MFinalState)getOwner();
	  if (pstate.getContainer().getStateMachine() instanceof MActivityGraph) {
	      sel = new SelectionActionState(this);
		  ((SelectionActionState)sel).setOutgoingButtonEnabled(false);
	  }
	  else {
	      sel = new SelectionState(this);
		  ((SelectionState)sel).setOutgoingButtonEnabled(false);
	  }
      }
	  return sel;
  }

  public void setOwner(Object node) {
    super.setOwner(node);
    bindPort(node, _bigPort);
  }

  /** Final states are fixed size. */
  public boolean isResizable() { return false; }

//   public Selection makeSelection() {
//     return new SelectionMoveClarifiers(this);
//   }

  public void setLineColor(Color col) { _outCircle.setLineColor(col); }
  public Color getLineColor() { return _outCircle.getLineColor(); }

  public void setFillColor(Color col) { _inCircle.setFillColor(col); }
  public Color getFillColor() { return _inCircle.getFillColor(); }

  public void setFilled(boolean f) { }
  public boolean getFilled() { return true; }

  public void setLineWidth(int w) { _outCircle.setLineWidth(w); }
  public int getLineWidth() { return _outCircle.getLineWidth(); }

  ////////////////////////////////////////////////////////////////
  // Event handlers

  public void mouseClicked(MouseEvent me) { }
  public void keyPressed(KeyEvent ke) { }


  static final long serialVersionUID = -3506578343969467480L;

} /* end class FigFinalState */


