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




// File: NodeCPU.java
// Classes: NodeCPU
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef.demo;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import uci.gef.*;
import uci.util.*;

/** An example subclass of NetNode for use in the Example
 *  application. This class represents a computer that can be plugged
 *  into electrical power and attached to a printer. The Computer has
 *  attributes that can be set via the property sheet.
 *
 * @see Example */

public class NodeCPU extends NetNode implements Serializable {

  ////////////////////////////////////////////////////////////////
  // constants

  public static final String pMEGS_OF_RAM = "Megs Of RAM";
  public static final String pBUS_SPEED_MHZ = "Bus Speed Mhz";
  public static final String pCPU_SPEED_MHZ = "CPU Speed Mhz";
  public static final String pHAS_CD_ROM = "Has CD ROM";
  public static final String pOWNER_INFO = "Owner";

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected int _megsOfRAM = 16;
  protected int _busSpeedMHz = 25;
  protected int _cpuSpeedMHz = 100;
  protected boolean _hasCDROM = true;
  protected String _ownerInfo = "Your Name Here\n"+
  "Your Company\n" + "Your Address Line1\n" + "Your Address Line 2";

  PortPower powerPort;
  PortData dataPort;

  ////////////////////////////////////////////////////////////////
  // constructors and initialization code

  /** Initialize a new NodeCPU. <p>
   *
   *  Needs-More-Work: for now we construct the FigNode
   *  programatically in this class.  A more powerful way to do it is
   *  to make your own sublcass of FigNode. */

  public void initialize(Hashtable args) {
    addPort(powerPort = new PortPower(this, PortPower.RECEPTICAL));
    addPort(dataPort = new PortData(this));
   }

  ////////////////////////////////////////////////////////////////
  // accessors

  public int getMegsOfRAM() { return _megsOfRAM; }
  public int getBusSpeedMHz() { return _busSpeedMHz; }
  public int getCPUSpeedMhz() { return _cpuSpeedMHz; }
  public boolean getHasCDROM() { return _hasCDROM; }
  public String getOwnerInfo() { return _ownerInfo; }

  public void setMegsOfRAM(int m) {
    if (m % 8 == 0) _megsOfRAM = m;
  }
  public void setBusSpeedMHz(int b) {
    if (b % 25 == 0 || b % 33 == 0)
      _busSpeedMHz = b;
  }
  public void setCPUSpeedMhz(int c) {
    if (c == 25 || c == 33 || c == 50 || c == 66 || c == 75 ||
	c == 90 || c == 100 || c == 120 || c == 133 || c == 150 ||
	c == 166 || c == 200 || c == 233)
      _cpuSpeedMHz = c;
  }

  public void setHasCDROM(boolean r) { _hasCDROM = r; }

  public void setOwnerInfo(String oi) { _ownerInfo = oi; }

  static {
    Vector possibleCPUSpeeds = new Vector();
    possibleCPUSpeeds.addElement(new Integer(25));
    possibleCPUSpeeds.addElement(new Integer(33));
    possibleCPUSpeeds.addElement(new Integer(66));
    possibleCPUSpeeds.addElement(new Integer(75));
    possibleCPUSpeeds.addElement(new Integer(90));
    possibleCPUSpeeds.addElement(new Integer(100));
    possibleCPUSpeeds.addElement(new Integer(120));
    possibleCPUSpeeds.addElement(new Integer(133));
    possibleCPUSpeeds.addElement(new Integer(150));
    possibleCPUSpeeds.addElement(new Integer(166));
    possibleCPUSpeeds.addElement(new Integer(200));
    possibleCPUSpeeds.addElement(new Integer(233));
    // //uci.ui.PropSheetCategory.addEnumProp(pCPU_SPEED_MHZ, possibleCPUSpeeds);
    // //uci.ui.PropSheetCategory.addBigTextProp(pOWNER_INFO, 4);
  }

  ////////////////////////////////////////////////////////////////
  // presentations

  public FigNode makePresentation(Layer lay) {
    URL imageURL = null;
    try {
      imageURL = new URL("http://www.ics.uci.edu/~jrobbins/images/cpu.gif");
    }
    catch (java.net.MalformedURLException e) { }
    Fig obj1 = new FigImage(0, 0, imageURL);
    int imageWidth = obj1.getWidth();
    Fig obj2 = new FigRect(2, -7, 14, 14, Color.black, Color.white);
    FigRRect obj3 = new FigRRect(imageWidth - 4, 10, 8, 15, Color.black,
			    Color.black);
    obj3.setCornerRadius(3);
    Vector temp_list = new Vector();
    temp_list.addElement(obj1);
    temp_list.addElement(obj2);
    temp_list.addElement(obj3);
    FigNode fn = new FigNode(this, temp_list);
    fn.bindPort(powerPort, obj2);
    fn.bindPort(dataPort, obj3);
    return fn;
  }

  static final long serialVersionUID = -4727051976743645287L;

} /* end class NodeCPU */
