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

package org.argouml.notation2;

public interface NotationLanguage {

    /**
     * Get the name the language
     * @return the language name
     */
    public String getName();
    
    
    /**
     * Create the NotationText instance for the given NotatedItem.
     * The implementation determines the actual instance to create
     * depending on the met type and notation type of the NotatedItem.
     * @param item the NotatedItem
     * @return the NotationText instance
     */
    public NotationText createNotationText(NotatedItem item);
}
