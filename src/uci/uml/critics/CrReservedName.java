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



// File: CrReservedName.java
// Classes: CrReservedName
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import javax.swing.*;

import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;
import uci.uml.ui.ProjectBrowser;
import uci.uml.ui.Project;


public class CrReservedName extends CrUML {

  ////////////////////////////////////////////////////////////////
  // static variables

  public static Vector _umlReserved = new Vector();
  public static Vector _javaReserved = new Vector();
  

  static {
    _umlReserved.addElement("none");
    _umlReserved.addElement("interface");
    _umlReserved.addElement("sequential");
    _umlReserved.addElement("guarded");
    _umlReserved.addElement("concurrent");
    _umlReserved.addElement("frozen");
    _umlReserved.addElement("aggregate");
    _umlReserved.addElement("composite");
    _umlReserved.addElement("becomes");
    _umlReserved.addElement("call");
    _umlReserved.addElement("component");
    //_umlReserved.addElement("copy");
    //_umlReserved.addElement("create");
    _umlReserved.addElement("deletion");
    _umlReserved.addElement("derived");
    //_umlReserved.addElement("document");
    _umlReserved.addElement("enumeration");
    _umlReserved.addElement("extends");
    _umlReserved.addElement("facade");
    //_umlReserved.addElement("file");
    _umlReserved.addElement("framework");
    _umlReserved.addElement("friend");
    _umlReserved.addElement("import");
    _umlReserved.addElement("inherits");
    _umlReserved.addElement("instance");
    _umlReserved.addElement("invariant");
    _umlReserved.addElement("library");
    //_umlReserved.addElement("node");
    _umlReserved.addElement("metaclass");
    _umlReserved.addElement("powertype");
    _umlReserved.addElement("private");
    _umlReserved.addElement("process");
    _umlReserved.addElement("requirement");
    //_umlReserved.addElement("send");
    _umlReserved.addElement("stereotype");
    _umlReserved.addElement("stub");
    _umlReserved.addElement("subclass");
    _umlReserved.addElement("subtype");
    _umlReserved.addElement("system");
    _umlReserved.addElement("table");
    _umlReserved.addElement("thread");
    _umlReserved.addElement("type");
    _umlReserved.addElement("useCaseModel");
    _umlReserved.addElement("uses");
    _umlReserved.addElement("utility");
    //_umlReserved.addElement("destroy");
    _umlReserved.addElement("implementationClass");
    _umlReserved.addElement("postcondition");
    _umlReserved.addElement("precondition");
    _umlReserved.addElement("topLevelPackage");
    _umlReserved.addElement("subtraction");

//     _umlReserved.addElement("initial");
//     _umlReserved.addElement("final");
//     _umlReserved.addElement("fork");
//     _umlReserved.addElement("join");
//     _umlReserved.addElement("history");

    _javaReserved.addElement("public");
    _javaReserved.addElement("private");
    _javaReserved.addElement("protected");
    _javaReserved.addElement("package");
    _javaReserved.addElement("import");
    _javaReserved.addElement("java");
    _javaReserved.addElement("class");
    _javaReserved.addElement("interface");
    _javaReserved.addElement("extends");
    _javaReserved.addElement("implements");
    _javaReserved.addElement("native");
    _javaReserved.addElement("boolean");
    _javaReserved.addElement("void");
    _javaReserved.addElement("int");
    _javaReserved.addElement("char");
    _javaReserved.addElement("float");
    _javaReserved.addElement("long");
    _javaReserved.addElement("short");
    _javaReserved.addElement("byte");
    _javaReserved.addElement("double");
    _javaReserved.addElement("String");
    _javaReserved.addElement("Vector");
    _javaReserved.addElement("Hashtable");
    _javaReserved.addElement("Properties");
    _javaReserved.addElement("null");
    _javaReserved.addElement("true");
    _javaReserved.addElement("false");
    _javaReserved.addElement("rest");
    _javaReserved.addElement("operator");
    _javaReserved.addElement("inner");
    _javaReserved.addElement("outer");
    _javaReserved.addElement("this");
    _javaReserved.addElement("super");
    _javaReserved.addElement("byvalue");
    _javaReserved.addElement("cast");
    _javaReserved.addElement("const");
    _javaReserved.addElement("future");
    _javaReserved.addElement("generic");
    _javaReserved.addElement("goto");
    _javaReserved.addElement("throws");
    _javaReserved.addElement("try");
    _javaReserved.addElement("catch");
    _javaReserved.addElement("finally");
    _javaReserved.addElement("new");
    _javaReserved.addElement("synchronized");
    _javaReserved.addElement("static");
    _javaReserved.addElement("final");
    _javaReserved.addElement("abstract");
    _javaReserved.addElement("for");
    _javaReserved.addElement("if");
    _javaReserved.addElement("else");
    _javaReserved.addElement("while");
    _javaReserved.addElement("return");
    _javaReserved.addElement("continue");
    _javaReserved.addElement("break");
    _javaReserved.addElement("do");
    _javaReserved.addElement("until");
    _javaReserved.addElement("switch");
    _javaReserved.addElement("case");
    _javaReserved.addElement("default");
    _javaReserved.addElement("instanceof");
    _javaReserved.addElement("var");
    _javaReserved.addElement("volatile");
    _javaReserved.addElement("transient");
  }




  ////////////////////////////////////////////////////////////////
  // constructor

  public CrReservedName() {
    setHeadline("Change <ocl>self</ocl> to a Non-Reserved Word");
    sd("\"<ocl>self</ocl>\" is a reserver word or very close to one.  The "+
       "names of model elements must not conflict with "+
       "reserved words of programming languages or UML.\n\n"+
       "Using legal names is needed to generate compilable code. \n\n"+
       "To fix this, use the \"Next>\" button, or manually select the "+
       "highlighted element and use the Properties tab to give it "+
       "a different name.");
    setPriority(ToDoItem.HIGH_PRIORITY);
    addSupportedDecision(CrUML.decNAMING);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    addTrigger("name");
    addTrigger("feature_name");
  }

  ////////////////////////////////////////////////////////////////
  // Critic implementation

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof ModelElement)) return NO_PROBLEM;
    ModelElement me = (ModelElement) dm;
    Name meName = me.getName();
    if (meName == null || meName.equals(Name.UNSPEC)) return NO_PROBLEM;
    String nameStr = meName.getBody();
    if (nameStr == null || nameStr.length() == 0) return NO_PROBLEM;

    // Dont critique the built-in java types, they are supposed to
    // have those "reserved" names.
    Project p = ProjectBrowser.TheInstance.getProject();
    Hashtable definedTypes = p.getDefinedTypes();
    if (definedTypes.get(nameStr) == dm) return NO_PROBLEM;

    java.util.Enumeration enum = _umlReserved.elements();
    while (enum.hasMoreElements()) {
      String word = (String) enum.nextElement();
      if (word.equalsIgnoreCase(nameStr)) return PROBLEM_FOUND;
    }

    enum = _javaReserved.elements();
    while (enum.hasMoreElements()) {
      String word = (String) enum.nextElement();
      if (word.equalsIgnoreCase(nameStr)) return PROBLEM_FOUND;
    }

    return NO_PROBLEM;
  }

  public Icon getClarifier() { return ClClassName.TheInstance; }

  public void initWizard(Wizard w) {
    if (w instanceof WizMEName) {
      ToDoItem item = w.getToDoItem();
      ModelElement me = (ModelElement) item.getOffenders().elementAt(0);
      String sug = me.getName().getBody();
      String ins = "Change the name to something different.";
      ((WizMEName)w).setInstructions(ins);
      ((WizMEName)w).setSuggestion(sug);
      ((WizMEName)w).setMustEdit(true);
    }
  }
  public Class getWizardClass(ToDoItem item) { return WizMEName.class; }

} /* end class CrReservedName */

