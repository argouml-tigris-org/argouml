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

/**
   This piece of code is just one token.
*/
public class SimpleCodePiece extends CodePiece
{
    /** */
    private StringBuffer text;

    /** */
    private int line;

    /** */
    private int startPosition;

    /** */
    private int endPosition;

    /**
       Create a simple piece of code.
    */
    public SimpleCodePiece(StringBuffer text,
                           int line,
                           int startPosition,
                           int endPosition)
    {
	this.text = text;
	this.line = line;
	this.startPosition = startPosition;
	this.endPosition = endPosition;
    }

    /**
       Create a simple piece of code from a token.
    */
    public SimpleCodePiece(antlr.Token token)
    {
	this(new StringBuffer(token.getText()),
	     token.getLine() - 1,
	     token.getColumn() - 1,
	     token.getColumn() + token.getText().length() - 1);
    }

    /**
       Return the string representation for this piece of code.
    */
    public StringBuffer getText()
    {
	return text;
    }

    /**
       Return the start position.
    */
    public int getStartPosition()
    {
	return startPosition;
    }

    /**
       Return the end position.
    */
    public int getEndPosition()
    {
	return endPosition;
    }

    /**
	Return the start line
    */
    public int getStartLine()
    {
	return line;
    }

    /**
	Return the end line
    */
    public int getEndLine()
    {
	return line;
    }
}
