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



// File: CrNameConfusion.java
// Classes: CrNameConfusion
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
import uci.uml.Behavioral_Elements.State_Machines.*;

/** Well-formedness rule [1] for Namespace. See page 33 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrNameConfusion extends CrUML {

  public CrNameConfusion() {
    setHeadline("Revise Name to Avoid Confusion");
    sd("Names should be clearly distinct from each other. These two "+
       "names are so close to each other that readers might be confused.\n\n"+
       "Clear and unambiguous naming is key to code generation and "+
       "the understandability and maintainability of the design. \n\n"+
       "To fix this, use the \"Next>\" button, or manually select the "+
       "elements and use the Properties tab to change their names.  Avoid " +
       "names that differ from other names only in capitalization, or "+
       "use of underscore characters, or by only one character.");
    addSupportedDecision(CrUML.decNAMING);
    setKnowledgeTypes(Critic.KT_PRESENTATION);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    addTrigger("name");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof ModelElement)) return NO_PROBLEM;
    ModelElement me = (ModelElement) dm;
    VectorSet offs = computeOffenders(me);
    if (offs.size() > 1) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

  public VectorSet computeOffenders(ModelElement dm) {
    Namespace ns = dm.getNamespace();
    VectorSet res = new VectorSet(dm);
    Name n = dm.getName();
    if (n == null || n.equals(Name.UNSPEC)) return res;
    String dmNameStr = n.getBody();
    if (dmNameStr == null || dmNameStr.length() == 0) return res;
    String stripped2 = strip(dmNameStr);
    if (ns == null) return res;
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
    VectorSet offs = computeOffenders(me);
    return new ToDoItem(this, offs, dsgr);
  }

  public boolean stillValid(ToDoItem i, Designer dsgr) {
    if (!isActive()) return false;
    VectorSet offs = i.getOffenders();
    ModelElement dm = (ModelElement) offs.firstElement();
    if (!predicate(dm, dsgr)) return false;
    VectorSet newOffs = computeOffenders(dm);
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

  public Icon getClarifier() {
    return ClClassName.TheInstance;
  }


  public void initWizard(Wizard w) {
    if (w instanceof WizManyNames) {
      ToDoItem item = w.getToDoItem();
      String ins = "Change each name to be significantly different from "+
	"the others.  Names should differ my more than one character and " +
	"not just differ my case (capital or lower case).";
      ((WizManyNames)w).setInstructions(ins);
      ((WizManyNames)w).setMEs(item.getOffenders().asVector());
    }
  }
  public Class getWizardClass(ToDoItem item) {
    return WizManyNames.class;
  }

} /* end class CrNameConfusion.java */

