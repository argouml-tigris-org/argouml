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



// File: FigClass.java
// Classes: FigClass
// Original Author: abonner
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
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;

/** Class to display graphics for a UML Class in a diagram. */

public class FigClass extends FigNodeModelElement {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected FigText _attr;
  protected FigText _oper;
  protected FigRect _bigPort;

  protected FigText _lastVisible;
  protected boolean _attrVisible = true;
  protected boolean _operVisible = true;

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigClass() {
    _bigPort = new FigRect(10, 10, 90, 60, Color.cyan, Color.cyan);

    _attr = new FigText(10, 30, 90, 21);
    _attr.setFont(LABEL_FONT);
    _attr.setExpandOnly(true);
    _attr.setTextColor(Color.black);
    _attr.setJustification(FigText.JUSTIFY_LEFT);

    _oper = new FigText(10, 50, 90, 21);
    _oper.setFont(LABEL_FONT);
    _oper.setExpandOnly(true);
    _oper.setTextColor(Color.black);
    _oper.setJustification(FigText.JUSTIFY_LEFT);

    _lastVisible = _attr; // added by Eric Lefevre 13 Mar 1999

    addFig(_bigPort);
    addFig(_name);
    addFig(_attr);
    addFig(_oper);

    //Vector ports = gm.getPorts(node);
    //Object onlyPort = ports.firstElement();
    Rectangle r = getBounds();
    setBounds(r.x, r.y, r.width, r.height);

//     _attr.addPropertyChangeListener(this);
//     _oper.addPropertyChangeListener(this);
  }

  public FigClass(GraphModel gm, Object node) {
    this();
    setOwner(node);
  }

  public String placeString() { return "new Class"; }

  public Object clone() {
    FigClass figClone = (FigClass) super.clone();
    Vector v = figClone.getFigs();
    figClone._bigPort = (FigRect) v.elementAt(0);
    figClone._name = (FigText) v.elementAt(1);
    if (isAttributeVisible()) figClone._attr = (FigText) v.elementAt(2);
    if (isOperationVisible()) figClone._oper = (FigText) v.elementAt(3);
    return figClone;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public Selection makeSelection() {
    return new SelectionClass(this);
  }

  public void setOwner(Object node) {
    super.setOwner(node);
    Object onlyPort = node; //this kngl should be in the GraphModel
    bindPort(onlyPort, _bigPort);
  }

  public FigText getOperationFig() { return _oper; }
  public FigText getAttributeFig() { return _attr; }

  /**
   * Returns the status of the operation field.
   * @return true if the operations are visible, false otherwise
   */
  // created by Eric Lefevre 13 Mar 1999
  public boolean isOperationVisible() { return _operVisible; }

  /**
   * Returns the status of the attribute field.
   * @return true if the attributes are visible, false otherwise
   */
  // created by Eric Lefevre 13 Mar 1999
  public boolean isAttributeVisible() { return _attrVisible; }

  /**
   * Returns the height of a text element in the class icon.
   * This represents the size that the attribute part or the operator part could have.
   */
  // created by Eric Lefevre 13 Mar 1999
  private int figTextHeight() {
    return _lastVisible.getBounds().height;
  }

  /**
   * Sets the operation field visible or not in the class icon.
   * @param true to set the operations visible, false otherwise
   */
  // created by Eric Lefevre 13 Mar 1999
  public void setOperationVisible(boolean isVisible) {
    Rectangle rect = getBounds();
    if ( _operVisible ) {
      if ( !isVisible ) {
        damage();
        removeFig(_oper);
        int h = figTextHeight();
        _operVisible = false;
        setBounds(rect.x, rect.y, rect.width, rect.height - h -1);
        if ( !_attrVisible ) _lastVisible = _oper;
        else _lastVisible = _attr;
      }
    }
    else {
      if ( isVisible ) {
        int h = figTextHeight();
        addFig(_oper);
        _operVisible = true;
        setBounds(rect.x, rect.y, rect.width, rect.height + h);
        damage();
        _lastVisible = _oper;
      }
    }
  }

  /**
   * Sets the attribute field visible or not in the class icon.
   * @param true to set the attributes visible, false otherwise
   */
  // created by Eric Lefevre 13 Mar 1999
  public void setAttributeVisible(boolean isVisible) {
    Rectangle rect = getBounds();
    if ( _attrVisible ) {
      if ( !isVisible ) {
        damage();
        int h = figTextHeight();
        removeFig(_attr);
        _attrVisible = false;
        setBounds(rect.x, rect.y, rect.width, rect.height - h);
        if ( !_operVisible ) _lastVisible = _attr;
        else _lastVisible = _oper;
      }
    }
    else {
      if ( isVisible ) {
        addFig(_attr);
        int h = figTextHeight();
        _attrVisible = true;
        setBounds(rect.x, rect.y, rect.width, rect.height + h);
        damage();
        _lastVisible = _attr;
      }
    }
  }

  // modified by Eric Lefevre 13 Mar 1999
  public Dimension getMinimumSize() {
    Dimension nameMin = _name.getMinimumSize();
    Dimension attrMin;
    if ( _attrVisible )
      attrMin = _attr.getMinimumSize();
    else
      attrMin = new Dimension();
    Dimension operMin;
    if ( _operVisible )
      operMin = _oper.getMinimumSize();
    else
      operMin = new Dimension();

    int h = nameMin.height;
    int w = nameMin.width;
    if ( _attrVisible ) {
      h += attrMin.height;
      w = Math.max(w, attrMin.width);
    }
    if ( _operVisible ) {
      h += operMin.height;
      w = Math.max(w, operMin.width);
    }
    //    if ( int w = Math.max(Math.max(nameMin.width, attrMin.width), operMin.width);
    return new Dimension(w, h);
  }

  public void translate(int dx, int dy) {
    super.translate(dx, dy);
    Editor ce = Globals.curEditor();
    Selection sel = ce.getSelectionManager().findSelectionFor(this);
    if (sel instanceof SelectionClass)
      ((SelectionClass)sel).hideButtons();
  }

  /* Override setBounds to keep shapes looking right */
  // modified by Eric Lefevre 13 Mar 1999
  public void setBounds(int x, int y, int w, int h) {
    if (_name == null) return;
    Rectangle oldBounds = getBounds();
    Dimension nameMin = _name.getMinimumSize();

    Dimension attrMin;
    if ( _attrVisible )
      attrMin = _attr.getMinimumSize();
    else
      attrMin = new Dimension();

    Dimension operMin;
    if ( _operVisible )
      operMin = _oper.getMinimumSize();
    else
      operMin = new Dimension();

    int extra_total = h - nameMin.height - attrMin.height - operMin.height;
    int extra_each = extra_total/2;
    if ( _attrVisible ^ _operVisible )
      extra_each = extra_total;

    _name.setBounds(x, y, w, nameMin.height);

    int attributeIncrement;
    int increment2;
    if ( _attrVisible ) {
      _attr.setBounds(x, y + _name.getBounds().height - 1,
                      w, attrMin.height + extra_each + 1);
      attributeIncrement = _attr.getBounds().height -1;
      increment2 = 2;
    }
    else {
      attributeIncrement = 0;
      increment2 = 1;
    }
    if ( _operVisible )
      _oper.setBounds(x, y + attributeIncrement +
                      _name.getBounds().height - 1,
		      w, operMin.height + extra_each + increment2);
    if ( (!_attrVisible) && (!_operVisible) )
      h = nameMin.height;
    _bigPort.setBounds(x+1, y+1, w-2, h-2);

    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
    updateEdges();
    firePropChange("bounds", oldBounds, getBounds());
  }

  ////////////////////////////////////////////////////////////////
  // user interaction methods

  public void mousePressed(MouseEvent me) {
    super.mousePressed(me);
    Editor ce = Globals.curEditor();
    Selection sel = ce.getSelectionManager().findSelectionFor(this);
    if (sel instanceof SelectionClass)
      ((SelectionClass)sel).hideButtons();
  }

  public void setEnclosingFig(Fig encloser) {
    super.setEnclosingFig(encloser);
    if (!(getOwner() instanceof ModelElement)) return;
    ModelElement me = (ModelElement) getOwner();
    Namespace m = null;
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    if (encloser != null && (encloser.getOwner() instanceof Model)) {
      m = (Namespace) encloser.getOwner();
    }
    else {
      if (pb.getTarget() instanceof UMLDiagram) {
	m = (Namespace) ((UMLDiagram)pb.getTarget()).getNamespace();
      }
    }
    try {
      me.setNamespace(m);
    }
    catch (Exception e) {
      System.out.println("could not set package");
    }
  }


  ////////////////////////////////////////////////////////////////
  // internal methods

  protected void textEdited(FigText ft) throws PropertyVetoException {
    super.textEdited(ft);
    Classifier cls = (Classifier) getOwner();
    if (cls == null) return;
    if (ft == _attr) {
      //System.out.println("\n\n\n Edited Attr");
      String s = ft.getText();
      ParserDisplay.SINGLETON.parseAttributeCompartment(cls, s);
    }
    if (ft == _oper) {
      String s = ft.getText();
      ParserDisplay.SINGLETON.parseOperationCompartment(cls, s);
    }
  }

  protected void modelChanged() {
    super.modelChanged();
    Classifier cls = (Classifier) getOwner();
    if (cls == null) return;
    //    String clsNameStr = GeneratorDisplay.Generate(cls.getName());
    Vector strs = cls.getStructuralFeature();
    String attrStr = "";
    if (strs != null) {
      java.util.Enumeration enum = strs.elements();
      while (enum.hasMoreElements()) {
	    StructuralFeature sf = (StructuralFeature) enum.nextElement();
	    attrStr += GeneratorDisplay.Generate(sf);
	    if (enum.hasMoreElements())
	      attrStr += "\n";
      }
    }
    Vector behs = cls.getBehavioralFeature();
    String operStr = "";
    if (behs != null) {
      java.util.Enumeration enum = behs.elements();
      while (enum.hasMoreElements()) {
	    BehavioralFeature bf = (BehavioralFeature) enum.nextElement();
	    operStr += GeneratorDisplay.Generate(bf);
	    if (enum.hasMoreElements())
	      operStr += "\n";
      }
    }

    _attr.setText(attrStr);
    _oper.setText(operStr);

    if (cls.getIsAbstract()) _name.setFont(ITALIC_LABEL_FONT);
    else _name.setFont(LABEL_FONT);

  }

} /* end class FigClass */
