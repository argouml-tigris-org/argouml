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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.argouml.model.Model;
import org.argouml.uml.diagram.activity.ui.SelectionActionState;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigCircle;

/**
 * Class to display graphics for a UML Initial State in a diagram.
 *
 * @author abonner
 */
public class FigInitialState extends FigStateVertex {

    ////////////////////////////////////////////////////////////////
    // constants

    private static final int X = 10;
    private static final int Y = 10;
    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;

    ////////////////////////////////////////////////////////////////
    // instance variables

    private FigCircle head;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Main constructor.
     */
    public FigInitialState() {
        setEditable(false);
        FigCircle bigPort =
            new FigCircle(X, Y, WIDTH, HEIGHT, Color.cyan, Color.cyan);
        head = new FigCircle(X, Y, WIDTH, HEIGHT, Color.black, Color.black);

        // add Figs to the FigNode in back-to-front order
        addFig(bigPort);
        addFig(head);

        setBigPort(bigPort);

        setBlinkPorts(false); //make port invisble unless mouse enters
    }

    /**
     * Constructor which hooks the Fig into an existing UML element.
     *
     * @param gm ignored
     * @param node the UML element
     */
    public FigInitialState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /*
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigInitialState figClone = (FigInitialState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        setBigPort((FigCircle) it.next());
        figClone.head = (FigCircle) it.next();
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
            if (pstate == null) {
                return sel;
            }
            if (Model.getFacade().isAActivityGraph(
                    Model.getFacade().getStateMachine(
                            Model.getFacade().getContainer(pstate)))) {
                sel = new SelectionActionState(this);
                ((SelectionActionState) sel).setIncomingButtonEnabled(false);
                Collection outs = Model.getFacade().getOutgoings(getOwner());
                ((SelectionActionState) sel)
                        .setOutgoingButtonEnabled(outs == null
                                || outs.size() == 0);
            } else {
                sel = new SelectionState(this);
                ((SelectionState) sel).setIncomingButtonEnabled(false);
                Collection outs = Model.getFacade().getOutgoings(getOwner());
                ((SelectionState) sel).setOutgoingButtonEnabled(outs == null
                        || outs.size() == 0);
            }
        }
        return sel;
    }

    /**
     * Initial states are fixed size.
     *
     * @return false
     */
    public boolean isResizable() {
        return false;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
        head.setLineColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return head.getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
        head.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
        return head.getFillColor();
    }

    /**
     * Ignored - figure has fixed rendering
     * @param f ignored
     */
    public void setFilled(boolean f) {
        // ignored
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
        head.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return head.getLineWidth();
    }

    ////////////////////////////////////////////////////////////////
    // Event handlers

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
        // ignored
    }

    /**
     * The UID.
     */
    static final long serialVersionUID = 6572261327347541373L;

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
} /* end class FigInitialState */
