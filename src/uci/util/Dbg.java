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

// File: Dbg.java
// Class: Dbg
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.util;

/** This is a useful utility for debugging. In various places in the
 *  framework there are calls to Dbg.log(). If the proper -D
 *  defines are supplied when running the application those debugging
 *  statements will be enabled or disabled. */

public class Dbg {

  public final static boolean on = true;

  public static final void assert(boolean cond, String msg) {
    if (on && !cond) throw new AssertionException(msg);
  }

  /** Print the given message if the guard was defined with a command
   *  line -D option. */
  public static final void log(String guard, String msg) { 
    if (Boolean.getBoolean(guard)) System.out.println(msg);
  }

  /** Print the given message if the guard was defined with a command
   *  line -D option and the given condition evaluates to true. */
  public static final void log(String guard, boolean cond, String msg) {
    if (Boolean.getBoolean(guard) && cond) System.out.println(msg);
  }

} /* end class Dbg */



class AssertionException extends Error {
  public AssertionException(String s) { super(s); }
}

