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
import java.beans.PropertyChangeEvent;
import java.util.Iterator;

import org.argouml.model.Model;
import org.argouml.notation.Notation;
import org.argouml.uml.diagram.ui.FigSingleLineText;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML SubmachineState in a diagram.
 *
 * @author pepargouml@yahoo.es
 */

public class FigSubmachineState extends FigState {

    ////////////////////////////////////////////////////////////////
    // instance variables

    private FigRect cover;
    private FigLine divider;
    private FigLine divider2;
    private FigRect circle1;
    private FigRect circle2;
    private FigLine circle1tocircle2;
    private FigText include;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    public FigSubmachineState() {
        super();
        cover =
            new FigRRect(getInitialX(), getInitialY(),
                getInitialWidth(), getInitialHeight(),
                Color.black, Color.white);

        getBigPort().setLineWidth(0);

        divider =
                new FigLine(getInitialX(),
                        getInitialY() + 2 + getNameFig().getBounds().height + 1,
                        getInitialWidth() - 1,
                        getInitialY() + 2 + getNameFig().getBounds().height + 1,
                        Color.black);

        include = new FigSingleLineText(10, 10, 90, 21, true);
        include.setText(placeString());
        include.setBounds(getInitialX() + 2, getInitialY() + 2,
                getInitialWidth() - 4, include.getBounds().height);
        include.setEditable(false);
        include.setBotMargin(4); // leave some space below the "include"

        divider2 =
                new FigLine(getInitialX(),
                        getInitialY() + 2 + getNameFig().getBounds().height + 1,
                        getInitialWidth() - 1,
                        getInitialY() + 2 + getNameFig().getBounds().height + 1,
                        Color.black);

        circle1 =
            new FigRRect(getInitialX() + getInitialWidth() - 55,
                getInitialY() + getInitialHeight() - 15,
                20, 10,
                Color.black, Color.white);
        circle2 =
            new FigRRect(getInitialX() + getInitialWidth() - 25,
                getInitialY() + getInitialHeight() - 15,
                20, 10,
                Color.black, Color.white);

        circle1tocircle2 =
                new FigLine(getInitialX() + getInitialWidth() - 35,
                        getInitialY() + getInitialHeight() - 10,
                        getInitialX() + getInitialWidth() - 25,
                        getInitialY() + getInitialHeight() - 10,
                        Color.black);

        addFig(getBigPort());
        addFig(cover);
        addFig(getNameFig());
        addFig(divider);
        addFig(include);
        addFig(divider2);
        addFig(getInternal());
        addFig(circle1);
        addFig(circle2);
        addFig(circle1tocircle2);

        //setBlinkPorts(false); //make port invisble unless mouse enters
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }

    /**
     * The constructor.
     *
     * @param gm (ignored)
     * @param node the owner UML object
     */
    public FigSubmachineState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object node) {
        super.setOwner(node);
        updateInclude();
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigSubmachineState figClone = (FigSubmachineState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.cover = (FigRect) it.next();
        figClone.setNameFig((FigText) it.next());
        figClone.divider = (FigLine) it.next();
        figClone.include = (FigText) it.next();
        figClone.divider2 = (FigLine) it.next();
        figClone.setInternal((FigText) it.next());
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        Dimension nameDim = getNameFig().getMinimumSize();
        Dimension internalDim = getInternal().getMinimumSize();
        Dimension includeDim = include.getMinimumSize();

        int h =
            SPACE_TOP + nameDim.height
            + SPACE_MIDDLE + includeDim.height
            + SPACE_MIDDLE + internalDim.height
            + SPACE_BOTTOM;
        int waux =
            Math.max(nameDim.width,
                internalDim.width) + 2 * MARGIN;
        int w = Math.max(waux, includeDim.width + 50);
        return new Dimension(w, h);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getUseTrapRect()
     */
    public boolean getUseTrapRect() {
        return true;
    }

    /**
     * Override setBounds to keep shapes looking right.
     *
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    protected void setBoundsImpl(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
        }

        Rectangle oldBounds = getBounds();
        Dimension nameDim = getNameFig().getMinimumSize();
        Dimension includeDim = include.getMinimumSize();

        getNameFig().setBounds(x + MARGIN,
                y + SPACE_TOP,
                w - 2 * MARGIN,
                nameDim.height);
        divider.setShape(x,
                y + DIVIDER_Y + nameDim.height,
                x + w - 1,
                y + DIVIDER_Y + nameDim.height);

        include.setBounds(x + MARGIN,
                y + SPACE_TOP + nameDim.height + SPACE_TOP,
                w - 2 * MARGIN,
                includeDim.height);
        divider2.setShape(x,
                y + nameDim.height + DIVIDER_Y + includeDim.height + DIVIDER_Y,
                x + w - 1,
                y + nameDim.height + DIVIDER_Y + includeDim.height + DIVIDER_Y);

        getInternal().setBounds(
                x + MARGIN,
                y + SPACE_TOP + nameDim.height
                    + SPACE_TOP + includeDim.height + SPACE_MIDDLE,
                w - 2 * MARGIN,
                h - SPACE_TOP - nameDim.height
                    - SPACE_TOP - includeDim.height
                    - SPACE_MIDDLE - SPACE_BOTTOM);

        circle1.setBounds(x + w - 55,
                y + h - 15,
                20, 10);
        circle2.setBounds(x + w - 25,
                y + h - 15,
                20, 10);
        circle1tocircle2.setShape(x + w - 35,
                y + h - 10,
                x + w - 25,
                y + h - 10);

        getBigPort().setBounds(x, y, w, h);
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
        divider2.setLineColor(col);
        circle1.setLineColor(col);
        circle2.setLineColor(col);
        circle1tocircle2.setLineColor(col);
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
        getBigPort().setFilled(f);
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
        divider2.setLineWidth(w);
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
     * Update the text labels and listeners.
     *
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (getOwner() == null) {
            return;
        }
        if ((mee.getSource().equals(getOwner()))) {
            if ((mee.getPropertyName()).equals("submachine")) {
                updateInclude();
                if (mee.getOldValue() != null) {
                    updateListenersX(getOwner(), mee.getOldValue());
                }
            }
        } else {
            if (mee.getSource()
                    == Model.getFacade().getSubmachine(getOwner())) {
                // The Machine State has got a new name
                if (mee.getPropertyName().equals("name")) {
                    updateInclude();
                }
                // The Machine State has been deleted from model
                if (mee.getPropertyName().equals("top")) {
                    updateListeners(getOwner(), null);
                }
            }
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object)
     */
    protected void updateListeners(Object oldOwner, Object newOwner) {
        super.updateListeners(oldOwner, newOwner);
        if (newOwner != null) {
            Object newSm = Model.getFacade().getSubmachine(newOwner);
            if (newSm != null) {
                addElementListener(newSm);
            }
        }
    }

    private void updateListenersX(Object newOwner, Object oldV) {
        this.updateListeners(getOwner(), newOwner);
        if (oldV != null) {
            removeElementListener(oldV);
        }
    }

    private void updateInclude() {
        include.setText(generateSubmachine(getOwner()));
        calcBounds();
        setBounds(getBounds());
        damage();
    }

    private String generateSubmachine(Object m) {
        Object c = Model.getFacade().getSubmachine(m);
        String s = "include / ";
        if ((c == null)
                || (Model.getFacade().getName(c) == null)
                || (Model.getFacade().getName(c).length() == 0)) {
            return s;
        }
        return (s + Model.getFacade().getName(c));
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

} /* end class FigSubmachineState */
