// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.uml.reveng.classfile;

import antlr.*;
import antlr.collections.*;
import java.io.*;



/**************************************
 * A simple lexer to scan a bytestream
 * Using a generated scanner results in
 * too much overhead and performance
 * issues.
 **************************************/
public class SimpleByteLexer implements ClassfileTokenTypes, TokenStream {

    //////////////////////
    // Instance variables. 

    InputStream _input = null;


    ///////////////
    // Constructors

    /**
     * Create a new bytestream scanner.
     */
    public SimpleByteLexer(InputStream in) {
        _input = in;
    }  
    

    //////////
    // Methods

    /**
     * Return the next byte as a token.
     *
     * @return The next byte as a token or a EOF token.
     */
    final public Token nextToken() throws TokenStreamException {
	int nextByte;

	try {
	    nextByte = _input.read();
	} catch (IOException ie) {
	    throw new TokenStreamIOException(ie);  
	}

	// System.out.println("Generating token for: " + nextByte);

	return (nextByte == -1) ? new ByteToken( Token.EOF_TYPE) : new ByteToken( BYTE, (byte) nextByte);
    }
}

