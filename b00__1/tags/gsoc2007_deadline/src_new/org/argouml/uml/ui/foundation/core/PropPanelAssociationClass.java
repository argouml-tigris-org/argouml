// $Id:PropPanelAssociationClass.java 12672 2007-05-26 21:28:01Z tfmorris $
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

package org.argouml.uml.ui.foundation.core;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;

/**
 * The properties panel for an AssociationClass.
 * It is basically a PropPanelClass but with the proper Association Ends
 * for an Association Class.
 *
 *
 * @author pepargouml
 */
public class PropPanelAssociationClass extends PropPanelClassifier {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = -7620821534700927917L;

    private JScrollPane attributeScroll;

    private JScrollPane operationScroll;

    private JScrollPane assocEndScroll;

    private static UMLClassAttributeListModel attributeListModel =
            new UMLClassAttributeListModel();

    private static UMLClassOperationListModel operationListModel =
            new UMLClassOperationListModel();

    /**
     * Construct a property panel for AssociationClass elements.
     */
    public PropPanelAssociationClass() {
        super("AssociationClass", lookupIcon("AssociationClass"),
                ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.namespace"),
                getNamespaceSelector());
        getModifiersPanel().add(new UMLClassActiveCheckBox());
        add(getModifiersPanel());
        add(getNamespaceVisibilityPanel());

        addSeparator();

        addField(Translator.localize("label.client-dependencies"),
                getClientDependencyScroll());
        addField(Translator.localize("label.supplier-dependencies"),
                getSupplierDependencyScroll());
        addField(Translator.localize("label.generalizations"),
                getGeneralizationScroll());
        addField(Translator.localize("label.specializations"),
                getSpecializationScroll());

        JList assocEndList = new UMLLinkedList(
                new UMLAssociationConnectionListModel());
        assocEndScroll = new JScrollPane(assocEndList);
        addField(Translator.localize("label.connections"),
                assocEndScroll);

        addSeparator();

        addField(Translator.localize("label.attributes"),
                getAttributeScroll());

        JList connections = new UMLLinkedList(
                new UMLClassifierAssociationEndListModel());
        JScrollPane connectionsScroll = new JScrollPane(connections);
        addField(Translator.localize("label.association-ends"),
                connectionsScroll);

        addField(Translator.localize("label.operations"),
                getOperationScroll());
        addField(Translator.localize("label.owned-elements"),
                getOwnedElementsScroll());

        addAction(new ActionNavigateNamespace());
        addAction(new ActionAddAttribute());
        addAction(new ActionAddOperation());
        addAction(getActionNewReception());
        addAction(new ActionNewInnerClass());
        addAction(new ActionNewClass());
        addAction(new ActionNewStereotype());
        addAction(getDeleteAction());
    }

    /**
     * Returns the operationScroll.
     *
     * @return JScrollPane
     */
    @Override
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
    @Override
    public JScrollPane getAttributeScroll() {
        if (attributeScroll == null) {
            JList list = new UMLLinkedList(attributeListModel);
            attributeScroll = new JScrollPane(list);
        }
        return attributeScroll;
    }

}
