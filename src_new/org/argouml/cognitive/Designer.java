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



// File: Designer.java
// Classes: Designer
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.cognitive;

import java.util.*;
import java.awt.*;
import java.beans.*;
import javax.swing.*;

import ru.novosoft.uml.*;

import org.tigris.gef.util.*;

import org.argouml.kernel.*;
import org.apache.log4j.Category;
import org.argouml.cognitive.critics.*;

/** This class models the designer who is building a complex design in
 * some application domain and needs continuous feedback to aid in the
 * making of good design decisions.
 * This area needs work. Currently everything is hardcoded.
 */

public class Designer
implements Poster, Runnable, PropertyChangeListener, MElementListener, java.io.Serializable {
    
    protected static Category cat = Category.getInstance(Designer.class);
  ////////////////////////////////////////////////////////////////
  // instance variables

  public static Designer TheDesigner = new Designer();
  public static boolean _userWorking = false;

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
  private int _critiquingInterval = 8000;
  private int _critiqueCPUPercent = 10;
  private boolean _autoCritique = true;

  /** dm's that should be critiqued ASAP. */
  private Vector _hotQueue = new Vector();
  private Vector _hotReasonQueue = new Vector();
  private Vector _addQueue = new Vector();
  private Vector _addReasonQueue = new Vector();
  private Vector _removeQueue = new Vector();
  public static int _longestAdd = 0;
  public static int _longestHot = 0;

  /** dm's that should be critiqued relatively soon. */
  private Vector _warmQueue = new Vector();

  private ChildGenerator _cg = new ChildGenDMElements();
  //TODO: theInstance()

  private static Object _CritiquingRoot;

  protected long _critiqueDuration;

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
    /* TODO really should be a separate class */
    _critiquer = new Thread(this, "CritiquingThread");
    _critiquer.setDaemon(true);
    _critiquer.setPriority(Thread.currentThread().getPriority() - 1);
    _critiquer.start();
    _CritiquingRoot = root;
  }


  /** Continuously select and execute critics against this designer's
   *  design. spawnCritiquer is used to start a Thread that runs
   *  this. */
  public void run() {
//     Vector alreadyCritiqued = new Vector();       // jer added 970911
//     Vector toCritique = new Vector();     // jer added 970911
    while (true) {
      long critiqueStartTime = System.currentTimeMillis();
      long cutoffTime = critiqueStartTime + 3000;
      if (_CritiquingRoot != null && getAutoCritique()) {
        synchronized (this) {
	  //synchronized (_addQueue) {
	    int size = _addQueue.size();
	    for (int i = 0; i < size; i++) {
	      _hotQueue.addElement(_addQueue.elementAt(i));
	      _hotReasonQueue.addElement(_addReasonQueue.elementAt(i));
	    }
	    _addQueue.removeAllElements();
	    _addReasonQueue.removeAllElements();
	    //}
	}
	synchronized (this) {
	  _longestHot = Math.max(_longestHot, _hotQueue.size());
          _agency.determineActiveCritics(this);

          while (_hotQueue.size() > 0) {
            Object dm = _hotQueue.elementAt(0);
	    Long reasonCode = (Long) _hotReasonQueue.elementAt(0);
            _hotQueue.removeElementAt(0);
	    _hotReasonQueue.removeElementAt(0);
	    Agency.applyAllCritics(dm, theDesigner(), reasonCode.longValue());
          }
	    int size = _removeQueue.size();
	    for (int i = 0; i < size; i++)
	      _warmQueue.removeElement(_removeQueue.elementAt(i));
	    _removeQueue.removeAllElements();
	    if (_warmQueue.size() == 0)
	      _warmQueue.addElement(_CritiquingRoot);
	    while (_warmQueue.size() > 0 && System.currentTimeMillis() < cutoffTime) {
	      Object dm = _warmQueue.elementAt(0);
	      _warmQueue.removeElementAt(0);
	      Agency.applyAllCritics(dm, theDesigner());
	      java.util.Enumeration subDMs = _cg.gen(dm);
	      while (subDMs.hasMoreElements()) {
		Object nextDM = subDMs.nextElement();
		if (!(_warmQueue.contains(nextDM)))
		  _warmQueue.addElement(nextDM);
	      }
	      //}
	  }
	}
      }
      _critiqueDuration = System.currentTimeMillis() - critiqueStartTime;
      long cycleDuration = (_critiqueDuration * 100) / _critiqueCPUPercent;
      long sleepDuration = Math.min(cycleDuration - _critiqueDuration, 3000);
      sleepDuration = Math.max(sleepDuration, 1000);
      cat.debug("sleepDuration= " + sleepDuration);
      try { _critiquer.sleep(sleepDuration); }
      catch (InterruptedException ignore) {
        cat.error("InterruptedException!!!", ignore);
      }
    }
  }

  // TODO: what about when objects are first created?
  public synchronized void critiqueASAP(Object dm, String reason) {
    long rCode = Critic.reasonCodeFor(reason);
      if (!_userWorking) return;
      cat.debug("critiqueASAP:" + dm);
      int addQueueIndex = _addQueue.indexOf(dm);
      if (addQueueIndex == -1) {
	_addQueue.addElement(dm);
	Long reasonCodeObj = new Long(rCode);
	_addReasonQueue.addElement(reasonCodeObj);
      }
      else {
	Long reasonCodeObj = (Long) _addReasonQueue.elementAt(addQueueIndex);
	long rc = reasonCodeObj.longValue() | rCode;
	Long newReasonCodeObj = new Long(rc);
	_addReasonQueue.setElementAt(newReasonCodeObj, addQueueIndex);
      }
      _removeQueue.addElement(dm);
      _longestAdd = Math.max(_longestAdd, _addQueue.size());
      //}
  }

  /** Look for potential problems or open issues in the given design. */
  public void critique(Design des) { des.critique(this); }

  public void propertyChange(PropertyChangeEvent pce) {
    critiqueASAP(pce.getSource(), pce.getPropertyName());
  }
	public void propertySet(MElementEvent mee) {
    critiqueASAP(mee.getSource(), ((ru.novosoft.uml.foundation.core.MModelElement)mee.getOldValue()).getName());
	}
	public void listRoleItemSet(MElementEvent mee) {
	}
	public void recovered(MElementEvent mee) {
	}
	public void removed(MElementEvent mee) {
	}
	public void roleAdded(MElementEvent mee) {
	}
	public void roleRemoved(MElementEvent mee) {
	}


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

  /// TODO: change to percentage of CPU time!

  protected long _lastCritique = 0;
  public int getCritiquingInterval() {

    return _critiquingInterval;
  }
  public void setCritiquingInterval(int i) { _critiquingInterval = i; }

   public static void setCritiquingRoot(Object d) {
     _CritiquingRoot = d;
     synchronized (theDesigner()) {
       theDesigner()._toDoList.removeAllElements(); //v71
       theDesigner()._hotQueue.removeAllElements();
       theDesigner()._hotReasonQueue.removeAllElements();
       theDesigner()._addQueue.removeAllElements();
       theDesigner()._addReasonQueue.removeAllElements();
       theDesigner()._removeQueue.removeAllElements();
       theDesigner()._warmQueue.removeAllElements();
     }
     //clear out queues! @@@
   }
   public static Object getCritiquingRoot() { return _CritiquingRoot; }

   public ChildGenerator getChildGenerator() { return _cg; }
   public void setChildGenerator(ChildGenerator cg) { _cg = cg; }

  public DecisionModel getDecisionModel() { return _decisions; }
  public Vector getDecisions() { return _decisions.getDecisions(); }
  public GoalModel getGoalModel() { return _goals; }
  public Vector getGoals() { return _goals.getGoals(); }
  //public SkillsModel getSkillsModel() { return _skills; }

  /** ToDoItem's that are posted by the designer are assumed to be
   *  valid until the designer explicitly removes them. Perhaps in the
   *  future the designer could specify a condition to determine when
   *  his items expire.
   * @see ToDoItem
   * @see org.argouml.cognitive.critics.Critic#stillValid
   */
  public boolean stillValid(ToDoItem i, Designer d) { return true; }

  public static Vector UNSPEC_DECISION_VECTOR = null;
  public static Vector UNSPEC_GOAL_VECTOR = null;
  static {
      UNSPEC_DECISION_VECTOR = new Vector();
      UNSPEC_DECISION_VECTOR.addElement(Decision.UNSPEC);
      UNSPEC_GOAL_VECTOR = new Vector();
      UNSPEC_GOAL_VECTOR.addElement(Goal.UNSPEC);
  }
  public boolean supports(Decision d) { return d == Decision.UNSPEC; }
  public Vector getSupportedDecisions() { return UNSPEC_DECISION_VECTOR; }

  public boolean supports(Goal g) { return true; }
  public Vector getSupportedGoals() { return UNSPEC_GOAL_VECTOR; }

  public boolean containsKnowledgeType(String type) {
    return type.equals("Designer's");
  }

  public String expand(String desc, VectorSet offs) { return desc; }

  public Icon getClarifier() { return null; }

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

  public boolean isConsidering(Decision d) {
    return d.getPriority() > 0;
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

  public void startConsidering(Decision d) {
    _decisions.startConsidering(d);
  }

  public void stopConsidering(String decision) {
    _decisions.stopConsidering(decision);
  }

  public void stopConsidering(Decision d) {
    _decisions.stopConsidering(d);
  }

  /** Record the extent to which the designer desires the given goal. */
  public boolean hasGoal(String goal) { return _goals.hasGoal(goal); }

  public void setGoalPriority(String goal, int priority) {
    _goals.setGoalPriority(goal, priority);
  }

//   public Object getGoalInfo(String goal){
//     return _goals.getGoalInfo(goal);
//   }

//   public void setGoalInfo(String goal, String info) {
//     _goals.setGoalInfo(goal, info);
//   }

  public void startDesiring(String goal) { _goals.startDesiring(goal); }
  public void stopDesiring(String goal) { _goals.stopDesiring(goal); }
  public String getExpertEmail() { return _emailAddr; }
  public void setExpertEmail(String addr) { _emailAddr = addr; }
  public void snooze() { /* do nothing */ }
  public void unsnooze() { /* do nothing */ }

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
    // TODO: check prefs
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
