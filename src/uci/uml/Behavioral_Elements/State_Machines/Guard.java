// Source file: f:/jr/projects/uml/Behavioral_Elements/State_Machines/Guard.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

public class Guard extends ModelElementImpl {
  public BooleanExpression _expression;
  public Transition _guard;
    
  public Guard() { }

  public BooleanExpression getExpression() { return _expression; }
  public void setExpression(BooleanExpression x) {
    _expression = x;
  }
  public Transition getGuard() { return _guard; }
  public void setGuard(Transition x) {
    _guard = x;
  }
  
}
