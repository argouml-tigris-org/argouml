// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import java.beans.PropertyChangeSupport;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Icon;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.cognitive.critics.Agency;
import org.argouml.cognitive.critics.Critic;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.model.InvalidElementException;
import org.tigris.gef.util.ChildGenerator;
import org.tigris.gef.util.EnumerationEmpty;


/**
 * This class models the designer who is building a complex design in
 * some application domain and needs continuous feedback to aid in the
 * making of good design decisions.<p>
 *
 * <strong>This area needs work, especially as it is a
 * central idea of Argo.</strong><p>
 *
 * Currently (almost) everything is hardcoded. What can be configurable??<p>
 *
 * The ToDoList is dependent on this class.<p>
 *
 * This class listens to property changes from ...?<p>
 *
 * A Designer can as well create ToDO Items, such as critics do. Hence he
 * implements the Poster interface.
 *
 * @author Jason Robbins
 */
public final class Designer
     implements Poster,
         Runnable,
         PropertyChangeListener {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(Designer.class);

    /**
     * the singleton of this class.
     */
    private static Designer theDesignerSingleton = new Designer();

    private static boolean userWorking;

    private static Vector<Decision> unspecDecisionVector;
    private static Vector<Goal> unspecGoalVector;

    private static Action saveAction;

    static {
        unspecDecisionVector = new Vector<Decision>();
        unspecDecisionVector.addElement(Decision.UNSPEC);
        unspecGoalVector = new Vector<Goal>();
        unspecGoalVector.addElement(Goal.getUnspecifiedGoal());
    }

    /**
     * The key to remember persistently the latest choice made
     * for the menuitem Toggle Auto-Critique.
     */
    public static final ConfigurationKey AUTO_CRITIQUE =
        Configuration.makeKey("cognitive", "autocritique");

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * ToDoList items that are on the designers ToDoList because of
     * this material.
     */
    private ToDoList toDoList;

    /**
     * Preferences -- very ill defined.
     */
    private Properties prefs;

    /**
     * The designerName is the name of the current user, as he can enter in the
     * menuitem Edit->Settings...->User->Full Name.<p>
     *
     * The designerName gets updated when the user enters a new name.
     */
    private String designerName;

    /**
     * The decisions currently being considered by the designer.<p>
     *
     * Decisions are currently modeled as simple descriptive strings.<p>
     *
     * Each decision also has a priority number which is ill defined,
     * but positive Ints mean that the designer is considering it. This
     * explicit representation of what decisions the designer is
     * interested in at a given moment allows the Agency to select
     * relevant critics for execution.
     */
    private DecisionModel decisions;

    /**
     * The goals of the designer are likewise used by the Agency to
     * determine what critics are relevant.
     */
    private GoalModel goals;

    /**
     * Each designer has their own Agency instance that is responsible
     * for selecting and executing critics that are relevant to this
     * designer on an on going basis.
     */
    private Agency agency;

    /**
     * The clarifying icon for this poster.
     */
    private Icon clarifier;


    private Thread critiquerThread;

    private int critiquingInterval;

    private int critiqueCPUPercent;

   /**
     * dm's that should be critiqued ASAP.
     */
    private Vector<Object> hotQueue;

    private Vector<Long> hotReasonQueue;

    private Vector<Object> addQueue;

    private Vector<Long> addReasonQueue;

    private Vector<Object> removeQueue;

    private static int longestAdd;

    private static int longestHot;

    /**
     * dm's that should be critiqued relatively soon.
     */
    private Vector<Object> warmQueue;

    private ChildGenerator childGenerator;

    private static Object critiquingRoot;

    private long critiqueDuration;

    private int critiqueLock;

    private static PropertyChangeSupport pcs;

    /**
     * Property Names.
     */
    public static final String MODEL_TODOITEM_ADDED =
        "MODEL_TODOITEM_ADDED";

    /**
     * Property Names.
     */
    public static final String MODEL_TODOITEM_DISMISSED =
        "MODEL_TODOITEM_DISMISSED";

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

        toDoList = new ToDoList();
        toDoList.spawnValidityChecker(this);

        userWorking = false;
        
        critiquingInterval = 8000;
        critiqueCPUPercent = 10;

        hotQueue = new Vector<Object>();
        hotReasonQueue = new Vector<Long>();
        addQueue = new Vector<Object>();
        addReasonQueue = new Vector<Long>();
        removeQueue = new Vector<Object>();
        longestAdd = 0;
        longestHot = 0;

        warmQueue = new Vector<Object>();

        childGenerator = new EmptyChildGenerator();

        critiqueLock = 0;
    }

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

    /**
     * Continuously select and execute critics against this designer's
     * design. {@link #spawnCritiquer(Object)} is used to start a
     * Thread that runs this.
     */
    public void run() {
        try {
            while (true) {

                // local variables - what do they do?
                long critiqueStartTime;
                long cutoffTime;
                int minWarmElements = 5;
                int size;

                // the critiquing thread should wait if disabled.
                synchronized (this) {
                    while (!Configuration.getBoolean(
                            Designer.AUTO_CRITIQUE, true)) {
                        try {
                            this.wait();
                        } catch (InterruptedException ignore) {
                            LOG.error("InterruptedException!!!", ignore);
                        }
                    }
                }

                // why?
                if (critiquingRoot != null
//                      && getAutoCritique()
                        && critiqueLock <= 0) {

                    // why?
                    synchronized (this) {
                        critiqueStartTime = System.currentTimeMillis();
                        cutoffTime = critiqueStartTime + 3000;

                        size = addQueue.size();
                        for (int i = 0; i < size; i++) {
                            hotQueue.addElement(addQueue.elementAt(i));
                            hotReasonQueue.addElement(addReasonQueue
                                    .elementAt(i));
                        }
                        addQueue.removeAllElements();
                        addReasonQueue.removeAllElements();

                        longestHot = Math.max(longestHot, hotQueue.size());
                        agency.determineActiveCritics(this);

                        while (hotQueue.size() > 0) {
                            Object dm = hotQueue.elementAt(0);
                            Long reasonCode =
                                    hotReasonQueue.elementAt(0);
                            hotQueue.removeElementAt(0);
                            hotReasonQueue.removeElementAt(0);
                            Agency.applyAllCritics(dm, theDesigner(),
                                    reasonCode.longValue());
                        }

                        size = removeQueue.size();
                        for (int i = 0; i < size; i++) {
                            warmQueue.removeElement(removeQueue.elementAt(i));
                        }
                        removeQueue.removeAllElements();

                        if (warmQueue.size() == 0) {
                            warmQueue.addElement(critiquingRoot);
                        }
                        while (warmQueue.size() > 0
                                && (System.currentTimeMillis() < cutoffTime
                                        || minWarmElements > 0)) {
                            if (minWarmElements > 0) {
                                minWarmElements--;
                            }
                            Object dm = warmQueue.elementAt(0);
                            warmQueue.removeElementAt(0);
                            try {
                                Agency.applyAllCritics(dm, theDesigner());
                                java.util.Enumeration subDMs =
                                        childGenerator.gen(dm);
                                while (subDMs.hasMoreElements()) {
                                    Object nextDM = subDMs.nextElement();
                                    if (!(warmQueue.contains(nextDM))) {
                                        warmQueue.addElement(nextDM);
                                    }
                                }
                            } catch (InvalidElementException e) {
                                // Don't let a transient error kill the thread
                                LOG.warn("Element " + dm
                                        + "caused an InvalidElementException.  "
                                        + "Ignoring for this pass.");
                            }
                        }
                    }
                } else {
                    critiqueStartTime = System.currentTimeMillis();
                }
                critiqueDuration =
                        System.currentTimeMillis() - critiqueStartTime;
                long cycleDuration =
                    (critiqueDuration * 100) / critiqueCPUPercent;
                long sleepDuration =
                    Math.min(cycleDuration - critiqueDuration, 3000);
                sleepDuration = Math.max(sleepDuration, 1000);
                LOG.debug("sleepDuration= " + sleepDuration);
                try {
                    Thread.sleep(sleepDuration);
                } catch (InterruptedException ignore) {
                    LOG.error("InterruptedException!!!", ignore);
                }
            }
        } catch (Exception e) {
            LOG.error("Critic thread killed by exception", e);
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
        if (!userWorking) {
	    return;
	}
        // TODO: Should we be doing anything on deleted elements?
        // This throws an exception on remove events. - skip for now - tfm
        if ("remove".equals(reason)) {
            return;
        }
        LOG.debug("critiqueASAP:" + dm);
        int addQueueIndex = addQueue.indexOf(dm);
        if (addQueueIndex == -1) {
            addQueue.addElement(dm);
            Long reasonCodeObj = new Long(rCode);
            addReasonQueue.addElement(reasonCodeObj);
        } else {
            Long reasonCodeObj =
		addReasonQueue.elementAt(addQueueIndex);
            long rc = reasonCodeObj.longValue() | rCode;
            Long newReasonCodeObj = new Long(rc);
            addReasonQueue.setElementAt(newReasonCodeObj, addQueueIndex);
        }
        removeQueue.addElement(dm);
        longestAdd = Math.max(longestAdd, addQueue.size());
    }

    /**
     * Look for potential problems or open issues in the given design.
     * This is currently done by invoking the Agency.
     *
     * @param des the design to be checked
     */
    public void critique(Object des) {
        Agency.applyAllCritics(des, this);
    }

    /**
     * Adds a property change listener.
     *
     * @param pcl
     *           The property change listener to add
     */
    public static void addListener(PropertyChangeListener pcl) {
        if (pcs == null) {
            pcs = new PropertyChangeSupport(theDesigner());
        }
        LOG.debug("addPropertyChangeListener(" + pcl + ")");
        pcs.addPropertyChangeListener(pcl);
    }

    /**
     * Removes a property change listener.
     *
     * @param p
     *           The class to remove as a property change listener.
     */
    public static void removeListener(PropertyChangeListener p) {
        if (pcs != null) {
            LOG.debug("removePropertyChangeListener()");
            pcs.removePropertyChangeListener(p);
        }
    }

    /**
     * Setter for saveAction.
     *
     * @param theSaveAction The new saveAction.
     */
    public static void setSaveAction(Action theSaveAction) {
    	saveAction = theSaveAction;
    }

    /**
     * @param property the property name
     * @param oldValue the old value
     * @param newValue the new value
     */
    public static void firePropertyChange(String property, Object oldValue,
            Object newValue) {
        if (pcs != null) {
            pcs.firePropertyChange(property, oldValue, newValue);
        }
        if (MODEL_TODOITEM_ADDED.equals(property)
                || MODEL_TODOITEM_DISMISSED.equals(property)) {
            if (saveAction != null) {
                saveAction.setEnabled(true);
            }
        }
    }

    /*
     * Performs critique asap.
     *
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent pce) {
        if (pce.getPropertyName().equals(Argo.KEY_USER_FULLNAME.getKey())) {
            designerName = pce.getNewValue().toString();
        } else {
            critiqueASAP(pce.getSource(), pce.getPropertyName());
        }
    }

    ////////////////////////////////////////////////////////////////
    // criticism control

    /**
     * Ask this designer's agency to select which critics should be active.
     */
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
    public boolean getAutoCritique() {
        return Configuration.getBoolean(Designer.AUTO_CRITIQUE, true);
    }

    /**
     * @see #getAutoCritique()
     * @param b true to set auto critique on, false for off
     */
    public void setAutoCritique(boolean b) {
        Configuration.setBoolean(Designer.AUTO_CRITIQUE, b);
	synchronized (this) {
	    if (b) {
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
        /* Don't clear everything here, breaks loading! */
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
     * @return the goals
     */
    public GoalModel getGoalModel() { return goals; }

    /**
     * @return the goals
     */
    public Vector getGoals() { return goals.getGoals(); }

    /**
     * This method returns true.<p>
     *
     * ToDoItem's that are posted by the designer are assumed to be
     * valid until the designer explicitly removes them. Perhaps in the
     * future the designer could specify a condition to determine when
     * his items expire.
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

    /*
     * @see org.argouml.cognitive.Poster#supports(org.argouml.cognitive.Decision)
     */
    public boolean supports(Decision d) { return d == Decision.UNSPEC; }

    /*
     * @see org.argouml.cognitive.Poster#getSupportedDecisions()
     */
    public Vector<Decision> getSupportedDecisions() { 
        return unspecDecisionVector; 
    }

    /*
     * @see org.argouml.cognitive.Poster#supports(org.argouml.cognitive.Goal)
     */
    public boolean supports(Goal g) { return true; }

    /*
     * @see org.argouml.cognitive.Poster#getSupportedGoals()
     */
    public Vector<Goal> getSupportedGoals() { 
        return unspecGoalVector; 
    }

    /*
     * @see org.argouml.cognitive.Poster#containsKnowledgeType(java.lang.String)
     */
    public boolean containsKnowledgeType(String type) {
        return type.equals("Designer's");
    }

    /*
     * Just returns the descr param.
     *
     * @see org.argouml.cognitive.Poster#expand(java.lang.String, ListSet)
     */
    public String expand(String desc, ListSet offs) {
        return desc;
    }

    /*
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

    /**
     * @return this Designer's ToDoList, a list of pending problems and
     * issues that the designer might be interested in.
     *
     * @see ToDoList
     */
    public ToDoList getToDoList() {
        return toDoList;
    }

//    /**
//     * Add all the items in the given list to my list.
//     *
//     * @param list the items to be added
//     */
//    public void addToDoItems(ToDoList list) {
//        toDoList.addAll(list);
//    }
//
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

    /*
     * @see org.argouml.cognitive.Poster#snooze()
     */
    public void snooze() { /* do nothing */ }

    /*
     * @see org.argouml.cognitive.Poster#unsnooze()
     */
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
        toDoList.addElement(item);
    }

    /**
     * set the name of this designer.
     * @param name the designer name
     */
    public void setDesignerName(String name) {
        designerName = name;
    }

    /**
     * query the name of the designer.
     * @return the designer name
     */
    public String getDesignerName() {
        return designerName;
    }

    /*
     * This is used in the todo panel, when "By Poster" is chosen for a
     * manually created todo item.
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        //TODO: This should be the name of the designer that created
        //      the todoitem, not the current username!
        return getDesignerName();
    }

    ////////////////////////////////////////////////////////////////
    // issue resolution

    /*
     * Does not do anything.
     *
     * @see org.argouml.cognitive.Poster#fixIt(org.argouml.cognitive.ToDoItem,
     * java.lang.Object)
     */
    public void fixIt(ToDoItem item, Object arg) { }

    /*
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


    /**
     * ChildGenerator which always returns an empty enumeration.
     * @author MarkusK
     *
     */
    static class EmptyChildGenerator implements ChildGenerator {
        /**
         * Reply a Enumeration of the children of the given Object.
	 *
	 * @param o The object.
	 * @return the Enumeration.
         */
        public Enumeration gen(Object o) {
            return EnumerationEmpty.theInstance();
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = 7599621170029351645L;
    } /* end class ChildGenDMElements */

    /**
     * The UID.
     */
    private static final long serialVersionUID = -3647853023882216454L;
} /* end class Designer */
