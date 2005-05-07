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

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler for unknown elements in PGML files or elements that are
 * completely specified by their attributes.
 * This handler skips the element's contents
 * and sub-elements.
 */
public class UnknownHandler extends DefaultHandler {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(UnknownHandler.class);

    private int depthCount;
    private HandlerStack stack;

    /**
     * @param theStack The stack of ContentHandler's for this parsing operation
     */
    public UnknownHandler(HandlerStack theStack) {
        depthCount = 1;
        stack = theStack;
    }

    /**
     * Increments depth count.
     *
     * @param uri
     * @param localname
     * @param qname
     * @param attributes
     */
    public void startElement(String uri, String localname, String qname,
        Attributes attributes) {
        LOG.info("Ignoring unexpected element: " + qname);
        depthCount++;
    }

    /**
     * Decrements depth count; pops itself off the stack when the depth count
     * is 0.
     *
     * @param uri
     * @param localname
     * @param qname
     */
    public void endElement(String uri, String localname, String qname) {
        if (--depthCount == 0) {
            stack.popHandlerStack();
        }
    }
}
