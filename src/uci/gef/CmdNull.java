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

// File: CmdNull.java
// Classes: CmdNull
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;

/** Cmd to do nothing.  This makes some other code simpler.  For
 *  example, Mode#keybinding can return a "real" Cmd if there is one
 *  bound to the given key, and an instance of CmdNull if there is
 *  not.  The alternative would be to return null and force the caller
 *  to check for null. */

public class CmdNull extends Cmd {

  public CmdNull() { super("Do nothing"); }

  /** Do nothing */
  public void doIt() { }

  /** This is the only undo method that works :) */
  public void undoIt() { }

} /* end class CmdNull */
