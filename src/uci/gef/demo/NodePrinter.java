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




// File: NodePrinter.java
// Classes: NodePrinter
// Original Author: ics125b spring 1996
// $Id$

package uci.gef.demo;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import uci.gef.*;

/** An example subclass of NetNode for use in the Example
 * application. This class represents a printer that can be plugged
 * in and connected to a computer. 
 *
 * @see EquipmentApplet */

public class NodePrinter extends NetNode implements Serializable {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected PortPower _powerPort;
  protected PortData _dataPort;
  
   /** Initialize a new NodePrinter. */

  public void initialize(Hashtable args) {
    addPort(_powerPort = new PortPower(this, PortPower.RECEPTICAL));
    addPort(_dataPort = new PortData(this));
  }

  public FigNode makePresentation(Layer lay) {
    URL imageURL = null;
    try { imageURL = new URL("http://www.ics.uci.edu/~jrobbins/images/printer.gif"); }
    catch (java.net.MalformedURLException e) { }
    Fig obj1 = new FigImage(0, 0, imageURL);
    Image i = Globals.getImage(imageURL);
    Globals.waitForImages();
    Fig obj2 = new FigRect( 2, -7, 14, 14, Color.black, Color.white);
    FigRRect obj3 = new FigRRect( -8, 10, 8, 15, Color.black, Color.black);
    obj3.setCornerRadius(3);


    Vector temp_list = new Vector();
    temp_list.addElement(obj1);
    temp_list.addElement(obj2);
    temp_list.addElement(obj3);
    FigNode fn = new FigNode(this, temp_list);

    fn.bindPort(_powerPort, obj2);
    fn.bindPort(_dataPort, obj3);

    return fn;
  }

  static final long serialVersionUID = -1991803325169884172L;

} /* end class NodePrinter */
