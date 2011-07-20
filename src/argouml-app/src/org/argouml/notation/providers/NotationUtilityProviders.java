/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *******************************************************************************
 */

package org.argouml.notation.providers;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;

/**
 * Utility code for the classes in this package.
 *
 * @author Michiel van der Wulp
 */
class NotationUtilityProviders {


    static void addListenersForTransition(NotationProvider np, 
            Object transition) {
        np.addElementListener(transition, 
                new String[] {"guard", "trigger", "effect"});

        Object guard = Model.getFacade().getGuard(transition);
        if (guard != null) {
            np.addElementListener(guard, "expression");
            /* We are not interested in the name. */
        }

        List triggers = Model.getFacade().getTriggers(transition);
        
        for (Object trigger : triggers) {
            addListenersForEvent(np, trigger);
        }

        Object effect = Model.getFacade().getEffect(transition);
        addListenersForAction(np, effect);
    }    
    
    private static void addListenersForEvent(NotationProvider np, 
            Object event) {
        if (event != null) {
            if (Model.getFacade().isAEvent(event)) {
                np.addElementListener(event,
                        new String[] {
                            "parameter", "name"});
            }
            if (Model.getFacade().isATimeEvent(event)) {
                np.addElementListener(event, new String[] {"when"});
            }
            if (Model.getFacade().isAChangeEvent(event)) {
                np.addElementListener(event,
                        new String[] {"changeExpression"});
            }
            /* And the parameter names and values: */
            Collection prms = Model.getFacade().getParameters(event);
            Iterator i = prms.iterator();
            while (i.hasNext()) {
                Object parameter = i.next();
                np.addElementListener(parameter, 
                      new String[] {"defaultValue", "name", "type", "kind"});
            }
        }
    }
    
    static void addListenersForAction(NotationProvider np, 
            Object action) {
        if (action != null) {
            np.addElementListener(action,
                    new String[] {
                        "script", "actualArgument", "action"
                    });
            /* And the arguments: */
            Collection args = Model.getFacade().getActualArguments(action);
            Iterator i = args.iterator();
            while (i.hasNext()) {
                Object argument = i.next();
                np.addElementListener(argument, "value");
            }
            if (Model.getFacade().isAActionSequence(action)) {
                Collection subactions = Model.getFacade().getActions(action);
                i = subactions.iterator();
                while (i.hasNext()) {
                    Object a = i.next();
                    addListenersForAction(np, a);
                }
            }
        }
    }

}
