/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

package org.argouml.cognitive;


/**
 * This class models a goal of a designer.
 *
 */
public class Goal {

    // TODO: values

    ////////////////////////////////////////////////////////////////
    // constants

    /**
     * The unspecified goal.
     */
    private static final Goal UNSPEC = new Goal("label.goal.unspecified", 1);

    ////////////////////////////////////////////////////////////////
    // instance variables
    /**
     * The localized name.
     */
    private String name;

    /**
     * The priority.
     */
    private int priority;

    /**
     * The constructor.
     *
     * @param n the name (to be localized)
     * @param p the priority
     */
    public Goal(String n, int p) {
	name = Translator.localize(n);
	priority = p;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /*
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        if (name == null) {
            return 0;
        }
        return name.hashCode();
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object d2) {
	if (!(d2 instanceof Goal)) {
	    return false;
	}
	return ((Goal) d2).getName().equals(getName());
    }

    /**
     * @return the localized name
     */
    public String getName() { return name; }

    /**
     * @param n the localized name
     */
    public void setName(String n) { name = n; }

    /**
     * @return the priority
     */
    public int getPriority() { return priority; }

    /**
     * @param p the priority
     */
    public void setPriority(int p) { priority = p; }

    /*
     * @see java.lang.Object#toString()
     */
    public String toString() { return getName(); }

    /**
     * @return Returns the unspecified goal.
     */
    public static Goal getUnspecifiedGoal() {
        return UNSPEC;
    }

} /* end class Goal */
