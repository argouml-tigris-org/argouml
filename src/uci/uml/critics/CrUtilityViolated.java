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





// File: CrUtilityViolated.java
// Classes: CrUtilityViolated
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;

/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). */

public class CrUtilityViolated extends CrUML {

  public CrUtilityViolated() {
    setHeadline("Remove Instance Variables from Utility Class");
    sd("The <<utility>> class <ocl>self</ocl> has instance variables. "+
       "Utility classes shold provide only static attributes and methods.\n\n"+
       "Applying and following the constraints imposed by stereotypes "+
       "allows you to add additional meaning to your design that helps "+
       "to clarify and make explicit your intent. \n\n"+
       "To fix this, press the \"Next>\" button, or remove instance "+
       "variables by dobule clicking on them in the navigator pane and "+
       "using the Remove From Project command, or remove the <<utility>> "+
       "stereotype. ");

    addSupportedDecision(CrUML.decSTORAGE);
    addSupportedDecision(CrUML.decSTEREOTYPES);
    addTrigger("stereotype");
    addTrigger("behavioralFeature");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof MMClass)) return NO_PROBLEM;
    MMClass cls = (MMClass) dm;
    if (!cls.containsStereotype(Stereotype.UTILITY)) return NO_PROBLEM;
    Vector str = cls.getInheritedStructuralFeatures();
    if (str == null) return NO_PROBLEM;
    java.util.Enumeration enum = str.elements();
    while (enum.hasMoreElements()) {
      StructuralFeature sf = (StructuralFeature) enum.nextElement();
      ChangeableKind ck = sf.getChangeable();
      ScopeKind sk = sf.getOwnerScope();
      if (ChangeableKind.NONE.equals(ck) && ScopeKind.INSTANCE.equals(sk))
	return PROBLEM_FOUND;
    }
    //needs-more-work?: don't count static or constants?
    return NO_PROBLEM;
  }

} /* end class CrUtilityViolated */

