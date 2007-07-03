// $Id: FigMNode.java 11516 2006-11-25 04:30:15Z tfmorris $
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

package org.argouml.uml.diagram.deployment.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Geometry;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCube;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML Node in a diagram.
 *
 * @author 5eichler
 */
public class FigMNode extends FigNodeModelElement {

    private int d = 20;
    ////////////////////////////////////////////////////////////////
    // instance variables

    private FigCube cover;

    private int x = 10;
    private int y = 10;
    private int width = 200;
    private int height = 180;
    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Main constructor - only directly used for file loading.
     */
    public FigMNode() {
        setBigPort(new CubePortFigRect(x, y - d, width + d, height + d, d));
        getBigPort().setFilled(false);
        getBigPort().setLineWidth(0);
        cover = new FigCube(x, y, width, height, Color.black, Color.white);

        d = 20;
        //d = cover.getDepth();

	getNameFig().setLineWidth(0);
	getNameFig().setFilled(false);
	getNameFig().setJustification(0);

	addFig(getBigPort());
	addFig(cover);
	addFig(getStereotypeFig());
	addFig(getNameFig());
    }

    /**
     * Constructor which hooks the new Fig into an existing UML element.
     *
     * @param gm ignored
     * @param node the UML element
     */
    public FigMNode(GraphModel gm, Object node) {
	this();
	setOwner(node);
	if (Model.getFacade().isAClassifier(node)
	        && (Model.getFacade().getName(node) != null)) {
	    getNameFig().setText(Model.getFacade().getName(node));
	}
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() { return "new Node"; }

    /*
     * @see java.lang.Object#clone()
     */
    public Object clone() {
	FigMNode figClone = (FigMNode) super.clone();
	Iterator it = figClone.getFigs().iterator();
	figClone.setBigPort((FigRect) it.next());
	figClone.cover = (FigCube) it.next();
	it.next();
	figClone.setNameFig((FigText) it.next());
	return figClone;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee instanceof AssociationChangeEvent 
                || mee instanceof AttributeChangeEvent) {
            renderingChanged();
            updateListeners(getOwner(), getOwner());
            damage();
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object)
     */
    protected void updateListeners(Object oldOwner, Object newOwner) {
        if (oldOwner != null) {
            removeAllElementListeners();
        }
        if (newOwner != null) {
            // add the listeners to the newOwner
            addElementListener(newOwner);
            Collection c = Model.getFacade().getStereotypes(newOwner);
            Iterator i = c.iterator();
            while (i.hasNext()) {
                Object st = i.next();
                addElementListener(st, "name");
            }
        }
    }

    /**
     * Build a collection of menu items relevant for a right-click popup menu.
     *
     * @param     me     a mouse event
     * @return           a collection of menu items
     *
     * @see org.tigris.gef.ui.PopupGenerator#getPopUpActions(java.awt.event.MouseEvent)
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        // Modifiers ...
        popUpActions.insertElementAt(
                buildModifierPopUp(ABSTRACT | LEAF | ROOT),
                popUpActions.size() - getPopupAddOffset());
        return popUpActions;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color c) {
	cover.setLineColor(c);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        cover.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return cover.getFilled();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean f) {
        cover.setFilled(f);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
	return new SelectionNode(this);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
	Dimension stereoDim = getStereotypeFig().getMinimumSize();
	Dimension nameDim = getNameFig().getMinimumSize();

	int w = Math.max(stereoDim.width, nameDim.width + 1);
	int h = stereoDim.height + nameDim.height - 4;
        w = Math.max(3 * d, w); // so it still looks like a cube
        h = Math.max(3 * d, h);
	return new Dimension(w, h);
    }

    /*
     * @see org.tigris.gef.presentation.FigNode#setBoundsImpl(int, int, int, int)
     */
    protected void setStandardBounds(int x, int y, int w, int h) {
	if (getNameFig() == null) {
	    return;
	}
	Rectangle oldBounds = getBounds();
	getBigPort().setBounds(x, y, w, h);
        cover.setBounds(x, y + d, w - d, h - d);

	Dimension stereoDim = getStereotypeFig().getMinimumSize();
	Dimension nameDim = getNameFig().getMinimumSize();
	getNameFig().setBounds(
                x + 4, y + d + stereoDim.height + 1,
	        w - d - 8, nameDim.height);
	getStereotypeFig().setBounds(x + 1, y + d + 1,
                w - d - 2, stereoDim.height);
	_x = x;
        _y = y;
        _w = w;
        _h = h;
	firePropChange("bounds", oldBounds, getBounds());
	updateEdges();
    }

    ////////////////////////////////////////////////////////////////
    // user interaction methods

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
	super.mouseClicked(me);
	setLineColor(Color.black);
    }


    /*
     * @see org.tigris.gef.presentation.Fig#setEnclosingFig(org.tigris.gef.presentation.Fig)
     */
    public void setEnclosingFig(Fig encloser) {
        if (encloser == null
                || (encloser != null
                && Model.getFacade().isANode(encloser.getOwner()))) {
            super.setEnclosingFig(encloser);
        }

        if (getLayer() != null) {
            // elementOrdering(figures);
            Collection contents = getLayer().getContents();
            Collection bringToFrontList = new ArrayList();
            Iterator it = contents.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof FigEdgeModelElement) {
                    bringToFrontList.add(o);

                }
            }
            Iterator bringToFrontIter = bringToFrontList.iterator();
            while (bringToFrontIter.hasNext()) {
                FigEdgeModelElement figEdge =
                        (FigEdgeModelElement) bringToFrontIter.next();
                figEdge.getLayer().bringToFront(figEdge);
            }
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     */
    protected void updateStereotypeText() {
        getStereotypeFig().setOwner(getOwner());
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    protected void textEditStarted(FigText ft) {
        if (ft == getNameFig()) {
            showHelp("parsing.help.fig-node");
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getUseTrapRect()
     */
    public boolean getUseTrapRect() { return true; }

    /*
     * @see org.tigris.gef.presentation.Fig#getClosestPoint(java.awt.Point)
     */
    public Point getClosestPoint(Point anotherPt) {
        Rectangle r = getBounds();
        int[] xs = {
            r.x,
            r.x + d,
            r.x + r.width,
            r.x + r.width,
            r.x + r.width - d,
            r.x,
            r.x,
        };
        int[] ys = {
            r.y + d,
            r.y,
            r.y,
            r.y + r.height - d,
            r.y + r.height,
            r.y + r.height,
            r.y + d,
        };
        Point p = Geometry.ptClosestTo(xs, ys, 7, anotherPt);
        return p;
    }

    /**
     * The UID.
     */
    static final long serialVersionUID = 8822005566372687713L;

} /* end class FigMNode */
