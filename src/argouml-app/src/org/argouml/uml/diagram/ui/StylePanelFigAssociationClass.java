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

// Copyright (c) 2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JCheckBox;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.StylePanelFigNodeModelElement;
import org.tigris.gef.presentation.Fig;

/**
 * The style Panel for FigEdgeModelElement.
 *
 */
public class StylePanelFigAssociationClass
    extends StylePanelFigNodeModelElement
    implements ItemListener, FocusListener, KeyListener {

    private JCheckBox attrCheckBox =
        new JCheckBox(Translator.localize("checkbox.attributes"));

    private JCheckBox operCheckBox =
        new JCheckBox(Translator.localize("checkbox.operations"));

    /**
     * Flag to indicate that a refresh is going on.
     */
    private boolean refreshTransaction;

    /**
     * Constructor.
     */
    public StylePanelFigAssociationClass() {
        super();

        addToDisplayPane(attrCheckBox);
        addToDisplayPane(operCheckBox);

        attrCheckBox.setSelected(false);
        operCheckBox.setSelected(false);
        attrCheckBox.addItemListener(this);
        operCheckBox.addItemListener(this);
    }

    /**
     * Bounding box is editable (although this is style panel for an
     * FigEdgeModelElement).
     * 
     * @param value Ignored argument.
     */
    @Override
    protected void hasEditableBoundingBox(boolean value) {
        super.hasEditableBoundingBox(true);
    }

    /*
     * @see org.argouml.ui.StylePanelFig#setTargetBBox()
     */
    @Override
    protected void setTargetBBox() {
        Fig target = getPanelTarget();
        // Can't do anything if we don't have a fig.
        if (target == null) {
            return;
        }
        // Parse the boundary box text. Null is
        // returned if it is empty or
        // invalid, which causes no change. Otherwise we tell
        // GEF we are making
        // a change, make the change and tell GEF we've
        // finished.
        Rectangle bounds = parseBBox();
        if (bounds == null) {
            return;
        }

        // Get class box, because we will set it's bounding box
        Rectangle oldAssociationBounds = target.getBounds();
        if (((FigAssociationClass) target).getAssociationClass() != null) {
            target = ((FigAssociationClass) target).getAssociationClass();
        }

        if (!target.getBounds().equals(bounds)
                && !oldAssociationBounds.equals(bounds)) {
            target.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
            target.endTrans();
        }
    }

    /*
     * Only refresh the tab if the bounds propertyChange event arrives.
     *
     * @see org.argouml.ui.StylePanel#refresh(java.beans.PropertyChangeEvent)
     */
    public void refresh(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        if (propertyName.equals("bounds")) {
            refresh();
        }
    }
    /*
     * @see org.argouml.ui.StylePanelFig#refresh()
     */
    @Override
    public void refresh() {
        // StylePanelFigClass relies on getPanelTarget() to return a 
        // FigCompartmentBox
        refreshTransaction = true;
        FigAssociationClass panelTarget =
            (FigAssociationClass) getPanelTarget();
        try {
            super.refresh();
            final FigCompartmentBox fcb = panelTarget.getAssociationClass();
            if (fcb != null) {
                FigCompartment compartment =
                    fcb.getCompartment(Model.getMetaTypes().getAttribute());
                attrCheckBox.setSelected(compartment.isVisible());
                compartment =
                    fcb.getCompartment(Model.getMetaTypes().getOperation());
                operCheckBox.setSelected(compartment.isVisible());
            }
        } finally {
            refreshTransaction = false;
        }

        // The boundary box as held in the target fig, and as listed
        // in the boundary box style field (null if we don't have 
        // anything valid)
        Fig target = panelTarget;

        // Get class box, because we will set it's bounding box in text field
        if (((FigAssociationClass) target).getAssociationClass() != null) {
            target = ((FigAssociationClass) target).getAssociationClass();
        }

        Rectangle figBounds = target.getBounds();
        Rectangle styleBounds = parseBBox();

        // Only reset the text if the two are not the same (i.e the fig
        // has
        // moved, rather than we've just edited the text, when
        // setTargetBBox()
        // will have made them the same). Note that styleBounds could
        // be null,
        // so we do the test this way round.

        if (!(figBounds.equals(styleBounds))) {
            getBBoxField().setText(
                    figBounds.x + "," + figBounds.y + "," + figBounds.width
                            + "," + figBounds.height);
        }
    }

    ////////////////////////////////////////////////////////////////
    // event handling

    /*
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
        if (!refreshTransaction) {
            Object src = e.getSource();

            if (src == attrCheckBox) {
                FigCompartmentBox fcb = (FigCompartmentBox) getPanelTarget();
                fcb.showCompartment(Model.getMetaTypes().getAttribute(), 
                        attrCheckBox.isSelected());
            } else if (src == operCheckBox) {
                FigCompartmentBox fcb = (FigCompartmentBox) getPanelTarget();
                fcb.showCompartment(Model.getMetaTypes().getOperation(),
                        operCheckBox.isSelected());
            } else {
                super.itemStateChanged(e);
            }
        }
    }

} /* end class StylePanelFigAssociationClass */
