// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

// $Id$
package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;

import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.presentation.ArrowHeadTriangle;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

/**
 * This class represents a Fig for a Realization.
 *
 *
 * @author agauthie
 */
public class FigRealization extends FigEdgeModelElement {

    ////////////////////////////////////////////////////////////////
    // constructors

    private ArrowHeadTriangle endArrow;

    /**
     * The constructor.
     *
     */
    public FigRealization() {
        addPathItem(getStereotypeFig(), new PathConvPercent(this, 50, 10));
        endArrow = new ArrowHeadTriangle();
        endArrow.setFillColor(Color.white);
        setDestArrowHead(endArrow);
        setBetweenNearestPoints(true);
        getFig().setDashed(true);
    }

    /**
     * The constructor.
     *
     * @param edge the owning UML element
     */
    public FigRealization(Object edge) {
        this();
        setOwner(edge);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @see org.tigris.gef.presentation.FigEdge#setFig(org.tigris.gef.presentation.Fig)
     */
    public void setFig(Fig f) {
        super.setFig(f);
        getFig().setDashed(true);
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#canEdit(org.tigris.gef.presentation.Fig)
     */
    protected boolean canEdit(Fig f) {
        return false;
    }

    /**
     * This is called after any part of the UML MModelElement has changed. This
     * method automatically updates the name FigText. Subclasses should override
     * and update other parts.
     *
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent e) {
        // do not set name
        //updateStereotypeText();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        endArrow.setLineColor(getLineColor());
        super.paint(g);
    }

} /* end class FigRealization */

