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



// File: GoalModel.java
// Classes: GoalModel
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.cognitive;

import java.util.*;

/** Models the designers goals in making this design.  Provides useful
 *  control information to the Agency so that only critics relevant to
 *  the designers goals are ever executed.
 *
 *  TODO: Really this should be part of a domain extension
 *  and not the kernel.  I have not developed this part of Argo very
 *  much. */

public class GoalModel extends Observable
implements java.io.Serializable {
  ////////////////////////////////////////////////////////////////
  // instance variables

  private Vector _goals = new Vector();

  ////////////////////////////////////////////////////////////////
  // constructor

  public GoalModel() {
    addGoal(Goal.UNSPEC);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public Vector getGoals() { return _goals; }
  public void addGoal(Goal g) { _goals.addElement(g); }
  public void removeGoal(Goal g) { _goals.removeElement(g); }
  
   /** Reply true iff the Designer wants to achieve the given goal. */
   public boolean hasGoal(String goalName) {
     Enumeration goalEnum = _goals.elements();
     while (goalEnum.hasMoreElements()) {
       Goal g = (Goal) goalEnum.nextElement();
       if (g.getName().equals(goalName)) return g.getPriority() > 0;
     }
     return false;
   }

  public synchronized void setGoalPriority(String goalName, int priority) {
    Goal g = new Goal(goalName, priority);
    _goals.removeElement(g);
    _goals.addElement(g);
  }

//   public Object getGoalInfo(String goal) {
//     return _goals.getProperty(goal);
//     /* TODO, we need a better representation of goals */
//   }

//   public void setGoalInfo(String goal, String info) {
//     _goals.put(goal, info);
//     /* TODO, we need a better representation of goals */
//   }

  /** The Designer wants to achieve the given goal. */
  public void startDesiring(String goalName) {
    addGoal(new Goal(goalName, 1)); }

  /** The Designer does not care about the given goal. */
  public void stopDesiring(String goalName) {
    removeGoal(new Goal(goalName, 0));
  }


} /* end class GoalModel */
