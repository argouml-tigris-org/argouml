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

package org.argouml.uml.cognitive.critics;

import java.util.*;
import java.awt.*;

import ru.novosoft.uml.foundation.core.*;

import org.tigris.gef.util.*;

import org.argouml.cognitive.*;

/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). */

public class CrUselessAbstract extends CrUML {

  public CrUselessAbstract() {
    setHeadline("Define Concrete (Sub)Class");
    addSupportedDecision(CrUML.decINHERITANCE);
    addSupportedGoal(Goal.UNSPEC);
    addTrigger("specialization");
    addTrigger("isAbstract");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    MClass cls, c;
    if (!(dm instanceof MClass)) return false;
    cls = (MClass) dm;
    if (!cls.isAbstract()) return false;  // original class was not abstract
    VectorSet derived = (new VectorSet(cls)).reachable(new ChildGenDerivedClasses());
    java.util.Enumeration enum = derived.elements();
    while (enum.hasMoreElements()) {
      c = (MClass) enum.nextElement();
      if (!c.isAbstract()) return false;  // found a concrete subclass
    }
    return true; // no concrete subclasses defined, this class is "useless"
  }

} /* end class CrUselessAbstract */



class ChildGenDerivedClasses implements ChildGenerator {
  public java.util.Enumeration gen(Object o) {
    MClass c = (MClass) o;
    Vector specs = new Vector(c.getSpecializations());
    if (specs == null) {
      return EnumerationEmpty.theInstance();
    }
    // TODO: it would be nice to have a EnumerationXform
    // and a Functor object in uci.util
    Vector specClasses = new Vector(specs.size());
    java.util.Enumeration enum = specs.elements();
    while (enum.hasMoreElements()) {
      MGeneralization g = (MGeneralization) enum.nextElement();
      MGeneralizableElement ge = g.getChild();
      // assert: ge != null
      if (ge != null) specClasses.addElement(ge);
    }
    return specClasses.elements();
  }
} /* end class derivedClasses */
