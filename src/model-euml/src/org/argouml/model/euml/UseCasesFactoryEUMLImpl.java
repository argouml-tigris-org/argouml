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

import org.argouml.model.NotImplementedException;
import org.argouml.model.UseCasesFactory;
import org.eclipse.uml2.uml.Actor;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.ExtensionPoint;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.InstanceSpecification;
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

    public Actor buildActor(Object actor, Object model) {
        Namespace ns = ((Actor) actor).getNamespace();
        if (ns == null) {
            ns = (Namespace) model;
        }
        Actor actor2 = (Actor) createActor();
        modelImpl.getCoreHelper().addOwnedElement(ns, actor2);
        actor2.setIsLeaf(false);
//        actor2.setIsRoot(false);
        return actor2;
    }

    public Extend buildExtend(Object abase, Object anextension) {
        return buildExtend(abase, anextension, null);
    }

    public Extend buildExtend(Object abase, Object anextension, Object apoint) {
        ExtensionPoint ep;
        if (apoint == null) {
            ep = buildExtensionPoint((UseCase) abase);
        } else {
            ep = (ExtensionPoint) apoint;
            if (!ep.getUseCase().equals(abase)) {
                throw new IllegalArgumentException(
                        "extension point must belong to " + //$NON-NLS-1$
                        "extended use case"); //$NON-NLS-1$
            }
        }
        Extend extend =
                ((UseCase) anextension).createExtend(null, (UseCase) abase);
        extend.getExtensionLocations().add(ep);
        return extend;
    }

    public ExtensionPoint buildExtensionPoint(Object modelElement) {
        return ((UseCase) modelElement).createExtensionPoint(null);
    }

    public Include buildInclude(Object abase, Object anaddition) {
        return ((UseCase) anaddition).createInclude(null, (UseCase) abase);
    }

    public Actor createActor() {
        return UMLFactory.eINSTANCE.createActor();
    }

    public Extend createExtend() {
        return UMLFactory.eINSTANCE.createExtend();
    }

    public ExtensionPoint createExtensionPoint() {
        return UMLFactory.eINSTANCE.createExtensionPoint();
    }

    public Include createInclude() {
        return UMLFactory.eINSTANCE.createInclude();
    }
    
    public UseCase createUseCase() {
        return UMLFactory.eINSTANCE.createUseCase();
    }

    @SuppressWarnings("deprecation")
    public InstanceSpecification createUseCaseInstance() {
        throw new NotImplementedException();
    }

}
