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

// File: PaletteAttr.java
// Classes: PaletteAttr
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** This class defines a palette of attribute pop up menus that affect
 *  the Fig's selected in the Global current Editor. This
 *  palette also has buttons for sent-to-back, etc.  Needs-More-Work:
 *  This code is not currently exercised by the Example application,
 *  but it should be.*/

public class PaletteAttr extends Palette {

  /** Construct a new PaletteAttr in a Frame named 'Attributes" (if
   * appropriate) */
  public PaletteAttr() {
    super(2, 1, "Attributes");
  }

  /** Define the ActiveComponents for the menus and buttons in this
   * palette.  Needs-More-Work: this code would be way shorter if each
   * action choice had a graphAttr string associated with it and I
   * could just add values with optional labels (if labels are not
   * given, value.toString() would be used). */
  public Vector defineButtons() {
    Vector v = new Vector();

    Action setLineColorRed = new ActionSetAttr("LineColor", Color.red);
    Action setLineColorBlue = new ActionSetAttr("LineColor", Color.blue);
    Action setLineColorBlack = new ActionSetAttr("LineColor", Color.black);
    Action setLineColorWhite = new ActionSetAttr("LineColor", Color.white);

    Action setFillColorRed = new ActionSetAttr("FillColor", Color.red);
    Action setFillColorBlue = new ActionSetAttr("FillColor", Color.blue);
    Action setFillColorBlack = new ActionSetAttr("FillColor", Color.black);
    Action setFillColorWhite = new ActionSetAttr("FillColor", Color.white);

    Action setFontSize10 = new ActionSetAttr("FontSize", new Integer(10));
    Action setFontSize12 = new ActionSetAttr("FontSize", new Integer(12));
    Action setFontSize18 = new ActionSetAttr("FontSize", new Integer(18));
    Action setFontSize24 = new ActionSetAttr("FontSize", new Integer(24));
    Action setFontSize48 = new ActionSetAttr("FontSize", new Integer(48));

    Action setAlignLeft = new ActionSetAttr("Alignment", "Left");
    Action setAlignCenter = new ActionSetAttr("Alignment", "Center");
    Action setAlignRight = new ActionSetAttr("Alignment", "Right");

    Action setFontTimes = new ActionSetAttr("FontName", "TimesRoman");
    Action setFontHelvetica = new ActionSetAttr("FontName", "Helvetica");
    Action setFontCourier = new ActionSetAttr("FontName", "Courier");
    Action setFontDialog = new ActionSetAttr("FontName", "Dialog");
    Action setFontDialogInput = new ActionSetAttr("FontName", "DialogInput");
    Action setFontZapfDingbats = new ActionSetAttr("FontName", "ZapfDingbats");

    ActiveChoice lineColors = new ActiveChoice();
    lineColors.addItem("Red", setLineColorRed);
    lineColors.addItem("Blue", setLineColorBlue);
    lineColors.addItem("Black", setLineColorBlack);
    lineColors.addItem("White", setLineColorWhite);
    v.addElement(lineColors);

    ActiveChoice fillColors = new ActiveChoice();
    fillColors.addItem("Red", setFillColorRed);
    fillColors.addItem("Blue", setFillColorBlue);
    fillColors.addItem("Black", setFillColorBlack);
    fillColors.addItem("White", setFillColorWhite);
    v.addElement(fillColors);

    ActiveChoice textFonts = new ActiveChoice();
    textFonts.addItem("TimesRoman", setFontTimes);
    textFonts.addItem("Helvetica",  setFontHelvetica);
    textFonts.addItem("Courier",  setFontCourier);
    textFonts.addItem("Dialog",  setFontDialog);
    textFonts.addItem("DialogInput",  setFontDialogInput);
    textFonts.addItem("ZapfDingbats",  setFontZapfDingbats);
    v.addElement(textFonts);

    ActiveChoice fontSizes = new ActiveChoice();
    fontSizes.addItem("10", setFontSize10);
    fontSizes.addItem("12", setFontSize12);
    fontSizes.addItem("18", setFontSize18);
    fontSizes.addItem("24", setFontSize24);
    fontSizes.addItem("48", setFontSize48);
    v.addElement(fontSizes);

    ActiveChoice textAligns = new ActiveChoice();
    textAligns.addItem("Left", setAlignLeft);
    textAligns.addItem("Center", setAlignCenter);
    textAligns.addItem("Right", setAlignRight);
    v.addElement(textAligns);

    Action sendToBackAct = new ActionReorder(ActionReorder.SEND_TO_BACK);
    v.addElement(new ActiveButton("SendToBack", sendToBackAct));
    Action bringToFrontAct = new ActionReorder(ActionReorder.BRING_TO_FRONT);
    v.addElement(new ActiveButton("BringToFront", bringToFrontAct));
    Action sendBackwardAct = new ActionReorder(ActionReorder.SEND_BACKWARD);
    v.addElement(new ActiveButton("SendBackward", sendBackwardAct));
    Action bringForwardAct = new ActionReorder(ActionReorder.BRING_FORWARD);
    v.addElement(new ActiveButton("BringForward", bringForwardAct));

    return v;
  }

} /* end class PaletteAttr */
