// Source file: f:/jr/projects/uml/Behavioral_Elements/State_Machines/CompositeState.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

public class CompositeState extends State {
  public Boolean _isConcurent;
  //% public StateVertex _substate[];
  public Vector _substate;
    
  public CompositeState() { }
  
  public Boolean getIsConcurent() { return _isConcurent; }
  public void setIsConcurent(Boolean x) {
    _isConcurent = x;
  }

  public Vector getSubstate() { return _substate; }
  public void setSubstate(Vector x) {
    _substate = x;
  }
  public void addSubstate(StateVertex x) {
    if (_substate == null) _substate = new Vector();
    _substate.addElement(x);
  }
  public void removeSubstate(StateVertex x) {
    _substate.removeElement(x);
  }
  
}
