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

import uci.uml.Behavioral_Elements.Common_Behavior.ActionSequence;
import uci.uml.Foundation.Core.Attribute;
import uci.uml.Foundation.Data_Types.Name;

/** The actions associated with a state are in the same Namespace as
 *  the State. */

public class State extends StateVertex {
  public ActionSequence _entry;
  public ActionSequence _exit;
  public Vector _deferredEvent;
  public StateMachine _stateMachine;
  public Vector _stateVariable;
  public Vector _classifierInState;
  public Vector _internalTransition;

  public State() { }
  public State(Name name) { super(name); }
  public State(String nameStr) { super(new Name(nameStr)); }

  public ActionSequence getEntry() { return _entry; }
  public void setEntry(ActionSequence x) throws PropertyVetoException {
    fireVetoableChange("entry", _entry, x);
    _entry = x;
    _entry.setNamespace(getNamespace());
  }
  
  public ActionSequence getExit() { return _exit; }
  public void setExit(ActionSequence x) throws PropertyVetoException {
    fireVetoableChange("exit", _exit, x);
    _exit = x;
    _exit.setNamespace(getNamespace());
  }

  public Vector getDeferredEvent() { return _deferredEvent; }
  public void setDeferredEvent(Vector x) throws PropertyVetoException {
    if (_deferredEvent == null) _deferredEvent = new Vector();
    fireVetoableChangeNoCompare("deferredEvent", _deferredEvent, x);
    _deferredEvent = x;
    java.util.Enumeration enum = _deferredEvent.elements();
    while (enum.hasMoreElements()) {
      Event e = (Event) enum.nextElement();
      e.setNamespace(getNamespace());
    }
  }
  public void addDeferredEvent(Event x) throws PropertyVetoException {
    if (_deferredEvent == null) _deferredEvent = new Vector();
    fireVetoableChange("deferredEvent", _deferredEvent, x);
    _deferredEvent.addElement(x);
    x.setNamespace(getNamespace());
  }
  public void removeDeferredEvent(Event x) throws PropertyVetoException {
    if (_deferredEvent == null) return;
    fireVetoableChange("deferredEvent", _deferredEvent, x);
    _deferredEvent.removeElement(x);
  }

  public StateMachine getStateMachine() { return _stateMachine; }
  public void setStateMachine(StateMachine x) throws PropertyVetoException {
    if (_stateMachine == x) return;
    fireVetoableChange("stateMachine", _stateVariable, x);
    StateMachine oldStateMachine = _stateMachine;
    _stateMachine = x;
    if (oldStateMachine != null && oldStateMachine.getTop() == this) {
      oldStateMachine.setTop(null);
    }
  }

  public Vector getStateVariable() { return _stateVariable; }
  public void setStateVariable(Vector x) throws PropertyVetoException {
    if (_stateVariable == null) _stateVariable = new Vector();
    fireVetoableChangeNoCompare("stateVariable", _stateVariable, x);
    _stateVariable = x;
    java.util.Enumeration enum = _stateVariable.elements();
    while (enum.hasMoreElements()) {
      Attribute a = (Attribute) enum.nextElement();
      a.setNamespace(getNamespace());
    }
  }
  public void addStateVariable(Attribute x) throws PropertyVetoException {
    if (_stateVariable == null) _stateVariable = new Vector();
    fireVetoableChange("stateVariable", _stateVariable, x);
    _stateVariable.addElement(x);
    x.setNamespace(getNamespace());
  }
  public void removeStateVariable(Attribute x) throws PropertyVetoException {
    if (_stateVariable == null) return;
    fireVetoableChange("stateVariable", _stateVariable, x);
    _stateVariable.removeElement(x);
  }

  public Vector getClassifierInState() { return _classifierInState; }
  public void setClassifierInState(Vector x) throws PropertyVetoException {
    fireVetoableChangeNoCompare("classifierInState", _classifierInState, x);
    if (_classifierInState == null) _classifierInState = new Vector();
    _classifierInState = x;
  }
  public void addClassifierInState(ClassifierInState x)
       throws PropertyVetoException {
    if (_classifierInState == null) _classifierInState = new Vector();
    fireVetoableChange("classifierInState", _classifierInState, x);
    _classifierInState.addElement(x);
  }
  public void removeClassifierInState(ClassifierInState x)
       throws PropertyVetoException {
    if (_classifierInState == null) return;
    fireVetoableChange("classifierInState", _classifierInState, x);
    _classifierInState.removeElement(x);
  }

  public Vector getInternalTransition() { return _internalTransition; }
  public void setInternalTransition(Vector x) throws PropertyVetoException {
    if (_internalTransition == null) _internalTransition = new Vector();
    fireVetoableChangeNoCompare("internalTransition", _internalTransition, x);
    java.util.Enumeration enum = _internalTransition.elements();
    while (enum.hasMoreElements()) {
      Transition t = (Transition) enum.nextElement();
      t.setSource(null);
      t.setTarget(null);
      t.setNamespace(null);
    }
    _internalTransition = x;
    enum = _internalTransition.elements();
    while (enum.hasMoreElements()) {
      Transition t = (Transition) enum.nextElement();
      t.setSource(this);
      t.setTarget(this);
      t.setNamespace(getNamespace());
    }
  }
  public void addInternalTransition(Transition x) throws PropertyVetoException {
    if (_internalTransition == null) _internalTransition = new Vector();
    if (_internalTransition.contains(x)) return;
    fireVetoableChange("internalTransition", _internalTransition, x);
    _internalTransition.addElement(x);
    x.setSource(this);
    x.setTarget(this);
    x.setNamespace(getNamespace());
  }
  public void removeInternalTransition(Transition x) throws PropertyVetoException {
    if (_internalTransition == null) return;
    fireVetoableChange("internalTransition", _internalTransition, x);
    _internalTransition.removeElement(x);
    x.setSource(null);
    x.setTarget(null);
    x.setNamespace(null);
  }

  public Object prepareForTrash() throws PropertyVetoException {
    setStateMachine(null);
    return super.prepareForTrash();
    //needs-more-work
  }

  public void recoverFromTrash(Object momento) throws PropertyVetoException {
    // needs-more-work
    super.recoverFromTrash(momento);
  }

  ////////////////////////////////////////////////////////////////
  // debugging

  public String dbgString() {
    String s = "State " + (getName() == null?"(anon)":getName().getBody());
    s += "     {\n";
    if (_entry != null) s += "    Entry:" + _entry.toString() + "\n";
    if (_exit != null) s += "    Exit:" + _exit.toString() + "\n";
    if (_internalTransition != null) {
      java.util.Enumeration interns = _internalTransition.elements();
      while (interns.hasMoreElements()) {
	Transition t = (Transition) interns.nextElement();
	s += "    " + t.dbgString() + "\n";
      }
    }
    s += "    }\n";
    return s;
  }

  static final long serialVersionUID = 6934758603162888178L;
}
