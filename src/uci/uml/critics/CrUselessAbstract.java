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
    setHeadline("Define Concrete (Sub)Class");
    String s;
    s = "<ocl>self</ocl> can never influence the running system because "+
      "it can never have any instances, and none of its "+
      "subclasses can have instances either. \n\n"+
      "To fix this problem: (1) define concrete subclasses that "+
      "implement the interface of this class; or (2) make "+
      "<ocl>self</ocl> or one of its existing subclasses concrete.";
    setDescription(s);
    addSupportedDecision(CrUML.decINHERITANCE);
    addSupportedGoal(Goal.UNSPEC);
    addTrigger("specialization");
    addTrigger("isAbstract");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    MMClass cls, c;
    if (!(dm instanceof MMClass)) return false;
    cls = (MMClass) dm;
    if (!cls.getIsAbstract()) return false;  // original class was not abstract
    VectorSet derived = (new VectorSet(cls)).reachable(new ChildGenDerivedClasses());
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
