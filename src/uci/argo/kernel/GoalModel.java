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

// File: GoalModel.java
// Classes: GoalModel
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.argo.kernel;

import java.util.*;
import java.awt.*;
import uci.util.*;
//import uci.ui.*;


/** Models the designers goals in making this design.  Provides useful
 *  control information to the Agency so that only critics relevant to
 *  the designers goals are ever executed.
 *
 *  Needs-More-Work: Really this should be part of a domain extension
 *  and not the kernel.  I have not developed this part of Argo very
 *  much. */

public class GoalModel extends Observable
implements java.io.Serializable {
  ////////////////////////////////////////////////////////////////
  // instance variables

  private Properties _goals = new Properties();

  ////////////////////////////////////////////////////////////////
  // constructor

  public GoalModel() { }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Reply true iff the Designer wants to achieve the given goal. */
  public boolean hasGoal(String goal) {
    String intStr = _goals.getProperty(goal);
    if 	(null == intStr) return false;
    int priority = Integer.parseInt(intStr);
    return priority >= 1;
  }

  public synchronized void setGoalPriority(String goal, int priority) {
    String priStr = (new Integer(priority)).toString();
    if (priority > 0) _goals.put(goal, priStr);
    else _goals.remove(goal);
  }

  public Object getGoalInfo(String goal) {
    return _goals.getProperty(goal);
    /* needs-more-work, we need a better representation of goals */
  }

  public void setGoalInfo(String goal, String info) {
    _goals.put(goal, info);
    /* needs-more-work, we need a better representation of goals */
  }

  /** The Designer wants to achieve the given goal. */
  public void startDesiring(String goal) { setGoalPriority(goal, 1); }

  /** The Designer does not care about the given goal. */
  public void stopDesiring(String goal) { setGoalPriority(goal, 0); }


} /* end class GoalModel */
