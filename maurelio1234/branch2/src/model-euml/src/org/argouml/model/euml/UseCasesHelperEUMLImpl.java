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
import java.util.HashSet;

import org.argouml.model.NotImplementedException;
import org.argouml.model.UseCasesHelper;
import org.eclipse.uml2.uml.Actor;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.ExtensionPoint;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UseCase;

/**
 * The implementation of the UseCasesHelper for EUML2.
 */
class UseCasesHelperEUMLImpl implements UseCasesHelper {

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
    public UseCasesHelperEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public void addExtend(Object handle, Object extend) {
        if (handle instanceof UseCase) {
            ((UseCase) handle).getExtends().add(
                    (Extend) extend);
        } else if (handle instanceof ExtensionPoint) {
            ((Extend) extend).getExtensionLocations().add(
                    (ExtensionPoint) handle);
        }
    }

    public void addExtensionPoint(Object handle, Object extensionPoint) {
        if (handle instanceof UseCase) {
            ((UseCase) handle).getExtensionPoints().add(
                    (ExtensionPoint) extensionPoint);
        } else if (handle instanceof Extend) {
            ((Extend) handle).getExtensionLocations().add(
                    (ExtensionPoint) extensionPoint);
        }
        throw new IllegalArgumentException(
                "Must be UseCase or Extend : " //$NON-NLS-1$
                + handle);
    }

    public void addExtensionPoint(Object handle, int position,
            Object extensionPoint) {
        ((Extend) handle).getExtensionLocations().add(position,
                (ExtensionPoint) extensionPoint);
    }

    public void addInclude(Object usecase, Object include) {
        ((UseCase) usecase).getIncludes().add((Include) include);
    }

    public Collection getAllActors(Object ns) {
        return getAllOfType((org.eclipse.uml2.uml.Package) ns, Actor.class);
    }

    public Collection getAllUseCases(Object ns) {
        return getAllOfType((org.eclipse.uml2.uml.Package) ns, UseCase.class);
    }
    
    private Collection<Type> getAllOfType(org.eclipse.uml2.uml.Package pkg,
            Class type) {
        return modelImpl.getModelManagementHelper().getAllModelElementsOfKind(
                pkg, type);
    }

    public Collection getExtendedUseCases(Object ausecase) {
        Collection<UseCase> result = new HashSet<UseCase>();
        for (Extend extend : ((UseCase) ausecase).getExtends()) {
            result.add(extend.getExtension());
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    public Collection<UseCase> getExtendingUseCases(Object usecase) {
        // TODO: association is not navigable in this direction
        throw new NotImplementedException();
    }

    public Extend getExtends(Object abase, Object anextension) {
        return ((UseCase) anextension).getExtend(null, (UseCase) abase);
    }

    public Collection<UseCase> getIncludedUseCases(Object ausecase) {
        Collection<UseCase> result = new HashSet<UseCase>();
        for (Include include : ((UseCase) ausecase).getIncludes() ) {
            result.add(include.getAddition());
        }
        return result;
    }

    public Include getIncludes(Object abase, Object aninclusion) {
        return ((UseCase) abase).getInclude(null, (UseCase) aninclusion);
    }

    public Collection getSpecificationPath(Object ausecase) {
        // TODO Auto-generated method stub
        return null;
    }

    public void removeExtend(Object elem, Object extend) {
        // TODO Auto-generated method stub
    }

    public void removeExtensionPoint(Object elem, Object ep) {
        // TODO Auto-generated method stub

    }

    public void removeInclude(Object usecase, Object include) {
        ((UseCase) usecase).getIncludes().remove((Include) include);
    }

    public void setAddition(Object handle, Object useCase) {
        ((Include) handle).setAddition((UseCase) useCase);
    }

    public void setBase(Object extend, Object base) {
        // TODO Auto-generated method stub

    }

    public void setCondition(Object handle, Object booleanExpression) {
        // TODO Auto-generated method stub

    }

    public void setExtension(Object handle, Object ext) {
    }

    public void setExtensionPoints(Object handle, Collection extensionPoints) {
        if (handle instanceof Extend) {
            ((Extend) handle).getExtensionLocations().clear();
            ((Extend) handle).getExtensionLocations().addAll(
                    (Collection<ExtensionPoint>) extensionPoints);
        } else if (handle instanceof UseCase) {
            ((UseCase) handle).getExtensionPoints().clear();
            ((UseCase) handle).getExtensionPoints().addAll(
                    (Collection<ExtensionPoint>) extensionPoints);
        } else {
            throw new IllegalArgumentException(
                    "Expected Extend or UseCase : "  //$NON-NLS-1$
                    + handle);
        }
    }

    public void setIncludes(Object handle, Collection includes) {
        // TODO Auto-generated method stub

    }

    public void setLocation(Object handle, String loc) {
        // TODO Auto-generated method stub

    }

    public void setUseCase(Object elem, Object usecase) {
        // TODO Auto-generated method stub

    }

}
