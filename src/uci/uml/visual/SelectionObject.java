
package uci.uml.visual;

import com.sun.java.util.collections.*;
import java.awt.*;
import java.awt.event.*;
//import java.awt.image.*;
import javax.swing.Icon;

import uci.gef.*;
import uci.graph.*;
import uci.util.Util;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.common_behavior.*;

public class SelectionObject extends SelectionWButtons {
  ////////////////////////////////////////////////////////////////
  // constants
  public static Icon dep = Util.loadIconResource("Link");



  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new SelectionObject for the given Fig */
  public SelectionObject(Fig f) { super(f); }

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
      h.instructions = "Add an object";
    }
    else if (hitBelow(cx + cw/2, cy + ch, aw, ah, r)) {
      h.index = 11;
      h.instructions = "Add an object";
    }
    else if (hitLeft(cx + cw, cy + ch/2, aw, ah, r)) {
      h.index = 12;
      h.instructions = "Add an object";
    }
    else if (hitRight(cx, cy + ch/2, aw, ah, r)) {
      h.index = 13;
      h.instructions = "Add an object";
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
    Class nodeClass = MObject.class;
    int bx = mX, by = mY;
    boolean reverse = false;
    switch (hand.index) {
    case 10: //add link
      edgeClass = MLinkImpl.class;
      by = cy;
      bx = cx + cw/2;
      break;
    case 11: //add link
      edgeClass = MLinkImpl.class;
      reverse = true;
      by = cy + ch;
      bx = cx + cw/2;
      break;
    case 12: //add link
      edgeClass = MLinkImpl.class;
      by = cy + ch/2;
      bx = cx + cw;
      break;
    case 13: // add link
      edgeClass = MLinkImpl.class;
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
    MObject newNode = new MObjectImpl();
    FigObject fc = (FigObject) _content;
    MObject cls = (MObject) fc.getOwner();

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
      lay.bumpOffOtherNodesIn(newFC, outputRect, false, true);
    }
    else if (buttonCode == 11) {
      newFC.setLocation(fc.getX(), fc.getY() + fc.getHeight() + 100);
      outputRect.y = fc.getY() + fc.getHeight() + 100;
      outputRect.height = 200;
      lay.bumpOffOtherNodesIn(newFC, outputRect, false, true);
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

    if (buttonCode == 10) newEdge = addObjectClassAbove(mgm, cls, newNode);
    else if (buttonCode == 11) newEdge = addObjectClassBelow(mgm, cls, newNode);
    else if (buttonCode == 12) newEdge = addObjectClassRight(mgm, cls, newNode);
    else if (buttonCode == 13) newEdge = addObjectClassLeft(mgm, cls, newNode);

    FigEdge fe = (FigEdge) lay.presentationFor(newEdge);
    edgeShape.setLineColor(Color.black);
    edgeShape.setFilled(false);
    edgeShape._isComplete = true;
    fe.setFig(edgeShape);
    ce.getSelectionManager().select(fc);
  }

  public Object addObjectClassAbove(MutableGraphModel mgm, MObject cls,
			    MObject newCls) {
    return mgm.connect(cls, newCls, MLinkImpl.class);
  }

  public Object addObjectClassBelow(MutableGraphModel mgm, MObject cls,
			    MObject newCls) {
    return mgm.connect(newCls, cls, MLinkImpl.class);
  }

  public Object addObjectClassRight(MutableGraphModel mgm, MObject cls,
			    MObject newCls) {
    return mgm.connect(cls, newCls, MLinkImpl.class);
  }

  public Object addObjectClassLeft(MutableGraphModel mgm, MObject cls,
			    MObject newCls) {
    return mgm.connect(newCls, cls, MLinkImpl.class);
  }


} /* end class SelectionObject */

