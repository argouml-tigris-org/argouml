// Source file: Behavioral_Elements/State_Machines/StateMachine.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

import uci.uml.Foundation.Core.*;


public class StateMachine extends ModelElementImpl {
  public ModelElement _context;
  public State _top;
  //% public Transition _transitions[];
  public Vector _transitions;
  //% public SubmachineState _submachineState[];
  public Vector _submachineState;
  
  public StateMachine() { }

  public ModelElement getContext() { return _context; }
  public void setContext(ModelElement x) {
    _context = x;
  }

  public State getTop() { return _top; }
  public void setTop(State x) {
    _top = x;
  }

  public Vector getTransitions() { return _transitions; }
  public void setTransitions(Vector x) {
    _transitions = x;
  }
  public void addTransitions(Transition x) {
    if (_transitions == null) _transitions = new Vector();
    _transitions.addElement(x);
  }
  public void removeTransitions(Transition x) {
    _transitions.removeElement(x);
  }

  public Vector getSubmachineState() { return _submachineState; }
  public void setSubmachineState(Vector x) {
    _submachineState = x;
  }
  public void addSubmachineState(SubmachineState x) {
    if (_submachineState == null) _submachineState = new Vector();
    _submachineState.addElement(x);
  }
  public void removeSubmachineState(SubmachineState x) {
    _submachineState.removeElement(x);
  }
  
}
