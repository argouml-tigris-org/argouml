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

package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

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

    private static final int X = 10;
    private static final int Y = 10;
    private static final int WIDTH = 24;
    private static final int HEIGHT = 24;

    private FigCircle inCircle;

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

    @Override
    public Object clone() {
        FigFinalState figClone = (FigFinalState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigCircle) it.next());
        figClone.inCircle = (FigCircle) it.next();

        return figClone;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    @Override
    public Selection makeSelection() {
        Object pstate = getOwner();
        Selection sel = null;
        if ( pstate != null) {
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
    @Override
    public boolean isResizable() {
        return false;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    @Override
    public void setLineColor(Color col) {
        getBigPort().setLineColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    @Override
    public Color getLineColor() {
        return getBigPort().getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    @Override
    public void setFillColor(Color col) {
        inCircle.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    @Override
    public Color getFillColor() {
        return inCircle.getFillColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    @Override
    public void setFilled(boolean f) {
        // ignored - rendering is fixed
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    @Override
    public boolean getFilled() {
        return true;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    @Override
    public void setLineWidth(int w) {
        getBigPort().setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    @Override
    public int getLineWidth() {
	return getBigPort().getLineWidth();
    }

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        // ignore mouse clicks
    }

    /**
     * The UID.
     */
    static final long serialVersionUID = -3506578343969467480L;

    /**
     * Return a list of gravity points around the outer circle. Used in place of
     * the default bounding box.
     *
     * {@inheritDoc}
     */
    @Override
    public List getGravityPoints() {
        return getCircleGravityPoints();
    }

    /**
     * Override setBounds to keep shapes looking right.
     * {@inheritDoc}
     */
    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
        }
        Rectangle oldBounds = getBounds();

        getBigPort().setBounds(x, y, w, h);
        inCircle.setBounds(x + 5, y + 5, w - 10, h - 10);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

}
