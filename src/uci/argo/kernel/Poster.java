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

// File: Poster.java
// Classes: Poster
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.argo.kernel;

import java.util.*;

/** Interface that defines methods required on any object that can
 *  post a ToDoItem to the Designer's ToDoList. Basically requires that
 *  the poster (1) have contact information, (2) be able to hush
 *  and unhush itself, and (3) be able to determine if a ToDoItem it
 *  posted previously should still be on the Designer's ToDoList. <p>
 *
 *  Currently Critic and Designer implement this interface.
 *
 * @see Critic
 * @see Designer */

public interface Poster {

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Get some contact information on the Poster. */
  String getExpertEmail();

  /** Update the Poster's contact info. Is this needed? */
  void setExpertEmail(String addr);

  /** Reply true if the given item should be kept on the Designer's
   * ToDoList, false if it is no longer valid. */
  boolean stillValid(ToDoItem i, Designer d);

  boolean supports(Decision d);
  Vector getSupportedDecisions();
  boolean supports(Goal g);
  Vector getSupportedGoals();
  boolean includesKnowledgeType(int knowledgeType);
  
  ////////////////////////////////////////////////////////////////
  // criticism control

  /** temporarily disable this Poster. */
  void hush();

  /** Unhush this Poster, it may resume posting without further
   * delay. */
  void unhush();

  ////////////////////////////////////////////////////////////////
  // issue resolution

  void fixIt(ToDoItem item, Object arg);

  boolean canFixIt(ToDoItem item);

} /* end interface Poster */
