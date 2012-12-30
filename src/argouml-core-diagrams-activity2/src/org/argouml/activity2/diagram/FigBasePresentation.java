/* $Id$
 *****************************************************************************
 * Copyright (c) 2010-2012 Contributors - see below
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;

abstract class FigBasePresentation extends FigComposite
        implements StereotypeDisplayer, NameDisplayer {
    
    private final DiagramElement border;
    private final FigNameCompartment nameDisplay;
    private static final int MIN_WIDTH = 90;
    private static final int MARGIN = 1;
    
    public FigBasePresentation(
            final Object owner,
            final Rectangle rect,
            final Color lineColor,
            final Color fillColor,
            final Object modelElement,
            final DiagramSettings settings) {
        
        super(owner, settings);
        nameDisplay = new FigNameCompartment(
                modelElement,
                new Rectangle(0, 0, 0, 0),
                settings);
        border = createBorder(rect, lineColor, fillColor);
        addFig((Fig) border);
        addFig((Fig) getNameDisplay());
        setBounds(rect);
    }
    
    public DiagramElement getStereotypeDisplay() {
        return null;
    }

    public DiagramElement getNameDisplay() {
        return nameDisplay;
    }

    abstract DiagramElement createBorder(
            final Rectangle rect,
            final Color lineColor,
            final Color fillColor);
    
    DiagramElement getBorder() {
        return border;
    }
    
    // TODO: Move an empty implementation to FigGroup in GEF
    protected void positionChildren() {
        final Rectangle bounds = getBounds();
        
        getBorder().setBounds(bounds);
        
        getNameDisplay().setBounds(
                new Rectangle(bounds.x + getLeftMargin(), bounds.y + getTopMargin(), bounds.width - (getLeftMargin() + getRightMargin()), bounds.height - (getTopMargin() + getBottomMargin())));
    }
    
    @Override
    public Dimension getMinimumSize() {
        
        final Dimension nameDim = getNameDisplay().getMinimumSize();
        int width = nameDim.width;
        int height = nameDim.height;
        
        int w = width + getRightMargin() + getLeftMargin();
        final int h = height + getTopMargin() + getBottomMargin();
        w = Math.max(w, MIN_WIDTH);
        return new Dimension(w, h);
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
