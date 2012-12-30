/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
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
import java.awt.Font;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.Facade;
import org.argouml.model.Model;
import org.argouml.model.StateMachinesHelper;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.SelectionMoveClarifiers;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML StubState in a diagram.
 *
 * @author pepargouml@yahoo.es
 */

public class FigStubState extends FigStateVertex {

    private static final Logger LOG =
        Logger.getLogger(FigStubState.class.getName());

    private static final int X = 0;
    private static final int Y = 0;
    private static final int WIDTH = 45;
    private static final int HEIGHT = 20;

    private FigText referenceFig;
    private FigLine stubline;

    private Facade facade;
    private StateMachinesHelper stateMHelper;


    /**
     * Construct a new FigStubState.
     *
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigStubState(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initFigs();
    }

    @Override
    protected Fig createBigPortFig() {
        FigRect fr = new FigRect(X, Y, WIDTH, HEIGHT);
        fr.setLineWidth(0);
        fr.setFilled(false);
        return fr;
    }

    private void initFigs() {
        facade = Model.getFacade();
        stateMHelper = Model.getStateMachinesHelper();

        stubline = new FigLine(X,
                Y,
                WIDTH,
                Y,
                TEXT_COLOR);

        referenceFig = new FigText(0, 0, WIDTH, HEIGHT, true);
        referenceFig.setFont(getSettings().getFontPlain());
        referenceFig.setTextColor(TEXT_COLOR);
        referenceFig.setReturnAction(FigText.END_EDITING);
        referenceFig.setTabAction(FigText.END_EDITING);
        referenceFig.setJustification(FigText.JUSTIFY_CENTER);
        referenceFig.setLineWidth(0);
        referenceFig.setBounds(X, Y,
                WIDTH, referenceFig.getBounds().height);
        referenceFig.setFilled(false);
        referenceFig.setEditable(false);


        addFig(getBigPort());
        addFig(referenceFig);
        addFig(stubline);

        setShadowSize(0);
        setBlinkPorts(false); //make port invisible unless mouse enters
    }

    /*
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        FigStubState figClone = (FigStubState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.referenceFig = (FigText) it.next();
        figClone.stubline = (FigLine) it.next();
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    /**
     * Synch states are fixed size.
     * @return false
     */
    @Override
    public boolean isResizable() {
        return false;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    @Override
    public Selection makeSelection() {
        return new SelectionMoveClarifiers(this);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    @Override
    public void setLineColor(Color col) {
        stubline.setLineColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    @Override
    public Color getLineColor() {
        return stubline.getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    @Override
    public void setFillColor(Color col) {
        referenceFig.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    @Override
    public Color getFillColor() {
        return referenceFig.getFillColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    @Override
    public void setFilled(boolean f) {
        referenceFig.setFilled(f);
    }

    @Override
    public boolean isFilled() {
        return referenceFig.isFilled();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    @Override
    public void setLineWidth(int w) {
        stubline.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    @Override
    public int getLineWidth() {
        return stubline.getLineWidth();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    @Override
    protected void setStandardBounds(int theX, int theY, int theW, int theH) {
        Rectangle oldBounds = getBounds();
        theW = 60;

        referenceFig.setBounds(theX, theY, theW,
                referenceFig.getBounds().height);
        stubline.setShape(theX, theY,
                theX + theW, theY);

        getBigPort().setBounds(theX, theY, theW, theH);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    ////////////////////////////////////////////////////////////////
    // event processing

    /*
     * Update the text labels.
     *
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    @Override
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        // TODO: Rather than specifically ignore some item maybe it would be better
        // to specifically state what items are of interest. Otherwise we may still
        // be acting on other events we don't need
        if (!Model.getFacade().isATransition(mee.getNewValue())
                && getOwner() != null) {
            Object container = facade.getContainer(getOwner());

            //The event source is the owner stub state
            if ((mee.getSource().equals(getOwner()))) {
                if (mee.getPropertyName().equals("referenceState")) {
                    updateReferenceText();
                    final Object oldRef;
                    if (container != null && facade.isASubmachineState(container)
                            && facade.getSubmachine(container) != null) {
                        final Object top;
                        top = facade.getTop(facade.getSubmachine(container));
                        oldRef = stateMHelper.getStatebyName(
                                (String) mee.getOldValue(), top);
                    } else {
                        oldRef = null;
                    }
                    updateListeners(oldRef, getOwner());
                } else if ((mee.getPropertyName().equals("container")
                        && facade.isASubmachineState(container))) {
                    removeListeners();
                    Object o = mee.getOldValue();
                    if (o != null && facade.isASubmachineState(o)) {
                        removeElementListener(o);
                    }
                    stateMHelper.setReferenceState(getOwner(), null);
                    updateListeners(getOwner(), getOwner());
                    updateReferenceText();
                }
            } else {
                /*The event source is the submachine state*/
                if (container != null
                        && mee.getSource().equals(container)
                        && facade.isASubmachineState(container)
                        && facade.getSubmachine(container) != null) {
                    /* The submachine has got a new name*/
                    // This indicates a change in association, not name - tfm
                    if (mee.getPropertyName().equals("submachine")) {
                        final Object oldRef;
                        if (mee.getOldValue() != null) {
                            final Object top;
                            top = facade.getTop(mee.getOldValue());
                            oldRef = stateMHelper.getStatebyName(facade
                                    .getReferenceState(getOwner()), top);
                        } else {
                            oldRef = null;
                        }
                        stateMHelper.setReferenceState(getOwner(), null);
                        updateListeners(oldRef, getOwner());
                        updateReferenceText();
                    }

                } else {
                    // The event source is the stub state's referenced state
                    // or one of the referenced state's path.
                    final Object top;
                    if (facade.getSubmachine(container) != null) {
                        top = facade.getTop(facade.getSubmachine(container));
                    } else {
                        top = null;
                    }
                    String path = facade.getReferenceState(getOwner());
                    Object refObject = stateMHelper.getStatebyName(path, top);
                    String ref;
                    if (refObject == null) {
                        // The source was the referenced state that has got
                        // a new name.
                        ref = stateMHelper.getPath(mee.getSource());
                    } else {
                        //The source was one of the referenced state's path which
                        // has got a new name.
                        ref = stateMHelper.getPath(refObject);
                    }
                    // The Referenced State or one of his path's states has got
                    // a new name
                    stateMHelper.setReferenceState(getOwner(), ref);
                    updateReferenceText();
                }
            }
        }
    }

    /**
     * Rerender the whole figure.
     * Call superclass then add reference text
     */
    @Override
    public void renderingChanged() {
        super.renderingChanged();
        updateReferenceText();
    }

    /**
     * Update the reference text.
     */
    public void updateReferenceText() {
        Object text = null;
        try {
            text = facade.getReferenceState(getOwner());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Exception caught and ignored!!", e);
        }
        if (text != null) {
            referenceFig.setText((String) text);
        } else {
            referenceFig.setText("");
        }
        calcBounds();
        setBounds(getBounds());
        damage();
    }

    /**
     * @param newOwner
     */
    private void addListeners(Object newOwner) {
        Object container;
        Object top;
        Object reference;
        container = facade.getContainer(newOwner);
        //The new submachine container is added as listener
        if (container != null
                && facade.isASubmachineState(container)) {
            addElementListener(container);
        }

        //All states in the new reference state's path are added
        // as listeners
        if (container != null
                && facade.isASubmachineState(container)
                && facade.getSubmachine(container) != null) {
            top = facade.getTop(facade.getSubmachine(container));
            reference = stateMHelper.getStatebyName(facade
                    .getReferenceState(newOwner), top);
            String[] properties = {"name", "container"};
            container = reference;
            while (container != null
                    && !container.equals(top)) {
                addElementListener(container);
                container = facade.getContainer(container);
            }
        }
    }

    /**
     * Remove all the existing listeners
     */
    private void removeListeners() {
        Object container;
        Object top;
        Object reference;
        Object owner = getOwner();
        if (owner == null) {
            return;
        }
        container = facade.getContainer(owner);
        //The old submachine container is deleted as listener
        if (container != null
                && facade.isASubmachineState(container)) {
            removeElementListener(container);
        }
        //All states in the old reference state's path are deleted
        // as listeners
        if (container != null
                && facade.isASubmachineState(container)
                && facade.getSubmachine(container) != null) {

            top = facade.getTop(facade.getSubmachine(container));
            reference = stateMHelper.getStatebyName(facade
                    .getReferenceState(owner), top);
            if (reference != null) {
                removeElementListener(reference);
                container = facade.getContainer(reference);
                while (container != null && !facade.isTop(container)) {
                    removeElementListener(container);
                    container = facade.getContainer(container);
                }
            }
        }
    }

    /**
     * @param newOwner the new owner UML object
     * @param oldV the old owner UML object
     * @deprecated for 0.27.2 by tfmorris. Use
     *             {@link #updateListeners(Object, Object)} with the argument
     *             order swapper. There are no internal users of this method, so
     *             the only potential users are people who've subclassed this
     *             Fig.
     */
    protected void updateListenersX(Object newOwner, Object oldV) {
        // Just swap order of arguments to get to new form
        updateListeners(oldV, newOwner);
    }

    @Override
    protected void updateListeners(Object oldV, Object newOwner) {
        if (oldV != null) {
            if (oldV != newOwner) {
                removeElementListener(oldV);
            }
            Object container = facade.getContainer(oldV);
            while (container != null && !facade.isTop(container)) {
                removeElementListener(container);
                container = facade.getContainer(container);
            }
        }
        super.updateListeners(getOwner(), newOwner);
    }

    @Override
    protected void updateFont() {
        super.updateFont();
        Font f = getSettings().getFont(Font.PLAIN);
        referenceFig.setFont(f);
    }

}
