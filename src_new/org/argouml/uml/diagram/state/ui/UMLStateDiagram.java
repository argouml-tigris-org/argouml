// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: UMLStateDiagram.java
// Classes: UMLStateDiagram
// Original Author: your email here
// $Id$

package org.argouml.uml.diagram.state.ui;

import java.beans.PropertyVetoException;

import javax.swing.Action;

import org.apache.log4j.Logger;

import org.argouml.application.api.Argo;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.state.StateDiagramGraphModel;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.ActionAddNote;

import org.tigris.gef.base.CmdSetMode;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;

public class UMLStateDiagram extends UMLDiagram {
    private Logger _cat = Logger.getLogger(UMLStateDiagram.class);

    /**
     * this diagram needs to be deleted when its statemachine is deleted.
     */
    MStateMachine theStateMachine;

    ////////////////
    // actions for toolbar

    protected static Action _actionState =
        new CmdCreateNode((Class)ModelFacade.STATE, "State");

    protected static Action _actionCompositeState =
        new CmdCreateNode((Class)ModelFacade.COMPOSITESTATE, "CompositeState");

    // start state, end state, forks, joins, etc.
    protected static Action _actionStartPseudoState =
        new ActionCreatePseudostate(ModelFacade.INITIAL_PSEUDOSTATEKIND, "Initial");

    protected static Action _actionFinalPseudoState =
        new CmdCreateNode((Class)ModelFacade.FINALSTATE, "FinalState");

    protected static Action _actionBranchPseudoState =
        new ActionCreatePseudostate(ModelFacade.BRANCH_PSEUDOSTATEKIND, "Branch");

    protected static Action _actionForkPseudoState =
        new ActionCreatePseudostate(ModelFacade.FORK_PSEUDOSTATEKIND, "Fork");

    protected static Action _actionJoinPseudoState =
        new ActionCreatePseudostate(ModelFacade.JOIN_PSEUDOSTATEKIND, "Join");

    protected static Action _actionShallowHistoryPseudoState =
        new ActionCreatePseudostate(ModelFacade.SHALLOWHISTORY_PSEUDOSTATEKIND,
				    "ShallowHistory");

    protected static Action _actionDeepHistoryPseudoState =
        new ActionCreatePseudostate(ModelFacade.DEEPHISTORY_PSEUDOSTATEKIND,
				    "DeepHistory");

    protected static Action _actionTransition =
        new CmdSetMode(
		       ModeCreatePolyEdge.class,
		       "edgeClass",
		       (Class)ModelFacade.TRANSITION,
		       "Transition");

    ////////////////////////////////////////////////////////////////
    // contructors

    protected static int _StateDiagramSerial = 1;
    /** 
     *  this constructor is used to build a dummy state diagram so
     *  that a project will load properly.
     */
    public UMLStateDiagram() {

        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) {
        }
    }

    public UMLStateDiagram(Object namespace, MStateMachine sm) {
        this();
        if (sm != null && namespace == null) {
            Object context = sm.getContext();
            if (ModelFacade.isAClassifier(context)) {
                namespace = context;
            } else if (ModelFacade.isABehavioralFeature(context)) {
                namespace = ModelFacade.getOwner(context);
            }
        }
        if (namespace != null && ModelFacade.getName(namespace) != null) {
            String name = null, diag_name = ModelFacade.getName(namespace);
            Object[] args = {name};
            int number = (ModelFacade.getBehaviors(namespace)) == null
                ? 0 
                : ModelFacade.getBehaviors(namespace).size();
            name = diag_name + " " + (number++);
            Argo.log.info("UMLStateDiagram constructor: String name = " + name);
            try {
                setName(name);
            } catch (PropertyVetoException pve) {
            }
        }
        if (namespace != null) {
            setup(namespace, sm);
        }
    }

    /**
     * The owner of a statediagram is the statediagram it's showing.
     */
    public Object getOwner() {
        StateDiagramGraphModel gm = (StateDiagramGraphModel) getGraphModel();
        return gm.getMachine();
    }

    /**
     * Called by the PGML parser to initialize the statediagram. First the
     * parser creates a statediagram via the default constructor. Then this
     * method is called.
     * @see org.tigris.gef.base.Diagram#initialize(Object)
     */
    public void initialize(Object o) {
        if (ModelFacade.isAStateMachine(o)) {
            MStateMachine sm = (MStateMachine) o;
            Object context = sm.getContext();
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
            throw new IllegalStateException("Cannot find context namespace "
					    + "while initializing "
					    + "statediagram");
        }
    }

    /** method to perform a number of important initializations of a
     * StateDiagram.
     * 
     * each diagram type has a similar <I>UMLxxxDiagram</I> class.
     *
     * @param m MClass from the model in NSUML...connects the class to
     * the State diagram.
     * @param sm MStateMachine from the model in NSUML...
     * @modified changed <I>lay</I> from <I>LayerPerspective</I> to
     * <I>LayerPerspectiveMutable</I>.  This class is a child of
     * <I>LayerPerspective</I> and was implemented to correct some
     * difficulties in changing the model. <I>lay</I> is used mainly
     * in <I>LayerManager</I>(GEF) to control the adding, changing and
     * deleting layers on the diagram...
     *
     * @author psager@tigris.org Jan. 24, 2oo2
     */
    public void setup(Object namespace, MStateMachine sm) {
        setNamespace(namespace);

        // add the diagram as a listener to the statemachine so
        // that when the statemachine is removed() the diagram is deleted also.
        UmlModelEventPump.getPump().addModelEventListener(this, sm);
        theStateMachine = sm;

        StateDiagramGraphModel gm = new StateDiagramGraphModel();
        gm.setNamespace(namespace);
        if (sm != null)
            gm.setMachine(sm);
        setGraphModel(gm);
        LayerPerspective lay = new LayerPerspectiveMutable(ModelFacade.getName(namespace), gm);
        setLayer(lay);
        StateDiagramRenderer rend = new StateDiagramRenderer(); // singleton
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
    }

    public MStateMachine getStateMachine() {
        return (MStateMachine)((StateDiagramGraphModel) getGraphModel()).getMachine();
    }

    /**
     * @param sm
     * @param m
     */
    public void setStateMachine(MStateMachine sm) {
        ((StateDiagramGraphModel) getGraphModel()).setMachine(sm);
    }

    /**
     * Get the actions from which to create a toolbar or equivilent
     * graphic triggers
     */
    protected Object[] getUmlActions() {
        Object actions[] = {
            _actionState,
            _actionCompositeState,
            _actionTransition, null,
            _actionStartPseudoState,
            _actionFinalPseudoState,
            _actionBranchPseudoState,
            _actionForkPseudoState,
            _actionJoinPseudoState,
            _actionShallowHistoryPseudoState,
            _actionDeepHistoryPseudoState,
            null,
            ActionAddNote.SINGLETON,
            null
        };
        return actions;
    }

    protected static String getNewDiagramName() {
        String name = null;
        name = "State Diagram " + _StateDiagramSerial;
        _StateDiagramSerial++;
        if (!ProjectManager.getManager().getCurrentProject()
                .isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
    }

    /**
     * This diagram listens to NSUML events from its Statemachine;
     * When the Statemachine is removed, we also want to delete this
     * diagram too.
     */
    public void removed(MElementEvent e) {

        UmlModelEventPump.getPump().removeModelEventListener(
							     this,
							     theStateMachine);
        super.removed(e);
    }
} /* end class UMLStateDiagram */