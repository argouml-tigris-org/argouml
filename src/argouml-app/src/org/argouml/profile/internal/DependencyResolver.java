/* $Id$
 *****************************************************************************
 * Copyright (c) 2010-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Luis Sergio Oliveira (euluis)
 *****************************************************************************
 */

package org.argouml.profile.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A dependency resolver for items of type T. It implements a state-full
 * dependency resolution algorithm.
 *
 * @author Luis Sergio Oliveira (euluis)
 * @param <T> the type of items for which dependencies will be resolved.
 */
class DependencyResolver<T> {

    private static final Logger LOG =
        Logger.getLogger(DependencyResolver.class.getName());

    private DependencyChecker<T> checker;

    /**
     * WARNING: only to be used from outside classes by tests.
     * This is the state-full part of the algorithm, storing the unresolved
     * items between resolve methods calls.
     */
    Collection<T> unresolvedItems;

    /**
     * Create a dependency resolver and initialize it with the associated
     * dependency checker.
     *
     * @param checker the object that will be invoked to check if for a certain
     *                item all dependencies are resolved.
     */
    DependencyResolver(DependencyChecker<T> checker) {
        this.checker = checker;
        unresolvedItems = new HashSet<T>();
    }

    /**
     * Attempt to resolve the dependencies of the items already handed over to
     * the resolver instance.
     */
    void resolve() {
        if (unresolvedItems.isEmpty()) {
            return;
        }
        final Collection<T> items = Collections.emptyList();
        resolve(items);
    }

    /**
     * Attempt to resolve the dependencies of the items already handed over to
     * the resolver instance and the additional items handed over now.
     *
     * @param items additional items to resolve.
     */
    void resolve(Collection<T> items) {
        if (unresolvedItems.isEmpty() && items.isEmpty()) {
            return;
        }
        Collection<T> allUnresolvedItems = new HashSet<T>();
        allUnresolvedItems.addAll(items);
        allUnresolvedItems.addAll(unresolvedItems);
        
        if (LOG.isLoggable(Level.FINE)) {
            LOG.log(Level.FINE,
                    items2Msg("Attempt to resolve the following items:",
                              allUnresolvedItems));
        }
        Collection<T> resolved = internalResolve(allUnresolvedItems);
        allUnresolvedItems.removeAll(resolved);
        unresolvedItems.clear();
        unresolvedItems.addAll(allUnresolvedItems);
        if (!unresolvedItems.isEmpty()) {
            LOG.log(Level.WARNING,
                    items2Msg("The following items were left unresolved after "
                              + "attempt:\n",
                              unresolvedItems));
        }
    }

    private String items2Msg(String preface, Collection<T> items) {
        StringBuffer msg = new StringBuffer(preface);
        for (T item : items) {
            msg.append("\t");
            msg.append(item.toString());
            msg.append("\n");
        }
        return msg.toString();
    }

    /**
     * Recursively resolve all dependencies. Stops when an iteration through
     * all unresolved items didn't manage to resolve any.
     *
     * @param items items to resolve.
     * @return the items that were resolved.
     */
    private Collection<T> internalResolve(Collection<T> items) {
        Collection<T> resolved = new HashSet<T>();
        for (T item : items) {
            if (checker.check(item)) {
                resolved.add(item);
            }
        }
        HashSet<T> toResolveItems = new HashSet<T>(items);
        toResolveItems.removeAll(resolved);
        if (!resolved.isEmpty()) {
            resolved.addAll(internalResolve(toResolveItems));
        }
        return resolved;
    }
}
