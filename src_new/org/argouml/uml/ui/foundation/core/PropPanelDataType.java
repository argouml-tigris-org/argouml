// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

import java.util.Collection;
import java.util.Iterator;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.util.ConfigLoader;

/**
 * TODO: this property panel needs refactoring to remove dependency on old gui
 * components.
 */
public class PropPanelDataType extends PropPanelClassifier {

    private JScrollPane _attributeScroll;

    private JScrollPane _operationScroll;

    private static UMLClassAttributeListModel attributeListModel = new UMLClassAttributeListModel();

    private static UMLClassOperationListModel operationListModel = new UMLClassOperationListModel();

    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelDataType() {
        super("DataType", _dataTypeIcon, ConfigLoader.getTabPropsOrientation());

        Class mclass = (Class) ModelFacade.DATATYPE;

        addField(Translator.localize("UMLMenu", "label.name"),
                getNameTextField());
        addField(Translator.localize("UMLMenu", "label.stereotype"),
                new UMLComboBoxNavigator(this, Translator.localize("UMLMenu",
                        "tooltip.nav-stereo"), getStereotypeBox()));
        addField(Translator.localize("UMLMenu", "label.namespace"),
                getNamespaceComboBox());
        addField(Translator.localize("UMLMenu", "label.modifiers"),
                _modifiersPanel);
        addField(Translator.localize("UMLMenu", "label.namespace-visibility"),
                getNamespaceVisibilityPanel());

        addSeperator();

        addField(Translator.localize("UMLMenu", "label.client-dependencies"),
                getClientDependencyScroll());
        addField(Translator.localize("UMLMenu", "label.supplier-dependencies"),
                getSupplierDependencyScroll());
        addField(Translator.localize("UMLMenu", "label.generalizations"),
                getGeneralizationScroll());
        addField(Translator.localize("UMLMenu", "label.specializations"),
                getSpecializationScroll());

        addSeperator();

        addField(Translator.localize("UMLMenu", "label.operations"),
                getOperationScroll());

        addField(Translator.localize("UMLMenu", "label.literals"),
                getAttributeScroll());

        new PropPanelButton(this, buttonPanel, _navUpIcon, Translator.localize(
                "UMLMenu", "button.go-up"), "navigateUp", null);
        new PropPanelButton(this, buttonPanel, _dataTypeIcon, Translator
                .localize("UMLMenu", "button.new-datatype"), "newDataType",
                null);
        new PropPanelButton(this, buttonPanel, _addAttrIcon, Translator
                .localize("UMLMenu", "button.new-enumeration-literal"),
                "addAttribute", null);

        new PropPanelButton(this, buttonPanel, _addOpIcon, Translator.localize(
                "UMLMenu", "button.new-operation"), "addOperation", null);
        new PropPanelButton(this, buttonPanel, _deleteIcon,
                localize("Delete datatype"), "removeElement", null);
    }

    public void addAttribute() {
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isAClassifier(target)) {
            Object classifier = /* (MClassifier) */target;
            Object stereo = null;
            if (ModelFacade.getStereotypes(classifier).size() > 0) {
                stereo = ModelFacade.getStereotypes(classifier).iterator()
                        .next();
            }
            if (stereo == null) {
                //
                //  if there is not an enumeration stereotype as
                //     an immediate child of the model, add one
                Object model = ModelFacade.getModel(classifier);
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
                                String stereoName = ModelFacade.getName(stereo);
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
                        ModelFacade.setStereotype(classifier, stereo);
                    }
                }
            }

            Object attr = CoreFactory.getFactory().buildAttribute(classifier);
            ModelFacade.setChangeable(attr,false);
            TargetManager.getInstance().setTarget(attr);
        }

    }
    
    public void addOperation() {
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isAClassifier(target)) {
            Object newOper =
                UmlFactory.getFactory().getCore().buildOperation(
                    /*(MClassifier)*/ target);
            // due to Well Defined rule [2.5.3.12/1]       
            ModelFacade.setQuery(newOper, true);
            TargetManager.getInstance().setTarget(newOper);
        }
    }

    /**
     * Returns the operationScroll.
     * 
     * @return JScrollPane
     */
    public JScrollPane getOperationScroll() {
        if (_operationScroll == null) {
            JList list = new UMLLinkedList(operationListModel);
            _operationScroll = new JScrollPane(list);
        }
        return _operationScroll;
    }

    /**
     * Returns the attributeScroll.
     * 
     * @return JScrollPane
     */
    public JScrollPane getAttributeScroll() {
        if (_attributeScroll == null) {
            JList list = new UMLLinkedList(attributeListModel);
            _attributeScroll = new JScrollPane(list);
        }
        return _attributeScroll;
    }

    public void newDataType() {
        Object target = getTarget();
        if (ModelFacade.isADataType(target)) {
            Object dt = /* (MDataType) */target;
            Object ns = ModelFacade.getNamespace(dt);
            Object newDt = CoreFactory.getFactory().createDataType();
            ModelFacade.addOwnedElement(ns, newDt);
            TargetManager.getInstance().setTarget(newDt);
        }
    }

} /* end class PropPanelDataType */