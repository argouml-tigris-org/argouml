
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

/** Class to display graphics for a UML collaboration in a diagram. */

public class FigObject extends FigNodeModelElement {

  ////////////////////////////////////////////////////////////////
  // constants
  public int PADDING = 5;
  ////////////////////////////////////////////////////////////////
  // instance variables

  FigRect _bigPort;
  FigRect _cover;

  // add other Figs here aes needed

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigObject() {
    _bigPort = new FigRect(10, 10, 90, 50, Color.cyan, Color.cyan);
    _cover = new FigRect(10, 10, 90, 50, Color.black, Color.white);
    _name.setLineWidth(0);
    _name.setFilled(false);
    _name.setUnderline(true);
    Dimension nameMin = _name.getMinimumSize();
    _name.setBounds(10, 10, nameMin.width+20, nameMin.height);

    // add Figs to the FigNode in back-to-front order
    addFig(_bigPort);
    addFig(_cover);
    addFig(_name);

    Rectangle r = getBounds();
    setBounds(r.x, r.y, nameMin.width, nameMin.height);
  }

  public FigObject(GraphModel gm, Object node) {
    this();
    setOwner(node);
  }

  public String placeString() { return "new Object"; }

  public Object clone() {
    FigObject figClone = (FigObject) super.clone();
    Vector v = figClone.getFigs();
    figClone._bigPort = (FigRect) v.elementAt(0);
    figClone._cover = (FigRect) v.elementAt(1);
    figClone._name = (FigText) v.elementAt(2);
    return figClone;
  }


  ////////////////////////////////////////////////////////////////
  // Fig accessors

  public void setLineColor(Color col) { _cover.setLineColor(col); }
  public Color getLineColor() { return _cover.getLineColor(); }

  public void setFillColor(Color col) { _cover.setFillColor(col); }
  public Color getFillColor() { return _cover.getFillColor(); }

  public void setFilled(boolean f) { _cover.setFilled(f); }
  public boolean getFilled() { return _cover.getFilled(); }

  public void setLineWidth(int w) { _cover.setLineWidth(w); }
  public int getLineWidth() { return _cover.getLineWidth(); }

  
  public Selection makeSelection() {
    return new SelectionObject(this);
  }

  public void setOwner(Object node) {
    super.setOwner(node);
    Object onlyPort = node;
    bindPort(onlyPort, _bigPort);
  }

  public Dimension getMinimumSize() {
    Dimension bigPortMin = _bigPort.getMinimumSize();
    Dimension coverMin = _cover.getMinimumSize();
    Dimension nameMin = _name.getMinimumSize();

    int w = nameMin.width + 10;
    int h = nameMin.height + 5;
    return new Dimension(w, h);
  }

  /* Override setBounds to keep shapes looking right */
  public void setBounds(int x, int y, int w, int h) {
    if (_name == null) return;

    Rectangle oldBounds = getBounds();

    Dimension nameMin = _name.getMinimumSize();

    _bigPort.setBounds(x, y, w, h);
    _cover.setBounds(x, y, w, h);
    _name.setBounds(x, y, nameMin.width+10, nameMin.height);

    //_bigPort.setBounds(x+1, y+1, w-2, h-2);
    _x = x; _y = y; _w = w; _h = h;

    firePropChange("bounds", oldBounds, getBounds());
    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
    updateEdges();
  }

  
  ////////////////////////////////////////////////////////////////
  // event handlers

  protected void textEdited(FigText ft) throws PropertyVetoException {
    super.textEdited(ft);
//    MMObject cls = (MMObject) getOwner();
//    if (ft == _name) {
//       String s = ft.getText();
//      ParserDisplay.SINGLETON.parseMMObject(cls, s);
//    }
  }

  public void setEnclosingFig(Fig encloser) {
    super.setEnclosingFig(encloser);
    if (!(getOwner() instanceof MModelElement)) return;
    if (getOwner() instanceof MObjectImpl) {
      MObject me = (MObject) getOwner();
      MComponentInstance mcomp = null;

      if (encloser != null && (encloser.getOwner() instanceof MComponentInstanceImpl)) {
        mcomp = (MComponentInstance) encloser.getOwner();
      }
      try {
        if(mcomp != null) {
          me.setComponentInstance(mcomp);
        }
        else {
          if (me.getComponentInstance() != null) {
            me.setComponentInstance(null);
          }
        }
      }
      catch (Exception e) {
        System.out.println("could not set package");
      }
    }
  }

  protected void modelChanged() {
    super.modelChanged();
//    MMObject cr = (MMObject) getOwner();
//    if (cr == null) return;
//    String nameStr = GeneratorDisplay.Generate(cr.getName()).trim();
//    String baseString = cr.getBaseString().trim();
//    if (_readyToEdit) {
//      if( nameStr == "" && baseString == "")
//	_name.setText("");
//      else
//	_name.setText(nameStr.trim() + " : " + baseString);
//    }
  }

//  public void keyPressed(KeyEvent ke) {}




  static final long serialVersionUID = -185736690375678962L;

} /* end class FigMMObject */
