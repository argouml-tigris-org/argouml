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



// File: Agency.java
// Classes: Agency, Trigger
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.cognitive.critics;

import java.util.*;

import org.tigris.gef.util.*;

import org.argouml.cognitive.*;

/** Agency manages Critics.  Since classes are not really first class
 *  objects in java, a singleton instance of Agency is made and passed
 *  around as needed.  The Agency keeps a registry of all Critics that
 *  should be applied to each type of design material. When a
 *  design material instance is critiqued it asks Agency to apply all
 *  registered Critic's.  In the current scheme there is a thread that
 *  proactively, continuously critiques the Design at hand, even if
 *  the user is idle! This is simple and it works.  The disadvantage
 *  is that _all_ active critics related to a given design material are
 *  applied, regardless of the reason for the critiquing and a lot of
 *  CPU time is basically wasted.  <p>
 *
 *  Needs-More-Work: I am moving toward a more reactionary scheme in
 *  which specific design manipulations in the editor cause critics
 *  relevant to those manipulations to be applied.  This transition is
 *  still half done.  Trigger's are the critiquing requests.  The code
 *  for triggers is currently dormant (latent?). */

public class Agency extends Observable { //implements java.io.Serialization

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** A registery of all critics that are currently loaded into the
   * design environment. */
  private static Hashtable _criticRegistry = new Hashtable(100);

  private static Vector _critics = new Vector();

//   private static boolean _hot = false;

  /** The main control mechanism for determining which critics should
   * be active. */
  private ControlMech _controlMech;

  public static int _numCriticsApplied = 0;

  private static Hashtable _singletonCritics = new Hashtable(40);

  ////////////////////////////////////////////////////////////////
  // constructor and singleton methdos

  /** Contruct a new Agency instance with the given ControlMech as the
   * main control mechanism for determining which critics should be
   * active. */
  public Agency(ControlMech cm) { _controlMech = cm; }

  /** Contruct a new Agency instance and use a StandardCM as the main
   * control mechanism for determining which critics should be
   * active. */
  public Agency() { _controlMech = new StandardCM(); }

  /** Since Java does not really suport classes as first class
   *  objects, there is one instance of Agency that is passed around as
   *  needed. theAgency is actually stored in
   *  <TT>Designer.theDesigner().</TT>
   *
   * @see Designer#theDesigner */
  public static Agency theAgency() {
    Designer dsgr = Designer.theDesigner();
    if (dsgr == null) return null;
    else return dsgr.getAgency();
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Reply the registery. */
  private static Hashtable getCriticRegistry() { return _criticRegistry; }

  public static Vector getCritics() { return _critics; }

//   public static void setHot(boolean b) { _hot = b; }

  ////////////////////////////////////////////////////////////////
  // critic registration

  protected static void addCritic(Critic cr) {
    if (_critics.contains(cr)) return;
    if (!(cr instanceof CompoundCritic)) _critics.addElement(cr);
    else {
      Vector subs = ((CompoundCritic)cr).getCritics();
      Enumeration enum = subs.elements();
      while (enum.hasMoreElements())
	addCritic((Critic) enum.nextElement());
      return;
    }
  }


  public static void register(String crClassName, String dmClassName) {
    Class dmClass;
    try { dmClass = Class.forName(dmClassName); }
    catch (java.lang.ClassNotFoundException e) {
	System.out.println("Error loading dm " + dmClassName);
	return;
    }
    Critic cr = (Critic) _singletonCritics.get(crClassName);
    if (cr == null) {
      Class crClass;
      try { crClass = Class.forName(crClassName); }
      catch (java.lang.ClassNotFoundException e) {
	System.out.println("Error loading cr " + crClassName);
	return;
      }
      try { cr = (Critic) crClass.newInstance(); }
      catch (java.lang.IllegalAccessException e) {
	System.out.println("Error instancating cr " + crClassName);
	return;
      }
      catch (java.lang.InstantiationException e) {
	System.out.println("Error instancating cr " + crClassName);
	return;
      }
      _singletonCritics.put(crClassName, cr);
      addCritic(cr);
    }
    register(cr, dmClass);
  }

  /** Register a critic in the global table of critics that have been
   * loaded. Critics are associated with one or more design material
   * classes. One way to do registration is in a static initializer of
   * the design material class. But additional (after-market) critics
   * could added thorugh a menu command in some control panel...*/
  public static void register(Critic cr, Class clazz) {
    //System.out.println("registering critic " + c.toString());
    Vector critics = (Vector) getCriticRegistry().get(clazz);
    if (critics == null) {
       critics = new Vector();
       _criticRegistry.put(clazz, critics);
    }
    critics.addElement(cr);
    notifyStaticObservers(cr);
    Dbg.log("debugRegistration","Registered: " + critics.toString());
    _cachedCritics.remove(clazz);
    addCritic(cr);
  }

  protected static Hashtable _cachedCritics = new Hashtable();

  /** Return a Vector of all critics that can be applied to the
   * design material subclass, including inherited critics. */
  public static Vector criticsForClass(Class clazz) {
    Vector critics = (Vector) _cachedCritics.get(clazz);
    if (critics != null) return critics;
    Class origClazz = clazz;
    critics = new Vector();
    while (clazz != null) {
      Vector specificCritics = criticsForSpecificClass(clazz);
      Enumeration cur = specificCritics.elements();
      while (cur.hasMoreElements())
	critics.addElement(cur.nextElement());
      clazz = clazz.getSuperclass();
    }
    _cachedCritics.put(origClazz, critics);
    return critics;
  }

  /** Return a Vector of all critics that are directly associated with
   * the given design material subclass. */
  protected static Vector criticsForSpecificClass(Class clazz) {
    Vector critics = (Vector) getCriticRegistry().get(clazz);
    if (critics == null) {
       critics = new Vector();
       _criticRegistry.put(clazz, critics);
    }
    return critics;
  }

  /** Reply a Vector of all critics that are registered for any
   * design material class. */
//   public static Vector allCritics() {
//     return _critics;
//   }

  ////////////////////////////////////////////////////////////////
  // criticism control

  /** Apply all critics that can be applied to the given
   * design material instance as appropriate for the given
   * Designer. <p>
   *
   * I would call this critique, but it causes a compilation error
   * because it conflicts with the instance method critique! */
  public static void applyAllCritics(Object dm, Designer d, long reasonCode) {
    //System.out.println("applyAllCritics for reason:" + dm);
    Class dmClazz = dm.getClass();
    Vector critics = criticsForClass(dmClazz);
    //System.out.println("critics=" + critics);
    applyCritics(dm, d, critics, reasonCode);
  }

  public static void applyAllCritics(Object dm, Designer d) {
    //System.out.println("applyAllCritics for no reason:" + dm);
    //System.out.println("applying all critics ====================");
    Class dmClazz = dm.getClass();
    Vector critics = criticsForClass(dmClazz);
    //System.out.println("critics=" + critics);
    applyCritics(dm, d, critics, -1L);
  }

  public static void applyCritics(Object dm, Designer d, Vector
				  critics, long reasonCode) {
    int size = critics.size();
//     if (_hot) System.out.println("----------");
//     if (_hot) System.out.println("HOT: " +
// 				 Long.toBinaryString(reasonCode) +
// 				 " " +  dm);
    for (int i = 0; i < size; i++) {
      Critic c = (Critic) critics.elementAt(i);
      if (c.isActive() && c.matchReason(reasonCode)) {
// 	if (_hot) System.out.println("hot: " +
// 				     Long.toBinaryString(c.getTriggerMask()) +
// 				     " " + c.getClass().getName());
	try {
	  //_numCriticsApplied++;
	  c.critique(dm, d);
	}
	catch (Exception ex) {
	  System.out.println("Disabling critique due to exception");
	  System.out.println(c.toString());
	  System.out.println(dm.toString());
	  ex.printStackTrace();
	  System.out.println("----------");
          //
          //   if the critic is going to throw exceptions,
          //       disable it
          //
          c.setEnabled(false);
	}
      }
    }
  }

  /** Compute which critics should be active (i.e., they can be
   * applied by applyAllCritics) for a given Designer. <p>
   *
   * Needs-More-Work: I am setting global data, the
   * isEnabled bit in each critic, based on the needs of one designer.
   * I don't really support more than one Designer. */

  //@ should loop over simpler vector of critics, not CompoundCritics
  public void determineActiveCritics(Designer d) {
    //     Enumeration clazzEnum = getCriticRegistry().keys();
    //     while (clazzEnum.hasMoreElements()) {
    //       Class clazz = (Class) (clazzEnum.nextElement());
    Enumeration criticEnum = _critics.elements();
    while (criticEnum.hasMoreElements()) {
      Critic c = (Critic)(criticEnum.nextElement());
      if (_controlMech.isRelevant(c, d)) {
	//System.out.println("Activated: " + c.toString());
	//Dbg.log("debugActivation","Activated: " + c.toString());
	c.beActive();
      }
      else {
	//System.out.println("Deactivated: " + c.toString());
	//Dbg.log("debugActivation","Deactivated: " + c.toString());
	c.beInactive();
      }
    }
    //}
  }

  ////////////////////////////////////////////////////////////////
  // triggers

//   /** Needs-More-Work */
//   public static void addTrigger(Object dm, Designer dsgr, Object arg) {
//     Trigger t = new Trigger(dm, dsgr, arg);
//     getTriggers().addElement(t);
//     notifyStaticObservers(t);
//   }

//   /** Needs-More-Work */
//   public static void addTrigger(Object dm, Designer dsgr) {
//     addTrigger(dm, dsgr, null);
//   }

//   /** Needs-More-Work */
//   public static void fireTriggers() {
//     while (getTriggers().size() > 0)
//       fireTrigger((Trigger)(getTriggers().firstElement()));
//   }

//   /** Fire the given trigger and remove all identical triggers from
//    * the list of pending triggers. */
//   public static void fireTrigger(Trigger t) {
//     Vector toRemove = new Vector();
//     Vector ts = getTriggers();
//     Enumeration cur = ts.elements();
//     while (cur.hasMoreElements()) {
//       Trigger t2 = (Trigger) cur.nextElement();
//       if (t.equals(t2)) toRemove.addElement(t2);
//     }
//     cur = toRemove.elements();
//     while (cur.hasMoreElements()) {
//       Trigger t2 = (Trigger) cur.nextElement();
//       ts.removeElement(t2);
//     }
//     t.fire();
//   }

  ////////////////////////////////////////////////////////////////
  // notifications and updates

  /** Let some object recieve notifications when the Agency changes
   *  state.  Static observers are normal Observers on the singleton
   *  instance of this class. */
  public static void addStaticObserver(Observer obs) {
    Agency a = theAgency();
    if (a == null) return;
    else a.addObserver(obs);
  }

  /** When the agency changes, notify observers. */
  public static void notifyStaticObservers(Object o) {
    if (theAgency() != null) {
      theAgency().setChanged();
      theAgency().notifyObservers(o);
    }
  }

} /* end class Agency */


// /** This class store information about a design manipulation that
//  * happens in some design editor and then it is matched against a
//  * pattern associated with each Critic. Currently another Trigger
//  * instance is used as the pattern. Null values in either Trigger are
//  * considered wild-card value. <p>
//  *
//  * Needs-More-Work: This code is not really used yet. Also, shouldn't
//  * this be a public class so that the Editor can do something with it? */
// class Trigger {
//   Object _dm;
//   Designer _dsgr;
//   Object _arg;

//   Trigger(Object dm, Designer dsgr, Object arg) {
//     _dm = dm;
//     _dsgr = dsgr;
//     _arg = arg;
//   }

//   /** Needs-More-Work */
//   boolean equals(Trigger t2) {
//     return _dm == t2._dm && _dsgr == t2._dsgr && _arg == t2._arg;
//   }

//   public String toString() {
//     return "Trigger[" + _dm.toString() + "\n----\n" +
//        (_arg == null ? " " : _arg.toString()) + "]";
//   }

//   void fire() {
//     /* needs-more-work: should take arg into account, apply only some
//      critics */
//     Agency.applyAllCritics(_dm, _dsgr);
//   }
// } /* end class Trigger */
