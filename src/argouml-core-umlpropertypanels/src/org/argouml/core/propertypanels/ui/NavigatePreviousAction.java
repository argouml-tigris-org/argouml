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

import java.util.List;

/**
 * This action changes the target to the owning model element
 * of the given element.
 *
 * @author Bob Tarling
 */
class NavigatePreviousAction extends NavigateSiblingAction {

    public NavigatePreviousAction(Object modelElement) {
    	super(modelElement, "action.navigate-back");
    }
    
    protected Object getTargetSibling() {
        List list = getAllSiblings();
        if (list != null) {
            final int posn = list.indexOf(modelElement);
            if (posn > 0) {
                return list.get(posn - 1);
            }
        }
        return null;
    }
}
