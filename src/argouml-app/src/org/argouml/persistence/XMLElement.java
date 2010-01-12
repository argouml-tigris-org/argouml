/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author Jim Holt
 */
class XMLElement {

    ////////////////////////////////////////////////////////////////
    // instance variables

    private String        name       = null;
    private StringBuffer  text       = new StringBuffer(100);
    private Attributes    attributes = null;

    /**
     * Constructor.
     *
     * @param n The name of the element.
     * @param a The attributes.
     */
    public XMLElement(String n, Attributes a) {
	name = n;
	attributes = new AttributesImpl(a);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @return the name of this element
     */
    public String getName()            { return name; }
    /**
     * @param n the name of this element
     */
    public void   setName(String n) { name = n; }

    /**
     * @param t the text to be appended
     */
    public void   addText(String t) { text = text.append(t); }

    /**
     * Append text to the end of the element.
     * @param c character array containing the text to be appended
     * @param offset starting offset of text
     * @param len length of text to append
     */
    public void addText(char[] c, int offset, int len) {
        text = text.append(c, offset, len);
    }
    
    /**
     * @param t the new text
     */
    public void   setText(String t) { text = new StringBuffer(t); }

    /**
     * Erase the text of this element.
     */
    public void   resetText()          { text.setLength(0); }

    /**
     * @return the text of this element
     */
    public String getText()            { return text.toString(); }

    /**
     * Get the length of the text in the element.
     * @return the length of the text in this element
     */
    public int length()            { return text.length(); }

    /**
     * Change the attributes for this element.
     *
     * @param a The new list of attributes.
     */
    public void   setAttributes(Attributes a) {
	attributes = new AttributesImpl(a);
    }

    /**
     * @param attribute the attribute name
     * @return the attribute value
     */
    public String getAttribute(String attribute) {
	return attributes.getValue(attribute);
    }

    /**
     * @param i the index for the list of attributes
     * @return the attribute name for the attribute at the given index
     */
    public String getAttributeName(int i) {
        return attributes.getLocalName(i);
    }

    /**
     * @param i the index for the list of attributes
     * @return the attribute value for the attribute at the given index
     */
    public String getAttributeValue(int i) {
        return attributes.getValue(i);
    }

    /**
     * @return the number of attributes
     */
    public int    getNumAttributes() { return attributes.getLength(); }

} /* end class XMLElement */
