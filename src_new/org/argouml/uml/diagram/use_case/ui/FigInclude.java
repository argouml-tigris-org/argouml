// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// File: FigInclude.java
// Classes: FigInclude
// Original Author: mail@jeremybennett.com
// $Id$

// 3 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Written to support
// Include relationships.


package org.argouml.uml.diagram.use_case.ui;

import java.awt.Color;
import java.awt.Graphics;

import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MElementEvent;


/**
 * <p>A fig for use with include relationships on use case diagrams.</p>
 *
 * <p>Realised as a dotted line with an open arrow head and the label
 *   <<include>> alongside a la stereotype.</p>
 */

public class FigInclude extends FigEdgeModelElement {


    private FigText label = null; // the label for the stereotype

    private ArrowHeadGreater endArrow = new ArrowHeadGreater();
    
    /**
     * <p>The default constructor, but should never be called directly (use
     *   {@link #FigInclude(Object)}, since that sets the owner. However we
     *   can't mark it as private, since GEF expects to be able to call this
     *   when creating the diagram.</p>
     *
     * @deprecated As of ArgoUml version 0.9.8,
     *             use {@link #FigInclude(Object)}, since that sets the owner.
     */

    public FigInclude() {

        // We need a FigText to hold the <<include>> label. We DO NOT use the
        // stereotype generator for it since it's not a stereotype and using
        // the sterotype generator may cluther the model.

        label = new FigText(10, 30, 90, 20);

        label.setFont(LABEL_FONT);
        label.setTextColor(Color.black);
        label.setTextFilled(false);
        label.setFilled(false);
        label.setLineWidth(0);
        label.setExpandOnly(false);
        label.setMultiLine(false);
        label.setAllowsTab(false);
        label.setText("<<include>>");

        addPathItem(label, new PathConvPercent(this, 50, 10));

        // Make the line dashed

        setDashed(true);

        // Add an arrow with an open arrow head

        setDestArrowHead(endArrow);

        // Make the edge go between nearest points

        setBetweenNearestPoints(true);
    }


    /**
     * <p>The main constructor. Builds the FigEdge required and makes the given
     *   edge object its owner.</p>
     *
     * @param edge  The edge that will own the fig
     */

    public FigInclude(Object edge) {
        this();
        setOwner(edge);
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Accessors
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Set a new fig to represent this edge.</p>
     *
     * <p>We invoke the superclass accessor. Then change aspects of the
     *   new fig that are not as we want. In this case to use dashed lines.</p>
     *
     * @param f  The fig to use.
     */

    public void setFig(Fig f) {
        super.setFig(f);

        // Make sure the line is dashed

        setDashed(true);
    }

    /**
     * <p>Define whether the given fig can be edited (it can't).</p>
     *
     * @param f  The fig about which the enquiry is being made. Ignored in this
     *           implementation.
     *
     * @return   <code>false</code> under all circumstances.
     */

    protected boolean canEdit(Fig f) {
        return false;
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Event handlers
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>This is called aftern any part of the UML MModelElement has
     *   changed. This method automatically updates things specific to this
     *   fig. Subclasses should override and update other parts.</p>
     *
     * <p>This implementation does nothing.</p>
     */

    protected void modelChanged(MElementEvent e) { }

    public void paint(Graphics g) {
        endArrow.setLineColor(getLineColor());
        super.paint(g);
    }

} /* end class FigInclude */

