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

package org.argouml.uml.diagram.deployment.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;

import org.argouml.application.api.*;
import org.argouml.uml.diagram.ui.*;

/** Class to display graphics for a UML Component in a diagram. */

public class FigComponent extends FigNodeModelElement {

/** The distance between the left edge of the fig and the left edge of the
  	  main rectangle. */
  private static final int BIGPORT_X = 10;

  public static int OVERLAP = 4;

  protected FigRect _cover;
  protected FigRect _upperRect;
  protected FigRect _lowerRect;


  ////////////////////////////////////////////////////////////////
  // constructors

  public FigComponent() {
    _cover = new FigRect(BIGPORT_X, 10, 120, 80, Color.black, Color.white);
    _upperRect = new FigRect(0, 20, 20, 10, Color.black, Color.white);
    _lowerRect = new FigRect(0, 40, 20, 10, Color.black, Color.white);

    _name.setLineWidth(0);
    _name.setFilled(false);
    _name.setText( placeString() );

    addFig(_bigPort);
    addFig(_cover);
    addFig(_stereo);
    addFig(_name);
    addFig(_upperRect);
    addFig(_lowerRect);
  }

  // Why not just super( gm, node ) instead?? (ChL)
  public FigComponent(GraphModel gm, Object node) {
    this();
    setOwner(node);
    if (node instanceof MClassifier && (((MClassifier)node).getName() != null))
	_name.setText(((MModelElement)node).getName());
//     _name.setText(placeString());
    updateBounds();
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

  public Dimension getMinimumSize() {
    Dimension stereoDim = _stereo.getMinimumSize();
    Dimension nameDim = _name.getMinimumSize();

    int h = stereoDim.height + nameDim.height - OVERLAP;
    int w = Math.max(stereoDim.width, nameDim.width) + BIGPORT_X;

    return new Dimension(w, h);
  }

  public void setBounds(int x, int y, int w, int h) {

    Rectangle oldBounds = getBounds();
    _bigPort.setBounds(x + BIGPORT_X, y, w - BIGPORT_X, h);
    _cover.setBounds(x + BIGPORT_X, y, w - BIGPORT_X, h);

    Dimension stereoDim = _stereo.getMinimumSize();
    Dimension nameDim = _name.getMinimumSize();
    if (h<50) {
      _upperRect.setBounds(x, y+h/6, 20, 10);
      _lowerRect.setBounds(x, y+3*h/6, 20, 10);
    }
    else {
      _upperRect.setBounds(x, y+13, 20, 10);
      _lowerRect.setBounds(x, y+39, 20, 10);
    }

    _stereo.setBounds(x+BIGPORT_X+1, y+1, w-BIGPORT_X-2, stereoDim.height);
    _name.setBounds(x+BIGPORT_X+1, y + stereoDim.height - OVERLAP + 1, w-BIGPORT_X-2,
    		 nameDim.height);
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

    if (getLayer() != null) {
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
    }

    if (!(getOwner() instanceof MModelElement)) return;
    if (getOwner() instanceof MComponent) {
      MComponent me = (MComponent) getOwner();
      MNode mnode = null;

      if (encloser != null && (encloser.getOwner() instanceof MNode)) {
        mnode = (MNode) encloser.getOwner();
      }
      if (encloser != null && (encloser.getOwner() instanceof MComponent)) {
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
  }

  protected void updateStereotypeText() {
    MModelElement me = (MModelElement) getOwner();
    if (me == null) return;
    MStereotype stereo = me.getStereotype();
    if (stereo == null || stereo.getName() == null || stereo.getName().length() == 0)
        _stereo.setText("");
    else {
        _stereo.setText(Notation.generateStereotype(this, stereo));
    }
  }



  /** Get the rectangle on whose corners the dragging handles are to be drawn.
  	  Used by Selection Resize. */
  public Rectangle getHandleBox() {

  	Rectangle r = getBounds();
  	return new Rectangle( r. x + BIGPORT_X, r. y, r. width - BIGPORT_X, r. height );

  }

  public void setHandleBox( int x, int y, int w, int h ) {

  	setBounds( x - BIGPORT_X, y, w + BIGPORT_X, h );

  }


  static final long serialVersionUID = 1647392857462847651L;

} /* end class FigComponent */

