// Source file: f:/jr/projects/uml/Foundation/Extension_Mechanisms/Stereotype.java

package uci.uml.Foundation.Extension_Mechanisms;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.generate.*;


public class Stereotype extends GeneralizableElementImpl {


  //needs-more-work: some of these apply to multiple OCL base classes

  //maybe these constants should be moved to their respective base
  //class source files...

  public static Stereotype BECOMES =
  new Stereotype("becomes", "Dependency");
  public static Stereotype CALL =
  new Stereotype("call", "Dependency");
  public static Stereotype COPY =
  new Stereotype("copy", "Dependency");
  public static Stereotype CREATE =
  new Stereotype("create", "BehavioralFeature");
  public static Stereotype DESTROY =
  new Stereotype("destroy", "BehavioralFeature");
  public static Stereotype DELETION =
  new Stereotype("deletion", "Refinement");
  public static Stereotype DERIVED =
  new Stereotype("derived", "Dependency");
  public static Stereotype DOCUMENT =
  new Stereotype("document", "Component");
  public static Stereotype ENUMERATION =
  new Stereotype("enumeration", "DataType");
  public static Stereotype EXECUTABLE =
  new Stereotype("executable", "Component");
  public static Stereotype EXTENDS =
  new Stereotype("extends", "Generalization");
  public static Stereotype FACADE =
  new Stereotype("facade", "Package");
  public static Stereotype FILE =
  new Stereotype("file", "Component");
  public static Stereotype FRAMEWORK =
  new Stereotype("framework", "Package");
  public static Stereotype FRIEND =
  new Stereotype("friend", "Dependency");
  public static Stereotype IMPORT =
  new Stereotype("import", "Dependency");
  public static Stereotype IMPLEMENTATIONCLASS =
  new Stereotype("implementationClass", "Class");
  public static Stereotype INHERITS =
  new Stereotype("inherits", "Generalization");
  public static Stereotype INSTANCE =
  new Stereotype("instance", "Dependency");
  public static Stereotype INVARIANT =
  new Stereotype("invariant", "Constraint");
  public static Stereotype LIBRARY =
  new Stereotype("library", "Component");
  public static Stereotype METACLASS =
  new Stereotype("metaclass", "Dependency");
  public static Stereotype POSTCONDITION =
  new Stereotype("postcondition", "Constraint");
  public static Stereotype POWERTYPE =
  new Stereotype("powertype", "Classifier");
  public static Stereotype PRECONDITION =
  new Stereotype("precondition", "Constraint");
  public static Stereotype PRIVATE =
  new Stereotype("private", "Generalization");
  public static Stereotype PROCESS =
  new Stereotype("process", "Classifier");
  public static Stereotype REQUIREMENT =
  new Stereotype("requirement", "Comment");
  public static Stereotype SEND =
  new Stereotype("send", "Dependency");
  public static Stereotype STEREOTYPE =
  new Stereotype("stereotype", "Classifier");
  public static Stereotype STUB =
  new Stereotype("stub", "Package");
  public static Stereotype SUBCLASS =
  new Stereotype("subclass", "Generalization");
  public static Stereotype SUBTRACTION =
  new Stereotype("subtraction", "Refinement");
  public static Stereotype SUBTYPE =
  new Stereotype("subtype", "Generalization");
  public static Stereotype SYSTEM =
  new Stereotype("system", "Package");
  public static Stereotype TABLE =
  new Stereotype("table", "Component");
  public static Stereotype THREAD =
  new Stereotype("thread", "Classifier");
  public static Stereotype TOPLEVELPACKAGE =
  new Stereotype("topLevelPackage", "Package");
  public static Stereotype TYPE =
  new Stereotype("type", "Class");
  public static Stereotype USECASEMODEL =
  new Stereotype("useCaseModel", "Model");
  public static Stereotype USES =
  new Stereotype("uses", "Generalization");
  public static Stereotype UTILITY =
  new Stereotype("utility", "Classifier");
  
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
