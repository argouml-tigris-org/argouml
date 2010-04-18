// $Id$
/*******************************************************************************
 * Copyright (c) 2007,2010 Bogdan Pistol and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bogdan Pistol - initial implementation
 *******************************************************************************/
package org.argouml.model.euml;

import org.argouml.model.CopyHelper;
import org.eclipse.uml2.uml.Element;

/**
 * The implementation of the CopyHelper for EUML2.
 */
class CopyHelperEUMLImpl implements CopyHelper {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     * 
     * @param implementation
     *                The ModelImplementation.
     */
    public CopyHelperEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }
    
    public Element copy(Object source, Object destination) {
        if (!(source instanceof Element) || !(destination instanceof Element)) {
            throw new IllegalArgumentException(
                    "The source and destination must be instances of Element"); //$NON-NLS-1$
        }
        Element copiedElement = UMLUtil.copy(
                modelImpl, (Element) source, (Element) destination);
        if (copiedElement == null) {
            throw new UnsupportedOperationException(
                    "Could not copy " + source + " to destination " + destination); //$NON-NLS-1$//$NON-NLS-2$
        }
        return copiedElement;
    }
   

}
