// $Id:XmiReaderEUMLImpl.java 12721 2007-05-30 18:14:55Z tfmorris $
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
import java.util.Map;

import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.xml.sax.InputSource;

/**
 * The implementation of the XmiReader for EUML2.
 */
class XmiReaderEUMLImpl implements XmiReader {

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
    public XmiReaderEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public int getIgnoredElementCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    public String[] getIgnoredElements() {
        // TODO Auto-generated method stub
        return null;
    }

    public Map getXMIUUIDToObjectMap() {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection parse(InputSource pIs) throws UmlException {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection parse(InputSource pIs, boolean profile)
        throws UmlException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean setIgnoredElements(String[] elementNames) {
        // TODO Auto-generated method stub
        return false;
    }

}
