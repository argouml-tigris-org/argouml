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

// File: FigRealization.java
// Classes: FigRealization
// Original Author: agauthie@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Graphics;

import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.presentation.ArrowHeadTriangle;
import org.tigris.gef.presentation.Fig;

import ru.novosoft.uml.MElementEvent;

public class FigRealization extends FigEdgeModelElement {

    ////////////////////////////////////////////////////////////////
    // constructors

    ArrowHeadTriangle endArrow;

    public FigRealization() {
	addPathItem(_stereo, new PathConvPercent(this, 50, 10));
	endArrow = new ArrowHeadTriangle();
	endArrow.setFillColor(Color.white);
	setDestArrowHead(endArrow);
	setBetweenNearestPoints(true);
	_stereo.setText("");
    }

    public FigRealization(Object edge) {
	this();
	setOwner(edge);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public void setFig(Fig f) {
	super.setFig(f);
	_fig.setDashed(true);
    }


    protected boolean canEdit(Fig f) { return false; }

    /** This is called aftern any part of the UML MModelElement has
     *  changed. This method automatically updates the name FigText.
     *  Subclasses should override and update other parts. */
    protected void modelChanged(MElementEvent e) {
	// do not set _name
	//updateStereotypeText();
    }

    public void paint(Graphics g) {
        endArrow.setLineColor(getLineColor());
        super.paint(g);
    }

} /* end class FigRealization */

