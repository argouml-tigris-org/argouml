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
import java.util.Iterator;
import java.util.Vector;

import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/** Class to display graphics for a UML MState in a diagram. */

public class FigActor extends FigNodeModelElement {

    ////////////////////////////////////////////////////////////////
    // instance variables

    /* Put in the things for the "person" in the FigActor */
    private FigCircle head;
    private FigLine body;
    private FigLine arms;
    private FigLine leftLeg;
    private FigLine rightLeg;

    // add other Figs here as needed

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Main Constructor for the creation of a new Actor. 
     */
    public FigActor() {
        // Put this rectangle behind the rest, so it goes first
        FigRect bigPort = new FigRect(10, 30, 15, 45);
        bigPort.setFilled(false);
        bigPort.setLineWidth(0);
        head = new FigCircle(10, 30, 15, 15, Color.black, Color.white);
        body = new FigLine(20, 45, 20, 60, Color.black);
        arms = new FigLine(10, 50, 30, 50, Color.black);
        leftLeg = new FigLine(20, 60, 15, 75, Color.black);
        rightLeg = new FigLine(20, 60, 25, 75, Color.black);
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

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigActor figClone = (FigActor) super.clone();
        Iterator it = figClone.getFigs(null).iterator();
        figClone.setBigPort((FigCircle) it.next());
        figClone.head = (FigCircle) it.next();
        figClone.body = (FigLine) it.next();
        figClone.arms = (FigLine) it.next();
        figClone.leftLeg = (FigLine) it.next();
        figClone.rightLeg = (FigLine) it.next();
        figClone.setNameFig((FigText) it.next());
        return figClone;
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
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
        head.setLineColor(col);
        body.setLineColor(col);
        arms.setLineColor(col);
        leftLeg.setLineColor(col);
        rightLeg.setLineColor(col);
    }
    
    /**
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return head.getLineColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
        head.setFillColor(col);
    }
    
    /**
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
        return head.getFillColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean f) {
        head.setFilled(f);
    }
    
    /**
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return head.getFilled();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        head.setLineWidth(w);
        body.setLineWidth(w);
        arms.setLineWidth(w);
        leftLeg.setLineWidth(w);
        rightLeg.setLineWidth(w);
    }
    
    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return head.getLineWidth();
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
        head.setLocation(x + middle - head.getWidth() / 2, y + h - 65);
        body.setLocation(x + middle, y + h - 50);
        arms.setLocation(x + middle - arms.getWidth() / 2, y + h - 45);
        leftLeg.setLocation(x + middle - leftLeg.getWidth(), y + h - 35);
        rightLeg.setLocation(x + middle, y + h - 35);

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
        int cx = head.center().x;
        int cy = head.center().y;
        int radiusx = Math.round(head.getWidth() / 2) + 1;
        int radiusy = Math.round(head.getHeight() / 2) + 1;
        final int maxPoints = 20;
        Point point = null;
        for (int i = 0; i < maxPoints; i++) {
	    double angle = 2 * Math.PI / maxPoints * i;
            point = new Point((int) (cx + Math.cos(angle) * radiusx),
			      (int) (cy + Math.sin(angle) * radiusy));
            ret.add(point);
        }
        ret.add(new Point(body.getX(), body.getY()));
        ret.add(new Point(body.getX1(), body.getY1()));
        ret.add(new Point(leftLeg.getX(), leftLeg.getY()));
        ret.add(new Point(leftLeg.getX1(), leftLeg.getY1()));
        ret.add(new Point(rightLeg.getX(), rightLeg.getY()));
        ret.add(new Point(rightLeg.getX1(), rightLeg.getY1()));
        ret.add(new Point(arms.getX(), arms.getY()));
        ret.add(new Point(arms.getX1(), arms.getY1()));
        return ret;
    }

} /* end class FigActor */
