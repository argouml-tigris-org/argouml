// Source file: Foundation/Core/Parameter.java

package uci.uml.Foundation.Core;

import java.util.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;

public class Parameter extends ModelElementImpl {
  public static final Name RETURN_NAME = new Name("return");
  
  public Expression _defaultValue;
  public ParameterDirectionKind _kind;
  public BehavioralFeature _behavioralFeature;
  public Classifier _type;
  public Event _parameters;
  //% public ObjectFlowState _availability[];
  public Vector _availability;
  public Signal _signal;
  
  public Parameter() { }
  public Parameter(Name name) { super(name); }
  public Parameter(String nameStr) { super(new Name(nameStr)); }
  public Parameter(Classifier type, Name name) {
    this(name);
    setType(type);
  }
  public Parameter(Classifier type, String nameStr) {
    this(nameStr);
    setType(type);
  }

  public Expression getDefaultValue() { return _defaultValue; }
  public void setDefaultValue(Expression x) {
    _defaultValue = x;
  }
  public ParameterDirectionKind getKind() { return _kind; }
  public void setKind(ParameterDirectionKind x) {
    _kind = x;
  }
  public BehavioralFeature getBehavioralFeature() {
    return _behavioralFeature;
  }
  public void setBehavioralFeature(BehavioralFeature x) {
    _behavioralFeature = x;
  }
  public Classifier getType() { return _type; }
  public void setType(Classifier x) {
    _type = x;
  }
  public Event getParameters() { return _parameters; }
  public void setParameters(Event x) {
    _parameters = x;
  }
  
  public Vector getAvailability() {
    return _availability;
  }
  public void setAvailability(Vector x) {
    _availability = x;
  }
  public void addAvailability(ObjectFlowState x) {
    if (_availability == null) _availability = new Vector();
    _availability.addElement(x);
  }
  public void removeAvailability(ObjectFlowState x) {
    _availability.removeElement(x);
  }
  
  public Signal getSignal() { return _signal; }
  public void setM_Signal(Signal x) {
    _signal = x;
  }

  
}
