// $Id:StateBodyNotation.java 12991 2007-07-06 17:05:03Z mvw $
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.notation.providers;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;

/**
 * This abstract class forms the basis of all Notation providers
 * for the text shown in the body of a state. Subclass this for all languages.
 *
 * @author mvw@tigris.org
 */
public abstract class StateBodyNotation extends NotationProvider {

    /**
     * The constructor.
     *
     * @param state The state.
     */
    public StateBodyNotation(Object state) {
        if (!Model.getFacade().isAState(state)) {
            throw new IllegalArgumentException("This is not a State.");
        }
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#addListener(java.beans.PropertyChangeListener, java.lang.Object)
     */
    public void initialiseListener(PropertyChangeListener listener, 
            Object modelElement) {
        addElementListener(listener, modelElement);
//      register for internal transitions:
        Iterator it =
            Model.getFacade().getInternalTransitions(modelElement).iterator();
        while (it.hasNext()) {
            addListenersForTransition(listener, it.next());
        }
        // register for the doactivity etc.
        Object doActivity = Model.getFacade().getDoActivity(modelElement);
        addListenersForAction(listener, doActivity);
        Object entryAction = Model.getFacade().getEntry(modelElement);
        addListenersForAction(listener, entryAction);
        Object exitAction = Model.getFacade().getExit(modelElement);
        addListenersForAction(listener, exitAction);
    }

    private void addListenersForAction(PropertyChangeListener listener, 
            Object action) {
        if (action != null) {
            addElementListener(listener, action,
                    new String[] {
                        "script", "actualArgument", "action"
                    });
            Collection args = Model.getFacade().getActualArguments(action);
            Iterator i = args.iterator();
            while (i.hasNext()) {
                Object argument = i.next();
                addElementListener(listener, argument, "value");
            }
            if (Model.getFacade().isAActionSequence(action)) {
                Collection subactions = Model.getFacade().getActions(action);
                i = subactions.iterator();
                while (i.hasNext()) {
                    Object a = i.next();
                    addListenersForAction(listener, a);
                }
            }
        }
    }

    private void addListenersForEvent(PropertyChangeListener listener, 
            Object event) {
        if (event != null) {
            addElementListener(listener, event,
                    new String[] {
                        "parameter", "name",
                    });
            Collection prms = Model.getFacade().getParameters(event);
            Iterator i = prms.iterator();
            while (i.hasNext()) {
                Object parameter = i.next();
                addElementListener(listener, parameter);
            }
        }
    }
    
    private void addListenersForTransition(PropertyChangeListener listener, 
            Object transition) {
        addElementListener(listener, transition, 
                new String[] {"guard", "trigger", "effect"});

        Object guard = Model.getFacade().getGuard(transition);
        if (guard != null) {
            addElementListener(listener, guard, "expression");
        }

        Object trigger = Model.getFacade().getTrigger(transition);
        addListenersForEvent(listener, trigger);

        Object effect = Model.getFacade().getEffect(transition);
        addListenersForAction(listener, effect);
    }    

}
