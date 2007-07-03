// $Id: ClassifierEndCodePiece.java 11510 2006-11-24 07:37:59Z tfmorris $
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

package org.argouml.language.java.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

import org.argouml.model.Model;

/**
 * This code piece represents the end of a class or an interface.
 *
 * JavaRE - Code generation and reverse engineering for UML and Java
 *
 * @author Marcus Andersson andersson@users.sourceforge.net
 */
public class ClassifierEndCodePiece extends NamedCodePiece {
    /**
     * The curly bracket at the end.
     */
    private CodePiece bracket;

    /**
     * Constructor.
     *
     * @param br The curly bracket at the end.
     */
    public ClassifierEndCodePiece(CodePiece br) {
	bracket = br;
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getText()
     */
    public StringBuffer getText() {
	return bracket.getText();
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getStartPosition()
     */
    public int getStartPosition() {
	return bracket.getStartPosition();
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getEndPosition()
     */
    public int getEndPosition() {
	return bracket.getEndPosition();
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getStartLine()
     */
    public int getStartLine() {
	return bracket.getStartLine();
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getEndLine()
     */
    public int getEndLine() {
	return bracket.getEndLine();
    }

    /*
     * @see org.argouml.language.java.generator.NamedCodePiece#write(
     *         java.io.BufferedReader, java.io.BufferedWriter, java.util.Stack)
     */
    public void write(BufferedReader reader,
                      BufferedWriter writer,
                      Stack parseStateStack) throws IOException {
        ParseState parseState = (ParseState) parseStateStack.pop();
        Object mClassifier = parseState.getClassifier();
        Vector newFeatures = parseState.getNewFeatures();
        Vector newInnerClasses = parseState.getNewInnerClasses();

        // Insert new features
        for (Iterator i = newFeatures.iterator(); i.hasNext();) {
            Object mFeature = /*(MFeature)*/ i.next();
            if (Model.getFacade().isAOperation(mFeature)) {
                CodeGenerator.generateOperation(mFeature,
						mClassifier, reader, writer);
            } else if (Model.getFacade().isAAttribute(mFeature)) {
                CodeGenerator.generateAttribute(mFeature,
						mClassifier, reader, writer);
            }
        }

        // Insert new inner classes
        for (Iterator i = newInnerClasses.iterator(); i.hasNext();) {
            Object element = /*(MModelElement)*/ i.next();
            if (Model.getFacade().isAClass(element)) {
                CodeGenerator.generateClass(element, reader, writer);
            } else if (Model.getFacade().isAInterface(element)) {
                CodeGenerator.generateInterface(element, reader, writer);
            }
        }

	StringBuffer sb =
	    GeneratorJava.getInstance()
	        .appendClassifierEnd(new StringBuffer(2), mClassifier);
	writer.write (sb.toString());
	// fast forward original code (overwriting)
	ffCodePiece(reader, null);
    }
}
