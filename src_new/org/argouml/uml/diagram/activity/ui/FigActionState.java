// $Id$
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

// File: FigActionState.java
// Classes: FigActionState
// Original Author: ics 125b silverbullet team

package org.argouml.uml.diagram.activity.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Vector;

import org.argouml.uml.diagram.state.ui.FigStateVertex;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigText;

/** Class to display graphics for a UML MState in a diagram. */

public class FigActionState extends FigStateVertex {

    ////////////////////////////////////////////////////////////////
    // constants

    public final int MARGIN = 2;
    public int PADDING = 8;

    ////////////////////////////////////////////////////////////////
    // instance variables

    /** UML does not really use ports, so just define one big one so
     *  that users can drag edges to or from any point in the icon. */

    FigRRect _bigPort;
    FigRRect _cover;


    ////////////////////////////////////////////////////////////////
    // constructors

    public FigActionState() {
	_bigPort = new FigRRect(10 + 1, 10 + 1, 90 - 2, 25 - 2,
				Color.cyan, Color.cyan);
	_cover = new FigRRect(10, 10, 90, 25, Color.black, Color.white);

	_bigPort.setLineWidth(0);
	_name.setLineWidth(0);
	_name.setBounds(10 + PADDING, 10, 90 - PADDING * 2, 25);
	_name.setFilled(false);
	_name.setMultiLine(true);

	// add Figs to the FigNode in back-to-front order
	addFig(_bigPort);
	addFig(_cover);
	addFig(_name);

	//setBlinkPorts(false); //make port invisble unless mouse enters
	Rectangle r = getBounds();
	setBounds(r.x, r.y, r.width, r.height);
    }

    public FigActionState(GraphModel gm, Object node) {
	this();
	setOwner(node);
    }

    public String placeString() { return "new ActionState"; }

    public Selection makeSelection() {
	return new SelectionActionState(this);
    }

    public Object clone() {
	FigActionState figClone = (FigActionState) super.clone();
	Vector v = figClone.getFigs();
	figClone._bigPort = (FigRRect) v.elementAt(0);
	figClone._cover = (FigRRect) v.elementAt(1);
	figClone._name = (FigText) v.elementAt(2);
	return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    public void setOwner(Object node) {
	super.setOwner(node);
	bindPort(node, _bigPort);
    }

    public Dimension getMinimumSize() {
	Dimension nameDim = _name.getMinimumSize();
	int w = nameDim.width + PADDING * 2;
	int h = nameDim.height; // + PADDING*2;
	return new Dimension(w, h);
    }

    /* Override setBounds to keep shapes looking right */
    public void setBounds(int x, int y, int w, int h) {
	if (_name == null) return;
	Rectangle oldBounds = getBounds();

	_name.setBounds(x + PADDING, y, w - PADDING * 2, h);
	_bigPort.setBounds(x + 1, y + 1, w - 2, h - 2);
	_cover.setBounds(x, y, w, h);
	_bigPort.setCornerRadius(h / 3 * 2);
	_cover.setCornerRadius(h / 3 * 2);

	calcBounds(); //_x = x; _y = y; _w = w; _h = h;
	updateEdges();
	firePropChange("bounds", oldBounds, getBounds());
    }


    public void setLineColor(Color col) { _cover.setLineColor(col); }
    public Color getLineColor() { return _cover.getLineColor(); }

    public void setFillColor(Color col) { _cover.setFillColor(col); }
    public Color getFillColor() { return _cover.getFillColor(); }

    public void setFilled(boolean f) { _cover.setFilled(f); }
    public boolean getFilled() { return _cover.getFilled(); }

    public void setLineWidth(int w) { _cover.setLineWidth(w); }
    public int getLineWidth() { return _cover.getLineWidth(); }

} /* end class FigActionState */
