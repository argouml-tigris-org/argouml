// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;

/**
 * Class to display graphics for an Actor in a diagram.
 */
public class FigActor extends FigNodeModelElement {

    /**
     * The serialization version - Eclipse generated for rev. 1.40
     */
    private static final long serialVersionUID = 7265843766314395713L;

    /**
     * The padding between the actor body and name and the top of the
     * stereotype.
     */
    protected static final int MIN_VERT_PADDING = 4;

    //These are the positions of child figs inside this fig
    //They mst be added in the constructor in this order.
    //For now the name must not be last as this would force
    //zero width lines (until GEF is fixed)
    private static final int HEAD_POSN = 2;
    private static final int BODY_POSN = 3;
    private static final int ARMS_POSN = 4;
    private static final int LEFT_LEG_POSN = 5;
    private static final int RIGHT_LEG_POSN = 6;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Main Constructor for the creation of a new Actor.
     */
    public FigActor() {
        // Put this rectangle behind the rest, so it goes first
        FigRect bigPort = new ActorPortFigRect(10, 10, 15, 60, this);
        bigPort.setVisible(false);
        FigCircle head =
            new FigCircle(10, 10, 15, 15, Color.black, Color.white);
        FigLine body = new FigLine(20, 25, 20, 40, Color.black);
        FigLine arms = new FigLine(10, 30, 30, 30, Color.black);
        FigLine leftLeg = new FigLine(20, 40, 15, 55, Color.black);
        FigLine rightLeg = new FigLine(20, 40, 25, 55, Color.black);
        getNameFig().setBounds(5, 55, 35, 20);

        getNameFig().setTextFilled(false);
        getNameFig().setFilled(false);
        getNameFig().setLineWidth(0);
        // initialize any other Figs here
        getStereotypeFig().setBounds(getBigPort().getCenter().x,
                                     getBigPort().getCenter().y,
                                     0, 0);

        // add Figs to the FigNode in back-to-front order
        addFig(bigPort);
        addFig(getNameFig());
        addFig(head);
        addFig(body);
        addFig(arms);
        addFig(leftLeg);
        addFig(rightLeg);
        addFig(getStereotypeFig());
        setBigPort(bigPort);
    }

    /**
     * Constructor for use if this figure is created for an existing actor
     * node in the metamodel.<p>
     *
     * @param gm ignored!
     * @param node The UML object being placed.
     */
    public FigActor(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int width) {
        // Miss out the text fix, this should have no line
        getFigAt(HEAD_POSN).setLineWidth(width);
        getFigAt(BODY_POSN).setLineWidth(width);
        getFigAt(ARMS_POSN).setLineWidth(width);
        getFigAt(LEFT_LEG_POSN).setLineWidth(width);
        getFigAt(RIGHT_LEG_POSN).setLineWidth(width);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean filled) {
        // Only the head should be filled (not the text)
        getFigAt(HEAD_POSN).setFilled(filled);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() {
        return "new Actor";
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionActor(this);
    }

    /*
     * @see org.tigris.gef.ui.PopupGenerator#getPopUpActions(
     *         java.awt.event.MouseEvent)
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        // Modifiers ...
        popUpActions.insertElementAt(
                buildModifierPopUp(ABSTRACT | LEAF | ROOT),
                popUpActions.size() - getPopupAddOffset());
        return popUpActions;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#isResizable()
     */
    public boolean isResizable() {
        return false;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        Dimension nameDim = getNameFig().getMinimumSize();
        int w = nameDim.width;
        int h = nameDim.height + 65;
        return new Dimension(w, h);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    protected void setBoundsImpl(int x, int y, int w, int h) {
        int middle = w / 2;
        h = _h;
        Rectangle oldBounds = getBounds();
        getBigPort().setLocation(
                x + middle - getBigPort().getWidth() / 2, y + h - 65);
        getFigAt(HEAD_POSN).setLocation(
                x + middle - getFigAt(HEAD_POSN).getWidth() / 2, y + h - 65);
        getFigAt(BODY_POSN).setLocation(x + middle, y + h - 50);
        getFigAt(ARMS_POSN).setLocation(
                x + middle - getFigAt(ARMS_POSN).getWidth() / 2, y + h - 45);
        getFigAt(LEFT_LEG_POSN).setLocation(
                x + middle - getFigAt(LEFT_LEG_POSN).getWidth(), y + h - 35);
        getFigAt(RIGHT_LEG_POSN).setLocation(x + middle, y + h - 35);

        Dimension minTextSize = getNameFig().getMinimumSize();
        getNameFig().setBounds(x + middle - minTextSize.width / 2,
			       y + h - minTextSize.height,
			       minTextSize.width,
			       minTextSize.height);
//        updateStereotypeText();
        updateEdges();
        _x = x;
        _y = y;
        _w = w;
        // do not set height
        firePropChange("bounds", oldBounds, getBounds());
    }

    /*
     * @see org.tigris.gef.presentation.FigNode#deepHitPort(int, int)
     */
    public Object deepHitPort(int x, int y) {
        Object o = super.deepHitPort(x, y);
        if (o != null) {
            return o;
        }
        if (hit(new Rectangle(new Dimension(x, y)))) {
            return getOwner();
        }
        return null;
    }

    /*
     * Makes sure that the edges stick to the outline of the fig.
     * @see org.tigris.gef.presentation.Fig#getGravityPoints()
     */
    public List getGravityPoints() {
        final int maxPoints = 20;
        List ret = new ArrayList();
        int cx = getFigAt(HEAD_POSN).getCenter().x;
        int cy = getFigAt(HEAD_POSN).getCenter().y;
        int radiusx = Math.round(getFigAt(HEAD_POSN).getWidth() / 2) + 1;
        int radiusy = Math.round(getFigAt(HEAD_POSN).getHeight() / 2) + 1;
        Point point = null;
        for (int i = 0; i < maxPoints; i++) {
            double angle = 2 * Math.PI / maxPoints * i;
            point =
                new Point((int) (cx + Math.cos(angle) * radiusx),
                          (int) (cy + Math.sin(angle) * radiusy));
            ret.add(point);
        }
        ret.add(new Point(((FigLine) getFigAt(LEFT_LEG_POSN)).getX2(),
                          ((FigLine) getFigAt(LEFT_LEG_POSN)).getY2()));
        ret.add(new Point(((FigLine) getFigAt(RIGHT_LEG_POSN)).getX2(),
                          ((FigLine) getFigAt(RIGHT_LEG_POSN)).getY2()));
        ret.add(new Point(((FigLine) getFigAt(ARMS_POSN)).getX1(),
                          ((FigLine) getFigAt(ARMS_POSN)).getY1()));
        ret.add(new Point(((FigLine) getFigAt(ARMS_POSN)).getX2(),
                          ((FigLine) getFigAt(ARMS_POSN)).getY2()));
        return ret;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        //      name updating
        super.modelChanged(mee);

        boolean damage = false;
        if (getOwner() == null) {
            return;
        }

        if (mee == null || mee.getPropertyName().equals("isAbstract")) {
            updateAbstract();
            damage = true;
        }
        if (mee == null 
                || mee.getPropertyName().equals("stereotype") 
                || Model.getFacade().getStereotypes(getOwner())
                                .contains(mee.getSource())) {
            updateStereotypeText();
            damage = true;
        }

        if (damage) {
            damage();
        }
    }

    /**
     * Rerenders the fig if needed. This functionality was originally
     * the functionality of modelChanged but modelChanged takes the
     * event now into account.
     */
    public void renderingChanged() {
        if (getOwner() != null) {
            updateAbstract();
        }
        super.renderingChanged();
        damage();
    }


    /**
     * Updates the name if modelchanged receives an "isAbstract" event.
     */
    protected void updateAbstract() {
        Rectangle rect = getBounds();
        if (getOwner() == null) {
            return;
        }
        Object cls = getOwner();
        if (Model.getFacade().isAbstract(cls)) {
            getNameFig().setFont(getItalicLabelFont());
	} else {
            getNameFig().setFont(getLabelFont());
	}
        super.updateNameText();
        setBounds(rect.x, rect.y, rect.width, rect.height);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     */
    protected void updateStereotypeText() {
        super.updateStereotypeText();
        if (!Model.getFacade().getStereotypes(getOwner()).isEmpty()) {
            Dimension stereoMin = getStereotypeFig().getMinimumSize();
            getStereotypeFig().setBounds(
                (getBigPort().getCenter().x
                                     - getStereotypeFig().getWidth() / 2),
                (getBigPort().getY() + getBigPort().getHeight()
                                     + MIN_VERT_PADDING),
                stereoMin.width,
                stereoMin.height);
        } else {
            getStereotypeFig().setBounds(getBigPort().getCenter().x,
                                         getBigPort().getCenter().y,
                                         0,
                                         0);
        }
        damage();
    }

    /**
     * The bigport needs to overrule the getGravityPoints,
     * because it is the port of this FigNode.
     *
     * @author mvw@tigris.org
     */
    static class ActorPortFigRect extends FigRect {
        /**
         * the parent fig, i.e. the Actor
         */
        private Fig parent;

        /**
         * The constructor.
         *
         * @param x the x
         * @param y the y
         * @param w the width
         * @param h the hight
         * @param p the Actor fig
         */
        public ActorPortFigRect(int x, int y, int w, int h, Fig p) {
            super(x, y, w, h, null, null);
            parent = p;
        }

        /**
         * Makes sure that the edges stick to the outline of the fig.
         * @see org.tigris.gef.presentation.Fig#getGravityPoints()
         */
        public List getGravityPoints() {
            return parent.getGravityPoints();
        }

        /**
         * The serial version - Eclise generated for Rev. 1.40
         */
        private static final long serialVersionUID = 5973857118854162659L;
    }

} /* end class FigActor */
