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

// File: Design.java
// Classes: Design
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.argo.kernel;

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
   * Needs-More-Work: in the future Argo will use less tree walking
   * and more trigger-driven critiquing. I.e., critiquing will be done
   * in response to specific manipulations in the editor.  */
  public void critique(Designer d) {
    super.critique(d);
    Enumeration cur = elements();
    // alternative execution policies here?
    while (cur.hasMoreElements()) {
      DesignMaterial dm = (DesignMaterial) cur.nextElement();
      dm.critique(d);
      Thread.yield();
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
