/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *******************************************************************************
 */

package org.argouml.transformer;

import java.util.List;

import javax.swing.Action;

import org.argouml.kernel.Project;

/**
 * Interface implemented by a class that is able to transform 
 * (convert, mutate) one modelelement into another type. 
 * E.g. converting a SimpleState into a CompositeState.
 *
 * @author mvw
 */
interface Transformer {

    /**
     * Determine if this implementation is able to transform the supplied 
     * element. In fact, this boolean only determines if the transformation 
     * menu will become visible - it is still exceptionally possible that the 
     * actual conversion fails.
     * 
     * @param sourceModelElement an UML element. Null is not allowed.
     * @return true if the given element is transformable
     */
    boolean canTransform(Object sourceModelElement);

    /**
     * @param p the project in which the modelelement resides 
     * @param sourceModelElement
     * @return
     */
    List<Action> actions(Project p, Object sourceModelElement);
}
