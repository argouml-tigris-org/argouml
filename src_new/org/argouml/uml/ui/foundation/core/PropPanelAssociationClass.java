// File: PropPanelAssociationClass.java
// Classes: PropPanelAssociationClass
// Original Author: pepargouml@yahoo.es

package org.argouml.uml.ui.foundation.core;

import org.argouml.i18n.Translator;
import org.argouml.uml.diagram.ui.ActionAddAttribute;
import org.argouml.uml.diagram.ui.ActionAddOperation;
import org.argouml.uml.ui.*;
import org.argouml.util.ConfigLoader;

import javax.swing.*;

/**
 * The properties panel for an AssociationClass.
 * It is basically a PropPanelClass but with the proper Association Ends
 * for an Association Class.
 *
 * TODO: this property panel needs refactoring to remove dependency on old gui
 * components.
 */
public class PropPanelAssociationClass extends PropPanelClassifier {

    private JScrollPane attributeScroll;

    private JScrollPane operationScroll;

    private JScrollPane assocEndScroll;

    private static UMLClassAttributeListModel attributeListModel =
            new UMLClassAttributeListModel();

    private static UMLClassOperationListModel operationListModel =
            new UMLClassOperationListModel();

    ////////////////////////////////////////////////////////////////
    // contructors
    /**
     * The constructor.
     */
    public PropPanelAssociationClass() {
        super("AssociationClass", ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.stereotype"),
                getStereotypeBox());
        addField(Translator.localize("label.namespace"),
                getNamespaceComboBox());
        getModifiersPanel().add(new UMLClassActiveCheckBox());
        add(getModifiersPanel());
        add(getNamespaceVisibilityPanel());

        addSeperator();

        addField(Translator.localize("label.client-dependencies"),
                getClientDependencyScroll());
        addField(Translator.localize("label.supplier-dependencies"),
                getSupplierDependencyScroll());
        addField(Translator.localize("label.generalizations"),
                getGeneralizationScroll());
        addField(Translator.localize("label.specializations"),
                getSpecializationScroll());

        addSeperator();

        addField(Translator.localize("label.attributes"),
                getAttributeScroll());
        JList assocEndList = new UMLLinkedList(
                new UMLAssociationConnectionListModel());
        assocEndScroll = new JScrollPane(assocEndList);
        addField(Translator.localize("label.association-ends"),
                assocEndScroll);
        addField(Translator.localize("label.operations"),
                getOperationScroll());
        addField(Translator.localize("label.owned-elements"),
                getOwnedElementsScroll());

        addButton(new PropPanelButton2(this,
                new ActionNavigateNamespace()));
        addButton(new PropPanelButton2(this,
                new ActionAddAttribute()));
        addButton(new PropPanelButton2(this,
                new ActionAddOperation()));
        addButton(new PropPanelButton2(this, getActionNewReception()));
        new PropPanelButton(this, lookupIcon("InnerClass"),
                Translator.localize("button.new-inner-class"),
                new ActionNewInnerClass());
        new PropPanelButton(this, lookupIcon("Class"), Translator.localize(
                "button.new-class"), new ActionNewClass());
        addButton(new PropPanelButton2(this, new ActionRemoveFromModel()));
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

} /* end class PropPanelAssociationClass */
