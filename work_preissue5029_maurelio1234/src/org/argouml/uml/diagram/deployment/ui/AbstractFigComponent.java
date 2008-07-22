// $Id$
// Copyright (c) 2007-2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.deployment.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Common abstract superclass for FigComponent and FigComponentInstance
 * to encapsulate common behavior.
 * 
 * @author 5eichler
 * @author Tom Morris <tfmorris@gmail.com>
 */
public abstract class AbstractFigComponent extends FigNodeModelElement {

    /**
     * Size of the prong or finger that extends from the left side of the
     * figure. It is also the distance between the left edge of the fig and the
     * left edge of the main rectangle. Originally named BIGPORT_X (which
     * explains what BX stands for).
     */
    private static final int BX = 10;
    private static final int FINGER_HEIGHT = BX;
    private static final int FINGER_WIDTH = BX * 2;
    private static final int OVERLAP = 0;
    private static final int DEFAULT_WIDTH = 120;
    private static final int DEFAULT_HEIGHT = 80;
    private FigRect cover;
    private FigRect upperRect;
    private FigRect lowerRect;

    /**
     * The constructor.
     */
    public AbstractFigComponent() {
        super();
        cover = new FigRect(BX, 10, DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.black,
                Color.white);
        upperRect = new FigRect(0, 2 * FINGER_HEIGHT, 
                FINGER_WIDTH, FINGER_HEIGHT,
                Color.black, Color.white);
        lowerRect = new FigRect(0, 5 * FINGER_HEIGHT, 
                FINGER_WIDTH, FINGER_HEIGHT,
                Color.black, Color.white);

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
    public AbstractFigComponent(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    @Override
    public Object clone() {
        AbstractFigComponent figClone = (AbstractFigComponent) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.cover = (FigRect) it.next();
        it.next();
        figClone.setNameFig((FigText) it.next());
        figClone.upperRect = (FigRect) it.next();
        figClone.lowerRect = (FigRect) it.next();
    
        return figClone;
    }

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

    @Override
    protected void updateListeners(Object oldOwner, Object newOwner) {
        super.updateListeners(oldOwner, newOwner);
        if (newOwner != null) {
            Collection c = Model.getFacade().getStereotypes(newOwner);
            Iterator i = c.iterator();
            while (i.hasNext()) {
                Object st = i.next();
                addElementListener(st, "name");
            }
        }
    }

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

    @Override
    public Dimension getMinimumSize() {
        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        Dimension nameDim = getNameFig().getMinimumSize();

        int h = Math.max(stereoDim.height + nameDim.height - OVERLAP,
                4 * FINGER_HEIGHT);
        int w = Math.max(stereoDim.width, nameDim.width) + FINGER_WIDTH;

        return new Dimension(w, h);
    }

    @Override
    protected void setStandardBounds(int x, int y, int w,
            int h) {
        if (getNameFig() == null) {
            return;
        }

        Rectangle oldBounds = getBounds();
        getBigPort().setBounds(x + BX, y, w - BX, h);
        cover.setBounds(x + BX, y, w - BX, h);

        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        Dimension nameDim = getNameFig().getMinimumSize();

        int halfHeight = FINGER_HEIGHT / 2;
        upperRect.setBounds(x, y + h / 3 - halfHeight, FINGER_WIDTH,
                FINGER_HEIGHT);
        lowerRect.setBounds(x, y + 2 * h / 3 - halfHeight, FINGER_WIDTH,
                FINGER_HEIGHT);

        getStereotypeFig().setBounds(x + FINGER_WIDTH + 1,
                y + 1,
                w - FINGER_WIDTH - 2,
                stereoDim.height);
        getNameFig().setBounds(x + FINGER_WIDTH + 1,
                y + stereoDim.height - OVERLAP + 1,
                w - FINGER_WIDTH - 2,
                nameDim.height);
        _x = x;
        _y = y;
        _w = w;
        _h = h;
        firePropChange("bounds", oldBounds, getBounds());
        updateEdges();
    }

    /**
     * TODO: This is not used anywhere. Can we remove it?
     * 
     * @param figures ?
     * @deprecated for 0.25.4 by tfmorris. Unused in ArgoUML. Not clear it was
     *             ever intended to be part of the public API.
     */
    @Deprecated
    public void setNode(List figures) {
        int size = figures.size();
        if (figures != null && (size > 0)) {
            for (int i = 0; i < size; i++) {
                Object o = figures.get(i);
                if (o instanceof FigComponent) {
                    FigComponent figcomp = (FigComponent) o;
                    figcomp.setEnclosingFig(this);
                }
            }
        }
    }

    @Override
    public boolean getUseTrapRect() {
        return true;
    }

    @Override
    public Rectangle getHandleBox() {
        Rectangle r = getBounds();
        return new Rectangle(r.x + BX, r.y, r.width - BX, r.height);
    }

    @Override
    public void setHandleBox(int x, int y, int w, int h) {
        setBounds(x - BX, y, w + BX, h);
    }

}