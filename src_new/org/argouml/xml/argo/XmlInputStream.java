/*
 * Created on 13-Oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.argouml.xml.argo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * A BufferInputStream that is aware of XML structure.
 * It can searches for the first occurence of a named tag
 * and reads only the data (inclusively) from that tag
 * to the matching end tag.
 * The tag is not expected to be an empty tag.
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
    
    /**
     * Construct a new XmiInputStream
     * @param in the input stream to wrap.
     * @param tag the tag name from which to start reading
     */
    public XmlInputStream(InputStream in, String theTag) {
        this(in,theTag,null);
    }
    
    /**
     * Construct a new XmiInputStream
     * @param in the input stream to wrap.
     * @param tag the tag name from which to start reading
     * @param instanceNo the instance of the tag required (starting at zero)
     */
    public XmlInputStream(InputStream in, String theTag, Map attributes) {
        super(in);
        this.tagName = theTag;
        this.endTagName = '/' + theTag;
        this.attributes = attributes;
    }
    
    /**
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
    
    /**
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
        
        if (!xmlStarted) {
            skipToTag();
            xmlStarted = true;
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
                // We've found the matching tag but do we have
                // the correct instance?
                if (attributes == null) {
                    reset();
                    return;
                } else {
                    Map attributesFound = new HashMap();
                    if (terminator != '>') {
                        attributesFound = readAttributes();
                    }
                    Iterator it = attributes.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        if (!pair.getValue().equals(attributesFound.get(pair.getKey()))) {
                            continue;
                        }
                    }
                    reset();
                    return;
                }
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
        HashMap attributes = new HashMap();
        int character;
        while ((character = realRead()) != '>') {
            if (!Character.isWhitespace((char)character)) {
                StringBuffer attributeName = new StringBuffer(character);
                while ((character = realRead()) != '=' && !Character.isWhitespace((char)character)) {
                    attributeName.append(character);
                }
                // Skip any whitespace till we should be on an equals sign.
                while (Character.isWhitespace((char)character)) {
                    character = realRead();
                }
                if (character != '=') {
                    throw new IOException("Expected = sign after attribute " + attributeName);
                }
                // Skip any whitespace till we should be on a quote symbol.
                int quoteSymbol = realRead();
                while (Character.isWhitespace((char)quoteSymbol)) {
                    quoteSymbol = realRead();
                }
                if (quoteSymbol != '"' && quoteSymbol != '\'') {
                    throw new IOException("Expected \" or ' around attribute value after attribute " + attributeName);
                }
                StringBuffer attributeValue = new StringBuffer(character);
                while ((character = realRead()) != quoteSymbol) {
                    attributeValue.append(character);
                }
                attributes.put(attributeName.toString(), attributeValue.toString());
            }
        }
        return attributes;
    }
                    
    private int realRead() throws IOException {
        int read = super.read();
        if (read == -1) {
            throw new IOException("Tag " + tagName + " not found");
        }
        return read;
    }
}