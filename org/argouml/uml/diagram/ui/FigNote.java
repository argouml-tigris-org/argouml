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

// File: FigNote.java
// Classes: FigNote
// Original Author: your email address here
// $Id$

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigRect;

/** Renders a Comment in a diagram.
 *
 * @deprecated as of 0.15.1, but don't remove becuase it is need for loading
 *             'old' .zargo project files, replaced by 
 *             {@link org.argouml.uml.diagram.static_structure.ui.FigComment 
 *             FigComment}
 *
 */

public class FigNote extends FigNodeModelElement {

    ////////////////////////////////////////////////////////////////
    // constants

    public final int MARGIN = 2;

    ////////////////////////////////////////////////////////////////
    // instance variables

    // add other Figs here aes needed


    ////////////////////////////////////////////////////////////////
    // constructors

    public FigNote() {
	Color handleColor = Globals.getPrefs().getHandleColor();
	_bigPort = new FigRect(10, 10, 90, 20, handleColor, Color.lightGray);
	_name.setExpandOnly(true);
	_name.setText("FigNote");
	// initialize any other Figs here

	// add Figs to the FigNode in back-to-front order
	addFig(_bigPort);
	addFig(_name);


	setBlinkPorts(true); //make port invisble unless mouse enters
	Rectangle r = getBounds();
    }

    public FigNote(GraphModel gm, Object node) {
	this();
	setOwner(node);

    }


    public Dimension getMinimumSize() {
	Dimension nameDim = _name.getMinimumSize();
	int w = nameDim.width;
	int h = nameDim.height;
	return new Dimension(w, h);
    }

    public void setBounds(int x, int y, int w, int h) {
	Rectangle oldBounds = getBounds();
	_name.setBounds(x, y, w, h);
	_x = x; _y = y; _w = w; _h = h;
	firePropChange("bounds", oldBounds, getBounds());
	updateEdges();
    }


} /* end class FigNote */
