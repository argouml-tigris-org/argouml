// Source file: f:/jr/projects/uml/Behavioral_Elements/Common_Behavior/Reception.java

package uci.uml.Behavioral_Elements.Common_Behavior;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.State_Machines.*;


public class Reception extends BehavioralFeature {
  public Boolean _isPolymorphic;
  public Uninterpreted _specification;
  public Signal _signal;
  
  public Reception() { }

  public Boolean getIsPolymorphic() { return _isPolymorphic; }
  public void setIsPolymorphic(Boolean x) {
    _isPolymorphic = x;
  }

  public Uninterpreted getSpecification() { return _specification; }
  public void setSpecification(Uninterpreted x) {
    _specification = x;
  }

  public Signal getSignal() { return _signal; }
  public void setSignal(Signal x) {
    _signal = x;
  }
  
}
