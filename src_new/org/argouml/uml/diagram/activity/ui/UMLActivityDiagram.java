// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

// File: UMLActivityDiagram.java
// Classes: UMLActivityDiagram

package org.argouml.uml.diagram.activity.ui;

import java.beans.PropertyVetoException;

import javax.swing.Action;

import org.apache.log4j.Logger;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.ui.CmdCreateNode;
import org.argouml.ui.CmdSetMode;
import org.argouml.uml.diagram.activity.ActivityDiagramGraphModel;
import org.argouml.uml.diagram.state.ui.ActionCreatePseudostate;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;

import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;

/**
 * The Activity diagram.<p>
 * 
 * TODO: Finish the work on swimlanes, callstates, subactivity states.
 */
public class UMLActivityDiagram extends UMLDiagram {

    private static final Logger LOG = Logger.
                                    getLogger(UMLActivityDiagram.class);

    ///////////////////////////
    // actions for the toolbar

    private Action actionState;
    private Action actionStartPseudoState;
    private Action actionFinalPseudoState;
    private Action actionJunctionPseudoState;
    private Action actionForkPseudoState;
    private Action actionJoinPseudoState;
    private Action actionTransition;
    private Action actionObjectFlowState;
    private Action actionNewSwimlane; //MVW
    private Action actionCallState;
    private Action actionSubactivityState;

    /**
     * Constructor
     */
    public UMLActivityDiagram() {
        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) { }
    }

    /**
     * @param m the namespace for the diagram
     */
    public UMLActivityDiagram(Object m) {
        this();
        setNamespace(m);
        Object context = ModelFacade.getContext(getStateMachine());
        String name = null;
        if (context != null
            && ModelFacade.getName(context) != null
            && ModelFacade.getName(context).length() > 0) {
            name = ModelFacade.getName(context);
            try {
                setName(name);
            } catch (PropertyVetoException pve) { }
        }
    }

    /** 
     * Constructor
     * @param namespace the namespace for the diagram
     * @param agraph the ActivityGraph for the diagram
     */
    public UMLActivityDiagram(Object namespace, Object agraph) {

        this();

        if (!ModelFacade.isANamespace(namespace)
            || !ModelFacade.isAActivityGraph(agraph))
            throw new IllegalArgumentException();

        if (namespace != null && ModelFacade.getName(namespace) != null) 
            if (ModelFacade.getName(namespace).trim() != "") {
                String name =
                    ModelFacade.getName(namespace)
                    + " activity "
                    + (ModelFacade.getBehaviors(namespace).size());
                try {
                    setName(name);
                } catch (PropertyVetoException pve) { }
            }
        if (namespace != null)
            setup(namespace, agraph);
        else
            throw new NullPointerException("Namespace may not be null");
    }

    /**
     * @see org.tigris.gef.base.Diagram#initialize(java.lang.Object)
     */
    public void initialize(Object o) {
        if (!(org.argouml.model.ModelFacade.isAActivityGraph(o)))
            return;
        Object context = ModelFacade.getContext(o);
        if (context != null
            && org.argouml.model.ModelFacade.isANamespace(context))
            setup(context, o);
        else
            LOG.debug("ActivityGraph without context not yet possible :-(");
    }

    /**
     * Method to perform a number of important initializations of an
     * <I>Activity Diagram</I>.<p>
     * 
     * Each diagram type has a similar <I>UMLxxxDiagram</I> class.<p>
     *
     * Changed <I>lay</I> from <I>LayerPerspective</I> to
     * <I>LayerPerspectiveMutable</I>.  This class is a child of
     * <I>LayerPerspective</I> and was implemented to correct some
     * difficulties in changing the model. <I>lay</I> is used mainly
     * in <I>LayerManager</I>(GEF) to control the adding, changing and
     * deleting layers on the diagram...  psager@tigris.org Jan. 24,
     * 2002

     * @param m  Namespace from the model
     * @param agraph ActivityGraph from the model
     */
    public void setup(Object m, Object agraph) {

        if (!ModelFacade.isANamespace(m)
            || !ModelFacade.isAActivityGraph(agraph))
            throw new IllegalArgumentException();

        super.setNamespace(m);
        ActivityDiagramGraphModel gm = new ActivityDiagramGraphModel();
        gm.setNamespace(m);
        if (agraph != null) {
            gm.setMachine(agraph);
        }
        LayerPerspective lay =
            new LayerPerspectiveMutable(ModelFacade.getName(m), gm);
        ActivityDiagramRenderer rend = new ActivityDiagramRenderer();  
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);

        setLayer(lay);

    }

    /**
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getOwner()
     */
    public Object getOwner() {
        ActivityDiagramGraphModel gm = (ActivityDiagramGraphModel) 
                                    getGraphModel();
        Object sm = gm.getMachine();
        if (sm != null)
            return sm;
        return gm.getNamespace();
    }

    /**
     * @return the statemachine
     */
    public Object getStateMachine() {
        return ((ActivityDiagramGraphModel) getGraphModel()).getMachine();
    }

    /**
     * @param sm set the statemachine for this diagram
     */
    public void setStateMachine(Object sm) {

        if (!ModelFacade.isAStateMachine(sm))
            throw new IllegalArgumentException();

        ((ActivityDiagramGraphModel) getGraphModel()).setMachine(sm);
    }

    /**
     * Get the actions from which to create a toolbar or equivalent
     * graphic triggers.
     *
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getUmlActions()
     */
    protected Object[] getUmlActions() {
        Object actions[] =
        {
            getActionState(),
            getActionTransition(),
	    null,
	    getActionStartPseudoState(),
	    getActionFinalPseudoState(),
	    getActionJunctionPseudoState(),
	    getActionForkPseudoState(),
	    getActionJoinPseudoState(),
	    //getActionNewSwimlane(),
	    null,
	    /*getActionCallState(),*/ // uncomment these ...
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
        String name = "Activity Diagram " + getNextDiagramSerial();
        if (!ProjectManager.getManager().getCurrentProject()
                 .isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
    }

    /**
     * @return Returns the actionCallState.
     */
    protected Action getActionCallState() {
        if (actionCallState == null) {
            actionCallState = new RadioAction(
                    new CmdCreateNode(ModelFacade.CALLSTATE, "CallState"));
        }
        return actionCallState;
    }
    /**
     * @return Returns the actionFinalPseudoState.
     */
    protected Action getActionFinalPseudoState() {
        if (actionFinalPseudoState == null) {
            actionFinalPseudoState = new RadioAction(
                    new CmdCreateNode(ModelFacade.FINALSTATE, 
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
                new ActionCreatePseudostate(ModelFacade.FORK_PSEUDOSTATEKIND, 
                            "Fork"));
        }
        return actionForkPseudoState;
    }
    /**
     * @return Returns the actionJoinPseudoState.
     */
    protected Action getActionJoinPseudoState() {
        if (actionJoinPseudoState == null) {
            actionJoinPseudoState = new RadioAction(
                new ActionCreatePseudostate(ModelFacade.JOIN_PSEUDOSTATEKIND,
                            "Join"));
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
                        ModelFacade.JUNCTION_PSEUDOSTATEKIND, "Junction"));
        }
        return actionJunctionPseudoState;
    }
    /**
     * @return Returns the actionNewSwimlane.
     */
    protected Action getActionNewSwimlane() {
        if (actionNewSwimlane == null) {
            actionNewSwimlane = new CmdCreateNode(ModelFacade.PARTITION, 
                "Create a new swimlane");
        }
        return actionNewSwimlane;
    }
    /**
     * @return Returns the actionObjectFlowState.
     */
    protected Action getActionObjectFlowState() {
        if (actionObjectFlowState == null) {
            actionObjectFlowState = new RadioAction(new CmdCreateNode(
                    ModelFacade.OBJECTFLOWSTATE, "ObjectFlowState"));
        }
        return actionObjectFlowState;
    }
    /**
     * @return Returns the actionStartPseudoState.
     */
    protected Action getActionStartPseudoState() {
        if (actionStartPseudoState == null) {
            actionStartPseudoState =  new RadioAction(
                new ActionCreatePseudostate(
                        ModelFacade.INITIAL_PSEUDOSTATEKIND, "Initial"));
        }
        return actionStartPseudoState;
    }
    /**
     * @return Returns the actionState.
     */
    protected Action getActionState() {
        if (actionState == null) {
            actionState = new RadioAction(
                    new CmdCreateNode(ModelFacade.ACTION_STATE, "ActionState"));
        }
        return actionState;
    }
    /**
     * @return Returns the actionSubactivityState.
     */
    protected Action getActionSubactivityState() {
        if (actionSubactivityState == null) {
            actionSubactivityState = new RadioAction(
                    new CmdCreateNode(ModelFacade.SUBACTIVITYSTATE, 
                                                "SubactivityState"));
        }
        return actionSubactivityState;
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
                            ModelFacade.TRANSITION,
                            "Transition"));
        }
        return actionTransition;
    }
} /* end class UMLActivityDiagram */
