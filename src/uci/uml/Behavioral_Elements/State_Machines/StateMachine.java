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

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** By default, Statemachines are in the same Namespace as their
 *  context ModelElement, and Transitions and States and
 *  substatemachines within a StateMachine are in the same
 *  Namespace. */

public class StateMachine extends ModelElementImpl {
  public ModelElement _context;
  public State _top;
  public Vector _transitions = new Vector();
  public Vector _submachineState;

  public StateMachine() { }
  public StateMachine(Name name) { super(name); }
  public StateMachine(Name name, ModelElement context) {
    super(name);
    try { setContext(context); }
    catch (PropertyVetoException pve) { }
  }
  public StateMachine(String nameStr) { super(new Name(nameStr)); }
  public StateMachine(String nameStr, ModelElement context) {
    this(new Name(nameStr), context);
  }

  public ModelElement getContext() { return _context; }
  public void setContext(ModelElement x) throws PropertyVetoException {
    if (_context == x) return;
    fireVetoableChange("context", _context, x);
    if (_context != null) _context.removeBehavior(this);
    _context = x;
    if (_context != null) {
      _context.addBehavior(this);
      //setNamespace(_context.getNamespace());
    }
  }

  public State getTop() { return _top; }
  public void setTop(State x) throws PropertyVetoException {
    if (_top == x) return;
    fireVetoableChange("top", _top, x);
    _top = x;
    if (_top != null) {
      _top.setStateMachine(this);
      _top.setNamespace(getNamespace());
    }
  }

  public Vector getTransitions() { return _transitions; }
  public void setTransitions(Vector x) throws PropertyVetoException {
    if (_transitions == null) _transitions = new Vector();
    fireVetoableChangeNoCompare("transitions", _transitions, x);
    _transitions = x;
    java.util.Enumeration enum = _transitions.elements();
    while (enum.hasMoreElements()) {
      Transition t = (Transition) enum.nextElement();
      t.setNamespace(getNamespace());
    }
  }
  public void addTransition(Transition x) throws PropertyVetoException {
    if (_transitions == null) _transitions = new Vector();
    if (_transitions.contains(x)) return;
    fireVetoableChange("transitions", _transitions, x);
    _transitions.addElement(x);
    x.setStateMachine(this);
    x.setNamespace(getNamespace());
  }
  public void removeTransition(Transition x) throws PropertyVetoException {
    if (_transitions == null) return;
    if (!_transitions.contains(x)) return;
    fireVetoableChange("transitions", _transitions, x);
    _transitions.removeElement(x);
    x.setStateMachine(null);
  }

  public Vector getSubmachineState() { return _submachineState; }
  public void setSubmachineState(Vector x) throws PropertyVetoException {
    if (_submachineState == null) _submachineState = new Vector();
    fireVetoableChangeNoCompare("submachineState", _submachineState, x);
    _submachineState = x;
    java.util.Enumeration enum = _submachineState.elements();
    while (enum.hasMoreElements()) {
      StateMachine sm = (StateMachine) enum.nextElement();
      sm.setNamespace(getNamespace());
    }
  }
  public void addSubmachineState(SubmachineState x) throws PropertyVetoException {
    if (_submachineState == null) _submachineState = new Vector();
    fireVetoableChange("submachineState", _submachineState, x);
    _submachineState.addElement(x);
    x.setNamespace(getNamespace());
  }
  public void removeSubmachineState(SubmachineState x)
       throws PropertyVetoException {
    if (_submachineState == null) return;
    fireVetoableChange("submachineState", _submachineState, x);
    _submachineState.removeElement(x);
  }

  public Vector alsoTrash() {
    Vector res = new Vector();
    res.addElement(getTop());
    return res;
  }
  
  ////////////////////////////////////////////////////////////////
  // debugging

  public String dbgString() {
    String s = "StateMachine " + (getName() == null?"(anon)":getName().getBody());
    s += " {\n";
    if (_top != null) {
      s += "\n\nStates ================\n";
      s += _top.dbgString();
    }
    if (_transitions != null) {
      s += "\n\nTransitions ================\n";
      java.util.Enumeration trans = _transitions.elements();
      while (trans.hasMoreElements()) {
	Transition t = (Transition) trans.nextElement();
	s += "  " + t.dbgString() + "\n";
      }
    }
    s += "}";
    return s;
  }

  static final long serialVersionUID = -5514946867958180870L;
}
