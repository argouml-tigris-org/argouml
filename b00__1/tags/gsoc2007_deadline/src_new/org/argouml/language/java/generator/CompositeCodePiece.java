// $Id:CompositeCodePiece.java 11510 2006-11-24 07:37:59Z tfmorris $
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

import java.util.Iterator;
import java.util.Vector;

/**
   This piece of code is a composition of several adjacent pieces of
   code. The code piece can have holes.
*/
public class CompositeCodePiece extends CodePiece {
    /** The code pieces this code piece consists of. */
    private Vector codePieces;

    /**
     * Create a composite piece of code.
     * 
     * @param codePiece
     *            A starter code piece.
     */
    public CompositeCodePiece(CodePiece codePiece) {
	codePieces = new Vector();
	if (codePiece != null) {
	    codePieces.addElement(codePiece);
	}
    }

    /**
     * Append a code piece to the end.
     *
     * @param codePiece the given codepiece
     */
    public void add(CodePiece codePiece) {
	if (codePiece != null) {
	    codePieces.addElement(codePiece);
	}
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getText()
     */
    public StringBuffer getText() {
	Iterator i = codePieces.iterator();
	CodePiece cp = (CodePiece) i.next();
	StringBuffer text = cp.getText();
	int prevEnd = cp.getEndPosition();
	int prevLine = cp.getEndLine();

	for (; i.hasNext();) {
	    cp = (CodePiece) i.next();
	    int spaces = cp.getStartPosition() - prevEnd;
	    if (prevLine != cp.getStartLine()) {
		text.append('\n');
		spaces--;
	    }
	    for (int j = 0; j < spaces; j++) {
		text.append(' ');
	    }
	    text.append(cp.getText().toString());
	    prevEnd = cp.getEndPosition();
	    prevLine = cp.getEndLine();
	}
	return text;
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getStartPosition()
     */
    public int getStartPosition() {
	if (codePieces.size() > 0) {
	    return ((CodePiece) codePieces.firstElement()).getStartPosition();
        }
        return 0;
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getEndPosition()
     */
    public int getEndPosition() {
	if (codePieces.size() > 0) {
	    return ((CodePiece) codePieces.lastElement()).getEndPosition();
        }
	return 0;
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getStartLine()
     */
    public int getStartLine() {
	if (codePieces.size() > 0) {
	    return ((CodePiece) codePieces.firstElement()).getStartLine();
        }
        return 0;
    }

    /*
     * @see org.argouml.language.java.generator.CodePiece#getEndLine()
     */
    public int getEndLine() {
	if (codePieces.size() > 0) {
	    return ((CodePiece) codePieces.lastElement()).getEndLine();
        }
        return 0;
    }
}
