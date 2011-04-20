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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.argouml.notation2.NotationType;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;

abstract class BaseDisplayState extends FigGroup
        implements StereotypeDisplayer, NameDisplayer, PropertyChangeListener {

    private final DiagramElement bigPort;
    private final DiagramElement nameDisplay;
    
    public BaseDisplayState(
            final Rectangle rect,
            final Color lineColor,
            final Color fillColor,
            final Object modelElement,
            final DiagramSettings settings) {
        nameDisplay = new FigNotation(
                modelElement,
                new Rectangle(0, 0, 0, 0),
                settings,
                NotationType.NAME);
        bigPort = createBigPort(rect, lineColor, fillColor);
        addFig((Fig) bigPort);
        addFig((Fig) getNameDisplay());
        ((Fig) nameDisplay).addPropertyChangeListener(this);
    }
    
    public DiagramElement getStereotypeDisplay() {
        return null;
    }

    public DiagramElement getNameDisplay() {
        return nameDisplay;
    }

    abstract DiagramElement createBigPort(
            final Rectangle rect,
            final Color lineColor,
            final Color fillColor);
    
    DiagramElement getPort() {
        return bigPort;
    }
    
    /**
     * Default implementation makes sure the model element name is
     * displayed in the centre of the node.
     * TODO: We need stereotypes displayed above this.
     */
    @Override
    protected void setBoundsImpl(
            final int x,
            final int y,
            final int w,
            final int h) {

        final Rectangle oldBounds = getBounds();
        
        getPort().setBounds(new Rectangle(x, y, w, h));
        
        final Dimension nameDim = getNameDisplay().getMinimumSize();
        final int nameWidth = nameDim.width;
        final int nameHeight = nameDim.height;
        
        final int nx = x + (w - nameWidth) /2;
        final int ny = y + (h - nameHeight) /2;
        getNameDisplay().setLocation(nx, ny);
        calcBounds();
        firePropChange("bounds", oldBounds, getBounds());
    }

    public void propertyChange(PropertyChangeEvent pce) {
        if (pce.getSource() == getNameDisplay() && pce.getPropertyName().equals("bounds")) {
            calcBounds();
        }
        super.propertyChange(pce);
    }
    
    
}
