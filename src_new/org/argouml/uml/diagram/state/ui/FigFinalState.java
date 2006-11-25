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

package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.argouml.model.Model;
import org.argouml.uml.diagram.activity.ui.SelectionActionState;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigCircle;

/**
 * Class to display graphics for a UML FinalState in a diagram.
 *
 * @author ics125b spring 98
 */
public class FigFinalState extends FigStateVertex {

    ////////////////////////////////////////////////////////////////
    // constants

    private static final int X = 10;
    private static final int Y = 10;
    private static final int WIDTH = 24;
    private static final int HEIGHT = 24;

    ////////////////////////////////////////////////////////////////
    // instance variables

    private FigCircle inCircle;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The main constructor.
     */
    public FigFinalState() {
        super();
        setEditable(false);
        Color handleColor = Globals.getPrefs().getHandleColor();
        FigCircle bigPort =
            new FigCircle(X, Y, WIDTH, HEIGHT, Color.black, Color.white);
        inCircle =
            new FigCircle(
        		  X + 5,
        		  Y + 5,
        		  WIDTH - 10,
        		  HEIGHT - 10,
        		  handleColor,
        		  Color.black);

        bigPort.setLineWidth(1);
        inCircle.setLineWidth(0);

        addFig(bigPort);
        addFig(inCircle);
        setBigPort(bigPort);

        setBlinkPorts(false); //make port invisble unless mouse enters
    }

    /**
     * The constructor that hooks the Fig into the UML element.
     * @param gm ignored
     * @param node the UML element
     */
    public FigFinalState(GraphModel gm, Object node) {
    	this();
    	setOwner(node);
    }

    /*
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigFinalState figClone = (FigFinalState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigCircle) it.next());
        figClone.inCircle = (FigCircle) it.next();

        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        Object pstate = null;
        Selection sel = null;
        if (getOwner() != null) {
            pstate = getOwner();
            if (Model.getFacade().isAActivityGraph(
                            Model.getFacade().getStateMachine(
                            Model.getFacade().getContainer(pstate)))) {
                sel = new SelectionActionState(this);
                ((SelectionActionState) sel).setOutgoingButtonEnabled(false);
            } else {
                sel = new SelectionState(this);
                ((SelectionState) sel).setOutgoingButtonEnabled(false);
            }
        }
        return sel;
    }

    /**
     * Final states are fixed size.
     * @return false
     * @see org.tigris.gef.presentation.Fig#isResizable()
     */
    public boolean isResizable() {
        return false;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
        getBigPort().setLineColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return getBigPort().getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
        inCircle.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
        return inCircle.getFillColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean f) {
        // ignored - rendering is fixed
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return true;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        getBigPort().setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
	return getBigPort().getLineWidth();
    }

    ////////////////////////////////////////////////////////////////
    // Event handlers

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
        // ignore mouse clicks
    }

    /**
     * The UID.
     */
    static final long serialVersionUID = -3506578343969467480L;


    /**
     * TODO: MVW: I do not see any reason for this. Can we remove it?
     *
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    /*public void setBoundsImpl(int boundX, int boundY,
        int boundW, int boundH) {
        _x = boundX;
        _y = boundY;
        getBigPort().setX(boundX);
        getBigPort().setY(boundY);
        inCircle.setX(boundX + 5);
        inCircle.setY(boundY + 5);
    }*/

    /*
     * Makes sure that edges stick to the outer circle and not to the box.
     *
     * @see org.tigris.gef.presentation.Fig#getGravityPoints()
     */
    public List getGravityPoints() {
        Vector ret = new Vector();
        int cx = getBigPort().getCenter().x;
        int cy = getBigPort().getCenter().y;
        double radius = getBigPort().getWidth() / 2 + 1;
        final int maxPoints = 32;
        Point point = null;
        final double pi2 = Math.PI * 2;
        for (int i = 0; i < maxPoints; i++) {
            int px = (int) (cx + Math.cos(pi2 * i / maxPoints) * radius);
            int py = (int) (cy + Math.sin(pi2 * i / maxPoints) * radius);
            point = new Point(px, py);
            ret.add(point);
        }
        return ret;

    }

} /* end class FigFinalState */
