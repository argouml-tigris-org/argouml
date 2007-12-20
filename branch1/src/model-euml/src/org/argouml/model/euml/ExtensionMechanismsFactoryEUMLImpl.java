// $Id$
// Copyright (c) 2007, The ArgoUML Project
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in the
//       documentation and/or other materials provided with the distribution.
//     * Neither the name of the ArgoUML Project nor the
//       names of its contributors may be used to endorse or promote products
//       derived from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE ArgoUML PROJECT ``AS IS'' AND ANY
// EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE ArgoUML PROJECT BE LIABLE FOR ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.argouml.model.euml;

import java.util.Collection;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.ExtensionMechanismsFactory;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * The implementation of the ExtensionMechanismsFactory for EUML2.
 */
class ExtensionMechanismsFactoryEUMLImpl implements
        ExtensionMechanismsFactory, AbstractModelFactory {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     * 
     * @param implementation
     *            The ModelImplementation.
     */
    public ExtensionMechanismsFactoryEUMLImpl(
            EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public Object buildStereotype(Object theModelElementObject, Object theName,
            Object theNamespaceObject) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildStereotype(Object theModelElementObject, String theName,
            Object model, Collection models) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildStereotype(String text, Object ns) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildTagDefinition(String name, Object stereotype, Object ns) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Object buildTagDefinition(String name, Object stereotype, 
            Object namespace, String tagType) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildTaggedValue(String tag, String value) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object copyStereotype(Object source, Object ns) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object copyTagDefinition(Object aTd, Object aNs) {
        // TODO Auto-generated method stub
        return null;
    }

    public void copyTaggedValues(Object source, Object target) {
        // TODO Auto-generated method stub

    }

    public Object createStereotype() {
        return UMLFactory.eINSTANCE.createStereotype();
    }

    public Object createTagDefinition() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createTaggedValue() {
        // TODO Auto-generated method stub
        return null;
    }

}
