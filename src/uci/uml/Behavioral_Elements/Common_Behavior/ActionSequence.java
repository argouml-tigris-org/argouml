// Source file: f:/jr/projects/uml/Behavioral_Elements/Common_Behavior/ActionSequence.java

package uci.uml.Behavioral_Elements.Common_Behavior;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Collaborations.*;


public class ActionSequence extends ModelElementImpl {
  //public Action _action[];
  //% public Action _action[];
  public Vector _action;
  //public State _state;
  public State _state;
  public Transition _transition;
    
  public ActionSequence() { }

  public Vector getAction() { return _action; }
  public void setAction(Vector x) {
     _action = x;
  }
  public void addAction(Action x) {
     _action.addElement(x);
  }
  public void removeAction(Action x) {
     _action.removeElement(x);
  }

  public State getState() { return _state; }
  public void setState(State x) {
     _state = x;
  }

  public Transition getTransition() { return _transition; }
  public void setTransition(Transition x) {
     _transition = x;
  }

}
