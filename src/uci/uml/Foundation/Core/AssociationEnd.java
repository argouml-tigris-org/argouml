// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.




/** AssociationEnd's are in the Namespace of the Classifier that they
 *  attach to. */

package uci.uml.Foundation.Core;

//nmw: import uci.uml.Behavioral_Elements.Collaborations.AssociationEndRole;
import java.util.*;
import java.beans.*;

import uci.uml.Behavioral_Elements.Common_Behavior.LinkEnd;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.generate.*;


public class AssociationEnd extends ModelElementImpl {
  public boolean _isNavigable = true;
  public boolean _isOrdered;
  public AggregationKind _aggregation = AggregationKind.NONE;
  public ScopeKind _targetScope = ScopeKind.INSTANCE;
  public Multiplicity _multiplicity = Multiplicity.ONE;
  public ChangeableKind _changeable = ChangeableKind.NONE;
  public IAssociation _association;
  //% public Attribute _qualifier[];
  public Vector _qualifier = new Vector();
  public Classifier _type;
  //% public Classifier _specification[];
  public Vector _specification = new Vector();
  //nmw: public AssociationEndRole _associationEndRole[];
  //% public LinkEnd _linkEnd[];
  public Vector _linkEnd = new Vector();

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
    try { setType(c); }
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

  public Vector getQualifier() { return (Vector) _qualifier;}
  public void setQualifier(Vector x) throws PropertyVetoException {
    if (_qualifier == null) _qualifier = new Vector();
    fireVetoableChangeNoCompare("qualifier", _qualifier, x);
    _qualifier = x;
  }
  public void addQualifier(Attribute x) throws PropertyVetoException {
    if (_qualifier == null) _qualifier = new Vector();
    fireVetoableChange("qualifier", _qualifier, x);
    _qualifier.addElement(x);
  }
  public void removeQualifier(Attribute x) throws PropertyVetoException {
    if (_qualifier == null) return;
    fireVetoableChange("qualifier", _qualifier, x);
    _qualifier.removeElement(x);
  }

  public Classifier getType() { return _type; }
  public void setType(Classifier x) throws PropertyVetoException {
    if (x == _type) return;
    fireVetoableChange("type", _type, x);
    Classifier oldType = _type;
    _type = x;
    if (oldType != null) oldType.removeAssociationEnd(this);
    if (_type != null) _type.addAssociationEnd(this);
  }

  public Vector getSpecification() { return (Vector) _specification;}
  public void setSpecification(Vector x) throws PropertyVetoException {
    if (_specification == null) _specification = new Vector();
    fireVetoableChangeNoCompare("specification", _specification, x);
    _specification = x;
  }
  public void addSpecification(Classifier x) throws PropertyVetoException {
    if (_specification == null) _specification = new Vector();
    fireVetoableChange("specification", _specification, x);
    _specification.addElement(x);
  }
  public void removeSpecification(Classifier x) throws PropertyVetoException {
    if (_specification == null) return;
    fireVetoableChange("specification", _specification, x);
    _specification.removeElement(x);
  }

  //- public AssociationEndRole[] getAssociationEndRole() {
  //-   return associationEndRole;
  //- }
  //- public void setAssociationEndRole(Vector x) {
  //-   associationEndRole = x;
  //- }

  public Vector getLinkEnd() { return (Vector) _linkEnd;}
  public void setLinkEnd(Vector x) throws PropertyVetoException {
    if (_linkEnd == null) _linkEnd = new Vector();
    fireVetoableChangeNoCompare("linkEnd", _linkEnd, x);
    _linkEnd = x;
  }
  public void addLinkEnd(LinkEnd x) throws PropertyVetoException {
    if (_linkEnd == null) _linkEnd = new Vector();
    fireVetoableChange("linkEnd", _linkEnd, x);
    _linkEnd.addElement(x);
  }
  public void removeLinkEnd(LinkEnd x) throws PropertyVetoException {
    if (_linkEnd == null) return;
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

  static final long serialVersionUID = -228791821531110999L;
}
