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

// File: CrUselessAbstract.java
// Classes: CrUselessAbstract
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import java.awt.*;

import uci.uml.Foundation.Core.*;

import uci.argo.kernel.*;
import uci.util.*;

/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). */

public class CrUselessAbstract extends CrUML {

  public CrUselessAbstract() {
    setHeadline("Useless Abstract");
    setDescription("This class can never influence the running system because\n"+
		"it can never have any instances, and none of its\n"+
		"subclasses can have instances either.\n\n"+
		"To fix this problem: (1) define concrete subclasses that\n"+
		"implement the interface of this class; or (2) make this\n"+
		"class or one of its existing subclasses concrete");
    addSupportedDecision(CrUML.decINHERITANCE);
    addSupportedGoal(Goal.UNSPEC);
  }

  public boolean predicate(Object dm, Designer dsgr) {
    MMClass cls, c;
    if (!(dm instanceof MMClass)) return false;
    cls = (MMClass) dm;
    if (!cls.getIsAbstract()) return false;  // original class was not abstract
    Set derived = (new Set(cls)).reachable(new ChildGenDerivedClasses());
    Enumeration enum = derived.elements();
    while (enum.hasMoreElements()) {
      c = (MMClass) enum.nextElement();
      if (!c.getIsAbstract()) return false;  // found a concrete subclass
    }
    return true; // no concrete subclasses defined, this class is "useless"
  }

} /* end class CrUselessAbstract */



class ChildGenDerivedClasses implements ChildGenerator {
  public Enumeration gen(Object o) {
    MMClass c = (MMClass) o;
    Vector specs = c.getSpecialization();
    if (specs == null) {
      return EnumerationEmpty.theInstance();
    }
    // needs-more-work: it would be nice to have a EnumerationXform
    // and a Functor object in uci.util
    Vector specClasses = new Vector(specs.size());
    Enumeration enum = specs.elements();
    while (enum.hasMoreElements()) {
      Generalization g = (Generalization) enum.nextElement();
      GeneralizableElement ge = g.getSubtype();
      // assert: ge != null
      if (ge != null) specClasses.addElement(ge);
    }
    return specClasses.elements();
  }
} /* end class derivedClasses */
