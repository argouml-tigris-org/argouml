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




// File: EdgeData.java
// Classes: EdgeData
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef.demo;

import java.util.*;
import java.io.*;

import uci.util.*;
import uci.gef.*;

/** A sample NetEdge subclass for use in the Example application.  There
 *  are no real details here yet.  If I was to expand this example more
 *  the Edge could have more attributes, e.g. bandwidth... and it
 *  could have its own subclasses of FigEdge to make it look a
 *  certain way. */

public class EdgeData extends NetEdge implements Serializable {
  ////////////////////////////////////////////////////////////////
  // constants

  public static final String pCABLE_TYPE = "Cable Type";
  public static final String pBITS_PER_SECOND = "Bits/Second";

  public static final String CABLE_SCSI = "SCSI";
  public static final String CABLE_ETHERNET = "Ethernet";
  public static final String CABLE_FIREWIRE = "FireWire";
  public static final String CABLE_FDDI = "FDDI";

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Speed of this data line */
  protected int _bitsPerSecond = 100000;

  /** Type of cable, choosen from the constants above: SCSI, FireWire,
   * etc. */
  protected String _cableType = CABLE_SCSI;


  ////////////////////////////////////////////////////////////////
  // constructors

  public EdgeData() { } /* needs-more-work */

  ////////////////////////////////////////////////////////////////
  // accessors

  public String getCableType() { return _cableType; }
  public void setCableType(String ct) {
    if (_PossibleCableTypes.contains(ct))
      _cableType = ct;
  }

  public int getBitsPerSecond() { return _bitsPerSecond; }
  public void setBitsPerSecond(int bps) { _bitsPerSecond = bps; }

  public static Vector _PossibleCableTypes = null;

  static {
    _PossibleCableTypes = new Vector();
    _PossibleCableTypes.addElement(CABLE_SCSI);
    _PossibleCableTypes.addElement(CABLE_ETHERNET);
    _PossibleCableTypes.addElement(CABLE_FIREWIRE);
    _PossibleCableTypes.addElement(CABLE_FDDI);
    // //uci.ui.PropSheetCategory.addEnumProp(pCABLE_TYPE, _PossibleCableTypes);
  }

  ////////////////////////////////////////////////////////////////
  // perspectives

  public FigEdge makePresentation(Layer lay) {
    return new FigEdgeRectiline();
  }

  static final long serialVersionUID = 3557704616625241980L;

} /* end class EdgeData */
