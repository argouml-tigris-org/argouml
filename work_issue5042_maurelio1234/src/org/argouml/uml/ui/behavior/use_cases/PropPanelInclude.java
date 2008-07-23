// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
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

import javax.swing.JList;

import org.argouml.model.Model;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelRelationship;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;

/**
 * Builds the property panel for an Include relationship.<p>
 *
 * @author Jeremy Bennett
 */
public class PropPanelInclude extends PropPanelRelationship {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = -8235207258195445477L;


    /**
     * Construct a property panel for Include model elements.
     */
    public PropPanelInclude() {
        super("label.include", lookupIcon("Include"));

        addField("label.name", getNameTextField());
        addField("label.namespace", getNamespaceSelector());

        addSeparator();

        addField("label.usecase-base",
		 getSingleRowScroll(new UMLIncludeBaseListModel()));

        addField("label.addition",
		 getSingleRowScroll(new UMLIncludeAdditionListModel()));

        // Add the toolbar buttons:
        addAction(new ActionNavigateNamespace());
        addAction(new ActionNewStereotype());
        addAction(getDeleteAction());
    }

    /**
     * Get the current base use case of the include relationship.<p>
     * @return The UseCase that is the base of this include relationship or
     * <code>null</code> if there is none.
     */
    public Object getBase() {
        Object base   = null;
        Object      target = getTarget();

        if (Model.getFacade().isAInclude(target)) {
            base = Model.getFacade().getBase(target);
        }
        return base;
    }

    /**
     * Set the base use case of the include relationship.<p>
     * @param base The UseCase to set as the base of this include relationship.
     */
    public void setBase(Object/*MUseCase*/ base) {
        Object target = getTarget();

        if (Model.getFacade().isAInclude(target)) {
            Model.getUseCasesHelper().setBase(target, base);
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
     * @param modElem the ModelElement to test.
     *
     * @return <code>true</code> if modElem is a use case,
     * <code>false</code> otherwise.
     */
    public boolean isAcceptableUseCase(Object/*MModelElement*/ modElem) {

        return Model.getFacade().isAUseCase(modElem);
    }


} 
