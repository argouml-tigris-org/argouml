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

import org.argouml.model.DirectionKind;
import org.eclipse.uml2.uml.ParameterDirectionKind;

/**
 * The Eclipse UML2 implementation of DirectionKind.
 * 
 * @author Tom Morris
 */
class DirectionKindEUMLImpl implements DirectionKind {

    public Object getInOutParameter() {
        return ParameterDirectionKind.INOUT_LITERAL;
    }

    public Object getInParameter() {
        return ParameterDirectionKind.IN_LITERAL;
    }

    public Object getOutParameter() {
        return ParameterDirectionKind.OUT_LITERAL;
    }

    public Object getReturnParameter() {
        return ParameterDirectionKind.RETURN_LITERAL;
    }

}
