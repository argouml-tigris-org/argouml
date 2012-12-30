/* $Id$
 *****************************************************************************
 * Copyright (c) 2005-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *    Tom Morris
 *    Luis Sergio Oliveira (euluis)
 *    Laurent Braud
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import org.argouml.model.UmlException;
import org.argouml.model.XmiException;
import org.argouml.model.XmiReader;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.netbeans.lib.jmi.xmi.InputConfig;
import org.netbeans.lib.jmi.xmi.UnknownElementsListener;
import org.netbeans.lib.jmi.xmi.XMIHeaderConsumer;
import org.omg.uml.UmlPackage;
import org.openide.ErrorManager;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;

/**
 * A wrapper around the genuine XmiReader that provides public access with no
 * knowledge of actual UML implementation.
 *
 * @author Bob Tarling
 */
class XmiReaderImpl implements XmiReader, UnknownElementsListener,
        XMIHeaderConsumer {

    static final String TEMP_XMI_FILE_PREFIX = "zargo_model_";

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(XmiReaderImpl.class.getName());

    private static String tempXMIFileURIPrefix;

    private MDRModelImplementation modelImpl;

    private XmiReferenceResolverImpl resolver;

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

    /**
     * String that we pulled from the header of the XMI file
     */
    private String xmiHeader;


    /**
     * Constructor for XMIReader.
     * @param parentModelImplementation The ModelImplementation
     */
    XmiReaderImpl(MDRModelImplementation parentModelImplementation) {
        modelImpl = parentModelImplementation;
    }

    public Collection parse(InputSource inputSource, boolean readOnly)
        throws UmlException {

        System.setProperty("javax.xml.transform.TransformerFactory",
        "net.sf.saxon.TransformerFactoryImpl");

        Collection<RefObject> newElements = Collections.emptyList();

        String extentBase = inputSource.getPublicId();
        if (extentBase == null) {
            extentBase = inputSource.getSystemId();
        }
        if (extentBase == null) {
            extentBase = MDRModelImplementation.MODEL_EXTENT_NAME;
        }
        String extentName = extentBase;
        UmlPackage extent =
            (UmlPackage) modelImpl.getRepository().getExtent(extentName);
        int serial = 1;
        while (extent != null) {
            extentName = extentBase + " " + serial;
            serial++;
            extent = (UmlPackage) modelImpl.getRepository().getExtent(
                    extentName);
        }

        extent = (UmlPackage) modelImpl.createExtent(extentName, readOnly);
        if (extent == null) {
            LOG.log(Level.SEVERE, "Failed to create extent " + extentName);
        }

        try {
            LOG.log(Level.INFO, "Loading to extent {0} {1}", new Object[]{extentName, extent});

            InputConfig config = new InputConfig();
            config.setUnknownElementsListener(this);
            config.setUnknownElementsIgnored(true);

            String pId = inputSource.getPublicId();
            String sId = modelImpl.getPublic2SystemIds().get(pId);
            if (sId != null) {
                if (sId.equals(inputSource.getSystemId())) {
                    LOG.log(Level.INFO, "Attempt to reread profile - ignoring - "
                            + "publicId = \"" + pId + "\";  systemId = \""
                            + sId + "\".");
                    return Collections.emptySet();
                } else {
                    throw new UmlException("Profile with the duplicate publicId "
                            + "is being loaded! publicId = \"" + pId
                            + "\"; existing systemId = \""
                            + modelImpl.getPublic2SystemIds().get(pId)
                            + "\"; new systemId = \"" + sId + "\".");
                }
            }
            resolver = new XmiReferenceResolverImpl(new RefPackage[] {extent},
                    config, modelImpl.getObjectToId(),
                    modelImpl.getPublic2SystemIds(), modelImpl.getIdToObject(),
                    modelImpl.getSearchPath(),
                    readOnly,
                    inputSource.getPublicId(), inputSource.getSystemId(),
                    modelImpl);
            config.setReferenceResolver(resolver);
            config.setHeaderConsumer(this);

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
             *              org.netbeans.mdr.Logger
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

            try {
                String systemId = inputSource.getSystemId();
                // If we've got a streaming input, copy it to make sure we'll
                // be able to rewind it if necessary
                if (inputSource.getByteStream() != null
                        || inputSource.getCharacterStream() != null) {
                    File file = copySource(inputSource);
                    systemId = file.toURI().toURL().toExternalForm();
                    String publicId = inputSource.getPublicId();
                    inputSource = new InputSource(systemId);
                    inputSource.setPublicId(publicId);
                }
                MDRepository repository = modelImpl.getRepository();

                // Use a transaction to avoid the performance penalty (3x) of
                // MDR's autocommit mode
                repository.beginTrans(true);

                // Issue 5816 : invalid XMI
                try {
                    newElements = xmiReader.read(inputSource.getByteStream(),
                            systemId, extent);
                } catch (MalformedXMIException e) {
                    repository.endTrans(true);
                    repository.beginTrans(true);
                    resolver.clearIdMaps();
                    inputSource = convertFromInvalidXMI(inputSource);
                    newElements = xmiReader.read(inputSource.getByteStream(),
                            systemId, extent);

                }

                // If a UML 1.3 file, attempt to upgrade it to UML 1.4
                if (uml13) {
                    // Roll back transaction from first attempt & start new one
                    repository.endTrans(true);
                    repository.beginTrans(true);

                    // Clear the associated ID maps & reset starting collection
                    resolver.clearIdMaps();

                    newElements = convertAndLoadUml13(inputSource.getSystemId(),
                            extent, xmiReader, inputSource);
                }

                // Commit our transaction
                repository.endTrans();
            } catch (Throwable e) {
                // Roll back transaction to remove any partial results read
                try {
                    modelImpl.getRepository().endTrans(true);
                } catch (Throwable e2) {
                    // Ignore any error.  The transaction may already have
                    // been unwound as part of exception processing by MDR
                }
                if (e instanceof MalformedXMIException) {
                    throw (MalformedXMIException) e;
                } else if (e instanceof IOException) {
                    throw (IOException) e;
                } else {
                    // We shouldn't get here, but just in case...
                    // We want a wide exception catcher to make sure our
                    // transaction always gets ended
                    e.printStackTrace();
                    throw new MalformedXMIException();
                }
            } finally {
                modelImpl.getModelEventPump().startPumpingEvents();
            }

            if (unknownElement) {
                modelImpl.deleteExtent(extent);
                throw new XmiException("Unknown element in XMI file : "
                        + unknownElementName);
            }

            if (ignoredElementCount > 0) {
                LOG.log(Level.WARNING, "Ignored one or more elements from list "
                        + ignoredElements);
            }

        } catch (MalformedXMIException e) {
            // If we can find a nested SAX exception, it will have information
            // on the line number, etc.
            ErrorManager.Annotation[] annotations =
                ErrorManager.getDefault().findAnnotations(e);
            for (ErrorManager.Annotation annotation : annotations) {
                Throwable throwable = annotation.getStackTrace();
                if (throwable instanceof SAXParseException) {
                    SAXParseException spe = (SAXParseException) throwable;
                    throw new XmiException(spe.getMessage(), spe.getPublicId(),
                            spe.getSystemId(), spe.getLineNumber(),
                            spe.getColumnNumber(), e);
                } else if (throwable instanceof SAXException) {
                    SAXException se = (SAXException) throwable;
                    Exception e1 = se.getException();
                    if (e1 instanceof org.argouml.model.XmiReferenceRuntimeException) {
                        String href =
                            ((org.argouml.model.XmiReferenceRuntimeException) e1)
                                .getReference();
                        throw new org.argouml.model.XmiReferenceException(href,
                                e);
                    }
                    throw new XmiException(se.getMessage(), se);
                }
            }
            modelImpl.deleteExtent(extent);
            throw new XmiException(e);
        } catch (IOException e) {
            try {
                modelImpl.deleteExtent(extent);
            } catch (InvalidObjectException e2) {
                // Ignore if the extent never got created or has been deleted
            }
            throw new XmiException(e);
        }

        return newElements;
    }


    private Collection<RefObject> convertAndLoadUml13(String systemId,
            RefPackage extent, XMIReader xmiReader, InputSource input)
        throws FileNotFoundException, UmlException, IOException,
            MalformedXMIException {

        LOG.log(Level.INFO, "XMI file doesn't appear to be UML 1.4 - "
                + "attempting UML 1.3->UML 1.4 conversion");
        final String[] transformFiles = new String[] {
            "NormalizeNSUML.xsl",
            "uml13touml14.xsl", };

        unknownElement = false;
        // InputSource xformedInput = chainedTransform(transformFiles, pIs);
        InputSource xformedInput = serialTransform(transformFiles,
                input);
        xformedInput.setPublicId(input.getPublicId());
        return xmiReader.read(xformedInput.getByteStream(), xformedInput
                .getSystemId(), extent);
    }

    /**
     *
     * @param input
     * @return InputSource : a new XML to test
     * @throws UmlException
     */
    private InputSource convertFromInvalidXMI(InputSource input)
        throws UmlException {

        LOG.log(Level.INFO, "XMI file doesn't appear to be a valid XMI");

        final String[] transformFiles = new String[] {
            "umbrello.xsl",
            };

        unknownElement = false;
        // InputSource xformedInput = chainedTransform(transformFiles, pIs);
        InputSource xformedInput = serialTransform(transformFiles,
                input);
        xformedInput.setPublicId(input.getPublicId());
        return xformedInput;
    }

    /**
     * Defines the URI prefix of the temporary XMI file that is being read.
     *
     * @return the URI prefix of the temporary XMI file that is being read.
     */
    static String getTempXMIFileURIPrefix() {
        if (tempXMIFileURIPrefix == null) {
            tempXMIFileURIPrefix =
                new File(System.getProperty("java.io.tmpdir")).toURI()
                + TEMP_XMI_FILE_PREFIX;
        }
        return tempXMIFileURIPrefix;
    }

    /*
     * @see org.argouml.model.XmiReader#getXMIUUIDToObjectMap()
     */
    public Map<String, Object> getXMIUUIDToObjectMap() {
        if (resolver != null) {
            // Give the resolver.getIdToObjectMap() entries
            // priority over entries with the same UUID from
            // resolver.getIdToObjectMaps() because entries
            // in resolver.getIdToObjectMaps() are historic.
            HashMap<String, Object> globalXmiIdToObjectMap =
                new HashMap<String, Object>(resolver.getIdToObjectMap());

            Map<String, Map<String, Object>> idToObjectMaps =
                resolver.getIdToObjectMaps();
            Set<Entry<String,Map<String,Object>>> entrySet = null;
            // I think that the synchronized access to idToObjectMaps is
            // required in order to respect the thread safe nature of the
            // object.
            // FIXME: maybe this should be moved into XmiReferenceResolverImpl,
            // because it depends on internal implementation details of it.
            synchronized (idToObjectMaps) {
                entrySet =
                    new HashSet<Entry<String,Map<String,Object>>>(
                            idToObjectMaps.entrySet());
                for (Entry<String, Map<String, Object>> entry : entrySet) {
                    entry.setValue(new HashMap<String,Object>(entry.getValue()));
                }
            }
            for (Entry<String, Map<String, Object>> entry : entrySet) {
                String xmiIdPrefix =
                    entry.getKey().startsWith(getTempXMIFileURIPrefix()) ? "" :
                        entry.getKey() + "#";
                for (Entry<String, Object> innerMapEntry :
                        entry.getValue().entrySet()) {

                    String id = xmiIdPrefix + innerMapEntry.getKey();
                    if (!globalXmiIdToObjectMap.containsKey(id)) {
                        globalXmiIdToObjectMap.put(
                                id,
                                innerMapEntry.getValue());
                    }
                }
            }
            return globalXmiIdToObjectMap;
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
            File tmpFile = File.createTempFile(TEMP_XMI_FILE_PREFIX, ".xmi");
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

                LOG.log(Level.INFO, "Transforming with {0}", xsltFileName);

                URL xsltUrl = getClass().getResource(xsltFileName);
                if (xsltUrl == null) {
                    throw new UmlException("Error opening XSLT style sheet : "
                            + xsltFileName);
                }
                StreamSource xsltStreamSource =
                    new StreamSource(xsltUrl.openStream());
                xsltStreamSource.setSystemId(xsltUrl.toExternalForm());

                // Create & set up temporary output file
                File tmpOutFile = File.createTempFile(TEMP_XMI_FILE_PREFIX, ".xmi");
                tmpOutFile.deleteOnExit();
                StreamResult result =
                    new StreamResult(new FileOutputStream(
                        tmpOutFile));

                // Create transformer and do transformation
                Transformer transformer = stf.newTransformer(xsltStreamSource);
                transformer.transform(myInput, result);

                LOG.log(Level.INFO, "Wrote converted XMI file - {0} converted using : {1}",
                        new Object[]{tmpOutFile, xsltFileName});

                // Set up for next iteration
                myInput =
                    new SAXSource(new InputSource(new FileInputStream(
                        tmpOutFile)));
                myInput.setSystemId(tmpOutFile.toURI().toURL().toExternalForm());
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
        File tmpOutFile = File.createTempFile(TEMP_XMI_FILE_PREFIX, ".xmi");
        tmpOutFile.deleteOnExit();
        FileOutputStream out = new FileOutputStream(tmpOutFile);

        // TODO: Bob says - Coding by use of side effect here.
        // Maybe this should be done in a clearer way but it fixes
        // http://argouml.tigris.org/issues/show_bug.cgi?id=4978
        // It seems that when loading an XMI that is not contained in a zip
        // file then the InputStream given as the argument to this method
        // can't be reused as it is at the end of the stream. In that case
        // systemId appears to be none-null at this stage.
        // So if systemId is not null we recreate the InputSource.
        String systemId = input.getSystemId();
        if (systemId != null) {
            input = new InputSource(new URL(systemId).openStream());
        }

        InputStream in = input.getByteStream();

        while ((len = in.read(buf)) >= 0) {
            out.write(buf, 0, len);
        }
        out.close();

        LOG.log(Level.FINE, "Wrote copied XMI file to {0}", tmpOutFile);
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
        LOG.log(Level.SEVERE, "Unknown XMI element named : " + name);

    }


    public boolean setIgnoredElements(String[] elementNames) {
        if (elementNames == null) {
            ignoredElements = new String[] {};
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
        modelImpl.addSearchPath(path);
    }

    public void removeSearchPath(String path) {
        modelImpl.removeSearchPath(path);
    }

    public List<String> getSearchPath() {
        return modelImpl.getSearchPath();
    }

    public void consumeHeader(InputStream stream) {
        try {
            int length = stream.available();
            byte[] bytes = new byte[length];
            stream.read(bytes, 0, length);
            // we presume the stream is encoded using the default char encoding
            xmiHeader = new String(bytes);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Exception reading XMI file header", e);
        }
    }


    public String getHeader() {
        return xmiHeader;
    }

}
