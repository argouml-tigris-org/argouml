/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.activity.ui.SelectionActionState;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;

/**
 * Class to display graphics for a UML Initial State in a diagram.
 *
 * @author abonner
 */
public class FigInitialState extends FigStateVertex {

    private static final int X = X0;
    private static final int Y = Y0;
    private static final int STATE_WIDTH = 16;
    private static final int HEIGHT = 16;

    private FigCircle head;

    
    /**
     * Construct a new FigSubactivityState.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigInitialState(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initFigs();
    }

    @Override
    protected Fig createBigPortFig() {
        return
            new FigCircle(X, Y, STATE_WIDTH, HEIGHT, DEBUG_COLOR, DEBUG_COLOR);
    }

    private void initFigs() {
        setEditable(false);

        head = new FigCircle(X, Y, STATE_WIDTH, HEIGHT, LINE_COLOR,
                SOLID_FILL_COLOR);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(head);

        setBlinkPorts(false); //make port invisible unless mouse enters
    }

    @Override
    public Object clone() {
        FigInitialState figClone = (FigInitialState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        setBigPort((FigCircle) it.next());
        figClone.head = (FigCircle) it.next();
        return figClone;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    @Override
    public Selection makeSelection() {
        Object pstate = getOwner();
        Selection sel = null;
        if ( pstate != null) {
            if (Model.getFacade().getUmlVersion().startsWith("1")
                    && Model.getFacade().isAActivityGraph(
                            Model.getFacade().getStateMachine(
                                    Model.getFacade().getContainer(pstate)))) {
                sel = new SelectionActionState(this);
                ((SelectionActionState) sel).setIncomingButtonEnabled(false);
                Collection outs = Model.getFacade().getOutgoings(getOwner());
                ((SelectionActionState) sel)
                        .setOutgoingButtonEnabled(outs.isEmpty());
            } else {
                sel = new SelectionState(this);
                ((SelectionState) sel).setIncomingButtonEnabled(false);
                Collection outs = Model.getFacade().getOutgoings(getOwner());
                ((SelectionState) sel)
                        .setOutgoingButtonEnabled(outs.isEmpty());
            }
        }
        return sel;
    }

    /**
     * Initial states are fixed size.
     *
     * @return false
     */
    @Override
    public boolean isResizable() {
        return false;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    @Override
    public void setLineColor(Color col) {
        head.setLineColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    @Override
    public Color getLineColor() {
        return head.getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    @Override
    public void setFillColor(Color col) {
        head.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    @Override
    public Color getFillColor() {
        return head.getFillColor();
    }

    /**
     * Ignored - figure has fixed rendering
     * @param f ignored
     */
    @Override
    public void setFilled(boolean f) {
        // ignored - rendering is fixed
    }


    @Override
    public boolean isFilled() {
        return true;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    @Override
    public void setLineWidth(int w) {
        head.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    @Override
    public int getLineWidth() {
        return head.getLineWidth();
    }

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        // ignore mouse clicks
    }

    /**
     * The UID.
     */
    static final long serialVersionUID = 6572261327347541373L;

    /**
     * Return a list of gravity points around the outer circle. Used in place of
     * the default bounding box.
     * 
     * {@inheritDoc}
     */
    @Override
    public List getGravityPoints() {
        return getCircleGravityPoints();
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


        getBigPort().setBounds(x, y, w, h);
        head.setBounds(x, y, w, h);
        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }
}
