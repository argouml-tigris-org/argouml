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



// File: CrReservedName.java.java
// Classes: CrReservedName.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;


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
    setHeadline("Choose a Non-Reserved Name");
    sd("The names of model elements must should not conflict with "+
       "reserved words of programming languages or UML.\n\n"+
       "Using legal names is needed to generate compilable code. \n\n"+
       "To fix this, use the FixIt button, or manually select the "+
       "highlighted element and use the Properties tab to give it "+
       "a different name.");
    addSupportedDecision(CrUML.decNAMING);
    setPriority(ToDoItem.HIGH_PRIORITY);
  }

  protected void sd(String s) { setDescription(s); }

  ////////////////////////////////////////////////////////////////
  // Critic implementation
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof ModelElement)) return NO_PROBLEM;
    ModelElement me = (ModelElement) dm;
    Name meName = me.getName();
    if (meName == null || meName.equals(Name.UNSPEC)) return NO_PROBLEM;
    String nameStr = meName.getBody();

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

} /* end class CrReservedName */

