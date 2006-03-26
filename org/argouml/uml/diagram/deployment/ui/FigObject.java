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

package org.argouml.uml.diagram.deployment.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider4;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML Object in a diagram.
 *
 * @author 5eichler
 */
public class FigObject extends FigNodeModelElement {

    ////////////////////////////////////////////////////////////////
    // instance variables

    private FigRect cover;
    private Object resident =
            Model.getCoreFactory().createElementResidence();

    private NotationProvider4 notationProvider;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Main constructor.
     */
    public FigObject() {
        setBigPort(new FigRect(10, 10, 90, 50, Color.cyan, Color.cyan));
        cover = new FigRect(10, 10, 90, 50, Color.black, Color.white);
        getNameFig().setLineWidth(0);
        getNameFig().setFilled(false);
        getNameFig().setUnderline(true);
        Dimension nameMin = getNameFig().getMinimumSize();
        getNameFig().setBounds(10, 10, nameMin.width + 20, nameMin.height);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(cover);
        addFig(getNameFig());

        Rectangle r = getBounds();
        setBounds(r.x, r.y, nameMin.width, nameMin.height);
    }

    /**
     * Constructor that hooks the Fig to an existing UML element.
     *
     * @param gm ignored
     * @param node the UML element
     */
    public FigObject(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#initNotationProviders(java.lang.Object)
     */
    protected void initNotationProviders(Object own) {
        super.initNotationProviders(own);
        if (Model.getFacade().isAObject(own)) {
            notationProvider =
                NotationProviderFactory2.getInstance().getNotationProvider(
                    NotationProviderFactory2.TYPE_OBJECT, this, own);
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() { return "new Object"; }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigObject figClone = (FigObject) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.cover = (FigRect) it.next();
        figClone.setNameFig((FigText) it.next());
        return figClone;
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee instanceof AssociationChangeEvent 
                || mee instanceof AttributeChangeEvent) {
            renderingChanged();
            updateListeners(getOwner());
            damage();
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object)
     */
    protected void updateListeners(Object newOwner) {
        Object oldOwner = getOwner();
        if (oldOwner != null) {
            removeAllElementListeners();
        }
        if (newOwner != null) {
            // add the listeners to the newOwner
            addElementListener(newOwner);
            // Add the following once we show stereotypes:
//            Collection c = Model.getFacade().getStereotypes(newOwner);
//            Iterator i = c.iterator();
//            while (i.hasNext()) {
//                Object st = i.next();
//                addElementListener(st, "name");
//            }
            Collection c = Model.getFacade().getClassifiers(newOwner);
            Iterator i = c.iterator();
            while (i.hasNext()) {
                Object st = i.next();
                addElementListener(st, "name");
            }
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) { cover.setLineColor(col); }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() { return cover.getLineColor(); }

    /**
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) { cover.setFillColor(col); }

    /**
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() { return cover.getFillColor(); }

    /**
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean f) { cover.setFilled(f); }

    /**
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() { return cover.getFilled(); }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) { cover.setLineWidth(w); }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() { return cover.getLineWidth(); }

    /**
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionObject(this);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        Dimension nameMin = getNameFig().getMinimumSize();

        int w = nameMin.width + 10;
        int h = nameMin.height + 5;
        
        w = Math.max(60, w);
        return new Dimension(w, h);
    }

    /**
     * Override setBounds to keep shapes looking right.
     *
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    protected void setBoundsImpl(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
        }

        Rectangle oldBounds = getBounds();

        Dimension nameMin = getNameFig().getMinimumSize();

        getBigPort().setBounds(x, y, w, h);
        cover.setBounds(x, y, w, h);
        getNameFig().setBounds(x, y, nameMin.width + 10, nameMin.height + 4);

        //_bigPort.setBounds(x+1, y+1, w-2, h-2);
        _x = x; _y = y; _w = w; _h = h;

        firePropChange("bounds", oldBounds, getBounds());
        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
    }


    ////////////////////////////////////////////////////////////////
    // user interaction methods

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) throws PropertyVetoException {
        if (ft == getNameFig()) {
            ft.setText(notationProvider.parse(ft.getText()));
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    protected void textEditStarted(FigText ft) {
        if (ft == getNameFig()) {
            showHelp(notationProvider.getParsingHelp());
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setEnclosingFig(org.tigris.gef.presentation.Fig)
     */
    public void setEnclosingFig(Fig encloser) {
        // super.setEnclosingFig(encloser);

        if (Model.getFacade().isAObject(getOwner())) {
            Object me = /*(MObject)*/ getOwner();
            Object mcompInst = null;
            Object mcomp = null;

            if (encloser != null
                    && (Model.getFacade()
                            .isAComponentInstance(encloser.getOwner()))) {

                mcompInst = /*(MComponentInstance)*/ encloser.getOwner();
                Model.getCommonBehaviorHelper()
                        .setComponentInstance(me, mcompInst);
                super.setEnclosingFig(encloser);

            } else if (Model.getFacade().getComponentInstance(me) != null) {
                Model.getCommonBehaviorHelper().setComponentInstance(me, null);
                super.setEnclosingFig(null);
            }
            if (encloser != null
                    && (Model.getFacade()
                            .isAComponent(encloser.getOwner()))) {

                mcomp = /*(MComponent)*/ encloser.getOwner();
                Object obj = /*(MObject)*/ getOwner();
                Model.getCoreHelper().setContainer(resident, mcomp);
                Model.getCoreHelper().setResident(resident, obj);
                super.setEnclosingFig(encloser);
            } else if (encloser != null
                    && Model.getFacade().isANode(encloser.getOwner())) {
                super.setEnclosingFig(encloser);
            } else {
                if (Model.getFacade().getContainer(resident) != null) {
                    Model.getCoreHelper().setContainer(resident, null);
                    Model.getCoreHelper().setResident(resident, null);
                    super.setEnclosingFig(null);
                }
            }
        }

    }

    /**
     * The UID.
     */
    static final long serialVersionUID = -185736690375678962L;

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        if (isReadyToEdit()) {
            getNameFig().setText(notationProvider.toString());
        }
        Dimension nameMin = getNameFig().getMinimumSize();
        Rectangle r = getBounds();
        setBounds(r.x, r.y, nameMin.width + 10, nameMin.height + 4);
    }

} /* end class FigObject */
