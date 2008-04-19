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

import org.argouml.model.ActivityGraphsHelper;

/**
 * The implementation of the ActivityGraphsHelper for EUML.
 */
class ActivityGraphsHelperEUMLImpl implements ActivityGraphsHelper {

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
    public ActivityGraphsHelperEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public void addContent(Object partition, Object modeElement) {
        // TODO Auto-generated method stub

    }

    public void addInState(Object classifierInState, Object state) {
        // TODO Auto-generated method stub

    }

    public void addParameter(Object objectFlowState, Object parameter) {
        // TODO Auto-generated method stub

    }

    public Object findClassifierByName(Object ofs, String s) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object findStateByName(Object c, String s) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isAddingActivityGraphAllowed(Object context) {
        // TODO Auto-generated method stub
        return false;
    }

    public void removeContent(Object partition, Object modeElement) {
        // TODO Auto-generated method stub

    }

    public void removeParameter(Object objectFlowState, Object parameter) {
        // TODO Auto-generated method stub

    }

    public void setContents(Object partition, Collection newContents) {
        // TODO Auto-generated method stub

    }

    public void setInStates(Object classifierInState, Collection newStates) {
        // TODO Auto-generated method stub

    }

    public void setParameters(Object objectFlowState, Collection parameters) {
        // TODO Auto-generated method stub

    }

    public void setSynch(Object objectFlowState, boolean isSynch) {
        // TODO Auto-generated method stub

    }

}
