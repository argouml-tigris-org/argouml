// Source file: Foundation/Core/StructuralFeature.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Data_Types.*;


public abstract class StructuralFeature extends Feature {
  public Multiplicity _multiplicity = Multiplicity.ONE;
  public ChangeableKind _changeable;
  public ScopeKind _targetScope;
  public Classifier _type;
  
  public StructuralFeature() { }
  public StructuralFeature(Name name) { super(name); }
  public StructuralFeature(String nameStr) { super(new Name(nameStr)); }
  public StructuralFeature(Name name, Classifier type) {
    super(name);
    try {
      setType(type);
    }
    catch (PropertyVetoException pve) { }
  }
  public StructuralFeature(String nameStr, Classifier type) {
    this(new Name(nameStr), type);
  }
  
  public Multiplicity getMultiplicity() { return _multiplicity; }
  public void setMultiplicity(Multiplicity x) throws PropertyVetoException {
    fireVetoableChange("multiplicity", _multiplicity, x);
    _multiplicity = x;
  }
  public ChangeableKind getChangeable() { return _changeable; }
  public void setChangeable(ChangeableKind x) throws PropertyVetoException {
    fireVetoableChange("changeable", _changeable, x);
    _changeable = x;
  }
  public ScopeKind getTargetScope() { return _targetScope; }
  public void setTargetScope(ScopeKind x) throws PropertyVetoException {
    fireVetoableChange("targetScope", _targetScope, x);
    _targetScope = x;
  }
  public Classifier getType() { return _type; }
  public void setType(Classifier x) throws PropertyVetoException {
    fireVetoableChange("type", _type, x);
    _type = x;
  }
  
}
