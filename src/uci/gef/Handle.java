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




// File: Handle.java
// Classes: Handle
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.net.*;

/** This class stores the index of the handle that the user is dragging. I
 *  originally used a simple int, but some dragHandle() methods need to change
 *  the index because new handles can be added during a drag.
 *
 * @see FigPoly#moveVertex.  */

public class Handle {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Index of the handle on some Fig that was clicked on. */
  public int index;

  /** Instructions to be shown when the user's mouse is hovering over
   *  or is dragging this handle */
  public String instructions = " ";

  /** Mouse cursor Cursor while hovering or dragging */
  public Cursor cursor = null;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Make a new Handle with the given handle index. */
  public Handle(int ind) { index = ind; }

} /* end class Handle */
