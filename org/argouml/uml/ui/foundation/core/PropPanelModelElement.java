// Copyright (c) 1996-2001 The Regents of the University of California. All
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

// 4 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Icons for extend and
// include relationships added.

package org.argouml.uml.ui.foundation.core;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.argouml.application.api.Argo;
import org.argouml.application.api.ArgoModule;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.swingext.LabelledLayout;
import org.argouml.swingext.Orientation;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.UMLButtonPanel;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.UMLPlainTextDocument;
import org.argouml.uml.ui.UMLSearchableComboBox;
import org.argouml.uml.ui.UMLTextField2;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;

abstract public class PropPanelModelElement extends PropPanel {

    ////////////////////////////////////////////////////////////////
    // constants

    protected static ImageIcon _objectIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Object");
    protected static ImageIcon _componentInstanceIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("ComponentInstance");
    protected static ImageIcon _nodeInstanceIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("NodeInstance");
    protected static ImageIcon _instanceIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Instance");
    protected static ImageIcon _linkIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Link");
    protected static ImageIcon _stimulusIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Stimulus");
    protected static ImageIcon _associationIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Association");
    protected static ImageIcon _assocEndIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("AssociationEnd");
    protected static ImageIcon _assocEndRoleIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("AssociationEndRole");
    protected static ImageIcon _generalizationIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Generalization");
    protected static ImageIcon _realizationIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Realization");
    protected static ImageIcon _classIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Class");
    protected static ImageIcon _collaborationIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Collaboration");
    protected static ImageIcon _interfaceIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Interface");
    protected static ImageIcon _addOpIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("AddOperation");
    protected static ImageIcon _addAttrIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("AddAttribute");
    protected static ImageIcon _addAssocIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Association");
    protected static ImageIcon _packageIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Package");
    protected static ImageIcon _modelIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Model");
    protected static ImageIcon _innerClassIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("InnerClass");
    protected static ImageIcon _nodeIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Node");
    protected static ImageIcon _componentIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Component");
    protected static ImageIcon _dataTypeIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("DataType");
    protected static ImageIcon _actorIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Actor");
    protected static ImageIcon _useCaseIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("UseCase");
    protected static ImageIcon _extendIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Extend");
    protected static ImageIcon _extensionPointIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("ExtensionPoint");
    protected static ImageIcon _includeIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Include");
    protected static ImageIcon _dependencyIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Dependency");
    protected static ImageIcon _permissionIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Permission");
    protected static ImageIcon _usageIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Usage");
    protected static ImageIcon _parameterIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Parameter");
    protected static ImageIcon _operationIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Operation");
    protected static ImageIcon _signalIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("SignalSending");
    protected static ImageIcon _stereotypeIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Stereotype");
    protected static ImageIcon _guardIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Guard");
    protected static ImageIcon _transitionIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Transition");
    protected static ImageIcon _classifierRoleIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("ClassifierRole");
    protected static ImageIcon _associationRoleIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("AssociationRole");
    protected static ImageIcon _callActionIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("CallAction");
    protected static ImageIcon _interactionIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Interaction");
    // added next one so someone can change the icon independant of callaction
    protected static ImageIcon _actionIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("CallAction");
    protected static ImageIcon _receptionIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Reception");
    protected static ImageIcon _commentIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Note");
    protected static ImageIcon _messageIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Message");
    protected static ImageIcon _flowIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Flow");
    protected static ImageIcon _stateMachineIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("StateMachine");

    private JScrollPane namespaceScroll;
    private JComboBox namespaceComboBox;
    private JComboBox stereotypeBox;
    private JScrollPane supplierDependencyScroll;
    private JScrollPane clientDependencyScroll;
    private JScrollPane targetFlowScroll;
    private JScrollPane sourceFlowScroll;
    private JScrollPane constraintScroll;
    private JPanel namespaceVisibilityPanel;
    private JCheckBox specializationCheckBox;
    private JScrollPane elementResidenceScroll;
    private JTextField nameTextField;

    private static UMLModelElementNamespaceComboBoxModel namespaceComboBoxModel = new UMLModelElementNamespaceComboBoxModel();
    private static UMLModelElementStereotypeComboBoxModel stereotypeComboBoxModel = new UMLModelElementStereotypeComboBoxModel();
    private static UMLModelElementNamespaceListModel namespaceListModel = new UMLModelElementNamespaceListModel();
    private static UMLModelElementClientDependencyListModel clientDependencyListModel = new UMLModelElementClientDependencyListModel();
    private static UMLModelElementConstraintListModel constraintListModel = new UMLModelElementConstraintListModel();
    private static UMLModelElementElementResidenceListModel elementResidenceListModel = new UMLModelElementElementResidenceListModel();
    private static UMLModelElementNameDocument nameDocument = new UMLModelElementNameDocument();
    private static UMLModelElementSourceFlowListModel sourceFlowListModel = new UMLModelElementSourceFlowListModel();
    private static UMLModelElementSupplierDependencyListModel supplierDependencyListModel = new UMLModelElementSupplierDependencyListModel();
    private static UMLModelElementTargetFlowListModel targetFlowListModel = new UMLModelElementTargetFlowListModel();

    /**
     *    Constructs the PropPanel.
     *    @param title Title of panel
     *    @param panelCount number of horizontal panels
     *    @deprecated 7-Dec-2002 by Bob Tarling. Use the constructor
     *    specifying orientation instead.
     */
    public PropPanelModelElement(String name, int columns) {
        this(name, null, columns);
    }

    public PropPanelModelElement(String name, ImageIcon icon, Orientation orientation) {
        super(name, icon, orientation);
    }

    public PropPanelModelElement(String name, Orientation orientation) {
        super(name, orientation);
    }

    public PropPanelModelElement(String name, ImageIcon icon, int columns) {
        super(name, icon, columns);
    }

    /**
     * Constructor that is used if no other proppanel can be found for a modelelement
     * of some kind. Since this is the default
     */
    public PropPanelModelElement() {
        this("ModelElement", null, ConfigLoader.getTabPropsOrientation());
        addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
        addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"), getStereotypeBox()));
        addField(Argo.localize("UMLMenu", "label.namespace"), getNamespaceScroll());

        add(LabelledLayout.getSeperator());

        addField(Argo.localize("UMLMenu", "label.supplier-dependencies"), getSupplierDependencyScroll());
        addField(Argo.localize("UMLMenu", "label.client-dependencies"), getClientDependencyScroll());
        addField(Argo.localize("UMLMenu", "label.source-flows"), getSourceFlowScroll());
        addField(Argo.localize("UMLMenu", "label.target-flows"), getTargetFlowScroll());

        add(LabelledLayout.getSeperator());

        addField(Argo.localize("UMLMenu", "label.constraints"), getConstraintScroll());
        addField(Argo.localize("UMLMenu", "label.namespace-visibility"), getNamespaceVisibilityPanel());
    }

    public void navigateUp() {
        Object target = getTarget();
        if (target instanceof MModelElement) {
            MNamespace namespace = ((MModelElement) target).getNamespace();
            if (namespace != null) {
                navigateTo(namespace);
            }
        }
    }

    public void navigateNamespace() {
        Object target = getTarget();
        if (target instanceof MModelElement) {
            MModelElement elem = (MModelElement) target;
            MNamespace ns = elem.getNamespace();
            if (ns != null) {
                navigateTo(ns);
            }
        }
    }

    //
    // Pluggable Property Panel support
    //
    // THIS CLASS MUST NOT IMPLEMENT PluggablePropertyPanel.  These
    // are present to provide default implementations for any
    // property panel that extends this class.
    public PropPanel getPropertyPanel() {
        return this;
    }
    public boolean isModuleEnabled() {
        return true;
    }
    public Vector getModulePopUpActions(Vector v, Object o) {
        return null;
    }
    public boolean shutdownModule() {
        return true;
    }
    public boolean initializeModule() {
        ArgoModule.cat.debug("initializeModule()");
        return true;
    }
    public void setModuleEnabled(boolean enabled) {
    }
    public boolean inContext(Object[] o) {
        return true;
    }

    protected JScrollPane getNamespaceScroll() {
        if (namespaceScroll == null) {
            JList namespaceList = new UMLLinkedList(namespaceListModel);
            namespaceList.setVisibleRowCount(1);
            namespaceScroll = new JScrollPane(namespaceList);
        }
        return namespaceScroll;
    }

    protected JComboBox getNamespaceComboBox() {
        if (namespaceComboBox == null) {
            namespaceComboBox = new UMLSearchableComboBox(namespaceComboBoxModel, ActionSetModelElementNamespace.SINGLETON, true);
        }
        return namespaceComboBox;

    }

    protected JComboBox getStereotypeBox() {
        if (stereotypeBox == null) {
            stereotypeBox = new UMLComboBox2(stereotypeComboBoxModel, ActionSetModelElementStereotype.SINGLETON);
        }
        return stereotypeBox;
    }

    protected JScrollPane getSupplierDependencyScroll() {
        if (supplierDependencyScroll == null) {
            JList supplierDependencyList = new UMLLinkedList(new UMLModelElementSupplierDependencyListModel());
            supplierDependencyScroll = new JScrollPane(supplierDependencyList);
        }
        return supplierDependencyScroll;
    }

    protected JScrollPane getClientDependencyScroll() {
        if (clientDependencyScroll == null) {
            JList clientDependencyList = new UMLLinkedList(clientDependencyListModel);
            clientDependencyScroll = new JScrollPane(clientDependencyList);
        }
        return clientDependencyScroll;
    }

    protected JScrollPane getTargetFlowScroll() {
        if (targetFlowScroll == null) {
            JList targetFlowList = new UMLLinkedList(targetFlowListModel);
            targetFlowScroll = new JScrollPane(targetFlowList);
        }
        return targetFlowScroll;
    }

    protected JScrollPane getSourceFlowScroll() {
        if (sourceFlowScroll == null) {
            JList sourceFlowList = new UMLLinkedList(sourceFlowListModel);
            sourceFlowScroll = new JScrollPane(sourceFlowList);
        }
        return sourceFlowScroll;
    }

    protected JScrollPane getConstraintScroll() {
        if (constraintScroll == null) {
            JList constraintList = new UMLMutableLinkedList(constraintListModel, null, ActionNewModelElementConstraint.SINGLETON);
            constraintScroll = new JScrollPane(constraintList);
        }
        return constraintScroll;
    }

    protected JPanel getNamespaceVisibilityPanel() {
        if (namespaceVisibilityPanel == null) {
            namespaceVisibilityPanel = new UMLButtonPanel(new UMLElementOwnershipVisibilityButtonGroup(this));
        }
        return namespaceVisibilityPanel;
    }

    protected JCheckBox getSpecializationCheckBox() {
        if (specializationCheckBox == null) {
            specializationCheckBox = new UMLElementOwnershipSpecificationCheckBox();
        }
        return specializationCheckBox;
    }

    protected JScrollPane getElementResidenceScroll() {
        if (elementResidenceScroll == null) {
            JList elementResidenceList = new UMLLinkedList(elementResidenceListModel);
            elementResidenceScroll = new JScrollPane(elementResidenceList);
        }
        return elementResidenceScroll;
    }
    
    protected JTextField getNameTextField() {
        if (nameTextField == null) {
            nameTextField = new UMLTextField2(nameDocument);
        }
        return nameTextField;
    }
    
    /**
     * Returns the document (model) for the name. Only used for the
     * PropPanelComment.
     * @return Document
     */
    protected UMLPlainTextDocument getNameDocument() {
        return nameDocument;
    }

}
