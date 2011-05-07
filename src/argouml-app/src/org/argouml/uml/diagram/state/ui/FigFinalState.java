/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.activity.ui.SelectionActionState;
import org.argouml.uml.diagram.use_case.ui.FigUseCase.FigMyCircle;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;

/**
 * Class to display graphics for a UML FinalState in a diagram.
 * <p>
 * This class supports any line width.
 *
 * @author ics125b spring 98
 */
public class FigFinalState extends FigStateVertex {

    /**
     * The diameter of the disc when the line width would be 0
     */
    private static final int DISC = 22;
    
    /**
     * The fixed outside diameter.
     */
    private static final int DIA = DISC + 2 * LINE_WIDTH;

    private FigCircle inCircle;
    private FigCircle outCircle;

    
    /**
     * Construct a new FigFinalState.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigFinalState(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initFigs(bounds);
    }

    private void initFigs(Rectangle bounds) {
        setEditable(false);
        Color handleColor = Globals.getPrefs().getHandleColor();
        outCircle =
            new FigCircle(X0, Y0, DIA, DIA, LINE_COLOR, FILL_COLOR);
        inCircle =
            new FigCircle(
        		  X0 + 5,
        		  Y0 + 5,
        		  DIA - 10,
        		  DIA - 10,
        		  handleColor,
        		  LINE_COLOR);

        outCircle.setLineWidth(LINE_WIDTH);
        outCircle.setLineColor(LINE_COLOR);
        inCircle.setLineWidth(0);

        addFig(getBigPort());
        addFig(outCircle);
        addFig(inCircle);

        setBlinkPorts(false); //make port invisible unless mouse enters

        /* Set the drop location in the case of D&D: */
        if (bounds != null) {
            setLocation(bounds.x, bounds.y);
        }

        setSuppressCalcBounds(false);
        setBounds(getBounds());
        enableSizeChecking(true);
    }
    
    @Override
    protected Fig createBigPortFig() {
        return new FigMyCircle(X0, Y0, DIA, DIA, 
                LINE_COLOR, FILL_COLOR);
    }

    @Override
    public Object clone() {
        FigFinalState figClone = (FigFinalState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigCircle) it.next());
        figClone.outCircle = (FigCircle) it.next();
        figClone.inCircle = (FigCircle) it.next();

        return figClone;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    @Override
    public Selection makeSelection() {
        Object pstate = getOwner();
        Selection sel = null;
        if ( pstate != null) {
            if (Model.getFacade().getUmlVersion().startsWith("1")
                    && Model.getFacade().isAActivityGraph(
                            Model.getFacade().getStateMachine(
                            Model.getFacade().getContainer(pstate)))) {
                sel = new SelectionActionState(this);
                ((SelectionActionState) sel).setOutgoingButtonEnabled(false);
            } else {
                sel = new SelectionState(this);
                ((SelectionState) sel).setOutgoingButtonEnabled(false);
            }
        }
        return sel;
    }

    /**
     * Final states are fixed size.
     * @return false
     * @see org.tigris.gef.presentation.Fig#isResizable()
     */
    @Override
    public boolean isResizable() {
        return false;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    @Override
    public void setLineColor(Color col) {
        outCircle.setLineColor(col);
        inCircle.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    @Override
    public Color getLineColor() {
        return outCircle.getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    @Override
    public void setFillColor(Color col) {
        if (Color.black.equals(col)) {
            /* See issue 5721. 
             * Projects before 0.28 have their fill color set to black.
             * We refuse that color and replace by white.
             * All other fill colors are accepted: */
            col = Color.white;
        }
        outCircle.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    @Override
    public Color getFillColor() {
        return outCircle.getFillColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    @Override
    public void setFilled(boolean f) {
        // ignored - rendering is fixed
    }

    
    @Override
    public boolean isFilled() {
        return true;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    @Override
    public void setLineWidth(int w) {
        outCircle.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    @Override
    public int getLineWidth() {
	return outCircle.getLineWidth();
    }

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        // ignore mouse clicks
    }

    /**
     * Return a list of gravity points around the outer circle. Used in place of
     * the default bounding box.
     *
     * {@inheritDoc}
     */
    @Override
    public List getGravityPoints() {
        return getCircleGravityPoints();
    }

    /**
     * Override setBounds to keep shapes looking right.
     * Special care is taken to have the inner circle nicely centered 
     * within the outer circle.
     * {@inheritDoc}
     */
    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
        }
        Rectangle oldBounds = getBounds();

        /* This assert fails for the TestPropertyPanels, 
         * file GUITestPropertyPanels.zargo: */
        //assert  w == h;

        /* Ignore w and h from here on. */

        int out_d = DISC + 2 * getLineWidth();
        
        getBigPort().setBounds(x, y, out_d, out_d);
        outCircle.setBounds(x, y, out_d, out_d);

        int inner_d = (out_d - 2 * getLineWidth()) - 6;
        assert (inner_d % 2) == (out_d % 2);
        // keep d even or odd, just like the line width:
        inner_d = inner_d - (getLineWidth() % 2);
        inCircle.setBounds(
                x + (out_d - inner_d) / 2, 
                y + (out_d - inner_d) / 2, 
                inner_d, 
                inner_d);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

}
