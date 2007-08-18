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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.xml.sax.InputSource;

/**
 * The implementation of the XmiReader for EUML2.
 */
class XmiReaderEUMLImpl implements XmiReader {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;
    
    private static List<String> searchDirs = new ArrayList();

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
        if (inputSource == null) {
            throw new NullPointerException("The input source must be non-null."); //$NON-NLS-1$
        }
        InputStream is = null;
        boolean needsClosing = false;
        if (inputSource.getByteStream() != null) {
            is = inputSource.getByteStream();
        } else if (inputSource.getSystemId() != null) {
            try {
                URL url = new URL(inputSource.getSystemId());
                if (url != null) {
                    is = url.openStream();
                    if (is != null) {
                        is = new BufferedInputStream(is);
                        needsClosing = true;
                    }
                }
            } catch (MalformedURLException e) {
                // do nothing
            } catch (IOException e) {
                // do nothing
            }

        }
        if (is == null) {
            throw new UnsupportedOperationException();
        }

        EditingDomain editingDomain = modelImpl.getEditingDomain();
        for (Resource resource : editingDomain.getResourceSet().getResources()) {
            resource.unload();
        }
        
        Resource r = UMLUtil.getResource(modelImpl, UMLUtil.DEFAULT_URI);
        try {
            modelImpl.getModelEventPump().stopPumpingEvents();
            r.load(is, null);
            modelImpl.getModelEventPump().startPumpingEvents();
        } catch (IOException e) {
            throw new UmlException(e);
        } finally {
            if (needsClosing) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
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
        searchDirs.add(path);
    }

    public List<String> getSearchPath() {
        return searchDirs;
    }

    public void removeSearchPath(String path) {
        searchDirs.remove(path);
    }
    
}
