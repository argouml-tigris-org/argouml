// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
import java.util.Locale;

import org.tigris.gef.util.Localizer;

/**
 * The API class to the localization. All localization calls goes through
 * this class.
 *
 * @author Jean-Hugues de Raigniac
 */
public final class Translator {
    /**
     * This class should only be used in a static constant so make
     * the constructor private. See issue 3111.
     */
    private Translator() {
    }

    /**
     * Default Locale is set and resources Bundles are loaded.
     */
    public static void init () {

        Locale.setDefault(new Locale(System.getProperty("user.language", "en"),
				     System.getProperty("user.country", "")));

        /* bundle default Locale, different from user default Locale */
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
     * Helper for those that don't want to give the bundle.<p>
     *
     * <em>Note:</em> This one argument <code>key</code>
     * doesn't seem to work for tags that aren't prefixed with the
     * property file name. We get a NullPointerException.
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
     * @param key the key to localize
     * @param args the args as Objects, inserted in the localized String
     * @return the localized String with inserted arguments
     */
    public static String messageFormat(String key, Object[] args) {
        return new MessageFormat(localize(key)).format(args);
    }
}
