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

// File: Set.java
// Classes: Set
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.util;

import java.util.*;


/** An Ordered, non-duplicated collecton of objects (not exactly a
 *  mathemetical set because it is ordered).  Implemented with a
 *  Vector. */

public class Set implements java.io.Serializable  {
  ////////////////////////////////////////////////////////////////
  // constants
  public static final int TC_LIMIT = 50;

  ////////////////////////////////////////////////////////////////
  // instance variables
  private Vector _v;

  ////////////////////////////////////////////////////////////////
  // constructors
  
  public Set() { _v = new Vector(); }
  public Set(int n) { _v = new Vector(n); }
  public Set(Object o1) { _v = new Vector(); addElement(o1); }

  public void addElement(Object o) { if (!contains(o)) _v.addElement(o); }
  public void addAllElements(Enumeration enum) {
    while (enum.hasMoreElements()) {
      addElement(enum.nextElement());
    }
  }
  public void addAllElementsSuchThat(Enumeration enum, Predicate p) {
    if (p instanceof PredicateTrue) addAllElements(enum);
    else 
      while (enum.hasMoreElements()) {
	Object e = enum.nextElement();
	if (p.predicate(e)) addElement(e);
      }
  }  
  public void addAllElements(Set s) { addAllElements(s.elements()); }
  public void addAllElementsSuchThat(Set s, Predicate p) {
    addAllElementsSuchThat(s.elements(), p);
  }

  public void remove(Object o) { _v.removeElement(o); }
  public void removeElement(Object o) { _v.removeElement(o); }
  public void removeAllElements() { _v.removeAllElements(); }
  public boolean contains(Object o) { return _v.contains(o); }
  public boolean containsSuchThat(Predicate p) {
    return findSuchThat(p) != null;
  }

  /** return the first element that causes p.predicate() to return
   * true. */
  public Object findSuchThat(Predicate p) {
    Enumeration elts = elements();
    while (elts.hasMoreElements()) {
      Object o = elts.nextElement();
      if (p.predicate(o)) return o;
    }
    return null;
  }

  public Enumeration elements() { return _v.elements(); }

  public Vector asVector() { return _v; }

  public boolean equals(Object o) {
    if (!(o instanceof Set)) return false;
    Set set = (Set) o;
    if (set.size() != size()) return false;
    Enumeration myEs = elements();
    while (myEs.hasMoreElements()) {
      Object obj = myEs.nextElement();
      if (!(set.contains(obj))) return false;
    }
    return true;
  }


  public Object firstElement() { return _v.firstElement(); }
						       
  public int size() { return _v.size(); }
  public String toString() {
    String res = "Set{";
    Enumeration eles = elements();
    while (eles.hasMoreElements()) {
      res += eles.nextElement().toString();
      if (eles.hasMoreElements()) res += ", ";
    }
    return res + "}";
  }

  /** Reply the Set of all objects that can be reached from the
   * receiving Set by taking steps defined by the given
   * ChildGenerator.  The result includes the elements of the original
   * Set. In order to avoid very deep searches which are often
   * programming mistakes, only paths of length TC_LIMIT or less are
   * considered. */
  public Set transitiveClosure(ChildGenerator cg) {
    return transitiveClosure(cg, TC_LIMIT, PredicateTrue.theInstance());
  }

  /** Reply the Set of all objects that can be reached from the
   * receiving Set by taking steps defined by the given
   * ChildGenerator.  The result DOES NOT include the elements of the
   * original Set. In order to avoid very deep searches which are
   * often programming mistakes, only paths of length TC_LIMIT or less
   * are considered.*/
  public Set reachable(ChildGenerator cg) {
    return reachable(cg, TC_LIMIT, PredicateTrue.theInstance());
  }
  
  /** Reply the Set of all objects that can be reached from the
   * receiving Set by taking steps defined by the given
   * ChildGenerator.  The result DOES NOT include the elements of the
   * original Set. In order to avoid very deep searches which are
   * often programming mistakes, only paths of given max length or
   * less are considered. Only paths consisting of elements which all
   * cause p.predicate() to return true are considered. */
  public Set reachable(ChildGenerator cg, int max, Predicate p) {
    Set kids = new Set();
    Enumeration rootEnum = elements();
    while (rootEnum.hasMoreElements()) {
      Object r = rootEnum.nextElement();
      kids.addAllElementsSuchThat(cg.gen(r), p);
    }
    return kids.transitiveClosure(cg, max, p);
  }
  
  /** Reply the Set of all objects that can be reached from the
   * receiving Set by taking steps defined by the given
   * ChildGenerator.  The result includes the elements of the original
   * Set. In order to avoid very deep searches which are often
   * programming mistakes, only paths of given max length or less are
   * considered. Only paths consisting of elements which all cause
   * p.predicate() to return true are considered. */
  public Set transitiveClosure(ChildGenerator cg, int max, Predicate p) {
    int iterCount = 0;
    int lastSize = -1;
    Set touched = new Set();
    Set frontier, recent = this;

    touched.addAllElements(this);
    while ((iterCount < max) && (touched.size() > lastSize)) {
      iterCount++;
      lastSize = touched.size();
      frontier = new Set();
      Enumeration recentEnum = recent.elements();
      while (recentEnum.hasMoreElements()) {
	Enumeration frontsEnum = cg.gen(recentEnum.nextElement());
	frontier.addAllElementsSuchThat(frontsEnum, p);
      }
      touched.addAllElements(frontier);
      recent = frontier;
    }
    return touched;
  }

} /* end class Set */
