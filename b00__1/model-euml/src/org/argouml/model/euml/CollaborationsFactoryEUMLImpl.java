// $Id:CollaborationsFactoryEUMLImpl.java 12721 2007-05-30 18:14:55Z tfmorris $
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

import org.argouml.model.CollaborationsFactory;

/**
 * Eclipse UML2 implementation of CollaborationsFactory.
 */
class CollaborationsFactoryEUMLImpl implements CollaborationsFactory {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param implementation The ModelImplementation.
     */
    public CollaborationsFactoryEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public Object buildActivator(Object owner, Object interaction) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildAssociationEndRole(Object atype) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildAssociationRole(Object from, Object to) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildAssociationRole(Object from, Object agg1, Object to, Object agg2, Boolean unidirectional) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildAssociationRole(Object link) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildClassifierRole(Object collaboration) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildCollaboration(Object handle) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildCollaboration(Object namespace, Object representedElement) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildInteraction(Object handle) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildMessage(Object acollab, Object arole) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createAssociationEndRole() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createAssociationRole() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createClassifierRole() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createCollaboration() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createCollaborationInstanceSet() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createInteraction() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createInteractionInstanceSet() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createMessage() {
        // TODO Auto-generated method stub
        return null;
    }


}
