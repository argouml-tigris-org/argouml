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

package org.argouml.xml.argo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.model.uml.UmlHelper;
import org.argouml.xml.xmi.XMIReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The file persister for the UML model.
 * @author Bob Tarling
 */
public class ModelMemberFilePersister extends MemberFilePersister {
    
    /** logger */
    private static final Logger LOG =
        Logger.getLogger(ModelMemberFilePersister.class);
    
    private InputStream inputStream;
    
    private Project project;

    /**
     * Construct a new ModelMemberFilePersister.
     * @param url the url from which to load the model.
     * @param theProject the project to populate.
     * @throws SAXException on any parsing error.
     */
    public ModelMemberFilePersister(URL url, Project theProject)
        throws SAXException {
        
        this.project = theProject;
        try {
            inputStream = openStreamAtXmi(url);
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }
    
    /**
     * Opens the input stream of a URL and positions
     * it at the start of the XMI.
     * This is a first draft of this method.
     * Work still in progress (Bob Tarling).
     */
    private InputStream openStreamAtXmi(URL theUrl) throws IOException {
        XmlInputStream is = new XmlInputStream(theUrl.openStream(), "XMI");
        return is;
    }

    /**
     * Loads a model (XMI only) from an input source. BE ADVISED this
     * method has a side effect. It sets _UUIDREFS to the model.
     * 
     * If there is a problem with the xmi file, an error is set in the
     * getLastLoadStatus() field. This needs to be examined by the
     * calling function.
     *
     * @throws SAXException If the parser template is syntactically incorrect. 
     */
    public void load() throws SAXException {
                
        InputSource source = new InputSource(inputStream);
        Object mmodel = null;

        // 2002-07-18
        // Jaap Branderhorst
        // changed the loading of the projectfiles to solve hanging 
        // of argouml if a project is corrupted. Issue 913
        // Created xmireader with method getErrors to check if parsing went well
        XMIReader xmiReader = null;
        try {
            xmiReader = new XMIReader();
            source.setEncoding("UTF-8");
            mmodel = xmiReader.parseToModel(source);        
        } catch (SAXException e) { // duh, this must be caught and handled
            LOG.error("SAXException caught", e);
            throw e;
        } catch (ParserConfigurationException e) { 
            LOG.error("ParserConfigurationException caught", e);
            throw new SAXException(e);
        } catch (IOException e) {
            LOG.error("IOException caught", e);
            throw new SAXException(e);
        }

        if (xmiReader.getErrors()) {
            ArgoParser.getInstance().setLastLoadStatus(false);
            ArgoParser.getInstance().setLastLoadMessage(
                    "XMI file could not be parsed.");
            LOG.error("XMI file could not be parsed.");
            throw new SAXException(
                    "XMI file could not be parsed.");
        }

        // This should probably be inside xmiReader.parse
        // but there is another place in this source
        // where XMIReader is used, but it appears to be
        // the NSUML XMIReader.  When Argo XMIReader is used
        // consistently, it can be responsible for loading
        // the listener.  Until then, do it here.
        UmlHelper.getHelper().addListenersToModel(mmodel);

        project.addMember(mmodel);

        project.setUUIDRefs(new HashMap(xmiReader.getXMIUUIDToObjectMap()));
    }
    
    /**
     * A BufferInputStream that is aware of XML structure.
     * It can searches for the first occurence of a named tag
     * and reads only the data (inclusively) from that tag
     * to the matching end tag.
     * The tag is not expected to be an empty tag.
     * @author Bob Tarling
     */
    private class XmlInputStream extends BufferedInputStream {

        private boolean xmiStarted;
        private boolean inTag;
        private StringBuffer currentTag = new StringBuffer();
        private boolean endStream;
        private String tagName;
        private String endTagName;
        
        /**
         * Construct a new XmiInputStream
         * @param in the input stream to wrap.
         * @param tag the tag name from which to start reading
         */
        public XmlInputStream(InputStream in, String theTag) {
            super(in);
            this.tagName = theTag;
            this.endTagName = '/' + theTag;
        }
        
        /**
         * @see java.io.InputStream#read()
         */
        public synchronized int read() throws IOException {
            
            if (!xmiStarted) {
                skipToTag();
                xmiStarted = true;
            }
            if (endStream) {
                return -1;
            }
            int ch = super.read();
            endStream = isLastTag(ch);
            return ch;
        }
        
        /**
         * @see java.io.InputStream#read(byte[], int, int)
         */
        public synchronized int read(byte[] b, int off, int len)
            throws IOException {
            
            if (!xmiStarted) {
                skipToTag();
                xmiStarted = true;
            }
            if (endStream) {
                return -1;
            }
            int read = super.read(b, off, len);
            if (read == -1) {
                return -1;
            }
            for (int i = 0; i < read; ++i) {
                if (endStream) {
                    b[i] = -1;
                    read = i;
                    return read;
                }
                endStream = isLastTag(b[i + off]);
            }
            return read;
        }
        
        
        
        /**
         * @see java.io.InputStream#read(byte[])
         */
        public int read(byte[] b) throws IOException {
            
            if (!xmiStarted) {
                skipToTag();
                xmiStarted = true;
            }
            if (endStream) {
                b[0] = -1;
                return -1;
            }
            int read = super.read(b);
            if (read == -1) {
                b[0] = -1;
                return -1;
            }
            for (int i = 0; i < read; ++i) {
                if (endStream) {
                    read = i;
                    b[i] = -1;
                    if (i == 0) {
                        return -1;
                    } else {
                        return i;
                    }
                }
                endStream = isLastTag(b[i]);
            }
            return read;
        }
        
        /**
         * Determines if the character is the last character of the last tag of
         * interest.
         * Every character read after the first tag of interest should be passed
         * through this method in order.
         * 
         * @param ch the character to test.
         * @return true if this is the end of the last tag.
         */
        private boolean isLastTag(int ch) {
            if (ch == '<') {
                inTag = true;
                currentTag.setLength(0);
            } else if (ch == '>') {
                inTag = false;
                if (currentTag.toString().equals(endTagName)) {
                    return true;
                }
            } else if (inTag) {
                currentTag.append((char) ch);
            }
            return false;
        }
        
        /**
         * Keep on reading an input stream until a specific
         * sequence of characters has ben read.
         * This method assumes there is at least one match.
         * @param search the characters to search for.
         * @throws IOException
         */
        private void skipToTag() throws IOException {
            char[] searchChars = tagName.toCharArray();
            int i;
            boolean found;
            while (true) {
                mark(tagName.length() + 3);
                // Keep reading till we get the left bracket of an opening tag
                while (realRead() != '<') {
                    mark(tagName.length() + 3);
                }
                found = true;
                // Compare each following character to see
                // that it matches the tag we want
                for (i = 0; i < tagName.length(); ++i) {
                    if (realRead() != searchChars[i]) {
                        found = false;
                        break;
                    }
                }
                int terminator = realRead();
                // We also want to match with the right bracket of the tag or
                // some other terminator
                if (found && isNameTerminator((char) terminator)) {
                    reset();
                    return;
                }
            }
        }

        private boolean isNameTerminator(char ch) {
            return (ch == '>' || Character.isWhitespace(ch));
        }
                        
        private int realRead() throws IOException {
            int read = super.read();
            if (read == -1) {
                throw new IOException("Tag " + tagName + " not found");
            }
            return read;
        }
    }
}