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
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.argouml.application.api.ArgoModule;
import org.argouml.application.api.Pluggable;
import org.argouml.application.api.PluggablePropertyPanel;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.uml.UmlHelper;
import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsFactory;
import org.argouml.swingext.Orientation;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.UMLPlainTextDocument;
import org.argouml.uml.ui.UMLSearchableComboBox;
import org.argouml.uml.ui.UMLTextField2;
import org.argouml.util.ConfigLoader;

public abstract class PropPanelModelElement extends PropPanel {

    private static final Logger LOG = Logger
            .getLogger(PropPanelModelElement.class);

    ////////////////////////////////////////////////////////////////
    // constants

    protected static ImageIcon _partitionIcon = lookupIcon("Partition");

    protected static ImageIcon _objectIcon = lookupIcon("Object");

    protected static ImageIcon _componentInstanceIcon = lookupIcon("ComponentInstance");

    protected static ImageIcon _nodeInstanceIcon = lookupIcon("NodeInstance");

    protected static ImageIcon _instanceIcon = lookupIcon("Instance");

    protected static ImageIcon _linkIcon = lookupIcon("Link");

    protected static ImageIcon _stimulusIcon = lookupIcon("Stimulus");

    protected static ImageIcon _associationIcon = lookupIcon("Association");

    protected static ImageIcon _assocEndIcon = lookupIcon("AssociationEnd");

    protected static ImageIcon _assocEndRoleIcon = lookupIcon("AssociationEndRole");

    protected static ImageIcon _generalizationIcon = lookupIcon("Generalization");

    protected static ImageIcon _realizationIcon = lookupIcon("Realization");

    protected static ImageIcon _classIcon = lookupIcon("Class");

    protected static ImageIcon _collaborationIcon = lookupIcon("Collaboration");

    protected static ImageIcon _interfaceIcon = lookupIcon("Interface");

    protected static ImageIcon _addOpIcon = lookupIcon("NewOperation");

    protected static ImageIcon _addAttrIcon = lookupIcon("NewAttribute");

    protected static ImageIcon _addAssocIcon = lookupIcon("Association");

    protected static ImageIcon _packageIcon = lookupIcon("Package");

    protected static ImageIcon _modelIcon = lookupIcon("Model");
    
    protected static ImageIcon _subsystemIcon = lookupIcon("Subsystem");

    protected static ImageIcon _innerClassIcon = lookupIcon("InnerClass");

    protected static ImageIcon _nodeIcon = lookupIcon("Node");

    protected static ImageIcon _componentIcon = lookupIcon("Component");

    protected static ImageIcon _dataTypeIcon = lookupIcon("DataType");

    protected static ImageIcon _actorIcon = lookupIcon("Actor");

    protected static ImageIcon _useCaseIcon = lookupIcon("UseCase");

    protected static ImageIcon _extendIcon = lookupIcon("Extend");

    protected static ImageIcon _extensionPointIcon = lookupIcon("ExtensionPoint");

    protected static ImageIcon _includeIcon = lookupIcon("Include");

    protected static ImageIcon _dependencyIcon = lookupIcon("Dependency");

    protected static ImageIcon _permissionIcon = lookupIcon("Permission");

    protected static ImageIcon _usageIcon = lookupIcon("Usage");

    protected static ImageIcon _parameterIcon = lookupIcon("Parameter");

    protected static ImageIcon _operationIcon = lookupIcon("Operation");

    protected static ImageIcon _signalIcon = lookupIcon("SignalSending");

    protected static ImageIcon _stereotypeIcon = lookupIcon("Stereotype");

    protected static ImageIcon _guardIcon = lookupIcon("Guard");

    protected static ImageIcon _transitionIcon = lookupIcon("Transition");

    protected static ImageIcon _classifierRoleIcon = lookupIcon("ClassifierRole");

    protected static ImageIcon _associationRoleIcon = lookupIcon("AssociationRole");

    protected static ImageIcon _callActionIcon = lookupIcon("CallAction");

    protected static ImageIcon _eventIcon = lookupIcon("Event");

    protected static ImageIcon _interactionIcon = lookupIcon("Interaction");

    // added next one so someone can change the icon independant of callaction
    protected static ImageIcon _actionIcon = lookupIcon("CallAction");

    protected static ImageIcon _receptionIcon = lookupIcon("Reception");

    protected static ImageIcon _commentIcon = lookupIcon("Note");

    protected static ImageIcon _messageIcon = lookupIcon("Message");

    protected static ImageIcon _flowIcon = lookupIcon("Flow");

    protected static ImageIcon _stateMachineIcon = lookupIcon("StateMachine");

    private JScrollPane namespaceScroll;

    private JComboBox namespaceComboBox;

    private JComboBox stereotypeComboBox;

    private Box _stereotypeBox;

    private JScrollPane supplierDependencyScroll;

    private JScrollPane clientDependencyScroll;

    private JScrollPane targetFlowScroll;

    private JScrollPane sourceFlowScroll;

    private JScrollPane constraintScroll;

    private JPanel namespaceVisibilityPanel;

    private JCheckBox specializationCheckBox;

    private JScrollPane elementResidenceScroll;

    private JTextField nameTextField;

    private UMLModelElementNamespaceComboBoxModel namespaceComboBoxModel = new UMLModelElementNamespaceComboBoxModel();

    private static UMLModelElementStereotypeComboBoxModel stereotypeComboBoxModel = new UMLModelElementStereotypeComboBoxModel();

    private static UMLModelElementNamespaceListModel namespaceListModel = new UMLModelElementNamespaceListModel();

    private static UMLModelElementClientDependencyListModel clientDependencyListModel = new UMLModelElementClientDependencyListModel();

    private static UMLModelElementConstraintListModel constraintListModel = new UMLModelElementConstraintListModel();

    private static UMLModelElementElementResidenceListModel elementResidenceListModel = new UMLModelElementElementResidenceListModel();

    private static UMLModelElementNameDocument nameDocument = new UMLModelElementNameDocument();

    private static UMLModelElementSourceFlowListModel sourceFlowListModel = new UMLModelElementSourceFlowListModel();

    private static UMLModelElementSupplierDependencyListModel supplierDependencyListModel = new UMLModelElementSupplierDependencyListModel();

    private static UMLModelElementTargetFlowListModel targetFlowListModel = new UMLModelElementTargetFlowListModel();

    public PropPanelModelElement(String name, ImageIcon icon,
            Orientation orientation) {
        super(name, icon, orientation);
    }

    public PropPanelModelElement(String name, Orientation orientation) {
        super(name, orientation);
    }

    /**
     * Constructor that is used if no other proppanel can be found for a
     * modelelement of some kind. Since this is the default
     */
    public PropPanelModelElement() {
        this("ModelElement", null, ConfigLoader.getTabPropsOrientation());
        addField(Translator.localize("UMLMenu", "label.name"),
                getNameTextField());
        addField(Translator.localize("UMLMenu", "label.stereotype"), getStereotypeBox());

        addField(Translator.localize("UMLMenu", "label.namespace"),
                getNamespaceScroll());

        addSeperator();

        addField(Translator.localize("UMLMenu", "label.supplier-dependencies"),
                getSupplierDependencyScroll());
        addField(Translator.localize("UMLMenu", "label.client-dependencies"),
                getClientDependencyScroll());
        addField(Translator.localize("UMLMenu", "label.source-flows"),
                getSourceFlowScroll());
        addField(Translator.localize("UMLMenu", "label.target-flows"),
                getTargetFlowScroll());

        addSeperator();

        addField(Translator.localize("UMLMenu", "label.constraints"),
                getConstraintScroll());
        add(getNamespaceVisibilityPanel());
    }

    /**
     * Calling this method navigates the target one level up, to the owner of
     * the current target. In most cases this navigates to the owning namespace.
     * In some cases it navigates to, for example, the owning composite state
     * for some simple state.
     */
    public void navigateUp() {
        TargetManager.getInstance().setTarget(
                UmlHelper.getHelper().getOwner(getTarget()));
    }


    //
    // Pluggable Property Panel support
    //
    // THIS CLASS MUST NOT IMPLEMENT PluggablePropertyPanel. These
    // are present to provide default implementations for any
    // property panel that extends this class.
    /**
     * @see PluggablePropertyPanel#getPropertyPanel()
     */
    public PropPanel getPropertyPanel() {
        return this;
    }

    /**
     * @see ArgoModule#isModuleEnabled()
     */
    public boolean isModuleEnabled() {
        return true;
    }

    /**
     * @see ArgoModule#getModulePopUpActions(Vector, Object)
     */
    public Vector getModulePopUpActions(Vector v, Object o) {
        return null;
    }

    /**
     * @see ArgoModule#shutdownModule()
     */
    public boolean shutdownModule() {
        return true;
    }

    /**
     * @see ArgoModule#initializeModule()
     */
    public boolean initializeModule() {
        LOG.debug("initializeModule()");
        return true;
    }

    /**
     * @see ArgoModule#setModuleEnabled(boolean)
     */
    public void setModuleEnabled(boolean enabled) {
    }

    /**
     * @see Pluggable#inContect(Object[])
     */
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
            namespaceComboBox = new UMLSearchableComboBox(
                    namespaceComboBoxModel,
                    ActionSetModelElementNamespace.SINGLETON, true);
        }
        return namespaceComboBox;

    }

    protected JComboBox getStereotypeComboBox() {
        if (stereotypeComboBox == null) {
            stereotypeComboBox = new UMLComboBox2(stereotypeComboBoxModel,
                    ActionSetModelElementStereotype.SINGLETON);
        }
        return stereotypeComboBox;
    }

    /**
     * Returns the stereotype box. This is a box with a combobox to select the
     * stereotype and a button to create a new one
     * 
     * @return
     */
    protected Box getStereotypeBox() {
        if (_stereotypeBox == null) {
            _stereotypeBox = new Box(BoxLayout.X_AXIS);
            _stereotypeBox.add(new UMLComboBoxNavigator(this, Translator
                    .localize("UMLMenu", "tooltip.nav-stereo"),
                    getStereotypeComboBox()));
            JButton stereoTypeButton = new JButton(new AbstractAction(null,
                    _stereotypeIcon) {

                public void actionPerformed(ActionEvent e) {
                    Object newTarget = ExtensionMechanismsFactory.getFactory()
                            .buildStereotype(getTarget(), null);
                    TargetManager.getInstance().setTarget(newTarget);
                }

            });
            // we don't want to 'see' the button
            // stereoTypeButton.setBorderPainted(false);
            // stereoTypeButton.setContentAreaFilled(false);
            stereoTypeButton.setSize(stereoTypeButton.getWidth() - 10,
                    stereoTypeButton.getHeight());
            //JToolBar toolbar = new JToolBar();
            //toolbar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
            //toolbar.add(stereoTypeButton);
            //_stereotypeBox.add(toolbar);
            _stereotypeBox.add(stereoTypeButton);
        }
        return _stereotypeBox;
    }

    protected JScrollPane getSupplierDependencyScroll() {
        if (supplierDependencyScroll == null) {
            JList supplierDependencyList = new UMLLinkedList(
                    new UMLModelElementSupplierDependencyListModel());
            supplierDependencyScroll = new JScrollPane(supplierDependencyList);
        }
        return supplierDependencyScroll;
    }

    protected JScrollPane getClientDependencyScroll() {
        if (clientDependencyScroll == null) {
            JList clientDependencyList = new UMLLinkedList(
                    clientDependencyListModel);
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
            JList constraintList = new UMLMutableLinkedList(
                    constraintListModel, null,
                    ActionNewModelElementConstraint.SINGLETON);
            constraintScroll = new JScrollPane(constraintList);
        }
        return constraintScroll;
    }

    protected JPanel getNamespaceVisibilityPanel() {
        if (namespaceVisibilityPanel == null) {
            namespaceVisibilityPanel = new UMLModelElementVisibilityRadioButtonPanel(
                    Translator.localize("UMLMenu", "label.visibility"), true);
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
            JList elementResidenceList = new UMLLinkedList(
                    elementResidenceListModel);
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
     * 
     * @return Document
     */
    protected UMLPlainTextDocument getNameDocument() {
        return nameDocument;
    }

    /**
     * Look up an icon.
     * 
     * @param name
     *            the resource name.
     * @return an ImageIcon corresponding to the given resource name.
     */
    private static ImageIcon lookupIcon(String name) {
        return ResourceLoaderWrapper
                .lookupIconResource(name);
    }
}