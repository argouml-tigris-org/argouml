/* $Id: $
 *****************************************************************************
 * Copyright (c) 2010-2011 Contributors - see below
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

import org.argouml.notation2.NotationType;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;

abstract class FigBasePresentation extends FigComposite
        implements StereotypeDisplayer, NameDisplayer {
    
    private final DiagramElement border;
    private final DiagramElement nameDisplay;
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
        nameDisplay = new FigNotation(
                modelElement,
                new Rectangle(0, 0, 0, 0),
                settings,
                NotationType.NAME);
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
        Rectangle myBounds = getBounds();
        
        getBorder().setBounds(myBounds);
        
        final Dimension nameDim = getNameDisplay().getMinimumSize();
        final int nameWidth = nameDim.width;
        final int nameHeight = nameDim.height;
        
        final Rectangle bounds = getBounds();
        
        final int nx = bounds.x + getLeftMargin()
            + (bounds.width - (nameWidth + getLeftMargin() + getRightMargin()))
            / 2;
        final int ny = bounds.y + getTopMargin()
            + (bounds.height - nameHeight - getTopMargin() - getBottomMargin())
            / 2;
        getNameDisplay().setLocation(nx, ny);
    }
    
    @Override
    public Dimension getMinimumSize() {
        
        final Dimension nameDim = getNameDisplay().getMinimumSize();
        int width = nameDim.width;
        int height = nameDim.height;
//        if (getStereotypeDisplay() != null) {
//            final Dimension stereoDim = getStereotypeDisplay().getMinimumSize();
//            width += Math.max(stereoDim.width, nameDim.width);
//            height += (stereoDim.height - 2);
//        }
        
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
