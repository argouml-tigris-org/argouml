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

// File: ChildGenerator.java
// Classes: ChildGenerator
// Original Author: jason robbins
// $Id$

package uci.util;

import java.util.*;

/** Interface used in Set to compute transitive closures.  This is
 *  basically a Functor interface for generating (expanding) the
 *  children of some object. */

public interface ChildGenerator extends java.io.Serializable {
  /** Reply a Enumeration of the children of the given Object */
  public Enumeration gen(Object o);
} /* end interface ChildGenerator */

