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

// File: EdgeData.java
// Classes: EdgeData
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef.demo;

import uci.gef.*;
import java.util.*;
import uci.util.*;

/** A sample NetEdge subclass for use in the Example application.  There
 *  are no real details here yet.  If I was to expand this Example more
 *  the Edge could have more attributes, e.g. bandwidth... and it
 *  could have its own subclasses of FigEdge to make it look a
 *  certain way. */

public class EdgeData extends NetEdge {
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

} /* end class EdgeData */
