// Source file: Behavioral_Elements/State_Machines/CallEvent.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

import uci.uml.Foundation.Core.Operation;


import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;

// needs-more-work

public class CallEvent extends Event //implements ModelElement
{
  public Operation _operation;
  
  public CallEvent() { }
  
  public Operation getOperation() { return _operation; }
  public void setOperation(Operation x) {
    _operation = x;
  }
  
}
