// Source file: Foundation/Core/AssociationEnd.java

package uci.uml.Foundation.Core;

//nmw: import uci.uml.Behavioral_Elements.Collaborations.AssociationEndRole;
import java.util.*;
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
    setType(c);
    setMultiplicity(m);
    setAggregation(a);
  }
      
  public boolean getIsNavigable() { return _isNavigable; }
  public void setIsNavigable(boolean x) {
    _isNavigable = x;
  }

  public boolean getIsOrdered() { return _isOrdered; }
  public void setIsOrdered(boolean x) {
    _isOrdered = x;
  }

  public AggregationKind getAggregation() { return _aggregation; }
  public void setAggregation(AggregationKind x) {
    _aggregation = x;
  }

  public ScopeKind getTargetScope() { return _targetScope; }
  public void setTargetScope(ScopeKind x) {
    _targetScope = x;
  }

  public Multiplicity getMultiplicity() { return _multiplicity; }
  public void setMultiplicity(Multiplicity x) {
    _multiplicity = x;
  }

  public ChangeableKind getChangeable() { return _changeable; }
  public void setChangeable(ChangeableKind x) {
    _changeable = x;
  }

  public IAssociation getAssociation() { return _association; }
  public void setAssociation(IAssociation x) {
    _association = x;
  }

  public Vector getQualifier() { return _qualifier; }
  public void setQualifier(Vector x) {
    _qualifier = x;
  }
  public void addQualifier(Attribute x) {
    if (_qualifier == null) _qualifier = new Vector();
    _qualifier.addElement(x);
  }
  public void removeQualifier(Attribute x) {
    _qualifier.removeElement(x);
  }

  public Classifier getType() { return _type; }
  public void setType(Classifier x) {
    _type = x;
  }

  public Vector getSpecification() { return _specification; }
  public void setSpecification(Vector x) {
    _specification = x;
  }
  public void addSpecification(Classifier x) {
    if (_specification == null) _specification = new Vector();
    _specification.addElement(x);
  }
  public void removeSpecification(Classifier x) {
    _specification.removeElement(x);
  }
  
  //- public AssociationEndRole[] getAssociationEndRole() {
  //-   return associationEndRole;
  //- }
  //- public void setAssociationEndRole(Vector x) {
  //-   associationEndRole = x;
  //- }
  
  public Vector getLinkEnd() { return _linkEnd; }
  public void setLinkEnd(Vector x) {
    _linkEnd = x;
  }
  public void addLinkEnd(LinkEnd x) {
    if (_linkEnd == null) _linkEnd = new Vector();
    _linkEnd.addElement(x);
  }
  public void removeLinkEnd(LinkEnd x) {
    _linkEnd.removeElement(x);
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
