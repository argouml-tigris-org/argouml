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




// File: ColorPickerGrid.java
// Classes: ColorPickerGrid
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.beans.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import java.beans.*;

/** A small window that allows the user to choose a color. This window
 *  is slave to a ColorEditor (a small colored tile that appears in
 *  a PropSheet). Whenever the user choose a color, this object
 *  notifies its master ColorEditor. */

public class ColorPickerGrid extends JPanel
implements ActionListener {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** An array printf tiny colored squares. */
  protected ColorTilePanel _tiles = new ColorTilePanel(36);
  /** The ColorEditor that is master to this slave window. */
  protected ColorEditor _peColor = null;
  /** PATTERN: singleton */
  protected static ColorPickerGrid _theInstance = null;
  /** Text to be displayed in the status bar area of this window. */
  protected JLabel _statusLabel = new JLabel("No color selected");

  ////////////////////////////////////////////////////////////////
  // constructors

  public ColorPickerGrid(Color orig) {
    super();
    //System.out.println("making a ColorPickerGrid");
    setLayout(new BorderLayout());
    _statusLabel.setFont(new Font("Courier", Font.PLAIN, 10));
    setBackground(new Color(12632256));
    add(_tiles, BorderLayout.CENTER);
    add(_statusLabel, BorderLayout.SOUTH);
    //System.out.println("setting color1");
    _tiles.setColor(orig);
    _tiles.addActionListener(this);
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


  public void actionPerformed(ActionEvent ae) {
    updateStatusLabel();
    if (_peColor != null) _peColor.setValue(_tiles.getColor());
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
//   public static void edit(ColorEditor pec, int x, int y) {
//     if (_theInstance == null) {
//       _theInstance = new ColorPickerGrid(Color.white);
//       _theInstance.move(x, y);
//       _theInstance.show();
//     }
//     else {
//       _theInstance.show();
//       //_theInstance.toFront();
//     }
//     _theInstance.setPEColor(pec);
//   }

//   /** The user has done something to hide my master ColorEditor,
//    * so don't allow any more changes. */
//   public static void stopEditing() {
//     if (_theInstance == null) return;
//     _theInstance.setPEColor(null);
//   }

//   public static void stopIfEditing(ColorEditor pec) {
//     if (_theInstance == null) return;
//     if (_theInstance._peColor != pec) return;
//     _theInstance.setPEColor(null);
//   }

//   /** If the window is shown and is slave to the given
//    * ColorEditor, set its selected color to that of the given
//    * ColorEditor. */
//   public static void updateIfEditing(ColorEditor pec) {
//     if (_theInstance == null) return;
//     if (_theInstance._peColor != pec) return;
//     _theInstance.setColor((Color)pec.getValue());
//   }

} /* end class ColorPickerGrid */
