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

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.application.api.Notation;
import org.argouml.model.ModelFacade;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.collaboration.ui.FigAssociationRole;
import org.argouml.uml.generator.ParserDisplay;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.FigText;

/** Class to display graphics for a UML message in a diagram.
 *
 *
 * @author agauthie
 */
public class FigMessage extends FigNodeModelElement {

    ////////////////////////////////////////////////////////////////
    // constants
    private static final int PADDING = 5;
    private static Vector arrowDirections = new Vector();

    ////////////////////////////////////////////////////////////////
    // instance variables

    private FigPoly figPoly;

    /**
     * The <code>arrowDirection</code> values are:
     * 1: South
     * 2: East
     * 3: West
     * 4: North
     */
    private int arrowDirection = 0;


    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The main constructor
     */
    public FigMessage() {
	getNameFig().setLineWidth(0);
	getNameFig().setMultiLine(false);
	getNameFig().setFilled(false);
	Dimension nameMin = getNameFig().getMinimumSize();
	getNameFig().setBounds(10, 10, 90, nameMin.height);

	figPoly = new FigPoly(Color.black, Color.black);
	int[] xpoints = {75, 75, 77, 75, 73, 75};
	int[] ypoints = {33, 24, 24, 15, 24, 24};
	Polygon polygon = new Polygon(xpoints, ypoints, 6);
	figPoly.setPolygon(polygon);
	figPoly.setBounds(100, 10, 5, 18);

	arrowDirections.addElement("North");
	arrowDirections.addElement("South");
	arrowDirections.addElement("East");
	arrowDirections.addElement("West");

	// add Figs to the FigNode in back-to-front order
	addFig(getNameFig());
	addFig(figPoly);

	Rectangle r = getBounds();
	setBounds(r.x, r.y, r.width, r.height);
    }

    /**
     * The constructor that hooks the Fig into an existing UML element
     * @param gm ignored
     * @param lay the layer
     * @param node the UML element
     */
    public FigMessage(GraphModel gm, Layer lay, Object node) {
	this();
	setLayer(lay);
	setOwner(node);
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() { return "new Message"; }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
	FigMessage figClone = (FigMessage) super.clone();
	Iterator it = figClone.getFigs().iterator();
	figClone.setNameFig((FigText) it.next());
	figClone.figPoly = (FigPoly) it.next();
	//figClone._polygon = (Polygon) _polygon.clone();
	return figClone;
    }



    ////////////////////////////////////////////////////////////////
    // Fig accessors

    /**
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
	figPoly.setLineColor(col);
	getNameFig().setLineColor(col);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() { return figPoly.getLineColor(); }

    /**
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
	//figPoly.setFillColor(col);
	getNameFig().setFillColor(col);
    }
    /**
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() { return getNameFig().getFillColor(); }

    /**
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean f) {  }

    /**
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() { return true; }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) { figPoly.setLineWidth(w); }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() { return figPoly.getLineWidth(); }

    /**
     * @param direction for the arrow
     * 1: South
     * 2: East
     * 3: West
     * 4: North
     */
    public void setArrow(int direction) {
	Rectangle bbox = getBounds();

	arrowDirection = direction;
	switch (direction) {
	    // south
	case 1: {
	    int[] xpoints = {75, 75, 77, 75, 73, 75};
	    int[] ypoints = {15, 24, 24, 33, 24, 24};
	    Polygon polygon = new Polygon(xpoints, ypoints, 6);
	    figPoly.setPolygon(polygon);
	    break;
	}
	    // east
	case 2: {
	    int[] xpoints = {66, 75, 75, 84, 75, 75};
	    int[] ypoints = {24, 24, 26, 24, 22, 24};
	    Polygon polygon = new Polygon(xpoints, ypoints, 6);
	    figPoly.setPolygon(polygon);
	    break;
	}
	    // west
	case 3: {
	    int[] xpoints = {84, 75, 75, 66, 75, 75};
	    int[] ypoints = {24, 24, 26, 24, 22, 24};
	    Polygon polygon = new Polygon(xpoints, ypoints, 6);
	    figPoly.setPolygon(polygon);
	    break;
	}
	    // north
	default: {
	    int[] xpoints = {75, 75, 77, 75, 73, 75};
	    int[] ypoints = {33, 24, 24, 15, 24, 24};
	    Polygon polygon = new Polygon(xpoints, ypoints, 6);
	    figPoly.setPolygon(polygon);
	}
	}
	setBounds(bbox);
    }

    /**
     * @return the arrow direction
     */
    public int getArrow() { return arrowDirection; }

    /**
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
	Dimension nameMin = getNameFig().getMinimumSize();
	Dimension figPolyMin = figPoly.getSize();

	int h = Math.max(figPolyMin.height, nameMin.height);
	int w = figPolyMin.width + nameMin.width;
	return new Dimension(w, h);
    }

    /** Override setBounds to keep shapes looking right
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    public void setBounds(int x, int y, int w, int h) {
	if (getNameFig() == null) {
	    return;
	}

	Rectangle oldBounds = getBounds();

	Dimension nameMin = getNameFig().getMinimumSize();

	int ht = 0;

	if (nameMin.height > figPoly.getHeight())
	    ht = (nameMin.height - figPoly.getHeight()) / 2;

	getNameFig().setBounds(x, y, w - figPoly.getWidth(), nameMin.height);
	figPoly.setBounds(x + getNameFig().getWidth(), y + ht,
			   figPoly.getWidth(), figPoly.getHeight());

	firePropChange("bounds", oldBounds, getBounds());
	calcBounds(); //_x = x; _y = y; _w = w; _h = h;
	updateEdges();
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) throws PropertyVetoException {
	Object message = getOwner();
	if (message != null && ft == getNameFig()) {
	    String s = ft.getText();
	    try {
		ParserDisplay.SINGLETON.parseMessage(message, s);
		ProjectBrowser.getInstance().getStatusBar().showStatus("");
	    } catch (ParseException pe) {
		ProjectBrowser.getInstance().getStatusBar()
		    .showStatus("Error: " + pe + " at " + pe.getErrorOffset());
	    }
	}
	else
	    super.textEdited(ft);
    }

    /**
     * Determines the direction of the message arrow. Deetermination of the
     * type of arrow happens in modelchanged
     */
    protected void updateArrow() {
  	Object mes = getOwner(); // MMessage
	if (mes == null || getLayer() == null) return;
	Object sender = ModelFacade.getSender(mes); // MClassifierRole
	Object receiver = ModelFacade.getReceiver(mes); // MClassifierRole
	Fig senderPort = getLayer().presentationFor(sender);
	Fig receiverPort = getLayer().presentationFor(receiver);
	if (senderPort == null || receiverPort == null) return;
	int sx = senderPort.getX();
	int sy = senderPort.getY();
	int rx = receiverPort.getX();
	int ry = receiverPort.getY();
	if (sx < rx && Math.abs(sy - ry) <= Math.abs(sx - rx)) { // east
	    setArrow(2);
	} else
	    if (sx > rx && Math.abs(sy - ry) <= Math.abs(sx - rx)) { // west
		setArrow(3);
	    } else
		if (sy < ry) { // south
		    setArrow(1);
		} else
		    setArrow(4);
    }

    /**
     * Add the FigMessage to the Path Items of its FigAssociationRole.
     * @param lay the Layer
     */
    public void addPathItemToFigAssociationRole(Layer lay) {

	Object associationRole =
	    ModelFacade.getCommunicationConnection(getOwner());
	if (associationRole != null && lay != null) {
	    FigAssociationRole figAssocRole =
		(FigAssociationRole) lay.presentationFor(associationRole);
	    if (figAssocRole != null) {
		figAssocRole.addMessage(this);
		lay.bringToFront(this);
	    }
	}
    }


    /**
     * @see org.tigris.gef.presentation.Fig#paint(Graphics)
     */
    public void paint(Graphics g) {
	updateArrow();
	super.paint(g);
    }



    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        Object mes =  getOwner();
        if (mes == null) return;
        getNameFig().setText(Notation.generate(this, mes));
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    public void renderingChanged() {
        super.renderingChanged();
        updateArrow();
    }

    /**
     * @return Returns the arrowDirections.
     */
    public static Vector getArrowDirections() {
        return arrowDirections;
    }
} /* end class FigMessage */
