// Source file: Foundation/Core/AssociationEnd.java

package uci.uml.Foundation.Core;

//nmw: import uci.uml.Behavioral_Elements.Collaborations.AssociationEndRole;
import java.util.*;
import java.beans.*;

import uci.uml.Behavioral_Elements.Common_Behavior.LinkEnd;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.generate.*;


public class AssociationEnd extends ModelElementImpl {
  public boolean _isNavigable;
  public boolean _isOrdered;
  public AggregationKind _aggregation;
  public ScopeKind _targetScope;
  public Multiplicity _multiplicity;
  public ChangeableKind _changeable;
  public IAssociation _association;
  //% public Attribute _qualifier[];
  public Vector _qualifier;
  public Classifier _type;
  //% public Classifier _specification[];
  public Vector _specification;
  //nmw: public AssociationEndRole _associationEndRole[];
  //% public LinkEnd _linkEnd[];
  public Vector _linkEnd;
    
  public AssociationEnd() {}
  public AssociationEnd(Name name) { super(name); }
  public AssociationEnd(String nameStr) { super(new Name(nameStr)); }
  public AssociationEnd(Name name, Classifier c, Multiplicity m,
			AggregationKind a) {
    super(name);
    try {
    setType(c);
    setMultiplicity(m);
    setAggregation(a);
    }
    catch (PropertyVetoException pce) { }
  }

  public AssociationEnd(Classifier c) {
    super();
    try {
    setType(c);
    }
    catch (PropertyVetoException pce) { }
  }

  public boolean getIsNavigable() { return _isNavigable; }
  public void setIsNavigable(boolean x) throws PropertyVetoException {
    fireVetoableChange("navigable", _isNavigable, x);    
    _isNavigable = x;
  }

  public boolean getIsOrdered() { return _isOrdered; }
  public void setIsOrdered(boolean x) throws PropertyVetoException {
    fireVetoableChange("ordered", _isOrdered, x);    
    _isOrdered = x;
  }

  public AggregationKind getAggregation() { return _aggregation; }
  public void setAggregation(AggregationKind x) throws PropertyVetoException {
    fireVetoableChange("aggregation", _aggregation, x);    
    _aggregation = x;
  }

  public ScopeKind getTargetScope() { return _targetScope; }
  public void setTargetScope(ScopeKind x) throws PropertyVetoException {
    fireVetoableChange("targetScope", _targetScope, x);    
    _targetScope = x;
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

  public IAssociation getAssociation() { return _association; }
  public void setAssociation(IAssociation x) throws PropertyVetoException {
    fireVetoableChange("association", _association, x);    
    _association = x;
  }

  public Vector getQualifier() { return _qualifier; }
  public void setQualifier(Vector x) throws PropertyVetoException {
    fireVetoableChange("qualifier", _qualifier, x);    
    _qualifier = x;
  }
  public void addQualifier(Attribute x) throws PropertyVetoException {
    fireVetoableChange("qualifier", _qualifier, x);    
    if (_qualifier == null) _qualifier = new Vector();
    _qualifier.addElement(x);
  }
  public void removeQualifier(Attribute x) throws PropertyVetoException {
    fireVetoableChange("qualifier", _qualifier, x);    
    _qualifier.removeElement(x);
  }

  public Classifier getType() { return _type; }
  public void setType(Classifier x) throws PropertyVetoException {
    fireVetoableChange("type", _type, x);    
    _type = x;
  }

  public Vector getSpecification() { return _specification; }
  public void setSpecification(Vector x) throws PropertyVetoException {
    fireVetoableChange("specification", _specification, x);    
    _specification = x;
  }
  public void addSpecification(Classifier x) throws PropertyVetoException {
    fireVetoableChange("specification", _specification, x);    
    if (_specification == null) _specification = new Vector();
    _specification.addElement(x);
  }
  public void removeSpecification(Classifier x) throws PropertyVetoException {
    fireVetoableChange("specification", _specification, x);    
    _specification.removeElement(x);
  }
  
  //- public AssociationEndRole[] getAssociationEndRole() {
  //-   return associationEndRole;
  //- }
  //- public void setAssociationEndRole(Vector x) {
  //-   associationEndRole = x;
  //- }
  
  public Vector getLinkEnd() { return _linkEnd; }
  public void setLinkEnd(Vector x) throws PropertyVetoException {
    fireVetoableChange("linkEnd", _linkEnd, x);    
    _linkEnd = x;
  }
  public void addLinkEnd(LinkEnd x) throws PropertyVetoException {
    fireVetoableChange("linkEnd", _linkEnd, x);    
    if (_linkEnd == null) _linkEnd = new Vector();
    _linkEnd.addElement(x);
  }
  public void removeLinkEnd(LinkEnd x) throws PropertyVetoException {
    fireVetoableChange("linkEnd", _linkEnd, x);    
    _linkEnd.removeElement(x);
  }

  ////////////////////////////////////////////////////////////////
  // events
  public void fireVetoableChange(String propertyName, 
				 Object oldValue, Object newValue) 
       throws PropertyVetoException {
    super.fireVetoableChange(propertyName, oldValue, newValue);
    if (_association instanceof ElementImpl) {
       ((ElementImpl)_association).fireVetoableChange("end_"+propertyName,
				 oldValue, newValue);
    }
  }


  
  ////////////////////////////////////////////////////////////////
  // debugging

  public String dbgString() {
    String s = getOCLTypeStr() + "{" +
      GeneratorDisplay.Generate(this) +
      "}";
    return s;
  }
  
}
