// Source file: f:/jr/projects/uml/Behavioral_Elements/State_Machines/ClassifierInState.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

import uci.uml.Foundation.Core.Classifier;


public class ClassifierInState extends Classifier {
  public State _inState;
  //% public ObjectFlowState _objectFlowState[];
  public Vector _objectFlowState;
  
  public ClassifierInState() { }
  public State getInState() { return _inState; }
  public void setInState(State x) {
    _inState = x;
  }

  public Vector getObjectFlowState() { return _objectFlowState; }
  public void setPbjectFlowState(Vector x) {
    _objectFlowState = x;
  }
  public void addPbjectFlowState(ObjectFlowState x) {
    if (_objectFlowState == null) _objectFlowState = new Vector();
    _objectFlowState.addElement(x);
  }
  public void removePbjectFlowState(ObjectFlowState x) {
    _objectFlowState.removeElement(x);
  }
  
}
