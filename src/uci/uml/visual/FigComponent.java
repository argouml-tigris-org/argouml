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



// File: FigComponent.java
// Classes: FigComponent
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$


package uci.uml.visual;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
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
import ru.novosoft.uml.foundation.extension_mechanisms.*;

/** Class to display graphics for a UML Component in a diagram. */

public class FigComponent extends FigNodeModelElement {

  ////////////////////////////////////////////////////////////////
  // instance variables
  public static int OVERLAP = 4;
  
  protected FigRect _bigPort;
  protected FigRect _cover;
  protected FigRect _upperRect;
  protected FigRect _lowerRect;
  protected FigText _stereo;


  ////////////////////////////////////////////////////////////////
  // constructors

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
    _name.setText("");
    
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
 
  ////////////////////////////////////////////////////////////////
  // acessors

  public void setUnderline(boolean b) {
    _name.setUnderline(b);
  }

  public void setLineColor(Color c) {
     //super.setLineColor(c);
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
    if (h<50) {
      _upperRect.setBounds(x-10, y+h/6, 20, 10);
      _lowerRect.setBounds(x-10, y+3*h/6, 20, 10); 
    }
    else {
      _upperRect.setBounds(x-10, y+13, 20, 10);
      _lowerRect.setBounds(x-10, y+39, 20, 10); 
    }

    _stereo.setBounds(x+1, y+1, w-2, stereoDim.height);
    _name.setBounds(x+1, y + stereoDim.height - OVERLAP + 1, w-2, nameDim.height);
    _x = x; _y = y; _w = w; _h = h;
    firePropChange("bounds", oldBounds, getBounds());
    updateEdges();
  }

  ////////////////////////////////////////////////////////////////
  // user interaction methods

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

    Vector figures = getEnclosedFigs();
    elementOrdering(figures);

    Vector contents = getLayer().getContents();
    int contentsSize = contents.size();
    for (int j=0; j<contentsSize; j++) {
      Object o = contents.elementAt(j);
      if (o instanceof FigEdgeModelElement) {
        FigEdgeModelElement figedge = (FigEdgeModelElement) o;
        figedge.getLayer().bringToFront(figedge);
      }
    }

    if (!(getOwner() instanceof MModelElementImpl)) return;
    if (getOwner() instanceof MComponentImpl) {
      MComponent me = (MComponent) getOwner();
      MNode mnode = null;

      if (encloser != null && (encloser.getOwner() instanceof MNodeImpl)) {
        mnode = (MNode) encloser.getOwner();
      }
      if (encloser != null && (encloser.getOwner() instanceof MComponentImpl)) {
        MComponent comp = (MComponent) encloser.getOwner();
        Collection compNodes = comp.getDeploymentLocations();
        Iterator it = compNodes.iterator();
        while (it.hasNext()) {
          mnode = (MNode) it.next();
        }
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
        setNode(figures);                   
      }
      catch (Exception e) {
        System.out.println("could not set package");
      }
    }
  }

  public void setNode(Vector figures) {
    int size = figures.size();
    if (figures != null && (size > 0)) {
      for (int i=0; i<size; i++) {
        Object o = figures.elementAt(i);
        if (o instanceof FigComponent) {
          FigComponent figcomp = (FigComponent) o;
          figcomp.setEnclosingFig(this);
        }
      }
    }    
  }

  public boolean getUseTrapRect() { return true; }
	

  ////////////////////////////////////////////////////////////////
  // internal methods

  protected void modelChanged() {
    super.modelChanged();
    updateStereotypeText();
  }

  public void updateStereotypeText() {
    MComponent comp = (MComponent) getOwner();
    if (comp == null) return;
    MStereotype stereo = comp.getStereotype();
    if (stereo == null) {
      _stereo.setText("");
      return;
    }
    if (stereo != null) {
      String stereoStr = stereo.getName();
      if (stereoStr.length() == 0) _stereo.setText("");
      else _stereo.setText("<<" + stereoStr + ">>");
    }
  }

  static final long serialVersionUID = 1647392857462847651L;

} /* end class FigComponent */

