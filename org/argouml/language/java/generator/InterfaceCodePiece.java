// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

/*
  JavaRE - Code generation and reverse engineering for UML and Java
  Author: Marcus Andersson andersson@users.sourceforge.net
*/


package org.argouml.language.java.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Stack;

/**
   This code piece represents an interface declaration.
*/
public class InterfaceCodePiece extends NamedCodePiece {
    /** The code piece this interface represents. */
    private CodePiece interfaceDef;

    /** The name of the interface. */
    private String name;

    /**
       Constructor.

       @param def The code piece this interface represents.
       @param n The name of the interface.
    */
    public InterfaceCodePiece(CodePiece def,
                              String n) {
	interfaceDef = def;
	name = n;
    }

    /**
     * @see org.argouml.language.java.generator.CodePiece#getText()
     *
     * Return the string representation for this piece of code.
     */
    public StringBuffer getText() {
	return interfaceDef.getText();
    }

    /**
     * @see org.argouml.language.java.generator.CodePiece#getStartPosition()
     *
     * Return the start position.
     */
    public int getStartPosition() {
	return interfaceDef.getStartPosition();
    }

    /**
     * @see org.argouml.language.java.generator.CodePiece#getEndPosition()
     *
     * Return the end position.
     */
    public int getEndPosition() {
	return interfaceDef.getEndPosition();
    }

    /**
     * @see org.argouml.language.java.generator.CodePiece#getStartLine()
     *
     * Return the start line
     */
    public int getStartLine() {
	return interfaceDef.getStartLine();
    }

    /**
     * @see org.argouml.language.java.generator.CodePiece#getEndLine()
     * Return the end line
     */
    public int getEndLine() {
	return interfaceDef.getEndLine();
    }

    /**
     * @see org.argouml.language.java.generator.NamedCodePiece#write(
     *         java.io.BufferedReader, java.io.BufferedWriter, java.util.Stack)
     *
     * Write the code this piece represents to file. This will add one
     * level to the stack if the interface is in the model.
     */
    public void write(BufferedReader reader,
                      BufferedWriter writer,
                      Stack parseStateStack) throws IOException {
        ParseState parseState = (ParseState) parseStateStack.peek();
        Object mInterface = /*(MInterface)*/ parseState.newClassifier(name);

	if (mInterface != null) {
	    parseStateStack.push(new ParseState(mInterface));
	    StringBuffer sbText =
		GeneratorJava.getInstance().generateClassifierStart(mInterface);
	    if (sbText != null) {
		writer.write (sbText.toString());
	    }
            // dispose code piece in reader
            ffCodePiece(reader, null);
        } else {
            // not in model, so write the original code
            ffCodePiece(reader, writer);
        }
    }
}
