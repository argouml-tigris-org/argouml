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

package org.argouml.uml.diagram.activity.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.CmdCreateNode;
import org.argouml.ui.CmdSetMode;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.activity.ActivityDiagramGraphModel;
import org.argouml.uml.diagram.state.StateDiagramGraphModel;
import org.argouml.uml.diagram.state.ui.ActionCreatePseudostate;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.graph.GraphModel;

/**
 * The Activity diagram.<p>
 *
 * TODO: Finish the work on swimlanes, subactivity states.
 */
public class UMLActivityDiagram extends UMLDiagram {
    private Action actionState;
    private Action actionStartPseudoState;
    private Action actionFinalPseudoState;
    private Action actionJunctionPseudoState;
    private Action actionForkPseudoState;
    private Action actionJoinPseudoState;
    private Action actionTransition;
    private Action actionObjectFlowState;
    private Action actionNewSwimlane;
    private Action actionCallState;
    private Action actionSubactivityState;

    /**
     * Constructor.
     */
    public UMLActivityDiagram() {
        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) { }
    }

    /**
     * Constructor.
     *
     * @param namespace the namespace for the diagram
     * @param agraph the ActivityGraph for the diagram
     */
    public UMLActivityDiagram(Object namespace, Object agraph) {

        this();

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
                } catch (PropertyVetoException pve) { }
            }
        }
        setup(namespace, agraph);
    }

    /**
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
            Object namespace = Model.getFacade().getNamespace(o);
            if (namespace != null) {
                setup(namespace, o);
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

     * @param m  Namespace from the model
     * @param agraph ActivityGraph from the model
     */
    public void setup(Object m, Object agraph) {

        if (!Model.getFacade().isANamespace(m)
            || !Model.getFacade().isAActivityGraph(agraph)) {
            throw new IllegalArgumentException();
        }

        super.setNamespace(m);
        ActivityDiagramGraphModel gm = new ActivityDiagramGraphModel();
//        setGraphModel(gm); //MVW
        gm.setHomeModel(m);
        if (agraph != null) {
            gm.setMachine(agraph);
//            setStateMachine(agraph); // MVW
        }
        LayerPerspective lay =
            new LayerPerspectiveMutable(Model.getFacade().getName(m), gm);
        ActivityDiagramRenderer rend = new ActivityDiagramRenderer();
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);

        setLayer(lay);

        /* Listen to activitygraph namespace changes, 
         * to adapt the namespace of the diagram. */
        Model.getPump().addModelEventListener(this, agraph, "namespace");
    }

    /**
     * @see org.argouml.uml.diagram.ui.UMLDiagram#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        if (evt.getSource() == getStateMachine()) {
            Object newNamespace = 
                Model.getFacade().getNamespace(getStateMachine());
            if (getNamespace() != newNamespace) {
                /* The namespace of the activitygraph is changed! */
                setNamespace(newNamespace);
                ((UMLMutableGraphSupport)getGraphModel())
                                .setHomeModel(newNamespace);
            }
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getOwner()
     */
    public Object getOwner() {
        ActivityDiagramGraphModel gm =
            (ActivityDiagramGraphModel) getGraphModel();
        return gm.getMachine();
    }

    /**
     * @return the statemachine
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
//	    getActionNewSwimlane(), // uncomment this ...
	    null,
	    getActionCallState(),
            getActionObjectFlowState(),
            /*getActionSubactivityState()*/
	};
        return actions;
    }

    /**
     * Creates a new diagram name.<p>
     *
     * @return String
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
     * @return Returns the actionNewSwimlane.
     */
    protected Action getActionNewSwimlane() {
        if (actionNewSwimlane == null) {
            actionNewSwimlane =
                new CmdCreateNode(Model.getMetaTypes().getPartition(),
                        	  "button.new-partition");
        }
        return actionNewSwimlane;
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
        if ((getStateMachine() == null)
                || (Model.getUmlFactory().isRemoved(getStateMachine()))) {
            return true;
        }
        if (Model.getUmlFactory().isRemoved(getNamespace())) {
            return true;
        }
        Object context = Model.getFacade().getContext(getStateMachine());
        if (context == null) {
            return true;
        }
        return false;
    }

    /**
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getDependentElement()
     */
    public Object getDependentElement() {
        return getStateMachine(); /* The ActivityGraph. */
    }

    /**
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

    /**
     * @see org.argouml.uml.diagram.ui.UMLDiagram#relocate(java.lang.Object)
     */
    public boolean relocate(Object base) {
        return false;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 6223128918989919230L;
} /* end class UMLActivityDiagram */
