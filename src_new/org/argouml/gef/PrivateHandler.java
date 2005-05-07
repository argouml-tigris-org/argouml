// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.gef;

import org.xml.sax.SAXException;

/**
 * Content handler for <em>private</em> sub-elements
 * of Fig PGML-file elements.
 * The character contents of these elements specify additional
 * information about the properties of the Fig object.
 */
public class PrivateHandler extends BaseHandler {
    /**
     * The container.
     */
    private Container container;

    /**
     * @param parser The parser object containing the diagram that contains
     * the fig specified by the element containing this element
     * @param theContainer The object that will receive the text contents of
     * this element
     */
    public PrivateHandler(PGMLStackParser parser, Container theContainer) {
        super(parser);
        container = theContainer;
    }

    /**
     * Send the text content of the <em>private</em> element to the containing
     * element.
     *
     * @param contents Text content of the element
     * @throws SAXException if something goes wrong.
     */
    public void gotElement(String contents)
    	throws SAXException {
        container.addObject(contents);
    }
}
