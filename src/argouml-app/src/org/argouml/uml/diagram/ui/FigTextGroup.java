/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 *
 * Some portions of this file were previously release using the BSD License:
 */
// $Id$
// Copyright (c) 2003-2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

/**
 * Custom class to group FigTexts in such a way that they don't
 * overlap and that the group is shrinked to fit (no whitespace in
 * group).
 *
 * @author jaap.branderhorst@xs4all.nl
 */
public class FigTextGroup extends ArgoFigGroup implements MouseListener {

    private boolean supressCalcBounds = false;

    /**
     * Construct a FigGroup with the given render settings.
     * 
     * @param owner owning UML element
     * @param settings rendering settings.
     */
    public FigTextGroup(Object owner, DiagramSettings settings) {
        super(owner, settings);
    }
    
    /**
     * Adds a FigText to the list with figs. Makes sure that the
     * figtexts do not overlap.
     * {@inheritDoc}
     */
    @Override
    public void addFig(Fig f) {
	super.addFig(f);
        updateFigTexts();
        calcBounds();
    }

    /**
     * Updates the FigTexts. FigTexts without text (equals "") are not shown.
     * The rest of the figtexts are shown non-overlapping. The first figtext
     * added (via addFig) is shown at the bottom of the FigTextGroup.
     */
    private void updateFigTexts() {
        int height = 0;
        for (Fig fig : getFigs()) {
            int figHeight = fig.getMinimumSize().height;
            fig.setBounds(getX(), getY() + height, fig.getWidth(), figHeight);
            fig.endTrans();
            height += fig.getHeight();
        }
    }


    /*
     * @see org.tigris.gef.presentation.Fig#calcBounds()
     */
    @Override
    public void calcBounds() {
    	updateFigTexts();
        if (!supressCalcBounds) {
            super.calcBounds();
            // get the widest of all textfigs
            // calculate the total height
            int maxWidth = 0;
            int height = 0;
            for (Fig fig : getFigs()) {
//                fig.calcBounds();
                if (fig.getWidth() > maxWidth) {
                    maxWidth = fig.getWidth();
                }
                fig.setHeight(fig.getMinimumSize().height);
                height += fig.getHeight();
            }
            _w = maxWidth;
            _h = height;
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#removeFromDiagram()
     */
    @Override
    public void removeFromDiagram() {
        for (Fig fig : getFigs()) {
            fig.removeFromDiagram();
        }
        super.removeFromDiagram();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#deleteFromModel()
     */
    @Override
    public void deleteFromModel() {
        for (Fig fig : getFigs()) {
            fig.deleteFromModel();
        }
        super.deleteFromModel();
    }

    ////////////////////////////////////////////////////////////////
    // event handlers - MouseListener implementation

    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent me) {
        // ignored
    }

    /*
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent me) {
        // ignored
    }

    /*
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent me) {
        // ignored
    }

    /*
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent me) {
        // ignored
    }

    /**
     * Handle mouse click. If the user double clicks on any part of this
     * FigGroup, pass it down to one of the internal Figs. This allows the user
     * to initiate direct text editing.
     * 
     * {@inheritDoc}
     */
    public void mouseClicked(MouseEvent me) {
        if (me.isConsumed()) {
            return;
        }
        if (me.getClickCount() >= 2) {
            Fig f = hitFig(new Rectangle(me.getX() - 2, me.getY() - 2, 4, 4));
            if (f instanceof MouseListener) {
                ((MouseListener) f).mouseClicked(me);
            }
            if (me.isConsumed()) {
                return;
            }
            // If the mouse event hasn't been consumed, it means that the user
            // double clicked on an area that didn't contain an editable fig.
            // in this case, scan through the list and start editing the first 
            // fig with editable text.  This allows us to remove the editable 
            // box clarifier outline, and just outline the whole FigTextGroup, 
            // see issue 1048.
            for (Object o : this.getFigs()) {
                f = (Fig) o;
                if (f instanceof MouseListener && f instanceof FigText) {
                    if ( ((FigText) f).getEditable()) {
                        ((MouseListener) f).mouseClicked(me);
                    }
                }
            }
        }
        // TODO: 21/12/2008 dthompson mouseClicked(me) above consumes the 
        // mouse event internally, so I suspect that this line might not be 
        // necessary.
        me.consume();
    }
    
    /**
     * Checks to see if the given click point (Rectangle) hits
     * the FigTextGroup.
     * This overrides the FigGroup implementation of hit where the hit 
     * rectangle must hit a sub fig.
     * Now, we count a hit if the user clicks anywhere within the outer 
     * bounds of the FigTextGroup, see issue 5620.
     * This method is important for registering double clicks to edit text 
     * boxes in a FigTextGroup.
     * @param r The hit rectangle.
     * @return True if the given hit rectangle intersects or is contained by
     * the outer limits of this FigTextGroup.
     * @see org.tigris.gef.presentation.FigGroup#hit(java.awt.Rectangle)
     */
    public boolean hit(Rectangle r) {
        return this.intersects(r);
    }
    
    /** 
     * Returns true if the bounds of the group contain the given point. 
     * Used to check if a mouse click occurs within the FigTextGroup.
     * Overrides the FigGroup implementation of contains where the
     * x, y point must hit a sub fig.
     * Now we count a hit if the user clicks anywhere within the outer
     * bounds of the FigTextGroup, see issue 5620.
     * This method is important for handling mousePressed events including
     * the start of mouse drags.
     * @param x The x coordinate of the point to test.
     * @param y The y coordinate of the point to test.
     * @return True if the given point is within the outer limits of this
     * FigTextGroup.
     * @see org.tigris.gef.presentation.FigGroup#contains(int, int)
     */
    public boolean contains(int x, int y) {
        return (_x <= x) && (x <= _x + _w) && (_y <= y) && (y <= _y + _h);
    }
}
