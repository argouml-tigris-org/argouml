// $Id$
// Copyright (c) 2007-2009 The Regents of the University of California. All
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

package org.argouml.sequence2.diagram;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.Set;

import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.notation.SDNotationSettings;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigTextGroup;
import org.argouml.uml.diagram.ui.PathItemPlacement;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.ArrowHeadTriangle;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

/**
 * The Fig that represents a message between classifier roles.
 * @author penyaskito
 */
public class FigMessage extends FigEdgeModelElement {

    private static final long serialVersionUID = -2961220746360335159L;
    private FigTextGroup textGroup; 

    /**
     * The action owned by the message
     */
    private Object action = null;
    
    private SDNotationSettings notationSettings;
    
    /**
     * Constructs a new FigMessage and sets the owner of the FigMessage.
     *
     * @param owner is the owner.
     * @deprecated for 0.28.alpha3 by penyaskito. Use
     *             {@link #FigMessage(Object, DiagramSettings)}
     */
    public FigMessage(Object owner) {
        super();
        textGroup = new FigTextGroup();
        initialize();
        setOwner(owner);
    }

    /**
     * Construct a fig owned by the given UML element with the provided render
     * settings.
     * @param edge owning UML element
     * @param settings rendering settings
     */
    public FigMessage(Object edge, DiagramSettings settings) {
        super(edge, settings);
        textGroup = new FigTextGroup(edge, settings);
        initialize();
        action = Model.getFacade().getAction(getOwner());
        updateArrow();
        addElementListener(action, "isAsynchronous");
    }
    
    private void initialize() {
        textGroup.addFig(getNameFig());
        textGroup.addFig(getStereotypeFig());
        addPathItem(textGroup, new PathItemPlacement(this, textGroup, 50, 10));
        notationSettings = new SDNotationSettings();
    }
        
    @Override
    protected int getNotationProviderType() {
        /* Use a different notation as Messages on a collaboration diagram: */
        return NotationProviderFactory2.TYPE_SD_MESSAGE;
    }
    
    /* This next argument may be used to switch off 
     * the generation of sequence numbers - this is 
     * still to be implemented.
     * They are less desired in sequence diagrams, 
     * since they do not add any information. 
     * In collaboration diagrams they are needed, 
     * and they are still optional in sequence diagrams. */
    @Override
    protected void initNotationProviders(Object own) {
        super.initNotationProviders(own);
        notationSettings.setShowSequenceNumbers(false);
    }

    @Override
    protected void textEditStarted(FigText ft) {
        /* This is a temporary hack until the notation provider
         * for a SD Message will be able to parse successfully when the sequence
         * number is missing.
         * Remove this method completely then.*/
        notationSettings.setShowSequenceNumbers(true);
        super.textEditStarted(ft);
        notationSettings.setShowSequenceNumbers(false);
    }
    
    protected SDNotationSettings getNotationSettings() {
        return notationSettings;
    }

    /**
     * @deprecated for 0.28.alpha3 by penyaskito. Owner must be specified in the
     * constructor and can't be changed afterwards.
     */
    @Override
    @Deprecated
    public void setOwner(Object owner) {       
        super.setOwner(owner);
        action = Model.getFacade().getAction(owner);
        updateArrow();
    }
    
    boolean isCallAction() {
    	return Model.getFacade().isACallAction(getAction());
    }

    boolean isCreateAction() {
    	return Model.getFacade().isACreateAction(getAction());
    }

    boolean isDestroyAction() {
    	return Model.getFacade().isADestroyAction(getAction());
    }

    boolean isReturnAction() {
    	return Model.getFacade().isAReturnAction(getAction());
    }

    boolean isSendAction() {
        return Model.getFacade().isASendAction(getAction());
    }
    
    /**
     * Updates the arrow head and the arrow line according
     * to the action type..
     */
    private void updateArrow() {
        if (isReturnAction()) {
            getFig().setDashed(true);
        } else {
            getFig().setDashed(false);
        }
        Object act = getAction();
        if (act != null && Model.getFacade().isAsynchronous(getAction())) {
            setDestArrowHead(new ArrowHeadGreater());
        } else {
            setDestArrowHead(new ArrowHeadTriangle());            
        }
        getDestArrowHead().setLineColor(getLineColor());
	getDestArrowHead().setFillColor(getLineColor());
    }

    /**
     * Constructor here for saving and loading purposes.
     * @deprecated for 0.28.alpha3 by penyaskito. Use
     *             {@link #FigMessage(Object, DiagramSettings)}
     */
    public FigMessage() {
        this(null);
    }
    
    /**
     * Gets the action attached to the message.
     * @return the action
     */
    public Object getAction() {
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
        if (points.length > 0) {
            finalY = points[points.length - 1].y;
        }
        return finalY;
    }    

    int getStartY() {
	int finalY = 0;
        Point[] points = getFig().getPoints();
        if (points.length > 0) {
            finalY = points[0].y;
        }
        return finalY;
    }    

    
    /**
     * Checks if the message source and dest are the same.
     * @return true if they are the same, otherwise false. 
     */
    boolean isSelfMessage() {
        // If possible we determine this by checking the destination
        // and source Figs are the same. If this is not possible
        // because the edge is not yet connected then we check the
        // model.
        if (getDestPortFig() == null || getSourcePortFig() == null) {
            return getDestination().equals(getSource());
        } else {
            return (getDestPortFig().equals(getSourcePortFig()));
        }
    }
    
    /**
     * Converts the message into a spline.  
     * This is needed for self-referencing messages.
     */
    public void convertToArc() {
        if (getPoints().length > 0) {
            FigMessageSpline spline = new FigMessageSpline(getPoint(0));
            spline.setDashed(isReturnAction());
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
	if (source != null) {
	    source.createActivations();
	}
	
	// for performance, we check if this is a selfmessage 
	// if it is, we have just updated the activations
	if (!isSelfMessage()) {
	    FigClassifierRole dest = (FigClassifierRole) getDestFigNode();
	    if (dest != null) {
		dest.createActivations();
	    }
	}
    }

    @Override
    public void setLineColor(Color c) {
    	super.setLineColor(c);
    	getDestArrowHead().setFillColor(c);
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

    @Override
    public void translate(int dx, int dy) {
        if (isSelfMessage()) {
            ((FigMessageSpline) getFig()).translateFig(dx, dy);
        }        
        super.translate(dx, dy);
    }

    /**
     * {@inheritDoc}
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent pve) {
        if ("isAsynchronous".equals(pve.getPropertyName())) {
            updateArrow();
        }
        super.propertyChange(pve);
    }
    
    /* 
     * Overridden purely to keep our superclass from removing the listener
     * that we just added.
     * 
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateListeners(java.lang.Object, java.lang.Object)
     */
    @Override
    protected void updateListeners(Object o1, Object o2 ) {
        Set<Object[]> listeners = new HashSet<Object[]>();
        listeners.add(new Object[] {getOwner(), "remove"});
        listeners.add(new Object[] {getAction(), "isAsynchronous"});
        updateElementListeners(listeners);
    }
    
}
