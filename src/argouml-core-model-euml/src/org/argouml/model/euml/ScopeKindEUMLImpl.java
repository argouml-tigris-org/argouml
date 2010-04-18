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

import org.argouml.model.ScopeKind;

/**
 * The implementation of the ScopeKind for EUML2.
 * 
 * @author Tom Morris
 * @deprecated This is no longer used in UML 2.  We return literal placeholder
 * values for any code that still uses this.
 */
@Deprecated
class ScopeKindEUMLImpl implements ScopeKind {

    @Deprecated
    public Object getClassifier() {
        return "classifier"; //$NON-NLS-1$
    }

    @Deprecated
    public Object getInstance() {
        return "instance"; //$NON-NLS-1$
    }

}
