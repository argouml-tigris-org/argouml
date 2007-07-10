// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.uml.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Icon;

/**
 * Provides some utility methods for code generation.
 *
 * @author Daniele Tamino
 */
public final class GeneratorHelper {
    /**
     * The constructor.
     */
    private GeneratorHelper() {
    }

    /**
     * Generate code for one or more elements in a given language.
     * @param lang The language to use.
     * @param elements The elements to generate code for.
     * @param deps Whether to generate dependency too.
     * @return A collection of SourceUnit objects. The collection may be empty
     * if no file is generated.
     */
    public static Collection generate(
            Language lang, Collection elements, boolean deps) {
        CodeGenerator gen =
            GeneratorManager.getInstance().getGenerator(lang);
        if (gen != null) {
            return gen.generate(elements, deps);
        }
        return new ArrayList(); // empty list
    }

    /**
     * Generate code for a single element.
     * @param lang The language to use.
     * @param elem The element to generate code for.
     * @param deps Whether to generate dependency too.
     * @return A collection of SourceUnit objects. The collection may be empty
     * if no file is generated.
     */
    public static Collection generate(
            Language lang, Object elem, boolean deps) {
        List list = new ArrayList();
        list.add(elem);
        return generate(lang, list, deps);
    }

    /**
     * Creates a new Language only if one with the same name doesn't already
     * exist in GeneratorManager, in which case that one is returned.
     * @param theName The name of the language.
     * @param theTitle A string representing the language for display.
     * @param theIcon An icon for the language.
     * @return The Language object found or created.
     */
    public static Language makeLanguage(String theName, String theTitle,
            Icon theIcon) {
        Language lang;
        lang = GeneratorManager.getInstance().findLanguage(theName);
        if (lang == null) {
            lang = new Language(theName, theTitle, theIcon);
        }
        return lang;
    }

    /**
     * Creates a language with no icon.
     * @see #makeLanguage(String, String, Icon)
     * @param theName The name of the language.
     * @param theTitle A string representing the language for display.
     * @return The Language object found or created.
     */
    public static Language makeLanguage(String theName, String theTitle) {
        return makeLanguage(theName, theTitle, null);
    }

    /**
     * Creates a language with title equal to the name.
     * @see #makeLanguage(String, String, Icon)
     * @param theName The name of the language.
     * @param theIcon An icon for the language.
     * @return The Language object found or created.
     */
    public static Language makeLanguage(String theName, Icon theIcon) {
        return makeLanguage(theName, theName, theIcon);
    }

    /**
     * Creates a language with title equal to the name and no icon.
     * @see #makeLanguage(String, String, Icon)
     * @param theName The name of the language.
     * @return The Language object found or created.
     */
    public static Language makeLanguage(String theName) {
        return makeLanguage(theName, theName, null);
    }

}
