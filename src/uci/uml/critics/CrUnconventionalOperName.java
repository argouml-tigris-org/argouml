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



// File: CrUnconventionalOperName.java
// Classes: CrUnconventionalOperName
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;


public class CrUnconventionalOperName extends CrUML {

  public CrUnconventionalOperName() {
    setHeadline("Choose a Better Operation Name");
    sd("Normally operation names begin with a lowercase letter. "+
       "The name '<ocl>self</ocl>' is unconventional because it does not.\n\n"+
       "Following good naming conventions help to improve "+
       "the understandability and maintainability of the design. \n\n"+
       "To fix this, use the \"Next>\" button, or manually select <ocl>self</ocl> "+
       "and use the Properties tab to give it a new name.");
    addSupportedDecision(CrUML.decNAMING);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    addTrigger("feature_name");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof Operation)) return NO_PROBLEM;
    Operation oper = (Operation) dm;
    Name myName = oper.getName();
    if (myName == null || myName.equals(Name.UNSPEC)) return NO_PROBLEM;
    String nameStr = myName.getBody();
    if (nameStr == null || nameStr.length() == 0) return NO_PROBLEM;
    char initalChar = nameStr.charAt(0);
    if (oper.containsStereotype(Stereotype.CONSTRUCTOR)) return NO_PROBLEM;
    if (!Character.isLowerCase(initalChar)) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

    public ToDoItem toDoItem(Object dm, Designer dsgr) {
    Feature f = (Feature) dm;
    VectorSet offs = computeOffenders(f);
    return new ToDoItem(this, offs, dsgr);
  }

  protected VectorSet computeOffenders(Feature dm) {
    VectorSet offs = new VectorSet(dm);
    offs.addElement(dm.getOwner());
    return offs;
  }

  public boolean stillValid(ToDoItem i, Designer dsgr) {
    if (!isActive()) return false;
    VectorSet offs = i.getOffenders();
    Feature f = (Feature) offs.firstElement();
    if (!predicate(f, dsgr)) return false;
    VectorSet newOffs = computeOffenders(f);
    boolean res = offs.equals(newOffs);
//      System.out.println("offs="+ offs.toString() +
//  		       " newOffs="+ newOffs.toString() +
//  		       " res = " + res);
    return res;
  }

  public void initWizard(Wizard w) {
    if (w instanceof WizMEName) {
      ToDoItem item = w.getToDoItem();
      ModelElement me = (ModelElement) item.getOffenders().elementAt(0);
      String sug = me.getName().getBody();
      sug = sug.substring(0,1).toLowerCase() + sug.substring(1);
      String ins = "Change the operation name to start with a "+
	"lowercase letter.";
      ((WizMEName)w).setInstructions(ins);
      ((WizMEName)w).setSuggestion(sug);
    }
  }
  public Class getWizardClass(ToDoItem item) { return WizMEName.class; }

} /* end class CrUnconventionalOperName */

