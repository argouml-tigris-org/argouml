/* $Id$
 *******************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *    Michiel van der Wulp
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */
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

package org.argouml.uml.diagram.use_case.ui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.Action;

import org.argouml.model.Model;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.ActionAddExtensionPoint;
import org.argouml.uml.diagram.ui.ActionAddNote;
import org.argouml.uml.diagram.ui.ActionCompartmentDisplay;
import org.argouml.uml.diagram.ui.FigCompartment;
import org.argouml.uml.diagram.ui.FigCompartmentBox;
import org.argouml.uml.diagram.ui.FigExtensionPointsCompartment;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;

/**
 * A fig to display use cases on use case diagrams.<p>
 *
 * Realized as a solid oval containing the name of the use
 * case. Optionally may be split into two compartments, with the lower
 * compartment displaying the extension points for the use case.<p>
 *
 * Implements all interfaces through its superclasses.<p>
 *
 * There is some coordinate geometry to be done to fit rectangular
 * text boxes inside an ellipse, and to draw a horizontal line 
 * at any height within the ellipse, touching the ellipse. 
 * In the following, we start from a coordinate 
 * system with the center at the center of the ellipse.
 * The rectangular text box contains the
 * name and any extension points if shown, and is deemed to be of
 * height <em>2h</em> and width <em>2w</em>. We allow a margin of
 * <em>p</em> above the top and below the bottom of the box, so we
 * know the height of the ellipse, <em>2b</em> = <em>2h</em> +
 * <em>2p</em>.<p>
 *
 * The formula for an ellipse of width <em>2a</em> and height
 * <em>2b</em>, centered on the origin, is<p>
 *
 * <em>x</em>^2/<em>a</em>^2 + <em>y</em>^2/<em>b</em>^2 = 1.<p>
 * or:<p>
 * x²/a² + y²/b² = 1<p>
 *
 * We know that a corner of the rectangle is at coordinate
 * (<em>w</em>,<em>h</em>), since the rectangle must also be centered
 * on the origin to fit within the ellipse. Substituting these values
 * for <em>x</em> and <em>y</em> in the formula above, we can compute
 * <em>a</em>, half the width of the ellipse, since we know
 * <em>b</em>.<p>
 *
 * <em>a</em> = <em>wb</em>/sqrt(<em>b</em>^2 - <em>h</em>^2).<p>
 *
 * But <em>b</em> was defined in terms of the height of the rectangle
 * plus agreed padding at the top, so we can write.<p>
 *
 * <em>a</em> = (<em>wh</em> + <em>wb</em>)/
 *                 sqrt(2<em>hp</em> + <em>p</em>^2)<p>
 *
 * Given we now know <em>a</em> and <em>b</em>, we can find the
 * coordinates of any partition line required between use case name
 * and extension points.<p>
 *
 * Finally we need to transform our coordinates, to recognise that the
 * origin is at our top left corner, and the Y coordinates are
 * reversed.<p>
 */
public class FigUseCase extends FigCompartmentBox {

    /**
     * The minimum padding allowed above the rectangle for
     * the use case name and extension points to the top of the use
     * case oval itself.
     */
    private static final int MIN_VERT_PADDING = 4;

    /**
     * The Fig for the extensionPoints compartment (if any).
     */
    private FigExtensionPointsCompartment extensionPointsFigCompartment;
    
    /**
     * Initialization which is common to multiple constructors.<p>
     * 
     * There should be no size calculations here, nor color setting,
     * since not all attributes are set yet (like e.g. fill color).
     */
    private void initialize(Rectangle bounds) {       
        enableSizeChecking(false);
        setSuppressCalcBounds(true); 
        
        FigExtensionPointsCompartment epc =
            /* Side effect: This creates the fig: */
            getExtensionPointsCompartment();

        /*
         * A use case has an external separator.
         * External means external to the compartment box. 
         * This horizontal line sticks out of the box, 
         * and touches the ellipse edge.
         */
        Fig separatorFig = epc.getSeparatorFig();
        
        /* TODO: This next line prevent loading a UseCase 
         * with a stereotype to grow. Why? */
        getStereotypeFig().setVisible(true);
        
        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(getNameFig());
        // stereotype fig covers the name fig:
        addFig(getStereotypeFig());
        addFig(epc);
        addFig(separatorFig);

        // Make all the parts match the main fig
        setFilled(true);
        super.setFillColor(FILL_COLOR);
        super.setLineColor(LINE_COLOR);
        super.setLineWidth(LINE_WIDTH);
        
        // by default, do not show extension points:
        setExtensionPointsVisible(false);

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
        /* Use arbitrary dimensions for now. */
        Fig b = new FigMyCircle(0, 0, 100, 60);
        b.setFilled(true);
        b.setFillColor(FILL_COLOR);
        b.setLineColor(LINE_COLOR);
        b.setLineWidth(LINE_WIDTH);
        return b;
    }

    /**
     * Construct a use case figure with the given owner, bounds, and rendering 
     * settings.  This constructor is used by the PGML parser.
     * 
     * @param owner owning model element
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigUseCase(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initialize(bounds);
    }

    /**
     * Build a collection of menu items relevant for a right-click
     * popup menu on a Use Case.<p>
     *
     * Adds to the generic pop up items from the parent.<p>
     *
     * @param me  The mouse event that generated this popup.
     *
     * @return    A collection of menu items
     */
    @Override
    public Vector getPopUpActions(MouseEvent me) {
        /* Check if multiple items are selected: */
        boolean ms = TargetManager.getInstance().getTargets().size() > 1;

        // Get the parent vector first
        Vector popUpActions = super.getPopUpActions(me);

        // Add menu to add an extension point or note. Placed one before last,
        // so the "Properties" entry is always last.
        ArgoJMenu addMenu = new ArgoJMenu("menu.popup.add");

        if (!ms) {
            addMenu.add(ActionAddExtensionPoint.singleton());
        }
        addMenu.add(new ActionAddNote());

        popUpActions.add(popUpActions.size() - getPopupAddOffset(), addMenu);

        // Modifier menu. Placed one before last, so the "Properties" entry is
        // always last.
        popUpActions.add(popUpActions.size() - getPopupAddOffset(),
                buildModifierPopUp(LEAF | ROOT));

        return popUpActions;
    }

    /**
     * Show menu to display/hide the extension point compartment.
     * @return the menu
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#buildShowPopUp()
     */
    @Override
    protected ArgoJMenu buildShowPopUp() {
        ArgoJMenu showMenu = super.buildShowPopUp();
        Iterator i = ActionCompartmentDisplay.getActions().iterator();
        while (i.hasNext()) {
            showMenu.add((Action) i.next());
        }
        return showMenu;
    }

    /**
     * USED BY PGML.tee.
     * @return the class name and bounds together with compartment
     * visibility.
     */
    @Override
    public String classNameAndBounds() {
        return super.classNameAndBounds()
                + "extensionPointVisible=" + isExtensionPointsVisible();
    }

    public boolean isExtensionPointsVisible() {
        return extensionPointsFigCompartment != null 
            && extensionPointsFigCompartment.isVisible();
    }

    /**
     * Set the visibility of the extension point compartment. This is
     * called from outside this class when the user sets visibility
     * explicitly through the style panel or the context sensitive
     * pop-up menu.<p>
     *
     * We don't change the size of the use case, so we just have to
     * mark the extension point elements' visibility.
     * {@link #setBounds(int, int, int, int)} will do the relayout
     * (with name in the middle) for us.<p>
     *
     * @param isVisible  <code>true</code> if the compartment should be shown,
     *                   <code>false</code> otherwise.
     */
    public void setExtensionPointsVisible(boolean isVisible) {
        setCompartmentVisible(extensionPointsFigCompartment, isVisible);
    }

    /**
     * Creates a set of handles for dragging generalization/specializations
     *   or associations.<p>
     *
     * @return  The new selection object (a GEF entity).
     */
    @Override
    public Selection makeSelection() {
        return new SelectionUseCase(this);
    }

    /**
     * Compute the dimensions of an ellipse that intersects the 4 corners 
     * of the given box.
     * 
     * @param box the width and height of the box
     * @return the dimension of the ellipse
     */
    public Dimension addCompartmentBoxSurroundings(Dimension box) {
        containerBox = box;
        
        @SuppressWarnings("unused")
        double h = box.height;
        double w = box.width;

        int padding = Math.max((int) (w / 10.0), MIN_VERT_PADDING);
        return calcEllipse(box, padding);
    }

    /**
     * A private utility to calculate the bounding oval for the given
     * rectangular text box.<p>
     *
     * To sufficiently constrain the problem, we define that there is a gap
     * given by the parameter <code>vertPadding</code> above the top of the
     * box to the top of the oval.<p>
     *
     * All computations are done in double, and then converted to integer at
     * the end.<p>
     *
     * @param rectSize     The dimensions of the rectangle to be bounded
     *
     * @param vertPadding  The padding between the top of the box and the top
     *                     of the ellipse.
     *
     * @return             The dimensions of the required oval.
     */
    private Dimension calcEllipse(Dimension rectSize, int vertPadding) {

        // Work out the radii of the ellipse, a and b. The top right corner of
        // the ellipse (Cartesian coordinates, centered on the origin) will be
        // at (x,y)

        double a;
        double b = rectSize.height / 2.0 + vertPadding;

        double x = rectSize.width / 2.0;
        double y = rectSize.height / 2.0;

        // Formula for a is described in the overall class description.

        a = (x * b) / Math.sqrt(b * b - y * y);

        // Result as integers, rounded up. We ensure that the radii are
        // integers for convenience.

        return new Dimension(((int) (Math.ceil(a) + getLineWidth()) * 2),
			     ((int) (Math.ceil(b) + getLineWidth()) * 2));
    }

    @Override
    protected Rectangle calculateCompartmentBoxDimensions(
            int x, int y, int w, int h) {
        /* For an ellipse, we can put the box in the middle:  */
        return new Rectangle(
                x + (w - containerBox.width) / 2, 
                y + (h - containerBox.height) / 2, 
                containerBox.width, 
                containerBox.height);
    }

    @Override
    protected void setCompartmentBounds(FigCompartment c, 
            Rectangle cb, Rectangle ob) {
        Rectangle r = new Rectangle();
        r.y = cb.y;
        r.height = getLineWidth();
        r.width = (int) (2.0 * (calcX(
                ob.width / 2.0,
                ob.height / 2.0,
                ob.height / 2.0 - (cb.y - ob.y))));
        r.x = cb.x + cb.width / 2 - r.width / 2;

        c.setExternalSeparatorFigBounds(r);            
        c.setBounds(cb.x, cb.y, cb.width, cb.height);
    }

    /**
     * Private utility routine to work out the (positive) x coordinate of a
     * point on an oval, given the radii and y coordinate.<p>
     * TODO: Use this to calculate the separator lines!
     *
     * @param a  radius in X direction
     * @param b  radius in Y direction
     * @param y  Y coordinate
     * @return   Positive X coordinate for the given Y coordinate
     */
    private double calcX(double a, double b, double y) {
        assert a > 0;
        assert b > 0;
        assert b > y;
        return (a * Math.sqrt(b * b - y * y)) / b;
    }

    /**
     * Set the fill colour for the use case oval.<p>
     *
     * This involves setting the fill color of all figs, but not the bigPort.
     * Calling the super method would cause all FigGroup elements
     * to follow suit - which is not wanted for the bigPort nor the separator.
     *
     * @param col  The colour desired.
     */
    @Override
    public void setFillColor(Color col) {
        getBigPort().setFillColor(col);
    }
    
    public Color getFillColor() {
        return getBigPort().getFillColor();
    }
    
    public boolean getFilled() {
        return getBigPort().isFilled();
    }
    
    public boolean isFilled() {
        return getBigPort().isFilled();
    }

    /**
     * Set whether the use case oval is to be filled.<p>
     *
     * This is overridden to have no effect as the use case is always filled
     * @param f this argument is ignored.
     */
    @Override
    public void setFilled(boolean f) {
        //
    }

    /**
     * FigMyCircle is a FigCircle with corrected connectionPoint method:
     *   this methods calculates where a connected edge ends.<p>
     *   
     *   TODO: Once we are at GEF version 0.13.1M4, this whole class can be 
     *   removed, since it was taken over by GEF.
     */
    public static class FigMyCircle extends FigCircle {
        /**
         * Constructor just invokes the parent constructor.<p>
         *
         * @param x       X coordinate of the upper left corner of the bounding
         *                box.
         *
         * @param y       Y coordinate of the upper left corner of the bounding
         *                box.
         *
         * @param w       Width of the bounding box.
         *
         * @param h       Height of the bounding box.
         *
         * @param lColor  Line colour of the fig.
         *
         * @param fColor  Fill colour of the fig.
         */
        public FigMyCircle(int x, int y, int w, int h,
			   Color lColor,
			   Color fColor) {
            super(x, y, w, h, lColor, fColor);
        }

        /**
         * Constructor just invokes the parent constructor.<p>
         *
         * @param x       X coordinate of the upper left corner of the bounding
         *                box.
         *
         * @param y       Y coordinate of the upper left corner of the bounding
         *                box.
         *
         * @param w       Width of the bounding box.
         *
         * @param h       Height of the bounding box.
         */
        public FigMyCircle(int x, int y, int w, int h) {
            super(x, y, w, h);
        }

        /**
         * Compute the border point of the ellipse that is on the edge
         *   between the stored upper left corner and the given parameter.<p>
         *   
         *   TODO: Once we are at GEF version 0.13.1M4, this method 
         *   and in fact the whole class can be 
         *   removed, since it was taken over by GEF in revision 1279.
         *
         * @param anotherPt  The remote point to which an edge is drawn.
         *
         * @return           The connection point on the boundary of the
         *                   ellipse.
         */
        @Override
        public Point connectionPoint(Point anotherPt) {
            double rx = _w / 2;
            double ry = _h / 2;
            double dx = anotherPt.x - (_x + rx);
            double dy = anotherPt.y - (_y + ry);
            double dd = ry * ry * dx * dx + rx * rx * dy * dy;
            double mu = rx * ry / Math.sqrt(dd);

            Point res =
		new Point((int) (mu * dx + _x + rx),
			  (int) (mu * dy + _y + ry));
            return res;
        }

    }

    /*
     * Use the code from the FigCircle, not the one from Fig.
     */
    @Override
    public Point connectionPoint(Point anotherPt) {
        return getBigPort().connectionPoint(anotherPt);
    }

    @Override
    protected void updateListeners(Object oldOwner, Object newOwner) {
        Set<Object[]> listeners = new HashSet<Object[]>();
        /* Let's register for events from all modelelements
         * that change the name or body text: 
         */
        if (newOwner != null) {
            /* Register for name changes, added extensionPoints
             * and abstract makes the text italic.
             * All Figs need to listen to "remove", too: */
            listeners.add(new Object[] {newOwner, 
                                new String[] {"remove", "name", "isAbstract", 
                                    "extensionPoint", "stereotype"}});
            
            // register for extension points:
            for (Object ep : Model.getFacade().getExtensionPoints(newOwner)) {
                listeners.add(new Object[] {ep, new String[] {"location", "name"}});
            }
            
            for (Object st : Model.getFacade().getStereotypes(newOwner)) {
                listeners.add(new Object[] {st, "name"});
            }
        }
        updateElementListeners(listeners);
    }

    @Override
    public void renderingChanged() {
        super.renderingChanged();
        if (getOwner() != null) {
            updateExtensionPoints();
        }
    }
    
    protected void updateExtensionPoints() {
        if (!isExtensionPointsVisible()) {
            return;
        }
        extensionPointsFigCompartment.populate();

        setBounds(getBounds());
        damage();
    }
    
    /**
     * @return the Fig for the extension point compartment
     */
    public FigExtensionPointsCompartment getExtensionPointsCompartment() {
        // Set bounds will be called from our superclass constructor before
        // our constructor has run, so make sure this gets set up if needed.
        if (extensionPointsFigCompartment == null) {
            extensionPointsFigCompartment = new FigExtensionPointsCompartment(
                    getOwner(),
                    DEFAULT_COMPARTMENT_BOUNDS, 
                    getSettings());
        }
        return extensionPointsFigCompartment;
    }

}
