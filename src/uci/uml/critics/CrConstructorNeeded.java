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





// File: CrConstructorNeeded.java
// Classes: CrConstructorNeeded
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

public class CrConstructorNeeded extends CrUML {

  public CrConstructorNeeded() {
    setHeadline("Add Constructor to <ocl>self</ocl>");
    sd("You have not yet defined a constructor for class <ocl>self</ocl>. "+
       "Constructors initialize new instances such that their "+
       "attributes have valid values.  This class probably needs a constructor "+
       "because not all of its attributes have initial values. \n\n"+
       "Defining good constructors is key to establishing class invariants, and "+
       "class invariants are a powerful aid in writing solid code. \n\n"+
       "To fix this, press the \"Next>\" button, or add a constructor manually "+
       "by clicking on <ocl>self</ocl> in the navigator pane and "+
       "using the Create menu to make a new constructor. ");

    addSupportedDecision(CrUML.decSTORAGE);
    addTrigger("behavioralFeature");
    addTrigger("structuralFeature");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof MMClass)) return NO_PROBLEM;
    MMClass cls = (MMClass) dm;

    boolean uninitializedIVar = false;
    Vector str = cls.getStructuralFeature();
    if (str == null) return NO_PROBLEM;
    java.util.Enumeration enum = str.elements();
    while (enum.hasMoreElements()) {
      StructuralFeature sf = (StructuralFeature) enum.nextElement();
      if (!(sf instanceof Attribute)) continue;  // what else could it be?
      Attribute attr = (Attribute) sf;
      ScopeKind sk = attr.getOwnerScope();
      ChangeableKind ck = attr.getChangeable();
      Expression init = attr.getInitialValue();
      if (ScopeKind.INSTANCE.equals(sk) && ChangeableKind.NONE.equals(ck))
	if (init == null || init.getBody() == null ||
	    init.getBody().getBody() == null ||
	    init.getBody().getBody().trim().length() == 0)
	  uninitializedIVar = true;
    }

    if (!uninitializedIVar) return NO_PROBLEM;

    Vector beh = cls.getBehavioralFeature();
    String className = cls.getName().getBody();
    if (beh == null) return PROBLEM_FOUND;
    enum = beh.elements();
    while (enum.hasMoreElements()) {
      BehavioralFeature bf = (BehavioralFeature) enum.nextElement();
      String operName = bf.getName().getBody();
      if (bf.getReturnType() != null) continue;
      if (!operName.equals(className)) continue;
      ScopeKind sk = bf.getOwnerScope();
      if (!ScopeKind.INSTANCE.equals(sk)) continue;
      if (bf.getReturnType() == null) return NO_PROBLEM;
    }
    return PROBLEM_FOUND;
  }

} /* end class CrConstructorNeeded */

