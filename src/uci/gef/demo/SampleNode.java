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




// File: SampleNode.java
// Classes: SampleNode
// Original Author: ics125b spring 1996
// $Id$

package uci.gef.demo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import uci.gef.*;

/** An example subclass of NetNode for use in the Example application.
 *
 * @see Example */
public class SampleNode extends NetNode implements Serializable {

  public SamplePort north, east, west, south;
  protected int _number;
  
   /** Initialize a new SampleNode from the given default node and
    *  application specific model. <p>
    *
    *  Needs-More-Work: for now we construct the FigNode
    *  programatically, but eventually we will store it in a class
    *  variable and just refer to it, or copy it(?). That way the user
    *  can edit the FigNode(s) stored in the class variable and
    *  have those changes shown for all existing nodes, or for all
    *  future nodes. Maybe I should think about doing virtual copies?<p>
    */

  public void initialize(Hashtable args) {
    addPort(east = new SamplePort2(this));
    addPort(west = new SamplePort2(this));
    addPort(north = new SamplePort(this));
    addPort(south = new SamplePort(this));
    _number = _NextNumber++;
   }

  static int _NextNumber = 1;

  public int getNumber() { return _number; }
  public int getId() { return _number; }
  
  public FigNode makePresentation(Layer lay) {
    FigSampleNode fn = new FigSampleNode();
    fn.setOwner(this);
    return fn;
  }

  /** Sample event handler: prints a message to the console. */
  public void mouseEntered(MouseEvent e) {
    //    System.out.println("sample node got mouseEnter");
  }

  /** Sample event handler: prints a message to the console. */
  public void mouseExited(MouseEvent e) {
    //    System.out.println("sample node got mouseExit");
  }

  /** Sample event handler: prints a message to the console. */
  public void mouseReleased(MouseEvent e) {
    //    System.out.println("sample node got mouseUp");
  }

  /** Sample event handler: prints a message to the console. */
  public void mousePressed(MouseEvent e) {
    //    System.out.println("sample node got mouseDown");
  }

  /** Sample event handler: prints a message to the console. */
  public void mouseClicked(MouseEvent e) {
    //    System.out.println("sample node got mouseDown");
  }

  /** Sample event handler: prints a message to the console. */
  public void mouseDragged(MouseEvent e) {
    //    System.out.println("sample node got mouseDrag");
  }

  /** Sample event handler: prints a message to the console. */
  public void mouseMoved(MouseEvent e) {
    //    System.out.println("sample node got mouseMove");
  }

  /** Sample event handler: prints a message to the console. */
  public void keyTyped(KeyEvent e) {
    //    System.out.println("sample node got keyUp");
  }

  /** Sample event handler: prints a message to the console. */
  public void keyReleased(KeyEvent e) {
    //    System.out.println("sample node got keyUp");
  }

  /** Sample event handler: prints a message to the console. */
  public void keyPressed(KeyEvent e) {
    //    System.out.println("sample node got keyDown");
  }

  static final long serialVersionUID = -8629963393808576433L;

} /* end class SampleNode */
