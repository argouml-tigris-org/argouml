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

// File: FigShallowHistoryState.java
// Classes: FigShallowHistoryState
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
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigText;

/** Class to display graphics for a UML MState in a diagram. */

public class FigShallowHistoryState extends FigStateVertex {

    ////////////////////////////////////////////////////////////////
    // constants

    public final int MARGIN = 2;
    public int x = 0;
    public int y = 0;
    public int width = 24;
    public int height = 24;

    ////////////////////////////////////////////////////////////////
    // instance variables

    /** The main label on this icon. */
    FigText _name;

    /** UML does not really use ports, so just define one big one so
     *  that users can drag edges to or from any point in the icon. */

    FigCircle _bigPort;

    // add other Figs here aes needed

    FigCircle _head;
    ////////////////////////////////////////////////////////////////
    // constructors

    public FigShallowHistoryState() {
	_bigPort = new FigCircle(x, y, width, height, Color.cyan, Color.cyan);
	_head = new FigCircle(x, y, width, height, Color.black, Color.white);
	_name = new FigText(x, y, width - 10, height - 10);
	_name.setText("H");
	_name.setTextColor(Color.black);
	_name.setFilled(false);
	_name.setLineWidth(0);

	// add Figs to the FigNode in back-to-front order
	addFig(_bigPort);
	addFig(_head);
	addFig(_name);

	setBlinkPorts(false); //make port invisble unless mouse enters
	Rectangle r = getBounds();
    }

    public String placeString() { return "H"; }

    public FigShallowHistoryState(GraphModel gm, Object node) {
	this();
	setOwner(node);
    }

    public Object clone() {
	FigShallowHistoryState figClone =
	    (FigShallowHistoryState) super.clone();
	Vector v = figClone.getFigs();
	figClone._bigPort = (FigCircle) v.elementAt(0);
	figClone._head = (FigCircle) v.elementAt(1);
	figClone._name = (FigText) v.elementAt(2);
	return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    public void setOwner(Object node) {
	super.setOwner(node);
	bindPort(node, _bigPort);
    }

    /** History states are fixed size. */
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


    static final long serialVersionUID = 6572261327347541373L;

} /* end class FigShallowHistoryState */
