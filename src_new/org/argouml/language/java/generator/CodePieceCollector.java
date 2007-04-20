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

package org.argouml.language.java.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

import org.argouml.application.api.Argo;
import org.argouml.application.configuration.Configuration;

/**
 * This class collects pieces of code when a source file is parsed,
 * and then updates the file with new code from the model.
 *
 * taken from:
 *
 * JavaRE - Code generation and reverse engineering for UML and Java.
 *
 * @author Marcus Andersson andersson@users.sourceforge.net
 */
public class CodePieceCollector {
    /** Code pieces the parser found. */
    private Vector codePieces;

    /**
       Constructor.
    */
    public CodePieceCollector() {
	codePieces = new Vector();
    }

    /**
       The parser adds a code piece here. The code pieces will be
       inserted in sorted order in the codePieces vector.

       @param codePiece A named code piece found in the code.
    */
    public void add(NamedCodePiece codePiece) {
	int index = 0;

	// Insert in sorted order
	for (Iterator i = codePieces.iterator(); i.hasNext(); index++) {
	    CodePiece cp = (CodePiece) i.next();
	    if (cp.getStartLine() > codePiece.getStartLine()
		|| (cp.getStartLine() == codePiece.getStartLine()
		    && cp.getStartPosition() > codePiece.getStartPosition())) {
		break;
	    }
	}
	codePieces.insertElementAt(codePiece, index);
    }

    /**
     * Replace all the code pieces in a source file with new code from
     * the model, or maintain them if nothing is found in the model.
     *
     * @param source The source file.
     * @param destination The destination file.
     * @param mNamespace The package the source belongs to.
     * @throws IOException if we cannot write or read from the files.
     */
    public void filter(File source,
                       File destination,
                       Object/*MNamespace*/ mNamespace) throws IOException {
	String encoding = null;
	if (Configuration.getString(Argo.KEY_INPUT_SOURCE_ENCODING) == null
	    || Configuration.getString(Argo.KEY_INPUT_SOURCE_ENCODING)
	    	.trim().equals("")) {
	    encoding = System.getProperty("file.encoding");
	} else {
	    encoding = Configuration.getString(Argo.KEY_INPUT_SOURCE_ENCODING);
	}
	FileInputStream in = new FileInputStream(source);
	FileOutputStream out = new FileOutputStream(destination);

	BufferedReader reader =
	    new BufferedReader(new InputStreamReader(in, encoding));
	BufferedWriter writer =
	    new BufferedWriter(new OutputStreamWriter(out, encoding));
	int line = 0;
	int column = 0;
	Stack parseStateStack = new Stack();
	parseStateStack.push(new ParseState(mNamespace));

	for (Iterator i = codePieces.iterator(); i.hasNext();) {
	    NamedCodePiece cp = (NamedCodePiece) i.next();
	    // copy until code piece
	    while (line < cp.getStartLine()) {
		line++;
		column = 0;
		writer.write(reader.readLine());
		writer.newLine();
	    }
	    while (column < cp.getStartPosition()) {
		writer.write(reader.read());
		column++;
	    }
	    // write code piece
	    cp.write(reader, writer, parseStateStack);
	    line = cp.getEndLine();
	    column = cp.getEndPosition();
	}

	// Copy the rest of the file
	String data;
	while ((data = reader.readLine()) != null) {
	    writer.write(data);
	    writer.newLine();
	}

	reader.close();
	writer.close();
    }
}
