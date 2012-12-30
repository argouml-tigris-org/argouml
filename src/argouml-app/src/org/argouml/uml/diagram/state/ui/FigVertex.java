/* $Id$
 *****************************************************************************
 * Copyright (c) 2011-2012 Contributors - see below
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
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.notation.NotationSettings;
import org.argouml.ui.ActionCreateContainedModelElement;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.Selection;
import org.tigris.gef.di.DiagramElement;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigText;

/**
 * A Fig that can represent a UML2 vertex. The current implementation
 * is limited to just handling States. Other vertices are currently handled
 * by different Figs but they will most likely all be handled here in future.
 */
public class FigVertex extends FigNodeModelElement {

    private static final Logger LOG =
        Logger.getLogger(FigVertex.class.getName());

    private static final int MARGIN = 2;

    private NotationProvider notationProviderBody;

    /**
     * The body for entry/exit/do actions
     */
    private FigBody bodyText;

    private FigGroup regionCompartment;

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

        LOG.log(Level.INFO, "Registering as listener");
        Model.getPump().addModelEventListener(this, getOwner(), "region");
    }

    @Override
    public void setEnclosingFig(Fig encloser) {
        LayerPerspective lp = (LayerPerspective) getLayer();
        if (lp == null) {
            return;
        }

        super.setEnclosingFig(encloser);

        Object region = null;
        if (encloser != null) {
            // Get the region as the first Region in the State.
            // If there is no region in the StateMachine then create one.
            List regions = Model.getStateMachinesHelper().getRegions(
                    encloser.getOwner());
            if (regions.isEmpty()) {
                // There are no regions so create one and
                // place the vertex there.
                region = Model.getUmlFactory().buildNode(
                        Model.getMetaTypes().getRegion(), encloser.getOwner());
            } else {
                // There are one or more regions so find the one that the
                //vertex was dropped in
                FigVertex compositeState = (FigVertex) encloser;
                for (DiagramElement de :
                        compositeState.regionCompartment.getDiagramElements()) {
                    if (((Fig) de).getBounds().contains(getBounds())) {
                        region = de.getOwner();
                        break;
                    }
                }
            }
        } else {
            // The vertex was dropped onto the diagram.
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
        return new SelectionVertex(this);
    }

    @Override
    protected Fig createBigPortFig() {
        return new FigRRect(0, 0, 0, 0, LINE_COLOR, FILL_COLOR);
    }

    private void initialize() {
        getNameFig().setLineWidth(0);
        getNameFig().setFilled(false);

        bodyText = new FigBody(0, 0, 0, 0);
        regionCompartment = new FigRegionCompartment(0, 0, 0, 0);

        addFig(getBigPort());
        addFig(getNameFig());
        addFig(getBodyText());
        addFig(regionCompartment);

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

    // Temporary start
//    private static final Color[] COLOR_ARRAY = {
//        Color.RED, Color.BLUE, Color.CYAN, Color.YELLOW, Color.GREEN};
//    private int nextColor = 0;
    // Temporary end

    @Override
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);

        assert (mee.getPropertyName().equals("region"));

        if (mee instanceof AddAssociationEvent) {
            // TODO: Before adding a new region make the last region
            // its minimum size (smallest size that will still
            // contain all enclosed)

            Object newRegion = mee.getNewValue();
            FigRegion rg = new FigRegion(newRegion);
            rg.setBounds(
                    regionCompartment.getX(), regionCompartment.getY(),
                    rg.getMinimumSize().width, rg.getMinimumSize().height);

            // Temporary start - colour the regions so that we can
            // see them for now
//            rg.setFillColor(COLOR_ARRAY[nextColor++]);
//            if (nextColor >= COLOR_ARRAY.length) {
//                nextColor = 0;
//            }
            // Temporary end

            regionCompartment.addFig(rg);
            setSize(getMinimumSize());
        }
        if (mee instanceof RemoveAssociationEvent) {
            Object oldRegion = mee.getNewValue();
            for (DiagramElement de : regionCompartment.getDiagramElements()) {
                if (de.getOwner() == oldRegion) {
                    regionCompartment.removeFig((Fig) de);
                    // TODO: After removing a region reset the overall
                    // size of the node.
                    renderingChanged();
                    damage();
                }
            }
            LOG.log(Level.FINE, "Removing region {0}", oldRegion);
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

    public void notationRenderingChanged(
            NotationProvider np,
            String rendering) {
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
            + nameSize.height
            + getBottomMargin();

        h += regionCompartment.getMinimumSize().height;

        if (getBodyText().getText().length() > 0) {
            h += bodySize.height;
        }

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

    public List<Rectangle> getTrapRects() {
        List regions = Model.getStateMachinesHelper().getRegions(getOwner());

        ArrayList<Rectangle> rects = new ArrayList<Rectangle>(regions.size());
        if (regions.isEmpty()) {
            rects.add(regionCompartment.getBounds());
        } else {
            for (DiagramElement f : regionCompartment.getDiagramElements()) {
                rects.add(((Fig) f).getBounds());
            }
        }
        return rects;
    }

    protected void setStandardBounds(int x, int y, int w, int h) {
        Dimension nameSize = getNameFig().getMinimumSize();
        Dimension bodySize = getBodyText().getMinimumSize();

        getNameFig().setBounds(
                x + getLeftMargin(), y + getTopMargin(),
                w - getLeftMargin() - getRightMargin(), nameSize.height);



        if (getBodyText().getText().length() > 0) {
            getBodyText().setBounds(
                    x + getLeftMargin(), y + getTopMargin() + nameSize.height,
                    bodySize.width,
                    bodySize.height);
        } else {
            getBodyText().setBounds(
                    x + getLeftMargin(), y + getTopMargin() + nameSize.height,
                    bodySize.width,
                    1);
        }

        regionCompartment.setBounds(
                x + getLeftMargin(),
                getBodyText().getY() + getBodyText().getHeight(),
                w - getLeftMargin() - getRightMargin(),
                h - getTopMargin() - getBottomMargin()
                    - getNameFig().getHeight() - getBodyText().getHeight());

        getBigPort().setBounds(x, y, w, h);

        calcBounds(); // _x = x; _y = y; _w = w; _h = h;
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

    /*
     * @see org.tigris.gef.ui.PopupGenerator#getPopUpActions(java.awt.event.MouseEvent)
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        if (TargetManager.getInstance().getTargets().size() == 1) {
            popUpActions.add(
                    popUpActions.size() - getPopupAddOffset(),
                    new ActionCreateContainedModelElement(
                            Model.getMetaTypes().getRegion(), getOwner()));
        }
        return popUpActions;
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

    /**
     * The text Fig that displays the body of the actions on the state
     *
     * @author Bob Tarling
     */
    private class FigRegionCompartment extends FigGroup {
        public FigRegionCompartment(int x, int y, int width, int height) {
            super();
        }

        @Override
        protected void setBoundsImpl(
                final int x,
                int y,
                final int w,
                int h) {

            _x = x;
            _y = y;
            _w = w;
            _h = h;

            for (Iterator it = getFigs().iterator(); it.hasNext(); ) {
                Fig fig = (Fig) it.next();
                if (it.hasNext()) {
                    fig.setBounds(x, y, w, fig.getMinimumSize().height);
                    h -= fig.getMinimumSize().height;
                } else {
                    fig.setBounds(x, y, w, h);
                }
                y += fig.getHeight();
            }
        }


        @Override
        public Dimension getMinimumSize() {
            int minWidth = 0;
            int minHeight = 0;
            for (Iterator it = getFigs().iterator(); it.hasNext(); ) {
                Fig fig = (Fig) it.next();
                minWidth = Math.max(fig.getMinimumSize().width, minWidth);
                if (it.hasNext()) {
                    minHeight += fig.getHeight();
                } else {
                    minHeight += fig.getMinimumSize().height;
                }
            }

            return new Dimension(minWidth, minHeight);
        }

        public void paint(Graphics g) {
            super.paint(g);

            for (Iterator it = getFigs().iterator(); it.hasNext(); ) {
                Fig fig = (Fig) it.next();
                if (it.hasNext()) {
                    g.setColor(getLineColor());

                    drawDashedLine(
                            g, 1,
                            fig.getX(),
                            fig.getY() + fig.getHeight(),
                            fig.getX() + fig.getWidth(),
                            fig.getY() + fig.getHeight(),
                            0, new float [] {
                                5.0f, 5.0f },
                            10);
                }
            }

        }
    }
}
