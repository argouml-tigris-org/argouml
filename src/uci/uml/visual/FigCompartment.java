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



// File: FigCompartment.java
// Classes: FigCompartment
// Original Author: Eric Lefevre
// NOT RELEVANT:
// $Id$

package uci.uml.visual;

import java.awt.*;
import java.awt.event.*;
import uci.gef.*;

/** A compartment is a FigText that has the capacity to be
 *  displayed or not. A compartment is typically containing informations
 *  on the current figure (see the attributes compartment for FigClass). <BR>
 *  need-more-work: maybe we should have it inheriting Fig. 
 *  need-more-work: it is really necessary to have a separate class for that?
 *                  after all, it only contains a boolean. */

public class FigCompartment extends FigText {
  ////////////////////////////////////////////////////////////////
  // instance variables

  protected boolean _displayed = true;

  /** Construct a new FigCompartment with the given position, size, and attributes. */
  public FigCompartment(int x, int y, int w, int h ) {
    super(x, y, w, h);
  }

  /** Construct a new FigCompartment with the given position, size, color,
   *  string, font, and font size. Text string is initially empty and
   *  centered. */
  public FigCompartment(int x, int y, int w, int h,
		 Color textColor, String familyName, int fontSize) {
    super(x, y, w, h, textColor, familyName, fontSize);
  }
  ////////////////////////////////////////////////////////////////
  // accessors

   /** Returns true if it is to be displayed. */
   public boolean isDisplayed() { return _displayed; }

   /** Returns true if it is to be displayed. */
   public void setDisplayed(boolean isDisplayed) { _displayed = isDisplayed; }

   
  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Check if it should be displayed. If yes, call super.paint. Otherwise, does nothing. */
  public void paint(Graphics g) {
    if ( _displayed ) super.paint(g);
  }

  ////////////////////////////////////////////////////////////////
  // event handlers



  public void mouseClicked(MouseEvent me) {
    if (isDisplayed()) super.mouseClicked(me);
    //do not process events if not displayed
  }

} /* end class FigCompartment */
