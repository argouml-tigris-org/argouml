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




// File: Guide.java
// Classes: Guide
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;

/** Constrains interactions to certain coordinates. For example
 *  GuideGrid makes objects snap to a grid. Other subclasses might
 *  implement other snapping rules, for example, a polar grid or
 *  gravity (objects cling to other objects when they get close).
 *
 * @see GuideGrid */

public abstract class Guide implements java.io.Serializable {

  ////////////////////////////////////////////////////////////////
  // geometric constraints

  /** Return a NEW Point that is close to p and on the guideline
   *  (e.g., gridline). */
  public final Point snapTo(Point p) {
    Point res = new Point(p.x, p.y);
    snap(res);
    return res;
  }

  /** Modify the given point to satisfy guide conditions (e.g. be on a
   *  gridline). */
  public abstract void snap(Point p);

  ////////////////////////////////////////////////////////////////
  // user interface

  /** Bring up a dialog box to set the parameters for this
   *  guide. E.g., set the size of a grid. */
   public void adjust() { };

  static final long serialVersionUID = -3263257514204584581L;
} /* end class Guide */

