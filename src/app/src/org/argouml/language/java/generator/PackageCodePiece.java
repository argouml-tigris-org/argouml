// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.language.java.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Stack;

import org.argouml.model.Model;

/**
 * This code piece represents a package declaration.
 *
 * JavaRE - Code generation and reverse engineering for UML and Java.
 *
 * @author Marcus Andersson andersson@users.sourceforge.net
 */
class PackageCodePiece extends NamedCodePiece {
    /**
     * The code piece for the package identifier.
     */
    private CodePiece identifier;

    /**
     * Constructor.
     *
     * @param id Code piece for the package identifier.
     */
    public PackageCodePiece(CodePiece id) {
	identifier = id;
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getText()
     */
    public StringBuffer getText() {
	return identifier.getText();
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getStartPosition()
     */
    public int getStartPosition() {
	return identifier.getStartPosition();
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getEndPosition()
     */
    public int getEndPosition() {
	return identifier.getEndPosition();
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getStartLine()
     */
    public int getStartLine() {
	return identifier.getStartLine();
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getEndLine()
     */
    public int getEndLine() {
	return identifier.getEndLine();
    }

    /*
     * @see org.argouml.language.java.generator.NamedCodePiece#write(
     *         java.io.BufferedReader, java.io.BufferedWriter, java.util.Stack)
     */
    public void write(BufferedReader reader,
                      BufferedWriter writer,
                      Stack<ParseState> parseStateStack) throws IOException {

	ParseState parseState = parseStateStack.peek();
	Object mNamespace = parseState.getNamespace();

	if (!(Model.getFacade().isAModel(mNamespace))) {
	    writer.write("package ");
	    writer.write(GeneratorJava.getInstance()
			 .getPackageName(mNamespace));
	    writer.write(";");
	}
	// fast forward original code (overwriting)
	ffCodePiece(reader, null);
    }
}
