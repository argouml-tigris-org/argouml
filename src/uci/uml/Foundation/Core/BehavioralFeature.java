// Source file: Foundation/Core/BehavioralFeature.java

package uci.uml.Foundation.Core;

import java.util.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.Common_Behavior.Exception;
import uci.uml.Foundation.Data_Types.*;


public abstract class BehavioralFeature extends Feature {
  public Boolean _isQuery;
  //% public Parameter parameter[];
  public Vector _parameter;
  //% public Exception raisedException[];
  public Vector _raisedException;
  
  public BehavioralFeature() { }
  public BehavioralFeature(Name name) { super(name); }
  public BehavioralFeature(String nameStr) { super(new Name(nameStr)); }
  
  public Boolean getIsQuery() { return _isQuery; }
  public void setIsQuery(Boolean x) {
    _isQuery = x;
  }
  
  public Vector getParameter() { return _parameter; }
  public void setParameter(Vector x) {
    _parameter = x;
  }
  public void addParameter(Parameter x) {
    if (_parameter == null) _parameter = new Vector();
    _parameter.addElement(x);
  }
  public void removeParameter(Parameter x) {
    _parameter.removeElement(x);
  }

  public Vector getRaisedException() { return _raisedException; }
  public void setRaisedException(Vector x) {
    _raisedException = x;
  }
  public void addRaisedException(Exception x) {
    if (_raisedException == null) _raisedException = new Vector();
    _raisedException.addElement(x);
  }
  public void removeRaisedException(Exception x) {
    _raisedException.removeElement(x);
  }
  
}
