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



// File: CrClassMustBeAbstract.java
// Classes: CrClassMustBeAbstract
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;

/** Well-formedness rules [1] and [3] for Class. See page 29 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrClassMustBeAbstract extends CrUML {

  public CrClassMustBeAbstract() {
    setHeadline("Class Must be Abstract");
    sd("Classes that include or inherit abstract methods from base classes or "+
       "interfaces must be marked Abstract.\n\n"+
       "Deciding which classes are abstract or concrete is a key part of class "+
       "hierarchy design.\n\n"+
       "To fix this, use the \"Next>\" button, or manually select the class and use the "+
       "properties tab to add the Abstract keyword, or manually override each abstract "+
       "operation that is inherited from a base class or interface.");

    addSupportedDecision(CrUML.decINHERITANCE);
    addSupportedDecision(CrUML.decMETHODS);
    setKnowledgeTypes(Critic.KT_SEMANTICS);
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof MMClass)) return NO_PROBLEM;
    MMClass cls = (MMClass) dm;
    if (!cls.getIsAbstract()) return NO_PROBLEM;
    Vector beh = cls.getInheritedBehavioralFeatures();
    java.util.Enumeration enum = beh.elements();
    while (enum.hasMoreElements()) {
      BehavioralFeature bf = (BehavioralFeature) enum.nextElement();
      //needs-more-work: abstract methods are not part of UML, only java
      //if (bf.getIsAbstract()) return PROBLEM_FOUND;
    }
    return NO_PROBLEM;
  }

} /* end class CrClassMustBeAbstract.java */

