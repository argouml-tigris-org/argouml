// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: CrNameConfusion.java.java
// Classes: CrNameConfusion.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;

/** Well-formedness rule [1] for Namespace. See page 33 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrNameConfusion extends CrUML {

  public CrNameConfusion() {
    setHeadline("Revise Name to Avoid Confusion");
    sd("Names should be clearly distinct from each other. These two \n"+
       "names are so close to each other that readers might be confused. \n\n"+
       "Clear and unambiguous naming is key to code generation and \n"+
       "the understandability and maintainability of the design. \n\n"+
       "To fix this, use the FixIt button, or manually select the elements \n"+
       "and use the Properties tab to change their names.  Avoid names\n" +
       "that differ from other names only in capitalization, or use of \n"+
       "underscore characters, or by only one character.");
    addSupportedDecision(CrUML.decNAMING);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof ModelElement)) return NO_PROBLEM;
    ModelElement me = (ModelElement) dm;
    Set offs = computeOffenders(me);
    if (offs.size() > 1) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }
  
  public Set computeOffenders(ModelElement dm) {
    Namespace ns = dm.getNamespace();
    Set res = new Set(dm);
    Name n = dm.getName();
    if (n == null || n.equals(Name.UNSPEC)) return res;
    String dmNameStr = n.getBody();
    String stripped2 = strip(dmNameStr);
    Vector oes = ns.getOwnedElement();
    if (oes == null) return res;
    java.util.Enumeration enum = oes.elements();
    while (enum.hasMoreElements()) {
      ElementOwnership eo = (ElementOwnership) enum.nextElement();
      ModelElement me2 = (ModelElement) eo.getModelElement();
      if (me2 == dm) continue;
      Name meName = me2.getName();
      if (meName == null || meName.equals(Name.UNSPEC)) continue;
      String compareName = meName.getBody();
      if (confusable(stripped2, strip(compareName)) &&
	  !dmNameStr.equals(compareName)) {
	res.addElement(me2);
      }
    }
    return res;
  }
    
  public ToDoItem toDoItem(Object dm, Designer dsgr) {
    ModelElement me = (ModelElement) dm;
    Set offs = computeOffenders(me);
    return new ToDoItem(this, offs, dsgr);
  }

  public boolean stillValid(ToDoItem i, Designer dsgr) {
    if (!isActive()) return false;
    Set offs = i.getOffenders();
    ModelElement dm = (ModelElement) offs.firstElement();
    if (!predicate(dm, dsgr)) return false;
    Set newOffs = computeOffenders(dm);
    boolean res = offs.equals(newOffs);
//      System.out.println("offs="+ offs.toString() +
//  		       " newOffs="+ newOffs.toString() +
//  		       " res = " + res);
    return res;
  }

  public boolean confusable(String stripped1, String stripped2) {
    int countDiffs = countDiffs(stripped1, stripped2);
    return countDiffs <= 1;
  }

  public int countDiffs(String s1, String s2) {
    int len = Math.min(s1.length(), s2.length());
    int count = Math.abs(s1.length() - s2.length());
    if (count > 2) return count;
    for (int i = 0; i < len; i++) {
      if (s1.charAt(i) != s2.charAt(i)) count++;
    }
    return count;
  }
  
  public String strip(String s) {
    StringBuffer res = new StringBuffer(s.length());
    int len = s.length();
    for (int i = 0; i < len; i++) {
      char c = s.charAt(i);
      if (Character.isLetterOrDigit(c))
	res.append(Character.toLowerCase(c));
    }
    return res.toString();
  }
  
} /* end class CrNameConfusion.java */

