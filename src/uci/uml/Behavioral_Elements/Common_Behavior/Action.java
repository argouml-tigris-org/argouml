// Source file: f:/jr/projects/uml/Behavioral_Elements/Common_Behavior/Action.java

package uci.uml.Behavioral_Elements.Common_Behavior;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Collaborations.*;


public class Action extends ModelElementImpl {
  public Expression _recurrence;
  public ObjectSetExpression _target;
  public Boolean _isAsynchronous;
  public String _script;
  //public Argument _actualArgument[];
  public Request _request;
  //public ActionSequence m_ActionSequence;
  public ActionSequence _actionSequence;
  //% public Message _message[];
  public Vector _message;
    
  public Action() { }
  
  public Expression getRecurrence() { return _recurrence; }
  public void setRecurrence(Expression x) {
    _recurrence = x;
  }

  public Request getRequest() { return _request; }
  public void setRequest(Request x) {
    _request = x;
  }

  public ActionSequence getActionSequence() { return _actionSequence;
  }
  public void setActionSequence(ActionSequence x) {
    _actionSequence = x;
  }

  public Vector getMessage() { return _message; }
  public void setMessage(Vector x) {
    _message = x;
  }
  public void addMessage(Message x) {
    if (_message == null) _message = new Vector();
    _message.addElement(x);
  }
  public void removeMessage(Message x) {
    _message.removeElement(x);
  }

  public ObjectSetExpression getTarget() { return _target; }
  public void setTarget(ObjectSetExpression x) {
    _target = x;
  }

  public Boolean getIsAsynchronous() { return _isAsynchronous; }
  public void setIsAsynchronous(Boolean x) {
    _isAsynchronous = x;
  }

  public String getScript() { return _script; }
  public void setScript(String x) {
    _script = x;
  }
  
}
  
