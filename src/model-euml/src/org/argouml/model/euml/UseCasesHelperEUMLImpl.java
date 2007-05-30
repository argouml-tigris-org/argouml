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

import org.argouml.model.UseCasesHelper;

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

    public void addExtend(Object elem, Object extend) {
        // TODO Auto-generated method stub

    }

    public void addExtensionPoint(Object handle, Object extensionPoint) {
        // TODO Auto-generated method stub

    }

    public void addExtensionPoint(Object handle, int position,
            Object extensionPoint) {
        // TODO Auto-generated method stub

    }

    public void addInclude(Object usecase, Object include) {
        // TODO Auto-generated method stub

    }

    public Collection getAllActors(Object ns) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllUseCases(Object ns) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getExtendedUseCases(Object ausecase) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getExtendingUseCases(Object usecase) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getExtends(Object abase, Object anextension) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getIncludedUseCases(Object ausecase) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getIncludes(Object abase, Object aninclusion) {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub

    }

    public void setAddition(Object handle, Object useCase) {
        // TODO Auto-generated method stub

    }

    public void setBase(Object extend, Object base) {
        // TODO Auto-generated method stub

    }

    public void setCondition(Object handle, Object booleanExpression) {
        // TODO Auto-generated method stub

    }

    public void setExtension(Object handle, Object ext) {
        // TODO Auto-generated method stub

    }

    public void setExtensionPoints(Object handle, Collection extensionPoints) {
        // TODO Auto-generated method stub

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
