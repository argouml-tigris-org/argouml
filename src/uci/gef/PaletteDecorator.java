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

// File: PaletteDecorator.java
// Classes: PaletteDecorator
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** Abstract class with shared behavior for windows that provide a
 *  palette of diagram tools and materials. Palettes may be displayed
 *  in their own Frame, or they may simply define a Panel that is used
 *  as part of a PaletteCompound. */

public class PaletteDecorator extends Palette  {
  private Palette _subpalette;

  /** Construct a new Palette */
  public PaletteDecorator() { super(); }

  /** Construct a new Palette with its components arranged in r rows
   * and c columns. */
  public PaletteDecorator(Palette subpal) {
    super();
    _subpalette = subpal;
  }

  public Vector defineButtons() {
    // this should never be called
    // raise an exception here?
    return null;
  }

  public Vector activeComps() { return _subpalette.activeComps(); }

  public void definePanel() {
    setLayout(new BorderLayout());
    setBackground(Color.lightGray);
    _subpalette.definePanel();
    add("Center", _subpalette);
  }

} /* end class PaletteDecorator */
