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

import junit.framework.*;

public class TestMyTokenizer extends TestCase {
	public TestMyTokenizer(String str) {
		super(str);
	}

	public void testConstructors() {
		Vector seps = new Vector();
		seps.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
		seps.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);

		String str1 = "A String";
		String delim1 = " ";
		String res1[] = {"A", " ", "String"};

		String str2 = "public newAttr [1..*] : Type = Val {param}";
		String delim2 = " ,\t,<<,>>,[,],:,=,{,},\\,";
		String res2[] = {"public", " ", "newAttr", " ", "[", "1..*",
				"]", " ", ":", " ", "Type", " ", "=", " ",
				"Val", " ", "{", "param", "}"};

		String str3 = "<< stereo >> newAttr";
		String delim3 = " ,\t,<<,>>,[,],:,=,{,},\\,";
		String res3[] = {"<<", " ", "stereo", " ", ">>", " ", "newAttr"};

		String str4 = " newAttr = \": h\" ";
		String delim4 = " ,\t,<<,>>,[,],:,=,{,},\\,";
		String res4_1[] = {" ", "newAttr", " ", "=", " ", "\"", ":", " ", "h\"", " "};
		String res4_2[] = {" ", "newAttr", " ", "=", " ", "\": h\"", " "};

		String str5 = " newAttr = \': h\' ";
		String delim5 = " ,\t,<<,>>,[,],:,=,{,},\\,";
		String res5_1[] = {" ", "newAttr", " ", "=", " ", "\'", ":", " ", "h\'", " "};
		String res5_2[] = {" ", "newAttr", " ", "=", " ", "\': h\'", " "};

System.out.println("str1");
		checkConstr(str1, delim1, res1);
System.out.println("str1-1");
		checkConstr(str1, delim1, res1, MyTokenizer.SINGLE_QUOTED_SEPARATOR);
System.out.println("str1-2");
		checkConstr(str1, delim1, res1, MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
System.out.println("str1-3");
		checkConstr(str1, delim1, res1, seps);

System.out.println("str2");
		checkConstr(str2, delim2, res2);
		checkConstr(str2, delim2, res2, MyTokenizer.SINGLE_QUOTED_SEPARATOR);
		checkConstr(str2, delim2, res2, MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
		checkConstr(str2, delim2, res2, seps);

System.out.println("str3");
		checkConstr(str3, delim3, res3);
		checkConstr(str3, delim3, res3, MyTokenizer.SINGLE_QUOTED_SEPARATOR);
		checkConstr(str3, delim3, res3, MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
		checkConstr(str3, delim3, res3, seps);

System.out.println("str4");
		checkConstr(str4, delim4, res4_1);
		checkConstr(str4, delim4, res4_1, MyTokenizer.SINGLE_QUOTED_SEPARATOR);
		checkConstr(str4, delim4, res4_2, MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
		checkConstr(str4, delim4, res4_2, seps);

System.out.println("str5");
		checkConstr(str5, delim5, res5_1);
		checkConstr(str5, delim5, res5_2, MyTokenizer.SINGLE_QUOTED_SEPARATOR);
		checkConstr(str5, delim5, res5_1, MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
		checkConstr(str5, delim5, res5_2, seps);
	}

	private void checkConstr(String str, String delim, String res[]) {
		MyTokenizer tokenizer = new MyTokenizer(str, delim);
		String tok;
		int i;
		int idx = 0;

		for (i = 0; i < res.length; i++) {
			assert("MyTokenizer(\"" + str + "\", \"" + delim + "\") lacks tokens", tokenizer.hasMoreTokens());
			tok = tokenizer.nextToken();
			assert("tokenIndex broken", idx == tokenizer.getTokenIndex());
			idx += tok.length();
			assert("MyTokenizer(\"" + str + "\", \"" + delim + "\") has wrong token \"" + tok + "\" != \"" + res[i] + "\"", res[i].equals(tok));
		}
		assert("MyTokenizer(\"" + str + "\", \"" + delim + "\") has too many tokens", !tokenizer.hasMoreTokens());
	}

	private void checkConstr(String str, String delim, String res[], CustomSeparator sep) {
System.out.flush();
		MyTokenizer tokenizer = new MyTokenizer(str, delim, sep);
		String tok;
		int i;
		int idx = 0;

System.out.println("Scanning string: ");
System.out.flush();
		for (i = 0; i < res.length; i++) {
			assert("MyTokenizer(\"" + str + "\", \"" + delim + "\") lacks tokens", tokenizer.hasMoreTokens());
			tok = tokenizer.nextToken();
			assert("tokenIndex broken", idx == tokenizer.getTokenIndex());
			idx += tok.length();
			assert("MyTokenizer(\"" + str + "\", \"" + delim + "\") has wrong token \"" + tok + "\" != \"" + res[i] + "\"", res[i].equals(tok));
		}
		assert("MyTokenizer(\"" + str + "\", \"" + delim + "\") has too many tokens", !tokenizer.hasMoreTokens());
	}

	private void checkConstr(String str, String delim, String res[], Collection seps) {
System.out.flush();
		MyTokenizer tokenizer = new MyTokenizer(str, delim, seps);
		String tok;
		int i;
		int idx = 0;

System.out.println("Scanning string: ");
System.out.flush();
		for (i = 0; i < res.length; i++) {
			assert("MyTokenizer(\"" + str + "\", \"" + delim + "\") lacks tokens", tokenizer.hasMoreTokens());
			tok = tokenizer.nextToken();
System.out.println(" \"" + tok + "\"");
System.out.flush();
			assert("tokenIndex broken", idx == tokenizer.getTokenIndex());
			idx += tok.length();
			assert("MyTokenizer(\"" + str + "\", \"" + delim + "\") has wrong token \"" + tok + "\" != \"" + res[i] + "\"", res[i].equals(tok));
		}
System.out.println();
System.out.flush();
		assert("MyTokenizer(\"" + str + "\", \"" + delim + "\") has too many tokens", !tokenizer.hasMoreTokens());
	}
}

