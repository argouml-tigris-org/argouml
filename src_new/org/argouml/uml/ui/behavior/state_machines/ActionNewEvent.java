// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// $header$
package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;

import org.argouml.model.uml.behavioralelements.statemachines.StateMachinesHelper;
import org.argouml.uml.ui.AbstractActionNewModelElement;

/**
 * @since Dec 15, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class ActionNewEvent extends AbstractActionNewModelElement {

    /**
     * The constant defining the role the event to be created plays for its
     * parent. For example, if one wishes to create a trigger event for a
     * transition, this is filled with "trigger". The values are defined in the
     * interface Roles
     */
    public final static String ROLE = "role";

    public static interface Roles {

        /**
         * The trigger for some transition
         */
        public final static String TRIGGER = "trigger";

    }
    /**
     * Constructor for ActionNewEvent.
     */
    protected ActionNewEvent() {
        super();
    }

    /**
     * Implementors should create a concrete event like an instance of
     * SignalEvent in this method.
     * @return Object
     */
    protected abstract Object createEvent();

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object event = createEvent();
        if (getValue(ROLE).equals(Roles.TRIGGER)) {
            StateMachinesHelper.getHelper().setEventAsTrigger(getTarget(), event);
        }
    }
}
