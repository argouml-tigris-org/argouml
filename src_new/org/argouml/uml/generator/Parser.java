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

package org.argouml.uml.generator;

import java.text.ParseException;

/**
 * Parent of ParserDisplay.
 * TODO: Explain the purpose of this class!
 * TODO: Why do these functions return the object they receive?
 */
public abstract class Parser {

    /**
     * @param s  the input string to be parsed
     * @return the extensionpoint
     */
    public abstract Object parseExtensionPoint(String s);

    /**
     * @param s the input string to be parsed
     * @param op the operation
     * @throws ParseException when the input is invalid and should be rejected
     */
    public abstract void parseOperation(String s, Object op)
	throws ParseException;

    /**
     * @param s   the input string to be parsed
     * @param attr the attribute
     * @throws ParseException when the input is invalid and should be rejected
     */
    public abstract void parseAttribute(String s, Object attr)
	throws ParseException;

    /**
     * @param trans the transition
     * @param s  the input string to be parsed
     * @return the transition
     * @throws ParseException when there is a syntax error in the input text
     *                        so that it should be rejected
     */
    public abstract Object parseTransition(Object trans, String s)
        throws ParseException;

    /**
     * Parse a given string s with the information given from
     * the action state actionState and update this actionState.
     * @param actionState the input actionstate
     * @param s the input string
     * @return the actionstate
     */
    public abstract Object parseActionState(String s, Object actionState);
} /* end class Parser */
