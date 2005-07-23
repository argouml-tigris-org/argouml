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

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.ActionDeleteSingleModelElement;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLConditionExpressionModel;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionModel2;
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
     * Constructor. Builds up the various fields required.
     * TODO: improve the conditionfield so it can be checked and the
     * OCL editor can be used.
     */

    public PropPanelExtend() {
        super("Extend", lookupIcon("Extend"), 
                ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
		 getNameTextField());
        addField(Translator.localize("label.stereotype"),
                getStereotypeSelector());
        addField(Translator.localize("label.namespace"),
		 getNamespaceScroll());

        addSeperator();


        // Link to the two ends. This is done as a drop down. First for the
        // base use case.
        addField(Translator.localize("label.usecase-base"),
		 new UMLComboBox2(new UMLExtendBaseComboBoxModel(),
				  ActionSetExtendBase.getInstance()));

        addField(Translator.localize("label.extension"),
		 new UMLComboBox2(new UMLExtendExtensionComboBoxModel(),
				  ActionSetExtendExtension.getInstance()));

        JList extensionPointList =
	    new UMLMutableLinkedList(new UMLExtendExtensionPointListModel(),
		ActionAddExtendExtensionPoint.getInstance(),
		ActionNewExtendExtensionPoint.SINGLETON);
        addField(Translator.localize("label.extension-points"),
		new JScrollPane(extensionPointList));

        addSeperator();

        UMLExpressionModel2 conditionModel =
            new UMLConditionExpressionModel(this, "condition");

        JTextArea conditionArea =
            new UMLExpressionBodyField(conditionModel, true);
        conditionArea.setRows(5);
        JScrollPane conditionScroll =
            new JScrollPane(conditionArea);

        addField("Condition:", conditionScroll);

        // Add the toolbar buttons:
        addButton(new PropPanelButton2(new ActionNavigateNamespace()));
        addButton(new PropPanelButton2(new ActionNewExtensionPoint(),
                lookupIcon("ExtensionPoint")));
        addButton(new PropPanelButton2(new ActionNewStereotype(),
                lookupIcon("Stereotype")));
        addButton(new PropPanelButton2(new ActionDeleteSingleModelElement(),
                lookupIcon("Delete")));
    }

    /**
     * Invoked by the "New Extension Point" toolbar button to create a new
     *   extension point for this extend relationship in the same namespace as
     *   the current extend relationship.<p>
     *
     * This code uses getFactory and adds the extension point to
     *   the current extend relationship.<p>
     */
    private class ActionNewExtensionPoint
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

} /* end class PropPanelExtend */
