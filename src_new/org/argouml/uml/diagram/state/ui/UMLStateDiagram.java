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

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
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

public class UMLStateDiagram extends UMLDiagram {
    protected static Category cat = Category.getInstance(UMLStateDiagram.class);

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
        if (m != null && m.getNamespace() != null)
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

    /** initialize the toolbar for this diagram type */
    protected void initToolBar() {
        cat.debug("making state toolbar");
        _toolBar = new ToolBar();
        _toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        //_toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        _toolBar.add(_actionSelect);
        _toolBar.add(_actionBroom);
        _toolBar.addSeparator();

        _toolBar.add(_actionState);
        _toolBar.add(_actionCompositeState);
        _toolBar.add(_actionTransition);
        _toolBar.addSeparator();

        _toolBar.add(_actionStartPseudoState);
        _toolBar.add(_actionFinalPseudoState);
        _toolBar.add(_actionBranchPseudoState);
        _toolBar.add(_actionForkPseudoState);
        _toolBar.add(_actionJoinPseudoState);
        _toolBar.add(_actionShallowHistoryPseudoState);
        _toolBar.add(_actionDeepHistoryPseudoState);
        _toolBar.addSeparator();
        _toolBar.add(ActionAddNote.SINGLETON);
        _toolBar.addSeparator();

        //_toolBar.add(Actions.AddInternalTrans);
        //_toolBar.addSeparator();

        _toolBar.add(_actionRectangle);
        _toolBar.add(_actionRRectangle);
        _toolBar.add(_actionCircle);
        _toolBar.add(_actionLine);
        _toolBar.add(_actionText);
        _toolBar.add(_actionPoly);
        _toolBar.add(_actionSpline);
        _toolBar.add(_actionInk);
        _toolBar.addSeparator();

        _toolBar.add(_diagramName.getJComponent());
    }

    protected static String getNewDiagramName() {
        String name = null;
        name = "state diagram " + _StateDiagramSerial;
        _StateDiagramSerial++;

        return name;
    }

} /* end class UMLStateDiagram */
