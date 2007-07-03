// $Id: XmlInputStream.java 12790 2007-06-08 09:20:37Z tfmorris $
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;


/**
 * A BufferInputStream that is aware of XML structure.
 * It can searches for the first occurence of a named tag
 * and reads only the data (inclusively) from that tag
 * to the matching end tag or it can search for the first
 * occurence of a named tag and read on the child tags.
 * The tag is not expected to be an empty tag.
 * 
 * TODO: This is hardwired to assume a fixed single byte
 * character encoding.  It probably needs to be updated to
 * handle multi-byte encodings. - tfm 20070607
 * 
 * @author Bob Tarling
 */
class XmlInputStream extends BufferedInputStream {

    private boolean xmlStarted;
    private boolean inTag;
    private StringBuffer currentTag = new StringBuffer();
    private boolean endStream;
    private String tagName;
    private String endTagName;
    private Map attributes;
    private boolean childOnly;
    private int instanceCount;
    //private EventListenerList listenerList = new EventListenerList();

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(XmlInputStream.class);

    /**
     * Construct a new XmlInputStream.
     *
     * @param inStream the input stream to wrap.
     * @param theTag the tag name from which to start reading
     * @param theLength the expected length of the input stream
     * @param theEventSpacing the number of characers to read before
     *        firing a progress event.
     */
    public XmlInputStream(
            InputStream inStream,
            String theTag,
            long theLength,
            long theEventSpacing) {
        super(inStream);
        tagName = theTag;
        endTagName = '/' + theTag;
        attributes = null;
        childOnly = false;
    }

    /**
     * Reopen a stream that has already reached the end
     * of an XML fragment.
     *
     * @param theTag the tag name
     * @param attribs the attributes
     * @param child child only
     */
    public synchronized void reopen(
                String theTag,
                Map attribs,
                boolean child) {
        endStream = false;
        xmlStarted = false;
        inTag = false;
        tagName = theTag;
        endTagName = '/' + theTag;
        attributes = attribs;
        childOnly = child;
    }

    /**
     * Reopen a stream that has already reached the end
     * of an XML fragment.
     *
     * @param theTag the tag name
     */
    public synchronized void reopen(String theTag) {
        endStream = false;
        xmlStarted = false;
        inTag = false;
        tagName = theTag;
        endTagName = '/' + theTag;
        attributes = null;
        childOnly = false;
    }

    /*
     * @see java.io.InputStream#read()
     */
    public synchronized int read() throws IOException {

        if (!xmlStarted) {
            skipToTag();
            xmlStarted = true;
        }
        if (endStream) {
            return -1;
        }
        int ch = super.read();
        endStream = isLastTag(ch);
        return ch;
    }

    /*
     * @see java.io.InputStream#read(byte[], int, int)
     */
    public synchronized int read(byte[] b, int off, int len)
        throws IOException {

        if (!xmlStarted) {
            skipToTag();
            xmlStarted = true;
        }
        if (endStream) {
            return -1;
        }

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
            String tag = currentTag.toString();
            if (tag.equals(endTagName)
                    // TODO: The below is not strictly correct, but should
                    // cover the case we deal with.  Using a real XML parser
                    // would be better.
                    // Look for XML document has just a single root element
                    || (currentTag.charAt(currentTag.length() - 1) == '/' 
                        && tag.startsWith(tagName)
                        && tag.indexOf(' ') == tagName.indexOf(' '))) {
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
     *
     * @throws IOException
     */
    private void skipToTag() throws IOException {
        char[] searchChars = tagName.toCharArray();
        int i;
        boolean found;
        while (true) {
            if (!childOnly) {
		mark(1000);
	    }
            // Keep reading till we get the left bracket of an opening tag
            while (realRead() != '<') {
                if (!childOnly) {
		    mark(1000);
		}
            }
            found = true;
            // Compare each following character to see
            // that it matches the tag we want
            for (i = 0; i < tagName.length(); ++i) {
                int c = realRead();
                if (c != searchChars[i]) {
                    found = false;
                    break;
                }
            }
            int terminator = realRead();
            // We also want to match with the right bracket of the tag or
            // some other terminator
            if (found && !isNameTerminator((char) terminator)) {
                found = false;
            }

            if (found) {
                // We've found the matching tag but do we have
                // the correct instance with matching attributes?
                if (attributes != null) {
                    Map attributesFound = new HashMap();
                    if (terminator != '>') {
                        attributesFound = readAttributes();
                    }
                    // Search all attributes found to those expected.
                    // If any don't match then turn off the found flag
                    // so that we search for the next matching tag.
                    Iterator it = attributes.entrySet().iterator();
                    while (found && it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        if (!pair.getValue().equals(
                                attributesFound.get(pair.getKey()))) {
                            found = false;
                        }
                    }
                }
            }

            if (found) {
                if (instanceCount < 0) {
                    found = false;
                    ++instanceCount;
                }
            }

            if (found) {
                if (childOnly) {
                    // Read the name of the child tag
                    // and then reset read position
                    // back to that child tag.
                    mark(1000);
                    while (realRead() != '<') {
                        /* do nothing */
		    }
                    tagName = "";
                    char ch = (char) realRead();
                    while (!isNameTerminator(ch)) {
                        tagName += ch;
                        ch = (char) realRead();
                    }
                    endTagName = "/" + tagName;
                    LOG.info("Start tag = " + tagName);
                    LOG.info("End tag = " + endTagName);
                }
                reset();
                return;
            }
        }
    }

    private boolean isNameTerminator(char ch) {
        return (ch == '>' || Character.isWhitespace(ch));
    }

    /**
     * Having read the inputstream up until the tag name.
     * This method continues to read the contents of the tag to
     * retrieve any attribute names and values.
     * @return a map of name value pairs.
     * @throws IOException
     */
    private Map readAttributes() throws IOException {
        Map attributesFound = new HashMap();
        int character;
        while ((character = realRead()) != '>') {
            if (!Character.isWhitespace((char) character)) {
                StringBuffer attributeName = new StringBuffer();
                attributeName.append((char) character);
                while ((character = realRead()) != '='
                        && !Character.isWhitespace((char) character)) {
                    attributeName.append((char) character);
                }
                // Skip any whitespace till we should be on an equals sign.
                while (Character.isWhitespace((char) character)) {
                    character = realRead();
                }
                if (character != '=') {
                    throw new IOException(
                            "Expected = sign after attribute "
                            + attributeName);
                }
                // Skip any whitespace till we should be on a quote symbol.
                int quoteSymbol = realRead();
                while (Character.isWhitespace((char) quoteSymbol)) {
                    quoteSymbol = realRead();
                }
                if (quoteSymbol != '"' && quoteSymbol != '\'') {
                    throw new IOException(
                            "Expected \" or ' around attribute value after "
                            + "attribute " + attributeName);
                }
                StringBuffer attributeValue = new StringBuffer();
                while ((character = realRead()) != quoteSymbol) {
                    attributeValue.append((char) character);
                }
                attributesFound.put(
                        attributeName.toString(),
                        attributeValue.toString());
            }
        }
        return attributesFound;
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

    private int realRead() throws IOException {
        int read = super.read();
        if (read == -1) {
            throw new IOException("Tag " + tagName + " not found");
        }
        return read;
    }

}
