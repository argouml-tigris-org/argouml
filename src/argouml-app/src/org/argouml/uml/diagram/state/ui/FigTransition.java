/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram.state.ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.Action;

import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.PathItemPlacement;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewActionSequence;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewCallAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewCreateAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewDestroyAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewReturnAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewSendAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewTerminateAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewUninterpretedAction;
import org.argouml.uml.ui.behavior.state_machines.ButtonActionNewGuard;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;

/**
 * This class represents the graphical representation of a transition
 * on a Statechart diagram and an Activity diagram.
 */
public class FigTransition extends FigEdgeModelElement {

    private ArrowHeadGreater endArrow = new ArrowHeadGreater();

    /**
     * If <code>dashed</code> is true, then the transition represents
     * "object flow".
     * If the line is solid, then it represents "control flow".
     */
    private boolean dashed;
    
    /**
     * Constructor used by PGML parser.
     * 
     * @param owner owning uml element
     * @param settings rendering settings
     */
    public FigTransition(Object owner, DiagramSettings settings) {
        super(owner, settings);
        
        initializeTransition();
    }

    private void initializeTransition() {
        addPathItem(getNameFig(),
                new PathItemPlacement(this, getNameFig(), 50, 10));
        getFig().setLineColor(LINE_COLOR);
        setDestArrowHead(endArrow);
        allowRemoveFromDiagram(false);
        
        updateDashed();
    }

    @Override
    public void setLayer(Layer lay) {
        super.setLayer(lay);

        /* This presumes that the layer is set after the owner: */
        if (getLayer() != null && getOwner() != null) {
            initPorts(lay, getOwner());
        }
    }

    /**
     * Set the owners of the associated FigNodes to be the StateVertexes which
     * are at either end of the Transition.
     * <p>
     * TODO: This needs documentation! Is this really needed? Why?
     * 
     * @param lay diagram layer containing this fig
     * @param owner owning UML element
     * @deprecated in 0.28 by Bob Tarling - The above TODO is from Michiel.
     * I also don't understand the purpose of this method. The GEF framework
     * should be setting source/dest or persistence should manage.
     */
    @Deprecated
    private void initPorts(Layer lay, Object owner) {
        final Object sourceSV = Model.getFacade().getSource(owner);
        final FigNode sourceFN = (FigNode) lay.presentationFor(sourceSV);
        if (sourceFN != null) {
            // The purpose of this method is not explained and it give give
            // NPE depending on z order of figs as they are read. For now
            // ignore if null but for future lets delete this.
            setSourcePortFig(sourceFN);
            setSourceFigNode(sourceFN);
        }
        
        final Object destSV = Model.getFacade().getTarget(owner);
        final FigNode destFN = (FigNode) lay.presentationFor(destSV);
        if (destFN != null) {
            // The purpose of this method is not explained and it give give
            // NPE depending on z order of figs as they are read. For now
            // ignore if null but for future lets delete this.
            setDestPortFig(destFN);
            setDestFigNode(destFN);
        }
    }

    /*
     * The Transition has a name text box. It contains:
     * <ul>
     * <li>The event-signature
     * <li>The guard condition between []
     * <li>The action expression
     * </ul><p>
     *
     * The content of the text box is generated by its own notationProvider.
     * 
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getNotationProviderType()
     */
    @Override
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_TRANSITION;
    }

    @Override
    public void renderingChanged() {
        super.renderingChanged();
        updateDashed();
    }
    
    /**
     * The transition is dashed if connected to an 
     * ObjectFlowState (only for an Activity diagram).
     * This method updates the rendering of the transition on the diagram.
     */
    private void updateDashed() {
        if (Model.getFacade().isATransition(getOwner())) {
            dashed =
                Model.getFacade().isAObjectFlowState(
                        Model.getFacade().getSource(getOwner()))
                    || Model.getFacade().isAObjectFlowState(
                            Model.getFacade().getTarget(getOwner()));
            getFig().setDashed(dashed);
        }
    }

    @Override
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        /* Check if multiple items are selected: */
        boolean ms = TargetManager.getInstance().getTargets().size() > 1;
        /* None of the menu-items below apply
         * when multiple modelelements are selected:*/
        if (ms) {
            return popUpActions;
        }

        Action a;

        ArgoJMenu triggerMenu =
            new ArgoJMenu("menu.popup.trigger");
        a = new ButtonActionNewCallEvent();
        a.putValue(Action.NAME, a.getValue(Action.SHORT_DESCRIPTION));
        triggerMenu.add(a);
        a = new ButtonActionNewChangeEvent();
        a.putValue(Action.NAME, a.getValue(Action.SHORT_DESCRIPTION));
        triggerMenu.add(a);
        a = new ButtonActionNewSignalEvent();
        a.putValue(Action.NAME, a.getValue(Action.SHORT_DESCRIPTION));
        triggerMenu.add(a);
        a = new ButtonActionNewTimeEvent();
        a.putValue(Action.NAME, a.getValue(Action.SHORT_DESCRIPTION));
        triggerMenu.add(a);
        popUpActions.add(
                popUpActions.size() - getPopupAddOffset(),
                triggerMenu);

        a = new ButtonActionNewGuard();
        a.putValue(Action.NAME, a.getValue(Action.SHORT_DESCRIPTION));
        popUpActions.add(popUpActions.size() - getPopupAddOffset(), a);

        ArgoJMenu effectMenu =
            new ArgoJMenu("menu.popup.effect");
        a = ActionNewCallAction.getButtonInstance();
        a.putValue(Action.NAME, a.getValue(Action.SHORT_DESCRIPTION));
        effectMenu.add(a);
        a = ActionNewCreateAction.getButtonInstance();
        a.putValue(Action.NAME, a.getValue(Action.SHORT_DESCRIPTION));
        effectMenu.add(a);
        a = ActionNewDestroyAction.getButtonInstance();
        a.putValue(Action.NAME, a.getValue(Action.SHORT_DESCRIPTION));
        effectMenu.add(a);
        a = ActionNewReturnAction.getButtonInstance();
        a.putValue(Action.NAME, a.getValue(Action.SHORT_DESCRIPTION));
        effectMenu.add(a);
        a = ActionNewSendAction.getButtonInstance();
        a.putValue(Action.NAME, a.getValue(Action.SHORT_DESCRIPTION));
        effectMenu.add(a);
        a = ActionNewTerminateAction.getButtonInstance();
        a.putValue(Action.NAME, a.getValue(Action.SHORT_DESCRIPTION));
        effectMenu.add(a);
        a = ActionNewUninterpretedAction.getButtonInstance();
        a.putValue(Action.NAME, a.getValue(Action.SHORT_DESCRIPTION));
        effectMenu.add(a);
        a = ActionNewActionSequence.getButtonInstance();
        a.putValue(Action.NAME, a.getValue(Action.SHORT_DESCRIPTION));
        effectMenu.add(a);
        popUpActions.add(popUpActions.size() - getPopupAddOffset(), 
                effectMenu);

        return popUpActions;
    }

    /*
     * @see org.tigris.gef.presentation.FigEdge#setFig(org.tigris.gef.presentation.Fig)
     */
    @Override
    public void setFig(Fig f) {
        super.setFig(f);
        getFig().setDashed(dashed);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getDestination()
     */
    @Override
    protected Object getDestination() {
        if (getOwner() != null) {
            return Model.getStateMachinesHelper().getDestination(getOwner());
        }
        return null;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getSource()
     */
    @Override
    protected Object getSource() {
        if (getOwner() != null) {
            return Model.getStateMachinesHelper().getSource(getOwner());
        }
        return null;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        endArrow.setLineColor(getLineColor());
        super.paint(g);
    }
}
