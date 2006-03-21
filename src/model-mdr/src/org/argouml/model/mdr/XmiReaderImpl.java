// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.model.mdr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.netbeans.lib.jmi.xmi.InputConfig;
import org.netbeans.lib.jmi.xmi.UnknownElementsListener;
import org.omg.uml.modelmanagement.Model;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;

/**
 * A wrapper around the genuine XmiReader that provides public access with no
 * knowledge of actual UML implementation.
 * 
 * @author Bob Tarling
 */
public class XmiReaderImpl implements XmiReader, UnknownElementsListener {

    private Logger LOG = Logger.getLogger(XmiReaderImpl.class);
    
    private MDRModelImplementation parent;

    private XmiReferenceResolverImpl resolver;

    private RefPackage modelPackage;
 
    /*
     * Flag indicating unknown element was found in XMI file
     */
    private boolean unknownElement;
    
    /*
     * Name of first unknown element found (if not a UML 1.3 name)
     */
    private String unknownElementName;
    
    /*
     * Flag indicating that we think unknown element was due to a UML 1.3 file
     */
    private boolean uml13;
    
    /*
     * Elements to ignore errors on if they aren't found in the metamodel.
     */
    private String[] ignoredElements = new String[] {};
    
    /*
     * Flag indicating that we stripped at least one diagram during the import
     */
    private int ignoredElementCount;



    /**
     * Constructor for XMIReader.
     * @param parentModelImplementation The ModelImplementation
     * @param modelPackage extent to read user models into
     */
    public XmiReaderImpl(MDRModelImplementation parentModelImplementation,
            RefPackage modelPackage) {

        this.parent = parentModelImplementation;
        this.modelPackage = modelPackage;
    }

    /**
     * Parses a given inputsource as an XMI file conforming to our metamodel.
     * 
     * @param pIs
     *            The input source for parsing.
     * @return a collection of top level ModelElements
     * @throws UmlException
     *             if there is a problem
     */
    public Collection parse(InputSource pIs) throws UmlException {
        return parse(pIs, false);
    }

    /**
     * Parses a given inputsource as an XMI file conforming to our metamodel.
     * 
     * @param pIs
     *            The input source for parsing.
     * @param profile
     *            true if the model is a profile model. This will be read into a
     *            separate extent.
     * @return a collection of top level ModelElements
     * @throws UmlException
     *             if there is a problem
     */
    public Collection parse(InputSource pIs, boolean profile) throws UmlException {
        Collection newElements = null;
        RefPackage extent = modelPackage;

        try {
            LOG.info("Loading '" + pIs.getSystemId() + "'");

            InputConfig config = new InputConfig();
            config.setUnknownElementsListener(this);
            config.setUnknownElementsIgnored(true);
            
            resolver = new XmiReferenceResolverImpl(
                    new RefPackage[] {extent }, config);

            config.setReferenceResolver(resolver);

            XMIReader xmiReader = XMIReaderFactory.getDefault()
                    .createXMIReader(config);

            // Copy stream to a file to be sure it can be repositioned. 
            // TODO: find a way to remove this, since this *always* alterate the
            // performances for reading an XMI file, even if it's not UML 1.4.
            File tmpFile = copySource(pIs);

            /*
             * MDR has a hardcoded printStackTrace on all exceptions,
             * even if they're caught, which is unsightly, so we handle
             * unknown elements ourselves rather than letting MDR throw
             * an exception for us to catch.
             */
            InputConfig config2 = (InputConfig) xmiReader.getConfiguration();
            config2.setUnknownElementsListener(this);
            config2.setUnknownElementsIgnored(true);
            unknownElement = false;
            uml13 = false;
            ignoredElementCount = 0;

            // Disable event delivery during model load
            parent.getModelEventPump().stopPumpingEvents();
            
            try {
                newElements = 
                    xmiReader.read(tmpFile.toURI().toString(), extent);
                
                // If a UML 1.3 file, attempt to upgrade it to UML 1.4
                if (uml13) {
                    LOG.info("XMI file doesn't appear to be UML 1.4 - "
                            + "attempting UML 1.3->UML 1.4 conversion");
                    final String[] transformFiles = 
                        new String[] {
                            "NormalizeNSUML.xsl", 
                            "uml13touml14.xsl" 
                        };
                    
                    unknownElement = false;
                    // InputSource xformedInput = 
                    //        chainedTransform(transformFiles, pIs);
                    InputSource xformedInput = serialTransform(transformFiles,
                            new InputSource(new FileInputStream(tmpFile)));
                    newElements = xmiReader.read(xformedInput.getByteStream(),
                            xformedInput.getSystemId(), extent);
                }
                
            } finally {
                parent.getModelEventPump().startPumpingEvents();  
            }
            
            if (unknownElement) {
                throw new UmlException("Unknown element in XMI file : "
                        + unknownElementName);
            }
            
            if (ignoredElementCount > 0) {
                LOG.warn("Ignored one or more elements from list "
                        + ignoredElements);
            }

        } catch (MalformedXMIException e) {
            throw new UmlException(e);
        } catch (IOException e) {
            throw new UmlException(e);
        }
        LOG.info("Loaded total of " + newElements.size() 
                + " model element(s).");
        if (profile) {
            if (newElements.size() != 1) {
                LOG.error("Unexpected number of profile model elements (must be 1) : "
                        + newElements.size());
            } else {
                RefObject model = (RefObject) newElements.iterator().next();
                LOG.info("**Saving profile with MofID : " + model.refMofId());
                parent.setProfileModel(model);
            }

        }
        return newElements;
    }
    
    /**
     * @see org.argouml.model.XmiReader#parseToModel(org.xml.sax.InputSource)
     */
    public Object parseToModel(InputSource is) throws UmlException {
        Model model = null;
        Collection newElements = parse(is);
        if (newElements != null && !newElements.isEmpty()) {
            Object current;
            Iterator elements = newElements.iterator();
            while (elements.hasNext()) {
                current = elements.next();
                if (current instanceof Model) {
                    Model currentModel = (Model) current;
                    LOG.info("Loaded model '" + currentModel.getName() + "'");
                    model = currentModel;
                }
            }
        }
        return model;
    }

    /**
     * @return the map
     */
    public Map getXMIUUIDToObjectMap() {
        if (resolver != null)
            return resolver.getIdToObjectMap();
        return null;
    }

    private static final String STYLE_PATH = 
        "/org/argouml/model/mdr/conversions/";

    /*
     * A near clone of this code works fine outside of ArgoUML, but throws a
     * null pointer exception during the transform when run within ArgoUML I
     * think it's something to do with the class libraries being used, but I
     * can't figure out what, so I've done a simpler, less efficient stepwise
     * translation below in serialTransform
     */
    private InputSource chainedTransform(String[] styles, InputSource input)
        throws UmlException {
        SAXTransformerFactory stf = (SAXTransformerFactory) TransformerFactory.
                newInstance();

        try {
            // Set up reader to be first filter in chain
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XMLReader last = parser.getXMLReader();

            // Create filter for each style sheet and chain to previous
            // filter/reader
            for (int i = 0; i < styles.length; i++) {
                String xsltFileName = STYLE_PATH + styles[i];
                URL xsltUrl = getClass().getResource(xsltFileName);
                if (xsltUrl == null) {
                    throw new UmlException("Error opening XSLT style sheet : "
                            + xsltFileName);
                }
                StreamSource xsltStreamSource = new StreamSource(xsltUrl.
                        openStream());
                xsltStreamSource.setSystemId(xsltUrl.toExternalForm());
                XMLFilter filter = stf.newXMLFilter(xsltStreamSource);

                filter.setParent(last);
                last = filter;
            }

            SAXSource transformSource = new SAXSource(last, input);

            // Create temporary file for output
            // TODO: we should be able to chain this directly to XMI reader
            File tmpFile = File.createTempFile("zargo_model_", ".xmi");
            tmpFile.deleteOnExit();
            StreamResult result = new StreamResult(
                    new FileOutputStream(tmpFile));

            Transformer transformer = stf.newTransformer();
            transformer.transform(transformSource, result);

            return new InputSource(new FileInputStream(tmpFile));

        } catch (SAXException e) {
            throw new UmlException(e);
        } catch (ParserConfigurationException e) {
            throw new UmlException(e);
        } catch (IOException e) {
            throw new UmlException(e);
        } catch (TransformerConfigurationException e) {
            throw new UmlException(e);
        } catch (TransformerException e) {
            throw new UmlException(e);
        }

    }

    private InputSource serialTransform(String[] styles, InputSource input)
        throws UmlException {
        SAXSource myInput = new SAXSource(input);
        SAXTransformerFactory stf = (SAXTransformerFactory) TransformerFactory.
                newInstance();
        try {

            for (int i = 0; i < styles.length; i++) {
                // Set up source for style sheet
                String xsltFileName = STYLE_PATH + styles[i];
                URL xsltUrl = getClass().getResource(xsltFileName);
                if (xsltUrl == null) {
                    throw new UmlException("Error opening XSLT style sheet : "
                            + xsltFileName);
                }
                StreamSource xsltStreamSource = new StreamSource(xsltUrl.
                        openStream());
                xsltStreamSource.setSystemId(xsltUrl.toExternalForm());

                // Create & set up temporary output file
                File tmpOutFile = File.createTempFile("zargo_model_", ".xmi");
                tmpOutFile.deleteOnExit();
                StreamResult result = new StreamResult(new FileOutputStream(
                        tmpOutFile));

                // Create transformer and do transformation
                Transformer transformer = stf.newTransformer(xsltStreamSource);
                transformer.transform(myInput, result);

                LOG.info("Wrote converted XMI file - " + tmpOutFile
                        + " converted using : " + xsltFileName);

                // Set up for next iteration
                myInput = new SAXSource(new InputSource(new FileInputStream(
                        tmpOutFile)));
            }
            return myInput.getInputSource();
        } catch (IOException e) {
            throw new UmlException(e);
        } catch (TransformerConfigurationException e) {
            throw new UmlException(e);
        } catch (TransformerException e) {
            throw new UmlException(e);
        }

    }

    private File copySource(InputSource input) throws IOException {
        byte[] buf = new byte[2048];
        int len;

        // Create & set up temporary output file
        File tmpOutFile = File.createTempFile("zargo_model_", ".xmi");
        tmpOutFile.deleteOnExit();
        FileOutputStream out = new FileOutputStream(tmpOutFile);
        InputStream in = input.getByteStream();

        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        LOG.debug("Wrote copied XMI file to " + tmpOutFile);
        return tmpOutFile;
    }

    /**
     * @see org.netbeans.lib.jmi.xmi.UnknownElementsListener#elementFound(java.lang.String)
     */
    public void elementFound(String name) {
        // Silently ignore anything specificed by caller attempt to continue
        if (ignoredElements != null) {
            for (int i = 0; i < ignoredElements.length; i++) {
                if (name.equals(ignoredElements[i])) {
                    ignoredElementCount++;   
                    return;
                }
            }
        }
        
        unknownElement = true;
        if (name.startsWith("Foundation.Core.")) {
            uml13 = true;
        } else {
            if (unknownElementName == null) {
                unknownElementName = name;
            }
            LOG.error("Unknown XMI element named : " + name);
        }
    }


    /**
     * @see org.argouml.model.XmiReader#setIgnoredElements(java.lang.String[])
     */
    public boolean setIgnoredElements(String[] elementNames) {
        if (elementNames == null) {
            elementNames = new String[] {};
        } else {
            ignoredElements = elementNames;
        }
        return true;
    }
    
    /**
     * @see org.argouml.model.XmiReader#getIgnoredElements()
     */
    public String[] getIgnoredElements() {
        return ignoredElements;
    }
    

    /**
     * @see org.argouml.model.XmiReader#getIgnoredElementCount()
     */
    public int getIgnoredElementCount() {
        return ignoredElementCount;
    }
}
