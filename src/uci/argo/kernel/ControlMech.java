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

// File: ControlMech.java
// Classes: ControlMech EnabledCM NotHushedCM CompositeCM
// Original Author: jrobbins@ics.uci.edu
// $Id$

// TO DO: Do all these classes need to be in their own files? public?

package uci.argo.kernel;

import java.util.*;
import uci.util.*;

/** A ControlMech determines when a Critic should be active.  New
 *  ControlMech's can be added to add value to existing Critic's.  Each
 *  ControlMech implements a isRelevant() predicate that determines if a
 *  given critic is relevant to a given Designer at the current
 *  time. */

public abstract class ControlMech implements java.io.Serializable {

  public ControlMech() { }
  public boolean isRelevant(Critic c, Designer d) {
    return true;
  }
} // end class ControlMech



class EnabledCM extends ControlMech {
  public boolean isRelevant(Critic c, Designer d) {
    return c.isEnabled();
  }
} // end class EnabledCM



class NotHushedCM extends ControlMech {
  public boolean isRelevant(Critic c, Designer d) {
    return !c.hushOrder().getHushed();
  }
} // end class NotHushedCM



class DesignGoalsCM extends ControlMech {
  public boolean isRelevant(Critic c, Designer d) {
    return c.isRelevantToGoals(d);
  }
} // end class DesignGoalsCM

// How much control should critics have over when they are relavant?
// Does doing that in code instead of declaratively limit reasoning?
// How does using more semantically rich method calls impact
// componentization?

class CurDecisionCM extends ControlMech {
  public boolean isRelevant(Critic c, Designer d) {
    return c.isRelevantToDecisions(d);
  }
} // end class CurDecisionCM



class CompositeCM extends ControlMech {
  protected Vector _mechs = new Vector();
  public void addMech(ControlMech cm) {
    _mechs.addElement(cm);
  }
} // end class CompositeCM



class AndCM extends CompositeCM {
  public boolean isRelevant(Critic c, Designer d) {
    Enumeration cur = _mechs.elements();
    while (cur.hasMoreElements()) {
      ControlMech cm = (ControlMech)cur.nextElement();
      if (!cm.isRelevant(c, d)) return false;
    }
    return true;
  }
} // end class AndCM



class OrCM extends CompositeCM {
  public boolean isRelevant(Critic c, Designer d) {
    Enumeration cur = _mechs.elements();
    while (cur.hasMoreElements()) {
      ControlMech cm = (ControlMech)cur.nextElement();
      if (cm.isRelevant(c, d)) return true;
    }
    return false;
  }
} // end class OrCM



class StandardCM extends AndCM {
  public StandardCM() {
    addMech(new EnabledCM());
    addMech(new NotHushedCM());
    addMech(new DesignGoalsCM());
    addMech(new CurDecisionCM());
  }
}
