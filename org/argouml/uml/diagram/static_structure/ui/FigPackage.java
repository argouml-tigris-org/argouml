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

// File: FigPackage.java
// Classes: FigPackage
// Original Author: agauthie@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;

import org.argouml.application.api.*;
import org.argouml.uml.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.generator.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.ui.*;

/** Class to display graphics for a UML MState in a diagram. */

public class FigPackage extends FigNodeModelElement {

  ////////////////////////////////////////////////////////////////
  // constants

  public final int MARGIN = 2;
  public int x = 0;
  public int y = 0;
  public int width = 140;
  public int height = 100;
  public int indentX = 50;
  public int indentY = 20;
  public int textH = 20;
  protected int _radius = 20;

  ////////////////////////////////////////////////////////////////
  // instance variables

  FigText _body;

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigPackage() {
    Color handleColor = Globals.getPrefs().getHandleColor();
    _bigPort = new FigRect(x, y, width, height, null, null) {
      public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.black);
        g.fillRect(_body.getX()+_shadowSize,_body.getY()+_body.getHeight(),_body.getWidth()-_shadowSize,_shadowSize);
        g.fillRect(_body.getX()+_body.getWidth(),_body.getY()+_shadowSize,_shadowSize,_body.getHeight());
        if (_name.getHeight() > _shadowSize)
          g.fillRect(_name.getX()+_name.getWidth(),_name.getY()+_shadowSize,_shadowSize,_name.getHeight()-_shadowSize);
      }
    };
    _body = new FigText(x, y + textH, width, height - textH);
    _body.setEditable(false);
    _name.setBounds(x, y, width - indentX, textH + 2);
    _name.setJustification(FigText.JUSTIFY_LEFT);

    _bigPort.setFilled(false);
    _bigPort.setLineWidth(0);

    // add Figs to the FigNode in back-to-front order
    addFig(_bigPort);
    addFig(_name);
    addFig(_body);

    setBlinkPorts(false); //make port invisble unless mouse enters
    Rectangle r = getBounds();
    setBounds(r.x, r.y, r.width, r.height);
    updateEdges();
  }

  public FigPackage(GraphModel gm, Object node) {
    this();
    setOwner(node);

    // If this figure is created for an existing package node in the
    // metamodel, set the figure's name according to this node. This is
    // used when the user click's on 'add to diagram' in the navpane.
    // Don't know if this should rather be done in one of the super
    // classes, since similar code is used in FigClass.java etc.
    // Andreas Rueckert <a_rueckert@gmx.net>
    if (node instanceof MPackage && (((MPackage)node).getName() != null))
        _name.setText(((MModelElement)node).getName());
  }

  public String placeString() { return "new Package"; }

  public Object clone() {
    FigPackage figClone = (FigPackage) super.clone();
    Vector v = figClone.getFigs();
    figClone._bigPort = (FigRect) v.elementAt(0);
    figClone._name = (FigText) v.elementAt(1);
    figClone._body = (FigText) v.elementAt(2);
    return figClone;
  }

  ////////////////////////////////////////////////////////////////
  // Fig accessors

  public void setLineColor(Color col) {
    _name.setLineColor(col);
    _body.setLineColor(col);
  }
 public Color getLineColor() { return _body.getLineColor(); }

  public void setFillColor(Color col) {
    _name.setFillColor(col);
    _body.setFillColor(col);
  }
  public Color getFillColor() { return _body.getFillColor(); }

  public void setFilled(boolean f) {
    _name.setFilled(f);
    _body.setFilled(f);
  }
  public boolean getFilled() { return _body.getFilled(); }

  public void setLineWidth(int w) {
    _name.setLineWidth(w);
    _body.setLineWidth(w);
  }
  public int getLineWidth() { return _body.getLineWidth(); }

  ////////////////////////////////////////////////////////////////
  // user interaction methods

  public void setEnclosingFig(Fig encloser) {
    super.setEnclosingFig(encloser);
    if (!(getOwner() instanceof MModelElement)) return;
    MModelElement me = (MModelElement) getOwner();
    MNamespace m = null;
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    if (encloser != null && (encloser.getOwner() instanceof MPackage)) {
      m = (MNamespace) encloser.getOwner();
    }
    else {
      if (pb.getTarget() instanceof UMLDiagram) {
	m = (MNamespace) ((UMLDiagram)pb.getTarget()).getNamespace();
      }
    }
    try {
       // If moved into an Package
        if (encloser.getOwner() instanceof MPackage) {
             me.setNamespace(m);
        }

        // If default Namespace is not already set
        if (me.getNamespace() == null) {
          me.setNamespace(m);
        }
    }
    catch (Exception e) {
      System.out.println("could not set package");
    }
  }

  ////////////////////////////////////////////////////////////////
  // accessor methods

  public boolean getUseTrapRect() { return true; }

  public Dimension getMinimumSize() {
    Dimension nameMinimum = _name.getMinimumSize();

    int widthP = nameMinimum.width + indentX;
    int heightP = nameMinimum.height + height - textH;
    return new Dimension(widthP, heightP);
  }

  public void setBounds(int x, int y, int w, int h) {
    if (_name == null) return;
    Rectangle oldBounds = getBounds();
    Dimension nameMinimum = _name.getMinimumSize();

    _name.setBounds(x, y, w - indentX, nameMinimum.height + 2);
    _body.setBounds(x, y+1 + nameMinimum.height,
		    w, h - 1 - nameMinimum.height);
    _bigPort.setBounds(x+1, y+1,
		       w-2, h - 2);

    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
    firePropChange("bounds", oldBounds, getBounds());
  }

  /**
    * Build a collection of menu items relevant for a right-click popup menu on a Package.
    *
    * @param     me     a mouse event
    * @return          a collection of menu items
    */
  public Vector getPopUpActions(MouseEvent me) {
    Vector popUpActions = super.getPopUpActions(me);
    MPackage mclass = (MPackage) getOwner();
    ArgoJMenu modifierMenu = new ArgoJMenu("Modifiers");

    modifierMenu.addCheckItem(new ActionModifier("Abstract", "isAbstract", "isAbstract", "setAbstract", mclass));
    modifierMenu.addCheckItem(new ActionModifier("Final", "isLeaf", "isLeaf", "setLeaf", mclass));
    modifierMenu.addCheckItem(new ActionModifier("Root", "isRoot", "isRoot", "setRoot", mclass));

    popUpActions.insertElementAt(modifierMenu, popUpActions.size() - 1);

    return popUpActions;
  }


} /* end class FigPackage */

