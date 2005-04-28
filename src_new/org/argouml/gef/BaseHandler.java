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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

/**
 * Implements a SAX ContentHandler that contains a reference to an
 * object derived from PGMLStackParser and uses that object to get
 * handlers for sub-elements. Classes that know how to parse elements
 * appearing in a PGML file are derived from this class.
 * @see PGMLStackParser
 * @author Michael A. MacDonald
 */
abstract public class BaseHandler extends DefaultHandler {
    /**
     * Accumulates all the characters that the SAX parser has seen for
     * this element and provided in the {@link #characters} call.
     */
    protected StringBuffer _elementContents;
    private PGMLStackParser _parser;

    //////////////////////////////////////////////////////////////////////////
    // Constructors
    public BaseHandler( PGMLStackParser parser)
    {
        _elementContents=new StringBuffer();
        _parser=parser;
    }

    //////////////////////////////////////////////////////////////////////////
    // Public interface
    public PGMLStackParser getPGMLStackParser()
    {
        return _parser;
    }


    /**
     * Override this to do processing when we have all the information
     * about the element.
     * @param contents The text contents of the element.
     * @throws SAXException on error
     */
    public void gotElement( String contents) throws SAXException {
        
    }

    //////////////////////////////////////////////////////////////////////////
    // Internal but overridable
    /**
     * Called to obtain a handler for a sub-element.  The
     * default implementation is to call getElementHandler.  If getElementHandler
     * can find a handler, it returns that; otherwise, it returns an
     * UnknownHandler which will skip over the element (and any contained
     * sub-elements) without doing anything.  The only reason to
     * override this would be to customize the behavior for an uknown
     * element.
     * @see UnknownHandler
     * @see #getElementHandler
     * @return DefaultHandler implementation appropriate for the element.
     */
    protected DefaultHandler getElementOrUnknownHandler( HandlerStack stack,
        Object container, String uri, String localname, String qname,
        Attributes attributes)
    throws SAXException
    {
        DefaultHandler result=getElementHandler( stack, container, uri,
                                                 localname, qname,
                                                 attributes);
        if ( result==null)
            result=new UnknownHandler( getPGMLStackParser());
        return result;
    }

    /**
     * Called to return a SAX handler appropriate for an element.  The
     * default implementation calls the getHandler method of the
     * BaseHandler's PGMLStackParser object.  If no appropriate handler
     * is found for the element, returns null.
     * @return DefaultHandler implementation appropriate for the element.
     */
    protected DefaultHandler getElementHandler( HandlerStack stack,
               Object container, String uri, String localname, String qname,
                                                Attributes attributes)
                                            throws SAXException{
        return getPGMLStackParser().getHandler( stack,
                                               container,
                                               uri,
                                               localname,
                                               qname,
                                               attributes);
    }

    //////////////////////////////////////////////////////////////////////////
    // ContentHandler implementation
    public void startElement( String uri, String localname, String qname,
        Attributes attributes) throws SAXException
    {
        DefaultHandler handler=getElementOrUnknownHandler(
            getPGMLStackParser(),
            this,
            uri,
            localname,
            qname,
            attributes);
        getPGMLStackParser().pushHandlerStack( handler);
    }

    public void endElement( String uri, String localname, String qname)
        throws SAXException
    {
        getPGMLStackParser().popHandlerStack();
        gotElement( _elementContents.toString());
    }

    public void characters( char ch[], int start, int length)
    {
        _elementContents.append( ch, start, length);
    }

    public void ignorableWhitespace( char ch[], int start, int length)
    {
        _elementContents.append( ch, start, length);
    }
}