// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.




// Source file: f:/jr/projects/uml/Foundation/Core/Classifier.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.PropertyVetoException;

import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Data_Types.Name;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
//nmw: import uci.uml.Behavioral_Elements.Collaborations.*;
import uci.uml.util.*;


public abstract class Classifier extends GeneralizableElementImpl {
  //% public Feature _feature[];
  public Vector _behavioralFeature = new Vector();
  //% public StructuralFeature _structuralFeature[];
  public Vector _structuralFeature = new Vector();
  //% public Classifier _specification[];
  public Vector _specification = new Vector();
  //% public Classifier _realization[];
  public Vector _realization = new Vector();
  //% public AssociationEnd _associationEnd[];
  public Vector _associationEnd = new Vector();
  //% public AssociationEnd _participant[];
  public Vector _participant = new Vector();
  
  public Vector _specialization = new Vector();

  public Classifier() { }
  public Classifier(Name name) { super(name); }
  public Classifier(String nameStr) { super(new Name(nameStr)); }

  public Vector getBehavioralFeature() {
    if (_behavioralFeature == null) return null;
    return (Vector) _behavioralFeature;
  }
  public void setBehavioralFeature(Vector x)
  throws PropertyVetoException {
    if (_behavioralFeature == null) _behavioralFeature = new Vector();
    fireVetoableChange("behavioralFeature", _behavioralFeature, x);
    _behavioralFeature = x;
    java.util.Enumeration enum = _behavioralFeature.elements();
    while (enum.hasMoreElements()) {
      BehavioralFeature bf = (BehavioralFeature) enum.nextElement();
      bf.setOwner(this);
      bf.setNamespace(getNamespace());
    }
  }
  public void addBehavioralFeature(Feature x)
  throws PropertyVetoException {
    if (_behavioralFeature == null) _behavioralFeature = new Vector();
    fireVetoableChange("behavioralFeature", _behavioralFeature, x);
    _behavioralFeature.addElement(x);
    x.setOwner(this);
    x.setNamespace(getNamespace());
  }
  public void removeBehavioralFeature(Feature x)
  throws PropertyVetoException {
    if (_behavioralFeature == null) return;
    fireVetoableChange("behavioralFeature", _behavioralFeature, x);
    _behavioralFeature.removeElement(x);
  }

  public Vector getStructuralFeature() {
    return _structuralFeature;
  }
  public void setStructuralFeature(Vector x)
  throws PropertyVetoException {
    if (_structuralFeature == null) _structuralFeature = new Vector();
    fireVetoableChangeNoCompare("structuralFeature", _structuralFeature, x);
    _structuralFeature = x;
    java.util.Enumeration enum = _structuralFeature.elements();
    while (enum.hasMoreElements()) {
      StructuralFeature sf = (StructuralFeature) enum.nextElement();
      sf.setOwner(this);
      sf.setNamespace(getNamespace());
    }
  }
  public void addStructuralFeature(StructuralFeature x)
  throws PropertyVetoException {
    if (_structuralFeature == null) _structuralFeature = new Vector();
    fireVetoableChange("structuralFeature", _structuralFeature, x);
    _structuralFeature.addElement(x);
    x.setOwner(this);
    x.setNamespace(getNamespace());
  }
  public void removeStructuralFeature(StructuralFeature x)
  throws PropertyVetoException {
    if (_structuralFeature == null) return;
    fireVetoableChange("structuralFeature", _structuralFeature, x);
    _structuralFeature.removeElement(x);
  }

  public Vector getSpecification() { return (Vector) _specification;}
  public void setSpecification(Vector x)
  throws PropertyVetoException {
    if (_specification == null) _specification = new Vector();
    fireVetoableChangeNoCompare("specification", _specification, x);
    _specification = x;
  }
  public void addSpecification(Classifier x)
  throws PropertyVetoException {
    if (_specification == null) _specification = new Vector();
    fireVetoableChange("specification", _specification, x);
    _specification.addElement(x);
  }
  public void removeSpecification(Classifier x)
  throws PropertyVetoException {
    if (_specification == null) return;
    fireVetoableChange("specification", _specification, x);
    _specification.removeElement(x);
  }

  public Vector getRealization() { return (Vector) _realization;}
  public void setRealization(Vector x) throws PropertyVetoException {
    if (_realization == null) _realization = new Vector();
    fireVetoableChangeNoCompare("realization", _realization, x);
    _realization = x;
    java.util.Enumeration enum = _realization.elements();
    while (enum.hasMoreElements()) {
      Realization r = (Realization) enum.nextElement();
      r.setNamespace(getNamespace());
    }
  }
  public void addRealization(Realization x)
  throws PropertyVetoException {
    if (_realization == null) _realization = new Vector();
    if (_realization.contains(x)) return;
    fireVetoableChange("realization", _realization, x);
    _realization.addElement(x);
    x.setNamespace(getNamespace());
  }
  public void removeRealization(Realization x)
  throws PropertyVetoException {
    if (_realization == null || !_realization.contains(x)) return;
    fireVetoableChange("realization", _realization, x);
    _realization.removeElement(x);
  }
  
  public Vector getSpecialization() {
    return (Vector) _specialization;
  }
  public void setSpecialization(Vector x) throws PropertyVetoException {
    if (_specialization == null) _specialization = new Vector();
    fireVetoableChangeNoCompare("specialization", _specialization, x);
    _specialization = x;
    java.util.Enumeration enum = _specialization.elements();
    while (enum.hasMoreElements()) {
      Realization r = (Realization) enum.nextElement();
      r.setNamespace(getNamespace());
    }
  }
  public void addSpecialization(Realization x) throws PropertyVetoException {
    if (_specialization == null) _specialization = new Vector();
    if (_specialization.contains(x)) return;
    fireVetoableChange("specialization", _specialization, x);
     _specialization.addElement(x);
    x.setNamespace(getNamespace());
  }
  public void removeSpecialization(Realization x)
       throws PropertyVetoException
  {
    if (_specialization == null || !_specialization.contains(x)) return;
    fireVetoableChange("specialization", _specialization, x);
    _specialization.removeElement(x);
  }
  
  public Vector getAssociationEnd() {
    return _associationEnd;
  }
  public void setAssociationEnd(Vector x)
  throws PropertyVetoException {
    if (_associationEnd == null) _associationEnd = new Vector();
    fireVetoableChangeNoCompare("associationEnd", _associationEnd, x);
    _associationEnd = x;
    java.util.Enumeration enum = _associationEnd.elements();
    while (enum.hasMoreElements()) {
      AssociationEnd ae = (AssociationEnd) enum.nextElement();
      ae.setNamespace(getNamespace());
    }
  }
  public void addAssociationEnd(AssociationEnd x)
  throws PropertyVetoException {
    if (_associationEnd == null) _associationEnd = new Vector();
    fireVetoableChange("associationEnd", _associationEnd, x);
    _associationEnd.addElement(x);
    x.setNamespace(getNamespace());
  }
  public void removeAssociationEnd(AssociationEnd x)
  throws PropertyVetoException {
    if (_associationEnd == null) return;
    fireVetoableChange("associationEnd", _associationEnd, x);
    _associationEnd.removeElement(x);
  }

  // needs-more-work: what are paricipants?
  public Vector getParticipant() { return (Vector) _participant;}
  public void setParticipant(Vector x)
  throws PropertyVetoException {
    if (_participant == null) _participant = new Vector();
    fireVetoableChangeNoCompare("participant", _participant, x);
    _participant = x;
    java.util.Enumeration enum = _associationEnd.elements();
    while (enum.hasMoreElements()) {
      AssociationEnd ae = (AssociationEnd) enum.nextElement();
      ae.setNamespace(getNamespace());
    }
  }
  public void addParticipant(AssociationEnd x)
  throws PropertyVetoException {
    if (_participant == null) _participant = new Vector();
    fireVetoableChange("participant", _participant, x);
    _participant.addElement(x);
    x.setNamespace(getNamespace());
  }
  public void removeParticipant(AssociationEnd x)
  throws PropertyVetoException {
    if (_participant == null) return;
    fireVetoableChange("participant", _participant, x);
    _participant.removeElement(x);
  }

  public void addFeature(Feature f) throws PropertyVetoException {
    if (f instanceof StructuralFeature)
      addStructuralFeature((StructuralFeature)f);
    else if (f instanceof BehavioralFeature)
      addBehavioralFeature((BehavioralFeature)f);
    else System.out.println("should never get here");
  }

  ////////////////////////////////////////////////////////////////
  // transitive accessors

  public Vector getInheritedBehavioralFeatures() {
    Vector res = (Vector) getBehavioralFeature().clone();
    Vector supers = getGeneralization();
    if (supers == null || supers.size() == 0) return res;

    java.util.Enumeration superEnum = GenAncestorClasses.TheInstance.gen(this);
    while (superEnum.hasMoreElements()) {
      Classifier c = (Classifier) superEnum.nextElement();
      Vector beh = c.getBehavioralFeature();
      int size = beh.size();
      for (int i = 0; i < size; i++) {
	res.addElement(beh.elementAt(i));
      }
    }
    return res;
  }

  public Vector getInheritedStructuralFeatures() {
    Vector res = (Vector) getStructuralFeature().clone();
    Vector supers = getGeneralization();
    if (supers == null || supers.size() == 0) return res;

    java.util.Enumeration superEnum = GenAncestorClasses.TheInstance.gen(this);
    while (superEnum.hasMoreElements()) {
      Classifier c = (Classifier) superEnum.nextElement();
      Vector str = c.getStructuralFeature();
      java.util.Enumeration enum = str.elements();
      while (enum.hasMoreElements()) {
	res.addElement(enum.nextElement());
      }
    }
    return res;
  }

  public Vector getInheritedAssociationEnds() {
    Vector res = (Vector) getAssociationEnd().clone();
    Vector supers = getGeneralization();
    if (supers == null || supers.size() == 0) return res;

    java.util.Enumeration superEnum = GenAncestorClasses.TheInstance.gen(this);
    while (superEnum.hasMoreElements()) {
      Classifier c = (Classifier) superEnum.nextElement();
      Vector ends = c.getAssociationEnd();
      java.util.Enumeration enum = ends.elements();
      while (enum.hasMoreElements()) {
	res.addElement(enum.nextElement());
      }
    }
    return res;
  }

  public Vector getInheritedRealizations() {
    Vector res = (Vector) getRealization().clone();
    Vector supers = getGeneralization();
    if (supers == null || supers.size() == 0) return res;

    java.util.Enumeration superEnum = GenAncestorClasses.TheInstance.gen(this);
    while (superEnum.hasMoreElements()) {
      Classifier c = (Classifier) superEnum.nextElement();
      Vector reals = c.getRealization();
      java.util.Enumeration enum = reals.elements();
      while (enum.hasMoreElements()) {
	res.addElement(enum.nextElement());
      }
    }
    return res;
  }


  ////////////////////////////////////////////////////////////////
  // debugging

  public String toString() { return getName().getBody(); }
  
  public String dbgString() {
    String s = "";
    Vector v;
    java.util.Enumeration enum;

    s += getOCLTypeStr() + "(" + getName().getBody().toString() + ")[";

    String stereos = dbgStereotypes();
    if (stereos != "") s += "\n" + stereos;

    String tags = dbgTaggedValues();
    if (tags != "") s += "\n" + tags;

    if ((v = getStructuralFeature()) != null) {
      s += "\n  attributes:";
      enum = v.elements();
      while (enum.hasMoreElements())
	s += "\n  | " + ((Attribute)enum.nextElement()).dbgString();
    }
    if ((v = getBehavioralFeature()) != null) {
      s += "\n  operations:";
      enum = v.elements();
      while (enum.hasMoreElements())
	s += "\n  | " + ((Operation) enum.nextElement()).dbgString();
    }
    s += "\n]";
    return s;
  }

  
} /* end class Classifier */

