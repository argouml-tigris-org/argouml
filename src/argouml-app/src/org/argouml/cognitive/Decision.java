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
 * Posters look at the design material from different view points, since they
 * have to make different decisions in their evaluation process. A decision is a
 * unit which describes what kind of decision a given poster supports. E.g. a
 * designer is interested in making particular decisions, whereas a critic can
 * support relevant information which help making a particular decision (aka a
 * decision which lies in the domain specified by this class).
 *
 */
public class Decision {
    ////////////////////////////////////////////////////////////////
    // constants
    /**
     * This is another test.
     */
    public static final Decision UNSPEC =
        new Decision("misc.decision.uncategorized", 1);

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
     * @param n
     *            the (not yet localized) name
     * @param p
     *            the priority
     */
    public Decision(String n, int p) {
        name = Translator.localize(n);
        priority = p;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if (name == null) {
            return 0;
        }
        return name.hashCode();
    }

    /**
     * Two decisions are considered to be equal if their names are equal. The
     * priority is not considered.
     *
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object d2) {
        if (!(d2 instanceof Decision)) {
            return false;
        }
        return ((Decision) d2).getName().equals(getName());
    }

    /**
     * @return the localized name
     */
    public String getName() {
        return name;
    }

    /**
     * @param n
     *            the localized name
     */
    public void setName(String n) {
        name = n;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param p
     *            the priority
     */
    public void setPriority(int p) {
        priority = p;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getName();
    }
}
