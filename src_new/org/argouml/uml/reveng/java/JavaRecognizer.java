// $ANTLR 2.7.1: "java.g" -> "JavaRecognizer.java"$

package org.argouml.uml.reveng.java;

import java.util.*;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

/** Java 1.2 Recognizer
 *
 * Run 'java Main <directory full of java files>'
 *
 * Contributing authors:
 *		John Mitchell		johnm@non.net
 *		Terence Parr		parrt@magelang.com
 *		John Lilley			jlilley@empathy.com
 *		Scott Stanchfield	thetick@magelang.com
 *		Markus Mohnen       mohnen@informatik.rwth-aachen.de
 *		Peter Williams		pwilliams@netdynamics.com
 *
 * Version 1.00 December 9, 1997 -- initial release
 * Version 1.01 December 10, 1997
 *		fixed bug in octal def (0..7 not 0..8)
 * Version 1.10 August 1998 (parrt)
 *		added tree construction
 *		fixed definition of WS,comments for mac,pc,unix newlines
 *		added unary plus
 * Version 1.11 (Nov 20, 1998)
 *		Added "shutup" option to turn off last ambig warning.
 *		Fixed inner class def to allow named class defs as statements
 *		synchronized requires compound not simple statement
 *		add [] after builtInType DOT class in primaryExpression
 *		"const" is reserved but not valid..removed from modifiers
 * Version 1.12 (Feb 2, 1999)
 *		Changed LITERAL_xxx to xxx in tree grammar.
 *		Updated java.g to use tokens {...} now for 2.6.0 (new feature).
 *
 * Version 1.13 (Apr 23, 1999)
 *		Didn't have (stat)? for else clause in tree parser.
 *		Didn't gen ASTs for interface extends.  Updated tree parser too.
 *		Updated to 2.6.0.
 * Version 1.14 (Jun 20, 1999)
 *		Allowed final/abstract on local classes.
 *		Removed local interfaces from methods
 *		Put instanceof precedence where it belongs...in relationalExpr
 *			It also had expr not type as arg; fixed it.
 *		Missing ! on SEMI in classBlock
 *		fixed: (expr) + "string" was parsed incorrectly (+ as unary plus).
 *		fixed: didn't like Object[].class in parser or tree parser
 * Version 1.15 (Jun 26, 1999)
 *		Screwed up rule with instanceof in it. :(  Fixed.
 *		Tree parser didn't like (expr).something; fixed.
 *		Allowed multiple inheritance in tree grammar. oops.
 * Version 1.16 (August 22, 1999)
 *		Extending an interface built a wacky tree: had extra EXTENDS.
 *		Tree grammar didn't allow multiple superinterfaces.
 *		Tree grammar didn't allow empty var initializer: {}
 * Version 1.17 (October 12, 1999)
 *		ESC lexer rule allowed 399 max not 377 max.
 *		java.tree.g didn't handle the expression of synchronized
 *			statements.
 *
 * Version tracking now done with following ID:
 *
 * $Id$
 *
 * BUG:
 * 		Doesn't like boolean.class!
 *
 * class Test {
 *   public static void main( String args[] ) {
 *     if (boolean.class.equals(boolean.class)) {
 *       System.out.println("works");
 *     }
 *   }
 * }
 *
 * This grammar is in the PUBLIC DOMAIN
 */
public class JavaRecognizer extends antlr.LLkParser
       implements JavaTokenTypes
 {

	// Constants for access modifiers according to the JVM specs chapter 4
   	public static final short ACC_PUBLIC    = 0x0001;
        public static final short ACC_PRIVATE   = 0x0002;
        public static final short ACC_PROTECTED = 0x0004;
        public static final short ACC_STATIC    = 0x0008;
        public static final short ACC_FINAL     = 0x0010;
        public static final short ACC_SUPER     = 0x0020;
        public static final short ACC_VOLATILE  = 0x0040;
        public static final short ACC_TRANSIENT = 0x0080;
        public static final short ACC_NATIVE    = 0x0100;
        public static final short ACC_INTERFACE = 0x0200;
        public static final short ACC_ABSTRACT  = 0x0400;

	// This one is not(!) in the JVM specs, but required
	public static final short ACC_SYNCHRONIZED  = 0x0800;

	/**
         * To get direct access to the lexer (for the javadoc
	 * comments), we store a reference to it.
         */
	private JavaLexer _lexer = null;

	/**
	 * Set the lexer for this parser.
	 *
	 * @param lexer The lexer for this parser.
	 */
	private void setLexer(JavaLexer lexer) {
	    _lexer = lexer;
	}

	/**
	 * Get the last parsed javadoc comment from the lexer.
         */
	private String getJavadocComment() {
	    return _lexer.getJavadocComment();
	}

        private Modeller _modeller;

	Modeller getModeller() {
	    return _modeller;
	}

	void setModeller(Modeller modeller) {
	    _modeller = modeller;
        }

        // A reference to the last added MOperation (here: method)
        private Object _currentMethod = null;

	/**
	 * get reference to the last added MOperation (here: method)
	 */
	Object getMethod() {
	    return _currentMethod;
	}

	/**
	 * set reference to the last added MOperation (here: method)
	 */
	void setMethod(Object method) {
	    _currentMethod = method;
        }

        // A method body
        private String _methodBody = null;

	/**
	 * get last method body
	 */
	String getBody() {
	    return _methodBody;
	}

	/**
	 * set last method body
	 */
	void setBody(String body) {
	    _methodBody = body + '\n';
        }

	// A flag to indicate if we track the tokens for a expression.
	private boolean      _trackExpression  = false;

	// A flag to indicate if we are inside a compoundStatement
	private boolean      _inCompoundStatement  = false;

	// A string buffer for the current expression.
    	private StringBuffer _expressionBuffer = new StringBuffer();

	/**
	 * set if we are inside a compoundStatement
	 */
	void setIsInCompoundStatement(boolean flag) {
	    _inCompoundStatement = flag;
	}

	/**
	 * check if we are inside a compoundStatement
	 */
	boolean isInCompoundStatement() {
	    return _inCompoundStatement;
	}

	/**
	 * Activate the tracking of expressions.
	 */
	void activateExpressionTracking() {
	    _trackExpression = true;
	}

	/**
         * Deactivate the tracking of expressions.
	 */
	void deactivateExpressionTracking() {
	    _trackExpression = false;
        }

	/**
     	 * Get a tracked expression.
     	 *
     	 * @return the tracked expression.
     	 */
    	public String getExpression() {
            String result = _expressionBuffer.toString();

            _expressionBuffer = new StringBuffer();

            return result;
        }

	/**
     	 * Appends to a tracked expression. (used to restore it)
     	 */
    	public void appendExpression(String expr) {
            _expressionBuffer.append(expr);
        }

	public void match(int t) throws MismatchedTokenException, TokenStreamException {
            String text = ((ArgoToken)LT(1)).getWhitespace() + LT(1).getText();

            super.match(t);

            // '== 0' to avoid the following when backtracking
            if(_trackExpression && inputState.guessing==0)
                appendExpression(text);
    	}

protected JavaRecognizer(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public JavaRecognizer(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected JavaRecognizer(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public JavaRecognizer(TokenStream lexer) {
  this(lexer,2);
}

public JavaRecognizer(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	public final void compilationUnit(
		 Modeller modeller, JavaLexer lexer
	) throws RecognitionException, TokenStreamException {

		setModeller(modeller);
		setLexer(lexer);

		{
		switch ( LA(1)) {
		case LITERAL_package:
		{
			packageDefinition();
			break;
		}
		case EOF:
		case FINAL:
		case ABSTRACT:
		case SEMI:
		case LITERAL_import:
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_static:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case LITERAL_class:
		case LITERAL_interface:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		_loop4:
		do {
			if ((LA(1)==LITERAL_import)) {
				importDefinition();
			}
			else {
				break _loop4;
			}

		} while (true);
		}
		{
		_loop6:
		do {
			if ((_tokenSet_0.member(LA(1)))) {
				typeDefinition();
			}
			else {
				break _loop6;
			}

		} while (true);
		}
		match(Token.EOF_TYPE);
	}

	public final void packageDefinition() throws RecognitionException, TokenStreamException {

		String packageName = null;

		try {      // for error handling
			match(LITERAL_package);
			packageName=identifier();
			match(SEMI);
			if ( inputState.guessing==0 ) {
				getModeller().addPackage(packageName);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_1);
			} else {
			  throw ex;
			}
		}
	}

	public final void importDefinition() throws RecognitionException, TokenStreamException {

		String name=null;

		try {      // for error handling
			match(LITERAL_import);
			name=identifierStar();
			match(SEMI);
			if ( inputState.guessing==0 ) {
				getModeller().addImport(name);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_1);
			} else {
			  throw ex;
			}
		}
	}

	public final void typeDefinition() throws RecognitionException, TokenStreamException {

		short m = 0;

		try {      // for error handling
			switch ( LA(1)) {
			case FINAL:
			case ABSTRACT:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_static:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_threadsafe:
			case LITERAL_synchronized:
			case LITERAL_volatile:
			case LITERAL_class:
			case LITERAL_interface:
			{
				m=modifiers();
				{
				switch ( LA(1)) {
				case LITERAL_class:
				{
					classDefinition(getJavadocComment(), m);
					break;
				}
				case LITERAL_interface:
				{
					interfaceDefinition(getJavadocComment(), m);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case SEMI:
			{
				match(SEMI);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
	}

	public final String  identifier() throws RecognitionException, TokenStreamException {
		String name=null;

		Token  t1 = null;
		Token  t2 = null;

		t1 = LT(1);
		match(IDENT);
		if ( inputState.guessing==0 ) {
			name = t1.getText();
		}
		{
		_loop26:
		do {
			if ((LA(1)==DOT)) {
				match(DOT);
				t2 = LT(1);
				match(IDENT);
				if ( inputState.guessing==0 ) {
					name += "." + t2.getText();
				}
			}
			else {
				break _loop26;
			}

		} while (true);
		}
		return name;
	}

	public final String  identifierStar() throws RecognitionException, TokenStreamException {
		String name=null;

		Token  t1 = null;
		Token  t2 = null;

		t1 = LT(1);
		match(IDENT);
		if ( inputState.guessing==0 ) {
			name = t1.getText();
		}
		{
		_loop29:
		do {
			if ((LA(1)==DOT) && (LA(2)==IDENT)) {
				match(DOT);
				t2 = LT(1);
				match(IDENT);
				if ( inputState.guessing==0 ) {
					name = name + "." + t2.getText();
				}
			}
			else {
				break _loop29;
			}

		} while (true);
		}
		{
		switch ( LA(1)) {
		case DOT:
		{
			match(DOT);
			match(STAR);
			if ( inputState.guessing==0 ) {
				name = name + "." + "*";
			}
			break;
		}
		case SEMI:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return name;
	}

	public final  short  modifiers() throws RecognitionException, TokenStreamException {
		 short mod_flags;

		mod_flags = 0;
		short cur_flag;

		{
		_loop14:
		do {
			if ((_tokenSet_3.member(LA(1)))) {
				cur_flag=modifier();
				if ( inputState.guessing==0 ) {
					mod_flags |= cur_flag;
				}
			}
			else {
				break _loop14;
			}

		} while (true);
		}
		return mod_flags;
	}

	public final void classDefinition(
		String javadoc, short modifiers
	) throws RecognitionException, TokenStreamException {

		Token  className = null;
		String superClassName = null; Vector ic = null;

		match(LITERAL_class);
		className = LT(1);
		match(IDENT);
		superClassName=superClassClause();
		ic=implementsClause();
		if ( inputState.guessing==0 ) {
			getModeller().addClass(className.getText(), modifiers, superClassName, ic, javadoc);
		}
		classBlock();
		if ( inputState.guessing==0 ) {
			getModeller().popClassifier();
		}
	}

	public final void interfaceDefinition(
		String javadoc, short modifiers
	) throws RecognitionException, TokenStreamException {

		Token  interfaceName = null;
		Vector ie=null;

		match(LITERAL_interface);
		interfaceName = LT(1);
		match(IDENT);
		ie=interfaceExtends();
		if ( inputState.guessing==0 ) {
			getModeller().addInterface(interfaceName.getText(), modifiers,
			ie, javadoc);
		}
		classBlock();
		if ( inputState.guessing==0 ) {
			getModeller().popClassifier();
		}
	}

/** A declaration is the creation of a reference or primitive-type variable
 *  Create a separate Type/Var tree for each var in the var list.
 */
	public final void declaration() throws RecognitionException, TokenStreamException {

		short m = 0; String t=null;

		m=modifiers();
		t=typeSpec();
		variableDefinitions("", m, t);
	}

	public final String  typeSpec() throws RecognitionException, TokenStreamException {
		String type=null;

		String c=null, b=null;

		switch ( LA(1)) {
		case IDENT:
		{
			c=classTypeSpec();
			if ( inputState.guessing==0 ) {
				type=c;
			}
			break;
		}
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		{
			b=builtInTypeSpec();
			if ( inputState.guessing==0 ) {
				type=b;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return type;
	}

	public final void variableDefinitions(
		String javadoc, short modifiers, String returnType
	) throws RecognitionException, TokenStreamException {


		variableDeclarator(javadoc, modifiers, returnType);
		{
		_loop61:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				variableDeclarator(javadoc, modifiers, returnType);
			}
			else {
				break _loop61;
			}

		} while (true);
		}
	}

	public final short  modifier() throws RecognitionException, TokenStreamException {
		short mod_flag = 0;


		switch ( LA(1)) {
		case LITERAL_private:
		{
			match(LITERAL_private);
			if ( inputState.guessing==0 ) {
				mod_flag = ACC_PRIVATE;
			}
			break;
		}
		case LITERAL_public:
		{
			match(LITERAL_public);
			if ( inputState.guessing==0 ) {
				mod_flag = ACC_PUBLIC;
			}
			break;
		}
		case LITERAL_protected:
		{
			match(LITERAL_protected);
			if ( inputState.guessing==0 ) {
				mod_flag = ACC_PROTECTED;
			}
			break;
		}
		case LITERAL_static:
		{
			match(LITERAL_static);
			if ( inputState.guessing==0 ) {
				mod_flag = ACC_STATIC;
			}
			break;
		}
		case LITERAL_transient:
		{
			match(LITERAL_transient);
			if ( inputState.guessing==0 ) {
				mod_flag = ACC_TRANSIENT;
			}
			break;
		}
		case FINAL:
		{
			match(FINAL);
			if ( inputState.guessing==0 ) {
				mod_flag = ACC_FINAL;
			}
			break;
		}
		case ABSTRACT:
		{
			match(ABSTRACT);
			if ( inputState.guessing==0 ) {
				mod_flag = ACC_ABSTRACT;
			}
			break;
		}
		case LITERAL_native:
		{
			match(LITERAL_native);
			if ( inputState.guessing==0 ) {
				mod_flag = ACC_NATIVE;
			}
			break;
		}
		case LITERAL_threadsafe:
		{
			match(LITERAL_threadsafe);
			break;
		}
		case LITERAL_synchronized:
		{
			match(LITERAL_synchronized);
			if ( inputState.guessing==0 ) {
				mod_flag = ACC_SYNCHRONIZED;
			}
			break;
		}
		case LITERAL_volatile:
		{
			match(LITERAL_volatile);
			if ( inputState.guessing==0 ) {
				mod_flag = ACC_VOLATILE;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return mod_flag;
	}

	public final String  classTypeSpec() throws RecognitionException, TokenStreamException {
		String type=null;


		type=identifier();
		{
		_loop18:
		do {
			if ((LA(1)==LBRACK)) {
				match(LBRACK);
				match(RBRACK);
				if ( inputState.guessing==0 ) {
					type +="[]";
				}
			}
			else {
				break _loop18;
			}

		} while (true);
		}
		return type;
	}

	public final String  builtInTypeSpec() throws RecognitionException, TokenStreamException {
		String type=null;


		type=builtInType();
		{
		_loop21:
		do {
			if ((LA(1)==LBRACK)) {
				match(LBRACK);
				match(RBRACK);
				if ( inputState.guessing==0 ) {
					type += "[]";
				}
			}
			else {
				break _loop21;
			}

		} while (true);
		}
		return type;
	}

	public final String  builtInType() throws RecognitionException, TokenStreamException {
		String type=null;


		switch ( LA(1)) {
		case LITERAL_void:
		{
			match(LITERAL_void);
			if ( inputState.guessing==0 ) {
				type="void";
			}
			break;
		}
		case LITERAL_boolean:
		{
			match(LITERAL_boolean);
			if ( inputState.guessing==0 ) {
				type="boolean";
			}
			break;
		}
		case LITERAL_byte:
		{
			match(LITERAL_byte);
			if ( inputState.guessing==0 ) {
				type="byte";
			}
			break;
		}
		case LITERAL_char:
		{
			match(LITERAL_char);
			if ( inputState.guessing==0 ) {
				type="char";
			}
			break;
		}
		case LITERAL_short:
		{
			match(LITERAL_short);
			if ( inputState.guessing==0 ) {
				type="short";
			}
			break;
		}
		case LITERAL_int:
		{
			match(LITERAL_int);
			if ( inputState.guessing==0 ) {
				type="int";
			}
			break;
		}
		case LITERAL_float:
		{
			match(LITERAL_float);
			if ( inputState.guessing==0 ) {
				type="float";
			}
			break;
		}
		case LITERAL_long:
		{
			match(LITERAL_long);
			if ( inputState.guessing==0 ) {
				type="long";
			}
			break;
		}
		case LITERAL_double:
		{
			match(LITERAL_double);
			if ( inputState.guessing==0 ) {
				type="double";
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return type;
	}

	public final String  type() throws RecognitionException, TokenStreamException {
		String type=null;


		switch ( LA(1)) {
		case IDENT:
		{
			type=identifier();
			break;
		}
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		{
			type=builtInType();
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return type;
	}

	public final String  superClassClause() throws RecognitionException, TokenStreamException {
		String superClassName = null;


		{
		switch ( LA(1)) {
		case LITERAL_extends:
		{
			match(LITERAL_extends);
			superClassName=identifier();
			break;
		}
		case LCURLY:
		case LITERAL_implements:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return superClassName;
	}

	public final Vector  implementsClause() throws RecognitionException, TokenStreamException {
		Vector names=new Vector();

		Token  i = null;
		String n=null;

		{
		switch ( LA(1)) {
		case LITERAL_implements:
		{
			i = LT(1);
			match(LITERAL_implements);
			n=identifier();
			if ( inputState.guessing==0 ) {
				names.addElement(n);
			}
			{
			_loop46:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					n=identifier();
					if ( inputState.guessing==0 ) {
						names.addElement(n);
					}
				}
				else {
					break _loop46;
				}

			} while (true);
			}
			break;
		}
		case LCURLY:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return names;
	}

	public final void classBlock() throws RecognitionException, TokenStreamException {


		match(LCURLY);
		{
		_loop38:
		do {
			switch ( LA(1)) {
			case FINAL:
			case ABSTRACT:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case IDENT:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_static:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_threadsafe:
			case LITERAL_synchronized:
			case LITERAL_volatile:
			case LITERAL_class:
			case LITERAL_interface:
			case LCURLY:
			{
				field();
				break;
			}
			case SEMI:
			{
				match(SEMI);
				break;
			}
			default:
			{
				break _loop38;
			}
			}
		} while (true);
		}
		match(RCURLY);
	}

	public final Vector  interfaceExtends() throws RecognitionException, TokenStreamException {
		Vector names=new Vector();

		Token  e = null;
		String n=null;

		{
		switch ( LA(1)) {
		case LITERAL_extends:
		{
			e = LT(1);
			match(LITERAL_extends);
			n=identifier();
			if ( inputState.guessing==0 ) {
				names.addElement(n);
			}
			{
			_loop42:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					n=identifier();
					if ( inputState.guessing==0 ) {
						names.addElement(n);
					}
				}
				else {
					break _loop42;
				}

			} while (true);
			}
			break;
		}
		case LCURLY:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return names;
	}

	public final void field() throws RecognitionException, TokenStreamException {

		Token  name = null;
		short mods=0; String t=null; Vector param=null; String a=null;
		boolean isOutestCompStat = !isInCompoundStatement();

		if ((_tokenSet_4.member(LA(1))) && (_tokenSet_5.member(LA(2)))) {
			mods=modifiers();
			{
			switch ( LA(1)) {
			case LITERAL_class:
			{
				classDefinition("", mods);
				break;
			}
			case LITERAL_interface:
			{
				interfaceDefinition("", mods);
				break;
			}
			default:
				if ((LA(1)==IDENT) && (LA(2)==LPAREN)) {
					ctorHead(mods);
					compoundStatement();
					if ( inputState.guessing==0 ) {
					  if (isOutestCompStat) {
						getModeller().addBodyToOperation(getMethod(),getBody());
									 setMethod(null);
									 setBody(null);
					  }
					}
				}
				else if (((LA(1) >= LITERAL_void && LA(1) <= IDENT)) && (_tokenSet_6.member(LA(2)))) {
					t=typeSpec();
					{
					if ((LA(1)==IDENT) && (LA(2)==LPAREN)) {
						{
						name = LT(1);
						match(IDENT);
						match(LPAREN);
						param=parameterDeclarationList();
						match(RPAREN);
						{
						a=returnTypeBrackersOnEndOfMethodHead();
						if ( inputState.guessing==0 ) {
							t += a;
						}
						}
						{
						switch ( LA(1)) {
						case LITERAL_throws:
						{
							throwsClause();
							break;
						}
						case SEMI:
						case LCURLY:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						{
						switch ( LA(1)) {
						case LCURLY:
						{
							compoundStatement();
							break;
						}
						case SEMI:
						{
							match(SEMI);
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						}
						if ( inputState.guessing==0 ) {
						  if (isOutestCompStat) {
							setMethod(getModeller().addOperation(mods, t, name.getText(), param, getJavadocComment()));
											   getModeller().addBodyToOperation(getMethod(),getBody());
											   setMethod(null);
											   setBody(null);
						  }
						}
					}
					else if ((LA(1)==IDENT) && (_tokenSet_7.member(LA(2)))) {
						classVariableDefinitions(getJavadocComment(), mods, t);
						match(SEMI);
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

					}
				}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		else if ((LA(1)==LITERAL_static) && (LA(2)==LCURLY)) {
			match(LITERAL_static);
			compoundStatement();
		}
		else if ((LA(1)==LCURLY)) {
			compoundStatement();
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

	}

	public final void ctorHead(
		 short mods
	) throws RecognitionException, TokenStreamException {

		Token  name = null;
		Vector param = null;
		boolean isOutestCompStat = !isInCompoundStatement();

		name = LT(1);
		match(IDENT);
		match(LPAREN);
		param=parameterDeclarationList();
		match(RPAREN);
		if ( inputState.guessing==0 ) {
		  if (isOutestCompStat) {
			setMethod(getModeller().addOperation(mods, null,
						name.getText(), param, getJavadocComment()));
		  }
		}
		{
		switch ( LA(1)) {
		case LITERAL_throws:
		{
			throwsClause();
			break;
		}
		case LCURLY:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}

	public final void compoundStatement() throws RecognitionException, TokenStreamException {

		boolean isOutestCompStat = !isInCompoundStatement();

		match(LCURLY);
		if ( inputState.guessing==0 ) {
			if (isOutestCompStat) {
					   setIsInCompoundStatement(true);
					   activateExpressionTracking();}
		}
		{
		_loop96:
		do {
			if ((_tokenSet_8.member(LA(1)))) {
				statement();
			}
			else {
				break _loop96;
			}

		} while (true);
		}
		if ( inputState.guessing==0 ) {
			if (isOutestCompStat) {
					   setBody(getExpression());
					   deactivateExpressionTracking();
					   setIsInCompoundStatement(false);}
		}
		match(RCURLY);
	}

	public final Vector  parameterDeclarationList() throws RecognitionException, TokenStreamException {
		Vector paramList=new Vector();

		Vector currentParameter=null;

		{
		switch ( LA(1)) {
		case FINAL:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case IDENT:
		{
			currentParameter=parameterDeclaration();
			if ( inputState.guessing==0 ) {
				paramList.add(currentParameter);
			}
			{
			_loop86:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					currentParameter=parameterDeclaration();
					if ( inputState.guessing==0 ) {
						paramList.add(currentParameter);
					}
				}
				else {
					break _loop86;
				}

			} while (true);
			}
			break;
		}
		case RPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return paramList;
	}

	public final String  returnTypeBrackersOnEndOfMethodHead() throws RecognitionException, TokenStreamException {
		String a="";


		{
		_loop82:
		do {
			if ((LA(1)==LBRACK)) {
				match(LBRACK);
				match(RBRACK);
				if ( inputState.guessing==0 ) {
					a += "[]";
				}
			}
			else {
				break _loop82;
			}

		} while (true);
		}
		return a;
	}

	public final void throwsClause() throws RecognitionException, TokenStreamException {


		match(LITERAL_throws);
		identifier();
		{
		_loop79:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				identifier();
			}
			else {
				break _loop79;
			}

		} while (true);
		}
	}

	public final void classVariableDefinitions(
		String javadoc, short modifiers, String returnType
	) throws RecognitionException, TokenStreamException {


		classVariableDeclarator(javadoc, modifiers, returnType);
		{
		_loop56:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				classVariableDeclarator(javadoc, modifiers, returnType);
			}
			else {
				break _loop56;
			}

		} while (true);
		}
	}

/** Declaration of a class variable.
 * It can also include possible initialization.
 */
	public final void classVariableDeclarator(
		String javadoc, short modifiers, String varType
	) throws RecognitionException, TokenStreamException {

		Token  id = null;
		String initializer=null; String b=null;

		{
		id = LT(1);
		match(IDENT);
		b=declaratorBrackets();
		initializer=varInitializer();
		}
		if ( inputState.guessing==0 ) {
			getModeller().addAttribute(modifiers, varType+b, id.getText(), initializer, javadoc);
		}
	}

	public final String  declaratorBrackets() throws RecognitionException, TokenStreamException {
		String b="";


		{
		_loop66:
		do {
			if ((LA(1)==LBRACK)) {
				match(LBRACK);
				match(RBRACK);
				if ( inputState.guessing==0 ) {
					b += "[]";
				}
			}
			else {
				break _loop66;
			}

		} while (true);
		}
		return b;
	}

	public final String  varInitializer() throws RecognitionException, TokenStreamException {
		String expression=null;

		String trackedSoFar = null;

		{
		switch ( LA(1)) {
		case ASSIGN:
		{
			match(ASSIGN);
			if ( inputState.guessing==0 ) {
				trackedSoFar=getExpression();
						   if (!isInCompoundStatement())
						     activateExpressionTracking();
			}
			initializer();
			if ( inputState.guessing==0 ) {
				expression=getExpression();
						   if (isInCompoundStatement()) {
						     activateExpressionTracking();
						     appendExpression(trackedSoFar);
						     appendExpression(expression);
						   } else
						     deactivateExpressionTracking();
			}
			break;
		}
		case SEMI:
		case COMMA:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return expression;
	}

/** Declaration of a variable.  This can be a class/instance variable,
 *   or a local variable in a method
 * It can also include possible initialization.
 */
	public final void variableDeclarator(
		String javadoc, short modifiers, String varType
	) throws RecognitionException, TokenStreamException {

		String initializer=null;

		{
		match(IDENT);
		declaratorBrackets();
		initializer=varInitializer();
		}
	}

	public final void initializer() throws RecognitionException, TokenStreamException {


		switch ( LA(1)) {
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case IDENT:
		case LPAREN:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case LITERAL_this:
		case LITERAL_super:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case NUM_FLOAT:
		{
			expression();
			break;
		}
		case LCURLY:
		{
			arrayInitializer();
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
	}

	public final void arrayInitializer() throws RecognitionException, TokenStreamException {


		match(LCURLY);
		{
		switch ( LA(1)) {
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case IDENT:
		case LCURLY:
		case LPAREN:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case LITERAL_this:
		case LITERAL_super:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case NUM_FLOAT:
		{
			initializer();
			{
			_loop72:
			do {
				if ((LA(1)==COMMA) && (_tokenSet_9.member(LA(2)))) {
					match(COMMA);
					initializer();
				}
				else {
					break _loop72;
				}

			} while (true);
			}
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				break;
			}
			case RCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case RCURLY:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(RCURLY);
	}

	public final void expression() throws RecognitionException, TokenStreamException {


		assignmentExpression();
	}

	public final Vector  parameterDeclaration() throws RecognitionException, TokenStreamException {
		Vector pd=new Vector();

		Token  id = null;
		short pm=0; String ts=null; String pdb=null;

		{
		pm=parameterModifier();
		ts=typeSpec();
		id = LT(1);
		match(IDENT);
		}
		pdb=parameterDeclaratorBrackets();
		if ( inputState.guessing==0 ) {
			pd.add(new Short(pm));
					  pd.add(ts + pdb);
					  pd.add(id.getText());
		}
		return pd;
	}

	public final short  parameterModifier() throws RecognitionException, TokenStreamException {
		short mods=0;;


		{
		switch ( LA(1)) {
		case FINAL:
		{
			match(FINAL);
			if ( inputState.guessing==0 ) {
				mods |= ACC_FINAL;
			}
			break;
		}
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case IDENT:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return mods;
	}

	public final String  parameterDeclaratorBrackets() throws RecognitionException, TokenStreamException {
		String pdb="";


		{
		_loop91:
		do {
			if ((LA(1)==LBRACK)) {
				match(LBRACK);
				match(RBRACK);
				if ( inputState.guessing==0 ) {
					pdb += "[]";
				}
			}
			else {
				break _loop91;
			}

		} while (true);
		}
		return pdb;
	}

	public final void statement() throws RecognitionException, TokenStreamException {


		switch ( LA(1)) {
		case LCURLY:
		{
			compoundStatement();
			break;
		}
		case LITERAL_class:
		{
			classDefinition("",(short)0);
			break;
		}
		case LITERAL_if:
		{
			match(LITERAL_if);
			match(LPAREN);
			expression();
			match(RPAREN);
			statement();
			{
			if ((LA(1)==LITERAL_else) && (_tokenSet_8.member(LA(2)))) {
				match(LITERAL_else);
				statement();
			}
			else if ((_tokenSet_10.member(LA(1))) && (_tokenSet_11.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}

			}
			break;
		}
		case LITERAL_for:
		{
			match(LITERAL_for);
			match(LPAREN);
			forInit();
			match(SEMI);
			forCond();
			match(SEMI);
			forIter();
			match(RPAREN);
			statement();
			break;
		}
		case LITERAL_while:
		{
			match(LITERAL_while);
			match(LPAREN);
			expression();
			match(RPAREN);
			statement();
			break;
		}
		case LITERAL_do:
		{
			match(LITERAL_do);
			statement();
			match(LITERAL_while);
			match(LPAREN);
			expression();
			match(RPAREN);
			match(SEMI);
			break;
		}
		case LITERAL_break:
		{
			match(LITERAL_break);
			{
			switch ( LA(1)) {
			case IDENT:
			{
				match(IDENT);
				break;
			}
			case SEMI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(SEMI);
			break;
		}
		case LITERAL_continue:
		{
			match(LITERAL_continue);
			{
			switch ( LA(1)) {
			case IDENT:
			{
				match(IDENT);
				break;
			}
			case SEMI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(SEMI);
			break;
		}
		case LITERAL_return:
		{
			match(LITERAL_return);
			{
			switch ( LA(1)) {
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case IDENT:
			case LPAREN:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_this:
			case LITERAL_super:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			{
				expression();
				break;
			}
			case SEMI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(SEMI);
			break;
		}
		case LITERAL_switch:
		{
			match(LITERAL_switch);
			match(LPAREN);
			expression();
			match(RPAREN);
			match(LCURLY);
			{
			_loop105:
			do {
				if ((LA(1)==LITERAL_case||LA(1)==LITERAL_default)) {
					casesGroup();
				}
				else {
					break _loop105;
				}

			} while (true);
			}
			match(RCURLY);
			break;
		}
		case LITERAL_try:
		{
			tryBlock();
			break;
		}
		case LITERAL_throw:
		{
			match(LITERAL_throw);
			expression();
			match(SEMI);
			break;
		}
		case SEMI:
		{
			match(SEMI);
			break;
		}
		default:
			if ((LA(1)==FINAL) && (LA(2)==LITERAL_class)) {
				match(FINAL);
				classDefinition("",ACC_FINAL);
			}
			else if ((LA(1)==ABSTRACT) && (LA(2)==LITERAL_class)) {
				match(ABSTRACT);
				classDefinition("",ACC_ABSTRACT);
			}
			else {
				boolean synPredMatched99 = false;
				if (((_tokenSet_12.member(LA(1))) && (_tokenSet_13.member(LA(2))))) {
					int _m99 = mark();
					synPredMatched99 = true;
					inputState.guessing++;
					try {
						{
						declaration();
						}
					}
					catch (RecognitionException pe) {
						synPredMatched99 = false;
					}
					rewind(_m99);
					inputState.guessing--;
				}
				if ( synPredMatched99 ) {
					declaration();
					match(SEMI);
				}
				else if ((_tokenSet_14.member(LA(1))) && (_tokenSet_15.member(LA(2)))) {
					expression();
					match(SEMI);
				}
				else if ((LA(1)==IDENT) && (LA(2)==COLON)) {
					match(IDENT);
					match(COLON);
					statement();
				}
				else if ((LA(1)==LITERAL_synchronized) && (LA(2)==LPAREN)) {
					match(LITERAL_synchronized);
					match(LPAREN);
					expression();
					match(RPAREN);
					compoundStatement();
				}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}}
		}

	public final void forInit() throws RecognitionException, TokenStreamException {


		{
		boolean synPredMatched117 = false;
		if (((_tokenSet_12.member(LA(1))) && (_tokenSet_13.member(LA(2))))) {
			int _m117 = mark();
			synPredMatched117 = true;
			inputState.guessing++;
			try {
				{
				declaration();
				}
			}
			catch (RecognitionException pe) {
				synPredMatched117 = false;
			}
			rewind(_m117);
			inputState.guessing--;
		}
		if ( synPredMatched117 ) {
			declaration();
		}
		else if ((_tokenSet_14.member(LA(1))) && (_tokenSet_16.member(LA(2)))) {
			expressionList();
		}
		else if ((LA(1)==SEMI)) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
	}

	public final void forCond() throws RecognitionException, TokenStreamException {


		{
		switch ( LA(1)) {
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case IDENT:
		case LPAREN:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case LITERAL_this:
		case LITERAL_super:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case NUM_FLOAT:
		{
			expression();
			break;
		}
		case SEMI:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}

	public final void forIter() throws RecognitionException, TokenStreamException {


		{
		switch ( LA(1)) {
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case IDENT:
		case LPAREN:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case LITERAL_this:
		case LITERAL_super:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case NUM_FLOAT:
		{
			expressionList();
			break;
		}
		case RPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}

	public final void casesGroup() throws RecognitionException, TokenStreamException {


		{
		int _cnt108=0;
		_loop108:
		do {
			if ((LA(1)==LITERAL_case||LA(1)==LITERAL_default) && (_tokenSet_17.member(LA(2)))) {
				aCase();
			}
			else {
				if ( _cnt108>=1 ) { break _loop108; } else {throw new NoViableAltException(LT(1), getFilename());}
			}

			_cnt108++;
		} while (true);
		}
		caseSList();
	}

	public final void tryBlock() throws RecognitionException, TokenStreamException {


		match(LITERAL_try);
		compoundStatement();
		{
		_loop124:
		do {
			if ((LA(1)==LITERAL_catch)) {
				handler();
			}
			else {
				break _loop124;
			}

		} while (true);
		}
		{
		switch ( LA(1)) {
		case LITERAL_finally:
		{
			match(LITERAL_finally);
			compoundStatement();
			break;
		}
		case FINAL:
		case ABSTRACT:
		case SEMI:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case IDENT:
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_static:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case LITERAL_class:
		case LCURLY:
		case RCURLY:
		case LPAREN:
		case LITERAL_if:
		case LITERAL_else:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_do:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_return:
		case LITERAL_switch:
		case LITERAL_throw:
		case LITERAL_case:
		case LITERAL_default:
		case LITERAL_try:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case LITERAL_this:
		case LITERAL_super:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case NUM_FLOAT:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}

	public final void aCase() throws RecognitionException, TokenStreamException {


		{
		switch ( LA(1)) {
		case LITERAL_case:
		{
			match(LITERAL_case);
			expression();
			break;
		}
		case LITERAL_default:
		{
			match(LITERAL_default);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(COLON);
	}

	public final void caseSList() throws RecognitionException, TokenStreamException {


		{
		_loop113:
		do {
			if ((_tokenSet_8.member(LA(1)))) {
				statement();
			}
			else {
				break _loop113;
			}

		} while (true);
		}
	}

	public final void expressionList() throws RecognitionException, TokenStreamException {


		expression();
		{
		_loop130:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				expression();
			}
			else {
				break _loop130;
			}

		} while (true);
		}
	}

	public final void handler() throws RecognitionException, TokenStreamException {


		match(LITERAL_catch);
		match(LPAREN);
		parameterDeclaration();
		match(RPAREN);
		compoundStatement();
	}

	public final void assignmentExpression() throws RecognitionException, TokenStreamException {


		conditionalExpression();
		{
		switch ( LA(1)) {
		case ASSIGN:
		case PLUS_ASSIGN:
		case MINUS_ASSIGN:
		case STAR_ASSIGN:
		case DIV_ASSIGN:
		case MOD_ASSIGN:
		case SR_ASSIGN:
		case BSR_ASSIGN:
		case SL_ASSIGN:
		case BAND_ASSIGN:
		case BXOR_ASSIGN:
		case BOR_ASSIGN:
		{
			{
			switch ( LA(1)) {
			case ASSIGN:
			{
				match(ASSIGN);
				break;
			}
			case PLUS_ASSIGN:
			{
				match(PLUS_ASSIGN);
				break;
			}
			case MINUS_ASSIGN:
			{
				match(MINUS_ASSIGN);
				break;
			}
			case STAR_ASSIGN:
			{
				match(STAR_ASSIGN);
				break;
			}
			case DIV_ASSIGN:
			{
				match(DIV_ASSIGN);
				break;
			}
			case MOD_ASSIGN:
			{
				match(MOD_ASSIGN);
				break;
			}
			case SR_ASSIGN:
			{
				match(SR_ASSIGN);
				break;
			}
			case BSR_ASSIGN:
			{
				match(BSR_ASSIGN);
				break;
			}
			case SL_ASSIGN:
			{
				match(SL_ASSIGN);
				break;
			}
			case BAND_ASSIGN:
			{
				match(BAND_ASSIGN);
				break;
			}
			case BXOR_ASSIGN:
			{
				match(BXOR_ASSIGN);
				break;
			}
			case BOR_ASSIGN:
			{
				match(BOR_ASSIGN);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			assignmentExpression();
			break;
		}
		case SEMI:
		case RBRACK:
		case RCURLY:
		case COMMA:
		case RPAREN:
		case COLON:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}

	public final void conditionalExpression() throws RecognitionException, TokenStreamException {


		logicalOrExpression();
		{
		switch ( LA(1)) {
		case QUESTION:
		{
			match(QUESTION);
			assignmentExpression();
			match(COLON);
			conditionalExpression();
			break;
		}
		case SEMI:
		case RBRACK:
		case RCURLY:
		case COMMA:
		case RPAREN:
		case ASSIGN:
		case COLON:
		case PLUS_ASSIGN:
		case MINUS_ASSIGN:
		case STAR_ASSIGN:
		case DIV_ASSIGN:
		case MOD_ASSIGN:
		case SR_ASSIGN:
		case BSR_ASSIGN:
		case SL_ASSIGN:
		case BAND_ASSIGN:
		case BXOR_ASSIGN:
		case BOR_ASSIGN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}

	public final void logicalOrExpression() throws RecognitionException, TokenStreamException {


		logicalAndExpression();
		{
		_loop138:
		do {
			if ((LA(1)==LOR)) {
				match(LOR);
				logicalAndExpression();
			}
			else {
				break _loop138;
			}

		} while (true);
		}
	}

	public final void logicalAndExpression() throws RecognitionException, TokenStreamException {


		inclusiveOrExpression();
		{
		_loop141:
		do {
			if ((LA(1)==LAND)) {
				match(LAND);
				inclusiveOrExpression();
			}
			else {
				break _loop141;
			}

		} while (true);
		}
	}

	public final void inclusiveOrExpression() throws RecognitionException, TokenStreamException {


		exclusiveOrExpression();
		{
		_loop144:
		do {
			if ((LA(1)==BOR)) {
				match(BOR);
				exclusiveOrExpression();
			}
			else {
				break _loop144;
			}

		} while (true);
		}
	}

	public final void exclusiveOrExpression() throws RecognitionException, TokenStreamException {


		andExpression();
		{
		_loop147:
		do {
			if ((LA(1)==BXOR)) {
				match(BXOR);
				andExpression();
			}
			else {
				break _loop147;
			}

		} while (true);
		}
	}

	public final void andExpression() throws RecognitionException, TokenStreamException {


		equalityExpression();
		{
		_loop150:
		do {
			if ((LA(1)==BAND)) {
				match(BAND);
				equalityExpression();
			}
			else {
				break _loop150;
			}

		} while (true);
		}
	}

	public final void equalityExpression() throws RecognitionException, TokenStreamException {


		relationalExpression();
		{
		_loop154:
		do {
			if ((LA(1)==NOT_EQUAL||LA(1)==EQUAL)) {
				{
				switch ( LA(1)) {
				case NOT_EQUAL:
				{
					match(NOT_EQUAL);
					break;
				}
				case EQUAL:
				{
					match(EQUAL);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				relationalExpression();
			}
			else {
				break _loop154;
			}

		} while (true);
		}
	}

	public final void relationalExpression() throws RecognitionException, TokenStreamException {


		shiftExpression();
		{
		switch ( LA(1)) {
		case SEMI:
		case RBRACK:
		case RCURLY:
		case COMMA:
		case RPAREN:
		case ASSIGN:
		case COLON:
		case PLUS_ASSIGN:
		case MINUS_ASSIGN:
		case STAR_ASSIGN:
		case DIV_ASSIGN:
		case MOD_ASSIGN:
		case SR_ASSIGN:
		case BSR_ASSIGN:
		case SL_ASSIGN:
		case BAND_ASSIGN:
		case BXOR_ASSIGN:
		case BOR_ASSIGN:
		case QUESTION:
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case NOT_EQUAL:
		case EQUAL:
		case LT:
		case GT:
		case LE:
		case GE:
		{
			{
			_loop159:
			do {
				if (((LA(1) >= LT && LA(1) <= GE))) {
					{
					switch ( LA(1)) {
					case LT:
					{
						match(LT);
						break;
					}
					case GT:
					{
						match(GT);
						break;
					}
					case LE:
					{
						match(LE);
						break;
					}
					case GE:
					{
						match(GE);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					shiftExpression();
				}
				else {
					break _loop159;
				}

			} while (true);
			}
			break;
		}
		case LITERAL_instanceof:
		{
			match(LITERAL_instanceof);
			typeSpec();
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}

	public final void shiftExpression() throws RecognitionException, TokenStreamException {


		additiveExpression();
		{
		_loop163:
		do {
			if (((LA(1) >= SL && LA(1) <= BSR))) {
				{
				switch ( LA(1)) {
				case SL:
				{
					match(SL);
					break;
				}
				case SR:
				{
					match(SR);
					break;
				}
				case BSR:
				{
					match(BSR);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				additiveExpression();
			}
			else {
				break _loop163;
			}

		} while (true);
		}
	}

	public final void additiveExpression() throws RecognitionException, TokenStreamException {


		multiplicativeExpression();
		{
		_loop167:
		do {
			if ((LA(1)==PLUS||LA(1)==MINUS)) {
				{
				switch ( LA(1)) {
				case PLUS:
				{
					match(PLUS);
					break;
				}
				case MINUS:
				{
					match(MINUS);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				multiplicativeExpression();
			}
			else {
				break _loop167;
			}

		} while (true);
		}
	}

	public final void multiplicativeExpression() throws RecognitionException, TokenStreamException {


		unaryExpression();
		{
		_loop171:
		do {
			if ((_tokenSet_18.member(LA(1)))) {
				{
				switch ( LA(1)) {
				case STAR:
				{
					match(STAR);
					break;
				}
				case DIV:
				{
					match(DIV);
					break;
				}
				case MOD:
				{
					match(MOD);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				unaryExpression();
			}
			else {
				break _loop171;
			}

		} while (true);
		}
	}

	public final void unaryExpression() throws RecognitionException, TokenStreamException {


		switch ( LA(1)) {
		case INC:
		{
			match(INC);
			unaryExpression();
			break;
		}
		case DEC:
		{
			match(DEC);
			unaryExpression();
			break;
		}
		case MINUS:
		{
			match(MINUS);
			unaryExpression();
			break;
		}
		case PLUS:
		{
			match(PLUS);
			unaryExpression();
			break;
		}
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case IDENT:
		case LPAREN:
		case BNOT:
		case LNOT:
		case LITERAL_this:
		case LITERAL_super:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case NUM_FLOAT:
		{
			unaryExpressionNotPlusMinus();
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
	}

	public final void unaryExpressionNotPlusMinus() throws RecognitionException, TokenStreamException {


		switch ( LA(1)) {
		case BNOT:
		{
			match(BNOT);
			unaryExpression();
			break;
		}
		case LNOT:
		{
			match(LNOT);
			unaryExpression();
			break;
		}
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case IDENT:
		case LPAREN:
		case LITERAL_this:
		case LITERAL_super:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case NUM_FLOAT:
		{
			{
			if ((LA(1)==LPAREN) && ((LA(2) >= LITERAL_void && LA(2) <= LITERAL_double))) {
				match(LPAREN);
				builtInTypeSpec();
				match(RPAREN);
				unaryExpression();
			}
			else {
				boolean synPredMatched176 = false;
				if (((LA(1)==LPAREN) && (LA(2)==IDENT))) {
					int _m176 = mark();
					synPredMatched176 = true;
					inputState.guessing++;
					try {
						{
						match(LPAREN);
						classTypeSpec();
						match(RPAREN);
						unaryExpressionNotPlusMinus();
						}
					}
					catch (RecognitionException pe) {
						synPredMatched176 = false;
					}
					rewind(_m176);
					inputState.guessing--;
				}
				if ( synPredMatched176 ) {
					match(LPAREN);
					classTypeSpec();
					match(RPAREN);
					unaryExpressionNotPlusMinus();
				}
				else if ((_tokenSet_19.member(LA(1))) && (_tokenSet_20.member(LA(2)))) {
					postfixExpression();
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}

	public final void postfixExpression() throws RecognitionException, TokenStreamException {


		switch ( LA(1)) {
		case IDENT:
		case LPAREN:
		case LITERAL_this:
		case LITERAL_super:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case NUM_FLOAT:
		{
			primaryExpression();
			{
			_loop183:
			do {
				switch ( LA(1)) {
				case DOT:
				{
					match(DOT);
					{
					switch ( LA(1)) {
					case IDENT:
					{
						match(IDENT);
						break;
					}
					case LITERAL_this:
					{
						match(LITERAL_this);
						break;
					}
					case LITERAL_class:
					{
						match(LITERAL_class);
						break;
					}
					case LITERAL_new:
					{
						newExpression();
						break;
					}
					case LITERAL_super:
					{
						match(LITERAL_super);
						match(LPAREN);
						{
						switch ( LA(1)) {
						case LITERAL_void:
						case LITERAL_boolean:
						case LITERAL_byte:
						case LITERAL_char:
						case LITERAL_short:
						case LITERAL_int:
						case LITERAL_float:
						case LITERAL_long:
						case LITERAL_double:
						case IDENT:
						case LPAREN:
						case PLUS:
						case MINUS:
						case INC:
						case DEC:
						case BNOT:
						case LNOT:
						case LITERAL_this:
						case LITERAL_super:
						case LITERAL_true:
						case LITERAL_false:
						case LITERAL_null:
						case LITERAL_new:
						case NUM_INT:
						case CHAR_LITERAL:
						case STRING_LITERAL:
						case NUM_FLOAT:
						{
							expressionList();
							break;
						}
						case RPAREN:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						match(RPAREN);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					break;
				}
				case LPAREN:
				{
					match(LPAREN);
					argList();
					match(RPAREN);
					break;
				}
				default:
					if ((LA(1)==LBRACK) && (LA(2)==RBRACK)) {
						{
						int _cnt182=0;
						_loop182:
						do {
							if ((LA(1)==LBRACK)) {
								match(LBRACK);
								match(RBRACK);
							}
							else {
								if ( _cnt182>=1 ) { break _loop182; } else {throw new NoViableAltException(LT(1), getFilename());}
							}

							_cnt182++;
						} while (true);
						}
						match(DOT);
						match(LITERAL_class);
					}
					else if ((LA(1)==LBRACK) && (_tokenSet_14.member(LA(2)))) {
						match(LBRACK);
						expression();
						match(RBRACK);
					}
				else {
					break _loop183;
				}
				}
			} while (true);
			}
			{
			switch ( LA(1)) {
			case INC:
			{
				match(INC);
				break;
			}
			case DEC:
			{
				match(DEC);
				break;
			}
			case SEMI:
			case RBRACK:
			case STAR:
			case RCURLY:
			case COMMA:
			case RPAREN:
			case ASSIGN:
			case COLON:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case NOT_EQUAL:
			case EQUAL:
			case LT:
			case GT:
			case LE:
			case GE:
			case LITERAL_instanceof:
			case SL:
			case SR:
			case BSR:
			case PLUS:
			case MINUS:
			case DIV:
			case MOD:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		{
			builtInType();
			{
			_loop186:
			do {
				if ((LA(1)==LBRACK)) {
					match(LBRACK);
					match(RBRACK);
				}
				else {
					break _loop186;
				}

			} while (true);
			}
			match(DOT);
			match(LITERAL_class);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
	}

	public final void primaryExpression() throws RecognitionException, TokenStreamException {


		switch ( LA(1)) {
		case IDENT:
		{
			match(IDENT);
			break;
		}
		case LITERAL_new:
		{
			newExpression();
			break;
		}
		case NUM_INT:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case NUM_FLOAT:
		{
			constant();
			break;
		}
		case LITERAL_super:
		{
			match(LITERAL_super);
			break;
		}
		case LITERAL_true:
		{
			match(LITERAL_true);
			break;
		}
		case LITERAL_false:
		{
			match(LITERAL_false);
			break;
		}
		case LITERAL_this:
		{
			match(LITERAL_this);
			break;
		}
		case LITERAL_null:
		{
			match(LITERAL_null);
			break;
		}
		case LPAREN:
		{
			match(LPAREN);
			assignmentExpression();
			match(RPAREN);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
	}

/** object instantiation.
 *  Trees are built as illustrated by the following input/tree pairs:
 *
 *  new T()
 *
 *  new
 *   |
 *   T --  ELIST
 *           |
 *          arg1 -- arg2 -- .. -- argn
 *
 *  new int[]
 *
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR
 *
 *  new int[] {1,2}
 *
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR -- ARRAY_INIT
 *                                  |
 *                                EXPR -- EXPR
 *                                  |      |
 *                                  1      2
 *
 *  new int[3]
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR
 *                |
 *              EXPR
 *                |
 *                3
 *
 *  new int[1][2]
 *
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR
 *               |
 *         ARRAY_DECLARATOR -- EXPR
 *               |              |
 *             EXPR             1
 *               |
 *               2
 *
 */
	public final void newExpression() throws RecognitionException, TokenStreamException {

		String t = null;

		match(LITERAL_new);
		t=type();
		{
		switch ( LA(1)) {
		case LPAREN:
		{
			match(LPAREN);
			argList();
			match(RPAREN);
			{
			switch ( LA(1)) {
			case LCURLY:
			{
				if ( inputState.guessing==0 ) {
					getModeller().addAnonymousClass(t);
				}
				classBlock();
				if ( inputState.guessing==0 ) {
					getModeller().popClassifier();
				}
				break;
			}
			case SEMI:
			case LBRACK:
			case RBRACK:
			case DOT:
			case STAR:
			case RCURLY:
			case COMMA:
			case LPAREN:
			case RPAREN:
			case ASSIGN:
			case COLON:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case NOT_EQUAL:
			case EQUAL:
			case LT:
			case GT:
			case LE:
			case GE:
			case LITERAL_instanceof:
			case SL:
			case SR:
			case BSR:
			case PLUS:
			case MINUS:
			case DIV:
			case MOD:
			case INC:
			case DEC:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case LBRACK:
		{
			newArrayDeclarator();
			{
			switch ( LA(1)) {
			case LCURLY:
			{
				arrayInitializer();
				break;
			}
			case SEMI:
			case LBRACK:
			case RBRACK:
			case DOT:
			case STAR:
			case RCURLY:
			case COMMA:
			case LPAREN:
			case RPAREN:
			case ASSIGN:
			case COLON:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case NOT_EQUAL:
			case EQUAL:
			case LT:
			case GT:
			case LE:
			case GE:
			case LITERAL_instanceof:
			case SL:
			case SR:
			case BSR:
			case PLUS:
			case MINUS:
			case DIV:
			case MOD:
			case INC:
			case DEC:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}

	public final void argList() throws RecognitionException, TokenStreamException {


		{
		switch ( LA(1)) {
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case IDENT:
		case LPAREN:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case LITERAL_this:
		case LITERAL_super:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case NUM_FLOAT:
		{
			expressionList();
			break;
		}
		case RPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}

	public final void constant() throws RecognitionException, TokenStreamException {


		switch ( LA(1)) {
		case NUM_INT:
		{
			match(NUM_INT);
			break;
		}
		case CHAR_LITERAL:
		{
			match(CHAR_LITERAL);
			break;
		}
		case STRING_LITERAL:
		{
			match(STRING_LITERAL);
			break;
		}
		case NUM_FLOAT:
		{
			match(NUM_FLOAT);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
	}

	public final void newArrayDeclarator() throws RecognitionException, TokenStreamException {


		{
		int _cnt197=0;
		_loop197:
		do {
			if ((LA(1)==LBRACK) && (_tokenSet_21.member(LA(2)))) {
				match(LBRACK);
				{
				switch ( LA(1)) {
				case LITERAL_void:
				case LITERAL_boolean:
				case LITERAL_byte:
				case LITERAL_char:
				case LITERAL_short:
				case LITERAL_int:
				case LITERAL_float:
				case LITERAL_long:
				case LITERAL_double:
				case IDENT:
				case LPAREN:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case LITERAL_this:
				case LITERAL_super:
				case LITERAL_true:
				case LITERAL_false:
				case LITERAL_null:
				case LITERAL_new:
				case NUM_INT:
				case CHAR_LITERAL:
				case STRING_LITERAL:
				case NUM_FLOAT:
				{
					expression();
					break;
				}
				case RBRACK:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(RBRACK);
			}
			else {
				if ( _cnt197>=1 ) { break _loop197; } else {throw new NoViableAltException(LT(1), getFilename());}
			}

			_cnt197++;
		} while (true);
		}
	}


	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"BLOCK",
		"MODIFIERS",
		"OBJBLOCK",
		"SLIST",
		"CTOR_DEF",
		"METHOD_DEF",
		"VARIABLE_DEF",
		"INSTANCE_INIT",
		"STATIC_INIT",
		"TYPE",
		"CLASS_DEF",
		"INTERFACE_DEF",
		"PACKAGE_DEF",
		"ARRAY_DECLARATOR",
		"EXTENDS_CLAUSE",
		"IMPLEMENTS_CLAUSE",
		"PARAMETERS",
		"PARAMETER_DEF",
		"LABELED_STAT",
		"TYPECAST",
		"INDEX_OP",
		"POST_INC",
		"POST_DEC",
		"METHOD_CALL",
		"EXPR",
		"ARRAY_INIT",
		"IMPORT",
		"UNARY_MINUS",
		"UNARY_PLUS",
		"CASE_GROUP",
		"ELIST",
		"FOR_INIT",
		"FOR_CONDITION",
		"FOR_ITERATOR",
		"EMPTY_STAT",
		"\"final\"",
		"\"abstract\"",
		"\"package\"",
		"SEMI",
		"\"import\"",
		"LBRACK",
		"RBRACK",
		"\"void\"",
		"\"boolean\"",
		"\"byte\"",
		"\"char\"",
		"\"short\"",
		"\"int\"",
		"\"float\"",
		"\"long\"",
		"\"double\"",
		"IDENT",
		"DOT",
		"STAR",
		"\"private\"",
		"\"public\"",
		"\"protected\"",
		"\"static\"",
		"\"transient\"",
		"\"native\"",
		"\"threadsafe\"",
		"\"synchronized\"",
		"\"volatile\"",
		"\"class\"",
		"\"extends\"",
		"\"interface\"",
		"LCURLY",
		"RCURLY",
		"COMMA",
		"\"implements\"",
		"LPAREN",
		"RPAREN",
		"ASSIGN",
		"\"throws\"",
		"COLON",
		"\"if\"",
		"\"else\"",
		"\"for\"",
		"\"while\"",
		"\"do\"",
		"\"break\"",
		"\"continue\"",
		"\"return\"",
		"\"switch\"",
		"\"throw\"",
		"\"case\"",
		"\"default\"",
		"\"try\"",
		"\"finally\"",
		"\"catch\"",
		"PLUS_ASSIGN",
		"MINUS_ASSIGN",
		"STAR_ASSIGN",
		"DIV_ASSIGN",
		"MOD_ASSIGN",
		"SR_ASSIGN",
		"BSR_ASSIGN",
		"SL_ASSIGN",
		"BAND_ASSIGN",
		"BXOR_ASSIGN",
		"BOR_ASSIGN",
		"QUESTION",
		"LOR",
		"LAND",
		"BOR",
		"BXOR",
		"BAND",
		"NOT_EQUAL",
		"EQUAL",
		"LT",
		"GT",
		"LE",
		"GE",
		"\"instanceof\"",
		"SL",
		"SR",
		"BSR",
		"PLUS",
		"MINUS",
		"DIV",
		"MOD",
		"INC",
		"DEC",
		"BNOT",
		"LNOT",
		"\"this\"",
		"\"super\"",
		"\"true\"",
		"\"false\"",
		"\"null\"",
		"\"new\"",
		"NUM_INT",
		"CHAR_LITERAL",
		"STRING_LITERAL",
		"NUM_FLOAT",
		"WS",
		"SL_COMMENT",
		"JAVADOC",
		"ML_COMMENT",
		"ESC",
		"HEX_DIGIT",
		"VOCAB",
		"EXPONENT",
		"FLOAT_SUFFIX"
	};

	private static final long _tokenSet_0_data_[] = { -288224328837758976L, 47L, 0L, 0L };
	public static final BitSet _tokenSet_0 = new BitSet(_tokenSet_0_data_);
	private static final long _tokenSet_1_data_[] = { -288215532744736766L, 47L, 0L, 0L };
	public static final BitSet _tokenSet_1 = new BitSet(_tokenSet_1_data_);
	private static final long _tokenSet_2_data_[] = { -288224328837758974L, 47L, 0L, 0L };
	public static final BitSet _tokenSet_2 = new BitSet(_tokenSet_2_data_);
	private static final long _tokenSet_3_data_[] = { -288228726884270080L, 7L, 0L, 0L };
	public static final BitSet _tokenSet_3 = new BitSet(_tokenSet_3_data_);
	private static final long _tokenSet_4_data_[] = { -216241501590519808L, 47L, 0L, 0L };
	public static final BitSet _tokenSet_4 = new BitSet(_tokenSet_4_data_);
	private static final long _tokenSet_5_data_[] = { -144166315366547456L, 1071L, 0L, 0L };
	public static final BitSet _tokenSet_5 = new BitSet(_tokenSet_5_data_);
	private static final long _tokenSet_6_data_[] = { 108103983242936320L, 0L, 0L };
	public static final BitSet _tokenSet_6 = new BitSet(_tokenSet_6_data_);
	private static final long _tokenSet_7_data_[] = { 21990232555520L, 4352L, 0L, 0L };
	public static final BitSet _tokenSet_7 = new BitSet(_tokenSet_7_data_);
	private static final long _tokenSet_8_data_[] = { -216237103544008704L, -1873497444818451377L, 2047L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_8 = new BitSet(_tokenSet_8_data_);
	private static final long _tokenSet_9_data_[] = { 71987225293750272L, -1873497444986125248L, 2047L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_9 = new BitSet(_tokenSet_9_data_);
	private static final long _tokenSet_10_data_[] = { -216237103544008704L, -1873497444717722417L, 2047L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_10 = new BitSet(_tokenSet_10_data_);
	private static final long _tokenSet_11_data_[] = { -46729244180480L, -11025L, 2047L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_11 = new BitSet(_tokenSet_11_data_);
	private static final long _tokenSet_12_data_[] = { -216241501590519808L, 7L, 0L, 0L };
	public static final BitSet _tokenSet_12 = new BitSet(_tokenSet_12_data_);
	private static final long _tokenSet_13_data_[] = { -144166315366547456L, 7L, 0L, 0L };
	public static final BitSet _tokenSet_13 = new BitSet(_tokenSet_13_data_);
	private static final long _tokenSet_14_data_[] = { 71987225293750272L, -1873497444986125312L, 2047L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_14 = new BitSet(_tokenSet_14_data_);
	private static final long _tokenSet_15_data_[] = { 288181997640089600L, -1073736704L, 2047L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_15 = new BitSet(_tokenSet_15_data_);
	private static final long _tokenSet_16_data_[] = { 288181997640089600L, -1073736448L, 2047L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_16 = new BitSet(_tokenSet_16_data_);
	private static final long _tokenSet_17_data_[] = { 71987225293750272L, -1873497444986108928L, 2047L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_17 = new BitSet(_tokenSet_17_data_);
	private static final long _tokenSet_18_data_[] = { 144115188075855872L, 1729382256910270464L, 0L, 0L };
	public static final BitSet _tokenSet_18 = new BitSet(_tokenSet_18_data_);
	private static final long _tokenSet_19_data_[] = { 71987225293750272L, 1024L, 2046L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_19 = new BitSet(_tokenSet_19_data_);
	private static final long _tokenSet_20_data_[] = { 288217182012178432L, -1073717888L, 2047L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_20 = new BitSet(_tokenSet_20_data_);
	private static final long _tokenSet_21_data_[] = { 72022409665839104L, -1873497444986125312L, 2047L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_21 = new BitSet(_tokenSet_21_data_);

	}
