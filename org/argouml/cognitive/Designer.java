// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.cognitive;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.swing.Icon;

import org.apache.log4j.Logger;

import org.argouml.cognitive.critics.Agency;
import org.argouml.cognitive.critics.Critic;

import org.tigris.gef.util.ChildGenerator;
import org.tigris.gef.util.VectorSet;
import org.tigris.gef.util.EnumerationEmpty;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

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
 * <p>A Designer can as well create ToDO Items, such as critics do. Hence he 
 * implements the Poster interface.
 *
 * <p>TODO: implement as singleton?? There are comments that suggest this
 * should be done.
 *
 * @author Jason Robbins
 */
public class Designer
     implements Poster,
         Runnable, // TODO: remove/refactor per issue 1024
         PropertyChangeListener,
         MElementListener, // TODO: remove.
         java.io.Serializable
{
    private static Logger LOG = Logger.getLogger(Designer.class);
    
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
    private ToDoList toDoList;
    
    /** Preferences -- very ill defined */
    private Properties prefs;
    
    /** The email address where other designers can send this designer
     * email. This is not used yet. */
    private String emailAddr;
    
    /** The decisions currently being considered by the designer.
     *
     *  <p>Decisions are currently modeled as simple descriptive strings.
     *
     *  <p>Each decision also has a priority number which is ill defined,
     *  but positive Ints mean that the designer is considering it. This
     *  explicit representation of what decisions the designer is
     *  interested in at a given moment allows the Agency to select
     *  relevant critics for execution. */
    private DecisionModel decisions;
    
    /** The goals of the designer are likewise used by the Agency to
     *  determine what critics are relevant. */
    private GoalModel goals;
    
    /** Each designer has their own Agency instance that is responsible
     *  for selecting and executing critics that are relevant to thid
     *  designer on an on going basis. */
    private Agency agency;
    
    /* the clarifying icon for this poster */
    private Icon clarifier = null;
    
    /** needs documenting */
    private Thread critiquerThread;
    /** needs documenting */
    private int critiquingInterval;
    /** needs documenting */
    private int critiqueCPUPercent;
    /** needs documenting */
    private boolean autoCritique;
    
    /** dm's that should be critiqued ASAP. */
    private Vector hotQueue;
    /** needs documenting */
    private Vector hotReasonQueue;
    /** needs documenting */
    private Vector addQueue;
    /** needs documenting */
    private Vector addReasonQueue;
    /** needs documenting */
    private Vector removeQueue;
    /** needs documenting */
    public static int _longestAdd;
    /** needs documenting */
    public static int _longestHot;
    
    /** dm's that should be critiqued relatively soon. */
    private Vector warmQueue;
    
    /** needs documenting */
    private ChildGenerator childGenerator;
    
    /** needs documenting */
    private static Object critiquingRoot;
    
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
        decisions = new DecisionModel();
        goals = new GoalModel();
        agency = new Agency();
        prefs = new Properties();
        
        toDoList = ToDoList.getInstance();
        
        toDoList.spawnValidityChecker(this);
        // TODO: make this configurable
        emailAddr = "users@argouml.tigris.org";
        TheDesigner = this;
        _userWorking = false;
        
        critiquingInterval = 8000;
        critiqueCPUPercent = 10;
        autoCritique = true;
        
        hotQueue = new Vector();
        hotReasonQueue = new Vector();
        addQueue = new Vector();
        addReasonQueue = new Vector();
        removeQueue = new Vector();
        _longestAdd = 0;
        _longestHot = 0;
        
        warmQueue = new Vector();
        
        childGenerator = new ChildGenDMElements();
        
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
        /* TODO: really should be a separate class */
        critiquerThread = new Thread(this, "CritiquingThread");
        critiquerThread.setDaemon(true);
        critiquerThread.setPriority(Thread.currentThread().getPriority() - 1);
        critiquerThread.start();
        critiquingRoot = root;
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
            
            // the critiquing thread should wait if disabled.
            synchronized (this) {
                while (!autoCritique) {
                    try {
                        this.wait();
                    } catch (InterruptedException ignore) {
                        LOG.error("InterruptedException!!!", ignore);
                    }
                }
            }
            
            // why?
            if (critiquingRoot != null
//		&& getAutoCritique()
		&& _critiqueLock <= 0) {
                
                // why?
                synchronized (this) {
                    critiqueStartTime = System.currentTimeMillis();
                    cutoffTime = critiqueStartTime + 3000;
                    
                    size = addQueue.size();
                    for (int i = 0; i < size; i++) {
                        hotQueue.addElement(addQueue.elementAt(i));
                        hotReasonQueue.addElement(addReasonQueue.elementAt(i));
                    }
                    addQueue.removeAllElements();
                    addReasonQueue.removeAllElements();
                    
                    _longestHot = Math.max(_longestHot, hotQueue.size());
                    agency.determineActiveCritics(this);
                    
                    while (hotQueue.size() > 0) {
                        Object dm = hotQueue.elementAt(0);
                        Long reasonCode = (Long) hotReasonQueue.elementAt(0);
                        hotQueue.removeElementAt(0);
                        hotReasonQueue.removeElementAt(0);
                        Agency.applyAllCritics(dm, theDesigner(),
					       reasonCode.longValue());
                    }
                    
                    size = removeQueue.size();
                    for (int i = 0; i < size; i++)
                        warmQueue.removeElement(removeQueue.elementAt(i));
                    removeQueue.removeAllElements();
                    
                    if (warmQueue.size() == 0)
                        warmQueue.addElement(critiquingRoot);
                    while (warmQueue.size() > 0
			   && (System.currentTimeMillis() < cutoffTime
			       || minWarmElements > 0)) {
                        if (minWarmElements > 0)
                            minWarmElements--;
                        Object dm = warmQueue.elementAt(0);
                        warmQueue.removeElementAt(0);
                        Agency.applyAllCritics(dm, theDesigner());
                        java.util.Enumeration subDMs = childGenerator.gen(dm);
                        while (subDMs.hasMoreElements()) {
                            Object nextDM = subDMs.nextElement();
                            if (!(warmQueue.contains(nextDM)))
                                warmQueue.addElement(nextDM);
                        }
                    }
                }
            } else {
                critiqueStartTime = System.currentTimeMillis();
            }
            _critiqueDuration = System.currentTimeMillis() - critiqueStartTime;
            long cycleDuration =
		(_critiqueDuration * 100) / critiqueCPUPercent;
            long sleepDuration =
		Math.min(cycleDuration - _critiqueDuration, 3000);
            sleepDuration = Math.max(sleepDuration, 1000);
            LOG.debug("sleepDuration= " + sleepDuration);
            try { Thread.sleep(sleepDuration); }
            catch (InterruptedException ignore) {
                LOG.error("InterruptedException!!!", ignore);
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
        LOG.debug("critiqueASAP:" + dm);
        int addQueueIndex = addQueue.indexOf(dm);
        if (addQueueIndex == -1) {
            addQueue.addElement(dm);
            Long reasonCodeObj = new Long(rCode);
            addReasonQueue.addElement(reasonCodeObj);
        }
        else {
            Long reasonCodeObj =
		(Long) addReasonQueue.elementAt(addQueueIndex);
            long rc = reasonCodeObj.longValue() | rCode;
            Long newReasonCodeObj = new Long(rc);
            addReasonQueue.setElementAt(newReasonCodeObj, addQueueIndex);
        }
        removeQueue.addElement(dm);
        _longestAdd = Math.max(_longestAdd, addQueue.size());
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
        agency.determineActiveCritics(this);
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
    public boolean getAutoCritique() { return autoCritique; }
    
    /** see getAutoCritique() */
    public void setAutoCritique(boolean b) {
	autoCritique = b; 
    
	synchronized (this) {
	    if (autoCritique) {
		this.notifyAll();
	    }
	}
    }
    
    /**
     * Get the Critiquing interval.
     *
     * @return The interval.
     */
    public int getCritiquingInterval() {
        
        return critiquingInterval;
    }

    /**
     * Set the Critiquing Interval.
     *
     * @param i The new interval.
     */
    public void setCritiquingInterval(int i) {
	critiquingInterval = i;
    }
    
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
            theDesigner().toDoList.removeAllElements(); //v71
            theDesigner().hotQueue.removeAllElements();
            theDesigner().hotReasonQueue.removeAllElements();
            theDesigner().addQueue.removeAllElements();
            theDesigner().addReasonQueue.removeAllElements();
            theDesigner().removeQueue.removeAllElements();
            theDesigner().warmQueue.removeAllElements();
        }
        //clear out queues! @@@
    }
    
    /** needs documenting */
    public static void setCritiquingRoot(Object d) {
        synchronized (theDesigner()) {
            critiquingRoot = d;
        }
        /*  Don't clear everything here, breaks loading! */
    }

    /** needs documenting */
    public static Object getCritiquingRoot() {
        synchronized (theDesigner()) {
            return critiquingRoot;
        }
    }
    
    /** needs documenting */
    public ChildGenerator getChildGenerator() { return childGenerator; }

    /** needs documenting */
    public void setChildGenerator(ChildGenerator cg) { childGenerator = cg; }
    
    /** needs documenting */
    public DecisionModel getDecisionModel() { return decisions; }
    /** needs documenting */
    public Vector getDecisions() { return decisions.getDecisions(); }
    /** needs documenting */
    public GoalModel getGoalModel() { return goals; }
    /** needs documenting */
    public Vector getGoals() { return goals.getGoals(); }
    
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
    
    /** get the generic clarifier for this designer/poster */
    public Icon getClarifier() { 
        return clarifier;
    }
    
    /** get the generic clarifier for this designer/poster */
    public void setClarifier(Icon clar) {
        clarifier = clar;
    }
    
    /** Reply this Designer's ToDoList, a list of pending problems and
     *  issues that the designer might be interested in.
     * @see ToDoList */
    public ToDoList getToDoList() {
        return toDoList;
    }
    
    /** Add all the items in the given list to my list. */
    public void addToDoItems(ToDoList list) {
        toDoList.addAll(list);
    }
    
    /** Remove all the items in the given list from my list. */
    public void removeToDoItems(ToDoList list) {
        toDoList.removeAll(list);
    }
    
    /** Reply the designers personal preferneces. */
    public Properties getPrefs() {
        return prefs;
    }
    
    /** Reply true iff the designer is currently considering the given
     *  decison. */
    public boolean isConsidering(String decision) {
        return decisions.isConsidering(decision);
    }
    
    /** needs documenting */
    public boolean isConsidering(Decision d) {
        return d.getPriority() > 0;
    }
    
    /** Record the extent to which the designer is considering the given
     *  decision. */
    public void setDecisionPriority(String decision, int priority) {
        decisions.setDecisionPriority(decision, priority);
    }
    
    /** needs documenting */
    public void defineDecision(String decision, int priority) {
        decisions.defineDecision(decision, priority);
    }
    
    /** needs documenting */
    public void startConsidering(String decision) {
        decisions.startConsidering(decision);
    }
    
    /** needs documenting */
    public void startConsidering(Decision d) {
        decisions.startConsidering(d);
    }
    
    /** needs documenting */
    public void stopConsidering(String decision) {
        decisions.stopConsidering(decision);
    }
    
    /** needs documenting */
    public void stopConsidering(Decision d) {
        decisions.stopConsidering(d);
    }
    
    /** Record the extent to which the designer desires the given goal. */
    public boolean hasGoal(String goal) { return goals.hasGoal(goal); }
    
    /** needs documenting */
    public void setGoalPriority(String goal, int priority) {
        goals.setGoalPriority(goal, priority);
    }    

    /** needs documenting */
    public void startDesiring(String goal) { goals.startDesiring(goal); }

    /** needs documenting */
    public void stopDesiring(String goal) { goals.stopDesiring(goal); }

    /** needs documenting */
    public String getExpertEmail() { return emailAddr; }

    /** needs documenting */
    public void setExpertEmail(String addr) { emailAddr = addr; }

    /** empty */
    public void snooze() { /* do nothing */ }

    /** empty */
    public void unsnooze() { /* do nothing */ }
    
    /** Reply the Agency object that is helping this Designer. */
    public Agency getAgency() { return agency; }
    
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
        toDoList.addElement(item);
    }
    
    /**
     * just returns the value 9,
     *
     * Used to determine which ToDoItems are urgent. */
    public int disruptiveThreshold() {
        // TODO: check prefs
        return 9;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        //String printString = super.toString() + " [\n";
        //printString += "  " + "decisions: " + _decisions.toString() + "\n";
        //printString += "  " + "goals: " + _goals.toString() + "\n";
        //printString += "  " + "prefs: " + _prefs.toString() + "\n";
        //printString += "  " + "to do: " + _toDoList.toString() + "\n";
        //printString += "]\n";
        
        // made change in respect to ToDo List Tree
        String printString = "Designer [name?]";
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
