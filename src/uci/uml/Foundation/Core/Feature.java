// Source file: Foundation/Core/Feature.java

package uci.uml.Foundation.Core;

import java.util.*;
//nmw: import uci.uml.Behavioral_Elements.Collaborations.ClassifierRole;

import java.beans.PropertyVetoException;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;

public abstract class Feature extends ModelElementImpl {
  public ScopeKind _ownerScope = ScopeKind.UNSPEC;
  public VisibilityKind _visibility = VisibilityKind.UNSPEC;
  public Classifier _owner;
  //nmw: public ClassifierRole _classifierRole[];
    
  public Feature() { }
  public Feature(Name name) { super(name); }
  public Feature(String nameStr) { super(new Name(nameStr)); }

  public ScopeKind getOwnerScope() { return _ownerScope; }
  public void setOwnerScope(ScopeKind x) throws PropertyVetoException {
    fireVetoableChange("ownerScope", _ownerScope, x);
    _ownerScope = x;
  }
  public VisibilityKind getVisibility() { return _visibility; }
  public void setVisibility(VisibilityKind x) throws PropertyVetoException {
    fireVetoableChange("visibility", _visibility, x);
    _visibility = x;
  }
  public Classifier getOwner() { return _owner; }
  public void setOwner(Classifier x) throws PropertyVetoException {
    fireVetoableChange("owner", _visibility, x);
    _owner = x;
  }


  //public ClassifierRole[] getClassifierRole() { return _classifierRole;
  //}
  //public void setClassifierRole(ClassifierRole[] x) {
  //  classifierRole = x;
  //}

  ////////////////////////////////////////////////////////////////
  // events
  public void fireVetoableChange(String propertyName, 
				 Object oldValue, Object newValue) 
       throws PropertyVetoException {
    super.fireVetoableChange(propertyName, oldValue, newValue);
    if (_owner != null) {
       _owner.fireVetoableChange("feature_"+propertyName,
				 oldValue, newValue);
    }
  }


  
  ////////////////////////////////////////////////////////////////
  // debugging
  
  public String toString() { return getName().getBody(); }

}
