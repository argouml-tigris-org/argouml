// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: PropPanelUseCase.java
// Classes: PropPanelUseCase
// Original Author: your email address here
// $Id$

// 21 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Changed to use the
// labels "Generalizes:" for inheritance (needs Specializes some time).

// 21 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Specializes field
// added. Factoring to use PropPanelModifiers and tidying up of layout.

// 4 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Tool tip changed to
// "Add use case".

package org.argouml.uml.ui.behavior.use_cases;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesFactory;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelClassifier;
import org.argouml.util.ConfigLoader;

/**
 * <p>Builds the property panel for a use case.</p>
 *
 * <p>This is a type of Classifier, and like other Classifiers can have
 *   attributes and operations (some processes use these to define
 *   requirements). <em>Note</em>. ArgoUML does not currently support separate
 *   compartments on the display for this.</p>
 */

public class PropPanelUseCase extends PropPanelClassifier {

    /**
     * <p>Constructor. Builds up the various fields required.</p>
     */
    public PropPanelUseCase() {
        // Invoke the Classifier constructor, but passing in our name and
        // representation and requesting 3 columns
        super("UseCase", ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("UMLMenu", "label.name"), 
                getNameTextField());
    	// addField(Translator.localize("UMLMenu", "label.stereotype"), 
        //     new UMLComboBoxNavigator(this, Translator.localize("UMLMenu", 
        //     "tooltip.nav-stereo"), getStereotypeBox()));
        addField(Translator.localize("UMLMenu", "label.stereotype"), 
                getStereotypeBox());
    	addField(Translator.localize("UMLMenu", "label.namespace"), 
                getNamespaceComboBox());

	
	// Modifiers - an abstract use case does not really make any sense
        _modifiersPanel.remove(0);
        add(_modifiersPanel);
        

	JList extensionPoints = new UMLMutableLinkedList(
            new UMLUseCaseExtensionPointListModel(), null, 
            ActionNewUseCaseExtensionPoint.SINGLETON);
	addField(Translator.localize("UMLMenu", "label.extension-points"),
		 new JScrollPane(extensionPoints));

	addSeperator();

	addField(Translator.localize("UMLMenu", "label.generalizations"), 
            getGeneralizationScroll());
	addField(Translator.localize("UMLMenu", "label.specializations"), 
            getSpecializationScroll());

	JList extendsList = new UMLLinkedList(new UMLUseCaseExtendListModel());
	addField(Translator.localize("UMLMenu", "label.extends"),
		 new JScrollPane(extendsList));

	JList includesList = new UMLLinkedList(
            new UMLUseCaseIncludeListModel());
	addField(Translator.localize("UMLMenu", "label.includes"),
		 new JScrollPane(includesList));

	addSeperator();

        addField(Translator.localize("UMLMenu", "label.association-ends"), 
            getAssociationEndScroll());

        buttonPanel.add(new PropPanelButton2(this, 
                new ActionNavigateNamespace()));
        new PropPanelButton(this, buttonPanel, useCaseIcon,
                Translator.localize("UMLMenu", "button.new-usecase"), 
                "newUseCase", null);
        new PropPanelButton(this, buttonPanel, extensionPointIcon,
                localize("New Extension Point"),
                "newExtensionPoint",
                null);
        new PropPanelButton(this, buttonPanel, _receptionIcon, 
                Translator.localize("UMLMenu", "button.new-reception"), 
                getActionNewReception());
        buttonPanel
        .add(new PropPanelButton2(this, new ActionRemoveFromModel()));

    }


    /**
     * <p>Invoked by the "Add use case" toolbar button to create a new use case
     *   property panel in the same namespace as the current use case.</p>
     *
     * <p>This code uses getFactory and adds the use case explicitly to the
     *   namespace. Extended to actually navigate to the new use case.</p>
     */

    public void newUseCase() {
        Object target = getTarget();

        if (ModelFacade.isAUseCase(target)) {
            Object ns = ModelFacade.getNamespace(target);

            if (ns != null) {
                Object useCase = UseCasesFactory.getFactory().createUseCase();
                ModelFacade.addOwnedElement(ns, useCase);
                TargetManager.getInstance().setTarget(useCase);
            }
        }
    }


    /**
     * <p>Invoked by the "New Extension Point" toolbar button to create a new
     *   extension point for this use case in the same namespace as the current
     *   use case.</p>
     *
     * <p>This code uses getFactory and adds the extension point explicitly to
     *   the, making its associated use case the current use case.</p>
     */
    public void newExtensionPoint() {
        Object target = getTarget();

        if (ModelFacade.isAUseCase(target)) {
            TargetManager.getInstance().setTarget(UseCasesFactory.getFactory()
                    .buildExtensionPoint(target));
        }
    }
} /* end class PropPanelUseCase */
