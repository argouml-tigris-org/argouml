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

class TokenSep {
	public TokenSep next = null;
	private final String _string;
	private final int _length;
	private int _pattern;

	public TokenSep(String str) {
		_string = str;
		_length = str.length();
		if (_length > 32)
			throw new IllegalArgumentException("TokenSep " + str + " is " + _length + " (> 32) chars long");
		_pattern = 0;
	}

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

	public void reset() {
		_pattern = 0;
	}

	public int length() {
		return _length;
	}

	public String getString() {
		return _string;
	}
}

public class MyTokenizer implements Enumeration
{
	private int _sIdx;
	private final int _eIdx;
	private int _tokIdx;
	private final String _source;
	private final TokenSep _delims;
	private String _savedToken;
	private int _savedIdx;

	public MyTokenizer(String string, String delim) {
		_source = string;
		_delims = parseDelimString(delim);
		_sIdx = 0;
		_tokIdx = 0;
		_eIdx = string.length();
		_savedToken = null;
	}

	public boolean hasMoreTokens() {
		return _sIdx < _eIdx || _savedToken != null;
	}

	public String nextToken() {
		TokenSep sep;
		String s = null;
		int i;

		if (_savedToken != null) {
			s = _savedToken;
			_tokIdx = _savedIdx;
			_savedToken = null;
			return s;
		}

		if (_sIdx >= _eIdx)
			throw new NoSuchElementException("No more tokens available");

		for (sep = _delims; sep != null; sep = sep.next)
			sep.reset();

		for (i = _sIdx; i < _eIdx; i++) {
			char c = _source.charAt(i);

			for (sep = _delims; sep != null; sep = sep.next)
				if (sep.addChar(c))
					break;
			if (sep != null)
			{
				if (i - _sIdx + 1 > sep.length()) {
					s = _source.substring(_sIdx, i - sep.length() + 1);
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

	public Object nextElement() {
		return nextToken();
	}

	public boolean hasMoreElements() {
		return hasMoreTokens();
	}

	public int getTokenIndex() {
		return _tokIdx;
	}

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

