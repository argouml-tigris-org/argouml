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
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;

import org.argouml.model.Model;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML MSynchState in a diagram.
 *
 * @author pepargouml@yahoo.es
 */
public class FigSynchState extends FigStateVertex {

    ////////////////////////////////////////////////////////////////
    // constants

    private static final int X = 10;
    private static final int Y = 10;
    private static final int WIDTH = 25;
    private static final int HEIGHT = 25;

    ////////////////////////////////////////////////////////////////
    // instance variables

    private FigText bound;
    private FigCircle head;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    public FigSynchState() {
        setEditable(false);

        setBigPort(new FigCircle(X, Y, WIDTH, HEIGHT, Color.cyan,
                Color.cyan));
        head = new FigCircle(X, Y, WIDTH, HEIGHT, Color.black, Color.white);

        bound = new FigText(X - 2, Y + 2, 0, 0, true);
        bound.setFilled(false);
        bound.setLineWidth(0);
        bound.setFont(getLabelFont());
        bound.setTextColor(Color.black);
        bound.setReturnAction(FigText.END_EDITING);
        bound.setTabAction(FigText.END_EDITING);
        bound.setJustificationByName("center");
        bound.setEditable(false);
        bound.setText("*");

        addFig(getBigPort());
        addFig(head);
        addFig(bound);

        setBlinkPorts(false); //make port invisble unless mouse enters
    }

    /**
     * The constructor.
     *
     * @param gm the graphmodel (not used)
     * @param node the UML object
     */
    public FigSynchState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /*
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigSynchState figClone = (FigSynchState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.head = (FigCircle) it.next();
        figClone.bound = (FigText) it.next();
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    /**
     * Synch states are fixed size.
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

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
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
    // event processing

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee.getPropertyName().equals("bound")) {
            if (getOwner() == null) {
                return;
            }
            int b = Model.getFacade().getBound(getOwner());
            String aux;
            if (b <= 0) {
                aux = "*";
            } else {
                aux = String.valueOf(b);
            }
            bound.setText(aux);
            updateBounds();
            damage();
        }
    }

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -7025279430656399031L;
} /* end class FigSynchState */
