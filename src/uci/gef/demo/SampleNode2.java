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




// File: SampleNode2.java
// Classes: SampleNode2
// Original Author: ics125b spring 1996
// $Id$

package uci.gef.demo;

import java.awt.*;
import java.io.*;
import java.util.*;
import uci.gef.*;

/** An example subclass of NetNode for use in the demos.
 *
 * @see BasicApplication */

public class SampleNode2 extends SampleNode implements Serializable {

  public FigNode makePresentation(Layer lay) {
    Fig obj1 = new FigRect(-25, -25, 50, 50, Color.black, Color.white);
    Fig obj2 = new FigRect(-13, -13, 26, 26, null, Color.pink);
    Fig obj3 = new FigCircle( -5, -24, 10, 10, Color.blue, Color.white);
    Fig obj4 = new FigCircle( -5,  14, 10, 10, Color.blue, Color.white);
    Fig obj5 = new FigRect(-24,  -5, 10, 10, Color.blue, Color.white);
    Fig obj6 = new FigRect( 14,  -5, 10, 10, Color.blue, Color.white);
    Vector temp_list = new Vector();
    temp_list.addElement(obj1);
    temp_list.addElement(obj2);
    temp_list.addElement(obj3);
    temp_list.addElement(obj4);
    temp_list.addElement(obj5);
    temp_list.addElement(obj6);
    FigNode fn = new FigNode(this, temp_list);
    fn.bindPort(north, obj3);
    fn.bindPort(south, obj4);
    fn.bindPort(east, obj5);
    fn.bindPort(west, obj6);
    fn.setBlinkPorts(true);
    return fn;
  }

  static final long serialVersionUID = 6298627794743859836L;

} /* end class SampleNode2 */
