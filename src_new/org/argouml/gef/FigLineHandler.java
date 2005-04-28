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

import org.tigris.gef.presentation.FigLine;

import org.xml.sax.Attributes;

import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler for elements in PGML file that represent FigLine objects
 */
public class FigLineHandler extends BaseHandler {
    private FigLine _line;
    int _x1, _y1, _x2, _y2;

    /**
     * @param parser The PGMLStackParser for the diagram that contains this
     * FigLine
     * @param line The object corresponding to the element being parsed
     */
    public FigLineHandler(PGMLStackParser parser, FigLine line)
    {
        super( parser);
        _line=line;
        _x1=_y1=0;
    }

    /**
     * We set the line coordinates according
     * to the values received from <b>moveto</b> and <b>lineto</b>
     * sub-elements.
     */
    public void gotElement( String contents)
    {
        _line.setShape( _x1, _y1, _x2, _y2);
    }

    /**
     * Override the getElementHandler in {@link BaseHandler BaseHandler}.
     * We interpret the attributes of sub-elements moveto and lineto
     * immediately as line coordinates, and then skip over these
     * elements by returning null from this function.
     */
    protected DefaultHandler getElementHandler( HandlerStack stack,
                                                Object container,
                                                String uri,
                                                String localname,
                                                String qname,
                                                Attributes attributes)
    {
        if( qname.equals("moveto")) {
            String x1 = attributes.getValue("x");
            String y1 = attributes.getValue("y");
            _x1 = (x1 == null || x1.equals("")) ? 0 : Integer.parseInt(x1);
            _y1 = (y1 == null || y1.equals("")) ? 0 : Integer.parseInt(y1);
        }
        else if( qname.equals("lineto")) {
            String x2 = attributes.getValue("x");
            String y2 = attributes.getValue("y");
            _x2 = (x2 == null || x2.equals("")) ? _x1 : Integer.parseInt(x2);
            _y2 = (y2 == null || y2.equals("")) ? _y1 : Integer.parseInt(y2);
        }
        return null;
    }
}