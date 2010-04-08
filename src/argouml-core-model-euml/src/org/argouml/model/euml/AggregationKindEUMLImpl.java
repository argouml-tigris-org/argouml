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

import org.argouml.model.AggregationKind;

/**
 * Eclipse UML2 implementation of AggregationKind.
 * 
 * @author Tom Morris
 */
class AggregationKindEUMLImpl implements AggregationKind {

    public Object getAggregate() {
        return org.eclipse.uml2.uml.AggregationKind.SHARED_LITERAL;
    }

    public Object getComposite() {
        return org.eclipse.uml2.uml.AggregationKind.COMPOSITE_LITERAL;
    }

    public Object getNone() {
        return org.eclipse.uml2.uml.AggregationKind.NONE_LITERAL;
    }

}
