// Source file: f:/jr/projects/uml/Behavioral_Elements/State_Machines/TimeEvent.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

import uci.uml.Foundation.Data_Types.TimeExpression;

public class TimeEvent extends Event {
  public TimeExpression _duration;
    
  public TimeEvent() { }
  
  public TimeExpression getDuration() { return _duration; }
  public void setDuration(TimeExpression x) {
    _duration = x;
  }
  
}
