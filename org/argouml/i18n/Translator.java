// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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
 * A tool class to help localization.
 *
 * @author Jean-Hugues de Raigniac
 *
 */
public class Translator {
    /** logger */
    private static Logger cat = 
        Logger.getLogger(Translator.class);

    /** Key for menu resource bundle. */
    //public static final String MENU_BUNDLE = "CoreMenu";

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
        org.workingfrog.i18n.util.Translator.setDefaultLocale(
							      new Locale("en", ""));
        org.workingfrog.i18n.util.Translator.setBundlesPath("org.argouml.i18n");
  
        Localizer.addResource("GefBase",
			      "org.tigris.gef.base.BaseResourceBundle");
        Localizer.addResource("GefPres",
			      "org.tigris.gef.presentation.PresentationResourceBundle");
        Localizer.addResource("CoreMenu",
			      "org.argouml.i18n.MenuResourceBundle");
        Localizer.addResource("UMLMenu",
			      "org.argouml.i18n.UMLResourceBundle");
        Localizer.addResource("Cognitive",
			      "org.argouml.i18n.UMLCognitiveResourceBundle");
    }

    /**
     * For Locale selection.
     * @return Locales used in ArgoUML
     */
    public static Locale[] getLocales () {
        return org.workingfrog.i18n.util.Translator.getLocales(
							       new Translator());
    }

    /**
     * Change the current Locale.
     * @param locale the new Locale
     */
    public static void setLocale (Locale locale) {
        org.workingfrog.i18n.util.Translator.setLocale(locale);
    }

    /**   
     * Loads image bindings from a File.
     * @param file the properties file
     * @return the properties in file
     */
    private static Properties loadImageBindings (String file) {

        InputStream inputStream = null;
        Properties properties = new Properties();

        try {
            inputStream = Translator.class.getResourceAsStream(propertiesFile);
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException ex) {
            cat.fatal("Unable to load properties from file: " + file, ex);
            System.exit(1);
        }

        return properties;
    }

    /**   
     * Provide a "gef compliant" image file name.
     * @param name the new i18n key
     * @return the old i18n key
     */
    public static String getImageBinding (String name) {

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
     * the gef util library. 
     *
     * @param bundle a binding to a bundle of i18n resources
     * @param key the key to loacalize
     * @return the translation
     */
    public static String localize(String bundle, String key) {
        return org.argouml.application.api.Argo.localize(bundle, key);
    }

    /**
     * Generates an localized String with Arguments
     *
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
}
