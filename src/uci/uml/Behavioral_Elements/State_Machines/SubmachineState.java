// Source file: f:/jr/projects/uml/Behavioral_Elements/State_Machines/SubmachineState.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

public class SubmachineState extends State {
  public StateMachine _submachine;
    
  public SubmachineState() { }
  
  public StateMachine getSubmachine() { return _submachine; }
  public void setSubmachine(StateMachine x) {
    _submachine = x;
  }
  
}
