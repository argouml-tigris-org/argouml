// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

import java.io.*;
import ru.novosoft.uml.foundation.core.*;
import java.util.*;

/**
   This code piece represents the end of a class or an interface.
*/
public class ClassifierEndCodePiece extends NamedCodePiece
{
    /** The curly bracket at the end. */
    private CodePiece bracket;

    /**
       Constructor.

       @param bracket The curly bracket at the end.
    */
    public ClassifierEndCodePiece(CodePiece bracket)
    {
	this.bracket = bracket;
    }

    /**
       Return the string representation for this piece of code.
    */
    public StringBuffer getText()
    {
	return bracket.getText();
    }

    /**
       Return the start position.
    */
    public int getStartPosition()
    {
	return bracket.getStartPosition();
    }

    /**
       Return the end position.
    */
    public int getEndPosition()
    {
	return bracket.getEndPosition();
    }

    /**
       Return the start line
    */
    public int getStartLine()
    {
	return bracket.getStartLine();
    }

    /**
       Return the end line
    */
    public int getEndLine()
    {
	return bracket.getEndLine();
    }

    /**
       Write the code this piece represents to file. This removes one
       layer from the stack and adds new inner classes and features
       to the class or interface.
    */
    public void write (BufferedReader reader,
                       BufferedWriter writer,
                       Stack parseStateStack) throws Exception
    {
        ParseState parseState = (ParseState) parseStateStack.pop();
        MClassifier mClassifier = parseState.getClassifier();
        Vector newFeatures = parseState.getNewFeatures();
        Vector newInnerClasses = parseState.getNewInnerClasses();

        // Insert new features
        for (Iterator i = newFeatures.iterator(); i.hasNext(); ) {
            MFeature mFeature = (MFeature) i.next();
            if (mFeature instanceof MOperation) {
                CodeGenerator.generateOperation((MOperation) mFeature, mClassifier, reader, writer);
            }
            else if (mFeature instanceof MAttribute) {
                CodeGenerator.generateAttribute((MAttribute) mFeature, mClassifier, reader, writer);
            }
        }

        // Insert new inner classes
        for (Iterator i = newInnerClasses.iterator(); i.hasNext(); ) {
            MModelElement element = (MModelElement) i.next();
            if (element instanceof MClass) {
                CodeGenerator.generateClass((MClass) element, reader, writer);
            }
            else if (element instanceof MInterface) {
		CodeGenerator.generateInterface((MInterface) element, reader, writer);
            }
        }

	StringBuffer sb = GeneratorJava.getInstance()
	    .appendClassifierEnd (new StringBuffer (2), mClassifier, true);
	writer.write (sb.toString());
	// fast forward original code (overwriting)
	ffCodePiece(reader, null);
    }
}
