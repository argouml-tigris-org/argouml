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

// File: CrEmptyPackage.java
// Classes: CrEmptyPackage
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Behavioral_Elements.Collaborations.*;
import uci.uml.Model_Management.*;

/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). */

public class CrUnnavigableAssoc extends CrUML {

  public CrUnnavigableAssoc() {
    setHeadline("Make <ocl>self</ocl> Navigable");
    sd("The Association <ocl>self</ocl> is not navigable in any direction. "+
       "All associations should be navigable at least one way.\n\n"+
       "Setting the navigablility of associations allows your code to access "+
       "data by following pointers. \n\n"+
       "To fix this, select association \"<ocl>self</ocl>\" in the diagram or "+
       "navigator panel and click the properties tab.  Then use the "+
       "checkboxes at the bottom of the properties panel to turn on "+
       "navigablility. ");

    addSupportedDecision(CrUML.decRELATIONSHIPS);
    addTrigger("end_navigable");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof Association)) return NO_PROBLEM;
    Association asc = (Association) dm;
    Vector conn = asc.getConnection();
    if (asc instanceof AssociationRole)
      conn = ((AssociationRole)asc).getAssociationEndRole();
    int size = conn.size();
    for (int i=0; i < size; i++) {
      AssociationEnd ae = (AssociationEnd) conn.elementAt(i);
      if (ae.getIsNavigable()) return NO_PROBLEM;
    }
    return PROBLEM_FOUND;
  }

  public Class getWizardClass(ToDoItem item) { return WizNavigable.class; }

} /* end class CrUnnavigableAssoc */

