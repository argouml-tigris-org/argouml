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





package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.PropertyVetoException;

import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Collaborations.*;
import uci.uml.Model_Management.*;

/** An implementation of ModelElement.  */

public class ModelElementImpl extends ElementImpl implements ModelElement {
  public static final int MAX_STEREOTYPE = 10;

  //- public Namespace _namespace;
  public ElementOwnership _elementOwnership;
  //% public Constraint _constraint[];
  public Vector _constraint = new Vector();
  //% public Dependency _provision[];
  public Vector _provision = new Vector();
  //% public TaggedValue _taggedValue[];
  public Vector _taggedValue = new Vector();
  //% public Dependency _requirement[];
  public Vector _requirement = new Vector();
  //    public ViewElement _view[];
  //    public Binding _binding;
  //% public ModelElement _templateParameter[];
  public Vector _templateParameter = new Vector();
  public ModelElement _template;
  //    public Component _implementation[];
  //% public Stereotype _stereotype[] = new Stereotype[MAX_STEREOTYPE];
  public Vector _stereotype = new Vector();
  //% public StateMachine _behavior[];
  public Vector _behavior = new Vector();
  //    public Partition _partition;
  //% public Collaboration _collaboration[];
  public Vector _collaboration = new Vector();
  //% public Package _package[];
  //- public Vector _package;
  public Vector _elementReference = new Vector();

  public ModelElementImpl() { }
  public ModelElementImpl(Name name) { super(name); }
  public ModelElementImpl(String nameStr) { super(new Name(nameStr)); }

  // needs-more-work: is there a different name space? Or is it only
  // transitive via ElementOwnership? check spec.

  public Namespace getNamespace() {
    if (_elementOwnership == null) return null;
    return _elementOwnership.getNamespace();
  }
  public void setNamespace(Namespace x) {
    if (_elementOwnership != null &&
	_elementOwnership.getNamespace() == x) return;
    VisibilityKind vk = (_elementOwnership == null) ?
      VisibilityKind.PUBLIC : _elementOwnership.getVisibility();
    try { setElementOwnership(new ElementOwnership(x, vk, this)); }
    catch (PropertyVetoException pve) { }
  }

  // there was a bug with null ElementOwnership in enroll.argo
  public ElementOwnership getElementOwnership() { return _elementOwnership; }
  public void setElementOwnership(ElementOwnership x)
  throws PropertyVetoException {
    fireVetoableChange("elementOwnership", _elementOwnership, x);
    ElementOwnership old = _elementOwnership;
    _elementOwnership = x;

    if (old != null && old.getNamespace() != null)
      old.getNamespace().removeOwnedElement(old);
    if (x != null && x.getNamespace() != null)
      x.getNamespace().addOwnedElement(x);
  }

  public VisibilityKind getVisibility() {
    //if (getElementOwnership() == null) return VisibilityKind.UNSPEC;
    if (getElementOwnership() == null) return VisibilityKind.PUBLIC;
    return getElementOwnership().getVisibility();
  }
  public void setVisibility(VisibilityKind x) throws PropertyVetoException {
    fireVetoableChange("elementOwnership", _elementOwnership, x);
    if (getElementOwnership() == null) return;
    getElementOwnership().setVisibility(x);
  }


  public Vector getConstraint() { return (Vector) _constraint;}
  public void setConstraint(Vector x)
  throws PropertyVetoException {
    if (_constraint == null) _constraint = new Vector();
    fireVetoableChangeNoCompare("constraint", _constraint, x);
    java.util.Enumeration enum = _constraint.elements();
    while (enum.hasMoreElements()) {
      Constraint c = (Constraint) enum.nextElement();
      Namespace ns = getNamespace();
      ModelElementImpl thisObject = this;
      if (thisObject instanceof Namespace) ns = (Namespace) thisObject;
      c.setNamespace(ns);
    }
    _constraint = x;
  }
  public void addConstraint(Constraint x)
  throws PropertyVetoException {
    if (_constraint == null) _constraint = new Vector();
    fireVetoableChange("constraint", _constraint, x);
    _constraint.addElement(x);
    Namespace ns = getNamespace();
    ModelElementImpl thisObject = this;
    if (thisObject instanceof Namespace) ns = (Namespace) thisObject;
    x.setNamespace(ns);
  }
  public void removeConstraint(Constraint x)
  throws PropertyVetoException {
    if (_constraint == null) return;
    fireVetoableChange("constraint", _constraint, x);
    _constraint.removeElement(x);
  }

  public Vector getProvision() { return (Vector) _provision;}
  public void setProvision(Vector x)
  throws PropertyVetoException {
    if (_provision == null) _provision = new Vector();
    fireVetoableChangeNoCompare("provision", _provision, x);
    _provision = x;
  }
  public void addProvision(Dependency x)
  throws PropertyVetoException {
    if (_provision == null) _provision = new Vector();
    if (_provision.contains(x)) return;
    fireVetoableChange("provision", _provision, x);
    _provision.addElement(x);
    x.addSupplier(this);
  }
  public void removeProvision(Dependency x)
  throws PropertyVetoException {
    if (_provision == null) return;
    if (!_provision.contains(x)) return;
    fireVetoableChange("provision", _provision, x);
    _provision.removeElement(x);
    x.removeSupplier(this);
  }

  public Vector getRequirement() { return (Vector) _requirement;}
  public void setRequirement(Vector x)
  throws PropertyVetoException {
    if (_requirement == null) _requirement = new Vector();
    fireVetoableChangeNoCompare("requirement", _requirement, x);
    _requirement = x;
  }
  public void addRequirement(Dependency x)
  throws PropertyVetoException {
    if (_requirement == null) _requirement = new Vector();
    if (_requirement.contains(x)) return;
    fireVetoableChange("requirement", _requirement, x);
    _requirement.addElement(x);
    x.addClient(this);
  }
  public void removeRequirement(Dependency x)
  throws PropertyVetoException {
    if (_requirement == null) return;
    if (!_requirement.contains(x)) return;
    fireVetoableChange("requirement", _requirement, x);
    _requirement.removeElement(x);
    x.removeClient(this);
  }

  public Vector getTemplateParameter() {
    return (Vector) _templateParameter;
  }
  public void setTemplateParameter(Vector x)
  throws PropertyVetoException {
    if (_templateParameter == null) _templateParameter = new Vector();
    fireVetoableChangeNoCompare("templateParameter", _templateParameter, x);
    _templateParameter = x;
  }
  public void addTemplateParameter(ModelElement x)
  throws PropertyVetoException {
    if (_templateParameter == null) _templateParameter = new Vector();
    fireVetoableChange("templateParameter", _templateParameter, x);
    _templateParameter.addElement(x);
  }
  public void removeTemplateParameter(ModelElement x)
  throws PropertyVetoException {
    if (_templateParameter == null) return;
    fireVetoableChange("templateParameter", _templateParameter, x);
    _templateParameter.removeElement(x);
  }

  public ModelElement getTemplate() { return _template; }
  public void setTemplate(ModelElement x)
  throws PropertyVetoException {
    fireVetoableChange("template", _template, x);
    _template = x;
  }

  //-   public Vector getPackage() { return _package; }
  //-   public void setPackage(Vector x) {
  //-     _package = x;
  //-   }
  //-   public void addPackage(Package x) {
  //-     if (_package == null) _package = new Vector();
  //-     _package.addElement(x);
  //-   }
  //-   public void removePackage(Package x) {
  //-     _package.removeElement(x);
  //-   }
  public Vector getElementReference() { return (Vector) _elementReference;}
  public void setElementReference(Vector x)
  throws PropertyVetoException {
    if (_elementReference == null) _elementReference = new Vector();
    fireVetoableChangeNoCompare("elementReference", _elementReference, x);
    _elementReference = x;
  }
  public void addElementReference(ElementReference x)
  throws PropertyVetoException {
    if (_elementReference == null) _elementReference = new Vector();
    fireVetoableChange("elementReference", _elementReference, x);
    _elementReference.addElement(x);
  }
  public void removeElementReference(ElementReference x)
  throws PropertyVetoException {
    if (_elementReference == null) return;
    fireVetoableChange("elementReference", _elementReference, x);
    _elementReference.removeElement(x);
  }

  public Vector getStereotype() { return (Vector) _stereotype;}
  public void setStereotype(Vector x) throws PropertyVetoException {
    if (_stereotype == null) _stereotype = new Vector();
    fireVetoableChangeNoCompare("stereotype", _stereotype, x);
    _stereotype = x;
  }
  public void addStereotype(Stereotype x) throws PropertyVetoException {
    if (_stereotype == null) _stereotype = new Vector();
    else if (_stereotype.contains(x)) return;
    fireVetoableChange("stereotype", _stereotype, x);
    _stereotype.addElement(x);
  }
  public void removeStereotype(Stereotype x) throws PropertyVetoException {
    if (_stereotype == null) return;
    else if (!_stereotype.contains(x)) return;
    fireVetoableChange("stereotype", _stereotype, x);
    _stereotype.removeElement(x);
  }
  public boolean containsStereotype(Stereotype x) {
    if (_stereotype == null) return false;
    return _stereotype.contains(x);
  }

  public Vector getBehavior() { return (Vector) _behavior;}
  public void setBehavior(Vector x)
  throws PropertyVetoException {
    if (_behavior == null) _behavior = new Vector();
    fireVetoableChangeNoCompare("behavior", _behavior, x);
    _behavior = x;
    java.util.Enumeration enum = _behavior.elements();
    while (enum.hasMoreElements()) {
      StateMachine sm = (StateMachine) enum.nextElement();
      Namespace ns = getNamespace();
      ModelElementImpl thisObject = this;
      if (thisObject instanceof Namespace) ns = (Namespace) thisObject;
      sm.setNamespace(ns);
    }
  }
  public void addBehavior(StateMachine x) throws PropertyVetoException {
    if (_behavior == null) _behavior = new Vector();
    else if (_behavior.contains(x)) return;
    fireVetoableChange("behavior", _behavior, x);
    _behavior.addElement(x);
    Namespace ns = getNamespace();
    ModelElementImpl thisObject = this;
    if (thisObject instanceof Namespace) ns = (Namespace) thisObject;
    x.setNamespace(ns);
    x.setContext(this);
  }
  public void removeBehavior(StateMachine x)
  throws PropertyVetoException {
    if (_behavior == null) return;
    else if (!_behavior.contains(x)) return;
    fireVetoableChange("behavior", _behavior, x);
    _behavior.removeElement(x);
  }

  public Object prepareForTrash() throws PropertyVetoException {
    //setNamespace(null);
    return null;
    //needs-more-work: remember old namespace
  }

  public void recoverFromTrash(Object momento) throws PropertyVetoException {
    // needs-more-work: restore old namespace
  }

  public Vector alsoTrash() {
    Vector res = new Vector();
    for (int i = 0; i < _provision.size(); i++)
      res.addElement(_provision.elementAt(i));
    for (int i = 0; i < _requirement.size(); i++)
      res.addElement(_requirement.elementAt(i));
    for (int i = 0; i < _behavior.size(); i++)
      res.addElement(_behavior.elementAt(i));
    return res;
  }

  public boolean isLegalXMI() { return true; }
  public boolean isNotLegalXMI() { return !isLegalXMI(); }

  ////////////////////////////////////////////////////////////////
  // debugging

  public String dbgStereotypes() {
    String s = "";
    java.util.Enumeration enum;
    Vector v;
    if ((v = getStereotype()) != null) {
      enum = v.elements();
      while (enum.hasMoreElements())
	s += ((Stereotype)enum.nextElement()).dbgString();
    }
    return s;
  }

  public String dbgTaggedValues() {
    String s = "";
    java.util.Enumeration enum;
    Vector v;
    boolean first = true;
    if ((v = getTaggedValue()) != null) {
      enum = v.elements();
      while (enum.hasMoreElements()) {
	if (!first) s += ", ";
	s += ((TaggedValue)enum.nextElement()).dbgString();
	first = false;
      }
    }
    return s;
  }

  static final long serialVersionUID = -7335992986976076953L;
}
