// $Id$
/// Copyright (c) 2007,2009 Tom Morris and other contributors
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in the
//       documentation and/or other materials provided with the distribution.
//     * Neither the name of the project or its contributors may be used 
//       to endorse or promote products derived from this software without
//       specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE CONTRIBUTORS ``AS IS'' AND ANY
// EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE CONTRIBUTORS BE LIABLE FOR ANY
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.xml.sax.InputSource;

/**
 * The implementation of the XmiReader for EUML2.
 */
class XmiReaderEUMLImpl implements XmiReader {
    
    private static final Logger LOG = Logger.getLogger(XmiReaderEUMLImpl.class);

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;
    
    private static Set<String> searchDirs = new HashSet<String>();
    
    private Resource resource;

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
        // TODO: Auto-generated method stub
        return 0;
    }

    public String[] getIgnoredElements() {
        // TODO: Auto-generated method stub
        return new String[0];
    }

    public Map getXMIUUIDToObjectMap() {
        if (resource == null) {
            throw new IllegalStateException();
        }
        Map<String, EObject> map = new HashMap<String, EObject>();
        Iterator<EObject> it = resource.getAllContents();
        while (it.hasNext()) {
            EObject o  = it.next();
            map.put(resource.getURIFragment(o), o);
        }
        return map;
    }


    public Collection parse(InputSource inputSource, boolean readOnly)
        throws UmlException {
        
        if (inputSource == null) {
            throw new NullPointerException(
                    "The input source must be non-null."); //$NON-NLS-1$
        }
        InputStream is = null;
        boolean needsClosing = false;
        String name = inputSource.getSystemId();
        if (name == null) {
            name = inputSource.getPublicId();
        }
        if (name == null) {
            name = inputSource.toString();
        }
        LOG.debug("Parsing " + name);
        if (inputSource.getByteStream() != null) {
            is = inputSource.getByteStream();
        } else if (inputSource.getSystemId() != null) {
            try {
                URL url = new URL(inputSource.getSystemId());
                if (url != null) {
                    LOG.debug("Parsing URL " + url);
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

        String id = inputSource.getSystemId();
        if (id == null) {
            id = inputSource.getPublicId();
        }
        // The id contains the whole path, which is not what we want in models
        // (it's a tradeoff, but collisions will be unlikely with the xmi.id)
        if (id != null) {
            // for ArgoUML internal profiles, id will start with "org/argouml"
            int ix = id.indexOf("org/argouml/");
            if (ix != -1) {
                id = id.substring(ix);
            } else if ((ix = id.lastIndexOf('/')) != -1) {
                // for user profiles, use the filename only
                id = id.substring(ix + 1);
            }
        }

        Resource r = UMLUtil.getResource(modelImpl, 
                URI.createURI(id), readOnly);
        
        try {
            modelImpl.getModelEventPump().stopPumpingEvents();
            r.load(is, null);
        } catch (IOException e) {
            throw new UmlException(e);
        } finally {
            modelImpl.getModelEventPump().startPumpingEvents();
            if (needsClosing) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        resource = r;
        LOG.debug("Parsed resource " + resource 
                + " with " + resource.getContents().size() + " elements");
        return r.getContents();
    }

    public boolean setIgnoredElements(String[] elementNames) {
        // TODO: Auto-generated method stub
        return false;
    }

    public String getTagName() {
        if (resource == null) {
            return "uml:Model"; //$NON-NLS-1$
        }
        List l = resource.getContents();
        if (!l.isEmpty()) {
            return "uml:" //$NON-NLS-1$
                    + modelImpl.getMetaTypes().getName(l.get(0)); 
        } else {
            return null;
        }
    }

    public void addSearchPath(String path) {
        searchDirs.add(path);
    }

    public List<String> getSearchPath() {
        return new ArrayList<String>(searchDirs);
    }

    public void removeSearchPath(String path) {
        searchDirs.remove(path);
    }

    public String getHeader() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();
    }
    
}
