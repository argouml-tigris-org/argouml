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

// File: UMLActivityDiagram.java
// Classes: UMLActivityDiagram
// Original Author: your email here
// $Id$

package org.argouml.uml.diagram.activity.ui;

import java.beans.PropertyVetoException;

import javax.swing.Action;

import org.apache.log4j.Category;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.state.StateDiagramGraphModel;
import org.argouml.uml.diagram.state.ui.ActionCreatePseudostate;
import org.argouml.uml.diagram.state.ui.StateDiagramRenderer;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.ActionAddNote;
import org.tigris.gef.base.CmdSetMode;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;

import ru.novosoft.uml.behavior.activity_graphs.MActivityGraph;
import ru.novosoft.uml.behavior.state_machines.MFinalState;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;

/** Enabling an activity diagram connected to an
 * actor has been requested as a feature.
 *
 * As well enabling swim lanes in the activity
 * diagram is considered valuable as well.
 */
public class UMLActivityDiagram extends UMLDiagram {
    protected static Category cat =
        Category.getInstance(UMLActivityDiagram.class);

    ////////////////
    // actions for toolbar

    protected static Action _actionState =
        new CmdCreateNode((Class) ModelFacade.ACTION_STATE, "ActionState");

    // start state, end state, forks, joins, etc.
    protected static Action _actionStartPseudoState =
        new ActionCreatePseudostate(MPseudostateKind.INITIAL, "Initial");

    protected static Action _actionFinalPseudoState =
        new CmdCreateNode(MFinalState.class, "FinalState");

    protected static Action _actionBranchPseudoState =
        new ActionCreatePseudostate(MPseudostateKind.BRANCH, "Branch");

    protected static Action _actionForkPseudoState =
        new ActionCreatePseudostate(MPseudostateKind.FORK, "Fork");

    protected static Action _actionJoinPseudoState =
        new ActionCreatePseudostate(MPseudostateKind.JOIN, "Join");

    protected static Action _actionTransition =
        new CmdSetMode(
		       ModeCreatePolyEdge.class,
		       "edgeClass",
		       MTransition.class,
		       "Transition");

    ////////////////////////////////////////////////////////////////
    // contructors

    protected static int _ActivityDiagramSerial = 1;

    public UMLActivityDiagram() {

        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) {
        }
    }

    public UMLActivityDiagram(MNamespace m) {
        this();
        setNamespace(m);
        MStateMachine sm = getStateMachine();
        String name = null;
        if (sm.getContext() != null
            && sm.getContext().getName() != null
            && sm.getContext().getName().length() > 0) {
            name = sm.getContext().getName();
            try {
                setName(name);
            } catch (PropertyVetoException pve) {
            }
        }
    }

    public UMLActivityDiagram(MNamespace m, MActivityGraph agraph) {

        this();
        if (m != null && m.getName() != null) {
            String name =
                m.getName() + " activity " + (m.getBehaviors().size());
            try {
                setName(name);
            } catch (PropertyVetoException pve) {
            }
        }
        if (m != null)
            setup(m, agraph);
        else 
            throw new NullPointerException("Namespace may not be null");
    }

    public void initialize(Object o) {
        if (!(o instanceof MActivityGraph))
            return;
        MActivityGraph sm = (MActivityGraph) o;
        MModelElement context = sm.getContext();
        if (context != null && context instanceof MNamespace)
            setup((MNamespace) context, sm);
        else
            cat.debug("ActivityGraph without context not yet possible :-(");
    }

    /** method to perform a number of important initializations of an
     * <I>Activity Diagram</I>.
     * 
     * each diagram type has a similar <I>UMLxxxDiagram</I> class.
     *
     * @param m  MNamespace from the model in NSUML...
     * @param agraph MActivityGraph from the model in NSUML...
     * @modified changed <I>lay</I> from <I>LayerPerspective</I> to
     * <I>LayerPerspectiveMutable</I>.  This class is a child of
     * <I>LayerPerspective</I> and was implemented to correct some
     * difficulties in changing the model. <I>lay</I> is used mainly
     * in <I>LayerManager</I>(GEF) to control the adding, changing and
     * deleting layers on the diagram...  psager@tigris.org Jan. 24,
     * 2oo2
     */
    public void setup(MNamespace m, MActivityGraph agraph) {
        super.setNamespace(m);
        StateDiagramGraphModel gm = new StateDiagramGraphModel();
        gm.setNamespace(m);
        if (agraph != null) {
            gm.setMachine(agraph);
        }
        setGraphModel(gm);
        LayerPerspective lay = new LayerPerspectiveMutable(m.getName(), gm);
        setLayer(lay);
        StateDiagramRenderer rend = new StateDiagramRenderer(); // singleton
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
    }

    public MModelElement getOwner() {
        StateDiagramGraphModel gm = (StateDiagramGraphModel) getGraphModel();
        MStateMachine sm = gm.getMachine();
        if (sm != null)
            return sm;
        return gm.getNamespace();
    }

    public MStateMachine getStateMachine() {
        return ((StateDiagramGraphModel) getGraphModel()).getMachine();
    }

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
	    _actionTransition,
	    null,
	    _actionStartPseudoState,
	    _actionFinalPseudoState,
	    _actionBranchPseudoState,
	    _actionForkPseudoState,
	    _actionJoinPseudoState,
	    null,
	    ActionAddNote.SINGLETON 
	};
        return actions;
    }

    /**
     * Creates a new diagramname.
     * @return String
     */
    protected static String getNewDiagramName() {
        String name = null;
        name = "Activity Diagram " + _ActivityDiagramSerial;
        _ActivityDiagramSerial++;
        if (!ProjectManager.getManager().getCurrentProject()
                .isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
    }
} /* end class UMLActivityDiagram */
