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




// File: SelectionClass.java
// Classes: SelectionClass
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


public class SelectionClass extends SelectionNodeClarifiers {
  ////////////////////////////////////////////////////////////////
  // constants
  public static int IMAGE_SIZE = 22;
  public static int MARGIN = 2;
  public static Icon inherit = Util.loadIconResource("Generalization");
  public static Icon assoc = Util.loadIconResource("Association");
  public static Icon compos = Util.loadIconResource("CompositeAggregation");
  public static Color PRESSED_COLOR = Color.gray.brighter();

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected boolean _paintButtons = true;
  protected boolean _useComposite = false;
  protected int _pressedButton = -1;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new SelectionClass for the given Fig */
  public SelectionClass(Fig f) { super(f); }

  /** Return a handle ID for the handle under the mouse, or -1 if
   *  none. Needs-More-Work: in the future, return a Handle instance or
   *  null. <p>
   *  <pre>
   *   0-------1-------2
   *   |               |
   *   3               4
   *   |               |
   *   5-------6-------7
   * </pre>
   */
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
    int iw = inherit.getIconWidth();
    int ih = inherit.getIconHeight();
    int aw = assoc.getIconWidth();
    int ah = assoc.getIconHeight();
    if (hitAbove(cx + cw/2, cy, iw, ih, r)) {
      h.index = 10;
      h.instructions = "Add a superclass";
    }
    else if (hitBelow(cx + cw/2, cy + ch, iw, ih, r)) {
      h.index = 11;
      h.instructions = "Add a subclass";
    }
    else if (hitLeft(cx + cw, cy + ch/2, aw, ah, r)) {
      h.index = 12;
      h.instructions = "Add an associated class";
    }
    else if (hitRight(cx, cy + ch/2, aw, ah, r)) {
      h.index = 13;
      h.instructions = "Add an associated class";
    }
    else {
      h.index = -1;
      h.instructions = "Move object(s)";
    }
  }

  public boolean hitAbove(int x, int y, int w, int h, Rectangle r) {
    return intersectsRect(r, x - w/2, y - h - MARGIN, w, h + MARGIN);
  }

  public boolean hitBelow(int x, int y, int w, int h, Rectangle r) {
    return intersectsRect(r, x - w/2, y, w, h + MARGIN);
  }

  public boolean hitLeft(int x, int y, int w, int h, Rectangle r) {
    return intersectsRect(r, x, y - h/2, w + MARGIN, h);
  }

  public boolean hitRight(int x, int y, int w, int h, Rectangle r) {
    return intersectsRect(r, x - w - MARGIN, y - h/2, w + MARGIN, h);
  }

  public boolean intersectsRect(Rectangle r, int x, int y, int w, int h) {
    return !((r.x + r.width <= x) ||
	     (r.y + r.height <= y) ||
	     (r.x >= x + w) ||
	     (r.y >= y + h));
  }

  /** Paint the handles at the four corners and midway along each edge
   * of the bounding box.  */
  public void paint(Graphics g) {
    super.paint(g);
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
    paintButtonAbove(inherit, g, cx + cw/2, cy, 10);
    paintButtonBelow(inherit, g, cx + cw/2, cy + ch, 11);
    if (_useComposite) {
      paintButtonLeft(compos, g, cx + cw, cy + ch/2, 12);
      paintButtonRight(compos, g, cx, cy + ch/2, 13);
    }
    else {
      paintButtonLeft(assoc, g, cx + cw, cy + ch/2, 12);
      paintButtonRight(assoc, g, cx, cy + ch/2, 13);
    }
  }

  public void paintButtonAbove(Icon i, Graphics g, int x, int y, int hi) {
    paintButton(i, g, x - i.getIconWidth()/2, y - i.getIconHeight() -
		MARGIN, hi);
  }

  public void paintButtonBelow(Icon i, Graphics g, int x, int y, int hi) {
    paintButton(i, g, x - i.getIconWidth()/2, y + MARGIN, hi);
  }

  public void paintButtonLeft(Icon i, Graphics g, int x, int y, int hi) {
    paintButton(i, g, x + MARGIN, y - i.getIconHeight()/2, hi);
  }

  public void paintButtonRight(Icon i, Graphics g, int x, int y, int hi) {
    paintButton(i, g, x - i.getIconWidth() - MARGIN, y -
		i.getIconHeight()/2, hi);
  }

  public void paintButton(Icon i, Graphics g, int x, int y, int hi) {
    int w = i.getIconWidth() + 4;
    int h = i.getIconHeight() + 4;

    if (hi == _pressedButton) {
      g.setColor(PRESSED_COLOR);
      g.fillRect(x-2, y-2, w, h);
    }
    i.paintIcon(null, g, x, y);

    g.translate(x-2, y-2);

    Color handleColor = Globals.getPrefs().handleColorFor(_content);
    g.setColor(handleColor.darker());
    g.drawRect(0, 0, w-2, h-2);

    g.setColor(handleColor.brighter().brighter().brighter());
    if (hi != _pressedButton) {
      g.drawLine(1, h-3, 1, 1);
      g.drawLine(1, 1, w-3, 1);
    }

    g.drawLine(0, h-1, w-1, h-1);
    g.drawLine(w-1, h-1, w-1, 0);

    g.translate(-x+2, -y+2);
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
    Class nodeClass = uci.uml.Foundation.Core.MMClass.class;
    int bx = mX, by = mY;
    boolean reverse = false;
    switch (hand.index) {
    case 10: //add superclass
      edgeClass = uci.uml.Foundation.Core.Generalization.class;
      by = cy;
      bx = cx + cw/2;
      break;
    case 11: //add subclass
      edgeClass = uci.uml.Foundation.Core.Generalization.class;
      reverse = true;
      by = cy + ch;
      bx = cx + cw/2;
      break;
    case 12: //add assoc
      edgeClass = uci.uml.Foundation.Core.Association.class;
      by = cy + ch/2;
      bx = cx + cw;
      break;
    case 13: // add assoc
      edgeClass = uci.uml.Foundation.Core.Association.class;
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
	ModeCreateEdgeAndNode(ce, edgeClass, nodeClass, _useComposite);
      m.setup((FigNode)_content, _content.getOwner(), bx, by, reverse);
      ce.mode(m);
    }

  }

  public Rectangle getBounds() {
    return new Rectangle(_content.getX() - IMAGE_SIZE * 2,
			 _content.getY() - IMAGE_SIZE * 2,
			 _content.getWidth() + IMAGE_SIZE * 4,
			 _content.getHeight() + IMAGE_SIZE * 4);
  }

  /** Dont show buttons while the user is moving the Class.  Called
   *  from FigClass when it is translated. */
  public void hideButtons() { _paintButtons = false; }

  public void buttonClicked(int buttonCode) {
    MMClass newNode = new MMClass();
    FigClass fc = (FigClass) _content;
    MMClass cls = (MMClass) fc.getOwner();

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
    if (buttonCode == 10) {
      newFC.setLocation(fc.getX(), Math.max(0, fc.getY() - 200));
      outputRect.height = 200;
      lay.bumpOffOtherNodesIn(newFC, outputRect, false, false);
    }
    else if (buttonCode == 11) {
      newFC.setLocation(fc.getX(), fc.getY() + fc.getHeight() + 100);
      outputRect.y = fc.getY() + fc.getHeight() + 100;
      outputRect.height = 200;
      lay.bumpOffOtherNodesIn(newFC, outputRect, false, false);
    }
    else if (buttonCode == 12) {
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
    if (buttonCode == 10) newEdge = addSuperClass(mgm, cls, newNode);
    else if (buttonCode == 11) newEdge = addSubClass(mgm, cls, newNode);
    else if (buttonCode == 12) newEdge = addAssocClassRight(mgm, cls, newNode);
    else if (buttonCode == 13) newEdge = addAssocClassLeft(mgm, cls, newNode);

    FigEdge fe = (FigEdge) lay.presentationFor(newEdge);
    edgeShape.setLineColor(Color.black);
    edgeShape.setFilled(false);
    edgeShape._isComplete = true;
    fe.setFig(edgeShape);
    ce.getSelectionManager().select(fc);
  }

  public Object addSuperClass(MutableGraphModel mgm, MMClass cls,
			    MMClass newCls) {
    return mgm.connect(cls, newCls, Generalization.class);
  }

  public Object addSubClass(MutableGraphModel mgm, MMClass cls,
			    MMClass newCls) {
    return mgm.connect(newCls, cls, Generalization.class);
  }

  public Object addAssocClassRight(MutableGraphModel mgm, MMClass cls,
			    MMClass newCls) {
    return mgm.connect(cls, newCls, Association.class);
  }

  public Object addAssocClassLeft(MutableGraphModel mgm, MMClass cls,
			    MMClass newCls) {
    return mgm.connect(newCls, cls, Association.class);
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  public void mousePressed(MouseEvent me) {
    Handle h = new Handle(-1);
    hitHandle(me.getX(), me.getY(), 0, 0, h);
    _pressedButton = h.index;
    Editor ce = Globals.curEditor();
    ce.damaged(this);
  }

  public void mouseReleased(MouseEvent me) {
    if (_pressedButton < 10) return;
    Handle h = new Handle(-1);
    hitHandle(me.getX(), me.getY(), 0, 0, h);
    if (_pressedButton == h.index) {
      buttonClicked(_pressedButton);
    }
    _pressedButton = -1;
    Editor ce = Globals.curEditor();
    ce.damaged(this);
  }

  public void mouseEntered(MouseEvent me) {
    super.mouseEntered(me);
    _paintButtons = true;
    _useComposite = me.isShiftDown();
    Editor ce = Globals.curEditor();
    ce.damaged(this);
  }
  public void mouseExited(MouseEvent me) {
    super.mouseExited(me);
    _paintButtons = false;
    Editor ce = Globals.curEditor();
    ce.damaged(this);
  }

} /* end class SelectionClass */

