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

package org.argouml.activity2.diagram;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;

import org.argouml.model.InvalidElementException;
import org.argouml.notation2.NotationType;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;

/**
 * The UML defines a Name Compartment, and a List Compartment. 
 * This class implements the latter.<p>
 * 
 * A List Compartment is a boxed compartment,
 * containing vertically stacked figs,
 * which is common to e.g. a stereotypes compartment, operations
 * compartment and an attributes compartment.<p>
 * 
 * @author Bob Tarling
 */
class FigCompartment extends FigGroup {
    
    private static final int MARGIN = 0;

    private Rectangle bounds;
    
    public FigCompartment(Object owner, Rectangle bounds,
            DiagramSettings settings, List elements) {
        for (Object element : elements) {
            try {
                int y = bounds.y + getTopMargin();
                int x = bounds.x + getLeftMargin();
                Rectangle childBounds = new Rectangle(x, y, 0, 0);
                FigNotation fn =
                    new FigNotation(element, childBounds, settings, NotationType.NAME);
                addFig(fn);
                y += fn.getHeight();
            } catch (InvalidElementException e) {
            } 
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
    
    @Override
    public Dimension getMinimumSize() {
        int minWidth = 0;
        int minHeight = 0;
        for (Object f : getFigs()) {
            Fig fig = (Fig) f;
            minWidth = Math.max(fig.getMinimumSize().width, minWidth);
            minHeight += fig.getMinimumSize().height;
        }

        minHeight += getTopMargin() + getBottomMargin();
        minWidth += getLeftMargin() + getRightMargin();
        
        return new Dimension(minWidth, minHeight);
    }

    @Override
    protected void setBoundsImpl(int x, int y, int w, int h) {
        Rectangle oldBounds = getBounds();

        Dimension minimumSize = getMinimumSize();
        
        w = Math.max(w, minimumSize.width);
        h = Math.max(h, minimumSize.height);
        
        bounds = new Rectangle(x, y, w, h);

        w -= getLeftMargin() + getRightMargin();
        h -= getTopMargin() + getBottomMargin();

        y += getTopMargin();
        
        for (Object f : getFigs()) {
            Fig fig = (Fig) f;
            fig.setBounds(x, y, w, fig.getHeight());
            y += fig.getHeight();
        }
        firePropChange("bounds", oldBounds, getBounds());
    }

    protected Rectangle getBoundsImpl() {
        return bounds;
    }
    
    public void populate() {

    }
}
