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


package uci.uml.visual;

import java.util.*;
import java.awt.*;
import java.beans.*;
import javax.swing.*;

import uci.gef.*;
import uci.graph.*;
import uci.ui.*;
import uci.uml.ui.*;
//import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Behavioral_Elements.State_Machines.*;


public class UMLStateDiagram extends UMLDiagram {

  ////////////////
  // actions for toolbar

  protected static Action _actionState =
  new CmdCreateNode(State.class, "State");

  protected static Action _actionCompositeState =
  new CmdCreateNode(CompositeState.class, "CompositeState");

  // start state, end state, forks, joins, etc.
  protected static Action _actionStartPseudoState =
  new ActionCreatePseudostate(PseudostateKind.INITIAL, "Initial");

  protected static Action _actionFinalPseudoState =
  new ActionCreatePseudostate(PseudostateKind.FINAL, "FinalState");

  protected static Action _actionBranchPseudoState =
  new ActionCreatePseudostate(PseudostateKind.BRANCH, "Branch");

  protected static Action _actionForkPseudoState =
  new ActionCreatePseudostate(PseudostateKind.FORK, "Fork");

  protected static Action _actionJoinPseudoState =
  new ActionCreatePseudostate(PseudostateKind.JOIN, "Join");

  protected static Action _actionHistoryPseudoState =
  new ActionCreatePseudostate(PseudostateKind.SHALLOW_HISTORY, "ShallowHistory");


  protected static Action _actionTransition =
  new CmdSetMode(ModeCreatePolyEdge.class,
		 "edgeClass", Transition.class,
		 "Transition");


  ////////////////////////////////////////////////////////////////
  // contructors

  protected static int _StateDiagramSerial = 1;


  public UMLStateDiagram() {
    String name = "state diagram " + _StateDiagramSerial;
    _StateDiagramSerial++;
    try { setName(name); } catch (PropertyVetoException pve) { }
  }

  public UMLStateDiagram(Namespace m) {
    this();
    setNamespace(m);
    StateMachine sm = getStateMachine();
    String name = null;
    if (sm.getContext() != null && sm.getContext().getName() != Name.UNSPEC &&
	sm.getContext().getName().getBody().length() > 0) {
      name = sm.getContext().getName().getBody() + " states";
      try { setName(name); }
      catch (PropertyVetoException pve) { }
    }
  }

  public void setNamespace(Namespace m) {
    super.setNamespace(m);
    StateDiagramGraphModel gm = new StateDiagramGraphModel();
    gm.setNamespace(m);
    StateMachine sm = null;
    Vector beh = m.getBehavior();
    if (beh.size() == 1) sm = (StateMachine) beh.elementAt(0);
    else System.out.println("could not setMachine()");
    gm.setMachine(sm);
    setGraphModel(gm);
    LayerPerspective lay = new LayerPerspective(m.getName().getBody(), gm);
    setLayer(lay);
    StateDiagramRenderer rend = new StateDiagramRenderer(); // singleton
    lay.setGraphNodeRenderer(rend);
    lay.setGraphEdgeRenderer(rend);
  }

  public StateMachine getStateMachine() {
    return ((StateDiagramGraphModel)getGraphModel()).getMachine();
  }

  public void setStateMachine(StateMachine sm) {
    ((StateDiagramGraphModel)getGraphModel()).setMachine(sm);
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
    _toolBar.add(_actionHistoryPseudoState);
    _toolBar.addSeparator();

    _toolBar.add(Actions.AddInternalTrans);
    _toolBar.addSeparator();

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
