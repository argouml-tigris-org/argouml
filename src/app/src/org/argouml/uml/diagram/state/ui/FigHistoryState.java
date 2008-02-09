// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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
import java.util.Iterator;

import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML HistoryState in a diagram.
 *
 * This abstract class is used for both a DeepHistory and a ShallowHistory.
 *
 * @author jrobbins
 */
public abstract class FigHistoryState extends FigStateVertex {

    ////////////////////////////////////////////////////////////////
    // constants

    private static final int X = 10;
    private static final int Y = 10;
    private static final int WIDTH = 24;
    private static final int HEIGHT = 24;

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * The main label on this icon.
     */
    private FigText h;
    private FigCircle head;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Main constructor.
     */
    public FigHistoryState() {
        setEditable(false);
        setBigPort(new FigCircle(X, Y, WIDTH, HEIGHT, Color.cyan, Color.cyan));
        head = new FigCircle(X, Y, WIDTH, HEIGHT, Color.black, Color.white);
        h = new FigText(X, Y, WIDTH - 10, HEIGHT - 10);
        h.setText(getH());
        h.setTextColor(Color.black);
        h.setFilled(false);
        h.setLineWidth(0);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(head);
        addFig(h);

        setBlinkPorts(false); //make port invisble unless mouse enters
    }

    /**
     * This should return the text shown at the center of the history state.
     *
     * @return the text at the center (H or H*)
     */
    protected abstract String getH();

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() {
        return "H";
    }

    /**
     * The constructor that hooks the Fig into the UML modelelement.
     *
     * @param gm ignored
     * @param node the UML element
     */
    public FigHistoryState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /*
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigHistoryState figClone = (FigHistoryState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigCircle) it.next());
        figClone.head = (FigCircle) it.next();
        figClone.h = (FigText) it.next();
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    /**
     * History states are fixed size.
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

} /* end class FigHistoryState */
