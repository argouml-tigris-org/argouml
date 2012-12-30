// $Id$
/*****************************************************************************
 * Copyright (c) 2007-2012 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial implementation
 *****************************************************************************/
package org.argouml.model.euml;

import org.argouml.model.ConcurrencyKind;
import org.eclipse.uml2.uml.CallConcurrencyKind;

/**
 * The Eclipse UML2 implementation for Concurrency Kind.
 * @author Tom Morris
 */
class ConcurrencyKindEUMLImpl implements ConcurrencyKind {

    public Object getConcurrent() {
        return CallConcurrencyKind.CONCURRENT_LITERAL;
    }

    public Object getGuarded() {
        return CallConcurrencyKind.GUARDED_LITERAL;
    }

    public Object getSequential() {
        return CallConcurrencyKind.SEQUENTIAL_LITERAL;
    }

}
