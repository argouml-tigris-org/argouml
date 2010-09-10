/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram.activity.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.state.ui.FigStateVertex;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigText;


/**
 * Class to display graphics for a UML SubactivityState in a diagram.
 *
 * @author MVW
 */
public class FigSubactivityState extends FigStateVertex {

    private static final int PADDING = 8;

    private static final int X = X0;
    private static final int Y = Y0;
    private static final int W = 90;
    private static final int H = 25;

    private static final int SX = 3;
    private static final int SY = 3;
    private static final int SW = 9;
    private static final int SH = 5;


    private FigRRect cover;

    private FigRRect s1;
    private FigRRect s2;
    private FigLine s3;

    
    /**
     * Construct a new FigSubactivityState.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigSubactivityState(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initFigs();
    }

    @Override
    protected Fig createBigPortFig() {
        FigRRect frr = new FigRRect(X, Y, W, H, DEBUG_COLOR, DEBUG_COLOR);
        frr.setCornerRadius(frr.getHeight() / 2);
        frr.setLineWidth(0);
        return frr;
    }

    private void initFigs() {
        cover = new FigRRect(X, Y, W, H, LINE_COLOR, FILL_COLOR);
        cover.setCornerRadius(getHeight() / 2);

        getNameFig().setLineWidth(0);
        getNameFig().setBounds(10 + PADDING, 10, 90 - PADDING * 2, 25);
        getNameFig().setFilled(false);
        getNameFig().setReturnAction(FigText.INSERT);
        getNameFig().setEditable(false);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(cover);
        addFig(getNameFig());

        makeSubStatesIcon(X + W, Y);

        setBounds(getBounds());
    }

    /**
     * @param x the x-coordinate of the right corner
     * @param y the y coordinate of the bottom corner
     */
    private void makeSubStatesIcon(int x, int y) {
        s1 = new FigRRect(x - 22, y + 3, 8, 6, LINE_COLOR, FILL_COLOR);
        s2 = new FigRRect(x - 11, y + 9, 8, 6, LINE_COLOR, FILL_COLOR);
        s1.setFilled(true);
        s2.setFilled(true);
        s1.setLineWidth(LINE_WIDTH);
        s2.setLineWidth(LINE_WIDTH);
        s1.setCornerRadius(SH);
        s2.setCornerRadius(SH);
        s3 = new FigLine(x - 18, y + 6, x - 7, y + 12, LINE_COLOR);

        addFig(s3); // add them back to front
        addFig(s1);
        addFig(s2);
    }

    /*
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        FigSubactivityState figClone = (FigSubactivityState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRRect) it.next());
        figClone.cover = (FigRRect) it.next();
        figClone.setNameFig((FigText) it.next());
        return figClone;
    }


    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    @Override
    public Dimension getMinimumSize() {
        Dimension nameDim = getNameFig().getMinimumSize();
        int w = nameDim.width + PADDING * 2;
        int h = nameDim.height + PADDING;
        return new Dimension(Math.max(w, W / 2), Math.max(h, H / 2));
    }

    /*
     * Override setBounds to keep shapes looking right.
     *
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
        }
        Rectangle oldBounds = getBounds();

        getNameFig().setBounds(x + PADDING, y, w - PADDING * 2, h - PADDING);
        getBigPort().setBounds(x, y, w, h);
        cover.setBounds(x, y, w, h);
        ((FigRRect) getBigPort()).setCornerRadius(h);
        cover.setCornerRadius(h);

        s1.setBounds(x + w - 2 * (SX + SW), y + h - 1 * (SY + SH), SW, SH);
        s2.setBounds(x + w - 1 * (SX + SW), y + h - 2 * (SY + SH), SW, SH);
        s3.setShape(x + w - (SX * 2 + SW + SW / 2), y + h - (SY + SH / 2),
                x + w - (SX + SW / 2), y + h - (SY * 2 + SH + SH / 2));

        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    @Override
    public void setLineColor(Color col) {
        cover.setLineColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return cover.getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
        cover.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
        return cover.getFillColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    @Override
    public void setFilled(boolean f) {
        cover.setFilled(f);
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
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    @Override
    public int getLineWidth() {
        return cover.getLineWidth();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    @Override
    protected void modelChanged(PropertyChangeEvent mee) {
        // Let our superclass sort itself out first
        super.modelChanged(mee);
        if (mee instanceof AssociationChangeEvent 
                || mee instanceof AttributeChangeEvent) {
            // TODO: Rather than specifically ignore some item maybe it would be better
            // to specifically state what items are of interest. Otherwise we may still
            // be acting on other events we don't need
            if (!Model.getFacade().isATransition(mee.getNewValue())) {
                renderingChanged();
                updateListeners(getOwner(), getOwner());
            }
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object)
     */
    @Override
    protected void updateListeners(Object oldOwner, Object newOwner) {
        Set<Object[]> l = new HashSet<Object[]>();
        if (newOwner != null) {
            // add the listeners to the newOwner
            l.add(new Object[] {newOwner, null});
            // and listen to name changes of the submachine
            Object machine = Model.getFacade().getSubmachine(newOwner);
            if (machine != null) {
                l.add(new Object[] {machine, null});
            }
        }
        updateElementListeners(l);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    @Override
    protected void updateNameText() {
        String s = "";
        if (getOwner() != null) {
            Object machine = Model.getFacade().getSubmachine(getOwner());
            if (machine != null) {
                s = Model.getFacade().getName(machine);
            }
        }
        if (s == null) {
            s = "";
        }
        getNameFig().setText(s);
    }

}
