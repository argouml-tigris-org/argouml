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

package org.argouml.util;
import java.util.*;
import junit.framework.*;

/**
 *   This class is the base class of tests that tests
 *   ListResourceBundle.
 *   It has functions to:
 *   - capture any missing resource names
 *   - compare list of resource names between different files.
 *
 *   This was originally written by Curt Arnold. Converted to JUnit test
 *   case by Linus Tolke.
 *
 *   @author Linus Tolke
 *   @since 0.9.8
 *   @see java.util.ListResourceBundle
 */
public class CheckResourceBundle {

    private static boolean bundleContains(ResourceBundle b, String key) {
	try {
	    b.getObject(key);
	}
	catch (MissingResourceException e) {
	    return false;
	}
	return true;
    }

    /**
     * check that no key is entered twice
     */
    public static void checkNoDuplicates(TestCase tc,
					 ResourceBundle b) {
        Set set = new HashSet();
        for(Enumeration e = b.getKeys();
	    e.hasMoreElements();
	    ) {
	    Object c = e.nextElement();
	    tc.assert("Duplicate key \"" 
		      + c
		      + "\" in "
		      + b.getClass().getName(),
		      !set.contains(c));
	    set.add(c);
        }
    }

    public static void checkContainsAllFrom(TestCase tc,
					    ResourceBundle b,
					    String[] tags) {
	for (int i = 0; i < tags.length; i++)
	    tc.assert("Can't find tag \"" + tags[i]
		      + "\" in "
		      + b.getClass().getName(),
		      bundleContains(b, tags[i]));
    }

    /**
     * check that two ResourceBundles has the same set of keys.
     */
    private static void checkSameSetOfKeys(TestCase tc,
					   ResourceBundle b1,
					   ResourceBundle b2) {
	checkAllKeysFromAreIn(tc, b1, b2);
	checkAllKeysFromAreIn(tc, b2, b1);
    }

    /**
     * check that all keys in ARG1 are present in ARG2.
     */
    private static void checkAllKeysFromAreIn(TestCase tc,
					      ResourceBundle b1,
					      ResourceBundle b2) {
	for (Enumeration e = b1.getKeys();
	     e.hasMoreElements();
	     ) {
	    String tag = (String) e.nextElement();
	    tc.assert("Missing tag \""
		      + tag
		      + "\" in "
		      + b2.getClass().getName()
		      + " (it was present in "
		      + b1.getClass().getName()
		      + ")",
		      bundleContains(b2, tag));
	}
    }

    /**
     * Localizations that we do.
     */
    private static final String[] supportedLanguages = { 
	"fr",
	"de",
	"en_GB",
	"es", 
	null };
    /**
     * Checks that:
     *
     * The resource bundle contains all tags.
     *
     * The resource bundle exists for all supported languages and that each
     * of these languages contains all tags.
     *
     * @param tc	The test case.
     * @param b		The resource bundle we are looking at.
     * @param tags	The list of tags that shall exist. If the author of the
     *			test case doesn't have the patience to add it just
     *			leave it empty.
     */
    public static void checkResourceBundle(TestCase tc,
					   ListResourceBundle b,
					   String[] tags) {
	checkContainsAllFrom(tc, b, tags);
	checkNoDuplicates(tc, b);

	// Check the localized parts.
	for (int i = 0; i < supportedLanguages.length; i++) {
	    if (supportedLanguages[i] == null)
		continue;

	    Class locclass;
	    String newClassName = b.getClass().getName() 
		+ "_" 
		+ supportedLanguages[i];
	    try {
		locclass = Class.forName(newClassName);
	    }
	    catch (ClassNotFoundException n) {
		tc.assert("Class "
			  + newClassName
			  + " does not exist for " 
			  + supportedLanguages[i],
			  false);
		return;
	    };

	    ResourceBundle locb;
	    try {
		locb = (ResourceBundle)locclass.newInstance();
	    }
	    catch (InstantiationException e) {
		tc.assert("Class "
			  + newClassName
			  + " cannot be instantiated for " 
			  + supportedLanguages[i],
			  false);
		return;
	    }
	    catch (IllegalAccessException e) {
		tc.assert("Class "
			  + newClassName
			  + " cannot be instantiated "
			  + "(IllegalAccessException) for " 
			  + supportedLanguages[i],
			  false);
		return;
	    }

	    checkContainsAllFrom(tc, locb, tags);
	    checkNoDuplicates(tc, locb);
	    checkSameSetOfKeys(tc, b, locb);
	}
    }

    public static void checkResourceBundle(TestCase tc,
					   ListResourceBundle b) {
	String[] n = { };
	checkResourceBundle(tc, b, n);
    }
}
