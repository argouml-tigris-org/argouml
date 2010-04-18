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

import org.argouml.model.VisibilityKind;

/**
 * The Eclipse UML2 implementation of VisibilityKind.
 * 
 * @author Tom Morris
 */
class VisibilityKindEUMLImpl implements VisibilityKind {

    public Object getPackage() {
        return org.eclipse.uml2.uml.VisibilityKind.PACKAGE_LITERAL;
    }

    public Object getPrivate() {
        return org.eclipse.uml2.uml.VisibilityKind.PRIVATE_LITERAL;
    }

    public Object getProtected() {
        return org.eclipse.uml2.uml.VisibilityKind.PROTECTED_LITERAL;
    }

    public Object getPublic() {
        return org.eclipse.uml2.uml.VisibilityKind.PUBLIC_LITERAL;
    }

}
