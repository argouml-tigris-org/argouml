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

import java.util.Collection;
import java.util.Vector;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;

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
 	String res1[] = {
	    "A", " ", "String"
	};

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

	String str3 = "<< stereo >> newAttr";
	String delim3 = " ,\t,<<,>>,[,],:,=,{,},\\,";
	String res3[] = {
	    "<<", " ", "stereo", " ", ">>", " ", "newAttr"
	};
	
	String str4 = " newAttr = \": h\" ";
	String delim4 = " ,\t,<<,>>,[,],:,=,{,},\\,";
	String res4_1[] = {
	    " ", "newAttr", " ", "=", " ", "\"", ":", " ", "h\"", " " 
	};
	String res4_2[] = {
	    " ", "newAttr", " ", "=", " ", "\": h\"", " "
	};

	String str5 = " newAttr = \': h\' ";
	String delim5 = " ,\t,<<,>>,[,],:,=,{,},\\,";
	String res5_1[] = {
	    " ", "newAttr", " ", "=", " ", "\'", ":", " ", "h\'", " " 
	};
	String res5_2[] = {
	    " ", "newAttr", " ", "=", " ", "\': h\'", " "
	};

	String str6 = "newAttr = (: ()h) ";
	String delim6 = " ,\t,<<,>>,[,],:,=,{,},\\,";
	String res6_1[] = {
	    "newAttr", " ", "=", " ", "(", ":", " ", "()h)", " " 
	};
	String res6_2[] = {
	    "newAttr", " ", "=", " ", "(: ()h)", " "
	};

	String str7 = "newAttr = (\"\\\" )(\" () \'\\\' )(\')";
	String delim7 = " ,\t,<<,>>,[,],:,=,{,},\\,";
	String res7_1[] = {
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
	String res7_2[] = {
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
	String res7_3[] = {
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
	String res7_4[] = {
	    "newAttr",
	    " ",
	    "=",
	    " ",
	    "(\"\\\" )",
	    "(\" () \'\\\' )",
	    "(\')" 
	};
	String res7_5[] = {
	    "newAttr", " ", "=", " ", "(\"\\\" )(\" () \'\\\' )(\')" 
	};
	String res7_6[] = {
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

	checkConstr(str4, delim4, res4_1);
	checkConstr(str4, delim4, res4_1, MyTokenizer.SINGLE_QUOTED_SEPARATOR);
	checkConstr(str4, delim4, res4_2, MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	checkConstr(str4, delim4, res4_1, MyTokenizer.PAREN_EXPR_SEPARATOR);
	checkConstr(
		    str4,
		    delim4,
		    res4_1,
		    MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);
	checkConstr(str4, delim4, res4_2, seps);

	checkConstr(str5, delim5, res5_1);
	checkConstr(str5, delim5, res5_2, MyTokenizer.SINGLE_QUOTED_SEPARATOR);
	checkConstr(str5, delim5, res5_1, MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	checkConstr(str5, delim5, res5_1, MyTokenizer.PAREN_EXPR_SEPARATOR);
	checkConstr(
		    str5,
		    delim5,
		    res5_1,
		    MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);
	checkConstr(str5, delim5, res5_2, seps);

	checkConstr(str6, delim6, res6_1);
	checkConstr(str6, delim6, res6_1, MyTokenizer.SINGLE_QUOTED_SEPARATOR);
	checkConstr(str6, delim6, res6_1, MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	checkConstr(str6, delim6, res6_2, MyTokenizer.PAREN_EXPR_SEPARATOR);
	checkConstr(
		    str6,
		    delim6,
		    res6_2,
		    MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);
	checkConstr(str6, delim6, res6_1, seps);

	checkConstr(str7, delim7, res7_1);
	checkConstr(str7, delim7, res7_2, MyTokenizer.SINGLE_QUOTED_SEPARATOR);
	checkConstr(str7, delim7, res7_3, MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
	checkConstr(str7, delim7, res7_4, MyTokenizer.PAREN_EXPR_SEPARATOR);
	checkConstr(
		    str7,
		    delim7,
		    res7_5,
		    MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);
	checkConstr(str7, delim7, res7_6, seps);
    }

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

    private void checkConstr(String str, String delim, String res[]) {
	MyTokenizer tokenizer = new MyTokenizer(str, delim);
	String tok;
	int i;
	int idx = 0;

	for (i = 0; i < res.length; i++) {
	    assertTrue(
		       "MyTokenizer(\"" + str + "\", \"" + delim + "\") lacks tokens",
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
		       "MyTokenizer(\"" + str + "\", \"" + delim + "\") lacks tokens",
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
		       "MyTokenizer(\"" + str + "\", \"" + delim + "\") lacks tokens",
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
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
	super.setUp();
	ArgoSecurityManager.getInstance().setAllowExit(true);
    }
}
