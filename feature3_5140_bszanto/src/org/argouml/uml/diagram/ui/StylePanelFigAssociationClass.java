// $Id$
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
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;

import org.argouml.uml.diagram.static_structure.ui.StylePanelFigClass;
import org.tigris.gef.presentation.Fig;

/**
 * The style Panel for FigEdgeModelElement.
 *
 */
public class StylePanelFigAssociationClass extends StylePanelFigClass implements
        ItemListener, FocusListener, KeyListener {

    public StylePanelFigAssociationClass() {
    }

    /**
     * Bounding box is editable (although this is style panel for an
     * FigEdgeModelElement).
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
     * @see org.argouml.ui.StylePanelFig#refresh()
     */
    @Override
    public void refresh() {
        super.refresh();

        // The boundary box as held in the target fig, and as listed in
        // the
        // boundary box style field (null if we don't have anything
        // valid)
        Fig target = getPanelTarget();

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

} /* end class StylePanelFigAssociationClass */
