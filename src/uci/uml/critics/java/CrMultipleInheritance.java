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



// File: CrMultipleInheritance.java.java
// Classes: CrMultipleInheritance.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics.java;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.critics.*;

/** Well-formedness rule [2] for AssociationEnd. See page 28 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrMultipleInheritance extends CrUML {

  public CrMultipleInheritance() {
    setHeadline("Change Multiple Inheritance to Interfaces");
    sd("<ocl>self</ocl> has multiple base classes, but Java does not support "+
       "multiple inheritance.  You must use interfaces instead. \n\n"+
       "This change is required before you can generate Java code.\n\n"+
       "To fix this, use the \"Next>\" button, or manually (1) remove one of "+
       "the base classes and then (2) optionally define a new interface "+
       "with the same method declarations and (3) add it as an "+
       "interface of <ocl>self</ocl>, and (4) move the method bodies from the "+
       "old base class down into <ocl>self</ocl>.");

    addSupportedDecision(CrUML.decINHERITANCE);
    addSupportedDecision(CrUML.decCODE_GEN);
    addTrigger("generalization");
  }

  protected void sd(String s) { setDescription(s); }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof Classifier)) return NO_PROBLEM;
    Classifier cls = (Classifier) dm;
    Vector gen = cls.getGeneralization();
    if (gen != null && gen.size() > 1)
      return PROBLEM_FOUND;
    else
      return NO_PROBLEM;
  }

  public void initWizard(Wizard w) {
    if (w instanceof WizCueCards) {
      WizCueCards wcc = (WizCueCards) w;
      ToDoItem item = w.getToDoItem();
      ModelElement me = (ModelElement) item.getOffenders().elementAt(0);
      String nameStr = me.getName().getBody();
      wcc.addCue("Remove the generalization arrow to one of the base "+
		 "classes of {name}.");
      wcc.addCue("Optionally, use the Interface tool to create a new "+
		 "Interface for {name} to implement.");
      wcc.addCue("Use the Realization tool to add a dashed arrow from "+
		 "{name} to the new Interface.");
      wcc.addCue("Move method declarations from the unused base class "+
		 "to the new Interface and move method bodies down into "+
		 "{name}.");
      wcc.addCue("If the unused base class is not used by anything else "+
		 "then it can be removed.");
    }
  }
  public Class getWizardClass(ToDoItem item) { return WizCueCards.class; }

} /* end class CrMultipleInheritance.java */

