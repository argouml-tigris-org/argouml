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

// File: SelectionNode.java
// Classes: SelectionNode
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$


package org.argouml.uml.diagram.deployment.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.Icon;

import org.apache.log4j.Category;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.uml.UmlFactory;
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
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MNode;
import ru.novosoft.uml.foundation.core.MNodeImpl;

public class SelectionNode extends SelectionWButtons {
    protected static Category cat = 
        Category.getInstance(SelectionNode.class);
  ////////////////////////////////////////////////////////////////
  // constants
  public static Icon dep = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Association");



  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new SelectionNode for the given Fig */
  public SelectionNode(Fig f) { super(f); }

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
    int aw = dep.getIconWidth();
    int ah = dep.getIconHeight();
    if (hitAbove(cx + cw/2, cy, aw, ah, r)) {
      h.index = 10;
      h.instructions = "Add a node";
    }
    else if (hitBelow(cx + cw/2, cy + ch, aw, ah, r)) {
      h.index = 11;
      h.instructions = "Add a node";
    }
    else if (hitLeft(cx + cw, cy + ch/2, aw, ah, r)) {
      h.index = 12;
      h.instructions = "Add a node";
    }
    else if (hitRight(cx, cy + ch/2, aw, ah, r)) {
      h.index = 13;
      h.instructions = "Add a node";
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
    paintButtonAbove(dep, g, cx + cw/2, cy, 10);
    paintButtonBelow(dep, g, cx + cw/2, cy + ch, 11);
    paintButtonLeft(dep, g, cx + cw, cy + ch/2, 12);
    paintButtonRight(dep, g, cx, cy + ch/2, 13);
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
    Class nodeClass = MNodeImpl.class;
    int bx = mX, by = mY;
    boolean reverse = false;
    switch (hand.index) {
    case 10: //add dep
      edgeClass = MAssociation.class;
      by = cy;
      bx = cx + cw/2;
      break;
    case 11: //add dep
      edgeClass = MAssociation.class;
      reverse = true;
      by = cy + ch;
      bx = cx + cw/2;
      break;
    case 12: //add dep
      edgeClass = MAssociation.class;
      by = cy + ch/2;
      bx = cx + cw;
      break;
    case 13: // add dep
      edgeClass = MAssociation.class;
      reverse = true;
      by = cy + ch/2;
      bx = cx;
      break;
    default:
      cat.warn("invalid handle number");
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
    MNode newNode = UmlFactory.getFactory().getCore().createNode();
    FigMNode fc = (FigMNode) _content;
    MNode cls = (MNode) fc.getOwner();

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
   // handle the case that it is not a self association
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

    if (buttonCode == 10) newEdge = addNodeClassAbove(mgm, cls, newNode);
    else if (buttonCode == 11) newEdge = addNodeClassBelow(mgm, cls, newNode);
    else if (buttonCode == 12) newEdge = addNodeClassRight(mgm, cls, newNode);
    else if (buttonCode == 13) newEdge = addNodeClassLeft(mgm, cls, newNode);

    FigEdge fe = (FigEdge) lay.presentationFor(newEdge);
    edgeShape.setLineColor(Color.black);
    edgeShape.setFilled(false);
    edgeShape._isComplete = true;
    fe.setFig(edgeShape);
    ce.getSelectionManager().select(fc);
  }

  public Object addNodeClassAbove(MutableGraphModel mgm, MNode cls,
			    MNode newCls) {
    return mgm.connect(cls, newCls, MAssociation.class);
  }

  public Object addNodeClassBelow(MutableGraphModel mgm, MNode cls,
			    MNode newCls) {
    return mgm.connect(newCls, cls, MAssociation.class);
  }
  public Object addNodeClassRight(MutableGraphModel mgm, MNode cls,
			    MNode newCls) {
    return mgm.connect(cls, newCls, MAssociation.class);
  }

  public Object addNodeClassLeft(MutableGraphModel mgm, MNode cls,
			    MNode newCls) {
    return mgm.connect(newCls, cls, MAssociation.class);
  }


} /* end class SelectionNode */

