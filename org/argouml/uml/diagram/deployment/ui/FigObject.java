// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// File: FigObject.java
// Classes: FigObject
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.diagram.deployment.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.Vector;

import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.generator.ParserDisplay;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;
import ru.novosoft.uml.behavior.common_behavior.MObject;

/** Class to display graphics for a UML Object in a diagram. */

public class FigObject extends FigNodeModelElement {

    ////////////////////////////////////////////////////////////////
    // instance variables

    FigRect _cover;
    public Object resident =
	UmlFactory.getFactory().getCore().createElementResidence();

    // add other Figs here aes needed

    ////////////////////////////////////////////////////////////////
    // constructors

    public FigObject() {
	_bigPort = new FigRect(10, 10, 90, 50, Color.cyan, Color.cyan);
	_cover = new ShadowRect(10, 10, 90, 50, Color.black, Color.white);
	_name.setLineWidth(0);
	_name.setFilled(false);
	_name.setUnderline(true);
	Dimension nameMin = _name.getMinimumSize();
	_name.setBounds(10, 10, nameMin.width + 20, nameMin.height);

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
	_name.setBounds(x, y, nameMin.width + 10, nameMin.height + 4);

	//_bigPort.setBounds(x+1, y+1, w-2, h-2);
	_x = x; _y = y; _w = w; _h = h;

	firePropChange("bounds", oldBounds, getBounds());
	calcBounds(); //_x = x; _y = y; _w = w; _h = h;
	updateEdges();
    }

  
    ////////////////////////////////////////////////////////////////
    // user interaction methods

    protected void textEdited(FigText ft) throws PropertyVetoException {
	Object obj = /*(MObject)*/ getOwner();
	if (ft == _name) {
	    String s = ft.getText().trim();
	    if (s.length() > 0 && (s.endsWith(":"))) {
		s = s.substring(0, (s.length() - 1));
	    }
	    ParserDisplay.SINGLETON.parseObject((MObject)obj, s);
	}
    }


    public void setEnclosingFig(Fig encloser) {
	// super.setEnclosingFig(encloser);
	if (!(ModelFacade.isAModelElement(getOwner()))) return;
	if (ModelFacade.isAObject(getOwner())) {
	    Object me = /*(MObject)*/ getOwner();
	    Object mcompInst = null;
	    Object mcomp = null;

	    if (encloser != null
                    && (ModelFacade.isAComponentInstance(encloser.getOwner()))) {
		mcompInst = /*(MComponentInstance)*/ encloser.getOwner();
		ModelFacade.setComponentInstance(me, mcompInst);
	    }
	    else if (ModelFacade.getComponentInstance(me) != null) {
                ModelFacade.setComponentInstance(me, null);
	    }
	    if (encloser != null && 
		(ModelFacade.isAComponent(encloser.getOwner()))) {
		mcomp = /*(MComponent)*/ encloser.getOwner();
		Object obj = /*(MObject)*/ getOwner();
		ModelFacade.setImplementationLocation(resident, mcomp);
		ModelFacade.setResident(resident, obj);
	    }
	    else {
		if (ModelFacade.getImplementationLocation(resident) != null) {
		    ModelFacade.setImplementationLocation(resident, null);
		    ModelFacade.setResident(resident, null);
		}
	    }
	}
    }

    static final long serialVersionUID = -185736690375678962L;

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        Object obj = /*(MObject)*/ getOwner();
	if (obj == null) return;
	String nameStr = "";
	if (ModelFacade.getName(obj) != null) {
	    nameStr = ModelFacade.getName(obj).trim();
	}

	Vector bases = new Vector(ModelFacade.getClassifiers(obj));
 
	String baseString = "";

	if (ModelFacade.getClassifiers(obj) != null && ModelFacade.getClassifiers(obj).size() > 0) {

	    baseString += ModelFacade.getName(bases.elementAt(0));
	    for (int i = 1; i < bases.size(); i++)
		baseString +=
		    ", "  + ModelFacade.getName(bases.elementAt(i));
	}

	if (_readyToEdit) {
	    if ( nameStr == "" && baseString == "")
		_name.setText("");
	    else
		_name.setText(nameStr.trim() + " : " + baseString);
	}
	Dimension nameMin = _name.getMinimumSize();
	Rectangle r = getBounds();
	setBounds(r.x, r.y, nameMin.width + 10, nameMin.height + 4);
    }

} /* end class FigObject */