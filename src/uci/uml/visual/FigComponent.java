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

public class FigComponent extends FigNodeModelElement {

  public static int OVERLAP = 4;
  
  protected FigRect _bigPort;
  protected FigRect _cover;
  protected FigRect _upperRect;
  protected FigRect _lowerRect;
  protected FigText _stereo;

  public FigComponent() {
    _bigPort = new FigRect(10, 10, 120, 80, Color.cyan, Color.cyan);

    _cover = new FigRect(10, 10, 120, 80, Color.black, Color.white);
    _upperRect = new FigRect(0, 20, 20, 10, Color.black, Color.white);
    _lowerRect = new FigRect(0, 40, 20, 10, Color.black, Color.white);

    _stereo = new FigText(10,10,120,15,Color.black, "Times", 10);
    _stereo.setExpandOnly(true);
    _stereo.setFilled(false);
    _stereo.setLineWidth(0);
    _stereo.setEditable(false);
    _stereo.setHeight(15);

    _name.setLineWidth(0);
    _name.setFilled(false);
    
    addFig(_bigPort);
    addFig(_cover);
    addFig(_stereo);
    addFig(_name);  
    addFig(_upperRect);
    addFig(_lowerRect);

    Rectangle r = getBounds();
    setBounds(r.x, r.y, r.width, r.height);
  }

  public FigComponent(GraphModel gm, Object node) {
    this();
    setOwner(node);
  }

  public String placeString() { 
    return "new Component"; 
  }



  // Needs more work!!

  public Object clone() {
    FigComponent figClone = (FigComponent) super.clone();
    Vector v = figClone.getFigs();
    figClone._bigPort = (FigRect) v.elementAt(0);
    figClone._cover = (FigRect) v.elementAt(1);
    figClone._stereo = (FigText) v.elementAt(2);
    figClone._name = (FigText) v.elementAt(3);
    figClone._upperRect = (FigRect) v.elementAt(4);
    figClone._lowerRect = (FigRect) v.elementAt(5);

    return figClone;
  }
 
  public void setUnderline(boolean b) {
    _name.setUnderline(b);
  }

  public void setLineColor(Color c) {
//     super.setLineColor(c);
     _cover.setLineColor(c);
     _stereo.setFilled(false);
     _stereo.setLineWidth(0);
     _name.setFilled(false);
     _name.setLineWidth(0);
     _upperRect.setLineColor(c);
     _lowerRect.setLineColor(c);
  }

 
  public Selection makeSelection() {
      return new SelectionComponent(this);
  }

  public void setOwner(Object node) {
    super.setOwner(node);
    bindPort(node, _bigPort);
  }

  public Dimension getMinimumSize() {
    Dimension stereoDim = _stereo.getMinimumSize();
    Dimension nameDim = _name.getMinimumSize();

    int h = stereoDim.height + nameDim.height - OVERLAP;
    int w = Math.max(stereoDim.width, nameDim.width);

    return new Dimension(w, h);
  }

  public void setBounds(int x, int y, int w, int h) {
    if (_name == null) return;

    Rectangle oldBounds = getBounds();
    _bigPort.setBounds(x, y, w, h);
    _cover.setBounds(x, y, w, h);

    Dimension stereoDim = _stereo.getMinimumSize();
    Dimension nameDim = _name.getMinimumSize();

    _upperRect.setBounds(x-10, y+h/6, 20, 10);
    _lowerRect.setBounds(x-10, y+3*h/6, 20, 10); 

    _stereo.setBounds(x+1, y+1, w-2, stereoDim.height);
    _name.setBounds(x+1, y + stereoDim.height - OVERLAP + 1, w-2, nameDim.height);
    _x = x; _y = y; _w = w; _h = h;
    firePropChange("bounds", oldBounds, getBounds());
    updateEdges();
  }

  public void mouseClicked(MouseEvent me) {
    super.mouseClicked(me);
    setLineColor(Color.black);
  }

  public void mousePressed(MouseEvent me) {
    super.mousePressed(me);
    Editor ce = Globals.curEditor();
    Selection sel = ce.getSelectionManager().findSelectionFor(this);
    if (sel instanceof SelectionComponent) {
      ((SelectionComponent) sel).hideButtons();
    }
  }


  public void setEnclosingFig(Fig encloser) {
    super.setEnclosingFig(encloser);
    if (!(getOwner() instanceof MModelElementImpl)) return;
    if (getOwner() instanceof MComponentImpl) {
      MComponent me = (MComponent) getOwner();
      MNode mnode = null;

      if (encloser != null && (encloser.getOwner() instanceof MNodeImpl)) {
        mnode = (MNode) encloser.getOwner();
      }
      try {

        Collection nodes = me.getDeploymentLocations();  
        if ((nodes != null) && (nodes.size()>0) && (!(nodes.contains(mnode)))) {
          Iterator itnodes = nodes.iterator();
          while (itnodes.hasNext()) {
            MNode cls = (MNode) itnodes.next();
            me.removeDeploymentLocation(cls);
         }
        }

        if(mnode != null && (!(nodes.contains(mnode)))) {
          me.addDeploymentLocation(mnode);
        }                   
      }
      catch (Exception e) {
        System.out.println("could not set package");
      }
    }
  }


  public boolean getUseTrapRect() { return true; }
	
  protected void modelChanged() {
    super.modelChanged();
    updateStereotypeText();
  }

  public void updateStereotypeText() {
//    MClassifier mcl = (MClassifier) getOwner();
//    if (mcl == null) return;
//    Vector stereos = mcl.getStereotype();
//    if (stereos == null || stereos.size() == 0) {
//      _stereo.setText("");
//      return;
//    }
//    String stereoStr = ((MStereotype) stereos.elementAt(0)).getName();
//    if (stereoStr.length() == 0) _stereo.setText("");
//    else _stereo.setText("<<" + stereoStr + ">>");
  }

  static final long serialVersionUID = 1647392857462847651L;

}
