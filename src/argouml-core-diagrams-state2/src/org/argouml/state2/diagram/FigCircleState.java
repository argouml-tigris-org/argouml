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

package org.argouml.state2.diagram;

import java.awt.Point;
import java.awt.Rectangle;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;

/**
 * Abstract class for any state class with a circular boundary.
 * @author Bob Tarling
 */
abstract class FigCircleState extends FigNodeModelElement {

    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;

    public FigCircleState(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initialize();
    }

    @Override
    protected Fig createBigPortFig() {
        FigCircle fc = new FigCircle(0, 0, WIDTH, HEIGHT);
        fc.setLineWidth(1);
        return fc;
    }

    private void initialize() {
        setEditable(false);
        addFig(getBigPort());
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    @Override
    public Point connectionPoint(Point anotherPt) {
        return getBigPort().connectionPoint(anotherPt);
    }
    
    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
        getBigPort().setBounds(x, y, w, h);
        calcBounds();
        updateEdges();
    }
}
