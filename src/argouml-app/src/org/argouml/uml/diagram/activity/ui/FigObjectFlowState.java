/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
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
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.argouml.model.Model;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.notation.NotationSettings;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigSingleLineText;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;


/**
 * Class to display graphics for a UML ObjectFlowState in a diagram.<p>
 *
 * The Fig of this modelElement may either represent the following UMLelements:
 * <p>
 * (1) an ObjectFlowState with a Classifier as type, or <p>
 * (2) an ObjectFlowState with a ClassifierInState as type. <p>
 *
 * In both cases (1) and (2), the Fig shows
 * the underlined name of the Classifier,
 * and in the latter case (2), it shows also the names of the states
 * of the ClassifierInState. <p>
 *
 * In the examples in the UML standard, this is written like<pre>
 *      PurchaseOrder
 *       [approved]
 * </pre>
 * i.e. in 2 lines. The first line is underlined,
 * to indicate that it is an instance (object).<p>
 *
 * The fact that the first line is underlined, and the 2nd not, is the
 * reason to implement them in 2 separate Figs.<p>
 *
 * TODO: Allow stereotypes to be shown.
 *
 * @author mvw
 */
public class FigObjectFlowState extends FigNodeModelElement {

    private static final int PADDING = 8;
    private static final int OFS_WIDTH = 70;
    private static final int HEIGHT = 50;
    private static final int STATE_HEIGHT = NAME_FIG_HEIGHT;

    private NotationProvider notationProviderState;
    
    private FigRect cover;
    private FigText state;      // the state name

    
    /**
     * Construct a new FigObjectFlowState.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigObjectFlowState(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);
        state = new FigSingleLineText(owner, new Rectangle(X0, Y0, OFS_WIDTH,
                STATE_HEIGHT), settings, true);
        initFigs(bounds);
    }

    private void initFigs(Rectangle bounds) {
        cover =
            new FigRect(X0, Y0, OFS_WIDTH, HEIGHT,
                    LINE_COLOR, FILL_COLOR);

        getNameFig().setUnderline(true);
        getNameFig().setLineWidth(0);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(cover);
        addFig(getNameFig());
        addFig(state);

        enableSizeChecking(false);

        /* Set the drop location in the case of D&D: */
        if (bounds != null) {
            setLocation(bounds.x, bounds.y);
        }

        renderingChanged();
        setSuppressCalcBounds(false);
        setBounds(getBounds());
        enableSizeChecking(true);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#initNotationProviders(java.lang.Object)
     */
    @Override
    protected void initNotationProviders(Object own) {
        super.initNotationProviders(own);
        if (Model.getFacade().isAModelElement(own)) {
            NotationName notationName = Notation
                    .findNotation(getNotationSettings().getNotationLanguage());
            notationProviderState =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        NotationProviderFactory2.TYPE_OBJECTFLOWSTATE_STATE,
                        own, this, notationName);
        }
    }
    
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_OBJECTFLOWSTATE_TYPE;
    }

    @Override
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        // TODO: Rather than specifically ignore some item maybe it would be better
        // to specifically state what items are of interest. Otherwise we may still
        // be acting on other events we don't need
        if (!Model.getFacade().isATransition(mee.getNewValue())) {
            renderingChanged();
            updateListeners(getOwner(), getOwner());
        }
    }

    @Override
    protected void updateListeners(Object oldOwner, Object newOwner) {
        Set<Object[]> l = new HashSet<Object[]>();

        if (newOwner != null) {
            /* Don't listen to all property names
             * We only need to listen to its "type", and "remove". */
            l.add(new Object[] {newOwner, new String[] {"type", "remove"}});
            // register for events from the type
            Object type = Model.getFacade().getType(newOwner);
            if (Model.getFacade().isAClassifier(type)) {
                if (Model.getFacade().isAClassifierInState(type)) {
                    Object classifier = Model.getFacade().getType(type);
                    l.add(new Object[] {classifier, "name"});
                    l.add(new Object[] {type, "inState"});
                    Collection states = Model.getFacade().getInStates(type);
                    Iterator i = states.iterator();
                    while (i.hasNext()) {
                        l.add(new Object[] {i.next(), "name"});
                    }
                } else {
                    l.add(new Object[] {type, "name"});
                }
            }
        }

        updateElementListeners(l);
    }

    @Override
    public Object clone() {
        FigObjectFlowState figClone = (FigObjectFlowState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.cover = (FigRect) it.next();
        figClone.setNameFig((FigText) it.next());
        figClone.state = (FigText) it.next();
        return figClone;
    }

    @Override
    public void setEnclosingFig(Fig encloser) {
        LayerPerspective layer = (LayerPerspective) getLayer();
        // If the layer is null, then most likely we are being deleted.
        if (layer == null) {
            return;
        }

        super.setEnclosingFig(encloser);
    }

    /*
     * The space between the 2 text figs is: PADDING.
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    @Override
    public Dimension getMinimumSize() {
        Dimension tempDim = getNameFig().getMinimumSize();
        int w = tempDim.width + PADDING * 2;
        int h = tempDim.height + PADDING;
        tempDim = state.getMinimumSize();
        w = Math.max(w, tempDim.width + PADDING * 2);
        h = h + PADDING + tempDim.height + PADDING;

        return new Dimension(Math.max(w, OFS_WIDTH / 2), Math.max(h, HEIGHT / 2));
    }

    /*
     * Override setBounds to keep shapes looking right.
     * The classifier and state Figs are nicely centered vertically,
     * and stretched out over the full width,
     * to allow easy selection with the mouse.
     * The Fig can only be shrunk to half its original size - so that
     * it is not reducible to a few pixels only.
     *
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
        Rectangle oldBounds = getBounds();

        Dimension classDim = getNameFig().getMinimumSize();
        Dimension stateDim = state.getMinimumSize();
        /* the height of the blank space above and below the text figs: */
        int blank = (h - PADDING - classDim.height - stateDim.height) / 2;
        getNameFig().setBounds(x + PADDING,
                y + blank,
                w - PADDING * 2,
                classDim.height);
        state.setBounds(x + PADDING,
                y + blank + classDim.height + PADDING,
                w - PADDING * 2,
                stateDim.height);

        getBigPort().setBounds(x, y, w, h);
        cover.setBounds(x, y, w, h);

        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    @Override
    public void renderingChanged() {
        super.renderingChanged();
        updateStateText();
        updateBounds();
        damage();
    }

    /**
     * Updates the text of the state FigText.
     */
    private void updateStateText() {
        if (isReadyToEdit()) {
            state.setText(notationProviderState.toString(getOwner(), 
                    getNotationSettings()));
        }
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
    @Override
    public Color getLineColor() {
        return cover.getLineColor();
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
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    @Override
    protected void textEdited(FigText ft) throws PropertyVetoException {
        super.textEdited(ft);
        if (ft == state) {
            notationProviderState.parse(getOwner(), ft.getText());
            ft.setText(notationProviderState.toString(getOwner(),
                    getNotationSettings()));
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    @Override
    protected void textEditStarted(FigText ft) {
        super.textEditStarted(ft);
        if (ft == state) {
            showHelp(notationProviderState.getParsingHelp());
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    @Override
    public Selection makeSelection() {
        return new SelectionActionState(this);
    }

    public void notationRenderingChanged(NotationProvider np, String rendering) {
        super.notationRenderingChanged(np, rendering);
        if (notationProviderState == np) {
            state.setText(rendering);
            updateBounds();
            damage();
        }
    }

    public NotationSettings getNotationSettings(NotationProvider np) {
        // both have the same settings
        return getNotationSettings();
    }

    public Object getOwner(NotationProvider np) {
        // both have the same owner
        return getOwner();
    }

}
