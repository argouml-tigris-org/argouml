// Source file: Behavioral_Elements/State_Machines/SignalEvent.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

import uci.uml.Behavioral_Elements.Common_Behavior.Signal;


public class SignalEvent extends Event {
  public Signal _signal;
  
  public SignalEvent() { }
  
  public Signal getSignal() { return _signal; }
  public void setSignal(Signal x) {
    _signal = x;
  }
  
}
