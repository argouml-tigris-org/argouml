// Source file: f:/jr/projects/uml/Foundation/Extension_Mechanisms/Stereotype.java

package uci.uml.Foundation.Extension_Mechanisms;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.generate.*;


public class Stereotype extends GeneralizableElementImpl {
  //public Geometry icon;
  public Name _baseClass;
  //% public TaggedValue _requiredTag[];
  public Vector _requiredTag;
  //- public ModelElement _extendedElement[];
  //% public Constraint _stereotypeConstraint[];
  public Vector _stereotypeConstraint;
  //public Element _classification[];
    
  public Stereotype() { }
  public Stereotype(Name name) { super(name); }
  public Stereotype(String nameStr) { super(new Name(nameStr)); }
  public Stereotype(String nameStr, String baseClassName) {
    this(nameStr);
    setBaseClass(new Name(baseClassName));
  }

  public Name getBaseClass() { return _baseClass; }
  public void setBaseClass(Name x) {
    _baseClass = x;
  }
  
  public Vector getRequiredTag() { return _requiredTag; }
  public void setRequiredTag(Vector x) {
    _requiredTag = x;
  }
  public void addRequiredTag(TaggedValue x) {
    if (_requiredTag == null) _requiredTag = new Vector();
    _requiredTag.addElement(x);
  }
  public void removeRequiredTag(TaggedValue x) {
    _requiredTag.removeElement(x);
  }
  
  //- public Vector getExtendedElement() { return _extendedElement; }
  //- public void setExtendedElement(Vector x) {
  //-   _extendedElement = x;
  //- }
  
  public Vector getStereotypeConstraint() {
    return _stereotypeConstraint;
  }
  public void setStereotypeConstraint(Vector x) {
    _stereotypeConstraint = x;
  }
  public void addStereotypeConstraint(Constraint x) {
    if (_stereotypeConstraint == null) _stereotypeConstraint = new Vector();
    _stereotypeConstraint.addElement(x);
  }
  public void removeStereotypeConstraint(Constraint x) {
    _stereotypeConstraint.removeElement(x);
  }

  //public Element[] getClassification() { return _classification; }
  //public void setClassification(Element[] x) {
  //  _classification = x;
  //}

  ////////////////////////////////////////////////////////////////
  // debugging

  public String dbgString() {
    String s = getOCLTypeStr() + "{" +
      GeneratorDisplay.Generate(this) +
      "}";
    return s;
  }

}
