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





// File: FigInk.java
// Classes: FigInk
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/** Primitive Fig to paint Ink on a LayerDiagram. Ink is like an open
 *  polygon with no fill.  The main difference between FigInk and
 *  FigPoly is in the way that they are created.
 *
 * @see FigPoly
 * @see ModeCreateFigInk */

public class FigInk extends FigPoly {

  /** Construct a new FigInk w/ the given attributes. */
  public FigInk() { super(); _filled = false;}

  /** Construct a new FigInk w/ the given point and attributes. */
  public FigInk(int x, int y) {
    super(x, y);
    _filled = false;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Line width of ink must be always be 1, so do nothing  */
  public void setLineWidth(int w) { }

  /** FigInks can never be filled, so do nothing. */
  public void setFilled(boolean f) { }

  /** FigInks can never be rectilinear, so do nothing. */
  public void setRectilinear(boolean r) { }

  public boolean contains(int x, int y) {
    return super.findHandle(x, y) != -1;
  }

  static final long serialVersionUID = 660593786382654496L;

} /* end class FigInk */

