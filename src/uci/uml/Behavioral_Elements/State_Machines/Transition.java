// Source file: Behavioral_Elements/State_Machines/Transition.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;


public class Transition extends ModelElementImpl {
  public Guard _guard;
  public ActionSequence _effect;
  public State _state;
  public Event _trigger;
  public StateMachine _stateMachine;
  public StateVertex _source;
  public StateVertex _target;
    
  public Transition() { }

  public Guard getGuard() { return _guard; }
  public void setGuard(Guard x) {
    _guard = x;
  }
  public ActionSequence getEffect() { return _effect; }
  public void setEffect(ActionSequence x) {
    _effect = x;
  }
  public State getState() { return _state; }
  public void setState(State x) {
    _state = x;
  }
  public Event getTrigger() { return _trigger; }
  public void setTrigger(Event x) {
    _trigger = x;
  }
  public StateMachine getStateMachine() { return _stateMachine; }
  public void setStateMachine(StateMachine x) {
    _stateMachine = x;
  }
  public StateVertex getSource() { return _source; }
  public void setSource(StateVertex x) {
    _source = x;
  }
  public StateVertex getTarget() { return _target; }
  public void setTarget(StateVertex x) {
    _target = x;
  }
  
}
