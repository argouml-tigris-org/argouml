// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import javax.swing.JTextArea;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLConditionExpressionModel;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;

/**
 * Builds the property panel for an Extend relationship.<p>
 *
 * This is a type of Relationship, but, since Relationship has no semantic
 *   meaning of its own, we derive directly from PropPanelModelElement (as
 *   other children of Relationship do).<p>
 *
 * TODO: this property panel needs refactoring to remove dependency on
 *       old gui components.
 *
 * @author mail@jeremybennett.com
 */
public class PropPanelExtend extends PropPanelModelElement {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = -3257769932777323293L;

    /**
     * Construct a new property panel for an Extend.<p>
     *
     * TODO: improve the conditionfield so it can be checked and the
     * OCL editor can be used.
     */

    public PropPanelExtend() {
        super("Extend", lookupIcon("Extend"),
                ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
		 getNameTextField());
        addField(Translator.localize("label.namespace"),
                getNamespaceSelector());

        addSeparator();


        // Link to the two ends.
        addField(Translator.localize("label.usecase-base"),
                getSingleRowScroll(new UMLLinkedList(
                        new UMLExtendBaseListModel())));

        addField(Translator.localize("label.extension"),
                getSingleRowScroll(new UMLLinkedList(
                        new UMLExtendExtensionListModel())));

        JList extensionPointList =
	    new UMLMutableLinkedList(new UMLExtendExtensionPointListModel(),
		ActionAddExtendExtensionPoint.getInstance(),
		ActionNewExtendExtensionPoint.SINGLETON);
        addField(Translator.localize("label.extension-points"),
		new JScrollPane(extensionPointList));

        addSeparator();

        UMLExpressionModel2 conditionModel =
            new UMLConditionExpressionModel(this, "condition");

        JTextArea conditionArea =
            new UMLExpressionBodyField(conditionModel, true);
        conditionArea.setRows(5);
        JScrollPane conditionScroll =
            new JScrollPane(conditionArea);

        addField(Translator.localize("label.condition"), conditionScroll);

        // Add the toolbar buttons:
        addAction(new ActionNavigateNamespace());
        addAction(new ActionNewExtensionPoint());
        addAction(new ActionNewStereotype());
        addAction(getDeleteAction());
    }

    /**
     * Invoked by the "New Extension Point" toolbar button to create a new
     * extension point for this extend relationship in the same namespace as the
     * current extend relationship.<p>
     *
     * This code uses getFactory and adds the extension point to the current
     * extend relationship.
     */
    private static class ActionNewExtensionPoint
        extends AbstractActionNewModelElement {

        /**
         * The serial version.
         */
        private static final long serialVersionUID = 2643582245431201015L;

        /**
         * Construct an action to create a new ExtensionPoint.
         */
        public ActionNewExtensionPoint() {
            super("button.new-extension-point");
            putValue(Action.NAME,
                    Translator.localize("button.new-extension-point"));
        }

        /*
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Object target = TargetManager.getInstance().getModelTarget();
            if (Model.getFacade().isAExtend(target)) {
                Object ns = Model.getFacade().getNamespace(target);
                if (ns != null) {
                    if (Model.getFacade().getBase(target) != null) {
                        Object extensionPoint =
                            Model.getUseCasesFactory()
                            	.buildExtensionPoint(
                            	        Model.getFacade().getBase(target));
                        Model.getUseCasesHelper().addExtensionPoint(
                                target,
                                extensionPoint);
                        TargetManager.getInstance().setTarget(extensionPoint);
                        super.actionPerformed(e);
                    }
                }
            }
        }
    }

}
