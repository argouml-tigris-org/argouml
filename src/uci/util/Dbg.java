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

  // needs-more-work: upgrade to sun collections
  public final static VectorSet guards = new VectorSet();

  public static final void assert(boolean cond, String msg) {
    if (on && !cond) throw new AssertionException(msg);
  }

  /** Print the given message if the guard was defined with a command
   *  line -D option. */
  public static final void log(String guard, String msg) {
    if (Boolean.getBoolean(guard) || guards.contains(guard))
      System.out.println(msg);
  }

  public static final void log(String guard, Throwable t) {
    if (Boolean.getBoolean(guard) || guards.contains(guard))
      t.printStackTrace();
  }

  /** Print the given message if the guard was defined with a command
   *  line -D option and the given condition evaluates to true. */
  public static final void log(String guard, boolean cond, String msg) {
    if ((Boolean.getBoolean(guard)  || guards.contains(guard)) && cond)
      System.out.println(msg);
  }

} /* end class Dbg */



class AssertionException extends Error {
  public AssertionException(String s) { super(s); }
}

