// $Id:UseCasesFactoryEUMLImpl.java 12721 2007-05-30 18:14:55Z tfmorris $
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

import org.argouml.model.UseCasesFactory;
import org.eclipse.uml2.uml.Actor;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.ExtensionPoint;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UseCase;


/**
 * The implementation of the UseCasesFactory for EUML2.
 */
class UseCasesFactoryEUMLImpl implements UseCasesFactory {

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
    public UseCasesFactoryEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public Object buildActor(Object actor, Object model) {
        Namespace ns = ((Actor) actor).getNamespace();
        if (ns == null) {
            ns = (Namespace) model;
        }
        Actor actor2 = (Actor) createActor();
//        actor2.setNamespace(ns);
        ns.getOwnedElements().add(actor2);
        actor2.setIsLeaf(false);
//        actor2.setIsRoot(false);
        return actor2;
    }

    public Object buildExtend(Object abase, Object anextension) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildExtend(Object abase, Object anextension, Object apoint) {
        Extend ext = (Extend) createExtend();
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildExtensionPoint(Object modelElement) {
        ExtensionPoint ep = (ExtensionPoint) createExtensionPoint();
        ((UseCase) modelElement).getExtensionPoints().add(ep);
        return ep;
    }

    public Object buildInclude(Object abase, Object anaddition) {
        Include inc = (Include) createInclude();
        // TODO Auto-generated method stub
        return inc;
    }

    public Object createActor() {
        return UMLFactory.eINSTANCE.createActor();
    }

    public Object createExtend() {
        return UMLFactory.eINSTANCE.createExtend();
    }

    public Object createExtensionPoint() {
        return UMLFactory.eINSTANCE.createExtensionPoint();
    }

    public Object createInclude() {
        return UMLFactory.eINSTANCE.createInclude();
    }
    
    public Object createUseCase() {
        return UMLFactory.eINSTANCE.createUseCase();
    }

    public Object createUseCaseInstance() {
        // TODO Auto-generated method stub
        return null;
    }

}
