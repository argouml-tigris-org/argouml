// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// File: SelectionActionState.java
// Classes: SelectionState
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.activity.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.Icon;

import org.apache.log4j.Category;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.uml.UmlFactory;
import org.argouml.uml.diagram.state.ui.FigStateVertex;
import org.argouml.uml.diagram.ui.ModeCreateEdgeAndNode;
import org.argouml.uml.diagram.ui.SelectionWButtons;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.base.ModeModify;
import org.tigris.gef.base.SelectionManager;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.Handle;
import ru.novosoft.uml.behavior.activity_graphs.MActionState;
import ru.novosoft.uml.behavior.activity_graphs.MActionStateImpl;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MTransition;

public class SelectionActionState extends SelectionWButtons {
    protected static Category cat = 
        Category.getInstance(SelectionActionState.class);
  ////////////////////////////////////////////////////////////////
  // constants
  public static Icon trans = 
      ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Transition");
  public static Icon transDown = 
      ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("TransitionDown");
  ////////////////////////////////////////////////////////////////
  // instance varables
  protected boolean _showIncomingLeft = true;
    protected boolean _showIncomingAbove = true;
  protected boolean _showOutgoingRight = true;
    protected boolean _showOutgoingBelow = true;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new SelectionActionState for the given Fig */
  public SelectionActionState(Fig f) { super(f); }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setOutgoingButtonEnabled(boolean b) {
        setOutgoingRightButtonEnabled(b);
        setIncomingAboveButtonEnabled(b);
  }

  public void setIncomingButtonEnabled(boolean b) {
        setIncomingLeftButtonEnabled(b);
        setOutgoingBelowButtonEnabled(b);
  }

  public void setIncomingLeftButtonEnabled(boolean b) {
    _showIncomingLeft = b;
  }

  public void setOutgoingRightButtonEnabled(boolean b) {
    _showOutgoingRight = b;
  }

   public void setIncomingAboveButtonEnabled(boolean b) {
    _showIncomingAbove = b;
  }

  public void setOutgoingBelowButtonEnabled(boolean b) {
    _showOutgoingBelow = b;
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
    int iwd = transDown.getIconWidth();
    int ihd = transDown.getIconHeight();
    if (_showOutgoingRight && hitLeft(cx + cw, cy + ch/2, iw, ih, r)) {
      h.index = 12;
      h.instructions = "Add an outgoing transition";
    }
    else if (_showIncomingLeft && hitRight(cx, cy + ch/2, iw, ih, r)) {
      h.index = 13;
      h.instructions = "Add an incoming transition";
    }
    else if (_showOutgoingBelow && hitAbove(cx+cw/2, cy, iwd, ihd, r)) {
        h.index=14;
        h.instructions = "Add an incoming transaction";
    }
    else if (_showIncomingAbove && hitBelow(cx+cw/2,cy+ch, iwd, ihd, r)) {
        h.index=15;
        h.instructions = "Add an outgoing transaction";
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
    if (_showOutgoingRight) 
        paintButtonLeft(trans, g, cx + cw, cy + ch/2, 12);
    if (_showIncomingLeft) 
        paintButtonRight(trans, g, cx, cy + ch/2, 13);
    if (_showOutgoingBelow) 
        paintButtonAbove(transDown, g, cx + cw/2, cy , 14);
    if (_showIncomingAbove) 
        paintButtonBelow(transDown, g, cx+cw/2, cy + ch, 15);
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
    Class nodeClass = MActionStateImpl.class;

    Editor ce = Globals.curEditor();
    GraphModel gm = ce.getGraphModel();
    if (!(gm instanceof MutableGraphModel)) return;
   
    MutableGraphModel mgm = (MutableGraphModel) gm;

    int bx = mX, by = mY;
    boolean reverse = false;
    switch (hand.index) {
    case 12: //add outgoing
      edgeClass = MTransition.class;
      by = cy + ch/2;
      bx = cx + cw;
      break;
    case 13: // add incoming
      edgeClass = MTransition.class;
      reverse = true;
      by = cy + ch/2;
      bx = cx;
      break;
     case 14: // add incoming
      edgeClass = MTransition.class;
      reverse = true;
      by = cy ;
      bx = cx + cw/2;
      break;
     case 15: // add outgoing
      edgeClass = MTransition.class;
      by = cy + ch;
      bx = cx + cw/2;
      break;
    default:
      cat.warn("invalid handle number");
      break;
    }
    if (edgeClass != null && nodeClass != null) {
      ModeCreateEdgeAndNode m = new
	ModeCreateEdgeAndNode(ce, edgeClass, nodeClass, false);
      m.setup((FigNode)_content, _content.getOwner(), bx, by, reverse);
      ce.mode(m);
    }
  }


  public void buttonClicked(int buttonCode) {
    super.buttonClicked(buttonCode);
    MActionState newNode =  UmlFactory.getFactory().getActivityGraphs().createActionState();

    FigStateVertex fc = (FigStateVertex) _content;
    MStateVertex cls = (MStateVertex) fc.getOwner();

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
    if (buttonCode >=10 && buttonCode <= 13) {
            int x = 0;
            int y = 0; 
            if (buttonCode == 10) {
                // superclass
                x = fc.getX();
                y = Math.max(0, fc.getY() - 200);
            }
            else if (buttonCode == 11) {
                x = fc.getX();
                y = fc.getY() + fc.getHeight() + 100;
            }
            else if (buttonCode == 12) {
                x = fc.getX() + fc.getWidth() + 100;
                y = fc.getY();
            }
            else if (buttonCode == 13) {
                x = Math.max(0, fc.getX() - 200);
                y = fc.getY();
                
            }        
            // place the fig if it is not a selfassociation       
            if (!placeFig(newFC, lay, x, y, outputRect)) return;
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
    else if (buttonCode == 14) newEdge = addIncomingTrans(mgm, cls, newNode);
    else if (buttonCode == 15) newEdge = addOutgoingTrans(mgm, cls, newNode);

    FigEdge fe = (FigEdge) lay.presentationFor(newEdge);
    edgeShape.setLineColor(Color.black);
    edgeShape.setFilled(false);
    edgeShape._isComplete = true;
    fe.setFig(edgeShape);
    ce.getSelectionManager().select(fc);
  }

  public Object addOutgoingTrans(MutableGraphModel mgm, MStateVertex cls,
			    MStateVertex newCls) {
    return mgm.connect(cls, newCls, MTransition.class);
  }

  public Object addIncomingTrans(MutableGraphModel mgm, MStateVertex cls,
			    MStateVertex newCls) {
    return mgm.connect(newCls, cls, MTransition.class);
  }

  ////////////////////////////////////////////////////////////////
  // event handlers


  public void mouseEntered(MouseEvent me) {
    super.mouseEntered(me);
    //_reverse = me.isShiftDown();
  }

} /* end class SelectionActionState */




