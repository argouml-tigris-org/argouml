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




// File: EdgePower.java
// Classes: EdgePower
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef.demo;

import java.io.*;
import uci.gef.*;

/** A sample NetEdge subclass for use in the example application. This
 *  represents a power cord that can go from the computer to the wall,
 *  or from the printer to the wall. */

public class EdgePower extends NetEdge implements Serializable {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Voltage currently on line. */
  protected int _voltage = 110;

  /** Maximum Voltage that this line can handle. */
  protected int _maxVoltage = 560; // realistic?

  /** Some power cords have a third grounding prong, some don't. */
  protected boolean _hasGroundProng = true;

  ////////////////////////////////////////////////////////////////
  // constructor

  public EdgePower() { } /* needs-more-work */

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setVoltage(int v) { _voltage = v; }
  public int getVoltage() { return _voltage; }

  public void setMaxVoltage(int v) { _maxVoltage = v; }
  public int getMaxVoltage() { return _maxVoltage; }

  public void setHasGroundProng(boolean hgp) { _hasGroundProng = hgp; }
  public boolean getHasGroundProng() { return _hasGroundProng; }

  ////////////////////////////////////////////////////////////////
  // NetEdge API

  public FigEdge makePresentation(Layer lay) {
    return new FigEdgeLine();
  }

  static final long serialVersionUID = -2818734475958408590L;

} /* end class EdgePower */
