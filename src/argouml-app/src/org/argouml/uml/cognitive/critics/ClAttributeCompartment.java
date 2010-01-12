/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

package org.argouml.uml.cognitive.critics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.ui.Clarifier;
import org.argouml.uml.diagram.ui.FigCompartment;
import org.argouml.uml.diagram.ui.FigCompartmentBox;
import org.tigris.gef.presentation.Fig;

/**
 * Class that represents the clarifier (red wavy line)
 * for a attribute compartment.
 *
 */
public class ClAttributeCompartment implements Clarifier {

    private static ClAttributeCompartment theInstance =
	new ClAttributeCompartment();

    private static final int WAVE_LENGTH = 4;
    private static final int WAVE_HEIGHT = 2;

    ////////////////////////////////////////////////////////////////
    // instance variables
    private Fig fig;

    /*
     * @see org.argouml.ui.Clarifier#setFig(org.tigris.gef.presentation.Fig)
     */
    public void setFig(Fig f) { fig = f; }

    /*
     * @see org.argouml.ui.Clarifier#setToDoItem(org.argouml.cognitive.ToDoItem)
     */
    public void setToDoItem(ToDoItem i) { }

    /*
     * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics,
     * int, int)
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        final Object modelElement = fig.getOwner();
        if (Model.getUmlFactory().isContainmentValid(
                Model.getMetaTypes().getAttribute(), modelElement)) {
            FigCompartmentBox fcb = (FigCompartmentBox) fig;
            FigCompartment fc =
                fcb.getCompartment(Model.getMetaTypes().getAttribute());

	    // added by Eric Lefevre 13 Mar 1999: we must check if the
	    // FigText for attributes is drawn before drawing things
	    // over it
	    if (fc == null || !fc.isVisible()) {
		fig = null;
		return;
	    }

	    Rectangle fr = fc.getBounds();
	    int left  = fr.x + 6;
	    int height = fr.y + fr.height - 5;
	    int right = fr.x + fr.width - 6;
	    g.setColor(Color.red);
	    int i = left;
	    while (true) {
		g.drawLine(i, height, i + WAVE_LENGTH, height + WAVE_HEIGHT);
		i += WAVE_LENGTH;
		if (i >= right) {
		    break;
		}
		g.drawLine(i, height + WAVE_HEIGHT, i + WAVE_LENGTH, height);
		i += WAVE_LENGTH;
		if (i >= right) {
		    break;
		}
		g.drawLine(i, height, i + WAVE_LENGTH,
			   height + WAVE_HEIGHT / 2);
		i += WAVE_LENGTH;
		if (i >= right) {
		    break;
		}
		g.drawLine(i, height + WAVE_HEIGHT / 2, i + WAVE_LENGTH,
			   height);
		i += WAVE_LENGTH;
		if (i >= right) {
		    break;
		}
	    }
	    fig = null;
	}
    }

    /*
     * @see javax.swing.Icon#getIconWidth()
     */
    public int getIconWidth() { return 0; }

    /*
     * @see javax.swing.Icon#getIconHeight()
     */
    public int getIconHeight() { return 0; }

    /*
     * @see org.argouml.ui.Clarifier#hit(int, int)
     */
    public boolean hit(int x, int y) {
	final Object modelElement = fig.getOwner();
        if (Model.getUmlFactory().isContainmentValid(
                Model.getMetaTypes().getAttribute(), modelElement)) {
            FigCompartmentBox fcb = (FigCompartmentBox) fig;
            FigCompartment fc =
                fcb.getCompartment(Model.getMetaTypes().getAttribute());
            Rectangle fr = fc.getBounds();
            fig = null;
            return fr.contains(x, y);
        }
        return false;
    }
    /**
     * @return Returns the theInstance.
     */
    public static ClAttributeCompartment getTheInstance() {
        return theInstance;
    }

} /* end class ClAttributeCompartment */
