/* $Id$
 *****************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    euluis
 *****************************************************************************
 */

package org.argouml.profile.internal;

/**
 * An interface to be used to check if all dependencies of items are resolved
 * or not.
 * @author Luis Sergio Oliveira (euluis)
 * @param <T> the type of the items for which the dependencies are to be
 *            checked.
 */
interface DependencyChecker<T> {
    /**
     * Check if all dependencies of item are resolved.
     *
     * @param item the item for which to check if the dependencies are
     *             resolved.
     * @return true if the check if item dependencies are all resolved is
     *         successful.
     */
    boolean check(T item);
}
