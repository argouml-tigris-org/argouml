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

// File: Predicate.java
// Classes: Predicate
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.util;

/** Interface for objects that act as predicate functions.  For
  * example, if you want to find an object in a Set that fits a
  * certain condition, then write a (anonymous?) class that
  * implements predicate and use it in Set.findSuchThat(). */

public interface Predicate extends java.io.Serializable {

  public boolean predicate(Object obj);

} /* end interface Predicate */
