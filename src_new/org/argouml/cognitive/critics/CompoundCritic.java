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



// File: CompoundCritic.java
// Classes: CompoundCritic
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.cognitive.critics;

import java.util.*;
import javax.swing.*;

import org.tigris.gef.util.*;

import org.argouml.cognitive.*;

/** A CompoundCritic acts like a regular critic in that it checks the
 *  design and produces design feedback.  However, a CompoundCritic is
 *  composed of several regular critics that are applied in order.
 *  The first one the produces feedback ends the application.  This is
 *  useful when criticism can be ordered from specific to general:
 *  general feedback should not be produced if specific feedback is
 *  available.  For example, one critic might check for the legality
 *  of the name of a design element, and another might check for the
 *  presence of any name.  If a given design element has no name, both
 *  critics could produce feedback, but it would be more useful if
 *  only the first one did.  */

// TODO: maybe should stop at first, or find highest priority.

public class CompoundCritic extends Critic {


  ////////////////////////////////////////////////////////////////
  // instance variables

  /**  The sub-critics that make up this CompoundCritic. */
  protected Vector _critics = new Vector();

  ////////////////////////////////////////////////////////////////
  // constructor

  public CompoundCritic() {
  }

  public CompoundCritic(Critic c1, Critic c2) {
    this();
    _critics.addElement(c1);
    _critics.addElement(c2);
  }

  public CompoundCritic(Critic c1, Critic c2, Critic c3) {
    this(c1, c2);
    _critics.addElement(c3);
  }

  public CompoundCritic(Critic c1, Critic c2, Critic c3, Critic c4) {
    this(c1, c2, c3);
    _critics.addElement(c4);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setCritics(Vector critics) { _critics = critics; }
  public Vector getCritics() { return _critics; }
  public void addCritic(Critic c) { _critics.addElement(c); }
  public void removeCritic(Critic c) { _critics.removeElement(c); }
  
  ////////////////////////////////////////////////////////////////
  // critiquing

  public void critique(Object dm, Designer dsgr) {
    int size = _critics.size();
    for (int i = 0; i < size; ++i) {
      Critic c = (Critic) _critics.elementAt(i);
      if (c.isActive() && c.predicate(dm, dsgr)) {
	ToDoItem item = c.toDoItem(dm, dsgr);
	postItem(item, dm, dsgr);
	return; // once one criticism is found, exit
      }
    }
  }

  public boolean supports(Decision d) {
    int size = _critics.size();
    for (int i = 0; i < size; ++i) {
      Critic c = (Critic) _critics.elementAt(i);
      if (c.supports(d)) return true;
    }
    return false;
  }

  public Vector getSupportedDecisions() {
      throw new UnsupportedOperationException("this method should never be called: "+
		      "CompoundCritic getSupportedDecisions");
  }

  public void addSupportedDecision(Decision d) {
      throw new UnsupportedOperationException("this method should never be called: "+
		      "CompoundCritic addSupportedDecision");
  }

  public boolean supports(Goal g) {
    int size = _critics.size();
    for (int i = 0; i < size; ++i) {
      Critic c = (Critic) _critics.elementAt(i);
      if (c.supports(g)) return true;
    }
    return false;
  }

  public Vector getSupportedGoals() {
    throw new UnsupportedOperationException("this method should never be called: "+
		    "CompoundCritic getSupportedGoals");
  }

  public void addSupportedGoal(Goal g) {
    throw new UnsupportedOperationException("this method should never be called: "+
		    "CompoundCritic addSupportedGoal");
  }

  public boolean containsKnowledgeType(String type) {
    int size = _critics.size();
    for (int i = 0; i < size; ++i) {
      Critic c = (Critic) _critics.elementAt(i);
      if (c.containsKnowledgeType(type)) return true;
    }
    return false;
  }
  public void addKnowledgeType(String type) {
    throw new UnsupportedOperationException("this method should never be called: "+
		    "CompoundCritic addKnowledgeType");
  }
  
  public String expand(String desc, VectorSet offs) {
      throw new UnsupportedOperationException("this method should never be called: "+
		      "CompoundCritic expand");
  }

  public Icon getClarifier() {
      throw new UnsupportedOperationException("this method should never be called: "+
		      "CompoundCritic getClarifier");
  }
  

  public boolean isActive() {
    int size = _critics.size();
    for (int i = 0; i < size; ++i) {
      Critic c = (Critic) _critics.elementAt(i);
      if (c.isActive()) return true;
    }
    return false;
  }

  ////////////////////////////////////////////////////////////////
  // criticism control
  
  public boolean isEnabled() {
    return true;
  }

  ////////////////////////////////////////////////////////////////
  // design feedback

  public ToDoItem toDoItem(Object dm, Designer dsgr) {
      throw new UnsupportedOperationException("this method should never be called: "+
		      "CompoundCritic toDoItem()");
  }

} /* end class CompoundCritic */
