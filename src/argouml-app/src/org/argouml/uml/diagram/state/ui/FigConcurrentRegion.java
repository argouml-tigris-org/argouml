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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JSeparator;

import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.ui.ProjectActions;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.ActionAddConcurrentRegion;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;
import org.tigris.gef.presentation.Handle;

/**
 * Class to display graphics for a UML ConcurrentRegion in a diagram.
 *
 * @author pepargouml@yahoo.es *
 */
public class FigConcurrentRegion extends FigState
        implements
        MouseListener,
        MouseMotionListener {

    /**
     * The horizontal margin between the region and its composite parent state.
     */
    public static final int INSET_HORZ = 3;
    /**
     * The vertical margin between the region and its composite parent state.
     */
    public static final int INSET_VERT = 5;

    private FigRect cover;
    /**
     * The divider line is the horizontal dashed line
     * shown at the top side for
     * every region except the first one.
     */
    private FigLine dividerline;
    private static Handle curHandle = new Handle(-1);

    private void initialize() {
        cover =
            new FigRect(getInitialX(),
                getInitialY(),
                getInitialWidth(), getInitialHeight(),
                INVISIBLE_LINE_COLOR, FILL_COLOR);
        dividerline = new FigLine(getInitialX(),
                getInitialY(),
                getInitialWidth(),
                getInitialY(),
                getInitialColor());
        dividerline.setDashed(true);
        getBigPort().setLineWidth(0);
        cover.setLineWidth(0);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(cover);
        addFig(getNameFig());
        addFig(dividerline);
        addFig(getInternal());

        setShadowSize(0);
    }

    /**
     * Construct a new concurrent region fig.
     * 
     * @param node owning UML element
     * @param bounds position and size
     * @param settings render settings
     */
    public FigConcurrentRegion(Object node, Rectangle bounds, DiagramSettings
            settings) {
        super(node, bounds, settings);
        initialize();
        if (bounds != null) {
            /* We have to use the specific methods written for this Fig: 
             * This fixes issue 5070. */
            setBounds(bounds.x - _x, bounds.y - _y, bounds.width, 
                    bounds.height - _h, true);
        }
        updateNameText();
    }

    /**
     * The moment we add this fig to a layer, 
     * especially during load,
     * it needs to be made aware that it 
     * is enclosed by a FigCompositeState.
     * This fixes issue 3736.
     * 
     * @param lay the layer
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#setLayer(org.tigris.gef.base.Layer)
     */
    @Override
    public void setLayer(Layer lay) {
        super.setLayer(lay);
        for (Fig f : lay.getContents()) {
            if (f instanceof FigCompositeState) {
                if (f.getOwner() 
                        == Model.getFacade().getContainer(getOwner())) {
                    setEnclosingFig(f);
                    break; // there can only be one
                }
            }
        }
    }
    
    /*
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        FigConcurrentRegion figClone = (FigConcurrentRegion) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.cover = (FigRect) it.next();
        figClone.setNameFig((FigText) it.next());
        figClone.dividerline = (FigLine) it.next();
        figClone.setInternal((FigText) it.next());
        return figClone;
    }

    /*
     * @see org.tigris.gef.ui.PopupGenerator#getPopUpActions(java.awt.event.MouseEvent)
     */
    @Override
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        popUpActions.remove(
                ProjectActions.getInstance().getRemoveFromDiagramAction());
        popUpActions.add(new JSeparator());
        // TODO: There's a cyclic dependency between FigConcurrentRegion and
        // the actions ActionAddConcurrentRegion
        popUpActions.addElement(
                new ActionAddConcurrentRegion());
        return popUpActions;
    }


    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    @Override
    public Dimension getMinimumSize() {
        Dimension nameDim = getNameFig().getMinimumSize();
        Dimension internalDim = getInternal().getMinimumSize();
        int h = nameDim.height + 4 + internalDim.height;
        int w = nameDim.width + 2 * MARGIN;
        return new Dimension(w, h);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getUseTrapRect()
     */
    @Override
    public boolean getUseTrapRect() {
        return true;
    }

    /**
     * Override setBounds to keep shapes looking right. <p>
     *
     * When resized by this way, it only changes the height and the
     * adjacent region's height.
     *
     * {@inheritDoc}
     */
    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
        }
        Rectangle oldBounds = getBounds();
        Dimension nameDim = getNameFig().getMinimumSize();
        int adjacentindex = -1;
        List regionsList = null;
        int index = 0;
        if (getEnclosingFig() != null) {
            x = oldBounds.x;
            w = oldBounds.width;
            FigCompositeState f = ((FigCompositeState) getEnclosingFig());
            regionsList = f.getEnclosedFigs();
            index = regionsList.indexOf(this);

            /* if curHandle.index is 0 or 2,
             * the adjacent region is the previous region
             * but if it is 5 or 7, the adjacent region is the next region.
             * curHandle.index show which corner of the bound we are dragging.
             */
            if (((curHandle.index == 0) || (curHandle.index == 2))
                    && index > 0) {
                adjacentindex = index - 1;
            }
            if (((curHandle.index == 5) || (curHandle.index == 7))
                    && (index < (regionsList.size() - 1))) {
                adjacentindex = index + 1;
            }
            if (h <= getMinimumSize().height) {
                if (h <= oldBounds.height) {
                    h = oldBounds.height;
                    y = oldBounds.y;
                }
            }

            /* We aren't able to resize neither the top bound
             * from the first region nor
             * the bottom bound from the last region.
             */

            if (adjacentindex == -1) {
                x = oldBounds.x;
                y = oldBounds.y;
                h = oldBounds.height;

                /*The group must be resized if a text field exceed the bounds*/
                if (w > f.getBounds().width) {
                    Rectangle fR = f.getBounds();
                    f.setBounds(fR.x, fR.y, w + 6, fR.height);
                }
            } else {
                int hIncrement = oldBounds.height - h;
                FigConcurrentRegion adjacentFig =
                    ((FigConcurrentRegion)
                        regionsList.get(adjacentindex));
                if ((adjacentFig.getBounds().height + hIncrement)
                        <= adjacentFig.getMinimumSize().height) {
                    y = oldBounds.y;
                    h = oldBounds.height;
                } else {
                    if ((curHandle.index == 0) || (curHandle.index == 2)) {
                        ((FigConcurrentRegion) regionsList.
                            get(adjacentindex)).setBounds(0, hIncrement);
                    }
                    if ((curHandle.index == 5) || (curHandle.index == 7)) {
                        ((FigConcurrentRegion) regionsList.
                            get(adjacentindex)).setBounds(-hIncrement,
                                    hIncrement);
                    }
                }
            }
        }

        dividerline.setShape(x, y, x + w, y);
        getNameFig().setBounds(x + MARGIN,
                y + SPACE_TOP,
                w - 2 * MARGIN,
                nameDim.height);
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
    }

    /**
     * To resize with X and Y increments, absolute width and keeping the height.
     * @param xInc the x increment
     * @param yInc the y increment
     * @param w the width
     * @param concurrency is concurrent?
     */
    public void setBounds(int xInc, int yInc, int w, boolean concurrency) {
        if (getNameFig() == null) {
            return;
        }
        Rectangle oldBounds = getBounds();
        Dimension nameDim = getNameFig().getMinimumSize();
        int x = oldBounds.x + xInc;
        int y = oldBounds.y + yInc;
        int h = oldBounds.height;

        dividerline.setShape(x, y, x + w , y);
        getNameFig().setBounds(x + 2, y + 2, w - 4, nameDim.height);
        getInternal().setBounds(x + 2, y + nameDim.height + 4,
                w - 4, h - nameDim.height - 8);
        getBigPort().setBounds(x, y, w, h);
        cover.setBounds(x, y, w, h);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    /**
     * To resize with X, Y and height increments and absolute width.
     * The boolean parameter is added in order to override the method.
     *
     * @param xInc the x increment
     * @param yInc the y increment
     * @param w the width
     * @param concurrency is concurrent?
     * @param hInc the height increment
     */
    public void setBounds(int xInc, int yInc, int w, int hInc,
            boolean concurrency) {
        if (getNameFig() == null) {
            return;
        }
        Rectangle oldBounds = getBounds();
        Dimension nameDim = getNameFig().getMinimumSize();
        int x = oldBounds.x + xInc;
        int y = oldBounds.y + yInc;
        int h = oldBounds.height + hInc;

        dividerline.setShape(x, y,
                x + w , y);
        getNameFig().setBounds(x + 2, y + 2, w - 4, nameDim.height);
        getInternal().setBounds(x + 2, y + nameDim.height + 4,
                w - 4, h - nameDim.height - 8);
        getBigPort().setBounds(x, y, w, h);
        cover.setBounds(x, y, w, h);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    /**
     * To resize with Y increments, height increment and
     * keeping the X and width.
     *
     * @param yInc the y increment
     * @param hInc the height increment
     */
    public void setBounds(int yInc, int hInc) {
        if (getNameFig() == null) {
            return;
        }
        Rectangle oldBounds = getBounds();
        Dimension nameDim = getNameFig().getMinimumSize();
        int x = oldBounds.x;
        int y = oldBounds.y + yInc;
        int w = oldBounds.width;
        int h = oldBounds.height + hInc;

        dividerline.setShape(x, y, x + w , y);
        getNameFig().setBounds(x + 2, y + 2, w - 4, nameDim.height);
        getInternal().setBounds(x + 2, y + nameDim.height + 4,
                w - 4, h - nameDim.height - 8);
        getBigPort().setBounds(x, y, w, h);
        cover.setBounds(x, y, w, h);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    ////////////////////////////////////////////////////////////////
    // fig accessors

    /*
     * This function only sets the color of the divider line 
     * (since that is the only visible part), and can be used to make 
     * the divider line invisible for the top region in a composite state.
     * 
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    @Override
    public void setLineColor(Color col) {
        cover.setLineColor(INVISIBLE_LINE_COLOR);
        dividerline.setLineColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    @Override
    public Color getLineColor() {
        return dividerline.getLineColor();
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


    @Override
    public boolean isFilled() {
        return cover.isFilled();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    @Override
    public void setLineWidth(int w) {
        dividerline.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    @Override
    public int getLineWidth() {
        return dividerline.getLineWidth();
    }

    ////////////////////////////////////////////////////////////////
    // event processing

    protected void modelChanged(PropertyChangeEvent mee) {
        // TODO: Rather than specifically ignore some item 
        // maybe it would be better to specifically state 
        // what items are of interest. Otherwise we may still
        // be acting on other events we don't need
        if (!Model.getFacade().isATransition(mee.getNewValue())
                && !("container".equals(mee.getPropertyName()))
                && !("isConcurrent".equals(mee.getPropertyName()))
                && !("subvertex".equals(mee.getPropertyName()))) {
            super.modelChanged(mee);
        }
    }
    
    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    @Override
    public Selection makeSelection() {
        Selection sel = new SelectionState(this);
        ((SelectionState) sel).setIncomingButtonEnabled(false);
        ((SelectionState) sel).setOutgoingButtonEnabled(false);
        return sel;
    }

    /*
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialHeight()
     */
    public int getInitialHeight() {
        return 130;
    }

    /*
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialWidth()
     */
    protected int getInitialWidth() {
        return 30;
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

    /**
     * @return the initial color
     */
    protected Color getInitialColor() {
        return LINE_COLOR;
    }

    /////////////////////////////////////////////////////////////////////////
    // event handlers - MouseListener and MouseMotionListener implementation

    @Override
    protected void updateLayout(UmlChangeEvent event) {
        if (!"container".equals(event.getPropertyName()) 
                && !"isConcurrent".equals(event.getPropertyName())) {
            super.updateLayout(event);
        }
        final String eName = event.getPropertyName();
        /*
         * A Concurrent region cannot have incoming or outgoing transitions so
         * incoming or outgoing transitions are redirected to its concurrent
         * composite state container.
         */
        // TODO: This comparison is very suspect, it should use equals
        // method. The code within the block is in fact never executed.
        // I hesitate to change this now as it will trigger code has never been
        // used before and am not aware of any problems that it usage may
        // introduce.
        // I do think that we need to be able to find a different way to
        // implement the intent here which seems to be to correct edge drawings
        // that should actually not be allowed - Bob
        if (eName == "incoming" || eName == "outgoing") {
            final Object owner = getOwner();
            final Collection transactions = (Collection) event.getNewValue();
            if (!transactions.isEmpty()) {
                final Object transition = transactions.iterator().next();
                if (eName == "incoming") {
                    if (Model.getFacade().isATransition(transition)) {
                        Model.getCommonBehaviorHelper().setTarget(transition,
                                Model.getFacade().getContainer(owner));
                    }
                } else {
                    if (Model.getFacade().isATransition(transition)) {
                        Model.getStateMachinesHelper().setSource(transition,
                                Model.getFacade().getContainer(owner));
                    }
                }
            }
        }
    }

    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Globals.curEditor().getSelectionManager().hitHandle(
                new Rectangle(x - 4, y - 4, 8, 8), curHandle);
    }

    /*
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        curHandle.index = -1;
    }

    /*
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e) {
        if (curHandle.index == -1) {
            Globals.curEditor().getSelectionManager().select(getEnclosingFig());
        }
    }

    /*
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent e) {
        // ignored
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -7228935179004210975L;
}
