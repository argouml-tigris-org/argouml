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

// File: Handle.java
// Classes: Handle
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.net.*;

/** This class stores the index of the handle that the user is dragging. I
 *  originally used a simple int, but some dragHandle() methods need to change
 *  the index because new handles can be added during a drag.
 *
 * @see FigPoly#moveVertex.  */

public class Handle {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Index of the handle on some Fig that was clicked on. */
  public int index;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Make a new Handle with the given handle index. */
  public Handle(int ind) { index = ind; }

} /* end class Handle */
