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



// File: CrNavFromInterface.java
// Classes: CrNavFromInterface.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Behavioral_Elements.Collaborations.*;

public class CrNavFromInterface extends CrUML {

  public CrNavFromInterface() {
    setHeadline("Remove Navigation from Interface <ocl>self</ocl>");
    sd("Associations involving an Interface can be not be naviagable in "+
       "the direction from the Interface.  This is because interfaces do "+
       "contain only operation declarations and cannot hold pointers to "+
       "other objects.\n\n" +
       "This part of the design should be changed before you can generate "+
       "code from this design.  If you do generate code before fixing this "+
       "problem, the code will not match the design.\n\n"+
       "To fix this, select the Association and use the \"Properties\" "+
       "tab to uncheck Navigable for the end touching the Interface.  "+
       "The Association should then appear with an stick arrowhead pointed "+
       "away from the Interface.");
    addSupportedDecision(CrUML.decRELATIONSHIPS);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    addTrigger("end_navigable");
  }

  /** Applies to Associations only, not AssociationClasses. */
  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof Association)) return NO_PROBLEM;
    Association asc = (Association) dm;
    Vector conns = asc.getConnection();
    if (asc instanceof AssociationRole)
      conns = ((AssociationRole)asc).getAssociationEndRole();
    int aggCount = 0;
    java.util.Enumeration enum = conns.elements();
    while (enum.hasMoreElements()) {
      AssociationEnd ae = (AssociationEnd) enum.nextElement();
      if (!ae.getIsNavigable()) continue;
      if (ae.getType() instanceof Interface) return PROBLEM_FOUND;
      if (ae.getType() instanceof ClassifierRole &&
	   ((ClassifierRole)ae.getType()).getBase() instanceof Interface)
	return PROBLEM_FOUND;
    }
    return NO_PROBLEM;
  }

} /* end class CrNavFromInterface */

