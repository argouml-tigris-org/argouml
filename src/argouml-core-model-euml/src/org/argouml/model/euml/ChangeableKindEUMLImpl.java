// $Id$
/*******************************************************************************
 * Copyright (c) 2007,2010 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial API and implementation
 *******************************************************************************/
package org.argouml.model.euml;

import org.argouml.model.ChangeableKind;

/**
 * Eclipse UML2 implementation for ChangeableKind.
 * 
 * @author Tom Morris
 * @deprecated This is a simple binary choice (isReadOnly or not) in UML2, so
 *             this is purely for backward compatibility.
 */
@Deprecated
class ChangeableKindEUMLImpl implements ChangeableKind {

    @Deprecated
    public Object getAddOnly() {
    // TODO: Change to throw exception when uses are fixed
//        throw new NotImplementedException();
        return ""; //$NON-NLS-1$
    }

    @Deprecated
    public Object getChangeable() {
        return "changeable"; //$NON-NLS-1$
    }

    @Deprecated
    public Object getFrozen() {
        return "frozen"; //$NON-NLS-1$
    }


}
