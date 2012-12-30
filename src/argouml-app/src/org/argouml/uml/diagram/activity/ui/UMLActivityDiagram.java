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

package org.argouml.uml.diagram.activity.ui;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.ActivityDiagram;
import org.argouml.model.ActivityGraphsHelper;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.Model;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.DiagramElement;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.activity.ActivityDiagramGraphModel;
import org.argouml.uml.diagram.state.StateDiagramGraphModel;
import org.argouml.uml.diagram.state.ui.ActionCreatePseudostate;
import org.argouml.uml.diagram.state.ui.ButtonActionNewCallEvent;
import org.argouml.uml.diagram.state.ui.ButtonActionNewChangeEvent;
import org.argouml.uml.diagram.state.ui.ButtonActionNewSignalEvent;
import org.argouml.uml.diagram.state.ui.ButtonActionNewTimeEvent;
import org.argouml.uml.diagram.state.ui.FigBranchState;
import org.argouml.uml.diagram.state.ui.FigFinalState;
import org.argouml.uml.diagram.state.ui.FigForkState;
import org.argouml.uml.diagram.state.ui.FigInitialState;
import org.argouml.uml.diagram.state.ui.FigJoinState;
import org.argouml.uml.diagram.state.ui.FigJunctionState;
import org.argouml.uml.diagram.state.ui.FigStateVertex;
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
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;

/**
 * The Activity diagram.<p>
 *
 * TODO: Finish the work on subactivity states.
 */
public class UMLActivityDiagram extends UMLDiagram implements ActivityDiagram {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(UMLActivityDiagram.class.getName());

    /**
     * The UID.
     */
    private static final long serialVersionUID = 6223128918989919230L;

    /**
     * this diagram needs to be deleted when its statemachine is deleted.
     */
    private Object theActivityGraph;

    private Action actionState;
    private Action actionStartPseudoState;
    private Action actionFinalPseudoState;
    private Action actionJunctionPseudoState;
    private Action actionForkPseudoState;
    private Action actionJoinPseudoState;
    private Action actionTransition;
    private Action actionObjectFlowState;
    private Action actionSwimlane;
    private Action actionCallState;
    private Action actionSubactivityState;
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

    /**
     * Constructor.
     *
     * @deprecated for 0.28 by tfmorris.  Use
     * {@link #UMLActivityDiagram(String, Object, GraphModel)}.
     */
    @Deprecated
    public UMLActivityDiagram() {
        super();
        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) {
            // no action required in case of veto on setName
        }
        // TODO: All super constructors should take a GraphModel
        setGraphModel(createGraphModel());
    }

    /**
     * Constructor.
     *
     * @param namespace the namespace for the diagram
     * @param agraph the ActivityGraph for the diagram
     * @deprecated for 0.28 by tfmorris.  Use
     * {@link #UMLActivityDiagram(String, Object, GraphModel)}.
     */
    @Deprecated
    public UMLActivityDiagram(Object namespace, Object agraph) {

        this();

        if (namespace == null) {
            namespace = Model.getFacade().getNamespace(agraph);
        }

        if (!Model.getFacade().isANamespace(namespace)
            || !Model.getFacade().isAActivityGraph(agraph)) {
            throw new IllegalArgumentException();
        }

        if (Model.getFacade().getName(namespace) != null) {
            if (!Model.getFacade().getName(namespace).trim().equals("")) {
                String name =
                    Model.getFacade().getName(namespace)
                    + " activity "
                    + (Model.getFacade().getBehaviors(namespace).size());
                try {
                    setName(name);
                } catch (PropertyVetoException pve) {
                    // no action required
                }
            }
        }
        setup(namespace, agraph);
    }

    /*
     * @see org.tigris.gef.base.Diagram#initialize(java.lang.Object)
     */
    public void initialize(Object o) {
        if (!(Model.getFacade().isAActivityGraph(o))) {
            return;
        }
        Object context = Model.getFacade().getContext(o);
        if (context != null) {
            if (Model.getFacade().isABehavioralFeature(context)) {
                setup(Model.getFacade().getNamespace(
                                Model.getFacade().getOwner(context)), o);
            } else {
                setup(context, o);
            }
        } else {
            Object namespace4Diagram = Model.getFacade().getNamespace(o);
            if (namespace4Diagram != null) {
                setup(namespace4Diagram, o);
            } else {
                throw new IllegalStateException("Cannot find context "
                        + "nor namespace while initializing activity diagram");
            }
        }
    }

    /**
     * Method to perform a number of important initializations of an
     * <em>Activity Diagram</em>.<p>
     *
     * Each diagram type has a similar <em>UMLxxxDiagram</em> class.<p>
     *
     * Changed <em>lay</em> from <em>LayerPerspective</em> to
     * <em>LayerPerspectiveMutable</em>.  This class is a child of
     * <em>LayerPerspective</em> and was implemented to correct some
     * difficulties in changing the model. <em>lay</em> is used mainly
     * in <em>LayerManager</em>(GEF) to control the adding, changing and
     * deleting layers on the diagram...  psager@tigris.org Jan. 24,
     * 2002

     * @param namespace  Namespace from the model
     * @param agraph ActivityGraph from the model
     */
    public void setup(Object namespace, Object agraph) {
        if (!Model.getFacade().isANamespace(namespace)
            || !Model.getFacade().isAActivityGraph(agraph)) {
            throw new IllegalArgumentException();
        }

        setNamespace(namespace);

        theActivityGraph = agraph;


        ActivityDiagramGraphModel gm = createGraphModel();

        gm.setHomeModel(namespace);
        if (theActivityGraph != null) {
            gm.setMachine(theActivityGraph);
        }
        ActivityDiagramRenderer rend = new ActivityDiagramRenderer();

        LayerPerspective lay = new LayerPerspectiveMutable(
                Model.getFacade().getName(namespace), gm);
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
        setLayer(lay);

        /* Listen to activitygraph deletion,
         * delete this diagram. */
        Model.getPump().addModelEventListener(this, theActivityGraph,
                new String[] {"remove", "namespace"});
    }

    // TODO: Needs to be tidied up after stable release. Graph model
    // should be created in constructor
    private ActivityDiagramGraphModel createGraphModel() {
	if ((getGraphModel() instanceof ActivityDiagramGraphModel)) {
	    return (ActivityDiagramGraphModel) getGraphModel();
	} else {
	    return new ActivityDiagramGraphModel();
	}
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ((evt.getSource() == theActivityGraph)
                && (evt instanceof DeleteInstanceEvent)
                && "remove".equals(evt.getPropertyName())) {
            Model.getPump().removeModelEventListener(this,
                    theActivityGraph, new String[] {"remove", "namespace"});
            getProject().moveToTrash(this);
        }
        if (evt.getSource() == getStateMachine()) {
            Object newNamespace =
                Model.getFacade().getNamespace(getStateMachine());
            if (getNamespace() != newNamespace) {
                /* The namespace of the activitygraph is changed! */
                setNamespace(newNamespace);
                ((UMLMutableGraphSupport) getGraphModel())
                                .setHomeModel(newNamespace);
            }
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getOwner()
     */
    public Object getOwner() {
        if (!(getGraphModel() instanceof ActivityDiagramGraphModel)) {
            throw new IllegalStateException(
                    "Incorrect graph model of "
                    + getGraphModel().getClass().getName());
        }
        ActivityDiagramGraphModel gm =
            (ActivityDiagramGraphModel) getGraphModel();
        return gm.getMachine();
    }

    /**
     * @return the statemachine
     *
     * TODO: If this method is called by any of the Figs, it will introduce
     * a dependency cycle.  It would be much better if they could just
     * use {@link org.argouml.uml.diagram.ArgoDiagram#getOwner()} which does
     * the same thing.
     */
    public Object getStateMachine() {
        GraphModel gm = getGraphModel();
        if (gm instanceof StateDiagramGraphModel) {
            Object machine = ((StateDiagramGraphModel) gm).getMachine();
            if (!Model.getUmlFactory().isRemoved(machine)) {
                return machine;
            }
        }
        return null;
    }

    /**
     * @param sm set the statemachine for this diagram
     */
    public void setStateMachine(Object sm) {

        if (!Model.getFacade().isAStateMachine(sm)) {
            throw new IllegalArgumentException();
        }

        ((ActivityDiagramGraphModel) getGraphModel()).setMachine(sm);
    }

    /**
     * Get the actions from which to create a toolbar or equivalent
     * graphic triggers.
     *
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getUmlActions()
     * @return the array of actions
     */
    protected Object[] getUmlActions() {
        Object[] actions =
        {
            getActionState(),
            getActionTransition(),
	    null,
	    getActionStartPseudoState(),
	    getActionFinalPseudoState(),
	    getActionJunctionPseudoState(),
	    getActionForkPseudoState(),
	    getActionJoinPseudoState(),
	    getActionSwimlane(),
	    null,
	    getActionCallState(),
            getActionObjectFlowState(),
            /*getActionSubactivityState()*/
            null,
            getTriggerActions(),
            getActionGuard(),
            getEffectActions(),
	};
        return actions;
    }

    protected Object[] getTriggerActions() {
        Object[] actions = {
            getActionCallEvent(),
            getActionChangeEvent(),
            getActionSignalEvent(),
            getActionTimeEvent(),
        };
        ToolBarUtility.manageDefault(actions, "diagram.activity.trigger");
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
        ToolBarUtility.manageDefault(actions, "diagram.activity.effect");
        return actions;
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getLabelName()
     */
    public String getLabelName() {
        return Translator.localize("label.activity-diagram");
    }

    /**
     * @return Returns the actionCallState.
     */
    protected Action getActionCallState() {
        if (actionCallState == null) {
            actionCallState =
                new RadioAction(
                        new CmdCreateNode(
                                Model.getMetaTypes().getCallState(),
                                "button.new-callstate"));
        }
        return actionCallState;
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
            actionForkPseudoState =
                new RadioAction(
                        new ActionCreatePseudostate(
                                Model.getPseudostateKind().getFork(),
                        	"button.new-fork"));
        }
        return actionForkPseudoState;
    }
    /**
     * @return Returns the actionJoinPseudoState.
     */
    protected Action getActionJoinPseudoState() {
        if (actionJoinPseudoState == null) {
            actionJoinPseudoState =
                new RadioAction(
                        new ActionCreatePseudostate(
                                Model.getPseudostateKind().getJoin(),
                        	"button.new-join"));
        }
        return actionJoinPseudoState;
    }
    /**
     * @return Returns the actionJunctionPseudoState.
     */
    protected Action getActionJunctionPseudoState() {
        if (actionJunctionPseudoState == null) {
            actionJunctionPseudoState =
                new RadioAction(
                        new ActionCreatePseudostate(
                                Model.getPseudostateKind().getJunction(),
                                "button.new-junction"));
        }
        return actionJunctionPseudoState;
    }
    /**
     * @return Returns the actionSwimlane.
     */
    protected Action getActionSwimlane() {
        if (actionSwimlane == null) {
            actionSwimlane =
                new ActionCreatePartition(getStateMachine());
        }
        return actionSwimlane;
    }
    /**
     * @return Returns the actionObjectFlowState.
     */
    protected Action getActionObjectFlowState() {
        if (actionObjectFlowState == null) {
            actionObjectFlowState =
                new RadioAction(
                        new CmdCreateNode(
                                Model.getMetaTypes().getObjectFlowState(),
                                "button.new-objectflowstate"));
        }
        return actionObjectFlowState;
    }
    /**
     * @return Returns the actionStartPseudoState.
     */
    protected Action getActionStartPseudoState() {
        if (actionStartPseudoState == null) {
            actionStartPseudoState =
                new RadioAction(
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
                        new CmdCreateNode(
                                Model.getMetaTypes().getActionState(),
                        	"button.new-actionstate"));
        }
        return actionState;
    }
    /**
     * @return Returns the actionSubactivityState.
     */
    protected Action getActionSubactivityState() {
        if (actionSubactivityState == null) {
            actionSubactivityState =
                new RadioAction(
                        new CmdCreateNode(
                                Model.getMetaTypes().getSubactivityState(),
                        "button.new-subactivitystate"));
        }
        return actionSubactivityState;
    }
    /**
     * @return Returns the actionTransition.
     */
    protected Action getActionTransition() {
        if (actionTransition == null) {
            actionTransition =
                new RadioAction(
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
    public Object getDependentElement() {
        return getStateMachine(); /* The ActivityGraph. */
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#isRelocationAllowed(java.lang.Object)
     */
    public boolean isRelocationAllowed(Object base) {
        return false;
        /* TODO: We may return the following when the
         * relocate() has been implemented.
         */
//      Model.getActivityGraphsHelper()
//      .isAddingActivityGraphAllowed(base);
    }

    @SuppressWarnings("unchecked")
    public Collection getRelocationCandidates(Object root) {
        /* TODO: We may return something useful when the
         * relocate() has been implemented. */
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

    /**
     * Once the diagram has loaded we build the previous/next links between
     * any swimlanes.
     */
    @Override
    public void postLoad() {
	FigPartition previous = null;

	// Create a map of partitions keyed by x coordinate
	HashMap map = new HashMap();

	Iterator it = new ArrayList(getLayer().getContents()).iterator();
	while (it.hasNext()) {
	    Fig f = (Fig) it.next();
	    if (f instanceof FigPartition) {
		map.put(Integer.valueOf(f.getX()), f);
	    }
	}

	// Sort the x coordinates into order
	List xList = new ArrayList(map.keySet());
        Collections.sort(xList);

        // Link the previous/next reference of the swimlanes
        // according to the x order.
	it = xList.iterator();
	while (it.hasNext()) {
	    Fig f = (Fig) map.get(it.next());
	    if (f instanceof FigPartition) {
		FigPartition fp = (FigPartition) f;
		if (previous != null) {
		    previous.setNextPartition(fp);
		}
		fp.setPreviousPartition(previous);
		fp.setNextPartition(null);
		previous = fp;
	    }
	}
    }

    /**
     * Extends basic functionality to handle logic for enclosement of states
     * within a swimlane.
     * @param enclosed The FigNode enclosed.
     * @param oldEncloser The previous encloser (null if none)
     * @param newEncloser The encloser (null if none)
     */
    public void encloserChanged(
            FigNode enclosed, FigNode oldEncloser, FigNode newEncloser) {

	if (oldEncloser == null && newEncloser == null) {
	    return;
	}

	if (enclosed instanceof FigStateVertex
		|| enclosed instanceof FigObjectFlowState) {
	    changePartition(enclosed);
	}
    }

    /**
     * Extends basic functionality to handle logic for enclosement of states
     * within a swimlane.
     * @param enclosed The FigNode enclosed.
     */
    private void changePartition(FigNode enclosed) {

	assert enclosed != null;

	Object state = enclosed.getOwner();
	ActivityGraphsHelper activityGraph = Model.getActivityGraphsHelper();

        for (Object f : getLayer().getContentsNoEdges()) {
            if (f instanceof FigPartition) {
        	FigPartition fig = (FigPartition) f;
        	Object partition = fig.getOwner();
        	if (fig.getBounds().intersects(enclosed.getBounds())) {
                    activityGraph.addContent(partition, state);
        	} else if (isStateInPartition(state, partition)) {
                    activityGraph.removeContent(partition, state);
        	}
            }
        }
    }

    private boolean isStateInPartition(Object state, Object partition) {
	return Model.getFacade().getContents(partition).contains(state);
    }

    @Override
    public boolean doesAccept(Object objectToAccept) {
        if (Model.getFacade().isAPartition(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAState(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAPseudostate(objectToAccept)) {
            Object kind = Model.getFacade().getKind(objectToAccept);
            if (kind == null) {
                LOG.log(Level.WARNING, "found a null type pseudostate");
                return false;
            }
            if (kind.equals(
                    Model.getPseudostateKind().getShallowHistory())) {
                return false;
            } else if (kind.equals(
                    Model.getPseudostateKind().getDeepHistory())) {
                return false;
            }
            return true;
        } else if (Model.getFacade().isAComment(objectToAccept)) {
            return true;
        }
        return false;
    }

    public DiagramElement createDiagramElement(
            final Object modelElement,
            final Rectangle bounds) {

        FigNodeModelElement figNode = null;

        DiagramSettings settings = getDiagramSettings();

        if (Model.getFacade().isAPartition(modelElement)) {
            figNode = new FigPartition(modelElement, bounds, settings);
        } else if (Model.getFacade().isAActionState(modelElement)) {
            figNode = new FigActionState(modelElement, bounds, settings);
        } else if (Model.getFacade().isACallState(modelElement)) {
            figNode = new FigCallState(modelElement, bounds, settings);
        } else if (Model.getFacade().isAObjectFlowState(modelElement)) {
            figNode = new FigObjectFlowState(modelElement, bounds, settings);
        } else if (Model.getFacade().isASubactivityState(modelElement)) {
            figNode = new FigSubactivityState(modelElement, bounds, settings);
        } else if (Model.getFacade().isAFinalState(modelElement)) {
            figNode = new FigFinalState(modelElement, bounds, settings);
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
            } else {
                LOG.log(Level.WARNING, "found a type not known");
            }
        } else if (Model.getFacade().isAComment(modelElement)) {
            figNode = new FigComment(modelElement, bounds, settings);
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
