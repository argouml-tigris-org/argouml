// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import org.argouml.model.ModelFacade;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;

/**
 * Class to display graphics for a UML MSynchState in a diagram.
 *
 * @author pepargouml@yahoo.es
 */

public class FigSynchState extends FigStateVertex {

    ////////////////////////////////////////////////////////////////
    // constants

    public final int MARGIN = 2;
    public int X = 10;
    public int Y = 10;
    public int WIDTH = 25;
    public int HEIGHT = 25;

    ////////////////////////////////////////////////////////////////
    // instance variables

    public FigText bound;
    private FigCircle head;

    ////////////////////////////////////////////////////////////////
    // constructors

    public FigSynchState() {

        setBigPort(new FigCircle(X, Y, WIDTH, HEIGHT, Color.cyan,
                Color.cyan));
        head = new FigCircle(X, Y, WIDTH, HEIGHT, Color.black, Color.white);

        bound = new FigText(X - 2, Y + 2, 0, 0, true);
        bound.setFilled(false);
        bound.setLineWidth(0);
        bound.setFont(getLabelFont());
        bound.setTextColor(Color.black);
        bound.setMultiLine(false);
        bound.setAllowsTab(false);
        bound.setJustificationByName("center");
        bound.setEditable(false);
        bound.setText("*");

        addFig(getBigPort());
        addFig(head);
        addFig(bound);

        setBlinkPorts(false); //make port invisble unless mouse enters
        Rectangle r = getBounds();
    }

    public FigSynchState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

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
     */
    public boolean isResizable() {
        return false;
    }

    /*public Selection makeSelection() {
        return new SelectionMoveClarifiers(this);
    }*/

    public void setLineColor(Color col) {
        head.setLineColor(col);
    }

    public Color getLineColor() {
        return head.getLineColor();
    }

    public void setFillColor(Color col) {
        head.setFillColor(col);
    }

    public Color getFillColor() {
        return head.getFillColor();
    }

    public void setFilled(boolean f) {
    }

    public boolean getFilled() {
        return true;
    }

    public void setLineWidth(int w) {
        head.setLineWidth(w);
    }

    public int getLineWidth() {
        return head.getLineWidth();
    }

    ////////////////////////////////////////////////////////////////
    // event processing

    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee.getPropertyName().equals("bound")) {
            if (getOwner() == null)
                return;
            int b = ModelFacade.getBound(getOwner());
            String aux;
            if (b <= 0)
                aux = "*";
            else
                aux = String.valueOf(b);
            bound.setText(aux);
            updateBounds();
            damage();
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
    }

    /**
     * Block any textentry on the diagram - there is nothing to edit!
     *
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent ke) {
    }

} /* end class FigSynchState */