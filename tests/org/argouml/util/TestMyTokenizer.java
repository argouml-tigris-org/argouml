// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import java.util.Collection;
import java.util.Vector;

import junit.framework.TestCase;

/**
 * Testing the MyTokenizer class.
 *
 */
public class TestMyTokenizer extends TestCase {

    /**
     * The constructor.
     *
     * @param str the name of the test.
     */
    public TestMyTokenizer(String str) {
	super(str);
    }

    /**
     * Test the constructor.
     */
    public void testConstructor1() {
	Vector seps = new Vector();
	seps.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	seps.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);

	String str1 = "A String";
	String delim1 = " ";
 	String res1[] = {
	    "A", " ", "String"
	};

	checkConstr(str1, delim1, res1);
	checkConstr(str1, delim1, res1, MyTokenizer.SINGLE_QUOTED_SEPARATOR);
	checkConstr(str1, delim1, res1, MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	checkConstr(str1, delim1, res1, MyTokenizer.PAREN_EXPR_SEPARATOR);
	checkConstr(
		    str1,
		    delim1,
		    res1,
		    MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);
	checkConstr(str1, delim1, res1, seps);
    }

    /**
     * Test the constructor.
     */
    public void testConstructor2() {
	Vector seps = new Vector();
	seps.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	seps.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);

	String str2 = "public newAttr [1..*] : Type = Val {param}";
	String delim2 = " ,\t,<<,>>,[,],:,=,{,},\\,";
	String res2[] = {
	    "public",
	    " ",
	    "newAttr",
	    " ",
	    "[",
	    "1..*",
	    "]",
	    " ",
	    ":",
	    " ",
	    "Type",
	    " ",
	    "=",
	    " ",
	    "Val",
	    " ",
	    "{",
	    "param",
	    "}"
	};

	checkConstr(str2, delim2, res2);
	checkConstr(str2, delim2, res2, MyTokenizer.SINGLE_QUOTED_SEPARATOR);
	checkConstr(str2, delim2, res2, MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	checkConstr(str2, delim2, res2, MyTokenizer.PAREN_EXPR_SEPARATOR);
	checkConstr(
		    str2,
		    delim2,
		    res2,
		    MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);
	checkConstr(str2, delim2, res2, seps);
    }

    /**
     * Test the constructor.
     */
    public void testConstructor3() {
	Vector seps = new Vector();
	seps.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	seps.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);

	String str3 = "<< stereo >> newAttr";
	String delim3 = " ,\t,<<,>>,[,],:,=,{,},\\,";
	String res3[] = {
	    "<<", " ", "stereo", " ", ">>", " ", "newAttr"
	};

	checkConstr(str3, delim3, res3);
	checkConstr(str3, delim3, res3, MyTokenizer.SINGLE_QUOTED_SEPARATOR);
	checkConstr(str3, delim3, res3, MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	checkConstr(str3, delim3, res3, MyTokenizer.PAREN_EXPR_SEPARATOR);
	checkConstr(
		    str3,
		    delim3,
		    res3,
		    MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);
	checkConstr(str3, delim3, res3, seps);
    }

    /**
     * Test the constructor.
     */
    public void testConstructor4() {
	Vector seps = new Vector();
	seps.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	seps.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);

	String str4 = " newAttr = \": h\" ";
	String delim4 = " ,\t,<<,>>,[,],:,=,{,},\\,";
	String res4dot1[] = {
	    " ", "newAttr", " ", "=", " ", "\"", ":", " ", "h\"", " "
	};
	String res4dot2[] = {
	    " ", "newAttr", " ", "=", " ", "\": h\"", " "
	};

	checkConstr(str4, delim4, res4dot1);
	checkConstr(str4, delim4, res4dot1,
	            MyTokenizer.SINGLE_QUOTED_SEPARATOR);
	checkConstr(str4, delim4, res4dot2,
	            MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	checkConstr(str4, delim4, res4dot1, MyTokenizer.PAREN_EXPR_SEPARATOR);
	checkConstr(
		    str4,
		    delim4,
		    res4dot1,
		    MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);
	checkConstr(str4, delim4, res4dot2, seps);
    }

    /**
     * Test the constructor.
     */
    public void testConstructor5() {
	Vector seps = new Vector();
	seps.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	seps.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);

	String str5 = " newAttr = \': h\' ";
	String delim5 = " ,\t,<<,>>,[,],:,=,{,},\\,";
	String res5dot1[] = {
	    " ", "newAttr", " ", "=", " ", "\'", ":", " ", "h\'", " "
	};
	String res5dot2[] = {
	    " ", "newAttr", " ", "=", " ", "\': h\'", " "
	};

	checkConstr(str5, delim5, res5dot1);
	checkConstr(str5, delim5, res5dot2,
	            MyTokenizer.SINGLE_QUOTED_SEPARATOR);
	checkConstr(str5, delim5, res5dot1,
	            MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	checkConstr(str5, delim5, res5dot1, MyTokenizer.PAREN_EXPR_SEPARATOR);
	checkConstr(
		    str5,
		    delim5,
		    res5dot1,
		    MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);
	checkConstr(str5, delim5, res5dot2, seps);
    }

    /**
     * Test the constructor.
     */
    public void testConstructor6() {
	Vector seps = new Vector();
	seps.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	seps.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);

	String str6 = "newAttr = (: ()h) ";
	String delim6 = " ,\t,<<,>>,[,],:,=,{,},\\,";
	String res6dot1[] = {
	    "newAttr", " ", "=", " ", "(", ":", " ", "()h)", " "
	};
	String res6dot2[] = {
	    "newAttr", " ", "=", " ", "(: ()h)", " "
	};

	checkConstr(str6, delim6, res6dot1);
	checkConstr(str6, delim6, res6dot1,
	            MyTokenizer.SINGLE_QUOTED_SEPARATOR);
	checkConstr(str6, delim6, res6dot1,
	            MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	checkConstr(str6, delim6, res6dot2, MyTokenizer.PAREN_EXPR_SEPARATOR);
	checkConstr(
		    str6,
		    delim6,
		    res6dot2,
		    MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);
	checkConstr(str6, delim6, res6dot1, seps);
    }

    /**
     * Test the constructor.
     */
    public void testConstructor7() {
	Vector seps = new Vector();
	seps.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	seps.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);

	String str7 = "newAttr = (\"\\\" )(\" () \'\\\' )(\')";
	String delim7 = " ,\t,<<,>>,[,],:,=,{,},\\,";
	String res7dot1[] = {
	    "newAttr",
	    " ",
	    "=",
	    " ",
	    "(\"\\\"",
	    " ",
	    ")(\"",
	    " ",
	    "()",
	    " ",
	    "\'\\\'",
	    " ",
	    ")(\')"
	};
	String res7dot2[] = {
	    "newAttr",
	    " ",
	    "=",
	    " ",
	    "(\"\\\"",
	    " ",
	    ")(\"",
	    " ",
	    "()",
	    " ",
	    "\'\\\' )(\'",
	    ")"
	};
	String res7dot3[] = {
	    "newAttr",
	    " ",
	    "=",
	    " ",
	    "(",
	    "\"\\\" )(\"",
	    " ",
	    "()",
	    " ",
	    "\'\\\'",
	    " ",
	    ")(\')"
	};
	String res7dot4[] = {
	    "newAttr",
	    " ",
	    "=",
	    " ",
	    "(\"\\\" )",
	    "(\" () \'\\\' )",
	    "(\')"
	};
	String res7dot5[] = {
	    "newAttr", " ", "=", " ", "(\"\\\" )(\" () \'\\\' )(\')"
	};
	String res7dot6[] = {
	    "newAttr",
	    " ",
	    "=",
	    " ",
	    "(",
	    "\"\\\" )(\"",
	    " ",
	    "()",
	    " ",
	    "\'\\\' )(\'",
	    ")"
	};

	checkConstr(str7, delim7, res7dot1);
	checkConstr(str7, delim7, res7dot2,
	            MyTokenizer.SINGLE_QUOTED_SEPARATOR);
	checkConstr(str7, delim7, res7dot3,
	            MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	checkConstr(str7, delim7, res7dot4, MyTokenizer.PAREN_EXPR_SEPARATOR);
	checkConstr(
		    str7,
		    delim7,
		    res7dot5,
		    MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);
	checkConstr(str7, delim7, res7dot6, seps);
    }

    /**
     * Test putToken().
     */
    public void testPutToken() {
	MyTokenizer st = new MyTokenizer("Hello old friend", " ");
	int oidx;

	assertTrue("Token 1", "Hello".equals(st.nextToken()));

	oidx = st.getTokenIndex();
	st.putToken(",");
	assertTrue("PutToken 1", ",".equals(st.nextToken()));
	assertTrue("PutTokenIndex 1", oidx == st.getTokenIndex());

	assertTrue("Token 2", " ".equals(st.nextToken()));
	assertTrue("Token 3", "old".equals(st.nextToken()));

	oidx = st.getTokenIndex();
	st.putToken(",");
	st.putToken(";");
	assertTrue("PutToken 2", ";".equals(st.nextToken()));
	assertTrue("PutTokenIndex 2", oidx == st.getTokenIndex());

	st.putToken("?");
	assertTrue("PutToken 3", "?".equals(st.nextToken()));
	assertTrue("PutTokenIndex 3", oidx == st.getTokenIndex());

	assertTrue("Token 4", " ".equals(st.nextToken()));
	assertTrue("Token 5", "friend".equals(st.nextToken()));

	assertTrue("MoreTokens 1", !st.hasMoreTokens());
	st.putToken("?");
	assertTrue("MoreTokens 2", st.hasMoreTokens());
	assertTrue("PutToken 4", "?".equals(st.nextToken()));
	assertTrue("MoreTokens 3", !st.hasMoreTokens());

	try {
	    st.putToken(null);
	    assertTrue("Allows nulls", false);
	} catch (NullPointerException npe) {
	}
    }

    /**
     * Test lineseparators.
     */
    public void testLineSeparator() {
	MyTokenizer st = new MyTokenizer("str1\nstr2\r\nstr3\rstr4",
					 "",
					 MyTokenizer.LINE_SEPARATOR);
	MyTokenizer st2 = new MyTokenizer("\n\n\r\n\r\n\r\r",
					  "",
					  MyTokenizer.LINE_SEPARATOR);

	assertEquals(st.nextElement(), "str1");
	assertEquals(st.nextElement(), "\n");
	assertEquals(st.nextElement(), "str2");
	assertEquals(st.nextElement(), "\r\n");
	assertEquals(st.nextElement(), "str3");
	assertEquals(st.nextElement(), "\r");
	assertEquals(st.nextElement(), "str4");
	assertTrue(!st.hasMoreElements());

	assertEquals(st2.nextElement(), "\n");
	assertEquals(st2.nextElement(), "\n");
	assertEquals(st2.nextElement(), "\r\n");
	assertEquals(st2.nextElement(), "\r\n");
	assertEquals(st2.nextElement(), "\r");
	assertEquals(st2.nextElement(), "\r");
	assertTrue(!st2.hasMoreElements());
    }

    private void checkConstr(String str, String delim, String res[]) {
	MyTokenizer tokenizer = new MyTokenizer(str, delim);
	String tok;
	int i;
	int idx = 0;

	for (i = 0; i < res.length; i++) {
	    assertTrue(
		       "MyTokenizer(\"" + str + "\", \"" + delim
		           + "\") lacks tokens",
		       tokenizer.hasMoreTokens());
	    tok = tokenizer.nextToken();
	    assertTrue("tokenIndex broken", idx == tokenizer.getTokenIndex());
	    idx += tok.length();
	    assertTrue(
		       "MyTokenizer(\""
		       + str
		       + "\", \""
		       + delim
		       + "\") has wrong token \""
		       + tok
		       + "\" != \""
		       + res[i]
		       + "\"",
		       res[i].equals(tok));
	}
	assertTrue(
		   "MyTokenizer(\""
		   + str
		   + "\", \""
		   + delim
		   + "\") has too many tokens",
		   !tokenizer.hasMoreTokens());
    }

    private void checkConstr(
			     String str,
			     String delim,
			     String res[],
			     CustomSeparator sep) {
	MyTokenizer tokenizer = new MyTokenizer(str, delim, sep);
	String tok;
	int i;
	int idx = 0;

	for (i = 0; i < res.length; i++) {
	    assertTrue(
		       "MyTokenizer(\"" + str + "\", \"" + delim
		           + "\") lacks tokens",
		       tokenizer.hasMoreTokens());
	    tok = tokenizer.nextToken();
	    assertTrue("tokenIndex broken", idx == tokenizer.getTokenIndex());
	    idx += tok.length();
	    assertTrue(
		       "MyTokenizer(\""
		       + str
		       + "\", \""
		       + delim
		       + "\") has wrong token \""
		       + tok
		       + "\" != \""
		       + res[i]
		       + "\"",
		       res[i].equals(tok));
	}
	assertTrue(
		   "MyTokenizer(\""
		   + str
		   + "\", \""
		   + delim
		   + "\") has too many tokens",
		   !tokenizer.hasMoreTokens());
    }

    private void checkConstr(
			     String str,
			     String delim,
			     String res[],
			     Collection seps) {
	MyTokenizer tokenizer = new MyTokenizer(str, delim, seps);
	String tok;
	int i;
	int idx = 0;

	for (i = 0; i < res.length; i++) {
	    assertTrue(
		       "MyTokenizer(\"" + str + "\", \"" + delim
		           + "\") lacks tokens",
		       tokenizer.hasMoreTokens());
	    tok = tokenizer.nextToken();
	    assertTrue("tokenIndex broken", idx == tokenizer.getTokenIndex());
	    idx += tok.length();
	    assertTrue(
		       "MyTokenizer(\""
		       + str
		       + "\", \""
		       + delim
		       + "\") has wrong token \""
		       + tok
		       + "\" != \""
		       + res[i]
		       + "\"",
		       res[i].equals(tok));
	}
	assertTrue(
		   "MyTokenizer(\""
		   + str
		   + "\", \""
		   + delim
		   + "\") has too many tokens",
		   !tokenizer.hasMoreTokens());
    }
}
