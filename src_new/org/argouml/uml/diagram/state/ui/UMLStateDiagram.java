// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.beans.PropertyVetoException;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.argouml.ui.CmdCreateNode;
import org.argouml.ui.CmdSetMode;
import org.argouml.uml.diagram.state.StateDiagramGraphModel;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;


/**
 * The correct name for this class is "UMLStatechartDiagram". See issue 2306.
 *
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

    private Action actionState;
    private Action actionSynchState;
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
     *  this constructor is used to build a dummy statechart diagram so
     *  that a project will load properly.
     */
    public UMLStateDiagram() {

        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) { }
    }

    /** constructor
     * @param namespace the NameSpace for the new diagram
     * @param sm the StateMachine
     */
    public UMLStateDiagram(Object namespace, Object sm) {
        this();

        if (!ModelFacade.isAStateMachine(sm))
            throw new IllegalArgumentException();

        if (sm != null && namespace == null) {
            Object context = ModelFacade.getContext(sm);
            if (ModelFacade.isAClassifier(context)) {
                namespace = context;
            } else if (ModelFacade.isABehavioralFeature(context)) {
                namespace = ModelFacade.getOwner(context);
            }
        }
        if (namespace != null && ModelFacade.getName(namespace) != null)
            if (ModelFacade.getName(namespace).trim() != "") {
                String name = null;
                String diagramName = ModelFacade.getName(namespace);
                int number =
                    (ModelFacade.getBehaviors(namespace)) == null
                    ? 0
                            : ModelFacade.getBehaviors(namespace).size();
                name = diagramName + " " + (number++);
                LOG.info("UMLStateDiagram constructor: String name = " + name);
                try {
                    setName(name);
                } catch (PropertyVetoException pve) { }
            }
        if (namespace != null) {
            setup(namespace, sm);
        }
    }

    /**
     * The owner of a statechart diagram is the statechart diagram
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
        if (ModelFacade.isAStateMachine(o)) {
            Object sm = /*(MStateMachine)*/ o;
            Object context = ModelFacade.getContext(sm);
            Object contextNamespace = null;
            if (ModelFacade.isAClassifier(context)) {
                contextNamespace = context;
            } else if (ModelFacade.isABehavioralFeature(context)) {
                contextNamespace =
                    ModelFacade.getNamespace(ModelFacade.getOwner(context));
            }
            if (contextNamespace != null) {
                setup(contextNamespace, sm);
            }
        } else {
            throw new IllegalStateException(
                "Cannot find context namespace "
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
     * @param sm StateMachine from the UML model
     * @author psager@tigris.org Jan. 24, 2oo2
     */
    public void setup(Object namespace,
		      Object /*MStateMachine*/ sm) {
        setNamespace(namespace);

        // add the diagram as a listener to the statemachine so
        // that when the statemachine is removed() the diagram is deleted also.
        // Remark MVW: It also works without the next line.... So why?
        // UmlModelEventPump.getPump().addModelEventListener(this, sm);

        theStateMachine = sm;

        StateDiagramGraphModel gm = new StateDiagramGraphModel();
        gm.setNamespace(namespace);
        if (sm != null) {
            gm.setMachine(sm);
        }
        StateDiagramRenderer rend = new StateDiagramRenderer(); // singleton

        LayerPerspective lay =
            new LayerPerspectiveMutable(ModelFacade.getName(namespace), gm);
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
        setLayer(lay);

    }

    /**
     * @return the StateMachine belonging to this diagram
     */
    public Object getStateMachine() {
        return /*(MStateMachine)*/
         ((StateDiagramGraphModel) getGraphModel()).getMachine();
    }

    /**
     * @param sm Set the StateMachine for this diagram.
     */
    public void setStateMachine(Object sm) {

        if (!ModelFacade.isAStateMachine(sm)) {
            throw new IllegalArgumentException();
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
	    null,
	    getActionStartPseudoState(),
	    getActionFinalPseudoState(),
	    getActionJunctionPseudoState(),
	    getActionBranchPseudoState(),
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
        String name = "Statechart Diagram " + getNextDiagramSerial();
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
     * This diagram listens to NSUML events from its Statemachine;
     * When the Statemachine is removed, we also want to delete this diagram.
     * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    /*public void removed(MElementEvent e) {

        UmlModelEventPump.getPump().removeModelEventListener(
            this,
            theStateMachine);
        super.removed(e);
    }*/

    /**
     * @return Returns the actionBranchPseudoState.
     */
    protected Action getActionBranchPseudoState() {
        if (actionBranchPseudoState == null) {
            actionBranchPseudoState = new RadioAction(
                    new ActionCreatePseudostate(
                        ModelFacade.getBranchPseudostateKindToken(), "Choice"));
        }
        return actionBranchPseudoState;
    }
    /**
     * @return Returns the actionCompositeState.
     */
    protected Action getActionCompositeState() {
        if (actionCompositeState == null) {
            actionCompositeState = new RadioAction(new CmdCreateNode(
                    ModelFacade.getCompositeStateToken(), "CompositeState"));
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
                        ModelFacade.getDeepHistoryPseudostateKindToken(),
                        "DeepHistory"));
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
                                ModelFacade.getFinalStateToken(),
                                "FinalState"));
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
                            ModelFacade.getForkPseudostateKindToken(), "Fork"));
        }
        return actionForkPseudoState;
    }
    /**
     * @return Returns the actionJoinPseudoState.
     */
    protected Action getActionJoinPseudoState() {
        if (actionJoinPseudoState == null) {
            actionJoinPseudoState = new RadioAction(new ActionCreatePseudostate(
                    ModelFacade.getJoinPseudostateKindToken(), "Join"));
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
                        ModelFacade.getJunctionPseudostateKindToken(),
                        "Junction"));
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
                        ModelFacade.getShallowHistoryPseudostateKindToken(),
                        "ShallowHistory"));
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
                        ModelFacade.getInitialPseudostateKindToken(),
                        "Initial"));
        }
        return actionStartPseudoState;
    }
    /**
     * @return Returns the actionState.
     */
    protected Action getActionState() {
        if (actionState == null) {
            actionState = new RadioAction(
                    new CmdCreateNode(ModelFacade.getStateToken(), "State"));
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
                                ModelFacade.getSynchStateToken(),
                                "SynchState"));
        }
        return actionSynchState;
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
                        ModelFacade.getTransitionToken(),
                        "Transition"));
        }
        return actionTransition;
    }

    /**
     * @see org.argouml.uml.diagram.ui.UMLDiagram#needsToBeRemoved()
     */
    public boolean needsToBeRemoved() {
        Object context = ModelFacade.getContext(theStateMachine);
        if (context == null) {
            return true;
        }
        if (Model.getUmlFactory().isRemoved(theStateMachine)) {
            return true;
        }
        if (Model.getUmlFactory().isRemoved(getNamespace())) {
            return true;
        }
        return false;
    }

} /* end class UMLStateDiagram */
