// $Id$
// Copyright (c) 2004-2005 The Regents of the University of California. All
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

package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigPoly;

/** Class to display graphics for a UML Junction State in a diagram.
 *
 * @author pepargouml
 */
public class FigJunctionState extends FigStateVertex {

    ////////////////////////////////////////////////////////////////
    // constants

    private static final int X = 0;
    private static final int Y = 0;
    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;

    ////////////////////////////////////////////////////////////////
    // instance variables

    private FigPoly head;

    ////////////////////////////////////////////////////////////////
    // constructors
    /** constructor
     */
    public FigJunctionState() {
	setBigPort(new FigPoly(Color.cyan, Color.cyan));
	head = new FigPoly(Color.black, Color.white);
	getBigPort().addPoint(X, Y);
	getBigPort().addPoint(X + WIDTH / 2, Y + HEIGHT / 2);
	getBigPort().addPoint(X, Y + HEIGHT);
	getBigPort().addPoint(X - WIDTH / 2, Y + HEIGHT / 2);

	head.addPoint(X, Y);
	head.addPoint(X + WIDTH / 2, Y + HEIGHT / 2);
	head.addPoint(X, Y + HEIGHT);
	head.addPoint(X - WIDTH / 2, Y + HEIGHT / 2);
	head.addPoint(X, Y);

	// Add the following to allow name editing on the diagram
/*        setNameFig(new FigText(X + 10, Y + 22, 0, 21, true));
        getNameFig().setFilled(false);
        getNameFig().setLineWidth(0);
        getNameFig().setFont(getLabelFont());
        getNameFig().setTextColor(Color.black);
        getNameFig().setMultiLine(false);
        getNameFig().setAllowsTab(false);
        getNameFig().setJustificationByName("center");
*/
	// add Figs to the FigNode in back-to-front order
	addFig(getBigPort());
	addFig(head);
//        addFig(getNameFig());

	setBlinkPorts(false); //make port invisble unless mouse enters
	Rectangle r = getBounds();
    }

    /**
     * constructor
     *
     * @param gm ignored
     * @param node the owner
     */
    public FigJunctionState(GraphModel gm, Object node) {
	this();
	setOwner(node);
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
	FigJunctionState figClone = (FigJunctionState) super.clone();
	Iterator it = figClone.getFigs(null).iterator();
	figClone.setBigPort((FigPoly) it.next());
	figClone.head = (FigPoly) it.next();
	return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accesors

    /** Initial states are fixed size.
     * @see org.tigris.gef.presentation.Fig#isResizable()
     */
    public boolean isResizable() { return false; }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
        head.setLineColor(col);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return head.getLineColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
        head.setFillColor(col);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
        return head.getFillColor();
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
        head.setLineWidth(w);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return head.getLineWidth();
    }

    ////////////////////////////////////////////////////////////////
    // Event handlers

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) { }

    /**
     * Block any textentry on the diagram - there is nothing to edit!
     *
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent ke) { }


} /* end class FigJunctionState */
