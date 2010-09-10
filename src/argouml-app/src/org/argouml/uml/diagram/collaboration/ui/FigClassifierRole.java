/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
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

package org.argouml.uml.diagram.collaboration.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Iterator;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.UmlChangeEvent;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML classifier role in a  collaboration
 * diagram.<p>
 *
 * Stereotypes (if there are any) and name are displayed in the center of the
 * box.
 *
 * @author 10 Apr 2002. Jeremy Bennett (mail@jeremybennett.com). Modifications
 *         to ensure stereotypes are handled correctly.
 *
 * @author agauthie
 */
public class FigClassifierRole extends FigNodeModelElement {

    private static final int DEFAULT_HEIGHT = 50;

    private static final int DEFAULT_WIDTH = 90;

    /**
     * The minimum padding top and bottom.
     */
    private static final int PADDING = 5;

    /**
     * The fig that is used for the complete classifier role.
     * Identical in size to {@link FigNodeModelElement#bigPort}.<p>
     */
    private FigRect cover;

    /**
     * Construct a FigClassifierRole.
     * 
     * @param owner owning UML element
     * @param bounds position and size (size is ignored)
     * @param settings render settings
     */
    public FigClassifierRole(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initClassifierRoleFigs();
        if (bounds != null) {
            setLocation(bounds.x, bounds.y);
        }
    }

    @Override
    protected Fig createBigPortFig() {
        // The big port. Color of the big port is irrelevant
        return new FigRect(X0, Y0, DEFAULT_WIDTH, DEFAULT_HEIGHT,
                DEBUG_COLOR, DEBUG_COLOR);
    }

    /**
     * There should be no size calculations here, 
     * since not all attributes are set yet.
     */
    private void initClassifierRoleFigs() {
        // The cover. 
        cover = new FigRect(X0, Y0, DEFAULT_WIDTH, DEFAULT_HEIGHT, LINE_COLOR,
                FILL_COLOR);

        // The stereotype. Width is the same as the cover, height is its default
        // (since the font is not yet set). The text should be centered.

        getStereotypeFig().setLineWidth(0);
        getStereotypeFig().setVisible(true);
        //getStereotypeFig().setFilled(false);
        getStereotypeFig().setFillColor(DEBUG_COLOR);
        getStereotypeFig().setBounds(X0, Y0, 
                DEFAULT_WIDTH, getStereotypeFig().getHeight());

        // The name. Width is the same as the cover, height is the default.
        // The text of the name will be centered by
        // default. In the same place as the stereotype, since at this stage
        // the stereotype is not displayed. Being a classifier role it is
        // underlined

        getNameFig().setLineWidth(0);
        getNameFig().setReturnAction(FigText.END_EDITING);
        getNameFig().setFilled(false);
        getNameFig().setUnderline(true);

        getNameFig().setBounds(X0, Y0, 
                DEFAULT_WIDTH, getStereotypeFig().getHeight());

        // add Figs to the FigNode in back-to-front order

        addFig(getBigPort());
        addFig(cover);
        addFig(getStereotypeFig());
        addFig(getNameFig());
    }

    /*
     * The NotationProvider for the ClassifierRole. <p>
     * 
     * The syntax is for UML is:
     * <pre>
     * baselist := [base] [, base]*
     * classifierRole := [name] [/ role] [: baselist]
     * </pre></p>
     * 
     * The <code>name</code> is the Instance name, not used currently.
     * See ClassifierRoleNotationUml for details.<p>
     *
     * This syntax is compatible with the UML 1.4 specification.
     */
    @Override
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_CLASSIFIERROLE;
    }

    /**
     * Version of the clone to ensure all sub-figs are copied.<p>
     *
     * Uses the generic superclass clone which gives a list of all the
     * figs. Then initialize our instance variables from this list.<p>
     *
     * @return  A new copy of the the current fig.
     */
    @Override
    public Object clone() {
        FigClassifierRole figClone = (FigClassifierRole) super.clone();
        Iterator it = figClone.getFigs().iterator();

        figClone.setBigPort((FigRect) it.next());
        figClone.cover   = (FigRect) it.next();
        it.next();
        figClone.setNameFig((FigText) it.next());

        return figClone;
    }

    /**
     * Update the stereotype text.<p>
     *
     * If the stereotype text is non-existent, we must make sure it is
     * marked not displayed, and update the display accordingly.<p>
     *
     * Similarly if there is text, we must make sure it is marked
     * displayed.<p>
     */
    @Override
    protected void updateStereotypeText() {
        Rectangle rect = getBounds();

        int stereotypeHeight = 0;
        if (getStereotypeFig().isVisible()) {
            stereotypeHeight = getStereotypeFig().getHeight();
        }
        int heightWithoutStereo = getHeight() - stereotypeHeight;

        getStereotypeFig().populate();

        stereotypeHeight = 0;
        if (getStereotypeFig().isVisible()) {
            stereotypeHeight = getStereotypeFig().getHeight();
        }
        
        int minWidth = this.getMinimumSize().width;
        if (minWidth > rect.width) {
            rect.width = minWidth;
        }

        setBounds(
                rect.x,
                rect.y,
                rect.width,
                heightWithoutStereo + stereotypeHeight);
        calcBounds();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    @Override
    public void setLineColor(Color col) {
        cover.setLineColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    @Override
    public Color getLineColor() {
        return cover.getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    @Override
    public void setFillColor(Color col) {
        cover.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    @Override
    public Color getFillColor() {
        return cover.getFillColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    @Override
    public void setFilled(boolean f) {
        cover.setFilled(f);
    }


    @Override
    public boolean isFilled() {
        return cover.isFilled();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    @Override
    public void setLineWidth(int w) {
        cover.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    @Override
    public int getLineWidth() {
        return cover.getLineWidth();
    }


    /**
     * Work out the minimum size that this Fig can be.<p>
     *
     * This should be the size of the stereotype + name + padding. However
     * we allow for the possible case that the cover or big port could be
     * bigger still.<p>
     *
     * @return  The minimum size of this fig.
     */
    @Override
    public Dimension getMinimumSize() {

        Dimension stereoMin  = getStereotypeFig().getMinimumSize();
        Dimension nameMin    = getNameFig().getMinimumSize();

        Dimension newMin    = new Dimension(nameMin.width, nameMin.height);

        if (!(stereoMin.height == 0 && stereoMin.width == 0)) {
            newMin.width   = Math.max(newMin.width, stereoMin.width);
            newMin.height += stereoMin.height;
        }
        
        newMin.height += PADDING;

        return newMin;
    }


    /**
     * Override setBounds to keep shapes looking right.<p>
     *
     * Set the bounds of all components of the Fig. The stereotype (if any)
     * and name are centred in the fig.<p>
     *
     * We allow for the requested bounds being too small, and impose our
     * minimum size if necessary.<p>
     *
     * @param x  X coordinate of upper left corner
     *
     * @param y  Y coordinate of upper left corner
     *
     * @param w  width of bounding box
     *
     * @param h  height of bounding box
     *
     * @author 10 Apr 2002. Jeremy Bennett (mail@jeremybennett.com). Patch to
     *         allow for stereotype as well.
     */
    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {

        // In the rather unlikely case that we have no name, we give up.

        if (getNameFig() == null) {
            return;
        }

        // Remember where we are at present, so we can tell GEF later. Then
        // check we are as big as the minimum size

        Rectangle oldBounds = getBounds();
        Dimension minSize   = getMinimumSize();

        int newW = (minSize.width  > w) ? minSize.width  : w;
        int newH = (minSize.height > h) ? minSize.height : h;

        Dimension stereoMin = getStereotypeFig().getMinimumSize();
        Dimension nameMin   = getNameFig().getMinimumSize();

        // Work out the padding each side, depending on whether the stereotype
        // is displayed and set bounds accordingly

        int extraEach = (newH - nameMin.height - stereoMin.height) / 2;
        if (!(stereoMin.height == 0 && stereoMin.width == 0)) {
            /* At least one stereotype is visible */
            getStereotypeFig().setBounds(x, y + extraEach, newW, 
                    getStereotypeFig().getHeight());
        }
        getNameFig().setBounds(x, y + stereoMin.height + extraEach, newW,
                nameMin.height);

        // Set the bounds of the bigPort and cover

        getBigPort().setBounds(x, y, newW, newH);
        cover.setBounds(x, y, newW, newH);

        // Record the changes in the instance variables of our parent, tell GEF
        // and trigger the edges to reconsider themselves.

        _x = x;
        _y = y;
        _w = newW;
        _h = newH;

        firePropChange("bounds", oldBounds, getBounds());
        updateEdges();
    }

    @Override
    protected void updateLayout(UmlChangeEvent event) {
        super.updateLayout(event);
        if (event instanceof AddAssociationEvent
                || event instanceof AttributeChangeEvent) {
            // TODO: We need to be more specific here about what to build
            renderingChanged();
            // TODO: Is this really needed?
            damage();
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    @Override
    public Selection makeSelection() {
        return new SelectionClassifierRole(this);
    }

}
