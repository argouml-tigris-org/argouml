// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: XMIReader.java
// Classes: XMIReader
// Original Author: jaap.branderhorst@xs4all.nl

package org.argouml.xml.xmi;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.argouml.application.api.Argo;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.model_management.MModel;

/**
 * Parses an XMI file. Extended from the NSUML XMIReader since this
 * reader does not handle errorhandling very well and is not very well
 * programmed at all. This led to issues loading xmi files and hanging ArgoUML
 * by doing that. 
 * 
 * @author Jaap Branderhorst
 * @see ru.novosoft.uml.xmi.XMIReader
 */
public class XMIReader extends ru.novosoft.uml.xmi.XMIReader {
    private boolean errors = false;
    private org.xml.sax.Parser parser = null;

    /**
     * Constructor for XMIReader.
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public XMIReader() throws SAXException, ParserConfigurationException {
        super();
        SAXParserFactory saxpf = SAXParserFactory.newInstance();
        saxpf.setValidating(false);
        saxpf.setNamespaceAware(false);

        setParser(saxpf.newSAXParser().getParser());

        getParser().setErrorHandler(this);
        getParser().setDocumentHandler(this);
        getParser().setEntityResolver(this);

    }

    /**
     * Constructor for XMIReader.
     * @param p_factory
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public XMIReader(MFactory p_factory)
        throws SAXException, ParserConfigurationException {
        super(p_factory);
        SAXParserFactory saxpf = SAXParserFactory.newInstance();
        saxpf.setValidating(false);
        saxpf.setNamespaceAware(false);

        setParser(saxpf.newSAXParser().getParser());

        getParser().setErrorHandler(this);
        getParser().setDocumentHandler(this);
        getParser().setEntityResolver(this);

    }

    /**
     * Parses an xmi inputsource. Sets errors to true if an exception is 
     * thrown. Could not change the API from the superclass. Therefore this
     * strange construction.
     * @see ru.novosoft.uml.xmi.XMIReader#parseStream(InputSource)
     */
    protected void parseStream(InputSource p_is) {

        cleanup();

        try {
            getParser().parse(p_is);
            performLinking();
        }
        catch (Exception ex) {
            Argo.logger.error("The model file loaded is corrupted.");
            Argo.logger.error(ex);
            setErrors(true);
        }

    }
    
    public MModel parse(InputSource p_is) 
    {
        parseStream(p_is);
        return getParsedModel();
    }

    public void setErrors(boolean errors) {
        this.errors = errors;
    }

    public boolean getErrors() {
        return errors;
    }

    public void setParser(org.xml.sax.Parser parser) {
        this.parser = parser;
    }

    public org.xml.sax.Parser getParser() {
        return parser;
    }
}