/* $Id$
 *******************************************************************************
 * Copyright (c) 2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    katharina
 *******************************************************************************
 */

package org.argouml.uml.diagram.ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.Icon;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.diagram.static_structure.ui.FigClassifierBox;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.Handle;

/**
 * This class adds two buttons to the class-element. They are shown when the
 * mouse is over the selelected element. The upper button is for adding new
 * attributes. The lower button is for adding new operations.
 * 
 * @author katharina
 */
public abstract class SelectionNodeClarifierWithAttributeButtons extends
        SelectionNodeClarifiers2 {

    /** Upper right corner Handle */
    protected static final int UPPER_RIGHT = 2;

    /** Lower right corner Handle */
    protected static final int LOWER_RIGHT = 7;

    private int localPressedButton;

    private static Icon addIcon = 
        ResourceLoaderWrapper.lookupIconResource("Add");

    /**
     * @param f
     *            the given Fig
     */
    public SelectionNodeClarifierWithAttributeButtons(Fig f) {
       
        super(f);
    }

    /**
     * Handle h: handle in which to return selected Handle information (output
     * parameter). A handle index of -1 indicates that the cursor is not over
     * any GEF handle. If the mouse is over the new buttons nevertheless the
     * index is -1. There the hitHandle() is overridden (see above)
     */
    @Override
    public void hitHandle(Rectangle cursor, Handle h) {
        super.hitHandle(cursor, h);

        int cx = getContent().getX();
        int cy = getContent().getY();
        int cw = getContent().getWidth();
        int ch = getContent().getHeight();

        // super returns -1 if any of GEFs buttons was hit
        // (but maybe one of the not-GEF-buttons)
        if (h.index == -1) {
            if (hitBelow(cx + cw - (addIcon.getIconWidth() / 2), cy + 25,
                    addIcon.getIconWidth() + 5, addIcon.getIconHeight() + 3,
                    cursor)) {
                h.index = UPPER_RIGHT;
            } else if (hitAbove(cx + cw - (addIcon.getIconWidth() / 2),
                    cy + ch, addIcon.getIconWidth() + 3,
                    addIcon.getIconHeight() + 5,
                    cursor)) {
                h.index = LOWER_RIGHT;
            }
        }
    }

    /**
     * Handle h: handle in which to return selected Handle information (output
     * parameter). A handle index of -1 indicates that the cursor is not over
     * any GEF handle. If the mouse is over the new buttons nevertheless the
     * index is -1. There the hitHandle() is overridden.
     */
    public void mousePressed(MouseEvent me) {
        super.mousePressed(me);
        Handle h = new Handle(-1);
        hitHandle(me.getX(), me.getY(), 0, 0, h);

        // get the index of the pressed button
        localPressedButton = h.index;
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        super.mouseReleased(me);

        if (localPressedButton != UPPER_RIGHT
                && localPressedButton != LOWER_RIGHT) {
            return;
        }

        Handle h = new Handle(-1);
        hitHandle(me.getX(), me.getY(), 0, 0, h);

        // see if mouse was released on the same button
        if (localPressedButton == h.index) {

            if (localPressedButton == UPPER_RIGHT) {
                onButtonClicked(Model.getMetaTypes().getAttribute());
            }
            if (localPressedButton == LOWER_RIGHT) {
                onButtonClicked(Model.getMetaTypes().getOperation());
            }

            me.consume();
        }
    }
    
    /**
     * Adds new element to the class
     */
    private void onButtonClicked(Object metaType) {
        FigClassifierBox fcb = (FigClassifierBox) getContent();
        FigCompartment fc = fcb.getCompartment(metaType);
        fc.setEditOnRedraw(true);
        fc.createModelElement();
    }

    /*
     * @see org.tigris.gef.base.SelectionButtons#paintButtons(Graphics)
     */
    @Override
    public void paintButtons(Graphics g) {

        super.paintButtons(g);

        int cx = getContent().getX();
        int cy = getContent().getY();
        int cw = getContent().getWidth();
        int ch = getContent().getHeight();

        paintButtonLeft(addIcon, g, cx + cw, cy + 35, UPPER_RIGHT);
        paintButtonLeft(addIcon, g, cx + cw, cy + ch - 10, LOWER_RIGHT);
    }
}
