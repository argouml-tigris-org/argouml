// $Id$
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

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
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
    private static final Logger LOG = Logger.getLogger(XMIReader.class);
    
    private boolean errors = false;
    private org.xml.sax.Parser theParser = null;

    /**
     * Constructor for XMIReader.
     * @throws SAXException when there is a XML problem
     * @throws ParserConfigurationException if a parser cannot
     * be created which satisfies the requested configuration
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
     * @param pFactory the factory
     * @throws SAXException if there is a XML problem
     * @throws ParserConfigurationException if a parser cannot
     * be created which satisfies the requested configuration
     */
    public XMIReader(MFactory pFactory)
        throws SAXException, ParserConfigurationException {
        super(pFactory);
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
    protected void parseSourceStream(InputSource pIs) 
        throws SAXException, IOException {

        cleanup();

        try {
            getParser().parse(pIs);
            performLinking();
        } catch (IOException e) {            
            LOG.error("IOException while trying to read inputsource " 
                + pIs.getSystemId(), e);
            throw e;
        } catch (SAXException e) {
            LOG.error("Parsing error while trying to parse inputsource " 
                + pIs.getSystemId(), e);
            throw e;
        } catch (ClassCastException e) {
            LOG.error("Parsing error while trying to parse inputsource "
                + pIs.getSystemId(), e);
            throw new SAXException(e);
        }

    }

/*  Please do not delete this commented out code. Bob Tarling 3 Mar 2004.
//  This is useful for discovering load problems with corrupt XMI
//
//  NSUML isn't particularly good at reporting detail of errors
//  and unfortunately is a bit too protective of it's data to just extend.
//
//  To make use of this code - 
//   a) Refactor to rename this class to NsumlXMIReader
//   b) Refactor to move this class to ru.novosoft.uml.xmi
//   c) Uncomment the code.
//
//  By pretending to be part of the NSUML package this code can now get
//  access to the links attribute of the NSUML XMIReader and report on
//  (or ignore) any corrupt data.
//
//  This technique was used to fix 
//  http://argouml.tigris.org/issues/show_bug.cgi?id=2547 and 
//  http://argouml.tigris.org/issues/show_bug.cgi?id=2566 and will no
//  doubt prove useful again.
//
*/    
//    protected void performLinking() {
//        try {
//            Iterator i = links.iterator();
//            Object link = null;
//            while(i.hasNext()) {
//                link = i.next();
//                Class c = link.getClass();
//                Field[] fields = c.getDeclaredFields();
//                boolean methodType = fields[2].getBoolean(link);
//                Object sourceObject = fields[0].get(link);
//                if (sourceObject instanceof MBase) {
//                    if (methodType) {
//                        String parameterXMIID =  (String)fields[3].get(link);
//                        String parameterXMIUUID =  
//                          (String)fields[4].get(link);
//                        Object objectParameter =  getObject(parameterXMIID, 
//                                                          parameterXMIUUID);
//                        Object methodName =  fields[1].get(link);
//                        if (methodName.equals("type") 
//                              && sourceObject instanceof MAssociationEnd 
//                              && !(objectParameter instanceof MModelElement)) 
//                        {
//                            System.out.println("Link data from XMI " 
//                                  + sourceObject + " " + methodName + " " 
//                                  + parameterXMIID + " " + parameterXMIUUID 
//                                  + " " + objectParameter);
//                            //i.remove();
//                        }
//                    } else {
//                        String parameterXMIID =  (String)fields[3].get(link);
//                        String parameterXMIUUID = (String)fields[4].get(link);
//                        Object objectParameter =  
//                            getObject(parameterXMIID, parameterXMIUUID);
//                        if (!(objectParameter instanceof MModelElement)) {
//                            Object methodName =  fields[1].get(link);
//                            System.out.println("Invalid link data from XMI " 
//                                + sourceObject + " " + methodName + " " 
//                                + parameterXMIID + " " + parameterXMIUUID 
//                                + " " + objectParameter);
//                            //i.remove();
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Reflection failed");
//            e.printStackTrace();
//        }
//        super.performLinking();
//    }
    
    /**
     * Parses a given inputsource to a model. Does not override the novosoft 
     * parse method since that does not have the right signature.
     * @param pIs the input source for parsing
     * @return MModel the UML model
     * @throws SAXException if there is an XML problem
     * @throws IOException if there is a file I/O problem
     */
    public MModel parseToModel(InputSource pIs) 
        throws SAXException, IOException
    {        
	parseSourceStream(pIs);
	return getParsedModel();
        
    }

    /**
     * @param e true if there are errors
     */
    public void setErrors(boolean e) {
        errors = e;
    }

    /**
     * @return true if there were errors
     */
    public boolean getErrors() {
        return errors;
    }

    /**
     * @param parser the parser
     */
    public void setParser(org.xml.sax.Parser parser) {
        theParser = parser;
    }

    /**
     * @return the parser
     */
    public org.xml.sax.Parser getParser() {
        return theParser;
    }
}
