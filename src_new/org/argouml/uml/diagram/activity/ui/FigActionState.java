// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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
import java.awt.Rectangle;
import java.util.Iterator;

import org.argouml.uml.diagram.state.ui.FigStateVertex;
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

    /**
     * UML does not really use ports, so just define one big one so that users
     * can drag edges to or from any point in the icon.
     */
    // TODO: _bigPort is already defined in FigNodeModelElement
    // Why do we need it redefined here?
    FigRRect _bigPort;

    FigRRect _cover;

    ////////////////////////////////////////////////////////////////
    // constructors

    public FigActionState() {
        _bigPort = new FigRRect(10 + 1, 10 + 1, 90 - 2, 25 - 2, Color.cyan,
                Color.cyan);
        _cover = new FigRRect(10, 10, 90, 25, Color.black, Color.white);

        _bigPort.setLineWidth(0);
        getNameFig().setLineWidth(0);
        getNameFig().setBounds(10 + PADDING, 10, 90 - PADDING * 2, 25);
        getNameFig().setFilled(false);
        getNameFig().setMultiLine(true);

        // add Figs to the FigNode in back-to-front order
        addFig(_bigPort);
        addFig(_cover);
        addFig(getNameFig());

        //setBlinkPorts(false); //make port invisble unless mouse enters
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }

    public FigActionState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    public String placeString() {
        return "new ActionState";
    }

    public Object clone() {
        FigActionState figClone = (FigActionState) super.clone();
        Iterator it = figClone.getFigs(null).iterator();
        figClone._bigPort = (FigRRect) it.next();
        figClone._cover = (FigRRect) it.next();
        figClone.setNameFig((FigText) it.next());
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    public void setOwner(Object node) {
        super.setOwner(node);
        bindPort(node, _bigPort);
    }

    public Dimension getMinimumSize() {
        Dimension nameDim = getNameFig().getMinimumSize();
        int w = nameDim.width + PADDING * 2;
        int h = nameDim.height; // + PADDING*2;
        return new Dimension(w, h);
    }

    /* Override setBounds to keep shapes looking right */
    public void setBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) return;
        Rectangle oldBounds = getBounds();

        getNameFig().setBounds(x + PADDING, y, w - PADDING * 2, h);
        _bigPort.setBounds(x + 1, y + 1, w - 2, h - 2);
        _cover.setBounds(x, y, w, h);
        _bigPort.setCornerRadius(h / 3 * 2);
        _cover.setCornerRadius(h / 3 * 2);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    public void setLineColor(Color col) {
        _cover.setLineColor(col);
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
    }

    public int getLineWidth() {
        return _cover.getLineWidth();
    }

} /* end class FigActionState */
