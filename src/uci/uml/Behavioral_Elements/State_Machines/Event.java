// Source file: Behavioral_Elements/State_Machines/Event.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;



public abstract class Event extends ModelElementImpl {
  //% public State _state[];
  public Vector _state;
  //% public Parameter _parameters[];
  public Vector _parameters;
  //% public Transition _transition[];
  public Vector _transition;
  
  public Event() { }

  public Vector getState() { return _state; }
  public void setState(Vector x) {
    _state = x;
  }
  public void addState(State x) {
    if (_state == null) _state = new Vector();
    _state.addElement(x);
  }
  public void removeState(State x) {
    _state.removeElement(x);
  }
  
  public Vector getParameters() { return _parameters; }
  public void setParameters(Vector x) {
    _parameters = x;
  }
  public void addParameters(Parameter x) {
    if (_parameters == null) _parameters = new Vector();
    _parameters.addElement(x);
  }
  public void removeParameters(Parameter x) {
    _parameters.removeElement(x);
  }

  public Vector getTransition() { return _transition; }
  public void setTransition(Vector x) {
    _transition = x;
  }
  public void addTransition(Transition x) {
    if (_transition == null) _transition = new Vector();
    _transition.addElement(x);
  }
  public void removeTransition(Transition x) {
    _transition.removeElement(x);
  }


}
