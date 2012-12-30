/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.state.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.Model;
import org.argouml.model.StateDiagram;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.DiagramElement;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.activity.ui.FigActionState;
import org.argouml.uml.diagram.state.StateDiagramGraphModel;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.ui.ActionSetMode;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewActionSequence;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewCallAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewCreateAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewDestroyAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewReturnAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewSendAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewTerminateAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewUninterpretedAction;
import org.argouml.uml.ui.behavior.state_machines.ButtonActionNewGuard;
import org.argouml.util.ToolBarUtility;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.presentation.FigNode;


/**
 * The UML Statechart diagram. <p>
 *
 * The correct name for this class would be
 * "UMLStatechartDiagram". See issue 2306.
 */
public class UMLStateDiagram extends UMLDiagram implements StateDiagram {

    private static final long serialVersionUID = -1541136327444703151L;

    private static final Logger LOG =
        Logger.getLogger(UMLStateDiagram.class.getName());

    /**
     * this diagram needs to be deleted when its statemachine is deleted.
     */
    private Object theStateMachine;

    ////////////////
    // actions for toolbar

    private Action actionStubState;
    private Action actionState;
    private Action actionSynchState;
    private Action actionSubmachineState;
    private Action actionCompositeState;
    private Action actionStartPseudoState;
    private Action actionFinalPseudoState;
    private Action actionBranchPseudoState;
    private Action actionForkPseudoState;
    private Action actionJoinPseudoState;
    private Action actionShallowHistoryPseudoState;
    private Action actionDeepHistoryPseudoState;
    private Action actionCallEvent;
    private Action actionChangeEvent;
    private Action actionSignalEvent;
    private Action actionTimeEvent;
    private Action actionGuard;
    private Action actionCallAction;
    private Action actionCreateAction;
    private Action actionDestroyAction;
    private Action actionReturnAction;
    private Action actionSendAction;
    private Action actionTerminateAction;
    private Action actionUninterpretedAction;
    private Action actionActionSequence;
    private Action actionTransition;
    private Action actionJunctionPseudoState;

    /**
     * Constructor used by PGML parser to create a new diagram.  Use of
     * this constructor by other callers is deprecated.
     *
     * @deprecated for 0.27.3 by tfmorris.  Use
     * {@link #UMLStateDiagram(String, Object)}
     */
    @Deprecated
    public UMLStateDiagram() {
        super(new StateDiagramGraphModel());
        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) {
            // nothing we can do about veto, so just ignore it
        }
    }

    /**
     * Constructor.
     *
     * @param name the name of the diagram
     * @param machine the StateMachine for the new diagram
     */
    public UMLStateDiagram(String name, Object machine) {
        super(name, machine, new StateDiagramGraphModel());

        if (!Model.getFacade().isAStateMachine(machine)) {
            throw new IllegalStateException(
                "No StateMachine given to create a Statechart diagram");
        }
        namespace = getNamespaceFromMachine(machine);
        if (!Model.getFacade().isANamespace(namespace)) {
            throw new IllegalArgumentException();
        }

        nameDiagram(namespace);
        setup(namespace, machine);
    }


    /**
     * Constructor.
     *
     * @param ns the NameSpace for the new diagram
     * @param machine the StateMachine for the new diagram
     * @deprecated for 0.27.3 by tfmorris. Use
     *             {@link #UMLStateDiagram(String, Object)}/
     */
    @Deprecated
    public UMLStateDiagram(Object ns, Object machine) {
        this();

        if (!Model.getFacade().isAStateMachine(machine)) {
            throw new IllegalStateException(
                "No StateMachine given to create a Statechart diagram");
        }
        if (ns == null) {
            ns = getNamespaceFromMachine(machine);
        }
        if (!Model.getFacade().isANamespace(ns)) {
            throw new IllegalArgumentException();
        }

        nameDiagram(ns);
        setup(ns, machine);
    }

    /**
     * Name the diagram based on the name of its namespace and the number of
     * behaviors that it contains.
     * @param ns containing namespace
     */
    private void nameDiagram(Object ns) {
        String nname = Model.getFacade().getName(ns);
        if (nname != null && nname.trim().length() != 0) {
            int number = (Model.getFacade().getBehaviors(ns)) == null ? 0
                    : Model.getFacade().getBehaviors(ns).size();
            String name = nname + " " + (number++);

            LOG.log(Level.INFO,
                    "UMLStateDiagram constructor: String name = {0}",
                    name);

            try {
                setName(name);
            } catch (PropertyVetoException pve) {
                // nothing we can do about veto, so just ignore it
            }
        }

    }

    /**
     * From a given StateMachine, find the Namespace.
     * Guaranteed to give a non-null result.
     *
     * @param machine the given StateMachine.
     *          If not a StateMachine: throws exception
     * @return the best possible namespace to be deducted
     */
    private Object getNamespaceFromMachine(Object machine) {
        if (!Model.getFacade().isAStateMachine(machine)) {
            throw new IllegalStateException(
                "No StateMachine given to create a Statechart diagram");
        }

        Object ns = Model.getFacade().getNamespace(machine);
        if (ns != null) {
            return ns;
        }

        Object context = Model.getFacade().getContext(machine);
        if (Model.getFacade().isAClassifier(context)) {
            ns = context;
        } else if (Model.getFacade().isABehavioralFeature(context)) {
            ns = Model.getFacade().getNamespace( // or just the owner?
                    Model.getFacade().getOwner(context));
        }
        if (ns == null) {
            ns = getProject().getRoots().iterator().next();
        }
        if (ns == null || !Model.getFacade().isANamespace(ns)) {
            throw new IllegalStateException(
                    "Can not deduce a Namespace from a StateMachine");
        }
        return ns;
    }

    /**
     * Get the owner of a statechart diagram.
     * @return the statemachine which owns the diagram
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getOwner()
     */
    @Override
    public Object getOwner() {
        if (!(getGraphModel() instanceof StateDiagramGraphModel)) {
            throw new IllegalStateException(
                    "Incorrect graph model of "
                    + getGraphModel().getClass().getName());
        }
        StateDiagramGraphModel gm = (StateDiagramGraphModel) getGraphModel();
        return gm.getMachine();
    }

    /**
     * Called by the PGML parser to initialize the statechart
     * diagram. First the parser creates a statechart diagram via the
     * default constructor. Then this method is called.
     *
     * @param o the statemachine
     * @see org.tigris.gef.base.Diagram#initialize(Object)
     */
    @Override
    public void initialize(Object o) {
        if (Model.getFacade().isAStateMachine(o)) {
            Object machine = o;
            Object contextNamespace = getNamespaceFromMachine(machine);

            setup(contextNamespace, machine);
        } else {
            throw new IllegalStateException(
                "Cannot find namespace "
                    + "while initializing "
                    + "statechart diagram");
        }
    }

    /**
     * Method to perform a number of important initializations of a
     * StateDiagram.<p>
     *
     * Each diagram type has a similar <I>UMLxxxDiagram</I> class.<p>
     *
     * Changed <I>lay</I> from <I>LayerPerspective</I> to
     * <I>LayerPerspectiveMutable</I>.  This class is a child of
     * <I>LayerPerspective</I> and was implemented to correct some
     * difficulties in changing the model. <I>lay</I> is used mainly
     * in <I>LayerManager</I>(GEF) to control the adding, changing and
     * deleting layers on the diagram...
     *
     * @param namespace Class from the UML model...connects the class to
     * the Statechart diagram.
     * @param machine StateMachine from the UML model
     * @author psager@tigris.org Jan. 24, 2oo2
     */
    public void setup(Object namespace, Object machine) {
        setNamespace(namespace);

        theStateMachine = machine;

        StateDiagramGraphModel gm = createGraphModel();
        gm.setHomeModel(namespace);
        if (theStateMachine != null) {
            gm.setMachine(theStateMachine);
        }
        StateDiagramRenderer rend = new StateDiagramRenderer(); // singleton

        LayerPerspective lay = new LayerPerspectiveMutable(
                Model.getFacade().getName(namespace), gm);
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
        setLayer(lay);

        /* Listen to machine deletion,
         * to delete the diagram. */
        Model.getPump().addModelEventListener(this, theStateMachine,
                new String[] {"remove", "namespace"});
    }


    // TODO: Needs to be tidied up after stable release. Graph model
    // should be created in constructor
    private StateDiagramGraphModel createGraphModel() {
        if ((getGraphModel() instanceof StateDiagramGraphModel)) {
            return (StateDiagramGraphModel) getGraphModel();
        } else {
            return new StateDiagramGraphModel();
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ((evt.getSource() == theStateMachine)
                && (evt instanceof DeleteInstanceEvent)
                && "remove".equals(evt.getPropertyName())) {
            Model.getPump().removeModelEventListener(this,
                    theStateMachine, new String[] {"remove", "namespace"});
            if (getProject() != null) {
                getProject().moveToTrash(this);
            } else {
                DiagramFactory.getInstance().removeDiagram(this);
            }
        }
        if (evt.getSource() == theStateMachine
                && "namespace".equals(evt.getPropertyName())) {
            Object newNamespace = evt.getNewValue();
            if (newNamespace != null // this in case we are being deleted
                    && getNamespace() != newNamespace) {
                /* The namespace of the statemachine is changed! */
                setNamespace(newNamespace);
                ((UMLMutableGraphSupport) getGraphModel())
                                .setHomeModel(newNamespace);
            }
        }
    }

    /**
     * @return the StateMachine belonging to this diagram
     */
    public Object getStateMachine() {
        return ((StateDiagramGraphModel) getGraphModel()).getMachine();
    }

    /**
     * @param sm Set the StateMachine for this diagram.
     */
    public void setStateMachine(Object sm) {

        if (!Model.getFacade().isAStateMachine(sm)) {
            throw new IllegalArgumentException("This is not a StateMachine");
        }

        ((StateDiagramGraphModel) getGraphModel()).setMachine(sm);
    }

    /**
     * Get the actions from which to create a toolbar or equivalent
     * graphic triggers.
     * @return the array of actions
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getUmlActions()
     */
    protected Object[] getUmlActions() {

        ArrayList actions = new ArrayList();

        actions.add(getActionState());
        actions.add(getActionCompositeState());
        actions.add(getActionTransition());
        actions.add(getActionSynchState());
        actions.add(getActionSubmachineState());
        actions.add(getActionStubState());
        actions.add(null);
        actions.add(getActionStartPseudoState());
        actions.add(getActionFinalPseudoState());
        actions.add(getActionJunctionPseudoState());
        actions.add(getActionChoicePseudoState());
        actions.add(getActionForkPseudoState());
        actions.add(getActionJoinPseudoState());
        actions.add(getActionShallowHistoryPseudoState());
        actions.add(getActionDeepHistoryPseudoState());
        actions.add(null);
        actions.add(getTriggerActions());
        actions.add(getActionGuard());
        actions.add(getEffectActions());

        return actions.toArray();
    }

    protected Object[] getTriggerActions() {
        Object[] actions = {
            getActionCallEvent(),
            getActionChangeEvent(),
            getActionSignalEvent(),
            getActionTimeEvent(),
        };
        ToolBarUtility.manageDefault(actions, "diagram.state.trigger");
        return actions;
    }

    protected Object[] getEffectActions() {
        Object[] actions = {
            getActionCallAction(),
            getActionCreateAction(),
            getActionDestroyAction(),
            getActionReturnAction(),
            getActionSendAction(),
            getActionTerminateAction(),
            getActionUninterpretedAction(),
            getActionActionSequence(),
        };
        ToolBarUtility.manageDefault(actions, "diagram.state.effect");
        return actions;
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getLabelName()
     */
    public String getLabelName() {
        return Translator.localize("label.state-chart-diagram");
    }

    /**
     * @return Returns the actionChoicePseudoState.
     */
    protected Action getActionChoicePseudoState() {
        if (actionBranchPseudoState == null) {
            actionBranchPseudoState = new RadioAction(
                    new ActionCreatePseudostate(Model.getPseudostateKind()
                            .getChoice(), "button.new-choice"));
        }
        return actionBranchPseudoState;
    }

    /**
     * @return Returns the actionCompositeState.
     */
    protected Action getActionCompositeState() {
        if (actionCompositeState == null) {
            actionCompositeState =
                new RadioAction(new CmdCreateNode(
                        Model.getMetaTypes().getCompositeState(),
                        "button.new-compositestate"));
        }
        return actionCompositeState;
    }
    /**
     * @return Returns the actionDeepHistoryPseudoState.
     */
    protected Action getActionDeepHistoryPseudoState() {
        if (actionDeepHistoryPseudoState == null) {
            actionDeepHistoryPseudoState = new RadioAction(
                    new ActionCreatePseudostate(
                        Model.getPseudostateKind().getDeepHistory(),
                        "button.new-deephistory"));
        }
        return actionDeepHistoryPseudoState;
    }
    /**
     * @return Returns the actionFinalPseudoState.
     */
    protected Action getActionFinalPseudoState() {
        if (actionFinalPseudoState == null) {
            actionFinalPseudoState =
                new RadioAction(
                        new CmdCreateNode(
                                Model.getMetaTypes().getFinalState(),
                                "button.new-finalstate"));
        }
        return actionFinalPseudoState;
    }
    /**
     * @return Returns the actionForkPseudoState.
     */
    protected Action getActionForkPseudoState() {
        if (actionForkPseudoState == null) {
            actionForkPseudoState = new RadioAction(
                    new ActionCreatePseudostate(
                            Model.getPseudostateKind()
                            .getFork(), "button.new-fork"));
        }
        return actionForkPseudoState;
    }
    /**
     * @return Returns the actionJoinPseudoState.
     */
    protected Action getActionJoinPseudoState() {
        if (actionJoinPseudoState == null) {
            actionJoinPseudoState = new RadioAction(new ActionCreatePseudostate(
                    Model.getPseudostateKind().getJoin(), "button.new-join"));
        }
        return actionJoinPseudoState;
    }
    /**
     * @return Returns the actionJunctionPseudoState.
     */
    protected Action getActionJunctionPseudoState() {
        if (actionJunctionPseudoState == null) {
            actionJunctionPseudoState = new RadioAction(
                    new ActionCreatePseudostate(
                        Model.getPseudostateKind().getJunction(),
                        "button.new-junction"));
        }
        return actionJunctionPseudoState;
    }
    /**
     * @return Returns the actionShallowHistoryPseudoState.
     */
    protected Action getActionShallowHistoryPseudoState() {
        if (actionShallowHistoryPseudoState == null) {
            actionShallowHistoryPseudoState = new RadioAction(
                    new ActionCreatePseudostate(
                        Model.getPseudostateKind().getShallowHistory(),
                        "button.new-shallowhistory"));
        }
        return actionShallowHistoryPseudoState;
    }
    /**
     * @return Returns the actionStartPseudoState.
     */
    protected Action getActionStartPseudoState() {
        if (actionStartPseudoState == null) {
            actionStartPseudoState = new RadioAction(
                    new ActionCreatePseudostate(
                        Model.getPseudostateKind().getInitial(),
                        "button.new-initial"));
        }
        return actionStartPseudoState;
    }
    /**
     * @return Returns the actionState.
     */
    protected Action getActionState() {
        if (actionState == null) {
            actionState =
                new RadioAction(
                        new CmdCreateNode(Model.getMetaTypes().getSimpleState(),
                                          "button.new-simplestate"));
        }
        return actionState;
    }

    /**
     * @return Returns the actionSynchState.
     */
    protected Action getActionSynchState() {
        if (actionSynchState == null) {
            actionSynchState =
                new RadioAction(
                        new CmdCreateNode(
                                Model.getMetaTypes().getSynchState(),
                                "button.new-synchstate"));
        }
        return actionSynchState;
    }

    /**
     * @return Returns the actionSubmachineState.
     */
    protected Action getActionSubmachineState() {
        if (actionSubmachineState == null) {
            actionSubmachineState =
                    new RadioAction(
                            new CmdCreateNode(
                                    Model.getMetaTypes().getSubmachineState(),
                                    "button.new-submachinestate"));
        }
        return actionSubmachineState;
    }

    /**
     * @return Returns the actionSubmachineState.
     */
    protected Action getActionStubState() {
        if (actionStubState == null) {
            actionStubState =
                    new RadioAction(
                            new CmdCreateNode(
                                    Model.getMetaTypes().getStubState(),
                                    "button.new-stubstate"));
        }
        return actionStubState;
    }

    /**
     * @return Returns the actionTransition.
     */
    protected Action getActionTransition() {
        if (actionTransition == null) {
            actionTransition = new RadioAction(
                    new ActionSetMode(
                        ModeCreatePolyEdge.class,
                        "edgeClass",
                        Model.getMetaTypes().getTransition(),
                        "button.new-transition"));
        }
        return actionTransition;
    }

    /**
     * @return Returns the actionCallEvent.
     */
    protected Action getActionCallEvent() {
        if (actionCallEvent == null) {
            actionCallEvent = new ButtonActionNewCallEvent();
        }
        return actionCallEvent;
    }

    /**
     * @return Returns the actionCallEvent.
     */
    protected Action getActionChangeEvent() {
        if (actionChangeEvent == null) {
            actionChangeEvent = new ButtonActionNewChangeEvent();
        }
        return actionChangeEvent;
    }

    /**
     * @return Returns the actionCallEvent.
     */
    protected Action getActionSignalEvent() {
        if (actionSignalEvent == null) {
            actionSignalEvent = new ButtonActionNewSignalEvent();
        }
        return actionSignalEvent;
    }

    /**
     * @return Returns the actionCallEvent.
     */
    protected Action getActionTimeEvent() {
        if (actionTimeEvent == null) {
            actionTimeEvent = new ButtonActionNewTimeEvent();
        }
        return actionTimeEvent;
    }

    protected Action getActionGuard() {
        if (actionGuard == null) {
            actionGuard = new ButtonActionNewGuard();
        }
        return actionGuard;
    }

    protected Action getActionCallAction() {
        if (actionCallAction == null) {
            actionCallAction = ActionNewCallAction.getButtonInstance();
        }
        return actionCallAction;
    }

    protected Action getActionCreateAction() {
        if (actionCreateAction == null) {
            actionCreateAction = ActionNewCreateAction.getButtonInstance();
        }
        return actionCreateAction;
    }

    protected Action getActionDestroyAction() {
        if (actionDestroyAction == null) {
            actionDestroyAction = ActionNewDestroyAction.getButtonInstance();
        }
        return actionDestroyAction;
    }

    protected Action getActionReturnAction() {
        if (actionReturnAction == null) {
            actionReturnAction = ActionNewReturnAction.getButtonInstance();
        }
        return actionReturnAction;
    }

    protected Action getActionSendAction() {
        if (actionSendAction == null) {
            actionSendAction = ActionNewSendAction.getButtonInstance();
        }
        return actionSendAction;
    }

    protected Action getActionTerminateAction() {
        if (actionTerminateAction == null) {
            actionTerminateAction =
                ActionNewTerminateAction.getButtonInstance();
        }
        return actionTerminateAction;
    }

    protected Action getActionUninterpretedAction() {
        if (actionUninterpretedAction == null) {
            actionUninterpretedAction =
                ActionNewUninterpretedAction.getButtonInstance();
        }
        return actionUninterpretedAction;
    }


    protected Action getActionActionSequence() {
        if (actionActionSequence == null) {
            actionActionSequence =
                ActionNewActionSequence.getButtonInstance();
        }
        return actionActionSequence;
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getDependentElement()
     */
    @Override
    public Object getDependentElement() {
        return getStateMachine();
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#isRelocationAllowed(java.lang.Object)
     */
    public boolean isRelocationAllowed(Object base)  {
        return false;
        /* TODO: We may return the following when the
         * relocate() has been implemented. */
//      Model.getStateMachinesHelper()
//              .isAddingStatemachineAllowed(base);
    }

    @SuppressWarnings("unchecked")
    public Collection getRelocationCandidates(Object root) {
        /* TODO: We may return something useful when the
         * relocate() has been implemented, like
         * all StateMachines that are not ActivityGraphs. */
        Collection c =  new HashSet();
        c.add(getOwner());
        return c;
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#relocate(java.lang.Object)
     */
    public boolean relocate(Object base) {
        return false;
    }

    public void encloserChanged(FigNode enclosed,
            FigNode oldEncloser, FigNode newEncloser) {
        // Do nothing.
    }

    @Override
    public boolean doesAccept(Object objectToAccept) {
        if (Model.getFacade().isAState(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isASynchState(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAStubState(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAPseudostate(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAComment(objectToAccept)) {
            return true;
        }
        return false;
    }

    @Override
    public DiagramElement drop(Object droppedObject, Point location) {
        FigNodeModelElement figNode = null;

        // If location is non-null, convert to a rectangle that we can use
        Rectangle bounds = null;
        if (location != null) {
            bounds = new Rectangle(location.x, location.y, 0, 0);
        }
        DiagramSettings settings = getDiagramSettings();

        if (Model.getFacade().isAActionState(droppedObject)) {
            figNode = new FigActionState(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAFinalState(droppedObject)) {
            figNode = new FigFinalState(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAStubState(droppedObject)) {
            figNode = new FigStubState(droppedObject, bounds, settings);
        } else if (Model.getFacade().isASubmachineState(droppedObject)) {
            figNode = new FigSubmachineState(droppedObject, bounds, settings);
        } else if (Model.getFacade().isACompositeState(droppedObject)) {
            figNode = new FigCompositeState(droppedObject, bounds, settings);
        } else if (Model.getFacade().isASynchState(droppedObject)) {
            figNode = new FigSynchState(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAState(droppedObject)) {
            figNode = new FigSimpleState(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAComment(droppedObject)) {
            figNode = new FigComment(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAPseudostate(droppedObject)) {
            Object kind = Model.getFacade().getKind(droppedObject);
            if (kind == null) {
                LOG.log(Level.WARNING, "found a null type pseudostate");
                return null;
            }
            if (kind.equals(Model.getPseudostateKind().getInitial())) {
                figNode = new FigInitialState(droppedObject, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getChoice())) {
                figNode = new FigBranchState(droppedObject, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getJunction())) {
                figNode = new FigJunctionState(droppedObject, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getFork())) {
                figNode = new FigForkState(droppedObject, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getJoin())) {
                figNode = new FigJoinState(droppedObject, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getShallowHistory())) {
                figNode = new FigShallowHistoryState(droppedObject, bounds,
                        settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getDeepHistory())) {
                figNode = new FigDeepHistoryState(droppedObject, bounds,
                        settings);
            } else {
                LOG.log(Level.WARNING, "found a type not known");
            }
        }

        if (figNode != null) {
            // if location is null here the position of the new figNode is set
            // after in org.tigris.gef.base.ModePlace.mousePressed(MouseEvent e)
            if (location != null) {
                figNode.setLocation(location.x, location.y);
            }
            LOG.log(Level.FINE,
                    "Dropped object {0} converted to {1}",
                    new Object[]{droppedObject, figNode});
        } else {
            LOG.log(Level.FINE, "Dropped object NOT added {0}", figNode);
        }

        return figNode;
    }


    public DiagramElement createDiagramElement(
            final Object modelElement,
            final Rectangle bounds) {

        FigNodeModelElement figNode = null;

        DiagramSettings settings = getDiagramSettings();

        if (Model.getFacade().isAActionState(modelElement)) {
            figNode = new FigActionState(modelElement, bounds, settings);
        } else if (Model.getFacade().isAFinalState(modelElement)) {
            figNode = new FigFinalState(modelElement, bounds, settings);
        } else if (Model.getFacade().isAStubState(modelElement)) {
            figNode = new FigStubState(modelElement, bounds, settings);
        } else if (Model.getFacade().isASubmachineState(modelElement)) {
            figNode = new FigSubmachineState(modelElement, bounds, settings);
        } else if (Model.getFacade().isACompositeState(modelElement)) {
            figNode = new FigCompositeState(modelElement, bounds, settings);
        } else if (Model.getFacade().isASynchState(modelElement)) {
            figNode = new FigSynchState(modelElement, bounds, settings);
        } else if (Model.getFacade().isAState(modelElement)) {
            figNode = new FigSimpleState(modelElement, bounds, settings);
        } else if (Model.getFacade().isAComment(modelElement)) {
            figNode = new FigComment(modelElement, bounds, settings);
        } else if (Model.getFacade().isAPseudostate(modelElement)) {
            Object kind = Model.getFacade().getKind(modelElement);
            if (kind == null) {
                LOG.log(Level.WARNING, "found a null type pseudostate");
                return null;
            }
            if (kind.equals(Model.getPseudostateKind().getInitial())) {
                figNode = new FigInitialState(modelElement, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getChoice())) {
                figNode = new FigBranchState(modelElement, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getJunction())) {
                figNode = new FigJunctionState(modelElement, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getFork())) {
                figNode = new FigForkState(modelElement, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getJoin())) {
                figNode = new FigJoinState(modelElement, bounds, settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getShallowHistory())) {
                figNode = new FigShallowHistoryState(modelElement, bounds,
                        settings);
            } else if (kind.equals(
                    Model.getPseudostateKind().getDeepHistory())) {
                figNode = new FigDeepHistoryState(modelElement, bounds,
                        settings);
            } else {
                LOG.log(Level.WARNING, "found a type not known");
            }
        }

        if (figNode != null) {
            LOG.log(Level.FINE,
                    "Model element {0} converted to {1}",
                    new Object[]{modelElement, figNode});
        } else {
            LOG.log(Level.FINE, "Dropped object NOT added {0}", figNode);
        }
        return figNode;
    }

}
