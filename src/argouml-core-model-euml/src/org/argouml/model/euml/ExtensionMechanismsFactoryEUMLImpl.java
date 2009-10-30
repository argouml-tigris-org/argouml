// $Id$
// Copyright (c) 2007,2008 Tom Morris and other contributors
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in the
//       documentation and/or other materials provided with the distribution.
//     * Neither the name of the project or its contributors may be used 
//       to endorse or promote products derived from this software without
//       specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE CONTRIBUTORS ``AS IS'' AND ANY
// EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE CONTRIBUTORS BE LIABLE FOR ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.argouml.model.euml;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.argouml.model.AbstractModelFactory;
import org.argouml.model.ExtensionMechanismsFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * The implementation of the ExtensionMechanismsFactory for EUML2.
 */
class ExtensionMechanismsFactoryEUMLImpl implements
        ExtensionMechanismsFactory, AbstractModelFactory {

    private static final Logger LOG = Logger
            .getLogger(ExtensionMechanismsFactoryEUMLImpl.class);

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * The UML metamodel (lazily loaded singleton).
     */
    private static UMLPackage metamodel = null;
    
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
        
        if (theModelElementObject == null || theName == null
                || theNamespaceObject == null) {
            throw new IllegalArgumentException(
                    "one of the arguments is null: modelElement=" //$NON-NLS-1$
                    + theModelElementObject
                    + " name=" + theName //$NON-NLS-1$
                    + " namespace=" + theNamespaceObject); //$NON-NLS-1$
        }
        
        //ModelElement me = (ModelElement) theModelElementObject;
        
        String text = (String) theName;
        Namespace ns = (Namespace) theNamespaceObject;
        Stereotype stereo = (Stereotype)buildStereotype(text, ns);
        /*
        stereo.getBaseClass().add(modelImpl.getMetaTypes().getName(me));
        // TODO: this doesn't look right - review - tfm
        Stereotype stereo2 = (Stereotype) extensionHelper.getStereotype(ns,
                stereo);
        if (stereo2 != null) {
            me.getStereotype().add(stereo2);
            modelImpl.getUmlFactory().delete(stereo);
            return stereo2;
        }
        stereo.setNamespace(ns);
        me.getStereotype().add(stereo);
        */
        return stereo;
    }

    public Object buildStereotype(Object theModelElementObject, String theName,
            Object model, Collection models) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object buildStereotype(String text, Object ns) {
        Stereotype stereo = UMLFactory.eINSTANCE.createStereotype();
        stereo.setName(text);
        // more checking needed?
        if (ns instanceof Package) {
            stereo.setPackage((Package)ns);
        }
        return stereo;
    }

    public Object buildTagDefinition(String name, Object stereotype, Object ns) {
        // TODO: Auto-generated method stub
        return null;
    }
    
    public Object buildTagDefinition(String name, Object stereotype, 
            Object namespace, String tagType) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object buildTaggedValue(String tag, String value) {
        // TODO: Auto-generated method stub
        return null;
    }
    
    public Object buildTaggedValue(Object type, String[] value) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object copyStereotype(Object source, Object ns) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object copyTagDefinition(Object aTd, Object aNs) {
        // TODO: Auto-generated method stub
        return null;
    }

    public void copyTaggedValues(Object source, Object target) {
        // TODO: Auto-generated method stub

    }

    public Object createStereotype() {
        return UMLFactory.eINSTANCE.createStereotype();
    }

    public Object createTagDefinition() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createTaggedValue() {
        // TODO: Auto-generated method stub
        return null;
    }
}
