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

// File: EquipmentApplet.java
// Classes: EquipmentApplet
// Original Author: ics125b spring 1996
// $Id$

package uci.gef.demo;

import java.applet.*;
import java.awt.*;
import java.util.*;
import uci.util.*;
import uci.gef.*;
import uci.graph.*;
import com.sun.java.swing.*;

/** An example application to show off <B>some</B> of the capabilities
 *  of the UCI Graph Editing Framework. This example is about
 *  connecting computers to printers with cable, and plugging those
 *  machines into the wall power.  It mainly shows off using Images,
 *  and defining a simple model with properties that can be edited in
 *  the property sheet.
 *
 * @see NodeCPU
 * @see NodePrinter
 * @see NodeWall
 * @see EdgePower
 * @see EdgeData
 */

public class EquipmentApplet extends Applet {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected static boolean _spawnFrame = true;
  protected static int _drawAreaWidth = 400;
  protected static int _drawAreaHeight = 300;

  /** The net-level model to edit */
  //private NetList net;
  private GraphModel gm;

  /** The example palette, shows Nodes for computer equipment */
  Palette palette;

  /** The palette of shapes and selection tool */
  Palette shapePalette;

  /** The overall palette window */
  Palette masterPalette;

  /** The overall palette window */
  PaletteTop topPalette;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new EquipmentApplet */
  public EquipmentApplet() {
    System.out.println("making an example");
    gm = new DefaultGraphModel();
    //net.name("Sample Network");

    //palette = new EquipmentPalette();
    //shapePalette = new PaletteFig();
  }

  ////////////////////////////////////////////////////////////////
  // main

  /** If this is run as an application, basically do the same thing as
   * the applet */
  public static void main(String[] args) {
    EquipmentApplet demo = new EquipmentApplet();
    demo.init();
  }

  ////////////////////////////////////////////////////////////////
  // argument and parameter methods

  public static void parseParams(Applet applet) {
    _drawAreaWidth = 400;
    _drawAreaHeight = 300;
  }

  public void setupWindows() {
    JGraphFrame jgf = new JGraphFrame(gm);
    Globals.setStatusBar(jgf);
    jgf.setTitle("EquipmentApplet");
    jgf.show();
    //
    Vector pals = new Vector();
    pals.addElement(palette);
    pals.addElement(shapePalette);
    masterPalette = new PaletteSticky(new PaletteCompound(pals));
    topPalette = new PaletteTop(masterPalette);
    topPalette.definePanel();
    Frame palFrame = new Frame();
    palFrame.setLayout(new BorderLayout());
    palFrame.add("Center", topPalette);
    palFrame.move(400, 10);
    palFrame.pack();
    palFrame.show();
  }

  ////////////////////////////////////////////////////////////////
  // applet related methods

  /** Part of the required Applet API. Does nothing. */
  public void start() {  }

  /** This is called when the Applet/Application starts up. It does
   * preloading if the 'ShouldPreLoad' property is set to true. It
   * also registers some well known Action's as an example. */
  public void init() {
    Globals.setApplet(this);
    parseParams(this);
    setupWindows();
  }

  /** Part of the required Applet API. Does nothing. */
  public void stop() { }

  /** When the user closes this window try to free up as many objects
   * as possible. */
  public void destroy() {
    if (null != topPalette) topPalette.close();
    masterPalette = null;
    topPalette = null;
    gm = null;
    palette = null;
    shapePalette = null;
  }

  /** reply a breif string that describes this applet in the "About"
   * box. */
  public String getAppletInfo() {
    return "GEF (the Graph Editing Framework) example editor applet. \n" +
      "EquipmentApplet a very simple demonstration of how GEF can \n" +
      "be used. " + "\n\n" +
      "Author: Jason Robbins\n" +
      "Copyright (c) 1995, 1996 Regents of the University of California.\n"+
      "All rights reserved.\n\n";
  }

} /* end class EquipmentApplet */
