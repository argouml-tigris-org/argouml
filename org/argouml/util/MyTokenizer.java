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

package org.argouml.util;

import java.util.*;

/**
 * Internal class for managing the delimiters in MyTokenizer. It's rather
 * similar to CustomSeparator, but faster for short constant strings.
 */
class TokenSep {
    public TokenSep next = null;
    private final String _string;
    private final int _length;
    private int _pattern;

    /**
     * Constructs a TokenSep that will match the String given in str.
     *
     * @param str The delimiter string.
     */
    public TokenSep(String str) {
	_string = str;
	_length = str.length();
	if (_length > 32)
	    throw new IllegalArgumentException("TokenSep " + str
					       + " is " + _length + " (> 32) chars long");
	_pattern = 0;
    }

    /**
     * Called by MyTokenizer when a new character is processed in the
     * sequence. Returns true if we have found the delimiter.
     */
    public boolean addChar(char c) {
	int i;

	_pattern <<= 1;
	_pattern |= 1;
	for (i = 0; i < _length; i++) {
	    if (_string.charAt(i) != c) {
		_pattern &= ~(1 << i);
	    }
	}

	return (_pattern & (1 << (_length - 1))) != 0;
    }

    /**
     * Called by MyTokenizer before starting scanning for a new token.
     */
    public void reset() {
	_pattern = 0;
    }

    /**
     * Gets the length of this token.
     */
    public int length() {
	return _length;
    }

    /**
     * Gets this token.
     */
    public String getString() {
	return _string;
    }
}

/**
 * A descendent of CustomSeparator that recognizes tokens on one of two forms:
 * <ul>
 * <li><pre>'chr'.....'esc' 'chr'.....'chr'</pre>
 * <li><pre>'lchr'...'lchr'...'rchr'...'esc' 'rchr'....'rchr'</pre></ul>
 *
 * <p>The first form is suited for quoted strings, like <pre>"...\"...."</pre>
 * or <pre>'...\'...'</pre>.
 *
 * <p>The second form is suited for expressions, like
 * <pre>(a+(b*c)-15*eq(a, b))</pre>.
 *
 * <p>This is in fact the class currently used for the public separators in
 * MyTokenizer, except PAREN_EXPR_STRING_SEPARATOR.
 */
class QuotedStringSeparator extends CustomSeparator {
    private final char _escChr;
    private final char _startChr;
    private final char _stopChr;
    private boolean _esced;
    private int _tokLen;
    private int _level;

    /**
     * Creates a separator of the first form (see above) where
     * 'chr' = q and 'esc' = esc.
     *
     * @param q The delimiter character.
     * @param esc The escape character.
     */
    public QuotedStringSeparator(char q, char esc) {
	super(q);

	_esced = false;
	_escChr = esc;
	_startChr = 0;
	_stopChr = q;
	_tokLen = 0;
	_level = 1;
    }

    /**
     * Creates a separator of the second form (see above) where
     * 'lchr' = sq, 'rchr' = eq and 'esc' = esc.
     *
     * @param sq The left delimiter character.
     * @param eq The right delimiter character.
     * @param esc The escape character.
     */
    public QuotedStringSeparator(char sq, char eq, char esc) {
	super(sq);

	_esced = false;
	_escChr = esc;
	_startChr = sq;
	_stopChr = eq;
	_tokLen = 0;
	_level = 1;
    }

    public void reset() {
	super.reset();
	_tokLen = 0;
	_level = 1;
    }

    /**
     * {@inheritDoc}
     *
     * Overridden to return the entire length of the token.
     */
    public int tokenLength() {
	return super.tokenLength() + _tokLen;
    }

    /**
     * {@inheritDoc}
     *
     * Overridden to return true.
     *
     * @return true
     */
    public boolean hasFreePart() {
	return true;
    }

    /**
     * {@inheritDoc}
     *
     * Overridden to find the end of the token.
     */
    public boolean endChar(char c) {
	_tokLen++;

	if (_esced) {
	    _esced = false;
	    return false;
	}
	if (_escChr != 0 && c == _escChr) {
	    _esced = true;
	    return false;
	}
	if (_startChr != 0 && c == _startChr)
	    _level++;
	if (c == _stopChr)
	    _level--;
	return _level <= 0;
    }
}

/**
 * A descendent of CustomSeparator that recognizes tokens on the form:
 *
 * <br>( " \" ) " ' \' ) ' )
 *
 * <p>This is, an expression inside parentheses with proper consideration
 * for quoted strings inside the the expression.
 */
class ExprSeparatorWithStrings extends CustomSeparator {
    private boolean _isSQuot;
    private boolean _isDQuot;
    private boolean _isEsc;
    private int _tokLevel;
    private int _tokLen;

    /**
     * The constructor. No choices available.
     */
    public ExprSeparatorWithStrings() {
	super('(');

	_isEsc = false;
	_isSQuot = false;
	_isDQuot = false;
	_tokLevel = 1;
	_tokLen = 0;
    }

    public void reset() {
	super.reset();

	_isEsc = false;
	_isSQuot = false;
	_isDQuot = false;
	_tokLevel = 1;
	_tokLen = 0;
    }

    /**
     * {@inheritDoc}
     *
     * Overridden to return the entire length of the token.
     */
    public int tokenLength() {
	return super.tokenLength() + _tokLen;
    }

    /**
     * {@inheritDoc}
     *
     * Overridden to return true.
     *
     * @return true
     */
    public boolean hasFreePart() {
	return true;
    }

    /**
     * {@inheritDoc}
     *
     * Overridden to find the end of the token.
     */
    public boolean endChar(char c) {
	_tokLen++;
	if (_isSQuot) {
	    if (_isEsc) {
		_isEsc = false;
		return false;
	    }
	    if (c == '\\')
		_isEsc = true;
	    else if (c == '\'')
		_isSQuot = false;
	    return false;
	} else if (_isDQuot) {
	    if (_isEsc) {
		_isEsc = false;
		return false;
	    }
	    if (c == '\\')
		_isEsc = true;
	    else if (c == '\"')
		_isDQuot = false;
	    return false;
	} else {
	    if (c == '\'')
		_isSQuot = true;
	    else if (c == '\"')
		_isDQuot = true;
	    else if (c == '(')
		_tokLevel++;
	    else if (c == ')')
		_tokLevel--;
	    return _tokLevel <= 0;
	}
    }
}

/**
 * Class for dividing a String into any number of parts. Each part will be a
 * substring of the original String. The first part will at least contain the
 * first character in the string. All following parts will at least contain
 * the first character in the String not covered by any previous part.
 *
 * <p>The delim parameter to the constructors is a comma separated list of
 * tokens that should be recognized by the tokenizer. These tokens will be
 * returned by the tokenizer as tokens, and any arbitrary text between them
 * will also be returned as tokens. Since the comma has special meaning in
 * this string, it can be escaped with \ to only mean itself (like in "\\,").
 * For technical reasons it is not possible for any token in this list to be
 * more than 32 characters long.
 *
 * <p>In addition to the delim parameter it is also possible to use custom
 * separators that allow any string that can be generated by the limited
 * version of a Turing machine that your computer is, to be used as a
 * delimiter.
 *
 * <p>There are some custom separators provided that you can use to get
 * things like strings in one token. These cannot be used simultaneously by
 * several tokenizers, ie they are not thread safe.
 *
 * <p>The tokenizer works in a kind of greedy way. When the first separator
 * token from delim is matched or any CustomSeparator returns true from
 * addChar, then it is satisfied it has found a token and does NOT check if
 * it could have found a longer token. Eg: if you have this delim string
 * "<,<<", then "<<" will never be found.
 *
 * <p><b>Example</b><br><pre>
 * MyTokenizer tzer = new MyTokenizer("Hello, how are you?", " ,\\,");
 * while (tzer.hasMoreTokens())
 *   System.out.println("\"" + tzer.nextToken() + "\"");
 * </pre>
 *
 * <p>Which whould yield the following output:<pre>
 *   "Hello"
 *   ","
 *   " "
 *   "how"
 *   " "
 *   "are"
 *   " "
 *   "you?"
 * </pre>
 *
 * @author Michael Stockman
 * @since 0.11.2
 * @see CustomSeparator
 */
public class MyTokenizer implements Enumeration
{
    /** A custom separator for quoted strings enclosed in single quotes
     *  and using \ as escape character. There may not be an end quote
     *  if the tokenizer reaches the end of the String. */
    public final static CustomSeparator SINGLE_QUOTED_SEPARATOR =
	new QuotedStringSeparator('\'', '\\');

    /** A custom separator for quoted strings enclosed in double quotes
     *  and using \ as escape character. There may not be an end quote
     *  if the tokenizer reaches the end of the String. */
    public final static CustomSeparator DOUBLE_QUOTED_SEPARATOR =
	new QuotedStringSeparator('\"', '\\');

    /** A custom separator for expressions enclosed in parentheses and
     *  matching lparams with rparams. There may not be proper matching
     *  if the tokenizer reaches the end of the String. Do not use this
     *  together with PAREN_EXPR_STRING_SEPARATOR. */
    public final static CustomSeparator PAREN_EXPR_SEPARATOR =
	new QuotedStringSeparator('(', ')', '\0');

    /** A custom separator for expressions enclosed in parentheses and
     *  matching lparams with rparams. There may not be proper matching
     *  if the tokenizer reaches the end of the String. It also takes
     *  quoted strings (either single or double quotes) in the expression
     *  into consideration, unlike PAREN_EXPR_SEPARATOR. Do not use this
     *  together with PAREN_EXPR_SEPARATOR. */
    public final static CustomSeparator PAREN_EXPR_STRING_SEPARATOR =
	new ExprSeparatorWithStrings();

    private int _sIdx;
    private final int _eIdx;
    private int _tokIdx;
    private final String _source;
    private final TokenSep _delims;
    private String _savedToken;
    private int _savedIdx;
    private Vector _customSeps;
    private String _putToken;

    /**
     * Constructs a new instance. See above for a description of the
     * delimiter string.
     *
     * @param string	The String to be tokenized.
     * @param delim	The String of delimiters.
     */
    public MyTokenizer(String string, String delim) {
	_source = string;
	_delims = parseDelimString(delim);
	_sIdx = 0;
	_tokIdx = 0;
	_eIdx = string.length();
	_savedToken = null;
	_customSeps = null;
	_putToken = null;
    }

    /**
     * Constructs a new instance. See above for a description of the
     * delimiter string and custom separators.
     *
     * @param string	The String to be tokenized.
     * @param delim	The String of delimiters.
     * @param sep	A custom separator to use.
     */
    public MyTokenizer(String string, String delim, CustomSeparator sep) {
	_source = string;
	_delims = parseDelimString(delim);
	_sIdx = 0;
	_tokIdx = 0;
	_eIdx = string.length();
	_savedToken = null;
	_customSeps = new Vector();
	_customSeps.add(sep);
    }

    /**
     * Constructs a new instance. See above for a description of the
     * delimiter string and custom separators.
     *
     * @param string	The String to be tokenized.
     * @param delim	The String of delimiters.
     * @param seps	Some container with custom separators to use.
     */
    public MyTokenizer(String string, String delim, Collection seps) {
	_source = string;
	_delims = parseDelimString(delim);
	_sIdx = 0;
	_tokIdx = 0;
	_eIdx = string.length();
	_savedToken = null;
	_customSeps = new Vector(seps);
    }

    /**
     * Returns true if there are more tokens left.
     *
     * @return true if another token can be fetched with nextToken.
     */
    public boolean hasMoreTokens() {
	return _sIdx < _eIdx || _savedToken != null ||
	    _putToken != null;
    }

    /**
     * Retrives the next token.
     *
     * @return The next token.
     */
    public String nextToken() {
	CustomSeparator csep;
	TokenSep sep;
	String s = null;
	int i, j;

	if (_putToken != null) {
	    s = _putToken;
	    _putToken = null;
	    return s;
	}

	if (_savedToken != null) {
	    s = _savedToken;
	    _tokIdx = _savedIdx;
	    _savedToken = null;
	    return s;
	}

	if (_sIdx >= _eIdx)
	    throw new NoSuchElementException(
					     "No more tokens available");

	for (sep = _delims; sep != null; sep = sep.next)
	    sep.reset();

	if (_customSeps != null) {
	    for (i = 0; i < _customSeps.size(); i++)
		((CustomSeparator) _customSeps.get(i)).reset();
	}

	for (i = _sIdx; i < _eIdx; i++) {
	    char c = _source.charAt(i);

	    for (j = 0; _customSeps != null &&
		     j < _customSeps.size(); j++) {
		csep = (CustomSeparator) _customSeps.get(j);

		if (csep.addChar(c))
		    break;
	    }
	    if (_customSeps != null && j < _customSeps.size()) {
		csep = (CustomSeparator) _customSeps.get(j);

		while (csep.hasFreePart() && i + 1 < _eIdx)
		    if (csep.endChar(_source.charAt(++i)))
			break;

		int clen = Math.min(i + 1, _source.length());

		if (i - _sIdx + 1 > csep.tokenLength()) {
		    s = _source.substring(_sIdx,
					  i - csep.tokenLength() + 1);

		    _savedIdx = i - csep.tokenLength() + 1;
		    _savedToken = _source.substring(
						    _savedIdx, clen);
		} else {
		    s = _source.substring(_sIdx, clen);
		}

		_tokIdx = _sIdx;
		_sIdx = i + 1;
		break;
	    }

	    for (sep = _delims; sep != null; sep = sep.next)
		if (sep.addChar(c))
		    break;
	    if (sep != null)
	    {
		if (i - _sIdx + 1 > sep.length()) {
		    s = _source.substring(_sIdx,
					  i - sep.length() + 1);
		    _savedIdx = i - sep.length() + 1;
		    _savedToken = sep.getString();
		} else {
		    s = sep.getString();
		}
		_tokIdx = _sIdx;
		_sIdx = i + 1;
		break;
	    }
	}

	if (s == null) {
	    s = _source.substring(_sIdx);
	    _tokIdx = _sIdx;
	    _sIdx = _eIdx;
	}

	return s;
    }

    /**
     * This class implements the Enumeration interface. This call maps
     * to nextToken.
     *
     * @return nextToken();
     * @see	#nextToken() nextToken
     */
    public Object nextElement() {
	return nextToken();
    }

    /**
     * This class implements the Enumeration interface. This call maps
     * to hasMoreTokens.
     *
     * @return hasMoreTokens();
     * @see	#hasMoreTokens() hasMoreTokens
     */
    public boolean hasMoreElements() {
	return hasMoreTokens();
    }

    /**
     * Returns the index in the string of the last token returned by
     * nextToken, or zero if no token has been retrived.
     *
     * @return The index of the last token.
     */
    public int getTokenIndex() {
	return _tokIdx;
    }

    /**
     * Put a token on the input stream. This will be the next token read
     * from the tokenizer. If this function is called again before the
     * last token has been read, then it will be lost.
     *
     * <p>The index returned from getTokenIndex will be the same for the
     * token put as that of the last token that wasn't put.
     *
     * @param s The token to put.
     * @throws NullPointerException if s is null.
     */
    public void putToken(String s) {
	if (s == null)
	    throw new NullPointerException(
					   "Cannot put a null token");

	_putToken = s;
    }

    /**
     * Creates a linked list of TokenSeps from the comma separated string
     * str.
     *
     * @param str The string specifying delimiter strings.
     * @return A list of TokenSeps.
     */
    private static TokenSep parseDelimString(String str) {
	TokenSep first = null;
	TokenSep p = null;
	int idx0, idx1, length;
	String val = "";
	char c;

	length = str.length();
	for (idx0 = 0; idx0 < length; ) {
	    for (idx1 = idx0; idx1 < length; idx1++) {
		c = str.charAt(idx1);
		if (c == '\\') {
		    idx1++;
		    if (idx1 < length)
			val += str.charAt(idx1);
		} else if (c == ',') {
		    break;
		} else {
		    val += c;
		}
	    }
	    idx1 = Math.min(idx1, length);
	    if (idx1 > idx0) {
		p = new TokenSep(val);
		val = "";
		p.next = first;
		first = p;
	    }

	    idx0 = idx1 + 1;
	}

	return first;
    }
}

