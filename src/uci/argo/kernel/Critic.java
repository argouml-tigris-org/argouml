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

// File: Critic.java
// Classes: Critic
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.argo.kernel;

import java.util.*;
import javax.swing.*;

import uci.util.*;

/** "Abstract" base class for design critics.  Each subclass should define
 *  its own predicate method and define its own relevance tags. <p>
 *
 *  Steps to follow when adding a critic are listed in the Argo
 *  cookbook under <A HREF="../cookbook.html#define_critic">
 *  define_critic</a>. */

public class Critic implements Poster, java.io.Serializable {

  ////////////////////////////////////////////////////////////////
  // constants

  public static final boolean PROBLEM_FOUND = true;
  public static final boolean NO_PROBLEM = false;

  /** The keys of some predefined control records. */
  public static final String ENABLED = "enabled";
  public static final String SNOOZE_ORDER = "snoozeOrder";

  /** Types of knowledge that critics can deliver */
  public static String KT_DESIGNERS = "Designer's";
  public static String KT_CORRECTNESS = "Correctness";
  public static String KT_COMPLETENESS = "Completeness";
  public static String KT_CONSISTENCY = "Consistency";
  public static String KT_SYNTAX = "Syntax";
  public static String KT_SEMANTICS = "Semantics";
  public static String KT_OPTIMIZATION = "Optimization";
  public static String KT_PRESENTATION = "Presentation";
  public static String KT_ORGANIZATIONAL = "Organizational";
  public static String KT_EXPERIENCIAL = "Experiencial";
  public static String KT_TOOL = "Tool";


  /** Some priority constants for ToDoItem's. */
  //public static final int LOWEST_PRIORITY = 1;
  //public static final int DEFAULT_PRIORITY = 5;
  //public static final int HIGHEST_PRIORITY = 10;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The email address of the author/maintainer of this critic. */
  private String _emailAddr;

  /** The priority of the ToDoItem produced. */
  private int _priority;

  /** The headline of the ToDoItem produced. */
  private String _headline;

  /** The description of the ToDoItem produced. */
  private String _description;

  /** The moreInfoURL of the ToDoItem produced. */
  private String _moreInfoURL;

  /** Arguments used to configure the critic. */
  private Hashtable _args = new Hashtable();

  public static Icon DEFAULT_CLARIFIER = Util.loadIconResource("PostIt0");
  protected Icon _clarifier = DEFAULT_CLARIFIER;

  /** The decision category that this critic is relevant to. The idea
   *  of each critic being relevant to exactly one type of decision is
   *  a very simple one.  Subclasses that have more sophisticated
   *  decision relevance logic should override isRelevantToDecisions.
   *  You can also define new ControlMech's. <p>
   *
   *  Decision categories are defined in the DecisionModel of a
   *  particular domain.
   *
   * @see GoalModel
   * @see ControlMech */
  private String _decisionCategory;
  protected Vector _supportedDecisions = new Vector();

  protected Vector _supportedGoals = new Vector();

  /** The decision type of this critic.  For example, correctness,
   *  completeness, consistency, alternative, presentation,
   *  optimization, organizational, tool critics, etc.
   *
   * @see Critic#criticType */
  private String _criticType;

  /** Internal flag that stores the end result of all ControlMech
   *  evaluations of this critic. */
  private boolean _isActive = true;

  /** Control records used in determining if this Critic should be
   *  active. */
  private Hashtable _controlRecs = new Hashtable();

  protected VectorSet _knowledgeTypes = new VectorSet();
  protected long _triggerMask = 0L;

  public static int _numCriticsFired = 0;

  ////////////////////////////////////////////////////////////////
  // constructor

  /** Construct a new critic instance.  Typically only one instance of
   *  each critic class is created and stored in a static variable, as
   *  per the Singleton pattern.  Each domain extension should define
   *  a static initlizer block to make one instance of each critic and
   *  call <TT>Agency.register()</TT> with that instance. */
  public Critic() {
    addControlRec(ENABLED, Boolean.TRUE);
    addControlRec(SNOOZE_ORDER, new SnoozeOrder());
    _criticType = "correctness";
    _knowledgeTypes.addElement("Correctness");
    _decisionCategory = "Checking";
    _emailAddr = "jrobbins@ics.uci.edu";
    _moreInfoURL = "http://ics.uci.edu/~jrobbins";
    _description = "no description is availible";
    _headline = "default critic headline";
    _priority = ToDoItem.MED_PRIORITY;
  }


  ////////////////////////////////////////////////////////////////
  // critiquing

  /** Examine the given Object and Designer and, if
   *  appropriate, produce one or more ToDoItem's and add them to the
   *  offending design material's and the Designer's ToDoList. By
   *  default this is basically a simple if-statement that relies on
   *  predicate() to determine if there is some appropriate feedback,
   *  and toDoItem() to produce the ToDoItem.
   *
   *  The predicate() and toDoItem() pair of methods is simple and
   *  convient for many critics. More sophisticated critics that
   *  produce more than one ToDoItem per critiquing, or that produce
   *  ToDoItem's that contain information that was already computed in
   *  the predicate, should override critique. If you override this
   *  method, you should call super.critique().
   *
   * @see Critic#predicate
   # @see Critic#toDoItem */
  public void critique(Object dm, Designer dsgr) {
    //System.out.println("applying critic: " + _headline);
    if (predicate(dm, dsgr)) {
      //       if (Boolean.getBoolean("debug")) {
      // 	System.out.println(this.toString() + " detected error");
      //       }
      _numCriticsFired++;
      ToDoItem item = toDoItem(dm, dsgr);
      postItem(item, dm, dsgr);
    }
  }

  public void postItem(ToDoItem item, Object dm, Designer dsgr) {
    if (dm instanceof DesignMaterial) ((DesignMaterial)dm).inform(item);
    dsgr.inform(item);
  }



  /** Perform the Critic's analysis of the design. Subclasses should test
   *  the given Object to make sure that it is the type of
   *  object that is expected.  Each object in the design registers its
   *  own critics with the run-time system. The dm parameter is bound
   *  to each design object that registered this critic, one per
   *  call. Returning true means that feedback should be delivered to
   *  the Designer. By convention, subclasses should return their
   *  superclass predicate method if their own predicate would
   *  return false. */
  public boolean predicate(Object dm, Designer dsgr) {
    return false;
  }

  /** Return true iff the given ToDoItem is still valid and should be
   *  kept in the given designers ToDoList. Critics that are not
   *  enabled should always return false so that their ToDoItems will
   *  be removed. Subclasses of Critic that supply multiple offenders
   *  should always override this method. <p>
   *
   *  By default this method basically asks the critic to again
   *  critique the offending Object and then it checks if the
   *  resulting ToDoItem is the same as the one already posted. This is
   *  simple and it works fine for light-weight critics. Critics that
   *  expend a lot of computational effort in making feedback that can
   *  be easily check to see if it still holds, should override this
   *  method. <p>
   *
   *  Needs-More-Work: Maybe ToDoItem should carry some data to make
   *  this method more efficient. */
  public boolean stillValid(ToDoItem i, Designer dsgr) {
    if (!isActive()) { System.out.println("aasd"); return false; }
    if (i.getOffenders().size() != 1) return true;
    if (predicate(i.getOffenders().firstElement(), dsgr)) {
      ToDoItem item = toDoItem(i.getOffenders().firstElement(), dsgr);
      return (item.equals(i));
    }
    return false;
  }

  public boolean supports(Decision d) {
    return _supportedDecisions.contains(d);
  }

  public Vector getSupportedDecisions() {
    return _supportedDecisions;
  }

  public void addSupportedDecision(Decision d) {
    _supportedDecisions.addElement(d);
  }

  public boolean supports(Goal g) { return true; }

  public Vector getSupportedGoals() {
    return _supportedGoals;
  }

  public void addSupportedGoal(Goal g) {
    _supportedGoals.addElement(g);
  }


  public boolean containsKnowledgeType(String type) {
    return _knowledgeTypes.contains(type);
  }
  public void addKnowledgeType(String type) {
    _knowledgeTypes.addElement(type);
  }
  public VectorSet getKnowledgeTypes() { return _knowledgeTypes; }

  public void setKnowledgeTypes(VectorSet kt) { _knowledgeTypes = kt; }
  public void setKnowledgeTypes(String t1) {
    _knowledgeTypes = new VectorSet();
    addKnowledgeType(t1);
  }
  public void setKnowledgeTypes(String t1, String t2) {
    _knowledgeTypes = new VectorSet();
    addKnowledgeType(t1);
    addKnowledgeType(t2);
  }
  public void setKnowledgeTypes(String t1, String t2, String t3) {
    _knowledgeTypes = new VectorSet();
    addKnowledgeType(t1);
    addKnowledgeType(t2);
    addKnowledgeType(t3);
  }

  public static int reasonCodeFor(String s) {
    return 1 << (s.hashCode() % 62);
  }
  
  public long getTriggerMask() { return _triggerMask; }
  public void addTrigger(String s) {
    int newCode = reasonCodeFor(s);
    _triggerMask |= newCode;
  }
  public boolean matchReason(long patternCode) {
    return (_triggerMask == 0) || ((_triggerMask & patternCode) != 0);
  }
  
  public String expand(String desc, VectorSet offs) { return desc; }

  public Icon getClarifier() {
    return _clarifier;
  }


  ////////////////////////////////////////////////////////////////
  // criticism control

  /** Reply true iff this Critic can execute. This fact is normally
   *  determined by a ControlMech. */
  public boolean isActive() { return _isActive; }

  /** Make this critic active. From now on it can be applied to a
   *  design material in critiquing. */
  public void beActive() { _isActive = true; }

  /** Make this critic inactive. From now on it will be idle and will
   *  not be applied to a design material in critiquing. */
  public void beInactive() { _isActive = false; }

  /** Add some attribute used by ControlMech to determine if this
   *  Critic should be active. Critics store control record so that
   *  stateful ControlMech's do not need to store a parallel data
   *  structure. But Critic's do not directy use or modify this
   *  data. */
  public Object addControlRec(String name, Object controlData) {
    return _controlRecs.put(name, controlData);
  }

  /** Reply the named control record, or null if not defined. */
  public Object getControlRec(String name) {
    return _controlRecs.get(name);
  }

  /** This is a convient method for accessing one well-known control
   *  record. The enabled control record is a boolean that the user can
   *  turn on or off to manually enable or disable this Critic. It is
   *  normally combined with other ControlMech determinations with a
   *  logic-and. */
  public boolean isEnabled() {
    return  ((Boolean) getControlRec(ENABLED)).booleanValue();
  }

  public void setEnabled(boolean e) {
    Boolean enabledBool = e ? Boolean.TRUE : Boolean.FALSE;
    addControlRec(ENABLED, enabledBool);
  }

  /** Reply the SnoozeOrder that is defined for this critic. */
  public SnoozeOrder snoozeOrder() {
    return (SnoozeOrder)getControlRec(SNOOZE_ORDER);
  }

  /** Disable this Critic for the next few minutes. */
  public void snooze() { snoozeOrder().snooze(); }

  /** Lift any previous SnoozeOrder. */
  public void unsnooze() { snoozeOrder().unsnooze(); }

  /** Reply true iff this Critic is relevant to the decisions that
   *  the Designer is considering. By default just asks the Designer if
   *  he/she is considering my decisionCategory. Really this is
   *  something for a ControlMech to compute, but if a subclass of
   *  Critic encapsulates some information you may need to override
   *  this method. */
  public boolean isRelevantToDecisions(Designer dsgr) {
    Enumeration enum = getSupportedDecisions().elements();
    while (enum.hasMoreElements()) {
      Decision d = (Decision) enum.nextElement();
      //if (dsgr.isConsidering(d)) return true;
      if (d.getPriority() > 0) return true;
    }
    return false;
  }

  /** Reply true iff this Critic is relevant to the goals that the
   *  Designer is trying to achieve. By default, all Critic's are
   *  relevant regardless of the GoalModel. Really this is something for a
   *  ControlMech to compute, but if a subclass of Critic encapsulates
   *  some information you may need to override this method. <p>
   *
   *  Needs-More-Work: I would like a better default action, but goals
   *  are typed and their values must be interperted by critics. They
   *  are not as generic as the DecisionModel. */
  public boolean isRelevantToGoals(Designer dsgr) {
    return true;
  }

  ////////////////////////////////////////////////////////////////
  // corrective automations, wizards

  /** Create a new Wizard to help the user fix the identified problem.
   *  This version assumes subclasses override getWizClass to return
   *  the appropriate Class of wizard.  Critic subclasses that need to
   *  initialize their wizard might override this to call
   *  super.makeWizard() and then work with the result. */
  public Wizard makeWizard(ToDoItem item) {
    Class wizClass = getWizardClass(item);
    // if wizClass is not a subclass of Wizard, print a warning
    if (wizClass != null) {
      try {
	Wizard w = (Wizard) wizClass.newInstance();
	w.setToDoItem(item);
	initWizard(w);
	return w;
      }
      catch (Exception ex) {
	System.out.println("Could not make wizard: " + item);
      }
    }
    return null;
  }

  /** Return the Class of wizard that can fix the problem identifed by
   *  this critic.  Return null if no wizard is defined.  This method
   *  returns null, subclasses with wizards should override it. */
  public Class getWizardClass(ToDoItem item) { return null; }


  /** Initialize a newly created wizard with information found by the
   *  critic.  This is called right after the wizard is made in
   *  makeWizard() and after the wizard's ToDoItem is set.  Any critic
   *  that supports wizards should probably override this method, and
   *  call super initWizard() first. */
  public void initWizard(Wizard w) { }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Reply a string used to determine if this critic would be
   *  relevant to current design decisions. Strings returned from here
   *  are compared to strings in the DecisionModel. */
  public String getDecisionCategory() { return _decisionCategory; }

  /** Set the decisionCategory, usually done in the constructor. I
   *  have not yet thought of a case where dynamically changing the
   *  Critic's decisionCategory is useful. */
  protected void setDecisionCategory(String c) { _decisionCategory = c; }

  /** Reply a string used to contol critics according to
   *  type. Examples include: correctness, completeness, consistency,
   *  optimization, presentation, and alternative. */
  public String getCriticType() { return _criticType; }

  /** Reply the email address of the person who is the author or
   *  maintainer of this critic. */
  public String getExpertEmail() { return _emailAddr; }
  public void setExpertEmail(String addr) { _emailAddr = addr; }

  /** Reply the headline used in feedback produced by this Critic. */
  public String getHeadline(Object dm, Designer dsgr) {
    return getHeadline();
  }
  public String getHeadline(VectorSet offenders, Designer dsgr) {
    return getHeadline(offenders.firstElement(), dsgr);
  }
  public String getHeadline() { return _headline; }
  public void setHeadline(String h) {  _headline = h; }

  /** Reply the priority used in feedback produced by this Critic. */
  public int getPriority(VectorSet offenders, Designer dsgr) {
    return _priority;
  }
  public void setPriority(int p) { _priority = p; }
  public int getPriority() {
    return _priority;
  }

  /** Reply the description used in feedback produced by this Critic. */
  public String getDescription(VectorSet offenders, Designer dsgr) {
    return _description;
  }
  public void setDescription(String d) {  _description = d; }
  public String getDescriptionTemplate() {
    return _description;
  }

  /** Reply the moreInfoURL used in feedback produced by this Critic. */
  public String getMoreInfoURL(VectorSet offenders, Designer dsgr) {
    return _moreInfoURL;
  }
  public void setMoreInfoURL(String m) {  _moreInfoURL = m; }
  public String getMoreInfoURL() {
    return getMoreInfoURL(null, null);
  }

  protected void setArg(String name, Object value) {
    _args.put(name, value);
  }
  protected Object getArg(String name) {
    return _args.get(name);
  }
  public Hashtable  getArgs() { return _args; }
  public void setArgs(Hashtable h) { _args = h; }

  ////////////////////////////////////////////////////////////////
  // design feedback

  /** Reply the ToDoItem that the designer should see iff predicate()
   *  returns true. By default it just fills in the fields of the
   *  ToDoItem from accessor methods of this Critic. Critic Subclasses
   *  may override this method or the accessor methods to add computed
   *  fields to the ToDoItem.
   *
   *  Needs-More-Work: Critic's may want to add new fields to a
   *  ToDoItem to make stillValid more efficent.
   *
   * @see Critic#critique */
  public ToDoItem toDoItem(Object dm, Designer dsgr) {
    return new ToDoItem(this, dm, dsgr);
  }

  ////////////////////////////////////////////////////////////////
  // issue resolution

  /** Needs-More-Work: Not implemented yet. The idea is that some
   *  problems identified by Critic's can be fixed with certain design
   *  manipulations (or transforms) that can be applied automatically
   *  to resolve the problem. This method replies true iff the given
   *  problem can be fixed. The fixIt() method actually does the fix.
   *
   * @see Critic@fixIt */
  public boolean canFixIt(ToDoItem item) {
    return false;
  }

  /** Needs-More-Work: Not implemented yet. If the given ToDoItem can
   *  be fixed automaically, and the user wants that to happen, then do
   *  it. Obviously, this depends on the specific Critic and
   *  problem. By default this method does nothing.
   *
   * @see Critic#canFixIt */
  public void fixIt(ToDoItem item, Object arg) { }

  /** Reply a string that describes this Critic. Mainly useful for
   *  debugging. */
  public String toString() {
    return this.getClass().getName() + "(" +
      getCriticType() + "," +
      getDecisionCategory() + "," +
      getHeadline() + ")";
  }

} /* end class Critic */
