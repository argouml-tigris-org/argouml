// $Id:UmlHelperEUMLImpl.java 12721 2007-05-30 18:14:55Z tfmorris $
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
import java.util.Iterator;

import org.argouml.model.UmlHelper;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Transition;


/**
 * The implementation of the UmlHelper for EUML2.
 */
class UmlHelperEUMLImpl implements UmlHelper {

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
    public UmlHelperEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public void addListenersToModel(Object model) {
        // Nothing to do

    }

    // TODO: Model implementation independent
    public void deleteCollection(Collection col) {
        Iterator it = col.iterator();
        while (it.hasNext()) {
            modelImpl.getUmlFactory().delete(it.next());
        }
    }

    /*
     * @see org.argouml.model.UmlHelper#getSource(java.lang.Object)
     */
    public Object getSource(Object relationship) {
        if (relationship instanceof Relationship) {
            // handles all children of relationship including extend and
            // include which are not members of core
            return modelImpl.getCoreHelper().getSource(relationship);
        } else if (relationship instanceof Transition) {
            return modelImpl.getStateMachinesHelper().getSource(relationship);
        } else if (relationship instanceof Property) {
            // TODO: We expect an association end here - check more carefully? - tfm
            return modelImpl.getCoreHelper().getSource(relationship);
        }
        throw new IllegalArgumentException();
    }

    /*
     * @see org.argouml.model.UmlHelper#getDestination(java.lang.Object)
     */
    public Object getDestination(Object relationShip) {
        if (relationShip instanceof Relationship) {
            // handles all children of relationship including extend and
            // include which are not members of core
            return modelImpl.getCoreHelper().getDestination(relationShip);
        } else if (relationShip instanceof Transition) {
            return modelImpl.getStateMachinesHelper().
                    getDestination(relationShip);
        } else if (relationShip instanceof Property) {
            // TODO: We expect an association end here - check more carefully? - tfm
            return modelImpl.getCoreHelper().getDestination(relationShip);
        }
        throw new IllegalArgumentException();
    }

}
