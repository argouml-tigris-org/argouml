// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

import java.util.*;
import java.io.*;
import junit.framework.*;

class ClassFinder {
    // The cumulative list of classes found.
    final private Vector classNameList = new Vector ();

    /**
     * Find all classes stored in classfiles in classPathRoot
     * Inner classes are not supported.
     */
    public ClassFinder(final File classPathRoot) throws IOException {
	findAndStoreTestClasses(classPathRoot, null);
    }

    /**
     * Recursive method that adds all class names related to classfiles it finds in
     * the currentDirectory (and below).
     */
    private void findAndStoreTestClasses(final File currentDirectory,
					 final String packageprefix) throws IOException
    {
	String files[] = currentDirectory.list();
	for (int i = 0; i < files.length; i++) {
	    File file = new File(currentDirectory, files[i]);
	    String fileBase = file.getName ();
	    int idx = fileBase.indexOf(".class");
	    final int CLASS_EXTENSION_LENGTH = 6;
	    if (idx != -1 
		&& (fileBase.length() - idx) == CLASS_EXTENSION_LENGTH
		&& fileBase.startsWith("Test")) {
		String fname = packageprefix + "." + fileBase.substring(0, idx);

		System.out.println("Processing: " + fname);
		classNameList.add(fname);

	    } else if (file.isDirectory()) {
		if (packageprefix == null)
		    findAndStoreTestClasses(file, fileBase);
		else
		    findAndStoreTestClasses(file, 
					    packageprefix + "." + fileBase);
	    }
	}
    }

    /**
     * Return an iterator over the collection of classnames (Strings)
     */
    public Iterator getClasses () {
	return classNameList.iterator();
    };
}


class TestCaseLoader {
    final private Vector classList = new Vector ();

    public void loadTestCases (final Iterator classNamesIterator) {
	while (classNamesIterator.hasNext ()) {
	    String className = (String) classNamesIterator.next ();
	    try {
		Class candidateClass = Class.forName(className);
		classList.add(candidateClass);
	    } catch (ClassNotFoundException e) {
		System.err.println("Cannot load class: " + className);
	    }
	}
    }

    /**
     * Obtain an iterator over the collection of test case classes 
     * loaded by loadTestCases
     */
    public Iterator getClasses () {
	return classList.iterator ();
    }
}

public class TestAll extends TestCase {
    private static int addAllTests(final TestSuite suite,
				   final Iterator classIterator)
	throws java.io.IOException {
	int testClassCount = 0;
	while (classIterator.hasNext ()) {
	    Class testCaseClass = (Class) classIterator.next ();
	    suite.addTest(new TestSuite(testCaseClass));
	    System.out.println("Loaded test case: " 
			       + testCaseClass.getName ());
	    testClassCount++;
	}
	return testClassCount;
    }

    public static Test suite()
	throws Throwable {
	try {
	    File classRoot = new File("build/classes");
	    TestSuite suite = new TestSuite();
	    if (classRoot == null) // No test cases found.
		return suite;
	    ClassFinder classFinder = new ClassFinder(classRoot);
	    TestCaseLoader testCaseLoader = new TestCaseLoader();
	    testCaseLoader.loadTestCases(classFinder.getClasses());
	    int numberOfTests = addAllTests(suite, testCaseLoader.getClasses());
	    System.out.println("Number of test classes found: " + numberOfTests);
	    return suite;
	} catch (Throwable t) {
	    // This ensures we have extra information.
	    // Otherwise we get a "Could not invoke the suite method." message.
	    t.printStackTrace ();
	    throw t;
	}
    }

    /**
     * Basic constructor - called by the test runners.
     */

    public TestAll(String s) {
	super(s);
    }
}
