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




// File: EquipmentApplet.java
// Classes: EquipmentApplet
// Original Author: ics125b spring 1996
// $Id$

package uci.gef.demo;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import uci.util.*;
import uci.gef.*;
import uci.graph.*;
import javax.swing.*;

/** An example application to show off <B>some</B> of the capabilities
 *  of the UCI Graph Editing Framework. This example is about
 *  connecting computers to printers with cable, and plugging those
 *  machines into the wall power.  It mainly shows off using Images,
 *  and defining a simple model with properties that can be edited in
 *  the property sheet. Some simple constraints are shown as well: for
 *  example, you cannot connect the power socket to the printer port.
 *
 * @see NodeCPU
 * @see NodePrinter
 * @see NodeWall
 * @see EdgePower
 * @see EdgeData */

public class EquipmentApplet extends JApplet {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected static boolean _spawnFrame = true;
  protected static int _drawAreaWidth = 400;
  protected static int _drawAreaHeight = 300;

  /** The net-level model to edit */
  private GraphModel gm;

  /** The palette of shapes and selection tool */
  EquipmentPalette palette;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new EquipmentApplet */
  public EquipmentApplet() {
    System.out.println("making an example");
    gm = new DefaultGraphModel();
    palette = new EquipmentPalette();
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
    jgf.setToolBar(palette);
    // make the delete key remove elements from the underlying GraphModel
    jgf.getGraph().bindKey(new CmdDispose(), KeyEvent.VK_DELETE, 0);

    jgf.show();
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
    gm = null;
    palette = null;
  }

  /** reply a breif string that describes this applet in the "About"
   * box. */
  public String getAppletInfo() {
    return "GEF (the Graph Editing Framework) example editor applet. \n" +
      "EquipmentApplet a very simple demonstration of how GEF can \n" +
      "be used. " + "\n\n" +
      "Author: Jason Robbins\n" +
      "Copyright (c) 1996-1998 Regents of the University of California.\n"+
      "All rights reserved.\n\n";
  }

  static final long serialVersionUID = -5151592540895237366L;

} /* end class EquipmentApplet */
