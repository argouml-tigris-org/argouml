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

import org.argouml.model.ExtensionMechanismsHelper;
import org.eclipse.uml2.uml.Element;

/**
 * The implementation of the ExtensionMechanismsHelper for EUML2.
 */
class ExtensionMechanismsHelperEUMLImpl implements ExtensionMechanismsHelper {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param implementation The ModelImplementation.
     */
    public ExtensionMechanismsHelperEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public void addBaseClass(Object handle, Object baseClass) {
        // TODO Auto-generated method stub
        
    }

    public void addCopyStereotype(Object modelElement, Object stereotype) {
        // TODO Auto-generated method stub
        
    }

    public void addExtendedElement(Object handle, Object extendedElement) {
        // TODO Auto-generated method stub
        
    }

    public void addTaggedValue(Object handle, Object taggedValue) {
        // TODO Auto-generated method stub
        
    }

    public Collection getAllPossibleStereotypes(Collection models, Object modelElement) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getMetaModelName(Object m) {
        if (m instanceof Element) {
            return getMetaModelName(m.getClass());
        }
        throw new IllegalArgumentException("Not an Element");

    }

    /**
     * @param clazz
     *            the UML class
     * @return the meta name of the UML class
     */
    protected String getMetaModelName(Class clazz) {
        return modelImpl.getMetaTypes().getName(clazz);
    }
    
    
    public Object getStereotype(Object ns, Object stereo) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getStereotype(Collection models, Object stereo) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getStereotypes(Object ns) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getStereotypes(Collection models) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasStereoType(Object handle, String name) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isStereotype(Object object, String name, String base) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isStereotypeInh(Object object, String name, String base) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isValidStereoType(Object theModelElement, Object theStereotype) {
        // TODO Auto-generated method stub
        return false;
    }

    public void removeBaseClass(Object handle, Object baseClass) {
        // TODO Auto-generated method stub
        
    }

    public void removeTaggedValue(Object handle, Object taggedValue) {
        // TODO Auto-generated method stub
        
    }

    public void setIcon(Object handle, Object icon) {
        // TODO Auto-generated method stub
        
    }

    public void setTag(Object handle, Object tag) {
        // TODO Auto-generated method stub
        
    }

    public void setTaggedValue(Object handle, Collection taggedValues) {
        // TODO Auto-generated method stub
        
    }

    public void setType(Object handle, Object type) {
        // TODO Auto-generated method stub
        
    }

    public void setValueOfTag(Object handle, String value) {
        // TODO Auto-generated method stub
        
    }


}
