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

// File: PaletteFig.java
// Classes: PaletteFig
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;
import com.sun.java.swing.*;

/** A Palette that defines buttons to create lines, rectangles,
 *  rounded rectangles, circles, and text. Also a select button is
 *  provided to switch back to ModeSelect.
 *
 * @see ModeSelect
 * @see ModeCreateFigLine
 * @see ModeCreateFigRect
 * @see ModeCreateFigRRect
 * @see ModeCreateFigCircle
 * @see ModeCreateFigText
 * @see ModeCreateFigPoly */

public class PaletteFig extends uci.ui.ToolBar {

  
  
  /** Construct a new PaletteFig and set the name of the Frame to
   * "Shape" (if applicable). */

  public PaletteFig() {
    defineButtons();
  }

  /** Defined the buttons in this palette. Each of these buttons is
   * associated with an CmdSetMode, and that Cmd sets the next
   * global Mode to somethign appropriate. All the buttons can stick
   * except 'select'. If the user unclicks the sticky checkbox, the
   * 'select' button is automatically pressed. */
  public void defineButtons() {
    add(new CmdSetMode(ModeSelect.class, "Select"));
    addSeparator();
    add(new CmdCreateNode(uci.gef.demo.SampleNode.class, "Node One"));
    add(new CmdCreateNode(uci.gef.demo.SampleNode2.class, "Node Two"));
    addSeparator();
    add(new CmdSetMode(ModeCreateFigCircle.class, "Circle"));
    add(new CmdSetMode(ModeCreateFigRect.class, "Rectangle"));
    add(new CmdSetMode(ModeCreateFigRRect.class, "RRect"));
    add(new CmdSetMode(ModeCreateFigLine.class, "Line"));
    add(new CmdSetMode(ModeCreateFigText.class, "Text"));
    add(new CmdSetMode(ModeCreateFigPoly.class, "Polygon"));
    add(new CmdSetMode(ModeCreateFigInk.class, "Ink"));
    Cmd image1 = new CmdSetMode(ModeCreateFigImage.class,
				"imageURL",
				"http://www.ics.uci.edu/~jrobbins/images/"+
				"new.gif");
    image1.putValue(Action.NAME, "Image1");
    Cmd image2 = new CmdSetMode(ModeCreateFigImage.class,
				"imageURL",
				"http://www.ics.uci.edu/~jrobbins/images/"+
				"gef_banner.gif");
    image2.putValue(Action.NAME, "Image2");

    if (Globals.getAppletContext() != null) {
      add(image1);
      add(image2);
    }

//     circleButton.canStick(true);
//     rectButton.canStick(true);
//     rRectButton.canStick(true);
//     lineButton.canStick(true);
//     textButton.canStick(true);
//     polyButton.canStick(true);
//     inkButton.canStick(true);
//     image1Button.canStick(true);
//     image2Button.canStick(true);
  }

  
} /* end class PaletteFig */



