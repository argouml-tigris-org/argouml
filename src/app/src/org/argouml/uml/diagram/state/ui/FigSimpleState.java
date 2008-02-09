// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Iterator;

import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML MState in a diagram.
 *
 * @author ics 125b silverbullet team
 */
public class FigSimpleState extends FigState {

    private FigRect cover;
    private FigLine divider;

    /**
     * The main constructor
     */
    public FigSimpleState() {
	cover =
	    new FigRRect(getInitialX(), getInitialY(),
			 getInitialWidth(), getInitialHeight(),
			 Color.black, Color.white);

	getBigPort().setLineWidth(0);

	divider =
	    new FigLine(getInitialX(),
			getInitialY() + 2 + getNameFig().getBounds().height + 1,
			getInitialWidth() - 1,
			getInitialY() + 2 + getNameFig().getBounds().height + 1,
			Color.black);

	// add Figs to the FigNode in back-to-front order
	addFig(getBigPort());
	addFig(cover);
	addFig(getNameFig());
	addFig(divider);
	addFig(getInternal());

	//setBlinkPorts(false); //make port invisble unless mouse enters
	Rectangle r = getBounds();
	setBounds(r.x, r.y, r.width, r.height);
    }

    /**
     * The constructor that hooks into an existing UML element
     * @param gm ignored
     * @param node the UML element
     */
    public FigSimpleState(GraphModel gm, Object node) {
	this();
	setOwner(node);
    }


    @Override
    public Object clone() {
	FigSimpleState figClone = (FigSimpleState) super.clone();
	Iterator it = figClone.getFigs().iterator();
	figClone.setBigPort((FigRRect) it.next());
	figClone.cover = (FigRect) it.next();
	figClone.setNameFig((FigText) it.next());
	figClone.divider = (FigLine) it.next();
	figClone.setInternal((FigText) it.next());
	return figClone;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    @Override
    public Dimension getMinimumSize() {
	Dimension nameDim = getNameFig().getMinimumSize();
	Dimension internalDim = getInternal().getMinimumSize();

	int h = SPACE_TOP + nameDim.height
            + SPACE_MIDDLE + internalDim.height
            + SPACE_BOTTOM;
	int w = Math.max(nameDim.width + 2 * MARGIN,
                internalDim.width + 2 * MARGIN);
	return new Dimension(w, h);
    }

    /**
     * Override setBounds to keep shapes looking right.
     * {@inheritDoc}
     */
    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
	if (getNameFig() == null) {
	    return;
	}
	Rectangle oldBounds = getBounds();
	Dimension nameDim = getNameFig().getMinimumSize();

	getNameFig().setBounds(x + MARGIN,
                y + SPACE_TOP,
                w - 2 * MARGIN,
                nameDim.height);
	divider.setShape(x,
                y + DIVIDER_Y + nameDim.height,
                x + w - 1,
                y + DIVIDER_Y + nameDim.height);

	getInternal().setBounds(
                x + MARGIN,
	        y + SPACE_TOP + nameDim.height + SPACE_MIDDLE,
	        w - 2 * MARGIN,
	        h - SPACE_TOP - nameDim.height - SPACE_MIDDLE - SPACE_BOTTOM);

	getBigPort().setBounds(x, y, w, h);
	cover.setBounds(x, y, w, h);

	calcBounds(); //_x = x; _y = y; _w = w; _h = h;
	updateEdges();
	firePropChange("bounds", oldBounds, getBounds());
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    @Override
    public void setLineColor(Color col) {
        cover.setLineColor(col);
        divider.setLineColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    @Override
    public Color getLineColor() {
        return cover.getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    @Override
    public void setFillColor(Color col) {
        cover.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    @Override
    public Color getFillColor() {
        return cover.getFillColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    @Override
    public void setFilled(boolean f) {
        cover.setFilled(f);
        getBigPort().setFilled(f);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    @Override
    public boolean getFilled() {
        return cover.isFilled();
    }

    @Override
    public boolean isFilled() {
        return cover.isFilled();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    @Override
    public void setLineWidth(int w) {
        cover.setLineWidth(w);
        divider.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    @Override
    public int getLineWidth() {
        return cover.getLineWidth();
    }

    /*
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialHeight()
     */
    @Override
    protected int getInitialHeight() {
        return 40;
    }

    /*
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialWidth()
     */
    @Override
    protected int getInitialWidth() {
        return 70;
    }

    /*
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialX()
     */
    @Override
    protected int getInitialX() {
        return 0;
    }

    /*
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialY()
     */
    @Override
    protected int getInitialY() {
        return 0;
    }

} 
