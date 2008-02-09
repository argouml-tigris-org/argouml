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

package org.argouml.persistence;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import org.argouml.persistence.AbstractFilePersister.ProgressMgr;

/**
 * A BufferInputStream that is aware of XML structure.
 * It can search for the first occurence of a named tag
 * and reads only the data (inclusively) from that tag
 * to the matching end tag or it can search for the first
 * occurence of a named tag and read on the child tags.
 * The tag is not expected to be an empty tag.
 * @author Bob Tarling
 */
class XmiInputStream extends BufferedInputStream {
    
    private String tagName;
    private String endTagName;
    private String attributes;
    private boolean extensionFound;
    private boolean endFound;
    private boolean parsingExtension;
    private boolean readingName;
    
    private XmiExtensionParser xmiExtensionParser;
    
    private StringBuffer stringBuffer;
    private String type;
    
    /**
     * The number of bytes to be read between each progress
     * event.
     */
    private long eventSpacing;

    /**
     * The number of characters read so far.
     */
    private long readCount;

    /**
     * The expected stream length.
     */
    private ProgressMgr progressMgr;

    /**
     * Construct a new XmlInputStream.
     *
     * @param inputStream the input stream to wrap.
     * @param extParser the parser to call to read any
     *                           XMI.extension elements
     * @param len the expected length of the input stream
     * @param spacing the number of characers to read before
     *        firing a progress event.
     * @param prgrssMgr the progress manager
     */
    public XmiInputStream(
            InputStream inputStream,
            XmiExtensionParser extParser,
            long spacing,
            ProgressMgr prgrssMgr) {
        super(inputStream);
        eventSpacing = spacing;
        xmiExtensionParser  = extParser;
        progressMgr  = prgrssMgr;
    }
    
    /**
     * Construct a new XmlInputStream.
     *
     * @param inputStream the input stream to wrap.
     * @param extParser the parser to call to read any
     *                           XMI.extension elements
     * @param len the expected length of the input stream
     * @param spacing the number of characers to read before
     *        firing a progress event.
     * @param prgrssMgr the progress manager
     * @deprecated by Linus Tolke for 0.25.3.
     *        Use the constructor 
     *        {@link #XmiInputStream(InputStream, XmiExtensionParser,
     *        long, ProgressMgr)} that doesn't specify length
     */
    public XmiInputStream(
            InputStream inputStream,
            XmiExtensionParser extParser,
            long len,
            long spacing,
            ProgressMgr prgrssMgr) {
        super(inputStream);
        eventSpacing = spacing;
        xmiExtensionParser  = extParser;
        progressMgr  = prgrssMgr;
    }

    /*
     * @see java.io.InputStream#read()
     */
    public synchronized int read() throws IOException {

        if (endFound) {
            extensionFound = false;
            parsingExtension = false;
            endFound = false;
            readingName = false;
            tagName = null;
            endTagName = null;
        }

        int ch = super.read();
        
        if (parsingExtension) {
            stringBuffer.append((char) ch);
        }
        // else {
        // TODO: Only progress when reading standard XMI
        // extension parsers will continue progression.
        ++readCount;
        if (progressMgr != null && readCount == eventSpacing) {
            try {
                readCount = 0;
                progressMgr.nextPhase();
            } catch (InterruptedException e) {
                throw new InterruptedIOException(e);
            }
        }
//        }
        
        if (xmiExtensionParser != null) {
            if (readingName) {
                if (isNameTerminator((char) ch)) {
                    readingName = false;
                    if (parsingExtension && endTagName == null) {
                        endTagName = "/" + tagName;
                    } else if (tagName.equals("XMI.extension")) {
                        extensionFound = true;
                    } else if (tagName.equals(endTagName)) {
                        endFound = true;
                        xmiExtensionParser.parse(type, stringBuffer.toString());
                        stringBuffer.delete(0, stringBuffer.length());
                    }
                } else {
                    tagName += (char) ch;
                }
            }

            if (extensionFound) {
                if (ch == '>') {
                    extensionFound = false;
                    callExtensionParser();
                } else {
                    attributes += (char) ch;
                }
            }
            
            if (ch == '<') {
                readingName = true;
                tagName = "";
            }
        }
        return ch;
    }
    
    private void callExtensionParser() {
        String label = null;
        String extender = null;
        for (StringTokenizer st = new StringTokenizer(attributes, " =");
                st.hasMoreTokens(); ) {
            String attributeType = st.nextToken();
            if (attributeType.equals("xmi.extender")) {
                extender = st.nextToken();
                extender = extender.substring(1, extender.length() - 1);
            }
            if (attributeType.equals("xmi.label")) {
                label = st.nextToken();
                label = label.substring(1, label.length() - 1);
            }
        }
        if ("ArgoUML".equals(extender)) {
            type = label;
            stringBuffer = new StringBuffer();
            parsingExtension = true;
            endTagName = null;
        }
    }
    
    /*
     * @see java.io.InputStream#read(byte[], int, int)
     */
    public synchronized int read(byte[] b, int off, int len)
        throws IOException {

        int cnt;
        for (cnt = 0; cnt < len; ++cnt) {
            int read = read();
            if (read == -1) {
                break;
            }
            b[cnt + off] = (byte) read;
        }

        if (cnt > 0) {
            return cnt;
        }
        return -1;
    }


    private boolean isNameTerminator(char ch) {
        return (ch == '>' || Character.isWhitespace(ch));
    }

    /**
     * The close method is overridden to prevent some class out of
     * our control from closing the stream (such as a SAX parser).
     * Use realClose() to finally close the stream for real.
     * @throws IOException to satisfy ancestor but will never happen.
     */
    public void close() throws IOException {
    }

    /**
     * Really close the input.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void realClose() throws IOException {
        super.close();
    }
}

class InterruptedIOException extends IOException {
    
    private static final long serialVersionUID = 5654808047803205851L;
    
    private InterruptedException cause;
    
    public InterruptedIOException(InterruptedException theCause) {
        cause = theCause;
    }
    
    public InterruptedException getInterruptedException() {
        return cause;
    }
}
