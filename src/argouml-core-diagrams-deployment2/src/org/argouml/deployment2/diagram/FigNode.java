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

package org.argouml.deployment2.diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Vector;

import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.presentation.Fig;

/**
 * A fig representing a node in a UML2 deployment diagram
 * @author Bob Tarling
 */
public class FigNode extends FigNodeModelElement {

    protected static final int DEPTH = 20;
    private static final int WIDTH = 200;
    private static final int HEIGHT = 180;
    private static final int MIN_WIDTH = 80;
    private static final int MIN_HEIGHT = 80;
    private static final int MARGIN = 2;

    @Override
    protected Fig createBigPortFig() {
        FigCube port = new FigCube(0, 0, 
               WIDTH, 
               HEIGHT, DEPTH);
        port.setFilled(false);
        port.setLineWidth(1);
        return port;
    }

    public FigNode(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initialize();
    }
    
    private void initialize() {
        addFig(getBigPort());
        addFig(getStereotypeFig());
        addFig(getNameFig());
    }
    
    @Override
    public Dimension getMinimumSize() {
        final Dimension stereoDim = getStereotypeFig().getMinimumSize();
        final Dimension nameDim = getNameFig().getMinimumSize();
    
        int w = Math.max(stereoDim.width, nameDim.width) + DEPTH;
        int h = stereoDim.height + nameDim.height + DEPTH;
        
        w = Math.max(MIN_WIDTH, w);
        h = Math.max(MIN_HEIGHT, h);
        return new Dimension(w, h);
    }

    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
        }
        getBigPort().setBounds(x, y, w, h);

        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        getStereotypeFig().setBounds(
                x + getLeftMargin(), y + getTopMargin(),
                w - getLeftMargin() - getRightMargin(), stereoDim.height);
        
        Dimension nameDim = getNameFig().getMinimumSize();
        getNameFig().setBounds(
                x + getLeftMargin(), getStereotypeFig().getY() + stereoDim.height + 1,
                w - getLeftMargin() - getRightMargin(), nameDim.height);
        _x = x;
        _y = y;
        _w = w;
        _h = h;
        updateEdges();
    }

    @Override
    public void setEnclosingFig(Fig encloser) {
        if (encloser == null
                || (encloser != null
                && Model.getFacade().isANode(encloser.getOwner()))) {
            super.setEnclosingFig(encloser);
        }
    }

    @Override
    public boolean getUseTrapRect() {
        return true;
    }
    
    public Rectangle getTrapRect() {
        return new Rectangle(_x, _y + DEPTH, _w - DEPTH, _h - DEPTH);
    }

    @Override
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        popUpActions.add(
                popUpActions.size() - getPopupAddOffset(),
                buildModifierPopUp(ABSTRACT | LEAF | ROOT));
        return popUpActions;
    }
    
    
    int getRightMargin() {
        return DEPTH + MARGIN;
    }

    int getLeftMargin() {
        return MARGIN;
    }
    
    int getTopMargin() {
        return DEPTH + MARGIN;
    }
    
    int getBottomMargin() {
        return MARGIN;
    }
    
    
    public class FigCube extends Fig implements Serializable {

        private final int depth;

        public FigCube(int x, int y, int w, int h, int d) {
            super(x, y, w, h);
            depth = d;
        }

        public void paint(Graphics g) {

            final Color fillColor = getFillColor();
            final Color lineColor = getLineColor();

            g.setColor(fillColor);
            Polygon p = new Polygon();
            p.addPoint(_x + depth,      _y);
            p.addPoint(_x + _w,         _y);
            p.addPoint(_x + _w,         _y + _h - depth);
            p.addPoint(_x + _w - depth, _y + _h);
            p.addPoint(_x,              _y + _h);
            p.addPoint(_x,              _y + depth);
            p.addPoint(_x + depth,      _y);
            g.fillPolygon(p);
            
            
            g.setColor(lineColor);
            g.drawPolygon(p);
            g.drawLine(_x, _y + depth, _x + _w - depth, _y + depth);
            g.drawLine(_x + _w - depth, _y + depth, _x + _w - depth, _y + _h);
            g.drawLine(_x + _w - depth, _y + depth, _x + _w, _y);
        }

        public void appendSvg(StringBuffer sb) {
        }
    }
}
