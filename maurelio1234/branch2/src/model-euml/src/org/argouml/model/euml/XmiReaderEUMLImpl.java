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

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.xml.sax.InputSource;

/**
 * The implementation of the XmiReader for EUML2.
 */
class XmiReaderEUMLImpl implements XmiReader {

    private static final Logger LOG = 
        Logger.getLogger(XmiReaderEUMLImpl.class);
        
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
        return new String[0];
    }

    public Map getXMIUUIDToObjectMap() {
        // TODO Auto-generated method stub
        return new HashMap();
    }

    public Collection parse(InputSource inputSource) throws UmlException {
        return parse(inputSource, false);
    }

    public Collection parse(InputSource inputSource, boolean profile)
            throws UmlException {
        EditingDomain editingDomain = modelImpl.getEditingDomain();
        for (Resource resource 
                : (EList<Resource>) editingDomain.getResourceSet().getResources()) {
            resource.unload();
        }

	Resource r = editingDomain.createResource(
	        "http://argouml.tigris.org/euml/resource/default_uri.xmi"); //$NON-NLS-1$
        try {
            r.load(inputSource.getByteStream(), null);
        } catch (IOException e) {
            throw new UmlException(e);
        }

        return r.getContents();
    }

    public boolean setIgnoredElements(String[] elementNames) {
        // TODO Auto-generated method stub
        return false;
    }

    public String getTagName() {
        // This is not quite right
        // TODO: Solve this
        return "uml:Model"; //$NON-NLS-1$
    }

    public void addSearchPath(String path) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();
        
    }

    public List<String> getSearchPath() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();
        
    }

    public void removeSearchPath(String path) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();
        
    }
    
}
