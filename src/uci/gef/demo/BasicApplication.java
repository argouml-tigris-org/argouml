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
import com.sun.java.swing.*;
import java.lang.*;
import uci.gef.*;
import uci.graph.*;

/** A simple example of the minimum code needed to build an
 *  application using GEF. */

public class BasicApplication {

  ////////////////////////////////////////////////////////////////
  // instance variables

  private JGraphFrame _gf;
  private Frame _palFrame;

  ////////////////////////////////////////////////////////////////
  // constructors

  public BasicApplication() {
    _gf = new JGraphFrame();
    _gf.reshape(10, 10, 200, 200);
    _gf.show();
    //_gf.setToolBar(new SamplePalette()); //needs-more-work 
  }

  ////////////////////////////////////////////////////////////////
  // main

  public static void main(String args[]) {
    BasicApplication demo = new BasicApplication();
  }

} /* end class BasicApplication */

