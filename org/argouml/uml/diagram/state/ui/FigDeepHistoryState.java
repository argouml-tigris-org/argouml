// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: FigHistoryState.java
// Classes: FigHistoryState
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigText;

/** Class to display graphics for a UML MState in a diagram. */

public class FigDeepHistoryState extends FigStateVertex {

    ////////////////////////////////////////////////////////////////
    // constants

    private static final int MARGIN = 2;

    private static final int X = 0;

    private static final int Y = 0;

    private static final int WIDTH = 24;

    private static final int HEIGHT = 24;

    ////////////////////////////////////////////////////////////////
    // instance variables

    /** The main label on this icon. */
    private FigText name;
    private FigCircle head;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The main constructor 
     */
    public FigDeepHistoryState() {
        setBigPort(new FigCircle(X, Y, WIDTH, HEIGHT, Color.cyan, Color.cyan));
        head = new FigCircle(X, Y, WIDTH, HEIGHT, Color.black, Color.white);
        name = new FigText(X, Y, WIDTH - 10, HEIGHT - 10);
        name.setText("H*");
        name.setTextColor(Color.black);
        name.setFilled(false);
        name.setLineWidth(0);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(head);
        addFig(name);

        setBlinkPorts(false); //make port invisble unless mouse enters
        Rectangle r = getBounds();
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() {
        return "H*";
    }

    /**
     * The constructor which links the Fig into the existing UML element 
     * @param gm ignored
     * @param node the UML element
     */
    public FigDeepHistoryState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigDeepHistoryState figClone = (FigDeepHistoryState) super.clone();
        Iterator it = figClone.getFigs(null).iterator();
        figClone.setBigPort((FigCircle) it.next());
        figClone.head = (FigCircle) it.next();
        figClone.name = (FigText) it.next();
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    /** History states are fixed size. 
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
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return true;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        head.setLineWidth(w);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return head.getLineWidth();
    }

    ////////////////////////////////////////////////////////////////
    // Event handlers

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
    }

    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent ke) {
    }

    static final long serialVersionUID = 6572261327347541373L;

} /* end class FigDeepHistoryState */
