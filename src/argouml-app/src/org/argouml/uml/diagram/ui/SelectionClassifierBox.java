/* $Id$
 *******************************************************************************
 * Copyright (c) 2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Katharina Fahnenbruck
 *    Bob Tarling
 *******************************************************************************
 */

package org.argouml.uml.diagram.ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Icon;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.uml.diagram.static_structure.ui.FigClassifierBox;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.Handle;

/**
 * This class adds two buttons to the class-element. They are shown when the
 * mouse is over the selected element. The upper button is for adding new
 * attributes. The lower button is for adding new operations.
 * 
 * @author Katharina Fahnenbruck
 */
public abstract class SelectionClassifierBox extends
        SelectionNodeClarifiers2 {

    /** Upper right corner Handle */
    private static final int UPPER_RIGHT = 2;

    /** Lower right corner Handle */
    private static final int LOWER_RIGHT = 7;

    private static int[] buttonIds = {UPPER_RIGHT, LOWER_RIGHT};
    
    private static Icon addIcon = 
        ResourceLoaderWrapper.lookupIconResource("Add");

    private class Button {
        int handle;
        Icon icon;
        Fig fig;
        Object metaType;
        
        public Button(
                int handle, 
                Icon icon, 
                Fig fig,
                Object metaType) {
            this.handle = handle;
            this.fig = fig;
            this.icon = icon;
            this.metaType = metaType;
        }
    }
    
    private ArrayList<Button> buttons = new ArrayList<Button>(2);
    
    /**
     * @param f
     *            the given Fig
     */
    public SelectionClassifierBox(Fig f) {
       
        super(f);
        
        FigClassifierBox fcb = (FigClassifierBox) getContent();
        int i=0;
        for (FigCompartment compartment : fcb.getCompartments()) {
            buttons.add(new Button(buttonIds[i++], addIcon, compartment, compartment.getCompartmentType()));
        }
    }

    /**
     * Handle h: handle in which to return selected Handle information (output
     * parameter). A handle index of -1 indicates that the cursor is not over
     * any GEF handle. If the mouse is over the new buttons nevertheless the
     * index is -1. There the hitHandle() is overridden (see above)
     */
    @Override
    public void hitHandle(
            Rectangle cursor, Handle h) {
        super.hitHandle(cursor, h);

        int cx = getContent().getX();
        int cy = getContent().getY();
        int cw = getContent().getWidth();
        int ch = getContent().getHeight();

        // super returns -1 if any of GEFs buttons was hit
        // (but maybe one of the not-GEF-buttons)
        if (h.index == -1) {
            if (hitBelow(
                    cx + cw - (addIcon.getIconWidth() / 2), cy + 25,
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

    @Override
    public void mouseReleased(MouseEvent me) {
        for (Button button : buttons) {
            int cx = button.fig.getX() + button.fig.getWidth() - button.icon.getIconWidth();
            int cy = button.fig.getY();
            int cw = button.icon.getIconWidth();
            int ch = button.icon.getIconHeight();
            Rectangle rect = new Rectangle(cx, cy, cw, ch);
            if (rect.contains(me.getX(), me.getY())) {
                onButtonClicked(button.metaType);
                me.consume();
                return;
            }
        }
        
        super.mouseReleased(me);
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

        for (Button button : buttons) {
            if (button.fig.isVisible()) {
                int cx = button.fig.getX() + button.fig.getWidth() - button.icon.getIconWidth();
                int cy = button.fig.getY();
                paintButton(button.icon, g, cx, cy, button.handle);
            }
        }
    }
}
