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

// File: FlexibleApplet.java
// Classes: FlexibleApplet
// Original Author: ics125b spring 1996
// $Id$

package uci.gef.demo;

import java.applet.*;
import java.awt.*;
import java.util.*;
import uci.util.*;
import uci.gef.*;

/** An example application to show off <B>some</B> of the capabilities
 *  of the UCI Graph Editing Framework. This example is a basic drawing
 *  editor that allows the user to place shapes in a drawing area and
 *  minipulate them. Also it uses LayerGrid. It has an Arrange menu
 *  that allows the user to so some manipulations, and an Attributes
 *  menu that allows the user to set fonts and colors. It registers
 *  some actions for execution from the ExecuteActionWindow. <p>
 *
 *  For another example see Argo. Argo (in its current form) is a
 *  Design Environment for C2 style software architectures. An Argo
 *  demo can be found
 *  <a href="http://www.ics.uci.edu/pub/arch/argo/">here</a>.<p>
 *
 *  Needs-More-Work: I need additional examples to show off the power
 *  of the framework and motivate its design. For example: a KISS
 *  viewer(?), a computer network editor, a class hierarchy editor (UML
 *  or OMT), etc.
 *
 * @see Editor
 * @see Palette
 * @see Action
 * @see Mode
 * @see Layer
 * @see DiagramElement
 * @see NetPrimitive
 * @see jargo.softarch.klax.Main
 */

public class FlexibleApplet extends Applet {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected static boolean _spawnFrame = true;
  protected static boolean _spawnPalette = true;
  protected static int _drawAreaWidth = 400;
  protected static int _drawAreaHeight = 300;
  protected static String _loadDocument;

  /** The Editor window */
  private Editor ed;

  /** The net-level model to edit */
  private NetList net;

  /** The example palette, shows SampleNode */
  Palette palette;

  /** The palette of shapes and selection tool */
  Palette shapePalette;

  /** The palette of attributes for shapes. Needed because applets
  /** cannot have menus when embedded in the browser page. */
  Palette attrPalette;

  /** The overall palette window */
  Palette masterPalette;

  /** The overall palette window */
  PaletteTop topPalette;

  ////////////////////////////////////////////////////////////////
  // constructor

  /** Construct a new FlexibleApplet */
  public FlexibleApplet() {
    System.out.println("making an example");
    net = new NetList();
    net.name("Sample Network");
    palette = new SamplePalette();
    shapePalette = new PaletteFig();
    attrPalette = new PaletteAttr();
  }


  ////////////////////////////////////////////////////////////////
  // main

  /** If this is run as an application, basically do the same thing as
   *  the applet */
  public static void main(String[] args) {
    FlexibleApplet demo = new FlexibleApplet();
    demo.init();
  }

  ////////////////////////////////////////////////////////////////
  // argument and parameter methods

  public static void parseParams(Applet applet) {
    _drawAreaWidth = 400;
    _drawAreaHeight = 300;
    _spawnFrame = "true".equals(applet.getParameter("SpawnFrame"));
    _spawnPalette = "true".equals(applet.getParameter("SpawnPalette"));
    String widthStr = applet.getParameter("DrawAreaWidth");
    if (widthStr != null) _drawAreaWidth = Integer.parseInt(widthStr);
    String heightStr = applet.getParameter("DrawAreaHeight");
    if (heightStr != null) _drawAreaHeight = Integer.parseInt(heightStr);
    _loadDocument = applet.getParameter("LoadDocument");
    System.out.println("SpawnFrame= " + _spawnFrame);
    System.out.println("SpawnPalette= " + _spawnPalette);
    System.out.println("DrawArea= " + _drawAreaWidth + " by " +
		       _drawAreaHeight);
    System.out.println("LoadDocument= " + _loadDocument);
  }

  public void setupWindows() {
    Component awt_comp;
    Dimension drawAreaSize = new Dimension(_drawAreaWidth, _drawAreaHeight);
    awt_comp = new ForwardingPanel(drawAreaSize);
    ed = new Editor(net, awt_comp);
    ((ForwardingComponent)awt_comp).setEventHandler(ed);
    if (_spawnFrame) {
      System.out.println("spawning frame");
      Frame drawingFrame = new ForwardingFrame(ed, drawAreaSize);
      ed.frame(drawingFrame);
      drawingFrame.add("Center", awt_comp);
      drawingFrame.pack();
      drawingFrame.move(10, 10);
      drawingFrame.show();
    }
    else add("Center", awt_comp);
    Vector pals = new Vector();
    pals.addElement(palette);
    pals.addElement(shapePalette);
    if (!_spawnPalette) pals.addElement(attrPalette);
    masterPalette = new PaletteSticky(new PaletteCompound(pals));
    topPalette = new PaletteTop(masterPalette);
    topPalette.definePanel();
    if (_spawnPalette) {
      System.out.println("spawning palette");
      Frame paletteFrame = new Frame();
      paletteFrame.setTitle("Palette");
      topPalette.frame(paletteFrame);
      paletteFrame.add("Center", topPalette);
      paletteFrame.pack();
      paletteFrame.move(10 + drawAreaSize.width, 10);
      paletteFrame.show();
    }
    else add("East", topPalette);
  }

  ////////////////////////////////////////////////////////////////
  // applet related methods

  /** Part of the required Applet API. Does nothing. */
  public void start() {  }

  /** This is called when the Applet/Application starts up. It does
   *  preloading if the 'ShouldPreLoad' property is set to true. It
   *  also registers some well known Action's as an example. */
  public void init() {
    Globals.setApplet(this);
    parseParams(this);
    setupWindows();
    Action.register(new ActionSave());
    Action.register(new ActionLoad());
    Action.register(new ActionDispose());
    Action.register(new ActionDelete());
    Action.register(new ActionSpawn());
    Action.register(new ActionAdjustGrid());
    Action.register(new ActionEditNode());
    Action.register(new ActionCreateNode("uci.gef.demo.SampleNode",
					 null, false));
    Action.register(new ActionQuit());
  }

  /** Part of the required Applet API. Does nothing. */
  public void stop() { }

  /** When the user closes this window try to free up as many objects
   *  as possible. */
  public void destroy() {
    if (null != topPalette) topPalette.close();
    if (null != ed) ed.close();
    masterPalette = null;
    topPalette = null;
    ed = null;
    net = null;
    palette = null;
    shapePalette = null;
    attrPalette = null;
  }

  /** Reply a breif string that describes this applet in the "About"
   *  box. */
  public String getAppletInfo() {
    return "GEF (the Graph Editing Framework) example editor applet. \n" +
      "FlexibleApplet a very simple demonstration of how GEF can \n" +
      "be used. " + "\n\n" +
      "Author: Jason Robbins\n" +
      "Copyright (c) 1995, 1996 Regents of the University of California.\n"+
      "All rights reserved.\n\n";
  }

  /** Reply an array of strings that describe the HTML PARAM's that
   *  can be passed to this applet. */
  public String[][] getParameterInfo() {
    String[][] info = new String[5][3];
    //
    info[0][0] = "LoadDocument";
    info[0][1] = "java.lang.String\n";
    info[0][2] = "If supplied, automatically load the given URL.\n\n";
    //
    info[1][0] = "SpawnFrame";
    info[1][1] = "boolean\n";
    info[1][2] = "Open a new Frame for the drawing area.\n\n";
    //
    info[2][0] = "SpawnPalette";
    info[2][1] = "boolean\n";
    info[2][2] = "Open a new Frame for the palette.\n\n";
    //
    info[3][0] = "DrawAreaWidth";
    info[3][1] = "int\n";
    info[3][2] = "Width of the drawing area in pixels.\n\n";
    //
    info[4][0] = "DrawAreaHeight";
    info[4][1] = "int\n";
    info[4][2] = "Height of the drawing area in pixels.\n\n";
    //
    return info;
  }

} /* end class FlexibleApplet */
