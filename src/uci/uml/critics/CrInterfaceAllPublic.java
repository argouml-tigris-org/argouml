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



// File: CrInterfaceAllPublic.java
// Classes: CrInterfaceAllPublic
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** Well-formedness rule [3] for Interface. See page 32 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrInterfaceAllPublic extends CrUML {

  public CrInterfaceAllPublic() {
    setHeadline("Operations in Interfaces must be public");
    sd("Interfaces are intended to specify the set of operations that "+
       "other classes must implement.  The must be public. \n\n"+
       "A well-designed set of interfaces is a good way to define the "+
       "possible extensions of a class framework. \n\n"+
       "To fix this, use the \"Next>\" button, or manually select the operations "+
       "of the interface and use the Properties tab add them public.");
    addSupportedDecision(CrUML.decPLANNED_EXTENSIONS);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    addTrigger("behavioralFeature");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof Interface)) return NO_PROBLEM;
    Interface inf = (Interface) dm;
    Vector bf = inf.getBehavioralFeature();
    if (bf == null) return NO_PROBLEM;
    java.util.Enumeration enum = bf.elements();
    while (enum.hasMoreElements()) {
      Feature f = (Feature) enum.nextElement();
      if (!f.getVisibility().equals(VisibilityKind.PUBLIC))
	return PROBLEM_FOUND;
    }
    return NO_PROBLEM;
  }

} /* end class CrInterfaceAllPublic.java */

