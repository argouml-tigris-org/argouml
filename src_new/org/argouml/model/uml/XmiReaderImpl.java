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

package org.argouml.model.uml;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A wrapper around the genuine XmiReader that provides public access with no
 * knowledge of actual UML implementation.
 * 
 * @author Bob Tarling
 */
public class XmiReaderImpl implements XmiReader {

    private NsumlXmiReader nsumlXmiReader;

    /**
     * Constructor for XMIReader.
     * 
     * @throws UmlException
     *             when there is a problem
     */
    public XmiReaderImpl() throws UmlException {
        try {
            nsumlXmiReader = new NsumlXmiReader();
        } catch (ParserConfigurationException e) {
            throw new UmlException(e);
        } catch (SAXException e) {
            throw new UmlException(e);
        }
    }

    /**
     * Parses a given inputsource to a model. Does not override the novosoft
     * parse method since that does not have the right signature.
     * 
     * @param pIs
     *            the input source for parsing
     * @return MModel the UML model
     * @throws UmlException
     *             if there is a problem
     */
    public Object parseToModel(InputSource pIs) throws UmlException {
        try {
            return nsumlXmiReader.parseToModel(pIs);
        } catch (SAXException e) {
            throw new UmlException(e);
        } catch (IOException e) {
            throw new UmlException(e);
        }
    }

    /**
     * @return the map
     */
    public Map getXMIUUIDToObjectMap() {
        return nsumlXmiReader.getXMIUUIDToObjectMap();
    }
}
