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



package uci.uml.Foundation.Extension_Mechanisms;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.generate.*;


public class Stereotype extends GeneralizableElementImpl {


  //needs-more-work: some of these apply to multiple OCL base classes

  //maybe these constants should be moved to their respective base
  //class source files...

  public static Stereotype BECOMES = new Stereotype("becomes", "Dependency");
  public static Stereotype CALL = new Stereotype("call", "Dependency");
  public static Stereotype COMPONENT = new Stereotype("component", "Instance");
  public static Stereotype COPY = new Stereotype("copy", "Dependency");
  public static Stereotype CREATE = new Stereotype("create", "BehavioralFeature");
  public static Stereotype DELETION = new Stereotype("deletion", "Refinement");
  public static Stereotype DERIVED = new Stereotype("derived", "Dependency");
  public static Stereotype DOCUMENT = new Stereotype("document", "Component");
  public static Stereotype ENUMERATION = new Stereotype("enumeration", "DataType");
  public static Stereotype EXECUTABLE = new Stereotype("executable", "Component");
  public static Stereotype EXTENDS = new Stereotype("extends", "Generalization");
  public static Stereotype FACADE = new Stereotype("facade", "Package");
  public static Stereotype FILE = new Stereotype("file", "Component");
  public static Stereotype FRAMEWORK = new Stereotype("framework", "Package");
  public static Stereotype FRIEND = new Stereotype("friend", "Dependency");
  public static Stereotype IMPORT = new Stereotype("import", "Dependency");
  public static Stereotype INHERITS = new Stereotype("inherits", "Generalization");
  public static Stereotype INSTANCE = new Stereotype("instance", "Dependency");
  public static Stereotype INVARIANT = new Stereotype("invariant", "Constraint");
  public static Stereotype LIBRARY = new Stereotype("library", "Component");
  public static Stereotype NODE = new Stereotype("node", "Instance");
  public static Stereotype METACLASS = new Stereotype("metaclass", "Dependency");
  public static Stereotype POWERTYPE = new Stereotype("powertype", "Classifier");
  public static Stereotype PRIVATE = new Stereotype("private", "Generalization");
  public static Stereotype PROCESS = new Stereotype("process", "Classifier");
  public static Stereotype REQUIREMENT = new Stereotype("requirement", "Comment");
  public static Stereotype SEND = new Stereotype("send", "Dependency");
  public static Stereotype STEREOTYPE = new Stereotype("stereotype", "Classifier");
  public static Stereotype STUB = new Stereotype("stub", "Package");
  public static Stereotype SUBCLASS = new Stereotype("subclass", "Generalization");
  public static Stereotype SUBTYPE = new Stereotype("subtype", "Generalization");
  public static Stereotype SYSTEM = new Stereotype("system", "Package");
  public static Stereotype TABLE = new Stereotype("table", "Component");
  public static Stereotype THREAD = new Stereotype("thread", "Classifier");
  public static Stereotype TYPE = new Stereotype("type", "Class");
  public static Stereotype USECASEMODEL = new Stereotype("useCaseModel", "Model");
  public static Stereotype USES = new Stereotype("uses", "Generalization");
  public static Stereotype UTILITY = new Stereotype("utility", "Classifier");

  public static Stereotype CONSTRUCTOR = new Stereotype("constructor", "Operation");
  
  public static Stereotype DESTROY =
  new Stereotype("destroy", "BehavioralFeature");
  public static Stereotype IMPLEMENTATIONCLASS =
  new Stereotype("implementationClass", "Class");
  public static Stereotype POSTCONDITION =
  new Stereotype("postcondition", "Constraint");
  public static Stereotype PRECONDITION =
  new Stereotype("precondition", "Constraint");
  public static Stereotype TOPLEVELPACKAGE =
  new Stereotype("topLevelPackage", "Package");
  public static Stereotype SUBTRACTION =
  new Stereotype("subtraction", "Refinement");

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

  static final long serialVersionUID = -3483418093201070198L;
}
