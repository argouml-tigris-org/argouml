// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: FigUseCase.java
// Classes: FigUseCase
// Original Author: your email address here
// $Id$

// 8 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to support
// the display of extension points.


package org.argouml.uml.diagram.use_case.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.use_cases.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;

import org.apache.log4j.Category;
import org.argouml.application.api.*;
import org.argouml.model.uml.UmlHelper;
import org.argouml.ui.*;
import org.argouml.uml.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.generator.*;


/**
 * <p>A fig to display use cases on use case diagrams.</p>
 *
 * <p>Realised as a solid oval containing the name of the use case. Optionally
 *   may be split into two compartments, with the lower compartment displaying
 *   the extension points for the use case.</p>
 *
 * <p>Implements all interfaces through its superclasses.</p>
 *
 * <p>There is some coordinate geometry to be done to fit rectangular text
 *   boxes inside an elipse. The rectangular text box contains the name and any
 *   extension points if shown, and is deemed to be of height <em>2h</em> and
 *   width <em>2w</em>. We allow a margin of <em>p</em> above the top and below
 *   the bottom of the box, so we know the height of the elipse, <em>2b</em> =
 *   <em>2h</em> + <em>2p</em>.</p>
 *
 * <p>The formula for an elipse of width <em>2a</em> and height <em>2b</em>,
 *   centred on the origin, is</p>
 *
 * <p><em>x</em>^2/<em>a</em>^2 + <em>y</em>^2/<em>b</em>^2 = 1.</p>
 *
 * <p>We know that a corner of the rectangle is at coordinate
 *   (<em>w</em>,<em>h</em>), since the rectangle must also be centred on the
 *   origin to fit within the elipse. Subsituting these values for <em>x</em>
 *   and <em>y</em> in the formula above, we can compute <em>a</em>, half the
 *   width of the elipse, since we know <em>b</em>.</p>
 *
 * <p><em>a</em> = <em>wb</em>/sqrt(<em>b</em>^2 - <em>h</em>^2).</p>
 *
 * <p>But <em>b</em> was defined in terms of the height of the rectangle plus
 *   agreed padding at the top, so we can write.</p>
 *
 * <p><em>a</em> = (<em>wh</em> + <em>wb</em>)/
 *                 sqrt(2<em>hp</em> + <em>p</em>^2)</p>
 *
 * <p>Given we now know <em>a</em> and <em>b</em>, we can find the coordinates
 *   of any partition line required between use case name and extension
 *   points.</p>
 *
 * <p>Finally we need to transform our coordinates, to recognise that the
 *   origin is at our top left corner, and the Y coordinates are reversed.</p>
 */

public class FigUseCase extends FigNodeModelElement {


    ///////////////////////////////////////////////////////////////////////////
    //
    // Constants
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>The minimum padding allowed above and below the rectangle for the use
     *   case name and extension points to the top of the use case oval
     *   itself.</p> */

    protected final int _MIN_VERT_PADDING = 4;


    /**
     * <p>Space above and below the line separating name from extension
     *   points. The line takes a further 1 pixel.</p>
     */

    protected final int _SPACER = 2;


    ///////////////////////////////////////////////////////////////////////////
    //
    // Instance variables
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>UML use cases do not really have ports, so just define one big one
     *   so that users can drag edges to or from any point in the icon.</p>
     */

    protected FigMyCircle _bigPort;


    /** 
     * <p>We don't use _bigPort for the actual graphics of the oval. We define
     *   an identical oval that sits on top of it.</p>
     */

    protected FigMyCircle _cover;


    /** 
     * <p>The line separating name and extension points.</p>
     */

    protected FigLine _epSep;


    /**
     * <p>The vector of graphics for extension points (if any). First one is
     *   the rectangle for the entire extension points box.</p> */

    protected FigGroup _epVec;


    /**
     * <p>The rectangle for the entire extension point box.</p>
     */

    protected FigRect _epBigPort;


    /**
     * <p>Text highlighted by mouse actions on the diagram. Assumed to belong
     *   to the extension point compartment.</p>
     */

    protected CompartmentFigText _highlightedFigText = null;


    ///////////////////////////////////////////////////////////////////////////
    //
    // Constructors
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Constructor for a new use case fig. We work out the smallest oval
     *   that will fit round.</p>
     *
     * <p>At creation the extension point box is not showing (for consistency
     *   with existing implementations). We can show it later.</p> */

     public FigUseCase() {

         // Create all the things we need, then use getMinimumSize to work out
         // the dimensions of the oval.

         // First the main port ellipse and the cover of identical size that
         // will realize it. Use arbitrary dimensions for now.

         _bigPort = new FigMyCircle(0, 0, 100, 60, Color.black, Color.white);
         _cover   = new FigMyCircle(0, 0, 100, 60, Color.black, Color.white);

         // Mark the text, but not the box as filled, mark that the name may
         // use multiline text (a bit odd - how do we enter a multi-line
         // name?).

         _name.setTextFilled(false);
         _name.setFilled(false);
         _name.setLineWidth(0);
         _name.setMultiLine(true);

         // The separator, again with arbitrary bounds for now.

         _epSep = new FigLine(0, 30, 100, 100, Color.black);

         _epSep.setDisplayed(false);

         // The surrounding box for the extension points, again with arbitrary
         // bounds for now (but made the same width as the name field, so the
         // name field width will dominate size calculations, but there is a
         // space to double click in for a new EP. It is not filled
         // (although we have to specify a fill color at creation), nor has it
         // a surrounding line. Its bounds, which allow for one line (which is
         // empty) are the same as for the name box at this stage.
         
         _epBigPort = new FigRect(0, 30, _name.getBounds().width, 20,
                                  Color.black, Color.white);

         _epBigPort.setFilled(false);
         _epBigPort.setLineWidth(0);
         _epBigPort.setDisplayed(false);

         // The group for the extension points. The first entry in the vector
         // is the overall surrounding box itself. The group is not filled, nor
         // has any line. The first entry we add is the epBigPort

         _epVec = new FigGroup();

         _epVec.setFilled(false);
         _epVec.setLineWidth(0);
         _epVec.setDisplayed(false);

         _epVec.addFig(_epBigPort);

         // We now use getMiniumSize to work out the dimensions of the ellipse
         // that we need, and then reset the bounds of everything.

         Dimension ellipse = getMinimumSize();

         // The size of the port and cover ellipses

         _bigPort.setBounds(0, 0, ellipse.width, ellipse.height);
         _cover.setBounds(0, 0, ellipse.width, ellipse.height);

         // Space for the name. Centred horizontally, and (since we are minimum
         // size) _MIN_VERT_PADDING from the top.

         Dimension nameSize = _name.getMinimumSize();

         _name.setBounds((ellipse.width - nameSize.width)/2, _MIN_VERT_PADDING,
                         nameSize.width, nameSize.height);

         // The separator. We cheat here. Since the name and extension points
         // rectangles are the same size at this stage, this must be at the
         // midpoint of the elipse.

         _epSep.setShape(0, ellipse.height/2, ellipse.width, ellipse.height/2);

         // The surrounding box for the extension points. At this stage we know
         // the separator is 1 pixel wide at the midpoint, and there is _SPACER
         // below this before the extension box.

         Dimension epSize = _epBigPort.getMinimumSize();

         _epBigPort.setBounds((ellipse.width - epSize.width)/2,
                              ellipse.height/2 + 1 + _SPACER, epSize.width,
                              epSize.height);

         // add Figs to the FigNode in back-to-front order

         addFig(_bigPort);
         addFig(_cover);
         addFig(_name);
         addFig(_epSep);
         addFig(_epVec);

         // Having built the figure, getBounds finds the enclosing rectangle,
         // which we set as our bounds.

         Rectangle r = getBounds();
         setBounds(r.x, r.y, r.width, r.height);
     }


    /**
     * <p>A version of the constructor used to associated the Fig with a
     *   particular NSUML object.</p>
     *
     * <p>Not clear that this is ever used. The load routines use the main
     *   constructor and call setOwner directly.</p>
     *
     * @param gm    The graph model to associate with this Fig. Ignored in this
     *              implementation.
     *
     * @param node  The NSUML object to associate with this Fig.
     */

    public FigUseCase(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }


    /**
     * <p>The text string to be used as the default name of the new use case
     *   fig. However this seems in general to be immediately overwritten -
     *   presumably somewhere in the creation code for the object, which choses
     *   to define a name.</p>
     *
     * <p><em>Note</em>. Good UML would probably prefer a name starting with a
     *   capital and no spaces!</p>
     *
     * @return  The desired text of the default name.
     */

    public String placeString() {
        return "new Use Case";
    }


    /**
     * <p>Make a copy of the current fig.</p>
     *
     * <p>Uses the generic superclass clone which gives a vector of all the
     *   figs. Then initialize our instance variables from this vector.</p>
     *
     * @return  A new copy of the the current fig.
     */

    public Object clone() {
        FigUseCase figClone = (FigUseCase) super.clone();
        Vector     allFigs  = figClone.getFigs();

        figClone._bigPort = (FigMyCircle) allFigs.elementAt(0);
        figClone._cover   = (FigMyCircle) allFigs.elementAt(1);
        figClone._name    = (FigText) allFigs.elementAt(2);
        figClone._epSep   = (FigLine) allFigs.elementAt(3);
        figClone._epVec   = (FigGroup) allFigs.elementAt(4);
        
        return figClone;
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Fig accessors
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Build a collection of menu items relevant for a right-click popup
     *   menu on a Use Case.</p>
     *
     * <p>Adds to the generic pop up items from the parent.</p>
     *
     * @param me  The mouse event that generated this popup.
     *
     * @return    A collection of menu items
     */

    public Vector getPopUpActions(MouseEvent me) {

        // Get the parent vector first

        Vector popUpActions = super.getPopUpActions(me);

        // Add menu to add an extension point or note. Placed one before last,
        // so the "Properties" entry is always last.

        JMenu addMenu = new JMenu("Add");

        addMenu.add(ActionAddExtensionPoint.singleton());
        addMenu.add(ActionAddNote.SINGLETON);

        popUpActions.insertElementAt(addMenu, popUpActions.size() - 1);

        // Show menu to display/hide the extension point compartment. Placed
        // one before last, so the "Properties" entry is always last.

        JMenu showMenu = new JMenu("Show");

        if (_epVec.isDisplayed()) {
            showMenu.add(ActionCompartmentDisplay.HideExtPointCompartment);
        }
        else {
            showMenu.add(ActionCompartmentDisplay.ShowExtPointCompartment);
        }

        popUpActions.insertElementAt(showMenu, popUpActions.size() - 1);

        // Modifier menu. Placed one before last, so the "Properties" entry is
        // always last.

        ArgoJMenu modifierMenu = new ArgoJMenu("Modifiers");

        MUseCase useCase = (MUseCase) getOwner();

        modifierMenu.addCheckItem(new ActionModifier("Abstract",
                                                     "isAbstract",
                                                     "isAbstract",
                                                     "setAbstract",
                                                     useCase));

        modifierMenu.addCheckItem(new ActionModifier("Leaf",
                                                     "isLeaf",
                                                     "isLeaf",
                                                     "setLeaf",
                                                     useCase));

        modifierMenu.addCheckItem(new ActionModifier("Root",
                                                     "isRoot",
                                                     "isRoot",
                                                     "setRoot",
                                                     useCase));

        popUpActions.insertElementAt(modifierMenu, popUpActions.size() - 1);

        return popUpActions;
    }


    /**
     * <p>Returns whether the extension points are currently displayed.</p>
     *
     * @return  <code>true</code> if the attributes are visible,
     *          <code>false</code> otherwise.
     */

    public boolean isExtensionPointVisible() {
        return _epVec.isDisplayed();
    }


    /**
     * <p>Set the visibility of the extension point compartment. This is called
     *   from outside this class when the user sets visibility explicitly
     *   through the style panel or the context sensitive pop-up menu.</p>
     *
     * <p>We don't change the size of the use case, so we just have to mark the
     *   extension point elements' visibility. {@link #setBounds(int, int,
     *   int, int)} will do the relayout (with name in the middle) for us.</p>
     *
     * @param isVisible  <code>true</code> if the compartment should be shown,
     *                   <code>false</code> otherwise.
     */

    public void setExtensionPointVisible(boolean isVisible) {

        // Record our current bounds for later use

        Rectangle oldBounds = getBounds();

        // First case is where the extension points are currently displayed and
        // we are asked to turn them off.

        if (_epVec.isDisplayed() & (!isVisible)) {

            // Tell GEF that we are starting to make a change. Loop through the
            // epVec marking each element as not visible.

            startTrans();

            Enumeration enum = _epVec.getFigs().elements();

            while (enum.hasMoreElements()) {
                ((Fig)(enum.nextElement())).setDisplayed(false);
            }

            // Mark the vector itself and the separator as not displayed

            _epVec.setDisplayed(false);
            _epSep.setDisplayed(false);

            // Redo the bounds and then tell GEF the change has finished

            setBounds(oldBounds.x, oldBounds.y, oldBounds.width,
                      oldBounds.height);
            endTrans();
        }

        // Second case is where the extension points are not currently
        // displayed and we are asked to turn them on.

        else if ((!_epVec.isDisplayed()) & isVisible) {

            // Tell GEF that we are starting to make a change. Loop through the
            // epVec marking each element as visible.

            startTrans();

            Enumeration enum = _epVec.getFigs().elements();

            while (enum.hasMoreElements()) {
                ((Fig)(enum.nextElement())).setDisplayed(true);
            }

            // Mark the vector itself and the separator as displayed

            _epVec.setDisplayed(true);
            _epSep.setDisplayed(true);

            // Redo the bounds and then tell GEF the change has finished

            setBounds(oldBounds.x, oldBounds.y, oldBounds.width,
                      oldBounds.height);
            endTrans();
        }
    }


    /**
     * <p>Creates a set of handles for dragging generalization/specializations
     *   or associations.</p>
     *
     * @return  The new selection object (a GEF entity).
     */

    public Selection makeSelection() {
        return new SelectionUseCase(this);
    }


    /**
     * <p>Associate this fig with a particular NSUML object.</p>
     *
     * <p>Associates the node with the "bigPort" that is the whole of this
     *   object.</p>
     *
     * <p>Must be public, since called directly, e.g. by the load routines.</p>
     *
     * @param node  The NSUML object to associate with this fig.
     */

    public void setOwner(Object node) {
        super.setOwner(node);
        bindPort(node, _bigPort);
    }


    /**
     * <p>Compute the minimum acceptable size of the use case.</p>
     *
     * <p>We work out the minimum size of the text box, and from that the radii
     *   of the enclosing ellipse.</p>
     *
     * @return  The dimensions of the smallest size bounding box of the use
     *          case.
     */

     public Dimension getMinimumSize() {

         Dimension textSize = _getTextSize();

         return _calcEllipse(textSize, _MIN_VERT_PADDING);
     }


    /**
     * <p>A private utility routine to calculate the minimum size of the
     *   rectangle to hold the name and extension points (if displayed).</p>
     *
     * @return  The dimensions of the rectangle
     */

    private Dimension _getTextSize() {
         Dimension minSize = _name.getMinimumSize();

         // Now allow for the extension points, if they are displayed

         if (_epVec.isDisplayed()) {

             // Allow for a separator (spacer each side + 1 pixel width line)

             minSize.height += 2*_SPACER + 1;

             // Loop through all the extension points, to find the widest
             // (remember the first fig is the box for the whole lot, so ignore
             // it).

            Enumeration enum = _epVec.getFigs().elements();
            enum.nextElement();  // ignore

            while (enum.hasMoreElements()) {
                int elemWidth =
                    ((FigText)enum.nextElement()).getMinimumSize().width;
                minSize.width = Math.max(minSize.width, elemWidth);
            }

            // Height allows one row for each extension point (remember to
            // ignore the first element, which is the box for the lot), subject
            // to there always being space for at least one extension point.
                                       
            minSize.height += ROWHEIGHT * 
                              Math.max(1, _epVec.getFigs().size() - 1);
         }

         return minSize;
    }


    /**
     * <p>A private utility to calculate the bounding oval for the given
     *   rectangular text box.</p>
     *
     * <p>To sufficiently constrain the problem, we define that there is a gap
     *   given by the parameter <code>vertPadding</code> above the top of the
     *   box to the top of the oval.</p>
     *
     * <p>All computations are done in double, and then converted to integer at
     *   the end.</p>
     *
     * @param rectSize     The dimensions of the rectangle to be bounded
     *
     * @param vertPadding  The padding between the top of the box and the top
     *                     of the ellipse.
     *
     * @return             The dimensions of the required oval */

    private Dimension _calcEllipse(Dimension rectSize, int vertPadding) {

        // Work out the radii of the ellipse, a and b. The top right corner of
        // the ellipse (Cartesian coordinates, centred on the origin) will be
        // at (x,y)

        double a;
        double b = ((double) rectSize.height)/2.0 + (double) vertPadding;

        double x = ((double) rectSize.width)/2.0;
        double y = ((double) rectSize.height)/2.0;

        // Formula for a is described in the overall class description.

        a = (x*b)/Math.sqrt(b*b - y*y);

        // Result as integers, rounded up. We ensure that the radii are
        // integers for convenience.

        return new Dimension(((int) (Math.ceil(a))*2),
                             ((int) (Math.ceil(b))*2));
    }
        

    /**
     * <p>Change the boundary of the use case.</p>
     *
     * <p>If we are called with less than the minimum size, we impose the
     *   minimum size.</p>
     *
     * <p>We place the name and extension points at the centre of the
     *   rectangle.</p>
     *
     * <p>Set the bounds of all components of the Fig.</p>
     *
     * @param x  X coordinate of upper left corner
     *
     * @param y  Y coordinate of upper left corner
     *
     * @param w  width of bounding box
     *
     * @param h  height of bounding box
     */

    public void setBounds(int x, int y, int w, int h) {

        // Remember where we are at present, so we can tell GEF later. Then
        // check we are as big as the minimum size

        Rectangle oldBounds = getBounds();
        Dimension minSize   = getMinimumSize();

        int newW = (minSize.width  > w) ? minSize.width  : w;
        int newH = (minSize.height > h) ? minSize.height : h;

        // Work out the size of the name and extension point rectangle, and
        // hence the vertical padding

        Dimension textSize = _getTextSize();
        int       vPadding = (newH - textSize.height)/2;

        // Adjust the alignment of the name.

        Dimension nameSize = _name.getMinimumSize();

        _name.setBounds(x + ((newW - nameSize.width)/2), y + vPadding,
                        nameSize.width, nameSize.height);

        // Place extension points if they are showing

        if (_epVec.isDisplayed()) {

            // currY tracks the current vertical position of each element. The
            // separator is _SPACER pixels below the name. Its length is
            // calculated from the formula for an ellipse.

            int currY  = y + vPadding + nameSize.height + _SPACER;
            int sepLen = 2 * (int) (_calcX(((double) newW)/2.0,
                                           ((double) newH)/2.0,
                                           ((double) newH)/2.0 -
                                             ((double) (currY - y))));

            _epSep.setShape(x + (newW - sepLen)/2, currY,
                            x + (newW + sepLen)/2, currY);

            // Extension points are 1 pixel for the line and _SPACER gap below
            // the separator

            currY += 1 + _SPACER;

            // Use the utility routine getUpdatedSize to move the extension
            // point figures. We can discard the result of this routine. For
            // now we assume that extension points are the width of the overall
            // text rectangle (true unless the name is wider than any EP).

            Dimension epSize =
                getUpdatedSize(_epVec, x + ((newW - textSize.width)/2),
                               currY, textSize.width,
                               textSize.height - nameSize.height -
                                 _SPACER*2 - 1); 
        }

        // Set the bounds of the bigPort and cover

        _bigPort.setBounds(x, y, newW, newH);
        _cover.setBounds(x, y, newW, newH);

        // Record the changes in the instance variables of our parent, tell GEF
        // and trigger the edges to reconsider themselves.

        _x = x;
        _y = y;
        _w = newW;
        _h = newH;
        
        firePropChange("bounds", oldBounds, getBounds());
        updateEdges();
    }
        

    /**
     * <p>Private utility routine to work out the (positive) x coordinate of a
     *   point on an oval, given the radii and y coordinate.</p>
     *
     * @param a  radius in X direction
     *
     * @param b  radius in Y direction
     *
     * @param y  Y coordinate
     *
     * @return   Positive X coordinate for the given Y coordinate
     */

    private double _calcX(double a, double b, double y) {
        return (a*Math.sqrt(b*b - y*y))/b;
    }


    /**
     * <p>Set the line colour for the use case oval.</p>
     *
     * <p>This involves setting the _cover oval, not the bigPort.</p>
     *
     * @param col The colour desired.  */

    public void setLineColor(Color col) {
        _cover.setLineColor(col);
    }


    /**
     * <p>Get the line colour for the use case oval.</p>
     *
     * <p>This involves getting the _cover oval colour, not the bigPort.</p>
     *
     * @return  The colour in use.
     */

    public Color getLineColor() {
        return _cover.getLineColor();
    }


    /**
     * <p>Set the fill colour for the use case oval.</p>
     *
     * <p>This involves setting the _cover oval, not the bigPort.</p>
     *
     * @param col  The colour desired.
     */

    public void setFillColor(Color col) {
        _cover.setFillColor(col);
    }


    /**
     * <p>Get the line colour for the use case oval.</p>
     *
     * <p>This involves getting the _cover oval colour, not the bigPort.</p>
     *
     * @return  The colour in use.
     */

    public Color getFillColor() {
        return _cover.getFillColor();
    }


    /**
     * <p>Set whether the use case oval is to be filled.</p>
     *
     * <p>This involves setting the _cover oval, not the bigPort.</p>
     *
     * @param f  <code>true</code> if the oval is to be filled,
     *           <code>false</code> if not.
     */

    public void setFilled(boolean f) {
        _cover.setFilled(f);
    }


    /**
     * <p>Get whether the use case oval is to be filled.</p>
     *
     * <p>This involves getting the _cover oval, not the bigPort.</p>
     *
     * @return  <code>true</code> if the oval is to be filled,
     *          <code>false</code> if not.
     */

    public boolean getFilled() {
        return _cover.getFilled();
    }


    /**
     * <p>Set the line width for the use case oval.</p>
     *
     * <p>This involves setting the _cover oval, not the bigPort.</p>
     *
     * @param w  The line width desired.
     */

    public void setLineWidth(int w) {
        _cover.setLineWidth(w);
    }


    /**
     * <p>Get the line width for the use case oval.</p>
     *
     * <p>This involves getting the _cover oval colour, not the bigPort.</p>
     *
     * @return  The line width set.
     */

    public int getLineWidth() {
        return _cover.getLineWidth();
    }

    
    /**
     * <p>FigMyCircle is a FigCircle with corrected connectionPoint method:
     *   this methods calculates where a connected edge ends.</p>
     */

    public class FigMyCircle extends FigCircle {
        protected Category cat = Category.getInstance(FigMyCircle.class);

        /**
         * <p>Constructor just invokes the parent constructor.</p>
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

        public FigMyCircle(int x, int y, int w, int h, Color lColor,
                           Color fColor) {
            super(x, y, w, h, lColor, fColor);
        }


        /**
         * <p>Compute the border point of the elipse that is on the edge
         *   between the stored upper left corner and the given parameter.</p>
         *
         * @param anotherPt  The remote point to which an edge is drawn.
         *
         * @return           The connection point on the boundary of the
         *                   elipse.
         */

        public Point connectionPoint(Point anotherPt) {
            double rx = _w/2;
            double ry = _h/2;
            double dx = anotherPt.x - _x;
            double dy = anotherPt.y - _y;
            double dd = ry*ry*dx*dx + rx*rx*dy*dy;
            double mu = rx*ry/Math.sqrt(dd);

            Point res = new Point((int)(mu*dx+_x+rx),(int)(mu*dy+_y+ry));
            cat.debug("    returns "+res.x+','+res.y+')');
            return res;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    // Event handlers
    //
    ///////////////////////////////////////////////////////////////////////////


    /**
     * <p>React to a mouse key being pressed.</p>
     *
     * @param me  The mouse action that caused us to be invoked.
     */

    public void mousePressed(MouseEvent me) {

        // Deal with anything from the parent first

        super.mousePressed(me);

        // If we are currently selected, turn off the draggable buttons at each
        // side, and unhighlight any currently selected extension points.

        Editor    ce  = Globals.curEditor();
        Selection sel = ce.getSelectionManager().findSelectionFor(this);

        if (sel instanceof SelectionUseCase) {
            ((SelectionUseCase) sel).hideButtons();
        }

        unhighlight();

        // Display extension point properties if necessary. Look to see if the
        // mouse (2x2 pixels) hit the extension point compartment. Use a flag
        // to track this.

        Rectangle r = new Rectangle(me.getX() - 1, me.getY() - 1, 2, 2);
        Fig       f = hitFig(r);

        boolean targetIsSet = false;

        if (f == _epVec) {

            // Work out which extension point this corresponds to. Each EP
            // takes ROWHEIGHT pixels, so take the difference between the
            // centre of the mouse (me.getY() - 1) and the top of the epVec
            // (f.getY()) and integer divide by ROWHEIGHT.

            Vector v = _epVec.getFigs();
            int    i = (me.getY() - f.getY() - 1)/ROWHEIGHT;

            // If we are in the range of the EP list size (avoids any nasty
            // boundary overflows), we can select that EP entry. Make this
            // entry the target Fig, and note that we do have a
            // target. Remember that the first entry in the vector is the
            // bigPort itself.

            if ((i >= 0) && (i < (v.size() - 1))) {
                targetIsSet = true;
                f           = (Fig)v.elementAt(i + 1);

		_highlightedFigText = (CompartmentFigText)f;
		_highlightedFigText.setHighlighted(true);
            }
	}

        // If we didn't get the EP compartment, we just select ourself.

	if (!targetIsSet) {
            ProjectBrowser.TheInstance.setTarget(getOwner());
        }
    }


    /**
     * <p>Deal with the mouse leaving the fig. Unhighlight the fig.</p>
     *
     * @param me  The mouse action that caused us to be invoked.
     */

    public void mouseExited(MouseEvent me) {
        super.mouseExited(me);
        unhighlight();
    }


    /**
     * <p>Deal with a key being pressed.</p>
     *
     * <p>We deal with UP and DOWN, and use these to move through the list
     *   of selected extension points. We deal with ENTER and use that to start
     *   the text editor.</p>
     *
     * @param ke  The key event that caused us to be invoked.
     */

    public void keyPressed(KeyEvent ke) {
        int key = ke.getKeyCode();

        // For UP and DOWN cycle through the extension points.

        if ((key == KeyEvent.VK_UP) || (key == KeyEvent.VK_DOWN)) {

            // Find the currently highlighted EP (if any) and unhighlight it

            CompartmentFigText ft = unhighlight();

            if (ft != null) {
                int i = _epVec.getFigs().indexOf(ft);

                // If we found one of the current EP's move forward or
                // backwards as appopriate, and set a newly selected EP as
                // highlighted

                if (i != -1) {
                    if (key == KeyEvent.VK_UP) {
                        ft = getPreviousVisibleFeature(_epVec, i);
                    } else {
                        ft = getNextVisibleFeature(_epVec, i);
                    }

                    if (ft != null) {
                        ft.setHighlighted(true);
                        _highlightedFigText = ft;
                        return;
                    }
                }
            }
        }

        // For ENTER start editing any text that is highlighted, remembering to
        // consume the ENTER event.

        else if ((key == KeyEvent.VK_ENTER) && (_highlightedFigText != null)) {
            _highlightedFigText.startTextEditor(ke);
            ke.consume();
            return;
        }

        // Anything else defer to our parent.

        else {
            super.keyPressed(ke);
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Internal methods
    //
    ///////////////////////////////////////////////////////////////////////////


    /**
     * <p>Invoked when text has been edited.</p>
     *
     * <p>We check that it is one of the extension point compartments and then
     *  parse accordingly.</p>
     *
     * @param ft  The text that has been edited.
     */

    protected void textEdited(FigText ft) throws PropertyVetoException {

        // Let the parent do anything it wants first

        super.textEdited(ft);

        // Only works if we have an owner

        MUseCase useCase = (MUseCase) getOwner();

        if (useCase == null) {
            return;
        }

        // Give up if we are not one of the extension points

        int i = _epVec.getFigs().indexOf(ft);

        if (i == -1) {
            return;
        }

        // Mark the text as highlighted, then parse it

        CompartmentFigText highlightedFigText = (CompartmentFigText)ft;
        highlightedFigText.setHighlighted(true);

        MExtensionPoint ep   = 
            (MExtensionPoint) (highlightedFigText.getOwner());
        String          text = highlightedFigText.getText().trim();
        
        ParserDisplay.SINGLETON.parseExtensionPointFig(useCase, ep, text);

        return;
    }


    /**
     * <p>Private method to find the previous visible feature to highlight.</p>
     *
     * <p>We're passed in a group (which will be the extension point vector)
     *   and the currently highlighted fig (a member of that vector).</p>
     *
     * @param fgVec  A fig group (invariably the extension point vector) in
     *               which to seek the previous visible feature.
     *
     * @param i      The index of the currently selected entry in
     *               <code>fgVec</code>.
     *
     * @return       The new fig to use.
     */

    private CompartmentFigText getPreviousVisibleFeature(FigGroup fgVec,
                                                         int      i) {

        // Give up if the index we don't have a vector, or the index is less
        // than 1 (the 0th entry is the bigPort surrounding all entries)

	if ((fgVec == null) || (i < 1)) {
            return null;
        }

        // Give up if we are off the top of the vector, or the indentified
        // element is not displayed

	Vector v = fgVec.getFigs();

	if ((i >= v.size()) || (!((FigText)v.elementAt(i)).isDisplayed())) {
            return null;
        }

        // Loop backwards through until we find an entry that is displayed. We
        // know this will terminate, since the current element is displayed

	CompartmentFigText cft = null;

	do {
            i   = (i <= 1) ? i - 1 : v.size() - 1;
            cft = (CompartmentFigText)v.elementAt(i);
	} while (!cft.isDisplayed());

	return cft;
    }

    /**
     * <p>Private method to find the next visible feature to highlight.</p>
     *
     * <p>We're passed in a group (which will be the extension point vector)
     *   and the currently highlighted fig (a member of that vector).</p>
     *
     * @param fgVec  A fig group (invariably the extension point vector) in
     *               which to seek the next visible feature.
     *
     * @param i      The index of the currently selected entry in
     *               <code>fgVec</code>.
     *
     * @return       The new fig to use.
     */

    private CompartmentFigText getNextVisibleFeature(FigGroup fgVec,
                                                     int      i) {

        // Give up if the index we don't have a vector, or the index is less
        // than 1 (the 0th entry is the bigPort surrounding all entries)

	if ((fgVec == null) || (i < 1)) {
            return null;
        }

        // Give up if we are off the top of the vector, or the indentified
        // element is not displayed

	Vector v = fgVec.getFigs();

	if ((i >= v.size()) || (!((FigText)v.elementAt(i)).isDisplayed())) {
            return null;
        }

        // Loop forwards through until we find an entry that is displayed. We
        // know this will terminate, since the current element is displayed

	CompartmentFigText cft = null;

	do {
            i   = (i >= (v.size() - 1)) ? 1 : i + 1;
            cft = (CompartmentFigText)v.elementAt(i);
	} while (!cft.isDisplayed());

	return cft;
    }


    /**
     * <p>Create a new "feature" (extension point) in the use case fig.</p>
     *
     * <p>Extension points are not strictly features, but that is a historical
     *   accident of naming. This creates a new entry in the extension point
     *   vector.</p>
     *
     * @param fg  The fig group to which this applies (which must be the
     *            extension point vector).
     *
     * @param ie  The input event that triggered us. In the current
     *            implementation a mouse double click.
     */

    protected void createFeatureIn(FigGroup fg, InputEvent ie) {

        // Give up if we don't have an owner

        if (getOwner() == null) {
            return;
        }

        // Invoke the relevant action method to create an empty extension
        // point, then start the editor, assuming we successfully created an
        // extension point.

        ActionAddExtensionPoint.singleton().actionPerformed(null);

	CompartmentFigText ft = (CompartmentFigText)fg.getFigs().lastElement();

	if (ft != null) {
            ft.startTextEditor(ie);
            ft.setHighlighted(true);

            _highlightedFigText = ft;
        }
    }


    /**
     * <p>Private utility to unhighlight any currently selected extension
     *   point.</p>
     *
     * @return  The extension point that was unhighlighted.
     */

    private CompartmentFigText unhighlight() {

        // Loop through the vector of extension points, until we find a
        // highlighted one.

        Vector v = _epVec.getFigs();
        int i;

        for (i = 1; i < v.size(); i++) {
            CompartmentFigText ft = (CompartmentFigText)v.elementAt(i);

            if (ft.isHighlighted()) {
                ft.setHighlighted(false);
                _highlightedFigText = null;

                return ft;
            }
        }

        // None were highlighted

        return null;
    }


    /**
     * <p>Called when there has been a change to the notation type (e.g. from
     *   UML to Java).</p>
     *
     * <p>The notation change handlers are actually defined in the
     *   superclass. We recompute all the figs needed for the model, which will
     *   regenerate all the notation.</p>
     */

    public void renderingChanged() {
        super.renderingChanged();
        modelChanged();
    }


    /**
     * <p>Adjust the fig in the light of some change to the model.</p>
     *
     * <p>Called both when there has been a change to notation, and when there
     *   has been an NSUML event.</p>
     */

    protected void modelChanged() {

        // Let our superclass sort itself out first

        super.modelChanged();

        // Give up if we have no owner

        MUseCase useCase = (MUseCase) getOwner();

        if (useCase == null) {
            return;
        }

        // Note our current bounds

        Rectangle oldBounds = getBounds();

        // Loop through all the extension points. epCount keeps track of the
        // fig's index as we go through the extension points.

        Collection eps     = UmlHelper.getHelper().getUseCases().getExtensionPoints(useCase);
	int        epCount = 1;

        if (eps != null) {
            int xpos = _epBigPort.getX();
            int ypos = _epBigPort.getY();

            // Take each EP and its corresponding fig in turn

            Iterator iter = eps.iterator();
            Vector   figs = _epVec.getFigs();

            while (iter.hasNext()) {
                CompartmentFigText epFig;
                MExtensionPoint ep = (MExtensionPoint) iter.next();

                // If we don't have a fig for this EP, we'll need to add
                // one. We set the bounds, but they will be reset later.

                if (figs.size() <= epCount) {
                    epFig =
                        new CompartmentFigText(xpos,
                                               ypos + (epCount-1)*ROWHEIGHT,
                                               0,
                                               ROWHEIGHT,
                                               _epBigPort);

                    epFig.setFilled(false);
                    epFig.setLineWidth(0);
                    epFig.setFont(LABEL_FONT);
                    epFig.setTextColor(Color.black);
                    epFig.setJustification(FigText.JUSTIFY_LEFT);
                    epFig.setMultiLine(false);

                    _epVec.addFig(epFig);
		} else {
                    epFig = (CompartmentFigText)figs.elementAt(epCount);
                }

                // Now put the text in
		// We must handle the case where the text is null
		String epText = Notation.generate(this, ep);
		if (epText == null) {
		    epText = "";
		}
                epFig.setText(epText);
                epFig.setOwner(ep);

                epCount++;
            }

            // Remove any spare figs we have if there are now fewer extension
            // points than figs

            if (figs.size() > epCount) {
                for (int i = figs.size() - 1; i >= epCount; i--) {
                    _epVec.removeFig((Fig)figs.elementAt(i));
                }
            }
	}

        // Now things to do with the use case itself. Put the use case in
        // italics if it is abstract, otherwise ordinary font.

        if (useCase.isAbstract()) {
            _name.setFont(ITALIC_LABEL_FONT);
        }
        else {
            _name.setFont(LABEL_FONT);
        }

        // Now recalculate all the bounds, using our old bounds.

	setBounds(oldBounds.x, oldBounds.y, oldBounds.width, oldBounds.height);
    }

} /* end class FigUseCase */
