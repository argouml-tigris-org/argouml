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



// File: CrDupRoleNames.java
// Classes: CrDupRoleNames
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Behavioral_Elements.Collaborations.*;

/** Well-formedness rule [1] for Associations. See page 27 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrDupRoleNames extends CrUML {

  public CrDupRoleNames() {
    setHeadline("Change <ocl>self</ocl> Role Names");
    sd("Association <ocl>self</ocl> has two roles with conflicting names. \n\n"+
       "Clear and unambiguous naming is key to code generation and "+
       "the understandability and maintainability of the design. \n\n"+
       "To fix this, use the \"Next>\" button, or manually select <ocl>self</ocl> "+
       "and use the Properties tab to change the role names.");
    addSupportedDecision(CrUML.decNAMING);
    addTrigger("connection");
    addTrigger("end_name");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof IAssociation)) return NO_PROBLEM;
    IAssociation asc = (IAssociation) dm;
    Vector conns = asc.getConnection();
    if (dm instanceof AssociationRole)
      conns = ((AssociationRole)asc).getAssociationEndRole();
    Vector namesSeen = new Vector();
    java.util.Enumeration enum = conns.elements();
    while (enum.hasMoreElements()) {
      AssociationEnd ae = (AssociationEnd) enum.nextElement();
      Name aeName = ae.getName();
      if (Name.UNSPEC.equals(aeName)) continue;
      String nameStr = aeName.getBody();
      if (nameStr.length() == 0) continue;
      if (namesSeen.contains(nameStr)) return PROBLEM_FOUND;
      namesSeen.addElement(nameStr);
    }
    return NO_PROBLEM;
  }

} /* end class CrDupRoleNames */

