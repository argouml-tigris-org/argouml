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




// File: PaletteFig.java
// Classes: PaletteFig
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/** A Palette that defines buttons to create lines, rectangles,
 *  rounded rectangles, circles, and text. Also a select button is
 *  provided to switch back to ModeSelect.
 *
 *  Needs-more-work: sticky mode buttons are not supported right
 *  now. They should be in the next release.
 *
 * @see ModeSelect
 * @see ModeCreateFigLine
 * @see ModeCreateFigRect
 * @see ModeCreateFigRRect
 * @see ModeCreateFigCircle
 * @see ModeCreateFigText
 * @see ModeCreateFigPoly */

public class PaletteFig extends uci.ui.ToolBar {

  public PaletteFig() { defineButtons(); }

  
  /** Defined the buttons in this palette. Each of these buttons is
   *  associated with an CmdSetMode, and that Cmd sets the next
   *  global Mode to somethign appropriate. All the buttons can stick
   *  except 'select'. If the user unclicks the sticky checkbox, the
   *  'select' button is automatically pressed. */
  public void defineButtons() {
    add(new CmdSetMode(ModeSelect.class, "Select"));
    add(new CmdSetMode(ModeBroom.class, "Broom"));
    addSeparator();
    add(new CmdSetMode(ModeCreateFigCircle.class, "Circle"));
    add(new CmdSetMode(ModeCreateFigRect.class, "Rectangle"));
    add(new CmdSetMode(ModeCreateFigRRect.class, "RRect"));
    add(new CmdSetMode(ModeCreateFigLine.class, "Line"));
    add(new CmdSetMode(ModeCreateFigText.class, "Text"));
    add(new CmdSetMode(ModeCreateFigPoly.class, "Polygon"));
    add(new CmdSetMode(ModeCreateFigSpline.class, "Spline"));
    add(new CmdSetMode(ModeCreateFigInk.class, "Ink"));

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


  static final long serialVersionUID = 3484460904733989358L;
} /* end class PaletteFig */



