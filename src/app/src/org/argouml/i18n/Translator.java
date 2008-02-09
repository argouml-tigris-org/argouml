// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.i18n;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.tigris.gef.util.Localizer;

/**
 * The API class to the localization. All localization calls goes through
 * this class.
 *
 * @author Jean-Hugues de Raigniac
 */
public final class Translator {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(Translator.class);

    /**
     * Where we search for bundles.
     */
    private static final String BUNDLES_PATH = "org.argouml.i18n";

    /**
     * Store bundles for current Locale.
     */
    private static Map<String, ResourceBundle> bundles;

    /**
     * Store of ClassLoaders where we could find the bundles.
     */
    private static List<ClassLoader> classLoaders = 
        new ArrayList<ClassLoader>();

    /**
     * Used to make this class self-initialising when needed.
     */
    private static boolean initialized;

    /**
     * Used to keep track of the original system default locale
     */
    private static Locale systemDefaultLocale;
    
    /**
     * This class should only be used in a static context so make
     * the constructor private.
     */
    private Translator() {
    }

    /**
     * Alternate initialization entry point for use by ArgoEclipse.
     * It leaves out telling GEF about bundles that it won't be able
     * to access.

     * NOTE: This must be called *before* any other methods are called to
     * work properly.
     * 
     * @deprecated by MVW in V0.25, replaced by initForEclipse(String).
     */
    @Deprecated
    public static void initForEclipse() {
        initInternal(""/*Configuration.getString(Argo.KEY_LOCALE)*/);
    }

    /**
     * Alternate initialization entry point for use by ArgoEclipse.
     * It leaves out telling GEF about bundles that it won't be able
     * to access. <p>
     * 
     * NOTE: This must be called *before* any other methods are called to
     * work properly.
     *
     * @param locale the configured locale or "" or null
     */
    public static void initForEclipse (String locale) {
        initInternal(locale);
    }

    /**
     * Default Locale is set and resources Bundles are loaded.
     * @deprecated by MVW in V0.25.3, replaced by init(String locale).
     */
    @Deprecated
    public static void init () {
        initInternal(""/*Configuration.getString(Argo.KEY_LOCALE)*/);
        /* TODO: This is an uplevel reference from GEF to ArgoUML - tfm
         * What is this bundle used for?  Is it used? 
         * MVW: Move into Main? */
        Localizer.addResource("UMLMenu",
			      "org.argouml.i18n.UMLResourceBundle");
    }

    /**
     * Initialise the locale.
     * 
     * @param locale a string with the locale
     */
    public static void init(String locale) {
//        assert !initialized; // GUITestActionOpenProject fails over this...
        initialized = true;

        // Retain the original one:
        systemDefaultLocale = Locale.getDefault();

        if ((!"".equals(locale)) && (locale != null)) {
            setLocale(locale);
        } else {
            setLocale(new Locale(
                    System.getProperty("user.language", "en"),
                    System.getProperty("user.country", "")));
        }

        /* TODO: This is using internal knowledge of GEF.  It should
         * handle this itself. - tfm
         * MVW: Move into something like Main.initGEF() */
        Localizer.addResource("GefBase",
                              "org.tigris.gef.base.BaseResourceBundle");
        Localizer.addResource(
                "GefPres",
                "org.tigris.gef.presentation.PresentationResourceBundle");
    }

    /*
     * Internal initialization method.  Handles initialization which
     * is common to both public methods.
     */
    private static void initInternal (String s) {
        assert !initialized;
        initialized = true;
        // Retain the original one:
        systemDefaultLocale = Locale.getDefault();

        if ((!"".equals(s)) && (s != null)) {
            setLocale(s);
        } else {
            setLocale(new Locale(
                    System.getProperty("user.language", "en"),
                    System.getProperty("user.country", "")));
        }

        // TODO: This is using internal knowledge of GEF.  It should
        // handle this itself. - tfm
        Localizer.addResource("GefBase",
			      "org.tigris.gef.base.BaseResourceBundle");
        Localizer.addResource(
		"GefPres",
		"org.tigris.gef.presentation.PresentationResourceBundle");
    }

    /**
     * For Locale selection.<p>
     *
     * TODO: Detect the available locales from the available files.
     *
     * @return Locales used in ArgoUML
     */
    public static Locale[] getLocales() {
        return new Locale[] {
            Locale.ENGLISH,
            Locale.FRENCH,
            new Locale("es", ""),
            Locale.GERMAN,
            Locale.ITALIAN,
            new Locale("nb", ""),
            new Locale("pt", ""),
            new Locale("ru", ""),
            Locale.CHINESE,
            Locale.UK,
        };
    }

    /**
     * Change the current Locale. The string with the name follows
     * this BNF format: <p>
     *     language [ "_" country ]
     * <p>
     * Only use this before the GUI is initialized.
     *
     * @param name the name of the new locale
     */
    public static void setLocale(String name) {
        /* This is needed for the JUnit tests. 
         * Otherwise a "assert initialized" would suffice. */
        if (!initialized) {
            init("en");
        }
        String language = name;
        String country = "";
        int i = name.indexOf("_");
        if ((i > 0) && (name.length() > i + 1)) {
            language = name.substring(0, i);
            country = name.substring(i + 1);
        }
        setLocale(new Locale(language, country));
    }

    /**
     * Change the current Locale.
     * <p>
     * Only use this before the GUI is initialized.
     *
     * @param locale the new Locale
     */
    public static void setLocale(Locale locale) {
        Locale.setDefault(locale);
        bundles = new HashMap<String, ResourceBundle>();
    }

    /**
     * Returns the original value of the default locale for this instance
     * of the Java Virtual Machine (which is independent from the selected
     * configuration).
     * 
     * @return the original system default locale
     */
    public static Locale getSystemDefaultLocale() {
        return systemDefaultLocale;
    }

    /**
     * Add another class loader that the resource bundle could be located
     * through.
     *
     * @param cl The classloader to add.
     */
    public static void addClassLoader(ClassLoader cl) {
	classLoaders.add(cl);
    }


    /**
     * Loads the bundle (if not already loaded).
     *
     * @param name The name of the bundle to load.
     */
    private static void loadBundle(String name) {
        if (bundles.containsKey(name)) {
            return;
        }
        String resource = BUNDLES_PATH + "." + name;
        ResourceBundle bundle = null;
        try {
            LOG.debug("Loading " + resource);
            Locale locale = Locale.getDefault();
            bundle = ResourceBundle.getBundle(resource, locale);
        } catch (MissingResourceException e1) {
            LOG.debug("Resource " + resource
		      + " not found in the default class loader.");

	    Iterator iter = classLoaders.iterator();
	    while (iter.hasNext()) {
		ClassLoader cl = (ClassLoader) iter.next();
		try {
		    LOG.debug("Loading " + resource + " from " + cl);
		    bundle =
			ResourceBundle.getBundle(resource,
						 Locale.getDefault(),
						 cl);
		    break;
		} catch (MissingResourceException e2) {
		    LOG.debug("Resource " + resource + " not found in " + cl);
		}
	    }
        }

        bundles.put(name, bundle);
    }

    /**
     * Calculate the name from the key.
     *
     * @param key The key to look up.
     * @return The name of the file or <code>null</code> if not possible.
     */
    private static String getName(String key) {
        if (key == null) {
            return null;
        }

        int indexOfDot = key.indexOf(".");
        if (indexOfDot > 0) {
            return key.substring(0, indexOfDot);
        }
        return null;
    }


    /**
     * Synonym for messageFormat to encourage developers to convert
     * existing uses of localize() + string concatentation to use
     * this method instead.
     * @see org.argouml.i18n.Translator#messageFormat(String, Object[])
     *
     * @param key the key to localize
     * @param args the arguments as Objects to be inserted in string
     * @return String the localized string
     */
    public static String localize(String key, Object[] args) {
        return messageFormat(key, args);
    }

    /**
     * The main function of this class that localizes strings.
     *
     * @param key The key to localize.
     * @return The localized String.
     */
    public static String localize(String key) {
        /* This is needed for the JUnit tests. 
         * Otherwise a "assert initialized" would suffice. */
        if (!initialized) {
            init("en");
        }

        if (key == null) {
            throw new IllegalArgumentException("null");
        }

        String name = getName(key);
        if (name == null) {
            return Localizer.localize("UMLMenu", key);
        }

        loadBundle(name);

        ResourceBundle bundle = bundles.get(name);
        if (bundle == null) {
            LOG.debug("Bundle (" + name + ") for resource "
                    + key + " not found.");
            return key;
        }

        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            LOG.debug("Resource " + key + " not found.");
            return key;
        }
    }

    /**
     * Generates an localized String with arguments.<p>
     *
     * The localized string is a pattern to be processed by
     * {@link MessageFormat}.
     *
     * @param key the key to localize
     * @param args the args as Objects, inserted in the localized String
     * @return the localized String with inserted arguments
     */
    public static String messageFormat(String key, Object[] args) {
        return new MessageFormat(localize(key)).format(args);
    }
}
