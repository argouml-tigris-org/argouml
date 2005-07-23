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

package org.argouml.uml.ui.behavior.use_cases;

import javax.swing.JComboBox;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionDeleteSingleModelElement;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;

/**
 * Builds the property panel for an Include relationship.<p>
 *
 * This is a type of Relationship, but, since Relationship has no
 * semantic meaning of its own, we derive directly from
 * PropPanelModelElement (as other children of Relationship do).<p>
 *
 * @author Jeremy Bennett
 */
public class PropPanelInclude extends PropPanelModelElement {

    /**
     * Constructor. Builds up the various fields required.
     */
    public PropPanelInclude() {
        super("Include", 
                lookupIcon("Include"), 
                ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
		 getNameTextField());
        addField(Translator.localize("label.stereotype"),
                getStereotypeSelector());
        addField(Translator.localize("label.namespace"),
		 getNamespaceScroll());

        addSeperator();

        JComboBox baseBox =
	    new UMLComboBox2(new UMLIncludeBaseComboBoxModel(),
			     ActionSetIncludeBase.getInstance());
        addField(Translator.localize("label.usecase-base"),
		 baseBox);

        JComboBox additionBox =
	    new UMLComboBox2(new UMLIncludeAdditionComboBoxModel(),
			     ActionSetIncludeAddition.getInstance());
        addField(Translator.localize("label.addition"),
		 additionBox);

        // Add the toolbar buttons:
        addButton(new PropPanelButton2(new ActionNavigateNamespace()));
        addButton(new PropPanelButton2(new ActionNewStereotype(),
                lookupIcon("Stereotype")));
        addButton(new PropPanelButton2(new ActionDeleteSingleModelElement(),
                lookupIcon("Delete")));
    }


    /**
     * Get the current base use case of the include relationship.<p>
     *
     * <em>Note</em>. There is a bug in NSUML, where the "include" and
     * "include2" associations of a use case are back to front, i.e
     * "include" is used as the opposite end of "addition" to point to
     * an including use case, rather than an included use case.  Fixed
     * within the include relationship, rather than the use case, by
     * reversing the use of access functions for the "base" and
     * "addition" associations in the code.<p>
     *
     * @return The UseCase that is the base of this include relationship or
     * <code>null</code> if there is none.
     */
    public Object getBase() {
        Object base   = null;
        Object      target = getTarget();

        // Note that because of the NSUML bug, we must use getAddition() rather
        // than getBase() to get the base use case.

        if (Model.getFacade().isAInclude(target)) {
            base = Model.getFacade().getAddition(target);
        }
        return base;
    }

    /**
     * Set the base use case of the include relationship.<p>
     *
     * <em>Note</em>. There is a bug in NSUML, where the "include" and
     * "include2" associations of a use case are back to front, i.e
     * "include" is used as the opposite end of "addition" to point to
     * an including use case, rather than an included use case.  Fixed
     * within the include relationship, rather than the use case, by
     * reversing the use of access functions for the "base" and
     * "addition" associations in the code.<p>
     *
     * @param base The UseCase to set as the base of this include relationship.
     */
    public void setBase(Object/*MUseCase*/ base) {
        Object target = getTarget();

        // Note that because of the NSUML bug, we must use setAddition() rather
        // than setBase() to set the base use case.

        if (Model.getFacade().isAInclude(target)) {
            Model.getUseCasesHelper().setAddition(target, base);
        }
    }


    /**
     * Get the current addition use case of the include relationship.<p>
     *
     *
     * @return The UseCase that is the addition of this include
     * relationship or <code>null</code> if there is none.
     */
    public Object getAddition() {
        Object addition   = null;
        Object target = getTarget();

        if (Model.getFacade().isAInclude(target)) {
            addition = Model.getFacade().getAddition(target);
        }

        return addition;
    }

    /**
     * Set the addition use case of the include relationship.<p>
     *
     *
     * @param addition The UseCase to set as the addition of this
     * include relationship.
     */
    public void setAddition(Object/*MUseCase*/ addition) {
        Object target = getTarget();

        // Note that because of the NSUML bug, we must use setBase() rather
        // than setAddition() to set the addition use case.

        if (Model.getFacade().isAInclude(target)) {
            Model.getUseCasesHelper().setAddition(target, addition);
        }
    }


    /**
     * Predicate to test if a model element may appear in the list of
     * potential use cases.<p>
     *
     * <em>Note</em>. We don't try to prevent the user setting up
     * circular include relationships. This may be necessary
     * temporarily, for example while reversing a relationship. It is
     * up to a critic to track this.<p>
     *
     * @param modElem the {@link
     * ru.novosoft.uml.foundation.core.MModelElement} to test.
     *
     * @return <code>true</code> if modElem is a use case,
     * <code>false</code> otherwise.
     */
    public boolean isAcceptableUseCase(Object/*MModelElement*/ modElem) {

        return Model.getFacade().isAUseCase(modElem);
    }


} /* end class PropPanelInclude */
