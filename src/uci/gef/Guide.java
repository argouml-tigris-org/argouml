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

// File: Guide.java
// Classes: Guide
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;

/** Constrains interactions to certain coordinates. For example
 *  GuideGrid makes objects snap to a grid. Other subclasses might
 *  implement other snapping rules, for example, a polar grid or
 *  gravity (objects cling to other objects when they get close).
 *
 * @see GuideGrid */

public abstract class Guide implements java.io.Serializable {
  //implements GEF {

  ////////////////////////////////////////////////////////////////
  // geometric constraints

  /** Return a NEW Point that is close to p and on the guideline
   *  (e.g., gridline). */
  public final Point snapTo(Point p) {
    Point res = new Point(p.x, p.y);
    snap(res);
    return res;
  }

  /** Modify the given point to satisfy guide conditions (e.g. be on a
   *  gridline). */
  public abstract void snap(Point p);

  ////////////////////////////////////////////////////////////////
  // user interface

  /** Bring up a dialog box to set the parameters for this
   *  guide. E.g., set the size of a grid. */
  public void adjust() { };
}
