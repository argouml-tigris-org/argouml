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

/*
 * @(#)Token.java	1.2 98/05/04
 *
 * Copyright (c) 1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package org.argouml.language.java;

import java.io.Serializable;

/**
 * Simple class to represent a lexical token.  This
 * wraps the JavaConstants used by the scanner to provide
 * a convenient class that can be stored as a attribute
 * value.
 *
 * @author  Timothy Prinzing
 * @version 1.2 05/04/98
 */
public class Token implements Serializable {

    Token(String representation, int scanValue) {
	this.representation = representation;
	this.scanValue = scanValue;
    }
    
    /**
     * A human presentable form of the token, useful
     * for things like lists, debugging, etc.
     */
    public String toString() {
	return representation;
    }

    /**
     * Numeric value of this token.  This is the value
     * returned by the scanner and is the tie between 
     * the lexical scanner and the tokens.
     */
    public int getScanValue() {
	return scanValue;
    }

    /**
     * Specifies the category of the token as a 
     * string that can be used as a label.
     */
    public String getCategory() {
	String nm = getClass().getName();
	int nmStart = nm.lastIndexOf('.') + 1; // not found results in 0
	return nm.substring(nmStart, nm.length());
    }

    /**
     * Returns a hashcode for this set of attributes.
     * @return     a hashcode value for this set of attributes.
     */
    public final int hashCode() {
	return scanValue;
    }

    /**
     * Compares this object to the specifed object.
     * The result is <code>true</code> if and only if the argument is not 
     * <code>null</code> and is a <code>Font</code> object with the same 
     * name, style, and point size as this font. 
     * @param     obj   the object to compare this font with.
     * @return    <code>true</code> if the objects are equal; 
     *            <code>false</code> otherwise.
     */
    public final boolean equals(Object obj) {
	if (obj instanceof Token) {
	    Token t = (Token) obj;
	    return (scanValue == t.scanValue);
	}
	return false;
    }

    // --- variables -------------------------------------

    public static final int MaximumScanValue =
	JavaConstants.INLINENEWINSTANCE + 1;

    /**
     * Key to be used in AttributeSet's holding a value of Token.
     */
    public static final Object TokenAttribute = new AttributeKey();

    String representation;
    int scanValue;

    public static class Operator extends Token {

	Operator(String representation, int scanValue) {
	    super(representation, scanValue);
	}

	Operator(int scanValue) {
	    super(JavaConstants.opNames[scanValue], scanValue);
	}

    }
    
    public static class Value extends Token {

	Value(String representation, int scanValue) {
	    super(representation, scanValue);
	}

	Value(int scanValue) {
	    super(JavaConstants.opNames[scanValue], scanValue);
	}

    }
    
    public static class Type extends Token {

	Type(String representation, int scanValue) {
	    super(representation, scanValue);
	}

	Type(int scanValue) {
	    super(JavaConstants.opNames[scanValue], scanValue);
	}

    }
    
    public static class TokExpression extends Token {

	TokExpression(String representation, int scanValue) {
	    super(representation, scanValue);
	}

	TokExpression(int scanValue) {
	    super(JavaConstants.opNames[scanValue], scanValue);
	}

    }
    
    public static class Statement extends Token {

	Statement(String representation, int scanValue) {
	    super(representation, scanValue);
	}

	Statement(int scanValue) {
	    super(JavaConstants.opNames[scanValue], scanValue);
	}

    }
    
    public static class Declaration extends Token {

	Declaration(String representation, int scanValue) {
	    super(representation, scanValue);
	}
	
	Declaration(int scanValue) {
	    super(JavaConstants.opNames[scanValue], scanValue);
	}

    }
    
    public static class Modifier extends Token {

	Modifier(String representation, int scanValue) {
	    super(representation, scanValue);
	}

	Modifier(int scanValue) {
	    super(JavaConstants.opNames[scanValue], scanValue);
	}

    }
    
    public static class Punctuation extends Token {

	Punctuation(String representation, int scanValue) {
	    super(representation, scanValue);
	}

	Punctuation(int scanValue) {
	    super(JavaConstants.opNames[scanValue], scanValue);
	}

    }
    
    public static class Special extends Token {

	Special(String representation, int scanValue) {
	    super(representation, scanValue);
	}

	Special(int scanValue) {
	    super(JavaConstants.opNames[scanValue], scanValue);
	}

    }
    
    static class AttributeKey {

        public AttributeKey() {
	}

        public String toString() {
	    return "token";
	}

    }

    /*
     * Operators
     */
    public static final Token COMMA =       new Operator(JavaConstants.COMMA);
    public static final Token ASSIGN =      new Operator(JavaConstants.ASSIGN);
    public static final Token ASGMUL =      new Operator(JavaConstants.ASGMUL);
    public static final Token ASGDIV =      new Operator(JavaConstants.ASGDIV);
    public static final Token ASGREM =      new Operator(JavaConstants.ASGREM);
    public static final Token ASGADD =      new Operator(JavaConstants.ASGADD);
    public static final Token ASGSUB =      new Operator(JavaConstants.ASGSUB);
    public static final Token ASGLSHIFT =
	new Operator(JavaConstants.ASGLSHIFT);
    public static final Token ASGRSHIFT =
	new Operator(JavaConstants.ASGRSHIFT);
    public static final Token ASGURSHIFT =
	new Operator(JavaConstants.ASGURSHIFT);
    public static final Token ASGBITAND =
	new Operator(JavaConstants.ASGBITAND);
    public static final Token ASGBITOR =
	new Operator(JavaConstants.ASGBITOR);
    public static final Token ASGBITXOR =
	new Operator(JavaConstants.ASGBITOR);
    public static final Token COND =        new Operator(JavaConstants.COND);
    public static final Token OR =          new Operator(JavaConstants.OR);
    public static final Token AND =         new Operator(JavaConstants.AND);
    public static final Token BITOR =       new Operator(JavaConstants.BITOR);
    public static final Token BITXOR =      new Operator(JavaConstants.BITXOR);
    public static final Token BITAND =      new Operator(JavaConstants.BITAND);
    public static final Token NE =          new Operator(JavaConstants.NE);
    public static final Token EQ =          new Operator(JavaConstants.EQ);
    public static final Token GE =          new Operator(JavaConstants.GE);
    public static final Token GT =          new Operator(JavaConstants.GT);
    public static final Token LE =          new Operator(JavaConstants.LE);
    public static final Token LT =          new Operator(JavaConstants.LT);
    public static final Token INSTANCEOF =
	new Operator(JavaConstants.INSTANCEOF);
    public static final Token LSHIFT =      new Operator(JavaConstants.LSHIFT);
    public static final Token RSHIFT =      new Operator(JavaConstants.RSHIFT);
    public static final Token URSHIFT =
	new Operator(JavaConstants.URSHIFT);
    public static final Token ADD =         new Operator(JavaConstants.ADD);
    public static final Token SUB =         new Operator(JavaConstants.SUB);
    public static final Token DIV =         new Operator(JavaConstants.DIV);
    public static final Token REM =         new Operator(JavaConstants.REM);
    public static final Token MUL =         new Operator(JavaConstants.MUL);
    public static final Token CAST =        new Operator(JavaConstants.CAST);
    public static final Token POS =         new Operator(JavaConstants.POS);
    public static final Token NEG =         new Operator(JavaConstants.NEG);
    public static final Token NOT =         new Operator(JavaConstants.NOT);
    public static final Token BITNOT =      new Operator(JavaConstants.BITNOT);
    public static final Token PREINC =      new Operator(JavaConstants.PREINC);
    public static final Token PREDEC =      new Operator(JavaConstants.PREDEC);
    public static final Token NEWARRAY =
	new Operator(JavaConstants.NEWARRAY);
    public static final Token NEWINSTANCE =
	new Operator(JavaConstants.NEWINSTANCE);
    public static final Token NEWFROMNAME =
	new Operator(JavaConstants.NEWFROMNAME);
    public static final Token POSTINC =
	new Operator(JavaConstants.POSTINC);
    public static final Token POSTDEC =
	new Operator(JavaConstants.POSTDEC);
    public static final Token FIELD =       new Operator(JavaConstants.FIELD);
    public static final Token METHOD =      new Operator(JavaConstants.METHOD);
    public static final Token ARRAYACCESS =
	new Operator(JavaConstants.ARRAYACCESS);
    public static final Token NEW =         new Operator(JavaConstants.NEW);
    public static final Token INC =         new Operator(JavaConstants.INC);
    public static final Token DEC =         new Operator(JavaConstants.DEC);
    public static final Token CONVERT =
	new Operator(JavaConstants.CONVERT);
    public static final Token EXPR =        new Operator(JavaConstants.EXPR);
    public static final Token ARRAY =       new Operator(JavaConstants.ARRAY);
    public static final Token GOTO =        new Operator(JavaConstants.GOTO);

    /*
     * Value tokens
     */
    public static final Token IDENT =       new Value(JavaConstants.IDENT);
    public static final Token BOOLEANVAL =
	new Value(JavaConstants.BOOLEANVAL);
    public static final Token BYTEVAL =     new Value(JavaConstants.BYTEVAL);
    public static final Token CHARVAL =     new Value(JavaConstants.CHARVAL);
    public static final Token SHORTVAL =    new Value(JavaConstants.SHORTVAL);
    public static final Token INTVAL =      new Value(JavaConstants.INTVAL);
    public static final Token LONGVAL =     new Value(JavaConstants.LONGVAL);
    public static final Token FLOATVAL =    new Value(JavaConstants.FLOATVAL);
    public static final Token DOUBLEVAL =   new Value(JavaConstants.DOUBLEVAL);
    public static final Token STRINGVAL =   new Value(JavaConstants.STRINGVAL);

    /*
     * Type keywords
     */
    public static final Token BYTE =    new Type(JavaConstants.BYTE);
    public static final Token CHAR =    new Type(JavaConstants.CHAR);
    public static final Token SHORT =   new Type(JavaConstants.SHORT);
    public static final Token INT =     new Type(JavaConstants.INT);
    public static final Token LONG =    new Type(JavaConstants.LONG);
    public static final Token FLOAT =   new Type(JavaConstants.FLOAT);
    public static final Token DOUBLE =  new Type(JavaConstants.DOUBLE);
    public static final Token VOID =    new Type(JavaConstants.VOID);
    public static final Token BOOLEAN = new Type(JavaConstants.BOOLEAN);

    /*
     * TokExpression keywords
     */
    public static final Token TRUE =  new TokExpression(JavaConstants.TRUE);
    public static final Token FALSE = new TokExpression(JavaConstants.FALSE);
    public static final Token THIS =  new TokExpression(JavaConstants.THIS);
    public static final Token SUPER = new TokExpression(JavaConstants.SUPER);
    public static final Token NULL =  new TokExpression(JavaConstants.NULL);

    /*
     * Statement keywords
     */
    public static final Token IF =       new Statement(JavaConstants.IF);
    public static final Token ELSE =     new Statement(JavaConstants.ELSE);
    public static final Token FOR =      new Statement(JavaConstants.FOR);
    public static final Token WHILE =    new Statement(JavaConstants.WHILE);
    public static final Token DO =       new Statement(JavaConstants.DO);
    public static final Token SWITCH =   new Statement(JavaConstants.SWITCH);
    public static final Token CASE =     new Statement(JavaConstants.CASE);
    public static final Token DEFAULT =  new Statement(JavaConstants.DEFAULT);
    public static final Token BREAK =    new Statement(JavaConstants.BREAK);
    public static final Token CONTINUE = new Statement(JavaConstants.CONTINUE);
    public static final Token RETURN =   new Statement(JavaConstants.RETURN);
    public static final Token TRY =      new Statement(JavaConstants.TRY);
    public static final Token CATCH =    new Statement(JavaConstants.CATCH);
    public static final Token FINALLY =  new Statement(JavaConstants.FINALLY);
    public static final Token THROW =    new Statement(JavaConstants.THROW);
    public static final Token STAT =     new Statement(JavaConstants.STAT);
    public static final Token EXPRESSION =
	new Statement(JavaConstants.EXPRESSION);
    public static final Token DECLARATION =
	new Statement(JavaConstants.DECLARATION);
    public static final Token VARDECLARATION =
	new Statement(JavaConstants.VARDECLARATION);

    /*
     * Declaration keywords
     */
    public static final Token IMPORT =  new Declaration(JavaConstants.IMPORT);
    public static final Token CLASS =   new Declaration(JavaConstants.CLASS);
    public static final Token EXTENDS = new Declaration(JavaConstants.EXTENDS);
    public static final Token IMPLEMENTS =
	new Declaration(JavaConstants.IMPLEMENTS);
    public static final Token INTERFACE =
	new Declaration(JavaConstants.INTERFACE);
    public static final Token PACKAGE = new Declaration(JavaConstants.PACKAGE);

    /*
     * Modifier keywords
     */
    public static final Token PRIVATE =   new Modifier(JavaConstants.PRIVATE);
    public static final Token PUBLIC =    new Modifier(JavaConstants.PUBLIC);
    public static final Token PROTECTED = new Modifier(JavaConstants.PROTECTED);
    public static final Token CONST =     new Modifier(JavaConstants.CONST);
    public static final Token STATIC =    new Modifier(JavaConstants.STATIC);
    public static final Token TRANSIENT = new Modifier(JavaConstants.TRANSIENT);
    public static final Token SYNCHRONIZED =
	new Modifier(JavaConstants.SYNCHRONIZED);
    public static final Token NATIVE =    new Modifier(JavaConstants.NATIVE);
    public static final Token FINAL =     new Modifier(JavaConstants.FINAL);
    public static final Token VOLATILE =  new Modifier(JavaConstants.VOLATILE);
    public static final Token ABSTRACT =  new Modifier(JavaConstants.ABSTRACT);

    /*
     * Punctuation
     */
    public static final Token SEMICOLON =
	new Punctuation(JavaConstants.SEMICOLON);
    public static final Token COLON =  new Punctuation(JavaConstants.COLON);
    public static final Token QUESTIONMARK =
	new Punctuation(JavaConstants.QUESTIONMARK);
    public static final Token LBRACE = new Punctuation(JavaConstants.LBRACE);
    public static final Token RBRACE = new Punctuation(JavaConstants.RBRACE);
    public static final Token LPAREN = new Punctuation(JavaConstants.LPAREN);
    public static final Token RPAREN = new Punctuation(JavaConstants.RPAREN);
    public static final Token LSQBRACKET =
	new Punctuation(JavaConstants.LSQBRACKET);
    public static final Token RSQBRACKET =
	new Punctuation(JavaConstants.RSQBRACKET);
    public static final Token THROWS =
	new Punctuation(JavaConstants.THROWS);

    /*
     * Special tokens
     */
    public static final Token ERROR =   new Special(JavaConstants.ERROR);
    public static final Token COMMENT = new Special(JavaConstants.COMMENT);
    public static final Token TYPE =    new Special(JavaConstants.TYPE);
    public static final Token LENGTH =  new Special(JavaConstants.LENGTH);
    public static final Token INLINERETURN =
	new Special(JavaConstants.INLINERETURN);
    public static final Token INLINEMETHOD =
	new Special(JavaConstants.INLINEMETHOD);
    public static final Token INLINENEWINSTANCE =
	new Special(JavaConstants.INLINENEWINSTANCE);
    public static final Token UNSCANNED = new Special("unscanned",
						      MaximumScanValue);

    public static Token[] operators = {
	COMMA, ASSIGN, ASGMUL, ASGDIV, ASGREM, ASGADD, ASGSUB, ASGLSHIFT,
	ASGRSHIFT, ASGURSHIFT, ASGBITAND, ASGBITOR, ASGBITXOR, COND, OR, AND,
	BITOR, BITXOR, BITAND, NE, EQ, GE, GT, LE, LT, INSTANCEOF, LSHIFT, 
	RSHIFT, URSHIFT, ADD, SUB, DIV, REM, MUL, CAST, POS, NEG, NOT, BITNOT,
	PREINC, PREDEC, NEWARRAY, NEWINSTANCE, NEWFROMNAME, POSTINC, POSTDEC,
	FIELD, METHOD, ARRAYACCESS, NEW, INC, DEC, CONVERT, EXPR, ARRAY, GOTO
    };

    public static Token[] values = {
	IDENT, BOOLEANVAL, BYTEVAL, CHARVAL, SHORTVAL, INTVAL, LONGVAL, 
	FLOATVAL, DOUBLEVAL, STRINGVAL
    };

    public static Token[] types = {
	BYTE, CHAR, SHORT, INT, LONG, FLOAT, DOUBLE, VOID, BOOLEAN
    };

    public static Token[] expressions = {
	TRUE, FALSE, THIS, SUPER, NULL
    };

    public static Token[] statements = {
	IF, ELSE, FOR, WHILE, DO, SWITCH, CASE, DEFAULT, BREAK,
	CONTINUE, RETURN, TRY, CATCH, FINALLY, THROW, STAT, EXPRESSION, 
	DECLARATION, VARDECLARATION
    };

    public static Token[] declarations = {
	IMPORT, CLASS, EXTENDS, IMPLEMENTS, INTERFACE, PACKAGE
    };

    public static Token[] modifiers = {
	PRIVATE, PUBLIC, PROTECTED, CONST, STATIC, TRANSIENT, SYNCHRONIZED,
	NATIVE, FINAL, VOLATILE, ABSTRACT
    };

    public static Token[] punctuations = {
	SEMICOLON, COLON, QUESTIONMARK, LBRACE, RBRACE, LPAREN, 
	RPAREN, LSQBRACKET, RSQBRACKET, THROWS
    };

    public static Token[] specials = {
	ERROR, COMMENT, TYPE, LENGTH,
	INLINERETURN, INLINEMETHOD, INLINENEWINSTANCE, UNSCANNED
    };

    public static Token[] all = {
	COMMA, ASSIGN, ASGMUL, ASGDIV, ASGREM, ASGADD, ASGSUB, ASGLSHIFT,
	ASGRSHIFT, ASGURSHIFT, ASGBITAND, ASGBITOR, ASGBITXOR, COND, OR, AND,
	BITOR, BITXOR, BITAND, NE, EQ, GE, GT, LE, LT, INSTANCEOF, LSHIFT, 
	RSHIFT, URSHIFT, ADD, SUB, DIV, REM, MUL, CAST, POS, NEG, NOT, BITNOT,
	PREINC, PREDEC, NEWARRAY, NEWINSTANCE, NEWFROMNAME, POSTINC, POSTDEC,
	FIELD, METHOD, ARRAYACCESS, NEW, INC, DEC, CONVERT, EXPR, ARRAY, GOTO, 
	IDENT, BOOLEANVAL, BYTEVAL, CHARVAL, SHORTVAL, INTVAL, LONGVAL, 
	FLOATVAL, DOUBLEVAL, STRINGVAL,
	BYTE, CHAR, SHORT, INT, LONG, FLOAT, DOUBLE, VOID, BOOLEAN,
	TRUE, FALSE, THIS, SUPER, NULL,
	IF, ELSE, FOR, WHILE, DO, SWITCH, CASE, DEFAULT, BREAK,
	CONTINUE, RETURN, TRY, CATCH, FINALLY, THROW, STAT, EXPRESSION, 
	DECLARATION, VARDECLARATION,
	IMPORT, CLASS, EXTENDS, IMPLEMENTS, INTERFACE, PACKAGE,
	PRIVATE, PUBLIC, PROTECTED, CONST, STATIC, TRANSIENT, SYNCHRONIZED,
	NATIVE, FINAL, VOLATILE, ABSTRACT,
	SEMICOLON, COLON, QUESTIONMARK, LBRACE, RBRACE, LPAREN, 
	RPAREN, LSQBRACKET, RSQBRACKET, THROWS,
	ERROR, COMMENT, TYPE, LENGTH,
	INLINERETURN, INLINEMETHOD, INLINENEWINSTANCE, UNSCANNED
    };

}
