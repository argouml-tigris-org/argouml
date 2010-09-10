/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2003-2009 The Regents of the University of California. All
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
import java.util.Iterator;

import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigEmptyRect;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigStereotypesGroup;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;

/**
 * This class represents a Pool of Swimlanes for Activity diagrams. This is
 * exists only to act as the GEF style encloser. Other nodes in the same
 * must be placed entirely within or outside the boundaries of this Fig
 * but cannot intersect with the boundary. <p>
 * TODO: There is no actual model element being represented here so we are
 * inheriting a lot of behaviour from FigNodeModelElement that we don't want.
 * We require to split FigNodeModelElement to separate the code that requires
 * a model element owner. See issue ... <p>
 * Remark mvw: Why not give it an owner instead? The ActivityGraph 
 * is the obvious candidate, or maybe the top state.
 *
 * @author mkl
 */
public class FigPool extends FigNodeModelElement {

    private void initialize(Rectangle r) {
        /* TODO: Replace the next deprecated call. This case is complicated 
         * by the use of parameters. All other Figs work differently. */
        setBigPort(new FigEmptyRect(r.x, r.y, r.width, r.height));
        getBigPort().setFilled(false);
        getBigPort().setLineWidth(0);

        addFig(getBigPort());

        setBounds(r);
    }
    
    /**
     * Constructor used by PGML parser when loading.
     * 
     * @param bounds bounding box rectangle
     * @param settings (ignored since this is just an empty rectangle)
     */
    public FigPool(Rectangle bounds, DiagramSettings settings) {
        super(null, bounds, settings);
        initialize(bounds);
    }


    /**
     * Create the Fig containing the stereotype(s). As there is no stereotype
     * display for this Fig we return null
     *
     * @return the stereotype FigGroup
     */
    protected FigStereotypesGroup createStereotypeFig() {
        return null;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#clone()
     */
    @Override
    public Object clone() {
        FigPool figClone = (FigPool) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        return figClone;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#addEnclosedFig(org.tigris.gef.presentation.Fig)
     */
    @Override
    public void addEnclosedFig(Fig figState) {
        super.addEnclosedFig(figState);
        Iterator it = getLayer().getContentsNoEdges().iterator();
        while (it.hasNext()) {
            Fig f = (Fig) it.next();
            if (f instanceof FigPartition
            	&& f.getBounds().intersects(figState.getBounds())) { 
                Model.getCoreHelper().setModelElementContainer(
                            figState.getOwner(), f.getOwner());
            }
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    @Override
    public void setFillColor(Color col) {
        getBigPort().setFillColor(col);
        getNameFig().setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    @Override
    public Color getFillColor() {
        return getBigPort().getFillColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    @Override
    public void setFilled(boolean f) {
        getBigPort().setFilled(f);
    }

    @Override
    public boolean isFilled() {
        return getBigPort().isFilled();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    @Override
    public Dimension getMinimumSize() {
        Dimension nameDim = getNameFig().getMinimumSize();
        int w = nameDim.width;
        int h = nameDim.height;

        // we want to maintain a minimum size for the partition
        w = Math.max(64, w);
        h = Math.max(256, h);

        return new Dimension(w, h);
    }

    /**
     * Using a traprect enables us to move containing figs easily.
     *
     * @return <code>true</code>
     *
     * @see org.tigris.gef.presentation.Fig#getUseTrapRect()
     */
    @Override
    public boolean getUseTrapRect() {
        return true;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
	
        Rectangle oldBounds = getBounds();
        getBigPort().setBounds(x, y, w, h);

        firePropChange("bounds", oldBounds, getBounds());
        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
    }
    
    /**
     * A FigPool can't be selected. Instead it is dragged or resized whenever
     * one of its swimlanes is dragged or resized.
     * @return false at all times
     */
    @Override
    public boolean isSelectable() {
        return false;
    }

}

