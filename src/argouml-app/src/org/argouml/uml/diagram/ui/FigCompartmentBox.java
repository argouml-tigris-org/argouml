// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
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
import java.util.List;

import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.static_structure.ui.SelectionClass;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;

/**
 * Class to display graphics for a node with compartments in a diagram.<p>
 * 
 * It adds a border around the box, 
 * and deals with highlighting editable compartments. <p>
 *
 * Note that the upper line of the name box will be blanked out
 * if there is eventually a stereotype above.
 */
public abstract class FigCompartmentBox extends FigNodeModelElement {

    /**
     * Default bounds for a compartment.
     */
    protected static final Rectangle DEFAULT_COMPARTMENT_BOUNDS = new Rectangle(
            X0, Y0 + 20, WIDTH, ROWHEIGHT + 2);
    
    /**
     * Text highlighted by mouse actions on the diagram.<p>
     */
    private static CompartmentFigText highlightedFigText = null;

    private Fig borderFig;
    
    /**
     * Constructor.
     * @deprecated for 0.27.3 by tfmorris.  Use 
     * {@link #FigCompartmentBox(Object, Rectangle, DiagramSettings)}
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigCompartmentBox() {
        super();
        initialize();
    }

    /**
     * Initialization shared by all constructors.
     */
    private void initialize() {
        // Set properties of the stereotype box. Make it 1 pixel higher than
        // before, so it overlaps the name box, and the blanking takes out both
        // lines. Initially not set to be displayed, but this will be changed
        // when we try to render it, if we find we have a stereotype.
        getStereotypeFig().setFilled(true);
        getStereotypeFig().setLineWidth(1);
        // +1 to have 1 pixel overlap with getNameFig()
        getStereotypeFig().setHeight(STEREOHEIGHT + 1);

        // The outside border of the box around all compartments.
        borderFig = new FigEmptyRect(X0, Y0, 0, 0);
        borderFig.setLineWidth(1);

        getBigPort().setLineWidth(0);
        getBigPort().setFillColor(Color.white);
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

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#createContainedModelElement(
     *         org.tigris.gef.presentation.FigGroup,
     *         java.awt.event.InputEvent)
     */
    protected void createContainedModelElement(FigGroup fg, InputEvent ie) {
        if (!(fg instanceof FigEditableCompartment)) {
            return;
        }
        ((FigEditableCompartment) fg).createModelElement();
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
     * Add size of a child component to overall size.  Width is maximized
     * with child's width and child's height is added to the overall height.
     * If the child figure is not visible, it's size is not added.
     * 
     * @param size current dimensions
     * @param child child figure
     * @return new Dimension with child size added
     */
    protected Dimension addChildDimensions(Dimension size, Fig child) {
        if (child.isVisible()) {
            Dimension childSize = child.getMinimumSize();
            size.width = Math.max(size.width, childSize.width);
            size.height += childSize.height;
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
            if (!isVisible) {  // hide compartment
                damage();
                for (Object f : compartment.getFigs()) {
                    ((Fig) f).setVisible(false);
                }
                compartment.setVisible(false);
                Dimension aSize = this.getMinimumSize();
                setBounds(rect.x, rect.y,
                          (int) aSize.getWidth(), (int) aSize.getHeight());
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
}
