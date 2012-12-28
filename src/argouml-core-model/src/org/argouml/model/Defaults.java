/* $Id$
 *******************************************************************************
 * Copyright (c) 2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 */

package org.argouml.model;

/**
 * Contains information on what default properties should be applied to a newly created
 * model element.
 * @author Bob Tarling
 */
public interface Defaults {
    
    /**
     * Get the default type to apply when creating on object of the given meta type.
     * @param metaType that is to be created
     * @return the type instance to apply to the newly created object
     */
    Object getDefaultType(Object metaType);
    
    /**
     * Get the default name to apply when creating on object of the given meta type.
     * @param metaType that is to be created
     * @return the name to apply to the newly created object
     */
    String getDefaultName(Object metaType);
}
