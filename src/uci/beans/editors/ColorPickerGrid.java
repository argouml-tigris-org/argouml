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

// File: ColorPickerGrid.java
// Classes: ColorPickerGrid
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.beans.editors;

import java.awt.*;
//import java.beans.*;

/** A small window that allows the user to choose a color. This window
 *  is slave to a ColorEditor (a small colored tile that appears in
 *  a PropSheet). Whenever the user choose a color, this object
 *  notifies its master ColorEditor. */

public class ColorPickerGrid extends Frame {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** An array printf tiny colored squares. */
  protected ColorTilePanel _tiles = new ColorTilePanel(36);
  /** The ColorEditor that is master to this slave window. */
  protected ColorEditor _peColor = null;
  /** PATTERN: singleton */
  protected static ColorPickerGrid _theInstance = null;
  /** Text to be displayed in the status bar area of this window. */
  protected Label _statusLabel = new Label("No color selected");

  ////////////////////////////////////////////////////////////////
  // constructors

  public ColorPickerGrid(Color orig) {
    super();
    setLayout(new BorderLayout());
    _statusLabel.setFont(new Font("Courier", Font.PLAIN, 10));
    setBackground(new Color(12632256));
    add("Center", _tiles);
    add("South", _statusLabel);
    _tiles.setColor(orig);
    pack();
    setTitle("Color Picker");
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setPEColor(ColorEditor pec) {
    _peColor = pec;
    if (_peColor != null) {
      _tiles.allowSelection(true);
      setColor((Color)_peColor.getValue());
    }
    else {
      _tiles.allowSelection(false);
      updateStatusLabel();
    }
  }

  public void setColor(Color c) {
    _tiles.setColor(c);
    updateStatusLabel();
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  public boolean handleEvent(Event event) {
    if (event.id == Event.WINDOW_DESTROY) {
      hide();         // hide the Frame
      return true;
    }
    return super.handleEvent(event);
  }

  public boolean action(Event e, Object what) {
    updateStatusLabel();
    if (_peColor != null) _peColor.setValue(_tiles.getColor());
    return true;
  }

  public void updateStatusLabel() {
    if (_peColor == null) {
      _statusLabel.setText("No color selected");
      return;
    }
    Color c = _tiles.getColor();
    String r = Integer.toString(c.getRed(), 16).toUpperCase();
    String b = Integer.toString(c.getBlue(), 16).toUpperCase();
    String g = Integer.toString(c.getGreen(), 16).toUpperCase();
    if (c.getRed() == 0) r = "00";
    if (c.getBlue() == 0) b = "00";
    if (c.getGreen() == 0) g = "00";
    _statusLabel.setText("Red:" + r + " Blue:" + b + " Green:" + g);
  }

  /** Open the singleton instance a a slave to the given
   *  ColorEditor, at the screen coordinates given.
   *  <A HREF="../bugs.html#in_color_picker_buried>
   *  <FONT COLOR=660000><B>BUG: in_color_picker_buried</B></FONT></A>
   */
  public static void edit(ColorEditor pec, int x, int y) {
    if (_theInstance == null) {
      _theInstance = new ColorPickerGrid(Color.white);
      _theInstance.move(x, y);
      _theInstance.show();
    }
    else {
      _theInstance.show();
      _theInstance.toFront();
    }
    _theInstance.setPEColor(pec);
  }

  /** The user has done something to hide my master ColorEditor,
   * so don't allow any more changes. */
  public static void stopEditing() {
    if (_theInstance == null) return;
    _theInstance.setPEColor(null);
  }

  public static void stopIfEditing(ColorEditor pec) {
    if (_theInstance == null) return;
    if (_theInstance._peColor != pec) return;
    _theInstance.setPEColor(null);
  }

  /** If the window is shown and is slave to the given
   * ColorEditor, set its selected color to that of the given
   * ColorEditor. */
  public static void updateIfEditing(ColorEditor pec) {
    if (_theInstance == null) return;
    if (_theInstance._peColor != pec) return;
    _theInstance.setColor((Color)pec.getValue());
  }

} /* end class ColorPickerGrid */
