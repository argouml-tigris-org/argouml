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

import java.util.*;
import java.awt.*;
import java.beans.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.state_machines.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.ui.*;

import org.argouml.ui.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.state.*;

// get the note from the class diagram
import org.argouml.uml.ui.*;
import org.argouml.uml.diagram.static_structure.ui.FigNote;

public class UMLStateDiagram extends UMLDiagram {

    ////////////////
    // actions for toolbar

    protected static Action _actionState =
	new CmdCreateNode(MStateImpl.class, "State");

    protected static Action _actionCompositeState =
	new CmdCreateNode(MCompositeStateImpl.class, "CompositeState");

    // start state, end state, forks, joins, etc.
    protected static Action _actionStartPseudoState =
	new ActionCreatePseudostate(MPseudostateKind.INITIAL, "Initial");

    protected static Action _actionFinalPseudoState =
	new CmdCreateNode(MFinalStateImpl.class, "FinalState");

    protected static Action _actionBranchPseudoState =
	new ActionCreatePseudostate(MPseudostateKind.BRANCH, "Branch");

    protected static Action _actionForkPseudoState =
	new ActionCreatePseudostate(MPseudostateKind.FORK, "Fork");

    protected static Action _actionJoinPseudoState =
	new ActionCreatePseudostate(MPseudostateKind.JOIN, "Join");

    protected static Action _actionShallowHistoryPseudoState =
	new ActionCreatePseudostate(MPseudostateKind.SHALLOW_HISTORY, "ShallowHistory");
 
    protected static Action _actionDeepHistoryPseudoState =
	new ActionCreatePseudostate(MPseudostateKind.DEEP_HISTORY, "DeepHistory");


    protected static Action _actionTransition =
	new CmdSetMode(ModeCreatePolyEdge.class,
		       "edgeClass", MTransitionImpl.class,
		       "Transition");


    ////////////////////////////////////////////////////////////////
    // contructors

    protected static int _StateDiagramSerial = 1;


    public UMLStateDiagram() {
	String name = "state diagram " + _StateDiagramSerial;
	_StateDiagramSerial++;
	try { setName(name); } catch (PropertyVetoException pve) { }
    }

    public UMLStateDiagram(MClass m, MStateMachine sm) {
	this();
	if (m != null && m.getName() != null) {
	    String name = m.getName();
	    // + " states "+ (m.getBehaviors().size());
	    try { setName(name); }
	    catch (PropertyVetoException pve) { }
	}
	if (m != null && m.getNamespace() != null) setup(m, sm);	
    }

    public MModelElement getOwner() {
	StateDiagramGraphModel gm = (StateDiagramGraphModel)getGraphModel();
	MStateMachine sm = gm.getMachine();
	if (sm != null) return sm;
	return gm.getNamespace();
    }

    public void initialize(Object o) {
	if (!(o instanceof MStateMachine)) return;
	MStateMachine sm = (MStateMachine)o;
	MModelElement context = sm.getContext();
	if (context != null && context instanceof MClass)
	    setup((MClass)context, sm);
	else
	    System.out.println("Statemachine without context not yet possible :-(");
    }
    
    public void setup(MClass m, MStateMachine sm) {
	// 	  MNamespace namespace = null;
	// 	  if (m.getNamespace() != null) namespace = m.getNamespace();
	// 	  else namespace = m;
	  
	setNamespace(m);
	StateDiagramGraphModel gm = new StateDiagramGraphModel();
	gm.setNamespace(m);
	if (sm != null) gm.setMachine(sm);
	setGraphModel(gm);
	LayerPerspective lay = new LayerPerspective(m.getName(), gm);
	setLayer(lay);
	StateDiagramRenderer rend = new StateDiagramRenderer(); // singleton
	lay.setGraphNodeRenderer(rend);
	lay.setGraphEdgeRenderer(rend);
    }
	  
    public MStateMachine getStateMachine() {
	return ((StateDiagramGraphModel)getGraphModel()).getMachine();
    }

    public void setStateMachine(MStateMachine sm) {
	((StateDiagramGraphModel)getGraphModel()).setMachine(sm);
    }

    /**
     * This diagram type uses a postLoad operation to set the encloser
     * of some figures. Since it requires the state machine of this
     * diagram and the figures are created by the PGML parser, I delay
     * the computation of the encloser.
     *
     * @author Andreas Rueckert <a_rueckert@gmx.net>
     */
    public void postLoad() {
	Vector nodes = getLayer().getContents();  // Get the figures of this diagram.
	MStateMachine machine = getStateMachine();  // Get the state machine of this diagram.
	
	for( Enumeration e = nodes.elements(); e.hasMoreElements(); ) {
	    Fig f = (Fig) e.nextElement();
	    if(f instanceof FigStateVertex) {  // Set the encloser for vertex subclasses.
		((FigStateVertex)f).setActualEncloser(machine);
	    }
	}

	// Since setting the enclosing figure should be done while loading, I finish it 1st,
	// before finishing the load operation.
	super.postLoad();
    }

    /** initialize the toolbar for this diagram type */
    protected void initToolBar() {
	//System.out.println("making state toolbar");
	_toolBar = new ToolBar();
	_toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
	//     _toolBar.add(Actions.Cut);
	//     _toolBar.add(Actions.Copy);
	//     _toolBar.add(Actions.Paste);
	//     _toolBar.addSeparator();

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

	_toolBar.add(_diagramName);
    }

} /* end class UMLStateDiagram */
