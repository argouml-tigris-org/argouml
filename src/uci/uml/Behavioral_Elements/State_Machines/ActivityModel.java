// Source file: f:/jr/projects/uml/Behavioral_Elements/State_Machines/ActivityModel.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

public class ActivityModel extends StateMachine {
  //% public Partition _partition[];
  public Vector _partition;
    
  public ActivityModel() { }

  public Vector getPartition() { return _partition; }
  public void setPartition(Vector x) {
    _partition = x;
  }
  public void addPartition(Partition x) {
    if (_partition == null) _partition = new Vector();
    _partition.addElement(x);
  }
  public void removePartition(Partition x) {
    _partition.removeElement(x);
  }
  
}
