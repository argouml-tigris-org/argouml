// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.CoreFactory;
import org.argouml.model.uml.UmlFactory;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.util.ConfigLoader;

/**
 * TODO: this property panel needs refactoring to remove dependency on old gui
 * components.
 */
public class PropPanelDataType extends PropPanelClassifier {

    private JScrollPane attributeScroll;

    private JScrollPane operationScroll;

    private static UMLClassAttributeListModel attributeListModel = 
        new UMLClassAttributeListModel();

    private static UMLClassOperationListModel operationListModel = 
        new UMLClassOperationListModel();

    /**
     * The constructor.
     * 
     */
    public PropPanelDataType() {
        super("DataType", lookupIcon("DataType"), 
                ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.stereotype"),
                getStereotypeBox());
        addField(Translator.localize("label.namespace"),
                getNamespaceComboBox());
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

        addField(Translator.localize("label.literals"),
                getAttributeScroll());

        addButton(new PropPanelButton2(new ActionNavigateContainerElement()));
        addButton(new PropPanelButton2(new ActionAddDataTypeToDataType(), 
                lookupIcon("DataType")));
        addButton(new PropPanelButton2(new ActionAddAttributeToDataType(), 
                lookupIcon("NewAttribute")));
        addButton(new PropPanelButton2(new ActionAddQueryOperation(), 
                lookupIcon("NewOperation")));
        addButton(new PropPanelButton2(new ActionRemoveFromModel(), 
                lookupIcon("Delete")));
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
            if (org.argouml.model.ModelFacade.isAClassifier(target)) {
                Object newOper = 
                    UmlFactory.getFactory().getCore().buildOperation(target);
                // due to Well Defined rule [2.5.3.12/1]
                ModelFacade.setQuery(newOper, true);
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
            if (org.argouml.model.ModelFacade.isAClassifier(target)) {
                Object stereo = null;
                if (ModelFacade.getStereotypes(target).size() > 0) {
                    stereo = ModelFacade.getStereotypes(target)
                        .iterator().next();
                }
                if (stereo == null) {
                    //  if there is not an enumeration stereotype as
                    //     an immediate child of the model, add one
                    Object model = ModelFacade.getModel(target);
                    Object ownedElement;
                    boolean match = false;
                    if (model != null) {
                        Collection ownedElements = ModelFacade
                        .getOwnedElements(model);
                        if (ownedElements != null) {
                            Iterator iter = ownedElements.iterator();
                            while (iter.hasNext()) {
                                ownedElement = iter.next();
                                if (org.argouml.model.ModelFacade
                                        .isAStereotype(ownedElement)) {
                                    stereo = /* (MStereotype) */ownedElement;
                                    String stereoName = 
                                        ModelFacade.getName(stereo);
                                    if (stereoName != null 
                                        && stereoName.equals("enumeration")) {
                                        match = true;
                                        break;
                                    }
                                }
                            }
                            if (!match) {
                                stereo = UmlFactory.getFactory()
                                    .getExtensionMechanisms()
                                        .createStereotype();
                                ModelFacade.setName(stereo, "enumeration");
                                ModelFacade.addOwnedElement(model, stereo);
                            }
                            ModelFacade.setStereotype(target, stereo);
                        }
                    }
                }
                
                Object attr = CoreFactory.getFactory().buildAttribute(target);
                ModelFacade.setChangeable(attr, false);
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

    private class ActionAddDataTypeToDataType 
        extends AbstractActionNewModelElement {
        
        /**
         * The constructor.
         */
        public ActionAddDataTypeToDataType() {
            super("button.new-datatype");
            putValue(Action.NAME, Translator.localize(
                "button.new-datatype"));
        }
        
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            Object target = TargetManager.getInstance().getModelTarget();
            if (ModelFacade.isADataType(target)) {
                Object ns = ModelFacade.getNamespace(target);
                Object newDt = CoreFactory.getFactory().createDataType();
                ModelFacade.addOwnedElement(ns, newDt);
                TargetManager.getInstance().setTarget(newDt);
                super.actionPerformed(e);
            }
        }
    }

} /* end class PropPanelDataType */