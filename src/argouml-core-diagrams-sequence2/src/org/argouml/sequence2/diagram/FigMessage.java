/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JSeparator;

import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.notation.SDNotationSettings;
import org.argouml.ui.ArgoJMenu;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigTextGroup;
import org.argouml.uml.diagram.ui.PathItemPlacement;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.ArrowHead;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.ArrowHeadTriangle;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.FigText;

/**
 * The Fig that represents a message between classifier roles.
 * @author penyaskito
 */
public class FigMessage extends FigEdgeModelElement {

    private static final long serialVersionUID = -2961220746360335159L;

    private static final Logger LOG =
        Logger.getLogger(FigEdgeModelElement.class.getName());

    private FigTextGroup textGroup;

    /**
     * The action owned by the message
     */
    private Object action = null;

    private SDNotationSettings notationSettings;
    private boolean t[] = {
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false
    };

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
        if (action != null) {
            addElementListener(action, "isAsynchronous");
        }
        t[1] = Model.getFacade().isASynchCallMessage(getOwner());
        t[2] = Model.getFacade().isAASynchCallMessage(getOwner());
        t[3] = Model.getFacade().isACreateMessage(getOwner());
        t[4] = Model.getFacade().isADeleteMessage(getOwner());
        t[5] = Model.getFacade().isAReplyMessage(getOwner());
        t[6] = Model.getFacade().isAASynchSignalMessage(getOwner());
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

    boolean isSynchCallMessage() {
    	return t[1];
    }

    boolean isASynchCallMessage() {
        return t[2];
    }

    boolean isCreateMessage() {
    	return t[3];
    }

    boolean isDeleteMessage() {
    	return t[4];
    }

    boolean isReplyMessage() {
    	return t[5];
    }

    boolean isASynchSignalMessage() {
        return t[6];
    }

    /**
     * Updates the arrow head and the arrow line according
     * to the action type..
     */
    private void updateArrow() {
        if (getOwner() == null) {
            return;
        }
        getFig().setDashed(isReplyMessage());
        final Object act = getAction();
        final ArrowHead arrowHead;
        if (act != null && Model.getFacade().isAsynchronous(getAction())) {
            arrowHead = new ArrowHeadGreater();
        } else {
            arrowHead = new ArrowHeadTriangle();
            getFig().setFillColor(getLineColor());
        }
        setDestArrowHead(arrowHead);
    }

    /**
     * Gets the action attached to the message.
     * @return the action
     */
    private Object getAction() {
        return action;
    }

    /**
     * @param me the MouseEvent that triggered the popup menu request
     * @return a Vector containing a combination of these 4 types: Action,
     *         JMenu, JMenuItem, JSeparator.
     */
    @Override
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);

        // Operations ...
        if (Model.getFacade().isACallAction(getAction())) {
            ArgoJMenu opMenu = buildOperationMenu();
            int index = popUpActions.size() - getPopupAddOffset() - 1;
            if (index < 0) {
                index = 0;
            }
            popUpActions.add(index, new JSeparator());
            popUpActions.add(index, opMenu);
        }

        return popUpActions;
    }

    protected ArgoJMenu buildOperationMenu() {
        ArgoJMenu opMenu = new ArgoJMenu("Operation");
        Iterator<Object> iter = getReceiverOperations().iterator();
        opMenu.setEnabled(iter.hasNext());
        while (iter.hasNext()) {
            Object op = iter.next();
            NotationProvider np = null;
            try {
                String s = getNotationSettings().getNotationLanguage();
                np = NotationProviderFactory2.getInstance()
                    .getNotationProvider(
                        NotationProviderFactory2.TYPE_OPERATION,
                        op,
                        Notation.findNotation(s));
            } catch (Exception e) {
                //TODO: add logging, but this will never happen and is handled
                np = null;
            }
            String label = (np != null)
                ? np.toString(op, getNotationSettings())
                : Model.getFacade().getName(op);
            opMenu.add(new ActionSetOperation(getAction(), op, label));
        }
        return opMenu;
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
            spline.setDashed(isReplyMessage());
            super.setFig(spline);
            computeRoute();
        }
    }

    @Override
    public void computeRouteImpl() {
	super.computeRouteImpl();
	updateActivations();
    }

    public void calcBounds() {
        final FigPoly fp = (FigPoly) getFig();
        final FigNode node = getSourceFigNode();
        if (node != null && isSelfMessage() && fp.isComplete()) {
            // TODO: calcBounds is called by SelectionManager when the Fig is
            // dragged. This code is needed to reposition any self message
            // as they are become detached from their classifier role
            // (see issue 5562). The cause of the detachment is not yet
            // understood.
            // Unfortunately calcBounds is called from several other places
            // so the code here is not optimal but is the best workaround until
            // ArgoUML can provide its own replacement SelectionManager for
            // sequence diagram requirements
            // See - http://gef.tigris.org/issues/show_bug.cgi?id=344
            final int x =
                node.getX()
                + (node.getWidth() + FigActivation.DEFAULT_WIDTH) / 2;
            final Point startPoint = new Point(x, getYs()[0]);
            final FigMessageSpline spline = new FigMessageSpline(startPoint);
            spline.setComplete(true);
            spline.setDashed(isReplyMessage());
            super.setFig(spline);
        }
        super.calcBounds();
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
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateLayout(org.argouml.model.UmlChangeEvent)
     */
    @Override
    public void updateLayout(UmlChangeEvent event) {
        if ("isAsynchronous".equals(event.getPropertyName())) {
            updateArrow();
        }
        super.updateLayout(event);
    }

    /*
     * Overridden purely to keep our superclass from removing the listener
     * that we just added.
     *
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateListeners(java.lang.Object, java.lang.Object)
     */
    @Override
    protected void updateListeners(Object oldOwner, Object newOwner ) {
        action = Model.getFacade().getAction(newOwner);
        Set<Object[]> listeners = new HashSet<Object[]>();
        Object action = getAction();
        listeners.add(new Object[] {getOwner(), "remove"});
        if (action != null) {
            listeners.add(new Object[] {action, "isAsynchronous"});
        }
        try {
            updateElementListeners(listeners);
        } catch (Exception e) {
            // This call seems not very robust. Yet to determine cause.
            LOG.log(Level.SEVERE, "Exception caught", e);
        }
    }

    private Collection<Object> getReceiverOperations() {
        ArrayList<Object> opList = new ArrayList<Object>();
        Object action = getAction();
	    Object receiver = Model.getFacade().getReceiver(getOwner());
        if (action != null && receiver != null) {
            //TODO: What can we do with other kind of actions?
            if (Model.getFacade().isACallAction(action)) {
                Iterator bases =
                    Model.getFacade().getBases(receiver).iterator();
                while (bases.hasNext()) {
                    Object base = bases.next();
                    opList.addAll(Model.getFacade().getOperations(base));
                }
            }
        }
        return opList;
    }
    /**
     * Determines the activator of this message based on the message position
     * in relation to other messages.
     * <p>Currently this only manages return messages. Any other message type
     * returns with no action taking place.
     * <p>The activator is set to the first call or create message found above
     * this message.
     * @return the activator that has been applied to the message.
     */
    public Object determineActivator() {
        final FigClassifierRole fcr = (FigClassifierRole) getSourceFigNode();
        final List<FigMessage> messageFigs = fcr.getFigMessages();
        final Iterator<FigMessage> it = messageFigs.iterator();
        Object activator = null;
        while (it.hasNext()) {
            FigMessage messageFig = it.next();
            if ((messageFig.isCreateMessage() || messageFig.isSynchCallMessage())
                    && messageFig.getDestFigNode() == fcr) {
                activator = messageFig.getOwner();
            } else if (messageFig == this) {
                Model.getCollaborationsHelper().setActivator(
                        getOwner(), activator);
                return activator;
            } else if (messageFig.isReplyMessage()) {
                activator = null;
            }
        }
        return null;
    }
}
