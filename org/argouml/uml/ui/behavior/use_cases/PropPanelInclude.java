// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// File: PropPanelInclude.java
// Classes: PropPanelInclude
// Original Author: mail@jeremybennett.com

package org.argouml.uml.ui.behavior.use_cases;

import javax.swing.JComboBox;

import org.argouml.application.api.Argo;

import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.MModelElement;


/**
 * <p>Builds the property panel for an Include relationship.</p>
 *
 * <p>This is a type of Relationship, but, since Relationship has no semantic
 *   meaning of its own, we derive directly from PropPanelModelElement (as
 *   other children of Relationship do).</p>
 *
 */

public class PropPanelInclude extends PropPanelModelElement {


    /**
     * Constructor. Builds up the various fields required.
     */

    public PropPanelInclude() {
        super("Include", ConfigLoader.getTabPropsOrientation());

        addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
        addField(Argo.localize("UMLMenu", "label.stereotype"),
            new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),getStereotypeBox()));
        addField(Argo.localize("UMLMenu", "label.namespace"), getNamespaceScroll());

        addSeperator();

        JComboBox baseBox = new UMLComboBox2(new UMLIncludeBaseComboBoxModel(), ActionSetIncludeBase.SINGLETON);
        addField(Argo.localize("UMLMenu", "label.usecase-base"), baseBox);

        JComboBox additionBox = new UMLComboBox2(new UMLIncludeAdditionComboBoxModel(), ActionSetIncludeAddition.SINGLETON);
        addField(Argo.localize("UMLMenu", "label.addition"), additionBox);

   /*
        // FIXME - Why is this code commented out - is there work to do here - Bob Tarling
    *
        // The addition use case (reuse earlier variables). Note that because
        // of the NSUML bug we look for the "base" event, rather than the
        // "addition" event" here.

        addSeperator();

        model = new UMLComboBoxModel(this, "isAcceptableUseCase",
                                     "base", "getAddition",
                                     "setAddition", true, MUseCase.class,
                                     true);
        box   = new UMLComboBox(model);
        nav   = new UMLComboBoxNavigator(this, "NavUseCase", box);

        addCaption("Addition:", 1, 1, 0);
        addField(nav, 1, 1, 0);
*/
        // Add the toolbar. Just the four basic buttons for now.

        new PropPanelButton(this, buttonPanel, _navUpIcon,
                            Argo.localize("UMLMenu", "button.go-up"), "navigateNamespace", null);
        new PropPanelButton(this, buttonPanel, _deleteIcon,
                            localize("Delete"), "removeElement", null);
    }


    /**
     * <p>Get the current base use case of the include relationship.</p>
     *
     * <p><em>Note</em>. There is a bug in NSUML, where the "include" and
     *   "include2" associations of a use case are back to front, i.e "include"
     *   is used as the opposite end of "addition" to point to an including use
     *   case, rather than an included use case.  Fixed within the include
     *   relationship, rather than the use case, by reversing the use of access
     *   functions for the "base" and "addition" associations in the code.</p>
     *
     * @return  The {@link MUseCase} that is the base of this include
     *          relationship or <code>null</code> if there is none. Returned as
     *          type {@link MUseCase} to fit in with the type specified for
     *          the {@link UMLComboBoxModel}.
     */
    public MUseCase getBase() {
        MUseCase base   = null;
        Object      target = getTarget();

        // Note that because of the NSUML bug, we must use getAddition() rather
        // than getBase() to get the base use case.

        if (target instanceof MInclude) {
            base = ((MInclude) target).getAddition();
        }

        return base;
    }

    /**
     * <p>Set the base use case of the include relationship.</p>
     *
     * <p><em>Note</em>. There is a bug in NSUML, where the "include" and
     *   "include2" associations of a use case are back to front, i.e "include"
     *   is used as the opposite end of "addition" to point to an including use
     *   case, rather than an included use case.  Fixed within the include
     *   relationship, rather than the use case, by reversing the use of access
     *   functions for the "base" and "addition" associations in the code.</p>
     *
     * @param base  The {@link MUseCase} to set as the base of this include
     *              relationship. Supplied as type {@link MUseCase} to fit in
     *              with the type specified for the {@link UMLComboBoxModel}.
     */

    public void setBase(MUseCase base) {
        Object target = getTarget();

        // Note that because of the NSUML bug, we must use setAddition() rather
        // than setBase() to set the base use case.

        if(target instanceof MInclude) {
            ((MInclude) target).setAddition((MUseCase) base);
        }
    }


    /**
     * <p>Get the current addition use case of the include relationship.</p>
     *
     * <p><em>Note</em>. There is a bug in NSUML, where the "include" and
     *   "include2" associations of a use case are back to front, i.e "include"
     *   is used as the opposite end of "addition" to point to an including use
     *   case, rather than an included use case.  Fixed within the include
     *   relationship, rather than the use case, by reversing the use of access
     *   functions for the "base" and "addition" associations in the code.</p>
     *
     * @return  The {@link MUseCase} that is the addition of this include
     *          relationship or <code>null</code> if there is none. Returned as
     *          type {@link MUseCase} to fit in with the type specified for the
     *          {@link UMLComboBoxModel}.
     */

    public MUseCase getAddition() {
        MUseCase addition   = null;
        Object      target      = getTarget();

        // Note that because of the NSUML bug, we must use getBase() rather
        // than getAddition() to get the addition use case.

        if (target instanceof MInclude) {
            addition = ((MInclude) target).getBase();
        }

        return addition;
    }

    /**
     * <p>Set the addition use case of the include relationship.</p>
     *
     * <p><em>Note</em>. There is a bug in NSUML, where the "include" and
     *   "include2" associations of a use case are back to front, i.e "include"
     *   is used as the opposite end of "addition" to point to an including use
     *   case, rather than an included use case.  Fixed within the include
     *   relationship, rather than the use case, by reversing the use of access
     *   functions for the "base" and "addition" associations in the code.</p>
     *
     * @param addition  The {@link MUseCase} to set as the addition of this
     *                   include relationship. Supplied as type {@link
     *                   MUseCase} to fit in with the type specified for the
     *                   {@link UMLComboBoxModel}.
     */

    public void setAddition(MUseCase addition) {
        Object target = getTarget();

        // Note that because of the NSUML bug, we must use setBase() rather
        // than setAddition() to set the addition use case.

        if(target instanceof MInclude) {
            ((MInclude) target).setBase((MUseCase) addition);
        }
    }


    /**
     * <p>Predicate to test if a model element may appear in the list of
     *   potential use cases.</p>
     *
     * <p><em>Note</em>. We don't try to prevent the user setting up circular
     *   include relationships. This may be necessary temporarily, for example
     *   while reversing a relationship. It is up to a critic to track
     *   this.</p>
     *
     * @param modElem  the {@link MModelElement} to test.
     *
     * @return         <code>true</code> if modElem is a use case,
     *                 <code>false</code> otherwise.
     */

    public boolean isAcceptableUseCase(MModelElement modElem) {

        return modElem instanceof MUseCase;
    }


} /* end class PropPanelInclude */
