/* $Id: $
 *****************************************************************************
 * Copyright (c) 2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 */

package org.argouml.uml.diagram.state.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.notation.NotationSettings;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigText;

/**
 * A Fig that can represent a UML2 vertex. The current implementation
 * is limited to just handling States. Other vertices are currently handled
 * by different Figs but they will most likely all be handled here in future.
 */
public class FigVertex extends FigNodeModelElement {

    private static final int MARGIN = 2;

    private NotationProvider notationProviderBody;

    /**
     * The body for entry/exit/do actions
     */
    private FigBody bodyText;

    /**
     * Create a new instance of FigVertex
     * @param owner the vertex that own this dagram element
     * @param bounds the bounds of the diagram element
     * @param settings the display settings for this diagram element
     */
    public FigVertex(Object owner, Rectangle bounds, DiagramSettings settings) {
        super(owner, bounds, settings);
        this.allowRemoveFromDiagram(false);

        initialize();

        NotationName notation = Notation.findNotation(getNotationSettings()
                .getNotationLanguage());
        notationProviderBody = NotationProviderFactory2.getInstance()
                .getNotationProvider(NotationProviderFactory2.TYPE_STATEBODY,
                        getOwner(), this, notation);
        updateNameText();
    }

    @Override
    public void setEnclosingFig(Fig encloser) {
        LayerPerspective lp = (LayerPerspective) getLayer();
        if (lp == null) {
            return;
        }
        
        super.setEnclosingFig(encloser);

        final Object region;
        if (encloser != null
                && (Model.getFacade().isACompositeState(encloser.getOwner()))) {
            // Get the region as the first Region in the State.
            // If there is no region in the StateMachine then create one.
            List regions = Model.getStateMachinesHelper().getRegions(
                    encloser.getOwner());
            if (regions.isEmpty()) {
                region = Model.getUmlFactory().buildNode(
                        Model.getMetaTypes().getRegion(), getOwner());
            } else {
                region = regions.get(0);
            }
        } else {
            // Get the region as the first Region in the StateMachine.
            // If there is no region in the StateMachine then create one.
            ArgoDiagram diagram = (ArgoDiagram) lp.getDiagram();
            Object stateMachine = diagram.getOwner();
            List regions = 
                Model.getStateMachinesHelper().getRegions(stateMachine);
            if (regions.isEmpty()) {
                region = Model.getUmlFactory().buildNode(
                        Model.getMetaTypes().getRegion(), stateMachine);
            } else {
                region = regions.get(0);
            }
        }
        if (region != null
                && Model.getFacade().getContainer(getOwner()) != region) {
            Model.getStateMachinesHelper().setContainer(getOwner(), region);
        }
    }

    @Override
    public Selection makeSelection() {
        return new SelectionState(this);
    }

    @Override
    protected Fig createBigPortFig() {
        return new FigRRect(0, 0, 0, 0, LINE_COLOR, FILL_COLOR);
    }

    private void initialize() {
        getNameFig().setLineWidth(0);
        getNameFig().setFilled(false);

        bodyText = new FigBody(0,0,0,0);
        
        addFig(getBigPort());
        addFig(getNameFig());
        addFig(getBodyText());

        setBounds(getBounds());
    }

    /*
     * @see
     * org.argouml.uml.diagram.state.ui.FigStateVertex#initNotationProviders
     * (java.lang.Object)
     */
    @Override
    protected void initNotationProviders(Object own) {
        if (notationProviderBody != null) {
            notationProviderBody.cleanListener();
        }
        super.initNotationProviders(own);
        NotationName notation = Notation.findNotation(getNotationSettings()
                .getNotationLanguage());
        if (Model.getFacade().isAState(own)) {
            notationProviderBody = NotationProviderFactory2.getInstance()
                    .getNotationProvider(
                            NotationProviderFactory2.TYPE_STATEBODY, own, this,
                            notation);
        }
    }

    /*
     * @see
     * org.argouml.uml.diagram.ui.FigNodeModelElement#removeFromDiagramImpl()
     */
    @Override
    public void removeFromDiagramImpl() {
        if (notationProviderBody != null) {
            notationProviderBody.cleanListener();
        }
        super.removeFromDiagramImpl();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    @Override
    public void renderingChanged() {
        super.renderingChanged();
        if (notationProviderBody != null) {
            bodyText.setText(notationProviderBody.toString(getOwner(),
                    getNotationSettings()));
        }
        calcBounds();
        setBounds(getBounds());
    }

    private FigText getBodyText() {
        return bodyText;
    }

    @Override
    protected void textEditStarted(FigText ft) {
        super.textEditStarted(ft);
        if (ft == bodyText) {
            showHelp(notationProviderBody.getParsingHelp());
        }
    }

    @Override
    public void textEdited(FigText ft) throws PropertyVetoException {
        super.textEdited(ft);
        if (ft == getBodyText()) {
            notationProviderBody.parse(getOwner(), ft.getText());
            ft.setText(notationProviderBody.toString(getOwner(),
                    getNotationSettings()));
        }
    }

    public void notationRenderingChanged(NotationProvider np, String rendering) {
        super.notationRenderingChanged(np, rendering);
        if (notationProviderBody == np) {
            bodyText.setText(rendering);
            updateBounds();
            damage();
        }
    }

    public NotationSettings getNotationSettings(NotationProvider np) {
        return getNotationSettings();
    }

    public Object getOwner(NotationProvider np) {
        return getOwner();
    }

    public Dimension getMinimumSize() {
        final Dimension nameSize = getNameFig().getMinimumSize();
        final Dimension bodySize = getBodyText().getMinimumSize();

        int h = getTopMargin()
            + nameSize.height + bodySize.height
            + getBottomMargin();
        
        int w = getLeftMargin()
            + Math.max(nameSize.width, bodySize.width)
            + getRightMargin();
        
        if (Model.getFacade().isACompositeState(getOwner())) {
            w = Math.max(180, w);
            h = Math.max(150, h);
        } else {
            w = Math.max(80, w);
            h = Math.max(40, h);
        }
        
        return new Dimension(w, h);
    }

    public boolean getUseTrapRect() {
        return true;
    }

    protected void setStandardBounds(int x, int y, int w, int h) {
        Dimension nameSize = getNameFig().getMinimumSize();
        Dimension bodySize = getBodyText().getMinimumSize();

        getNameFig().setBounds(
                x + getLeftMargin(), y + getTopMargin(),
                w - getLeftMargin() - getRightMargin(), nameSize.height);

        getBodyText().setBounds(
                x + getLeftMargin(), y + getTopMargin() + nameSize.height,
                bodySize.width,
                bodySize.height);

        getBigPort().setBounds(x, y, w, h);
    }
    
    /**
     * The text Fig that displays the body of the actions on the state
     *
     * @author Bob Tarling
     */
    private class FigBody extends FigText {
        public FigBody(int x, int y, int width, int height) {
            super (x, y, width, height);
            setFont(getSettings().getFont(Font.PLAIN));
            setTextColor(TEXT_COLOR);
            setLineWidth(0);
            setFilled(false);
            setExpandOnly(true);
            setReturnAction(FigText.INSERT);
            setJustification(FigText.JUSTIFY_LEFT);
        }
    }
    
    
    int getRightMargin() {
        return MARGIN;
    }

    int getLeftMargin() {
        return MARGIN;
    }
    
    int getTopMargin() {
        return MARGIN;
    }
    
    int getBottomMargin() {
        return MARGIN;
    }
}
