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

package org.argouml.uml.cognitive.checklist;

import org.apache.log4j.Logger;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.argouml.cognitive.checklist.CheckManager;
import org.argouml.cognitive.checklist.Checklist;
import org.argouml.cognitive.checklist.CheckItem;
import org.argouml.model.ModelFacade;
import org.argouml.i18n.Translator;


/**
 * Registers Checklists for different kinds of model elements. If you
 * add a new checklist, a line must be added here.
 *
 * @see org.argouml.cognitive.checklist.CheckManager
 */
public class Init {

    /**
     * @deprecated as of 0.15.3. Visibility changed to private.
     */
    protected static Logger cat =
	Logger.getLogger(Init.class);

    private static Logger _Log =
	Logger.getLogger(Init.class);

    /** 
     * static initializer, register all appropriate critics<p>
     *
     * When the checklists are converted to the new format, this function
     * no longer needs Locale as argument.
     */
    public static void init(Locale locale) {
	createChecklists();

	ResourceBundle bundle =
	    ResourceBundle
	    .getBundle("org.argouml.i18n.UMLCognitiveResourceBundle", locale);

	// addChecklist(bundle, (Class)ModelFacade.CLASS, "ChClass");
	addChecklist(bundle, (Class)ModelFacade.INTERFACE, "ChInterface");
	// addChecklist(bundle, (Class)ModelFacade.ATTRIBUTE, "ChAttribute");
	addChecklist(bundle, (Class)ModelFacade.OPERATION, "ChOperation");
	addChecklist(bundle, (Class)ModelFacade.ASSOCIATION, "ChAssociation");
	addChecklist(bundle, (Class)ModelFacade.ASSOCIATION_CLASS, "ChAssociation");
	addChecklist(bundle, (Class)ModelFacade.STATE, "ChState");
	addChecklist(bundle, (Class)ModelFacade.TRANSITION, "ChTransition");
	addChecklist(bundle, (Class)ModelFacade.USE_CASE, "ChUseCase");
	addChecklist(bundle, (Class)ModelFacade.ACTOR, "ChActor");
    }

    /**
     * @deprecated will not be used when all check lists are moved to the
     * new format.
     */
    private static void addChecklist(ResourceBundle bundle,
				     Class cls, String key) {
        try {
            UMLChecklist checklist =
		new UMLChecklist((String[][]) bundle.getObject(key));
            CheckManager.register(cls, checklist);
        }
        catch (MissingResourceException e) {
            _Log.error(e);
            e.printStackTrace();
        }
    }


    /**
     * Creat a check item (an UMLCheckItem) corresponding to the given key.
     * and add it to the given checklist.
     *
     * @param category to add the item to.
     * @param key to get the localized string.
     * @param checklist to add the item to.
     */
    private static void newCheckItem(String category, String key, 
				     Checklist checklist) {
	CheckItem checkitem = new UMLCheckItem(category, 
					       Translator.localize(key));
	checklist.addItem(checkitem);
    }

    /**
     * Create all check lists.<p>
     *
     * This is the new implementation and a change from the old implementation
     * in that the source code (i.e. this function) contains knowledge on
     * what to check.<p>
     *
     * The purpose of this is to allow the ArgoUML project to jointly develop
     * the knowledge database and let the internationalizations be just 
     * internationalizations.<p>
     *
     * When adding a new check list or an item to an existing check list you
     * will have to:<ol>
     * <li>Add the lines in this function.
     * <li>Add the tags for the new items to the i18n property file 
     *     (checklist.properties).
     * <li>Notify the localization teams.
     * </ol>
     */
    private static void createChecklists() {
	//
	// This function will, by design, contain long lists of i18n keys.
	// It might be tempting to shorten these by constructing them from
	// the parts but please then consider that we might loose the
	// possibility to build tools to statically check the property files 
	// against this file.
	//
	Checklist cl;
	String cat;

	// Class
	cl = new Checklist();

	cat = Translator.localize("checklist.class.naming");
	newCheckItem(cat, "checklist.class.naming.describe-clearly", cl);
	newCheckItem(cat, "checklist.class.naming.is-noun", cl);
	newCheckItem(cat, "checklist.class.naming.misinterpret", cl);

	cat = Translator.localize("checklist.class.encoding");
	newCheckItem(cat, "checklist.class.encoding.convert-to-attribute", cl);
	newCheckItem(cat, "checklist.class.encoding.do-just-one-thing", cl);
	newCheckItem(cat, "checklist.class.encoding.break-into-parts", cl);

	cat = Translator.localize("checklist.class.value");
	newCheckItem(cat, "checklist.class.value.start-with-meaningful-values",
		     cl);
	newCheckItem(cat, "checklist.class.value.convert-to-invariant", cl);
	newCheckItem(cat,
		     "checklist.class.value.establish-invariant-in-constructors",
		     cl);
	newCheckItem(cat, "checklist.class.value.maintain-invariant", cl);

	cat = Translator.localize("checklist.class.location");
	newCheckItem(cat, "checklist.class.location.move-somewhere", cl);
	newCheckItem(cat, "checklist.class.location.planned-subclasses", cl);
	newCheckItem(cat, "checklist.class.location.eliminate-class", cl);
	newCheckItem(cat,
		     "checklist.class.location.eliminates-or-affects-something-else",
		     cl);

	cat = Translator.localize("checklist.class.updates");
        newCheckItem(cat, "checklist.class.updates.reasons-for-update", cl);
	newCheckItem(cat, "checklist.class.updates.affects-something-else", cl);

	CheckManager.register((Class) ModelFacade.CLASS, cl);


	// Attribute
	cl = new Checklist();

	cat = Translator.localize("checklist.attribute.naming");
	newCheckItem(cat, "checklist.attribute.naming.describe-clearly", cl);
	newCheckItem(cat, "checklist.attribute.naming.is-noun", cl);
	newCheckItem(cat, "checklist.attribute.naming.misinterpret", cl);

	cat = Translator.localize("checklist.attribute.encoding");
	newCheckItem(cat, "checklist.attribute.encoding.is-too-restrictive",
		     cl);
	newCheckItem(cat,
		     "checklist.attribute.encoding.allow-impossible-values",
		     cl);
	newCheckItem(cat, "checklist.attribute.encoding.combine-with-other",
		     cl);
	newCheckItem(cat, "checklist.attribute.encoding.break-into-parts", cl);
	newCheckItem(cat, "checklist.attribute.encoding.is-computable", cl);

	cat = Translator.localize("checklist.attribute.value");
	newCheckItem(cat, "checklist.attribute.value.default-value", cl);
	newCheckItem(cat, "checklist.attribute.value.correct-default-value",
		     cl);
	newCheckItem(cat, "checklist.attribute.value.is-correctness-checkable",
		     cl);

	cat = Translator.localize("checklist.attribute.location");
	newCheckItem(cat, "checklist.attribute.location.move-somewhere", cl);
	newCheckItem(cat, "checklist.attribute.location.move-up-hierarchy", cl);
	newCheckItem(cat, "checklist.attribute.location.include-all", cl);
	newCheckItem(cat, "checklist.attribute.location.could-be-eliminated",
		     cl);
	newCheckItem(cat,
		     "checklist.attribute.location.eliminates-or-affects-something-else",
		     cl);

	cat = Translator.localize("checklist.attribute.updates");
        newCheckItem(cat, "checklist.attribute.updates.reasons-for-update", cl);
	newCheckItem(cat, "checklist.attribute.updates.affects-something-else",
		     cl);
	newCheckItem(cat,
		     "checklist.attribute.updates.exists-method-for-update",
		     cl);
	newCheckItem(cat,
		     "checklist.attribute.updates.exists-method-for-specific-value",
		     cl);

	CheckManager.register((Class) ModelFacade.ATTRIBUTE, cl);

    }	

} /* end class Init */
