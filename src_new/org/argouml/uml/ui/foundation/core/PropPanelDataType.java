// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.ActionDeleteSingleModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.CollectionUtil;
import org.argouml.util.ConfigLoader;

/**
 * The properties panel for a Datatype.
 */
public class PropPanelDataType extends PropPanelClassifier {

    private JScrollPane attributeScroll;

    private JScrollPane literalsScroll;

    private JScrollPane operationScroll;


    private static UMLClassAttributeListModel attributeListModel =
        new UMLClassAttributeListModel();

    private static UMLEnumerationLiteralsListModel literalsListModel =
        new UMLEnumerationLiteralsListModel();

    private static UMLClassOperationListModel operationListModel =
        new UMLClassOperationListModel();

    /**
     * The constructor.
     */
    public PropPanelDataType() {
        super("DataType", lookupIcon("DataType"),
                ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.stereotype"),
                getStereotypeSelector());
        addField(Translator.localize("label.namespace"),
                getNamespaceSelector());
        add(getModifiersPanel());

        addSeperator();

        add(getNamespaceVisibilityPanel());
        addField(Translator.localize("label.client-dependencies"),
                getClientDependencyScroll());
        addField(Translator.localize("label.supplier-dependencies"),
                getSupplierDependencyScroll());
        addField(Translator.localize("label.generalizations"),
                getGeneralizationScroll());
        addField(Translator.localize("label.specializations"),
                getSpecializationScroll());

        addSeperator();

        addField(Translator.localize("label.operations"),
                getOperationScroll());

        addField(Translator.localize("label.attributes"),
                getAttributeScroll());

        addField(Translator.localize("label.literals"),
                getLiteralsScroll());

        addAction(new ActionNavigateContainerElement());
        addAction(new ActionAddDataType());
        addAction(new ActionAddAttributeToDataType());
        addAction(new ActionAddQueryOperation());
        addAction(new ActionNewStereotype());
        addAction(new ActionDeleteSingleModelElement());
    }

    private class ActionAddQueryOperation
        extends AbstractActionNewModelElement {

        /**
         * The constructor.
         */
        public ActionAddQueryOperation() {
            super("button.new-operation");
            putValue(Action.NAME, Translator.localize("button.new-operation"));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            Object target = TargetManager.getInstance().getModelTarget();
            if (Model.getFacade().isAClassifier(target)) {
                Collection propertyChangeListeners =
                    ProjectManager.getManager()
                    	.getCurrentProject().findFigsForMember(target);
                Object model =
                    ProjectManager.getManager()
                    	.getCurrentProject().getModel();
                Object voidType =
                    ProjectManager.getManager()
                    	.getCurrentProject().findType("void");
                Object newOper =
                    Model.getCoreFactory()
                    	.buildOperation(target, model, voidType,
                    	        propertyChangeListeners);
                // due to Well Defined rule [2.5.3.12/1]
                Model.getCoreHelper().setQuery(newOper, true);
                TargetManager.getInstance().setTarget(newOper);
                super.actionPerformed(e);
            }
        }
    }

    private class ActionAddAttributeToDataType
        extends AbstractActionNewModelElement {

        /**
         * The constructor.
         */
        public ActionAddAttributeToDataType() {
            super("button.new-enumeration-literal");
            putValue(Action.NAME, Translator.localize(
                "button.new-enumeration-literal"));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            Object target = TargetManager.getInstance().getModelTarget();
            if (Model.getFacade().isAClassifier(target)) {
                Object stereo = CollectionUtil.getFirstItemOrNull(
                        Model.getFacade().getStereotypes(target));
                if (stereo == null) {
                    //  if there is not an enumeration stereotype as
                    //     an immediate child of the model, add one
                    Object model = Model.getFacade().getModel(target);
                    Object ownedElement;
                    boolean match = false;
                    if (model != null) {
                        Collection ownedElements =
                            Model.getFacade().getOwnedElements(model);
                        if (ownedElements != null) {
                            Iterator iter = ownedElements.iterator();
                            while (iter.hasNext()) {
                                ownedElement = iter.next();
                                if (Model.getFacade().isAStereotype(
                                        ownedElement)) {
                                    stereo = /* (MStereotype) */ownedElement;
                                    String stereoName =
                                        Model.getFacade().getName(stereo);
                                    if (stereoName != null
                                        && stereoName.equals("enumeration")) {
                                        match = true;
                                        break;
                                    }
                                }
                            }
                            if (!match) {
                                stereo =
                                    Model.getExtensionMechanismsFactory()
                                        .buildStereotype("enumeration", model);
                                Model.getCoreHelper().addOwnedElement(
                                        model,
                                        stereo);
                            }
                            Model.getCoreHelper().setStereotype(target, stereo);
                        }
                    }
                }

                Collection propertyChangeListeners =
                    ProjectManager.getManager()
                    	.getCurrentProject().findFigsForMember(target);
                Object intType =
                    ProjectManager.getManager()
                    	.getCurrentProject().findType("int");
                Object model =
                    ProjectManager.getManager()
                    	.getCurrentProject().getModel();
                Object attr =
                    Model.getCoreFactory().buildAttribute(target,
                            model, intType, propertyChangeListeners);
                Model.getCoreHelper().setChangeable(attr, false);
                TargetManager.getInstance().setTarget(attr);
                super.actionPerformed(e);
            }
        }
    }

    /**
     * Returns the operationScroll.
     *
     * @return JScrollPane
     */
    public JScrollPane getOperationScroll() {
        if (operationScroll == null) {
            JList list = new UMLLinkedList(operationListModel);
            operationScroll = new JScrollPane(list);
        }
        return operationScroll;
    }

    /**
     * Returns the attributeScroll.
     *
     * @return JScrollPane
     */
    public JScrollPane getAttributeScroll() {
        if (attributeScroll == null) {
            JList list = new UMLLinkedList(attributeListModel);
            attributeScroll = new JScrollPane(list);
        }
        return attributeScroll;
    }

    /**
     * Returns the attributeScroll.
     *
     * @return JScrollPane
     */
    public JScrollPane getLiteralsScroll() {
        if (literalsScroll == null) {
            JList list = new UMLLinkedList(literalsListModel);
            literalsScroll = new JScrollPane(list);
        }
        return literalsScroll;
    }

} /* end class PropPanelDataType */
