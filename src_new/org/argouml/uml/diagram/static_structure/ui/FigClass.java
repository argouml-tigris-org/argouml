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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;

import org.argouml.uml.*;
import org.argouml.uml.generator.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.ui.*;

/** Class to display graphics for a UML Class in a diagram. */

public class FigClass extends FigNodeModelElement {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected FigText _attr;
  protected FigText _oper;
  protected FigRect _bigPort;
  protected FigRect _stereoLineBlinder;
  public MElementResidence resident = new MElementResidenceImpl();

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigClass() {
    _bigPort = new FigRect(10, 10, 90, 60, Color.cyan, Color.cyan);

    _name.setLineWidth(1);
    _name.setFilled(true);

//     _stereo.setLineWidth(0);
//     _stereo.setFilled(false);

    _attr = new FigText(10, 30, 90, 21);
    _attr.setFilled(true);
    _attr.setLineWidth(1);
    _attr.setExpandOnly(true);
    _attr.setFont(LABEL_FONT);
    _attr.setTextColor(Color.black);
    _attr.setJustification(FigText.JUSTIFY_LEFT);

    _oper = new FigText(10, 50, 90, 21);
    _oper.setFilled(true);
    _oper.setLineWidth(1);
    _oper.setExpandOnly(true);
    _oper.setFont(LABEL_FONT);
    _oper.setTextColor(Color.black);
    _oper.setJustification(FigText.JUSTIFY_LEFT);

    _stereo.setExpandOnly(true);
    _stereo.setFilled(true);
    _stereo.setLineWidth(1);
    _stereo.setEditable(false);
    _stereo.setHeight(18);
    _stereo.setDisplayed(false);

    _stereoLineBlinder = new FigRect(10, 15, 2, 60, Color.white, Color.white);
    _stereoLineBlinder.setLineWidth(2);
    //_stereoLineBlinder.setFilled(true);
    _stereoLineBlinder.setDisplayed(false);

    addFig(_bigPort);
    addFig(_stereo);
    addFig(_name);
    addFig(_stereoLineBlinder);
    addFig(_attr);
    addFig(_oper);

    Rectangle r = getBounds();
    setBounds(r.x, r.y, r.width, r.height);
  }

  public FigClass(GraphModel gm, Object node) {
    this();
    setOwner(node);
    if (node instanceof MClassifier && (((MClassifier)node).getName() != null))
	_name.setText(((MModelElement)node).getName());
  }

  public String placeString() { return "new Class"; }

  public Object clone() {
	  FigClass figClone = (FigClass) super.clone();
	  Vector v = figClone.getFigs();
	  figClone._bigPort = (FigRect) v.elementAt(0);
	  figClone._name = (FigText) v.elementAt(1);
	  figClone._attr = (FigText) v.elementAt(2);
	  figClone._oper = (FigText) v.elementAt(3);
	  return figClone;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public Selection makeSelection() {
    return new SelectionClass(this);
  }

  public Vector getPopUpActions(MouseEvent me) {
    Vector popUpActions = super.getPopUpActions(me);
    JMenu addMenu = new JMenu("Add");
    addMenu.add(Actions.AddAttribute);
    addMenu.add(Actions.AddOperation);
    popUpActions.insertElementAt(addMenu,
				 popUpActions.size() - 1);
    JMenu showMenu = new JMenu("Show");
    if(_attr.isDisplayed() && _oper.isDisplayed())
      showMenu.add(Actions.HideAllCompartments);
    else if(!_attr.isDisplayed() && !_oper.isDisplayed())
      showMenu.add(Actions.ShowAllCompartments);

    if (_attr.isDisplayed())
      showMenu.add(Actions.HideAttrCompartment);
    else
      showMenu.add(Actions.ShowAttrCompartment);

    if (_oper.isDisplayed())
      showMenu.add(Actions.HideOperCompartment);
    else
      showMenu.add(Actions.ShowOperCompartment);

    popUpActions.insertElementAt(showMenu, popUpActions.size() - 1);
    return popUpActions;
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
  public boolean isOperationVisible() { return _oper.isDisplayed(); }

  /**
   * Returns the status of the attribute field.
   * @return true if the attributes are visible, false otherwise
   */
  // created by Eric Lefevre 13 Mar 1999
  public boolean isAttributeVisible() {  return _attr.isDisplayed(); }

  // created by Eric Lefevre 13 Mar 1999
  public void setAttributeVisible(boolean isVisible) {
    Rectangle rect = getBounds();
    if ( _attr.isDisplayed() ) {
      if ( !isVisible ) {
        damage();
        int h = _attr.getBounds().height;
        _attr.setDisplayed(false);
        setBounds(rect.x, rect.y, rect.width, rect.height - h -1);
      }
    }
    else {
      if ( isVisible ) {
        int h = _attr.getBounds().height;
        _attr.setDisplayed(true);
        setBounds(rect.x, rect.y, rect.width, rect.height + h);
        damage();
      }
    }
  }

  // created by Eric Lefevre 13 Mar 1999
  public void setOperationVisible(boolean isVisible) {
    Rectangle rect = getBounds();
    if ( _oper.isDisplayed() ) {
      if ( !isVisible ) {
        damage();
        int h = _oper.getBounds().height;
        _oper.setDisplayed(false);
        setBounds(rect.x, rect.y, rect.width, rect.height - h -1);
      }
    }
    else {
      if ( isVisible ) {
        int h = _oper.getBounds().height;
        _oper.setDisplayed(true);
        setBounds(rect.x, rect.y, rect.width, rect.height + h);
        damage();
      }
    }
  }

  // modified by Eric Lefevre 13 Mar 1999
  public Dimension getMinimumSize() {
    Dimension nameMin = _name.getMinimumSize();
    Dimension attrMin;
    if ( _attr.isDisplayed() )
      attrMin = _attr.getMinimumSize();
    else
      attrMin = new Dimension();
    Dimension operMin;
    if ( _oper.isDisplayed() )
      operMin = _oper.getMinimumSize();
    else
      operMin = new Dimension();

    int h = nameMin.height;
    int w = nameMin.width;
    if ( _attr.isDisplayed() ) {
      h += attrMin.height;
      w = Math.max(w, attrMin.width);
    }
    if ( _oper.isDisplayed() ) {
      h += operMin.height;
      w = Math.max(w, operMin.width);
    }
    //    if ( int w = Math.max(Math.max(nameMin.width, attrMin.width), operMin.width);
    return new Dimension(w, h);
  }

    public void setFillColor(Color lColor) {
	super.setFillColor(lColor);
	_stereoLineBlinder.setLineColor(lColor);
    }

    public void setLineColor(Color lColor) {
	super.setLineColor(lColor);
	_stereoLineBlinder.setLineColor(_stereoLineBlinder.getFillColor());
    }

  public void translate(int dx, int dy) {
    super.translate(dx, dy);
    Editor ce = Globals.curEditor();
    Selection sel = ce.getSelectionManager().findSelectionFor(this);
    if (sel instanceof SelectionClass)
      ((SelectionClass)sel).hideButtons();
  }

//   public void setLineColor(Color c) {
//       //super.setLineColor(c);
//      _stereo.setFilled(false);
//      _stereo.setLineWidth(0);
//      _name.setFilled(false);
//      _name.setLineWidth(0);
//   }


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
    if (!(getOwner() instanceof MModelElement)) return;
    MModelElement me = (MModelElement) getOwner();
    MNamespace m = null;
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    if ((encloser == null && me.getNamespace() == null )
    ||  (encloser != null && encloser.getOwner() instanceof MPackage)) {
      if (encloser != null && (encloser.getOwner() instanceof MPackage)) {
        m = (MNamespace) encloser.getOwner();
      } else if ( pb.getTarget() instanceof UMLDiagram ) {
	m = (MNamespace) ((UMLDiagram)pb.getTarget()).getNamespace();
      }
      try {
        me.setNamespace(m);
      } catch (Exception e) {
        System.out.println("could not set package");
      }
    }

    // The next if-clause is important for the Deployment-diagram
    // it detects if the enclosing fig is a component, in this case
    // the ImplementationLocation will be set for the owning MClass
    if (encloser != null && (encloser.getOwner() instanceof MComponentImpl)) {
      MComponent component = (MComponent) encloser.getOwner();
      MClass cl = (MClass) getOwner();
      resident.setImplementationLocation(component);
      resident.setResident(cl);
    }
    else {
      resident.setImplementationLocation(null);
      resident.setResident(null);
    }     

  }


  ////////////////////////////////////////////////////////////////
  // internal methods

  protected void textEdited(FigText ft) throws PropertyVetoException {
    super.textEdited(ft);
    MClassifier cls = (MClassifier) getOwner();
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
    MClassifier cls = (MClassifier) getOwner();
    if (cls == null) return;
    // String clsNameStr = GeneratorDisplay.Generate(cls.getName());
    Collection strs = MMUtil.SINGLETON.getAttributes(cls);
    String attrStr = "";
    if (strs != null) {
	Iterator iter = strs.iterator();
      while (iter.hasNext()) {
	    MStructuralFeature sf = (MStructuralFeature) iter.next();
	    // sf.addMElementListener(this);
	    attrStr += GeneratorDisplay.Generate(sf);
	    if (iter.hasNext())
	      attrStr += "\n";
      }
    }
    Collection behs = MMUtil.SINGLETON.getOperations(cls);
    behs.removeAll(strs);
    String operStr = "";
    if (behs != null) {
	Iterator iter = behs.iterator();
      while (iter.hasNext()) {
	    MBehavioralFeature bf = (MBehavioralFeature) iter.next();
	    // bf.addMElementListener(this);
	    operStr += GeneratorDisplay.Generate(bf);
	    if (iter.hasNext())
	      operStr += "\n";
      }
    }

    _attr.setText(attrStr);
    _oper.setText(operStr);

    if (cls.isAbstract()) _name.setFont(ITALIC_LABEL_FONT);
    else _name.setFont(LABEL_FONT);

    calcBounds();
    Rectangle rect = getBounds();
    setBounds(rect.x, rect.y, rect.width, rect.height);
    firePropChange("bounds", rect, getBounds());

  }

  
  protected void updateStereotypeText() {
    MModelElement me = (MModelElement) getOwner();
    if (me == null) return;
    MStereotype stereo = me.getStereotype();
    if (stereo == null || stereo.getName() == null || stereo.getName().length() == 0) {
	if (! _stereo.isDisplayed()) return;
	Rectangle oldBounds = getBounds();
	_stereoLineBlinder.setDisplayed(false);
	_stereo.setDisplayed(false);
	setBounds(oldBounds.x, oldBounds.y + _stereo.getMinimumSize().height, oldBounds.width, oldBounds.height- _stereo.getMinimumSize().height);
	calcBounds();
	firePropChange("bounds", oldBounds, getBounds());
	
	return;
    }
    else {
	Rectangle oldBounds = getBounds();
	String stereoStr = stereo.getName();
	_stereo.setText("<<" + stereoStr + ">>");
	if (!_stereo.isDisplayed()) {
	    _stereoLineBlinder.setDisplayed(true);
	    _stereoLineBlinder.setBounds(oldBounds.x, oldBounds.y, oldBounds.width, 2);
	    _stereo.setDisplayed(true);
	    _stereo.setBounds(oldBounds.x, oldBounds.y -  _stereo.getMinimumSize().height, oldBounds.width,  _stereo.getMinimumSize().height);
	}
	_stereo.calcBounds();
	calcBounds();
	Rectangle rect = getBounds();
	setBounds(rect.x, rect.y, rect.width, rect.height);
	firePropChange("bounds", oldBounds, getBounds());
    }
  }

    public void setBounds(int x, int y, int w, int h) {
	
	Rectangle oldBounds = getBounds();
	Enumeration figs = _figs.elements();

	int maxW = w;
	int cumulatedHeight = 0;
	figs.nextElement(); // jump over _bigPort
	while (figs.hasMoreElements()) {
	  Fig f = (Fig) figs.nextElement();
	  if (f.isDisplayed()) {
	      maxW = Math.max(f.getMinimumSize().width, maxW);
	      cumulatedHeight += f.getMinimumSize().height;
	  }
	}
	if (_stereoLineBlinder.isDisplayed())
	    cumulatedHeight -= _stereoLineBlinder.getMinimumSize().height;

	int extra_each = h - cumulatedHeight;
	int noExtraFigs = 1; //this is the bigPort
        if (_stereo.isDisplayed()) 
	    noExtraFigs++;
        if (_stereoLineBlinder.isDisplayed()) 
	    noExtraFigs++;
	extra_each = extra_each / (getDisplayedFigs().size()- noExtraFigs);

	Vector mutableFigs = new Vector(_figs);
	mutableFigs.remove(_stereo);
	mutableFigs.remove(_stereoLineBlinder);
	mutableFigs.remove(_bigPort);

	figs = mutableFigs.elements();

	int currentY = y;

	// handle _stereo
	if (_stereo.isDisplayed()) {
	    _stereo.setBounds(x, currentY, maxW, _stereo.getMinimumSize().height+1 );
	    currentY += _stereo.getMinimumSize().height;
	}

	// handle _stereoLineBlinder
	if (_stereoLineBlinder.isDisplayed()) {
	    _stereoLineBlinder.setBounds(x + 1, _stereo.getY() + _stereo.getHeight() -2, maxW - 2, 4 );
	}

	while (figs.hasMoreElements()) {
	  Fig f = (Fig) figs.nextElement();
	  if (f.isDisplayed()) {
	      int height =  f.getMinimumSize().height + extra_each;
	      int overlap = 0;
	      if (_stereo.isDisplayed()) 
		  overlap =  (getDisplayedFigs().size() > 4? 1:0);
	      else  overlap =  (getDisplayedFigs().size() > 3? 1:0);
	      f.setBounds(x, currentY, maxW,  height+overlap);
	      currentY += height;
	  }
	}
	_bigPort.setBounds(x, y, maxW, currentY - y);
	

	calcBounds(); //_x = x; _y = y; _w = w; _h = h;
	updateEdges();
	firePropChange("bounds", oldBounds, getBounds());
    }


  

} /* end class FigClass */
