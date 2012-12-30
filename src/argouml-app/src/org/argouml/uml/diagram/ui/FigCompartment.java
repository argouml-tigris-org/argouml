/* $Id$
 *******************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *    Katharina Fahnenbruck
 *******************************************************************************
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

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.kernel.Project;
import org.argouml.model.Defaults;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;

/**
 * Presentation logic for a UML List Compartment. <p>
 *
 * The UML defines a Name Compartment, and a List Compartment.
 * This class implements the latter.<p>
 *
 * A List Compartment is a boxed compartment,
 * containing vertically stacked figs,
 * which is common to e.g. an operations
 * compartment and an attributes compartment.<p>
 *
 * The bigPort is filled with a border. All other figs contained
 * in this group may not be filled.<p>
 *
 * The size calculation done here supports vertically
 * stacked sub-figs of this group and supports all
 * compartment specializations.
 *
 * @author Bob Tarling
 */
public abstract class FigCompartment extends ArgoFigGroup {

    private static final Logger LOG =
        Logger.getLogger(FigCompartment.class.getName());

    private Fig bigPort;

    private static final int MIN_HEIGHT = FigNodeModelElement.NAME_FIG_HEIGHT;

    /**
     * A separator line that may be wider than the compartment.
     */
    private Fig externalSeparatorFig = new FigSeparator(X0, Y0, 11, LINE_WIDTH);

    /**
     * If true the last element will be editable when
     * the populate method completes.
     */
    private boolean editOnRedraw;

    private void constructFigs(int x, int y, int w, int h) {
        bigPort = new FigPort(X0, Y0, w, h);
        bigPort.setFilled(false);
        bigPort.setLineWidth(0);

        addFig(bigPort);
    }

    /**
     * Construct a new FigCompartment.
     *
     * @param owner owning UML element
     * @param bounds rectangle describing bounds of compartment
     * @param settings render settings
     */
    public FigCompartment(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, settings);
        constructFigs(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
     * If a boxed compartment is set to invisible then remove all its
     * children.
     * This is to save on resources and increase efficiency as multiple
     * figs need not exist and be resized, moved etc if they are not visible.
     * If a compartment is later made visible then its child figs are rebuilt
     * from the model.
     * {@inheritDoc}
     */
    @Override
    public void setVisible(boolean visible) {
        if (isVisible() == visible) {
            return;
        }
        super.setVisible(visible);
        if (externalSeparatorFig != null) {
            externalSeparatorFig.setVisible(visible);
        }
        if (visible) {
            populate();
        } else {
            for (int i = getFigs().size() - 1; i >= 0; --i) {
                Fig f = getFigAt(i);
                if (f instanceof CompartmentFigText) {
                    removeFig(f);
                }
            }
        }
    }

    @Override
    public void addFig(Fig fig) {
        if (fig != getBigPort()
                && !(fig instanceof CompartmentFigText)
                && !(fig instanceof FigSeparator)) {
            LOG.log(Level.SEVERE, "Illegal Fig added to a FigEditableCompartment");
            throw new IllegalArgumentException(
                    "A FigEditableCompartment can only "
                    + "contain CompartmentFigTexts, "
                    + "received a " + fig.getClass().getName());
        }
        super.addFig(fig);
    }



    /**
     * @return the bigPort
     */
    public Fig getBigPort() {
        return bigPort;
    }

    /**
     * The minimum width is the minimum width of the child with the widest
     * minimum width.
     * The minimum height is the total minimum height of all child figs.
     * @return the minimum width
     */
    @Override
    public Dimension getMinimumSize() {
        int minWidth = 0;
        int minHeight = 0;
        for (Fig fig : (Collection<Fig>) getFigs()) {
            if (fig.isVisible() && fig != getBigPort()) {
                int fw = fig.getMinimumSize().width;
                if (fw > minWidth) {
                    minWidth = fw;
                }
                minHeight += fig.getMinimumSize().height;
            }
        }

        minHeight += 2; // 2 Pixel padding after compartment

        minHeight = Math.max(minHeight, MIN_HEIGHT);

        return new Dimension(minWidth, minHeight);
    }

    @Override
    protected void setBoundsImpl(int x, int y, int w, int h) {
        Rectangle oldBounds = getBounds();

        Dimension minimumSize = getMinimumSize();
        int newW = Math.max(w, minimumSize.width);
        int newH = Math.max(h, minimumSize.height);

        int currentHeight = 0;

        for  (Fig fig : getFigs()) {
            if (fig.isVisible() && fig != getBigPort()) {
                int fh = fig.getMinimumSize().height;

                fig.setBounds(x, y + currentHeight, newW, fh);
                currentHeight += fh;
            }
        }
        getBigPort().setBounds(x, y, newW, newH);
        calcBounds();
        firePropChange("bounds", oldBounds, getBounds());
    }

    /**
     * Create a new model element for the compartment.
     */
    protected void createModelElement() {
        Project project = getProject();
        Defaults defaults = project.getDefaults();
        Object attr =
            Model.getUmlFactory().buildNode(
                    getCompartmentType(),
                    getOwner(),
                    null,
                    defaults);
        TargetManager.getInstance().setTarget(attr);
    }

    @Override
    public void setFilled(boolean f) {
        // Only the bigPort may be filled
        super.setFilled(false);
//        bigPort.setFilled(f);
    }

    @Deprecated //see parent
    @Override
    public boolean getFilled() {
        return isFilled();
    }

    @Override
    public boolean isFilled() {
        return bigPort.isFilled();
    }

    /**
     * This operation shall return a name unique for this type of
     * compartment. Potential use: show at the top in the compartment
     * as described in the UML, or as an identification string for
     * the compartment type. <p>
     * See UML 1.4.2 OMG, chapter 5.24.1.2: Compartment name.
     *
     * @return the name of the compartment
     */
    public abstract String getName();

    /**
     * Implemented in the subclass to indicate the primary type of model element
     * the compartment is designed to hold.
     * @return a model element type
     */
    public abstract Object getCompartmentType();

    /**
     * @return the collection of UML objects
     *              on which this compartment is based
     */
    protected abstract Collection getUmlCollection();

    /**
     * @return the type of the notationProvider
     *              used to handle the text in the compartment
     */
    protected abstract int getNotationType();

    /**
     * Fills the Fig by adding all figs within.
     */
    public void populate() {
        if (!isVisible()) {
            return;
        }

        int xpos = bigPort.getX();
        int ypos = bigPort.getY();

        List<CompartmentFigText> figs = getElementFigs();
        // We remove all of them:
        for (Fig f : figs) {
            removeFig(f);
        }

        // We are going to add the ones still valid & new ones
        // in the right sequence:
        FigSingleLineTextWithNotation comp = null;
        try {
            int acounter = -1;
            for (Object umlObject : getUmlCollection()) {
                comp = findCompartmentFig(figs, umlObject);
                acounter++;

                // TODO: Some of these magic numbers probably assume a line
                // width of 1.  Replace with appropriate constants/variables.

                // If we don't have a fig for this UML object, we'll need to add
                // one. We set the bounds, but they will be reset later.
                if (comp == null) {
                    comp = createFigText(umlObject, new Rectangle(
                            xpos + 1 /*?LINE_WIDTH?*/,
                            ypos + 1 /*?LINE_WIDTH?*/ + acounter
                            * ROWHEIGHT,
                            0,
                            ROWHEIGHT - 2 /*? 2*LINE_WIDTH? */),
                            getSettings());
                } else {
                    /* This one is still usable, so let's retain it, */
                    /* but its position may have been changed: */
                    Rectangle b = comp.getBounds();
                    b.y = ypos + 1 /*?LINE_WIDTH?*/ + acounter * ROWHEIGHT;
                    // bounds not relevant here, but I am perfectionist...
                    comp.setBounds(b);
                }
                /* We need to set a new notationprovider, since
                 * the Notation language may have been changed:  */
                comp.initNotationProviders();
                addFig(comp); // add it again (but now in the right sequence)

                // Now put the text in
                // We must handle the case where the text is null
                String ftText = comp.getNotationProvider().toString(umlObject,
                        comp.getNotationSettings());
                if (ftText == null) {
                    ftText = "";
                }
                comp.setText(ftText);

                comp.setBotMargin(0);
            }
        } catch (InvalidElementException e) {
            // TODO: It would be better here to continue the loop and try to
            // build the rest of the compartment. Hence try/catch should be
            // internal to the loop.
            LOG.log(Level.FINE, "Attempted to populate a FigEditableCompartment"
                    + " using a deleted model element - aborting", e);
        }

        if (comp != null) {
            comp.setBotMargin(6); // the last one needs extra space below it

            if (editOnRedraw) {
                comp.startTextEditor(null);
                editOnRedraw = false;
            }
        }
    }

    /**
     * Set the editOnRedraw state. When this mode is turned on the compartment
     * will place the last element in edit mode the next time the component
     * draws (typically as the result of some event such as having a new item
     * added)
     *
     * @param editOnRedraw
     */
    public void setEditOnRedraw(final boolean editOnRedraw) {
        this.editOnRedraw = editOnRedraw;
    }


    /**
     * @return null
     * @deprecated for 0.27.3 by tfmorris.  Subclasses must implement
     * {@link #createFigText(Object, Rectangle, DiagramSettings,
     * NotationProvider)}
     * which will become abstract in the future when this deprecated method is
     * removed.
     */
    @Deprecated
    protected FigSingleLineTextWithNotation createFigText(
            int x, int y, int w, int h, Fig aFig, NotationProvider np) {
        // No longer abstract to allow subclasses to remove, so we provide a
        // null default implementation
        return null;
    }

    /**
     * Factory method to create a FigSingleLineTextWithNotation
     * which must be implemented by all subclasses.
     * It will become abstract after the release of 0.28 to
     * enforce this requirement.
     *
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     * @param np notation provider
     * @return a FigSingleLineText which can be used to display the text.
     */
    @SuppressWarnings("deprecation")
    protected FigSingleLineTextWithNotation createFigText(Object owner,
            Rectangle bounds,
            @SuppressWarnings("unused") DiagramSettings settings,
            NotationProvider np) {

        // If this is not overridden it will revert to the old behavior
        // All internal subclasses have been updated, but this if for
        // compatibility of non-ArgoUML extensions.
        FigSingleLineTextWithNotation comp = createFigText(
                    bounds.x,
                    bounds.y,
                    bounds.width,
                    bounds.height,
                    this.getBigPort(),
                    np);
        comp.setOwner(owner);
        return comp;
    }

    /**
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings the render settings
     * @return a FigSingleLineText with notation provider
     *                  which can be used to display the text
     */
    abstract FigSingleLineTextWithNotation createFigText(Object owner,
            Rectangle bounds,
            DiagramSettings settings);

    /**
     * Returns the new size of the FigGroup (e.g. attributes or
     * operations) after calculation new bounds for all sub-figs,
     * considering their minimal sizes; FigGroup need not be
     * displayed; no update event is fired.<p>
     *
     * This method has side effects that are sometimes used.
     *
     * @param x x
     * @param y y
     * @param w w
     * @param h h
     * @return the new dimension
     */
    @SuppressWarnings("unused")
    public Dimension updateFigGroupSize(
                       int x,
                       int y,
                       int w,
                       int h,
                       boolean checkSize,
                       int rowHeight) {
        return getMinimumSize();
    }

    /* Find the compartment fig for this umlObject: */
    private CompartmentFigText findCompartmentFig(
            List<CompartmentFigText> figs,
            Object umlObject) {
        for (CompartmentFigText fig : figs) {
            if (fig.getOwner() == umlObject) {
                return fig;
            }
        }
        return null;
    }

    private List<CompartmentFigText> getElementFigs() {
        final List<CompartmentFigText> figs =
            new ArrayList<CompartmentFigText>(getFigs().size());

        for (Object f : getFigs()) {
            if (f instanceof CompartmentFigText) {
                figs.add((CompartmentFigText) f);
            }
        }
        return figs;
    }

    @Override
    public void setLineColor(Color col) {
        super.setLineColor(col);
        externalSeparatorFig.setFillColor(col);
    }

    @Override
    public void setLineWidth(int w) {
        super.setLineWidth(0);
        bigPort.setLineWidth(0);
        externalSeparatorFig.setHeight(w);
    }

    @Override
    public void setFillColor(Color col) {
        super.setFillColor(col);
        externalSeparatorFig.setFillColor(getLineColor());
    }

    /**
     * Set new bounds for the external separator line (if it exists).
     *
     * @param r the new bounds
     */
    public void setExternalSeparatorFigBounds(Rectangle r) {
        externalSeparatorFig.setBounds(r);
    }

    /**
     * @return separator figure
     */
    public Fig getSeparatorFig() {
        return externalSeparatorFig;
    }

    /**
     * Fig representing a horizontal line separator for compartment. <p>
     *
     * This is a horizontal line, but implemented as a rectangle
     * filled with the line color, since using a FigLine would draw the line
     * around the start and end coordinates with a line width > 1.
     */
    private static class FigSeparator extends FigRect {
        /**
         * Constructor.
         *
         * @param x coordinate
         * @param y coordinate
         * @param len length of the line
         */
        FigSeparator(int x, int y, int len, int lineWidth) {
            super(x, y, len, lineWidth);
            setLineWidth(0);
            super.setFilled(true);
        }

        @Override
        public void setFilled(boolean filled) {
            // Override superclass to do nothing.
            // Fill property cannot be changed.
        }

        @Override
        public void setLineWidth(int width) {
            // Override superclass to do nothing.
            // Line width cannot be changed.
        }

    }

    /**
     * Fig representing a horizontal line separator for compartment. <p>
     *
     * This is a horizontal line, but implemented as a rectangle
     * filled with the line color, since using a FigLine would draw the line
     * around the start and end coordinates with a line width > 1.
     */
    private static class FigPort extends FigRect {
        /**
         * Constructor.
         *
         * @param x coordinate
         * @param y coordinate
         * @param len length of the line
         */
        FigPort(int x, int y, int len, int lineWidth) {
            super(x, y, len, lineWidth);
            super.setLineWidth(0);
            super.setFillColor(null);
            super.setFilled(false);
        }

        @Override
        public void setFilled(boolean filled) {
            // Override superclass to do nothing.
            // Fill property cannot be changed.
        }
        @Override
        public void setFillColor(Color color) {
            // Override superclass to do nothing.
            // Fill property cannot be changed.
        }
        @Override
        public void setLineWidth(int width) {
            // Override superclass to do nothing.
            // Line width property cannot be changed.
        }

        /**
         * The hit method in GEF {Fig.hit(Rectangle)} does not register a hit
         * inside the Fig if the FIg is not filled. When not filled only a hit
         * on the border is registered.
         *
         * This override is a workaround until GEF is fixed.
         *
         * We require GEF to all a Fig to be set so that filled = true but
         * have fill color as null (transparent). That way the base
         * functionality will work for us.
         *
         * @param r
         *                the rectangular hit area
         * @return true if the hit rectangle strikes this fig
         */
        public boolean hit(Rectangle r) {
            if (!isVisible() || !isSelectable()) {
                return false;
            }
            final int cornersHit =
                countCornersContained(r.x, r.y, r.width, r.height);
            return cornersHit > 0;
        }
    }
}
