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

// File: CompoundPalette.java
// Classes: CompoundPalette
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** This class implements a Palette that contains other, nested
 *  Palette's.  */

public class PaletteCompound extends Palette {

  /** A collection of Palette's contained in this PaletteCompound */
  private Vector _subpalettes;

  /** Construct a new PaletteCompound with the given subpalettes and title */
  public PaletteCompound(Vector subs, String title) {
    _subpalettes = subs;
    initialize(subs.size(), 1);
  }

  /** Same as above constructor, but use r rows and c columns */
  public PaletteCompound(Vector subs, int r, int c, String title) {
    _subpalettes = subs;
    initialize(r, c);
  }

  /** Construct a PaletteCompound from a colection of subpalettes */
  public PaletteCompound(Vector subs) {
    _subpalettes = subs;
    initialize(subs.size(), 1);
  }

  /** Defining the components of a PaletteCompound is accomplished by
   * accumulating all the components of its subpalettes. */
  public Vector defineButtons() {
    Vector buts = new Vector();
    Enumeration cur = _subpalettes.elements();
    while (cur.hasMoreElements()) {
      Vector palButs = ((Palette)cur.nextElement()).activeComps();
      if (palButs == null) System.out.println(this.toString());
      Enumeration curBut = palButs.elements();
      while (curBut.hasMoreElements()) {
	buts.addElement(curBut.nextElement());
      }
    }
    return buts;
  }

  /** The panel of a PaletteCompound is made up of a FlowLayout of all
   * the panels of its subpalettes */
  public void definePanel() {

    rows(Math.max(rows(), activeComps().size() / cols()));
    setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    Enumeration cur = _subpalettes.elements();
    while (cur.hasMoreElements()) {
      Palette pal = (Palette) cur.nextElement();
      pal.definePanel();
      add(pal);
    }
  }

} /* end class PaletteCompound */
