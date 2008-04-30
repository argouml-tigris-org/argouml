// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.uml.diagram.sequence2.ui;

import java.awt.Color;
import java.awt.Point;

import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigTextGroup;
import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.ArrowHead;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.ArrowHeadTriangle;
import org.tigris.gef.presentation.Fig;

/**
 * The Fig that represents a message between classifier roles.
 * @author penyaskito
 */
public class FigMessage extends FigEdgeModelElement {

    private static final long serialVersionUID = -2961220746360335159L;
    private FigTextGroup textGroup; 
    
    /**
     * The arrow head. It depends on the owner attached action. 
     */
    private ArrowHead arrowHead;
    
    /**
     * Contructs a new figlink and sets the owner of the figlink.
     *
     * @param owner is the owner.
     */
    public FigMessage(Object owner) {
        super();
        textGroup = new FigTextGroup();
        textGroup.addFig(getNameFig());
        textGroup.addFig(getStereotypeFig());
        addPathItem(textGroup, new PathConvPercent(this, 50, 10));        
        setOwner(owner);
    }
    
    @Override
    public void setOwner(Object owner) {       
        super.setOwner(owner);
        updateArrow();
    }

    /**
     * Updates the arrow head and the arrow line according
     * to the action type..
     */
    private void updateArrow() {
	Object action = getAction();
	if (Model.getFacade().isAReturnAction(action)) {
	    arrowHead = new ArrowHeadGreater();
	    getFig().setDashed(true);
	}
	else if (Model.getFacade().isADestroyAction(action)) {
	    arrowHead = new ArrowHeadGreater();
	    getFig().setDashed(true);
	}
	else if (Model.getFacade().isACreateAction(action)) {
	    arrowHead = new ArrowHeadTriangle();
	    // dashed it's false by default.
	}
	else if (Model.getFacade().isACallAction(action)) {
	    arrowHead = new ArrowHeadTriangle();
	    // dashed it's false by default.
	}

	setDestArrowHead(arrowHead);
    }

    /**
     * Constructor here for saving and loading purposes.
     *
     */
    public FigMessage() {
        this(null);
    }
    
    /**
     * Gets the action attached to the message.
     * @return the action
     */
    public Object getAction() {
        Object owner = getOwner();
        Object action = null;
        if (Model.getFacade().isAMessage(owner)) {          
            action = Model.getFacade().getAction(owner);
        }
        return action;
    }
 
    @Override
    public Selection makeSelection() {
        return new SelectionMessage(this);
    }
    
    /*
     * @see org.tigris.gef.presentation.FigEdge#setFig(org.tigris.gef.presentation.Fig)
     */
    public void setFig(Fig f) {        
        super.setFig(f);
        updateArrow();
    }
    
    int getFinalY() {
	int finalY = 0;
        Point[] points = getFig().getPoints();
        if (points.length > 0)
            finalY = points[points.length - 1].y;
        return finalY;
    }    

    int getStartY() {
	int finalY = 0;
        Point[] points = getFig().getPoints();
        if (points.length > 0)
            finalY = points[0].y;
        return finalY;
    }    

    
    /**
     * Checks if the message source and dest are the same.
     * @return true if they are the same, otherwise false. 
     */
    boolean isSelfMessage() {
        return getDestination().equals(getSource());
    }
    
    /**
     * Converts the message into a spline.  
     * This is needed for self-referencing messages.
     */
    public void convertToArc() {
        if (getPoints().length > 0) {
            FigMessageSpline spline = new FigMessageSpline(getPoint(0));
            super.setFig(spline);
            computeRoute();
        }        
    }

    @Override
    public void computeRouteImpl() {
	super.computeRouteImpl();
	updateActivations();
    }

    private synchronized void updateActivations() {
	// we update the activations...
	FigClassifierRole source = (FigClassifierRole) getSourceFigNode();
	if (source != null)
	    source.createActivations();
	
	// for performance, we check if this is a selfmessage 
	// if it is, we have just updated the activations
	if (!isSelfMessage()) {
	    FigClassifierRole dest = (FigClassifierRole) getDestFigNode();
	    if (dest != null)
		dest.createActivations();
	}
    }

    @Override
    public void setLineColor(Color c) {
    	super.setLineColor(c);
    	arrowHead.setFillColor(c);
    }

    @Override
    public void deleteFromModel() {
    	((FigClassifierRole) getSourceFigNode()).createActivations();
    	if (!isSelfMessage()) {
    	    ((FigClassifierRole) getDestFigNode()).createActivations();
    	}
    	super.deleteFromModel();
    }

    /**
     * The default behaviour from FigEdgeModelElement is not correct
     * here. See issue 5005. TODO: We must determine what to do here but for
     * now doing nothing is better. I'm not sure why the super method would
     * not work as I would expect that to do nothing if the ends are already
     * correct.
     * @return true at all times for now
     */
    @Override
    protected boolean determineFigNodes() {
        return true;
    }
}
