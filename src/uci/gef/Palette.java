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

// File: Palette.java
// Classes: Palette
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;

/** Abstract class with shared behavior for windows that provide a
 *  palette of diagram tools and materials. Palettes may be displayed
 *  in their own Frame, or they may simply define a Panel that is used
 *  as part of a PaletteCompound.
 *  <A HREF="../features.html#palettes">
 *  <TT>FEATURE: palettes</TT></A>
 */

public abstract class Palette extends Panel  {

  /** A collection of ActiveComponents displayed in this window */
  private Vector _activeComps;

  /** The size of the grid of of _activeComps */
  private int _rows, _cols;

  /** Construct a new Palette */
  public Palette() { }

  /** Construct a new Palette with its components arranged in r rows
   * and c columns. */
  public Palette(int r, int c) { initialize(r, c); }

  /** Same as previous constructor, but set the title of the palette */
  public Palette(int r, int c, String title) {
    initialize(r, c);
  }

  /** Set up the data structures used by Palette's */
  protected void initialize(int r, int c) {
    _rows = r;
    _cols = c;
    setFont(new Font("Helvetica", Font.PLAIN, 10));
  }

  /** Abstract method to define the components to be shown in a
   * Palette. This should probably be called defineActiveComponents,
   * because Palette's can have more than just buttons.
   *
   * @see PaletteFig#defineButtons
   * @see PaletteAttr#defineButtons */
  public abstract Vector defineButtons();

  /** Layout the Palette components in a grid */
  public void definePanel() {
    _rows = Math.max(_rows, activeComps().size() / _cols);
    setLayout(new GridLayout(_rows,_cols, 0, 0));
    Enumeration cur = activeComps().elements();
    while (cur.hasMoreElements()) {
      Object obj =  cur.nextElement();
      ActiveComponent ac = (ActiveComponent) obj;
      add(ac.component());
    }
    setFont(new Font("Helvetica", Font.PLAIN, 10));
  }

  /** Get and set methods */
  protected Vector activeComps() {
    if (_activeComps == null) _activeComps = defineButtons();
    return _activeComps;
  }

  public void rows(int r) { _rows = r; }
  public void cols(int c) { _cols = c; }
  public int rows() { return _rows; }
  public int cols() { return _cols; }

} /* end class Palette */

