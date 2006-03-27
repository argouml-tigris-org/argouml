// $Id$
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

package org.argouml.uml.ui.behavior.use_cases;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.ActionDeleteSingleModelElement;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelClassifier;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;

/**
 * Builds the property panel for a use case.<p>
 *
 * This is a type of Classifier, and like other Classifiers can have
 * attributes and operations (some processes use these to define
 * requirements). <em>Note</em>. ArgoUML does not currently support separate
 * compartments on the display for this.<p>
 */
public class PropPanelUseCase extends PropPanelClassifier {

    /**
     * Constructor. Builds up the various fields required.<p>
     */
    public PropPanelUseCase() {
        // Invoke the Classifier constructor, but passing in our name and
        // representation and requesting 3 columns
        super("UseCase",
            lookupIcon("UseCase"),
            ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.stereotype"),
                getStereotypeSelector());
    	addField(Translator.localize("label.namespace"),
                getNamespaceSelector());

        add(getModifiersPanel());



	addSeperator();

	addField(Translator.localize("label.generalizations"),
            getGeneralizationScroll());
	addField(Translator.localize("label.specializations"),
            getSpecializationScroll());

	JList extendsList = new UMLLinkedList(new UMLUseCaseExtendListModel());
	addField(Translator.localize("label.extends"),
		 new JScrollPane(extendsList));

	JList includesList =
            new UMLLinkedList(
                    new UMLUseCaseIncludeListModel());
	addField(Translator.localize("label.includes"),
		 new JScrollPane(includesList));

	addSeperator();

	JList extensionPoints =
	    new UMLMutableLinkedList(
	            new UMLUseCaseExtensionPointListModel(), null,
	            ActionNewUseCaseExtensionPoint.SINGLETON);
        addField(Translator.localize("label.extension-points"),
            new JScrollPane(extensionPoints));

        addField(Translator.localize("label.association-ends"),
            getAssociationEndScroll());

        addAction(new ActionNavigateNamespace());
        addAction(new ActionNewUseCase());
        addAction(new ActionNewExtensionPoint());
        addAction(getActionNewReception());
        addAction(new ActionNewStereotype());
        addAction(new ActionDeleteSingleModelElement());
    }


    /**
     * Invoked by the "New Extension Point" toolbar button to create a new
     * extension point for this use case in the same namespace as the current
     * use case.<p>
     *
     * This code uses getFactory and adds the extension point explicitly to
     * the, making its associated use case the current use case.<p>
     */
    private static class ActionNewExtensionPoint
        extends AbstractActionNewModelElement {

        /**
         * The constructor.
         */
        public ActionNewExtensionPoint() {
            super("button.new-extension-point");
            putValue(Action.NAME,
                    Translator.localize("button.new-extension-point"));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            Object target = TargetManager.getInstance().getModelTarget();
            if (Model.getFacade().isAUseCase(target)) {
                TargetManager.getInstance().setTarget(
                    Model.getUseCasesFactory().buildExtensionPoint(target));
                super.actionPerformed(e);
            }
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = 1556105736769814764L;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 8352300400553000518L;
} /* end class PropPanelUseCase */
