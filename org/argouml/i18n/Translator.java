// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import java.io.InputStream;
import java.io.IOException;
import java.text.MessageFormat;				
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.tigris.gef.util.Localizer;

/**
 * The API class to the localization. All localization calls goes through
 * this class.
 *
 * @author Jean-Hugues de Raigniac
 */
public class Translator {
    /** logger */
    private static final Logger LOG = Logger.getLogger(Translator.class);

    /** Binding between new key names and old ones needed by gef. */
    private static Properties images = null;

    /** Property file containing the bindings. */
    private static String propertiesFile = "images.properties";

    /**
     * Default Locale is set and resources Bundles are loaded.
     */
    public static void init () {

        Locale.setDefault(new Locale(System.getProperty("user.language", "en"),
				     System.getProperty("user.country", "")));

        /** bundle default Locale, different from user default Locale */
        org.workingfrog.i18n.util.Translator.init();
        org.workingfrog.i18n.util.Translator.setDefaultLocale(
		new Locale("en", ""));
        org.workingfrog.i18n.util.Translator.setBundlesPath("org.argouml.i18n");
        org.workingfrog.i18n.util.Translator.setLogLevel("none");
  
        Localizer.addResource("GefBase",
			      "org.tigris.gef.base.BaseResourceBundle");
        Localizer.addResource(
		"GefPres",
		"org.tigris.gef.presentation.PresentationResourceBundle");
        Localizer.addResource("UMLMenu",
			      "org.argouml.i18n.UMLResourceBundle");
    }

    /**
     * For Locale selection.
     *
     * @return Locales used in ArgoUML
     */
    public static Locale[] getLocales() {
        return org.workingfrog.i18n.util.Translator.getLocales(
		new Translator());
    }

    /**
     * Change the current Locale.
     *
     * @param locale the new Locale
     */
    public static void setLocale(Locale locale) {
        org.workingfrog.i18n.util.Translator.setLocale(locale);
    }

    /**   
     * Loads image bindings from a File.
     *
     * @param file the properties file
     * @return the properties in file
     */
    private static Properties loadImageBindings(String file) {

        InputStream inputStream = null;
        Properties properties = new Properties();

        try {
            inputStream = Translator.class.getResourceAsStream(propertiesFile);
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException ex) {
            LOG.fatal("Unable to load properties from file: " + file, ex);
            System.exit(1);
        }

        return properties;
    }

    /**   
     * Provide a "gef compliant" image file name.
     *
     * @param name the new i18n key
     * @return the old i18n key
     */
    public static String getImageBinding(String name) {

        String binding = null;

        if (images == null) {
            images = loadImageBindings(propertiesFile);
        }

        binding = images.getProperty(name);

        if (binding == null) {
            return name;
        } else {
            return binding;
        }
    }

    /** 
     * Helper for localization to eliminate the need to import
     * the gef util library.<p>
     *
     * This is only used when retrieving the strings localized in GEF.
     *
     * @deprecated by Linus Tolke as of 0.17.2.
     *             Use {@link #localize(String key)}.
     * @param bundle a binding to a bundle of i18n resources
     * @param key the key to loacalize
     * @return the translation
     */
    public static String localize(String bundle, String key) {
        String gefValue = org.tigris.gef.util.Localizer.localize(bundle, key);
        return org.workingfrog.i18n.util.Translator.localize(key, gefValue);
    }

    /**
     * Helper for those that don't want to give the bundle.<p>
     *
     * <em>Note:</em> The one argument
     * {@link org.workingfrog.i18n.util.Translator#localize(String key) 
     * localize(key)}
     * function doesn't seem to work for tags that aren't prefixed with the
     * property file name. We get a NullPointerException. For this reason we
     * stick to the two argument
     * {@link org.workingfrog.i18n.util.Translator#localize(String, String)
     * localize(bundle, key)}
     * for the time being.
     *
     * @param key The key to localize.
     * @return The localized String.
     */
    public static String localize(String key) {
        return org.workingfrog.i18n.util.Translator.localize(key, key);
    }

    /**
     * Generates an localized String with arguments.<p>
     *
     * The localized string is a pattern to be processed by
     * {@link MessageFormat}.
     *
     * @deprecated by Linus Tolke as of 0.17.2.
     *             Use {@link #messageFormat(String key, Object[] args)}.
     * @param bundle a binding to a bundle of i18n resources
     * @param key the key to localize
     * @param args the args as Objects, inserted in the localized String
     * @return the localized String with inserted arguments
     */
    public static String messageFormat(String bundle, 
				       String key, Object[] args)
    {
    	MessageFormat msgFmt = new MessageFormat(localize(bundle, key));
	return msgFmt.format(args);
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
    public static String messageFormat(String key, Object[] args)
    {
        return new MessageFormat(localize(key)).format(args);
    }
}
