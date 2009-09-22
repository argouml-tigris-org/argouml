// $Id$
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
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.static_structure.ui.SelectionClass;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;

/**
 * Class to display graphics for a node with compartments in a diagram.
 * <p>
 * 
 * The leaf descendants of this class shall add the compartments in top to
 * bottom order.
 * <p>
 * 
 * It adds a border around the box, and deals with highlighting editable
 * compartments.
 * <p>
 * 
 * All descendants of this class have the bigPort filled with the main fig fill
 * color, without border. The borderFig has a transparent fill, and a visible
 * border.
 * <p>
 * 
 * Why do we need a separate border fig: I (MVW) think because 
 * some figs may have parts protruding outside the box like a 
 * UML Package or like a UML Component.
 * 
 * TODO: Why is the fill not drawn by the borderFig? In the current 
 * situation, the border is drawn OVER the background fill (which 
 * won't work if colors have alpha channels). But the fill should only 
 * be drawn WITHIN the border. MVW: I propose to have the borderFig
 * show the fill color and have the bigPort be transparent and 
 * without border. Then we would need a drawing sequence change,
 * too. Or maybe the compartments should draw the fill color instead?
 * <p>
 * 
 * The name, keyword and stereotype are shown in transparent figs without
 * border, but their size is reduced so that they fit within the border of the
 * borderFig.
 */
public abstract class FigCompartmentBox extends FigNodeModelElement {

    private static final Logger LOG = Logger.getLogger(
            FigCompartmentBox.class);

    /**
     * Default bounds for a compartment.
     */
    protected static final Rectangle DEFAULT_COMPARTMENT_BOUNDS 
        = new Rectangle(
            X0, Y0 + 20 /* 20 = height of name fig ?*/, 
            WIDTH, ROWHEIGHT + 2 /* 2*LINE_WIDTH?  or extra padding? */ );
    
    /**
     * Text highlighted by mouse actions on the diagram.<p>
     */
    private static CompartmentFigText highlightedFigText = null;

    private Fig borderFig;

    private List<FigCompartment> compartments = 
        new ArrayList<FigCompartment>();

    /**
     * Initialization shared by all constructors.
     */
    private void initialize() {
        // Set properties of the stereotype box.
        // Initially not set to be displayed, but this will be changed
        // when we try to render it, if we find we have a stereotype.
        getStereotypeFig().setVisible(false);

        /*
         * The nameFig is transparent, since this is a box and the fill color is
         * drawn by the bigPort.
         */
        getNameFig().setFillColor(null);

        /*
         * The borderFig shows the outside border of the box around all
         * compartments. Its size always equals the bigPort. Its body is
         * transparent.
         */
        borderFig = new FigEmptyRect(X0, Y0, 0, 0);
        borderFig.setLineColor(LINE_COLOR);
        borderFig.setLineWidth(LINE_WIDTH);

        getBigPort().setLineWidth(0);
        /* The bigPort draws the background color: */
        getBigPort().setFillColor(FILL_COLOR);

        /*
         * TODO: The above means that the border is drawn OVER 
         * the background fill (which won't work if colors have 
         * alpha channels). But the fill should only be drawn WITHIN 
         * the border. MVW: I propose to have the borderFig show 
         * the fill color and have the bigPort be transparent and
         * without border.
         */
    }

    /**
     * Construct a Fig with owner, bounds, and settings.
     * 
     * @param owner the model element that owns this fig
     * @param bounds the rectangle defining the bounds
     * @param settings the rendering settings
     */
    public FigCompartmentBox(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initialize();
    }

    @Override
    public void addFig(Fig fig) {
        if (fig instanceof FigCompartment) {
            addCompartment((FigCompartment) fig);
        }
        super.addFig(fig);
    }

    private void addCompartment(FigCompartment c) {
        assert !compartments.contains(c);
        compartments.add(c);
    }

    protected int getVisibleCompartmentCount() {
        int result = 0;
        for (int i = 0; i < compartments.size(); i++) {
            result += compartments.get(i).isVisible() ? 1 : 0;
        }
        return result;
    }

    @Override
    public Dimension getMinimumSize() {
        // Use "aSize" to build up the minimum size. Start with the size of the
        // name compartment and build up.
        Dimension aSize = getNameFig().getMinimumSize();

        /*
         * Only take into account the stereotype width, not the height, since
         * the height is included in the name fig:
         */
        aSize = addChildWidth(aSize, getStereotypeFig());

        /* Add the height of all the compartments (if there are any),
         * and check their minimum width: */
        for (FigCompartment c : compartments) {
            aSize = addChildDimensions(aSize, c);
        }

        /* We want to maintain a minimum width for the fig. Also, add the border
         * dimensions to the minimum space required for its contents:
         */
        aSize.width = Math.max(WIDTH, aSize.width);
        aSize.width += 2 * getLineWidth();
        aSize.height += 2 * getLineWidth();

        return aSize;
    }

    /**
     * Sets the bounds, but the size will be at least the one returned by
     * {@link #getMinimumSize()}, unless checking of size is disabled.
     * <p>
     * 
     * If the required height is bigger, then the additional height is equally
     * distributed among all compartments, such that the accumulated height of
     * all visible figs equals the demanded height.
     * 
     * @param x Desired X coordinate of upper left corner
     * 
     * @param y Desired Y coordinate of upper left corner
     * 
     * @param width Desired width of the Fig
     * 
     * @param height Desired height of the Fig
     * 
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    @Override
    protected void setStandardBounds(final int x, final int y, final int w,
            final int h) {

        // Save our old boundaries so it can be used in property message later
        Rectangle oldBounds = getBounds();

        // Make sure we don't try to set things smaller than the minimum
        Dimension minimumSize = getMinimumSize();
        int newW = Math.max(w, minimumSize.width);
        int newH = Math.max(h, minimumSize.height);

        int currentHeight = 0;

        if (getStereotypeFig().isVisible()) {
            int stereotypeHeight = getStereotypeFig().getMinimumSize().height;
            getNameFig().setTopMargin(stereotypeHeight);
            getStereotypeFig().setBounds(x + getLineWidth(),
                    y + getLineWidth(), newW - 2 * getLineWidth(),
                    stereotypeHeight);
        } else {
            getNameFig().setTopMargin(0);
        }

        /* Now the new nameFig height will include the stereotype height: */
        Dimension nameMin = getNameFig().getMinimumSize();
        int minNameHeight = Math.max(nameMin.height, NAME_FIG_HEIGHT);

        getNameFig().setBounds(x + getLineWidth(), y + getLineWidth(),
                newW - 2 * getLineWidth(), minNameHeight);

        /* The new height can not be less than the name height: */
        /*
         * TODO: Is this needed/correct? 
         * For when all compartments are hidden?
         */
        newH = Math.max(minNameHeight + 2 * getLineWidth(), newH);

        currentHeight += minNameHeight;

        int requestedHeight = newH - currentHeight - 2 * getLineWidth();
        int neededHeight = 0;
        /* Calculate the minimum needed height for all the compartments:*/
        for (FigCompartment c : compartments) {
            if (c.isVisible()) {
                neededHeight += c.getMinimumSize().height;
            }
        }

        for (FigCompartment c : compartments) {
            if (c.isVisible()) {
                int compartmentHeight = c.getMinimumSize().height;
                if (requestedHeight > neededHeight) {
                    /*
                     * Distribute the extra height over the visible
                     * compartments:
                     */
                    compartmentHeight += (requestedHeight - neededHeight)
                            / getVisibleCompartmentCount();
                }
                c.setBounds(x + getLineWidth(), 
                        y + currentHeight + getLineWidth(), 
                        newW - 2 * getLineWidth(),
                        compartmentHeight);
                currentHeight += compartmentHeight;
            }
        }
        if (requestedHeight < neededHeight) {
            /* Increase the height of the fig: */
            newH += neededHeight - requestedHeight;
        }
        /*
         * If requested height equals needed height then do nothing; it fits
         * exactly.
         */

        /* Finally set the bounds of the big box and the border fig: */
        getBigPort().setBounds(x, y, newW, newH);
        getBorderFig().setBounds(x, y, newW, newH);

        // Now force calculation of the bounds of the figure, update the edges
        // and trigger anyone who's listening to see if the "bounds" property
        // has changed.

        calcBounds();
        updateEdges();
        LOG.debug("Bounds change : old - " + oldBounds + ", new - "
                + getBounds());
        firePropChange("bounds", oldBounds, getBounds());
    }

    /*
     * @see org.tigris.gef.presentation.Fig#translate(int, int)
     */
    @Override
    public void translate(int dx, int dy) {
        super.translate(dx, dy);
        Editor ce = Globals.curEditor();
        if (ce != null) {
            Selection sel = ce.getSelectionManager().findSelectionFor(this);
            if (sel instanceof SelectionClass) {
                ((SelectionClass) sel).hideButtons();
            }
        }
    }

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

        if (mouseEvent.isConsumed()) {
            return;
        }
        super.mouseClicked(mouseEvent);
        if (mouseEvent.isShiftDown()
                && TargetManager.getInstance().getTargets().size() > 0) {
            return;
        }

        Editor ce = Globals.curEditor();
        if (ce != null) {
            Selection sel = ce.getSelectionManager().findSelectionFor(this);
            if (sel instanceof SelectionClass) {
                ((SelectionClass) sel).hideButtons();
            }
        }
        unhighlight();

        Rectangle r =
            new Rectangle(
                mouseEvent.getX() - 1,
                mouseEvent.getY() - 1,
                2,
                2);

        Fig f = hitFig(r);
        if (f instanceof FigEditableCompartment) {
            FigEditableCompartment figCompartment = (FigEditableCompartment) f;
            f = figCompartment.hitFig(r);
            if (f instanceof CompartmentFigText) {
                if (highlightedFigText != null && highlightedFigText != f) {
                    highlightedFigText.setHighlighted(false);
                    if (highlightedFigText.getGroup() != null) {
                        /* Preventing NullPointerException. */
                        highlightedFigText.getGroup().damage();
                    }
                }
                ((CompartmentFigText) f).setHighlighted(true);
                highlightedFigText = (CompartmentFigText) f;
                TargetManager.getInstance().setTarget(f);
            }
        }
    }

    /**
     * Remove the highlight from the currently highlit FigText.
     *
     * @return the FigText that had highlight removed
     */
    protected CompartmentFigText unhighlight() {
        Fig fc;
        // Search all feature compartments for a text fig to unhighlight
        for (int i = 1; i < getFigs().size(); i++) {
            fc = getFigAt(i);
            if (fc instanceof FigEditableCompartment) {
                CompartmentFigText ft = 
                    unhighlight((FigEditableCompartment) fc);
                if (ft != null) {
                    return ft;
                }
            }
        }
        return null;
    }

    /**
     * Search the given compartment for a highlighted CompartmentFigText
     * and unhighlight it.
     * 
     * @param fc compartment to search for highlight item
     * @return item that was unhighlighted or null if no action was taken
     */
    protected final CompartmentFigText unhighlight(FigEditableCompartment fc) {
        Fig ft;
        for (int i = 1; i < fc.getFigs().size(); i++) {
            ft = fc.getFigAt(i);
            if (ft instanceof CompartmentFigText
                    && ((CompartmentFigText) ft).isHighlighted()) {
                ((CompartmentFigText) ft).setHighlighted(false);
                ft.getGroup().damage();
                return ((CompartmentFigText) ft);
            }
        }
        return null;
    }

    protected void createContainedModelElement(FigGroup fg, InputEvent ie) {
        if (!(fg instanceof FigEditableCompartment)) {
            return;
        }
        ((FigEditableCompartment) fg).createModelElement();
        /* Populate the compartment now, 
         * so that we can put the last one in edit mode: 
         * This fixes issue 5439. */
        ((FigEditableCompartment) fg).populate();
        // TODO: The above populate works but seems rather heavy here.
        // I can see something like this is needed though as events
        // won't manage this quick enough. Could we make
        // FigEditableCompartment.createModelElement() create
        // the new child Fig instance? It may also be useful
        // for it to return the new model element rather than
        // the current void return - Bob.
        List figList = fg.getFigs();
        if (figList.size() > 0) {
            Fig fig = (Fig) figList.get(figList.size() - 1);
            if (fig != null && fig instanceof CompartmentFigText) {
                if (highlightedFigText != null) {
                    highlightedFigText.setHighlighted(false);
                    if (highlightedFigText.getGroup() != null) {
                        /* Preventing NullPointerException. */
                        highlightedFigText.getGroup().damage();
                    }
                }
                CompartmentFigText ft = (CompartmentFigText) fig;
                ft.startTextEditor(ie);
                ft.setHighlighted(true);
                highlightedFigText = ft;
            }
        }
        ie.consume();
    }

    protected Fig getBorderFig() {
        return borderFig;
    }

    /**
     * This utility adds the size of a child component to an overall size. The
     * width is maximized with child's width and the child's height is added to
     * the overall height. If the child figure is not visible or not yet
     * created, it's size is not added.
     * 
     * @param size current dimensions - modified with the result
     * @param child child figure
     * @return new Dimension with child size added
     */
    protected static Dimension addChildDimensions(Dimension size, 
            Fig child) {
        if (child != null && child.isVisible()) {
            Dimension childSize = child.getMinimumSize();
            size.width = Math.max(size.width, childSize.width);
            size.height += childSize.height;
        }
        return size;
    }

    /**
     * This utility adds the width of a child component to an overall size. The
     * width is maximized with child's width and the child's height is ignored.
     * If the child figure is not visible, it's size is not added.
     * 
     * @param size current dimensions - modified with the result
     * @param child child figure
     * @return new Dimension with child width added
     */
    protected static Dimension addChildWidth(Dimension size, 
            Fig child) {
        if (child.isVisible()) {
            Dimension childSize = child.getMinimumSize();
            size.width = Math.max(size.width, childSize.width);
        }
        return size;
    }

    /**
     * @param compartment the compartment to be changed
     * @param isVisible true if the attribute compartment is visible
     *
     * @see org.argouml.uml.diagram.AttributesCompartmentContainer#setAttributesVisible(boolean)
     */
    protected void setCompartmentVisible(FigCompartment compartment,
            boolean isVisible) {
        Rectangle rect = getBounds();
        if (compartment.isVisible()) {
            if (!isVisible) { // hide compartment
                damage();
                for (Object f : compartment.getFigs()) {
                    ((Fig) f).setVisible(false);
                }
                compartment.setVisible(false);
                /*
                 * Hiding one compartment means that the Fig returns to minimal
                 * dimensions:
                 */
                Dimension aSize = getMinimumSize();
                setBounds(rect.x, rect.y, (int) aSize.getWidth(), (int) aSize
                        .getHeight());
                /*
                 * Alternatively, we could reduce the height of the Fig by the
                 * height of this one hidden compartment. But that would not be
                 * possible for the width - so we better return to minimal
                 * dimensions overall.
                 */
            }
        } else {
            if (isVisible) { // show compartment
                for (Object f : compartment.getFigs()) {
                    ((Fig) f).setVisible(true);
                }
                compartment.setVisible(true);
                Dimension aSize = this.getMinimumSize();
                setBounds(rect.x, rect.y,
                          (int) aSize.getWidth(), (int) aSize.getHeight());
                damage();
            }
        }
    }

    @Override
    public void setLineWidth(int w) {
        /* This sets the lineWidth of all in the group: */
        super.setLineWidth(w);
        /* NameFig and StereotypeFig are handled by parent. */
    }

    @Override
    public int getLineWidth() {
        return borderFig.getLineWidth();
    }

    @Override
    public void setFillColor(Color col) {
        super.setFillColor(col);
        getStereotypeFig().setFillColor(null);
        getNameFig().setFillColor(null);
    }

    @Override
    public Color getFillColor() {
        return getBigPort().getFillColor();
    }

    @Override
    public void setFilled(boolean f) {
        super.setFilled(f);
        getBigPort().setFilled(f);
        getNameFig().setFilled(false);
        getStereotypeFig().setFilled(false);
    }

    @Override
    protected void updateStereotypeText() {

        if (getOwner() == null) {
            return;
        }

        getStereotypeFig().setVisible(
                getStereotypeFig().getStereotypeCount() > 0);

        super.updateStereotypeText();

        if (getStereotypeFig().isVisible()) {
            getNameFig().setTopMargin(
                    getStereotypeFig().getMinimumSize().height);
        } else {
            getNameFig().setTopMargin(0);
        }

        /* TODO: Is this needed? */
        // forceRepaintShadow();
    }

}
