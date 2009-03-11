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

package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
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

    private static final int WIDTH = 24;
    private static final int HEIGHT = 24;

    private FigCircle inCircle;
    private FigCircle outCircle;

    
    /**
     * Construct a new FigFinalState.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigFinalState(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initFigs();
    }
    
    /**
     * The main constructor.
     * @deprecated for 0.27.4 by tfmorris.  Use 
     * {@link #FigFinalState(Object, Rectangle, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigFinalState() {
        super();
        initFigs();
    }

    private void initFigs() {
        setEditable(false);
        Color handleColor = Globals.getPrefs().getHandleColor();
        FigCircle bigPort =
            new FigCircle(X0, Y0, WIDTH, HEIGHT, LINE_COLOR, FILL_COLOR);
        outCircle =
            new FigCircle(X0, Y0, WIDTH, HEIGHT, LINE_COLOR, FILL_COLOR);
        inCircle =
            new FigCircle(
        		  X0 + 5,
        		  Y0 + 5,
        		  WIDTH - 10,
        		  HEIGHT - 10,
        		  handleColor,
        		  LINE_COLOR);

        outCircle.setLineWidth(LINE_WIDTH);
        outCircle.setLineColor(LINE_COLOR);
        inCircle.setLineWidth(0);

        addFig(bigPort);
        addFig(outCircle);
        addFig(inCircle);
        setBigPort(bigPort);

        setBlinkPorts(false); //make port invisible unless mouse enters
    }

    /**
     * The constructor that hooks the Fig into the UML element.
     * @param gm ignored
     * @param node the UML element
     * @deprecated for 0.27.4 by tfmorris.  Use 
     * {@link #FigFinalState(Object, Rectangle, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigFinalState(@SuppressWarnings("unused") GraphModel gm, 
            Object node) {
    	this();
    	setOwner(node);
    }

    @Override
    public Object clone() {
        FigFinalState figClone = (FigFinalState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigCircle) it.next());
        figClone.outCircle = (FigCircle) it.next();
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
        outCircle.setLineColor(col);
        inCircle.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    @Override
    public Color getLineColor() {
        return outCircle.getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    @Override
    public void setFillColor(Color col) {
        if (Color.black.equals(col)) {
            /* See issue 5721. 
             * Projects before 0.28 have their fill color set to black.
             * We refuse that color and replace by white.
             * All other fill colors are accepted: */
            col = Color.white;
        }
        outCircle.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    @Override
    public Color getFillColor() {
        return outCircle.getFillColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    @Override
    public void setFilled(boolean f) {
        // ignored - rendering is fixed
    }

    
    @Override
    public boolean isFilled() {
        return true;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    @Override
    public void setLineWidth(int w) {
        outCircle.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    @Override
    public int getLineWidth() {
	return outCircle.getLineWidth();
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
        outCircle.setBounds(x, y, w, h);
        inCircle.setBounds(x + 5, y + 5, w - 10, h - 10);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

}
