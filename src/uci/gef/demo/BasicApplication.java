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

// File: BasicApplication.java
// Class: BasicApplication
// original author: jrobbins@ics.uci.edu
// $Id$

package uci.gef.demo;

import java.awt.*;
import java.lang.*;
import uci.gef.*;

/** A simple example of the minimum code needed to build an
 *  application using GEF. */

public class BasicApplication {

  ////////////////////////////////////////////////////////////////
  // instance variables

  private Editor _ed;
  private Frame _palFrame;
  private PaletteTop _pal;

  ////////////////////////////////////////////////////////////////
  // constructors

  public BasicApplication() {
    _ed = new Editor(new NetList());
    _ed.setTitle("Untitled");
    _ed.show();
    _palFrame = new Frame();
    _pal = new PaletteTop(new PaletteFig());
    _pal.frame(_palFrame);
    _pal.definePanel();
    _palFrame.setLayout(new BorderLayout());
    _palFrame.add("Center", _pal);
    _palFrame.pack();
    _palFrame.setTitle("Palette");
    _palFrame.move(410, 10);
    _palFrame.show();
  }

  ////////////////////////////////////////////////////////////////
  // main

  public static void main(String args[]) {
    BasicApplication demo = new BasicApplication();
  }

} /* end class BasicApplication */

