/* $Id$
 *******************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *******************************************************************************
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

package org.argouml.application.events;
import java.util.EventObject;

/**
 * The root class from which all event state objects within Argo are derived.
 *
 * All ArgoEvents are constructed with a reference to the object,
 * the "source", that is logically deemed to be the object
 * upon which the Event in question initially occurred upon.
 */
public abstract class ArgoEvent extends EventObject
    implements ArgoEventTypes {

    private int eventType = 0;

    /**
     * The constructor.
     *
     * @param eT the event type
     * @param src the sourc, that triggered the event
     */
    public ArgoEvent(int eT, Object src) {
        super(src);
	eventType = eT;
    }

    /**
     * @return the event type
     */
    public int getEventType() { return eventType; }

    /**
     * Indicates the start of the range for any events.
     *
     * @return the first id reserved for events.
     */
    public int getEventStartRange() { return ANY_EVENT; }

    /**
     * Indicates the end of the range for notation events.<p>
     * 
     * TODO: Remove this - not used anyway.
     *
     * @return the last id reserved for events.
     */
    public int getEventEndRange() {
        return (getEventStartRange() == 0
	       ? ARGO_EVENT_END
	       : getEventStartRange() + 99);
    }

    /**
     * Provides formatted description of the event.
     *
     * @return the formatted information.
     */
    public String toString() {
        return "{" + getClass().getName() + ":" + eventType
            + "(" + getEventStartRange() + "-" + getEventEndRange() + ")"
            + "/" + super.toString() + "}";
    }

}
