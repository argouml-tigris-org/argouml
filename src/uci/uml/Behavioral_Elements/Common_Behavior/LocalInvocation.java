// Source file: f:/jr/projects/uml/Behavioral_Elements/Common_Behavior/LocalInvocation.java

package uci.uml.Behavioral_Elements.Common_Behavior;

import java.util.*;

import uci.uml.Foundation.Core.Operation;


public class LocalInvocation extends Action {
  public Operation _invoked;
  
  public LocalInvocation() { }

  public Operation getInvoked() { return _invoked; }
  public void setInvoked(Operation x) {
    _invoked = x;
  }
  
}
