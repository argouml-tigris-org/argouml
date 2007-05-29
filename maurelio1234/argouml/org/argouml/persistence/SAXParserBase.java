// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Jim Holt
 */

abstract class SAXParserBase extends DefaultHandler {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(SAXParserBase.class);

    /**
     * The constructor.
     */
    public SAXParserBase() {
        // empty constructor
    }

    ////////////////////////////////////////////////////////////////
    // static variables

    /**
     * Switching this to true gives some extra logging messages.
     */
    protected static final boolean DBG = false;

    /**
     * This acts as a stack of elements.<p>
     *
     * {@link #startElement(String, String, String, Attributes)} places
     * an item on the stack end {@link #endElement(String, String, String)}
     * removes it.
     */
    private   static  XMLElement[]  elements      = new XMLElement[100];

    /**
     * The number of items actually in use on the elements stack.
     */
    private   static  int           nElements     = 0;

    /**
     * This acts as a stack of elements.<p>
     *
     * {@link #startElement(String, String, String, Attributes)} places
     * an item on the stack end {@link #endElement(String, String, String)}
     * removes it.
     */
    private   static  XMLElement[]  freeElements  = new XMLElement[100];
    private   static  int           nFreeElements = 0;

    private   static  boolean       stats         = true;
    private   static  long          parseTime     = 0;

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @param s true if statistics have to be shown
     */
    public void    setStats(boolean s) { stats = s; }

    /**
     * @return  true if statistics have to be shown
     */
    public boolean getStats()              { return stats; }

    /**
     * @return the parsing time
     */
    public long    getParseTime()          { return parseTime; }

    ////////////////////////////////////////////////////////////////
    // main parsing method

    /**
     * @param is the inputstream of the project to read
     * @throws SAXException when parsing xml
     */
    public void parse(Reader is) throws SAXException {

        long start, end;

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setValidating(false);

        try {
            SAXParser parser = factory.newSAXParser();
            InputSource input = new InputSource(is);
            input.setSystemId(getJarResource("org.argouml.kernel.Project"));

            start = System.currentTimeMillis();
            parser.parse(input, this);
            end = System.currentTimeMillis();
            parseTime = end - start;
        } catch (IOException e) {
            throw new SAXException(e);
        } catch (ParserConfigurationException e) {
            throw new SAXException(e);
        }
        if (stats && LOG.isInfoEnabled()) {
            LOG.info("Elapsed time: " + (end - start) + " ms");
        }
    }

    ////////////////////////////////////////////////////////////////
    // abstract methods

    /**
     * Implement in the concrete class to handle reaching the start tag of
     * an element of interest.
     * @param e the element.
     * @throws SAXException on any error parsing the element.
     */
    protected abstract void handleStartElement(XMLElement e)
        throws SAXException;
    /**
     * Implement in the concrete class to handle reaching the end tag of
     * an element of interest.
     * @param e the element.
     * @throws SAXException on any error parsing the element.
     */
    protected abstract void handleEndElement(XMLElement e)
        throws SAXException;

    ////////////////////////////////////////////////////////////////
    // non-abstract methods

    /*
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     *         java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri,
            String localname,
            String name,
            Attributes atts) throws SAXException {
        if (isElementOfInterest(name)) {

            XMLElement element = createXmlElement(name, atts);

            if (LOG.isDebugEnabled()) {
                StringBuffer buf = new StringBuffer();
                buf.append("START: ").append(name).append(' ').append(element);
                for (int i = 0; i < atts.getLength(); i++) {
            	    buf.append("   ATT: ")
                        .append(atts.getLocalName(i))
                            .append(' ')
                                .append(atts.getValue(i));
                }
                LOG.debug(buf.toString());
            }

            elements[nElements++] = element;
            handleStartElement(element);
        }
    }

    /**
     * Factory method to return an XMLElement.
     * This will reuse previously created elements when possible.
     * @param name The element name.
     * @param atts The element attributes.
     * @return the element.
     */
    private XMLElement createXmlElement(String name, Attributes atts) {
        if (nFreeElements == 0) {
            return new XMLElement(name, atts);
        }
        XMLElement e = freeElements[--nFreeElements];
        e.setName(name);
        e.setAttributes(atts);
        e.resetText();
        return e;
    }

    /*
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
     *         java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String localname, String name)
        throws SAXException {
        if (isElementOfInterest(name)) {
            XMLElement e = elements[--nElements];
            if (LOG.isDebugEnabled()) {
                StringBuffer buf = new StringBuffer();
                buf.append("END: " + e.getName() + " ["
            	       + e.getText() + "] " + e + "\n");
                for (int i = 0; i < e.getNumAttributes(); i++) {
                    buf.append("   ATT: " + e.getAttributeName(i) + " "
                    	   + e.getAttributeValue(i) + "\n");
                }
                LOG.debug(buf);
            }
            handleEndElement(e);
        }
    }

    /**
     * Determine if an element of the given name is of interest to
     * the parser. The base implementation assumes always true.
     *
     * @param name the element name.
     * @return true if the element name is of interest.
     */
    protected boolean isElementOfInterest(String name) {
        return true;
    }

    // TODO: remove when code below in characters() is removed
//    private static final String    RETURNSTRING  = "\n      ";
    
    /*
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] ch, int start, int length)
        throws SAXException {
        
        elements[nElements - 1].addText(ch, start, length);
        
        // TODO: Remove this old implementation after 0.22 if it's
        // demonstrated that it's not needed. - tfm
        
        // Why does the text get added to ALL the elements on the stack? - tfm
//        for (int i = 0; i < nElements; i++) {
//            XMLElement e = elements[i];
//            if (e.length() > 0) {
//                // This seems wrong since this method can be called
//                // multiple times at the parser's discretion - tfm
//                e.addText(RETURNSTRING);
//            }
//            e.addText(ch, start, length);
//        }
    }


    /*
     * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String,
     *         java.lang.String)
     */
    public InputSource resolveEntity (String publicId, String systemId)
        throws SAXException {
        try {
	    URL testIt = new URL(systemId);
            InputSource s = new InputSource(testIt.openStream());
            return s;
        } catch (Exception e) {
            LOG.info("NOTE: Could not open DTD " + systemId
                    + " due to exception");

            String dtdName = systemId.substring(systemId.lastIndexOf('/') + 1);
            String dtdPath = "/org/argouml/persistence/" + dtdName;
            InputStream is = SAXParserBase.class.getResourceAsStream(dtdPath);
            if (is == null) {
                try {
                    is = new FileInputStream(dtdPath.substring(1));
                } catch (Exception ex) {
                    throw new SAXException(e);
                }
            }
            return new InputSource(is);
        }
    }

    /**
     * @param cls the class
     * @return the jar
     */
    public String getJarResource(String cls) {
  	//e.g:org.argouml.uml.generator.ui.ClassGenerationDialog -> poseidon.jar
        String jarFile = "";
        String fileSep = System.getProperty("file.separator");
        String classFile = cls.replace('.', fileSep.charAt(0)) + ".class";
        ClassLoader thisClassLoader = this.getClass().getClassLoader();
        URL url = thisClassLoader.getResource(classFile);
        if (url != null) {
            String urlString = url.getFile();
            int idBegin = urlString.indexOf("file:");
            int idEnd = urlString.indexOf("!");
            if (idBegin > -1 && idEnd > -1 && idEnd > idBegin) {
                jarFile = urlString.substring(idBegin + 5, idEnd);
            }
        }

        return jarFile;
    }

    ////////////////////////////////////////////////////////////////
    // convenience methods

    /**
     * @param e the element
     */
    public void ignoreElement(XMLElement e) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("NOTE: ignoring tag:" + e.getName());
        }
    }

    /**
     * @param e the element
     */
    public void notImplemented(XMLElement e) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("NOTE: element not implemented: " + e.getName());
        }
    }
} /* end class SAXParserBase */
