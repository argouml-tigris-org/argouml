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
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.model_management.*;

public class FigComponentInstance extends FigNodeModelElement {

  public static int OVERLAP = 4;
  
  protected FigRect _bigPort;
  protected FigRect _cover;
  protected FigRect _upperRect;
  protected FigRect _lowerRect;
  protected FigText _stereo;

  public FigComponentInstance() {
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
    _name.setUnderline(true);
    
    addFig(_bigPort);
    addFig(_cover);
    addFig(_stereo);
    addFig(_name);  
    addFig(_upperRect);
    addFig(_lowerRect);

    Rectangle r = getBounds();
    setBounds(r.x, r.y, r.width, r.height);
  }

  public FigComponentInstance(GraphModel gm, Object node) {
    this();
    setOwner(node);
  }

  public String placeString() { 
    return "new ComponentInstance";
  }



  // Needs more work!!

  public Object clone() {
    FigComponentInstance figClone = (FigComponentInstance) super.clone();
    Vector v = figClone.getFigs();
    figClone._bigPort = (FigRect) v.elementAt(0);
    figClone._cover = (FigRect) v.elementAt(1);
    figClone._stereo = (FigText) v.elementAt(2);
    figClone._name = (FigText) v.elementAt(3);
    figClone._upperRect = (FigRect) v.elementAt(4);
    figClone._lowerRect = (FigRect) v.elementAt(5);

    return figClone;
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
      return new SelectionComponentInstance(this);
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
    if (sel instanceof SelectionComponentInstance)
      ((SelectionComponentInstance)sel).hideButtons();
  }


  public void setEnclosingFig(Fig encloser) {
    super.setEnclosingFig(encloser);
    if (!(getOwner() instanceof MModelElement)) return;
    if (getOwner() instanceof MComponentInstanceImpl) {
      MComponentInstance me = (MComponentInstance) getOwner();
      MNodeInstance mnode = null;

      if (encloser != null && (encloser.getOwner() instanceof MNodeInstanceImpl)) {
        mnode = (MNodeInstance) encloser.getOwner();
      }
      try {
        if(mnode != null) {
          me.setNodeInstance(mnode);
        }
        else {
          if (me.getNodeInstance() != null) {
            me.setNodeInstance(null);
          }
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
    MInstance minst = (MInstance) getOwner();
    if (minst == null) return;
//    Vector stereos = mcl.getStereotype();
//    if (stereos == null || stereos.size() == 0) {
//      _stereo.setText("");
//      return;
//    }
//    String stereoStr = ((Stereotype) stereos.elementAt(0)).getName().getBody();
//    if (stereoStr.length() == 0) _stereo.setText("");
//    else _stereo.setText("<<" + stereoStr + ">>");
  }

  static final long serialVersionUID = 1647392857462847651L;

}
