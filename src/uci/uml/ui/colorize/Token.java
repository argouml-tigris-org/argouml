// Copyright (c) 1996-99 The Regents of the University of California. All
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
package uci.uml.ui.colorize;

import java.io.Serializable;
import sun.tools.java.Constants;

/**
 * Simple class to represent a lexical token.  This
 * wraps the Constants used by the scanner to provide
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

    public static final int MaximumScanValue = Constants.INLINENEWINSTANCE + 1;

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

    }
    
    public static class Value extends Token {

	Value(String representation, int scanValue) {
	    super(representation, scanValue);
	}

    }
    
    public static class Type extends Token {

	Type(String representation, int scanValue) {
	    super(representation, scanValue);
	}
    }
    
    public static class Expression extends Token {

	Expression(String representation, int scanValue) {
	    super(representation, scanValue);
	}
    }
    
    public static class Statement extends Token {

	Statement(String representation, int scanValue) {
	    super(representation, scanValue);
	}
    }
    
    public static class Declaration extends Token {

	Declaration(String representation, int scanValue) {
	    super(representation, scanValue);
	}
    }
    
    public static class Modifier extends Token {

	Modifier(String representation, int scanValue) {
	    super(representation, scanValue);
	}
    }
    
    public static class Punctuation extends Token {

	Punctuation(String representation, int scanValue) {
	    super(representation, scanValue);
	}
    }
    
    public static class Special extends Token {

	Special(String representation, int scanValue) {
	    super(representation, scanValue);
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
    public static final Token COMMA =       new Operator(Constants.opNames[Constants.COMMA], 
							 Constants.COMMA);
    public static final Token ASSIGN =      new Operator(Constants.opNames[Constants.ASSIGN],
							 Constants.ASSIGN);
    public static final Token ASGMUL =      new Operator(Constants.opNames[Constants.ASGMUL],
							 Constants.ASGMUL);
    public static final Token ASGDIV =      new Operator(Constants.opNames[Constants.ASGDIV],
							 Constants.ASGDIV);
    public static final Token ASGREM =      new Operator(Constants.opNames[Constants.ASGREM],
							 Constants.ASGREM);
    public static final Token ASGADD =      new Operator(Constants.opNames[Constants.ASGADD],
							 Constants.ASGADD);
    public static final Token ASGSUB =      new Operator(Constants.opNames[Constants.ASGSUB],
							 Constants.ASGSUB);
    public static final Token ASGLSHIFT =   new Operator(Constants.opNames[Constants.ASGLSHIFT],
							 Constants.ASGLSHIFT);
    public static final Token ASGRSHIFT =   new Operator(Constants.opNames[Constants.ASGRSHIFT],
							 Constants.ASGRSHIFT);
    public static final Token ASGURSHIFT =  new Operator(Constants.opNames[Constants.ASGURSHIFT],
							 Constants.ASGURSHIFT);
    public static final Token ASGBITAND =   new Operator(Constants.opNames[Constants.ASGBITAND],
							 Constants.ASGBITAND);
    public static final Token ASGBITOR =    new Operator(Constants.opNames[Constants.ASGBITOR],
							 Constants.ASGBITOR);
    public static final Token ASGBITXOR =   new Operator(Constants.opNames[Constants.ASGBITOR],
							 Constants.ASGBITOR);
    public static final Token COND =        new Operator(Constants.opNames[Constants.COND],
							 Constants.COND);
    public static final Token OR =          new Operator(Constants.opNames[Constants.OR],
							 Constants.OR);
    public static final Token AND =         new Operator(Constants.opNames[Constants.AND],
							 Constants.AND);
    public static final Token BITOR =       new Operator(Constants.opNames[Constants.BITOR],
							 Constants.BITOR);
    public static final Token BITXOR =      new Operator(Constants.opNames[Constants.BITXOR],
							 Constants.BITXOR);
    public static final Token BITAND =      new Operator(Constants.opNames[Constants.BITAND],
							 Constants.BITAND);
    public static final Token NE =          new Operator(Constants.opNames[Constants.NE],
							 Constants.NE);
    public static final Token EQ =          new Operator(Constants.opNames[Constants.EQ],
							 Constants.EQ);
    public static final Token GE =          new Operator(Constants.opNames[Constants.GE],
							 Constants.GE);
    public static final Token GT =          new Operator(Constants.opNames[Constants.GT],
							 Constants.GT);
    public static final Token LE =          new Operator(Constants.opNames[Constants.LE],
							 Constants.LE);
    public static final Token LT =          new Operator(Constants.opNames[Constants.LT],
							 Constants.LT);
    public static final Token INSTANCEOF =  new Operator(Constants.opNames[Constants.INSTANCEOF],
							 Constants.INSTANCEOF);
    public static final Token LSHIFT =      new Operator(Constants.opNames[Constants.LSHIFT],
							 Constants.LSHIFT);
    public static final Token RSHIFT =      new Operator(Constants.opNames[Constants.RSHIFT],
							 Constants.RSHIFT);
    public static final Token URSHIFT =     new Operator(Constants.opNames[Constants.URSHIFT],
							 Constants.URSHIFT);
    public static final Token ADD =         new Operator(Constants.opNames[Constants.ADD],
							 Constants.ADD);
    public static final Token SUB =         new Operator(Constants.opNames[Constants.SUB],
							 Constants.SUB);
    public static final Token DIV =         new Operator(Constants.opNames[Constants.DIV],
							 Constants.DIV);
    public static final Token REM =         new Operator(Constants.opNames[Constants.REM],
							 Constants.REM);
    public static final Token MUL =         new Operator(Constants.opNames[Constants.MUL],
							 Constants.MUL);
    public static final Token CAST =        new Operator(Constants.opNames[Constants.CAST],
							 Constants.CAST);
    public static final Token POS =         new Operator(Constants.opNames[Constants.POS],
							 Constants.POS);
    public static final Token NEG =         new Operator(Constants.opNames[Constants.NEG],
							 Constants.NEG);
    public static final Token NOT =         new Operator(Constants.opNames[Constants.NOT],
							 Constants.NOT);
    public static final Token BITNOT =      new Operator(Constants.opNames[Constants.BITNOT],
							 Constants.BITNOT);
    public static final Token PREINC =      new Operator(Constants.opNames[Constants.PREINC],
							 Constants.PREINC);
    public static final Token PREDEC =      new Operator(Constants.opNames[Constants.PREDEC],
							 Constants.PREDEC);
    public static final Token NEWARRAY =    new Operator(Constants.opNames[Constants.NEWARRAY],
							 Constants.NEWARRAY);
    public static final Token NEWINSTANCE = new Operator(Constants.opNames[Constants.NEWINSTANCE],
							 Constants.NEWINSTANCE);
    public static final Token NEWFROMNAME = new Operator(Constants.opNames[Constants.NEWFROMNAME],
							 Constants.NEWFROMNAME);
    public static final Token POSTINC =     new Operator(Constants.opNames[Constants.POSTINC],
							 Constants.POSTINC);
    public static final Token POSTDEC =     new Operator(Constants.opNames[Constants.POSTDEC],
							 Constants.POSTDEC);
    public static final Token FIELD =       new Operator(Constants.opNames[Constants.FIELD],
							 Constants.FIELD);
    public static final Token METHOD =      new Operator(Constants.opNames[Constants.METHOD],
							 Constants.METHOD);
    public static final Token ARRAYACCESS = new Operator(Constants.opNames[Constants.ARRAYACCESS],
							 Constants.ARRAYACCESS);
    public static final Token NEW =         new Operator(Constants.opNames[Constants.NEW],
							 Constants.NEW);
    public static final Token INC =         new Operator(Constants.opNames[Constants.INC],
							 Constants.INC);
    public static final Token DEC =         new Operator(Constants.opNames[Constants.DEC],
							 Constants.DEC);
    public static final Token CONVERT =     new Operator(Constants.opNames[Constants.CONVERT],
							 Constants.CONVERT);
    public static final Token EXPR =        new Operator(Constants.opNames[Constants.EXPR],
							 Constants.EXPR);
    public static final Token ARRAY =       new Operator(Constants.opNames[Constants.ARRAY],
							 Constants.ARRAY);
    public static final Token GOTO =        new Operator(Constants.opNames[Constants.GOTO],
							 Constants.GOTO);
    /*
     * Value tokens
     */
    public static final Token IDENT =       new Value(Constants.opNames[Constants.IDENT],
						      Constants.IDENT);
    public static final Token BOOLEANVAL =  new Value(Constants.opNames[Constants.BOOLEANVAL],
						      Constants.BOOLEANVAL);
    public static final Token BYTEVAL =     new Value(Constants.opNames[Constants.BYTEVAL],
						      Constants.BYTEVAL);
    public static final Token CHARVAL =     new Value(Constants.opNames[Constants.CHARVAL],
						      Constants.CHARVAL);
    public static final Token SHORTVAL =    new Value(Constants.opNames[Constants.SHORTVAL],
						      Constants.SHORTVAL);
    public static final Token INTVAL =      new Value(Constants.opNames[Constants.INTVAL],
						      Constants.INTVAL);
    public static final Token LONGVAL =     new Value(Constants.opNames[Constants.LONGVAL],
						      Constants.LONGVAL);
    public static final Token FLOATVAL =    new Value(Constants.opNames[Constants.FLOATVAL],
						      Constants.FLOATVAL);
    public static final Token DOUBLEVAL =   new Value(Constants.opNames[Constants.DOUBLEVAL],
						      Constants.DOUBLEVAL);
    public static final Token STRINGVAL =   new Value(Constants.opNames[Constants.STRINGVAL],
						      Constants.STRINGVAL);
    /*
     * Type keywords
     */
    public static final Token BYTE =        new Type(Constants.opNames[Constants.BYTE],
						     Constants.BYTE);
    public static final Token CHAR =        new Type(Constants.opNames[Constants.CHAR],
						     Constants.CHAR);
    public static final Token SHORT =       new Type(Constants.opNames[Constants.SHORT],
						     Constants.SHORT);
    public static final Token INT =         new Type(Constants.opNames[Constants.INT],
						     Constants.INT);
    public static final Token LONG =        new Type(Constants.opNames[Constants.LONG],
						     Constants.LONG);
    public static final Token FLOAT =       new Type(Constants.opNames[Constants.FLOAT],
						     Constants.FLOAT);
    public static final Token DOUBLE =      new Type(Constants.opNames[Constants.DOUBLE],
						     Constants.DOUBLE);
    public static final Token VOID =        new Type(Constants.opNames[Constants.VOID],
						     Constants.VOID);
    public static final Token BOOLEAN =     new Type(Constants.opNames[Constants.BOOLEAN],
						     Constants.BOOLEAN);
    /*
     * Expression keywords
     */
    public static final Token TRUE =        new Expression(Constants.opNames[Constants.TRUE],
							   Constants.TRUE);
    public static final Token FALSE =       new Expression(Constants.opNames[Constants.FALSE],
							   Constants.FALSE);
    public static final Token THIS =        new Expression(Constants.opNames[Constants.THIS],
							   Constants.THIS);
    public static final Token SUPER =       new Expression(Constants.opNames[Constants.SUPER],
							   Constants.SUPER);
    public static final Token NULL =        new Expression(Constants.opNames[Constants.NULL],
							   Constants.NULL);
    /*
     * Statement keywords
     */
    public static final Token IF =             new Statement(Constants.opNames[Constants.IF],
							     Constants.IF);
    public static final Token ELSE =           new Statement(Constants.opNames[Constants.ELSE],
							     Constants.ELSE);
    public static final Token FOR =            new Statement(Constants.opNames[Constants.FOR],
							     Constants.FOR);
    public static final Token WHILE =          new Statement(Constants.opNames[Constants.WHILE],
							     Constants.WHILE);
    public static final Token DO =             new Statement(Constants.opNames[Constants.DO],
							     Constants.DO);
    public static final Token SWITCH =         new Statement(Constants.opNames[Constants.SWITCH],
							     Constants.SWITCH);
    public static final Token CASE =           new Statement(Constants.opNames[Constants.CASE],
							     Constants.CASE);
    public static final Token DEFAULT =        new Statement(Constants.opNames[Constants.DEFAULT],
							     Constants.DEFAULT);
    public static final Token BREAK =          new Statement(Constants.opNames[Constants.BREAK],
							     Constants.BREAK);
    public static final Token CONTINUE =       new Statement(Constants.opNames[Constants.CONTINUE],
							     Constants.CONTINUE);
    public static final Token RETURN =         new Statement(Constants.opNames[Constants.RETURN],
							     Constants.RETURN);
    public static final Token TRY =            new Statement(Constants.opNames[Constants.TRY],
							     Constants.TRY);
    public static final Token CATCH =          new Statement(Constants.opNames[Constants.CATCH],
							     Constants.CATCH);
    public static final Token FINALLY =        new Statement(Constants.opNames[Constants.FINALLY],
							     Constants.FINALLY);
    public static final Token THROW =          new Statement(Constants.opNames[Constants.THROW],
							     Constants.THROW);
    public static final Token STAT =           new Statement(Constants.opNames[Constants.STAT],
							     Constants.STAT);
    public static final Token EXPRESSION =     new Statement(Constants.opNames[Constants.EXPRESSION],
							     Constants.EXPRESSION);
    public static final Token DECLARATION =    new Statement(Constants.opNames[Constants.DECLARATION],
							     Constants.DECLARATION);
    public static final Token VARDECLARATION = new Statement(Constants.opNames[Constants.VARDECLARATION],
							     Constants.VARDECLARATION);
    /*
     * Declaration keywords
     */
    public static final Token IMPORT =         new Declaration(Constants.opNames[Constants.IMPORT],
							       Constants.IMPORT);
    public static final Token CLASS =          new Declaration(Constants.opNames[Constants.CLASS],
							       Constants.CLASS);
    public static final Token EXTENDS =        new Declaration(Constants.opNames[Constants.EXTENDS],
							       Constants.EXTENDS);
    public static final Token IMPLEMENTS =     new Declaration(Constants.opNames[Constants.IMPLEMENTS],
							       Constants.IMPLEMENTS);
    public static final Token INTERFACE =      new Declaration(Constants.opNames[Constants.INTERFACE],
							       Constants.INTERFACE);
    public static final Token PACKAGE =        new Declaration(Constants.opNames[Constants.PACKAGE],
							       Constants.PACKAGE);
    /*
     * Modifier keywords
     */
    public static final Token PRIVATE =        new Modifier(Constants.opNames[Constants.PRIVATE],
							    Constants.PRIVATE);
    public static final Token PUBLIC =         new Modifier(Constants.opNames[Constants.PUBLIC],
							    Constants.PUBLIC);
    public static final Token PROTECTED =      new Modifier(Constants.opNames[Constants.PROTECTED],
							    Constants.PROTECTED);
    public static final Token CONST =          new Modifier(Constants.opNames[Constants.CONST],
							    Constants.CONST);
    public static final Token STATIC =         new Modifier(Constants.opNames[Constants.STATIC],
							    Constants.STATIC);
    public static final Token TRANSIENT =      new Modifier(Constants.opNames[Constants.TRANSIENT],
							    Constants.TRANSIENT);
    public static final Token SYNCHRONIZED =   new Modifier(Constants.opNames[Constants.SYNCHRONIZED],
							    Constants.SYNCHRONIZED);
    public static final Token NATIVE =         new Modifier(Constants.opNames[Constants.NATIVE],
							    Constants.NATIVE);
    public static final Token FINAL =          new Modifier(Constants.opNames[Constants.FINAL],
							    Constants.FINAL);
    public static final Token VOLATILE =       new Modifier(Constants.opNames[Constants.VOLATILE],
							    Constants.VOLATILE);
    public static final Token ABSTRACT =       new Modifier(Constants.opNames[Constants.ABSTRACT],
							    Constants.ABSTRACT);

    /*
     * Punctuation
     */
    public static final Token SEMICOLON =      new Punctuation(Constants.opNames[Constants.SEMICOLON],
							       Constants.SEMICOLON);
    public static final Token COLON =          new Punctuation(Constants.opNames[Constants.COLON],
							       Constants.COLON);
    public static final Token QUESTIONMARK =   new Punctuation(Constants.opNames[Constants.QUESTIONMARK],
							       Constants.QUESTIONMARK);
    public static final Token LBRACE =         new Punctuation(Constants.opNames[Constants.LBRACE],
							       Constants.LBRACE);
    public static final Token RBRACE =         new Punctuation(Constants.opNames[Constants.RBRACE],
							       Constants.RBRACE);
    public static final Token LPAREN =         new Punctuation(Constants.opNames[Constants.LPAREN],
							       Constants.LPAREN);
    public static final Token RPAREN =         new Punctuation(Constants.opNames[Constants.RPAREN],
							       Constants.RPAREN);
    public static final Token LSQBRACKET =     new Punctuation(Constants.opNames[Constants.LSQBRACKET],
							       Constants.LSQBRACKET);
    public static final Token RSQBRACKET =     new Punctuation(Constants.opNames[Constants.RSQBRACKET],
							       Constants.RSQBRACKET);
    public static final Token THROWS =         new Punctuation(Constants.opNames[Constants.THROWS],
							       Constants.THROWS);

    /*
     * Special tokens
     */
    public static final Token ERROR =             new Special(Constants.opNames[Constants.ERROR],
							      Constants.ERROR);
    public static final Token COMMENT =           new Special(Constants.opNames[Constants.COMMENT],
							      Constants.COMMENT);
    public static final Token TYPE =              new Special(Constants.opNames[Constants.TYPE],
							      Constants.TYPE);
    public static final Token LENGTH =            new Special(Constants.opNames[Constants.LENGTH],
							      Constants.LENGTH);
    public static final Token INLINERETURN =      new Special(Constants.opNames[Constants.INLINERETURN],
							      Constants.INLINERETURN);
    public static final Token INLINEMETHOD =      new Special(Constants.opNames[Constants.INLINEMETHOD],
							      Constants.INLINEMETHOD);
    public static final Token INLINENEWINSTANCE = new Special(Constants.opNames[Constants.INLINENEWINSTANCE],
							      Constants.INLINENEWINSTANCE);
    public static final Token UNSCANNED =         new Special("unscanned", MaximumScanValue);

    static Token[] operators = {
	COMMA, ASSIGN, ASGMUL, ASGDIV, ASGREM, ASGADD, ASGSUB, ASGLSHIFT,
	ASGRSHIFT, ASGURSHIFT, ASGBITAND, ASGBITOR, ASGBITXOR, COND, OR, AND,
	BITOR, BITXOR, BITAND, NE, EQ, GE, GT, LE, LT, INSTANCEOF, LSHIFT, 
	RSHIFT, URSHIFT, ADD, SUB, DIV, REM, MUL, CAST, POS, NEG, NOT, BITNOT,
	PREINC, PREDEC, NEWARRAY, NEWINSTANCE, NEWFROMNAME, POSTINC, POSTDEC,
	FIELD, METHOD, ARRAYACCESS, NEW, INC, DEC, CONVERT, EXPR, ARRAY, GOTO
    };

    static Token[] values = {
	IDENT, BOOLEANVAL, BYTEVAL, CHARVAL, SHORTVAL, INTVAL, LONGVAL, 
	FLOATVAL, DOUBLEVAL, STRINGVAL
    };

    static Token[] types = {
	BYTE, CHAR, SHORT, INT, LONG, FLOAT, DOUBLE, VOID, BOOLEAN
    };

    static Token[] expressions = {
	TRUE, FALSE, THIS, SUPER, NULL
    };

    static Token[] statements = {
	IF, ELSE, FOR, WHILE, DO, SWITCH, CASE, DEFAULT, BREAK,
	CONTINUE, RETURN, TRY, CATCH, FINALLY, THROW, STAT, EXPRESSION, 
	DECLARATION, VARDECLARATION
    };

    static Token[] declarations = {
	IMPORT, CLASS, EXTENDS, IMPLEMENTS, INTERFACE, PACKAGE
    };

    static Token[] modifiers = {
	PRIVATE, PUBLIC, PROTECTED, CONST, STATIC, TRANSIENT, SYNCHRONIZED,
	NATIVE, FINAL, VOLATILE, ABSTRACT
    };

    static Token[] punctuations = {
	SEMICOLON, COLON, QUESTIONMARK, LBRACE, RBRACE, LPAREN, 
	RPAREN, LSQBRACKET, RSQBRACKET, THROWS
    };

    static Token[] specials = {
	ERROR, COMMENT, TYPE, LENGTH, INLINERETURN, INLINEMETHOD, INLINENEWINSTANCE, UNSCANNED
    };

    static Token[] all = {
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
	ERROR, COMMENT, TYPE, LENGTH, INLINERETURN, INLINEMETHOD, INLINENEWINSTANCE, UNSCANNED
    };

}
