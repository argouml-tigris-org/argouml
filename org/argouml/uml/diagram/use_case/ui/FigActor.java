// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

// File: FigActor.java
// Classes: FigActor
// Original Author: abonner@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.use_case.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;

/** Class to display graphics for a UML MState in a diagram. */

public class FigActor extends FigNodeModelElement {

    ////////////////////////////////////////////////////////////////
    // instance variables

    //These are the positions of child figs inside this fig
    //They mst be added in the constructor in this order.
    private static final int HEAD_POSN = 1;
    private static final int BODY_POSN = 2;
    private static final int ARMS_POSN = 3;
    private static final int LEFT_LEG_POSN = 4;
    private static final int RIGHT_LEG_POSN = 5;
    private static final int NAME_POSN = 6;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Main Constructor for the creation of a new Actor. 
     */
    public FigActor() {
        // Put this rectangle behind the rest, so it goes first
        FigRect bigPort = new FigRect(10, 30, 15, 60);
        bigPort.setVisible(false);
        FigCircle head = new FigCircle(10, 30, 15, 15, Color.black, Color.white);
        FigLine body = new FigLine(20, 45, 20, 60, Color.black);
        FigLine arms = new FigLine(10, 50, 30, 50, Color.black);
        FigLine leftLeg = new FigLine(20, 60, 15, 75, Color.black);
        FigLine rightLeg = new FigLine(20, 60, 25, 75, Color.black);
        getNameFig().setBounds(5, 75, 35, 20);

        getNameFig().setTextFilled(false);
        getNameFig().setFilled(false);
        getNameFig().setLineWidth(0);
        // initialize any other Figs here

        // add Figs to the FigNode in back-to-front order
        addFig(bigPort);
        addFig(head);
        addFig(body);
        addFig(arms);
        addFig(leftLeg);
        addFig(rightLeg);
        addFig(getNameFig());
        setBigPort(bigPort);
    }

    /**
     * <p>Constructor for use if this figure is created for an existing actor
     *   node in the metamodel.</p>
     * 
     * @param gm ignored!
     * @param node The UML object being placed.
     */
    public FigActor(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() {
        return "new MActor";
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    /**
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionActor(this);
    }

    /** Returns true if this Fig can be resized by the user. 
     * @see org.tigris.gef.presentation.Fig#isResizable()
     */
    public boolean isResizable() {
        return false;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        Dimension nameDim = getNameFig().getMinimumSize();
        int w = nameDim.width;
        int h = nameDim.height + 65;
        return new Dimension(w, h);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    public void setBounds(int x, int y, int w, int h) {
        int middle = w / 2;
        h = _h;
        Rectangle oldBounds = getBounds();
        getBigPort().setLocation(x + middle - getBigPort().getWidth() / 2,
                                 y + h - 65);
        getFigAt(HEAD_POSN).setLocation(x + middle - getFigAt(HEAD_POSN).getWidth() / 2, y + h - 65);
        getFigAt(BODY_POSN).setLocation(x + middle, y + h - 50);
        getFigAt(ARMS_POSN).setLocation(x + middle - getFigAt(ARMS_POSN).getWidth() / 2, y + h - 45);
        getFigAt(LEFT_LEG_POSN).setLocation(x + middle - getFigAt(LEFT_LEG_POSN).getWidth(), y + h - 35);
        getFigAt(RIGHT_LEG_POSN).setLocation(x + middle, y + h - 35);

        Dimension minTextSize = getNameFig().getMinimumSize();
        getNameFig().setBounds(x + middle - minTextSize.width / 2,
			       y + h - minTextSize.height,
			       minTextSize.width,
			       minTextSize.height);

        updateEdges();
        _x = x;
        _y = y;
        _w = w;
        // do not set height
        firePropChange("bounds", oldBounds, getBounds());
    }

    /**
     * @see org.tigris.gef.presentation.FigNode#deepHitPort(int, int)
     */
    public Object deepHitPort(int x, int y) {
        Object o = super.deepHitPort(x, y);
        if (o != null)
            return o;
        if (hit(new Rectangle(new Dimension(x, y))))
            return getOwner();
        return null;
    }
    
    /**
     * Makes sure that the edges stick to the outline of the fig.
     * @see org.tigris.gef.presentation.Fig#getGravityPoints()
     */
    public Vector getGravityPoints() {
        Vector ret = new Vector();
        int cx = getFigAt(HEAD_POSN).center().x;
        int cy = getFigAt(HEAD_POSN).center().y;
        int radiusx = Math.round(getFigAt(HEAD_POSN).getWidth() / 2) + 1;
        int radiusy = Math.round(getFigAt(HEAD_POSN).getHeight() / 2) + 1;
        final int maxPoints = 20;
        Point point = null;
        for (int i = 0; i < maxPoints; i++) {
	    double angle = 2 * Math.PI / maxPoints * i;
            point = new Point((int) (cx + Math.cos(angle) * radiusx),
			      (int) (cy + Math.sin(angle) * radiusy));
            ret.add(point);
        }
        ret.add(new Point(getFigAt(BODY_POSN).getX(),
                          getFigAt(BODY_POSN).getY()));
        ret.add(new Point(((FigLine)getFigAt(BODY_POSN)).getX1(),
                          ((FigLine)getFigAt(BODY_POSN)).getY1()));
        ret.add(new Point(getFigAt(LEFT_LEG_POSN).getX(),
                          getFigAt(LEFT_LEG_POSN).getY()));
        ret.add(new Point(((FigLine)getFigAt(LEFT_LEG_POSN)).getX1(),
                          ((FigLine)getFigAt(LEFT_LEG_POSN)).getY1()));
        ret.add(new Point(getFigAt(RIGHT_LEG_POSN).getX(),
                          getFigAt(RIGHT_LEG_POSN).getY()));
        ret.add(new Point(((FigLine)getFigAt(RIGHT_LEG_POSN)).getX1(),
                          ((FigLine)getFigAt(RIGHT_LEG_POSN)).getY1()));
        ret.add(new Point(getFigAt(ARMS_POSN).getX(),
                          getFigAt(ARMS_POSN).getY()));
        ret.add(new Point(((FigLine)getFigAt(ARMS_POSN)).getX1(),
                          ((FigLine)getFigAt(ARMS_POSN)).getY1()));
        return ret;
    }

} /* end class FigActor */
