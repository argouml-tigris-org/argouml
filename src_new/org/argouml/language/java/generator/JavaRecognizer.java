// $ANTLR 2.7.1: "java.g" -> "JavaRecognizer.java"$

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
import antlr.collections.AST;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

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

	private CodePieceCollector cpc;

	private int anonymousNumber;

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

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST compilationUnit_AST = null;
		this.cpc = cpc;

		{
		switch ( LA(1)) {
		case LITERAL_package:
		{
			packageDefinition();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
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
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
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
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop6;
			}

		} while (true);
		}
		AST tmp1_AST = null;
		if (inputState.guessing==0) {
			tmp1_AST = (AST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp1_AST);
		}
		match(Token.EOF_TYPE);
		compilationUnit_AST = (AST)currentAST.root;
		returnAST = compilationUnit_AST;
	}

	public final void packageDefinition() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST packageDefinition_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		Token  t2 = null;
		AST t2_AST = null;
		CompositeCodePiece comp=null; CodePiece i=null;

		t1 = LT(1);
		if (inputState.guessing==0) {
			t1_AST = (AST)astFactory.create(t1);
			astFactory.addASTChild(currentAST, t1_AST);
		}
		match(LITERAL_package);
		if ( inputState.guessing==0 ) {
			comp = new CompositeCodePiece(new SimpleCodePiece(t1));
		}
		i=identifier();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			comp.add(i);
		}
		t2 = LT(1);
		if (inputState.guessing==0) {
			t2_AST = (AST)astFactory.create(t2);
			astFactory.addASTChild(currentAST, t2_AST);
		}
		match(SEMI);
		if ( inputState.guessing==0 ) {
			comp.add(new SimpleCodePiece(t2));
					 cpc.add(new PackageCodePiece(comp));
		}
		packageDefinition_AST = (AST)currentAST.root;
		returnAST = packageDefinition_AST;
	}

	public final void importDefinition() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST importDefinition_AST = null;
		Token  i = null;
		AST i_AST = null;

		i = LT(1);
		if (inputState.guessing==0) {
			i_AST = (AST)astFactory.create(i);
			astFactory.addASTChild(currentAST, i_AST);
		}
		match(LITERAL_import);
		identifierStar();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		AST tmp2_AST = null;
		if (inputState.guessing==0) {
			tmp2_AST = (AST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp2_AST);
		}
		match(SEMI);
		importDefinition_AST = (AST)currentAST.root;
		returnAST = importDefinition_AST;
	}

	public final void typeDefinition() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeDefinition_AST = null;
		Token  c = null;
		AST c_AST = null;
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
					if (inputState.guessing==0) {
						c_AST = (AST)astFactory.create(c);
						astFactory.addASTChild(currentAST, c_AST);
					}
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
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				if(pre == null) pre = new CompositeCodePiece(m);
								else pre.add(m);
			}
			{
			switch ( LA(1)) {
			case LITERAL_class:
			{
				classDefinition(pre);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				break;
			}
			case LITERAL_interface:
			{
				interfaceDefinition(pre);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			typeDefinition_AST = (AST)currentAST.root;
			break;
		}
		case SEMI:
		{
			AST tmp3_AST = null;
			if (inputState.guessing==0) {
				tmp3_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp3_AST);
			}
			match(SEMI);
			typeDefinition_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = typeDefinition_AST;
	}

	public final CompositeCodePiece  identifier() throws RecognitionException, TokenStreamException {
		CompositeCodePiece codePiece=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST identifier_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		Token  t2 = null;
		AST t2_AST = null;
		Token  t3 = null;
		AST t3_AST = null;

		t1 = LT(1);
		if (inputState.guessing==0) {
			t1_AST = (AST)astFactory.create(t1);
			astFactory.addASTChild(currentAST, t1_AST);
		}
		match(IDENT);
		if ( inputState.guessing==0 ) {
			codePiece = new CompositeCodePiece(new SimpleCodePiece(t1));
		}
		{
		_loop28:
		do {
			if ((LA(1)==DOT)) {
				t2 = LT(1);
				if (inputState.guessing==0) {
					t2_AST = (AST)astFactory.create(t2);
					astFactory.addASTChild(currentAST, t2_AST);
				}
				match(DOT);
				if ( inputState.guessing==0 ) {
					codePiece.add(new SimpleCodePiece(t2));
				}
				t3 = LT(1);
				if (inputState.guessing==0) {
					t3_AST = (AST)astFactory.create(t3);
					astFactory.addASTChild(currentAST, t3_AST);
				}
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
		identifier_AST = (AST)currentAST.root;
		returnAST = identifier_AST;
		return codePiece;
	}

	public final void identifierStar() throws RecognitionException, TokenStreamException {
		match(IDENT);
		{
		_loop29:
		do {
			if ((LA(1)==DOT) && (LA(2)==IDENT)) {
				match(DOT);
				match(IDENT);
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
		return;
	}

	public final CompositeCodePiece  modifiers() throws RecognitionException, TokenStreamException {
		CompositeCodePiece codePiece=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST modifiers_AST = null;
		CodePiece cp=null;

		{
		_loop16:
		do {
			if ((_tokenSet_1.member(LA(1)))) {
				cp=modifier();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
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
		modifiers_AST = (AST)currentAST.root;
		returnAST = modifiers_AST;
		return codePiece;
	}

	public final void classDefinition(
		CodePiece preCode
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classDefinition_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		Token  t2 = null;
		AST t2_AST = null;
		CodePiece sc=null, ic=null;
			 CompositeCodePiece codePiece = new CompositeCodePiece(preCode);

		t1 = LT(1);
		if (inputState.guessing==0) {
			t1_AST = (AST)astFactory.create(t1);
			astFactory.addASTChild(currentAST, t1_AST);
		}
		match(LITERAL_class);
		if ( inputState.guessing==0 ) {
			codePiece.add(new SimpleCodePiece(t1));
		}
		t2 = LT(1);
		if (inputState.guessing==0) {
			t2_AST = (AST)astFactory.create(t2);
			astFactory.addASTChild(currentAST, t2_AST);
		}
		match(IDENT);
		if ( inputState.guessing==0 ) {
			codePiece.add(new SimpleCodePiece(t2));
		}
		sc=superClassClause();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			codePiece.add(sc);
		}
		ic=implementsClause();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			codePiece.add(ic);
					 cpc.add(new ClassCodePiece(codePiece, t2.getText().toString()));
		}
		classBlock(codePiece);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		classDefinition_AST = (AST)currentAST.root;
		returnAST = classDefinition_AST;
	}

	public final void interfaceDefinition(
		CodePiece preCode
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST interfaceDefinition_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		Token  t2 = null;
		AST t2_AST = null;
		CodePiece ie=null;
			 CompositeCodePiece codePiece = new CompositeCodePiece(preCode);

		t1 = LT(1);
		if (inputState.guessing==0) {
			t1_AST = (AST)astFactory.create(t1);
			astFactory.addASTChild(currentAST, t1_AST);
		}
		match(LITERAL_interface);
		if ( inputState.guessing==0 ) {
			codePiece.add(new SimpleCodePiece(t1));
		}
		t2 = LT(1);
		if (inputState.guessing==0) {
			t2_AST = (AST)astFactory.create(t2);
			astFactory.addASTChild(currentAST, t2_AST);
		}
		match(IDENT);
		if ( inputState.guessing==0 ) {
			codePiece.add(new SimpleCodePiece(t2));
		}
		ie=interfaceExtends();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			codePiece.add(ie);
					 cpc.add(new InterfaceCodePiece(codePiece, t2.getText().toString()));
		}
		classBlock(codePiece);
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		interfaceDefinition_AST = (AST)currentAST.root;
		returnAST = interfaceDefinition_AST;
	}

/** A declaration is the creation of a reference or primitive-type variable
 *  Create a separate Type/Var tree for each var in the var list.
 */
	public final void declaration() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST declaration_AST = null;

		modifiers();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		typeSpec();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		variableDefinitions();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		declaration_AST = (AST)currentAST.root;
		returnAST = declaration_AST;
	}

	public final CodePiece  typeSpec() throws RecognitionException, TokenStreamException {
		CodePiece cp=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeSpec_AST = null;

		switch ( LA(1)) {
		case IDENT:
		{
			cp=classTypeSpec();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			typeSpec_AST = (AST)currentAST.root;
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
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			typeSpec_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = typeSpec_AST;
		return cp;
	}

	public final Vector  variableDefinitions() throws RecognitionException, TokenStreamException {
		Vector codePieces=new Vector();

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST variableDefinitions_AST = null;
		CodePiece v1=null, v2=null;

		v1=variableDeclarator();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			codePieces.addElement(v1);
		}
		{
		_loop69:
		do {
			if ((LA(1)==COMMA)) {
				AST tmp4_AST = null;
				if (inputState.guessing==0) {
					tmp4_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp4_AST);
				}
				match(COMMA);
				v2=variableDeclarator();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				if ( inputState.guessing==0 ) {
					codePieces.addElement(v2);
				}
			}
			else {
				break _loop69;
			}

		} while (true);
		}
		variableDefinitions_AST = (AST)currentAST.root;
		returnAST = variableDefinitions_AST;
		return codePieces;
	}

	public final CodePiece  modifier() throws RecognitionException, TokenStreamException {
		CodePiece codePiece=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST modifier_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		Token  t2 = null;
		AST t2_AST = null;
		Token  t3 = null;
		AST t3_AST = null;
		Token  t4 = null;
		AST t4_AST = null;
		Token  t5 = null;
		AST t5_AST = null;
		Token  t6 = null;
		AST t6_AST = null;
		Token  t7 = null;
		AST t7_AST = null;
		Token  t8 = null;
		AST t8_AST = null;
		Token  t10 = null;
		AST t10_AST = null;
		Token  t12 = null;
		AST t12_AST = null;
		Token  t13 = null;
		AST t13_AST = null;

		switch ( LA(1)) {
		case LITERAL_private:
		{
			{
			t1 = LT(1);
			if (inputState.guessing==0) {
				t1_AST = (AST)astFactory.create(t1);
				astFactory.addASTChild(currentAST, t1_AST);
			}
			match(LITERAL_private);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t1);
			}
			}
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_public:
		{
			{
			t2 = LT(1);
			if (inputState.guessing==0) {
				t2_AST = (AST)astFactory.create(t2);
				astFactory.addASTChild(currentAST, t2_AST);
			}
			match(LITERAL_public);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t2);
			}
			}
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_protected:
		{
			{
			t3 = LT(1);
			if (inputState.guessing==0) {
				t3_AST = (AST)astFactory.create(t3);
				astFactory.addASTChild(currentAST, t3_AST);
			}
			match(LITERAL_protected);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t3);
			}
			}
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_static:
		{
			{
			t4 = LT(1);
			if (inputState.guessing==0) {
				t4_AST = (AST)astFactory.create(t4);
				astFactory.addASTChild(currentAST, t4_AST);
			}
			match(LITERAL_static);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t4);
			}
			}
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_transient:
		{
			{
			t5 = LT(1);
			if (inputState.guessing==0) {
				t5_AST = (AST)astFactory.create(t5);
				astFactory.addASTChild(currentAST, t5_AST);
			}
			match(LITERAL_transient);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t5);
			}
			}
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case FINAL:
		{
			{
			t6 = LT(1);
			if (inputState.guessing==0) {
				t6_AST = (AST)astFactory.create(t6);
				astFactory.addASTChild(currentAST, t6_AST);
			}
			match(FINAL);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t6);
			}
			}
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case ABSTRACT:
		{
			{
			t7 = LT(1);
			if (inputState.guessing==0) {
				t7_AST = (AST)astFactory.create(t7);
				astFactory.addASTChild(currentAST, t7_AST);
			}
			match(ABSTRACT);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t7);
			}
			}
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_native:
		{
			{
			t8 = LT(1);
			if (inputState.guessing==0) {
				t8_AST = (AST)astFactory.create(t8);
				astFactory.addASTChild(currentAST, t8_AST);
			}
			match(LITERAL_native);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t8);
			}
			}
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_synchronized:
		{
			{
			t10 = LT(1);
			if (inputState.guessing==0) {
				t10_AST = (AST)astFactory.create(t10);
				astFactory.addASTChild(currentAST, t10_AST);
			}
			match(LITERAL_synchronized);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t10);
			}
			}
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_volatile:
		{
			{
			t12 = LT(1);
			if (inputState.guessing==0) {
				t12_AST = (AST)astFactory.create(t12);
				astFactory.addASTChild(currentAST, t12_AST);
			}
			match(LITERAL_volatile);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t12);
			}
			}
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_strictfp:
		{
			{
			t13 = LT(1);
			if (inputState.guessing==0) {
				t13_AST = (AST)astFactory.create(t13);
				astFactory.addASTChild(currentAST, t13_AST);
			}
			match(LITERAL_strictfp);
			if ( inputState.guessing==0 ) {
				codePiece = new SimpleCodePiece(t13);
			}
			}
			modifier_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = modifier_AST;
		return codePiece;
	}

	public final CompositeCodePiece  classTypeSpec() throws RecognitionException, TokenStreamException {
		CompositeCodePiece cp=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classTypeSpec_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		Token  t2 = null;
		AST t2_AST = null;
		CodePiece i=null;

		i=identifier();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			cp = new CompositeCodePiece(i);
		}
		{
		_loop20:
		do {
			if ((LA(1)==LBRACK)) {
				t1 = LT(1);
				if (inputState.guessing==0) {
					t1_AST = (AST)astFactory.create(t1);
					astFactory.addASTChild(currentAST, t1_AST);
				}
				match(LBRACK);
				if ( inputState.guessing==0 ) {
					cp.add(new SimpleCodePiece(t1));
				}
				t2 = LT(1);
				if (inputState.guessing==0) {
					t2_AST = (AST)astFactory.create(t2);
					astFactory.addASTChild(currentAST, t2_AST);
				}
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
		classTypeSpec_AST = (AST)currentAST.root;
		returnAST = classTypeSpec_AST;
		return cp;
	}

	public final CompositeCodePiece  builtInTypeSpec() throws RecognitionException, TokenStreamException {
		CompositeCodePiece cp=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST builtInTypeSpec_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		Token  t2 = null;
		AST t2_AST = null;
		CodePiece i=null;

		i=builtInType();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			cp = new CompositeCodePiece(i);
		}
		{
		_loop23:
		do {
			if ((LA(1)==LBRACK)) {
				t1 = LT(1);
				if (inputState.guessing==0) {
					t1_AST = (AST)astFactory.create(t1);
					astFactory.addASTChild(currentAST, t1_AST);
				}
				match(LBRACK);
				if ( inputState.guessing==0 ) {
					cp.add(new SimpleCodePiece(t1));
				}
				t2 = LT(1);
				if (inputState.guessing==0) {
					t2_AST = (AST)astFactory.create(t2);
					astFactory.addASTChild(currentAST, t2_AST);
				}
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
		builtInTypeSpec_AST = (AST)currentAST.root;
		returnAST = builtInTypeSpec_AST;
		return cp;
	}

	public final SimpleCodePiece  builtInType() throws RecognitionException, TokenStreamException {
		SimpleCodePiece cp=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST builtInType_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		Token  t2 = null;
		AST t2_AST = null;
		Token  t3 = null;
		AST t3_AST = null;
		Token  t4 = null;
		AST t4_AST = null;
		Token  t5 = null;
		AST t5_AST = null;
		Token  t6 = null;
		AST t6_AST = null;
		Token  t7 = null;
		AST t7_AST = null;
		Token  t8 = null;
		AST t8_AST = null;
		Token  t9 = null;
		AST t9_AST = null;

		switch ( LA(1)) {
		case LITERAL_void:
		{
			t1 = LT(1);
			if (inputState.guessing==0) {
				t1_AST = (AST)astFactory.create(t1);
				astFactory.addASTChild(currentAST, t1_AST);
			}
			match(LITERAL_void);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t1);
			}
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_boolean:
		{
			t2 = LT(1);
			if (inputState.guessing==0) {
				t2_AST = (AST)astFactory.create(t2);
				astFactory.addASTChild(currentAST, t2_AST);
			}
			match(LITERAL_boolean);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t2);
			}
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_byte:
		{
			t3 = LT(1);
			if (inputState.guessing==0) {
				t3_AST = (AST)astFactory.create(t3);
				astFactory.addASTChild(currentAST, t3_AST);
			}
			match(LITERAL_byte);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t3);
			}
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_char:
		{
			t4 = LT(1);
			if (inputState.guessing==0) {
				t4_AST = (AST)astFactory.create(t4);
				astFactory.addASTChild(currentAST, t4_AST);
			}
			match(LITERAL_char);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t4);
			}
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_short:
		{
			t5 = LT(1);
			if (inputState.guessing==0) {
				t5_AST = (AST)astFactory.create(t5);
				astFactory.addASTChild(currentAST, t5_AST);
			}
			match(LITERAL_short);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t5);
			}
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_int:
		{
			t6 = LT(1);
			if (inputState.guessing==0) {
				t6_AST = (AST)astFactory.create(t6);
				astFactory.addASTChild(currentAST, t6_AST);
			}
			match(LITERAL_int);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t6);
			}
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_float:
		{
			t7 = LT(1);
			if (inputState.guessing==0) {
				t7_AST = (AST)astFactory.create(t7);
				astFactory.addASTChild(currentAST, t7_AST);
			}
			match(LITERAL_float);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t7);
			}
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_long:
		{
			t8 = LT(1);
			if (inputState.guessing==0) {
				t8_AST = (AST)astFactory.create(t8);
				astFactory.addASTChild(currentAST, t8_AST);
			}
			match(LITERAL_long);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t8);
			}
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_double:
		{
			t9 = LT(1);
			if (inputState.guessing==0) {
				t9_AST = (AST)astFactory.create(t9);
				astFactory.addASTChild(currentAST, t9_AST);
			}
			match(LITERAL_double);
			if ( inputState.guessing==0 ) {
				cp = new SimpleCodePiece(t9);
			}
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = builtInType_AST;
		return cp;
	}

	public final CodePiece  type() throws RecognitionException, TokenStreamException {
		CodePiece cp=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST type_AST = null;

		switch ( LA(1)) {
		case IDENT:
		{
			cp=identifier();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			type_AST = (AST)currentAST.root;
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
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			type_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = type_AST;
		return cp;
	}

	public final CompositeCodePiece  superClassClause() throws RecognitionException, TokenStreamException {
		CompositeCodePiece codePiece=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST superClassClause_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		CodePiece id=null;

		{
		switch ( LA(1)) {
		case LITERAL_extends:
		{
			t1 = LT(1);
			if (inputState.guessing==0) {
				t1_AST = (AST)astFactory.create(t1);
				astFactory.addASTChild(currentAST, t1_AST);
			}
			match(LITERAL_extends);
			if ( inputState.guessing==0 ) {
				codePiece = new CompositeCodePiece(new SimpleCodePiece(t1));
			}
			id=identifier();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
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
		superClassClause_AST = (AST)currentAST.root;
		returnAST = superClassClause_AST;
		return codePiece;
	}

	public final CompositeCodePiece  implementsClause() throws RecognitionException, TokenStreamException {
		CompositeCodePiece codePiece=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST implementsClause_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		Token  t2 = null;
		AST t2_AST = null;
		CodePiece id1=null, id2=null;

		{
		switch ( LA(1)) {
		case LITERAL_implements:
		{
			t1 = LT(1);
			if (inputState.guessing==0) {
				t1_AST = (AST)astFactory.create(t1);
				astFactory.addASTChild(currentAST, t1_AST);
			}
			match(LITERAL_implements);
			if ( inputState.guessing==0 ) {
				codePiece = new CompositeCodePiece(new SimpleCodePiece(t1));
			}
			id1=identifier();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				codePiece.add(id1);
			}
			{
			_loop59:
			do {
				if ((LA(1)==COMMA)) {
					t2 = LT(1);
					if (inputState.guessing==0) {
						t2_AST = (AST)astFactory.create(t2);
						astFactory.addASTChild(currentAST, t2_AST);
					}
					match(COMMA);
					if ( inputState.guessing==0 ) {
						codePiece.add(new SimpleCodePiece(t2));
					}
					id2=identifier();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
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
		implementsClause_AST = (AST)currentAST.root;
		returnAST = implementsClause_AST;
		return codePiece;
	}

	public final void classBlock(
		CompositeCodePiece header
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classBlock_AST = null;
		Token  t0 = null;
		AST t0_AST = null;
		Token  t1 = null;
		AST t1_AST = null;

		t0 = LT(1);
		if (inputState.guessing==0) {
			t0_AST = (AST)astFactory.create(t0);
			astFactory.addASTChild(currentAST, t0_AST);
		}
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
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				break;
			}
			case SEMI:
			{
				AST tmp5_AST = null;
				if (inputState.guessing==0) {
					tmp5_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp5_AST);
				}
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
		if (inputState.guessing==0) {
			t1_AST = (AST)astFactory.create(t1);
			astFactory.addASTChild(currentAST, t1_AST);
		}
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			cpc.add(new ClassifierEndCodePiece(new SimpleCodePiece(t1)));
		}
		classBlock_AST = (AST)currentAST.root;
		returnAST = classBlock_AST;
	}

	public final CompositeCodePiece  interfaceExtends() throws RecognitionException, TokenStreamException {
		CompositeCodePiece codePiece=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST interfaceExtends_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		Token  t2 = null;
		AST t2_AST = null;
		CodePiece id1=null, id2=null;

		{
		switch ( LA(1)) {
		case LITERAL_extends:
		{
			t1 = LT(1);
			if (inputState.guessing==0) {
				t1_AST = (AST)astFactory.create(t1);
				astFactory.addASTChild(currentAST, t1_AST);
			}
			match(LITERAL_extends);
			if ( inputState.guessing==0 ) {
				codePiece = new CompositeCodePiece(new SimpleCodePiece(t1));
			}
			id1=identifier();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				codePiece.add(id1);
			}
			{
			_loop55:
			do {
				if ((LA(1)==COMMA)) {
					t2 = LT(1);
					if (inputState.guessing==0) {
						t2_AST = (AST)astFactory.create(t2);
						astFactory.addASTChild(currentAST, t2_AST);
					}
					match(COMMA);
					if ( inputState.guessing==0 ) {
						codePiece.add(new SimpleCodePiece(t2));
					}
					id2=identifier();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
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
		interfaceExtends_AST = (AST)currentAST.root;
		returnAST = interfaceExtends_AST;
		return codePiece;
	}

	public final void field() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST field_AST = null;
		Token  c = null;
		AST c_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		Token  t2 = null;
		AST t2_AST = null;
		Token  t3 = null;
		AST t3_AST = null;
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
					if (inputState.guessing==0) {
						c_AST = (AST)astFactory.create(c);
						astFactory.addASTChild(currentAST, c_AST);
					}
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
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
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
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				break;
			}
			case LITERAL_interface:
			{
				interfaceDefinition(composite);
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				break;
			}
			default:
				if ((LA(1)==IDENT) && (LA(2)==LPAREN)) {
					ctorHead(doccp, cp);
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					compoundStatement();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
				}
				else if (((LA(1) >= LITERAL_void && LA(1) <= IDENT)) && (_tokenSet_4.member(LA(2)))) {
					t=typeSpec();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					{
					if ((LA(1)==IDENT) && (LA(2)==LPAREN)) {
						t1 = LT(1);
						if (inputState.guessing==0) {
							t1_AST = (AST)astFactory.create(t1);
							astFactory.addASTChild(currentAST, t1_AST);
						}
						match(IDENT);
						t2 = LT(1);
						if (inputState.guessing==0) {
							t2_AST = (AST)astFactory.create(t2);
							astFactory.addASTChild(currentAST, t2_AST);
						}
						match(LPAREN);
						pdl=parameterDeclarationList();
						if (inputState.guessing==0) {
							astFactory.addASTChild(currentAST, returnAST);
						}
						t3 = LT(1);
						if (inputState.guessing==0) {
							t3_AST = (AST)astFactory.create(t3);
							astFactory.addASTChild(currentAST, t3_AST);
						}
						match(RPAREN);
						rb=returnTypeBrackersOnEndOfMethodHead();
						if (inputState.guessing==0) {
							astFactory.addASTChild(currentAST, returnAST);
						}
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
							if (inputState.guessing==0) {
								astFactory.addASTChild(currentAST, returnAST);
							}
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
							if (inputState.guessing==0) {
								astFactory.addASTChild(currentAST, returnAST);
							}
							break;
						}
						case SEMI:
						{
							AST tmp6_AST = null;
							if (inputState.guessing==0) {
								tmp6_AST = (AST)astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp6_AST);
							}
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
						if (inputState.guessing==0) {
							astFactory.addASTChild(currentAST, returnAST);
						}
						AST tmp7_AST = null;
						if (inputState.guessing==0) {
							tmp7_AST = (AST)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp7_AST);
						}
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
			field_AST = (AST)currentAST.root;
		}
		else if ((LA(1)==LITERAL_static) && (LA(2)==LCURLY)) {
			AST tmp8_AST = null;
			if (inputState.guessing==0) {
				tmp8_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp8_AST);
			}
			match(LITERAL_static);
			compoundStatement();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			field_AST = (AST)currentAST.root;
		}
		else if ((LA(1)==LCURLY)) {
			compoundStatement();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			field_AST = (AST)currentAST.root;
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		returnAST = field_AST;
	}

	public final void ctorHead(
		CompositeCodePiece doccp, CompositeCodePiece cp
	) throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST ctorHead_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		Token  t2 = null;
		AST t2_AST = null;
		CodePiece pdl=null;

		t1 = LT(1);
		if (inputState.guessing==0) {
			t1_AST = (AST)astFactory.create(t1);
			astFactory.addASTChild(currentAST, t1_AST);
		}
		match(IDENT);
		AST tmp9_AST = null;
		if (inputState.guessing==0) {
			tmp9_AST = (AST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp9_AST);
		}
		match(LPAREN);
		pdl=parameterDeclarationList();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		t2 = LT(1);
		if (inputState.guessing==0) {
			t2_AST = (AST)astFactory.create(t2);
			astFactory.addASTChild(currentAST, t2_AST);
		}
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
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
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
		ctorHead_AST = (AST)currentAST.root;
		returnAST = ctorHead_AST;
	}

	public final void compoundStatement() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST compoundStatement_AST = null;

		AST tmp10_AST = null;
		if (inputState.guessing==0) {
			tmp10_AST = (AST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp10_AST);
		}
		match(LCURLY);
		{
		_loop102:
		do {
			if ((_tokenSet_6.member(LA(1)))) {
				statement();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop102;
			}

		} while (true);
		}
		AST tmp11_AST = null;
		if (inputState.guessing==0) {
			tmp11_AST = (AST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp11_AST);
		}
		match(RCURLY);
		compoundStatement_AST = (AST)currentAST.root;
		returnAST = compoundStatement_AST;
	}

	public final CompositeCodePiece  parameterDeclarationList() throws RecognitionException, TokenStreamException {
		CompositeCodePiece cp=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parameterDeclarationList_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
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
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				cp = new CompositeCodePiece(pd);
			}
			{
			_loop93:
			do {
				if ((LA(1)==COMMA)) {
					t1 = LT(1);
					if (inputState.guessing==0) {
						t1_AST = (AST)astFactory.create(t1);
						astFactory.addASTChild(currentAST, t1_AST);
					}
					match(COMMA);
					if ( inputState.guessing==0 ) {
						cp.add(new SimpleCodePiece(t1));
					}
					pd=parameterDeclaration();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
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
		parameterDeclarationList_AST = (AST)currentAST.root;
		returnAST = parameterDeclarationList_AST;
		return cp;
	}

	public final CompositeCodePiece  returnTypeBrackersOnEndOfMethodHead() throws RecognitionException, TokenStreamException {
		CompositeCodePiece cp=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST returnTypeBrackersOnEndOfMethodHead_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		Token  t2 = null;
		AST t2_AST = null;

		{
		_loop89:
		do {
			if ((LA(1)==LBRACK)) {
				t1 = LT(1);
				if (inputState.guessing==0) {
					t1_AST = (AST)astFactory.create(t1);
					astFactory.addASTChild(currentAST, t1_AST);
				}
				match(LBRACK);
				if ( inputState.guessing==0 ) {
					if(cp==null)
								cp = new CompositeCodePiece(
									new SimpleCodePiece(t1));
							else
								cp.add(new SimpleCodePiece(t1));

				}
				t2 = LT(1);
				if (inputState.guessing==0) {
					t2_AST = (AST)astFactory.create(t2);
					astFactory.addASTChild(currentAST, t2_AST);
				}
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
		returnTypeBrackersOnEndOfMethodHead_AST = (AST)currentAST.root;
		returnAST = returnTypeBrackersOnEndOfMethodHead_AST;
		return cp;
	}

	public final void throwsClause() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST throwsClause_AST = null;

		AST tmp12_AST = null;
		if (inputState.guessing==0) {
			tmp12_AST = (AST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp12_AST);
		}
		match(LITERAL_throws);
		identifier();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop86:
		do {
			if ((LA(1)==COMMA)) {
				AST tmp13_AST = null;
				if (inputState.guessing==0) {
					tmp13_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp13_AST);
				}
				match(COMMA);
				identifier();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop86;
			}

		} while (true);
		}
		throwsClause_AST = (AST)currentAST.root;
		returnAST = throwsClause_AST;
	}

/** Declaration of a variable.  This can be a class/instance variable,
 *   or a local variable in a method
 * It can also include possible initialization.
 */
	public final CompositeCodePiece  variableDeclarator() throws RecognitionException, TokenStreamException {
		CompositeCodePiece cp=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST variableDeclarator_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		CodePiece db=null;

		t1 = LT(1);
		if (inputState.guessing==0) {
			t1_AST = (AST)astFactory.create(t1);
			astFactory.addASTChild(currentAST, t1_AST);
		}
		match(IDENT);
		if ( inputState.guessing==0 ) {
			cp = new CompositeCodePiece(
							new SimpleCodePiece(t1));
		}
		db=declaratorBrackets();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			cp.add(db);
		}
		varInitializer();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		variableDeclarator_AST = (AST)currentAST.root;
		returnAST = variableDeclarator_AST;
		return cp;
	}

	public final CompositeCodePiece  declaratorBrackets() throws RecognitionException, TokenStreamException {
		CompositeCodePiece cp=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST declaratorBrackets_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		Token  t2 = null;
		AST t2_AST = null;

		{
		_loop73:
		do {
			if ((LA(1)==LBRACK)) {
				t1 = LT(1);
				if (inputState.guessing==0) {
					t1_AST = (AST)astFactory.create(t1);
					astFactory.addASTChild(currentAST, t1_AST);
				}
				match(LBRACK);
				t2 = LT(1);
				if (inputState.guessing==0) {
					t2_AST = (AST)astFactory.create(t2);
					astFactory.addASTChild(currentAST, t2_AST);
				}
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
		declaratorBrackets_AST = (AST)currentAST.root;
		returnAST = declaratorBrackets_AST;
		return cp;
	}

	public final CompositeCodePiece  varInitializer() throws RecognitionException, TokenStreamException {
		CompositeCodePiece cp=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST varInitializer_AST = null;
		Token  t1 = null;
		AST t1_AST = null;

		{
		switch ( LA(1)) {
		case ASSIGN:
		{
			t1 = LT(1);
			if (inputState.guessing==0) {
				t1_AST = (AST)astFactory.create(t1);
				astFactory.addASTChild(currentAST, t1_AST);
			}
			match(ASSIGN);
			if ( inputState.guessing==0 ) {
				cp = new CompositeCodePiece(
								new SimpleCodePiece(t1));
			}
			initializer();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				cp.add(new SimpleCodePiece(
							new StringBuffer("@"),
							t1.getLine(),
							t1.getColumn()+1,
							t1.getColumn()+2));
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
		varInitializer_AST = (AST)currentAST.root;
		returnAST = varInitializer_AST;
		return cp;
	}

	public final void initializer() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST initializer_AST = null;

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
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			initializer_AST = (AST)currentAST.root;
			break;
		}
		case LCURLY:
		{
			arrayInitializer();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			initializer_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = initializer_AST;
	}

	public final void arrayInitializer() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST arrayInitializer_AST = null;

		AST tmp14_AST = null;
		if (inputState.guessing==0) {
			tmp14_AST = (AST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp14_AST);
		}
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
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			_loop79:
			do {
				if ((LA(1)==COMMA) && (_tokenSet_7.member(LA(2)))) {
					AST tmp15_AST = null;
					if (inputState.guessing==0) {
						tmp15_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp15_AST);
					}
					match(COMMA);
					initializer();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
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
				AST tmp16_AST = null;
				if (inputState.guessing==0) {
					tmp16_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp16_AST);
				}
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
		AST tmp17_AST = null;
		if (inputState.guessing==0) {
			tmp17_AST = (AST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp17_AST);
		}
		match(RCURLY);
		arrayInitializer_AST = (AST)currentAST.root;
		returnAST = arrayInitializer_AST;
	}

	public final void expression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expression_AST = null;

		assignmentExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		expression_AST = (AST)currentAST.root;
		returnAST = expression_AST;
	}

	public final CompositeCodePiece  parameterDeclaration() throws RecognitionException, TokenStreamException {
		CompositeCodePiece cp=null;

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parameterDeclaration_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		CodePiece ts=null;

		parameterModifier();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		ts=typeSpec();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		if ( inputState.guessing==0 ) {
			cp = new CompositeCodePiece(ts);
		}
		t1 = LT(1);
		if (inputState.guessing==0) {
			t1_AST = (AST)astFactory.create(t1);
			astFactory.addASTChild(currentAST, t1_AST);
		}
		match(IDENT);
		if ( inputState.guessing==0 ) {
			cp.add(new SimpleCodePiece(t1));
		}
		parameterDeclaratorBrackets();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		parameterDeclaration_AST = (AST)currentAST.root;
		returnAST = parameterDeclaration_AST;
		return cp;
	}

	public final void parameterModifier() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parameterModifier_AST = null;

		{
		switch ( LA(1)) {
		case FINAL:
		{
			AST tmp18_AST = null;
			if (inputState.guessing==0) {
				tmp18_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp18_AST);
			}
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
		parameterModifier_AST = (AST)currentAST.root;
		returnAST = parameterModifier_AST;
	}

	public final void parameterDeclaratorBrackets() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parameterDeclaratorBrackets_AST = null;

		{
		_loop97:
		do {
			if ((LA(1)==LBRACK)) {
				AST tmp19_AST = null;
				if (inputState.guessing==0) {
					tmp19_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp19_AST);
				}
				match(LBRACK);
				AST tmp20_AST = null;
				if (inputState.guessing==0) {
					tmp20_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp20_AST);
				}
				match(RBRACK);
			}
			else {
				break _loop97;
			}

		} while (true);
		}
		parameterDeclaratorBrackets_AST = (AST)currentAST.root;
		returnAST = parameterDeclaratorBrackets_AST;
	}

	public final void statement() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST statement_AST = null;
		Token  t1 = null;
		AST t1_AST = null;
		Token  t2 = null;
		AST t2_AST = null;

		switch ( LA(1)) {
		case LCURLY:
		{
			compoundStatement();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_class:
		{
			classDefinition(null);
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_if:
		{
			AST tmp21_AST = null;
			if (inputState.guessing==0) {
				tmp21_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp21_AST);
			}
			match(LITERAL_if);
			AST tmp22_AST = null;
			if (inputState.guessing==0) {
				tmp22_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp22_AST);
			}
			match(LPAREN);
			expression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp23_AST = null;
			if (inputState.guessing==0) {
				tmp23_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp23_AST);
			}
			match(RPAREN);
			statement();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			if ((LA(1)==LITERAL_else) && (_tokenSet_6.member(LA(2)))) {
				AST tmp24_AST = null;
				if (inputState.guessing==0) {
					tmp24_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp24_AST);
				}
				match(LITERAL_else);
				statement();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else if ((_tokenSet_8.member(LA(1))) && (_tokenSet_9.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}

			}
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_for:
		{
			AST tmp25_AST = null;
			if (inputState.guessing==0) {
				tmp25_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp25_AST);
			}
			match(LITERAL_for);
			AST tmp26_AST = null;
			if (inputState.guessing==0) {
				tmp26_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp26_AST);
			}
			match(LPAREN);
			forInit();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp27_AST = null;
			if (inputState.guessing==0) {
				tmp27_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp27_AST);
			}
			match(SEMI);
			forCond();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp28_AST = null;
			if (inputState.guessing==0) {
				tmp28_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp28_AST);
			}
			match(SEMI);
			forIter();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp29_AST = null;
			if (inputState.guessing==0) {
				tmp29_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp29_AST);
			}
			match(RPAREN);
			statement();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_while:
		{
			AST tmp30_AST = null;
			if (inputState.guessing==0) {
				tmp30_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp30_AST);
			}
			match(LITERAL_while);
			AST tmp31_AST = null;
			if (inputState.guessing==0) {
				tmp31_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp31_AST);
			}
			match(LPAREN);
			expression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp32_AST = null;
			if (inputState.guessing==0) {
				tmp32_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp32_AST);
			}
			match(RPAREN);
			statement();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_do:
		{
			AST tmp33_AST = null;
			if (inputState.guessing==0) {
				tmp33_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp33_AST);
			}
			match(LITERAL_do);
			statement();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp34_AST = null;
			if (inputState.guessing==0) {
				tmp34_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp34_AST);
			}
			match(LITERAL_while);
			AST tmp35_AST = null;
			if (inputState.guessing==0) {
				tmp35_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp35_AST);
			}
			match(LPAREN);
			expression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp36_AST = null;
			if (inputState.guessing==0) {
				tmp36_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp36_AST);
			}
			match(RPAREN);
			AST tmp37_AST = null;
			if (inputState.guessing==0) {
				tmp37_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp37_AST);
			}
			match(SEMI);
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_break:
		{
			AST tmp38_AST = null;
			if (inputState.guessing==0) {
				tmp38_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp38_AST);
			}
			match(LITERAL_break);
			{
			switch ( LA(1)) {
			case IDENT:
			{
				AST tmp39_AST = null;
				if (inputState.guessing==0) {
					tmp39_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp39_AST);
				}
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
			AST tmp40_AST = null;
			if (inputState.guessing==0) {
				tmp40_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp40_AST);
			}
			match(SEMI);
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_continue:
		{
			AST tmp41_AST = null;
			if (inputState.guessing==0) {
				tmp41_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp41_AST);
			}
			match(LITERAL_continue);
			{
			switch ( LA(1)) {
			case IDENT:
			{
				AST tmp42_AST = null;
				if (inputState.guessing==0) {
					tmp42_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp42_AST);
				}
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
			AST tmp43_AST = null;
			if (inputState.guessing==0) {
				tmp43_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp43_AST);
			}
			match(SEMI);
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_return:
		{
			AST tmp44_AST = null;
			if (inputState.guessing==0) {
				tmp44_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp44_AST);
			}
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
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
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
			AST tmp45_AST = null;
			if (inputState.guessing==0) {
				tmp45_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp45_AST);
			}
			match(SEMI);
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_switch:
		{
			AST tmp46_AST = null;
			if (inputState.guessing==0) {
				tmp46_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp46_AST);
			}
			match(LITERAL_switch);
			AST tmp47_AST = null;
			if (inputState.guessing==0) {
				tmp47_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp47_AST);
			}
			match(LPAREN);
			expression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp48_AST = null;
			if (inputState.guessing==0) {
				tmp48_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp48_AST);
			}
			match(RPAREN);
			AST tmp49_AST = null;
			if (inputState.guessing==0) {
				tmp49_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp49_AST);
			}
			match(LCURLY);
			{
			_loop111:
			do {
				if ((LA(1)==LITERAL_case||LA(1)==LITERAL_default)) {
					casesGroup();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
				}
				else {
					break _loop111;
				}

			} while (true);
			}
			AST tmp50_AST = null;
			if (inputState.guessing==0) {
				tmp50_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp50_AST);
			}
			match(RCURLY);
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_try:
		{
			tryBlock();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			statement_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_throw:
		{
			AST tmp51_AST = null;
			if (inputState.guessing==0) {
				tmp51_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp51_AST);
			}
			match(LITERAL_throw);
			expression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp52_AST = null;
			if (inputState.guessing==0) {
				tmp52_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp52_AST);
			}
			match(SEMI);
			statement_AST = (AST)currentAST.root;
			break;
		}
		case SEMI:
		{
			AST tmp53_AST = null;
			if (inputState.guessing==0) {
				tmp53_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp53_AST);
			}
			match(SEMI);
			statement_AST = (AST)currentAST.root;
			break;
		}
		default:
			if ((LA(1)==FINAL) && (LA(2)==LITERAL_class)) {
				t1 = LT(1);
				if (inputState.guessing==0) {
					t1_AST = (AST)astFactory.create(t1);
					astFactory.addASTChild(currentAST, t1_AST);
				}
				match(FINAL);
				classDefinition(new SimpleCodePiece(t1));
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				statement_AST = (AST)currentAST.root;
			}
			else if ((LA(1)==ABSTRACT) && (LA(2)==LITERAL_class)) {
				t2 = LT(1);
				if (inputState.guessing==0) {
					t2_AST = (AST)astFactory.create(t2);
					astFactory.addASTChild(currentAST, t2_AST);
				}
				match(ABSTRACT);
				classDefinition(new SimpleCodePiece(t2));
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				statement_AST = (AST)currentAST.root;
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
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					AST tmp54_AST = null;
					if (inputState.guessing==0) {
						tmp54_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp54_AST);
					}
					match(SEMI);
					statement_AST = (AST)currentAST.root;
				}
				else if ((_tokenSet_12.member(LA(1))) && (_tokenSet_13.member(LA(2)))) {
					expression();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					AST tmp55_AST = null;
					if (inputState.guessing==0) {
						tmp55_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp55_AST);
					}
					match(SEMI);
					statement_AST = (AST)currentAST.root;
				}
				else if ((LA(1)==IDENT) && (LA(2)==COLON)) {
					AST tmp56_AST = null;
					if (inputState.guessing==0) {
						tmp56_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp56_AST);
					}
					match(IDENT);
					AST tmp57_AST = null;
					if (inputState.guessing==0) {
						tmp57_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp57_AST);
					}
					match(COLON);
					statement();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					statement_AST = (AST)currentAST.root;
				}
				else if ((LA(1)==LITERAL_synchronized) && (LA(2)==LPAREN)) {
					AST tmp58_AST = null;
					if (inputState.guessing==0) {
						tmp58_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp58_AST);
					}
					match(LITERAL_synchronized);
					AST tmp59_AST = null;
					if (inputState.guessing==0) {
						tmp59_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp59_AST);
					}
					match(LPAREN);
					expression();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					AST tmp60_AST = null;
					if (inputState.guessing==0) {
						tmp60_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp60_AST);
					}
					match(RPAREN);
					compoundStatement();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					statement_AST = (AST)currentAST.root;
				}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}}
			returnAST = statement_AST;
		}

	public final void forInit() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forInit_AST = null;

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
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((_tokenSet_12.member(LA(1))) && (_tokenSet_14.member(LA(2)))) {
			expressionList();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
		}
		else if ((LA(1)==SEMI)) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}

		}
		forInit_AST = (AST)currentAST.root;
		returnAST = forInit_AST;
	}

	public final void forCond() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forCond_AST = null;

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
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
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
		forCond_AST = (AST)currentAST.root;
		returnAST = forCond_AST;
	}

	public final void forIter() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forIter_AST = null;

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
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
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
		forIter_AST = (AST)currentAST.root;
		returnAST = forIter_AST;
	}

	public final void casesGroup() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST casesGroup_AST = null;

		{
		int _cnt114=0;
		_loop114:
		do {
			if ((LA(1)==LITERAL_case||LA(1)==LITERAL_default) && (_tokenSet_15.member(LA(2)))) {
				aCase();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				if ( _cnt114>=1 ) { break _loop114; } else {throw new NoViableAltException(LT(1), getFilename());}
			}

			_cnt114++;
		} while (true);
		}
		caseSList();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		casesGroup_AST = (AST)currentAST.root;
		returnAST = casesGroup_AST;
	}

	public final void tryBlock() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST tryBlock_AST = null;

		AST tmp61_AST = null;
		if (inputState.guessing==0) {
			tmp61_AST = (AST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp61_AST);
		}
		match(LITERAL_try);
		compoundStatement();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop130:
		do {
			if ((LA(1)==LITERAL_catch)) {
				handler();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
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
			AST tmp62_AST = null;
			if (inputState.guessing==0) {
				tmp62_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp62_AST);
			}
			match(LITERAL_finally);
			compoundStatement();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
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
		tryBlock_AST = (AST)currentAST.root;
		returnAST = tryBlock_AST;
	}

	public final void aCase() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST aCase_AST = null;

		{
		switch ( LA(1)) {
		case LITERAL_case:
		{
			AST tmp63_AST = null;
			if (inputState.guessing==0) {
				tmp63_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp63_AST);
			}
			match(LITERAL_case);
			expression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		case LITERAL_default:
		{
			AST tmp64_AST = null;
			if (inputState.guessing==0) {
				tmp64_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp64_AST);
			}
			match(LITERAL_default);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		AST tmp65_AST = null;
		if (inputState.guessing==0) {
			tmp65_AST = (AST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp65_AST);
		}
		match(COLON);
		aCase_AST = (AST)currentAST.root;
		returnAST = aCase_AST;
	}

	public final void caseSList() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST caseSList_AST = null;

		{
		_loop119:
		do {
			if ((_tokenSet_6.member(LA(1)))) {
				statement();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop119;
			}

		} while (true);
		}
		caseSList_AST = (AST)currentAST.root;
		returnAST = caseSList_AST;
	}

	public final void expressionList() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expressionList_AST = null;

		expression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop136:
		do {
			if ((LA(1)==COMMA)) {
				AST tmp66_AST = null;
				if (inputState.guessing==0) {
					tmp66_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp66_AST);
				}
				match(COMMA);
				expression();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop136;
			}

		} while (true);
		}
		expressionList_AST = (AST)currentAST.root;
		returnAST = expressionList_AST;
	}

	public final void handler() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST handler_AST = null;

		AST tmp67_AST = null;
		if (inputState.guessing==0) {
			tmp67_AST = (AST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp67_AST);
		}
		match(LITERAL_catch);
		AST tmp68_AST = null;
		if (inputState.guessing==0) {
			tmp68_AST = (AST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp68_AST);
		}
		match(LPAREN);
		parameterDeclaration();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		AST tmp69_AST = null;
		if (inputState.guessing==0) {
			tmp69_AST = (AST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp69_AST);
		}
		match(RPAREN);
		compoundStatement();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		handler_AST = (AST)currentAST.root;
		returnAST = handler_AST;
	}

	public final void assignmentExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST assignmentExpression_AST = null;

		conditionalExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
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
				AST tmp70_AST = null;
				if (inputState.guessing==0) {
					tmp70_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp70_AST);
				}
				match(ASSIGN);
				break;
			}
			case PLUS_ASSIGN:
			{
				AST tmp71_AST = null;
				if (inputState.guessing==0) {
					tmp71_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp71_AST);
				}
				match(PLUS_ASSIGN);
				break;
			}
			case MINUS_ASSIGN:
			{
				AST tmp72_AST = null;
				if (inputState.guessing==0) {
					tmp72_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp72_AST);
				}
				match(MINUS_ASSIGN);
				break;
			}
			case STAR_ASSIGN:
			{
				AST tmp73_AST = null;
				if (inputState.guessing==0) {
					tmp73_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp73_AST);
				}
				match(STAR_ASSIGN);
				break;
			}
			case DIV_ASSIGN:
			{
				AST tmp74_AST = null;
				if (inputState.guessing==0) {
					tmp74_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp74_AST);
				}
				match(DIV_ASSIGN);
				break;
			}
			case MOD_ASSIGN:
			{
				AST tmp75_AST = null;
				if (inputState.guessing==0) {
					tmp75_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp75_AST);
				}
				match(MOD_ASSIGN);
				break;
			}
			case SR_ASSIGN:
			{
				AST tmp76_AST = null;
				if (inputState.guessing==0) {
					tmp76_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp76_AST);
				}
				match(SR_ASSIGN);
				break;
			}
			case BSR_ASSIGN:
			{
				AST tmp77_AST = null;
				if (inputState.guessing==0) {
					tmp77_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp77_AST);
				}
				match(BSR_ASSIGN);
				break;
			}
			case SL_ASSIGN:
			{
				AST tmp78_AST = null;
				if (inputState.guessing==0) {
					tmp78_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp78_AST);
				}
				match(SL_ASSIGN);
				break;
			}
			case BAND_ASSIGN:
			{
				AST tmp79_AST = null;
				if (inputState.guessing==0) {
					tmp79_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp79_AST);
				}
				match(BAND_ASSIGN);
				break;
			}
			case BXOR_ASSIGN:
			{
				AST tmp80_AST = null;
				if (inputState.guessing==0) {
					tmp80_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp80_AST);
				}
				match(BXOR_ASSIGN);
				break;
			}
			case BOR_ASSIGN:
			{
				AST tmp81_AST = null;
				if (inputState.guessing==0) {
					tmp81_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp81_AST);
				}
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
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
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
		assignmentExpression_AST = (AST)currentAST.root;
		returnAST = assignmentExpression_AST;
	}

	public final void conditionalExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST conditionalExpression_AST = null;

		logicalOrExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		switch ( LA(1)) {
		case QUESTION:
		{
			AST tmp82_AST = null;
			if (inputState.guessing==0) {
				tmp82_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp82_AST);
			}
			match(QUESTION);
			assignmentExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp83_AST = null;
			if (inputState.guessing==0) {
				tmp83_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp83_AST);
			}
			match(COLON);
			conditionalExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
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
		conditionalExpression_AST = (AST)currentAST.root;
		returnAST = conditionalExpression_AST;
	}

	public final void logicalOrExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalOrExpression_AST = null;

		logicalAndExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop144:
		do {
			if ((LA(1)==LOR)) {
				AST tmp84_AST = null;
				if (inputState.guessing==0) {
					tmp84_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp84_AST);
				}
				match(LOR);
				logicalAndExpression();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop144;
			}

		} while (true);
		}
		logicalOrExpression_AST = (AST)currentAST.root;
		returnAST = logicalOrExpression_AST;
	}

	public final void logicalAndExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalAndExpression_AST = null;

		inclusiveOrExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop147:
		do {
			if ((LA(1)==LAND)) {
				AST tmp85_AST = null;
				if (inputState.guessing==0) {
					tmp85_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp85_AST);
				}
				match(LAND);
				inclusiveOrExpression();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop147;
			}

		} while (true);
		}
		logicalAndExpression_AST = (AST)currentAST.root;
		returnAST = logicalAndExpression_AST;
	}

	public final void inclusiveOrExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST inclusiveOrExpression_AST = null;

		exclusiveOrExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop150:
		do {
			if ((LA(1)==BOR)) {
				AST tmp86_AST = null;
				if (inputState.guessing==0) {
					tmp86_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp86_AST);
				}
				match(BOR);
				exclusiveOrExpression();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop150;
			}

		} while (true);
		}
		inclusiveOrExpression_AST = (AST)currentAST.root;
		returnAST = inclusiveOrExpression_AST;
	}

	public final void exclusiveOrExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST exclusiveOrExpression_AST = null;

		andExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop153:
		do {
			if ((LA(1)==BXOR)) {
				AST tmp87_AST = null;
				if (inputState.guessing==0) {
					tmp87_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp87_AST);
				}
				match(BXOR);
				andExpression();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop153;
			}

		} while (true);
		}
		exclusiveOrExpression_AST = (AST)currentAST.root;
		returnAST = exclusiveOrExpression_AST;
	}

	public final void andExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST andExpression_AST = null;

		equalityExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop156:
		do {
			if ((LA(1)==BAND)) {
				AST tmp88_AST = null;
				if (inputState.guessing==0) {
					tmp88_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp88_AST);
				}
				match(BAND);
				equalityExpression();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop156;
			}

		} while (true);
		}
		andExpression_AST = (AST)currentAST.root;
		returnAST = andExpression_AST;
	}

	public final void equalityExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST equalityExpression_AST = null;

		relationalExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop160:
		do {
			if ((LA(1)==NOT_EQUAL||LA(1)==EQUAL)) {
				{
				switch ( LA(1)) {
				case NOT_EQUAL:
				{
					AST tmp89_AST = null;
					if (inputState.guessing==0) {
						tmp89_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp89_AST);
					}
					match(NOT_EQUAL);
					break;
				}
				case EQUAL:
				{
					AST tmp90_AST = null;
					if (inputState.guessing==0) {
						tmp90_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp90_AST);
					}
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
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop160;
			}

		} while (true);
		}
		equalityExpression_AST = (AST)currentAST.root;
		returnAST = equalityExpression_AST;
	}

	public final void relationalExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST relationalExpression_AST = null;

		shiftExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
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
						AST tmp91_AST = null;
						if (inputState.guessing==0) {
							tmp91_AST = (AST)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp91_AST);
						}
						match(LT);
						break;
					}
					case GT:
					{
						AST tmp92_AST = null;
						if (inputState.guessing==0) {
							tmp92_AST = (AST)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp92_AST);
						}
						match(GT);
						break;
					}
					case LE:
					{
						AST tmp93_AST = null;
						if (inputState.guessing==0) {
							tmp93_AST = (AST)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp93_AST);
						}
						match(LE);
						break;
					}
					case GE:
					{
						AST tmp94_AST = null;
						if (inputState.guessing==0) {
							tmp94_AST = (AST)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp94_AST);
						}
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
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
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
			AST tmp95_AST = null;
			if (inputState.guessing==0) {
				tmp95_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp95_AST);
			}
			match(LITERAL_instanceof);
			typeSpec();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		relationalExpression_AST = (AST)currentAST.root;
		returnAST = relationalExpression_AST;
	}

	public final void shiftExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST shiftExpression_AST = null;

		additiveExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop169:
		do {
			if (((LA(1) >= SL && LA(1) <= BSR))) {
				{
				switch ( LA(1)) {
				case SL:
				{
					AST tmp96_AST = null;
					if (inputState.guessing==0) {
						tmp96_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp96_AST);
					}
					match(SL);
					break;
				}
				case SR:
				{
					AST tmp97_AST = null;
					if (inputState.guessing==0) {
						tmp97_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp97_AST);
					}
					match(SR);
					break;
				}
				case BSR:
				{
					AST tmp98_AST = null;
					if (inputState.guessing==0) {
						tmp98_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp98_AST);
					}
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
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop169;
			}

		} while (true);
		}
		shiftExpression_AST = (AST)currentAST.root;
		returnAST = shiftExpression_AST;
	}

	public final void additiveExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST additiveExpression_AST = null;

		multiplicativeExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop173:
		do {
			if ((LA(1)==PLUS||LA(1)==MINUS)) {
				{
				switch ( LA(1)) {
				case PLUS:
				{
					AST tmp99_AST = null;
					if (inputState.guessing==0) {
						tmp99_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp99_AST);
					}
					match(PLUS);
					break;
				}
				case MINUS:
				{
					AST tmp100_AST = null;
					if (inputState.guessing==0) {
						tmp100_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp100_AST);
					}
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
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop173;
			}

		} while (true);
		}
		additiveExpression_AST = (AST)currentAST.root;
		returnAST = additiveExpression_AST;
	}

	public final void multiplicativeExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST multiplicativeExpression_AST = null;

		unaryExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop177:
		do {
			if ((_tokenSet_16.member(LA(1)))) {
				{
				switch ( LA(1)) {
				case STAR:
				{
					AST tmp101_AST = null;
					if (inputState.guessing==0) {
						tmp101_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp101_AST);
					}
					match(STAR);
					break;
				}
				case DIV:
				{
					AST tmp102_AST = null;
					if (inputState.guessing==0) {
						tmp102_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp102_AST);
					}
					match(DIV);
					break;
				}
				case MOD:
				{
					AST tmp103_AST = null;
					if (inputState.guessing==0) {
						tmp103_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp103_AST);
					}
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
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
			}
			else {
				break _loop177;
			}

		} while (true);
		}
		multiplicativeExpression_AST = (AST)currentAST.root;
		returnAST = multiplicativeExpression_AST;
	}

	public final void unaryExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST unaryExpression_AST = null;

		switch ( LA(1)) {
		case INC:
		{
			AST tmp104_AST = null;
			if (inputState.guessing==0) {
				tmp104_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp104_AST);
			}
			match(INC);
			unaryExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			unaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case DEC:
		{
			AST tmp105_AST = null;
			if (inputState.guessing==0) {
				tmp105_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp105_AST);
			}
			match(DEC);
			unaryExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			unaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case MINUS:
		{
			AST tmp106_AST = null;
			if (inputState.guessing==0) {
				tmp106_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp106_AST);
			}
			match(MINUS);
			unaryExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			unaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case PLUS:
		{
			AST tmp107_AST = null;
			if (inputState.guessing==0) {
				tmp107_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp107_AST);
			}
			match(PLUS);
			unaryExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			unaryExpression_AST = (AST)currentAST.root;
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
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			unaryExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = unaryExpression_AST;
	}

	public final void unaryExpressionNotPlusMinus() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST unaryExpressionNotPlusMinus_AST = null;

		switch ( LA(1)) {
		case BNOT:
		{
			AST tmp108_AST = null;
			if (inputState.guessing==0) {
				tmp108_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp108_AST);
			}
			match(BNOT);
			unaryExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
			break;
		}
		case LNOT:
		{
			AST tmp109_AST = null;
			if (inputState.guessing==0) {
				tmp109_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp109_AST);
			}
			match(LNOT);
			unaryExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
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
				AST tmp110_AST = null;
				if (inputState.guessing==0) {
					tmp110_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp110_AST);
				}
				match(LPAREN);
				builtInTypeSpec();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				AST tmp111_AST = null;
				if (inputState.guessing==0) {
					tmp111_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp111_AST);
				}
				match(RPAREN);
				unaryExpression();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
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
					AST tmp112_AST = null;
					if (inputState.guessing==0) {
						tmp112_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp112_AST);
					}
					match(LPAREN);
					classTypeSpec();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					AST tmp113_AST = null;
					if (inputState.guessing==0) {
						tmp113_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp113_AST);
					}
					match(RPAREN);
					unaryExpressionNotPlusMinus();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
				}
				else if ((_tokenSet_17.member(LA(1))) && (_tokenSet_18.member(LA(2)))) {
					postfixExpression();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			returnAST = unaryExpressionNotPlusMinus_AST;
		}

	public final void postfixExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST postfixExpression_AST = null;

		primaryExpression();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		_loop189:
		do {
			switch ( LA(1)) {
			case DOT:
			{
				AST tmp114_AST = null;
				if (inputState.guessing==0) {
					tmp114_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp114_AST);
				}
				match(DOT);
				{
				switch ( LA(1)) {
				case IDENT:
				{
					AST tmp115_AST = null;
					if (inputState.guessing==0) {
						tmp115_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp115_AST);
					}
					match(IDENT);
					break;
				}
				case LITERAL_this:
				{
					AST tmp116_AST = null;
					if (inputState.guessing==0) {
						tmp116_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp116_AST);
					}
					match(LITERAL_this);
					break;
				}
				case LITERAL_class:
				{
					AST tmp117_AST = null;
					if (inputState.guessing==0) {
						tmp117_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp117_AST);
					}
					match(LITERAL_class);
					break;
				}
				case LITERAL_new:
				{
					newExpression();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case LITERAL_super:
				{
					AST tmp118_AST = null;
					if (inputState.guessing==0) {
						tmp118_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp118_AST);
					}
					match(LITERAL_super);
					AST tmp119_AST = null;
					if (inputState.guessing==0) {
						tmp119_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp119_AST);
					}
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
						if (inputState.guessing==0) {
							astFactory.addASTChild(currentAST, returnAST);
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
					AST tmp120_AST = null;
					if (inputState.guessing==0) {
						tmp120_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp120_AST);
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
				AST tmp121_AST = null;
				if (inputState.guessing==0) {
					tmp121_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp121_AST);
				}
				match(LPAREN);
				argList();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
				}
				AST tmp122_AST = null;
				if (inputState.guessing==0) {
					tmp122_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp122_AST);
				}
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
							AST tmp123_AST = null;
							if (inputState.guessing==0) {
								tmp123_AST = (AST)astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp123_AST);
							}
							match(LBRACK);
							AST tmp124_AST = null;
							if (inputState.guessing==0) {
								tmp124_AST = (AST)astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp124_AST);
							}
							match(RBRACK);
						}
						else {
							if ( _cnt188>=1 ) { break _loop188; } else {throw new NoViableAltException(LT(1), getFilename());}
						}

						_cnt188++;
					} while (true);
					}
					AST tmp125_AST = null;
					if (inputState.guessing==0) {
						tmp125_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp125_AST);
					}
					match(DOT);
					AST tmp126_AST = null;
					if (inputState.guessing==0) {
						tmp126_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp126_AST);
					}
					match(LITERAL_class);
				}
				else if ((LA(1)==LBRACK) && (_tokenSet_12.member(LA(2)))) {
					AST tmp127_AST = null;
					if (inputState.guessing==0) {
						tmp127_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp127_AST);
					}
					match(LBRACK);
					expression();
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
					AST tmp128_AST = null;
					if (inputState.guessing==0) {
						tmp128_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp128_AST);
					}
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
			AST tmp129_AST = null;
			if (inputState.guessing==0) {
				tmp129_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp129_AST);
			}
			match(INC);
			break;
		}
		case DEC:
		{
			AST tmp130_AST = null;
			if (inputState.guessing==0) {
				tmp130_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp130_AST);
			}
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
		postfixExpression_AST = (AST)currentAST.root;
		returnAST = postfixExpression_AST;
	}

	public final void primaryExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST primaryExpression_AST = null;

		switch ( LA(1)) {
		case IDENT:
		{
			AST tmp131_AST = null;
			if (inputState.guessing==0) {
				tmp131_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp131_AST);
			}
			match(IDENT);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_new:
		{
			newExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case NUM_INT:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case NUM_FLOAT:
		{
			constant();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_super:
		{
			AST tmp132_AST = null;
			if (inputState.guessing==0) {
				tmp132_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp132_AST);
			}
			match(LITERAL_super);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_true:
		{
			AST tmp133_AST = null;
			if (inputState.guessing==0) {
				tmp133_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp133_AST);
			}
			match(LITERAL_true);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_false:
		{
			AST tmp134_AST = null;
			if (inputState.guessing==0) {
				tmp134_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp134_AST);
			}
			match(LITERAL_false);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_this:
		{
			AST tmp135_AST = null;
			if (inputState.guessing==0) {
				tmp135_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp135_AST);
			}
			match(LITERAL_this);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_null:
		{
			AST tmp136_AST = null;
			if (inputState.guessing==0) {
				tmp136_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp136_AST);
			}
			match(LITERAL_null);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LPAREN:
		{
			AST tmp137_AST = null;
			if (inputState.guessing==0) {
				tmp137_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp137_AST);
			}
			match(LPAREN);
			assignmentExpression();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp138_AST = null;
			if (inputState.guessing==0) {
				tmp138_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp138_AST);
			}
			match(RPAREN);
			primaryExpression_AST = (AST)currentAST.root;
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
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			_loop193:
			do {
				if ((LA(1)==LBRACK)) {
					AST tmp139_AST = null;
					if (inputState.guessing==0) {
						tmp139_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp139_AST);
					}
					match(LBRACK);
					AST tmp140_AST = null;
					if (inputState.guessing==0) {
						tmp140_AST = (AST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp140_AST);
					}
					match(RBRACK);
				}
				else {
					break _loop193;
				}

			} while (true);
			}
			AST tmp141_AST = null;
			if (inputState.guessing==0) {
				tmp141_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp141_AST);
			}
			match(DOT);
			AST tmp142_AST = null;
			if (inputState.guessing==0) {
				tmp142_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp142_AST);
			}
			match(LITERAL_class);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = primaryExpression_AST;
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

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST newExpression_AST = null;
		CodePiece t=null;

		AST tmp143_AST = null;
		if (inputState.guessing==0) {
			tmp143_AST = (AST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp143_AST);
		}
		match(LITERAL_new);
		t=type();
		if (inputState.guessing==0) {
			astFactory.addASTChild(currentAST, returnAST);
		}
		{
		switch ( LA(1)) {
		case LPAREN:
		{
			AST tmp144_AST = null;
			if (inputState.guessing==0) {
				tmp144_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp144_AST);
			}
			match(LPAREN);
			argList();
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			AST tmp145_AST = null;
			if (inputState.guessing==0) {
				tmp145_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp145_AST);
			}
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
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
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
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
			}
			{
			switch ( LA(1)) {
			case LCURLY:
			{
				arrayInitializer();
				if (inputState.guessing==0) {
					astFactory.addASTChild(currentAST, returnAST);
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
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		newExpression_AST = (AST)currentAST.root;
		returnAST = newExpression_AST;
	}

	public final void argList() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST argList_AST = null;

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
			if (inputState.guessing==0) {
				astFactory.addASTChild(currentAST, returnAST);
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
		argList_AST = (AST)currentAST.root;
		returnAST = argList_AST;
	}

	public final void constant() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_AST = null;

		switch ( LA(1)) {
		case NUM_INT:
		{
			AST tmp146_AST = null;
			if (inputState.guessing==0) {
				tmp146_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp146_AST);
			}
			match(NUM_INT);
			constant_AST = (AST)currentAST.root;
			break;
		}
		case CHAR_LITERAL:
		{
			AST tmp147_AST = null;
			if (inputState.guessing==0) {
				tmp147_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp147_AST);
			}
			match(CHAR_LITERAL);
			constant_AST = (AST)currentAST.root;
			break;
		}
		case STRING_LITERAL:
		{
			AST tmp148_AST = null;
			if (inputState.guessing==0) {
				tmp148_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp148_AST);
			}
			match(STRING_LITERAL);
			constant_AST = (AST)currentAST.root;
			break;
		}
		case NUM_FLOAT:
		{
			AST tmp149_AST = null;
			if (inputState.guessing==0) {
				tmp149_AST = (AST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp149_AST);
			}
			match(NUM_FLOAT);
			constant_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = constant_AST;
	}

	public final void newArrayDeclarator() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST newArrayDeclarator_AST = null;

		{
		int _cnt203=0;
		_loop203:
		do {
			if ((LA(1)==LBRACK) && (_tokenSet_19.member(LA(2)))) {
				AST tmp150_AST = null;
				if (inputState.guessing==0) {
					tmp150_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp150_AST);
				}
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
					if (inputState.guessing==0) {
						astFactory.addASTChild(currentAST, returnAST);
					}
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
				AST tmp151_AST = null;
				if (inputState.guessing==0) {
					tmp151_AST = (AST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp151_AST);
				}
				match(RBRACK);
			}
			else {
				if ( _cnt203>=1 ) { break _loop203; } else {throw new NoViableAltException(LT(1), getFilename());}
			}

			_cnt203++;
		} while (true);
		}
		newArrayDeclarator_AST = (AST)currentAST.root;
		returnAST = newArrayDeclarator_AST;
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

	private static final long _tokenSet_0_data_[] = { -576437112803426304L, 95L, 0L, 0L };
	public static final BitSet _tokenSet_0 = new BitSet(_tokenSet_0_data_);
	private static final long _tokenSet_1_data_[] = { -576459103035981824L, 15L, 0L, 0L };
	public static final BitSet _tokenSet_1 = new BitSet(_tokenSet_1_data_);
	private static final long _tokenSet_2_data_[] = { -432467060262436864L, 95L, 0L, 0L };
	public static final BitSet _tokenSet_2 = new BitSet(_tokenSet_2_data_);
	private static final long _tokenSet_3_data_[] = { -288316687814492160L, 2143L, 0L, 0L };
	public static final BitSet _tokenSet_3 = new BitSet(_tokenSet_3_data_);
	private static final long _tokenSet_4_data_[] = { 216207966485872640L, 0L, 0L };
	public static final BitSet _tokenSet_4 = new BitSet(_tokenSet_4_data_);
	private static final long _tokenSet_5_data_[] = { 39582418599936L, 8704L, 0L, 0L };
	public static final BitSet _tokenSet_5 = new BitSet(_tokenSet_5_data_);
	private static final long _tokenSet_6_data_[] = { -432480254401970176L, -3746994889636902753L, 4095L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_6 = new BitSet(_tokenSet_6_data_);
	private static final long _tokenSet_7_data_[] = { 143974450587500544L, -3746994889972250496L, 4095L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_7 = new BitSet(_tokenSet_7_data_);
	private static final long _tokenSet_8_data_[] = { -432480254401970176L, -3746994889435444833L, 4095L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_8 = new BitSet(_tokenSet_8_data_);
	private static final long _tokenSet_9_data_[] = { -81913616269312L, -22049L, 4095L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_9 = new BitSet(_tokenSet_9_data_);
	private static final long _tokenSet_10_data_[] = { -432484652448481280L, 15L, 0L, 0L };
	public static final BitSet _tokenSet_10 = new BitSet(_tokenSet_10_data_);
	private static final long _tokenSet_11_data_[] = { -288334280000536576L, 15L, 0L, 0L };
	public static final BitSet _tokenSet_11 = new BitSet(_tokenSet_11_data_);
	private static final long _tokenSet_12_data_[] = { 143974450587500544L, -3746994889972250624L, 4095L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_12 = new BitSet(_tokenSet_12_data_);
	private static final long _tokenSet_13_data_[] = { 576359597233668096L, -2147473408L, 4095L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_13 = new BitSet(_tokenSet_13_data_);
	private static final long _tokenSet_14_data_[] = { 576359597233668096L, -2147472896L, 4095L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_14 = new BitSet(_tokenSet_14_data_);
	private static final long _tokenSet_15_data_[] = { 143974450587500544L, -3746994889972217856L, 4095L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_15 = new BitSet(_tokenSet_15_data_);
	private static final long _tokenSet_16_data_[] = { 288230376151711744L, 3458764513820540928L, 0L, 0L };
	public static final BitSet _tokenSet_16 = new BitSet(_tokenSet_16_data_);
	private static final long _tokenSet_17_data_[] = { 143974450587500544L, 2048L, 4092L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_17 = new BitSet(_tokenSet_17_data_);
	private static final long _tokenSet_18_data_[] = { 576429965977845760L, -2147435776L, 4095L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_18 = new BitSet(_tokenSet_18_data_);
	private static final long _tokenSet_19_data_[] = { 144044819331678208L, -3746994889972250624L, 4095L, 0L, 0L, 0L };
	public static final BitSet _tokenSet_19 = new BitSet(_tokenSet_19_data_);

	}
