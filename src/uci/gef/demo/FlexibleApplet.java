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




// File: FlexibleApplet.java
// Classes: FlexibleApplet
// Original Author: ics125b spring 1996
// $Id$

package uci.gef.demo;

import java.applet.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

import uci.util.*;
import uci.gef.*;
import uci.graph.*;

/** An example application to show off <B>some</B> of the capabilities
 *  of the UCI Graph Editing Framework. This example is a basic drawing
 *  editor that allows the user to place shapes in a drawing area and
 *  minipulate them. Also it uses LayerGrid. It has an Arrange menu
 *  that allows the user to so some manipulations, and an Attributes
 *  menu that allows the user to set fonts and colors. It registers
 *  some Cmds for execution from the ExecuteCmdWindow. <p>
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
 * @see PaletteFig
 * @see Cmd
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
  protected static int _drawAreaWidth = 400;
  protected static int _drawAreaHeight = 300;
  protected static String _loadDocument;

  /** The net-level model to edit */
  private GraphModel gm;

  /** The example palette, shows SampleNode */
  SamplePalette palette;

  /** The palette of shapes and selection tool */
  PaletteFig shapePalette;



  ////////////////////////////////////////////////////////////////
  // constructor

  /** Construct a new FlexibleApplet */
  public FlexibleApplet() {
    System.out.println("making an example");
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
    String widthStr = applet.getParameter("DrawAreaWidth");
    if (widthStr != null) _drawAreaWidth = Integer.parseInt(widthStr);
    String heightStr = applet.getParameter("DrawAreaHeight");
    if (heightStr != null) _drawAreaHeight = Integer.parseInt(heightStr);
    _loadDocument = applet.getParameter("LoadDocument");
    System.out.println("SpawnFrame= " + _spawnFrame);
    System.out.println("DrawArea= " + _drawAreaWidth + " by " +
		       _drawAreaHeight);
    System.out.println("LoadDocument= " + _loadDocument);
  }

  public void initWindows() {
     Dimension drawAreaSize = new Dimension(_drawAreaWidth, _drawAreaHeight);
     JGraph jg = new JGraph(gm);
     jg.setSize(drawAreaSize);
     jg.setPreferredSize(drawAreaSize);

     if (_spawnFrame) {
       System.out.println("spawning frame");
       JFrame f = new JFrame();
       f.getContentPane().setLayout(new BorderLayout());
       f.getContentPane().add(palette, BorderLayout.NORTH);
       f.getContentPane().add(jg, BorderLayout.CENTER);
       f.setSize(drawAreaSize);
       f.show();
     }
     else {
       add("North", palette);
       add("Center", jg);
     }
  }

  ////////////////////////////////////////////////////////////////
  // applet related methods

  /** Part of the required Applet API. Does nothing. */
  public void start() {  }

  /** This is called when the Applet/Application starts up. It does
   *  preloading if the 'ShouldPreLoad' property is set to true. It
   *  also registers some well known Cmd's as an example. */
  public void init() {
    Globals.setApplet(this);
    gm = new DefaultGraphModel();
    palette = new SamplePalette();
    shapePalette = new PaletteFig();
    parseParams(this);
    initWindows();
    Cmd.register(new CmdSave());
    Cmd.register(new CmdOpen());
    Cmd.register(new CmdDispose());
    Cmd.register(new CmdDelete());
    Cmd.register(new CmdSpawn());
    Cmd.register(new CmdAdjustGrid());
    Cmd.register(new CmdShowProperties());
    Cmd.register(new CmdCreateNode(uci.gef.demo.SampleNode.class,
				   "Sample Node"));
    Cmd.register(new CmdExit());
  }

  /** Part of the required Applet API. Does nothing. */
  public void stop() { }

  /** When the user closes this window try to free up as many objects
   *  as possible. */
  public void destroy() {
    gm = null;
    palette = null;
    shapePalette = null;
  }

  /** Reply a breif string that describes this applet in the "About"
   *  box. */
  public String getAppletInfo() {
    return "GEF (the Graph Editing Framework) example editor applet. \n" +
      "FlexibleApplet a very simple demonstration of how GEF can \n" +
      "be used. " + "\n\n" +
      "Author: Jason Robbins\n" +
      "Copyright (c) 1996-1998 Regents of the University of California.\n"+
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

  static final long serialVersionUID = -5917674608779926046L;

} /* end class FlexibleApplet */
