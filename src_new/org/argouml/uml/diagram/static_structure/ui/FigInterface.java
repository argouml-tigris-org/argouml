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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;

import org.argouml.application.api.*;
import org.argouml.language.helpers.*;
import org.argouml.ui.*;
import org.argouml.uml.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.generator.*;
import org.argouml.uml.diagram.ui.*;

/** Class to display graphics for a UML Interface in a diagram. */

public class FigInterface extends FigNodeModelElement {

  ////////////////////////////////////////////////////////////////
  // constants

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected FigGroup _operVec;
  protected FigRect _bigPort;
  protected FigRect _operBigPort;
  protected FigRect _stereoLineBlinder;
  public MElementResidence resident = new MElementResidenceImpl();

  protected Vector mOpers = new Vector();
  protected CompartmentFigText highlightedFigText = null;

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigInterface() {
    // this rectangle marks the whole interface figure; everything is inside it:
    _bigPort = new FigRect(10, 10, 60, 20+ROWHEIGHT, Color.cyan, Color.cyan);

    _name.setLineWidth(1);
    _name.setFilled(true);

    // this rectangle marks the operation section; all operations are inside it:
    _operBigPort = new FigRect(10, 30+ROWHEIGHT, 60, ROWHEIGHT+1, Color.black, Color.white);
    _operBigPort.setFilled(true);
    _operBigPort.setLineWidth(1);

    _operVec = new FigGroup();
    _operVec.setFilled(true);
    _operVec.setLineWidth(1);
    _operVec.addFig(_operBigPort);

    _stereo.setText(NotationHelper.getLeftGuillemot()+"Interface"+NotationHelper.getRightGuillemot());
    _stereo.setExpandOnly(true);
    _stereo.setFilled(true);
    _stereo.setLineWidth(1);
    _stereo.setEditable(false);
    _stereo.setHeight(STEREOHEIGHT+1); // +1 to have 1 pixel overlap with _name
    _stereo.setDisplayed(true);

    _stereoLineBlinder = new FigRect(11, 10+STEREOHEIGHT, 58, 2, Color.white, Color.white);
    _stereoLineBlinder.setLineWidth(1);
    //_stereoLineBlinder.setFilled(true);
    _stereoLineBlinder.setDisplayed(true);

    suppressCalcBounds = true;
    addFig(_bigPort);
    addFig(_stereo);
    addFig(_name);
    addFig(_stereoLineBlinder);
    addFig(_operVec);
    suppressCalcBounds = false;

    setBounds(10, 10, 60, 20+ROWHEIGHT);
  }

  public FigInterface(GraphModel gm, Object node) {
    this();
    setOwner(node);
    // If this figure is created for an existing interface node in the
    // metamodel, set the figure's name according to this node. This is
    // used when the user click's on 'add to diagram' in the navpane.
    // Don't know if this should rather be done in one of the super
    // classes, since similar code is used in FigClass.java etc.
    // Andreas Rueckert <a_rueckert@gmx.net>
    if (node instanceof MInterface && (((MInterface)node).getName() != null))
        _name.setText(((MModelElement)node).getName());
  }

  public String placeString() { return "new Interface"; }

  public Object clone() {
    FigInterface figClone = (FigInterface) super.clone();
	Vector v = figClone.getFigs();
	figClone._bigPort = (FigRect) v.elementAt(0);
	figClone._stereo = (FigText) v.elementAt(1);
	figClone._name = (FigText) v.elementAt(2);
	figClone._stereoLineBlinder = (FigRect) v.elementAt(3);
	figClone._operVec = (FigGroup) v.elementAt(5);
	figClone.mOpers = mOpers;
	return figClone;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public Selection makeSelection() {
    return new SelectionInterface(this);
  }

  public Vector getPopUpActions(MouseEvent me) {
    Vector popUpActions = super.getPopUpActions(me);
    JMenu addMenu = new JMenu("Add");
    addMenu.add(ActionAddOperation.SINGLETON);
    addMenu.add(ActionAddNote.SINGLETON);
    popUpActions.insertElementAt(addMenu, popUpActions.size() - 1);

    return popUpActions;
  }

  public void setOwner(Object node) {
    super.setOwner(node);
    bindPort(node, _bigPort);
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
    int i = 0;
    Editor ce = Globals.curEditor();
    Selection sel = ce.getSelectionManager().findSelectionFor(this);
    if (sel instanceof SelectionClass)
      ((SelectionClass)sel).hideButtons();
    unhighlight();
    //display op properties if necessary:
    Rectangle r = new Rectangle(me.getX() - 1, me.getY() - 1, 2, 2);
	Fig f = hitFig(r);
    if (f == _operVec) {
	  Vector v = _operVec.getFigs();
	  i = mOpers.size() * (me.getY() - f.getY() - 3) / _operVec.getHeight();
	  if (i >= 0 && i < mOpers.size() && i < v.size()-1) {
	    ProjectBrowser.TheInstance.setTarget(mOpers.elementAt(i));
	    targetIsSet = true;
	    f = (Fig)v.elementAt(i+1);
		((CompartmentFigText)f).setHighlighted(true);
		highlightedFigText = (CompartmentFigText)f;
	  }
	}
	if (targetIsSet == false)
	  ProjectBrowser.TheInstance.setTarget(getOwner());
  }

  public void mouseExited(MouseEvent me) {
    super.mouseExited(me);
    unhighlight();
  }

  public void keyPressed(KeyEvent ke) {
    int key = ke.getKeyCode();
    if (key == KeyEvent.VK_TAB) {
      CompartmentFigText ft = unhighlight();
      if (ft != null) {
        int i = _operVec.getFigs().indexOf(ft);
        if (i != -1) {
          if (ke.isShiftDown()) {
            ft = (CompartmentFigText)getPreviousVisibleFeature(ft,i);
          } else {
            ft = (CompartmentFigText)getNextVisibleFeature(ft,i);
          }
          if (ft != null) {
	        ft.setHighlighted(true);
	        highlightedFigText = ft;
	        return;
          }
        }
      }
    } else if (key == KeyEvent.VK_ENTER && highlightedFigText != null) {
      highlightedFigText.startTextEditor(ke);
	  ke.consume();
	  return;
    }
    super.keyPressed(ke);
  }

  public void setEnclosingFig(Fig encloser) {
    super.setEnclosingFig(encloser);
    if (!(getOwner() instanceof MModelElement)) return;
    MModelElement me = (MModelElement) getOwner();
    MNamespace m = null;
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    if (me.getNamespace() == null) {
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
    }

    // The next if-clause is important for the Deployment-diagram
    // it detects if the enclosing fig is a component, in this case
    // the ImplementationLocation will be set for the owning MInterface
    if (encloser != null && (encloser.getOwner() instanceof MComponentImpl)) {
      MComponent component = (MComponent) encloser.getOwner();
      MInterface in = (MInterface) getOwner();
      resident.setImplementationLocation(component);
      resident.setResident(in);
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
    int i = _operVec.getFigs().indexOf(ft);
	if (i != -1) {
	  if (i > 0 && i <= mOpers.size()) {
	    ParserDisplay.SINGLETON.parseOperationFig(cls,(MOperation)mOpers.elementAt(i-1),ft.getText().trim());
	    highlightedFigText = (CompartmentFigText)ft;
	    highlightedFigText.setHighlighted(true);
	  }
	  return;
	}
  }

  protected FigText getPreviousVisibleFeature(FigText ft, int i) {
	FigText ft2 = null;
	Vector v = _operVec.getFigs();
	if (i < 1 || i >= v.size() || !((FigText)v.elementAt(i)).isDisplayed())
	  return null;
	do {
	  i--;
	  if (i < 1)
		i = v.size() - 1;
	  ft2 = (FigText)v.elementAt(i);
	  if (!ft2.isDisplayed())
	    ft2 = null;
	} while (ft2 == null);
	return ft2;
  }

  protected FigText getNextVisibleFeature(FigText ft, int i) {
	FigText ft2 = null;
	Vector v = _operVec.getFigs();
	if (i < 1 || i >= v.size() || !((FigText)v.elementAt(i)).isDisplayed())
	  return null;
	do {
	  i++;
	  if (i >= v.size())
		i = 1;
	  ft2 = (FigText)v.elementAt(i);
	  if (!ft2.isDisplayed())
	    ft2 = null;
	} while (ft2 == null);
	return ft2;
  }

  protected void createFeatureIn(FigGroup fg, InputEvent ie) {
	CompartmentFigText ft = null;
    MClassifier cls = (MClassifier)getOwner();
    if (cls == null)
      return;
	ActionAddOperation.SINGLETON.actionPerformed(null);
	ft = (CompartmentFigText)fg.getFigs().lastElement();
	if (ft != null) {
	  ft.startTextEditor(ie);
	  ft.setHighlighted(true);
	  highlightedFigText = ft;
    }
  }

  protected CompartmentFigText unhighlight() {
    CompartmentFigText ft;
    Vector v = _operVec.getFigs();
    int i;
    for (i = 1; i < v.size(); i++) {
      ft = (CompartmentFigText)v.elementAt(i);
      if (ft.isHighlighted()) {
        ft.setHighlighted(false);
	    highlightedFigText = null;
        return ft;
      }
    }
    return null;
  }

  protected void modelChanged() {
    super.modelChanged();
    Rectangle rect = getBounds();
    int xpos = _operBigPort.getX();
    int ypos = _operBigPort.getY();
    MClassifier cls = (MClassifier) getOwner();
    if (cls == null)
      return;
	int ocounter = 1;
    Collection behs = MMUtil.SINGLETON.getOperations(cls);
    if (behs != null) {
	  Iterator iter = behs.iterator();
      Vector figs = _operVec.getFigs();
	  FigText oper;
      while (iter.hasNext()) {
	    MBehavioralFeature bf = (MBehavioralFeature) iter.next();
	    if (figs.size() <= ocounter) {
	      oper = new CompartmentFigText(xpos+1, ypos+1+(ocounter-1)*ROWHEIGHT, 0, ROWHEIGHT-2, _operBigPort); // bounds not relevant here
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

  static final long serialVersionUID = 4928213949795787107L;

  public void renderingChanged() {
    super.renderingChanged();
    _stereo.setText(NotationHelper.getLeftGuillemot()+"Interface"+NotationHelper.getRightGuillemot());
	modelChanged();
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
   	aSize = getUpdatedSize(_operVec,x,currentY,newW,newH+y-currentY);

	// set bounds of big box
    _bigPort.setBounds(x,y,newW,newH);

	calcBounds(); //_x = x; _y = y; _w = w; _h = h;
	updateEdges();
	firePropChange("bounds", oldBounds, getBounds());
  }
} /* end class FigInterface */


