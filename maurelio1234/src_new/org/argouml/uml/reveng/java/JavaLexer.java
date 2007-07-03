// $ANTLR 2.7.2a2 (20020112-1): "java.g" -> "JavaLexer.java"$

package org.argouml.uml.reveng.java;

import java.util.*;

import java.io.InputStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.ANTLRException;
import java.io.Reader;
import java.util.Hashtable;
import antlr.CharScanner;
import antlr.InputBuffer;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.Token;
import antlr.CommonToken;
import antlr.RecognitionException;
import antlr.NoViableAltForCharException;
import antlr.MismatchedCharException;
import antlr.TokenStream;
import antlr.ANTLRHashString;
import antlr.LexerSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.SemanticException;

public class JavaLexer extends antlr.CharScanner implements JavaTokenTypes, TokenStream
 {

    /** flag for enabling the "assert" keyword */
    private boolean assertEnabled = true;
    /** flag for enabling the "enum" keyword */
    private boolean enumEnabled = true;

    /** Enable the "assert" keyword */
    public void enableAssert(boolean shouldEnable) { assertEnabled = shouldEnable; }
    /** Query the "assert" keyword state */
    public boolean isAssertEnabled() { return assertEnabled; }
    /** Enable the "enum" keyword */
    public void enableEnum(boolean shouldEnable) { enumEnabled = shouldEnable; }
    /** Query the "enum" keyword state */
    public boolean isEnumEnabled() { return enumEnabled; }

    private StringBuffer whitespaceBuffer = new StringBuffer();

    /**
     * This method adds a whitespace string to the whitespace buffer
     *
     * @param ws The whitespace to add to the buffer
     */
    public void addWhitespace( String ws) {
        whitespaceBuffer.append( ws);
    }

    /**
     * This method returns the current whitespace buffer as a string
     * and reinitializes it.
     *
     * @return the current whitespace buffer as a string.
     */
    public String getWhitespaceBuffer() {
        String result = whitespaceBuffer.toString();

        whitespaceBuffer = new StringBuffer();
        return result;
    }

    /**
     * Override makeToken to store the whitespaces in the
     * special Argo tokens.
     */
    public Token makeToken(int t) {
        Token tok = super.makeToken(t);

        ((ArgoToken)tok).setWhitespace( getWhitespaceBuffer());

        return tok;
    }

    /**
     * A buffer to hold the last parsed javadoc comment.
     */
    String _javadocComment = null;

    /**
     * Store a parsed javadoc comment.
     *
     * @param comment The parsed javadoc comment.
     */
    protected void setJavadocComment(String comment) {
    _javadocComment = comment;
    }

    /**
     * Return (and consume) the last available Javadoc Comment.
     *
     * @return The last parsed javadoc comment.
     */
    protected String getJavadocComment() {
    String result = _javadocComment;

    _javadocComment = null;  // Since we consume the comment, the buffer is reset.

    return result;
    }
public JavaLexer(InputStream in) {
	this(new ByteBuffer(in));
}
public JavaLexer(Reader in) {
	this(new CharBuffer(in));
}
public JavaLexer(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
}
public JavaLexer(LexerSharedInputState state) {
	super(state);
	caseSensitiveLiterals = true;
	setCaseSensitive(true);
	literals = new Hashtable();
	literals.put(new ANTLRHashString("byte", this), new Integer(80));
	literals.put(new ANTLRHashString("public", this), new Integer(89));
	literals.put(new ANTLRHashString("case", this), new Integer(123));
	literals.put(new ANTLRHashString("short", this), new Integer(82));
	literals.put(new ANTLRHashString("break", this), new Integer(116));
	literals.put(new ANTLRHashString("while", this), new Integer(114));
	literals.put(new ANTLRHashString("new", this), new Integer(159));
	literals.put(new ANTLRHashString("instanceof", this), new Integer(146));
	literals.put(new ANTLRHashString("implements", this), new Integer(107));
	literals.put(new ANTLRHashString("synchronized", this), new Integer(94));
	literals.put(new ANTLRHashString("float", this), new Integer(84));
	literals.put(new ANTLRHashString("package", this), new Integer(62));
	literals.put(new ANTLRHashString("return", this), new Integer(118));
	literals.put(new ANTLRHashString("throw", this), new Integer(120));
	literals.put(new ANTLRHashString("null", this), new Integer(158));
	literals.put(new ANTLRHashString("threadsafe", this), new Integer(93));
	literals.put(new ANTLRHashString("protected", this), new Integer(90));
	literals.put(new ANTLRHashString("class", this), new Integer(102));
	literals.put(new ANTLRHashString("throws", this), new Integer(109));
	literals.put(new ANTLRHashString("do", this), new Integer(115));
	literals.put(new ANTLRHashString("strictfp", this), new Integer(41));
	literals.put(new ANTLRHashString("super", this), new Integer(72));
	literals.put(new ANTLRHashString("transient", this), new Integer(91));
	literals.put(new ANTLRHashString("native", this), new Integer(92));
	literals.put(new ANTLRHashString("interface", this), new Integer(103));
	literals.put(new ANTLRHashString("final", this), new Integer(39));
	literals.put(new ANTLRHashString("if", this), new Integer(112));
	literals.put(new ANTLRHashString("double", this), new Integer(86));
	literals.put(new ANTLRHashString("volatile", this), new Integer(95));
	literals.put(new ANTLRHashString("assert", this), new Integer(121));
	literals.put(new ANTLRHashString("catch", this), new Integer(126));
	literals.put(new ANTLRHashString("try", this), new Integer(124));
	literals.put(new ANTLRHashString("enum", this), new Integer(104));
	literals.put(new ANTLRHashString("int", this), new Integer(83));
	literals.put(new ANTLRHashString("for", this), new Integer(122));
	literals.put(new ANTLRHashString("extends", this), new Integer(71));
	literals.put(new ANTLRHashString("boolean", this), new Integer(79));
	literals.put(new ANTLRHashString("char", this), new Integer(81));
	literals.put(new ANTLRHashString("private", this), new Integer(88));
	literals.put(new ANTLRHashString("default", this), new Integer(106));
	literals.put(new ANTLRHashString("false", this), new Integer(157));
	literals.put(new ANTLRHashString("this", this), new Integer(108));
	literals.put(new ANTLRHashString("static", this), new Integer(65));
	literals.put(new ANTLRHashString("abstract", this), new Integer(40));
	literals.put(new ANTLRHashString("continue", this), new Integer(117));
	literals.put(new ANTLRHashString("finally", this), new Integer(125));
	literals.put(new ANTLRHashString("else", this), new Integer(113));
	literals.put(new ANTLRHashString("import", this), new Integer(64));
	literals.put(new ANTLRHashString("void", this), new Integer(78));
	literals.put(new ANTLRHashString("switch", this), new Integer(119));
	literals.put(new ANTLRHashString("true", this), new Integer(156));
	literals.put(new ANTLRHashString("long", this), new Integer(85));
}

public Token nextToken() throws TokenStreamException {
	Token theRetToken=null;
tryAgain:
	for (;;) {
		Token _token = null;
		int _ttype = Token.INVALID_TYPE;
		resetText();
		try {   // for char stream error handling
			try {   // for lexical error handling
				switch ( LA(1)) {
				case '?':
				{
					mQUESTION(true);
					theRetToken=_returnToken;
					break;
				}
				case '(':
				{
					mLPAREN(true);
					theRetToken=_returnToken;
					break;
				}
				case ')':
				{
					mRPAREN(true);
					theRetToken=_returnToken;
					break;
				}
				case '[':
				{
					mLBRACK(true);
					theRetToken=_returnToken;
					break;
				}
				case ']':
				{
					mRBRACK(true);
					theRetToken=_returnToken;
					break;
				}
				case '{':
				{
					mLCURLY(true);
					theRetToken=_returnToken;
					break;
				}
				case '}':
				{
					mRCURLY(true);
					theRetToken=_returnToken;
					break;
				}
				case ':':
				{
					mCOLON(true);
					theRetToken=_returnToken;
					break;
				}
				case ',':
				{
					mCOMMA(true);
					theRetToken=_returnToken;
					break;
				}
				case '~':
				{
					mBNOT(true);
					theRetToken=_returnToken;
					break;
				}
				case ';':
				{
					mSEMI(true);
					theRetToken=_returnToken;
					break;
				}
				case '\t':  case '\n':  case '\u000c':  case '\r':
				case ' ':
				{
					mWS(true);
					theRetToken=_returnToken;
					break;
				}
				case '\'':
				{
					mCHAR_LITERAL(true);
					theRetToken=_returnToken;
					break;
				}
				case '"':
				{
					mSTRING_LITERAL(true);
					theRetToken=_returnToken;
					break;
				}
				case '.':  case '0':  case '1':  case '2':
				case '3':  case '4':  case '5':  case '6':
				case '7':  case '8':  case '9':
				{
					mNUM_INT(true);
					theRetToken=_returnToken;
					break;
				}
				case '@':
				{
					mAT(true);
					theRetToken=_returnToken;
					break;
				}
				default:
					if ((LA(1)=='>') && (LA(2)=='>') && (LA(3)=='>') && (LA(4)=='=')) {
						mBSR_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (LA(2)=='>') && (LA(3)=='=')) {
						mSR_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (LA(2)=='>') && (LA(3)=='>') && (true)) {
						mBSR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='<') && (LA(3)=='=')) {
						mSL_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='/') && (LA(2)=='*') && (LA(3)=='*')) {
						mJAVADOC(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='/') && (LA(2)=='*') && (_tokenSet_0.member(LA(3)))) {
						mML_COMMENT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='=') && (LA(2)=='=')) {
						mEQUAL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='!') && (LA(2)=='=')) {
						mNOT_EQUAL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='/') && (LA(2)=='=')) {
						mDIV_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='+') && (LA(2)=='=')) {
						mPLUS_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='+') && (LA(2)=='+')) {
						mINC(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='-') && (LA(2)=='=')) {
						mMINUS_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='-') && (LA(2)=='-')) {
						mDEC(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='*') && (LA(2)=='=')) {
						mSTAR_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%') && (LA(2)=='=')) {
						mMOD_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (LA(2)=='>') && (true)) {
						mSR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (LA(2)=='=')) {
						mGE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='<') && (true)) {
						mSL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='=')) {
						mLE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='^') && (LA(2)=='=')) {
						mBXOR_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='|') && (LA(2)=='=')) {
						mBOR_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='|') && (LA(2)=='|')) {
						mLOR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='&') && (LA(2)=='=')) {
						mBAND_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='&') && (LA(2)=='&')) {
						mLAND(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='/') && (LA(2)=='/')) {
						mSL_COMMENT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='=') && (true)) {
						mASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='!') && (true)) {
						mLNOT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='/') && (true)) {
						mDIV(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='+') && (true)) {
						mPLUS(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='-') && (true)) {
						mMINUS(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='*') && (true)) {
						mSTAR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%') && (true)) {
						mMOD(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (true)) {
						mGT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (true)) {
						mLT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='^') && (true)) {
						mBXOR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='|') && (true)) {
						mBOR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='&') && (true)) {
						mBAND(true);
						theRetToken=_returnToken;
					}
					else if ((_tokenSet_1.member(LA(1)))) {
						mIDENT(true);
						theRetToken=_returnToken;
					}
				else {
					if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}
				else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				}
				if ( _returnToken==null ) continue tryAgain; // found SKIP token
				_ttype = _returnToken.getType();
				_returnToken.setType(_ttype);
				return _returnToken;
			}
			catch (RecognitionException e) {
				throw new TokenStreamRecognitionException(e);
			}
		}
		catch (CharStreamException cse) {
			if ( cse instanceof CharStreamIOException ) {
				throw new TokenStreamIOException(((CharStreamIOException)cse).io);
			}
			else {
				throw new TokenStreamException(cse.getMessage());
			}
		}
	}
}

	public final void mQUESTION(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = QUESTION;
		int _saveIndex;
		
		match('?');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LPAREN;
		int _saveIndex;
		
		match('(');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RPAREN;
		int _saveIndex;
		
		match(')');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLBRACK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LBRACK;
		int _saveIndex;
		
		match('[');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRBRACK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RBRACK;
		int _saveIndex;
		
		match(']');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLCURLY(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LCURLY;
		int _saveIndex;
		
		match('{');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRCURLY(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RCURLY;
		int _saveIndex;
		
		match('}');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOLON(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COLON;
		int _saveIndex;
		
		match(':');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOMMA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COMMA;
		int _saveIndex;
		
		match(',');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ASSIGN;
		int _saveIndex;
		
		match('=');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mEQUAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = EQUAL;
		int _saveIndex;
		
		match("==");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLNOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LNOT;
		int _saveIndex;
		
		match('!');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBNOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BNOT;
		int _saveIndex;
		
		match('~');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mNOT_EQUAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NOT_EQUAL;
		int _saveIndex;
		
		match("!=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mDIV(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DIV;
		int _saveIndex;
		
		match('/');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mDIV_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DIV_ASSIGN;
		int _saveIndex;
		
		match("/=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mPLUS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PLUS;
		int _saveIndex;
		
		match('+');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mPLUS_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PLUS_ASSIGN;
		int _saveIndex;
		
		match("+=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mINC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = INC;
		int _saveIndex;
		
		match("++");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mMINUS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MINUS;
		int _saveIndex;
		
		match('-');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mMINUS_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MINUS_ASSIGN;
		int _saveIndex;
		
		match("-=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mDEC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DEC;
		int _saveIndex;
		
		match("--");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSTAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STAR;
		int _saveIndex;
		
		match('*');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSTAR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STAR_ASSIGN;
		int _saveIndex;
		
		match("*=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mMOD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MOD;
		int _saveIndex;
		
		match('%');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mMOD_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MOD_ASSIGN;
		int _saveIndex;
		
		match("%=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SR;
		int _saveIndex;
		
		match(">>");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SR_ASSIGN;
		int _saveIndex;
		
		match(">>=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBSR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BSR;
		int _saveIndex;
		
		match(">>>");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBSR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BSR_ASSIGN;
		int _saveIndex;
		
		match(">>>=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mGE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = GE;
		int _saveIndex;
		
		match(">=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mGT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = GT;
		int _saveIndex;
		
		match(">");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SL;
		int _saveIndex;
		
		match("<<");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSL_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SL_ASSIGN;
		int _saveIndex;
		
		match("<<=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LE;
		int _saveIndex;
		
		match("<=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LT;
		int _saveIndex;
		
		match('<');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBXOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BXOR;
		int _saveIndex;
		
		match('^');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBXOR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BXOR_ASSIGN;
		int _saveIndex;
		
		match("^=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BOR;
		int _saveIndex;
		
		match('|');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBOR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BOR_ASSIGN;
		int _saveIndex;
		
		match("|=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LOR;
		int _saveIndex;
		
		match("||");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBAND(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BAND;
		int _saveIndex;
		
		match('&');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBAND_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BAND_ASSIGN;
		int _saveIndex;
		
		match("&=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLAND(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LAND;
		int _saveIndex;
		
		match("&&");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSEMI(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SEMI;
		int _saveIndex;
		
		match(';');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WS;
		int _saveIndex;
		
		{
		int _cnt370=0;
		_loop370:
		do {
			switch ( LA(1)) {
			case ' ':
			{
				match(' ');
				break;
			}
			case '\t':
			{
				match('\t');
				break;
			}
			case '\u000c':
			{
				match('\f');
				break;
			}
			case '\n':  case '\r':
			{
				{
				if ((LA(1)=='\r') && (LA(2)=='\n') && (true) && (true)) {
					match("\r\n");
				}
				else if ((LA(1)=='\r') && (true) && (true) && (true)) {
					match('\r');
				}
				else if ((LA(1)=='\n')) {
					match('\n');
				}
				else {
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				
				}
				if ( inputState.guessing==0 ) {
					newline();
				}
				break;
			}
			default:
			{
				if ( _cnt370>=1 ) { break _loop370; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			}
			_cnt370++;
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			addWhitespace(new String(text.getBuffer(),_begin,text.length()-_begin));
			_ttype = Token.SKIP;
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSL_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SL_COMMENT;
		int _saveIndex;
		boolean nl = false;
		
		match("//");
		{
		_loop374:
		do {
			if ((_tokenSet_2.member(LA(1)))) {
				{
				match(_tokenSet_2);
				}
			}
			else {
				break _loop374;
			}
			
		} while (true);
		}
		{
		if ((LA(1)=='\n'||LA(1)=='\r')) {
			{
			if (((LA(1)=='\r') && (LA(2)=='\n'))&&(LA(2) == '\n')) {
				match('\r');
				match('\n');
			}
			else if ((LA(1)=='\r') && (true)) {
				match('\r');
			}
			else if ((LA(1)=='\n')) {
				match('\n');
			}
			else {
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
			if ( inputState.guessing==0 ) {
				nl = true;
			}
		}
		else {
		}
		
		}
		if ( inputState.guessing==0 ) {
			addWhitespace(new String(text.getBuffer(),_begin,text.length()-_begin));
			_ttype = Token.SKIP; if (nl) newline();
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mJAVADOC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = JAVADOC;
		int _saveIndex;
		
		match("/**");
		{
		_loop380:
		do {
			if ((LA(1)=='\r') && (LA(2)=='\n') && ((LA(3) >= '\u0003' && LA(3) <= '\uffff')) && ((LA(4) >= '\u0003' && LA(4) <= '\uffff'))) {
				match('\r');
				match('\n');
				if ( inputState.guessing==0 ) {
					newline();
				}
			}
			else if (((LA(1)=='*') && ((LA(2) >= '\u0003' && LA(2) <= '\uffff')) && ((LA(3) >= '\u0003' && LA(3) <= '\uffff')))&&( LA(2)!='/' )) {
				match('*');
			}
			else if ((LA(1)=='\r') && ((LA(2) >= '\u0003' && LA(2) <= '\uffff')) && ((LA(3) >= '\u0003' && LA(3) <= '\uffff')) && (true)) {
				match('\r');
				if ( inputState.guessing==0 ) {
					newline();
				}
			}
			else if ((LA(1)=='\n')) {
				match('\n');
				if ( inputState.guessing==0 ) {
					newline();
				}
			}
			else if ((_tokenSet_3.member(LA(1)))) {
				{
				match(_tokenSet_3);
				}
			}
			else {
				break _loop380;
			}
			
		} while (true);
		}
		match("*/");
		if ( inputState.guessing==0 ) {
			setJavadocComment(new String(text.getBuffer(),_begin,text.length()-_begin));
			_ttype = Token.SKIP;
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mML_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ML_COMMENT;
		int _saveIndex;
		
		match("/*");
		{
		if ((LA(1)=='\r') && (LA(2)=='\n') && ((LA(3) >= '\u0003' && LA(3) <= '\uffff')) && ((LA(4) >= '\u0003' && LA(4) <= '\uffff'))) {
			match('\r');
			match('\n');
			if ( inputState.guessing==0 ) {
				newline();
			}
		}
		else if ((LA(1)=='\r') && ((LA(2) >= '\u0003' && LA(2) <= '\uffff')) && ((LA(3) >= '\u0003' && LA(3) <= '\uffff')) && (true)) {
			match('\r');
			if ( inputState.guessing==0 ) {
				newline();
			}
		}
		else if ((LA(1)=='\n')) {
			match('\n');
			if ( inputState.guessing==0 ) {
				newline();
			}
		}
		else if ((_tokenSet_3.member(LA(1)))) {
			{
			match(_tokenSet_3);
			}
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		}
		{
		_loop386:
		do {
			if ((LA(1)=='\r') && (LA(2)=='\n') && ((LA(3) >= '\u0003' && LA(3) <= '\uffff')) && ((LA(4) >= '\u0003' && LA(4) <= '\uffff'))) {
				match('\r');
				match('\n');
				if ( inputState.guessing==0 ) {
					newline();
				}
			}
			else if (((LA(1)=='*') && ((LA(2) >= '\u0003' && LA(2) <= '\uffff')) && ((LA(3) >= '\u0003' && LA(3) <= '\uffff')))&&( LA(2)!='/' )) {
				match('*');
			}
			else if ((LA(1)=='\r') && ((LA(2) >= '\u0003' && LA(2) <= '\uffff')) && ((LA(3) >= '\u0003' && LA(3) <= '\uffff')) && (true)) {
				match('\r');
				if ( inputState.guessing==0 ) {
					newline();
				}
			}
			else if ((LA(1)=='\n')) {
				match('\n');
				if ( inputState.guessing==0 ) {
					newline();
				}
			}
			else if ((_tokenSet_3.member(LA(1)))) {
				{
				match(_tokenSet_3);
				}
			}
			else {
				break _loop386;
			}
			
		} while (true);
		}
		match("*/");
		if ( inputState.guessing==0 ) {
			addWhitespace(new String(text.getBuffer(),_begin,text.length()-_begin));
			_ttype = Token.SKIP;
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCHAR_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CHAR_LITERAL;
		int _saveIndex;
		
		match('\'');
		{
		if ((LA(1)=='\\')) {
			mESC(false);
		}
		else if ((_tokenSet_4.member(LA(1)))) {
			{
			match(_tokenSet_4);
			}
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		}
		match('\'');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mESC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ESC;
		int _saveIndex;
		
		match('\\');
		{
		switch ( LA(1)) {
		case 'n':
		{
			match('n');
			break;
		}
		case 'r':
		{
			match('r');
			break;
		}
		case 't':
		{
			match('t');
			break;
		}
		case 'b':
		{
			match('b');
			break;
		}
		case 'f':
		{
			match('f');
			break;
		}
		case '"':
		{
			match('"');
			break;
		}
		case '\'':
		{
			match('\'');
			break;
		}
		case '\\':
		{
			match('\\');
			break;
		}
		case 'u':
		{
			{
			int _cnt397=0;
			_loop397:
			do {
				if ((LA(1)=='u')) {
					match('u');
				}
				else {
					if ( _cnt397>=1 ) { break _loop397; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt397++;
			} while (true);
			}
			mHEX_DIGIT(false);
			mHEX_DIGIT(false);
			mHEX_DIGIT(false);
			mHEX_DIGIT(false);
			break;
		}
		case '0':  case '1':  case '2':  case '3':
		{
			matchRange('0','3');
			{
			if (((LA(1) >= '0' && LA(1) <= '7')) && (_tokenSet_5.member(LA(2))) && (true) && (true)) {
				matchRange('0','7');
				{
				if (((LA(1) >= '0' && LA(1) <= '7')) && (_tokenSet_5.member(LA(2))) && (true) && (true)) {
					matchRange('0','7');
				}
				else if ((_tokenSet_5.member(LA(1))) && (true) && (true) && (true)) {
				}
				else {
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				
				}
			}
			else if ((_tokenSet_5.member(LA(1))) && (true) && (true) && (true)) {
			}
			else {
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
			break;
		}
		case '4':  case '5':  case '6':  case '7':
		{
			matchRange('4','7');
			{
			if (((LA(1) >= '0' && LA(1) <= '7')) && (_tokenSet_5.member(LA(2))) && (true) && (true)) {
				matchRange('0','7');
			}
			else if ((_tokenSet_5.member(LA(1))) && (true) && (true) && (true)) {
			}
			else {
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSTRING_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STRING_LITERAL;
		int _saveIndex;
		
		match('"');
		{
		_loop393:
		do {
			if ((LA(1)=='\\')) {
				mESC(false);
			}
			else if ((_tokenSet_6.member(LA(1)))) {
				{
				match(_tokenSet_6);
				}
			}
			else {
				break _loop393;
			}
			
		} while (true);
		}
		match('"');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mHEX_DIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HEX_DIGIT;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		case '8':  case '9':
		{
			matchRange('0','9');
			break;
		}
		case 'A':  case 'B':  case 'C':  case 'D':
		case 'E':  case 'F':
		{
			matchRange('A','F');
			break;
		}
		case 'a':  case 'b':  case 'c':  case 'd':
		case 'e':  case 'f':
		{
			matchRange('a','f');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mVOCAB(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = VOCAB;
		int _saveIndex;
		
		matchRange('\3','\377');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mIDENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = IDENT;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case '_':
		{
			match('_');
			break;
		}
		case '$':
		{
			match('$');
			break;
		}
		default:
			if ((_tokenSet_7.member(LA(1)))) {
				mLETTER(false);
			}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		{
		_loop407:
		do {
			switch ( LA(1)) {
			case '_':
			{
				match('_');
				break;
			}
			case '$':
			{
				match('$');
				break;
			}
			default:
				if ((_tokenSet_7.member(LA(1)))) {
					mLETTER(false);
				}
				else if ((_tokenSet_8.member(LA(1)))) {
					mDIGIT(false);
				}
			else {
				break _loop407;
			}
			}
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			
			// check if "assert" keyword is enabled
			if (assertEnabled && "assert".equals(new String(text.getBuffer(),_begin,text.length()-_begin))) {
			_ttype = LITERAL_assert; // set token type for the rule in the parser
			}
			// check if "enum" keyword is enabled
			if (enumEnabled && "enum".equals(new String(text.getBuffer(),_begin,text.length()-_begin))) {
			_ttype = LITERAL_enum; // set token type for the rule in the parser
			}
			
		}
		_ttype = testLiteralsTable(_ttype);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mLETTER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LETTER;
		int _saveIndex;
		
		switch ( LA(1)) {
		case 'A':  case 'B':  case 'C':  case 'D':
		case 'E':  case 'F':  case 'G':  case 'H':
		case 'I':  case 'J':  case 'K':  case 'L':
		case 'M':  case 'N':  case 'O':  case 'P':
		case 'Q':  case 'R':  case 'S':  case 'T':
		case 'U':  case 'V':  case 'W':  case 'X':
		case 'Y':  case 'Z':
		{
			matchRange('\u0041','\u005a');
			break;
		}
		case 'a':  case 'b':  case 'c':  case 'd':
		case 'e':  case 'f':  case 'g':  case 'h':
		case 'i':  case 'j':  case 'k':  case 'l':
		case 'm':  case 'n':  case 'o':  case 'p':
		case 'q':  case 'r':  case 's':  case 't':
		case 'u':  case 'v':  case 'w':  case 'x':
		case 'y':  case 'z':
		{
			matchRange('\u0061','\u007a');
			break;
		}
		case '\u00aa':
		{
			match('\u00aa');
			break;
		}
		case '\u00b5':
		{
			match('\u00b5');
			break;
		}
		case '\u00ba':
		{
			match('\u00ba');
			break;
		}
		case '\u00c0':  case '\u00c1':  case '\u00c2':  case '\u00c3':
		case '\u00c4':  case '\u00c5':  case '\u00c6':  case '\u00c7':
		case '\u00c8':  case '\u00c9':  case '\u00ca':  case '\u00cb':
		case '\u00cc':  case '\u00cd':  case '\u00ce':  case '\u00cf':
		case '\u00d0':  case '\u00d1':  case '\u00d2':  case '\u00d3':
		case '\u00d4':  case '\u00d5':  case '\u00d6':
		{
			matchRange('\u00c0','\u00d6');
			break;
		}
		case '\u00d8':  case '\u00d9':  case '\u00da':  case '\u00db':
		case '\u00dc':  case '\u00dd':  case '\u00de':  case '\u00df':
		case '\u00e0':  case '\u00e1':  case '\u00e2':  case '\u00e3':
		case '\u00e4':  case '\u00e5':  case '\u00e6':  case '\u00e7':
		case '\u00e8':  case '\u00e9':  case '\u00ea':  case '\u00eb':
		case '\u00ec':  case '\u00ed':  case '\u00ee':  case '\u00ef':
		case '\u00f0':  case '\u00f1':  case '\u00f2':  case '\u00f3':
		case '\u00f4':  case '\u00f5':  case '\u00f6':
		{
			matchRange('\u00d8','\u00f6');
			break;
		}
		case '\u037b':  case '\u037c':  case '\u037d':
		{
			matchRange('\u037b','\u037d');
			break;
		}
		case '\u0386':
		{
			match('\u0386');
			break;
		}
		case '\u0388':  case '\u0389':  case '\u038a':
		{
			matchRange('\u0388','\u038a');
			break;
		}
		case '\u038c':
		{
			match('\u038c');
			break;
		}
		case '\u038e':  case '\u038f':  case '\u0390':  case '\u0391':
		case '\u0392':  case '\u0393':  case '\u0394':  case '\u0395':
		case '\u0396':  case '\u0397':  case '\u0398':  case '\u0399':
		case '\u039a':  case '\u039b':  case '\u039c':  case '\u039d':
		case '\u039e':  case '\u039f':  case '\u03a0':  case '\u03a1':
		{
			matchRange('\u038e','\u03a1');
			break;
		}
		case '\u03a3':  case '\u03a4':  case '\u03a5':  case '\u03a6':
		case '\u03a7':  case '\u03a8':  case '\u03a9':  case '\u03aa':
		case '\u03ab':  case '\u03ac':  case '\u03ad':  case '\u03ae':
		case '\u03af':  case '\u03b0':  case '\u03b1':  case '\u03b2':
		case '\u03b3':  case '\u03b4':  case '\u03b5':  case '\u03b6':
		case '\u03b7':  case '\u03b8':  case '\u03b9':  case '\u03ba':
		case '\u03bb':  case '\u03bc':  case '\u03bd':  case '\u03be':
		case '\u03bf':  case '\u03c0':  case '\u03c1':  case '\u03c2':
		case '\u03c3':  case '\u03c4':  case '\u03c5':  case '\u03c6':
		case '\u03c7':  case '\u03c8':  case '\u03c9':  case '\u03ca':
		case '\u03cb':  case '\u03cc':  case '\u03cd':  case '\u03ce':
		{
			matchRange('\u03a3','\u03ce');
			break;
		}
		case '\u03d0':  case '\u03d1':  case '\u03d2':  case '\u03d3':
		case '\u03d4':  case '\u03d5':  case '\u03d6':  case '\u03d7':
		case '\u03d8':  case '\u03d9':  case '\u03da':  case '\u03db':
		case '\u03dc':  case '\u03dd':  case '\u03de':  case '\u03df':
		case '\u03e0':  case '\u03e1':  case '\u03e2':  case '\u03e3':
		case '\u03e4':  case '\u03e5':  case '\u03e6':  case '\u03e7':
		case '\u03e8':  case '\u03e9':  case '\u03ea':  case '\u03eb':
		case '\u03ec':  case '\u03ed':  case '\u03ee':  case '\u03ef':
		case '\u03f0':  case '\u03f1':  case '\u03f2':  case '\u03f3':
		case '\u03f4':  case '\u03f5':
		{
			matchRange('\u03d0','\u03f5');
			break;
		}
		case '\u0531':  case '\u0532':  case '\u0533':  case '\u0534':
		case '\u0535':  case '\u0536':  case '\u0537':  case '\u0538':
		case '\u0539':  case '\u053a':  case '\u053b':  case '\u053c':
		case '\u053d':  case '\u053e':  case '\u053f':  case '\u0540':
		case '\u0541':  case '\u0542':  case '\u0543':  case '\u0544':
		case '\u0545':  case '\u0546':  case '\u0547':  case '\u0548':
		case '\u0549':  case '\u054a':  case '\u054b':  case '\u054c':
		case '\u054d':  case '\u054e':  case '\u054f':  case '\u0550':
		case '\u0551':  case '\u0552':  case '\u0553':  case '\u0554':
		case '\u0555':  case '\u0556':
		{
			matchRange('\u0531','\u0556');
			break;
		}
		case '\u0561':  case '\u0562':  case '\u0563':  case '\u0564':
		case '\u0565':  case '\u0566':  case '\u0567':  case '\u0568':
		case '\u0569':  case '\u056a':  case '\u056b':  case '\u056c':
		case '\u056d':  case '\u056e':  case '\u056f':  case '\u0570':
		case '\u0571':  case '\u0572':  case '\u0573':  case '\u0574':
		case '\u0575':  case '\u0576':  case '\u0577':  case '\u0578':
		case '\u0579':  case '\u057a':  case '\u057b':  case '\u057c':
		case '\u057d':  case '\u057e':  case '\u057f':  case '\u0580':
		case '\u0581':  case '\u0582':  case '\u0583':  case '\u0584':
		case '\u0585':  case '\u0586':  case '\u0587':
		{
			matchRange('\u0561','\u0587');
			break;
		}
		case '\u05d0':  case '\u05d1':  case '\u05d2':  case '\u05d3':
		case '\u05d4':  case '\u05d5':  case '\u05d6':  case '\u05d7':
		case '\u05d8':  case '\u05d9':  case '\u05da':  case '\u05db':
		case '\u05dc':  case '\u05dd':  case '\u05de':  case '\u05df':
		case '\u05e0':  case '\u05e1':  case '\u05e2':  case '\u05e3':
		case '\u05e4':  case '\u05e5':  case '\u05e6':  case '\u05e7':
		case '\u05e8':  case '\u05e9':  case '\u05ea':
		{
			matchRange('\u05d0','\u05ea');
			break;
		}
		case '\u05f0':  case '\u05f1':  case '\u05f2':
		{
			matchRange('\u05f0','\u05f2');
			break;
		}
		case '\u0621':  case '\u0622':  case '\u0623':  case '\u0624':
		case '\u0625':  case '\u0626':  case '\u0627':  case '\u0628':
		case '\u0629':  case '\u062a':  case '\u062b':  case '\u062c':
		case '\u062d':  case '\u062e':  case '\u062f':  case '\u0630':
		case '\u0631':  case '\u0632':  case '\u0633':  case '\u0634':
		case '\u0635':  case '\u0636':  case '\u0637':  case '\u0638':
		case '\u0639':  case '\u063a':
		{
			matchRange('\u0621','\u063a');
			break;
		}
		case '\u0641':  case '\u0642':  case '\u0643':  case '\u0644':
		case '\u0645':  case '\u0646':  case '\u0647':  case '\u0648':
		case '\u0649':  case '\u064a':
		{
			matchRange('\u0641','\u064a');
			break;
		}
		case '\u066e':  case '\u066f':
		{
			matchRange('\u066e','\u066f');
			break;
		}
		case '\u0671':  case '\u0672':  case '\u0673':  case '\u0674':
		case '\u0675':  case '\u0676':  case '\u0677':  case '\u0678':
		case '\u0679':  case '\u067a':  case '\u067b':  case '\u067c':
		case '\u067d':  case '\u067e':  case '\u067f':  case '\u0680':
		case '\u0681':  case '\u0682':  case '\u0683':  case '\u0684':
		case '\u0685':  case '\u0686':  case '\u0687':  case '\u0688':
		case '\u0689':  case '\u068a':  case '\u068b':  case '\u068c':
		case '\u068d':  case '\u068e':  case '\u068f':  case '\u0690':
		case '\u0691':  case '\u0692':  case '\u0693':  case '\u0694':
		case '\u0695':  case '\u0696':  case '\u0697':  case '\u0698':
		case '\u0699':  case '\u069a':  case '\u069b':  case '\u069c':
		case '\u069d':  case '\u069e':  case '\u069f':  case '\u06a0':
		case '\u06a1':  case '\u06a2':  case '\u06a3':  case '\u06a4':
		case '\u06a5':  case '\u06a6':  case '\u06a7':  case '\u06a8':
		case '\u06a9':  case '\u06aa':  case '\u06ab':  case '\u06ac':
		case '\u06ad':  case '\u06ae':  case '\u06af':  case '\u06b0':
		case '\u06b1':  case '\u06b2':  case '\u06b3':  case '\u06b4':
		case '\u06b5':  case '\u06b6':  case '\u06b7':  case '\u06b8':
		case '\u06b9':  case '\u06ba':  case '\u06bb':  case '\u06bc':
		case '\u06bd':  case '\u06be':  case '\u06bf':  case '\u06c0':
		case '\u06c1':  case '\u06c2':  case '\u06c3':  case '\u06c4':
		case '\u06c5':  case '\u06c6':  case '\u06c7':  case '\u06c8':
		case '\u06c9':  case '\u06ca':  case '\u06cb':  case '\u06cc':
		case '\u06cd':  case '\u06ce':  case '\u06cf':  case '\u06d0':
		case '\u06d1':  case '\u06d2':  case '\u06d3':
		{
			matchRange('\u0671','\u06d3');
			break;
		}
		case '\u06d5':
		{
			match('\u06d5');
			break;
		}
		case '\u06ee':  case '\u06ef':
		{
			matchRange('\u06ee','\u06ef');
			break;
		}
		case '\u06fa':  case '\u06fb':  case '\u06fc':
		{
			matchRange('\u06fa','\u06fc');
			break;
		}
		case '\u06ff':
		{
			match('\u06ff');
			break;
		}
		case '\u0710':
		{
			match('\u0710');
			break;
		}
		case '\u0712':  case '\u0713':  case '\u0714':  case '\u0715':
		case '\u0716':  case '\u0717':  case '\u0718':  case '\u0719':
		case '\u071a':  case '\u071b':  case '\u071c':  case '\u071d':
		case '\u071e':  case '\u071f':  case '\u0720':  case '\u0721':
		case '\u0722':  case '\u0723':  case '\u0724':  case '\u0725':
		case '\u0726':  case '\u0727':  case '\u0728':  case '\u0729':
		case '\u072a':  case '\u072b':  case '\u072c':  case '\u072d':
		case '\u072e':  case '\u072f':
		{
			matchRange('\u0712','\u072f');
			break;
		}
		case '\u074d':  case '\u074e':  case '\u074f':  case '\u0750':
		case '\u0751':  case '\u0752':  case '\u0753':  case '\u0754':
		case '\u0755':  case '\u0756':  case '\u0757':  case '\u0758':
		case '\u0759':  case '\u075a':  case '\u075b':  case '\u075c':
		case '\u075d':  case '\u075e':  case '\u075f':  case '\u0760':
		case '\u0761':  case '\u0762':  case '\u0763':  case '\u0764':
		case '\u0765':  case '\u0766':  case '\u0767':  case '\u0768':
		case '\u0769':  case '\u076a':  case '\u076b':  case '\u076c':
		case '\u076d':
		{
			matchRange('\u074d','\u076d');
			break;
		}
		case '\u0780':  case '\u0781':  case '\u0782':  case '\u0783':
		case '\u0784':  case '\u0785':  case '\u0786':  case '\u0787':
		case '\u0788':  case '\u0789':  case '\u078a':  case '\u078b':
		case '\u078c':  case '\u078d':  case '\u078e':  case '\u078f':
		case '\u0790':  case '\u0791':  case '\u0792':  case '\u0793':
		case '\u0794':  case '\u0795':  case '\u0796':  case '\u0797':
		case '\u0798':  case '\u0799':  case '\u079a':  case '\u079b':
		case '\u079c':  case '\u079d':  case '\u079e':  case '\u079f':
		case '\u07a0':  case '\u07a1':  case '\u07a2':  case '\u07a3':
		case '\u07a4':  case '\u07a5':
		{
			matchRange('\u0780','\u07a5');
			break;
		}
		case '\u07b1':
		{
			match('\u07b1');
			break;
		}
		case '\u07ca':  case '\u07cb':  case '\u07cc':  case '\u07cd':
		case '\u07ce':  case '\u07cf':  case '\u07d0':  case '\u07d1':
		case '\u07d2':  case '\u07d3':  case '\u07d4':  case '\u07d5':
		case '\u07d6':  case '\u07d7':  case '\u07d8':  case '\u07d9':
		case '\u07da':  case '\u07db':  case '\u07dc':  case '\u07dd':
		case '\u07de':  case '\u07df':  case '\u07e0':  case '\u07e1':
		case '\u07e2':  case '\u07e3':  case '\u07e4':  case '\u07e5':
		case '\u07e6':  case '\u07e7':  case '\u07e8':  case '\u07e9':
		case '\u07ea':
		{
			matchRange('\u07ca','\u07ea');
			break;
		}
		case '\u0904':  case '\u0905':  case '\u0906':  case '\u0907':
		case '\u0908':  case '\u0909':  case '\u090a':  case '\u090b':
		case '\u090c':  case '\u090d':  case '\u090e':  case '\u090f':
		case '\u0910':  case '\u0911':  case '\u0912':  case '\u0913':
		case '\u0914':  case '\u0915':  case '\u0916':  case '\u0917':
		case '\u0918':  case '\u0919':  case '\u091a':  case '\u091b':
		case '\u091c':  case '\u091d':  case '\u091e':  case '\u091f':
		case '\u0920':  case '\u0921':  case '\u0922':  case '\u0923':
		case '\u0924':  case '\u0925':  case '\u0926':  case '\u0927':
		case '\u0928':  case '\u0929':  case '\u092a':  case '\u092b':
		case '\u092c':  case '\u092d':  case '\u092e':  case '\u092f':
		case '\u0930':  case '\u0931':  case '\u0932':  case '\u0933':
		case '\u0934':  case '\u0935':  case '\u0936':  case '\u0937':
		case '\u0938':  case '\u0939':
		{
			matchRange('\u0904','\u0939');
			break;
		}
		case '\u093d':
		{
			match('\u093d');
			break;
		}
		case '\u0950':
		{
			match('\u0950');
			break;
		}
		case '\u0958':  case '\u0959':  case '\u095a':  case '\u095b':
		case '\u095c':  case '\u095d':  case '\u095e':  case '\u095f':
		case '\u0960':  case '\u0961':
		{
			matchRange('\u0958','\u0961');
			break;
		}
		case '\u097b':  case '\u097c':  case '\u097d':  case '\u097e':
		case '\u097f':
		{
			matchRange('\u097b','\u097f');
			break;
		}
		case '\u0985':  case '\u0986':  case '\u0987':  case '\u0988':
		case '\u0989':  case '\u098a':  case '\u098b':  case '\u098c':
		{
			matchRange('\u0985','\u098c');
			break;
		}
		case '\u098f':  case '\u0990':
		{
			matchRange('\u098f','\u0990');
			break;
		}
		case '\u0993':  case '\u0994':  case '\u0995':  case '\u0996':
		case '\u0997':  case '\u0998':  case '\u0999':  case '\u099a':
		case '\u099b':  case '\u099c':  case '\u099d':  case '\u099e':
		case '\u099f':  case '\u09a0':  case '\u09a1':  case '\u09a2':
		case '\u09a3':  case '\u09a4':  case '\u09a5':  case '\u09a6':
		case '\u09a7':  case '\u09a8':
		{
			matchRange('\u0993','\u09a8');
			break;
		}
		case '\u09aa':  case '\u09ab':  case '\u09ac':  case '\u09ad':
		case '\u09ae':  case '\u09af':  case '\u09b0':
		{
			matchRange('\u09aa','\u09b0');
			break;
		}
		case '\u09b2':
		{
			match('\u09b2');
			break;
		}
		case '\u09b6':  case '\u09b7':  case '\u09b8':  case '\u09b9':
		{
			matchRange('\u09b6','\u09b9');
			break;
		}
		case '\u09bd':
		{
			match('\u09bd');
			break;
		}
		case '\u09ce':
		{
			match('\u09ce');
			break;
		}
		case '\u09dc':  case '\u09dd':
		{
			matchRange('\u09dc','\u09dd');
			break;
		}
		case '\u09df':  case '\u09e0':  case '\u09e1':
		{
			matchRange('\u09df','\u09e1');
			break;
		}
		case '\u09f0':  case '\u09f1':
		{
			matchRange('\u09f0','\u09f1');
			break;
		}
		case '\u0a05':  case '\u0a06':  case '\u0a07':  case '\u0a08':
		case '\u0a09':  case '\u0a0a':
		{
			matchRange('\u0a05','\u0a0a');
			break;
		}
		case '\u0a0f':  case '\u0a10':
		{
			matchRange('\u0a0f','\u0a10');
			break;
		}
		case '\u0a13':  case '\u0a14':  case '\u0a15':  case '\u0a16':
		case '\u0a17':  case '\u0a18':  case '\u0a19':  case '\u0a1a':
		case '\u0a1b':  case '\u0a1c':  case '\u0a1d':  case '\u0a1e':
		case '\u0a1f':  case '\u0a20':  case '\u0a21':  case '\u0a22':
		case '\u0a23':  case '\u0a24':  case '\u0a25':  case '\u0a26':
		case '\u0a27':  case '\u0a28':
		{
			matchRange('\u0a13','\u0a28');
			break;
		}
		case '\u0a2a':  case '\u0a2b':  case '\u0a2c':  case '\u0a2d':
		case '\u0a2e':  case '\u0a2f':  case '\u0a30':
		{
			matchRange('\u0a2a','\u0a30');
			break;
		}
		case '\u0a32':  case '\u0a33':
		{
			matchRange('\u0a32','\u0a33');
			break;
		}
		case '\u0a35':  case '\u0a36':
		{
			matchRange('\u0a35','\u0a36');
			break;
		}
		case '\u0a38':  case '\u0a39':
		{
			matchRange('\u0a38','\u0a39');
			break;
		}
		case '\u0a59':  case '\u0a5a':  case '\u0a5b':  case '\u0a5c':
		{
			matchRange('\u0a59','\u0a5c');
			break;
		}
		case '\u0a5e':
		{
			match('\u0a5e');
			break;
		}
		case '\u0a72':  case '\u0a73':  case '\u0a74':
		{
			matchRange('\u0a72','\u0a74');
			break;
		}
		case '\u0a85':  case '\u0a86':  case '\u0a87':  case '\u0a88':
		case '\u0a89':  case '\u0a8a':  case '\u0a8b':  case '\u0a8c':
		case '\u0a8d':
		{
			matchRange('\u0a85','\u0a8d');
			break;
		}
		case '\u0a8f':  case '\u0a90':  case '\u0a91':
		{
			matchRange('\u0a8f','\u0a91');
			break;
		}
		case '\u0a93':  case '\u0a94':  case '\u0a95':  case '\u0a96':
		case '\u0a97':  case '\u0a98':  case '\u0a99':  case '\u0a9a':
		case '\u0a9b':  case '\u0a9c':  case '\u0a9d':  case '\u0a9e':
		case '\u0a9f':  case '\u0aa0':  case '\u0aa1':  case '\u0aa2':
		case '\u0aa3':  case '\u0aa4':  case '\u0aa5':  case '\u0aa6':
		case '\u0aa7':  case '\u0aa8':
		{
			matchRange('\u0a93','\u0aa8');
			break;
		}
		case '\u0aaa':  case '\u0aab':  case '\u0aac':  case '\u0aad':
		case '\u0aae':  case '\u0aaf':  case '\u0ab0':
		{
			matchRange('\u0aaa','\u0ab0');
			break;
		}
		case '\u0ab2':  case '\u0ab3':
		{
			matchRange('\u0ab2','\u0ab3');
			break;
		}
		case '\u0ab5':  case '\u0ab6':  case '\u0ab7':  case '\u0ab8':
		case '\u0ab9':
		{
			matchRange('\u0ab5','\u0ab9');
			break;
		}
		case '\u0abd':
		{
			match('\u0abd');
			break;
		}
		case '\u0ad0':
		{
			match('\u0ad0');
			break;
		}
		case '\u0ae0':  case '\u0ae1':
		{
			matchRange('\u0ae0','\u0ae1');
			break;
		}
		case '\u0b05':  case '\u0b06':  case '\u0b07':  case '\u0b08':
		case '\u0b09':  case '\u0b0a':  case '\u0b0b':  case '\u0b0c':
		{
			matchRange('\u0b05','\u0b0c');
			break;
		}
		case '\u0b0f':  case '\u0b10':
		{
			matchRange('\u0b0f','\u0b10');
			break;
		}
		case '\u0b13':  case '\u0b14':  case '\u0b15':  case '\u0b16':
		case '\u0b17':  case '\u0b18':  case '\u0b19':  case '\u0b1a':
		case '\u0b1b':  case '\u0b1c':  case '\u0b1d':  case '\u0b1e':
		case '\u0b1f':  case '\u0b20':  case '\u0b21':  case '\u0b22':
		case '\u0b23':  case '\u0b24':  case '\u0b25':  case '\u0b26':
		case '\u0b27':  case '\u0b28':
		{
			matchRange('\u0b13','\u0b28');
			break;
		}
		case '\u0b2a':  case '\u0b2b':  case '\u0b2c':  case '\u0b2d':
		case '\u0b2e':  case '\u0b2f':  case '\u0b30':
		{
			matchRange('\u0b2a','\u0b30');
			break;
		}
		case '\u0b32':  case '\u0b33':
		{
			matchRange('\u0b32','\u0b33');
			break;
		}
		case '\u0b35':  case '\u0b36':  case '\u0b37':  case '\u0b38':
		case '\u0b39':
		{
			matchRange('\u0b35','\u0b39');
			break;
		}
		case '\u0b3d':
		{
			match('\u0b3d');
			break;
		}
		case '\u0b5c':  case '\u0b5d':
		{
			matchRange('\u0b5c','\u0b5d');
			break;
		}
		case '\u0b5f':  case '\u0b60':  case '\u0b61':
		{
			matchRange('\u0b5f','\u0b61');
			break;
		}
		case '\u0b71':
		{
			match('\u0b71');
			break;
		}
		case '\u0b83':
		{
			match('\u0b83');
			break;
		}
		case '\u0b85':  case '\u0b86':  case '\u0b87':  case '\u0b88':
		case '\u0b89':  case '\u0b8a':
		{
			matchRange('\u0b85','\u0b8a');
			break;
		}
		case '\u0b8e':  case '\u0b8f':  case '\u0b90':
		{
			matchRange('\u0b8e','\u0b90');
			break;
		}
		case '\u0b92':  case '\u0b93':  case '\u0b94':  case '\u0b95':
		{
			matchRange('\u0b92','\u0b95');
			break;
		}
		case '\u0b99':  case '\u0b9a':
		{
			matchRange('\u0b99','\u0b9a');
			break;
		}
		case '\u0b9c':
		{
			match('\u0b9c');
			break;
		}
		case '\u0b9e':  case '\u0b9f':
		{
			matchRange('\u0b9e','\u0b9f');
			break;
		}
		case '\u0ba3':  case '\u0ba4':
		{
			matchRange('\u0ba3','\u0ba4');
			break;
		}
		case '\u0ba8':  case '\u0ba9':  case '\u0baa':
		{
			matchRange('\u0ba8','\u0baa');
			break;
		}
		case '\u0bae':  case '\u0baf':  case '\u0bb0':  case '\u0bb1':
		case '\u0bb2':  case '\u0bb3':  case '\u0bb4':  case '\u0bb5':
		case '\u0bb6':  case '\u0bb7':  case '\u0bb8':  case '\u0bb9':
		{
			matchRange('\u0bae','\u0bb9');
			break;
		}
		case '\u0c05':  case '\u0c06':  case '\u0c07':  case '\u0c08':
		case '\u0c09':  case '\u0c0a':  case '\u0c0b':  case '\u0c0c':
		{
			matchRange('\u0c05','\u0c0c');
			break;
		}
		case '\u0c0e':  case '\u0c0f':  case '\u0c10':
		{
			matchRange('\u0c0e','\u0c10');
			break;
		}
		case '\u0c12':  case '\u0c13':  case '\u0c14':  case '\u0c15':
		case '\u0c16':  case '\u0c17':  case '\u0c18':  case '\u0c19':
		case '\u0c1a':  case '\u0c1b':  case '\u0c1c':  case '\u0c1d':
		case '\u0c1e':  case '\u0c1f':  case '\u0c20':  case '\u0c21':
		case '\u0c22':  case '\u0c23':  case '\u0c24':  case '\u0c25':
		case '\u0c26':  case '\u0c27':  case '\u0c28':
		{
			matchRange('\u0c12','\u0c28');
			break;
		}
		case '\u0c2a':  case '\u0c2b':  case '\u0c2c':  case '\u0c2d':
		case '\u0c2e':  case '\u0c2f':  case '\u0c30':  case '\u0c31':
		case '\u0c32':  case '\u0c33':
		{
			matchRange('\u0c2a','\u0c33');
			break;
		}
		case '\u0c35':  case '\u0c36':  case '\u0c37':  case '\u0c38':
		case '\u0c39':
		{
			matchRange('\u0c35','\u0c39');
			break;
		}
		case '\u0c60':  case '\u0c61':
		{
			matchRange('\u0c60','\u0c61');
			break;
		}
		case '\u0c85':  case '\u0c86':  case '\u0c87':  case '\u0c88':
		case '\u0c89':  case '\u0c8a':  case '\u0c8b':  case '\u0c8c':
		{
			matchRange('\u0c85','\u0c8c');
			break;
		}
		case '\u0c8e':  case '\u0c8f':  case '\u0c90':
		{
			matchRange('\u0c8e','\u0c90');
			break;
		}
		case '\u0c92':  case '\u0c93':  case '\u0c94':  case '\u0c95':
		case '\u0c96':  case '\u0c97':  case '\u0c98':  case '\u0c99':
		case '\u0c9a':  case '\u0c9b':  case '\u0c9c':  case '\u0c9d':
		case '\u0c9e':  case '\u0c9f':  case '\u0ca0':  case '\u0ca1':
		case '\u0ca2':  case '\u0ca3':  case '\u0ca4':  case '\u0ca5':
		case '\u0ca6':  case '\u0ca7':  case '\u0ca8':
		{
			matchRange('\u0c92','\u0ca8');
			break;
		}
		case '\u0caa':  case '\u0cab':  case '\u0cac':  case '\u0cad':
		case '\u0cae':  case '\u0caf':  case '\u0cb0':  case '\u0cb1':
		case '\u0cb2':  case '\u0cb3':
		{
			matchRange('\u0caa','\u0cb3');
			break;
		}
		case '\u0cb5':  case '\u0cb6':  case '\u0cb7':  case '\u0cb8':
		case '\u0cb9':
		{
			matchRange('\u0cb5','\u0cb9');
			break;
		}
		case '\u0cbd':
		{
			match('\u0cbd');
			break;
		}
		case '\u0cde':
		{
			match('\u0cde');
			break;
		}
		case '\u0ce0':  case '\u0ce1':
		{
			matchRange('\u0ce0','\u0ce1');
			break;
		}
		case '\u0d05':  case '\u0d06':  case '\u0d07':  case '\u0d08':
		case '\u0d09':  case '\u0d0a':  case '\u0d0b':  case '\u0d0c':
		{
			matchRange('\u0d05','\u0d0c');
			break;
		}
		case '\u0d0e':  case '\u0d0f':  case '\u0d10':
		{
			matchRange('\u0d0e','\u0d10');
			break;
		}
		case '\u0d12':  case '\u0d13':  case '\u0d14':  case '\u0d15':
		case '\u0d16':  case '\u0d17':  case '\u0d18':  case '\u0d19':
		case '\u0d1a':  case '\u0d1b':  case '\u0d1c':  case '\u0d1d':
		case '\u0d1e':  case '\u0d1f':  case '\u0d20':  case '\u0d21':
		case '\u0d22':  case '\u0d23':  case '\u0d24':  case '\u0d25':
		case '\u0d26':  case '\u0d27':  case '\u0d28':
		{
			matchRange('\u0d12','\u0d28');
			break;
		}
		case '\u0d2a':  case '\u0d2b':  case '\u0d2c':  case '\u0d2d':
		case '\u0d2e':  case '\u0d2f':  case '\u0d30':  case '\u0d31':
		case '\u0d32':  case '\u0d33':  case '\u0d34':  case '\u0d35':
		case '\u0d36':  case '\u0d37':  case '\u0d38':  case '\u0d39':
		{
			matchRange('\u0d2a','\u0d39');
			break;
		}
		case '\u0d60':  case '\u0d61':
		{
			matchRange('\u0d60','\u0d61');
			break;
		}
		case '\u0d85':  case '\u0d86':  case '\u0d87':  case '\u0d88':
		case '\u0d89':  case '\u0d8a':  case '\u0d8b':  case '\u0d8c':
		case '\u0d8d':  case '\u0d8e':  case '\u0d8f':  case '\u0d90':
		case '\u0d91':  case '\u0d92':  case '\u0d93':  case '\u0d94':
		case '\u0d95':  case '\u0d96':
		{
			matchRange('\u0d85','\u0d96');
			break;
		}
		case '\u0d9a':  case '\u0d9b':  case '\u0d9c':  case '\u0d9d':
		case '\u0d9e':  case '\u0d9f':  case '\u0da0':  case '\u0da1':
		case '\u0da2':  case '\u0da3':  case '\u0da4':  case '\u0da5':
		case '\u0da6':  case '\u0da7':  case '\u0da8':  case '\u0da9':
		case '\u0daa':  case '\u0dab':  case '\u0dac':  case '\u0dad':
		case '\u0dae':  case '\u0daf':  case '\u0db0':  case '\u0db1':
		{
			matchRange('\u0d9a','\u0db1');
			break;
		}
		case '\u0db3':  case '\u0db4':  case '\u0db5':  case '\u0db6':
		case '\u0db7':  case '\u0db8':  case '\u0db9':  case '\u0dba':
		case '\u0dbb':
		{
			matchRange('\u0db3','\u0dbb');
			break;
		}
		case '\u0dbd':
		{
			match('\u0dbd');
			break;
		}
		case '\u0dc0':  case '\u0dc1':  case '\u0dc2':  case '\u0dc3':
		case '\u0dc4':  case '\u0dc5':  case '\u0dc6':
		{
			matchRange('\u0dc0','\u0dc6');
			break;
		}
		case '\u0e01':  case '\u0e02':  case '\u0e03':  case '\u0e04':
		case '\u0e05':  case '\u0e06':  case '\u0e07':  case '\u0e08':
		case '\u0e09':  case '\u0e0a':  case '\u0e0b':  case '\u0e0c':
		case '\u0e0d':  case '\u0e0e':  case '\u0e0f':  case '\u0e10':
		case '\u0e11':  case '\u0e12':  case '\u0e13':  case '\u0e14':
		case '\u0e15':  case '\u0e16':  case '\u0e17':  case '\u0e18':
		case '\u0e19':  case '\u0e1a':  case '\u0e1b':  case '\u0e1c':
		case '\u0e1d':  case '\u0e1e':  case '\u0e1f':  case '\u0e20':
		case '\u0e21':  case '\u0e22':  case '\u0e23':  case '\u0e24':
		case '\u0e25':  case '\u0e26':  case '\u0e27':  case '\u0e28':
		case '\u0e29':  case '\u0e2a':  case '\u0e2b':  case '\u0e2c':
		case '\u0e2d':  case '\u0e2e':  case '\u0e2f':  case '\u0e30':
		{
			matchRange('\u0e01','\u0e30');
			break;
		}
		case '\u0e32':  case '\u0e33':
		{
			matchRange('\u0e32','\u0e33');
			break;
		}
		case '\u0e40':  case '\u0e41':  case '\u0e42':  case '\u0e43':
		case '\u0e44':  case '\u0e45':
		{
			matchRange('\u0e40','\u0e45');
			break;
		}
		case '\u0e81':  case '\u0e82':
		{
			matchRange('\u0e81','\u0e82');
			break;
		}
		case '\u0e84':
		{
			match('\u0e84');
			break;
		}
		case '\u0e87':  case '\u0e88':
		{
			matchRange('\u0e87','\u0e88');
			break;
		}
		case '\u0e8a':
		{
			match('\u0e8a');
			break;
		}
		case '\u0e8d':
		{
			match('\u0e8d');
			break;
		}
		case '\u0e94':  case '\u0e95':  case '\u0e96':  case '\u0e97':
		{
			matchRange('\u0e94','\u0e97');
			break;
		}
		case '\u0e99':  case '\u0e9a':  case '\u0e9b':  case '\u0e9c':
		case '\u0e9d':  case '\u0e9e':  case '\u0e9f':
		{
			matchRange('\u0e99','\u0e9f');
			break;
		}
		case '\u0ea1':  case '\u0ea2':  case '\u0ea3':
		{
			matchRange('\u0ea1','\u0ea3');
			break;
		}
		case '\u0ea5':
		{
			match('\u0ea5');
			break;
		}
		case '\u0ea7':
		{
			match('\u0ea7');
			break;
		}
		case '\u0eaa':  case '\u0eab':
		{
			matchRange('\u0eaa','\u0eab');
			break;
		}
		case '\u0ead':  case '\u0eae':  case '\u0eaf':  case '\u0eb0':
		{
			matchRange('\u0ead','\u0eb0');
			break;
		}
		case '\u0eb2':  case '\u0eb3':
		{
			matchRange('\u0eb2','\u0eb3');
			break;
		}
		case '\u0ebd':
		{
			match('\u0ebd');
			break;
		}
		case '\u0ec0':  case '\u0ec1':  case '\u0ec2':  case '\u0ec3':
		case '\u0ec4':
		{
			matchRange('\u0ec0','\u0ec4');
			break;
		}
		case '\u0edc':  case '\u0edd':
		{
			matchRange('\u0edc','\u0edd');
			break;
		}
		case '\u0f00':
		{
			match('\u0f00');
			break;
		}
		case '\u0f40':  case '\u0f41':  case '\u0f42':  case '\u0f43':
		case '\u0f44':  case '\u0f45':  case '\u0f46':  case '\u0f47':
		{
			matchRange('\u0f40','\u0f47');
			break;
		}
		case '\u0f49':  case '\u0f4a':  case '\u0f4b':  case '\u0f4c':
		case '\u0f4d':  case '\u0f4e':  case '\u0f4f':  case '\u0f50':
		case '\u0f51':  case '\u0f52':  case '\u0f53':  case '\u0f54':
		case '\u0f55':  case '\u0f56':  case '\u0f57':  case '\u0f58':
		case '\u0f59':  case '\u0f5a':  case '\u0f5b':  case '\u0f5c':
		case '\u0f5d':  case '\u0f5e':  case '\u0f5f':  case '\u0f60':
		case '\u0f61':  case '\u0f62':  case '\u0f63':  case '\u0f64':
		case '\u0f65':  case '\u0f66':  case '\u0f67':  case '\u0f68':
		case '\u0f69':  case '\u0f6a':
		{
			matchRange('\u0f49','\u0f6a');
			break;
		}
		case '\u0f88':  case '\u0f89':  case '\u0f8a':  case '\u0f8b':
		{
			matchRange('\u0f88','\u0f8b');
			break;
		}
		case '\u1000':  case '\u1001':  case '\u1002':  case '\u1003':
		case '\u1004':  case '\u1005':  case '\u1006':  case '\u1007':
		case '\u1008':  case '\u1009':  case '\u100a':  case '\u100b':
		case '\u100c':  case '\u100d':  case '\u100e':  case '\u100f':
		case '\u1010':  case '\u1011':  case '\u1012':  case '\u1013':
		case '\u1014':  case '\u1015':  case '\u1016':  case '\u1017':
		case '\u1018':  case '\u1019':  case '\u101a':  case '\u101b':
		case '\u101c':  case '\u101d':  case '\u101e':  case '\u101f':
		case '\u1020':  case '\u1021':
		{
			matchRange('\u1000','\u1021');
			break;
		}
		case '\u1023':  case '\u1024':  case '\u1025':  case '\u1026':
		case '\u1027':
		{
			matchRange('\u1023','\u1027');
			break;
		}
		case '\u1029':  case '\u102a':
		{
			matchRange('\u1029','\u102a');
			break;
		}
		case '\u1050':  case '\u1051':  case '\u1052':  case '\u1053':
		case '\u1054':  case '\u1055':
		{
			matchRange('\u1050','\u1055');
			break;
		}
		case '\u10a0':  case '\u10a1':  case '\u10a2':  case '\u10a3':
		case '\u10a4':  case '\u10a5':  case '\u10a6':  case '\u10a7':
		case '\u10a8':  case '\u10a9':  case '\u10aa':  case '\u10ab':
		case '\u10ac':  case '\u10ad':  case '\u10ae':  case '\u10af':
		case '\u10b0':  case '\u10b1':  case '\u10b2':  case '\u10b3':
		case '\u10b4':  case '\u10b5':  case '\u10b6':  case '\u10b7':
		case '\u10b8':  case '\u10b9':  case '\u10ba':  case '\u10bb':
		case '\u10bc':  case '\u10bd':  case '\u10be':  case '\u10bf':
		case '\u10c0':  case '\u10c1':  case '\u10c2':  case '\u10c3':
		case '\u10c4':  case '\u10c5':
		{
			matchRange('\u10a0','\u10c5');
			break;
		}
		case '\u10d0':  case '\u10d1':  case '\u10d2':  case '\u10d3':
		case '\u10d4':  case '\u10d5':  case '\u10d6':  case '\u10d7':
		case '\u10d8':  case '\u10d9':  case '\u10da':  case '\u10db':
		case '\u10dc':  case '\u10dd':  case '\u10de':  case '\u10df':
		case '\u10e0':  case '\u10e1':  case '\u10e2':  case '\u10e3':
		case '\u10e4':  case '\u10e5':  case '\u10e6':  case '\u10e7':
		case '\u10e8':  case '\u10e9':  case '\u10ea':  case '\u10eb':
		case '\u10ec':  case '\u10ed':  case '\u10ee':  case '\u10ef':
		case '\u10f0':  case '\u10f1':  case '\u10f2':  case '\u10f3':
		case '\u10f4':  case '\u10f5':  case '\u10f6':  case '\u10f7':
		case '\u10f8':  case '\u10f9':  case '\u10fa':
		{
			matchRange('\u10d0','\u10fa');
			break;
		}
		case '\u1100':  case '\u1101':  case '\u1102':  case '\u1103':
		case '\u1104':  case '\u1105':  case '\u1106':  case '\u1107':
		case '\u1108':  case '\u1109':  case '\u110a':  case '\u110b':
		case '\u110c':  case '\u110d':  case '\u110e':  case '\u110f':
		case '\u1110':  case '\u1111':  case '\u1112':  case '\u1113':
		case '\u1114':  case '\u1115':  case '\u1116':  case '\u1117':
		case '\u1118':  case '\u1119':  case '\u111a':  case '\u111b':
		case '\u111c':  case '\u111d':  case '\u111e':  case '\u111f':
		case '\u1120':  case '\u1121':  case '\u1122':  case '\u1123':
		case '\u1124':  case '\u1125':  case '\u1126':  case '\u1127':
		case '\u1128':  case '\u1129':  case '\u112a':  case '\u112b':
		case '\u112c':  case '\u112d':  case '\u112e':  case '\u112f':
		case '\u1130':  case '\u1131':  case '\u1132':  case '\u1133':
		case '\u1134':  case '\u1135':  case '\u1136':  case '\u1137':
		case '\u1138':  case '\u1139':  case '\u113a':  case '\u113b':
		case '\u113c':  case '\u113d':  case '\u113e':  case '\u113f':
		case '\u1140':  case '\u1141':  case '\u1142':  case '\u1143':
		case '\u1144':  case '\u1145':  case '\u1146':  case '\u1147':
		case '\u1148':  case '\u1149':  case '\u114a':  case '\u114b':
		case '\u114c':  case '\u114d':  case '\u114e':  case '\u114f':
		case '\u1150':  case '\u1151':  case '\u1152':  case '\u1153':
		case '\u1154':  case '\u1155':  case '\u1156':  case '\u1157':
		case '\u1158':  case '\u1159':
		{
			matchRange('\u1100','\u1159');
			break;
		}
		case '\u115f':  case '\u1160':  case '\u1161':  case '\u1162':
		case '\u1163':  case '\u1164':  case '\u1165':  case '\u1166':
		case '\u1167':  case '\u1168':  case '\u1169':  case '\u116a':
		case '\u116b':  case '\u116c':  case '\u116d':  case '\u116e':
		case '\u116f':  case '\u1170':  case '\u1171':  case '\u1172':
		case '\u1173':  case '\u1174':  case '\u1175':  case '\u1176':
		case '\u1177':  case '\u1178':  case '\u1179':  case '\u117a':
		case '\u117b':  case '\u117c':  case '\u117d':  case '\u117e':
		case '\u117f':  case '\u1180':  case '\u1181':  case '\u1182':
		case '\u1183':  case '\u1184':  case '\u1185':  case '\u1186':
		case '\u1187':  case '\u1188':  case '\u1189':  case '\u118a':
		case '\u118b':  case '\u118c':  case '\u118d':  case '\u118e':
		case '\u118f':  case '\u1190':  case '\u1191':  case '\u1192':
		case '\u1193':  case '\u1194':  case '\u1195':  case '\u1196':
		case '\u1197':  case '\u1198':  case '\u1199':  case '\u119a':
		case '\u119b':  case '\u119c':  case '\u119d':  case '\u119e':
		case '\u119f':  case '\u11a0':  case '\u11a1':  case '\u11a2':
		{
			matchRange('\u115f','\u11a2');
			break;
		}
		case '\u11a8':  case '\u11a9':  case '\u11aa':  case '\u11ab':
		case '\u11ac':  case '\u11ad':  case '\u11ae':  case '\u11af':
		case '\u11b0':  case '\u11b1':  case '\u11b2':  case '\u11b3':
		case '\u11b4':  case '\u11b5':  case '\u11b6':  case '\u11b7':
		case '\u11b8':  case '\u11b9':  case '\u11ba':  case '\u11bb':
		case '\u11bc':  case '\u11bd':  case '\u11be':  case '\u11bf':
		case '\u11c0':  case '\u11c1':  case '\u11c2':  case '\u11c3':
		case '\u11c4':  case '\u11c5':  case '\u11c6':  case '\u11c7':
		case '\u11c8':  case '\u11c9':  case '\u11ca':  case '\u11cb':
		case '\u11cc':  case '\u11cd':  case '\u11ce':  case '\u11cf':
		case '\u11d0':  case '\u11d1':  case '\u11d2':  case '\u11d3':
		case '\u11d4':  case '\u11d5':  case '\u11d6':  case '\u11d7':
		case '\u11d8':  case '\u11d9':  case '\u11da':  case '\u11db':
		case '\u11dc':  case '\u11dd':  case '\u11de':  case '\u11df':
		case '\u11e0':  case '\u11e1':  case '\u11e2':  case '\u11e3':
		case '\u11e4':  case '\u11e5':  case '\u11e6':  case '\u11e7':
		case '\u11e8':  case '\u11e9':  case '\u11ea':  case '\u11eb':
		case '\u11ec':  case '\u11ed':  case '\u11ee':  case '\u11ef':
		case '\u11f0':  case '\u11f1':  case '\u11f2':  case '\u11f3':
		case '\u11f4':  case '\u11f5':  case '\u11f6':  case '\u11f7':
		case '\u11f8':  case '\u11f9':
		{
			matchRange('\u11a8','\u11f9');
			break;
		}
		case '\u1200':  case '\u1201':  case '\u1202':  case '\u1203':
		case '\u1204':  case '\u1205':  case '\u1206':  case '\u1207':
		case '\u1208':  case '\u1209':  case '\u120a':  case '\u120b':
		case '\u120c':  case '\u120d':  case '\u120e':  case '\u120f':
		case '\u1210':  case '\u1211':  case '\u1212':  case '\u1213':
		case '\u1214':  case '\u1215':  case '\u1216':  case '\u1217':
		case '\u1218':  case '\u1219':  case '\u121a':  case '\u121b':
		case '\u121c':  case '\u121d':  case '\u121e':  case '\u121f':
		case '\u1220':  case '\u1221':  case '\u1222':  case '\u1223':
		case '\u1224':  case '\u1225':  case '\u1226':  case '\u1227':
		case '\u1228':  case '\u1229':  case '\u122a':  case '\u122b':
		case '\u122c':  case '\u122d':  case '\u122e':  case '\u122f':
		case '\u1230':  case '\u1231':  case '\u1232':  case '\u1233':
		case '\u1234':  case '\u1235':  case '\u1236':  case '\u1237':
		case '\u1238':  case '\u1239':  case '\u123a':  case '\u123b':
		case '\u123c':  case '\u123d':  case '\u123e':  case '\u123f':
		case '\u1240':  case '\u1241':  case '\u1242':  case '\u1243':
		case '\u1244':  case '\u1245':  case '\u1246':  case '\u1247':
		case '\u1248':
		{
			matchRange('\u1200','\u1248');
			break;
		}
		case '\u124a':  case '\u124b':  case '\u124c':  case '\u124d':
		{
			matchRange('\u124a','\u124d');
			break;
		}
		case '\u1250':  case '\u1251':  case '\u1252':  case '\u1253':
		case '\u1254':  case '\u1255':  case '\u1256':
		{
			matchRange('\u1250','\u1256');
			break;
		}
		case '\u1258':
		{
			match('\u1258');
			break;
		}
		case '\u125a':  case '\u125b':  case '\u125c':  case '\u125d':
		{
			matchRange('\u125a','\u125d');
			break;
		}
		case '\u1260':  case '\u1261':  case '\u1262':  case '\u1263':
		case '\u1264':  case '\u1265':  case '\u1266':  case '\u1267':
		case '\u1268':  case '\u1269':  case '\u126a':  case '\u126b':
		case '\u126c':  case '\u126d':  case '\u126e':  case '\u126f':
		case '\u1270':  case '\u1271':  case '\u1272':  case '\u1273':
		case '\u1274':  case '\u1275':  case '\u1276':  case '\u1277':
		case '\u1278':  case '\u1279':  case '\u127a':  case '\u127b':
		case '\u127c':  case '\u127d':  case '\u127e':  case '\u127f':
		case '\u1280':  case '\u1281':  case '\u1282':  case '\u1283':
		case '\u1284':  case '\u1285':  case '\u1286':  case '\u1287':
		case '\u1288':
		{
			matchRange('\u1260','\u1288');
			break;
		}
		case '\u128a':  case '\u128b':  case '\u128c':  case '\u128d':
		{
			matchRange('\u128a','\u128d');
			break;
		}
		case '\u1290':  case '\u1291':  case '\u1292':  case '\u1293':
		case '\u1294':  case '\u1295':  case '\u1296':  case '\u1297':
		case '\u1298':  case '\u1299':  case '\u129a':  case '\u129b':
		case '\u129c':  case '\u129d':  case '\u129e':  case '\u129f':
		case '\u12a0':  case '\u12a1':  case '\u12a2':  case '\u12a3':
		case '\u12a4':  case '\u12a5':  case '\u12a6':  case '\u12a7':
		case '\u12a8':  case '\u12a9':  case '\u12aa':  case '\u12ab':
		case '\u12ac':  case '\u12ad':  case '\u12ae':  case '\u12af':
		case '\u12b0':
		{
			matchRange('\u1290','\u12b0');
			break;
		}
		case '\u12b2':  case '\u12b3':  case '\u12b4':  case '\u12b5':
		{
			matchRange('\u12b2','\u12b5');
			break;
		}
		case '\u12b8':  case '\u12b9':  case '\u12ba':  case '\u12bb':
		case '\u12bc':  case '\u12bd':  case '\u12be':
		{
			matchRange('\u12b8','\u12be');
			break;
		}
		case '\u12c0':
		{
			match('\u12c0');
			break;
		}
		case '\u12c2':  case '\u12c3':  case '\u12c4':  case '\u12c5':
		{
			matchRange('\u12c2','\u12c5');
			break;
		}
		case '\u12c8':  case '\u12c9':  case '\u12ca':  case '\u12cb':
		case '\u12cc':  case '\u12cd':  case '\u12ce':  case '\u12cf':
		case '\u12d0':  case '\u12d1':  case '\u12d2':  case '\u12d3':
		case '\u12d4':  case '\u12d5':  case '\u12d6':
		{
			matchRange('\u12c8','\u12d6');
			break;
		}
		case '\u12d8':  case '\u12d9':  case '\u12da':  case '\u12db':
		case '\u12dc':  case '\u12dd':  case '\u12de':  case '\u12df':
		case '\u12e0':  case '\u12e1':  case '\u12e2':  case '\u12e3':
		case '\u12e4':  case '\u12e5':  case '\u12e6':  case '\u12e7':
		case '\u12e8':  case '\u12e9':  case '\u12ea':  case '\u12eb':
		case '\u12ec':  case '\u12ed':  case '\u12ee':  case '\u12ef':
		case '\u12f0':  case '\u12f1':  case '\u12f2':  case '\u12f3':
		case '\u12f4':  case '\u12f5':  case '\u12f6':  case '\u12f7':
		case '\u12f8':  case '\u12f9':  case '\u12fa':  case '\u12fb':
		case '\u12fc':  case '\u12fd':  case '\u12fe':  case '\u12ff':
		case '\u1300':  case '\u1301':  case '\u1302':  case '\u1303':
		case '\u1304':  case '\u1305':  case '\u1306':  case '\u1307':
		case '\u1308':  case '\u1309':  case '\u130a':  case '\u130b':
		case '\u130c':  case '\u130d':  case '\u130e':  case '\u130f':
		case '\u1310':
		{
			matchRange('\u12d8','\u1310');
			break;
		}
		case '\u1312':  case '\u1313':  case '\u1314':  case '\u1315':
		{
			matchRange('\u1312','\u1315');
			break;
		}
		case '\u1318':  case '\u1319':  case '\u131a':  case '\u131b':
		case '\u131c':  case '\u131d':  case '\u131e':  case '\u131f':
		case '\u1320':  case '\u1321':  case '\u1322':  case '\u1323':
		case '\u1324':  case '\u1325':  case '\u1326':  case '\u1327':
		case '\u1328':  case '\u1329':  case '\u132a':  case '\u132b':
		case '\u132c':  case '\u132d':  case '\u132e':  case '\u132f':
		case '\u1330':  case '\u1331':  case '\u1332':  case '\u1333':
		case '\u1334':  case '\u1335':  case '\u1336':  case '\u1337':
		case '\u1338':  case '\u1339':  case '\u133a':  case '\u133b':
		case '\u133c':  case '\u133d':  case '\u133e':  case '\u133f':
		case '\u1340':  case '\u1341':  case '\u1342':  case '\u1343':
		case '\u1344':  case '\u1345':  case '\u1346':  case '\u1347':
		case '\u1348':  case '\u1349':  case '\u134a':  case '\u134b':
		case '\u134c':  case '\u134d':  case '\u134e':  case '\u134f':
		case '\u1350':  case '\u1351':  case '\u1352':  case '\u1353':
		case '\u1354':  case '\u1355':  case '\u1356':  case '\u1357':
		case '\u1358':  case '\u1359':  case '\u135a':
		{
			matchRange('\u1318','\u135a');
			break;
		}
		case '\u1380':  case '\u1381':  case '\u1382':  case '\u1383':
		case '\u1384':  case '\u1385':  case '\u1386':  case '\u1387':
		case '\u1388':  case '\u1389':  case '\u138a':  case '\u138b':
		case '\u138c':  case '\u138d':  case '\u138e':  case '\u138f':
		{
			matchRange('\u1380','\u138f');
			break;
		}
		case '\u13a0':  case '\u13a1':  case '\u13a2':  case '\u13a3':
		case '\u13a4':  case '\u13a5':  case '\u13a6':  case '\u13a7':
		case '\u13a8':  case '\u13a9':  case '\u13aa':  case '\u13ab':
		case '\u13ac':  case '\u13ad':  case '\u13ae':  case '\u13af':
		case '\u13b0':  case '\u13b1':  case '\u13b2':  case '\u13b3':
		case '\u13b4':  case '\u13b5':  case '\u13b6':  case '\u13b7':
		case '\u13b8':  case '\u13b9':  case '\u13ba':  case '\u13bb':
		case '\u13bc':  case '\u13bd':  case '\u13be':  case '\u13bf':
		case '\u13c0':  case '\u13c1':  case '\u13c2':  case '\u13c3':
		case '\u13c4':  case '\u13c5':  case '\u13c6':  case '\u13c7':
		case '\u13c8':  case '\u13c9':  case '\u13ca':  case '\u13cb':
		case '\u13cc':  case '\u13cd':  case '\u13ce':  case '\u13cf':
		case '\u13d0':  case '\u13d1':  case '\u13d2':  case '\u13d3':
		case '\u13d4':  case '\u13d5':  case '\u13d6':  case '\u13d7':
		case '\u13d8':  case '\u13d9':  case '\u13da':  case '\u13db':
		case '\u13dc':  case '\u13dd':  case '\u13de':  case '\u13df':
		case '\u13e0':  case '\u13e1':  case '\u13e2':  case '\u13e3':
		case '\u13e4':  case '\u13e5':  case '\u13e6':  case '\u13e7':
		case '\u13e8':  case '\u13e9':  case '\u13ea':  case '\u13eb':
		case '\u13ec':  case '\u13ed':  case '\u13ee':  case '\u13ef':
		case '\u13f0':  case '\u13f1':  case '\u13f2':  case '\u13f3':
		case '\u13f4':
		{
			matchRange('\u13a0','\u13f4');
			break;
		}
		case '\u166f':  case '\u1670':  case '\u1671':  case '\u1672':
		case '\u1673':  case '\u1674':  case '\u1675':  case '\u1676':
		{
			matchRange('\u166f','\u1676');
			break;
		}
		case '\u1681':  case '\u1682':  case '\u1683':  case '\u1684':
		case '\u1685':  case '\u1686':  case '\u1687':  case '\u1688':
		case '\u1689':  case '\u168a':  case '\u168b':  case '\u168c':
		case '\u168d':  case '\u168e':  case '\u168f':  case '\u1690':
		case '\u1691':  case '\u1692':  case '\u1693':  case '\u1694':
		case '\u1695':  case '\u1696':  case '\u1697':  case '\u1698':
		case '\u1699':  case '\u169a':
		{
			matchRange('\u1681','\u169a');
			break;
		}
		case '\u16a0':  case '\u16a1':  case '\u16a2':  case '\u16a3':
		case '\u16a4':  case '\u16a5':  case '\u16a6':  case '\u16a7':
		case '\u16a8':  case '\u16a9':  case '\u16aa':  case '\u16ab':
		case '\u16ac':  case '\u16ad':  case '\u16ae':  case '\u16af':
		case '\u16b0':  case '\u16b1':  case '\u16b2':  case '\u16b3':
		case '\u16b4':  case '\u16b5':  case '\u16b6':  case '\u16b7':
		case '\u16b8':  case '\u16b9':  case '\u16ba':  case '\u16bb':
		case '\u16bc':  case '\u16bd':  case '\u16be':  case '\u16bf':
		case '\u16c0':  case '\u16c1':  case '\u16c2':  case '\u16c3':
		case '\u16c4':  case '\u16c5':  case '\u16c6':  case '\u16c7':
		case '\u16c8':  case '\u16c9':  case '\u16ca':  case '\u16cb':
		case '\u16cc':  case '\u16cd':  case '\u16ce':  case '\u16cf':
		case '\u16d0':  case '\u16d1':  case '\u16d2':  case '\u16d3':
		case '\u16d4':  case '\u16d5':  case '\u16d6':  case '\u16d7':
		case '\u16d8':  case '\u16d9':  case '\u16da':  case '\u16db':
		case '\u16dc':  case '\u16dd':  case '\u16de':  case '\u16df':
		case '\u16e0':  case '\u16e1':  case '\u16e2':  case '\u16e3':
		case '\u16e4':  case '\u16e5':  case '\u16e6':  case '\u16e7':
		case '\u16e8':  case '\u16e9':  case '\u16ea':
		{
			matchRange('\u16a0','\u16ea');
			break;
		}
		case '\u1700':  case '\u1701':  case '\u1702':  case '\u1703':
		case '\u1704':  case '\u1705':  case '\u1706':  case '\u1707':
		case '\u1708':  case '\u1709':  case '\u170a':  case '\u170b':
		case '\u170c':
		{
			matchRange('\u1700','\u170c');
			break;
		}
		case '\u170e':  case '\u170f':  case '\u1710':  case '\u1711':
		{
			matchRange('\u170e','\u1711');
			break;
		}
		case '\u1720':  case '\u1721':  case '\u1722':  case '\u1723':
		case '\u1724':  case '\u1725':  case '\u1726':  case '\u1727':
		case '\u1728':  case '\u1729':  case '\u172a':  case '\u172b':
		case '\u172c':  case '\u172d':  case '\u172e':  case '\u172f':
		case '\u1730':  case '\u1731':
		{
			matchRange('\u1720','\u1731');
			break;
		}
		case '\u1740':  case '\u1741':  case '\u1742':  case '\u1743':
		case '\u1744':  case '\u1745':  case '\u1746':  case '\u1747':
		case '\u1748':  case '\u1749':  case '\u174a':  case '\u174b':
		case '\u174c':  case '\u174d':  case '\u174e':  case '\u174f':
		case '\u1750':  case '\u1751':
		{
			matchRange('\u1740','\u1751');
			break;
		}
		case '\u1760':  case '\u1761':  case '\u1762':  case '\u1763':
		case '\u1764':  case '\u1765':  case '\u1766':  case '\u1767':
		case '\u1768':  case '\u1769':  case '\u176a':  case '\u176b':
		case '\u176c':
		{
			matchRange('\u1760','\u176c');
			break;
		}
		case '\u176e':  case '\u176f':  case '\u1770':
		{
			matchRange('\u176e','\u1770');
			break;
		}
		case '\u1780':  case '\u1781':  case '\u1782':  case '\u1783':
		case '\u1784':  case '\u1785':  case '\u1786':  case '\u1787':
		case '\u1788':  case '\u1789':  case '\u178a':  case '\u178b':
		case '\u178c':  case '\u178d':  case '\u178e':  case '\u178f':
		case '\u1790':  case '\u1791':  case '\u1792':  case '\u1793':
		case '\u1794':  case '\u1795':  case '\u1796':  case '\u1797':
		case '\u1798':  case '\u1799':  case '\u179a':  case '\u179b':
		case '\u179c':  case '\u179d':  case '\u179e':  case '\u179f':
		case '\u17a0':  case '\u17a1':  case '\u17a2':  case '\u17a3':
		case '\u17a4':  case '\u17a5':  case '\u17a6':  case '\u17a7':
		case '\u17a8':  case '\u17a9':  case '\u17aa':  case '\u17ab':
		case '\u17ac':  case '\u17ad':  case '\u17ae':  case '\u17af':
		case '\u17b0':  case '\u17b1':  case '\u17b2':  case '\u17b3':
		{
			matchRange('\u1780','\u17b3');
			break;
		}
		case '\u17dc':
		{
			match('\u17dc');
			break;
		}
		case '\u1820':  case '\u1821':  case '\u1822':  case '\u1823':
		case '\u1824':  case '\u1825':  case '\u1826':  case '\u1827':
		case '\u1828':  case '\u1829':  case '\u182a':  case '\u182b':
		case '\u182c':  case '\u182d':  case '\u182e':  case '\u182f':
		case '\u1830':  case '\u1831':  case '\u1832':  case '\u1833':
		case '\u1834':  case '\u1835':  case '\u1836':  case '\u1837':
		case '\u1838':  case '\u1839':  case '\u183a':  case '\u183b':
		case '\u183c':  case '\u183d':  case '\u183e':  case '\u183f':
		case '\u1840':  case '\u1841':  case '\u1842':
		{
			matchRange('\u1820','\u1842');
			break;
		}
		case '\u1844':  case '\u1845':  case '\u1846':  case '\u1847':
		case '\u1848':  case '\u1849':  case '\u184a':  case '\u184b':
		case '\u184c':  case '\u184d':  case '\u184e':  case '\u184f':
		case '\u1850':  case '\u1851':  case '\u1852':  case '\u1853':
		case '\u1854':  case '\u1855':  case '\u1856':  case '\u1857':
		case '\u1858':  case '\u1859':  case '\u185a':  case '\u185b':
		case '\u185c':  case '\u185d':  case '\u185e':  case '\u185f':
		case '\u1860':  case '\u1861':  case '\u1862':  case '\u1863':
		case '\u1864':  case '\u1865':  case '\u1866':  case '\u1867':
		case '\u1868':  case '\u1869':  case '\u186a':  case '\u186b':
		case '\u186c':  case '\u186d':  case '\u186e':  case '\u186f':
		case '\u1870':  case '\u1871':  case '\u1872':  case '\u1873':
		case '\u1874':  case '\u1875':  case '\u1876':  case '\u1877':
		{
			matchRange('\u1844','\u1877');
			break;
		}
		case '\u1880':  case '\u1881':  case '\u1882':  case '\u1883':
		case '\u1884':  case '\u1885':  case '\u1886':  case '\u1887':
		case '\u1888':  case '\u1889':  case '\u188a':  case '\u188b':
		case '\u188c':  case '\u188d':  case '\u188e':  case '\u188f':
		case '\u1890':  case '\u1891':  case '\u1892':  case '\u1893':
		case '\u1894':  case '\u1895':  case '\u1896':  case '\u1897':
		case '\u1898':  case '\u1899':  case '\u189a':  case '\u189b':
		case '\u189c':  case '\u189d':  case '\u189e':  case '\u189f':
		case '\u18a0':  case '\u18a1':  case '\u18a2':  case '\u18a3':
		case '\u18a4':  case '\u18a5':  case '\u18a6':  case '\u18a7':
		case '\u18a8':
		{
			matchRange('\u1880','\u18a8');
			break;
		}
		case '\u1900':  case '\u1901':  case '\u1902':  case '\u1903':
		case '\u1904':  case '\u1905':  case '\u1906':  case '\u1907':
		case '\u1908':  case '\u1909':  case '\u190a':  case '\u190b':
		case '\u190c':  case '\u190d':  case '\u190e':  case '\u190f':
		case '\u1910':  case '\u1911':  case '\u1912':  case '\u1913':
		case '\u1914':  case '\u1915':  case '\u1916':  case '\u1917':
		case '\u1918':  case '\u1919':  case '\u191a':  case '\u191b':
		case '\u191c':
		{
			matchRange('\u1900','\u191c');
			break;
		}
		case '\u1950':  case '\u1951':  case '\u1952':  case '\u1953':
		case '\u1954':  case '\u1955':  case '\u1956':  case '\u1957':
		case '\u1958':  case '\u1959':  case '\u195a':  case '\u195b':
		case '\u195c':  case '\u195d':  case '\u195e':  case '\u195f':
		case '\u1960':  case '\u1961':  case '\u1962':  case '\u1963':
		case '\u1964':  case '\u1965':  case '\u1966':  case '\u1967':
		case '\u1968':  case '\u1969':  case '\u196a':  case '\u196b':
		case '\u196c':  case '\u196d':
		{
			matchRange('\u1950','\u196d');
			break;
		}
		case '\u1970':  case '\u1971':  case '\u1972':  case '\u1973':
		case '\u1974':
		{
			matchRange('\u1970','\u1974');
			break;
		}
		case '\u1980':  case '\u1981':  case '\u1982':  case '\u1983':
		case '\u1984':  case '\u1985':  case '\u1986':  case '\u1987':
		case '\u1988':  case '\u1989':  case '\u198a':  case '\u198b':
		case '\u198c':  case '\u198d':  case '\u198e':  case '\u198f':
		case '\u1990':  case '\u1991':  case '\u1992':  case '\u1993':
		case '\u1994':  case '\u1995':  case '\u1996':  case '\u1997':
		case '\u1998':  case '\u1999':  case '\u199a':  case '\u199b':
		case '\u199c':  case '\u199d':  case '\u199e':  case '\u199f':
		case '\u19a0':  case '\u19a1':  case '\u19a2':  case '\u19a3':
		case '\u19a4':  case '\u19a5':  case '\u19a6':  case '\u19a7':
		case '\u19a8':  case '\u19a9':
		{
			matchRange('\u1980','\u19a9');
			break;
		}
		case '\u19c1':  case '\u19c2':  case '\u19c3':  case '\u19c4':
		case '\u19c5':  case '\u19c6':  case '\u19c7':
		{
			matchRange('\u19c1','\u19c7');
			break;
		}
		case '\u1a00':  case '\u1a01':  case '\u1a02':  case '\u1a03':
		case '\u1a04':  case '\u1a05':  case '\u1a06':  case '\u1a07':
		case '\u1a08':  case '\u1a09':  case '\u1a0a':  case '\u1a0b':
		case '\u1a0c':  case '\u1a0d':  case '\u1a0e':  case '\u1a0f':
		case '\u1a10':  case '\u1a11':  case '\u1a12':  case '\u1a13':
		case '\u1a14':  case '\u1a15':  case '\u1a16':
		{
			matchRange('\u1a00','\u1a16');
			break;
		}
		case '\u1b05':  case '\u1b06':  case '\u1b07':  case '\u1b08':
		case '\u1b09':  case '\u1b0a':  case '\u1b0b':  case '\u1b0c':
		case '\u1b0d':  case '\u1b0e':  case '\u1b0f':  case '\u1b10':
		case '\u1b11':  case '\u1b12':  case '\u1b13':  case '\u1b14':
		case '\u1b15':  case '\u1b16':  case '\u1b17':  case '\u1b18':
		case '\u1b19':  case '\u1b1a':  case '\u1b1b':  case '\u1b1c':
		case '\u1b1d':  case '\u1b1e':  case '\u1b1f':  case '\u1b20':
		case '\u1b21':  case '\u1b22':  case '\u1b23':  case '\u1b24':
		case '\u1b25':  case '\u1b26':  case '\u1b27':  case '\u1b28':
		case '\u1b29':  case '\u1b2a':  case '\u1b2b':  case '\u1b2c':
		case '\u1b2d':  case '\u1b2e':  case '\u1b2f':  case '\u1b30':
		case '\u1b31':  case '\u1b32':  case '\u1b33':
		{
			matchRange('\u1b05','\u1b33');
			break;
		}
		case '\u1b45':  case '\u1b46':  case '\u1b47':  case '\u1b48':
		case '\u1b49':  case '\u1b4a':  case '\u1b4b':
		{
			matchRange('\u1b45','\u1b4b');
			break;
		}
		case '\u1d00':  case '\u1d01':  case '\u1d02':  case '\u1d03':
		case '\u1d04':  case '\u1d05':  case '\u1d06':  case '\u1d07':
		case '\u1d08':  case '\u1d09':  case '\u1d0a':  case '\u1d0b':
		case '\u1d0c':  case '\u1d0d':  case '\u1d0e':  case '\u1d0f':
		case '\u1d10':  case '\u1d11':  case '\u1d12':  case '\u1d13':
		case '\u1d14':  case '\u1d15':  case '\u1d16':  case '\u1d17':
		case '\u1d18':  case '\u1d19':  case '\u1d1a':  case '\u1d1b':
		case '\u1d1c':  case '\u1d1d':  case '\u1d1e':  case '\u1d1f':
		case '\u1d20':  case '\u1d21':  case '\u1d22':  case '\u1d23':
		case '\u1d24':  case '\u1d25':  case '\u1d26':  case '\u1d27':
		case '\u1d28':  case '\u1d29':  case '\u1d2a':  case '\u1d2b':
		{
			matchRange('\u1d00','\u1d2b');
			break;
		}
		case '\u1d62':  case '\u1d63':  case '\u1d64':  case '\u1d65':
		case '\u1d66':  case '\u1d67':  case '\u1d68':  case '\u1d69':
		case '\u1d6a':  case '\u1d6b':  case '\u1d6c':  case '\u1d6d':
		case '\u1d6e':  case '\u1d6f':  case '\u1d70':  case '\u1d71':
		case '\u1d72':  case '\u1d73':  case '\u1d74':  case '\u1d75':
		case '\u1d76':  case '\u1d77':
		{
			matchRange('\u1d62','\u1d77');
			break;
		}
		case '\u1d79':  case '\u1d7a':  case '\u1d7b':  case '\u1d7c':
		case '\u1d7d':  case '\u1d7e':  case '\u1d7f':  case '\u1d80':
		case '\u1d81':  case '\u1d82':  case '\u1d83':  case '\u1d84':
		case '\u1d85':  case '\u1d86':  case '\u1d87':  case '\u1d88':
		case '\u1d89':  case '\u1d8a':  case '\u1d8b':  case '\u1d8c':
		case '\u1d8d':  case '\u1d8e':  case '\u1d8f':  case '\u1d90':
		case '\u1d91':  case '\u1d92':  case '\u1d93':  case '\u1d94':
		case '\u1d95':  case '\u1d96':  case '\u1d97':  case '\u1d98':
		case '\u1d99':  case '\u1d9a':
		{
			matchRange('\u1d79','\u1d9a');
			break;
		}
		case '\u1ea0':  case '\u1ea1':  case '\u1ea2':  case '\u1ea3':
		case '\u1ea4':  case '\u1ea5':  case '\u1ea6':  case '\u1ea7':
		case '\u1ea8':  case '\u1ea9':  case '\u1eaa':  case '\u1eab':
		case '\u1eac':  case '\u1ead':  case '\u1eae':  case '\u1eaf':
		case '\u1eb0':  case '\u1eb1':  case '\u1eb2':  case '\u1eb3':
		case '\u1eb4':  case '\u1eb5':  case '\u1eb6':  case '\u1eb7':
		case '\u1eb8':  case '\u1eb9':  case '\u1eba':  case '\u1ebb':
		case '\u1ebc':  case '\u1ebd':  case '\u1ebe':  case '\u1ebf':
		case '\u1ec0':  case '\u1ec1':  case '\u1ec2':  case '\u1ec3':
		case '\u1ec4':  case '\u1ec5':  case '\u1ec6':  case '\u1ec7':
		case '\u1ec8':  case '\u1ec9':  case '\u1eca':  case '\u1ecb':
		case '\u1ecc':  case '\u1ecd':  case '\u1ece':  case '\u1ecf':
		case '\u1ed0':  case '\u1ed1':  case '\u1ed2':  case '\u1ed3':
		case '\u1ed4':  case '\u1ed5':  case '\u1ed6':  case '\u1ed7':
		case '\u1ed8':  case '\u1ed9':  case '\u1eda':  case '\u1edb':
		case '\u1edc':  case '\u1edd':  case '\u1ede':  case '\u1edf':
		case '\u1ee0':  case '\u1ee1':  case '\u1ee2':  case '\u1ee3':
		case '\u1ee4':  case '\u1ee5':  case '\u1ee6':  case '\u1ee7':
		case '\u1ee8':  case '\u1ee9':  case '\u1eea':  case '\u1eeb':
		case '\u1eec':  case '\u1eed':  case '\u1eee':  case '\u1eef':
		case '\u1ef0':  case '\u1ef1':  case '\u1ef2':  case '\u1ef3':
		case '\u1ef4':  case '\u1ef5':  case '\u1ef6':  case '\u1ef7':
		case '\u1ef8':  case '\u1ef9':
		{
			matchRange('\u1ea0','\u1ef9');
			break;
		}
		case '\u1f00':  case '\u1f01':  case '\u1f02':  case '\u1f03':
		case '\u1f04':  case '\u1f05':  case '\u1f06':  case '\u1f07':
		case '\u1f08':  case '\u1f09':  case '\u1f0a':  case '\u1f0b':
		case '\u1f0c':  case '\u1f0d':  case '\u1f0e':  case '\u1f0f':
		case '\u1f10':  case '\u1f11':  case '\u1f12':  case '\u1f13':
		case '\u1f14':  case '\u1f15':
		{
			matchRange('\u1f00','\u1f15');
			break;
		}
		case '\u1f18':  case '\u1f19':  case '\u1f1a':  case '\u1f1b':
		case '\u1f1c':  case '\u1f1d':
		{
			matchRange('\u1f18','\u1f1d');
			break;
		}
		case '\u1f20':  case '\u1f21':  case '\u1f22':  case '\u1f23':
		case '\u1f24':  case '\u1f25':  case '\u1f26':  case '\u1f27':
		case '\u1f28':  case '\u1f29':  case '\u1f2a':  case '\u1f2b':
		case '\u1f2c':  case '\u1f2d':  case '\u1f2e':  case '\u1f2f':
		case '\u1f30':  case '\u1f31':  case '\u1f32':  case '\u1f33':
		case '\u1f34':  case '\u1f35':  case '\u1f36':  case '\u1f37':
		case '\u1f38':  case '\u1f39':  case '\u1f3a':  case '\u1f3b':
		case '\u1f3c':  case '\u1f3d':  case '\u1f3e':  case '\u1f3f':
		case '\u1f40':  case '\u1f41':  case '\u1f42':  case '\u1f43':
		case '\u1f44':  case '\u1f45':
		{
			matchRange('\u1f20','\u1f45');
			break;
		}
		case '\u1f48':  case '\u1f49':  case '\u1f4a':  case '\u1f4b':
		case '\u1f4c':  case '\u1f4d':
		{
			matchRange('\u1f48','\u1f4d');
			break;
		}
		case '\u1f50':  case '\u1f51':  case '\u1f52':  case '\u1f53':
		case '\u1f54':  case '\u1f55':  case '\u1f56':  case '\u1f57':
		{
			matchRange('\u1f50','\u1f57');
			break;
		}
		case '\u1f59':
		{
			match('\u1f59');
			break;
		}
		case '\u1f5b':
		{
			match('\u1f5b');
			break;
		}
		case '\u1f5d':
		{
			match('\u1f5d');
			break;
		}
		case '\u1f5f':  case '\u1f60':  case '\u1f61':  case '\u1f62':
		case '\u1f63':  case '\u1f64':  case '\u1f65':  case '\u1f66':
		case '\u1f67':  case '\u1f68':  case '\u1f69':  case '\u1f6a':
		case '\u1f6b':  case '\u1f6c':  case '\u1f6d':  case '\u1f6e':
		case '\u1f6f':  case '\u1f70':  case '\u1f71':  case '\u1f72':
		case '\u1f73':  case '\u1f74':  case '\u1f75':  case '\u1f76':
		case '\u1f77':  case '\u1f78':  case '\u1f79':  case '\u1f7a':
		case '\u1f7b':  case '\u1f7c':  case '\u1f7d':
		{
			matchRange('\u1f5f','\u1f7d');
			break;
		}
		case '\u1f80':  case '\u1f81':  case '\u1f82':  case '\u1f83':
		case '\u1f84':  case '\u1f85':  case '\u1f86':  case '\u1f87':
		case '\u1f88':  case '\u1f89':  case '\u1f8a':  case '\u1f8b':
		case '\u1f8c':  case '\u1f8d':  case '\u1f8e':  case '\u1f8f':
		case '\u1f90':  case '\u1f91':  case '\u1f92':  case '\u1f93':
		case '\u1f94':  case '\u1f95':  case '\u1f96':  case '\u1f97':
		case '\u1f98':  case '\u1f99':  case '\u1f9a':  case '\u1f9b':
		case '\u1f9c':  case '\u1f9d':  case '\u1f9e':  case '\u1f9f':
		case '\u1fa0':  case '\u1fa1':  case '\u1fa2':  case '\u1fa3':
		case '\u1fa4':  case '\u1fa5':  case '\u1fa6':  case '\u1fa7':
		case '\u1fa8':  case '\u1fa9':  case '\u1faa':  case '\u1fab':
		case '\u1fac':  case '\u1fad':  case '\u1fae':  case '\u1faf':
		case '\u1fb0':  case '\u1fb1':  case '\u1fb2':  case '\u1fb3':
		case '\u1fb4':
		{
			matchRange('\u1f80','\u1fb4');
			break;
		}
		case '\u1fb6':  case '\u1fb7':  case '\u1fb8':  case '\u1fb9':
		case '\u1fba':  case '\u1fbb':  case '\u1fbc':
		{
			matchRange('\u1fb6','\u1fbc');
			break;
		}
		case '\u1fbe':
		{
			match('\u1fbe');
			break;
		}
		case '\u1fc2':  case '\u1fc3':  case '\u1fc4':
		{
			matchRange('\u1fc2','\u1fc4');
			break;
		}
		case '\u1fc6':  case '\u1fc7':  case '\u1fc8':  case '\u1fc9':
		case '\u1fca':  case '\u1fcb':  case '\u1fcc':
		{
			matchRange('\u1fc6','\u1fcc');
			break;
		}
		case '\u1fd0':  case '\u1fd1':  case '\u1fd2':  case '\u1fd3':
		{
			matchRange('\u1fd0','\u1fd3');
			break;
		}
		case '\u1fd6':  case '\u1fd7':  case '\u1fd8':  case '\u1fd9':
		case '\u1fda':  case '\u1fdb':
		{
			matchRange('\u1fd6','\u1fdb');
			break;
		}
		case '\u1fe0':  case '\u1fe1':  case '\u1fe2':  case '\u1fe3':
		case '\u1fe4':  case '\u1fe5':  case '\u1fe6':  case '\u1fe7':
		case '\u1fe8':  case '\u1fe9':  case '\u1fea':  case '\u1feb':
		case '\u1fec':
		{
			matchRange('\u1fe0','\u1fec');
			break;
		}
		case '\u1ff2':  case '\u1ff3':  case '\u1ff4':
		{
			matchRange('\u1ff2','\u1ff4');
			break;
		}
		case '\u1ff6':  case '\u1ff7':  case '\u1ff8':  case '\u1ff9':
		case '\u1ffa':  case '\u1ffb':  case '\u1ffc':
		{
			matchRange('\u1ff6','\u1ffc');
			break;
		}
		case '\u2071':
		{
			match('\u2071');
			break;
		}
		case '\u207f':
		{
			match('\u207f');
			break;
		}
		case '\u2102':
		{
			match('\u2102');
			break;
		}
		case '\u2107':
		{
			match('\u2107');
			break;
		}
		case '\u210a':  case '\u210b':  case '\u210c':  case '\u210d':
		case '\u210e':  case '\u210f':  case '\u2110':  case '\u2111':
		case '\u2112':  case '\u2113':
		{
			matchRange('\u210a','\u2113');
			break;
		}
		case '\u2115':
		{
			match('\u2115');
			break;
		}
		case '\u2119':  case '\u211a':  case '\u211b':  case '\u211c':
		case '\u211d':
		{
			matchRange('\u2119','\u211d');
			break;
		}
		case '\u2124':
		{
			match('\u2124');
			break;
		}
		case '\u2126':
		{
			match('\u2126');
			break;
		}
		case '\u2128':
		{
			match('\u2128');
			break;
		}
		case '\u212a':  case '\u212b':  case '\u212c':  case '\u212d':
		{
			matchRange('\u212a','\u212d');
			break;
		}
		case '\u212f':  case '\u2130':  case '\u2131':  case '\u2132':
		case '\u2133':  case '\u2134':  case '\u2135':  case '\u2136':
		case '\u2137':  case '\u2138':  case '\u2139':
		{
			matchRange('\u212f','\u2139');
			break;
		}
		case '\u213c':  case '\u213d':  case '\u213e':  case '\u213f':
		{
			matchRange('\u213c','\u213f');
			break;
		}
		case '\u2145':  case '\u2146':  case '\u2147':  case '\u2148':
		case '\u2149':
		{
			matchRange('\u2145','\u2149');
			break;
		}
		case '\u214e':
		{
			match('\u214e');
			break;
		}
		case '\u2183':  case '\u2184':
		{
			matchRange('\u2183','\u2184');
			break;
		}
		case '\u2c00':  case '\u2c01':  case '\u2c02':  case '\u2c03':
		case '\u2c04':  case '\u2c05':  case '\u2c06':  case '\u2c07':
		case '\u2c08':  case '\u2c09':  case '\u2c0a':  case '\u2c0b':
		case '\u2c0c':  case '\u2c0d':  case '\u2c0e':  case '\u2c0f':
		case '\u2c10':  case '\u2c11':  case '\u2c12':  case '\u2c13':
		case '\u2c14':  case '\u2c15':  case '\u2c16':  case '\u2c17':
		case '\u2c18':  case '\u2c19':  case '\u2c1a':  case '\u2c1b':
		case '\u2c1c':  case '\u2c1d':  case '\u2c1e':  case '\u2c1f':
		case '\u2c20':  case '\u2c21':  case '\u2c22':  case '\u2c23':
		case '\u2c24':  case '\u2c25':  case '\u2c26':  case '\u2c27':
		case '\u2c28':  case '\u2c29':  case '\u2c2a':  case '\u2c2b':
		case '\u2c2c':  case '\u2c2d':  case '\u2c2e':
		{
			matchRange('\u2c00','\u2c2e');
			break;
		}
		case '\u2c30':  case '\u2c31':  case '\u2c32':  case '\u2c33':
		case '\u2c34':  case '\u2c35':  case '\u2c36':  case '\u2c37':
		case '\u2c38':  case '\u2c39':  case '\u2c3a':  case '\u2c3b':
		case '\u2c3c':  case '\u2c3d':  case '\u2c3e':  case '\u2c3f':
		case '\u2c40':  case '\u2c41':  case '\u2c42':  case '\u2c43':
		case '\u2c44':  case '\u2c45':  case '\u2c46':  case '\u2c47':
		case '\u2c48':  case '\u2c49':  case '\u2c4a':  case '\u2c4b':
		case '\u2c4c':  case '\u2c4d':  case '\u2c4e':  case '\u2c4f':
		case '\u2c50':  case '\u2c51':  case '\u2c52':  case '\u2c53':
		case '\u2c54':  case '\u2c55':  case '\u2c56':  case '\u2c57':
		case '\u2c58':  case '\u2c59':  case '\u2c5a':  case '\u2c5b':
		case '\u2c5c':  case '\u2c5d':  case '\u2c5e':
		{
			matchRange('\u2c30','\u2c5e');
			break;
		}
		case '\u2c60':  case '\u2c61':  case '\u2c62':  case '\u2c63':
		case '\u2c64':  case '\u2c65':  case '\u2c66':  case '\u2c67':
		case '\u2c68':  case '\u2c69':  case '\u2c6a':  case '\u2c6b':
		case '\u2c6c':
		{
			matchRange('\u2c60','\u2c6c');
			break;
		}
		case '\u2c74':  case '\u2c75':  case '\u2c76':  case '\u2c77':
		{
			matchRange('\u2c74','\u2c77');
			break;
		}
		case '\u2c80':  case '\u2c81':  case '\u2c82':  case '\u2c83':
		case '\u2c84':  case '\u2c85':  case '\u2c86':  case '\u2c87':
		case '\u2c88':  case '\u2c89':  case '\u2c8a':  case '\u2c8b':
		case '\u2c8c':  case '\u2c8d':  case '\u2c8e':  case '\u2c8f':
		case '\u2c90':  case '\u2c91':  case '\u2c92':  case '\u2c93':
		case '\u2c94':  case '\u2c95':  case '\u2c96':  case '\u2c97':
		case '\u2c98':  case '\u2c99':  case '\u2c9a':  case '\u2c9b':
		case '\u2c9c':  case '\u2c9d':  case '\u2c9e':  case '\u2c9f':
		case '\u2ca0':  case '\u2ca1':  case '\u2ca2':  case '\u2ca3':
		case '\u2ca4':  case '\u2ca5':  case '\u2ca6':  case '\u2ca7':
		case '\u2ca8':  case '\u2ca9':  case '\u2caa':  case '\u2cab':
		case '\u2cac':  case '\u2cad':  case '\u2cae':  case '\u2caf':
		case '\u2cb0':  case '\u2cb1':  case '\u2cb2':  case '\u2cb3':
		case '\u2cb4':  case '\u2cb5':  case '\u2cb6':  case '\u2cb7':
		case '\u2cb8':  case '\u2cb9':  case '\u2cba':  case '\u2cbb':
		case '\u2cbc':  case '\u2cbd':  case '\u2cbe':  case '\u2cbf':
		case '\u2cc0':  case '\u2cc1':  case '\u2cc2':  case '\u2cc3':
		case '\u2cc4':  case '\u2cc5':  case '\u2cc6':  case '\u2cc7':
		case '\u2cc8':  case '\u2cc9':  case '\u2cca':  case '\u2ccb':
		case '\u2ccc':  case '\u2ccd':  case '\u2cce':  case '\u2ccf':
		case '\u2cd0':  case '\u2cd1':  case '\u2cd2':  case '\u2cd3':
		case '\u2cd4':  case '\u2cd5':  case '\u2cd6':  case '\u2cd7':
		case '\u2cd8':  case '\u2cd9':  case '\u2cda':  case '\u2cdb':
		case '\u2cdc':  case '\u2cdd':  case '\u2cde':  case '\u2cdf':
		case '\u2ce0':  case '\u2ce1':  case '\u2ce2':  case '\u2ce3':
		case '\u2ce4':
		{
			matchRange('\u2c80','\u2ce4');
			break;
		}
		case '\u2d00':  case '\u2d01':  case '\u2d02':  case '\u2d03':
		case '\u2d04':  case '\u2d05':  case '\u2d06':  case '\u2d07':
		case '\u2d08':  case '\u2d09':  case '\u2d0a':  case '\u2d0b':
		case '\u2d0c':  case '\u2d0d':  case '\u2d0e':  case '\u2d0f':
		case '\u2d10':  case '\u2d11':  case '\u2d12':  case '\u2d13':
		case '\u2d14':  case '\u2d15':  case '\u2d16':  case '\u2d17':
		case '\u2d18':  case '\u2d19':  case '\u2d1a':  case '\u2d1b':
		case '\u2d1c':  case '\u2d1d':  case '\u2d1e':  case '\u2d1f':
		case '\u2d20':  case '\u2d21':  case '\u2d22':  case '\u2d23':
		case '\u2d24':  case '\u2d25':
		{
			matchRange('\u2d00','\u2d25');
			break;
		}
		case '\u2d30':  case '\u2d31':  case '\u2d32':  case '\u2d33':
		case '\u2d34':  case '\u2d35':  case '\u2d36':  case '\u2d37':
		case '\u2d38':  case '\u2d39':  case '\u2d3a':  case '\u2d3b':
		case '\u2d3c':  case '\u2d3d':  case '\u2d3e':  case '\u2d3f':
		case '\u2d40':  case '\u2d41':  case '\u2d42':  case '\u2d43':
		case '\u2d44':  case '\u2d45':  case '\u2d46':  case '\u2d47':
		case '\u2d48':  case '\u2d49':  case '\u2d4a':  case '\u2d4b':
		case '\u2d4c':  case '\u2d4d':  case '\u2d4e':  case '\u2d4f':
		case '\u2d50':  case '\u2d51':  case '\u2d52':  case '\u2d53':
		case '\u2d54':  case '\u2d55':  case '\u2d56':  case '\u2d57':
		case '\u2d58':  case '\u2d59':  case '\u2d5a':  case '\u2d5b':
		case '\u2d5c':  case '\u2d5d':  case '\u2d5e':  case '\u2d5f':
		case '\u2d60':  case '\u2d61':  case '\u2d62':  case '\u2d63':
		case '\u2d64':  case '\u2d65':
		{
			matchRange('\u2d30','\u2d65');
			break;
		}
		case '\u2d80':  case '\u2d81':  case '\u2d82':  case '\u2d83':
		case '\u2d84':  case '\u2d85':  case '\u2d86':  case '\u2d87':
		case '\u2d88':  case '\u2d89':  case '\u2d8a':  case '\u2d8b':
		case '\u2d8c':  case '\u2d8d':  case '\u2d8e':  case '\u2d8f':
		case '\u2d90':  case '\u2d91':  case '\u2d92':  case '\u2d93':
		case '\u2d94':  case '\u2d95':  case '\u2d96':
		{
			matchRange('\u2d80','\u2d96');
			break;
		}
		case '\u2da0':  case '\u2da1':  case '\u2da2':  case '\u2da3':
		case '\u2da4':  case '\u2da5':  case '\u2da6':
		{
			matchRange('\u2da0','\u2da6');
			break;
		}
		case '\u2da8':  case '\u2da9':  case '\u2daa':  case '\u2dab':
		case '\u2dac':  case '\u2dad':  case '\u2dae':
		{
			matchRange('\u2da8','\u2dae');
			break;
		}
		case '\u2db0':  case '\u2db1':  case '\u2db2':  case '\u2db3':
		case '\u2db4':  case '\u2db5':  case '\u2db6':
		{
			matchRange('\u2db0','\u2db6');
			break;
		}
		case '\u2db8':  case '\u2db9':  case '\u2dba':  case '\u2dbb':
		case '\u2dbc':  case '\u2dbd':  case '\u2dbe':
		{
			matchRange('\u2db8','\u2dbe');
			break;
		}
		case '\u2dc0':  case '\u2dc1':  case '\u2dc2':  case '\u2dc3':
		case '\u2dc4':  case '\u2dc5':  case '\u2dc6':
		{
			matchRange('\u2dc0','\u2dc6');
			break;
		}
		case '\u2dc8':  case '\u2dc9':  case '\u2dca':  case '\u2dcb':
		case '\u2dcc':  case '\u2dcd':  case '\u2dce':
		{
			matchRange('\u2dc8','\u2dce');
			break;
		}
		case '\u2dd0':  case '\u2dd1':  case '\u2dd2':  case '\u2dd3':
		case '\u2dd4':  case '\u2dd5':  case '\u2dd6':
		{
			matchRange('\u2dd0','\u2dd6');
			break;
		}
		case '\u2dd8':  case '\u2dd9':  case '\u2dda':  case '\u2ddb':
		case '\u2ddc':  case '\u2ddd':  case '\u2dde':
		{
			matchRange('\u2dd8','\u2dde');
			break;
		}
		case '\u3006':
		{
			match('\u3006');
			break;
		}
		case '\u303c':
		{
			match('\u303c');
			break;
		}
		case '\u3041':  case '\u3042':  case '\u3043':  case '\u3044':
		case '\u3045':  case '\u3046':  case '\u3047':  case '\u3048':
		case '\u3049':  case '\u304a':  case '\u304b':  case '\u304c':
		case '\u304d':  case '\u304e':  case '\u304f':  case '\u3050':
		case '\u3051':  case '\u3052':  case '\u3053':  case '\u3054':
		case '\u3055':  case '\u3056':  case '\u3057':  case '\u3058':
		case '\u3059':  case '\u305a':  case '\u305b':  case '\u305c':
		case '\u305d':  case '\u305e':  case '\u305f':  case '\u3060':
		case '\u3061':  case '\u3062':  case '\u3063':  case '\u3064':
		case '\u3065':  case '\u3066':  case '\u3067':  case '\u3068':
		case '\u3069':  case '\u306a':  case '\u306b':  case '\u306c':
		case '\u306d':  case '\u306e':  case '\u306f':  case '\u3070':
		case '\u3071':  case '\u3072':  case '\u3073':  case '\u3074':
		case '\u3075':  case '\u3076':  case '\u3077':  case '\u3078':
		case '\u3079':  case '\u307a':  case '\u307b':  case '\u307c':
		case '\u307d':  case '\u307e':  case '\u307f':  case '\u3080':
		case '\u3081':  case '\u3082':  case '\u3083':  case '\u3084':
		case '\u3085':  case '\u3086':  case '\u3087':  case '\u3088':
		case '\u3089':  case '\u308a':  case '\u308b':  case '\u308c':
		case '\u308d':  case '\u308e':  case '\u308f':  case '\u3090':
		case '\u3091':  case '\u3092':  case '\u3093':  case '\u3094':
		case '\u3095':  case '\u3096':
		{
			matchRange('\u3041','\u3096');
			break;
		}
		case '\u309f':
		{
			match('\u309f');
			break;
		}
		case '\u30a1':  case '\u30a2':  case '\u30a3':  case '\u30a4':
		case '\u30a5':  case '\u30a6':  case '\u30a7':  case '\u30a8':
		case '\u30a9':  case '\u30aa':  case '\u30ab':  case '\u30ac':
		case '\u30ad':  case '\u30ae':  case '\u30af':  case '\u30b0':
		case '\u30b1':  case '\u30b2':  case '\u30b3':  case '\u30b4':
		case '\u30b5':  case '\u30b6':  case '\u30b7':  case '\u30b8':
		case '\u30b9':  case '\u30ba':  case '\u30bb':  case '\u30bc':
		case '\u30bd':  case '\u30be':  case '\u30bf':  case '\u30c0':
		case '\u30c1':  case '\u30c2':  case '\u30c3':  case '\u30c4':
		case '\u30c5':  case '\u30c6':  case '\u30c7':  case '\u30c8':
		case '\u30c9':  case '\u30ca':  case '\u30cb':  case '\u30cc':
		case '\u30cd':  case '\u30ce':  case '\u30cf':  case '\u30d0':
		case '\u30d1':  case '\u30d2':  case '\u30d3':  case '\u30d4':
		case '\u30d5':  case '\u30d6':  case '\u30d7':  case '\u30d8':
		case '\u30d9':  case '\u30da':  case '\u30db':  case '\u30dc':
		case '\u30dd':  case '\u30de':  case '\u30df':  case '\u30e0':
		case '\u30e1':  case '\u30e2':  case '\u30e3':  case '\u30e4':
		case '\u30e5':  case '\u30e6':  case '\u30e7':  case '\u30e8':
		case '\u30e9':  case '\u30ea':  case '\u30eb':  case '\u30ec':
		case '\u30ed':  case '\u30ee':  case '\u30ef':  case '\u30f0':
		case '\u30f1':  case '\u30f2':  case '\u30f3':  case '\u30f4':
		case '\u30f5':  case '\u30f6':  case '\u30f7':  case '\u30f8':
		case '\u30f9':  case '\u30fa':
		{
			matchRange('\u30a1','\u30fa');
			break;
		}
		case '\u30ff':
		{
			match('\u30ff');
			break;
		}
		case '\u3105':  case '\u3106':  case '\u3107':  case '\u3108':
		case '\u3109':  case '\u310a':  case '\u310b':  case '\u310c':
		case '\u310d':  case '\u310e':  case '\u310f':  case '\u3110':
		case '\u3111':  case '\u3112':  case '\u3113':  case '\u3114':
		case '\u3115':  case '\u3116':  case '\u3117':  case '\u3118':
		case '\u3119':  case '\u311a':  case '\u311b':  case '\u311c':
		case '\u311d':  case '\u311e':  case '\u311f':  case '\u3120':
		case '\u3121':  case '\u3122':  case '\u3123':  case '\u3124':
		case '\u3125':  case '\u3126':  case '\u3127':  case '\u3128':
		case '\u3129':  case '\u312a':  case '\u312b':  case '\u312c':
		{
			matchRange('\u3105','\u312c');
			break;
		}
		case '\u3131':  case '\u3132':  case '\u3133':  case '\u3134':
		case '\u3135':  case '\u3136':  case '\u3137':  case '\u3138':
		case '\u3139':  case '\u313a':  case '\u313b':  case '\u313c':
		case '\u313d':  case '\u313e':  case '\u313f':  case '\u3140':
		case '\u3141':  case '\u3142':  case '\u3143':  case '\u3144':
		case '\u3145':  case '\u3146':  case '\u3147':  case '\u3148':
		case '\u3149':  case '\u314a':  case '\u314b':  case '\u314c':
		case '\u314d':  case '\u314e':  case '\u314f':  case '\u3150':
		case '\u3151':  case '\u3152':  case '\u3153':  case '\u3154':
		case '\u3155':  case '\u3156':  case '\u3157':  case '\u3158':
		case '\u3159':  case '\u315a':  case '\u315b':  case '\u315c':
		case '\u315d':  case '\u315e':  case '\u315f':  case '\u3160':
		case '\u3161':  case '\u3162':  case '\u3163':  case '\u3164':
		case '\u3165':  case '\u3166':  case '\u3167':  case '\u3168':
		case '\u3169':  case '\u316a':  case '\u316b':  case '\u316c':
		case '\u316d':  case '\u316e':  case '\u316f':  case '\u3170':
		case '\u3171':  case '\u3172':  case '\u3173':  case '\u3174':
		case '\u3175':  case '\u3176':  case '\u3177':  case '\u3178':
		case '\u3179':  case '\u317a':  case '\u317b':  case '\u317c':
		case '\u317d':  case '\u317e':  case '\u317f':  case '\u3180':
		case '\u3181':  case '\u3182':  case '\u3183':  case '\u3184':
		case '\u3185':  case '\u3186':  case '\u3187':  case '\u3188':
		case '\u3189':  case '\u318a':  case '\u318b':  case '\u318c':
		case '\u318d':  case '\u318e':
		{
			matchRange('\u3131','\u318e');
			break;
		}
		case '\u31a0':  case '\u31a1':  case '\u31a2':  case '\u31a3':
		case '\u31a4':  case '\u31a5':  case '\u31a6':  case '\u31a7':
		case '\u31a8':  case '\u31a9':  case '\u31aa':  case '\u31ab':
		case '\u31ac':  case '\u31ad':  case '\u31ae':  case '\u31af':
		case '\u31b0':  case '\u31b1':  case '\u31b2':  case '\u31b3':
		case '\u31b4':  case '\u31b5':  case '\u31b6':  case '\u31b7':
		{
			matchRange('\u31a0','\u31b7');
			break;
		}
		case '\u31f0':  case '\u31f1':  case '\u31f2':  case '\u31f3':
		case '\u31f4':  case '\u31f5':  case '\u31f6':  case '\u31f7':
		case '\u31f8':  case '\u31f9':  case '\u31fa':  case '\u31fb':
		case '\u31fc':  case '\u31fd':  case '\u31fe':  case '\u31ff':
		{
			matchRange('\u31f0','\u31ff');
			break;
		}
		case '\u3400':
		{
			match('\u3400');
			break;
		}
		case '\u4db5':
		{
			match('\u4db5');
			break;
		}
		case '\u4e00':
		{
			match('\u4e00');
			break;
		}
		case '\u9fbb':
		{
			match('\u9fbb');
			break;
		}
		case '\ua000':  case '\ua001':  case '\ua002':  case '\ua003':
		case '\ua004':  case '\ua005':  case '\ua006':  case '\ua007':
		case '\ua008':  case '\ua009':  case '\ua00a':  case '\ua00b':
		case '\ua00c':  case '\ua00d':  case '\ua00e':  case '\ua00f':
		case '\ua010':  case '\ua011':  case '\ua012':  case '\ua013':
		case '\ua014':
		{
			matchRange('\ua000','\ua014');
			break;
		}
		case '\ua800':  case '\ua801':
		{
			matchRange('\ua800','\ua801');
			break;
		}
		case '\ua803':  case '\ua804':  case '\ua805':
		{
			matchRange('\ua803','\ua805');
			break;
		}
		case '\ua807':  case '\ua808':  case '\ua809':  case '\ua80a':
		{
			matchRange('\ua807','\ua80a');
			break;
		}
		case '\ua80c':  case '\ua80d':  case '\ua80e':  case '\ua80f':
		case '\ua810':  case '\ua811':  case '\ua812':  case '\ua813':
		case '\ua814':  case '\ua815':  case '\ua816':  case '\ua817':
		case '\ua818':  case '\ua819':  case '\ua81a':  case '\ua81b':
		case '\ua81c':  case '\ua81d':  case '\ua81e':  case '\ua81f':
		case '\ua820':  case '\ua821':  case '\ua822':
		{
			matchRange('\ua80c','\ua822');
			break;
		}
		case '\ua840':  case '\ua841':  case '\ua842':  case '\ua843':
		case '\ua844':  case '\ua845':  case '\ua846':  case '\ua847':
		case '\ua848':  case '\ua849':  case '\ua84a':  case '\ua84b':
		case '\ua84c':  case '\ua84d':  case '\ua84e':  case '\ua84f':
		case '\ua850':  case '\ua851':  case '\ua852':  case '\ua853':
		case '\ua854':  case '\ua855':  case '\ua856':  case '\ua857':
		case '\ua858':  case '\ua859':  case '\ua85a':  case '\ua85b':
		case '\ua85c':  case '\ua85d':  case '\ua85e':  case '\ua85f':
		case '\ua860':  case '\ua861':  case '\ua862':  case '\ua863':
		case '\ua864':  case '\ua865':  case '\ua866':  case '\ua867':
		case '\ua868':  case '\ua869':  case '\ua86a':  case '\ua86b':
		case '\ua86c':  case '\ua86d':  case '\ua86e':  case '\ua86f':
		case '\ua870':  case '\ua871':  case '\ua872':  case '\ua873':
		{
			matchRange('\ua840','\ua873');
			break;
		}
		case '\uac00':
		{
			match('\uac00');
			break;
		}
		case '\ud7a3':
		{
			match('\ud7a3');
			break;
		}
		case '\ufa30':  case '\ufa31':  case '\ufa32':  case '\ufa33':
		case '\ufa34':  case '\ufa35':  case '\ufa36':  case '\ufa37':
		case '\ufa38':  case '\ufa39':  case '\ufa3a':  case '\ufa3b':
		case '\ufa3c':  case '\ufa3d':  case '\ufa3e':  case '\ufa3f':
		case '\ufa40':  case '\ufa41':  case '\ufa42':  case '\ufa43':
		case '\ufa44':  case '\ufa45':  case '\ufa46':  case '\ufa47':
		case '\ufa48':  case '\ufa49':  case '\ufa4a':  case '\ufa4b':
		case '\ufa4c':  case '\ufa4d':  case '\ufa4e':  case '\ufa4f':
		case '\ufa50':  case '\ufa51':  case '\ufa52':  case '\ufa53':
		case '\ufa54':  case '\ufa55':  case '\ufa56':  case '\ufa57':
		case '\ufa58':  case '\ufa59':  case '\ufa5a':  case '\ufa5b':
		case '\ufa5c':  case '\ufa5d':  case '\ufa5e':  case '\ufa5f':
		case '\ufa60':  case '\ufa61':  case '\ufa62':  case '\ufa63':
		case '\ufa64':  case '\ufa65':  case '\ufa66':  case '\ufa67':
		case '\ufa68':  case '\ufa69':  case '\ufa6a':
		{
			matchRange('\ufa30','\ufa6a');
			break;
		}
		case '\ufa70':  case '\ufa71':  case '\ufa72':  case '\ufa73':
		case '\ufa74':  case '\ufa75':  case '\ufa76':  case '\ufa77':
		case '\ufa78':  case '\ufa79':  case '\ufa7a':  case '\ufa7b':
		case '\ufa7c':  case '\ufa7d':  case '\ufa7e':  case '\ufa7f':
		case '\ufa80':  case '\ufa81':  case '\ufa82':  case '\ufa83':
		case '\ufa84':  case '\ufa85':  case '\ufa86':  case '\ufa87':
		case '\ufa88':  case '\ufa89':  case '\ufa8a':  case '\ufa8b':
		case '\ufa8c':  case '\ufa8d':  case '\ufa8e':  case '\ufa8f':
		case '\ufa90':  case '\ufa91':  case '\ufa92':  case '\ufa93':
		case '\ufa94':  case '\ufa95':  case '\ufa96':  case '\ufa97':
		case '\ufa98':  case '\ufa99':  case '\ufa9a':  case '\ufa9b':
		case '\ufa9c':  case '\ufa9d':  case '\ufa9e':  case '\ufa9f':
		case '\ufaa0':  case '\ufaa1':  case '\ufaa2':  case '\ufaa3':
		case '\ufaa4':  case '\ufaa5':  case '\ufaa6':  case '\ufaa7':
		case '\ufaa8':  case '\ufaa9':  case '\ufaaa':  case '\ufaab':
		case '\ufaac':  case '\ufaad':  case '\ufaae':  case '\ufaaf':
		case '\ufab0':  case '\ufab1':  case '\ufab2':  case '\ufab3':
		case '\ufab4':  case '\ufab5':  case '\ufab6':  case '\ufab7':
		case '\ufab8':  case '\ufab9':  case '\ufaba':  case '\ufabb':
		case '\ufabc':  case '\ufabd':  case '\ufabe':  case '\ufabf':
		case '\ufac0':  case '\ufac1':  case '\ufac2':  case '\ufac3':
		case '\ufac4':  case '\ufac5':  case '\ufac6':  case '\ufac7':
		case '\ufac8':  case '\ufac9':  case '\ufaca':  case '\ufacb':
		case '\ufacc':  case '\ufacd':  case '\uface':  case '\ufacf':
		case '\ufad0':  case '\ufad1':  case '\ufad2':  case '\ufad3':
		case '\ufad4':  case '\ufad5':  case '\ufad6':  case '\ufad7':
		case '\ufad8':  case '\ufad9':
		{
			matchRange('\ufa70','\ufad9');
			break;
		}
		case '\ufb00':  case '\ufb01':  case '\ufb02':  case '\ufb03':
		case '\ufb04':  case '\ufb05':  case '\ufb06':
		{
			matchRange('\ufb00','\ufb06');
			break;
		}
		case '\ufb13':  case '\ufb14':  case '\ufb15':  case '\ufb16':
		case '\ufb17':
		{
			matchRange('\ufb13','\ufb17');
			break;
		}
		case '\ufb1d':
		{
			match('\ufb1d');
			break;
		}
		case '\ufb1f':  case '\ufb20':  case '\ufb21':  case '\ufb22':
		case '\ufb23':  case '\ufb24':  case '\ufb25':  case '\ufb26':
		case '\ufb27':  case '\ufb28':
		{
			matchRange('\ufb1f','\ufb28');
			break;
		}
		case '\ufb2a':  case '\ufb2b':  case '\ufb2c':  case '\ufb2d':
		case '\ufb2e':  case '\ufb2f':  case '\ufb30':  case '\ufb31':
		case '\ufb32':  case '\ufb33':  case '\ufb34':  case '\ufb35':
		case '\ufb36':
		{
			matchRange('\ufb2a','\ufb36');
			break;
		}
		case '\ufb38':  case '\ufb39':  case '\ufb3a':  case '\ufb3b':
		case '\ufb3c':
		{
			matchRange('\ufb38','\ufb3c');
			break;
		}
		case '\ufb3e':
		{
			match('\ufb3e');
			break;
		}
		case '\ufb40':  case '\ufb41':
		{
			matchRange('\ufb40','\ufb41');
			break;
		}
		case '\ufb43':  case '\ufb44':
		{
			matchRange('\ufb43','\ufb44');
			break;
		}
		case '\ufb46':  case '\ufb47':  case '\ufb48':  case '\ufb49':
		case '\ufb4a':  case '\ufb4b':  case '\ufb4c':  case '\ufb4d':
		case '\ufb4e':  case '\ufb4f':  case '\ufb50':  case '\ufb51':
		case '\ufb52':  case '\ufb53':  case '\ufb54':  case '\ufb55':
		case '\ufb56':  case '\ufb57':  case '\ufb58':  case '\ufb59':
		case '\ufb5a':  case '\ufb5b':  case '\ufb5c':  case '\ufb5d':
		case '\ufb5e':  case '\ufb5f':  case '\ufb60':  case '\ufb61':
		case '\ufb62':  case '\ufb63':  case '\ufb64':  case '\ufb65':
		case '\ufb66':  case '\ufb67':  case '\ufb68':  case '\ufb69':
		case '\ufb6a':  case '\ufb6b':  case '\ufb6c':  case '\ufb6d':
		case '\ufb6e':  case '\ufb6f':  case '\ufb70':  case '\ufb71':
		case '\ufb72':  case '\ufb73':  case '\ufb74':  case '\ufb75':
		case '\ufb76':  case '\ufb77':  case '\ufb78':  case '\ufb79':
		case '\ufb7a':  case '\ufb7b':  case '\ufb7c':  case '\ufb7d':
		case '\ufb7e':  case '\ufb7f':  case '\ufb80':  case '\ufb81':
		case '\ufb82':  case '\ufb83':  case '\ufb84':  case '\ufb85':
		case '\ufb86':  case '\ufb87':  case '\ufb88':  case '\ufb89':
		case '\ufb8a':  case '\ufb8b':  case '\ufb8c':  case '\ufb8d':
		case '\ufb8e':  case '\ufb8f':  case '\ufb90':  case '\ufb91':
		case '\ufb92':  case '\ufb93':  case '\ufb94':  case '\ufb95':
		case '\ufb96':  case '\ufb97':  case '\ufb98':  case '\ufb99':
		case '\ufb9a':  case '\ufb9b':  case '\ufb9c':  case '\ufb9d':
		case '\ufb9e':  case '\ufb9f':  case '\ufba0':  case '\ufba1':
		case '\ufba2':  case '\ufba3':  case '\ufba4':  case '\ufba5':
		case '\ufba6':  case '\ufba7':  case '\ufba8':  case '\ufba9':
		case '\ufbaa':  case '\ufbab':  case '\ufbac':  case '\ufbad':
		case '\ufbae':  case '\ufbaf':  case '\ufbb0':  case '\ufbb1':
		{
			matchRange('\ufb46','\ufbb1');
			break;
		}
		case '\ufd50':  case '\ufd51':  case '\ufd52':  case '\ufd53':
		case '\ufd54':  case '\ufd55':  case '\ufd56':  case '\ufd57':
		case '\ufd58':  case '\ufd59':  case '\ufd5a':  case '\ufd5b':
		case '\ufd5c':  case '\ufd5d':  case '\ufd5e':  case '\ufd5f':
		case '\ufd60':  case '\ufd61':  case '\ufd62':  case '\ufd63':
		case '\ufd64':  case '\ufd65':  case '\ufd66':  case '\ufd67':
		case '\ufd68':  case '\ufd69':  case '\ufd6a':  case '\ufd6b':
		case '\ufd6c':  case '\ufd6d':  case '\ufd6e':  case '\ufd6f':
		case '\ufd70':  case '\ufd71':  case '\ufd72':  case '\ufd73':
		case '\ufd74':  case '\ufd75':  case '\ufd76':  case '\ufd77':
		case '\ufd78':  case '\ufd79':  case '\ufd7a':  case '\ufd7b':
		case '\ufd7c':  case '\ufd7d':  case '\ufd7e':  case '\ufd7f':
		case '\ufd80':  case '\ufd81':  case '\ufd82':  case '\ufd83':
		case '\ufd84':  case '\ufd85':  case '\ufd86':  case '\ufd87':
		case '\ufd88':  case '\ufd89':  case '\ufd8a':  case '\ufd8b':
		case '\ufd8c':  case '\ufd8d':  case '\ufd8e':  case '\ufd8f':
		{
			matchRange('\ufd50','\ufd8f');
			break;
		}
		case '\ufd92':  case '\ufd93':  case '\ufd94':  case '\ufd95':
		case '\ufd96':  case '\ufd97':  case '\ufd98':  case '\ufd99':
		case '\ufd9a':  case '\ufd9b':  case '\ufd9c':  case '\ufd9d':
		case '\ufd9e':  case '\ufd9f':  case '\ufda0':  case '\ufda1':
		case '\ufda2':  case '\ufda3':  case '\ufda4':  case '\ufda5':
		case '\ufda6':  case '\ufda7':  case '\ufda8':  case '\ufda9':
		case '\ufdaa':  case '\ufdab':  case '\ufdac':  case '\ufdad':
		case '\ufdae':  case '\ufdaf':  case '\ufdb0':  case '\ufdb1':
		case '\ufdb2':  case '\ufdb3':  case '\ufdb4':  case '\ufdb5':
		case '\ufdb6':  case '\ufdb7':  case '\ufdb8':  case '\ufdb9':
		case '\ufdba':  case '\ufdbb':  case '\ufdbc':  case '\ufdbd':
		case '\ufdbe':  case '\ufdbf':  case '\ufdc0':  case '\ufdc1':
		case '\ufdc2':  case '\ufdc3':  case '\ufdc4':  case '\ufdc5':
		case '\ufdc6':  case '\ufdc7':
		{
			matchRange('\ufd92','\ufdc7');
			break;
		}
		case '\ufdf0':  case '\ufdf1':  case '\ufdf2':  case '\ufdf3':
		case '\ufdf4':  case '\ufdf5':  case '\ufdf6':  case '\ufdf7':
		case '\ufdf8':  case '\ufdf9':  case '\ufdfa':  case '\ufdfb':
		{
			matchRange('\ufdf0','\ufdfb');
			break;
		}
		case '\ufe70':  case '\ufe71':  case '\ufe72':  case '\ufe73':
		case '\ufe74':
		{
			matchRange('\ufe70','\ufe74');
			break;
		}
		case '\uff21':  case '\uff22':  case '\uff23':  case '\uff24':
		case '\uff25':  case '\uff26':  case '\uff27':  case '\uff28':
		case '\uff29':  case '\uff2a':  case '\uff2b':  case '\uff2c':
		case '\uff2d':  case '\uff2e':  case '\uff2f':  case '\uff30':
		case '\uff31':  case '\uff32':  case '\uff33':  case '\uff34':
		case '\uff35':  case '\uff36':  case '\uff37':  case '\uff38':
		case '\uff39':  case '\uff3a':
		{
			matchRange('\uff21','\uff3a');
			break;
		}
		case '\uff41':  case '\uff42':  case '\uff43':  case '\uff44':
		case '\uff45':  case '\uff46':  case '\uff47':  case '\uff48':
		case '\uff49':  case '\uff4a':  case '\uff4b':  case '\uff4c':
		case '\uff4d':  case '\uff4e':  case '\uff4f':  case '\uff50':
		case '\uff51':  case '\uff52':  case '\uff53':  case '\uff54':
		case '\uff55':  case '\uff56':  case '\uff57':  case '\uff58':
		case '\uff59':  case '\uff5a':
		{
			matchRange('\uff41','\uff5a');
			break;
		}
		case '\uff66':  case '\uff67':  case '\uff68':  case '\uff69':
		case '\uff6a':  case '\uff6b':  case '\uff6c':  case '\uff6d':
		case '\uff6e':  case '\uff6f':
		{
			matchRange('\uff66','\uff6f');
			break;
		}
		case '\uff71':  case '\uff72':  case '\uff73':  case '\uff74':
		case '\uff75':  case '\uff76':  case '\uff77':  case '\uff78':
		case '\uff79':  case '\uff7a':  case '\uff7b':  case '\uff7c':
		case '\uff7d':  case '\uff7e':  case '\uff7f':  case '\uff80':
		case '\uff81':  case '\uff82':  case '\uff83':  case '\uff84':
		case '\uff85':  case '\uff86':  case '\uff87':  case '\uff88':
		case '\uff89':  case '\uff8a':  case '\uff8b':  case '\uff8c':
		case '\uff8d':  case '\uff8e':  case '\uff8f':  case '\uff90':
		case '\uff91':  case '\uff92':  case '\uff93':  case '\uff94':
		case '\uff95':  case '\uff96':  case '\uff97':  case '\uff98':
		case '\uff99':  case '\uff9a':  case '\uff9b':  case '\uff9c':
		case '\uff9d':
		{
			matchRange('\uff71','\uff9d');
			break;
		}
		case '\uffa0':  case '\uffa1':  case '\uffa2':  case '\uffa3':
		case '\uffa4':  case '\uffa5':  case '\uffa6':  case '\uffa7':
		case '\uffa8':  case '\uffa9':  case '\uffaa':  case '\uffab':
		case '\uffac':  case '\uffad':  case '\uffae':  case '\uffaf':
		case '\uffb0':  case '\uffb1':  case '\uffb2':  case '\uffb3':
		case '\uffb4':  case '\uffb5':  case '\uffb6':  case '\uffb7':
		case '\uffb8':  case '\uffb9':  case '\uffba':  case '\uffbb':
		case '\uffbc':  case '\uffbd':  case '\uffbe':
		{
			matchRange('\uffa0','\uffbe');
			break;
		}
		case '\uffc2':  case '\uffc3':  case '\uffc4':  case '\uffc5':
		case '\uffc6':  case '\uffc7':
		{
			matchRange('\uffc2','\uffc7');
			break;
		}
		case '\uffca':  case '\uffcb':  case '\uffcc':  case '\uffcd':
		case '\uffce':  case '\uffcf':
		{
			matchRange('\uffca','\uffcf');
			break;
		}
		case '\uffd2':  case '\uffd3':  case '\uffd4':  case '\uffd5':
		case '\uffd6':  case '\uffd7':
		{
			matchRange('\uffd2','\uffd7');
			break;
		}
		case '\uffda':  case '\uffdb':  case '\uffdc':
		{
			matchRange('\uffda','\uffdc');
			break;
		}
		default:
			if (((LA(1) >= '\u00f8' && LA(1) <= '\u02af'))) {
				matchRange('\u00f8','\u02af');
			}
			else if (((LA(1) >= '\u03f7' && LA(1) <= '\u0481'))) {
				matchRange('\u03f7','\u0481');
			}
			else if (((LA(1) >= '\u048a' && LA(1) <= '\u0513'))) {
				matchRange('\u048a','\u0513');
			}
			else if (((LA(1) >= '\u1401' && LA(1) <= '\u166c'))) {
				matchRange('\u1401','\u166c');
			}
			else if (((LA(1) >= '\u1e00' && LA(1) <= '\u1e9b'))) {
				matchRange('\u1e00','\u1e9b');
			}
			else if (((LA(1) >= '\ua016' && LA(1) <= '\ua48c'))) {
				matchRange('\ua016','\ua48c');
			}
			else if (((LA(1) >= '\uf900' && LA(1) <= '\ufa2d'))) {
				matchRange('\uf900','\ufa2d');
			}
			else if (((LA(1) >= '\ufbd3' && LA(1) <= '\ufd3d'))) {
				matchRange('\ufbd3','\ufd3d');
			}
			else if (((LA(1) >= '\ufe76' && LA(1) <= '\ufefc'))) {
				matchRange('\ufe76','\ufefc');
			}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mDIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DIGIT;
		int _saveIndex;
		
		switch ( LA(1)) {
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		case '8':  case '9':
		{
			matchRange('\u0030','\u0039');
			break;
		}
		case '\u0660':  case '\u0661':  case '\u0662':  case '\u0663':
		case '\u0664':  case '\u0665':  case '\u0666':  case '\u0667':
		case '\u0668':  case '\u0669':
		{
			matchRange('\u0660','\u0669');
			break;
		}
		case '\u06f0':  case '\u06f1':  case '\u06f2':  case '\u06f3':
		case '\u06f4':  case '\u06f5':  case '\u06f6':  case '\u06f7':
		case '\u06f8':  case '\u06f9':
		{
			matchRange('\u06f0','\u06f9');
			break;
		}
		case '\u07c0':  case '\u07c1':  case '\u07c2':  case '\u07c3':
		case '\u07c4':  case '\u07c5':  case '\u07c6':  case '\u07c7':
		case '\u07c8':  case '\u07c9':
		{
			matchRange('\u07c0','\u07c9');
			break;
		}
		case '\u0966':  case '\u0967':  case '\u0968':  case '\u0969':
		case '\u096a':  case '\u096b':  case '\u096c':  case '\u096d':
		case '\u096e':  case '\u096f':
		{
			matchRange('\u0966','\u096f');
			break;
		}
		case '\u09e6':  case '\u09e7':  case '\u09e8':  case '\u09e9':
		case '\u09ea':  case '\u09eb':  case '\u09ec':  case '\u09ed':
		case '\u09ee':  case '\u09ef':
		{
			matchRange('\u09e6','\u09ef');
			break;
		}
		case '\u0a66':  case '\u0a67':  case '\u0a68':  case '\u0a69':
		case '\u0a6a':  case '\u0a6b':  case '\u0a6c':  case '\u0a6d':
		case '\u0a6e':  case '\u0a6f':
		{
			matchRange('\u0a66','\u0a6f');
			break;
		}
		case '\u0ae6':  case '\u0ae7':  case '\u0ae8':  case '\u0ae9':
		case '\u0aea':  case '\u0aeb':  case '\u0aec':  case '\u0aed':
		case '\u0aee':  case '\u0aef':
		{
			matchRange('\u0ae6','\u0aef');
			break;
		}
		case '\u0b66':  case '\u0b67':  case '\u0b68':  case '\u0b69':
		case '\u0b6a':  case '\u0b6b':  case '\u0b6c':  case '\u0b6d':
		case '\u0b6e':  case '\u0b6f':
		{
			matchRange('\u0b66','\u0b6f');
			break;
		}
		case '\u0be6':  case '\u0be7':  case '\u0be8':  case '\u0be9':
		case '\u0bea':  case '\u0beb':  case '\u0bec':  case '\u0bed':
		case '\u0bee':  case '\u0bef':
		{
			matchRange('\u0be6','\u0bef');
			break;
		}
		case '\u0c66':  case '\u0c67':  case '\u0c68':  case '\u0c69':
		case '\u0c6a':  case '\u0c6b':  case '\u0c6c':  case '\u0c6d':
		case '\u0c6e':  case '\u0c6f':
		{
			matchRange('\u0c66','\u0c6f');
			break;
		}
		case '\u0ce6':  case '\u0ce7':  case '\u0ce8':  case '\u0ce9':
		case '\u0cea':  case '\u0ceb':  case '\u0cec':  case '\u0ced':
		case '\u0cee':  case '\u0cef':
		{
			matchRange('\u0ce6','\u0cef');
			break;
		}
		case '\u0d66':  case '\u0d67':  case '\u0d68':  case '\u0d69':
		case '\u0d6a':  case '\u0d6b':  case '\u0d6c':  case '\u0d6d':
		case '\u0d6e':  case '\u0d6f':
		{
			matchRange('\u0d66','\u0d6f');
			break;
		}
		case '\u0e50':  case '\u0e51':  case '\u0e52':  case '\u0e53':
		case '\u0e54':  case '\u0e55':  case '\u0e56':  case '\u0e57':
		case '\u0e58':  case '\u0e59':
		{
			matchRange('\u0e50','\u0e59');
			break;
		}
		case '\u0ed0':  case '\u0ed1':  case '\u0ed2':  case '\u0ed3':
		case '\u0ed4':  case '\u0ed5':  case '\u0ed6':  case '\u0ed7':
		case '\u0ed8':  case '\u0ed9':
		{
			matchRange('\u0ed0','\u0ed9');
			break;
		}
		case '\u0f20':  case '\u0f21':  case '\u0f22':  case '\u0f23':
		case '\u0f24':  case '\u0f25':  case '\u0f26':  case '\u0f27':
		case '\u0f28':  case '\u0f29':
		{
			matchRange('\u0f20','\u0f29');
			break;
		}
		case '\u1040':  case '\u1041':  case '\u1042':  case '\u1043':
		case '\u1044':  case '\u1045':  case '\u1046':  case '\u1047':
		case '\u1048':  case '\u1049':
		{
			matchRange('\u1040','\u1049');
			break;
		}
		case '\u17e0':  case '\u17e1':  case '\u17e2':  case '\u17e3':
		case '\u17e4':  case '\u17e5':  case '\u17e6':  case '\u17e7':
		case '\u17e8':  case '\u17e9':
		{
			matchRange('\u17e0','\u17e9');
			break;
		}
		case '\u1810':  case '\u1811':  case '\u1812':  case '\u1813':
		case '\u1814':  case '\u1815':  case '\u1816':  case '\u1817':
		case '\u1818':  case '\u1819':
		{
			matchRange('\u1810','\u1819');
			break;
		}
		case '\u1946':  case '\u1947':  case '\u1948':  case '\u1949':
		case '\u194a':  case '\u194b':  case '\u194c':  case '\u194d':
		case '\u194e':  case '\u194f':
		{
			matchRange('\u1946','\u194f');
			break;
		}
		case '\u19d0':  case '\u19d1':  case '\u19d2':  case '\u19d3':
		case '\u19d4':  case '\u19d5':  case '\u19d6':  case '\u19d7':
		case '\u19d8':  case '\u19d9':
		{
			matchRange('\u19d0','\u19d9');
			break;
		}
		case '\u1b50':  case '\u1b51':  case '\u1b52':  case '\u1b53':
		case '\u1b54':  case '\u1b55':  case '\u1b56':  case '\u1b57':
		case '\u1b58':  case '\u1b59':
		{
			matchRange('\u1b50','\u1b59');
			break;
		}
		case '\uff10':  case '\uff11':  case '\uff12':  case '\uff13':
		case '\uff14':  case '\uff15':  case '\uff16':  case '\uff17':
		case '\uff18':  case '\uff19':
		{
			matchRange('\uff10','\uff19');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mNUM_INT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NUM_INT;
		int _saveIndex;
		Token f1=null;
		Token f2=null;
		Token f3=null;
		Token f4=null;
		boolean isDecimal=false; Token t=null;
		
		switch ( LA(1)) {
		case '.':
		{
			match('.');
			if ( inputState.guessing==0 ) {
				_ttype = DOT;
			}
			{
			switch ( LA(1)) {
			case '0':  case '1':  case '2':  case '3':
			case '4':  case '5':  case '6':  case '7':
			case '8':  case '9':
			{
				{
				{
				int _cnt412=0;
				_loop412:
				do {
					if (((LA(1) >= '0' && LA(1) <= '9'))) {
						matchRange('0','9');
					}
					else {
						if ( _cnt412>=1 ) { break _loop412; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
					}
					
					_cnt412++;
				} while (true);
				}
				{
				if ((LA(1)=='E'||LA(1)=='e')) {
					mEXPONENT(false);
				}
				else {
				}
				
				}
				{
				if ((LA(1)=='D'||LA(1)=='F'||LA(1)=='d'||LA(1)=='f')) {
					mFLOAT_SUFFIX(true);
					f1=_returnToken;
					if ( inputState.guessing==0 ) {
						t=f1;
					}
				}
				else {
				}
				
				}
				if ( inputState.guessing==0 ) {
					
					if (t != null && t.getText().toUpperCase().indexOf('F')>=0) {
					_ttype = NUM_FLOAT;
					}
					else {
					_ttype = NUM_DOUBLE; // assume double
					}
					
				}
				}
				break;
			}
			case '.':
			{
				{
				match("..");
				if ( inputState.guessing==0 ) {
					_ttype = TRIPLE_DOT;
				}
				}
				break;
			}
			default:
				{
				}
			}
			}
			break;
		}
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		case '8':  case '9':
		{
			{
			switch ( LA(1)) {
			case '0':
			{
				match('0');
				if ( inputState.guessing==0 ) {
					isDecimal = true;
				}
				{
				if ((LA(1)=='X'||LA(1)=='x')) {
					{
					switch ( LA(1)) {
					case 'x':
					{
						match('x');
						break;
					}
					case 'X':
					{
						match('X');
						break;
					}
					default:
					{
						throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
					}
					}
					}
					{
					int _cnt420=0;
					_loop420:
					do {
						if ((_tokenSet_9.member(LA(1))) && (true) && (true) && (true)) {
							mHEX_DIGIT(false);
						}
						else {
							if ( _cnt420>=1 ) { break _loop420; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
						}
						
						_cnt420++;
					} while (true);
					}
				}
				else {
					boolean synPredMatched425 = false;
					if ((((LA(1) >= '0' && LA(1) <= '9')) && (true) && (true) && (true))) {
						int _m425 = mark();
						synPredMatched425 = true;
						inputState.guessing++;
						try {
							{
							{
							int _cnt423=0;
							_loop423:
							do {
								if (((LA(1) >= '0' && LA(1) <= '9'))) {
									matchRange('0','9');
								}
								else {
									if ( _cnt423>=1 ) { break _loop423; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
								}
								
								_cnt423++;
							} while (true);
							}
							{
							switch ( LA(1)) {
							case '.':
							{
								match('.');
								break;
							}
							case 'E':  case 'e':
							{
								mEXPONENT(false);
								break;
							}
							case 'D':  case 'F':  case 'd':  case 'f':
							{
								mFLOAT_SUFFIX(false);
								break;
							}
							default:
							{
								throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
							}
							}
							}
							}
						}
						catch (RecognitionException pe) {
							synPredMatched425 = false;
						}
						rewind(_m425);
						inputState.guessing--;
					}
					if ( synPredMatched425 ) {
						{
						int _cnt427=0;
						_loop427:
						do {
							if (((LA(1) >= '0' && LA(1) <= '9'))) {
								matchRange('0','9');
							}
							else {
								if ( _cnt427>=1 ) { break _loop427; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
							}
							
							_cnt427++;
						} while (true);
						}
					}
					else if (((LA(1) >= '0' && LA(1) <= '7')) && (true) && (true) && (true)) {
						{
						int _cnt429=0;
						_loop429:
						do {
							if (((LA(1) >= '0' && LA(1) <= '7'))) {
								matchRange('0','7');
							}
							else {
								if ( _cnt429>=1 ) { break _loop429; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
							}
							
							_cnt429++;
						} while (true);
						}
					}
					else {
					}
					}
					}
					break;
				}
				case '1':  case '2':  case '3':  case '4':
				case '5':  case '6':  case '7':  case '8':
				case '9':
				{
					{
					matchRange('1','9');
					}
					{
					_loop432:
					do {
						if (((LA(1) >= '0' && LA(1) <= '9'))) {
							matchRange('0','9');
						}
						else {
							break _loop432;
						}
						
					} while (true);
					}
					if ( inputState.guessing==0 ) {
						isDecimal=true;
					}
					break;
				}
				default:
				{
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				}
				}
				{
				if ((LA(1)=='L'||LA(1)=='l')) {
					{
					switch ( LA(1)) {
					case 'l':
					{
						match('l');
						break;
					}
					case 'L':
					{
						match('L');
						break;
					}
					default:
					{
						throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
					}
					}
					}
					if ( inputState.guessing==0 ) {
						_ttype = NUM_LONG;
					}
				}
				else if (((LA(1)=='.'||LA(1)=='D'||LA(1)=='E'||LA(1)=='F'||LA(1)=='d'||LA(1)=='e'||LA(1)=='f'))&&(isDecimal)) {
					{
					switch ( LA(1)) {
					case '.':
					{
						match('.');
						{
						_loop437:
						do {
							if (((LA(1) >= '0' && LA(1) <= '9'))) {
								matchRange('0','9');
							}
							else {
								break _loop437;
							}
							
						} while (true);
						}
						{
						if ((LA(1)=='E'||LA(1)=='e')) {
							mEXPONENT(false);
						}
						else {
						}
						
						}
						{
						if ((LA(1)=='D'||LA(1)=='F'||LA(1)=='d'||LA(1)=='f')) {
							mFLOAT_SUFFIX(true);
							f2=_returnToken;
							if ( inputState.guessing==0 ) {
								t=f2;
							}
						}
						else {
						}
						
						}
						break;
					}
					case 'E':  case 'e':
					{
						mEXPONENT(false);
						{
						if ((LA(1)=='D'||LA(1)=='F'||LA(1)=='d'||LA(1)=='f')) {
							mFLOAT_SUFFIX(true);
							f3=_returnToken;
							if ( inputState.guessing==0 ) {
								t=f3;
							}
						}
						else {
						}
						
						}
						break;
					}
					case 'D':  case 'F':  case 'd':  case 'f':
					{
						mFLOAT_SUFFIX(true);
						f4=_returnToken;
						if ( inputState.guessing==0 ) {
							t=f4;
						}
						break;
					}
					default:
					{
						throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
					}
					}
					}
					if ( inputState.guessing==0 ) {
						
						if (t != null && t.getText().toUpperCase() .indexOf('F') >= 0) {
						_ttype = NUM_FLOAT;
						}
						else {
						_ttype = NUM_DOUBLE; // assume double
						}
						
					}
				}
				else {
				}
				
				}
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
			}
			_returnToken = _token;
		}
		
	protected final void mEXPONENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = EXPONENT;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case 'e':
		{
			match('e');
			break;
		}
		case 'E':
		{
			match('E');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		{
		switch ( LA(1)) {
		case '+':
		{
			match('+');
			break;
		}
		case '-':
		{
			match('-');
			break;
		}
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		case '8':  case '9':
		{
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		{
		int _cnt446=0;
		_loop446:
		do {
			if (((LA(1) >= '0' && LA(1) <= '9'))) {
				matchRange('0','9');
			}
			else {
				if ( _cnt446>=1 ) { break _loop446; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt446++;
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mFLOAT_SUFFIX(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = FLOAT_SUFFIX;
		int _saveIndex;
		
		switch ( LA(1)) {
		case 'f':
		{
			match('f');
			break;
		}
		case 'F':
		{
			match('F');
			break;
		}
		case 'd':
		{
			match('d');
			break;
		}
		case 'D':
		{
			match('D');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mAT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = AT;
		int _saveIndex;
		
		match('@');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	
	private static final long[] mk_tokenSet_0() {
		long[] data = new long[2048];
		data[0]=-4398046511112L;
		for (int i = 1; i<=1023; i++) { data[i]=-1L; }
		for (int i = 1024; i<=2047; i++) { data[i]=0L; }
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = new long[4088];
		data[0]=68719476736L;
		data[1]=576460745995190270L;
		data[2]=297241973452963840L;
		data[3]=-36028797027352577L;
		for (int i = 4; i<=9; i++) { data[i]=-1L; }
		data[10]=281474976710655L;
		for (int i = 11; i<=12; i++) { data[i]=0L; }
		data[13]=4035225266123964416L;
		data[14]=-17179879616L;
		data[15]=-18014398509514753L;
		for (int i = 16; i<=17; i++) { data[i]=-1L; }
		data[18]=-1021L;
		data[19]=-1L;
		data[20]=-562949952372737L;
		data[21]=-8581545985L;
		data[22]=255L;
		data[23]=1979120929931264L;
		data[24]=576460743713488896L;
		data[25]=-351843720886274L;
		data[26]=-1L;
		data[27]=-7205548297557114881L;
		data[28]=281474976514048L;
		data[29]=70368744169472L;
		data[30]=563224831328255L;
		data[31]=8796093021184L;
		for (int i = 32; i<=35; i++) { data[i]=0L; }
		data[36]=2594073385365405680L;
		data[37]=-576460735140265984L;
		data[38]=2577745637692514272L;
		data[39]=844440767840256L;
		data[40]=247132830528276448L;
		data[41]=7881300924956672L;
		data[42]=2589004636761079776L;
		data[43]=12884967424L;
		data[44]=2589004636760940512L;
		data[45]=562965791113216L;
		data[46]=288167810662516712L;
		data[47]=0L;
		data[48]=283724577500946400L;
		data[49]=12884901888L;
		data[50]=2589567586714640352L;
		data[51]=13958643712L;
		data[52]=288228177128316896L;
		data[53]=12884901888L;
		data[54]=3457638613854978016L;
		data[55]=127L;
		data[56]=3940649673949182L;
		data[57]=63L;
		data[58]=2309762420256548246L;
		data[59]=805306399L;
		data[60]=1L;
		data[61]=8796093021951L;
		data[62]=3840L;
		data[63]=0L;
		data[64]=7679401525247L;
		data[65]=4128768L;
		data[66]=-4294967296L;
		data[67]=576460752303358015L;
		data[68]=-1L;
		data[69]=-2080374785L;
		data[70]=-1065151889409L;
		data[71]=288230376151711743L;
		data[72]=-1L;
		data[73]=-3263218177L;
		data[74]=9168765891372858879L;
		data[75]=-8388803L;
		data[76]=-12713985L;
		data[77]=134217727L;
		data[78]=-4294901761L;
		data[79]=9007199254740991L;
		data[80]=-2L;
		for (int i = 81; i<=88; i++) { data[i]=-1L; }
		data[89]=35923243902697471L;
		data[90]=-4160749570L;
		data[91]=8796093022207L;
		data[92]=1125895612129279L;
		data[93]=527761286627327L;
		data[94]=4503599627370495L;
		data[95]=268435456L;
		data[96]=-4294967296L;
		data[97]=72057594037927927L;
		data[98]=2199023255551L;
		data[99]=0L;
		data[100]=536870911L;
		data[101]=8796093022142464L;
		data[102]=4398046511103L;
		data[103]=254L;
		data[104]=8388607L;
		for (int i = 105; i<=107; i++) { data[i]=0L; }
		data[108]=4503599627370464L;
		data[109]=4064L;
		for (int i = 110; i<=115; i++) { data[i]=0L; }
		data[116]=17592186044415L;
		data[117]=-72057611217797120L;
		data[118]=134217727L;
		data[119]=0L;
		for (int i = 120; i<=121; i++) { data[i]=-1L; }
		data[122]=-4026531841L;
		data[123]=288230376151711743L;
		data[124]=-3233808385L;
		data[125]=4611686017001275199L;
		data[126]=6908521828386340863L;
		data[127]=2295745090394464220L;
		data[128]=0L;
		data[129]=-9222809086901354496L;
		for (int i = 130; i<=131; i++) { data[i]=0L; }
		data[132]=-864764451093480316L;
		data[133]=17376L;
		data[134]=24L;
		for (int i = 135; i<=175; i++) { data[i]=0L; }
		data[176]=-140737488355329L;
		data[177]=67589176635162623L;
		data[178]=-1L;
		data[179]=137438953471L;
		data[180]=-281200098803713L;
		data[181]=274877906943L;
		data[182]=9187201948305063935L;
		data[183]=2139062143L;
		for (int i = 184; i<=191; i++) { data[i]=0L; }
		data[192]=1152921504606847040L;
		data[193]=-2L;
		data[194]=-6434062337L;
		data[195]=-8646911284551352321L;
		data[196]=-527765581332512L;
		data[197]=-1L;
		data[198]=72057589742993407L;
		data[199]=-281474976710656L;
		for (int i = 200; i<=207; i++) { data[i]=0L; }
		data[208]=1L;
		for (int i = 209; i<=309; i++) { data[i]=0L; }
		data[310]=9007199254740992L;
		data[311]=0L;
		data[312]=1L;
		for (int i = 313; i<=637; i++) { data[i]=0L; }
		data[638]=576460752303423488L;
		data[639]=0L;
		data[640]=-2097153L;
		for (int i = 641; i<=657; i++) { data[i]=-1L; }
		data[658]=8191L;
		for (int i = 659; i<=671; i++) { data[i]=0L; }
		data[672]=34359736251L;
		data[673]=4503599627370495L;
		for (int i = 674; i<=687; i++) { data[i]=0L; }
		data[688]=1L;
		for (int i = 689; i<=861; i++) { data[i]=0L; }
		data[862]=34359738368L;
		for (int i = 863; i<=995; i++) { data[i]=0L; }
		for (int i = 996; i<=999; i++) { data[i]=-1L; }
		data[1000]=-211106232532993L;
		data[1001]=-272678883688449L;
		data[1002]=-1L;
		data[1003]=67108863L;
		data[1004]=6881498030004502655L;
		data[1005]=-37L;
		data[1006]=1125899906842623L;
		data[1007]=-524288L;
		for (int i = 1008; i<=1011; i++) { data[i]=-1L; }
		data[1012]=4611686018427387903L;
		data[1013]=-65536L;
		data[1014]=-196609L;
		data[1015]=1152640029630136575L;
		data[1016]=0L;
		data[1017]=-9288674231451648L;
		data[1018]=-1L;
		data[1019]=2305843009213693951L;
		data[1020]=576460743713488896L;
		data[1021]=-281749720399874L;
		data[1022]=9223372033633550335L;
		data[1023]=486341884L;
		for (int i = 1024; i<=4087; i++) { data[i]=0L; }
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = new long[2048];
		data[0]=-9224L;
		for (int i = 1; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		for (int i = 1024; i<=2047; i++) { data[i]=0L; }
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = new long[2048];
		data[0]=-4398046520328L;
		for (int i = 1; i<=1023; i++) { data[i]=-1L; }
		for (int i = 1024; i<=2047; i++) { data[i]=0L; }
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = new long[2048];
		data[0]=-549755823112L;
		data[1]=-268435457L;
		for (int i = 2; i<=1023; i++) { data[i]=-1L; }
		for (int i = 1024; i<=2047; i++) { data[i]=0L; }
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = new long[2048];
		data[0]=-9224L;
		for (int i = 1; i<=1023; i++) { data[i]=-1L; }
		for (int i = 1024; i<=2047; i++) { data[i]=0L; }
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = new long[2048];
		data[0]=-17179878408L;
		data[1]=-268435457L;
		for (int i = 2; i<=1023; i++) { data[i]=-1L; }
		for (int i = 1024; i<=2047; i++) { data[i]=0L; }
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = new long[4088];
		data[0]=0L;
		data[1]=576460743847706622L;
		data[2]=297241973452963840L;
		data[3]=-36028797027352577L;
		for (int i = 4; i<=9; i++) { data[i]=-1L; }
		data[10]=281474976710655L;
		for (int i = 11; i<=12; i++) { data[i]=0L; }
		data[13]=4035225266123964416L;
		data[14]=-17179879616L;
		data[15]=-18014398509514753L;
		for (int i = 16; i<=17; i++) { data[i]=-1L; }
		data[18]=-1021L;
		data[19]=-1L;
		data[20]=-562949952372737L;
		data[21]=-8581545985L;
		data[22]=255L;
		data[23]=1979120929931264L;
		data[24]=576460743713488896L;
		data[25]=-351843720886274L;
		data[26]=-1L;
		data[27]=-7205548297557114881L;
		data[28]=281474976514048L;
		data[29]=70368744169472L;
		data[30]=563224831328255L;
		data[31]=8796093021184L;
		for (int i = 32; i<=35; i++) { data[i]=0L; }
		data[36]=2594073385365405680L;
		data[37]=-576460735140265984L;
		data[38]=2577745637692514272L;
		data[39]=844440767840256L;
		data[40]=247132830528276448L;
		data[41]=7881300924956672L;
		data[42]=2589004636761079776L;
		data[43]=12884967424L;
		data[44]=2589004636760940512L;
		data[45]=562965791113216L;
		data[46]=288167810662516712L;
		data[47]=0L;
		data[48]=283724577500946400L;
		data[49]=12884901888L;
		data[50]=2589567586714640352L;
		data[51]=13958643712L;
		data[52]=288228177128316896L;
		data[53]=12884901888L;
		data[54]=3457638613854978016L;
		data[55]=127L;
		data[56]=3940649673949182L;
		data[57]=63L;
		data[58]=2309762420256548246L;
		data[59]=805306399L;
		data[60]=1L;
		data[61]=8796093021951L;
		data[62]=3840L;
		data[63]=0L;
		data[64]=7679401525247L;
		data[65]=4128768L;
		data[66]=-4294967296L;
		data[67]=576460752303358015L;
		data[68]=-1L;
		data[69]=-2080374785L;
		data[70]=-1065151889409L;
		data[71]=288230376151711743L;
		data[72]=-1L;
		data[73]=-3263218177L;
		data[74]=9168765891372858879L;
		data[75]=-8388803L;
		data[76]=-12713985L;
		data[77]=134217727L;
		data[78]=-4294901761L;
		data[79]=9007199254740991L;
		data[80]=-2L;
		for (int i = 81; i<=88; i++) { data[i]=-1L; }
		data[89]=35923243902697471L;
		data[90]=-4160749570L;
		data[91]=8796093022207L;
		data[92]=1125895612129279L;
		data[93]=527761286627327L;
		data[94]=4503599627370495L;
		data[95]=268435456L;
		data[96]=-4294967296L;
		data[97]=72057594037927927L;
		data[98]=2199023255551L;
		data[99]=0L;
		data[100]=536870911L;
		data[101]=8796093022142464L;
		data[102]=4398046511103L;
		data[103]=254L;
		data[104]=8388607L;
		for (int i = 105; i<=107; i++) { data[i]=0L; }
		data[108]=4503599627370464L;
		data[109]=4064L;
		for (int i = 110; i<=115; i++) { data[i]=0L; }
		data[116]=17592186044415L;
		data[117]=-72057611217797120L;
		data[118]=134217727L;
		data[119]=0L;
		for (int i = 120; i<=121; i++) { data[i]=-1L; }
		data[122]=-4026531841L;
		data[123]=288230376151711743L;
		data[124]=-3233808385L;
		data[125]=4611686017001275199L;
		data[126]=6908521828386340863L;
		data[127]=2295745090394464220L;
		data[128]=0L;
		data[129]=-9222809086901354496L;
		for (int i = 130; i<=131; i++) { data[i]=0L; }
		data[132]=-864764451093480316L;
		data[133]=17376L;
		data[134]=24L;
		for (int i = 135; i<=175; i++) { data[i]=0L; }
		data[176]=-140737488355329L;
		data[177]=67589176635162623L;
		data[178]=-1L;
		data[179]=137438953471L;
		data[180]=-281200098803713L;
		data[181]=274877906943L;
		data[182]=9187201948305063935L;
		data[183]=2139062143L;
		for (int i = 184; i<=191; i++) { data[i]=0L; }
		data[192]=1152921504606847040L;
		data[193]=-2L;
		data[194]=-6434062337L;
		data[195]=-8646911284551352321L;
		data[196]=-527765581332512L;
		data[197]=-1L;
		data[198]=72057589742993407L;
		data[199]=-281474976710656L;
		for (int i = 200; i<=207; i++) { data[i]=0L; }
		data[208]=1L;
		for (int i = 209; i<=309; i++) { data[i]=0L; }
		data[310]=9007199254740992L;
		data[311]=0L;
		data[312]=1L;
		for (int i = 313; i<=637; i++) { data[i]=0L; }
		data[638]=576460752303423488L;
		data[639]=0L;
		data[640]=-2097153L;
		for (int i = 641; i<=657; i++) { data[i]=-1L; }
		data[658]=8191L;
		for (int i = 659; i<=671; i++) { data[i]=0L; }
		data[672]=34359736251L;
		data[673]=4503599627370495L;
		for (int i = 674; i<=687; i++) { data[i]=0L; }
		data[688]=1L;
		for (int i = 689; i<=861; i++) { data[i]=0L; }
		data[862]=34359738368L;
		for (int i = 863; i<=995; i++) { data[i]=0L; }
		for (int i = 996; i<=999; i++) { data[i]=-1L; }
		data[1000]=-211106232532993L;
		data[1001]=-272678883688449L;
		data[1002]=-1L;
		data[1003]=67108863L;
		data[1004]=6881498030004502655L;
		data[1005]=-37L;
		data[1006]=1125899906842623L;
		data[1007]=-524288L;
		for (int i = 1008; i<=1011; i++) { data[i]=-1L; }
		data[1012]=4611686018427387903L;
		data[1013]=-65536L;
		data[1014]=-196609L;
		data[1015]=1152640029630136575L;
		data[1016]=0L;
		data[1017]=-9288674231451648L;
		data[1018]=-1L;
		data[1019]=2305843009213693951L;
		data[1020]=576460743713488896L;
		data[1021]=-281749720399874L;
		data[1022]=9223372033633550335L;
		data[1023]=486341884L;
		for (int i = 1024; i<=4087; i++) { data[i]=0L; }
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = new long[2042];
		data[0]=287948901175001088L;
		for (int i = 1; i<=24; i++) { data[i]=0L; }
		data[25]=4393751543808L;
		data[26]=0L;
		data[27]=287948901175001088L;
		for (int i = 28; i<=30; i++) { data[i]=0L; }
		data[31]=1023L;
		for (int i = 32; i<=36; i++) { data[i]=0L; }
		data[37]=281200098803712L;
		data[38]=0L;
		data[39]=281200098803712L;
		data[40]=0L;
		data[41]=281200098803712L;
		data[42]=0L;
		data[43]=281200098803712L;
		data[44]=0L;
		data[45]=281200098803712L;
		data[46]=0L;
		data[47]=281200098803712L;
		data[48]=0L;
		data[49]=281200098803712L;
		data[50]=0L;
		data[51]=281200098803712L;
		data[52]=0L;
		data[53]=281200098803712L;
		for (int i = 54; i<=56; i++) { data[i]=0L; }
		data[57]=67043328L;
		data[58]=0L;
		data[59]=67043328L;
		data[60]=4393751543808L;
		for (int i = 61; i<=64; i++) { data[i]=0L; }
		data[65]=1023L;
		for (int i = 66; i<=94; i++) { data[i]=0L; }
		data[95]=4393751543808L;
		data[96]=67043328L;
		for (int i = 97; i<=100; i++) { data[i]=0L; }
		data[101]=65472L;
		data[102]=0L;
		data[103]=67043328L;
		for (int i = 104; i<=108; i++) { data[i]=0L; }
		data[109]=67043328L;
		for (int i = 110; i<=1019; i++) { data[i]=0L; }
		data[1020]=67043328L;
		for (int i = 1021; i<=2041; i++) { data[i]=0L; }
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = new long[1025];
		data[0]=287948901175001088L;
		data[1]=541165879422L;
		for (int i = 2; i<=1024; i++) { data[i]=0L; }
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	
	}
