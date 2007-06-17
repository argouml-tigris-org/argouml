// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.notation.providers.NotationProvider;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.ActionAddExtensionPoint;
import org.argouml.uml.diagram.ui.ActionAddNote;
import org.argouml.uml.diagram.ui.ActionCompartmentDisplay;
import org.argouml.uml.diagram.ui.CompartmentFigText;
import org.argouml.uml.diagram.ui.ExtensionsCompartmentContainer;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * A fig to display use cases on use case diagrams.<p>
 *
 * Realised as a solid oval containing the name of the use
 * case. Optionally may be split into two compartments, with the lower
 * compartment displaying the extension points for the use case.<p>
 *
 * Implements all interfaces through its superclasses.<p>
 *
 * There is some coordinate geometry to be done to fit rectangular
 * text boxes inside an elipse. The rectangular text box contains the
 * name and any extension points if shown, and is deemed to be of
 * height <em>2h</em> and width <em>2w</em>. We allow a margin of
 * <em>p</em> above the top and below the bottom of the box, so we
 * know the height of the elipse, <em>2b</em> = <em>2h</em> +
 * <em>2p</em>.<p>
 *
 * The formula for an elipse of width <em>2a</em> and height
 * <em>2b</em>, centred on the origin, is<p>
 *
 * <em>x</em>^2/<em>a</em>^2 + <em>y</em>^2/<em>b</em>^2 = 1.<p>
 *
 * We know that a corner of the rectangle is at coordinate
 * (<em>w</em>,<em>h</em>), since the rectangle must also be centred
 * on the origin to fit within the elipse. Subsituting these values
 * for <em>x</em> and <em>y</em> in the formula above, we can compute
 * <em>a</em>, half the width of the elipse, since we know
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
public class FigUseCase extends FigNodeModelElement
    implements ExtensionsCompartmentContainer {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(FigUseCase.class);

    ///////////////////////////////////////////////////////////////////////////
    //
    // Constants
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * The minimum padding allowed above and below the rectangle for
     * the use case name and extension points to the top of the use
     * case oval itself.<p>
     */
    protected static final int MIN_VERT_PADDING = 4;

    /**
     * Space above and below the line separating name from extension
     * points. The line takes a further 1 pixel.<p>
     */
    protected static final int SPACER = 2;

    ///////////////////////////////////////////////////////////////////////////
    //
    // Instance variables
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * UML use cases do not really have ports, so just define one big
     * one so that users can drag edges to or from any point in the
     * icon.<p>
     */
    private FigMyCircle bigPort;

    /**
     * We don't use _bigPort for the actual graphics of the oval. We
     * define an identical oval that sits on top of it.<p>
     */
    private FigMyCircle cover;

    /**
     * The line separating name and extension points.<p>
     */
    private FigLine epSep;

    /**
     * The vector of graphics for extension points (if any). First one
     * is the rectangle for the entire extension points box.<p>
     */
    private FigGroup epVec;

    /**
     * The rectangle for the entire extension point box.<p>
     */
    private FigRect epBigPort;

    /**
     * Text highlighted by mouse actions on the diagram. Assumed to
     * belong to the extension point compartment.<p>
     */
    private CompartmentFigText highlightedFigText;

    ///////////////////////////////////////////////////////////////////////////
    //
    // Constructors
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Constructor for a new use case fig. We work out the smallest
     * oval that will fit round.<p>
     *
     * At creation the extension point box is not showing (for
     * consistency with existing implementations). We can show it
     * later.<p>
     */
    public FigUseCase() {

        // Create all the things we need, then use getMinimumSize to work out
        // the dimensions of the oval.

        // First the main port ellipse and the cover of identical size that
        // will realize it. Use arbitrary dimensions for now.

        bigPort = new FigMyCircle(0, 0, 100, 60, Color.black, Color.white);
        cover = new FigMyCircle(0, 0, 100, 60, Color.black, Color.white);

        // Mark the text, but not the box as filled, mark that the name may
        // use multiline text (a bit odd - how do we enter a multi-line
        // name?).

        getNameFig().setTextFilled(false);
        getNameFig().setFilled(false);
        getNameFig().setLineWidth(0);
        getNameFig().setReturnAction(FigText.END_EDITING);

        // The separator, again with arbitrary bounds for now.

        epSep = new FigLine(0, 30, 100, 100, Color.black);

        epSep.setVisible(false);

        // The surrounding box for the extension points, again with arbitrary
        // bounds for now (but made the same width as the name field, so the
        // name field width will dominate size calculations, but there is a
        // space to double click in for a new EP. It is not filled
        // (although we have to specify a fill color at creation), nor has it
        // a surrounding line. Its bounds, which allow for one line (which is
        // empty) are the same as for the name box at this stage.

        epBigPort =
	    new FigRect(0, 30, getNameFig().getBounds().width, 20,
			Color.black, Color.white);

        epBigPort.setFilled(false);
        epBigPort.setLineWidth(0);
        epBigPort.setVisible(false);

        // The group for the extension points. The first entry in the vector
        // is the overall surrounding box itself. The group is not filled, nor
        // has any line. The first entry we add is the epBigPort

        epVec = new FigGroup();

        epVec.setFilled(false);
        epVec.setLineWidth(0);
        epVec.setVisible(false);

        epVec.addFig(epBigPort);

        // We now use getMiniumSize to work out the dimensions of the ellipse
        // that we need, and then reset the bounds of everything.

        Dimension ellipse = getMinimumSize();

        // The size of the port and cover ellipses

        bigPort.setBounds(0, 0, ellipse.width, ellipse.height);
        cover.setBounds(0, 0, ellipse.width, ellipse.height);

        // Space for the name. Centred horizontally, and (since we are minimum
        // size) _MIN_VERT_PADDING from the top.

        Dimension nameSize = getNameFig().getMinimumSize();

        getNameFig().setBounds((ellipse.width - nameSize.width) / 2,
			       MIN_VERT_PADDING,
			       nameSize.width,
			       nameSize.height);

        getStereotypeFig().setBounds(0, 0, 0, 0);

        // The separator. We cheat here. Since the name and extension points
        // rectangles are the same size at this stage, this must be at the
        // midpoint of the elipse.

        epSep.setShape(0,
			ellipse.height / 2,
			ellipse.width,
			ellipse.height / 2);

        // The surrounding box for the extension points. At this stage we know
        // the separator is 1 pixel wide at the midpoint, and there is _SPACER
        // below this before the extension box.

        Dimension epSize = epBigPort.getMinimumSize();

        epBigPort.setBounds((ellipse.width - epSize.width) / 2,
			     ellipse.height / 2 + 1 + SPACER,
			     epSize.width,
			     epSize.height);

        setBigPort(bigPort);

        // add Figs to the FigNode in back-to-front order
        addFig(bigPort);
        addFig(cover);
        addFig(getNameFig());
        addFig(getStereotypeFig());
        addFig(epSep);
        addFig(epVec);

        updateExtensionPoint();
        
        // Having built the figure, getBounds finds the enclosing rectangle,
        // which we set as our bounds.
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }

    /**
     * A version of the constructor used to associated the Fig with a
     * particular model element object.<p>
     *
     * Used at creation time of a UseCase.
     * And also when Add to Diagram is activated.
     * However, the load routines use the main constructor and
     * call setOwner directly.<p>
     *
     * @param gm    The graph model to associate with this Fig. Ignored in this
     *              implementation.
     *
     * @param node  The model element object to associate with this Fig.
     */
    public FigUseCase(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /**
     * The text string to be used as the default name of the new use
     * case fig. However this seems in general to be immediately
     * overwritten - presumably somewhere in the creation code for the
     * object, which choses to define a name.<p>
     *
     * <em>Note</em>. Good UML would probably prefer a name starting
     * with a capital and no spaces!<p>
     *
     * @return  The desired text of the default name.
     */
    public String placeString() {
        return "new Use Case";
    }

    /**
     * Make a copy of the current fig.<p>
     *
     * Uses the generic superclass clone which gives a vector of all
     * the figs. Then initialize our instance variables from this
     * vector.<p>
     *
     * @return  A new copy of the the current fig.
     */
    public Object clone() {
        FigUseCase figClone = (FigUseCase) super.clone();
        Iterator it = figClone.getFigs().iterator();

        figClone.bigPort = (FigMyCircle) it.next();
        figClone.cover = (FigMyCircle) it.next();
        figClone.setNameFig((FigText) it.next());
        it.next();
        figClone.epSep = (FigLine) it.next();
        figClone.epVec = (FigGroup) it.next();

        return figClone;
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    // Fig accessors
    //
    ///////////////////////////////////////////////////////////////////////////

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

        popUpActions.insertElementAt(addMenu,
            popUpActions.size() - getPopupAddOffset());

        // Show menu to display/hide the extension point compartment.
        ArgoJMenu showMenu = new ArgoJMenu("menu.popup.show");
        Iterator i = ActionCompartmentDisplay.getActions().iterator();
        while (i.hasNext()) {
            showMenu.add((Action) i.next());
        }
        popUpActions.insertElementAt(showMenu,
            popUpActions.size() - getPopupAddOffset());

        // Modifier menu. Placed one before last, so the "Properties" entry is
        // always last.
        popUpActions.insertElementAt(
                buildModifierPopUp(LEAF | ROOT),
                popUpActions.size() - getPopupAddOffset());

        return popUpActions;
    }

    /**
     * USED BY PGML.tee.
     * @return the class name and bounds together with compartment
     * visibility.
     */
    public String classNameAndBounds() {
        return super.classNameAndBounds()
                + "extensionPointVisible=" + isExtensionPointVisible();
    }

    /**
     * Returns whether the extension points are currently displayed.<p>
     *
     * @return  <code>true</code> if the extensionpoints are visible,
     *          <code>false</code> otherwise.
     *
     * @see org.argouml.uml.diagram.ui.ExtensionsCompartmentContainer#isExtensionPointVisible()
     */
    public boolean isExtensionPointVisible() {
        return epVec.isVisible();
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
     *
     * @see org.argouml.uml.diagram.ui.ExtensionsCompartmentContainer#setExtensionPointVisible(boolean)
     */
    public void setExtensionPointVisible(boolean isVisible) {
        if (epVec.isVisible() && (!isVisible)) {
            setExtensionPointVisibleInternal(false);
        } else if ((!epVec.isVisible()) && isVisible) {
            setExtensionPointVisibleInternal(true);
        }
        /* Move the stereotype out of the way: */
        updateStereotypeText();
    }

    private void setExtensionPointVisibleInternal(boolean visible) {
        // Record our current bounds for later use
        Rectangle oldBounds = getBounds();

        // Tell GEF that we are starting to make a change. Loop through the
        // epVec marking each element as not visible.
        for (Fig fig : (List<Fig>) epVec.getFigs()) {
            fig.setVisible(visible);
        }

        // Mark the vector itself and the separator as not displayed
        epVec.setVisible(visible);
        epSep.setVisible(visible);

        // Redo the bounds and then tell GEF the change has finished
        setBounds(oldBounds.x, oldBounds.y,
                oldBounds.width,
                oldBounds.height);
        endTrans();
    }

    /**
     * Creates a set of handles for dragging generalization/specializations
     *   or associations.<p>
     *
     * @return  The new selection object (a GEF entity).
     */
    public Selection makeSelection() {
        return new SelectionUseCase(this);
    }

    /**
     * Compute the minimum acceptable size of the use case.<p>
     *
     * We work out the minimum size of the text box, and from that the radii
     *   of the enclosing ellipse.<p>
     *
     * @return  The dimensions of the smallest size bounding box of the use
     *          case.
     */
    public Dimension getMinimumSize() {

        Dimension textSize = getTextSize();

        Dimension size = calcEllipse(textSize, MIN_VERT_PADDING);

        return new Dimension(Math.max(size.width, 100),
			     Math.max(size.height, 60));
    }

    /**
     * A private utility routine to calculate the minimum size of the
     *   rectangle to hold the name and extension points (if displayed).<p>
     *
     * @return  The dimensions of the rectangle
     */
    private Dimension getTextSize() {
        Dimension minSize = getNameFig().getMinimumSize();

        // Now allow for the extension points, if they are displayed
        if (epVec.isVisible()) {

            // Allow for a separator (spacer each side + 1 pixel width line)
            minSize.height += 2 * SPACER + 1;

            // Loop through all the extension points, to find the widest
            List<CompartmentFigText> figs = getEPFigs();
            for (CompartmentFigText f : figs) {
                int elemWidth = f.getMinimumSize().width;
                minSize.width = Math.max(minSize.width, elemWidth);
            }

            // Height allows one row for each extension point 
            minSize.height += ROWHEIGHT * Math.max(1, figs.size());
        }

        return minSize;
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
        // the ellipse (Cartesian coordinates, centred on the origin) will be
        // at (x,y)

        double a;
        double b = rectSize.height / 2.0 + vertPadding;

        double x = rectSize.width / 2.0;
        double y = rectSize.height / 2.0;

        // Formula for a is described in the overall class description.

        a = (x * b) / Math.sqrt(b * b - y * y);

        // Result as integers, rounded up. We ensure that the radii are
        // integers for convenience.

        return new Dimension(((int) (Math.ceil(a)) * 2),
			     ((int) (Math.ceil(b)) * 2));
    }

    /**
     * Change the boundary of the use case.<p>
     *
     * If we are called with less than the minimum size, we impose the
     *   minimum size.<p>
     *
     * We place the name and extension points at the centre of the
     *   rectangle.<p>
     *
     * Set the bounds of all components of the Fig.<p>
     *
     * @param x  X coordinate of upper left corner
     *
     * @param y  Y coordinate of upper left corner
     *
     * @param w  width of bounding box
     *
     * @param h  height of bounding box
     */
    protected void setBoundsImpl(int x, int y, int w, int h) {

        // Remember where we are at present, so we can tell GEF later. Then
        // check we are as big as the minimum size
        Rectangle oldBounds = getBounds();
        Dimension minSize = getMinimumSize();

        int newW = (minSize.width > w) ? minSize.width : w;
        int newH = (minSize.height > h) ? minSize.height : h;

        // Work out the size of the name and extension point rectangle, and
        // hence the vertical padding
        Dimension textSize = getTextSize();
        int vPadding = (newH - textSize.height) / 2;

        // Adjust the alignment of the name.
        Dimension nameSize = getNameFig().getMinimumSize();

        getNameFig().setBounds(x + ((newW - nameSize.width) / 2),
			       y + vPadding,
			       nameSize.width,
			       nameSize.height);

        // Place extension points if they are showing
        if (epVec.isVisible()) {

            // currY tracks the current vertical position of each element. The
            // separator is _SPACER pixels below the name. Its length is
            // calculated from the formula for an ellipse.
            int currY = y + vPadding + nameSize.height + SPACER;
            int sepLen =
		2 * (int) (calcX(newW / 2.0,
				  newH / 2.0,
				  newH / 2.0 - (currY - y)));

            epSep.setShape(x + (newW - sepLen) / 2,
			    currY,
			    x + (newW + sepLen) / 2,
			    currY);

            // Extension points are 1 pixel for the line and _SPACER gap below
            // the separator
            currY += 1 + SPACER;

            // Use the utility routine getUpdatedSize to move the extension
            // point figures. We can discard the result of this routine. For
            // now we assume that extension points are the width of the overall
            // text rectangle (true unless the name is wider than any EP).
            updateFigGroupSize(epVec,
                    	   x + ((newW - textSize.width) / 2),
                    	   currY,
                    	   textSize.width,
                    	   (textSize.height - nameSize.height
                    	    - SPACER * 2 - 1));
        }

        // Set the bounds of the bigPort and cover
        bigPort.setBounds(x, y, newW, newH);
        cover.setBounds(x, y, newW, newH);

        // Record the changes in the instance variables of our parent, tell GEF
        // and trigger the edges to reconsider themselves.
        _x = x;
        _y = y;
        _w = newW;
        _h = newH;
        
        positionStereotypes();

        firePropChange("bounds", oldBounds, getBounds());
        updateEdges();
    }

    /**
     * Returns the new size of the FigGroup (either attributes or operations)
     * after calculation new bounds for all sub-figs, considering their minimal
     * sizes; FigGroup need not be displayed; no update event is fired.
     * TODO: This is a duplicate method from FigEditableCompartment
     * it should just be in one place.<p>
     *
     * This method has side effects that are sometimes used.
     *
     * @param fg
     *            the FigGroup to be updated
     * @param x
     *            x
     * @param y
     *            y
     * @param w
     *            w
     * @param h
     *            h
     * @return the new dimension
     */
    protected Dimension updateFigGroupSize(FigGroup fg, int x, int y, int w,
            int h) {
        int newW = w;
        int n = fg.getFigs().size() - 1;
        int newH =
            isCheckSize() ? Math.max(h, ROWHEIGHT * Math.max(1, n) + 2)
                : h;
        int step = (n > 0) ? (newH - 1) / n : 0;
        // width step between FigText objects int maxA =
        // Toolkit.getDefaultToolkit().getFontMetrics(LABEL_FONT)
        // .getMaxAscent();

        // set new bounds for all included figs
        Iterator figs = fg.iterator();
        Fig myBigPort = (Fig) figs.next();
        Fig fi;
        int fw, yy = y;
        while (figs.hasNext()) {
            fi = (Fig) figs.next();
            fw = fi.getMinimumSize().width;
            if (!isCheckSize() && fw > newW - 2) {
                fw = newW - 2;
            }
            fi.setBounds(x + 1, yy + 1, fw, Math.min(ROWHEIGHT, step) - 2);
            if (isCheckSize() && newW < fw + 2) {
                newW = fw + 2;
            }
            yy += step;
        }
        myBigPort.setBounds(x, y, newW, newH);
        // rectangle containing all following FigText objects
        fg.calcBounds();
        return new Dimension(newW, newH);
    }

    /**
     * Private utility routine to work out the (positive) x coordinate of a
     * point on an oval, given the radii and y coordinate.<p>
     *
     * @param a  radius in X direction
     * @param b  radius in Y direction
     * @param y  Y coordinate
     * @return   Positive X coordinate for the given Y coordinate
     */
    private double calcX(double a, double b, double y) {
        return (a * Math.sqrt(b * b - y * y)) / b;
    }

    /**
     * Set the line colour for the use case oval.<p>
     *
     * This involves setting the _cover oval, not the bigPort.<p>
     *
     * @param col The colour desired.
     */
    public void setLineColor(Color col) {
        cover.setLineColor(col);
    }

    /**
     * Get the line colour for the use case oval.<p>
     *
     * This involves getting the _cover oval colour, not the bigPort.<p>
     *
     * @return  The colour in use.
     */
    public Color getLineColor() {
        return cover.getLineColor();
    }

    /**
     * Set the fill colour for the use case oval.<p>
     *
     * This involves setting the _cover oval, not the bigPort.<p>
     *
     * @param col  The colour desired.
     */
    public void setFillColor(Color col) {
        cover.setFillColor(col);
    }

    /**
     * Get the line colour for the use case oval.<p>
     *
     * This involves getting the _cover oval colour, not the bigPort.<p>
     *
     * @return  The colour in use.
     */
    public Color getFillColor() {
        return cover.getFillColor();
    }

    /**
     * Set whether the use case oval is to be filled.<p>
     *
     * This involves setting the _cover oval, not the bigPort.<p>
     *
     * @param f  <code>true</code> if the oval is to be filled,
     *           <code>false</code> if not.
     */
    public void setFilled(boolean f) {
        cover.setFilled(f);
    }

    /**
     * Get whether the use case oval is to be filled.<p>
     *
     * This involves getting the _cover oval, not the bigPort.<p>
     *
     * @return  <code>true</code> if the oval is to be filled,
     *          <code>false</code> if not.
     */
    public boolean getFilled() {
        return cover.getFilled();
    }

    /**
     * Set the line width for the use case oval.<p>
     *
     * This involves setting the _cover oval, not the bigPort.<p>
     *
     * @param w  The line width desired.
     */
    public void setLineWidth(int w) {
        cover.setLineWidth(w);
    }

    /**
     * Get the line width for the use case oval.<p>
     *
     * This involves getting the _cover oval colour, not the bigPort.<p>
     *
     * @return  The line width set.
     */
    public int getLineWidth() {
        return cover.getLineWidth();
    }

    /**
     * FigMyCircle is a FigCircle with corrected connectionPoint method:
     *   this methods calculates where a connected edge ends.<p>
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
         * Compute the border point of the elipse that is on the edge
         *   between the stored upper left corner and the given parameter.<p>
         *
         * @param anotherPt  The remote point to which an edge is drawn.
         *
         * @return           The connection point on the boundary of the
         *                   elipse.
         */
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
            LOG.debug("    returns " + res.x + ',' + res.y + ')');
            return res;
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = 2616728355472635182L;
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    // Event handlers
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * React to a mouse key being pressed.<p>
     *
     * @param me  The mouse action that caused us to be invoked.
     */
    public void mousePressed(MouseEvent me) {

        // Deal with anything from the parent first
        super.mousePressed(me);

        // If we are currently selected, turn off the draggable buttons at each
        // side, and unhighlight any currently selected extension points.
        Editor ce = Globals.curEditor();
        Selection sel = ce.getSelectionManager().findSelectionFor(this);

        if (sel instanceof SelectionUseCase) {
            ((SelectionUseCase) sel).hideButtons();
        }

        unhighlight();

        // Display extension point properties if necessary. Look to see if the
        // mouse (2x2 pixels) hit the extension point compartment. Use a flag
        // to track this.
        Rectangle r = new Rectangle(me.getX() - 1, me.getY() - 1, 2, 2);
        Fig f = hitFig(r);


        if (f == epVec) {

            // Work out which extension point this corresponds to. Each EP
            // takes ROWHEIGHT pixels, so take the difference between the
            // centre of the mouse (me.getY() - 1) and the top of the epVec
            // (f.getY()) and integer divide by ROWHEIGHT.
            int i = (me.getY() - f.getY() - 1) / ROWHEIGHT;

            List<CompartmentFigText> figs = getEPFigs();
            
            // If we are in the range of the EP list size (avoids any nasty
            // boundary overflows), we can select that EP entry. Make this
            // entry the target Fig, and note that we do have a
            // target.             
            if ((i >= 0) && (i < figs.size())) {
                highlightedFigText = figs.get(i);
                highlightedFigText.setHighlighted(true);
            }
        }
    }

    /**
     * React to a mouse key being clicked.<p>
     *
     * @param me  The mouse action that caused us to be invoked.
     */
    public void mouseClicked(MouseEvent me) {
	super.mouseClicked(me);

	if (me.isConsumed()) {
	    return;
	}

	if (!isExtensionPointVisible() || me.getY() < epSep.getY1()) {
	    getNameFig().mouseClicked(me);
	} else if (me.getClickCount() >= 2
		   && !(me.isPopupTrigger()
			|| me.getModifiers() == InputEvent.BUTTON3_MASK)) {
	    createContainedModelElement(epVec, me);
	}
    }

    /**
     * Deal with the mouse leaving the fig. Unhighlight the fig.<p>
     *
     * @param me  The mouse action that caused us to be invoked.
     */
    public void mouseExited(MouseEvent me) {
        super.mouseExited(me);
        unhighlight();
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    // Internal methods
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Invoked when text has been edited.<p>
     *
     * We check that it is one of the extension point compartments and then
     * parse accordingly.<p>
     *
     * The parameter ft is the Fig with the text that has been edited.
     *
     * {@inheritDoc}
     */
    protected void textEdited(FigText ft) throws PropertyVetoException {
        // Let the parent do anything it wants first (in casu: the usecase name)
        super.textEdited(ft);

        // Only works if we have an owner
        Object useCase = getOwner();
        if (useCase == null) {
            return;
        }

        // Give up if we are not one of the extension points
        if (!epVec.getFigs().contains(ft)) {
            return;
        }

        // Parse the text
        CompartmentFigText hlft = (CompartmentFigText) ft;
        hlft.getNotationProvider().parse(hlft.getOwner(), ft.getText());
        ft.setText(hlft.getNotationProvider().toString(hlft.getOwner(), null));
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(
     *      org.tigris.gef.presentation.FigText)
     */
    protected void textEditStarted(FigText ft) {
        super.textEditStarted(ft);
        if (epVec.getFigs().contains(ft)) {
            showHelp(((CompartmentFigText) ft)
                    .getNotationProvider().getParsingHelp());
        }
    }

    /**
     * Create a new "feature" (extension point) in the use case fig.<p>
     *
     * Extension points are not strictly features, but that is a historical
     *   accident of naming. This creates a new entry in the extension point
     *   vector.<p>
     *
     * @param fg  The fig group to which this applies (which must be the
     *            extension point vector).
     *
     * @param ie  The input event that triggered us. In the current
     *            implementation a mouse double click.
     */
    protected void createContainedModelElement(FigGroup fg, InputEvent ie) {

        // Give up if we don't have an owner
        if (getOwner() == null) {
            return;
        }

        // Invoke the relevant action method to create an empty extension
        // point, then start the editor, assuming we successfully created an
        // extension point.
        ActionAddExtensionPoint.singleton().actionPerformed(null);

        CompartmentFigText ft =
            (CompartmentFigText) fg.getFigs().get(fg.getFigs().size() - 1);

        if (ft != null) {
            ft.startTextEditor(ie);
            ft.setHighlighted(true);

            highlightedFigText = ft;
        }
        ie.consume();
    }

    /**
     * Private utility to unhighlight any currently selected extension
     * point.<p>
     *
     * @return  The extension point that was unhighlighted.
     */
    private CompartmentFigText unhighlight() {

        // Loop through the vector of extension points, until we find a
        // highlighted one.
        for (CompartmentFigText ft : getEPFigs()) {
            if (ft.isHighlighted()) {
                ft.setHighlighted(false);
                highlightedFigText = null;
                return ft;
            }
        }

        // None were highlighted
        return null;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {

        // Let our superclass sort itself out first
        super.modelChanged(mee);
        if (mee instanceof AssociationChangeEvent 
                || mee instanceof AttributeChangeEvent) {
            renderingChanged();
            updateListeners(getOwner(), getOwner());
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object, java.lang.Object)
     */
    protected void updateListeners(Object oldOwner, Object newOwner) {
        if (oldOwner != null) {
            removeAllElementListeners();
        }
        /* Now, let's register for events from all modelelements
         * that change the name or body text: 
         */
        if (newOwner != null) {
            /* Register for name changes, added extensionpoints
             * and abstract makes the text italic.
             * All Figs need to listen to "remove", too: */
            addElementListener(newOwner, 
                    new String[] {"remove", "name", "isAbstract", 
                        "extensionPoint", "stereotype"});
            // register for extension points:
            Iterator it =
                Model.getFacade().getExtensionPoints(newOwner).iterator();
            while (it.hasNext()) {
                addElementListener(it.next(),
                        new String[] {"location", "name"});
            }
            it = Model.getFacade().getStereotypes(newOwner).iterator();
            while (it.hasNext()) {
                addElementListener(it.next(),
                        new String[] {"name"});
            }
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    public void renderingChanged() {
        if (getOwner() != null) {
            updateExtensionPoint();
        }
        super.renderingChanged();
    }

    /**
     * Updates the extensionpoints in the fig. <p>
     * 
     * A difference in behaviour of this function
     * compared to the similar 
     * FigEditableCompartment.populate()
     * is that the extensionpoints 
     * are not ordered, while features are.
     */
    protected void updateExtensionPoint() {
        // Give up if we have no owner
        Object useCase = getOwner();
        if (useCase == null) {
            return;
        }

        // Note our current bounds
        Rectangle oldBounds = getBounds();

        // Loop through all the extension points. epCount keeps track of the
        // fig's index as we go through the extension points.
        Collection eps =
	    Model.getFacade().getExtensionPoints(useCase);
        int epCount = 1;

        if ((eps != null) && (eps.size() > 0)) {
            int xpos = epBigPort.getX();
            int ypos = epBigPort.getY();

            // Take each EP and its corresponding fig in turn
            Iterator iter = eps.iterator();
            List<CompartmentFigText> figs = getEPFigs();
            List<CompartmentFigText> toBeRemoved = 
                new ArrayList<CompartmentFigText>(figs);

            while (iter.hasNext()) {
                CompartmentFigText epFig = null;
                Object ep = iter.next();

                /* Find the fig for this ep: */
                for (CompartmentFigText candidate : figs) {
                    if (candidate.getOwner() == ep) {
                        epFig = candidate;
                        break;
                    }
                }
                
                // If we don't have a fig for this EP, we'll need to add
                // one. We set the bounds, but they will be reset later.
                if (epFig == null) {
                    NotationProvider np = 
                        NotationProviderFactory2.getInstance()
                            .getNotationProvider(
                                NotationProviderFactory2.TYPE_EXTENSION_POINT, 
                                ep);

                    epFig = new CompartmentFigText(
                            xpos,
			    ypos + (epCount - 1) * ROWHEIGHT,
			    0,
			    ROWHEIGHT,
			    epBigPort,
                            np);
                    
                    epFig.setFilled(false);
                    epFig.setLineWidth(0);
                    epFig.setFont(getLabelFont());
                    epFig.setTextColor(Color.black);
                    epFig.setJustification(FigText.JUSTIFY_LEFT);
                    epFig.setReturnAction(FigText.END_EDITING);

                    epVec.addFig(epFig);
                } else {
                    /* This one is still useable, so let's not remove it: */
                    toBeRemoved.remove(epFig);
                }

                // Now put the text in
                // We must handle the case where the text is null
                String epText = epFig.getNotationProvider().toString(ep, null);
                if (epText == null) {
                    epText = "";
                }
                epFig.setText(epText);

                epCount++;
            }

            // Remove any spare figs we have if there are now fewer extension
            // points than figs
            for (Fig f : toBeRemoved) {
                epVec.removeFig(f);    
            }
        }

        // Now recalculate all the bounds, using our old bounds.
        setBounds(oldBounds.x, oldBounds.y, oldBounds.width, oldBounds.height);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        Object useCase = getOwner();
        if (useCase == null) {
            return;
        }
        Rectangle oldBounds = getBounds();
        // Now things to do with the use case itself. Put the use case in
        // italics if it is abstract, otherwise ordinary font.

        if (Model.getFacade().isAbstract(useCase)) {
            getNameFig().setFont(getItalicLabelFont());
        } else {
            getNameFig().setFont(getLabelFont());
        }
        super.updateNameText();
        setBounds(oldBounds.x, oldBounds.y, oldBounds.width, oldBounds.height);

    }

    /*
     * Makes sure that the edges stick to the elipse fig of the usecase. <p>
     * 
     * TODO: This function is called way too many times - I count 6x when
     * simply clicking on this usecase, and 20x when clicking on the button
     * on selection at the right hand side of a usecase.
     * Once this problem is solved, try increasing the "maxPoint" 4-fold,
     * to make edge attachment when dragging much smoother.
     *
     * @see org.tigris.gef.presentation.Fig#getGravityPoints()
     */
    public List getGravityPoints() {
        final int maxPoints = 30;
        List ret = new ArrayList(maxPoints);
        int cx = bigPort.getCenter().x;
        int cy = bigPort.getCenter().y;
        int radiusx = Math.round(bigPort.getWidth() / 2) + 1;
        int radiusy = Math.round(bigPort.getHeight() / 2) + 1;
        Point point = null;
        for (int i = 0; i < maxPoints; i++) {
            point =
                new Point((int) (cx
				 + (Math.cos(2 * Math.PI / maxPoints * i)
				    * radiusx)),
                    (int) (cy
				 + (Math.sin(2 * Math.PI / maxPoints * i)
				    * radiusy)));
            ret.add(point);
        }
        return ret;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     */
    protected void updateStereotypeText() {
        super.updateStereotypeText();
        if (getOwner() == null) {
            return;
        }
        positionStereotypes();
        damage();
    }
    
    private void positionStereotypes() {
        if (((FigGroup) getStereotypeFig()).getFigCount() > 0) {
            getStereotypeFig().setBounds(
        	    (getX() + getWidth() / 2
        		    - getStereotypeFig().getWidth() / 2),
        	    (getY() + bigPort.getHeight() + MIN_VERT_PADDING),
                    getStereotypeFig().getWidth(),
                    getStereotypeFig().getHeight());
        } else {
            getStereotypeFig().setBounds(0, 0, 0, 0);
        }
    }
    

    /**
     * Get a list of the extension point Figs <em>without</em> the first fig
     * which is the bigport fig.
     * 
     * @return a list of the extension point Figs
     */
    private List<CompartmentFigText> getEPFigs() {
        List<CompartmentFigText> l = 
            new ArrayList<CompartmentFigText>(epVec.getFigs());
        l.remove(0);
        return l;
    }


    /**
     * The UID.
     */
    private static final long serialVersionUID = -4018623737124023696L;
}
