// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.diagram.deployment.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML Component in a diagram.
 *
 * @author 5eichler
 */
public class FigComponent extends FigNodeModelElement {
    /**
     * The distance between the left edge of the fig and the left edge of the
     * main rectangle.
     * Originally named BIGPORT_X (which explains what BX stands for).
     */
    private static final int BX = 10;

    private static final int OVERLAP = 0;

    private FigRect cover;
    private FigRect upperRect;
    private FigRect lowerRect;

    /**
     * Constructor.
     */
    public FigComponent() {
        cover = new FigRect(BX, 10, 120, 80, Color.black, Color.white);
        upperRect =
            new FigRect(0, 2 * BX, 2 * BX, BX, Color.black, Color.white);
        lowerRect =
            new FigRect(0, 4 * BX, 2 * BX, BX, Color.black, Color.white);

        getNameFig().setLineWidth(0);
        getNameFig().setFilled(false);
        getNameFig().setText(placeString());

        addFig(getBigPort());
        addFig(cover);
        addFig(getStereotypeFig());
        addFig(getNameFig());
        addFig(upperRect);
        addFig(lowerRect);
    }

    /**
     * The constructor that hooks the Fig into an existing UML element.
     *
     * @param gm ignored
     * @param node the UML element
     */
    public FigComponent(GraphModel gm, Object node) {
        this();
        setOwner(node);
        if (Model.getFacade().isAClassifier(node)
                && (Model.getFacade().getName(node) != null)) {
            getNameFig().setText(Model.getFacade().getName(node));
        }
        updateBounds();
    }


    /*
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        FigComponent figClone = (FigComponent) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.cover = (FigRect) it.next();
        it.next();
        figClone.setNameFig((FigText) it.next());
        figClone.upperRect = (FigRect) it.next();
        figClone.lowerRect = (FigRect) it.next();

        return figClone;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    @Override
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee instanceof AssociationChangeEvent 
                || mee instanceof AttributeChangeEvent) {
            renderingChanged();
            updateListeners(getOwner(), getOwner());
            damage();
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object)
     */
    @Override
    protected void updateListeners(Object oldOwner, Object newOwner) {
        if (oldOwner != null) {
            removeAllElementListeners();
        }
        if (newOwner != null) {
            // add the listeners to the newOwner
            addElementListener(newOwner);
            Collection c = Model.getFacade().getStereotypes(newOwner);
            Iterator i = c.iterator();
            while (i.hasNext()) {
                Object st = i.next();
                addElementListener(st, "name");
            }
        }
    }

    /**
     * Build a collection of menu items relevant for a right-click popup menu.
     *
     * @param     me     a mouse event
     * @return           a collection of menu items
     *
     * @see org.tigris.gef.ui.PopupGenerator#getPopUpActions(java.awt.event.MouseEvent)
     */
    @Override
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        // Modifiers ...
        popUpActions.insertElementAt(
                buildModifierPopUp(ABSTRACT | LEAF | ROOT),
                popUpActions.size() - getPopupAddOffset());
        return popUpActions;
    }

    /**
     * @param b switch underline on or off
     */
    public void setUnderline(boolean b) {
        getNameFig().setUnderline(b);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    @Override
    public void setLineColor(Color c) {
        cover.setLineColor(c);
        getStereotypeFig().setFilled(false);
        getStereotypeFig().setLineWidth(0);
        getNameFig().setFilled(false);
        getNameFig().setLineWidth(0);
        upperRect.setLineColor(c);
        lowerRect.setLineColor(c);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    @Override
    public Selection makeSelection() {
        return new SelectionComponent(this);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    @Override
    public Dimension getMinimumSize() {
        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        Dimension nameDim = getNameFig().getMinimumSize();

        int h = Math.max(stereoDim.height + nameDim.height - OVERLAP, 4 * BX);
        int w = Math.max(stereoDim.width, nameDim.width) + 2 * BX;

        return new Dimension(w, h);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {

        Rectangle oldBounds = getBounds();
        getBigPort().setBounds(x + BX, y, w - BX, h);
        cover.setBounds(x + BX, y, w - BX, h);

        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        Dimension nameDim = getNameFig().getMinimumSize();
        if (h < (6 * BX)) {
            upperRect.setBounds(x, y + 2 * h / 6, 20, 10);
            lowerRect.setBounds(x, y + 4 * h / 6, 20, 10);
        } else {
            upperRect.setBounds(x, y + 2 * BX, 2 * BX, BX);
            lowerRect.setBounds(x, y + 4 * BX, 2 * BX, BX);
        }

        getStereotypeFig().setBounds(x + 2 * BX + 1,
                y + 1,
                w - 2 * BX - 2,
                stereoDim.height);
        getNameFig().setBounds(x + 2 * BX + 1,
                y + stereoDim.height - OVERLAP + 1,
                w - 2 * BX - 2,
                nameDim.height);
        _x = x;
        _y = y;
        _w = w;
        _h = h;
        firePropChange("bounds", oldBounds, getBounds());
        updateEdges();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setEnclosingFig(org.tigris.gef.presentation.Fig)
     */
    @Override
    public void setEnclosingFig(Fig encloser) {

        Object comp = getOwner();
        if (encloser != null
                && (Model.getFacade().isANode(encloser.getOwner())
                || Model.getFacade().isAComponent(encloser.getOwner()))
                && getOwner() != null) {
            if (Model.getFacade().isANode(encloser.getOwner())) {
                Object node = encloser.getOwner();
                if (!Model.getFacade()
                        .getDeploymentLocations(comp).contains(node)) {
                    Model.getCoreHelper().addDeploymentLocation(comp, node);
                }
            }
            super.setEnclosingFig(encloser);

            if (getLayer() != null) {
                // elementOrdering(figures);
                List contents = new ArrayList(getLayer().getContents());
                Iterator it = contents.iterator();
                while (it.hasNext()) {
                    Object o = it.next();
                    if (o instanceof FigEdgeModelElement) {
                        FigEdgeModelElement figedge = (FigEdgeModelElement) o;
                        figedge.getLayer().bringToFront(figedge);
                    }
                }
            }
        } else if (encloser == null && getEnclosingFig() != null) {
            Object encloserOwner = getEnclosingFig().getOwner();
            if (Model.getFacade().isANode(encloserOwner)
                    && (Model.getFacade().getDeploymentLocations(comp).
                            contains(encloserOwner))) {
                Model.getCoreHelper()
                        .removeDeploymentLocation(comp, encloserOwner);
            }
            super.setEnclosingFig(encloser);
        }
    }

    /**
     * TODO: This is not used anywhere. Can we remove it?
     * @param figures ?
     * @deprecated for 0.25.4 by tfmorris.  Unused in ArgoUML.  Not clear it was ever 
     * intended to be part of the public API.
     */
    @Deprecated
    public void setNode(Vector figures) {
        int size = figures.size();
        if (figures != null && (size > 0)) {
            for (int i = 0; i < size; i++) {
                Object o = figures.elementAt(i);
                if (o instanceof FigComponent) {
                    FigComponent figcomp = (FigComponent) o;
                    figcomp.setEnclosingFig(this);
                }
            }
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getUseTrapRect()
     */
    @Override
    public boolean getUseTrapRect() {
        return true;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     */
    @Override
    protected void updateStereotypeText() {
        getStereotypeFig().setOwner(getOwner());
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    @Override
    protected void textEditStarted(FigText ft) {
        if (ft == getNameFig()) {
            showHelp("parsing.help.fig-component");
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getHandleBox()
     *
     * Get the rectangle on whose corners the dragging handles are to
     * be drawn.  Used by Selection Resize.
     */
    @Override
    public Rectangle getHandleBox() {
        Rectangle r = getBounds();
        return new Rectangle(r.x + BX, r.y, r.width - BX,
                r.height);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setHandleBox(int, int, int, int)
     */
    @Override
    public void setHandleBox(int x, int y, int w, int h) {
        setBounds(x - BX, y, w + BX, h);
    }

    /**
     * The UID.
     */
    static final long serialVersionUID = 1647392857462847651L;

}
