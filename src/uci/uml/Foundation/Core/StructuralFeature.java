// Source file: Foundation/Core/StructuralFeature.java

package uci.uml.Foundation.Core;

import java.util.*;
import uci.uml.Foundation.Data_Types.*;
//import uci.uml.Foundation.Data_Types.ChangeableKind;
//import uci.uml.Foundation.Data_Types.ScopeKind;

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
    setType(type);
  }
  public StructuralFeature(String nameStr, Classifier type) {
    this(new Name(nameStr), type);
  }
  
  public Multiplicity getMultiplicity() { return _multiplicity; }
  public void setMultiplicity(Multiplicity x) {
    _multiplicity = x;
  }
  public ChangeableKind getChangeable() { return _changeable; }
  public void setChangeable(ChangeableKind x) {
    _changeable = x;
  }
  public ScopeKind getTargetScope() { return _targetScope; }
  public void setTargetScope(ScopeKind x) {
    _targetScope = x;
  }
  public Classifier getType() { return _type; }
  public void setType(Classifier x) {
    _type = x;
  }
  
}
