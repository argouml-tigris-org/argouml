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

package org.argouml.xml;

import org.xml.sax.AttributeList;
import org.xml.sax.helpers.AttributeListImpl;

/**
 * @author Jim Holt
 */

public class XMLElement {

    ////////////////////////////////////////////////////////////////
    // constructors

    public XMLElement(String name, AttributeList attributes) {
	_name = name;
	_attributes = new AttributeListImpl(attributes);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public String getName()            { return _name; }
    public void   setName(String name) { _name = name; }

    public void   addText(String text) { _text = _text.append(text); }
    public void   setText(String text) { _text = new StringBuffer(text); }
    public void   resetText()          { _text.setLength(0); }
    public String getText()            { return _text.toString(); }

    public void   setAttributes(AttributeList attributes) {
	_attributes = new AttributeListImpl(attributes);
    }
    public String getAttribute(String attribute) {
	return _attributes.getValue(attribute);
    }
    public String getAttributeName(int i) { return _attributes.getName(i); }
    public String getAttributeValue(int i) { return _attributes.getValue(i); }
    public int    getNumAttributes() { return _attributes.getLength(); }

    ////////////////////////////////////////////////////////////////
    // instance variables

    private String        _name       = null;
    private StringBuffer  _text       = new StringBuffer(100);
    private AttributeList _attributes = null;

} /* end class XMLElement */
