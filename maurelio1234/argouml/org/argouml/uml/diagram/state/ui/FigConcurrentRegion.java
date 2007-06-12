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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.ui.ActionAddConcurrentRegion;
import org.argouml.uml.diagram.ui.ActionDeleteConcurrentRegion;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
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

    ////////////////////////////////////////////////////////////////
    // instance variables

    // /** The main label on this icon. */
    //FigText _name;

    private FigRect cover;
    private FigLine dividerline;
    private static Handle curHandle = new Handle(-1);

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    public FigConcurrentRegion() {
        super();
        cover =
            new FigRect(getInitialX(),
                getInitialY(),
                getInitialWidth(), getInitialHeight(),
                Color.white, Color.white);
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
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }

    /**
     * The constructor.
     *
     * @param gm (not used)
     * @param node the UML model element represented by this Fig
     */
    public FigConcurrentRegion(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /**
     * The constructor.
     *
     * @param gm (not used)
     * @param node the UML model element represented by this Fig
     * @param col the line color
     * @param width the width
     * @param height the height
     */
    public FigConcurrentRegion(GraphModel gm, Object node,
                               Color col, int width, int height) {
        this(gm, node);
        setLineColor(col);
        Rectangle r = getBounds();
        setBounds(r.x, r.y, width, height);
    }

    /*
     * @see java.lang.Object#clone()
     */
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

    ////////////////////////////////////////////////////////////////
    // accessors

    /*
     * @see org.tigris.gef.ui.PopupGenerator#getPopUpActions(java.awt.event.MouseEvent)
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        popUpActions.remove(
                ProjectBrowser.getInstance().getRemoveFromDiagramAction());
        popUpActions.add(new JSeparator());
        popUpActions.addElement(
                new ActionAddConcurrentRegion());
        popUpActions.addElement(
                new ActionDeleteConcurrentRegion());
        return popUpActions;
    }


    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
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
    protected void setStandardBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
        }
        Rectangle oldBounds = getBounds();
        Dimension nameDim = getNameFig().getMinimumSize();
        int adjacentindex = -1;
        Vector regionsVector = null;
        int index = 0;
        if (getEnclosingFig() != null) {
            x = oldBounds.x;
            w = oldBounds.width;
            FigCompositeState f = ((FigCompositeState) getEnclosingFig());
            regionsVector = f.getEnclosedFigs();
            index = regionsVector.indexOf(this, 0);

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
                    && (index < (regionsVector.size() - 1)))
                adjacentindex = index + 1;
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
                        regionsVector.elementAt(adjacentindex));
                if ((adjacentFig.getBounds().height + hIncrement)
                        <= adjacentFig.getMinimumSize().height) {
                    y = oldBounds.y;
                    h = oldBounds.height;
                } else {
                    if ((curHandle.index == 0) || (curHandle.index == 2)) {
                        ((FigConcurrentRegion) regionsVector.
                            elementAt(adjacentindex)).setBounds(0, hIncrement);
                    }
                    if ((curHandle.index == 5) || (curHandle.index == 7)) {
                        ((FigConcurrentRegion) regionsVector.
                            elementAt(adjacentindex)).setBounds(-hIncrement,
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
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
        cover.setLineColor(Color.white);
        dividerline.setLineColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return dividerline.getLineColor();
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

    /*
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return cover.getFilled();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        dividerline.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return dividerline.getLineWidth();
    }

    ////////////////////////////////////////////////////////////////
    // event processing

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
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
        return Color.black;
    }

    /////////////////////////////////////////////////////////////////////////
    // event handlers - MouseListener and MouseMotionListener implementation

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        final Object trCollection = mee.getNewValue();
        final String eName = mee.getPropertyName();
        final Object owner = getOwner();
        /*
         * A Concurrent region cannot have incoming or outgoing transitions so
         * incoming or outgoing transitions are redirected to its concurrent
         * composite state container.
         */
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Object tr = null;
                // TODO: Is this comparison correct?
                // Where is the string created?
                if (eName == "incoming") {
                    if (!((Collection) trCollection).isEmpty()) {
                        tr = ((Collection) trCollection).iterator().next();
                    }
                    if (tr != null
                            && Model.getFacade().isATransition(tr)) {
                        Model.getCommonBehaviorHelper().setTarget(tr,
                                Model.getFacade().getContainer(owner));
                    }
                } else if (eName == "outgoing") {
                    if (!((Collection) trCollection).isEmpty()) {
                        tr = ((Collection) trCollection).iterator().next();
                    }
                    if (tr != null
                            && Model.getFacade().isATransition(tr))
                    {
                        Model.getStateMachinesHelper().setSource(tr,
                                Model.getFacade().getContainer(owner));
                    }
                }
            }
        });
    }

    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Globals.curEditor().getSelectionManager().hitHandle(
                new Rectangle(x - 4, y - 4, 8, 8), curHandle);
    }

    /*
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
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
} /* end class FigConcurrentRegion */
