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

import org.tigris.gef.base.CmdSetMode;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.ui.*;

import org.argouml.ui.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.state.*;
import org.apache.commons.logging.Log;
import org.argouml.application.api.*;
// get the note from the class diagram
import org.argouml.uml.ui.*;
import org.argouml.uml.diagram.static_structure.ui.FigComment;

public class UMLStateDiagram extends UMLDiagram {
    protected static Log logger = org.apache.commons.logging.LogFactory.getLog(UMLStateDiagram.class);

  ////////////////
  // actions for toolbar

  protected static Action _actionState =
  new CmdCreateNode(MState.class, "State");

  protected static Action _actionCompositeState =
  new CmdCreateNode(MCompositeState.class, "CompositeState");

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

  protected static Action _actionShallowHistoryPseudoState =
  new ActionCreatePseudostate(MPseudostateKind.SHALLOW_HISTORY, "ShallowHistory");
 
 protected static Action _actionDeepHistoryPseudoState =
  new ActionCreatePseudostate(MPseudostateKind.DEEP_HISTORY, "DeepHistory");


  protected static Action _actionTransition =
  new CmdSetMode(ModeCreatePolyEdge.class,
		 "edgeClass", MTransition.class,
		 "Transition");


  ////////////////////////////////////////////////////////////////
  // contructors

  protected static int _StateDiagramSerial = 1;
/** 
 *  this constructor is used to build a dummy state diagram so that a project
 *  will load properly.
 */
  public UMLStateDiagram() {
  	
    try { setName(getNewDiagramName()); } catch (PropertyVetoException pve) { }
  }

  public UMLStateDiagram(MNamespace m, MStateMachine sm) {
      this();
	if (m != null && m.getName() != null) {
        String name = null, diag_name = m.getName();
        Object[] args = {name};
        int number = 
            ((m.getBehaviors()) == null ? 0 : m.getBehaviors().size());
        name = diag_name + " " + number;
        while (TheInstance.vetoCheck("state", args)) {
            name = diag_name + " " + (number++);
        }
        Argo.logger.info("UMLStateDiagram constructor: String name = " + name);
		try { setName(name); }
		catch (PropertyVetoException pve) { }
    }
	if (m != null && m.getNamespace() != null) setup(m, sm);
	
  }

	public MModelElement getOwner() {
		StateDiagramGraphModel gm = (StateDiagramGraphModel)getGraphModel();
                Argo.logger.info("UMLStateDiagram.getOwner()...GraphModel = " + gm);
                MStateMachine sm = gm.getMachine();
		if (sm != null) return sm;
                Argo.logger.info("UMLStateDiagram.getOwner()...NameSpace = " + gm.getNamespace());
		return gm.getNamespace();
	}

	public void initialize(Object o) {
		if (!(o instanceof MStateMachine)) return;
		MStateMachine sm = (MStateMachine)o;
		MModelElement context = sm.getContext();
		if (context != null && context instanceof MClass)
			setup((MClass)context, sm);
		else
			logger.warn("Statemachine without context not yet possible :-(");
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
	  if (sm != null) gm.setMachine(sm);
	  setGraphModel(gm);
	  LayerPerspective lay = new LayerPerspectiveMutable(m.getName(), gm);
	  setLayer(lay);
	  StateDiagramRenderer rend = new StateDiagramRenderer(); // singleton
	  lay.setGraphNodeRenderer(rend);
	  lay.setGraphEdgeRenderer(rend);
  }
	  
public MStateMachine getStateMachine() {
    return ((StateDiagramGraphModel)getGraphModel()).getMachine();
  }

/**
 * @param sm
 * @param m
 */
  public void setStateMachine(MStateMachine sm) {
    ((StateDiagramGraphModel)getGraphModel()).setMachine(sm);
  }

  /** initialize the toolbar for this diagram type */
  protected void initToolBar() {
    logger.debug("making state toolbar");
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
  
  protected static String getNewDiagramName() {
  	String name = null;
  	Object[] args = {name};
  	do {
        name = "state diagram " + _StateDiagramSerial;
        _StateDiagramSerial++;
        args[0] = name;
    }
    while (TheInstance.vetoCheck("state", args));
    return name;
  }

} /* end class UMLStateDiagram */
