// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: CrSingletonViolated.java
// Classes: CrSingletonViolated
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics.patterns;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.critics.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Extension_Mechanisms.*;

public class CrSingletonViolated extends CrUML {

  public CrSingletonViolated() {
    setHeadline("Singleton Stereotype Violated");
    sd("This class is marked with the Singleton stereotype, but it does\n"+
       "not satisfy the constraints imposed on singletons.  A singleton\n"+
       "class can have at most one instance.  This means that the class\n"+
       "must have (1) a static variable holding the instance, (2) only\n"+
       "private constructors so that new instances cannot be made by\n"+
       "other code, and (3) there must be at least one constructor to.\n"+
       "override the default constructor.??\n"+
       "\n"+
       "Whenever you mark a class with a stereotype, the class should\n"+
       "satisfy all constraints of the stereotype.  This is an important\n"+
       "part of making a self-consistent and understangle design. Using \n"+
       "the Singleton Pattern can save time and memory space.\n"+
       "\n"+
       "If you no longer want this class to be a Singleton, remove the\n"+
       "Singleton stereotype by clicking on the class and deleting Singleton\n"+
       "from the Props tab.\n"+
       "To automatically apply the Singleton Pattern, press the FixIt icon;\n"+
       "or manually (1) mark the class with the Singlton stereotype, (2) add\n"+
       "a static variable that holds one instance of this class, (3) and\n"+
       "make all constructors private.\n"+
       "\n"+
       "To learn more about the Singleton Pattern, press the MoreInfo icon.");
       
    addSupportedDecision(CrUML.decPATTERNS);
    setPriority(ToDoItem.LOW_PRIORITY);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof MMClass)) return NO_PROBLEM;
    MMClass cls = (MMClass) dm;
    Vector str = cls.getStructuralFeature();
    Vector ends = cls.getAssociationEnd();

    boolean markedSingleton = false;
    //if it is not marked Singleton, nevermind
    Vector stereos = cls.getStereotype();
    if (stereos != null) {
      java.util.Enumeration stereoEnum = stereos.elements();
      while (stereoEnum.hasMoreElements()) {
	Stereotype st = (Stereotype) stereoEnum.nextElement();
	if (st.containsStereotype(PatternStereotypes.Singleton))
	  markedSingleton = true;
      }
    }
    if (!markedSingleton) return NO_PROBLEM;

    //needs-more-work
    // (1) The class must have at least one static attr with of the same type
    // (2) All constructors should be private
    // (3) There must be at least on constructor to override default constructor

    return NO_PROBLEM;
  }

} /* end class CrSingletonViolated */

