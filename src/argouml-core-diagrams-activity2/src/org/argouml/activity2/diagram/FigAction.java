/* $Id: FigAction.java bobtarling $
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
import java.awt.Rectangle;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRRect;

public class FigAction extends FigNodeModelElement {

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
    public FigAction(final Object owner, final Rectangle bounds,
            final DiagramSettings settings) {
        super(owner, bounds, settings);
        addFig(getBigPort());
    }

    @Override
    protected Fig createBigPortFig() {
        return new FigRRect(X0, Y0, WIDTH, HEIGHT, LINE_COLOR, FILL_COLOR);
    }
    
    @Override
    public Dimension getMinimumSize() {
        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        Dimension nameDim = getNameFig().getMinimumSize();

        int w = Math.max(stereoDim.width, nameDim.width) + PADDING * 2;
        /* The stereoDim has height=2, even if it is empty, 
         * hence the -2 below: */
        int h = stereoDim.height - 2 + nameDim.height + PADDING;
        w = Math.max(w, h + 44); // the width needs to be > the height
        return new Dimension(w, h);
    }

    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
        }
        Rectangle oldBounds = getBounds();

        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        Dimension nameDim = getNameFig().getMinimumSize();
        getNameFig().setBounds(x + PADDING, y + stereoDim.height,
                w - PADDING * 2, nameDim.height);
        getStereotypeFig().setBounds(x + PADDING, y,
                w - PADDING * 2, stereoDim.height);
        getBigPort().setBounds(x, y, w, h);
        ((FigRRect) getBigPort()).setCornerRadius(h);

        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }
}
