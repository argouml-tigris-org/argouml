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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.ArgoFigUtil;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;

/**
 * Class to display graphics for an Actor in a diagram. <p>
 * 
 * The dimensions of the stick-man figure are fixed at 40 wide by 55 high.
 * It does not support different line-widths.<p>
 * 
 * Stereotypes and the name are shown below the stick-man.<p>
 * 
 *  This seems to be the only ArgoUML element where the stereotypes 
 *  are shown below the name. The UML 1.4.2 standard does not forbid nor 
 *  prescribe this layout detail.
 */
public class FigActor extends FigNodeModelElement {

    /**
     * The padding between the actor body and name and the top of the
     * stereotype.
     */
    protected static final int MIN_VERT_PADDING = 4;

    //These are the positions of child figs inside this fig
    //They must be added in the constructor in this order.
    //For now the name must not be last as this would force
    //zero width lines (until GEF is fixed)
    private static final int HEAD_POSN = 2;
    private static final int BODY_POSN = 3;
    private static final int ARMS_POSN = 4;
    private static final int LEFT_LEG_POSN = 5;
    private static final int RIGHT_LEG_POSN = 6;

    private void constructFigs(Rectangle bounds) {

        
        FigCircle head =
            new FigCircle(X0 + 2, Y0, 16, 15);
        FigLine body = new FigLine(X0 + 10, Y0 + 15, 20, 40);
        FigLine arms = new FigLine(X0, Y0 + 20, 30, 30);
        FigLine leftLeg = new FigLine(X0 + 10, Y0 + 30, 15, 55);
        FigLine rightLeg = new FigLine(X0 + 10, Y0 + 30, 25, 55);
        
        getNameFig().setBounds(X0, Y0 + 45, 20, 20);

        getNameFig().setTextFilled(false);
        getNameFig().setFilled(false);
        getNameFig().setLineWidth(0);
        // initialize any other Figs here
        getStereotypeFig().setBounds(getBigPort().getCenter().x,
                                     getBigPort().getCenter().y,
                                     0, 0);
        setSuppressCalcBounds(true);
        // add Figs to the FigNode in back-to-front order
        // Put this rectangle behind the rest, so it goes first
        addFig(getBigPort());
        addFig(getNameFig());
        addFig(head);
        addFig(body);
        addFig(arms);
        addFig(leftLeg);
        addFig(rightLeg);
        addFig(getStereotypeFig());
        
        bindPort(getOwner(), getBigPort());
        setResizable(false);
        
        setFilled(true);
        setFillColor(FILL_COLOR);
        setLineColor(LINE_COLOR);
        setLineWidth(LINE_WIDTH);
        setTextColor(TEXT_COLOR);
        
        /* Set the drop location in the case of D&D: */
        if (bounds != null) {
            setLocation(bounds.x, bounds.y);
        }

        setSuppressCalcBounds(false);
        setBounds(getBounds());
    }
    
    @Override
    protected Fig createBigPortFig() {
        return new ActorPortFigRect(X0, Y0, 0, 0, this);
    }

    /**
     * Construct a new Actor with the given owner, bounds, and settings.  This
     * constructor is used by the PGML parser.
     * 
     * @param owner model element that owns this fig
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigActor(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
        constructFigs(bounds);
    }
    
    public void paint(Graphics g) {
        forceRepaintShadow();
        super.paint(g);
    }

    @Override
    public void setLineWidth(int width) {
        /* This sets the lineWidth of all in the group: */
        super.setLineWidth(width);
        /* NameFig and StereotypeFig are handled by parent. */
    }
    
    @Override
    public void setFillColor(Color col) {
        super.setFillColor(col);
        getStereotypeFig().setFillColor(null);
        getNameFig().setFillColor(null);
    }

    @Override
    public void setFilled(boolean filled) {
        super.setFilled(filled);
        getBigPort().setFilled(false);
        getNameFig().setFilled(false);
        getStereotypeFig().setFilled(false);
        // Only the head should be filled (not the text)
    }

    @Override
    public Selection makeSelection() {
        return new SelectionActor(this);
    }

    @Override
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        // Modifiers ...
        popUpActions.add(
                popUpActions.size() - getPopupAddOffset(),
                buildModifierPopUp(ABSTRACT | LEAF | ROOT));
        return popUpActions;
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension aSize = new Dimension(40, 55);
        aSize = ArgoFigUtil.addChildDimensions(aSize, getNameFig());
        aSize = ArgoFigUtil.addChildDimensions(aSize, getStereotypeFig());
        return aSize;
    }

    @Override
    protected void setStandardBounds(final int x, final int y, 
            final int w, final int h) {

        Rectangle oldBounds = getBounds();
        
        // Make sure we don't try to set things smaller than the minimum
        Dimension minimumSize = getMinimumSize();
        int newW = Math.max(w, minimumSize.width);
        int newH = Math.max(h, minimumSize.height);

        int middle = x + newW / 2;

        getFigAt(HEAD_POSN).setLocation(
                middle - getFigAt(HEAD_POSN).getWidth() / 2, y + 10);
        getFigAt(BODY_POSN).setLocation(middle, y + 25);
        getFigAt(ARMS_POSN).setLocation(
                middle - getFigAt(ARMS_POSN).getWidth() / 2, y + 30);
        getFigAt(LEFT_LEG_POSN).setLocation(
                middle - getFigAt(LEFT_LEG_POSN).getWidth(), y + 40);
        getFigAt(RIGHT_LEG_POSN).setLocation(middle, y +  40);

        Dimension minTextSize = getNameFig().getMinimumSize();
        getNameFig().setBounds(middle - minTextSize.width / 2,
	        y +  55,
	        minTextSize.width,
	        minTextSize.height);

        if (getStereotypeFig().isVisible()) {
            Dimension minStereoSize = getStereotypeFig().getMinimumSize();
            assert minStereoSize.width <= newW;
            getStereotypeFig().setBounds(middle - minStereoSize.width / 2,
                    y + 55 + getNameFig().getHeight(),
                    minStereoSize.width, 
                    minStereoSize.height);
        }

        getBigPort().setBounds(x, y, newW, newH);

        calcBounds(); //Accumulate a bounding box for all the Figs in the group.
        firePropChange("bounds", oldBounds, getBounds());
        updateEdges();
    }

    @Override
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
    @Override
    public List<Point> getGravityPoints() {
        final int maxPoints = 20;
        List<Point> ret = new ArrayList<Point>();
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
        ret.add(new Point(((FigLine) getFigAt(RIGHT_LEG_POSN)).getX2(),
                          ((FigLine) getFigAt(RIGHT_LEG_POSN)).getY2() + getNameFig().getHeight()));
        ret.add(new Point(((FigLine) getFigAt(ARMS_POSN)).getX1(),
                          ((FigLine) getFigAt(ARMS_POSN)).getY1()));
        ret.add(new Point(((FigLine) getFigAt(ARMS_POSN)).getX2(),
                          ((FigLine) getFigAt(ARMS_POSN)).getY2()));
        ret.add(new Point(((FigLine) getFigAt(LEFT_LEG_POSN)).getX2(),
                          ((FigLine) getFigAt(LEFT_LEG_POSN)).getY2() + getNameFig().getHeight()));
        return ret;
    }

    @Override
    protected void modelChanged(PropertyChangeEvent mee) {
        //      name updating
        super.modelChanged(mee);

        boolean damage = false;
        if (getOwner() == null) {
            return;
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

    @Override
    protected int getNameFigFontStyle() {
        Object cls = getOwner();
        return Model.getFacade().isAbstract(cls) ? Font.ITALIC : Font.PLAIN;
    }

    /**
     * The bigPort needs to overrule the getGravityPoints,
     * because it is the port of this FigNode.
     *
     * @author mvw
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
         * @param h the height
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
        @Override
        public List getGravityPoints() {
            return parent.getGravityPoints();
        }

        @Override
        public void setFilled(boolean f) {
            super.setFilled(false);
        }

        @Override
        public void setLineWidth(int w) {
            super.setLineWidth(0);
        }

    }

}
