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

package org.argouml.cognitive.critics;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Vector;

import javax.swing.Icon;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.Decision;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Goal;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.Offender;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.Translator;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;

/**
 * "Abstract" base class for design critics.  Each subclass should define
 * its own predicate method and define its own relevance tags. <p>
 *
 * A critic supports design goals and decisions, which can be adjusted
 * accordingly. It will post todo items which may or may not be relevant to
 * the particular designer.<p>
 *
 * Steps to follow when adding a critic are listed in the Argo
 * cookbook under <a HREF="../cookbook.html#define_critic">
 * define_critic</a>.
 *
 * @author Jason Robbins
 */
public class Critic extends Observable implements Poster, Serializable {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(Critic.class);

    ////////////////////////////////////////////////////////////////
    // constants

    /**
     * PROBLEM_FOUND is used for the result of the check of a critic.
     */
    public static final boolean PROBLEM_FOUND = true;

    /**
     * NO_PROBLEM is used for the result of the check of a critic.
     */
    public static final boolean NO_PROBLEM = false;

    /**
     * The keys of some predefined control records.
     */
    private static final String ENABLED = "enabled";
    private static final String SNOOZE_ORDER = "snoozeOrder";

    /**
     * Type of knowledge that critics can deliver.
     */
    public static final String KT_DESIGNERS =
	Translator.localize("misc.knowledge.designers");

    /**
     * Type of knowledge that critics can deliver.
     */
    public static final String KT_CORRECTNESS =
	Translator.localize("misc.knowledge.correctness");

    /**
     * Type of knowledge that critics can deliver.
     */
    public static final String KT_COMPLETENESS =
	Translator.localize("misc.knowledge.completeness");

    /**
     * Type of knowledge that critics can deliver.
     */
    public static final String KT_CONSISTENCY =
	Translator.localize("misc.knowledge.consistency");

    /**
     * Type of knowledge that critics can deliver.
     */
    public static final String KT_SYNTAX =
	Translator.localize("misc.knowledge.syntax");

    /**
     * Type of knowledge that critics can deliver.
     */
    public static final String KT_SEMANTICS =
	Translator.localize("misc.knowledge.semantics");

    /**
     * Type of knowledge that critics can deliver.
     */
    public static final String KT_OPTIMIZATION =
	Translator.localize("misc.knowledge.optimization");

    /**
     * Type of knowledge that critics can deliver.
     */
    public static final String KT_PRESENTATION =
	Translator.localize("misc.knowledge.presentation");

    /**
     * Type of knowledge that critics can deliver.
     */
    public static final String KT_ORGANIZATIONAL =
	Translator.localize("misc.knowledge.organizational");

    /**
     * Type of knowledge that critics can deliver.
     */
    public static final String KT_EXPERIENCIAL =
	Translator.localize("misc.knowledge.experiential");

    /**
     * Type of knowledge that critics can deliver.
     */
    public static final String KT_TOOL =
	Translator.localize("misc.knowledge.tool");

    /**
     * This function calculates the default url to describe this critic.
     * This syntax is synchronized with:
     * <ol>
     * <li>Tags in the manual.
     * <li>Name of the ArgoUML site.
     * <li>How the manual is deployed on the site.
     * </ol>
     * so this must be updated when any of these change.
     *
     * @return the url in string format
     */
    public final String defaultMoreInfoURL() {
	String clsName = getClass().getName();
	clsName = clsName.substring(clsName.lastIndexOf(".") + 1);
	return "http://argouml-stats.tigris.org/documentation/"
	    + "manual-0.24-single/argomanual.html"
	    + "#critics." + clsName;
    }

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * The priority of the ToDoItem produced.
     */
    private int priority;

    /**
     * The headline of the ToDoItem produced.
     */
    private String headline;

    /**
     * The description of the ToDoItem produced.
     */
    private String description;

    /**
     * The moreInfoURL of the ToDoItem produced.
     */
    private String moreInfoURL;

    /**
     * Arguments used to configure the critic.
     * TODO: Could this be removed from the main critics class?
     */
    private Hashtable args = new Hashtable();

    /**
     * The icon representing the resource.
     */
    public static final Icon DEFAULT_CLARIFIER =
	ResourceLoaderWrapper
	    .lookupIconResource("PostIt0");

    private Icon clarifier = DEFAULT_CLARIFIER;

    /**
     * The decision category that this critic is relevant to. The idea
     * of each critic being relevant to exactly one type of decision
     * is a very simple one.  Subclasses that have more sophisticated
     * decision relevance logic should override isRelevantToDecisions.
     * You can also define new ControlMech's. <p>
     *
     * Decision categories are defined in the DecisionModel of a
     * particular domain.
     *
     * @see ControlMech
     */
    private String decisionCategory;

    private Vector supportedDecisions = new Vector();

    private Vector supportedGoals = new Vector();

    /**
     * The decision type of this critic.  For example, correctness,
     * completeness, consistency, alternative, presentation,
     * optimization, organizational, tool critics, etc.
     */
    private String criticType;

    /**
     * Internal flag that stores the end result of all ControlMech
     * evaluations of this critic.
     */
    private boolean isActive = true;

    /**
     * Control records used in determining if this Critic should be
     * active.
     */
    private Hashtable controlRecs = new Hashtable();

    private ListSet knowledgeTypes = new ListSet();
    private long triggerMask = 0L;

    ////////////////////////////////////////////////////////////////
    // constructor

    /**
     * Construct a new critic instance.  Typically only one instance of
     * each critic class is created and stored in a static variable, as
     * per the Singleton pattern.  Each domain extension should define
     * a static initializer block to make one instance of each critic and
     * call {@link Agency#register(Critic, Object)} with that instance.
     */
    public Critic() {
	/* TODO:  THIS IS A HACK.
	 * A much better way of doing this would be not to start
	 * the critic in the first place.
	 */
	if (Configuration.getBoolean(getCriticKey(), true)) {
	    addControlRec(ENABLED, Boolean.TRUE);
	    isActive = true;
	} else {
	    addControlRec(ENABLED, Boolean.FALSE);
	    isActive = false;
	}
	addControlRec(SNOOZE_ORDER, new SnoozeOrder());
	criticType = "correctness";
	knowledgeTypes.addElement(KT_CORRECTNESS);
	decisionCategory = "Checking";

	moreInfoURL = defaultMoreInfoURL();
	description = "no description is availible";
	headline = "default critic headline (" + getClass().getName() + ")";
	priority = ToDoItem.MED_PRIORITY;
    }

    /**
     * Returns the {@link org.argouml.configuration.ConfigurationKey}
     * that the critic uses to determine if it is enabled or disabled.<p>
     *
     * The string resulting from the ConfigurationKey
     * <code>argo.critic.critic_category.critic_name</code>.<p>
     *
     * <code>critic_category</code> would describe the type of critic and is
     * taken from {@link #getCriticCategory}.<p>
     *
     * <code>critic_name</code> would describe the function of the critic
     * and is taken from {@link #getCriticName}.<p>
     *
     * Some examples:<p>
     * <ul>
     * <li>argo.critic.layout.Overlap
     * <li>argo.critic.uml.ReservedWord
     * <li>argo.critic.java.ReservedWord
     * <li>argo.critic.idl.ReservedWord
     * </ul>
     *
     * @see org.argouml.configuration.Configuration#makeKey(String,
     *         String, String)
     * @see #getCriticCategory
     * @see #getCriticName
     *
     * @since 0.9.4
     *
     * @return the key
     */
    public ConfigurationKey getCriticKey() {
	return Configuration.makeKey("critic",
				     getCriticCategory(),
				     getCriticName());
    }

    /**
     * Returns a default critic category.
     * Critics should override this
     * to provide specific classification information.
     *
     * @since 0.9.4
     *
     * @return a default category
     */
    public String getCriticCategory() {
	return "unclassified";
    }

    /**
     * Returns a default critic name.
     * By default this is the simple class name.
     * Critic implementations should override this
     * to provide a better (more descriptive) string.
     *
     * @since 0.9.4
     *
     * @return a default critic name
     */
    public String getCriticName() {
	return getClass().getName()
	    .substring(getClass().getName().lastIndexOf(".") + 1);
    }

    ////////////////////////////////////////////////////////////////
    // critiquing

    /**
     * Examine the given Object and Designer and, if
     * appropriate, produce one or more ToDoItem's and add them to the
     * offending design material's and the Designer's ToDoList. By
     * default this is basically a simple if-statement that relies on
     * predicate() to determine if there is some appropriate feedback,
     * and toDoItem() to produce the ToDoItem.
     *
     * The predicate() and toDoItem() pair of methods is simple and
     * convient for many critics. More sophisticated critics that
     * produce more than one ToDoItem per critiquing, or that produce
     * ToDoItem's that contain information that was already computed in
     * the predicate, should override critique. If you override this
     * method, you should call super.critique().
     *
     * @see Critic#predicate
     * @see Critic#toDoItem
     *
     * @param dm            the design material
     * @param dsgr          the designer
     */
    public void critique(Object dm, Designer dsgr) {
	// The following debug line is now the single most memory consuming
	// line in the whole of ArgoUML. It allocates approximately 18% of
	// all memory allocated.
	// Suggestions for solutions:
	// Check if there is a LOG.debug(String, String) method that can
	// be used instead.
	// Use two calls.
	// For now I (Linus) just comment it out.
	// LOG.debug("applying critic: " + _headline);
	if (predicate(dm, dsgr)) {
	    // LOG.debug("predicate() returned true, creating ToDoItem");
	    ToDoItem item = toDoItem(dm, dsgr);
	    postItem(item, dm, dsgr);
	}
    }

    /**
     * @param item the todo item
     * @param dm the designmaterial/offender to be informed
     * @param dsgr the designer
     */
    public void postItem(ToDoItem item, Object dm, Designer dsgr) {
	if (dm instanceof Offender) {
	    ((Offender) dm).inform(item);
	}
	dsgr.inform(item);
    }



    /**
     * Perform the Critic's analysis of the design. Subclasses should test
     * the given Object to make sure that it is the type of
     * object that is expected.  Each object in the design registers its
     * own critics with the run-time system. The dm parameter is bound
     * to each design object that registered this critic, one per
     * call. Returning true means that feedback should be delivered to
     * the Designer. By convention, subclasses should return their
     * superclass predicate method if their own predicate would
     * return false.
     *
     * @param dm the design material, which is to be checked
     * @param dsgr the designer
     * @return the critic result
     */
    public boolean predicate(Object dm, Designer dsgr) {
	return false;
    }

    /**
     * Return true iff the given ToDoItem is still valid and should be
     * kept in the given designers ToDoList. Critics that are not
     * enabled should always return false so that their ToDoItems will
     * be removed. Subclasses of Critic that supply multiple offenders
     * should always override this method. <p>
     *
     * By default this method basically asks the critic to again
     * critique the offending Object and then it checks if the
     * resulting ToDoItem is the same as the one already posted. This is
     * simple and it works fine for light-weight critics. Critics that
     * expend a lot of computational effort in making feedback that can
     * be easily check to see if it still holds, should override this
     * method. <p>
     *
     * TODO: Maybe ToDoItem should carry some data to make
     * this method more efficient.
     *
     * @see org.argouml.cognitive.Poster#stillValid(
     * org.argouml.cognitive.ToDoItem, org.argouml.cognitive.Designer)
     */
    public boolean stillValid(ToDoItem i, Designer dsgr) {
	if (!isActive()) {
	    LOG.warn("got to stillvalid while not active");
	    return false;
	}
	if (i.getOffenders().size() != 1) {
	    return true;
	}
	if (predicate(i.getOffenders().firstElement(), dsgr)) {
	    // Now we know that this critic is still valid. What we need to
	    // figure out is if the corresponding to-do item is still valid.
	    // The to-do item is to be replaced if the name of some offender
	    // has changed that affects its description or if the contents
	    // of the list of offenders has changed.
	    // We check that by creating a new ToDoItem and then verifying
	    // that it looks exactly the same.
	    // This really creates a lot of to-do items that goes to waste.
	    ToDoItem item = toDoItem(i.getOffenders().firstElement(), dsgr);
	    return (item.equals(i));
	}
	return false;
    }

    /*
     * @see org.argouml.cognitive.Poster#supports(org.argouml.cognitive.Decision)
     */
    public boolean supports(Decision d) {
	return supportedDecisions.contains(d);
    }

    /*
     * @see org.argouml.cognitive.Poster#getSupportedDecisions()
     */
    public Vector getSupportedDecisions() {
	return supportedDecisions;
    }

    /**
     * @param d the decision
     */
    public void addSupportedDecision(Decision d) {
	supportedDecisions.addElement(d);
    }

    /*
     * @see org.argouml.cognitive.Poster#supports(org.argouml.cognitive.Goal)
     */
    public boolean supports(Goal g) {
        return supportedGoals.contains(g);
    }

    /*
     * @see org.argouml.cognitive.Poster#getSupportedGoals()
     */
    public Vector getSupportedGoals() {
	return supportedGoals;
    }

    /**
     * @param g the goal
     */
    public void addSupportedGoal(Goal g) {
	supportedGoals.addElement(g);
    }

    /*
     * @see org.argouml.cognitive.Poster#containsKnowledgeType(java.lang.String)
     */
    public boolean containsKnowledgeType(String type) {
	return knowledgeTypes.contains(type);
    }

    /**
     * @param type the knowledgetype
     */
    public void addKnowledgeType(String type) {
	knowledgeTypes.addElement(type);
    }

    /**
     * @return the knowledgetypes
     */
    public ListSet getKnowledgeTypes() { return knowledgeTypes; }

    /**
     * @param kt the knowledgetypes
     */
    public void setKnowledgeTypes(ListSet kt) { knowledgeTypes = kt; }

    /**
     * Reset all knowledgetypes, and add the given one.
     *
     * @param t1 the only knowledgetype in string format
     */
    public void setKnowledgeTypes(String t1) {
	knowledgeTypes = new ListSet();
	addKnowledgeType(t1);
    }

    /**
     * Reset all knowledgetypes, and add the given ones.
     *
     * @param t1 a knowledgetype in string format
     * @param t2 a knowledgetype in string format
     */
    public void setKnowledgeTypes(String t1, String t2) {
	knowledgeTypes = new ListSet();
	addKnowledgeType(t1);
	addKnowledgeType(t2);
    }

    /**
     * Reset all knowledgetypes, and add the given ones.
     *
     * @param t1 a knowledgetype in string format
     * @param t2 a knowledgetype in string format
     * @param t3 a knowledgetype in string format
     */
    public void setKnowledgeTypes(String t1, String t2, String t3) {
	knowledgeTypes = new ListSet();
	addKnowledgeType(t1);
	addKnowledgeType(t2);
	addKnowledgeType(t3);
    }

    /**
     * @param s the reason
     * @return the code for the given reason
     */
    public static int reasonCodeFor(String s) {
	return 1 << (s.hashCode() % 62);
    }

    /**
     * @return the trigger mask
     */
    public long getTriggerMask() { return triggerMask; }

    /**
     * @param s the trigger to be added (is ORed into the mask)
     */
    public void addTrigger(String s) {
	int newCode = reasonCodeFor(s);
	triggerMask |= newCode;
    }

    /**
     * @param patternCode the mask to be checked
     * @return true if it matches a trigger
     */
    public boolean matchReason(long patternCode) {
	return (triggerMask == 0) || ((triggerMask & patternCode) != 0);
    }

    /*
     * @see org.argouml.cognitive.Poster#expand(java.lang.String, org.argouml.cognitive.ListSet)
     */
    public String expand(String desc, ListSet offs) {
        return desc;
    }

    /*
     * @see org.argouml.cognitive.Poster#getClarifier()
     */
    public Icon getClarifier() {
	return clarifier;
    }


    ////////////////////////////////////////////////////////////////
    // criticism control

    /**
     * Reply true iff this Critic can execute. This fact is normally
     * determined by a ControlMech.
     *
     * @return true iff this Critic can execute
     */
    public boolean isActive() { return isActive; }

    /**
     * Make this critic active. From now on it can be applied to a
     * design material in critiquing.
     */
    public void beActive() {
	if (!isActive) {
	    Configuration.setBoolean(getCriticKey(), true);
            isActive = true;
            setChanged();
            notifyObservers(this);
	}
    }

    /**
     * Make this critic inactive. From now on it will be idle and will
     * not be applied to a design material in critiquing.
     */
    public void beInactive() {
	if (isActive) {
	    Configuration.setBoolean(getCriticKey(), false);
            isActive = false;
            setChanged();
            notifyObservers(this);
	}
    }
    
    

    /**
     * Add some attribute used by ControlMech to determine if this
     * Critic should be active. Critics store control record so that
     * stateful ControlMech's do not need to store a parallel data
     * structure. But Critic's do not directy use or modify this
     * data.
     *
     * @param name          the key
     * @param controlData   the value
     * @return              the previous value of the specified key
     *                     in this hashtable, or <code>null</code>
     *                     if it did not have one
     */
    public Object addControlRec(String name, Object controlData) {
	return controlRecs.put(name, controlData);
    }

    /**
     * Reply the named control record, or null if not defined.
     *
     * @param name the key
     * @return the value
     */
    public Object getControlRec(String name) {
	return controlRecs.get(name);
    }

    /**
     * This is a convient method for accessing one well-known control
     * record. The enabled control record is a boolean that the user can
     * turn on or off to manually enable or disable this Critic. It is
     * normally combined with other ControlMech determinations with a
     * logic-and.
     *
     * @return true if enabled
     */
    public boolean isEnabled() {
        if (this.getCriticName() != null 
                && this.getCriticName().equals("CrNoGuard")) {
            System.currentTimeMillis();
        }
	return  ((Boolean) getControlRec(ENABLED)).booleanValue();
    }

    /**
     * @param e the value to be set for the key ENABLED
     */
    public void setEnabled(boolean e) {
	Boolean enabledBool = e ? Boolean.TRUE : Boolean.FALSE;
	addControlRec(ENABLED, enabledBool);
    }

    /**
     * Reply the SnoozeOrder that is defined for this critic.
     *
     * @return the snooze order
     */
    public SnoozeOrder snoozeOrder() {
	return (SnoozeOrder) getControlRec(SNOOZE_ORDER);
    }

    /**
     * Disable this Critic for the next few minutes.
     */
    public void snooze() { snoozeOrder().snooze(); }

    /**
     * Lift any previous SnoozeOrder.
     */
    public void unsnooze() { snoozeOrder().unsnooze(); }

    /**
     * Checks if the critic is currently snoozed.
     * @return true if the critic is snoozed
     */
    public boolean isSnoozed() { 
        return snoozeOrder().getSnoozed(); 
    }

    /**
     * Reply true if this Critic is relevant to the decisions that
     * the Designer is considering. By default just asks the Designer if
     * he/she is considering my decisionCategory. Really this is
     * something for a ControlMech to compute, but if a subclass of
     * Critic encapsulates some information you may need to override
     * this method.
     *
     * @param dsgr the designer
     * @return true if relevant
     */
    public boolean isRelevantToDecisions(Designer dsgr) {
	Enumeration elems = getSupportedDecisions().elements();
	while (elems.hasMoreElements()) {
	    Decision d = (Decision) elems.nextElement();
        /* TODO: Make use of the constants defined in the ToDoItem class! */
	    if (d.getPriority() > 0 && d.getPriority() <= getPriority()) {
		return true;
            }
	}
	return false;
    }

    /**
     * Reply true iff this Critic is relevant to the goals that the
     * Designer is trying to achieve. By default, all Critic's are
     * relevant regardless of the GoalModel. Really this is something for a
     * ControlMech to compute, but if a subclass of Critic encapsulates
     * some information you may need to override this method. <p>
     *
     * TODO: I would like a better default action, but goals
     * are typed and their values must be interperted by critics. They
     * are not as generic as the DecisionModel.
     *
     * @param dsgr the designer
     * @return true if relevant
     */
    public boolean isRelevantToGoals(Designer dsgr) {
	return true;
    }

    ////////////////////////////////////////////////////////////////
    // corrective automations, wizards

    /**
     * Create a new Wizard to help the user fix the identified problem. This
     * version assumes subclasses override getWizClass to return the appropriate
     * Class of wizard. Critic subclasses that need to initialize their wizard
     * might override this to call super.makeWizard() and then work with the
     * result.
     *
     * @param item
     *           the todo item
     * @return the wizard
     */
    public Wizard makeWizard(ToDoItem item) {
        Class wizClass = getWizardClass(item);
        // if wizClass is not a subclass of Wizard, print a warning
        if (wizClass != null) {
            try {
                Wizard w = (Wizard) wizClass.newInstance();
                w.setToDoItem(item);
                initWizard(w);
                return w;
            } catch (IllegalAccessException illEx) {
                LOG.error("Could not access wizard: ", illEx);
            } catch (InstantiationException instEx) {
                LOG.error("Could not instantiate wizard: ", instEx);
            }
        }
        return null;
    }

    /**
     * Return the Class of wizard that can fix the problem identifed by
     * this critic.
     * This method returns null, subclasses with wizards should override it.
     *
     * @param item the todo item
     * @return null if no wizard is defined.
     */
    public Class getWizardClass(ToDoItem item) { return null; }


    /**
     * Initialize a newly created wizard with information found by the
     * critic.  This is called right after the wizard is made in
     * makeWizard() and after the wizard's ToDoItem is set.  Any critic
     * that supports wizards should probably override this method, and
     * call super initWizard() first.
     *
     * @param w the wizard
     */
    public void initWizard(Wizard w) { }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * Reply a string used to determine if this critic would be
     * relevant to current design decisions. Strings returned from here
     * are compared to strings in the DecisionModel.
     *
     * @return the decision category
     */
    public String getDecisionCategory() { return decisionCategory; }

    /**
     * Set the decisionCategory, usually done in the constructor. I
     * have not yet thought of a case where dynamically changing the
     * Critic's decisionCategory is useful.
     *
     * @param c the category
     */
    protected void setDecisionCategory(String c) { decisionCategory = c; }

    /**
     * Reply a string used to contol critics according to
     * type. Examples include: correctness, completeness, consistency,
     * optimization, presentation, and alternative.
     *
     * @return the critic knowledge type
     */
    public String getCriticType() { return criticType; }

    /**
     * Reply the headline used in feedback produced by this Critic.
     *
     * @param dm the design material
     * @param dsgr the designer
     * @return the headline
     */
    public String getHeadline(Object dm, Designer dsgr) {
	return getHeadline();
    }

    /**
     * Reply the headline used in feedback produced by this Critic.
     *
     * @param offenders the set of offenders
     * @param dsgr the designer
     * @return the headline
     */
    public String getHeadline(ListSet offenders, Designer dsgr) {
	return getHeadline(offenders.firstElement(), dsgr);
    }

    /**
     * Reply the headline used in feedback produced by this Critic.
     *
     * @return the headline
     */
    public String getHeadline() { return headline; }

    /**
     * Set the headline used in feedback produced by this Critic.
     *
     * @param h the headline
     */
    public void setHeadline(String h) {  headline = h; }

    /**
     * Reply the priority used in feedback produced by this Critic.
     *
     * @param offenders the offenders
     * @param dsgr the designer
     * @return the priority
     */
    public int getPriority(ListSet offenders, Designer dsgr) {
	return priority;
    }

    /**
     * @param p the priority
     */
    public void setPriority(int p) { priority = p; }

    /**
     * @return the priority
     */
    public int getPriority() {
	return priority;
    }

    /**
     * Reply the description used in feedback produced by this Critic.
     *
     * @param offenders the offenders
     * @param dsgr the designer
     * @return the description
     */
    public String getDescription(ListSet offenders, Designer dsgr) {
	return description;
    }

    /**
     * @param d the description
     */
    public void setDescription(String d) {  description = d; }

    /**
     * @return the description
     */
    public String getDescriptionTemplate() {
	return description;
    }

    /**
     * Reply the moreInfoURL used in feedback produced by this Critic.
     *
     * @param offenders the offenders
     * @param dsgr the designer
     * @return the more-info-url
     */
    public String getMoreInfoURL(ListSet offenders, Designer dsgr) {
	return moreInfoURL;
    }

    /**
     * @param m the more-info-url
     */
    public void setMoreInfoURL(String m) {  moreInfoURL = m; }

    /**
     * @return the more-info-url
     */
    public String getMoreInfoURL() {
	return getMoreInfoURL(null, null);
    }

    /**
     * @param name  the key
     * @param value the value
     */
    protected void setArg(String name, Object value) {
	args.put(name, value);
    }

    /**
     * @param name the key
     * @return     the value
     */
    protected Object getArg(String name) {
	return args.get(name);
    }

    /**
     * @return the (key, value) pairs
     */
    public Hashtable  getArgs() { return args; }

    /**
     * @param h the new table of (key, value) pairs
     */
    public void setArgs(Hashtable h) { args = h; }

    ////////////////////////////////////////////////////////////////
    // design feedback

    /**
     * Reply the ToDoItem that the designer should see iff predicate()
     * returns true. By default it just fills in the fields of the
     * ToDoItem from accessor methods of this Critic. Critic Subclasses
     * may override this method or the accessor methods to add computed
     * fields to the ToDoItem.
     *
     * TODO: Critic's may want to add new fields to a
     * ToDoItem to make stillValid more efficent.
     * 
     * @see Critic#critique
     *
     * @param dm
     * @param dsgr the designer
     * @return ToDoItem
     */
    public ToDoItem toDoItem(Object dm, Designer dsgr) {
	return new ToDoItem(this, dm, dsgr);
    }

    ////////////////////////////////////////////////////////////////
    // issue resolution

    /*
     * TODO: Not implemented yet. The idea is that some
     * problems identified by Critic's can be fixed with certain design
     * manipulations (or transforms) that can be applied automatically
     * to resolve the problem. This method replies true iff the given
     * problem can be fixed. The fixIt() method actually does the fix.
     *
     * @see org.argouml.cognitive.Poster#canFixIt(org.argouml.cognitive.ToDoItem)
     * Also 
     * @see Critic#fixIt
     */
    public boolean canFixIt(ToDoItem item) {
	return false;
    }

    /*
     * @see org.argouml.cognitive.Poster#fixIt(org.argouml.cognitive.ToDoItem, java.lang.Object)
     */
    public void fixIt(ToDoItem item, Object arg) { }

    /*
     * Reply a string that describes this Critic. Identical to getCriticName()
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
	return getCriticName();
    }

} /* end class Critic */
