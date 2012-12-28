/* $Id$
 *****************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 */

package org.argouml.kernel;

/**
 * To be implemented by classes that are "owned" by some model element.
 * e.g. a FigClass is owned by a class, a UMLClassDiagram is owned by a
 * namespace.
 * The owner is typically immutable and set by the constructor. Hence not
 * setter is provided as part of this interface.
 *
 * @author Bob Tarling
 */
public interface Owned {
    Object getOwner();
}
