// Source file: f:/jr/projects/uml/Behavioral_Elements/State_Machines/ObjectFlowState.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

import uci.uml.Foundation.Core.Parameter;


public class ObjectFlowState extends SimpleState {
  public Parameter _available;
  public ClassifierInState _typeState;
    
  public ObjectFlowState() { }
  
  public Parameter getAvailable() { return _available; }
  public void setAvailable(Parameter x) {
    _available = x;
  }
  public ClassifierInState getTypeState() { return _typeState; }
  public void setTypeState(ClassifierInState x) {
    _typeState = x;
  }
  
}
