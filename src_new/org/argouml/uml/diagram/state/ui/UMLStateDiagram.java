// $Id$
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

package org.argouml.uml.diagram.state.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.CmdCreateNode;
import org.argouml.ui.CmdSetMode;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.state.StateDiagramGraphModel;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;


/**
 * The UML Statechart diagram. <p>
 * 
 * The correct name for this class would be 
 * "UMLStatechartDiagram". See issue 2306.
 */
public class UMLStateDiagram extends UMLDiagram {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(UMLStateDiagram.class);

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
    private Action actionTransition;
    private Action actionJunctionPseudoState;

    ////////////////////////////////////////////////////////////////
    // contructors

    /**
     * This constructor is used to build a dummy statechart diagram so
     * that a project will load properly.
     */
    public UMLStateDiagram() {

        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) { }
    }

    /**
     * Constructor.
     *
     * @param namespace the NameSpace for the new diagram
     * @param machine the StateMachine for the new diagram
     */
    public UMLStateDiagram(Object namespace, Object machine) {
        this();

        if (!Model.getFacade().isAStateMachine(machine)) {
            throw new IllegalStateException(
                "No StateMachine given to create a Statechart diagram");
        }
        if (namespace == null) {
            namespace = getNamespaceFromMachine(machine);
        }
        if (!Model.getFacade().isANamespace(namespace)) {
            throw new IllegalArgumentException();
        }

        if (Model.getFacade().getName(namespace) != null) {
            if (!Model.getFacade().getName(namespace).trim().equals("")) {
                String name = null;
                String diagramName = Model.getFacade().getName(namespace);
                int number =
                    (Model.getFacade().getBehaviors(namespace)) == null
                    ? 0
                            : Model.getFacade().getBehaviors(namespace).size();
                name = diagramName + " " + (number++);
                LOG.info("UMLStateDiagram constructor: String name = " + name);
                try {
                    setName(name);
                } catch (PropertyVetoException pve) { }
            }
        }
        setup(namespace, machine);
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
        
        Object namespace = Model.getFacade().getNamespace(machine);
        if (namespace != null) return namespace;
        
        Object context = Model.getFacade().getContext(machine);
        if (Model.getFacade().isAClassifier(context)) {
            namespace = context;
        } else if (Model.getFacade().isABehavioralFeature(context)) {
            namespace = Model.getFacade().getNamespace( // or just the owner?
                    Model.getFacade().getOwner(context));
        }
        if (namespace == null) {
            namespace = 
                ProjectManager.getManager().getCurrentProject().getRoot();
        }
        if (namespace == null || !Model.getFacade().isANamespace(namespace)) {
            throw new IllegalStateException(
                    "Can not deduce a Namespace from a StateMachine");
        }
        return namespace;
    }

    /**
     * The owner of a statechart diagram is the statemachine
     * it's showing.
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getOwner()
     */
    public Object getOwner() {
        StateDiagramGraphModel gm = (StateDiagramGraphModel) getGraphModel();
        return gm.getMachine();
    }

    /**
     * Called by the PGML parser to initialize the statechart
     * diagram. First the parser creates a statechart diagram via the
     * default constructor. Then this method is called.
     *
     * @see org.tigris.gef.base.Diagram#initialize(Object)
     */
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

        // add the diagram as a listener to the statemachine so
        // that when the statemachine is removed() the diagram is deleted also.
        // Remark MVW: It also works without the next line.... So why?
        // UmlModelEventPump.getPump().addModelEventListener(this, sm);

        theStateMachine = machine;

        StateDiagramGraphModel gm = new StateDiagramGraphModel();
        gm.setHomeModel(namespace);
        if (machine != null) {
            gm.setMachine(machine);
        }
        StateDiagramRenderer rend = new StateDiagramRenderer(); // singleton

        LayerPerspective lay = new LayerPerspectiveMutable(
                Model.getFacade().getName(namespace), gm);
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
        setLayer(lay);

        /* Listen to machine namespace changes, 
         * to adapt the namespace of the diagram. */
        Model.getPump().addModelEventListener(this, theStateMachine, "namespace");
    }

    /**
     * @see org.argouml.uml.diagram.ui.UMLDiagram#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        if (evt.getSource() == theStateMachine 
                && "namespace".equals(evt.getPropertyName())) {
            Object newNamespace = evt.getNewValue();
            if (getNamespace() != newNamespace) {
                /* The namespace of the statemachine is changed! */
                setNamespace(newNamespace);
                ((UMLMutableGraphSupport)getGraphModel())
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
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getUmlActions()
     */
    protected Object[] getUmlActions() {
        Object[] actions =
        {
            getActionState(),
	    getActionCompositeState(),
	    getActionTransition(),
	    getActionSynchState(),
	    getActionSubmachineState(),
	    getActionStubState(),
	    null,
	    getActionStartPseudoState(),
	    getActionFinalPseudoState(),
	    getActionJunctionPseudoState(),
            getActionChoicePseudoState(),
	    getActionForkPseudoState(),
	    getActionJoinPseudoState(),
	    getActionShallowHistoryPseudoState(),
	    getActionDeepHistoryPseudoState(),
        };
        return actions;
    }

    /**
     * Creates a name for the diagram.
     *
     * @return the new diagram name
     */
    protected String getNewDiagramName() {
        String name = getLabelName() + " " + getNextDiagramSerial();
        if (!ProjectManager.getManager().getCurrentProject()
                 .isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
    }

    /**
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getLabelName()
     */
    public String getLabelName() {
        return Translator.localize("label.state-chart-diagram");
    }

    /**
     * @return Returns the actionBranchPseudoState.
     * @deprecated use getActionChoicePseudoState
     */
    protected Action getActionBranchPseudoState() {
        return getActionChoicePseudoState();
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
                    new CmdSetMode(
                        ModeCreatePolyEdge.class,
                        "edgeClass",
                        Model.getMetaTypes().getTransition(),
                        "button.new-transition"));
        }
        return actionTransition;
    }

    /**
     * @see org.argouml.uml.diagram.ui.UMLDiagram#needsToBeRemoved()
     */
    public boolean needsToBeRemoved() {
        if (Model.getUmlFactory().isRemoved(theStateMachine)) {
            return true;
        }
        if (Model.getUmlFactory().isRemoved(getNamespace())) {
            return true;
        }
        /* Removal of the context is NOT a reason to delete this diagram! */
        return false;
    }

    /**
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getDependentElement()
     */
    public Object getDependentElement() {
            return getStateMachine(); /* The StateMachine. */
    }

    /**
     * @see org.argouml.uml.diagram.ui.UMLDiagram#isRelocationAllowed(java.lang.Object)
     */
    public boolean isRelocationAllowed(Object base)  {
    	return false;
    	/* TODO: We may return the following when the
    	 * relocate() has been implemented. */
//    	Model.getStateMachinesHelper()
//        	.isAddingStatemachineAllowed(base);
    }

    /**
     * @see org.argouml.uml.diagram.ui.UMLDiagram#relocate(java.lang.Object)
     */
    public boolean relocate(Object base) {
        return false;
    }

} /* end class UMLStateDiagram */
