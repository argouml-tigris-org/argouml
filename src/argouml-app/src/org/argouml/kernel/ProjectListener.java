/* $Id$
 *******************************************************************************
 * Copyright (c) 2014 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 */

package org.argouml.kernel;

/**
 * An interface to be applied to any class interested in addition or removal of a diagram from a project
 *
 * @author Bob Tarling
 */
public interface ProjectListener {
    
    /**
     * Called when a diagram is added to a project
     * @param event
     */
    void diagramAdded(ProjectEvent event);
    
    /**
     * Called when a diagram is removed from a project
     * @param event
     */
    void diagramRemoved(ProjectEvent event);

}
