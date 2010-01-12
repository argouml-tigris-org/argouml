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

// Copyright (c) 2002-2008 The Regents of the University of California. All
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

package org.argouml.ui.targetmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;

/**
 * An event indicating that the target of ArgoUML has changed
 * from the old set of Targets to the new set of Targets.
 * 
 * @author jaap.branderhorst@xs4all.nl
 */
public class TargetEvent extends EventObject {

    /**
     * Indicates that a total new set of targets is set.
     */
    public static final String TARGET_SET = "set";

    /**
     * Indicates that a target is being added to the list of targets.
     */
    public static final String TARGET_ADDED = "added";

    /**
     * Indicates that a target is being removed from the list of targets.
     */
    public static final String TARGET_REMOVED = "removed";

    /**
     * The name of the event.
     */
    private String theEventName;

    /**
     * The old targets before the change took place.
     */
    private Object[] theOldTargets;

    /**
     * The new targets after the change took place.
     */
    private Object[] theNewTargets;

    /**
     * Constructs a new TargetEvent.
     * 
     * @param source The source that fired the TargetEvent, will always be the
     *                TargetManager
     * @param tEName The name of the TargetEvent, can be TARGET_SET,
     *                TARGET_REMOVED or TARGET_ADDED
     * @param oldTargets The old targets before the change took place
     * @param newTargets The new targets after the change took place
     */
    public TargetEvent(Object source, String tEName,
		       Object[] oldTargets, Object[] newTargets) {
	super(source);
	theEventName = tEName;
        theOldTargets = oldTargets;
        theNewTargets = newTargets;
    }

    /**
     * Getter for the name.
     * @return the name of the event
     */
    public String getName() {
	return theEventName;
    }

    /**
     * Getter for the old targets.
     * @return an object array with the old targets
     */
    public Object[] getOldTargets() {
	return theOldTargets == null ? new Object[] {} : theOldTargets;
    }

    /**
     * Getter for the new targets.
     * @return an object array with the new targets
     */
    public Object[] getNewTargets() {
        return theNewTargets == null ? new Object[] {} : theNewTargets;
    }

    /**
     * Helper for getting the new target.
     * @return the zero'th element in _newTargets, or null
     */
    public Object getNewTarget() {
        return theNewTargets == null
            || theNewTargets.length < 1 ? null : theNewTargets[0];
    }

    /**
     * Gets the targets that are removed from the selection.
     * @return the removed targets
     */
    public Collection getRemovedTargetCollection() {
        List removedTargets = new ArrayList();
        List oldTargets = Arrays.asList(theOldTargets);
        List newTargets = Arrays.asList(theNewTargets);
        for (Object o : oldTargets) {
            if (!newTargets.contains(o)) {
                removedTargets.add(o);
            }
        }
        return removedTargets;
    }

    /**
     * Gets the targets that are removed from the selection.
     * @return the removed targets
     */
    public Object[] getRemovedTargets() {
        return getRemovedTargetCollection().toArray();
    }

    /**
     * Returns the targets that are added to the selection.
     * @return the added targets
     */
    public Collection getAddedTargetCollection() {
        List addedTargets = new ArrayList();
        List oldTargets = Arrays.asList(theOldTargets);
        List newTargets = Arrays.asList(theNewTargets);
        for (Object o : newTargets) {
            if (!oldTargets.contains(o)) {
                addedTargets.add(o);
            }
        }
        return addedTargets;
    }

    /**
     * Returns the targets that are added to the selection.
     * @return the added targets
     */
    public Object[] getAddedTargets() {
        return getAddedTargetCollection().toArray();
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -307886693486269426L;
}

