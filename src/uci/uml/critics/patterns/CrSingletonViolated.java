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
    sd("This class is marked with the Singleton stereotype, but it does "+
       "not satisfy the constraints imposed on singletons.  A singleton "+
       "class can have at most one instance.  This means that the class "+
       "must have (1) a static variable holding the instance, (2) only "+
       "private constructors so that new instances cannot be made by "+
       "other code, and (3) there must be at least one constructor to "+
       "override the default constructor.\n"+
       "\n"+
       "Whenever you mark a class with a stereotype, the class should "+
       "satisfy all constraints of the stereotype.  This is an important "+
       "part of making a self-consistent and understangle design. Using "+
       "the Singleton Pattern can save time and memory space.\n"+
       "\n"+
       "If you no longer want this class to be a Singleton, remove the "+
       "Singleton stereotype by clicking on the class and deleting Singleton "+
       "from the Props tab. \n"+
       "To automatically apply the Singleton Pattern, press the \"Next>\" button; "+
       "or manually (1) mark the class with the Singlton stereotype, (2) add "+
       "a static variable that holds one instance of this class, (3) and "+
       "make all constructors private.\n"+
       "\n"+
       "To learn more about the Singleton Pattern, press the MoreInfo icon.");

    addSupportedDecision(CrUML.decPATTERNS);
    setPriority(ToDoItem.LOW_PRIORITY);
    addTrigger("stereotype");
    addTrigger("structuralFeature");
    addTrigger("associationEnd");
  }

  protected void sd(String s) { setDescription(s); }

  public boolean predicate2(Object dm, Designer dsgr) {
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

