// Source file: Behavioral_Elements/State_Machines/State.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

import uci.uml.Behavioral_Elements.Common_Behavior.ActionSequence;
import uci.uml.Foundation.Core.Attribute;


public class State extends StateVertex {
  public ActionSequence _entry;
  public ActionSequence _exit;
  //% public Event _deferredEvent[];
  public Vector _deferredEvent;
  public StateMachine _stateMachine;
  //% public Attribute _stateVariable[];
  public Vector _stateVariable;
  //% public ClassifierInState _classifierInState[];
  public Vector _classifierInState;
  //% public Transition _internalTransition[];
  public Vector _internalTransition;
    
  public State() { }

  public ActionSequence getEntry() { return _entry; }
  public void setEntry(ActionSequence x) {
    _entry = x;
  }
  
  public ActionSequence getExit() { return _exit; }
  public void setExit(ActionSequence x) {
    _exit = x;
  }

  public Vector getDeferredEvent() { return _deferredEvent; }
  public void setDeferredEvent(Vector x) {
    _deferredEvent = x;
  }
  public void addDeferredEvent(Event x) {
    if (_deferredEvent == null) _deferredEvent = new Vector();
    _deferredEvent.addElement(x);
  }
  public void removeDeferredEvent(Event x) {
    _deferredEvent.removeElement(x);
  }

  public StateMachine getStateMachine() { return _stateMachine; }
  public void setStateMachine(StateMachine x) {
    _stateMachine = x;
  }

  public Vector getStateVariable() { return _stateVariable; }
  public void setStateVariable(Vector x) {
    _stateVariable = x;
  }
  public void addStateVariable(Attribute x) {
    if (_stateVariable == null) _stateVariable = new Vector();
    _stateVariable.addElement(x);
  }
  public void removeStateVariable(Attribute x) {
    _stateVariable.removeElement(x);
  }

  public Vector getClassifierInState() {
    return _classifierInState; }
  public void setClassifierInState(Vector x) {
    _classifierInState = x;
  }
  public void addClassifierInState(ClassifierInState x) {
    if (_classifierInState == null) _classifierInState = new Vector();
    _classifierInState.addElement(x);
  }
  public void removeClassifierInState(ClassifierInState x) {
    _classifierInState.removeElement(x);
  }

  public Vector getInternalTransition() {
    return _internalTransition;
  }
  public void setInternalTransition(Vector x) {
    _internalTransition = x;
  }
  public void addInternalTransition(Transition x) {
    if (_internalTransition == null) _internalTransition = new
				       Vector();
    _internalTransition.addElement(x);
  }
  public void removeInternalTransition(Transition x) {
    _internalTransition.removeElement(x);
  }
  
}
