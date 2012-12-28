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
 * The different AggregationKinds.
 */
public interface MessageSort {
    
    /**
     * @return Returns the SynchCall MessageSort
     */
    Object getSynchCall();

    /**
     * @return Returns the ASynchCall MessageSort
     */
    Object getASynchCall();
    
    /**
     * @return Returns the CreateMessage MessageSort
     */
    Object getCreateMessage();

    /**
     * @return Returns the DeleteMessage MessageSort
     */
    Object getDeleteMessage();

    /**
     * @return Returns the Reply MessageSort
     */
    Object getReply();
    
    /**
     * @return Returns the ASynchSignal MessageSort
     */
    Object getASynchSignal();    
}
