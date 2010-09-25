/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 */

package org.argouml.core.propertypanels.ui;

import javax.swing.JComponent;

/**
 * An interface for components that have the possibility of being expanded and shrunk.
 * Not all components that implement this interface can actually be expanded, they only
 * have the possibility if the implementation if isExpandable is true.
 *
 * @author Bob Tarling
 */
interface Expandable {
    boolean isExpandable();
    boolean isExpanded();
    void toggleExpansion();
    JComponent getExpansion();
}
