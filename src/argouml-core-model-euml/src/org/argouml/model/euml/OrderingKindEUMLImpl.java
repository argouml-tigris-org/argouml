// $Id$
/*******************************************************************************
 * Copyright (c) 2007,2010 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial implementation 
 *******************************************************************************/

package org.argouml.model.euml;

import org.argouml.model.OrderingKind;

/**
 * The implementation of the OrderingKind for EUML2.
 */
class OrderingKindEUMLImpl implements OrderingKind {

    static final String ORDERED = "ordered"; //$NON-NLS-1$
    
    public Object getOrdered() {
        return ORDERED;
    }

    public Object getUnordered() {
        return "unordered"; //$NON-NLS-1$
    }

}
