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

package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.Iterator;

import org.argouml.uml.generator.ParserDisplay;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MElementEvent;

/**
 * Class to display graphics for a UML MCompositeState in a diagram.
 *
 * @author jrobbins@ics.uci.edu
 */
public class FigCompositeState extends FigState {

    private static final int MARGIN = 2;

    ////////////////////////////////////////////////////////////////
    // instance variables

    private FigRRect bigPort;
    private FigRect cover;
    private FigLine divider;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The main constructor
     * 
     */
    public FigCompositeState() {
        super();
        bigPort = new FigRRect(getInitialX() + 1, getInitialY() + 1,
				getInitialWidth() - 2, getInitialHeight() - 2,
				Color.cyan, Color.cyan);
        cover = new FigRRect(getInitialX(), getInitialY(),
			      getInitialWidth(), getInitialHeight(),
			      Color.black, Color.white);

        bigPort.setLineWidth(0);
        getNameFig().setLineWidth(0);
        getNameFig().setBounds(getInitialX() + 2, getInitialY() + 2,
			       getInitialWidth() - 4,
			       getNameFig().getBounds().height);
        getNameFig().setFilled(false);

        divider =
	    new FigLine(getInitialX(),
			getInitialY() + 2 + getNameFig().getBounds().height + 1,
			getInitialWidth() - 1,
			getInitialY() + 2 + getNameFig().getBounds().height + 1,
			Color.black);

        // add Figs to the FigNode in back-to-front order
        addFig(bigPort);
        addFig(cover);
        addFig(getNameFig());
        addFig(divider);
        addFig(internal);

        //setBlinkPorts(false); //make port invisble unless mouse enters
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }

    /**
     * The constructor for when a new Fig is created for an existing UML elm
     * @param gm ignored 
     * @param node the UML element
     */
    public FigCompositeState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() {
        return "new MCompositeState";
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigCompositeState figClone = (FigCompositeState) super.clone();
        Iterator it = figClone.getFigs(null).iterator();
        figClone.bigPort = (FigRRect) it.next();
        figClone.cover = (FigRect) it.next();
        figClone.setNameFig((FigText) it.next());
        figClone.divider = (FigLine) it.next();
        figClone.internal = (FigText) it.next();
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        Dimension nameDim = getNameFig().getMinimumSize();
        Dimension internalDim = internal.getMinimumSize();

        int h = nameDim.height + 4 + internalDim.height;
        int w = Math.max(nameDim.width + 4, internalDim.width + 4);
        return new Dimension(w, h);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getUseTrapRect()
     */
    public boolean getUseTrapRect() {
        return true;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     *
     * Override setBounds to keep shapes looking right.
     */
    public void setBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
	}
        Rectangle oldBounds = getBounds();
        Dimension nameDim = getNameFig().getMinimumSize();

        getNameFig().setBounds(x + 2, y + 2, w - 4, nameDim.height);
        divider.setShape(x, y + nameDim.height + 1,
			  x + w - 1, y + nameDim.height + 1);

        internal.setBounds(x + 2, y + nameDim.height + 4,
			    w - 4, h - nameDim.height - 6);

        bigPort.setBounds(x, y, w, h);
        cover.setBounds(x, y, w, h);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    ////////////////////////////////////////////////////////////////
    // fig accessors

    /**
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
        cover.setLineColor(col);
        divider.setLineColor(col);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return cover.getLineColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
        cover.setFillColor(col);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
        return cover.getFillColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean f) {
        cover.setFilled(f);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return cover.getFilled();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        cover.setLineWidth(w);
        divider.setLineWidth(w);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return cover.getLineWidth();
    }

    ////////////////////////////////////////////////////////////////
    // event processing

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(ru.novosoft.uml.MElementEvent)
     *
     * Update the text labels.
     */
    protected void modelChanged(MElementEvent mee) {
        super.modelChanged(mee);

        if (mee.getName().equals("isConcurrent")) {
            // TODO: this should split the composite state into two
            // regions. This must be implemented
            updateInternal();
        }

    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    public void textEdited(FigText ft) throws PropertyVetoException {
        super.textEdited(ft);
        if (ft == internal) {
            Object st = /*(MState)*/ getOwner();
            if (st == null)
                return;
            String s = ft.getText();
            ParserDisplay.SINGLETON.parseStateBody(st, s);
        }
    }

    /**
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialHeight()
     */
    protected int getInitialHeight() {
        return 150;
    }

    /**
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialWidth()
     */
    protected int getInitialWidth() {
        return 180;
    }

    /**
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialX()
     */
    protected int getInitialX() {
        return 0;
    }

    /**
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialY()
     */
    protected int getInitialY() {
        return 0;
    }

} /* end class FigCompositeState */
