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

// File: PredicateTrue.java
// Classes: PredicateTrue
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.util;

/** A class that implements Predicate and always returns true.  This
 *  is useful when you are calling a method that accepts a Predicate to
 *  filter or select objects, but you just want all the objects, or the
 *  first object. */

public class PredicateTrue implements Predicate {

  public PredicateTrue() { }
  public boolean predicate(Object obj) { return true; }

  private static PredicateTrue _theInstance = new PredicateTrue();
  public static PredicateTrue theInstance() {
    return _theInstance;
  }

} /* end class PredicateTrue */
