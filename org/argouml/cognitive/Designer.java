// $Id$
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

import org.argouml.kernel.*;
import org.argouml.cognitive.critics.*;

import org.tigris.gef.util.*;

import java.util.*;
import java.beans.*;
import javax.swing.*;

import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.MElementEvent;
import org.apache.log4j.Category;

/** This class models the designer who is building a complex design in
 * some application domain and needs continuous feedback to aid in the
 * making of good design decisions.
 *
 * <p><strong>This area needs work, especially as it is a
 * central idea of Argo.</strong>
 *
 * <p>Currently everything is hardcoded. what can be configurable??
 *
 * <p>the ToDoList is dependent on this class.
 *
 * <p>This class listens to property changes from ...?
 *
 * <p>This class implements Poster because ...?
 *
 * <p>TODO: implement as singleton?? There are comments that suggest this
 * should be done.
 */
public class Designer
    implements Poster,
        Runnable, // TODO remove/refactor per issue 1024
        PropertyChangeListener,
        MElementListener, // TODO remove.
        java.io.Serializable 
{
    
    protected static Category cat = Category.getInstance(Designer.class);
    
    /** the singleton of this class: TODO: needs to be made private.*/
    public static Designer TheDesigner = new Designer();
    
    /** needs documenting */
    public static boolean _userWorking;
    
    /** needs documenting */
    public static Vector UNSPEC_DECISION_VECTOR;

    /** needs documenting */
    public static Vector UNSPEC_GOAL_VECTOR;
    
    /** needs documenting */
    static {
        UNSPEC_DECISION_VECTOR = new Vector();
        UNSPEC_DECISION_VECTOR.addElement(Decision.UNSPEC);
        UNSPEC_GOAL_VECTOR = new Vector();
        UNSPEC_GOAL_VECTOR.addElement(Goal.UNSPEC);
    }
    
    ////////////////////////////////////////////////////////////////
    // instance variables
    
    /** ToDoList items that are on the designers ToDoList because of
     *  this material. */
    private ToDoList _toDoList;
    
    /** Preferences -- very ill defined */
    private Properties _prefs;
    
    /** The email address where other designers can send this designer
     * email. This is not used yet. */
    private String _emailAddr;
    
    /** The decisions currently being considered by the designer.
     *
     *  <p>Decisions are currently modeled as simple descriptive strings.
     *
     *  <p>Each decision also has a priority number which is ill defined,
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
    
    /** needs documenting */
    private Thread _critiquer;
    /** needs documenting */
    private int _critiquingInterval;
    /** needs documenting */
    private int _critiqueCPUPercent;
    /** needs documenting */
    private boolean _autoCritique;
    
    /** dm's that should be critiqued ASAP. */
    private Vector _hotQueue;
    /** needs documenting */
    private Vector _hotReasonQueue;
    /** needs documenting */
    private Vector _addQueue;
    /** needs documenting */
    private Vector _addReasonQueue;
    /** needs documenting */
    private Vector _removeQueue;
    /** needs documenting */
    public static int _longestAdd;
    /** needs documenting */
    public static int _longestHot;
    
    /** dm's that should be critiqued relatively soon. */
    private Vector _warmQueue;
    
    /** needs documenting */
    private ChildGenerator _cg;
    
    /** needs documenting */
    private static Object _CritiquingRoot;
    
    /** needs documenting */
    protected long _critiqueDuration;
    
    /** needs documenting */
    protected int _critiqueLock;
    
    /** needs documenting */
    protected long _lastCritique;
    
    ////////////////////////////////////////////////////////////////
    // constructor and singeton methods
    
    /** TODO: this is aparently meant to be
     * a singleton class - make this private + getInstance methods...
     */
    private Designer() {
        _decisions = new DecisionModel();
        _goals = new GoalModel();
        _agency = new Agency();
        _prefs = new Properties();
        _toDoList = new ToDoList();
        _toDoList.spawnValidityChecker(this);
        // TODO: make this configurable
        _emailAddr = "users@argouml.tigris.org";
        TheDesigner = this;
        _userWorking = false;
        
        _critiquingInterval = 8000;
        _critiqueCPUPercent = 10;
        _autoCritique = true;
        
        _hotQueue = new Vector();
        _hotReasonQueue = new Vector();
        _addQueue = new Vector();
        _addReasonQueue = new Vector();
        _removeQueue = new Vector();
        _longestAdd = 0;
        _longestHot = 0;
        
        _warmQueue = new Vector();
        
        _cg = new ChildGenDMElements();
        
        _critiqueLock = 0;
        
        _lastCritique = 0;
    }
    
    /** needs documenting */
    public static void theDesigner(Designer d) { TheDesigner = d; }
    /** needs documenting */
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
        
        while (true) {
            
            // local variables - what do they do?
            long critiqueStartTime;
            long cutoffTime;
            int minWarmElements = 5;
            int size;
            
            // why?
            if (_CritiquingRoot != null
		&& getAutoCritique()
		&& _critiqueLock <= 0) {
                
                // why?
                synchronized (this) {
                    critiqueStartTime = System.currentTimeMillis();
                    cutoffTime = critiqueStartTime + 3000;
                    
                    size = _addQueue.size();
                    for (int i = 0; i < size; i++) {
                        _hotQueue.addElement(_addQueue.elementAt(i));
                        _hotReasonQueue.addElement(_addReasonQueue.elementAt(i));
                    }
                    _addQueue.removeAllElements();
                    _addReasonQueue.removeAllElements();
                    
                    _longestHot = Math.max(_longestHot, _hotQueue.size());
                    _agency.determineActiveCritics(this);
                    
                    while (_hotQueue.size() > 0) {
                        Object dm = _hotQueue.elementAt(0);
                        Long reasonCode = (Long) _hotReasonQueue.elementAt(0);
                        _hotQueue.removeElementAt(0);
                        _hotReasonQueue.removeElementAt(0);
                        Agency.applyAllCritics(dm, theDesigner(),
					       reasonCode.longValue());
                    }
                    
                    size = _removeQueue.size();
                    for (int i = 0; i < size; i++)
                        _warmQueue.removeElement(_removeQueue.elementAt(i));
                    _removeQueue.removeAllElements();
                    
                    if (_warmQueue.size() == 0)
                        _warmQueue.addElement(_CritiquingRoot);
                    while (_warmQueue.size() > 0 &&
			   (System.currentTimeMillis() < cutoffTime ||
			    minWarmElements > 0)) {
                        if (minWarmElements > 0)
                            minWarmElements--;
                        Object dm = _warmQueue.elementAt(0);
                        _warmQueue.removeElementAt(0);
                        Agency.applyAllCritics(dm, theDesigner());
                        java.util.Enumeration subDMs = _cg.gen(dm);
                        while (subDMs.hasMoreElements()) {
                            Object nextDM = subDMs.nextElement();
                            if (!(_warmQueue.contains(nextDM)))
                                _warmQueue.addElement(nextDM);
                        }
                    }
                }
            } else {
                critiqueStartTime = System.currentTimeMillis();
            }
            _critiqueDuration = System.currentTimeMillis() - critiqueStartTime;
            long cycleDuration =
		(_critiqueDuration * 100) / _critiqueCPUPercent;
            long sleepDuration =
		Math.min(cycleDuration - _critiqueDuration, 3000);
            sleepDuration = Math.max(sleepDuration, 1000);
            cat.debug("sleepDuration= " + sleepDuration);
            try { Thread.sleep(sleepDuration); }
            catch (InterruptedException ignore) {
                cat.error("InterruptedException!!!", ignore);
            }
        }
    }
    
    /**
     * what does this method do? why is is synchronised?
     *
     * TODO: what about when objects are first created?
     */
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
            Long reasonCodeObj =
		(Long) _addReasonQueue.elementAt(addQueueIndex);
            long rc = reasonCodeObj.longValue() | rCode;
            Long newReasonCodeObj = new Long(rc);
            _addReasonQueue.setElementAt(newReasonCodeObj, addQueueIndex);
        }
        _removeQueue.addElement(dm);
        _longestAdd = Math.max(_longestAdd, _addQueue.size());
    }
    
    /** Look for potential problems or open issues in the given design. */
    public void critique(Design des) { des.critique(this); }
    
    /** performs critique asap */
    public void propertyChange(PropertyChangeEvent pce) {
        critiqueASAP(pce.getSource(), pce.getPropertyName());
    }
    
    /** TODO: remove this */
    public void propertySet(MElementEvent mee) {
        critiqueASAP(mee.getSource(),
		     org.argouml.model.ModelFacade.getName(mee.getOldValue()));
    }
    
    /** TODO: remove this */
    public void listRoleItemSet(MElementEvent mee) {
    }
    
    /** TODO: remove this */
    public void recovered(MElementEvent mee) {
    }
    
    /** TODO: remove this */
    public void removed(MElementEvent mee) {
    }
    
    /** TODO: remove this */
    public void roleAdded(MElementEvent mee) {
    }
    
    /** TODO: remove this */
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
    
    /** see getAutoCritique() */
    public void setAutoCritique(boolean b) { _autoCritique = b; }
    
    /** needs documenting */
    public int getCritiquingInterval() {
        
        return _critiquingInterval;
    }

    /** needs documenting */
    public void setCritiquingInterval(int i) { _critiquingInterval = i; }
    
    /** needs documenting */
    public static void disableCritiquing() {
        synchronized (theDesigner()) {
            theDesigner()._critiqueLock++;
        }
    }
    
    /** needs documenting */
    public static void enableCritiquing() {
        synchronized (theDesigner()) {
            theDesigner()._critiqueLock--;
        }
    }
    
    /** needs documenting */
    public static void clearCritiquing() {
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
    
    /** needs documenting */
    public static void setCritiquingRoot(Object d) {
        synchronized (theDesigner()) {
            _CritiquingRoot = d;
        }
        /*  Don't clear everything here, breaks loading! */
    }

    /** needs documenting */
    public static Object getCritiquingRoot() {
        synchronized (theDesigner()) {
            return _CritiquingRoot;
        }
    }
    
    /** needs documenting */
    public ChildGenerator getChildGenerator() { return _cg; }

    /** needs documenting */
    public void setChildGenerator(ChildGenerator cg) { _cg = cg; }
    
    /** needs documenting */
    public DecisionModel getDecisionModel() { return _decisions; }
    /** needs documenting */
    public Vector getDecisions() { return _decisions.getDecisions(); }
    /** needs documenting */
    public GoalModel getGoalModel() { return _goals; }
    /** needs documenting */
    public Vector getGoals() { return _goals.getGoals(); }
    
    /**
     * This method returns true.
     *
     * <p>ToDoItem's that are posted by the designer are assumed to be
     *  valid until the designer explicitly removes them. Perhaps in the
     *  future the designer could specify a condition to determine when
     *  his items expire.
     *
     * @see ToDoItem
     * @see org.argouml.cognitive.critics.Critic#stillValid
     */
    public boolean stillValid(ToDoItem i, Designer d) { return true; }
    
    /** needs documenting */
    public boolean supports(Decision d) { return d == Decision.UNSPEC; }

    /** needs documenting */
    public Vector getSupportedDecisions() { return UNSPEC_DECISION_VECTOR; }
    
    /** just returns true */
    public boolean supports(Goal g) { return true; }

    /** needs documenting */
    public Vector getSupportedGoals() { return UNSPEC_GOAL_VECTOR; }
    
    /** needs documenting */
    public boolean containsKnowledgeType(String type) {
        return type.equals("Designer's");
    }
    
    /** just returns the descr param */
    public String expand(String desc, VectorSet offs) { return desc; }
    
    /** just returns null */
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
    
    /** needs documenting */
    public boolean isConsidering(Decision d) {
        return d.getPriority() > 0;
    }
    
    /** Record the extent to which the designer is considering the given
     *  decision. */
    public void setDecisionPriority(String decision, int priority) {
        _decisions.setDecisionPriority(decision, priority);
    }
    
    /** needs documenting */
    public void defineDecision(String decision, int priority) {
        _decisions.defineDecision(decision, priority);
    }
    
    /** needs documenting */
    public void startConsidering(String decision) {
        _decisions.startConsidering(decision);
    }
    
    /** needs documenting */
    public void startConsidering(Decision d) {
        _decisions.startConsidering(d);
    }
    
    /** needs documenting */
    public void stopConsidering(String decision) {
        _decisions.stopConsidering(decision);
    }
    
    /** needs documenting */
    public void stopConsidering(Decision d) {
        _decisions.stopConsidering(d);
    }
    
    /** Record the extent to which the designer desires the given goal. */
    public boolean hasGoal(String goal) { return _goals.hasGoal(goal); }
    
    /** needs documenting */
    public void setGoalPriority(String goal, int priority) {
        _goals.setGoalPriority(goal, priority);
    }    

    /** needs documenting */
    public void startDesiring(String goal) { _goals.startDesiring(goal); }

    /** needs documenting */
    public void stopDesiring(String goal) { _goals.stopDesiring(goal); }

    /** needs documenting */
    public String getExpertEmail() { return _emailAddr; }

    /** needs documenting */
    public void setExpertEmail(String addr) { _emailAddr = addr; }

    /** empty */
    public void snooze() { /* do nothing */ }

    /** empty */
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
    
    /**
     * Empty.
     *
     * Inform the human designer that there is an urgent ToDoItem that
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
    
    /**
     * just returns the value 9,
     *
     * Used to determine which ToDoItems are urgent. */
    public int disruptiveThreshold() {
        // TODO: check prefs
        return 9;
    }
    
    /** needs documenting */
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
    // issue resolution
    
    /** empty */
    public void fixIt(ToDoItem item, Object arg) { }
    
    /** just returns false */
    public boolean canFixIt(ToDoItem item) { return false; }
    
    class ChildGenDMElements implements ChildGenerator {
        /** Reply a Enumeration of the children of the given Object */
        public Enumeration gen(Object o) {
            if (o instanceof Design)
                return ((Design) o).elements();
            else
                return EnumerationEmpty.theInstance();
        }
    } /* end class ChildGenDMElements */
    
} /* end class Designer */