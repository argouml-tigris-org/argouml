// $Id$
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

// File: FigExtend.java
// Classes: FigExtend
// Original Author: mail@jeremybennett.com
// $Id$

// 3 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Written to support
// Extend relationships.


package org.argouml.uml.diagram.use_case.ui;

import java.awt.Color;
import java.awt.Graphics;

import org.argouml.application.api.Notation;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.foundation.data_types.MBooleanExpression;


/**
 * <p>A fig for use with extend relationships on use case diagrams.</p>
 *
 * <p>Realised as a dotted line with an open arrow head and the label
 *   &laquo;extend&raquo; together with any condition alongside.</p>
 */

public class FigExtend extends FigEdgeModelElement {

    /**
     * <p>The &laquo;extend&raquo; label.</p>
     */

    protected FigText _label;

    /**
     * <p>The condition expression.</p>
     */

    protected FigText _condition;

    /**
     * <p>The group of label and condition.</p>
     */

    protected FigGroup _fg;


    private ArrowHeadGreater endArrow = new ArrowHeadGreater();

    ///////////////////////////////////////////////////////////////////////////
    //
    // Constructors
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>The default constructor, but should never be called directly (use
     *   {@link #FigExtend(Object)}, since that sets the owner. However we
     *   can't mark it as private, since GEF expects to be able to call this
     *   when creating the diagram.</p>
     *
     * @deprecated As of ArgoUml version 0.9.8,
     *             use {@link #FigExtend(Object)}, since that sets the owner.
     */

    public FigExtend() {

        // We need a FigText to hold the <<extend>> label. Details are the
        // same as a stereotype, and we use the stereotype notation generator
        // to give us the text. When its all done, use calcBounds() to shrink
        // to size.

        _label = new FigText(10, 30, 90, 20);

        _label.setFont(LABEL_FONT);
        _label.setTextColor(Color.black);
        _label.setTextFilled(false);
        _label.setFilled(false);
        _label.setLineWidth(0);
        _label.setExpandOnly(false);
        _label.setMultiLine(false);
        _label.setAllowsTab(false);
        _label.setText("<<extend>>");
        

        _label.calcBounds();

        // We need a FigText to hold the condition. At this stage we have
        // nothing to put in it (since we have no owner). Place it immediately
        // below the label, and with the same height and width.

        _condition = new FigText(10, 30 + _label.getBounds().height,
                                _label.getBounds().width,
                                _label.getBounds().height);

        _condition.setFont(LABEL_FONT);
        _condition.setTextColor(Color.black);
        _condition.setTextFilled(false);
        _condition.setFilled(false);
        _condition.setLineWidth(0);
        _condition.setExpandOnly(false);
        _condition.setMultiLine(false);
        _condition.setAllowsTab(false);

        // Join the two into a group

        _fg = new FigGroup();

        _fg.addFig(_label);
        _fg.addFig(_condition);

        // Place in the middle of the line and ensure the line is dashed.  Add
        // an arrow with an open arrow head. Remember that for an extends
        // relationship, the arrow points to the base use case, but because of
        // the way we draw it, that is still the destination end.

        addPathItem(_fg, new PathConvPercent(this, 50, 10));

        setDashed(true);

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

    public FigExtend(Object edge) {
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
     * <p>This is called after any part of the UML MModelElement has
     *   changed. This method automatically updates things specific to this
     *   fig. Subclasses should override and update other parts.</p>
     *
     * <p>We reset the condition text. We really ought to check that there has
     *    actually been a change, but for now we do it every time. We do it
     *    within {@link #startTrans()} and {@link #endTrans()} so GEF will get
     *    the redrawing right.</p>
     */

    protected void modelChanged(MElementEvent e) {

        // Give up if we have no owner

        MExtend extend = (MExtend) getOwner();

        if (extend == null) {
            return;
        }
        
        // Let the superclass sort itself out, and then tell GEF we are going
        // to start something

        super.modelChanged(e);
        startTrans();

        // Now sort out the condition text. Use the null string if there is no
        // condition set. We call the main generate method, which will realise
        // this is a MExpression (subclass) and invoke the correct method.

        MBooleanExpression condition = extend.getCondition();

        if (condition == null) {
            _condition.setText("");
        }
        else {
            _condition.setText(Notation.generate(this, condition));
        }
        
        

        // Let the group recalculate its bounds and then tell GEF we've
        // finished.

        _fg.calcBounds();
        endTrans();
    }

    public void paint(Graphics g) {
        endArrow.setLineColor(getLineColor());
        super.paint(g);
    }
    

} /* end class FigExtend */

