// Source file: f:/jr/projects/uml/Foundation/Core/Classifier.java

package uci.uml.Foundation.Core;

import java.util.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Data_Types.Name;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
//nmw: import uci.uml.Behavioral_Elements.Collaborations.*;


public abstract class Classifier extends GeneralizableElementImpl {
  //% public Feature _feature[];
  public Vector _behavioralFeature;
  //% public StructuralFeature _structuralFeature[];
  public Vector _structuralFeature;
  //% public Classifier _specification[];
  public Vector _specification;
  //% public Classifier _realization[];
  public Vector _realization;
  //% public AssociationEnd _associationEnd[];
  public Vector _associationEnd;
  //% public AssociationEnd _participant[];
  public Vector _participant;

  public Classifier() { }
  public Classifier(Name name) { super(name); }
  public Classifier(String nameStr) { super(new Name(nameStr)); }
  
  public Vector getBehavioralFeature() { return _behavioralFeature; }
  public void setBehavioralFeature(Vector x) {
    _behavioralFeature = x;
  }
  public void addBehavioralFeature(Feature x) {
    if (_behavioralFeature == null) _behavioralFeature = new Vector();
    _behavioralFeature.addElement(x);
  }
  public void removeBehavioralFeature(Feature x) {
    _behavioralFeature.removeElement(x);
  }

  public Vector getStructuralFeature() {
    return _structuralFeature;
  }
  public void setStructuralFeature(Vector x) {
    _structuralFeature = x;
  }
  public void addStructuralFeature(StructuralFeature x) {
    if (_structuralFeature == null) _structuralFeature = new Vector();
    _structuralFeature.addElement(x);
  }
  public void removeStructuralFeature(StructuralFeature x) {
    _structuralFeature.removeElement(x);
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

  public Vector getRealization() { return _realization; }
  public void setRealization(Vector x) {
    _realization = x;
  }
  public void addRealization(Classifier x) {
    if (_realization == null) _realization = new Vector();
    _realization.addElement(x);
  }
  public void removeRealization(Classifier x) {
    _realization.removeElement(x);
  }

  public Vector getAssociationEnd() {
    return _associationEnd;
  }
  public void setAssociationEnd(Vector x) {
    _associationEnd = x;
  }
  public void addAssociationEnd(AssociationEnd x) {
    if (_associationEnd == null) _associationEnd = new Vector();
    _associationEnd.addElement(x);
  }
  public void removeAssociationEnd(AssociationEnd x) {
    _associationEnd.removeElement(x);
  }

  public Vector getParticipant() { return _participant; }
  public void setParticipant(Vector x) {
    _participant = x;
  }
  public void addParticipant(AssociationEnd x) {
    if (_participant == null) _participant = new Vector();
    _participant.addElement(x);
  }
  public void removeParticipant(AssociationEnd x) {
    _participant.removeElement(x);
  }

  public void addFeature(Feature f) {
    if (f instanceof StructuralFeature)
      addStructuralFeature((StructuralFeature)f);
    else if (f instanceof BehavioralFeature)
      addBehavioralFeature((BehavioralFeature)f);
    else System.out.println("should never get here");
  }


  ////////////////////////////////////////////////////////////////
  // debugging

  public String dbgString() {
    String s = "";
    Vector v;
    java.util.Enumeration enum;

    s += getOCLTypeStr() + "(" + getName().toString() + ")[";

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

