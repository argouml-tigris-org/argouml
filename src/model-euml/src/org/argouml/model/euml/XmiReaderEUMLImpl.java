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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.xml.sax.InputSource;

/**
 * The implementation of the XmiReader for EUML2.
 */
class XmiReaderEUMLImpl implements XmiReader {

    private static Logger LOG = Logger.getLogger(XmiReaderEUMLImpl.class);
    
    // TODO: This shouldn't really be hardcoded - tfm
    private final static String ENCODING = "UTF-8";
    
    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    private ResourceSet resourceSet;

    /**
     * Constructor.
     * 
     * @param implementation
     *            The ModelImplementation.
     */
    public XmiReaderEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;

        resourceSet = new ResourceSetImpl();
        
        // TODO: We need both complete paths and version independence here
        // We probably need sometype of run-time lookup scheme
        // This code is from 
        // http://www.eclipse.org/modeling/mdt/uml2/docs/guides/UML2_2.0_Migration_Guide/guide.html
        URI umlPluginURI = URI.createURI("jar:file:/" 
                + "org.eclipse.uml2.uml_2.1.0.v200703011358.jar" 
                + "!/");
        URI umlResourcesPluginURI = URI.createURI("jar:file:/" 
                + "org.eclipse.uml2.uml.resources_2.1.0.v200705251139.jar"
                + "!/");

        // register the necessary pathmaps
        Map uriMap = URIConverter.URI_MAP;
        uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP),
                umlResourcesPluginURI.appendSegment("libraries").appendSegment(""));
        uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP),
                umlResourcesPluginURI.appendSegment("metamodels").appendSegment(""));
        uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP),
                umlResourcesPluginURI.appendSegment("profiles").appendSegment(""));


        // for a stand alone application, it is necessary to map the
        // platform plugin URI scheme to the workspace location
        uriMap.put(URI.createURI("platform:/plugin/org.eclipse.uml2.uml/"), 
                umlPluginURI);

        // Add namespace URIs for eCore and eUML2 to package registry
        Map packageRegistry = resourceSet.getPackageRegistry();
        packageRegistry.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
        packageRegistry.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);

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

        // TODO:  This is a hack.  Look for alternatives, possibly reworking API
        File inputFile;
        try {
            inputFile = copySource(inputSource);
        } catch (IOException e) {
            throw new UmlException("Error creating temporary file", e);
        }
        
        Resource resource = resourceSet.getResource(URI.createFileURI(inputFile.toString()), true);
        EcoreUtil.resolveAll(resource);

        return resource.getContents();
    }

    public boolean setIgnoredElements(String[] elementNames) {
        // TODO Auto-generated method stub
        return false;
    }

    private File copySource(InputSource input) throws IOException {
        byte[] buf = new byte[2048];
        int len;

        // Create & set up temporary output file
        File tmpOutFile = File.createTempFile("zargo_model_", ".xmi");
        tmpOutFile.deleteOnExit();
        FileOutputStream out = new FileOutputStream(tmpOutFile);
        
        // Add our XML declaration back to the front of the file
//        String decl = "<?xml version=\"1.0\" encoding=\"" 
//            + ENCODING + "\"?>\n";
//        out.write(decl.getBytes(ENCODING), 0, decl.length());

        InputStream in = input.getByteStream();
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        LOG.debug("Wrote copied XMI file to " + tmpOutFile);
        return tmpOutFile;
    }

    public String getTagName() {
        return "uml:Model";
    }
}
