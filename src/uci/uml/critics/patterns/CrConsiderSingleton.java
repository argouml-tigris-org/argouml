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
    setHeadline("Consider using Singleton Pattern");
    sd("This class has no attributes or associations that are "+
       "navigable away from instances of this class.  This means that every "+
       "instance of this class will be equal() to every other instance, "+
       "since there will be no instance variables to differentiate them. "+
       "If this not your intent, you should define some attributes or "+
       "associations that will represent differences bewteen instances. "+
       "If there are no attributes or associations that differentiate "+
       "instances, the you shoudld consider having exatly one instance "+
       "of this class, as in the Singleton Pattern.\n"+
       "\n"+
       "Defining the multiplicity of instances is needed to complete the "+
       "information representation part of your design.  Using the Singleton "+
       "Pattern can save time and memory space.\n"+
       "\n"+
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
	if (ScopeKind.INSTANCE.equals(sf.getTargetScope())) return NO_PROBLEM;
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

