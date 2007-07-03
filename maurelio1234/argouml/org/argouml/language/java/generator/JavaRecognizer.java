// $ANTLR 2.7.2a2 (20020112-1): "java.g" -> "JavaRecognizer.java"$

	package org.argouml.language.java.generator;

	import java.util.Vector;

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

/** Java 1.2 Recognizer
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
 * Version 1.18 (January 07, 2002)
 *              Added CodePiece parameter to classBlock so that '{' will be
 *              added to ClassCodePiece.
 *
 * Version tracking now done with following ID:
 *
 * $Id: java.g 5374 2003-12-05 07:44:28Z lepekhine $
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

	private CodePieceCollector cpc;

	private int anonymousNumber;
	
	// A flag to indicate if we are inside a compoundStatement
	private boolean      _inCompoundStatement  = false;


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
		CodePieceCollector cpc
	) throws RecognitionException, TokenStreamException {
		
		this.cpc = cpc;
		
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
		case JAVADOC:
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_static:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case LITERAL_strictfp:
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
				if ( inputState.guessing==0 ) {
					anonymousNumber=0;
				}
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
		
		Token  t1 = null;
		Token  t2 = null;
		CompositeCodePiece comp=null; CodePiece i=null;
		
		t1 = LT(1);
		match(LITERAL_package);
		if ( inputState.guessing==0 ) {
			comp = new CompositeCodePiece(new SimpleCodePiece(t1));
		}
		i=identifier();
		if ( inputState.guessing==0 ) {
			comp.add(i);
		}
		t2 = LT(1);
		match(SEMI);
		if ( inputState.guessing==0 ) {
			comp.add(new SimpleCodePiece(t2));
					 cpc.add(new PackageCodePiece(comp));
		}
	}
	
	public final void importDefinition() throws RecognitionException, TokenStreamException {
		
		Token  i = null;
		
		i = LT(1);
		match(LITERAL_import);
		identifierStar();
		match(SEMI);
	}
	
	public final void typeDefinition() throws RecognitionException, TokenStreamException {
		
		Token  c = null;
		CodePiece m=null; CompositeCodePiece pre=null;
		
		switch ( LA(1)) {
		case FINAL:
		case ABSTRACT:
		case JAVADOC:
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_static:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case LITERAL_strictfp:
		case LITERAL_class:
		case LITERAL_interface:
		{
			{
			_loop11:
			do {
				if ((LA(1)==JAVADOC)) {
					c = LT(1);
					match(JAVADOC);
					if ( inputState.guessing==0 ) {
						pre = new CompositeCodePiece(
											new SimpleCodePiece(c));
					}
				}
				else {
					break _loop11;
				}
				
			} while (true);
			}
			m=modifiers();
			if ( inputState.guessing==0 ) {
				if(pre == null) pre = new CompositeCodePiece(m); 
								else pre.add(m);
			}
			{
			switch ( LA(1)) {
			case LITERAL_class:
			{
				classDefinition(pre);
				break;
			}
			case LITERAL_interface:
			{
				interfaceDefinition(pre);
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
	
	public final CompositeCodePiece  identifier() throws RecognitionException, TokenStreamException {
		CompositeCodePiece codePiece=null;
		
		Token  t1 = null;
		Token  t2 = null;
		Token  t3 = null;
		
		t1 = LT(1);
		match(IDENT);
		if ( inputState.guessing==0 ) {
			codePiece = new CompositeCodePiece(new SimpleCodePiece(t1));
		}
		{
		_loop28:
		do {
			if ((LA(1)==DOT)) {
				t2 = LT(1);
				match(DOT);
				if ( inputState.guessing==0 ) {
					codePiece.add(new SimpleCodePiece(t2));
				}
				t3 = LT(1);
				match(IDENT);
				if ( inputState.guessing==0 ) {
					codePiece.add(new SimpleCodePiece(t3));
				}
			}
			else {
				break _loop28;
			}
			
		} while (true);
		}
		return codePiece;
	}
	
	public final void identifierStar() throws RecognitionException, TokenStreamException {
		
		
		match(IDENT);
		{
		_loop31:
		do {
			if ((LA(1)==DOT) && (LA(2)==IDENT)) {
				match(DOT);
				match(IDENT);
			}
			else {
				break _loop31;
			}
			
		} while (true);
		}
		{
		switch ( LA(1)) {
		case DOT:
		{
			match(DOT);
			match(STAR);
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
	
	public final CompositeCodePiece  modifiers() throws RecognitionException, TokenStreamException {
		CompositeCodePiece codePiece=null;
		
		CodePiece cp=null;
		
		{
		_loop16:
		do {
			if ((_tokenSet_1.member(LA(1)))) {
				cp=modifier();
				if ( inputState.guessing==0 ) {
					if(codePiece == null) {
								codePiece = new CompositeCodePiece(cp);
							  }
							  else {	
								codePiece.add(cp);
							  }
							
				}
			}
			else {
				break _loop16;
			}
			
		} while (true);
		}
		return codePiece;
	}
	
	public final void classDefinition(
		CodePiece preCode
	) throws RecognitionException, TokenStreamException {
		
		Token  t1 = null;
		Token  t2 = null;
		CodePiece sc=null, ic=null; 
			 CompositeCodePiece codePiece = new CompositeCodePiece(preCode);
		
		t1 = LT(1);
		match(LITERAL_class);
		if ( inputState.guessing==0 ) {
			if(!_inCompoundStatement) {codePiece.add(new SimpleCodePiece(t1));}
		}
		t2 = LT(1);
		match(IDENT);
		if ( inputState.guessing==0 ) {
			if(!_inCompoundStatement) {codePiece.add(new SimpleCodePiece(t2));}
		}
		sc=superClassClause();
		if ( inputState.guessing==0 ) {
			codePiece.add(sc);
		}
		ic=implementsClause();
		if ( inputState.guessing==0 ) {
			if(!_inCompoundStatement) {codePiece.add(ic);
					 cpc.add(new ClassCodePiece(codePiece, t2.getText().toString()));}
		}
		classBlock(codePiece);
	}
	
	public final void interfaceDefinition(
		CodePiece preCode
	) throws RecognitionException, TokenStreamException {
		
		Token  t1 = null;
		Token  t2 = null;
		CodePiece ie=null;
			 CompositeCodePiece codePiece = new CompositeCodePiece(preCode);
		
		t1 = LT(1);
		match(LITERAL_interface);
		if ( inputState.guessing==0 ) {
			codePiece.add(new SimpleCodePiece(t1));
		}
		t2 = LT(1);
		match(IDENT);
		if ( inputState.guessing==0 ) {
			codePiece.add(new SimpleCodePiece(t2));
		}
		ie=interfaceExtends();
		if ( inputState.guessing==0 ) {
			codePiece.add(ie);
					 cpc.add(new InterfaceCodePiece(codePiece, t2.getText().toString()));
		}
		classBlock(codePiece);
	}
	
/** A declaration is the creation of a reference or primitive-type variable
 *  Create a separate Type/Var tree for each var in the var list.
 */
	public final void declaration() throws RecognitionException, TokenStreamException {
		
		
		modifiers();
		typeSpec();
		variableDefinitions();
	}
	
	public final CodePiece  typeSpec() throws RecognitionException, TokenStreamException {
		CodePiece cp=null;
		
		
		switch ( LA(1)) {
		case IDENT:
		{
			cp=classTypeSpec();
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
			cp=builtInTypeSpec();
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return cp;
	}
	
	public final Vector  variableDefinitions() throws RecognitionException, TokenStreamException {
		Vector codePieces=new Vector();
		
		CodePiece v1=null, v2=null;
		
		v1=variableDeclarator();
		if ( inputState.guessing==0 ) {
			codePieces.addElement(v1);
		}
		{
		_loop69:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				v2=variableDeclarator();
				if ( inputState.guessing==0 ) {
					codePieces.addElement(v2);
				}
			}
			else {
				break _loop69;
			}
			
		} while (true);
		}
		return codePieces;
	}
	
	public final CodePiece  modifier() throws RecognitionException, TokenStreamException {
		CodePiece codePiece=null;
		
		Token  t1 = null;
		Token  t2 = null;
		Token  t3 = null;
		Token  t4 = null;
		Token  t5 = null;
		Token  t6 = null;
		Token  t7 = null;
		Token  t8 = null;
		Token  t10 = null;
		Token  t12 = null;
		Token  t13 = null;
		
		switch ( LA(1)) {
		case LITERAL_private:
		{
			{
			t1 = LT(1);
			match(LITERAL_private);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t1);
			}
			}
			break;
		}
		case LITERAL_public:
		{
			{
			t2 = LT(1);
			match(LITERAL_public);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t2);
			}
			}
			break;
		}
		case LITERAL_protected:
		{
			{
			t3 = LT(1);
			match(LITERAL_protected);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t3);
			}
			}
			break;
		}
		case LITERAL_static:
		{
			{
			t4 = LT(1);
			match(LITERAL_static);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t4);
			}
			}
			break;
		}
		case LITERAL_transient:
		{
			{
			t5 = LT(1);
			match(LITERAL_transient);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t5);
			}
			}
			break;
		}
		case FINAL:
		{
			{
			t6 = LT(1);
			match(FINAL);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t6);
			}
			}
			break;
		}
		case ABSTRACT:
		{
			{
			t7 = LT(1);
			match(ABSTRACT);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t7);
			}
			}
			break;
		}
		case LITERAL_native:
		{
			{
			t8 = LT(1);
			match(LITERAL_native);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t8);
			}
			}
			break;
		}
		case LITERAL_synchronized:
		{
			{
			t10 = LT(1);
			match(LITERAL_synchronized);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t10);
			}
			}
			break;
		}
		case LITERAL_volatile:
		{
			{
			t12 = LT(1);
			match(LITERAL_volatile);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t12);
			}
			}
			break;
		}
		case LITERAL_strictfp:
		{
			{
			t13 = LT(1);
			match(LITERAL_strictfp);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t13);
			}
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return codePiece;
	}
	
	public final CompositeCodePiece  classTypeSpec() throws RecognitionException, TokenStreamException {
		CompositeCodePiece cp=null;
		
		Token  t1 = null;
		Token  t2 = null;
		CodePiece i=null;
		
		i=identifier();
		if ( inputState.guessing==0 ) {
			cp = new CompositeCodePiece(i);
		}
		{
		_loop20:
		do {
			if ((LA(1)==LBRACK)) {
				t1 = LT(1);
				match(LBRACK);
				if ( inputState.guessing==0 ) {
					cp.add(new SimpleCodePiece(t1));
				}
				t2 = LT(1);
				match(RBRACK);
				if ( inputState.guessing==0 ) {
					cp.add(new SimpleCodePiece(t2));
				}
			}
			else {
				break _loop20;
			}
			
		} while (true);
		}
		return cp;
	}
	
	public final CompositeCodePiece  builtInTypeSpec() throws RecognitionException, TokenStreamException {
		CompositeCodePiece cp=null;
		
		Token  t1 = null;
		Token  t2 = null;
		CodePiece i=null;
		
		i=builtInType();
		if ( inputState.guessing==0 ) {
			cp = new CompositeCodePiece(i);
		}
		{
		_loop23:
		do {
			if ((LA(1)==LBRACK)) {
				t1 = LT(1);
				match(LBRACK);
				if ( inputState.guessing==0 ) {
					cp.add(new SimpleCodePiece(t1));
				}
				t2 = LT(1);
				match(RBRACK);
				if ( inputState.guessing==0 ) {
					cp.add(new SimpleCodePiece(t2));
				}
			}
			else {
				break _loop23;
			}
			
		} while (true);
		}
		return cp;
	}
	
	public final SimpleCodePiece  builtInType() throws RecognitionException, TokenStreamException {
		SimpleCodePiece cp=null;
		
		Token  t1 = null;
		Token  t2 = null;
		Token  t3 = null;
		Token  t4 = null;
		Token  t5 = null;
		Token  t6 = null;
		Token  t7 = null;
		Token  t8 = null;
		Token  t9 = null;
		
		switch ( LA(1)) {
		case LITERAL_void:
		{
			t1 = LT(1);
			match(LITERAL_void);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t1);
			}
			break;
		}
		case LITERAL_boolean:
		{
			t2 = LT(1);
			match(LITERAL_boolean);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t2);
			}
			break;
		}
		case LITERAL_byte:
		{
			t3 = LT(1);
			match(LITERAL_byte);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t3);
			}
			break;
		}
		case LITERAL_char:
		{
			t4 = LT(1);
			match(LITERAL_char);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t4);
			}
			break;
		}
		case LITERAL_short:
		{
			t5 = LT(1);
			match(LITERAL_short);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t5);
			}
			break;
		}
		case LITERAL_int:
		{
			t6 = LT(1);
			match(LITERAL_int);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t6);
			}
			break;
		}
		case LITERAL_float:
		{
			t7 = LT(1);
			match(LITERAL_float);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t7);
			}
			break;
		}
		case LITERAL_long:
		{
			t8 = LT(1);
			match(LITERAL_long);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t8);
			}
			break;
		}
		case LITERAL_double:
		{
			t9 = LT(1);
			match(LITERAL_double);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t9);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return cp;
	}
	
	public final CodePiece  type() throws RecognitionException, TokenStreamException {
		CodePiece cp=null;
		
		
		switch ( LA(1)) {
		case IDENT:
		{
			cp=identifier();
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
			cp=builtInType();
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return cp;
	}
	
	public final CompositeCodePiece  superClassClause() throws RecognitionException, TokenStreamException {
		CompositeCodePiece codePiece=null;
		
		Token  t1 = null;
		CodePiece id=null;
		
		{
		switch ( LA(1)) {
		case LITERAL_extends:
		{
			t1 = LT(1);
			match(LITERAL_extends);
			if ( inputState.guessing==0 ) {
				codePiece = new CompositeCodePiece(new SimpleCodePiece(t1));
			}
			id=identifier();
			if ( inputState.guessing==0 ) {
				codePiece.add(id);
			}
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
		return codePiece;
	}
	
	public final CompositeCodePiece  implementsClause() throws RecognitionException, TokenStreamException {
		CompositeCodePiece codePiece=null;
		
		Token  t1 = null;
		Token  t2 = null;
		CodePiece id1=null, id2=null;
		
		{
		switch ( LA(1)) {
		case LITERAL_implements:
		{
			t1 = LT(1);
			match(LITERAL_implements);
			if ( inputState.guessing==0 ) {
				codePiece = new CompositeCodePiece(new SimpleCodePiece(t1));
			}
			id1=identifier();
			if ( inputState.guessing==0 ) {
				codePiece.add(id1);
			}
			{
			_loop59:
			do {
				if ((LA(1)==COMMA)) {
					t2 = LT(1);
					match(COMMA);
					if ( inputState.guessing==0 ) {
						codePiece.add(new SimpleCodePiece(t2));
					}
					id2=identifier();
					if ( inputState.guessing==0 ) {
						codePiece.add(id2);
					}
				}
				else {
					break _loop59;
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
		return codePiece;
	}
	
	public final void classBlock(
		CompositeCodePiece header
	) throws RecognitionException, TokenStreamException {
		
		Token  t0 = null;
		Token  t1 = null;
		
		t0 = LT(1);
		match(LCURLY);
		if ( inputState.guessing==0 ) {
			if (header != null) {header.add (new SimpleCodePiece (t0));}
		}
		{
		_loop51:
		do {
			switch ( LA(1)) {
			case FINAL:
			case ABSTRACT:
			case JAVADOC:
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
			case LITERAL_synchronized:
			case LITERAL_volatile:
			case LITERAL_strictfp:
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
				break _loop51;
			}
			}
		} while (true);
		}
		t1 = LT(1);
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			if(!_inCompoundStatement) {cpc.add(new ClassifierEndCodePiece(new SimpleCodePiece(t1)));}
		}
	}
	
	public final CompositeCodePiece  interfaceExtends() throws RecognitionException, TokenStreamException {
		CompositeCodePiece codePiece=null;
		
		Token  t1 = null;
		Token  t2 = null;
		CodePiece id1=null, id2=null;
		
		{
		switch ( LA(1)) {
		case LITERAL_extends:
		{
			t1 = LT(1);
			match(LITERAL_extends);
			if ( inputState.guessing==0 ) {
				codePiece = new CompositeCodePiece(new SimpleCodePiece(t1));
			}
			id1=identifier();
			if ( inputState.guessing==0 ) {
				codePiece.add(id1);
			}
			{
			_loop55:
			do {
				if ((LA(1)==COMMA)) {
					t2 = LT(1);
					match(COMMA);
					if ( inputState.guessing==0 ) {
						codePiece.add(new SimpleCodePiece(t2));
					}
					id2=identifier();
					if ( inputState.guessing==0 ) {
						codePiece.add(id1);
					}
				}
				else {
					break _loop55;
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
		return codePiece;
	}
	
	public final void field() throws RecognitionException, TokenStreamException {
		
		Token  c = null;
		Token  t1 = null;
		Token  t2 = null;
		Token  t3 = null;
		CodePiece m=null, t=null, rb=null, pdl=null; 
			  Vector vd=null;
			  CompositeCodePiece cp=null, 
						doccp=null,
						composite=null;
		
		if ((_tokenSet_2.member(LA(1))) && (_tokenSet_3.member(LA(2)))) {
			{
			_loop62:
			do {
				if ((LA(1)==JAVADOC)) {
					c = LT(1);
					match(JAVADOC);
					if ( inputState.guessing==0 ) {
						composite = doccp = new CompositeCodePiece(
											new SimpleCodePiece(c));
					}
				}
				else {
					break _loop62;
				}
				
			} while (true);
			}
			m=modifiers();
			if ( inputState.guessing==0 ) {
				cp = new CompositeCodePiece(m); 
								if(composite == null) 
									composite = 
										(m==null)?
										null:
										new CompositeCodePiece(m); 
								else composite.add(m);
			}
			{
			switch ( LA(1)) {
			case LITERAL_class:
			{
				classDefinition(composite);
				break;
			}
			case LITERAL_interface:
			{
				interfaceDefinition(composite);
				break;
			}
			default:
				if ((LA(1)==IDENT) && (LA(2)==LPAREN)) {
					ctorHead(doccp, cp);
					compoundStatement();
				}
				else if (((LA(1) >= LITERAL_void && LA(1) <= IDENT)) && (_tokenSet_4.member(LA(2)))) {
					t=typeSpec();
					{
					if ((LA(1)==IDENT) && (LA(2)==LPAREN)) {
						t1 = LT(1);
						match(IDENT);
						t2 = LT(1);
						match(LPAREN);
						pdl=parameterDeclarationList();
						t3 = LT(1);
						match(RPAREN);
						rb=returnTypeBrackersOnEndOfMethodHead();
						if ( inputState.guessing==0 ) {
							cp.add(t);
											 cp.add(new SimpleCodePiece(t1));
											 cp.add(new SimpleCodePiece(t2));
											 cp.add(pdl);
											 cp.add(new SimpleCodePiece(t3));
											 cp.add(rb);
											 cpc.add(new OperationCodePiece(
													doccp,
													cp, 
													t1.getText().toString()));
											
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
					else if ((LA(1)==IDENT) && (_tokenSet_5.member(LA(2)))) {
						vd=variableDefinitions();
						match(SEMI);
						if ( inputState.guessing==0 ) {
							cpc.add(new AttributeCodePiece(composite, t, vd));
						}
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
		CompositeCodePiece doccp, CompositeCodePiece cp
	) throws RecognitionException, TokenStreamException {
		
		Token  t1 = null;
		Token  t2 = null;
		CodePiece pdl=null;
		
		t1 = LT(1);
		match(IDENT);
		match(LPAREN);
		pdl=parameterDeclarationList();
		t2 = LT(1);
		match(RPAREN);
		if ( inputState.guessing==0 ) {
			cp.add(new SimpleCodePiece(t1));
					 cp.add(pdl);
					 cp.add(new SimpleCodePiece(t2));
					 cpc.add(new OperationCodePiece(doccp, cp, t1.getText().toString()));
					
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
		
		
		match(LCURLY);
		if ( inputState.guessing==0 ) {
			_inCompoundStatement = true;
		}
		{
		_loop102:
		do {
			if ((_tokenSet_6.member(LA(1)))) {
				statement();
			}
			else {
				break _loop102;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			_inCompoundStatement = false;
		}
		match(RCURLY);
	}
	
	public final CompositeCodePiece  parameterDeclarationList() throws RecognitionException, TokenStreamException {
		CompositeCodePiece cp=null;
		
		Token  t1 = null;
		CodePiece pd=null;
		
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
			pd=parameterDeclaration();
			if ( inputState.guessing==0 ) {
				cp = new CompositeCodePiece(pd);
			}
			{
			_loop93:
			do {
				if ((LA(1)==COMMA)) {
					t1 = LT(1);
					match(COMMA);
					if ( inputState.guessing==0 ) {
						cp.add(new SimpleCodePiece(t1));
					}
					pd=parameterDeclaration();
					if ( inputState.guessing==0 ) {
						cp.add(pd);
					}
				}
				else {
					break _loop93;
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
		return cp;
	}
	
	public final CompositeCodePiece  returnTypeBrackersOnEndOfMethodHead() throws RecognitionException, TokenStreamException {
		CompositeCodePiece cp=null;
		
		Token  t1 = null;
		Token  t2 = null;
		
		{
		_loop89:
		do {
			if ((LA(1)==LBRACK)) {
				t1 = LT(1);
				match(LBRACK);
				if ( inputState.guessing==0 ) {
					if(cp==null)
								cp = new CompositeCodePiece(
									new SimpleCodePiece(t1));
							else
								cp.add(new SimpleCodePiece(t1));
							
				}
				t2 = LT(1);
				match(RBRACK);
				if ( inputState.guessing==0 ) {
					cp.add(new SimpleCodePiece(t2));
				}
			}
			else {
				break _loop89;
			}
			
		} while (true);
		}
		return cp;
	}
	
	public final void throwsClause() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_throws);
		identifier();
		{
		_loop86:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				identifier();
			}
			else {
				break _loop86;
			}
			
		} while (true);
		}
	}
	
/** Declaration of a variable.  This can be a class/instance variable,
 *   or a local variable in a method
 * It can also include possible initialization.
 */
	public final CompositeCodePiece  variableDeclarator() throws RecognitionException, TokenStreamException {
		CompositeCodePiece cp=null;
		
		Token  t1 = null;
		CodePiece db=null, vin=null;
		
		t1 = LT(1);
		match(IDENT);
		if ( inputState.guessing==0 ) {
			cp = new CompositeCodePiece(
							new SimpleCodePiece(t1));
		}
		db=declaratorBrackets();
		if ( inputState.guessing==0 ) {
			cp.add(db);
		}
		vin=varInitializer();
		if ( inputState.guessing==0 ) {
			if (vin!=null) cp.add(vin);
		}
		return cp;
	}
	
	public final CompositeCodePiece  declaratorBrackets() throws RecognitionException, TokenStreamException {
		CompositeCodePiece cp=null;
		
		Token  t1 = null;
		Token  t2 = null;
		
		{
		_loop73:
		do {
			if ((LA(1)==LBRACK)) {
				t1 = LT(1);
				match(LBRACK);
				t2 = LT(1);
				match(RBRACK);
				if ( inputState.guessing==0 ) {
					
								if(cp == null)
									cp = new CompositeCodePiece(
										new SimpleCodePiece(t1));
								else
									cp.add(new SimpleCodePiece(t1));
								cp.add(new SimpleCodePiece(t2));
							
				}
			}
			else {
				break _loop73;
			}
			
		} while (true);
		}
		return cp;
	}
	
	public final CompositeCodePiece  varInitializer() throws RecognitionException, TokenStreamException {
		CompositeCodePiece cp=null;
		
		Token  t1 = null;
		
		{
		switch ( LA(1)) {
		case ASSIGN:
		{
			t1 = LT(1);
			match(ASSIGN);
			if ( inputState.guessing==0 ) {
				
				//cp = new CompositeCodePiece(
								//new SimpleCodePiece(t1));
				
			}
			initializer();
			if ( inputState.guessing==0 ) {
				
				t1=LT(1);
				cp = new CompositeCodePiece(new SimpleCodePiece(
							new StringBuffer(""), 
							t1.getLine()-1,
							t1.getColumn()-1,
							t1.getColumn()-1));
				
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
		return cp;
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
			_loop79:
			do {
				if ((LA(1)==COMMA) && (_tokenSet_7.member(LA(2)))) {
					match(COMMA);
					initializer();
				}
				else {
					break _loop79;
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
	
	public final CompositeCodePiece  parameterDeclaration() throws RecognitionException, TokenStreamException {
		CompositeCodePiece cp=null;
		
		Token  t1 = null;
		CodePiece ts=null;
		
		parameterModifier();
		ts=typeSpec();
		if ( inputState.guessing==0 ) {
			cp = new CompositeCodePiece(ts);
		}
		t1 = LT(1);
		match(IDENT);
		if ( inputState.guessing==0 ) {
			cp.add(new SimpleCodePiece(t1));
		}
		parameterDeclaratorBrackets();
		return cp;
	}
	
	public final void parameterModifier() throws RecognitionException, TokenStreamException {
		
		
		{
		switch ( LA(1)) {
		case FINAL:
		{
			match(FINAL);
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
	}
	
	public final void parameterDeclaratorBrackets() throws RecognitionException, TokenStreamException {
		
		
		{
		_loop97:
		do {
			if ((LA(1)==LBRACK)) {
				match(LBRACK);
				match(RBRACK);
			}
			else {
				break _loop97;
			}
			
		} while (true);
		}
	}
	
	public final void statement() throws RecognitionException, TokenStreamException {
		
		Token  t1 = null;
		Token  t2 = null;
		
		switch ( LA(1)) {
		case LCURLY:
		{
			compoundStatement();
			break;
		}
		case LITERAL_class:
		{
			classDefinition(null);
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
			if ((LA(1)==LITERAL_else) && (_tokenSet_6.member(LA(2)))) {
				match(LITERAL_else);
				statement();
			}
			else if ((_tokenSet_8.member(LA(1))) && (_tokenSet_9.member(LA(2)))) {
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
			_loop111:
			do {
				if ((LA(1)==LITERAL_case||LA(1)==LITERAL_default)) {
					casesGroup();
				}
				else {
					break _loop111;
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
				t1 = LT(1);
				match(FINAL);
				classDefinition(new SimpleCodePiece(t1));
			}
			else if ((LA(1)==ABSTRACT) && (LA(2)==LITERAL_class)) {
				t2 = LT(1);
				match(ABSTRACT);
				classDefinition(new SimpleCodePiece(t2));
			}
			else {
				boolean synPredMatched105 = false;
				if (((_tokenSet_10.member(LA(1))) && (_tokenSet_11.member(LA(2))))) {
					int _m105 = mark();
					synPredMatched105 = true;
					inputState.guessing++;
					try {
						{
						declaration();
						}
					}
					catch (RecognitionException pe) {
						synPredMatched105 = false;
					}
					rewind(_m105);
					inputState.guessing--;
				}
				if ( synPredMatched105 ) {
					declaration();
					match(SEMI);
				}
				else if ((_tokenSet_12.member(LA(1))) && (_tokenSet_13.member(LA(2)))) {
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
		boolean synPredMatched123 = false;
		if (((_tokenSet_10.member(LA(1))) && (_tokenSet_11.member(LA(2))))) {
			int _m123 = mark();
			synPredMatched123 = true;
			inputState.guessing++;
			try {
				{
				declaration();
				}
			}
			catch (RecognitionException pe) {
				synPredMatched123 = false;
			}
			rewind(_m123);
			inputState.guessing--;
		}
		if ( synPredMatched123 ) {
			declaration();
		}
		else if ((_tokenSet_12.member(LA(1))) && (_tokenSet_14.member(LA(2)))) {
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
		int _cnt114=0;
		_loop114:
		do {
			if ((LA(1)==LITERAL_case||LA(1)==LITERAL_default) && (_tokenSet_15.member(LA(2)))) {
				aCase();
			}
			else {
				if ( _cnt114>=1 ) { break _loop114; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt114++;
		} while (true);
		}
		caseSList();
	}
	
	public final void tryBlock() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_try);
		compoundStatement();
		{
		_loop130:
		do {
			if ((LA(1)==LITERAL_catch)) {
				handler();
			}
			else {
				break _loop130;
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
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case LITERAL_strictfp:
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
		_loop119:
		do {
			if ((_tokenSet_6.member(LA(1)))) {
				statement();
			}
			else {
				break _loop119;
			}
			
		} while (true);
		}
	}
	
	public final void expressionList() throws RecognitionException, TokenStreamException {
		
		
		expression();
		{
		_loop136:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				expression();
			}
			else {
				break _loop136;
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
		_loop144:
		do {
			if ((LA(1)==LOR)) {
				match(LOR);
				logicalAndExpression();
			}
			else {
				break _loop144;
			}
			
		} while (true);
		}
	}
	
	public final void logicalAndExpression() throws RecognitionException, TokenStreamException {
		
		
		inclusiveOrExpression();
		{
		_loop147:
		do {
			if ((LA(1)==LAND)) {
				match(LAND);
				inclusiveOrExpression();
			}
			else {
				break _loop147;
			}
			
		} while (true);
		}
	}
	
	public final void inclusiveOrExpression() throws RecognitionException, TokenStreamException {
		
		
		exclusiveOrExpression();
		{
		_loop150:
		do {
			if ((LA(1)==BOR)) {
				match(BOR);
				exclusiveOrExpression();
			}
			else {
				break _loop150;
			}
			
		} while (true);
		}
	}
	
	public final void exclusiveOrExpression() throws RecognitionException, TokenStreamException {
		
		
		andExpression();
		{
		_loop153:
		do {
			if ((LA(1)==BXOR)) {
				match(BXOR);
				andExpression();
			}
			else {
				break _loop153;
			}
			
		} while (true);
		}
	}
	
	public final void andExpression() throws RecognitionException, TokenStreamException {
		
		
		equalityExpression();
		{
		_loop156:
		do {
			if ((LA(1)==BAND)) {
				match(BAND);
				equalityExpression();
			}
			else {
				break _loop156;
			}
			
		} while (true);
		}
	}
	
	public final void equalityExpression() throws RecognitionException, TokenStreamException {
		
		
		relationalExpression();
		{
		_loop160:
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
				break _loop160;
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
			_loop165:
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
					break _loop165;
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
		_loop169:
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
				break _loop169;
			}
			
		} while (true);
		}
	}
	
	public final void additiveExpression() throws RecognitionException, TokenStreamException {
		
		
		multiplicativeExpression();
		{
		_loop173:
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
				break _loop173;
			}
			
		} while (true);
		}
	}
	
	public final void multiplicativeExpression() throws RecognitionException, TokenStreamException {
		
		
		unaryExpression();
		{
		_loop177:
		do {
			if ((_tokenSet_16.member(LA(1)))) {
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
				break _loop177;
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
				boolean synPredMatched182 = false;
				if (((LA(1)==LPAREN) && (LA(2)==IDENT))) {
					int _m182 = mark();
					synPredMatched182 = true;
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
						synPredMatched182 = false;
					}
					rewind(_m182);
					inputState.guessing--;
				}
				if ( synPredMatched182 ) {
					match(LPAREN);
					classTypeSpec();
					match(RPAREN);
					unaryExpressionNotPlusMinus();
				}
				else if ((_tokenSet_17.member(LA(1))) && (_tokenSet_18.member(LA(2)))) {
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
		
		
		primaryExpression();
		{
		_loop189:
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
					int _cnt188=0;
					_loop188:
					do {
						if ((LA(1)==LBRACK)) {
							match(LBRACK);
							match(RBRACK);
						}
						else {
							if ( _cnt188>=1 ) { break _loop188; } else {throw new NoViableAltException(LT(1), getFilename());}
						}
						
						_cnt188++;
					} while (true);
					}
					match(DOT);
					match(LITERAL_class);
				}
				else if ((LA(1)==LBRACK) && (_tokenSet_12.member(LA(2)))) {
					match(LBRACK);
					expression();
					match(RBRACK);
				}
			else {
				break _loop189;
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
			_loop193:
			do {
				if ((LA(1)==LBRACK)) {
					match(LBRACK);
					match(RBRACK);
				}
				else {
					break _loop193;
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
		
		CodePiece t=null;
		
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
					cpc.add(new AnonymousClassCodePiece(
										t, 
										++anonymousNumber));
				}
				classBlock(null);
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
		int _cnt203=0;
		_loop203:
		do {
			if ((LA(1)==LBRACK) && (_tokenSet_19.member(LA(2)))) {
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
				if ( _cnt203>=1 ) { break _loop203; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt203++;
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
		"JAVADOC",
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
		"\"synchronized\"",
		"\"volatile\"",
		"\"strictfp\"",
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
		"ML_COMMENT",
		"ESC",
		"HEX_DIGIT",
		"VOCAB",
		"EXPONENT",
		"FLOAT_SUFFIX"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { -576437112803426304L, 95L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { -576459103035981824L, 15L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { -432467060262436864L, 95L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { -288316687814492160L, 2143L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 216207966485872640L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 39582418599936L, 8704L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { -432480254401970176L, -3746994889636902753L, 4095L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 143974450587500544L, -3746994889972250496L, 4095L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { -432480254401970176L, -3746994889435444833L, 4095L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { -81913616269312L, -22049L, 4095L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { -432484652448481280L, 15L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { -288334280000536576L, 15L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 143974450587500544L, -3746994889972250624L, 4095L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { 576359597233668096L, -2147473408L, 4095L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 576359597233668096L, -2147472896L, 4095L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { 143974450587500544L, -3746994889972217856L, 4095L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { 288230376151711744L, 3458764513820540928L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { 143974450587500544L, 2048L, 4092L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { 576429965977845760L, -2147435776L, 4095L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = { 144044819331678208L, -3746994889972250624L, 4095L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	
	}
