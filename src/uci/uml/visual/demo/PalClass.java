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

// File: PalClass.java
// Classes: PalClass
// Original Author: ics125b spring 1996
// $Id$

package uci.uml.visual.demo;

import java.awt.*;
import java.util.*;
import uci.gef.*;
import uci.uml.*;

/** A Palette that defines buttons to create lines, rectangles,
 *  rounded rectangles, circles, and text. Also a select button is
 *  provided to switch back to ModeSelect.
 *
 * @see ModeCreateFigRect */

public class PalClass extends Palette {

  /** The Editor Mode to enter when the user clicks on one of the
   * above buttons. Clicking on a button sets the next global Mode in
   * Globals. The next time an Editor gets a mouse entered event it
   * will set its mode to the stored next global Mode. */
  final String ModeCreateFigRectCLASS = "uci.gef.ModeCreateFigRect";
  final String ModeCreateFigClassCLASS = "uci.uml.visual.ModeCreateFigClass";
  final String ModeCreateFigNodeCLASS = "uci.uml.visual.ActionCreateNode";

  /** Construct a new PalClass and set the name of the Frame to
   * "Shape" (if applicable). */

  public PalClass() { super(1, 1, "Figs"); }

  /** Defined the buttons in this palette. Each of these buttons is
   * associated with an ActionSetMode, and that Action sets the next
   * global Mode to somethign appropriate. All the buttons can stick
   * except 'select'. If the user unclicks the sticky checkbox, the
   * 'select' button is automatically pressed. */
  public Vector defineButtons() {
    Vector v = new Vector();
    Action addRect = new ActionSetMode(ModeCreateFigRectCLASS);
    Action addClass = new ActionSetMode(ModeCreateFigClassCLASS);
    Action addNode = new ActionCreateNode("uci.uml.visual.demo.ClassNode", null, false);

    ActiveButton rectButton = new ActiveButton("Rect", addRect);
    ActiveButton classButton = new ActiveButton("Class", addClass);
    ActiveButton nodeButton = new ActiveButton("Node", addNode);

    rectButton.canStick(true);
    classButton.canStick(true);
    nodeButton.canStick(true);

    v.addElement(rectButton);
    v.addElement(classButton);
    v.addElement(nodeButton);

    return v;
  }

} /* end class PalClass */
