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



// File: CrUnconventionalPackName.java
// Classes: CrUnconventionalPackName
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


public class CrUnconventionalPackName extends CrUML {

  public CrUnconventionalPackName() {
    setHeadline("Revise Package Name <ocl>self</ocl>");
    sd("Normally package names are written in all lower case with "+
       "periods used to indicate \"nested\" packages.  "+
       "The name '<ocl>self</ocl>' "+
       "is unconventional because it is not all lower case with periods.\n\n"+
       "Following good naming conventions help to improve "+
       "the understandability and maintainability of the design. \n\n"+
       "To fix this, use the \"Next>\" button, or manually select <ocl>self</ocl> "+
       "and use the Properties tab to give it a different name.");
    addSupportedDecision(CrUML.decNAMING);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    addTrigger("name");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof Model)) return NO_PROBLEM;
    Model m = (Model) dm;
    Name myName = m.getName();
    if (myName == null || myName.equals(Name.UNSPEC)) return NO_PROBLEM;
    String nameStr = myName.getBody();
    if (nameStr == null || nameStr.length() == 0) return NO_PROBLEM;
    int size = nameStr.length();
    for (int i = 0; i < size; i++) {
      char c = nameStr.charAt(i);
      if (!Character.isLowerCase(c) && c != '.') return PROBLEM_FOUND;
    }
    return NO_PROBLEM;
  }

  public Icon getClarifier() {
    return ClClassName.TheInstance;
  }

    public void initWizard(Wizard w) {
    if (w instanceof WizMEName) {
      ToDoItem item = w.getToDoItem();
      ModelElement me = (ModelElement) item.getOffenders().elementAt(0);
      String ins = "Change the name of this package.";
      String nameStr = me.getName().getBody();
      String sug = "";
      int size = nameStr.length();
      for (int i = 0; i < size; i++) {
	char c = nameStr.charAt(i);
	if (Character.isLowerCase(c) || c == '.') sug += c;
	else if (Character.isUpperCase(c))
	  sug += Character.toLowerCase(c);
      }
      if (sug.equals("")) sug = "PackageName";
      ((WizMEName)w).setInstructions(ins);
      ((WizMEName)w).setSuggestion(sug);
    }
  }
  public Class getWizardClass(ToDoItem item) { return WizMEName.class; }

} /* end class CrUnconventionalPackName */

