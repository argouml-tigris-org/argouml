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
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.cognitive.critics.Agency;
import org.argouml.cognitive.critics.Critic;
import org.argouml.i18n.Translator;
import org.tigris.gef.util.ChildGenerator;
import org.tigris.gef.util.EnumerationEmpty;
import org.tigris.gef.util.VectorSet;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

/** This class models the designer who is building a complex design in
 * some application domain and needs continuous feedback to aid in the
 * making of good design decisions.
 *
 * <p><strong>This area needs work, especially as it is a
 * central idea of Argo.</strong>
 *
 * <p>Currently everything is hardcoded. What can be configurable??
 *
 * <p>The ToDoList is dependent on this class.
 *
 * <p>This class listens to property changes from ...?
 *
 * <p>A Designer can as well create ToDO Items, such as critics do. Hence he 
 * implements the Poster interface.
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
    private static final Logger LOG = Logger.getLogger(Designer.class);
    
    /** the singleton of this class.*/
    private static Designer theDesignerSingleton = new Designer();
    
    private static boolean userWorking;
    
    private static Vector unspecDecisionVector;
    private static Vector unspecGoalVector;
    
    static {
        unspecDecisionVector = new Vector();
        unspecDecisionVector.addElement(Decision.UNSPEC);
        unspecGoalVector = new Vector();
        unspecGoalVector.addElement(Goal.getUnspecifiedGoal());
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
     *  for selecting and executing critics that are relevant to this
     *  designer on an on going basis. */
    private Agency agency;
    
    /* the clarifying icon for this poster */
    private Icon clarifier = null;
    

    private Thread critiquerThread;

    private int critiquingInterval;

    private int critiqueCPUPercent;
    
    private boolean autoCritique;
    
    /** dm's that should be critiqued ASAP. */
    private Vector hotQueue;
    
    private Vector hotReasonQueue;
    
    private Vector addQueue;
    
    private Vector addReasonQueue;
    
    private Vector removeQueue;
    
    private static int longestAdd;

    private static int longestHot;
    
    /** dm's that should be critiqued relatively soon. */
    private Vector warmQueue;
    
    private ChildGenerator childGenerator;
    
    private static Object critiquingRoot;
    
    private long critiqueDuration;
    
    private int critiqueLock;
    
    private long lastCritique;
    
    ////////////////////////////////////////////////////////////////
    // constructor and singeton methods
    
    /**
     * The constructor.
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
        //TheDesigner = this; // already done above
        userWorking = false;
        
        critiquingInterval = 8000;
        critiqueCPUPercent = 10;
        autoCritique = true;
        
        hotQueue = new Vector();
        hotReasonQueue = new Vector();
        addQueue = new Vector();
        addReasonQueue = new Vector();
        removeQueue = new Vector();
        longestAdd = 0;
        longestHot = 0;
        
        warmQueue = new Vector();
        
        childGenerator = new ChildGenDMElements();
        
        critiqueLock = 0;
        
        lastCritique = 0;
    }
    
    
    /**
     * @param d the designer
     */
    //public static void theDesigner(Designer d) { TheDesigner = d; }
    
    /**
     * @return the designer singleton
     */
    public static Designer theDesigner() { return theDesignerSingleton; }
    
    
    ////////////////////////////////////////////////////////////////
    // critiquing
    
    /** 
     * Start a separate thread to continually select and execute
     * critics that are relevant to this designer's work. 
     * 
     * @param root the rootobject the critiques will check
     */
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
		&& critiqueLock <= 0) {
                
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
                    
                    longestHot = Math.max(longestHot, hotQueue.size());
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
            critiqueDuration = System.currentTimeMillis() - critiqueStartTime;
            long cycleDuration =
		(critiqueDuration * 100) / critiqueCPUPercent;
            long sleepDuration =
		Math.min(cycleDuration - critiqueDuration, 3000);
            sleepDuration = Math.max(sleepDuration, 1000);
            LOG.debug("sleepDuration= " + sleepDuration);
            try { Thread.sleep(sleepDuration); }
            catch (InterruptedException ignore) {
                LOG.error("InterruptedException!!!", ignore);
            }
        }
    }
    
    /**
     * A modelelement has been changed. 
     * Now we give it priority to be checked by the critics ASAP.
     * 
     * TODO: why is is synchronised?
     * TODO: what about when objects are first created?
     *
     * @param dm the design material
     * @param reason the reason
     */
    public synchronized void critiqueASAP(Object dm, String reason) {
        long rCode = Critic.reasonCodeFor(reason);
        if (!userWorking) return; 
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
        longestAdd = Math.max(longestAdd, addQueue.size());
    }
    
    /** 
     * Look for potential problems or open issues in the given design.
     * 
     * @param des the design to be checked
     */
    public void critique(Design des) { des.critique(this); }
    
    /** 
     * Performs critique asap.
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent pce) {
        critiqueASAP(pce.getSource(), pce.getPropertyName());
    }
    
    /** 
     * TODO: remove this
     * 
     * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent mee) {
        critiqueASAP(mee.getSource(),
		     org.argouml.model.ModelFacade.getName(mee.getOldValue()));
    }
    
    /** 
     * TODO: remove this
     *  
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent mee) {
    }
    
    /** 
     * TODO: remove this
     * 
     * @see ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent mee) {
    }
    
    /** 
     * TODO: remove this
     * 
     * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent mee) {
    }
    
    /** 
     * TODO: remove this
     * 
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent mee) {
    }
    
    /** 
     * TODO: remove this
     * 
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
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
    
    /** 
     * autoCritique and critiquingInterval are two prameters that
     * control how the critiquing thread operates. If autoCritique is
     * false then now critiquing is done in the background. The
     * critiquingInterval determines how often the critiquing thread
     * executes. The concept of an interval between runs will become
     * less important as Argo is redesigned to be more trigger
     * driven. 
     * 
     * @return autoCritique
     */
    public boolean getAutoCritique() { return autoCritique; }
    
    /** 
     * @see #getAutoCritique()
     * @param b
     */
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
    
    /**
     * Disable critiquing.
     */
    public static void disableCritiquing() {
        synchronized (theDesigner()) {
            theDesigner().critiqueLock++;
        }
    }
    
    /**
     * Enable critiquing.
     */
    public static void enableCritiquing() {
        synchronized (theDesigner()) {
            theDesigner().critiqueLock--;
        }
    }
    
    /**
     * Clear all critiquing results.
     */
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
    
    /**
     * @param d the critiquing root
     */
    public static void setCritiquingRoot(Object d) {
        synchronized (theDesigner()) {
            critiquingRoot = d;
        }
        /*  Don't clear everything here, breaks loading! */
    }

    /**
     * @return the critiquing root
     */
    public static Object getCritiquingRoot() {
        synchronized (theDesigner()) {
            return critiquingRoot;
        }
    }
    
    /**
     * @return the childgenerator
     */
    public ChildGenerator getChildGenerator() { return childGenerator; }

    /**
     * @param cg the childgenerator
     */
    public void setChildGenerator(ChildGenerator cg) { childGenerator = cg; }
    
    /**
     * @return the decisions
     */
    public DecisionModel getDecisionModel() { return decisions; }
    
    /**
     * @return the decisions
     */
    public Vector getDecisions() { return decisions.getDecisions(); }
    
    /**
     * @return the goals
     */
    public GoalModel getGoalModel() { return goals; }

    /**
     * @return the goals
     */
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
     *
     * @see org.argouml.cognitive.Poster#stillValid(
     * org.argouml.cognitive.ToDoItem, org.argouml.cognitive.Designer)
     * 
     * @param i the todo item
     * @param d the designer
     * @return true if still valid
     */
    public boolean stillValid(ToDoItem i, Designer d) { return true; }
    
    /**
     * @see org.argouml.cognitive.Poster#supports(org.argouml.cognitive.Decision)
     */
    public boolean supports(Decision d) { return d == Decision.UNSPEC; }

    /**
     * @see org.argouml.cognitive.Poster#getSupportedDecisions()
     */
    public Vector getSupportedDecisions() { return unspecDecisionVector; }
    
    /**
     * @see org.argouml.cognitive.Poster#supports(org.argouml.cognitive.Goal)
     */
    public boolean supports(Goal g) { return true; }

    /**
     * @see org.argouml.cognitive.Poster#getSupportedGoals()
     */
    public Vector getSupportedGoals() { return unspecGoalVector; }
    
    /**
     * @see org.argouml.cognitive.Poster#containsKnowledgeType(java.lang.String)
     */
    public boolean containsKnowledgeType(String type) {
        return type.equals("Designer's");
    }
    
    /** 
     * Just returns the descr param.
     * 
     * @see org.argouml.cognitive.Poster#expand(java.lang.String, 
     * org.tigris.gef.util.VectorSet)
     */
    public String expand(String desc, VectorSet offs) { return desc; }
    
    /** 
     * Get the generic clarifier for this designer/poster.
     * 
     * @see org.argouml.cognitive.Poster#getClarifier()
     */
    public Icon getClarifier() { 
        return clarifier;
    }
    
    /** 
     * Get the generic clarifier for this designer/poster.
     * 
     * @param clar the clarifier icon
     */
    public void setClarifier(Icon clar) {
        clarifier = clar;
    }
    
    /** Reply this Designer's ToDoList, a list of pending problems and
     *  issues that the designer might be interested in.
     * @see ToDoList */
    public ToDoList getToDoList() {
        return toDoList;
    }
    
    /** 
     * Add all the items in the given list to my list.
     * 
     * @param list the items to be added
     */
    public void addToDoItems(ToDoList list) {
        toDoList.addAll(list);
    }
    
    /** 
     * Remove all the items in the given list from my list.
     *  
     * @param list the items to be removed
     */
    public void removeToDoItems(ToDoList list) {
        toDoList.removeAll(list);
    }
    
    /** 
     * Reply the designers personal preferneces.
     * Currently not used (?).
     * 
     * @return the preferences
     */
    public Properties getPrefs() {
        return prefs;
    }
    
    /** 
     * Reply true iff the designer is currently considering the given
     * decison. 
     * 
     * @param decision the decision
     * @return true if considered
     */
    public boolean isConsidering(String decision) {
        return decisions.isConsidering(decision);
    }
    
    /**
     * @param d the decision
     * @return true if the given decision is considered
     */
    public boolean isConsidering(Decision d) {
        return d.getPriority() > 0;
    }
    
    /** 
     * Record the extent to which the designer is considering the given
     * decision. 
     * 
     * @param decision the decision
     * @param priority the priority
     */
    public void setDecisionPriority(String decision, int priority) {
        decisions.setDecisionPriority(decision, priority);
    }
    
    /**
     * @param decision the decision
     * @param priority the priority
     */
    public void defineDecision(String decision, int priority) {
        decisions.defineDecision(decision, priority);
    }
    
    /**
     * @param decision the decision
     */
    public void startConsidering(String decision) {
        decisions.startConsidering(decision);
    }
    
    /**
     * @param d the decision
     */
    public void startConsidering(Decision d) {
        decisions.startConsidering(d);
    }
    
    /**
     * @param decision the decision 
     */
    public void stopConsidering(String decision) {
        decisions.stopConsidering(decision);
    }
    
    /**
     * @param d the decision
     */
    public void stopConsidering(Decision d) {
        decisions.stopConsidering(d);
    }
    
    /** 
     * Record the extent to which the designer desires the given goal. 
     * 
     * @param goal the given goal
     * @return true if this goal is desired
     */
    public boolean hasGoal(String goal) { return goals.hasGoal(goal); }
    
    /**
     * @param goal the given goal
     * @param priority the priority
     */
    public void setGoalPriority(String goal, int priority) {
        goals.setGoalPriority(goal, priority);
    }    

    /**
     * @param goal the goal I (me, the designer) desire
     */
    public void startDesiring(String goal) { goals.startDesiring(goal); }

    /**
     * @param goal the goal that is not desired any more
     */
    public void stopDesiring(String goal) { goals.stopDesiring(goal); }

    /**
     * @see org.argouml.cognitive.Poster#getExpertEmail()
     */
    public String getExpertEmail() { return emailAddr; }

    /**
     * @see org.argouml.cognitive.Poster#setExpertEmail(java.lang.String)
     */
    public void setExpertEmail(String addr) { emailAddr = addr; }

    /** empty */
    public void snooze() { /* do nothing */ }

    /** empty */
    public void unsnooze() { /* do nothing */ }
    
    /** 
     * Reply the Agency object that is helping this Designer.
     * 
     * @return my agancy
     */
    public Agency getAgency() { return agency; }
    
    ////////////////////////////////////////////////////////////////
    // user interface
    
    /** 
     * Inform the human designer using this system that the given
     * ToDoItem should be considered. This can be disruptive if the item
     * is urgent, or (more commonly) it is added to his ToDoList so that
     * he can consider it at his leisure.
     * 
     * @param item the todo item
     */
    public void inform(ToDoItem item) {
        if (item.getPriority() >= disruptiveThreshold())
            disruptivelyWarn(item);
        else
            nondisruptivelyWarn(item);
    }
    
    /**
     * Inform the human designer that there is an urgent ToDoItem that
     * (s)he must consider before doing any more work.  Currently not
     * implemented. 
     *
     * @param item the todoitem
     */
    public synchronized void disruptivelyWarn(ToDoItem item) {
        // open a window or do something with item
    }
    
    /** 
     * Inform the human designer that there is a ToDoItem that is
     * relevant to his design work, and allow him to consider it on his
     * own initiative.
     * 
     * @param item the todo item
     */
    public synchronized void nondisruptivelyWarn(ToDoItem item) {
        toDoList.addElement(item);
    }
    
    /**
     * Used to determine which ToDoItems are urgent. Just returns the value 9. 
     * 
     * @return from this priority, we warn disruptively
     */
    public int disruptiveThreshold() {
        // TODO: check prefs
        return 9;
    }
    
    /**
     * This is used in the todo panel, when "By Poster" is chosen for a 
     * manually created todo item.
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        //TODO: This should be the name of the designer that created 
        //      the todoitem, not the current username!
        Object[] msgArgs = {Configuration.getString(Argo.KEY_USER_FULLNAME) };
        return Translator.messageFormat("misc.designer.name", msgArgs);
    }
    
    ////////////////////////////////////////////////////////////////
    // issue resolution
    
    /** 
     * Does not do anything.
     * 
     * @see org.argouml.cognitive.Poster#fixIt(org.argouml.cognitive.ToDoItem, 
     * java.lang.Object)
     */
    public void fixIt(ToDoItem item, Object arg) { }
    
    /** 
     * Just returns false.
     * 
     * @see org.argouml.cognitive.Poster#canFixIt(org.argouml.cognitive.ToDoItem)
     */
    public boolean canFixIt(ToDoItem item) { return false; }
    
    /**
     * @param working true if the user is working 
     * (i.e. this is not the startup phase of ArgoUML)
     */
    public static void setUserWorking(boolean working) {
        userWorking = working;
    }

    /**
     * @return true if the user is working 
     * (i.e. this is not the startup phase of ArgoUML)
     */
    public static boolean isUserWorking() {
        return userWorking;
    }

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
