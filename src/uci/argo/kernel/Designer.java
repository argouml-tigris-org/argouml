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

// File: Designer.java
// Classes: Designer
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.argo.kernel;

import java.util.*;
import java.awt.*;
import uci.util.*;

/** This class models the designer who is building a complex design in
 *  some application domain and needs continuous feedback to aid in the
 *  making of good design decisions. */

public class Designer implements Poster, Runnable, java.io.Serializable {
  ////////////////////////////////////////////////////////////////
  // instance variables

  public static Designer TheDesigner = new Designer();

  /** ToDoList items that are on the designers ToDoList because of
   *  this material. */
  private ToDoList _toDoList;

  /** Preferences -- very ill defined */
  private Properties _prefs;

  /** The email address where other designers can send this designer
   * email. This is not used yet. */
  private String _emailAddr;

  /** The decisions currently being considered by the designer.
   *  Decisions are currently modeled as simple descriptive strings.
   *  Each decision also has a priority number which is ill defined,
   *  but positive Ints mean that the designer is considering it. This
   *  explicit representation of what decisions the designer is
   *  interested in at a given moment allows the Agency to select
   *  relevant critics for execution. */

  private DecisionModel _decisions;

  /** The goals of the designer are likewise used by the Agency to
   *  determine what critics are relevant. */

  private GoalModel _goals;

  /** Each designer has their own Agency instance that is responsible
   *  for selecting and executing critics that are relevant to thid
   *  designer on an on going basis. */

  private Agency _agency;

  private Thread _critiquer;
  private int _critiquingInterval = 3000;
  private boolean _autoCritique = true;

  private ChildGenerator _cg = new ChildGenDMElements();
  //needs-more-work: theInstance()

  private static Object _CritiquingRoot;

  ////////////////////////////////////////////////////////////////
  // constructor and singeton methods

  public Designer() {
    _decisions = new DecisionModel();
    _goals = new GoalModel();
    _agency = new Agency();
    _prefs = new Properties();
    _toDoList = new ToDoList();
    _toDoList.spawnValidityChecker(this);
    _emailAddr = "jrobbins@ics.uci.edu";
    TheDesigner = this;
  }

  public static void theDesigner(Designer d) { TheDesigner = d; }
  public static Designer theDesigner() { return TheDesigner; }


  ////////////////////////////////////////////////////////////////
  // critiquing

  /** Start a separate thread to continually select and execute
   *  critics that are relevant to this designer's work. */
  public void spawnCritiquer(Object root) {
    /* needs-more-work really should be a separate class */
    _critiquer = new Thread(this);
    _critiquer.setDaemon(true);
    _critiquer.start();
    _CritiquingRoot = root;
  }

  /** Continuously select and execute critics against this designer's
   *  design. spawnCritiquer is used to start a Thread that runs
   *  this. */
  public void run() {
    Vector alreadyCritiqued = new Vector();       // jer added 970911
    Vector toCritique = new Vector();     // jer added 970911
    while (true) {
      if (_CritiquingRoot != null && getAutoCritique()) {
        synchronized (_CritiquingRoot) {
          _agency.determineActiveCritics(this);
          // jer added 970911
          // now use ChildGenerators instead of elements()
          //- _CritiquingRoot.critique(this);
          alreadyCritiqued.removeAllElements();
          toCritique.removeAllElements();
          toCritique.addElement(_CritiquingRoot);
          while (toCritique.size() > 0) {
            Object dm = toCritique.elementAt(toCritique.size()-1);
            toCritique.removeElement(dm);
            if (!alreadyCritiqued.contains(dm)) {
              alreadyCritiqued.addElement(dm);
              Agency.applyAllCritics(dm, theDesigner());
              Enumeration subDMs = _cg.gen(dm);
              while (subDMs.hasMoreElements())
                toCritique.addElement(subDMs.nextElement());
            }
          }
        }
      }
      // needs-more-work: limit % CPU time used
      try { _critiquer.sleep(getCritiquingInterval()); }
      catch (InterruptedException ignore) {
        System.out.println("InterruptedException!!!");
      }
    }
  }


  /** Look for potential problems or open issues in the given design. */
  public void critique(Design des) { des.critique(this); }

  ////////////////////////////////////////////////////////////////
  // criticism control

  /** Ask this designer's agency to select which critics should be active. */
  public void determineActiveCritics() {
    _agency.determineActiveCritics(this);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** autoCritique and critiquingInterval are two prameters that
   *  control how the critiquing thread operates. If autoCritique is
   *  false then now critiquing is done in the background. The
   *  critiquingInterval determines how often the critiquing thread
   *  executes. The concept of an interval between runs will become
   *  less important as Argo is redesigned to be more trigger
   *  driven. */

  public boolean getAutoCritique() { return _autoCritique; }
  public void setAutoCritique(boolean b) { _autoCritique = b; }
  public int getCritiquingInterval() { return _critiquingInterval; }
  public void setCritiquingInterval(int i) { _critiquingInterval = i; }

   public static void setCritiquingRoot(Object d) {
     synchronized (_CritiquingRoot) {
       _CritiquingRoot = d;
       theDesigner()._toDoList.removeAllElements();
     }
   }
   public static Object getCritiquingRoot() { return _CritiquingRoot; }

   public ChildGenerator getChildGenerator() { return _cg; }
   public void setChildGenerator(ChildGenerator cg) { _cg = cg; }

  public DecisionModel getDecisionModel() { return _decisions; }
  public Vector getDecisions() { return _decisions.getDecisions(); }
  public GoalModel getGoalModel() { return _goals; }
  //public SkillsModel getSkillsModel() { return _skills; }

  /** ToDoItem's that are posted by the designer are assumed to be
   *  valid until the designer explicitly removes them. Perhaps in the
   *  future the designer could specify a condition to determine when
   *  his items expire.
   * @see ToDoItem
   * @see Critic#stillValid
   */
  public boolean stillValid(ToDoItem i, Designer d) { return true; }


  public boolean supports(Decision d) { return d == Decision.UNSPEC; }
  public Vector getSupportedDecisions() { return null; }

  public boolean supports(Goal g) { return true; }

  public boolean includesKnowledgeType(int knowledgeType) { return true; }


  /** Reply this Designer's ToDoList, a list of pending problems and
   *  issues that the designer might be interested in.
   * @see ToDoList */
  public ToDoList getToDoList() {
    return _toDoList;
  }

  /** Add all the items in the given list to my list. */
  public void addToDoItems(ToDoList list) {
    _toDoList.addAll(list);
  }

  /** Remove all the items in the given list from my list. */
  public void removeToDoItems(ToDoList list) {
    _toDoList.removeAll(list);
  }

  /** Reply the designers personal preferneces. */
  public Properties getPrefs() {
    return _prefs;
  }

  /** Reply true iff the designer is currently considering the given
   *  decison. */
  public boolean isConsidering(String decision) {
    return _decisions.isConsidering(decision);
  }

  /** Record the extent to which the designer is considering the given
   *  decision. */
  public void setDecisionPriority(String decision, int priority) {
    _decisions.setDecisionPriority(decision, priority);
  }

  public void defineDecision(String decision, int priority) {
    _decisions.defineDecision(decision, priority);
  }

  public void startConsidering(String decision) {
    _decisions.startConsidering(decision);
  }

  public void stopConsidering(String decision) {
    _decisions.stopConsidering(decision);
  }

  /** Record the extent to which the designer desires the given goal. */
  public boolean hasGoal(String goal) { return _goals.hasGoal(goal); }

  public void setGoalPriority(String goal, int priority) {
    _goals.setGoalPriority(goal, priority);
  }

  public Object getGoalInfo(String goal){
    return _goals.getGoalInfo(goal);
  }

  public void setGoalInfo(String goal, String info) {
    _goals.setGoalInfo(goal, info);
  }

  public void startDesiring(String goal) { _goals.startDesiring(goal); }
  public void stopDesiring(String goal) { _goals.stopDesiring(goal); }
  public String getExpertEmail() { return _emailAddr; }
  public void setExpertEmail(String addr) { _emailAddr = addr; }
  public void hush() { /* do nothing */ }
  public void unhush() { /* do nothing */ }

  /** Reply the Agency object that is helping this Designer. */
  public Agency getAgency() { return _agency; }

  ////////////////////////////////////////////////////////////////
  // user interface

  /** Inform the human designer using this system that the given
   * ToDoItem should be considered. This can be disruptive if the item
   * is urgent, or (more commonly) it is added to his ToDoList so that
   * he can consider it at his leisure. */
  public void inform(ToDoItem item) {
    if (item.getPriority() >= disruptiveThreshold())
      disruptivelyWarn(item);
    else
      nondisruptivelyWarn(item);
  }

  /** Inform the human designer that there is an urgent ToDoItem that
   *  (s)he must consider before doing any more work.  Currently not
   *  implemented. */
  public synchronized void disruptivelyWarn(ToDoItem item) {
    // open a window or do something with item
  }

  /** Inform the human designer that there is a ToDoItem that is
   *  relevant to his design work, and allow him to consider it on his
   *  own initiative. */
  public synchronized void nondisruptivelyWarn(ToDoItem item) {
    _toDoList.addElement(item);
  }

  /** Used to determine which ToDoItems are urgent. */
  public int disruptiveThreshold() {
    // needs-more-work: check prefs
    return 9;
  }

  public String toString() {
    String printString = super.toString() + " [\n";
    printString += "  " + "decisions: " + _decisions.toString() + "\n";
    printString += "  " + "goals: " + _goals.toString() + "\n";
    printString += "  " + "prefs: " + _prefs.toString() + "\n";
    printString += "  " + "to do: " + _toDoList.toString() + "\n";
    printString += "]\n";
    return printString;
  }

  ////////////////////////////////////////////////////////////////
  // issie resolution

  public void fixIt(ToDoItem item, Object arg) { }
  public boolean canFixIt(ToDoItem item) { return false; }

} /* end class Designer */




