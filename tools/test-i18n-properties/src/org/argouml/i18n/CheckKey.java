/* $Id$
 *******************************************************************************
 * Copyright (c) 2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Linus Tolke
 *******************************************************************************
 */

package org.argouml.i18n;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 * Checks the contents of the internationalization property files.
 * 
 * This is a test case for property files. 
 */
public abstract class CheckKey {
    public interface IgnoredKey {
	public abstract boolean ignore(String theKey,
				       Locale theLocale,
				       ResourceBundle theLabels,
				       ResourceBundle theRootLabels);
    }

    /**
     * Create the list of objects to test.
     *
     * @param currentLocale the Locale to test.
     * @param predicate an IgnoredKey that returns true for keys to ignore
     * @return a Collection of arrays of Objects.
     */
    public static Collection<Object[]> getKeysFor(
            Locale currentLocale,
	    IgnoredKey predicate) {
	Collection<Object[]> retval = new ArrayList();
	for (String bundleName : Arrays.asList(
					       "aboutbox",
					       "action",
					       "button",
					       "checkbox",
					       "checklist",
					       "combobox",
					       "critics",
					       "dialog",
					       "filechooser",
					       "label",
					       "menu",
					       "message",
					       "misc",
					       "mnemonic",
					       "optionpane",
					       "parsing",
					       "profile",
					       "statusmsg",
					       "tab",
					       "UMLResourceBundle",
					       "wfr",
                                               "cpp" // from cpp project
					       )) {
	    try {
		ResourceBundle labels =
		    ResourceBundle.getBundle("org.argouml.i18n." + bundleName,
					     currentLocale);
		ResourceBundle rootLabels =
		    ResourceBundle.getBundle("org.argouml.i18n." + bundleName,
					     Locale.ROOT);
		for (String key : labels.keySet()) {
		    if (predicate.ignore(key,
					 currentLocale,
					 labels,
					 rootLabels)) {
			continue;
		    }

		    retval.add(new Object[] {
				   key,
				   currentLocale,
				   labels,
				   rootLabels
			       });
		}
	    } catch (MissingResourceException e) {
		// There is no such file.
	    }
	}

	return retval;
    }

    /**
     * Create the list of objects to test.
     *
     * @param currentLocale the Locale to test.
     * @return a Collection of arrays of Objects.
     */
    public static Collection<Object[]> getKeysFor(
            Locale locale) {
	return getKeysFor(locale,
			  new IgnoredKey() {
			      public boolean ignore(String theKey,
						    Locale theLocale,
						    ResourceBundle theLabels,
						    ResourceBundle theRootLabels) {
				  return false;
			      };
			  }
			  );
    }

    private String key;
    private Locale currentLocale;
    private ResourceBundle labels;
    private ResourceBundle rootLabels;

    public CheckKey(String theKey, Locale theLocale,
		    ResourceBundle theLabels,
		    ResourceBundle theRootLabels) {
	key = theKey;
	currentLocale = theLocale;
	labels = theLabels;
	rootLabels = theRootLabels;
    }

    /**
     * Check that the key exists among the base keys.
     */
    @Test public void localizedKeyIsInOrigin() {
	try {
	    assertTrue("Key " + key + " is in use by the underlying application.",
		       rootLabels.getString(key) instanceof String);
	} catch (MissingResourceException e) {
	    fail("Key " + key + " does not exist in root bundle.");
	}
    }

    /**
     * Check that the key is localized.
     */
    @Test public void keyIsLocalized() {
	try {
	    assertTrue("Key " + key + " localized for " + currentLocale + ".",
		       labels.getString(key) != rootLabels.getString(key));
	} catch (MissingResourceException e) {};
    }

    /**
     * Check that the strings has the same formatted values.
     */
    @Test public void checkMessageFormatValues() {
	String i18nString = labels.getString(key);
	String rootString;
	try {
	    rootString = rootLabels.getString(key);
	} catch (MissingResourceException e) {
	    return;
	};

	if (i18nString.equals(rootString)) {
	    return;
	}

	for (int i = 0; ; i++) {
	    String match = ".*[{]" + i + "[},].*";
	    boolean i18nFound = i18nString.matches(match);
	    boolean rootFound = rootString.matches(match);

	    assertTrue("Key " + key + " use value " + i
		       + " to the same extent",
		       i18nFound == rootFound);

	    if (!i18nFound && !rootFound) {
		break;
	    }
	}
    }
}
