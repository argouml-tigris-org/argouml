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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Iterator;

import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML MInstance in a diagram.
 * 
 * TODO: The UML element 'Instance' is abstract.  Shouldn't this
 * figure be too?  If not, it should only be used as a last resort
 * fallback if no more specialized type can be found. - tfm - 20070508
 *
 * @author agauthie
 */
public class FigInstance extends FigNodeModelElement {

    /** UML does not really use ports, so just define one big one so
     *  that users can drag edges to or from any point in the icon. */

    private FigText attr;

    // add other Figs here aes needed


    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor
     */
    public FigInstance() {

	getNameFig().setUnderline(true);
	getNameFig().setTextFilled(true);

	// initialize any other Figs here
	attr = new FigText(10, 30, 90, 40, Color.black, "Times", 10);
	attr.setFont(getLabelFont());
	attr.setExpandOnly(true);
	attr.setTextColor(Color.black);
	attr.setTabAction(FigText.END_EDITING);

	//_attr.setExpandOnly(true);
	attr.setJustification(FigText.JUSTIFY_LEFT);

	// add Figs to the FigNode in back-to-front order
	addFig(getBigPort());
	addFig(getNameFig());
	addFig(attr);

	setBlinkPorts(true); //make port invisble unless mouse enters
	Rectangle r = getBounds();
	setBounds(r.x, r.y, r.width, r.height);
    }

    /**
     * The constructor that hooks the Fig to the UML modelelement
     * @param gm ignored
     * @param node the UML element
     */
    public FigInstance(GraphModel gm, Object node) {
	this();
	setOwner(node);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() { return "new MInstance"; }

    /*
     * @see java.lang.Object#clone()
     */
    public Object clone() {
	FigInstance figClone = (FigInstance) super.clone();
	Iterator iter = figClone.getFigs().iterator();
	figClone.setBigPort((FigRect) iter.next());
	figClone.setNameFig((FigText) iter.next());
	figClone.attr = (FigText) iter.next();
	return figClone;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
	Dimension nameMin = getNameFig().getMinimumSize();
	Dimension attrMin = attr.getMinimumSize();

	int h = nameMin.height + attrMin.height;
	int w = Math.max(nameMin.width, attrMin.width);
	return new Dimension(w, h);
    }


    /* Override setBounds to keep shapes looking right
     *
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    protected void setBoundsImpl(int x, int y, int w, int h) {
	if (getNameFig() == null) return;
	Rectangle oldBounds = getBounds();

	Dimension nameMinimum = getNameFig().getMinimumSize();

	getNameFig().setBounds(x, y, w, nameMinimum.height);
	attr.setBounds(x, y + getNameFig().getBounds().height,
			w, h - getNameFig().getBounds().height);
	getBigPort().setBounds(x + 1, y + 1, w - 2, h - 2);

	calcBounds(); //_x = x; _y = y; _w = w; _h = h;
	updateEdges();
	firePropChange("bounds", oldBounds, getBounds());
    }


} /* end class FigInstance */
