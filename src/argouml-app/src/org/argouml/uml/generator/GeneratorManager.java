// $Id$
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoGeneratorEvent;
import org.argouml.model.Model;
import org.argouml.uml.reveng.ImportInterface;

/**
 * Keeps an instance of each CodeGenerator implementation module registered,
 * associated with a language name. Also remembers the currently selected
 * language. GeneratorManager is a singleton.
 */
public final class GeneratorManager {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(GeneratorManager.class);

    /**
     * The instance.
     */
    private static final GeneratorManager INSTANCE =
        new GeneratorManager();

    /**
     * @return The singleton instance of the generator manager.
     */
    public static GeneratorManager getInstance() {
        return INSTANCE;
    }

    private Map<Language, CodeGenerator> generators = 
        new HashMap<Language, CodeGenerator>();

    private Language currLanguage = null;

    /**
     * The constructor.
     */
    private GeneratorManager() {
        // private constructor to enforce singleton
    }

    /**
     * Registers a new generator. If a generator with the same language is
     * already registered, it's replaced by the new one.
     *
     * @param lang The language.
     * @param gen The CodeGenerator object to register.
     */
    public void addGenerator(Language lang, CodeGenerator gen) {
        if (currLanguage == null) {
            currLanguage = lang;
        }
        generators.put(lang, gen);
        ArgoEventPump.fireEvent(
                new ArgoGeneratorEvent(ArgoEventTypes.GENERATOR_ADDED, gen));
        LOG.debug("Added generator " + gen + " for " + lang);
    }

    /**
     * Removes a generator. If no generator with that name is registered,
     * nothing is done.
     *
     * @param lang The language. Shall not be null.
     * @return The old generator being removed or null.
     */
    public CodeGenerator removeGenerator(Language lang) {
        CodeGenerator old = generators.remove(lang);
        if (lang.equals(currLanguage)) {
            Iterator it = generators.keySet().iterator();
            if (it.hasNext()) {
                currLanguage = (Language) it.next();
            } else {
                currLanguage = null;
            }
        }
        if (old != null) {
            ArgoEventPump.fireEvent(
                    new ArgoGeneratorEvent(
                            ArgoEventTypes.GENERATOR_REMOVED, old));
        }
        LOG.debug("Removed generator " + old + " for " + lang);
        return old;
    }

    /**
     * Removes the generator associated with the specified language.
     * @param name The language name.
     * @return The old generator, or null if there wasn't any.
     */
    public CodeGenerator removeGenerator(String name) {
        Language lang = findLanguage(name);
        if (lang != null) {
            return removeGenerator(lang);
        }
        return null;
    }

    /**
     * Access method that finds the correct generator based on a name.
     *
     * @param lang The language.
     * @return a CodeGenerator (or <code>null</code> if not found).
     */
    public CodeGenerator getGenerator(Language lang) {
        if (lang == null) {
            return null;
        }
        return generators.get(lang);
    }

    /**
     * @param name The name of the language,
     * @return a CodeGenerator (or <code>null</code> if not found).
     */
    public CodeGenerator getGenerator(String name) {
        Language lang = findLanguage(name);
        return getGenerator(lang);
    }

    /**
     * @return the current language name, or <code>null</code> if there are no
     * generator registered at all.
     */
    public Language getCurrLanguage() {
        return currLanguage;
    }

    /**
     * @return the current generator, or <code>null</code> if there are no
     * generator registered at all.
     */
    public CodeGenerator getCurrGenerator() {
        return currLanguage == null ? null : getGenerator(currLanguage);
    }

    /**
     * @return A copy of the map of the generators. The map
     * keys Language objects, and values are CodeGenerator objects.
     */
    public Map<Language, CodeGenerator> getGenerators() {
        Object  clone = ((HashMap<Language, CodeGenerator>) generators).clone();
        return (Map<Language, CodeGenerator>) clone;
    }

    /**
     * @return A copy of the Set of the languages.
     */
    public Set<Language> getLanguages() {
        return generators.keySet();
    }

    /**
     * Find a language by name from the available ones.
     * @param name The name of the language
     * @return The language with the specified name, or null if it
     * doesn't exist.
     */
    public Language findLanguage(String name) {
        for (Language lang : getLanguages()) {
            if (lang.getName().equals(name)) {
                return lang;
            }
        }
        return null;
    }

    // some convenience methods

    /**
     * Gets the path of the code base for a model element.<p>
     * If empty or not existing return <code>null</code>.
     *
     * @param me The model element
     * @return String representation of SOURCE_PATH_TAG tagged value.
     */
    public static String getCodePath(Object me) {
        if (me == null) {
            return null;
        }

        Object taggedValue = Model.getFacade().getTaggedValue(me,
                ImportInterface.SOURCE_PATH_TAG);
        String s;
        if (taggedValue == null) {
            return null;
        }
        s =  Model.getFacade().getValueOfTag(taggedValue);
        if (s != null) {
            return s.trim();
        }
        return null;
    }

}
