/* $Id$
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
import java.util.Arrays;
import java.util.List;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AssociationChangeListener;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.notation2.NotationType;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;

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
class FigCompartment extends FigComposite implements AssociationChangeListener {

    public FigCompartment(
            final Object owner,
            final Rectangle bounds,
            final DiagramSettings settings,
            final Object metaType,
            final String propertyName) {
        this(owner, bounds, settings,
                Arrays.asList(new Object[] {metaType}),
                propertyName);
    }
    
    /**
     * @param owner the model element that owns the compartment
     * @param bounds the initial bounds of the compartment
     * @param settings the diagram settings
     * @param metaType the different metatype that can be displayed in the compartment
     */
    public FigCompartment(
            final Object owner,
            final Rectangle bounds,
            final DiagramSettings settings,
            final List<Object> metaTypes,
            final String propertyName) {
        
        super(owner, settings);
        
        Model.getFacade().getModelElementContents(owner);
        for (Object element
                : Model.getFacade().getModelElementContents(owner)) {
            if (metaTypes.contains(element.getClass())) {
                try {
                    int y = bounds.y + getTopMargin();
                    int x = bounds.x + getLeftMargin();
                    Rectangle childBounds = new Rectangle(x, y, 0, 0);
                    FigNotation fn = new FigNotation(
                            element, childBounds, settings, NotationType.NAME);
                    addFig(fn);
                    y += fn.getHeight();
                } catch (InvalidElementException e) {
                }
            }
        }
        
        Model.getPump().addModelEventListener(
                (AssociationChangeListener) this, owner, propertyName);
        // TODO: Remove listeners for add/remove events
    }
    
    public FigCompartment(
            final Object owner,
            final DiagramSettings settings) {
        super(owner, settings);
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
    protected void positionChildren() {

        int w = _w - (getLeftMargin() + getRightMargin());
        int x = _x + getLeftMargin();
        int y = _y + getTopMargin();
        
        for (Object f : getFigs()) {
            Fig fig = (Fig) f;
            fig.setBounds(x, y, w, fig.getMinimumSize().height);
            y += fig.getHeight();
        }
    }

    public void elementAdded(AddAssociationEvent evt) {
        Object element = evt.getNewValue();
        Rectangle childBounds = new Rectangle(getX() + getHeight(), getY(), 0, 0);
        FigNotation fn = new FigNotation(
                element, childBounds, getDiagramSettings(), NotationType.NAME);
        addFig(fn);
        calcBounds();
    }

    public void elementRemoved(RemoveAssociationEvent evt) {
        Object element = evt.getOldValue();
        for (Object f : getFigs()) {
            Fig fig = (Fig) f;
            if (fig.getOwner() == element) {
                removeFig(fig);
                calcBounds();
                return;
            }
        }
    }
}
