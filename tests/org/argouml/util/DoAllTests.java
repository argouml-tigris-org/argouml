package org.argouml.util;

import java.util.*;

import junit.framework.*;
import junit.runner.*;

public class DoAllTests extends TestSuite {
    public static Test suite() {
	TestSuite suite = new TestSuite();
	int count = 0;

	for (Enumeration e = (new LoadingTestCollector()).collectTests();
	     e.hasMoreElements();
	     ) {
	    Object o = e.nextElement();

	    if (!(o instanceof String))
		continue;
	    String s = (String) o;

	    if (s.equals("org.argouml.util.DoAllTests"))
		continue;

	    Class candidate;
	    try {
		candidate = Class.forName(s);
	    } catch (ClassNotFoundException exception) {
		System.err.println("Cannot load class: " + s);
		continue;
	    }

	    suite.addTest(new TestSuite(candidate));
	    count++;
	}
	System.out.println("Number of test classes found: " + count);

	return suite;
    }
}

