// Source file: f:/jr/projects/uml/Behavioral_Elements/Common_Behavior/SendAction.java

package uci.uml.Behavioral_Elements.Common_Behavior;

import java.util.*;

public class SendAction extends Action {
  public Signal _raises;
  public Signal _raised;
  
  public SendAction() { }
  
  public Signal getRaises() { return _raises; }
  public void setRaises(Signal x) {
    _raises = x;
  }
  public Signal getRaised() { return _raised; }
  public void setRaised(Signal x) {
    _raised = x;
  }
  
}
