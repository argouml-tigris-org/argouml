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

// File: Design.java
// Classes: Design
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.cognitive;

import java.util.*;

/** A composite DesignMaterial that contains other
 *  DesignMaterial's. */

public class Design extends DesignMaterial {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The contained DesignMaterial's (including other Design's). */
  private Vector _subdesigns = new Vector();

  ////////////////////////////////////////////////////////////////
  // constructor

  /** Construct a new Design. This method is currently empty. The
   * _subdesigns instance variable is set through an initializer. */
  public Design() { }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Reply a vector of contained DesignMaterial's. */
  public Vector getSubdesigns() { return _subdesigns; }

  /** Set the vector of contained DesignMaterial's. */
  public void setSubdesigns(Vector subs) { _subdesigns = subs; }

  /** Enumerate all contained DesignMaterial's. */
  public Enumeration elements() { return _subdesigns.elements(); }
  /** Enumerate all contained DesignMaterial's. */
  public Iterator iterator() { return _subdesigns.iterator(); }

  /** Reply true if the given DesignMaterial is part of this design. */
  public boolean transativelyIncludes(DesignMaterial dm) {
    Enumeration cur = elements();
    while (cur.hasMoreElements()) {
      DesignMaterial dm2 = (DesignMaterial) cur.nextElement();
      if (dm == dm2) return true;
      if ((dm2 instanceof Design) &&
	  (((Design)dm2).transativelyIncludes(dm))) {
	return true;
      }
    }
    return false;
  }

  /** Add the given DesignMaterial to this Design, if it is not
   * already. */
  public synchronized void addElement(DesignMaterial dm) {
    if (!transativelyIncludes(dm)) {
      _subdesigns.addElement(dm);
      dm.addParent(this);
    }
  }

  /** Remove the given DesignMaterial from this Design. */
  public synchronized void removeElement(DesignMaterial dm) {
    _subdesigns.removeElement(dm);
    dm.removeParent(this);
  }

  ////////////////////////////////////////////////////////////////
  // critiquing

  /** Critique a Design by critiquing each contained
   * DesignMaterial. <p>
   *
   * TODO: in the future Argo will use less tree walking
   * and more trigger-driven critiquing. I.e., critiquing will be done
   * in response to specific manipulations in the editor.  */
  public void critique(Designer d) {
    super.critique(d);
    Enumeration cur = elements();
    // alternative execution policies here?
    while (cur.hasMoreElements()) {
      DesignMaterial dm = (DesignMaterial) cur.nextElement();
      dm.critique(d);
    }
  }

  /** Reply a string that describes this Design. Inteneded for
   * debugging. */
  public String toString() {
    String printString = super.toString() + " [\n";
    Enumeration cur = elements();
    while (cur.hasMoreElements()) {
      DesignMaterial dm = (DesignMaterial) cur.nextElement();
      printString = printString + "  " + dm.toString() + "\n";
    }
    return printString + "]\n";
  }

} /* end class Design */
