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




// File: SelectionState.java
// Classes: SelectionState
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.visual;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
//import java.awt.image.*;
import javax.swing.Icon;

import uci.gef.*;
import uci.graph.*;
import uci.util.Util;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Behavioral_Elements.State_Machines.*;


public class SelectionState extends SelectionWButtons {
  ////////////////////////////////////////////////////////////////
  // constants
  public static Icon trans = Util.loadIconResource("Transition");

  ////////////////////////////////////////////////////////////////
  // instance varables
  protected boolean _showIncoming = true;
  protected boolean _showOutgoing = true;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new SelectionState for the given Fig */
  public SelectionState(Fig f) { super(f); }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setIncomingButtonEnabled(boolean b) {
    _showIncoming = b;
  }

  public void setOutgoingButtonEnabled(boolean b) {
    _showOutgoing = b;
  }

  public void hitHandle(Rectangle r, Handle h) {
    super.hitHandle(r, h);
    if (h.index != -1) return;
    if (!_paintButtons) return;
    Editor ce = Globals.curEditor();
    SelectionManager sm = ce.getSelectionManager();
    if (sm.size() != 1) return;
    ModeManager mm = ce.getModeManager();
    if (mm.includes(ModeModify.class) && _pressedButton == -1) return;
    int cx = _content.getX();
    int cy = _content.getY();
    int cw = _content.getWidth();
    int ch = _content.getHeight();
    int iw = trans.getIconWidth();
    int ih = trans.getIconHeight();
    if (_showOutgoing && hitLeft(cx + cw, cy + ch/2, iw, ih, r)) {
      h.index = 12;
      h.instructions = "Add an outgoing transition";
    }
    else if (_showIncoming && hitRight(cx, cy + ch/2, iw, ih, r)) {
      h.index = 13;
      h.instructions = "Add an incoming transition";
    }
    else {
      h.index = -1;
      h.instructions = "Move object(s)";
    }
  }


  /** Paint the handles at the four corners and midway along each edge
   * of the bounding box.  */
  public void paintButtons(Graphics g) {
    int cx = _content.getX();
    int cy = _content.getY();
    int cw = _content.getWidth();
    int ch = _content.getHeight();
    if (_showOutgoing) paintButtonLeft(trans, g, cx + cw, cy + ch/2, 12);
    if (_showIncoming) paintButtonRight(trans, g, cx, cy + ch/2, 13);
  }


  public void dragHandle(int mX, int mY, int anX, int anY, Handle hand) {
    if (hand.index < 10) {
      _paintButtons = false;
      super.dragHandle(mX, mY, anX, anY, hand);
      return;
    }
    int cx = _content.getX(), cy = _content.getY();
    int cw = _content.getWidth(), ch = _content.getHeight();
    int newX = cx, newY = cy, newW = cw, newH = ch;
    Dimension minSize = _content.getMinimumSize();
    int minWidth = minSize.width, minHeight = minSize.height;
    Class edgeClass = null;
    Class nodeClass = State.class;
    int bx = mX, by = mY;
    boolean reverse = false;
    switch (hand.index) {
    case 12: //add outgoing
      edgeClass = Transition.class;
      by = cy + ch/2;
      bx = cx + cw;
      break;
    case 13: // add incoming
      edgeClass = Transition.class;
      reverse = true;
      by = cy + ch/2;
      bx = cx;
      break;
    default:
      System.out.println("invalid handle number");
      break;
    }
    if (edgeClass != null && nodeClass != null) {
      Editor ce = Globals.curEditor();
      ModeCreateEdgeAndNode m = new
	ModeCreateEdgeAndNode(ce, edgeClass, nodeClass, false);
      m.setup((FigNode)_content, _content.getOwner(), bx, by, reverse);
      ce.mode(m);
    }
  }


  public void buttonClicked(int buttonCode) {
    super.buttonClicked(buttonCode);
    State newNode = new State();
    FigState fc = (FigState) _content;
    State cls = (State) fc.getOwner();

    Editor ce = Globals.curEditor();
    GraphModel gm = ce.getGraphModel();
    if (!(gm instanceof MutableGraphModel)) return;
    MutableGraphModel mgm = (MutableGraphModel) gm;

    if (!mgm.canAddNode(newNode)) return;
    GraphNodeRenderer renderer = ce.getGraphNodeRenderer();
    LayerPerspective lay = (LayerPerspective)
      ce.getLayerManager().getActiveLayer();
    Fig newFC = renderer.getFigNodeFor(gm, lay, newNode);

    Rectangle outputRect = new Rectangle(Math.max(0, fc.getX() - 200),
					 Math.max(0, fc.getY() - 200),
					 fc.getWidth() + 400,
					 fc.getHeight() + 400);
    if (buttonCode == 12) {
      newFC.setLocation(fc.getX() + fc.getWidth() + 100, fc.getY());
      outputRect.x = fc.getX()+ fc.getWidth() + 100 ;
      outputRect.width = 200;
      lay.bumpOffOtherNodesIn(newFC, outputRect, false, true);
    }
    else if (buttonCode == 13) {
      newFC.setLocation(Math.max(0, fc.getX() - 200), fc.getY());
      outputRect.x = fc.getX() - 200;
      outputRect.width = 200;
      lay.bumpOffOtherNodesIn(newFC, outputRect, false, true);
    }
    ce.add(newFC);
    mgm.addNode(newNode);

    FigPoly edgeShape = new FigPoly();
    Point fcCenter = fc.center();
    edgeShape.addPoint(fcCenter.x, fcCenter.y);
    Point newFCCenter = newFC.center();
    edgeShape.addPoint(newFCCenter.x, newFCCenter.y);
    Object newEdge = null;
    if (buttonCode == 12) newEdge = addOutgoingTrans(mgm, cls, newNode);
    else if (buttonCode == 13) newEdge = addIncomingTrans(mgm, cls, newNode);

    FigEdge fe = (FigEdge) lay.presentationFor(newEdge);
    edgeShape.setLineColor(Color.black);
    edgeShape.setFilled(false);
    edgeShape._isComplete = true;
    fe.setFig(edgeShape);
    ce.getSelectionManager().select(fc);
  }

  public Object addOutgoingTrans(MutableGraphModel mgm, State cls,
			    State newCls) {
    return mgm.connect(cls, newCls, Transition.class);
  }

  public Object addIncomingTrans(MutableGraphModel mgm, State cls,
			    State newCls) {
    return mgm.connect(newCls, cls, Transition.class);
  }

  ////////////////////////////////////////////////////////////////
  // event handlers


  public void mouseEntered(MouseEvent me) {
    super.mouseEntered(me);
    //_reverse = me.isShiftDown();
  }

} /* end class SelectionState */

