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

// File: CompositeCritic.java
// Classes: CompositeCritic
// Original Author: jrobbins@ics.uci.edu
// $Id$


package uci.argo.kernel;

import java.util.*;

/** A group of critics. Currently not used for anything. It could be
 *  an optimization to group critics that are likely to be activated or
 *  deactivated as a group...*/

public class CompositeCritic {
  ////////////////////////////////////////////////////////////////
  // instance variables

  private Vector _subcritics = new Vector();
  private ControlMech _controlMech;

  ////////////////////////////////////////////////////////////////
  // constructors

  public CompositeCritic(ControlMech cm) {
    _controlMech = cm;
  }

  public CompositeCritic() {
    _controlMech = new EnabledCM(); // just checks isEnabled
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void addElement(Critic c) {
    _subcritics.addElement(c);
  }

  ////////////////////////////////////////////////////////////////
  // critiquing

  public void critique(DesignMaterial dm, Designer d) {
    Enumeration cur = _subcritics.elements();
    while (cur.hasMoreElements()) {
      Critic c = (Critic)(cur.nextElement());
      c.critique(dm, d);
    }
  }

} /* end class CompositeCritic */
