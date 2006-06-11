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
     *
     * @param tc the testcase
     * @param b the resourcebundle
     */
    public static void checkNoDuplicates(TestCase tc,
					 ResourceBundle b) {
        Set set = new HashSet();
        for (Enumeration e = b.getKeys();
	     e.hasMoreElements();
	     ) {
	    Object c = e.nextElement();
	    Assert.assertTrue("Duplicate key \""
				+ c
				+ "\" in "
				+ b.getClass().getName(),
				!set.contains(c));
	    set.add(c);
        }
    }

    /**
     * @param tc the testcase
     * @param b the resourcebundle
     * @param tags the tags
     */
    public static void checkContainsAllFrom(TestCase tc,
					    ResourceBundle b,
					    String[] tags) {
	for (int i = 0; i < tags.length; i++)
	    Assert.assertTrue("Can't find tag \"" + tags[i]
				+ "\" in "
				+ b.getClass().getName(),
				bundleContains(b, tags[i]));
    }

    /**
     * check that all keys in ARG1 are present in ARG2.
     */
    private static void checkAllKeysFromAreIn(TestCase tc,
					      ResourceBundle b,
					      ResourceBundle locb) {
	for (Enumeration e = b.getKeys();
	     e.hasMoreElements();
	     ) {
	    String tag = (String) e.nextElement();
	    Assert.assertTrue("Missing tag \""
				+ tag
				+ "\" in "
				+ locb.getClass().getName()
				+ " (it was present in "
				+ b.getClass().getName()
				+ ")",
				bundleContains(locb, tag));
	}
    }

    /**
     * check that all keys in ARG2 are present in ARG1.
     */
    private static void checkAllKeysAreInFrom(TestCase tc,
					      ResourceBundle b,
					      ResourceBundle locb) {
	for (Enumeration e = locb.getKeys();
	     e.hasMoreElements();
	     ) {
	    String tag = (String) e.nextElement();
	    Assert.assertTrue("Extra tag \""
				+ tag
				+ "\" in "
				+ locb.getClass().getName()
				+ " (it was not present in "
				+ b.getClass().getName()
				+ ")",
				bundleContains(b, tag));
	}
    }

    /**
     * Localizations that we do.
     */
    private static final String[][] SUPPORTEDLANGUAGES = {
	{
	    "fr", "", ""
	},
	{
	    "de", "", ""
	},
	{
	    "en", "GB", ""
	},
	{
	    "es", "", ""
	},
	null
    };

    /**
     * Returns a Vector of Locales modified from list of supported languages.
     * Lift up the current locales (actually copying them to the start).
     * This means that it is possible to control what language you are
     * interested in using the -Duser.language=bla and -Duser.country=bla
     * Otherwise it would be a pain to use this since it only reports one
     * error.
     */
    private static final Vector getModifiedSupportedLanguages() {
	Vector el = new Vector();

	if (System.getProperty("user.language") != null
	    && System.getProperty("user.country") != null
	    && System.getProperty("user.variant") != null)
	    el.add(new Locale(System.getProperty("user.language"),
			      System.getProperty("user.country"),
			      System.getProperty("user.variant")));
	if (System.getProperty("user.language") != null
	    && System.getProperty("user.region") != null
	    && System.getProperty("user.variant") != null)
	    el.add(new Locale(System.getProperty("user.language"),
			      System.getProperty("user.region"),
			      System.getProperty("user.variant")));

	if (System.getProperty("user.language") != null
	    && System.getProperty("user.country") != null)
	    el.add(new Locale(System.getProperty("user.language"),
			      System.getProperty("user.country"),
			      ""));
	if (System.getProperty("user.language") != null
	    && System.getProperty("user.region") != null)
	    el.add(new Locale(System.getProperty("user.language"),
			      System.getProperty("user.region"),
			      ""));

	if (System.getProperty("user.language") != null)
	    el.add(new Locale(System.getProperty("user.language"),
			      "", ""));

	Vector v = new Vector();
	for (Enumeration ele = el.elements(); ele.hasMoreElements(); ) {
	    Locale elel = (Locale) ele.nextElement();
	    for (int j = 0; j < SUPPORTEDLANGUAGES.length; j++) {
		if (SUPPORTEDLANGUAGES[j] == null)
		    continue;
		if (elel.equals(new Locale(SUPPORTEDLANGUAGES[j][0],
					   SUPPORTEDLANGUAGES[j][1],
					   SUPPORTEDLANGUAGES[j][2]))) {
		    v.add(elel);
		    break;
		}
	    }
	}
	for (int j = 0; j < SUPPORTEDLANGUAGES.length; j++) {
	    if (SUPPORTEDLANGUAGES[j] == null)
		continue;
	    v.add(new Locale(SUPPORTEDLANGUAGES[j][0],
			     SUPPORTEDLANGUAGES[j][1],
			     SUPPORTEDLANGUAGES[j][2]));
	}
	return v;
    }


    /**
     * Checks that:
     *
     * The resource bundle contains all tags.
     *
     * The resource bundle exists for all supported languages and that each
     * of these languages contains all tags.
     *
     * @param tc	The test case.
     * @param bname     The name of the resource bundle we are looking at.
     * @param tags	The list of tags that shall exist. If the author of the
     *			test case doesn't have the patience to add it just
     *			leave it empty.
     */
    public static void checkResourceBundle(TestCase tc,
					   String bname,
					   String[] tags) {
	ResourceBundle b = ResourceBundle.getBundle(bname,
						    new Locale("", "", ""));

	checkContainsAllFrom(tc, b, tags);
	checkNoDuplicates(tc, b);

	// Check the localized parts.

	Vector v = getModifiedSupportedLanguages();

	for (Enumeration en = v.elements(); en.hasMoreElements(); ) {
	    Locale l = (Locale) en.nextElement();

	    ResourceBundle locb = ResourceBundle.getBundle(bname, l);

	    Assert.assertTrue("Resource bundle "
				+ bname
				+ " does not exist for "
				+ l.toString(),
				locb != null && locb != b);

	    checkContainsAllFrom(tc, locb, tags);
	    checkNoDuplicates(tc, locb);
	    checkAllKeysFromAreIn(tc, b, locb);
	    checkAllKeysAreInFrom(tc, b, locb);
	}
    }

    /**
     * @param tc the testcase
     * @param bname the name of the resourcebundle
     */
    public static void checkResourceBundle(TestCase tc,
					   String bname) {
	String[] n = {
	};
	checkResourceBundle(tc, bname, n);
    }
}
