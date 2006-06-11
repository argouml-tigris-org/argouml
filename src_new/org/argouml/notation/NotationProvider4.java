// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.notation;



/**
 * A class that implements this interface manages a text
 * shown on a diagram. This means it is able to generate
 * text that represents one or more UML objects.
 * And when the user has edited this text, the model may be adapted
 * by parsing the text.
 * Additionally, a help text for the parsing is provided,
 * so that the user knows the syntax.
 *
 * @author mvw@argouml.org
 */
public interface NotationProvider4 {

    /**
     * @param text the text given by the user to be parsed to adapt the model
     * @return after parsing, the modelelement is generated again,
     *         to normalize the given text
     */
    String parse(String text);

    /**
     * @return the string written in the correct notation
     */
    String toString();

    /**
     * @return a i18 key that represents a help string
     *         giving an explanation to the user of the syntax
     */
    String getParsingHelp();

    /**
     * Sets the <code>Value</code> associated with the specified key.
     *
     * @param key  the <code>String</code> that identifies the stored object
     * @param newValue the <code>Object</code> to store using this key
     */
    void putValue(String key, Object newValue);

    /**
     * Sets the <code>Value</code> associated with the specified key.
     * 
     * @param key the <code>String</code> that identifies the stored object
     * @param newValue the <code>Object</code> to store using this key
     */
    void putValue(String key, boolean newValue);


    /**
     * Gets the <code>Object</code> associated with the specified key.
     *
     * @param key a string containing the specified <code>key</code>
     * @return the binding <code>Object</code> stored with this key; if this
     *          key is not present, it will return <code>null</code>
     */
    Object getValue(String key);

    /**
     * Gets the <code>boolean</code> associated with the specified key.
     *
     * @param key a string containing the specified <code>key</code>
     * @return the binding <code>boolean</code> stored with this key; if this
     *          key is not present, it will return <code>false</code>
     */
    boolean isValue(String key);
}
