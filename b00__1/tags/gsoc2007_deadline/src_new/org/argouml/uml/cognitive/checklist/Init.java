// $Id:Init.java 12950 2007-07-01 08:10:04Z tfmorris $
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

package org.argouml.uml.cognitive.checklist;

import org.argouml.cognitive.checklist.CheckItem;
import org.argouml.cognitive.checklist.CheckManager;
import org.argouml.cognitive.checklist.Checklist;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


/**
 * Registers Checklists for different kinds of model elements. If you
 * add a new checklist, a line must be added here.
 *
 * @see org.argouml.cognitive.checklist.CheckManager
 */
public class Init {
    /**
     * static initializer, register all appropriate critics.
     */
    public static void init() {
	createChecklists();
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
	CheckItem checkitem =
	    new UMLCheckItem(category, Translator.localize(key));
	checklist.add(checkitem);
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
	newCheckItem(cat, "checklist.class.location.eliminate-from-model", cl);
	newCheckItem(cat,
	        "checklist.class.location.eliminates-or-affects-something-else",
		cl);

	cat = Translator.localize("checklist.class.updates");
        newCheckItem(cat, "checklist.class.updates.reasons-for-update", cl);
	newCheckItem(cat, "checklist.class.updates.affects-something-else", cl);

	CheckManager.register(Model.getMetaTypes().getUMLClass(), cl);


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

	CheckManager.register(Model.getMetaTypes().getAttribute(), cl);


	// Operation
	cl = new Checklist();

	cat = Translator.localize("checklist.operation.naming");
	newCheckItem(cat, "checklist.operation.naming.describe-clearly", cl);
	newCheckItem(cat, "checklist.operation.naming.is-verb", cl);
	newCheckItem(cat, "checklist.operation.naming.misinterpret", cl);
	newCheckItem(cat, "checklist.operation.naming.do-just-one-thing", cl);

	cat = Translator.localize("checklist.operation.encoding");
	newCheckItem(cat,
	    "checklist.operation.encoding.is-returntype-too-restrictive",
	    cl);
	newCheckItem(cat,
	    "checklist.operation.encoding.does-returntype-allow-impossible-"
            + "values", cl);
	newCheckItem(cat,
            "checklist.operation.encoding.combine-with-other", cl);
	newCheckItem(cat, "checklist.operation.encoding.break-into-parts", cl);
	newCheckItem(cat, "checklist.operation.encoding.break-into-series", cl);
	newCheckItem(cat, "checklist.operation.encoding.reduce-number-of-calls",
		     cl);

	cat = Translator.localize("checklist.operation.value");
	newCheckItem(cat, "checklist.operation.value.handle-all-inputs", cl);
	newCheckItem(cat, "checklist.operation.value.are-special-cases", cl);
	newCheckItem(cat, "checklist.operation.value.is-correctness-checkable",
		     cl);
	newCheckItem(cat,
		     "checklist.operation.value.express-preconditions-possible",
		     cl);
	newCheckItem(cat,
	    "checklist.operation.value.express-postconditions-possible",
	    cl);
	newCheckItem(cat,
	    "checklist.operation.value.how-behave-preconditions-violated",
	    cl);
	newCheckItem(cat,
	    "checklist.operation.value.how-behave-postconditions-not-achieved",
	    cl);

	cat = Translator.localize("checklist.operation.location");
	newCheckItem(cat, "checklist.operation.location.move-somewhere", cl);
	newCheckItem(cat, "checklist.operation.location.move-up-hierarchy", cl);
	newCheckItem(cat, "checklist.operation.location.include-all", cl);
	newCheckItem(cat, "checklist.operation.location.could-be-eliminated",
		     cl);
	newCheckItem(cat,
	    "checklist.operation.location.eliminates-or-affects-something-else",
	    cl);

	CheckManager.register(Model.getMetaTypes().getOperation(), cl);



	// Association
	cl = new Checklist();

	cat = Translator.localize("checklist.association.naming");
	newCheckItem(cat, "checklist.association.naming.describe-clearly", cl);
	newCheckItem(cat, "checklist.association.naming.is-noun", cl);
	newCheckItem(cat, "checklist.association.naming.misinterpret", cl);

	cat = Translator.localize("checklist.association.encoding");
	newCheckItem(cat, "checklist.association.encoding.convert-to-attribute",
		     cl);
	newCheckItem(cat, "checklist.association.encoding.do-just-one-thing",
		     cl);
	newCheckItem(cat, "checklist.association.encoding.break-into-parts",
		     cl);

	cat = Translator.localize("checklist.association.value");
	newCheckItem(cat,
		     "checklist.association.value.start-with-meaningful-values",
		     cl);
	newCheckItem(cat, "checklist.association.value.convert-to-invariant",
		     cl);
	newCheckItem(cat,
	    "checklist.association.value.establish-invariant-in-constructors",
	    cl);
	newCheckItem(cat, "checklist.association.value.maintain-invariant", cl);

	cat = Translator.localize("checklist.association.location");
	newCheckItem(cat, "checklist.association.location.move-somewhere", cl);
	newCheckItem(cat, "checklist.association.location.planned-subclasses",
		     cl);
	newCheckItem(cat, "checklist.association.location.eliminate-from-model",
		     cl);
	newCheckItem(cat,
	    "checklist.association.location.eliminates-or-affects-"
            + "something-else", cl);

	cat = Translator.localize("checklist.association.updates");
	newCheckItem(cat, "checklist.association.updates.reasons-for-update",
		     cl);
	newCheckItem(cat,
            "checklist.association.updates.affects-something-else",
	    cl);

	CheckManager.register(Model.getMetaTypes().getAssociation(), cl);


	// Interface
	cl = new Checklist();

	cat = Translator.localize("checklist.interface.naming");
	newCheckItem(cat, "checklist.interface.naming.describe-clearly", cl);
	newCheckItem(cat, "checklist.interface.naming.is-noun", cl);
	newCheckItem(cat, "checklist.interface.naming.misinterpret", cl);

	cat = Translator.localize("checklist.interface.encoding");
	newCheckItem(cat, "checklist.interface.encoding.convert-to-attribute",
		     cl);
	newCheckItem(cat, "checklist.interface.encoding.do-just-one-thing", cl);
	newCheckItem(cat, "checklist.interface.encoding.break-into-parts", cl);

	cat = Translator.localize("checklist.interface.value");
	newCheckItem(cat,
		     "checklist.interface.value.start-with-meaningful-values",
		     cl);
	newCheckItem(cat, "checklist.interface.value.convert-to-invariant", cl);
	newCheckItem(cat,
	    "checklist.interface.value.establish-invariant-in-constructors",
	    cl);
	newCheckItem(cat, "checklist.interface.value.maintain-invariant", cl);

	cat = Translator.localize("checklist.interface.location");
	newCheckItem(cat, "checklist.interface.location.move-somewhere", cl);
	newCheckItem(cat, "checklist.interface.location.planned-subclasses",
		     cl);
	newCheckItem(cat, "checklist.interface.location.eliminate-from-model",
		     cl);
	newCheckItem(cat,
	    "checklist.interface.location.eliminates-or-affects-something-else",
	    cl);

	cat = Translator.localize("checklist.interface.updates");
	newCheckItem(cat, "checklist.interface.updates.reasons-for-update", cl);
	newCheckItem(cat, "checklist.interface.updates.affects-something-else",
		     cl);

	CheckManager.register(Model.getMetaTypes().getInterface(), cl);


	// Instance
	cl = new Checklist();

	cat = Translator.localize("checklist.instance.general");
	newCheckItem(cat, "checklist.instance.general.describe-clearly", cl);

	cat = Translator.localize("checklist.instance.naming");
	newCheckItem(cat, "checklist.instance.naming.describe-clearly", cl);
	newCheckItem(cat, "checklist.instance.naming.denotes-state", cl);
	newCheckItem(cat, "checklist.instance.naming.misinterpret", cl);

	cat = Translator.localize("checklist.instance.structure");
	newCheckItem(cat, "checklist.instance.structure.merged-with-other", cl);
	newCheckItem(cat, "checklist.instance.structure.do-just-one-thing", cl);
	newCheckItem(cat, "checklist.instance.structure.break-into-parts", cl);
	newCheckItem(cat,
	    "checklist.instance.structure.can-write-characteristic-equation",
	    cl);
	newCheckItem(cat, "checklist.instance.structure.belong", cl);
	newCheckItem(cat, "checklist.instance.structure.make-internal", cl);
	newCheckItem(cat,
	    "checklist.instance.structure.is-state-in-another-machine-"
            + "exclusive", cl);

	cat = Translator.localize("checklist.instance.actions");
	newCheckItem(cat, "checklist.instance.actions.list-entry-actions", cl);
	newCheckItem(cat,
		     "checklist.instance.actions.update-attribute-on-entry",
		     cl);
	newCheckItem(cat, "checklist.instance.actions.list-exit-action", cl);
	newCheckItem(cat, "checklist.instance.actions.update-attribute-on-exit",
		     cl);
	newCheckItem(cat, "checklist.instance.actions.list-do-action", cl);
	newCheckItem(cat, "checklist.instance.actions.maintained-state", cl);

	cat = Translator.localize("checklist.instance.transitions");
	newCheckItem(cat,
	    "checklist.instance.transitions.need-another-transition-into",
	    cl);
	newCheckItem(cat,
	    "checklist.instance.transitions.use-all-transitions-into",
	    cl);
	newCheckItem(cat,
	    "checklist.instance.transitions.combine-with-other-incoming",
	    cl);
	newCheckItem(cat,
	    "checklist.instance.transitions.need-another-transition-out-of",
	    cl);
	newCheckItem(cat,
	    "checklist.instance.transitions.use-all-transitions-out-of",
	    cl);
	newCheckItem(cat,
	    "checklist.instance.transitions.are-transitions-out-of-exclusive",
	    cl);
	newCheckItem(cat,
	    "checklist.instance.transitions.combine-with-other-outgoing",
	    cl);

	CheckManager.register(Model.getMetaTypes().getInstance(), cl);


	// Link
	cl = new Checklist();

	cat = Translator.localize("checklist.link.naming");
	newCheckItem(cat, "checklist.link.naming.describe-clearly", cl);
	newCheckItem(cat, "checklist.link.naming.is-noun", cl);
	newCheckItem(cat, "checklist.link.naming.misinterpret", cl);

	cat = Translator.localize("checklist.link.encoding");
	newCheckItem(cat, "checklist.link.encoding.convert-to-attribute", cl);
	newCheckItem(cat, "checklist.link.encoding.do-just-one-thing", cl);
	newCheckItem(cat, "checklist.link.encoding.break-into-parts", cl);

	cat = Translator.localize("checklist.link.value");
	newCheckItem(cat, "checklist.link.value.start-with-meaningful-values",
		     cl);
	newCheckItem(cat, "checklist.link.value.convert-to-invariant", cl);
	newCheckItem(cat,
		     "checklist.link.value.establish-invariant-in-constructors",
		     cl);
	newCheckItem(cat, "checklist.link.value.maintain-invariant", cl);

	cat = Translator.localize("checklist.link.location");
	newCheckItem(cat, "checklist.link.location.move-somewhere", cl);
	newCheckItem(cat, "checklist.link.location.planned-subclasses", cl);
	newCheckItem(cat, "checklist.link.location.eliminate-from-model", cl);
	newCheckItem(cat,
	    "checklist.link.location.eliminates-or-affects-something-else",
	    cl);

	cat = Translator.localize("checklist.link.updates");
	newCheckItem(cat, "checklist.link.updates.reasons-for-update", cl);
	newCheckItem(cat, "checklist.link.updates.affects-something-else", cl);

	CheckManager.register(Model.getMetaTypes().getLink(), cl);


	// State
	cl = new Checklist();

	cat = Translator.localize("checklist.state.naming");
	newCheckItem(cat, "checklist.state.naming.describe-clearly", cl);
	newCheckItem(cat, "checklist.state.naming.denotes-state", cl);
	newCheckItem(cat, "checklist.state.naming.misinterpret", cl);

	cat = Translator.localize("checklist.state.structure");
	newCheckItem(cat, "checklist.state.structure.merged-with-other", cl);
	newCheckItem(cat, "checklist.state.structure.do-just-one-thing", cl);
	newCheckItem(cat, "checklist.state.structure.break-into-parts", cl);
	newCheckItem(cat,
	    "checklist.state.structure.can-write-characteristic-equation",
	    cl);
	newCheckItem(cat, "checklist.state.structure.belong", cl);
	newCheckItem(cat, "checklist.state.structure.make-internal", cl);
	newCheckItem(cat,
	    "checklist.state.structure.is-state-in-another-machine-exclusive",
	    cl);

	cat = Translator.localize("checklist.state.actions");
	newCheckItem(cat, "checklist.state.actions.list-entry-actions", cl);
	newCheckItem(cat, "checklist.state.actions.update-attribute-on-entry",
		     cl);
	newCheckItem(cat, "checklist.state.actions.list-exit-action", cl);
	newCheckItem(cat, "checklist.state.actions.update-attribute-on-exit",
		     cl);
	newCheckItem(cat, "checklist.state.actions.list-do-action", cl);
	newCheckItem(cat, "checklist.state.actions.maintained-state", cl);

	cat = Translator.localize("checklist.state.transitions");
	newCheckItem(cat,
	    "checklist.state.transitions.need-another-transition-into",
	    cl);
	newCheckItem(cat,
	    "checklist.state.transitions.use-all-transitions-into",
	    cl);
	newCheckItem(cat,
	    "checklist.state.transitions.combine-with-other-incoming",
	    cl);
	newCheckItem(cat,
	    "checklist.state.transitions.need-another-transition-out-of",
	    cl);
	newCheckItem(cat,
	    "checklist.state.transitions.use-all-transitions-out-of",
	    cl);
	newCheckItem(cat,
	    "checklist.state.transitions.are-transitions-out-of-exclusive",
	    cl);
	newCheckItem(cat,
	    "checklist.state.transitions.combine-with-other-outgoing",
	    cl);

	CheckManager.register(Model.getMetaTypes().getState(), cl);


	// Transition
	cl = new Checklist();

	cat = Translator.localize("checklist.transition.structure");
	newCheckItem(cat, "checklist.transition.structure.start-somewhere-else",
		     cl);
	newCheckItem(cat, "checklist.transition.structure.end-somewhere-else",
		     cl);
	newCheckItem(cat,
		     "checklist.transition.structure.need-another-like-this",
		     cl);
	newCheckItem(cat,
		     "checklist.transition.structure.unneeded-because-of-this",
		     cl);

	cat = Translator.localize("checklist.transition.trigger");
	newCheckItem(cat, "checklist.transition.trigger.needed", cl);
	newCheckItem(cat, "checklist.transition.trigger.happen-too-often", cl);
	newCheckItem(cat, "checklist.transition.trigger.happen-too-rarely", cl);

	cat = Translator.localize("checklist.transition.guard");
	newCheckItem(cat, "checklist.transition.guard.taken-too-often", cl);
	newCheckItem(cat, "checklist.transition.guard.is-too-restrictive", cl);
	newCheckItem(cat, "checklist.transition.guard.break-into-parts", cl);

	cat = Translator.localize("checklist.transition.actions");
	newCheckItem(cat, "checklist.transition.actions.should-have", cl);
	newCheckItem(cat, "checklist.transition.actions.should-have-exit", cl);
	newCheckItem(cat, "checklist.transition.actions.should-have-entry", cl);
	newCheckItem(cat, "checklist.transition.actions.is-precondition-met",
		     cl);
	newCheckItem(cat,
	    "checklist.transition.actions.is-postcondition-consistant-with-"
            + "destination", cl);

	CheckManager.register(Model.getMetaTypes().getTransition(), cl);


	// Use Case
	cl = new Checklist();

	cat = Translator.localize("checklist.usecase.naming");
	newCheckItem(cat, "checklist.usecase.naming.describe-clearly", cl);
	newCheckItem(cat, "checklist.usecase.naming.is-noun", cl);
	newCheckItem(cat, "checklist.usecase.naming.misinterpret", cl);

	cat = Translator.localize("checklist.usecase.encoding");
	newCheckItem(cat, "checklist.usecase.encoding.convert-to-attribute",
		     cl);
	newCheckItem(cat, "checklist.usecase.encoding.do-just-one-thing", cl);
	newCheckItem(cat, "checklist.usecase.encoding.break-into-parts", cl);

	cat = Translator.localize("checklist.usecase.value");
	newCheckItem(cat,
		     "checklist.usecase.value.start-with-meaningful-values",
		     cl);
	newCheckItem(cat, "checklist.usecase.value.convert-to-invariant", cl);
	newCheckItem(cat,
	    "checklist.usecase.value.establish-invariant-in-constructors",
	    cl);
	newCheckItem(cat, "checklist.usecase.value.maintain-invariant", cl);

	cat = Translator.localize("checklist.usecase.location");
	newCheckItem(cat, "checklist.usecase.location.move-somewhere", cl);
	newCheckItem(cat, "checklist.usecase.location.planned-subclasses", cl);
	newCheckItem(cat, "checklist.usecase.location.eliminate-from-model",
		     cl);
	newCheckItem(cat,
	    "checklist.usecase.location.eliminates-or-affects-something-else",
	    cl);

	cat = Translator.localize("checklist.usecase.updates");
	newCheckItem(cat, "checklist.usecase.updates.reasons-for-update", cl);
	newCheckItem(cat, "checklist.usecase.updates.affects-something-else",
		     cl);

	CheckManager.register(Model.getMetaTypes().getUseCase(), cl);


	// Actor
	cl = new Checklist();

	cat = Translator.localize("checklist.actor.naming");
	newCheckItem(cat, "checklist.actor.naming.describe-clearly", cl);
	newCheckItem(cat, "checklist.actor.naming.is-noun", cl);
	newCheckItem(cat, "checklist.actor.naming.misinterpret", cl);

	cat = Translator.localize("checklist.actor.encoding");
	newCheckItem(cat, "checklist.actor.encoding.convert-to-attribute", cl);
	newCheckItem(cat, "checklist.actor.encoding.do-just-one-thing", cl);
	newCheckItem(cat, "checklist.actor.encoding.break-into-parts", cl);

	cat = Translator.localize("checklist.actor.value");
	newCheckItem(cat, "checklist.actor.value.start-with-meaningful-values",
		     cl);
	newCheckItem(cat, "checklist.actor.value.convert-to-invariant", cl);
	newCheckItem(cat,
	    "checklist.actor.value.establish-invariant-in-constructors",
	    cl);
	newCheckItem(cat, "checklist.actor.value.maintain-invariant", cl);

	cat = Translator.localize("checklist.actor.location");
	newCheckItem(cat, "checklist.actor.location.move-somewhere", cl);
	newCheckItem(cat, "checklist.actor.location.planned-subclasses", cl);
	newCheckItem(cat, "checklist.actor.location.eliminate-from-model", cl);
	newCheckItem(cat,
            "checklist.actor.location.eliminates-or-affects-something-else",
	    cl);

	cat = Translator.localize("checklist.actor.updates");
	newCheckItem(cat, "checklist.actor.updates.reasons-for-update", cl);
	newCheckItem(cat, "checklist.actor.updates.affects-something-else", cl);

	CheckManager.register(Model.getMetaTypes().getActor(), cl);

    }

}
