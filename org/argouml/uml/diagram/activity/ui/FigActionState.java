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

import org.argouml.application.api.Notation;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.uml.diagram.state.ui.FigStateVertex;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MElementEvent;

/** Class to display graphics for a UML MState in a diagram. */

public class FigActionState extends FigStateVertex {

    ////////////////////////////////////////////////////////////////
    // constants

    private static final int MARGIN = 2;

    private static final int PADDING = 8;

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * UML does not really use ports, so just define one big one so that users
     * can drag edges to or from any point in the icon.
     */
    private FigRRect bigPort;

    private FigRRect cover;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor FigActionState
     */
    public FigActionState() {
        bigPort = new FigRRect(10 + 1, 10 + 1, 90 - 2, 25 - 2, Color.cyan,
                Color.cyan);
        bigPort.setCornerRadius(bigPort.getHalfHeight());
        cover = new FigRRect(10, 10, 90, 25, Color.black, Color.white);
        cover.setCornerRadius(getHalfHeight());

        bigPort.setLineWidth(0);
        getNameFig().setLineWidth(0);
        getNameFig().setBounds(10 + PADDING, 10, 90 - PADDING * 2, 25);
        getNameFig().setFilled(false);
        getNameFig().setMultiLine(true);

        // add Figs to the FigNode in back-to-front order
        addFig(bigPort);
        addFig(cover);
        addFig(getNameFig());

        //setBlinkPorts(false); //make port invisble unless mouse enters
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }

    /**
     * Constructor FigActionState
     * @param gm ignored!
     * @param node owner
     */
    public FigActionState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() {
        return "new ActionState";
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigActionState figClone = (FigActionState) super.clone();
        Iterator it = figClone.getFigs(null).iterator();
        figClone.bigPort = (FigRRect) it.next();
        figClone.cover = (FigRRect) it.next();
        figClone.setNameFig((FigText) it.next());
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    /**
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object node) {
        super.setOwner(node);
        bindPort(node, bigPort);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        Dimension nameDim = getNameFig().getMinimumSize();
        int w = nameDim.width + PADDING * 2;
        int h = nameDim.height + PADDING;
        return new Dimension(w, h);
    }

    /**
     * Override setBounds to keep shapes looking right
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    public void setBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) return;
        Rectangle oldBounds = getBounds();

        getNameFig().setBounds(x + PADDING, y, w - PADDING * 2, h - PADDING);
        bigPort.setBounds(x + 1, y + 1, w - 2, h - 2);
        cover.setBounds(x, y, w, h);
        bigPort.setCornerRadius(h);
        cover.setCornerRadius(h);

        calcBounds(); 
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
        cover.setLineColor(col);
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
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return cover.getLineWidth();
    }
    
    

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(ru.novosoft.uml.MElementEvent)
     */
    protected void modelChanged(MElementEvent mee) {        
        super.modelChanged(mee);
        if (mee.getSource() == getOwner() && mee.getName().equals("entry")) {
            if (mee.getNewValue() != null) {
                UmlModelEventPump.getPump().addModelEventListener(this, 
                                            mee.getNewValue(), "script");
            } else
                if (mee.getRemovedValue() != null) {
                    UmlModelEventPump.getPump().removeModelEventListener(this, 
                                            mee.getRemovedValue(), "script");
                }
            updateNameText();
            damage();
        } else
            if (ModelFacade.getEntry(getOwner()) == mee.getSource()) {
                updateNameText();
                damage();
            } 
        
    }
    
    
    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        if (getOwner() != null)
            getNameFig().setText(Notation.generate(this, getOwner()));
    }
} /* end class FigActionState */
