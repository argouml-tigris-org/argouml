/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2009 Tom Morris and other contributors.
// New BSD License

package org.argouml.uml.diagram;

/**
 * Interface to be implemented by UML Sequence Diagram implementations
 * 
 * @author Tom Morris
 */
public interface SequenceDiagram extends ArgoDiagram {

    /**
     * @return the collaboration from the associated graph model
     */
    public Object getCollaboration();
}
