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

import org.argouml.application.api.*;
import org.argouml.uml.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.generator.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.ui.*;

/** Class to display graphics for a UML Class in a diagram. */

public class FigClass extends FigNodeModelElement {

  ////////////////////////////////////////////////////////////////
  // constants

  private static final int ROWHEIGHT = 17; // min. 17, used to calculate y pos of FigText items in _attrVec and _operVec
  private static final int STEREOHEIGHT = 18;

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected FigGroup _attrVec;
  protected FigGroup _operVec;
  protected FigRect _bigPort;
  protected FigRect _attrBigPort;
  protected FigRect _operBigPort;
  protected FigRect _stereoLineBlinder;
  public MElementResidence resident = new MElementResidenceImpl();
  private boolean suppressCalcBounds = false;

  protected Vector mAttrs = new Vector();
  protected Vector mOpers = new Vector();

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigClass() {
    // this rectangle marks the whole class figure; everything is inside it:
    _bigPort = new FigRect(10, 10, 60, 20+2*ROWHEIGHT, Color.cyan, Color.cyan);

    _name.setLineWidth(1);
    _name.setFilled(true);

    // this rectangle marks the attribute section; all attributes are inside it:
    _attrBigPort = new FigRect(10, 30, 60, ROWHEIGHT+1, Color.black, Color.white);
    _attrBigPort.setFilled(true);
    _attrBigPort.setLineWidth(1);

    _attrVec = new FigGroup();
    _attrVec.setFilled(true);
    _attrVec.setLineWidth(1);
    _attrVec.addFig(_attrBigPort);

    // this rectangle marks the operation section; all operations are inside it:
    _operBigPort = new FigRect(10, 30+ROWHEIGHT, 60, ROWHEIGHT+1, Color.black, Color.white);
    _operBigPort.setFilled(true);
    _operBigPort.setLineWidth(1);

    _operVec = new FigGroup();
    _operVec.setFilled(true);
    _operVec.setLineWidth(1);
    _operVec.addFig(_operBigPort);

    _stereo.setExpandOnly(true);
    _stereo.setFilled(true);
    _stereo.setLineWidth(1);
    _stereo.setEditable(false);
    _stereo.setHeight(STEREOHEIGHT+1); // +1 to have 1 pixel overlap with _name
    _stereo.setDisplayed(false);

    _stereoLineBlinder = new FigRect(11, 10+STEREOHEIGHT, 58, 2, Color.white, Color.white);
    _stereoLineBlinder.setLineWidth(1);
    //_stereoLineBlinder.setFilled(true);
    _stereoLineBlinder.setDisplayed(false);

    suppressCalcBounds = true;
    addFig(_bigPort);
    addFig(_stereo);
    addFig(_name);
    addFig(_stereoLineBlinder);
    addFig(_attrVec);
    addFig(_operVec);
    suppressCalcBounds = false;

    setBounds(10, 10, 60, 20+2*ROWHEIGHT);
  }

  public FigClass(GraphModel gm, Object node) {
    this();
    setOwner(node);
    // If this figure is created for an existing class node in the
    // metamodel, set the figure's name according to this node. This is
    // used when the user click's on 'add to diagram' in the navpane.
    // Don't know if this should rather be done in one of the super
    // classes, since similar code is used in FigInterface.java etc.
    // Andreas Rueckert <a_rueckert@gmx.net>
    if (node instanceof MClassifier && (((MClassifier)node).getName() != null))
	    _name.setText(((MModelElement)node).getName());
  }

  public String placeString() { return "new Class"; }

  public Object clone() {
	FigClass figClone = (FigClass) super.clone();
	Vector v = figClone.getFigs();
	figClone._bigPort = (FigRect) v.elementAt(0);
	figClone._stereo = (FigText) v.elementAt(1);
	figClone._name = (FigText) v.elementAt(2);
	figClone._stereoLineBlinder = (FigRect) v.elementAt(3);
	figClone._attrVec = (FigGroup) v.elementAt(4);
	figClone._operVec = (FigGroup) v.elementAt(5);
	figClone.mAttrs = mAttrs;
	figClone.mOpers = mOpers;
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
    addMenu.add(ActionAddAttribute.SINGLETON);
    addMenu.add(ActionAddOperation.SINGLETON);
    addMenu.add(ActionAddNote.SINGLETON);
    popUpActions.insertElementAt(addMenu,
				 popUpActions.size() - 1);
    JMenu showMenu = new JMenu("Show");
    if(_attrVec.isDisplayed() && _operVec.isDisplayed())
      showMenu.add(ActionCompartmentDisplay.HideAllCompartments);
    else if(!_attrVec.isDisplayed() && !_operVec.isDisplayed())
      showMenu.add(ActionCompartmentDisplay.ShowAllCompartments);

    if (_attrVec.isDisplayed())
      showMenu.add(ActionCompartmentDisplay.HideAttrCompartment);
    else
      showMenu.add(ActionCompartmentDisplay.ShowAttrCompartment);

    if (_operVec.isDisplayed())
      showMenu.add(ActionCompartmentDisplay.HideOperCompartment);
    else
      showMenu.add(ActionCompartmentDisplay.ShowOperCompartment);

    popUpActions.insertElementAt(showMenu, popUpActions.size() - 1);
    return popUpActions;
  }

  public void setOwner(Object node) {
    super.setOwner(node);
    bindPort(node, _bigPort);
  }

  public FigGroup getOperationsFig() { return _operVec; }
  public FigGroup getAttributesFig() { return _attrVec; }

  /**
   * Returns the status of the operation field.
   * @return true if the operations are visible, false otherwise
   */
  public boolean isOperationVisible() { return _operVec.isDisplayed(); }

  /**
   * Returns the status of the attribute field.
   * @return true if the attributes are visible, false otherwise
   */
  public boolean isAttributeVisible() {  return _attrVec.isDisplayed(); }

  public void setAttributeVisible(boolean isVisible) {
    Rectangle rect = getBounds();
    int h = (ROWHEIGHT*Math.max(1,_attrVec.getFigs().size()-1)+1) * rect.height / getMinimumSize().height;
    if ( _attrVec.isDisplayed() ) {
      if ( !isVisible ) {
        damage();
        Enumeration enum = _attrVec.getFigs().elements();
        while (enum.hasMoreElements())
            ((Fig)(enum.nextElement())).setDisplayed(false);
        _attrVec.setDisplayed(false);
        setBounds(rect.x, rect.y, rect.width, rect.height - h);
      }
    }
    else {
      if ( isVisible ) {
        Enumeration enum = _attrVec.getFigs().elements();
        while (enum.hasMoreElements())
            ((Fig)(enum.nextElement())).setDisplayed(true);
        _attrVec.setDisplayed(true);
        setBounds(rect.x, rect.y, rect.width, rect.height + h);
        damage();
      }
    }
  }

  public void setOperationVisible(boolean isVisible) {
    Rectangle rect = getBounds();
    int h = (ROWHEIGHT*Math.max(1,_operVec.getFigs().size()-1)+1) * rect.height / getMinimumSize().height;
    if ( _operVec.isDisplayed() ) {
      if ( !isVisible ) {
        damage();
        Enumeration enum = _operVec.getFigs().elements();
        while (enum.hasMoreElements())
            ((Fig)(enum.nextElement())).setDisplayed(false);
        _operVec.setDisplayed(false);
        setBounds(rect.x, rect.y, rect.width, rect.height - h);
      }
    }
    else {
      if ( isVisible ) {
        Enumeration enum = _operVec.getFigs().elements();
        while (enum.hasMoreElements())
            ((Fig)(enum.nextElement())).setDisplayed(true);
        _operVec.setDisplayed(true);
        setBounds(rect.x, rect.y, rect.width, rect.height + h);
        damage();
      }
    }
  }

  public Dimension getMinimumSize() {
    Dimension aSize = _name.getMinimumSize();
    int h = aSize.height;
    int w = aSize.width;
    if (aSize.height < 21)
        aSize.height = 21;
    if (_stereo.isDisplayed()) {
      aSize.width = Math.max(aSize.width, _stereo.getMinimumSize().width);
      aSize.height += STEREOHEIGHT;
    }
    if (_attrVec.isDisplayed()) {
      // get minimum size of the attribute section
      Enumeration enum = _attrVec.getFigs().elements();
      enum.nextElement(); // _attrBigPort
      while (enum.hasMoreElements())
        aSize.width = Math.max(aSize.width,((FigText)enum.nextElement()).getMinimumSize().width+2);
      aSize.height += ROWHEIGHT*Math.max(1,_attrVec.getFigs().size()-1);
    }
    if (_operVec.isDisplayed()) {
      // get minimum size of the operation section
      Enumeration enum = _operVec.getFigs().elements();
      enum.nextElement(); // _operBigPort
      while (enum.hasMoreElements())
        aSize.width = Math.max(aSize.width,((FigText)enum.nextElement()).getMinimumSize().width+2);
      aSize.height += ROWHEIGHT*Math.max(1,_operVec.getFigs().size()-1);
    }
    return aSize;
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

  ////////////////////////////////////////////////////////////////
  // user interaction methods

  public void mousePressed(MouseEvent me) {
    super.mousePressed(me);
    boolean targetIsSet = false;
    Editor ce = Globals.curEditor();
    Selection sel = ce.getSelectionManager().findSelectionFor(this);
    if (sel instanceof SelectionClass)
      ((SelectionClass)sel).hideButtons();
    //display attr/op properties if necessary:
    Rectangle r = new Rectangle(me.getX() - 1, me.getY() - 1, 2, 2);
	Fig f = hitFig(r);
    if (f == _attrVec) {
	  Vector v = _attrVec.getFigs();
	  int i = mAttrs.size() * (me.getY() - f.getY() - 3) / _attrVec.getHeight();
	  if (i >= 0 && i < mAttrs.size() && i < v.size()-1) {
	    ProjectBrowser.TheInstance.setTarget(mAttrs.elementAt(i));
	    targetIsSet = true;
	  }
	}
    else if (f == _operVec) {
	  Vector v = _operVec.getFigs();
	  int i = mOpers.size() * (me.getY() - f.getY() - 3) / _operVec.getHeight();
	  if (i >= 0 && i < mOpers.size() && i < v.size()-1) {
	    ProjectBrowser.TheInstance.setTarget(mOpers.elementAt(i));
	    targetIsSet = true;
	  }
	}
	if (targetIsSet == false)
	  ProjectBrowser.TheInstance.setTarget(getOwner());
  }

  public void setEnclosingFig(Fig encloser) {
    super.setEnclosingFig(encloser);
    if (!(getOwner() instanceof MModelElement)) return;
    MModelElement me = (MModelElement) getOwner();
    MNamespace m = null;
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    if ((encloser == null && me.getNamespace() == null) ||
        (encloser != null && encloser.getOwner() instanceof MPackage)) {
      if (encloser != null) {
        m = (MNamespace) encloser.getOwner();
      } else if (pb.getTarget() instanceof UMLDiagram) {
	    m = (MNamespace) ((UMLDiagram)pb.getTarget()).getNamespace();
      }
      try {
        me.setNamespace(m);
      } catch (Exception e) {
        Argo.log.error("could not set package", e);
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
    int i = _attrVec.getFigs().indexOf(ft);
    if (i != -1) {
	  if (i > 0 && i <= mAttrs.size())
	    ParserDisplay.SINGLETON.parseAttributeFig(cls,(MAttribute)mAttrs.elementAt(i-1),ft.getText());
	  return;
	}
	i = _operVec.getFigs().indexOf(ft);
	if (i != -1) {
	  if (i > 0 && i <= mOpers.size())
	    ParserDisplay.SINGLETON.parseOperationFig(cls,(MOperation)mOpers.elementAt(i-1),ft.getText());
	  return;
	}
  }

  protected void modelChanged() {
    super.modelChanged();
    Rectangle rect = getBounds();
    int xpos = _attrBigPort.getX();
    int ypos = _attrBigPort.getY();
    MClassifier cls = (MClassifier) getOwner();
    if (cls == null)
      return;
	int acounter = 1;
    Collection strs = MMUtil.SINGLETON.getAttributes(cls);
    if (strs != null) {
	  Iterator iter = strs.iterator();
      Vector figs = _attrVec.getFigs();
	  FigText attr;
      while (iter.hasNext()) {
	    MStructuralFeature sf = (MStructuralFeature) iter.next();
	    if (figs.size() <= acounter) {
	      attr = new MyFigText(xpos+1, ypos+1+(acounter-1)*ROWHEIGHT, 0, ROWHEIGHT-2, _attrBigPort); // bounds not relevant here
          attr.setFilled(false);
          attr.setLineWidth(0);
          attr.setFont(LABEL_FONT);
          attr.setTextColor(Color.black);
          attr.setJustification(FigText.JUSTIFY_LEFT);
          attr.setMultiLine(false);
	      _attrVec.addFig(attr);
		} else {
		  attr = (FigText)figs.elementAt(acounter);
	    }
	    attr.setText(Notation.generate(this,sf));
	    // underline, if static
	    attr.setUnderline(MScopeKind.CLASSIFIER.equals(sf.getOwnerScope()));
	    if (acounter <= mAttrs.size())
	    	mAttrs.setElementAt(sf,acounter-1);
	    else
	    	mAttrs.addElement(sf);
	    acounter++;
      }
      if (figs.size() > acounter) {
        //cleanup of unused attribute FigText's
        for (int i=figs.size()-1; i>=acounter; i--)
          _attrVec.removeFig((Fig)figs.elementAt(i));
	  }
      if (mAttrs.size() >= acounter) {
        //cleanup of unused attributes
        for (int i=mAttrs.size()-1; i>=acounter-1; i--)
          mAttrs.removeElementAt(i);
	  }
	}
	int ocounter = 1;
    Collection behs = MMUtil.SINGLETON.getOperations(cls);
    if (behs != null) {
      behs.removeAll(strs);
	  Iterator iter = behs.iterator();
      Vector figs = _operVec.getFigs();
	  FigText oper;
      while (iter.hasNext()) {
	    MBehavioralFeature bf = (MBehavioralFeature) iter.next();
	    if (figs.size() <= ocounter) {
	      oper = new MyFigText(xpos+1, ypos+1+(ocounter-1)*ROWHEIGHT, 0, ROWHEIGHT-2, _operBigPort); // bounds not relevant here
          oper.setFilled(false);
          oper.setLineWidth(0);
          oper.setFont(LABEL_FONT);
          oper.setTextColor(Color.black);
          oper.setJustification(FigText.JUSTIFY_LEFT);
          oper.setMultiLine(false);
	      _operVec.addFig(oper);
		} else {
		  oper = (FigText)figs.elementAt(ocounter);
	    }
	    oper.setText(Notation.generate(this,bf));
	    // underline, if static
	    oper.setUnderline(MScopeKind.CLASSIFIER.equals(bf.getOwnerScope()));
	    // italics, if abstract
	    //oper.setItalic(((MOperation)bf).isAbstract()); // does not properly work (GEF bug?)
        if (((MOperation)bf).isAbstract()) oper.setFont(ITALIC_LABEL_FONT);
        else oper.setFont(LABEL_FONT);
	    if (ocounter <= mOpers.size())
	    	mOpers.setElementAt(bf,ocounter-1);
	    else
	    	mOpers.addElement(bf);
	    ocounter++;
      }
      if (figs.size() > ocounter) {
        //cleanup of unused operation FigText's
        for (int i=figs.size()-1; i>=ocounter; i--)
          _operVec.removeFig((Fig)figs.elementAt(i));
	  }
      if (mOpers.size() >= ocounter) {
        //cleanup of unused operations
        for (int i=mOpers.size()-1; i>=ocounter-1; i--)
          mOpers.removeElementAt(i);
	  }
	}

    if (cls.isAbstract()) _name.setFont(ITALIC_LABEL_FONT);
    else _name.setFont(LABEL_FONT);

	setBounds(rect.x, rect.y, rect.width, rect.height); // recalculates all bounds
  }

  public void renderingChanged() {
    super.renderingChanged();
    updateStereotypeText();
    modelChanged();
  }

  protected void updateStereotypeText() {
    MModelElement me = (MModelElement) getOwner();
    if (me == null)
      return;
	Rectangle rect = getBounds();
    MStereotype stereo = me.getStereotype();
    if (stereo == null || stereo.getName() == null || stereo.getName().length() == 0) {
	  if (! _stereo.isDisplayed())
	    return;
	  _stereoLineBlinder.setDisplayed(false);
	  _stereo.setDisplayed(false);
	  rect.y += STEREOHEIGHT;
	  rect.height -= STEREOHEIGHT;
    }
    else {
	  _stereo.setText(Notation.generateStereotype(this,stereo));
	  if (!_stereo.isDisplayed()) {
	    _stereoLineBlinder.setDisplayed(true);
	    _stereo.setDisplayed(true);
	    rect.y -= STEREOHEIGHT;
	    rect.height += STEREOHEIGHT;
	  }
    }
	setBounds(rect.x, rect.y, rect.width, rect.height);
  }

  /** sets the bounds, but the size will be at least the
      one returned by getMinimunSize(); if the required
      height is bigger, then the additional height is equally
      distributed among all figs, such that the cumulated
      height of all visible figs equals the demanded height
  */
  public void setBounds(int x, int y, int w, int h) {
	Rectangle oldBounds = getBounds();
	Dimension aSize = getMinimumSize();
	int newW = Math.max(w,aSize.width);
	int newH = h;
	int extra_each = 0; // extra height per displayed fig if requested height is greater than minimal
	int height_correction = 0; // height correction due to rounded division result, will be added to _name

	//first compute all nessessary height data
	if (newH < aSize.height) {
		newH = aSize.height;
	} else if (newH > aSize.height) {
	    extra_each = newH - aSize.height;
	    int displayedFigs = 1; //this is for _name
        if (_attrVec.isDisplayed())
	        displayedFigs++;
        if (_operVec.isDisplayed())
	        displayedFigs++;
	    extra_each = extra_each / displayedFigs; // result might be rounded, so:
	    height_correction = (newH - aSize.height) - (extra_each * displayedFigs); // will be added to _name only
	}

	//now resize all sub-figs, including not displayed figs
    int height = _name.getMinimumSize().height;
    if (height < 21)
        height = 21;
    height += extra_each+height_correction;
    int currentY = y;
    if (_stereo.isDisplayed())
        currentY += STEREOHEIGHT;
    _name.setBounds(x,currentY,newW,height);
	_stereo.setBounds(x,y,newW,STEREOHEIGHT+1);
    _stereoLineBlinder.setBounds(x+1,y+STEREOHEIGHT,newW-2,2);
    currentY += height-1; // -1 for 1 pixel overlap
   	aSize = getUpdatedSize(_attrVec,x,currentY,newW,ROWHEIGHT*Math.max(1,_attrVec.getFigs().size()-1)+1+extra_each);
   	if (_attrVec.isDisplayed())
        currentY += aSize.height-1; // -1 for 1 pixel overlap
   	aSize = getUpdatedSize(_operVec,x,currentY,newW,newH+y-currentY);

	// set bounds of big box
    _bigPort.setBounds(x,y,newW,newH);

	calcBounds(); //_x = x; _y = y; _w = w; _h = h;
	updateEdges();
	firePropChange("bounds", oldBounds, getBounds());
  }

  public void calcBounds() {
	if (suppressCalcBounds)
	    return;
	super.calcBounds();
  }

  /** returns the new size of the FigGroup (either attributes or operations)
      after calculation new bounds for all sub-figs, considering their
      minimal sizes; FigGroup need not be displayed; no update event is fired */
  protected Dimension getUpdatedSize(FigGroup fg, int x, int y, int w, int h) {
	int newW = w;
	int n = fg.getFigs().size()-1;
	int newH = Math.max(h,ROWHEIGHT*Math.max(1,n)+1);
	int step = (n>0) ? newH / n : 0; // width step between FigText objects
	//int maxA = Toolkit.getDefaultToolkit().getFontMetrics(LABEL_FONT).getMaxAscent();

	//set new bounds for all included figs
	Enumeration figs = fg.elements();
	Fig bigPort = (Fig)figs.nextElement();
	Fig fi;
	int fw, yy = y;
	while (figs.hasMoreElements()) {
	  fi = (Fig)figs.nextElement();
	  fw = fi.getMinimumSize().width;
	  fi.setBounds(x+1,yy+1,fw,ROWHEIGHT-2);
	  if (newW < fw+2)
	      newW = fw+2;
	  yy += step;
	}
	bigPort.setBounds(x,y,newW,newH); // rectangle containing all following FigText objects
	fg.calcBounds();
	return new Dimension(newW,newH);
  }

  private class MyFigText extends FigText
  {
	private Fig refFig;
	public MyFigText(int x, int y, int w, int h, Fig aFig) {super(x,y,w,h,true); refFig=aFig;}
	public void setLineWidth(int w) {super.setLineWidth(0);}
	public int getLineWidth() {return 1;} // don't dare to throw away these fakes!
	public boolean getFilled() {return true;}
	public Color getFillColor() {return refFig.getFillColor();}
	public Color getLineColor() {return refFig.getLineColor();}
  }
} /* end class FigClass */
