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

// File: SamplePalette.java
// Classes: SamplePalette
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.visual.demo;

import java.awt.*;
import java.util.*;
import uci.gef.*;

/** A class to define the left hand column of buttons in the Example
 * application. Right now it just has one kind of node.
 *
 * @see uci.gef.demo.FlexibleApplet */

public class SamplePalette extends Palette {

  /** The class for the NetNode's to make when the user clicks the
   * SampleNode button. */
  static final String SampNode1CLASS = "uci.gef.demo.SampleNode";
  static final String SampNode2CLASS = "uci.gef.FigText";
  static final String SampNode3CLASS = "uci.gef.demo.SampleNode3";

  /** Construct a new palette of example nodes for the Example application */
  public SamplePalette() { super(2, 1, "Example"); }

  /** Define a button to make for the Example application */
  public Vector defineButtons() {
    Vector v = new Vector();
    Action addSampNode1 = new ActionCreateNode(SampNode1CLASS, null, false);
    v.addElement(new ActiveButton("Node One", addSampNode1));
    Action addSampNode2 = new ActionCreateNode(SampNode2CLASS, null, false);
    v.addElement(new ActiveButton("FigT", addSampNode2));
    return v;
  }

} /* end class SamplePalette */
