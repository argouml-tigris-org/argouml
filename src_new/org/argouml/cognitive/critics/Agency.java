// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;

/**
 * Agency manages Critics.  Since classes are not really first class
 * objects in java, a singleton instance of Agency is made and passed
 * around as needed.  The Agency keeps a registry of all Critics that
 * should be applied to each type of design material. When a
 * design material instance is critiqued it asks Agency to apply all
 * registered Critic's.  In the current scheme there is a thread that
 * proactively, continuously critiques the Design at hand, even if
 * the user is idle! This is simple and it works.  The disadvantage
 * is that _all_ active critics related to a given design material are
 * applied, regardless of the reason for the critiquing and a lot of
 * CPU time is basically wasted.  <p>
 *
 * TODO: I am moving toward a more reactionary scheme in
 * which specific design manipulations in the editor cause critics
 * relevant to those manipulations to be applied.  This transition is
 * still half done.  Trigger's are the critiquing requests.  The code
 * for triggers is currently dormant (latent?).
 *
 * @author Jason Robbins
 */
public class Agency extends Observable { //implements java.io.Serialization
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(Agency.class);

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * A registery of all critics that are currently loaded into the
     * design environment.
     */
    private static Hashtable criticRegistry = new Hashtable(100);
    private static Vector critics = new Vector();

    //   private static boolean _hot = false;
    /**
     * The main control mechanism for determining which critics should
     * be active.
     */
    private ControlMech controlMech;
    private static Hashtable singletonCritics = new Hashtable(40);

    ////////////////////////////////////////////////////////////////
    // constructor and singleton methdos

    /**
     * Contruct a new Agency instance with the given ControlMech as the
     * main control mechanism for determining which critics should be
     * active.
     *
     * @param cm the given controlMech
     */
    public Agency(ControlMech cm) {
        controlMech = cm;
    }

    /**
     * Contruct a new Agency instance and use a StandardCM as the main
     * control mechanism for determining which critics should be
     * active.
     */
    public Agency() {
        controlMech = new StandardCM();
    }

    /**
     * Since Java does not really suport classes as first class
     * objects, there is one instance of Agency that is passed around as
     * needed.<p>
     *
     * theAgency is actually stored in <code>Designer.theDesigner()</code>.
     *
     * @see Designer#theDesigner
     */
    public static Agency theAgency() {
        Designer dsgr = Designer.theDesigner();
        if (dsgr == null) {
            return null;
	}
        return dsgr.getAgency();
    }
    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @return the registery.
     */
    private static Hashtable getCriticRegistry() {
        return criticRegistry;
    }

    /**
     * @return the critics
     */
    public static Vector getCritics() {
        return critics;
    }

    ////////////////////////////////////////////////////////////////
    // critic registration
    /**
     * @param cr the critic to add/register
     */
    protected static void addCritic(Critic cr) {
        if (critics.contains(cr)) {
            return;
	}
        if (!(cr instanceof CompoundCritic)) {
            critics.addElement(cr);
	} else {
            Vector subs = ((CompoundCritic) cr).getCritics();
            Enumeration subCritics = subs.elements();
            while (subCritics.hasMoreElements()) {
                addCritic((Critic) subCritics.nextElement());
	    }
            return;
        }
    }

    /**
     * @param crClassName the critic class name
     * @param dmClassName the design material class name
     */
    public static void register(String crClassName, String dmClassName) {
        Class dmClass;
        try {
            dmClass = Class.forName(dmClassName);
        } catch (java.lang.ClassNotFoundException e) {
            LOG.error("Error loading dm " + dmClassName, e);
            return;
        }
        Critic cr = (Critic) singletonCritics.get(crClassName);
        if (cr == null) {
            Class crClass;
            try {
                crClass = Class.forName(crClassName);
            } catch (java.lang.ClassNotFoundException e) {
                LOG.error("Error loading cr " + crClassName, e);
                return;
            }
            try {
                cr = (Critic) crClass.newInstance();
            } catch (java.lang.IllegalAccessException e) {
                LOG.error("Error instancating cr " + crClassName, e);
                return;
            } catch (java.lang.InstantiationException e) {
                LOG.error("Error instancating cr " + crClassName, e);
                return;
            }
            singletonCritics.put(crClassName, cr);
            addCritic(cr);
        }
        register(cr, dmClass);
    }

    /**
     * Register a critic in the global table of critics that have been
     * loaded. Critics are associated with one or more design material
     * classes. One way to do registration is in a static initializer of
     * the design material class. But additional (after-market) critics
     * could added through a menu command in some control panel...
     *
     * @param cr the critic to register
     * @param clazz the design material class that is to be criticized
     */
    public static void register(Critic cr, Object clazz) {
        Vector theCritics = (Vector) getCriticRegistry().get(clazz);
        if (theCritics == null) {
            theCritics = new Vector();
            criticRegistry.put(clazz, theCritics);
        }
        theCritics.addElement(cr);
        notifyStaticObservers(cr);
        LOG.debug("Registered: " + theCritics.toString());
        cachedCritics.remove(clazz);
        addCritic(cr);
    }

    private static Hashtable cachedCritics = new Hashtable();

    /**
     * Return a collection of all critics that can be applied to the
     * design material subclass, including inherited critics.
     *
     * @param clazz the design material to criticize
     * @return the collection of critics
     */
    public static Collection criticsForClass(Class clazz) {
        Collection col = (Collection) cachedCritics.get(clazz);
        if (col == null) {
            col = new ArrayList();
	    col.addAll(criticsForSpecificClass(clazz));
	    Collection classes = new ArrayList();
	    if (clazz.getSuperclass() != null) {
		classes.add(clazz.getSuperclass());
	    }
	    if (clazz.getInterfaces() != null) {
		classes.addAll(Arrays.asList(clazz.getInterfaces()));
	    }
	    Iterator it = classes.iterator();
	    while (it.hasNext()) {
		col.addAll(criticsForClass((Class) it.next()));
	    }
	    cachedCritics.put(clazz, col);
        }
                return col;

    }

    /**
     * Return the {@link Vector} of all critics that are directly
     * associated with the given design material subclass.<p>
     *
     * If there aren't any an empty Vector is returned.
     *
     * @param clazz the design material
     * @return the critics
     */
    protected static Vector criticsForSpecificClass(Class clazz) {
        Vector theCritics = (Vector) getCriticRegistry().get(clazz);
        if (theCritics == null) {
            theCritics = new Vector();
            criticRegistry.put(clazz, theCritics);
        }
        return theCritics;
    }

    ////////////////////////////////////////////////////////////////
    // criticism control

    /**
     * Apply all critics that can be applied to the given
     * design material instance as appropriate for the given
     * Designer. <p>
     *
     * I would call this critique, but it causes a compilation error
     * because it conflicts with the instance method critique!
     *
     * @param dm the design material
     * @param d the designer
     * @param reasonCode the reason
     */
    public static void applyAllCritics(
        Object dm,
        Designer d,
        long reasonCode) {
        Class dmClazz = dm.getClass();
        Collection c = criticsForClass(dmClazz);
        applyCritics(dm, d, c, reasonCode);
    }

    /**
     * @param dm the design material
     * @param d the designer
     */
    public static void applyAllCritics(Object dm, Designer d) {
        Class dmClazz = dm.getClass();
        Collection c = criticsForClass(dmClazz);
        applyCritics(dm, d, c, -1L);
    }

    /**
     * @param dm the design material
     * @param d the designer
     * @param theCritics the critics
     * @param reasonCode the reason
     */
    public static void applyCritics(
        Object dm,
        Designer d,
        Collection theCritics,
        long reasonCode) {

        Iterator it = theCritics.iterator();
        while (it.hasNext()) {
            Critic c = (Critic) it.next();
            if (c.isActive() && c.matchReason(reasonCode)) {
                try {
                    c.critique(dm, d);
                } catch (Exception ex) {
                    LOG.error("Disabling critique due to exception\n"
			      + c + "\n" + dm,
			      ex);
                    c.setEnabled(false);
                }
            }
        }
    }

    /**
     * Compute which critics should be active (i.e., they can be
     * applied by applyAllCritics) for a given Designer. <p>
     *
     * TODO: I am setting global data, the
     * isEnabled bit in each critic, based on the needs of one designer.
     * I don't really support more than one Designer.
     *
     * TODO: should loop over simpler vector of critics, not CompoundCritics
     *
     * @param d the designer
     */
    public void determineActiveCritics(Designer d) {
        //     Enumeration clazzEnum = getCriticRegistry().keys();
        //     while (clazzEnum.hasMoreElements()) {
        //       Class clazz = (Class) (clazzEnum.nextElement());
        Enumeration criticEnum = critics.elements();
        while (criticEnum.hasMoreElements()) {
            Critic c = (Critic) (criticEnum.nextElement());
            if (controlMech.isRelevant(c, d)) {
                c.beActive();
            } else {
                c.beInactive();
            }
        }
        //}
    }

    ////////////////////////////////////////////////////////////////
    // triggers
    //
    //   public static void addTrigger(Object dm, Designer dsgr, Object arg) {
    //     Trigger t = new Trigger(dm, dsgr, arg);
    //     getTriggers().addElement(t);
    //     notifyStaticObservers(t);
    //   }
    //
    //   public static void addTrigger(Object dm, Designer dsgr) {
    //     addTrigger(dm, dsgr, null);
    //   }
    //
    //   public static void fireTriggers() {
    //     while (getTriggers().size() > 0)
    //       fireTrigger((Trigger)(getTriggers().firstElement()));
    //   }
    //
    //   /**
    //    * Fire the given trigger and remove all identical triggers from
    //    * the list of pending triggers.
    //    */
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

    /**
     * Let some object recieve notifications when the Agency changes
     * state.  Static observers are normal Observers on the singleton
     * instance of this class.
     *
     * @param obs the notified object
     */
    public static void addStaticObserver(Observer obs) {
        Agency a = theAgency();
        if (a == null) {
            return;
	}
        a.addObserver(obs);
    }

    /**
     * When the agency changes, notify observers.
     *
     * @param o the notified object
     */
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
//  * TODO: This code is not really used yet. Also, shouldn't
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
//
//   boolean equals(Trigger t2) {
//     return _dm == t2._dm && _dsgr == t2._dsgr && _arg == t2._arg;
//   }
//   public String toString() {
//     return "Trigger[" + _dm.toString() + "\n----\n" +
//        (_arg == null ? " " : _arg.toString()) + "]";
//   }
//   void fire() {
//     /* TODO: should take arg into account, apply only some
//      critics */
//     Agency.applyAllCritics(_dm, _dsgr);
//   }
// } /* end class Trigger */
