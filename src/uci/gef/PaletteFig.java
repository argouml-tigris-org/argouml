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
import uci.gef.contrib.*;

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

public class PaletteFig extends Palette {

  /** The Editor Mode to enter when the user clicks on one of the
   * above buttons. Clicking on a button sets the next global Mode in
   * Globals. The next time an Editor gets a mouse entered event it
   * will set its mode to the stored next global Mode. */
  final String pack = "uci.gef.";
  final String ModeCreateFigTextCLASS = pack + "ModeCreateFigText";
  final String ModeCreateFigLineCLASS = pack + "ModeCreateFigLine";
  final String ModeCreateFigRectCLASS = pack + "ModeCreateFigRect";
  final String ModeCreateFigRRectCLASS = pack + "ModeCreateFigRRect";
  final String ModeSelectCLASS = pack + "ModeSelect";
  final String ModeCreateFigCircleCLASS = pack + "ModeCreateFigCircle";
  final String ModeCreateFigPolyCLASS = pack + "ModeCreateFigPoly";
  final String ModeCreateFigInkCLASS = pack + "ModeCreateFigInk";
  final String ModeCreateFigImageCLASS = pack + "ModeCreateFigImage";

  /** Construct a new PaletteFig and set the name of the Frame to
   * "Shape" (if applicable). */

  public PaletteFig() { super(1, 1, "Figs"); }

  /** Defined the buttons in this palette. Each of these buttons is
   * associated with an ActionSetMode, and that Action sets the next
   * global Mode to somethign appropriate. All the buttons can stick
   * except 'select'. If the user unclicks the sticky checkbox, the
   * 'select' button is automatically pressed. */
  public Vector defineButtons() {
    Vector v = new Vector();
    Action setSelect = new ActionSetMode(ModeSelectCLASS);
    Action addCircle = new ActionSetMode(ModeCreateFigCircleCLASS);
    Action addRect = new ActionSetMode(ModeCreateFigRectCLASS);
    Action addRRect = new ActionSetMode(ModeCreateFigRRectCLASS);
    Action addLine = new ActionSetMode(ModeCreateFigLineCLASS);
    Action addText = new ActionSetMode(ModeCreateFigTextCLASS);
    Action addPoly = new ActionSetMode(ModeCreateFigPolyCLASS);
    Action addInk = new ActionSetMode(ModeCreateFigInkCLASS);
    Action addImage1 =
      new ActionSetMode(ModeCreateFigImageCLASS,
			"imageURL",
			"http://www.ics.uci.edu/~jrobbins/images/new.gif");
    Action addImage2 =
      new ActionSetMode(ModeCreateFigImageCLASS,
			"imageURL",
			"http://www.ics.uci.edu/~jrobbins/images/gef_banner.gif");

    ActiveButton selectButton = new ActiveButton("Select", setSelect);
    ActiveButton circleButton = new ActiveButton("Circle", addCircle);
    ActiveButton rectButton = new ActiveButton("Rect", addRect);
    ActiveButton rRectButton = new ActiveButton("RoundRect", addRRect);
    ActiveButton lineButton = new ActiveButton("Line", addLine);
    ActiveButton textButton = new ActiveButton("Text", addText);
    ActiveButton polyButton = new ActiveButton("Polygon", addPoly);
    ActiveButton inkButton = new ActiveButton("Ink", addInk);
    ActiveButton image1Button = new ActiveButton("Image1", addImage1);
    ActiveButton image2Button = new ActiveButton("Image2", addImage2);

    circleButton.canStick(true);
    rectButton.canStick(true);
    rRectButton.canStick(true);
    lineButton.canStick(true);
    textButton.canStick(true);
    polyButton.canStick(true);
    inkButton.canStick(true);
    image1Button.canStick(true);
    image2Button.canStick(true);

    v.addElement(selectButton);
    v.addElement(circleButton);
    v.addElement(rectButton);
    v.addElement(rRectButton);
    v.addElement(lineButton);
    v.addElement(polyButton);
    v.addElement(inkButton);
    v.addElement(textButton);
    if (Globals.getAppletContext() != null) {
      v.addElement(image1Button);
      v.addElement(image2Button);
    }

    return v;
  }

} /* end class PaletteFig */



