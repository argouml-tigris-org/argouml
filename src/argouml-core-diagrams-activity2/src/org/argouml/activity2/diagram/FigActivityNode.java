/* $Id: $
 *****************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 */

package org.argouml.activity2.diagram;

import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Rectangle;

import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigRect;

public class FigActivityNode extends FigNodeModelElement {

    private static final int PADDING = 8;
    private static final int WIDTH = 90;
    private static final int HEIGHT = 25;

    /**
     * Constructor a new FigAction
     * 
     * @param owner the owning UML element
     * @param bounds rectangle describing bounds
     * @param settings rendering settings
     */
    public FigActivityNode(final Object owner, final Rectangle bounds,
            final DiagramSettings settings) {
        super(owner, bounds, settings);
        addFig(getBigPort());
    }

    @Override
    protected Fig createBigPortFig() {
        if (Model.getFacade().isASendSignalAction(getOwner())) {
            final int[] xs = new int[6];
            final int[] ys = new int[6];
            
            xs[0] = X0;                      ys[0] = Y0;
            xs[1] = X0 + WIDTH - HEIGHT / 2; ys[1] = Y0;
            xs[2] = X0 + WIDTH;              ys[2] = Y0 + HEIGHT / 2;
            xs[3] = X0 + WIDTH - HEIGHT / 2; ys[3] = Y0 + HEIGHT;
            xs[4] = X0;                      ys[4] = Y0 + HEIGHT;
            xs[5] = X0;                      ys[5] = Y0;
            final Polygon p = new Polygon(xs, ys, 6);
            final FigGravityPoly polyFig = new FigGravityPoly(p);
//            final FigPoly polyFig = new FigPoly();
//            polyFig.setPolygon(p);
            return polyFig;
        } else if (Model.getFacade().isAAcceptEventAction(getOwner())) {
            final int[] xs = new int[6];
            final int[] ys = new int[6];
                
            xs[0] = X0;              ys[0] = Y0;
            xs[1] = X0 + WIDTH;      ys[1] = Y0;
            xs[2] = X0 + WIDTH;      ys[2] = Y0 + HEIGHT;
            xs[3] = X0;              ys[3] = Y0 + HEIGHT;
            xs[4] = X0 + HEIGHT / 2; ys[4] = Y0 + HEIGHT / 2;
            xs[5] = X0;              ys[5] = Y0;
            final Polygon p = new Polygon(xs, ys, 6);
            final FigGravityPoly polyFig = new FigAcceptEventPoly(X0, Y0, WIDTH, HEIGHT, LINE_COLOR, FILL_COLOR);
            return polyFig;
        } else if (Model.getFacade().isAObjectNode(getOwner())) {
            return new FigRect(X0, Y0, WIDTH, HEIGHT, LINE_COLOR, FILL_COLOR);
        } else if (Model.getFacade().isAAction(getOwner())) {
            return new FigRRect(X0, Y0, WIDTH, HEIGHT, LINE_COLOR, FILL_COLOR);
        } else {
            // More to do here
            return null;
        }
    }
    
    @Override
    public Dimension getMinimumSize() {
        final Dimension stereoDim = getStereotypeFig().getMinimumSize();
        final Dimension nameDim = getNameFig().getMinimumSize();

        int w = Math.max(stereoDim.width, nameDim.width) + PADDING * 2;
        /* The stereoDim has height=2, even if it is empty, 
         * hence the -2 below: */
        final int h = stereoDim.height - 2 + nameDim.height + PADDING;
        w = Math.max(w, h + 44); // the width needs to be > the height
        return new Dimension(w, h);
    }

    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
        }
        final Rectangle oldBounds = getBounds();

        final Dimension stereoDim = getStereotypeFig().getMinimumSize();
        final Dimension nameDim = getNameFig().getMinimumSize();
        getNameFig().setBounds(x + PADDING, y + stereoDim.height,
                w - PADDING * 2, nameDim.height);
        getStereotypeFig().setBounds(x + PADDING, y,
                w - PADDING * 2, stereoDim.height);
        final Fig bigPort = getBigPort();
        if (Model.getFacade().isASendSignalAction(getOwner())) {
            final int[] xs = new int[6];
            final int[] ys = new int[6];
            xs[0] = x;                      ys[0] = y;
            xs[1] = x + WIDTH - HEIGHT / 2; ys[1] = y;
            xs[2] = x + WIDTH;              ys[2] = y + HEIGHT / 2;
            xs[3] = x + WIDTH - HEIGHT / 2; ys[3] = y + HEIGHT;
            xs[4] = x;                      ys[4] = y + HEIGHT;
            xs[5] = x;                      ys[5] = y;
            final FigPoly polyFig = (FigPoly)bigPort;
            final Polygon p = new Polygon(xs, ys, 6);
            polyFig.setPolygon(p);
        } else if (Model.getFacade().isAAcceptEventAction(getOwner())) {
            final int[] xs = new int[6];
            final int[] ys = new int[6];
            
            xs[0] = x;              ys[0] = y;
            xs[1] = x + WIDTH;      ys[1] = y;
            xs[2] = x + WIDTH;      ys[2] = y + HEIGHT;
            xs[3] = x;              ys[3] = y + HEIGHT;
            xs[4] = x + HEIGHT / 2; ys[4] = y + HEIGHT / 2;
            xs[5] = x;              ys[5] = y;
            final FigPoly polyFig = (FigPoly)bigPort;
            final Polygon p = new Polygon(xs, ys, 6);
            polyFig.setPolygon(p);
        } else {
            getBigPort().setBounds(x, y, w, h);
            ((FigRRect) getBigPort()).setCornerRadius(h);
        }

        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }
}
