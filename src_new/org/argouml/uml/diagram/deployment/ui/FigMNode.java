// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

// File: FigMNode.java
// Classes: FigMNode
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.diagram.deployment.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.application.api.Notation;
import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCube;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/** Class to display graphics for a UML Node in a diagram. */

public class FigMNode extends FigNodeModelElement {

    ////////////////////////////////////////////////////////////////
    // instance variables

    protected FigCube _cover;
    protected FigRect _test;

    ////////////////////////////////////////////////////////////////
    // constructors

    public FigMNode() {
	_bigPort = new FigRect(10, 10, 200, 180);
	_cover = new FigCube(10, 10, 200, 180, Color.black, Color.white);
	_test = new FigRect(10, 10, 1, 1, Color.black, Color.white);

	getNameFig().setLineWidth(0);
	getNameFig().setFilled(false);
	getNameFig().setJustification(0);

	addFig(_bigPort);
	addFig(_cover);
	addFig(getStereotypeFig());
	addFig(getNameFig());
	addFig(_test);
    }

    public FigMNode(GraphModel gm, Object node) {
	this();
	setOwner(node);
	if (ModelFacade.isAClassifier(node)
	        && (ModelFacade.getName(node) != null)) {
	    getNameFig().setText(ModelFacade.getName(node));
	}
    }

    public String placeString() { return "new Node"; }

    public Object clone() {
	FigMNode figClone = (FigMNode) super.clone();
	Iterator it = figClone.getFigs(null).iterator();
	figClone._bigPort = (FigRect) it.next();
	figClone._cover = (FigCube) it.next();
	figClone.setStereotypeFig((FigText) it.next());
	figClone.setNameFig((FigText) it.next());
	figClone._test = (FigRect) it.next();
	return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // acessors

    public void setLineColor(Color c) {
	//     super.setLineColor(c);
	_cover.setLineColor(c);
	getStereotypeFig().setFilled(false);
	getStereotypeFig().setLineWidth(0);
	getNameFig().setFilled(false);
	getNameFig().setLineWidth(0);
	_test.setLineColor(c);
    }

    public Selection makeSelection() {
	return new SelectionNode(this);
    }

    public Dimension getMinimumSize() {
	Dimension stereoDim = getStereotypeFig().getMinimumSize();
	Dimension nameDim = getNameFig().getMinimumSize();

	int w = Math.max(stereoDim.width, nameDim.width + 1);
	int h = stereoDim.height + nameDim.height - 4;
	return new Dimension(w, h);
    }

    public void setBounds(int x, int y, int w, int h) {
	if (getNameFig() == null) {
	    return;
	}

	Rectangle oldBounds = getBounds();
	_bigPort.setBounds(x , y, w , h);
	_cover.setBounds(x , y, w, h);

	Dimension stereoDim = getStereotypeFig().getMinimumSize();
	Dimension nameDim = getNameFig().getMinimumSize();
	getNameFig().setBounds(x + 1, y + stereoDim.height + 1,
			       w - 1, nameDim.height);
	getStereotypeFig().setBounds(x + 1, y + 1, w - 2, stereoDim.height);
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


    public void setEnclosingFig(Fig encloser) {
	super.setEnclosingFig(encloser);

	Vector figures = getEnclosedFigs();

	if (getLayer() != null) {
            // elementOrdering(figures);
            Collection contents = getLayer().getContents(null);
            Collection bringToFrontList = new ArrayList();
            Iterator it = contents.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof FigEdgeModelElement) {
                    bringToFrontList.add(o);
                    
                }
            }
            Iterator bringToFrontIter = bringToFrontList.iterator();
            while (bringToFrontIter.hasNext()) {
                FigEdgeModelElement figEdge = (FigEdgeModelElement) bringToFrontIter.next();
                figEdge.getLayer().bringToFront(figEdge);
            }
        }
    }

    protected void updateStereotypeText() {
	Object me = /*(MModelElement)*/ getOwner();
	if (me == null) return;
	Object stereo = null;
	if (ModelFacade.getStereotypes(me).size() > 0) {
            stereo = ModelFacade.getStereotypes(me).iterator().next();
        }
	if (stereo == null
	    || ModelFacade.getName(stereo) == null
	    || ModelFacade.getName(stereo).length() == 0) {

	    setStereotype("");

	} else {

	    setStereotype(Notation.generateStereotype(this, stereo));

	}
    }

    public boolean getUseTrapRect() { return true; }

    static final long serialVersionUID = 8822005566372687713L;

} /* end class FigMNode */
