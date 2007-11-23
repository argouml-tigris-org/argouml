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
import java.util.List;
import java.util.Stack;

import org.argouml.model.Model;

/**
 * This code piece represents an operation declaration.
 *
 * JavaRE - Code generation and reverse engineering for UML and Java.
 *
 * @author Marcus Andersson andersson@users.sourceforge.net
 */
class OperationCodePiece extends NamedCodePiece {
    /**
     * The code piece this operation represents.
     */
    private CodePiece operationDef;

    /**
     * The name of the operation.
     */
    private String name;

    /**
     * Constructor.
     *
     * @param javadoc The code piece for the javadoc.
     * @param operation The code piece this operation represents.
     * @param n The name of the operation.
     */
    public OperationCodePiece(CodePiece javadoc,
                              CodePiece operation,
                              String n) {
	name = n;
	if (javadoc != null) {
	    CompositeCodePiece cp = new CompositeCodePiece(javadoc);
	    cp.add(operation);
	    operationDef = cp;
	} else {
	    operationDef = operation;
	}
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getText()
     */
    public StringBuffer getText() {
	return operationDef.getText();
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getStartPosition()
     */
    public int getStartPosition() {
	return operationDef.getStartPosition();
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getEndPosition()
     */
    public int getEndPosition() {
	return operationDef.getEndPosition();
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getStartLine()
     */
    public int getStartLine() {
	return operationDef.getStartLine();
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getEndLine()
     */
    public int getEndLine() {
	return operationDef.getEndLine();
    }

    /*
     * @see org.argouml.language.java.generator.NamedCodePiece#write(
     *         java.io.BufferedReader, java.io.BufferedWriter, java.util.Stack)
     *
     * Write the code this piece represents to file. Remove this
     * feature from the top vector in the stack newFeaturesStack.
     */
    public void write (BufferedReader reader,
                       BufferedWriter writer,
                       Stack<ParseState> parseStateStack) throws IOException {
        ParseState parseState = parseStateStack.peek();
        List features = parseState.getNewFeaturesList();
        boolean found = false;

        for (Object feature : features) {
            if (Model.getFacade().getName(feature).equals(name)
                    && Model.getFacade().isAOperation(feature)) {
                found = true;
                parseState.newFeature(feature);
                Object mOperation = feature;
                writer.write(GeneratorJava.getInstance()
			     .generateOperation(mOperation, true));
            }
        }
        if (found) {
            // fast forward original code (overwriting)
            ffCodePiece(reader, null);
        } else {
            // not in model, so write the original code
            ffCodePiece(reader, writer);
        }
    }
}
