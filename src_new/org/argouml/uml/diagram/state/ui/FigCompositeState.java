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

// File: FigCompositeState.java
// Classes: FigCompositeState
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.Vector;

import org.apache.log4j.Category;
import org.argouml.uml.generator.ParserDisplay;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.behavior.state_machines.MState;

/** Class to display graphics for a UML MCompositeState in a diagram. */

public class FigCompositeState extends FigState {
    protected static Category cat =
	Category.getInstance(FigCompositeState.class);

    ////////////////////////////////////////////////////////////////
    // constants

    public final int MARGIN = 2;

    ////////////////////////////////////////////////////////////////
    // instance variables

    /** The main label on this icon. */
    //FigText _name;

    FigRect _cover;
    FigLine _divider;

    // add other Figs here aes needed

    ////////////////////////////////////////////////////////////////
    // constructors

    public FigCompositeState() {
        super();
        _bigPort = new FigRRect(getInitialX() + 1, getInitialY() + 1,
				getInitialWidth() - 2, getInitialHeight() - 2,
				Color.cyan, Color.cyan);
        _cover = new FigRRect(getInitialX(), getInitialY(),
			      getInitialWidth(), getInitialHeight(),
			      Color.black, Color.white);

        _bigPort.setLineWidth(0);
        _name.setLineWidth(0);
        _name.setBounds(getInitialX() + 2, getInitialY() + 2,
			getInitialWidth() - 4, _name.getBounds().height);
        _name.setFilled(false);

        _divider =
	    new FigLine(getInitialX(),
			getInitialY() + 2 + _name.getBounds().height + 1,
			getInitialWidth() - 1,
			getInitialY() + 2 + _name.getBounds().height + 1,
			Color.black);

        // add Figs to the FigNode in back-to-front order
        addFig(_bigPort);
        addFig(_cover);
        addFig(_name);
        addFig(_divider);
        addFig(_internal);

        //setBlinkPorts(false); //make port invisble unless mouse enters
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }

    public FigCompositeState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    public String placeString() {
        return "new MCompositeState";
    }

    public Object clone() {
        FigCompositeState figClone = (FigCompositeState) super.clone();
        Vector v = figClone.getFigs();
        figClone._bigPort = (FigRect) v.elementAt(0);
        figClone._cover = (FigRect) v.elementAt(1);
        figClone._name = (FigText) v.elementAt(2);
        figClone._divider = (FigLine) v.elementAt(3);
        figClone._internal = (FigText) v.elementAt(4);
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public Dimension getMinimumSize() {
        Dimension nameDim = _name.getMinimumSize();
        Dimension internalDim = _internal.getMinimumSize();

        int h = nameDim.height + 4 + internalDim.height;
        int w = Math.max(nameDim.width + 4, internalDim.width + 4);
        return new Dimension(w, h);
    }

    public boolean getUseTrapRect() {
        return true;
    }

    /* Override setBounds to keep shapes looking right */
    public void setBounds(int x, int y, int w, int h) {
        if (_name == null)
            return;
        Rectangle oldBounds = getBounds();
        Dimension nameDim = _name.getMinimumSize();

        _name.setBounds(x + 2, y + 2, w - 4, nameDim.height);
        _divider.setShape(x, y + nameDim.height + 1,
			  x + w - 1, y + nameDim.height + 1);

        _internal.setBounds(x + 2, y + nameDim.height + 4,
			    w - 4, h - nameDim.height - 6);

        _bigPort.setBounds(x, y, w, h);
        _cover.setBounds(x, y, w, h);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    ////////////////////////////////////////////////////////////////
    // fig accessors

    public void setLineColor(Color col) {
        _cover.setLineColor(col);
        _divider.setLineColor(col);
    }

    public Color getLineColor() {
        return _cover.getLineColor();
    }

    public void setFillColor(Color col) {
        _cover.setFillColor(col);
    }
    public Color getFillColor() {
        return _cover.getFillColor();
    }

    public void setFilled(boolean f) {
        _cover.setFilled(f);
    }
    public boolean getFilled() {
        return _cover.getFilled();
    }

    public void setLineWidth(int w) {
        _cover.setLineWidth(w);
        _divider.setLineWidth(w);
    }
    public int getLineWidth() {
        return _cover.getLineWidth();
    }

    ////////////////////////////////////////////////////////////////
    // event processing

    /** Update the text labels */
    protected void modelChanged(MElementEvent mee) {
        super.modelChanged(mee);

        if (mee.getName().equals("isConcurrent")) {
            // TODO: this should split the composite state into two
            // regions. This must be implemented
            updateInternal();
        }

    }

    public void textEdited(FigText ft) throws PropertyVetoException {
        super.textEdited(ft);
        if (ft == _internal) {
            MState st = (MState) getOwner();
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
