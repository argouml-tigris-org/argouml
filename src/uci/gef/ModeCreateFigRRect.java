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

// File: ModeCreateFigRRect.java
// Classes: ModeCreateFigRRect
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.event.MouseEvent;

/** A Mode to interpert user input while creating a FigRRect. All of
 *  the actual event handling is inherited from ModeCreate. This class
 *  just implements the differences needed to make it specific to
 *  RRects.
 *  <A HREF="../features.html#basic_shapes_rounded_rect">
 *  <TT>FEATURE: basic_shapes_rounded_rect</TT></A>
 */

public class ModeCreateFigRRect extends ModeCreate {
  ////////////////////////////////////////////////////////////////
  // Mode API

  public String instructions() {
    return "Drag to define a rounded rectangle";
  }

  ////////////////////////////////////////////////////////////////
  // ModeCreate API

  /** Create a new FigRect instance based on the given mouse down
   *  event and the state of the parent Editor. */
  public Fig createNewItem(MouseEvent me, int snapX, int snapY) {
    return new FigRRect(snapX, snapY, 0, 0, _editor.graphAttrs());
  }

} /* end class ModeCreateFigRRect */

