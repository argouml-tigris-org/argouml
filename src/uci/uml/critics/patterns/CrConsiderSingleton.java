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

// File: CrConsiderSingleton.java
// Classes: CrConsiderSingleton
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics.patterns;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.critics.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;


/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). */

public class CrConsiderSingleton extends CrUML {

  public CrConsiderSingleton() {
    setHeadline("Consider usins Singleton Pattern");
    sd("This class has no attributes or associations that are\n"+
       "navigable away from instances of this class.  This means that every\n"+
       "instance of this class will be equal() to every other instance,\n"+
       "since there will be no instance variables to differentiate them.\n"+
       "If this not your intent, you should define some attributes or \n"+
       "associations that will represent differences bewteen instances.\n"+
       "If there are no attributes or associations that differentiate\n"+
       "instances, the you shoudld consider having exatly one instance\n"+
       "of this class, as in the Singleton Pattern\n"+
       "\n"+
       "Defining the multiplicity of instances is needed to complete the\n"+
       "information representation part of your design.  Using the Singleton\n"+
       "Pattern can save time and memory space.\n"+
       "\n"+
       "To automatically apply the Singleton Pattern, press the FixIt icon;\n"+
       "or manually (1) mark the class with the Singlton stereotype, (2) add\n"+
       "a static variable that holds one instance of this class, (3) and\n"+
       "make all constructors private.\n"+
       "\n"+
       "To learn more about the Singleton Pattern, press the MoreInfo icon.");
       
    addSupportedDecision(CrUML.decINHERITANCE);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof uci.uml.Foundation.Core.Class)) return NO_PROBLEM;
    uci.uml.Foundation.Core.Class cls = (uci.uml.Foundation.Core.Class) dm;
    Vector str = cls.getStructuralFeature();
    Vector ends = cls.getAssociationEnd();

    //if it is already a Singleton, nevermind
    Vector stereos = cls.getStereotype();
    if (stereos != null) {
      java.util.Enumeration stereoEnum = stereos.elements();
      while (stereoEnum.hasMoreElements()) {
	Stereotype st = (Stereotype) stereoEnum.nextElement();
	if (st.getName().getBody().equals("Singleton")) return NO_PROBLEM;
      }
    }
    
    
    // if it has instance vars, no specific reason for Singleton
    if (str != null) {
      java.util.Enumeration strEnum = str.elements();
      while (strEnum.hasMoreElements()) {
	StructuralFeature sf = (StructuralFeature) strEnum.nextElement();
	if (sf.getTargetScope() == ScopeKind.INSTANCE) return NO_PROBLEM;
      }
    }
    
    // if it has outgoing assocs, no specific reason for Singleton
    if (ends != null) {
      java.util.Enumeration endEnum = ends.elements();
      while (endEnum.hasMoreElements()) {
	AssociationEnd ae = (AssociationEnd) endEnum.nextElement();
	IAssociation a = ae.getAssociation();
	Vector connections = a.getConnection();
	java.util.Enumeration connEnum = connections.elements();
	while (connEnum.hasMoreElements()) {
	  AssociationEnd ae2 = (AssociationEnd) connEnum.nextElement();
	  if (ae2 == ae) continue;
	  if (ae2.getIsNavigable()) return NO_PROBLEM;
	}
      }
    }
    // if it has no ivars, suggest the designer consider the Singleton Pattern
    return PROBLEM_FOUND;
  }

} /* end class CrConsiderSingleton */

