/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
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
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeEvent;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.ActionAddConcurrentRegion;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML CompositeState in a diagram.
 *
 * @author jrobbins@ics.uci.edu
 */
public class FigCompositeState extends FigState {

    private FigRect cover;
    private FigLine divider;

    
    /**
     * Construct a new FigStubState.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigCompositeState(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initFigs();
        updateNameText();
    }

    private void initFigs() {
        cover =
            new FigRRect(getInitialX(), getInitialY(),
			      getInitialWidth(), getInitialHeight(),
			      LINE_COLOR, FILL_COLOR);

        getBigPort().setLineWidth(0);

        divider =
	    new FigLine(getInitialX(),
			getInitialY() + 2 + getNameFig().getBounds().height + 1,
			getInitialWidth() - 1,
			getInitialY() + 2 + getNameFig().getBounds().height + 1,
			LINE_COLOR);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(cover);
        addFig(getNameFig());
        addFig(divider);
        addFig(getInternal());

        setBounds(getBounds());
    }

    /*
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigCompositeState figClone = (FigCompositeState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRRect) it.next());
        figClone.cover = (FigRect) it.next();
        figClone.setNameFig((FigText) it.next());
        figClone.divider = (FigLine) it.next();
        figClone.setInternal((FigText) it.next());
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        Dimension nameDim = getNameFig().getMinimumSize();
        Dimension internalDim = getInternal().getMinimumSize();

        int h =
            SPACE_TOP + nameDim.height
            + SPACE_MIDDLE + internalDim.height
            + SPACE_BOTTOM;
        int w =
            Math.max(nameDim.width + 2 * MARGIN,
                     internalDim.width + 2 * MARGIN);
        return new Dimension(w, h);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getUseTrapRect()
     */
    public boolean getUseTrapRect() {
        return true;
    }

    /*
     * Override setBounds to keep shapes looking right.
     *
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    protected void setStandardBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
        }

        Rectangle oldBounds = getBounds();
        Dimension nameDim = getNameFig().getMinimumSize();
        List regionsList = getEnclosedFigs();

        /* If it is concurrent and contains concurrent regions,
        the bottom region has a minimum height*/
        if (getOwner() != null) {
            if (isConcurrent()
                    && !regionsList.isEmpty()
                    && regionsList.get(regionsList.size() - 1)
                        instanceof FigConcurrentRegion) {
                FigConcurrentRegion f = 
                    ((FigConcurrentRegion) regionsList.get(
                            regionsList.size() - 1));
                Rectangle regionBounds = f.getBounds();
                if ((h - oldBounds.height + regionBounds.height)
                        <= (f.getMinimumSize().height)) {
                    h = oldBounds.height;
                    y = oldBounds.y;
                }
            }
        }

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
                y + nameDim.height + SPACE_TOP + SPACE_MIDDLE,
                w - 2 * MARGIN,
                h - nameDim.height - SPACE_TOP - SPACE_MIDDLE - SPACE_BOTTOM);

        getBigPort().setBounds(x, y, w, h);
        cover.setBounds(x, y, w, h);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());

        /*If it is concurrent and contains concurrent regions,
        the regions are resized*/
        if (getOwner() != null) {
            if (isConcurrent()
                    && !regionsList.isEmpty()
                    && regionsList.get(regionsList.size() - 1)
                        instanceof FigConcurrentRegion) {
                FigConcurrentRegion f = ((FigConcurrentRegion) regionsList
                        .get(regionsList.size() - 1));
                for (int i = 0; i < regionsList.size() - 1; i++) {
                    ((FigConcurrentRegion) regionsList.get(i))
                        .setBounds(x - oldBounds.x, y - oldBounds.y,
                                w - 2 * FigConcurrentRegion.INSET_HORZ, true);
                }
                f.setBounds(x - oldBounds.x,
                        y - oldBounds.y, 
                        w - 2 * FigConcurrentRegion.INSET_HORZ, 
                        h - oldBounds.height, true);
            }
        }

    }
    
    /*
     * The returned list of Figs is sorted according layout: from top to bottom.
     */
    @Override
    public Vector<Fig> getEnclosedFigs() {
        Vector<Fig> enclosedFigs = super.getEnclosedFigs();

        if (isConcurrent()) {
            TreeMap<Integer, Fig> figsByY = new TreeMap<Integer, Fig>();
            for (Fig fig : enclosedFigs) {
                if (fig instanceof FigConcurrentRegion) {
                    figsByY.put(fig.getY(), fig);
                }
            }
            return new Vector<Fig>(figsByY.values());
        }
        return enclosedFigs;
    }
    
    /**
     * @return true if this is a concurrent state, 
     * false otherwise, or if the owner is not known
     */
    public boolean isConcurrent() {
        Object owner = getOwner();
        if (owner == null) {
            return false;
        }
        return Model.getFacade().isConcurrent(owner);
    }

    /**
     * To resize only when a new concurrent region is added,
     * changing the height.
     * TODO: Probably shouldn't
     * exist as this class should be listening for added concurrent regions
     * and call this internally itself.
     *
     * @param h the new height
     */
    public void setCompositeStateHeight(int h) {
        if (getNameFig() == null) {
            return;
        }
        Rectangle oldBounds = getBounds();
        Dimension nameDim = getNameFig().getMinimumSize();
        int x = oldBounds.x;
        int y = oldBounds.y;
        int w = oldBounds.width;

        getInternal().setBounds(
                x + MARGIN, 
                y + nameDim.height + 4,
                w - 2 * MARGIN, 
                h - nameDim.height - 6);
        getBigPort().setBounds(x, y, w, h);
        cover.setBounds(x, y, w, h);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }


    /*
     * @see org.tigris.gef.ui.PopupGenerator#getPopUpActions(java.awt.event.MouseEvent)
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        /* Check if multiple items are selected: */
        boolean ms = TargetManager.getInstance().getTargets().size() > 1;
        if (!ms) {
            popUpActions.add(
                    popUpActions.size() - getPopupAddOffset(),
                    new ActionAddConcurrentRegion());
        }
        return popUpActions;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
        cover.setLineColor(col);
        divider.setLineColor(col);
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
    public void setFilled(boolean f) {
        cover.setFilled(f);
        getBigPort().setFilled(f);
    }


    @Override
    public boolean isFilled() {
        return cover.isFilled();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        cover.setLineWidth(w);
        divider.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return cover.getLineWidth();
    }

    ////////////////////////////////////////////////////////////////
    // event processing

    @Override 
    protected void updateLayout(UmlChangeEvent event) {
        /* We only handle the case where a region has been removed: */
        if (!(event instanceof RemoveAssociationEvent)
                ||  !"subvertex".equals(event.getPropertyName())) {
            return;
        }
        
        final Object removedRegion = event.getOldValue();
        
        List<FigConcurrentRegion> regionFigs =
            ((List<FigConcurrentRegion>) getEnclosedFigs().clone());

        int totHeight = getInitialHeight();
        if (!regionFigs.isEmpty()) {
            Fig removedFig = null;
            for (FigConcurrentRegion figRegion : regionFigs) {
                if (figRegion.getOwner() == removedRegion) {
                    removedFig = figRegion;
                    removeEnclosedFig(figRegion);
                    break;
                }
            }
            if (removedFig != null) {
                regionFigs.remove(removedFig);
                if (!regionFigs.isEmpty()) {
                    for (FigConcurrentRegion figRegion : regionFigs) {
                        if (figRegion.getY() > removedFig.getY()) {
                            figRegion.displace(0, -removedFig.getHeight());
                        }
                    }
                    totHeight = getHeight() - removedFig.getHeight();
                }
            }
        }
        
        setBounds(getX(), getY(), getWidth(), totHeight);

        // do we need to 
        renderingChanged();
    }


    /*
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialHeight()
     */
    protected int getInitialHeight() {
        return 150;
    }

    /*
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialWidth()
     */
    protected int getInitialWidth() {
        return 180;
    }

    /*
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialX()
     */
    protected int getInitialX() {
        return 0;
    }

    /*
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialY()
     */
    protected int getInitialY() {
        return 0;
    }

} /* end class FigCompositeState */
