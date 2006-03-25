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

package org.argouml.model;

import java.util.Collection;
import java.util.Map;

import org.xml.sax.InputSource;

/**
 * A wrapper around the genuine XmiReader that provides public
 * access with no knowledge of actual UML implementation.
 * Unlike many of the interfaces to the model there is no control to force
 * a single instance of an XmiReader. This is to allow work objects generated
 * by the imlementation to be garbage collected when an XmiReader instance
 * falls out of scope.
 *
 * @author Bob Tarling
 */
public interface XmiReader {

    /**
     * Parse a given inputsource to a model.
     *
     * @param pIs the input source for parsing
     * @return MModel the UML model
     * @throws UmlException on any error
     * @deprecated use parse()
     */
    Object parseToModel(InputSource pIs) throws UmlException;

    /**
     * Parse a given inputsource to a collection of top level model elements.
     *
     * @param pIs the input source for parsing
     * @return a collection of top level elements
     * @throws UmlException on any error
     */
    Collection parse(InputSource pIs) throws UmlException;

    /**
     * Parse a given inputsource to a collection of top level model elements.
     * 
     * @param pIs
     *            the input source for parsing
     * @param profile
     *            true if the resulting model will be used as a profile.
     *            Profiles are read-only and will not be written back out when
     *            the model data is written.
     * @return a collection of top level elements
     * @throws UmlException
     *             on any error
     */
    Collection parse(InputSource pIs, boolean profile) throws UmlException;

    /**
     * @return the map
     */
    Map getXMIUUIDToObjectMap();
    
    /**
     * Set the list of element names for which errors should be ignored when
     * reading the XMI file.  The common one is "UML:Diagram" but it can be
     * any element which is not in the metamodel.<p>
     * 
     * This may not be supported by all implementations.  The method will
     * return false if unsupported.
     * 
     * @param elementNames array of element names which reader should ignore
     * @return returns false if the operation is unsupported
     */
    public boolean setIgnoredElements(String[] elementNames);
    
    /**
     * Returns list of element names which will be ignored during XMI import
     * if they cause an error.
     * 
     * @return array of names (Strings)
     */
    public String[] getIgnoredElements();
    
    /**
     * Return the count of elements read during the last XMI read which were on
     * the ignore list.
     * 
     * @return the count
     */
    public int getIgnoredElementCount();
    
    /**
     * Return a user friendly desription of any error during the last XMI read.
     * 
     * @return the error message
     */
    public String getErrorMessage();
}
