// Source file: f:/jr/projects/uml/Behavioral_Elements/State_Machines/ChangeEvent.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

import uci.uml.Foundation.Data_Types.BooleanExpression;


public class ChangeEvent extends Event {
  public BooleanExpression _changeExpression;
  
  public ChangeEvent() { }

  public BooleanExpression getChangeExpression() {
    return _changeExpression;
  }
  public void setChangeExpression(BooleanExpression x) {
    _changeExpression = x;
  }
  
}
