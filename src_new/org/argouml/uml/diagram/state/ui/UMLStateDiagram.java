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
import javax.swing.JToolBar;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.state.StateDiagramGraphModel;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.ActionAddNote;
import org.tigris.gef.base.CmdSetMode;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.ui.ToolBar;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MFinalState;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;

import ru.novosoft.uml.MElementEvent;

public class UMLStateDiagram extends UMLDiagram {
    protected static Category cat = Category.getInstance(UMLStateDiagram.class);

    /**
     * this diagram needs to be deleted when its statemachine is deleted.
     */
    MStateMachine theStateMachine;
    
    ////////////////
    // actions for toolbar

    protected static Action _actionState = new CmdCreateNode(MState.class, "State");

    protected static Action _actionCompositeState = new CmdCreateNode(MCompositeState.class, "CompositeState");

    // start state, end state, forks, joins, etc.
    protected static Action _actionStartPseudoState = new ActionCreatePseudostate(MPseudostateKind.INITIAL, "Initial");

    protected static Action _actionFinalPseudoState = new CmdCreateNode(MFinalState.class, "FinalState");

    protected static Action _actionBranchPseudoState = new ActionCreatePseudostate(MPseudostateKind.BRANCH, "Branch");

    protected static Action _actionForkPseudoState = new ActionCreatePseudostate(MPseudostateKind.FORK, "Fork");

    protected static Action _actionJoinPseudoState = new ActionCreatePseudostate(MPseudostateKind.JOIN, "Join");

    protected static Action _actionShallowHistoryPseudoState = new ActionCreatePseudostate(MPseudostateKind.SHALLOW_HISTORY, "ShallowHistory");

    protected static Action _actionDeepHistoryPseudoState = new ActionCreatePseudostate(MPseudostateKind.DEEP_HISTORY, "DeepHistory");

    protected static Action _actionTransition = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MTransition.class, "Transition");

    ////////////////////////////////////////////////////////////////
    // contructors

    protected static int _StateDiagramSerial = 1;
    /** 
     *  this constructor is used to build a dummy state diagram so that a project
     *  will load properly.
     */
    public UMLStateDiagram() {

        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) {
        }
    }

    public UMLStateDiagram(MNamespace m, MStateMachine sm) {
        this();
        if (sm != null && m == null) {        
        	MModelElement context = sm.getContext();
        	if (context instanceof MClassifier) {
        	    m = (MNamespace)context;
        	} else
        	if (context instanceof MBehavioralFeature) {
        	    m = ((MBehavioralFeature)context).getOwner();
        	}
        }
        if (m != null && m.getName() != null) {
            String name = null, diag_name = m.getName();
            Object[] args = { name };
            int number = ((m.getBehaviors()) == null ? 0 : m.getBehaviors().size());
            name = diag_name + " " + (number++);
            Argo.log.info("UMLStateDiagram constructor: String name = " + name);
            try {
                setName(name);
            } catch (PropertyVetoException pve) {
            }
        }
        if (m != null)
            setup(m, sm);

    }

    public MModelElement getOwner() {
        StateDiagramGraphModel gm = (StateDiagramGraphModel) getGraphModel();
        Argo.log.info("UMLStateDiagram.getOwner()...GraphModel = " + gm);
        MStateMachine sm = gm.getMachine();
        if (sm != null)
            return sm;
        Argo.log.info("UMLStateDiagram.getOwner()...NameSpace = " + gm.getNamespace());
        return gm.getNamespace();
    }

    /**
     * Called by the PGML parser to initialize the statediagram. First the
     * parser creates a statediagram via the default constructor. Then this
     * method is called.
     * @see org.tigris.gef.base.Diagram#initialize(Object)
     */
    public void initialize(Object o) {
        if (!(o instanceof MStateMachine))
            return;
        MStateMachine sm = (MStateMachine) o;
        MModelElement context = sm.getContext();
        MNamespace contextNamespace = null;
        if (context instanceof MClassifier) {
            contextNamespace = (MClassifier)context;
        } else
        if (context instanceof MBehavioralFeature) {
            contextNamespace = ((MBehavioralFeature)context).getOwner().getNamespace();
        }
        if (contextNamespace != null) {
            setup(contextNamespace, sm);
        } else
        	throw new IllegalStateException("Cannot find context namespace while initializing statediagram");
    }

    /** method to perform a number of important initializations of a StateDiagram. 
     * 
     * each diagram type has a similar <I>UMLxxxDiagram</I> class.
     *
     * @param m  MClass from the model in NSUML...connects the class to the State diagram.
     * @param sm MStateMachine from the model in NSUML...
     * @modified changed <I>lay</I> from <I>LayerPerspective</I> to <I>LayerPerspectiveMutable</I>. 
     *           This class is a child of <I>LayerPerspective</I> and was implemented 
     *           to correct some difficulties in changing the model. <I>lay</I> is used 
     *           mainly in <I>LayerManager</I>(GEF) to control the adding, changing and 
     *           deleting layers on the diagram...
     *           psager@tigris.org   Jan. 24, 2oo2
     */
    public void setup(MNamespace m, MStateMachine sm) {
        setNamespace(m);
        
        // add the diagram as a listener to the statemachine so
        // that when the statemachine is removed() the diagram is deleted also.
        UmlModelEventPump.getPump().addModelEventListener(this, sm);
        theStateMachine = sm;
        
        StateDiagramGraphModel gm = new StateDiagramGraphModel();
        gm.setNamespace(m);
        if (sm != null)
            gm.setMachine(sm);
        setGraphModel(gm);
        LayerPerspective lay = new LayerPerspectiveMutable(m.getName(), gm);
        setLayer(lay);
        StateDiagramRenderer rend = new StateDiagramRenderer(); // singleton
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
    }

    public MStateMachine getStateMachine() {
        return ((StateDiagramGraphModel) getGraphModel()).getMachine();
    }

    /**
     * @param sm
     * @param m
     */
    public void setStateMachine(MStateMachine sm) {
        ((StateDiagramGraphModel) getGraphModel()).setMachine(sm);
    }

    /**
     * <p>Initialize the toolbar with buttons required for a use case diagram.</p>
     * @param toolBar The toolbar to which to add the buttons.
     */
    protected void initToolBar(JToolBar toolBar) {
        toolBar.add(_actionState);
        toolBar.add(_actionCompositeState);
        toolBar.add(_actionTransition);
        toolBar.addSeparator();

        toolBar.add(_actionStartPseudoState);
        toolBar.add(_actionFinalPseudoState);
        toolBar.add(_actionBranchPseudoState);
        toolBar.add(_actionForkPseudoState);
        toolBar.add(_actionJoinPseudoState);
        toolBar.add(_actionShallowHistoryPseudoState);
        toolBar.add(_actionDeepHistoryPseudoState);
        toolBar.addSeparator();
        toolBar.add(ActionAddNote.SINGLETON);
        toolBar.addSeparator();

        //_toolBar.add(Actions.AddInternalTrans);
        //_toolBar.addSeparator();
    }

    protected static String getNewDiagramName() {
        String name = null;
        name = "State Diagram " + _StateDiagramSerial;
        _StateDiagramSerial++;
        if (!ProjectManager.getManager().getCurrentProject().isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
    }
    
  /**
   * This diagram listens to NSUML events from its Statemachine;
   * When the Statemachine is removed, we also want to delete this diagram too.
   */
  public void removed(MElementEvent e){
      
      UmlModelEventPump.getPump().removeModelEventListener(this,theStateMachine);
      super.removed(e);
  }
} /* end class UMLStateDiagram */
