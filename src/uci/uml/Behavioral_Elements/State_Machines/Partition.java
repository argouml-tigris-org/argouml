// Source file: f:/jr/projects/uml/Behavioral_Elements/State_Machines/Partition.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

import uci.uml.Foundation.Core.ModelElement;


public class Partition {
  public ActivityModel _activityModel;
  //% public ModelElement _contents[];
  public Vector _contents;
  
  public Partition() { }

  public ActivityModel getActivityModel() { return _activityModel; }
  public void setActivityModel(ActivityModel x) {
    _activityModel = x;
  }

  public Vector getContents() { return _contents; }
  public void setContents(Vector x) {
    _contents = x;
  }
  public void addContents(ModelElement x) {
    if (_contents == null) _contents = new Vector();
    _contents.addElement(x);
  }
  public void removeContents(ModelElement x) {
    _contents.removeElement(x);
  }
  
}
