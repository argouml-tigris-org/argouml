// $Id: CheckManager.java 11340 2006-10-25 19:17:46Z thn $
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

package org.argouml.cognitive.checklist;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * The CheckManager keeps track of which Checklists should be
 * presented for a given design material.  CheckManager also keeps
 * track of which CheckItem's are checked off for a given design
 * element.
 *
 * @author Jason Robbins
 */
public class CheckManager implements Serializable {

    ////////////////////////////////////////////////////////////////
    // static variables

    /**
     * List of checklists.
     *
     * Indexed on the object type of the element that this checklist is
     * appropriate for.
     */
    private static Hashtable lists = new Hashtable();

    /**
     * List of ChecklistStatus:es.
     *
     * Indexed on the model element itself.
     * TODO: Should use weak references so that this is forgotten about
     * when the object is removed.
     */
    private static Hashtable statuses = new Hashtable();

    /**
     * Constructor.
     */
    public CheckManager() { }

    ////////////////////////////////////////////////////////////////
    // static accessors

    /**
     * Gets the checklist for an element.
     *
     * @param dm is the element
     * @return a checklist
     */
    public static Checklist getChecklistFor(Object dm) {
        Checklist cl;

	java.lang.Class cls = dm.getClass();
	while (cls != null) {
	    cl = lookupChecklist(cls);
	    if (cl != null) {
		return cl;
	    }
	    cls = cls.getSuperclass();
	}
	return null;
    }

    /**
     * Find an element in the list.
     *
     * This is a little more complex than the simple lookup since it might be
     * that we are indexing with a class and the list contains interfaces.
     *
     * Since the hashtable lookup is a lot faster than the linear search we
     * add the result of the linear search to the hashtable so that the next
     * time we need not do it.
     *
     * @return Checklist or null if noone exist.
     * @param cls the class to lookup.
     */
    private static Checklist lookupChecklist(Class cls) {
        if (lists.contains(cls)) {
            return (Checklist) lists.get(cls);
	}

        // Now lets search
        Enumeration enumeration = lists.keys();

        while (enumeration.hasMoreElements()) {
            Object clazz = enumeration.nextElement();

            Class[] intfs = cls.getInterfaces();
            for (int i = 0; i < intfs.length; i++) {
                if (intfs[i].equals(clazz)) {
                    // We found it!
                    Checklist chlist = (Checklist) lists.get(clazz);

                    // Enter the class to speed up the next search.
                    lists.put(cls, chlist);
                    return chlist;
                }
            }
        }

        return null;
    }


    /**
     * Registers a new list. Used when setting up the checklist stuff.
     *
     * @param dm the class for which the Checklist holds
     * @param cl the Checklist
     */
    public static void register(Object dm, Checklist cl) {
	lists.put(dm, cl);
    }

    /**
     * Get the ChecklistStatus for some object.
     *
     * If there is none, then create one.
     *
     * @return ChecklistStatus, a half filled list.
     * @param dm is the object that we retrieve the checklist for
     */
    public static ChecklistStatus getStatusFor(Object dm) {
	ChecklistStatus cls = (ChecklistStatus) statuses.get(dm);
	if (cls == null) {
	    cls = new ChecklistStatus();
	    statuses.put(dm, cls);
	}
	return cls;
    }
} /* end class CheckManager */

