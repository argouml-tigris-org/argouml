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



// File: CrDisambigStateName.java
// Classes: CrDisambigStateName
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

public class CrDisambigStateName extends CrUML {

  public CrDisambigStateName() {
    setHeadline("Choose a Unique Name for <ocl>self</ocl>");
    sd("Every state within a state machine must have a unique "+
       "name. There are at least two states in this machine named "+
       "\"<ocl>self</ocl>\".\n\n"+
       "Clear and unambiguous naming is key to code generation and "+
       "the understandability and maintainability of the design. \n\n"+
       "To fix this, use the \"Next>\" button, or manually select one of the "+
       "conflicting states and use the \"Properties\" tab to change "+
       "their names.");
    addSupportedDecision(CrUML.decNAMING);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    addTrigger("name");
    addTrigger("parent");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof State)) return NO_PROBLEM;
    State s = (State) dm;
    Name myName = s.getName();
    // needs-more-work: should define a CompoundCritic
    if (myName.equals(Name.UNSPEC)) return NO_PROBLEM;
    String myNameString = myName.getBody();
    if (myNameString.length() == 0) return NO_PROBLEM;
    ElementOwnership oe = s.getElementOwnership();
    if (oe == null) return NO_PROBLEM;
    Namespace ns = oe.getNamespace();
    if (ns == null) return NO_PROBLEM;
    Vector oes = ns.getOwnedElement();
    if (oes == null) return NO_PROBLEM;
    java.util.Enumeration enum = oes.elements();
    while (enum.hasMoreElements()) {
      ElementOwnership eo = (ElementOwnership) enum.nextElement();
      ModelElement me = (ModelElement) eo.getModelElement();
      if (!(me instanceof Classifier)) continue;
      if (me == s) continue;
      Name meName = me.getName();
      if (meName == null || meName.equals(Name.UNSPEC)) continue;
      if (meName.getBody().equals(myNameString)) return PROBLEM_FOUND;
    }
    return NO_PROBLEM;
  }

  public Icon getClarifier() {
    return ClClassName.TheInstance;
  }

} /* end class CrDisambigStateName */

