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

// File: FigBranchState.java
// Classes: FigBranchState
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Vector;

import org.argouml.uml.diagram.ui.SelectionMoveClarifiers;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigPoly;

/** Class to display graphics for a UML Branch MState in a diagram. */

public class FigBranchState extends FigStateVertex {

    ////////////////////////////////////////////////////////////////
    // constants

    public static final int MARGIN = 2;
    public static final int X = 0;
    public static final int Y = 0;
    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;

    ////////////////////////////////////////////////////////////////
    // instance variables

    FigPoly _bigPort;
    FigPoly _head;

    ////////////////////////////////////////////////////////////////
    // constructors
    public FigBranchState() {
	_bigPort = new FigPoly( Color.cyan, Color.cyan);
	_head = new FigPoly(Color.black, Color.white);
	_bigPort.addPoint(X, Y);
	_bigPort.addPoint(X + WIDTH / 2, Y + HEIGHT / 2);
	_bigPort.addPoint(X, Y + HEIGHT);
	_bigPort.addPoint(X - WIDTH / 2, Y + HEIGHT / 2);

	_head.addPoint(X, Y);
	_head.addPoint(X + WIDTH / 2, Y + HEIGHT / 2);
	_head.addPoint(X, Y + HEIGHT);
	_head.addPoint(X - WIDTH / 2, Y + HEIGHT / 2);
	_head.addPoint(X, Y);

	// add Figs to the FigNode in back-to-front order
	addFig(_bigPort);
	addFig(_head);

	setBlinkPorts(false); //make port invisble unless mouse enters
	Rectangle r = getBounds();
    }

    public FigBranchState(GraphModel gm, Object node) {
	this();
	setOwner(node);
    }

    public Object clone() {
	FigBranchState figClone = (FigBranchState) super.clone();
	Vector v = figClone.getFigs();
	figClone._bigPort = (FigPoly) v.elementAt(0);
	figClone._head = (FigPoly) v.elementAt(1);
	return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accesors

    public void setOwner(Object node) {
	super.setOwner(node);
	bindPort(node, _bigPort);
    }

    /** Initial states are fixed size. */
    public boolean isResizable() { return false; }

    public Selection makeSelection() {
	return new SelectionMoveClarifiers(this);
    }

    public void setLineColor(Color col) { _head.setLineColor(col); }
    public Color getLineColor() { return _head.getLineColor(); }

    public void setFillColor(Color col) { _head.setFillColor(col); }
    public Color getFillColor() { return _head.getFillColor(); }

    public void setFilled(boolean f) { }
    public boolean getFilled() { return true; }

    public void setLineWidth(int w) { _head.setLineWidth(w); }
    public int getLineWidth() { return _head.getLineWidth(); }

    ////////////////////////////////////////////////////////////////
    // Event handlers

    public void mouseClicked(MouseEvent me) { }
    public void keyPressed(KeyEvent ke) { }

    static final long serialVersionUID = 7975577199958200215L;

} /* end class FigBranchState */
