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

// File: FigInitialState.java
// Classes: FigInitialState
// Original Author: abonner@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Vector;

import org.argouml.uml.diagram.activity.ui.SelectionActionState;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigCircle;
import ru.novosoft.uml.behavior.activity_graphs.MActivityGraph;
import ru.novosoft.uml.behavior.state_machines.MPseudostate;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;

/** Class to display graphics for a UML State in a diagram. */

public class FigInitialState extends FigStateVertex {

  ////////////////////////////////////////////////////////////////
  // constants

  public final int MARGIN = 2;
  public int x = 10;
  public int y = 10;
  public int width = 20;
  public int height = 20;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** UML does not really use ports, so just define one big one so
   *  that users can drag edges to or from any point in the icon. */

  protected FigCircle _bigPort;
  protected FigCircle _head;

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigInitialState() {
    _bigPort = new FigCircle(x,y,width,height, Color.cyan, Color.cyan);
    _head = new FigCircle(x,y,width,height, Color.black, Color.black);
    // add Figs to the FigNode in back-to-front order
    addFig(_bigPort);
    addFig(_head);

    setBlinkPorts(false); //make port invisble unless mouse enters
    Rectangle r = getBounds();
  }

  public FigInitialState(GraphModel gm, Object node) {
    this();
    setOwner(node);
  }

  public Object clone() {
    FigInitialState figClone = (FigInitialState) super.clone();
    Vector v = figClone.getFigs();
    figClone._bigPort = (FigCircle) v.elementAt(0);
    figClone._head = (FigCircle) v.elementAt(1);
    return figClone;
  }

  ////////////////////////////////////////////////////////////////
  // Fig accessors

    public Selection makeSelection() {
        MPseudostate pstate = null;
        Selection sel = null;
        if (getOwner() != null) {
            pstate = (MPseudostate)getOwner();
            if (pstate == null) return sel;
            if (pstate.getContainer().getStateMachine() 
                instanceof MActivityGraph) {
                sel = new SelectionActionState(this);
                ((SelectionActionState)sel).setIncomingButtonEnabled(false);
                Collection outs = ((MStateVertex)getOwner()).getOutgoings();
                ((SelectionActionState)sel).
                    setOutgoingButtonEnabled(outs == null || outs.size() == 0);
            }
            else {
                sel = new SelectionState(this);
                ((SelectionState)sel).setIncomingButtonEnabled(false);
                Collection outs = ((MStateVertex)getOwner()).getOutgoings();
                ((SelectionState)sel).
                    setOutgoingButtonEnabled(outs == null || outs.size() == 0);
            }
        }
        return sel;
    }

  public void setOwner(Object node) {
    super.setOwner(node);
    bindPort(node, _bigPort);
  }

  /** Initial states are fixed size. */
  public boolean isResizable() { return false; }

//   public Selection makeSelection() {
//     return new SelectionMoveClarifiers(this);
//   }

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

  static final long serialVersionUID = 6572261327347541373L;

} /* end class FigInitialState */
