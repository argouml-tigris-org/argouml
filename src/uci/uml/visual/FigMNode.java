package uci.uml.visual;

import java.awt.*;
import java.awt.event.*;
import com.sun.java.util.collections.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import uci.gef.*;
import uci.graph.*;
import uci.argo.kernel.*;
import uci.uml.ui.*;
import uci.uml.generate.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;

public class FigMNode extends FigNodeModelElement {

  protected FigRect _bigPort;
  protected FigCube _cover;
  protected FigRect _test;

  public FigMNode() {
    _bigPort = new FigRect(10, 10, 200, 180);
    _cover = new FigCube(10, 10, 200, 180, Color.black, Color.white);
    _test = new FigRect(10,10,1,1, Color.black, Color.white);

    _name.setLineWidth(0);
    _name.setFilled(false);
    _name.setJustification(0);

    addFig(_bigPort);
    addFig(_cover);
    addFig(_name);
    addFig(_test);

    Rectangle r = getBounds();
    setBounds(r.x, r.y, r.width, r.height);
  }

  public FigMNode(GraphModel gm, Object node) {
    this();
    setOwner(node);
  }

  public String placeString() { return "new Node"; }


  // Needs more work!!

  public Object clone() {
    FigMNode figClone = (FigMNode) super.clone();
    Vector v = figClone.getFigs();
    figClone._bigPort = (FigRect) v.elementAt(0);
    figClone._cover = (FigCube) v.elementAt(1);
    figClone._name = (FigText) v.elementAt(2);
    figClone._test = (FigRect) v.elementAt(3);
    return figClone;
  }	

  public void setLineColor(Color c) {
//     super.setLineColor(c);
     _cover.setLineColor(c);
     _name.setFilled(false);
     _name.setLineWidth(0);
     _test.setLineColor(c);
  }

  public Selection makeSelection() {
      return new SelectionNode(this);
  }

  public void setOwner(Object node) {
    super.setOwner(node);
    bindPort(node, _bigPort);
  }

  public Dimension getMinimumSize() {
    Dimension nameDim = _name.getMinimumSize();
    int w = nameDim.width + 20;
    int h = nameDim.height + 20;
    return new Dimension(w, h);
  }

  public void setBounds(int x, int y, int w, int h) {
    if (_name == null) return;

    Rectangle oldBounds = getBounds();
    _bigPort.setBounds(x, y, w, h);
    _cover.setBounds(x, y, w, h);
    Dimension nameDim = _name.getMinimumSize();
    _name.setBounds(x, y, w, nameDim.height);
    _x = x; _y = y; _w = w; _h = h;
    firePropChange("bounds", oldBounds, getBounds());
    updateEdges();
  }

  public void mouseClicked(MouseEvent me) {
    super.mouseClicked(me);
    setLineColor(Color.black);
  }

/*
  public void setEnclosingFig(Fig encloser) {
    super.setEnclosingFig(encloser);
    if (!(getOwner() instanceof ModelElement)) return;
    MMNode me = (MMNode) getOwner();

    if (encloser != null && (encloser.getOwner() instanceof ModelElement)) {
      Rectangle oldBounds = getBounds();
      Rectangle ownerBounds = encloser.getBounds();
      Rectangle newBounds = getNewBounds(ownerBounds);
      setBounds(newBounds.x, newBounds.y, newBounds.width, newBounds.height);
      firePropChange("bounds", oldBounds, getBounds());
    }

  }
*/

  public boolean getUseTrapRect() { return true; }
	
  static final long serialVersionUID = 8822005566372687713L;

}
