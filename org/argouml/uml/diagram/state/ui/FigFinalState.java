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

// File: FigFinalState.java
// Classes: FigFinalState
// Original Author: ics125b spring 98

package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.uml.diagram.activity.ui.SelectionActionState;
import org.argouml.model.ModelFacade;

import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/** Class to display graphics for a UML MState in a diagram. */

public class FigFinalState extends FigStateVertex {

    ////////////////////////////////////////////////////////////////
    // constants

    private static final int MARGIN = 2;
    private int x = 0;
    private int y = 0;
    private int width = 20;
    private int height = 20;

    ////////////////////////////////////////////////////////////////
    // instance variables

    private FigRect bigPort;
    private FigCircle inCircle;
    private FigCircle outCircle;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The main constructor.
     */
    public FigFinalState() {
	super();
	Color handleColor = Globals.getPrefs().getHandleColor();
	x = 45;
	y = 0;
	bigPort = new FigRect(x, y, width, height);
	bigPort.setLineWidth(0);
	bigPort.setFilled(false);
	outCircle =
	    new FigCircle(x, y, width, height, Color.black, Color.white);
	inCircle =
	    new FigCircle(
			  x + 5,
			  y + 5,
			  width - 10,
			  height - 10,
			  handleColor,
			  Color.black);

	outCircle.setLineWidth(1);
	inCircle.setLineWidth(0);

	setNameFig(new FigText(x + 10, y + 22, 0, 21, true));
	getNameFig().setFilled(false);
	getNameFig().setLineWidth(0);
	getNameFig().setFont(getLabelFont());
	getNameFig().setTextColor(Color.black);
	getNameFig().setMultiLine(false);
	getNameFig().setAllowsTab(false);
	getNameFig().setJustificationByName("center");
		
	addFig(bigPort);
	addFig(outCircle);
	addFig(inCircle);
	addFig(getNameFig());

	setBlinkPorts(false); //make port invisble unless mouse enters
	Rectangle r = getBounds();
    }

    /**
     * The constructor that hooks the Fig into the UML element
     * @param gm ignored
     * @param node the UML element
     */
    public FigFinalState(GraphModel gm, Object node) {
	this();
	setOwner(node);
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
	FigFinalState figClone = (FigFinalState) super.clone();
	Iterator it = figClone.getFigs(null).iterator();
	figClone.bigPort = (FigRect) it.next();
	figClone.outCircle = (FigCircle) it.next();
	figClone.inCircle = (FigCircle) it.next();
	return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    /**
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
	Object pstate = null;
	Selection sel = null;
	if (getOwner() != null) {
	    pstate = getOwner();
	    if (org.argouml.model.ModelFacade.isAActivityGraph(
                    ModelFacade.getStateMachine(
                        ModelFacade.getContainer(pstate)))) {
		sel = new SelectionActionState(this);
		((SelectionActionState) sel).setOutgoingButtonEnabled(false);
	    } else {
		sel = new SelectionState(this);
		((SelectionState) sel).setOutgoingButtonEnabled(false);
	    }
	}
	return sel;
    }

    /** Final states are fixed size. 
     * @see org.tigris.gef.presentation.Fig#isResizable()
     */
    public boolean isResizable() {
	return false;
    }

    //   public Selection makeSelection() {
    //     return new SelectionMoveClarifiers(this);
    //   }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
	outCircle.setLineColor(col);
    }
    
    /**
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
	return outCircle.getLineColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
	inCircle.setFillColor(col);
    }
    
    /**
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
	return inCircle.getFillColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean f) {
    }
    
    /**
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
	return true;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
	outCircle.setLineWidth(w);
    }
    
    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
	return outCircle.getLineWidth();
    }

    ////////////////////////////////////////////////////////////////
    // Event handlers

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
    }
    
    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent ke) {
    }

    static final long serialVersionUID = -3506578343969467480L;


    /**
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    public void setBounds(int boundX, int boundY, int boundW, int boundH) {
	_x = boundX;
	_y = boundY;
	bigPort.setX(boundX);
	bigPort.setY(boundY);
	outCircle.setX(boundX);
	outCircle.setY(boundY);
	inCircle.setX(boundX + 5);
	inCircle.setY(boundY + 5);
	// bigPort.setBounds(boundX, boundY, boundW, boundH);
	// outCircle.setBounds(boundX, boundY, boundW, boundH);
    }

    /**
     * Returns the outCircle.
     * @return FigCircle
     */
    public FigCircle getOutCircle() {
	return outCircle;
    }

    /**
     * Makes sure that edges stick to the outer circle and not to the name or
     * stereobox.
     * @see org.tigris.gef.presentation.Fig#getGravityPoints()
     */
    public Vector getGravityPoints() {
	Vector ret = new Vector();
	int cx = outCircle.center().x;
	int cy = outCircle.center().y;
	int radius = Math.round(outCircle.getWidth() / 2) + 1;
	final int maxPoints = 20;
	Point point = null;
	for (int i = 0; i < maxPoints; i++) {
	    point =
		new Point((int)
			  (cx
			   + Math.cos(2 * Math.PI / maxPoints * i) * radius),
			  (int)
			  (cy
			   + Math.sin(2 * Math.PI / maxPoints * i) * radius));
	    ret.add(point);
	}
	return ret;
		
    }

} /* end class FigFinalState */
