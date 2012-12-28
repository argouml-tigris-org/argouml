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
 * An interface to be implemented by those classes wishing to listen for addition and removal
 * of model events.
 *
 * @author Bob Tarling
 */
public interface AssociationChangeListener extends UmlChangeListener {
    /**
     * Called when model element has been added to another
     * @param evt
     */
    void elementAdded(AddAssociationEvent evt);
    /**
     * Called when model element has been removed from another
     * @param evt
     */
    void elementRemoved(RemoveAssociationEvent evt);
}
