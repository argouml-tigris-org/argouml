// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.jmi.reflect.InvalidObjectException;
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
import org.argouml.model.XmiException;
import org.argouml.model.XmiReader;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.netbeans.lib.jmi.xmi.InputConfig;
import org.netbeans.lib.jmi.xmi.UnknownElementsListener;
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
class XmiReaderImpl implements XmiReader, UnknownElementsListener {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(XmiReaderImpl.class);

    private MDRModelImplementation modelImpl;

    private XmiReferenceResolverImpl resolver;

    private RefPackage modelPackage;

    /**
     * Flag indicating unknown element was found in XMI file.
     */
    private boolean unknownElement;

    /**
     * Name of first unknown element found (if not a UML 1.3 name).
     */
    private String unknownElementName;

    /**
     * Flag indicating that we think unknown element was due to a UML 1.3 file.
     */
    private boolean uml13;

    /**
     * Elements to ignore errors on if they aren't found in the metamodel.
     */
    private String[] ignoredElements = new String[] {};

    /**
     * Flag indicating that we stripped at least one diagram during the import.
     */
    private int ignoredElementCount;

    private static List<String> searchDirs = new ArrayList<String>();

    /**
     * Constructor for XMIReader.
     * @param parentModelImplementation The ModelImplementation
     * @param mp extent to read user models into
     */
    public XmiReaderImpl(MDRModelImplementation parentModelImplementation,
            RefPackage mp) {

        modelImpl = parentModelImplementation;
        modelPackage = mp;
    }

    @Deprecated
    public Collection parse(InputSource inputSource) throws UmlException {
        return parse(inputSource, false);
    }

    public Collection parse(InputSource inputSource, boolean profile)
        throws UmlException {
        
        Collection<RefObject> newElements = Collections.emptyList();
        RefPackage extent = modelPackage;

        try {
            LOG.info("Loading '" + inputSource.getSystemId() + "'");

            InputConfig config = new InputConfig();
            config.setUnknownElementsListener(this);
            config.setUnknownElementsIgnored(true);

            resolver = new XmiReferenceResolverImpl(new RefPackage[] {extent},
                    config, modelImpl.getObjectToId(), searchDirs, profile);
            config.setReferenceResolver(resolver);
            
            XMIReader xmiReader =
                    XMIReaderFactory.getDefault().createXMIReader(config);

            /*
             * MDR has a hardcoded printStackTrace on all exceptions,
             * even if they're caught, which is unsightly, so we handle
             * unknown elements ourselves rather than letting MDR throw
             * an exception for us to catch.
             * 
             * org/netbeans/lib/jmi/util/Logger.java
             * 
             * This can be uses to disable logging.  Default output is 
             * System.err
             * setProperty("org.netbeans.lib.jmi.Logger.fileName", "")
             * 
             * The property org.netbeans.lib.jmi.Logger controls the minimum 
             * severity level for logging
             */
            // Turn off NetBeans logging to System.err
//            System.setProperty("org.netbeans.lib.jmi.Logger.fileName", "");
            // Set minimum severity level for MDR
//            System.setProperty("org.netbeans.lib.jmi.Logger", 
//                    Integer.toString(ErrorManager.INFORMATIONAL));
            InputConfig config2 = (InputConfig) xmiReader.getConfiguration();
            config2.setUnknownElementsListener(this);
            config2.setUnknownElementsIgnored(true);
            unknownElement = false;
            uml13 = false;
            ignoredElementCount = 0;

            // Disable event delivery during model load
            modelImpl.getModelEventPump().stopPumpingEvents();

            Collection<RefObject> startTopElements = 
                modelImpl.getFacade().getRootElements();
            int numElements = startTopElements.size();
            LOG.debug("Number of top level elements before import: "
                    + numElements);

            try {
                String systemId = inputSource.getSystemId();
                if (systemId == null) {
                    File file = copySource(inputSource);
                    systemId = file.toURI().toURL().toExternalForm();
                    inputSource = new InputSource(systemId);
                }
                newElements =
                    xmiReader.read(inputSource.getByteStream(), systemId, extent);
                
                // If a UML 1.3 file, attempt to upgrade it to UML 1.4
                if (uml13) {
                    // First delete model data from our first attempt
                    deleteElements(newElements);

                    // Clear the associated ID maps & reset starting collection
                    resolver.clearIdMaps();
                    startTopElements = modelImpl.getFacade().getRootElements();

                    newElements = convertAndLoadUml13(inputSource.getSystemId(),
                            extent, xmiReader, inputSource);
                }

                numElements = modelImpl.getFacade().getRootElements().size()
                        - numElements;

                // This indicates a malformed XMI file.  Log the error.
                if (newElements.size() != numElements) {
                    LOG.error("Mismatch between number of elements returned by"
                            + " XMIReader ("
                            + newElements.size()
                            + ") and number of new top level elements found ("
                            + numElements + ")");
                }


            } finally {
                modelImpl.getModelEventPump().startPumpingEvents();
            }

            if (unknownElement) {
                throw new XmiException("Unknown element in XMI file : "
                        + unknownElementName);
            }

            if (ignoredElementCount > 0) {
                LOG.warn("Ignored one or more elements from list "
                        + ignoredElements);
            }

        } catch (MalformedXMIException e) {
            throw new XmiException(e);
        } catch (IOException e) {
            throw new XmiException(e);
        }

        if (profile) {
            modelImpl.setProfileElements(newElements);
        }
        return newElements;
    }

    
    private void deleteElements(Collection<RefObject> elements) {
        Collection<RefObject> toDelete = new ArrayList<RefObject>(elements);
        for (RefObject refObject : toDelete) {
            try {
                refObject.refDelete();
            } catch (InvalidObjectException e) {
                // Just continue.  We tried to delete something 
                // twice, probably because it was contained in 
                // another element that we already deleted.
            }
        }
    }

    private Collection<RefObject> convertAndLoadUml13(String systemId,
            RefPackage extent, XMIReader xmiReader, InputSource input)
        throws FileNotFoundException, UmlException, IOException,
            MalformedXMIException {
        
        LOG.info("XMI file doesn't appear to be UML 1.4 - "
                + "attempting UML 1.3->UML 1.4 conversion");
        final String[] transformFiles = new String[] { 
            "NormalizeNSUML.xsl",
            "uml13touml14.xsl", };

        unknownElement = false;
        // InputSource xformedInput = chainedTransform(transformFiles, pIs);
        InputSource xformedInput = serialTransform(transformFiles,
                input);
        return xmiReader.read(xformedInput.getByteStream(), xformedInput
                .getSystemId(), extent);
    }

    /*
     * @see org.argouml.model.XmiReader#getXMIUUIDToObjectMap()
     */
    public Map<String, Object> getXMIUUIDToObjectMap() {
        if (resolver != null) {
            return resolver.getIdToObjectMap();
        }
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
        throws XmiException {
        SAXTransformerFactory stf =
            (SAXTransformerFactory) TransformerFactory.newInstance();

        // TODO: Reconfigure exception handling to distinguish between errors
        // that are possible due to bad input data and those that represent
        // unexpected processing errors.
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
                    throw new IOException("Error opening XSLT style sheet : "
                            + xsltFileName);
                }
                StreamSource xsltStreamSource =
                    new StreamSource(xsltUrl.openStream());
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
            StreamResult result =
                new StreamResult(
                    new FileOutputStream(tmpFile));

            Transformer transformer = stf.newTransformer();
            transformer.transform(transformSource, result);

            return new InputSource(new FileInputStream(tmpFile));

        } catch (SAXException e) {
            throw new XmiException(e);
        } catch (ParserConfigurationException e) {
            throw new XmiException(e);
        } catch (IOException e) {
            throw new XmiException(e);
        } catch (TransformerConfigurationException e) {
            throw new XmiException(e);
        } catch (TransformerException e) {
            throw new XmiException(e);
        }

    }

    private InputSource serialTransform(String[] styles, InputSource input)
        throws UmlException {
        SAXSource myInput = new SAXSource(input);
        SAXTransformerFactory stf =
            (SAXTransformerFactory) TransformerFactory.newInstance();
        try {

            for (int i = 0; i < styles.length; i++) {
                // Set up source for style sheet
                String xsltFileName = STYLE_PATH + styles[i];
                URL xsltUrl = getClass().getResource(xsltFileName);
                if (xsltUrl == null) {
                    throw new UmlException("Error opening XSLT style sheet : "
                            + xsltFileName);
                }
                StreamSource xsltStreamSource =
                    new StreamSource(xsltUrl.openStream());
                xsltStreamSource.setSystemId(xsltUrl.toExternalForm());

                // Create & set up temporary output file
                File tmpOutFile = File.createTempFile("zargo_model_", ".xmi");
                tmpOutFile.deleteOnExit();
                StreamResult result =
                    new StreamResult(new FileOutputStream(
                        tmpOutFile));

                // Create transformer and do transformation
                Transformer transformer = stf.newTransformer(xsltStreamSource);
                transformer.transform(myInput, result);

                LOG.info("Wrote converted XMI file - " + tmpOutFile
                        + " converted using : " + xsltFileName);

                // Set up for next iteration
                myInput =
                    new SAXSource(new InputSource(new FileInputStream(
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

    private static final String UML_13_ELEMENTS[] = 
    {
        "TaggedValue.value",
        "TaggedValue.tag",
        "ModelElement.templateParameter2",
        "ModelElement.templateParameter3",
        "Classifier.structuralFeature",
        "Classifier.parameter",
        "AssociationEnd.type",
        "Node.resident",
        "ElementResidence.implementationLocation",
        "TemplateParameter.modelElement",
        "TemplateParameter.modelElement2",
        "Constraint.constrainedElement2",
        "UseCase.include2",
        "StateMachine.subMachineState",
        "ClassifierRole.message1",
        "ClassifierRole.message2",
        "Message.message3",
        "Message.message4",
        "ElementImport.modelElement",

        "ModelElement.elementResidence",
        "ModelElement.presentation",
        "ModelElement.supplierDependency",
        "ModelElement.templateParameter2",
        "ModelElement.templateParameter3",
        "ModelElement.binding",
        "GeneralizableElement.specialization",
        "Classifier.associationEnd",
        "Classifier.participant",
        "Operation.method",
        "Stereotype.extendedElement",
        "Stereotype.requiredTag",
        "TaggedValue.stereotype",
        "Signal.context",
        "Signal.reception",
        "Signal.sendAction",

        "UseCase.include2",
        "UseCase.extend2",
        "ExtensionPoint.extend",
        "Link.stimulus",
        "Instance.attributeLink",
        "Action.stimulus",
        "Event.state",
        "Event.transition",
        "Transition.state",

        "ClassifierRole.message1",
        "ClassifierRole.message2",
        "Message.message3",
        "Message.message4",

        "Action.state1",
        "Action.state2",
        "Action.state3",
        "Instance.stimulus1",
        "Instance.stimulus2",
        "Instance.stimulus3",

    };
    

    public void elementFound(String name) {
        // Silently ignore anything specified by caller attempt to continue
        if (ignoredElements != null) {
            for (int i = 0; i < ignoredElements.length; i++) {
                if (name.equals(ignoredElements[i])) {
                    ignoredElementCount++;
                    return;
                }
            }
        }
        
        if (name.startsWith("Foundation.")) {
            uml13 = true;
            return;
        }

        for (int i = 0; i < UML_13_ELEMENTS.length; i++) {
            if (name.endsWith(UML_13_ELEMENTS[i])) {
                uml13 = true;
                return;
            }
        }

        unknownElement = true;
        if (unknownElementName == null) {
            unknownElementName = name;
        }
        LOG.error("Unknown XMI element named : " + name);
        
    }


    public boolean setIgnoredElements(String[] elementNames) {
        if (elementNames == null) {
            elementNames = new String[] {};
        } else {
            ignoredElements = elementNames;
        }
        return true;
    }


    public String[] getIgnoredElements() {
        return ignoredElements;
    }


    public int getIgnoredElementCount() {
        return ignoredElementCount;
    }


    public String getTagName() {
        return "XMI";
    }


    public void addSearchPath(String path) {
        searchDirs.add(path);
    }

    public void removeSearchPath(String path) {
        searchDirs.remove(path);
    }

    public List<String> getSearchPath() {
        return searchDirs;
    }

}
