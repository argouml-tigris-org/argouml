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

/**
 * The properties panel for a modelelement.
 *
 */
public abstract class PropPanelModelElement extends PropPanel {

    private static final Logger LOG = Logger
            .getLogger(PropPanelModelElement.class);

    private JScrollPane namespaceScroll;

    private JComboBox namespaceComboBox;

    private JComboBox stereotypeComboBox;

    private Box stereotypeBox;

    private JScrollPane supplierDependencyScroll;

    private JScrollPane clientDependencyScroll;

    private JScrollPane targetFlowScroll;

    private JScrollPane sourceFlowScroll;

    private JScrollPane constraintScroll;

    private JPanel namespaceVisibilityPanel;

    private JCheckBox specializationCheckBox;

    private JScrollPane elementResidenceScroll;

    private JTextField nameTextField;

    private UMLModelElementNamespaceComboBoxModel namespaceComboBoxModel =
	new UMLModelElementNamespaceComboBoxModel();

    private static UMLModelElementStereotypeComboBoxModel 
        stereotypeComboBoxModel =
	new UMLModelElementStereotypeComboBoxModel();

    private static UMLModelElementNamespaceListModel namespaceListModel =
	new UMLModelElementNamespaceListModel();

    private static UMLModelElementClientDependencyListModel 
        clientDependencyListModel =
	new UMLModelElementClientDependencyListModel();

    private static UMLModelElementConstraintListModel constraintListModel =
	new UMLModelElementConstraintListModel();

    private static UMLModelElementElementResidenceListModel 
        elementResidenceListModel =
	new UMLModelElementElementResidenceListModel();

    private static UMLModelElementNameDocument nameDocument =
	new UMLModelElementNameDocument();

    private static UMLModelElementSourceFlowListModel sourceFlowListModel =
	new UMLModelElementSourceFlowListModel();

    // private static UMLModelElementSupplierDependencyListModel 
    //    supplierDependencyListModel =
    //        new UMLModelElementSupplierDependencyListModel();

    private static UMLModelElementTargetFlowListModel targetFlowListModel =
	new UMLModelElementTargetFlowListModel();

    /**
     * The constructor.
     * 
     * @param name the name of the properties panel
     * @param icon the icon to be shown next to the name
     * @param orientation the orientation
     */
    public PropPanelModelElement(String name, ImageIcon icon,
            Orientation orientation) {
        super(name, icon, orientation);
    }

    /**
     * The constructor.
     * 
     * @param name the name of the properties panel
     * @param orientation the orientation
     */
    public PropPanelModelElement(String name, Orientation orientation) {
        super(name, orientation);
    }

    /**
     * Constructor that is used if no other proppanel can be found for a
     * modelelement of some kind. Since this is the default
     */
    public PropPanelModelElement() {
        this("ModelElement", null, ConfigLoader.getTabPropsOrientation());
        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.stereotype"),
		 getStereotypeBox());

        addField(Translator.localize("label.namespace"),
                getNamespaceScroll());

        addSeperator();

        addField(Translator.localize("label.supplier-dependencies"),
                getSupplierDependencyScroll());
        addField(Translator.localize("label.client-dependencies"),
                getClientDependencyScroll());
        addField(Translator.localize("label.source-flows"),
                getSourceFlowScroll());
        addField(Translator.localize("label.target-flows"),
                getTargetFlowScroll());

        addSeperator();

        addField(Translator.localize("label.constraints"),
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
     * @see org.argouml.application.api.PluggablePropertyPanel#getPropertyPanel()
     */
    public PropPanel getPropertyPanel() {
        return this;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#isModuleEnabled()
     */
    public boolean isModuleEnabled() {
        return true;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModulePopUpActions(
     *         Vector, Object)
     */
    public Vector getModulePopUpActions(Vector v, Object o) {
        return null;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#shutdownModule()
     */
    public boolean shutdownModule() {
        return true;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#initializeModule()
     */
    public boolean initializeModule() {
        LOG.debug("initializeModule()");
        return true;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#setModuleEnabled(boolean)
     */
    public void setModuleEnabled(boolean enabled) {
    }

    /**
     * @see org.argouml.application.api.Pluggable#inContext(Object[])
     */
    public boolean inContext(Object[] o) {
        return true;
    }

    /**
     * @return a scrollpane for the namespace
     */
    protected JScrollPane getNamespaceScroll() {
        if (namespaceScroll == null) {
            JList namespaceList = new UMLLinkedList(namespaceListModel);
            namespaceList.setVisibleRowCount(1);
            namespaceScroll = new JScrollPane(namespaceList);
        }
        return namespaceScroll;
    }

    /**
     * @return a combobox for the namespace
     */
    protected JComboBox getNamespaceComboBox() {
        if (namespaceComboBox == null) {
            namespaceComboBox = new UMLSearchableComboBox(
                    namespaceComboBoxModel,
                    ActionSetModelElementNamespace.getInstance(), true);
        }
        return namespaceComboBox;

    }

    /**
     * @return a combobox for the stereotype
     */
    protected JComboBox getStereotypeComboBox() {
        if (stereotypeComboBox == null) {
            stereotypeComboBox = new UMLComboBox2(stereotypeComboBoxModel,
                    ActionSetModelElementStereotype.getInstance());
        }
        return stereotypeComboBox;
    }

    /**
     * Returns the stereotype box. This is a box with a combobox to select the
     * stereotype and a button to create a new one
     * 
     * @return the stereotype box
     */
    protected Box getStereotypeBox() {
        if (stereotypeBox == null) {
            stereotypeBox = new Box(BoxLayout.X_AXIS);
            stereotypeBox.add(new UMLComboBoxNavigator(this, Translator
                    .localize("tooltip.nav-stereo"),
                    getStereotypeComboBox()));
            JButton stereoTypeButton = new JButton(new AbstractAction(null,
                    lookupIcon("Stereotype")) {

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
            stereotypeBox.add(stereoTypeButton);
        }
        return stereotypeBox;
    }

    /**
     * @return a scrollpane for supplier dependency
     */
    protected JScrollPane getSupplierDependencyScroll() {
        if (supplierDependencyScroll == null) {
            JList supplierDependencyList = new UMLLinkedList(
                    new UMLModelElementSupplierDependencyListModel());
            supplierDependencyScroll = new JScrollPane(supplierDependencyList);
        }
        return supplierDependencyScroll;
    }

    /**
     * @return a scrollpane for client dependency
     */
    protected JScrollPane getClientDependencyScroll() {
        if (clientDependencyScroll == null) {
            JList clientDependencyList = new UMLLinkedList(
                    clientDependencyListModel);
            clientDependencyScroll = new JScrollPane(clientDependencyList);
        }
        return clientDependencyScroll;
    }

    /**
     * @return a scrollpane for target flow
     */
    protected JScrollPane getTargetFlowScroll() {
        if (targetFlowScroll == null) {
            JList targetFlowList = new UMLLinkedList(targetFlowListModel);
            targetFlowScroll = new JScrollPane(targetFlowList);
        }
        return targetFlowScroll;
    }

    /**
     * @return a scrollpane for source flow
     */
    protected JScrollPane getSourceFlowScroll() {
        if (sourceFlowScroll == null) {
            JList sourceFlowList = new UMLLinkedList(sourceFlowListModel);
            sourceFlowScroll = new JScrollPane(sourceFlowList);
        }
        return sourceFlowScroll;
    }

    /**
     * @return a scrollpane for constraints
     */
    protected JScrollPane getConstraintScroll() {
        if (constraintScroll == null) {
            JList constraintList = new UMLMutableLinkedList(
                    constraintListModel, null,
                    ActionNewModelElementConstraint.getInstance());
            constraintScroll = new JScrollPane(constraintList);
        }
        return constraintScroll;
    }

    /**
     * @return a panel for the visibility
     */
    protected JPanel getNamespaceVisibilityPanel() {
        if (namespaceVisibilityPanel == null) {
            namespaceVisibilityPanel =
		new UMLModelElementVisibilityRadioButtonPanel(
                    Translator.localize("label.visibility"), true);
        }
        return namespaceVisibilityPanel;
    }

    /**
     * @return a checkbox for the specialization
     */
    protected JCheckBox getSpecializationCheckBox() {
        if (specializationCheckBox == null) {
            specializationCheckBox =
		new UMLElementOwnershipSpecificationCheckBox();
        }
        return specializationCheckBox;
    }

    /**
     * @return a scrollpane for residence
     */
    protected JScrollPane getElementResidenceScroll() {
        if (elementResidenceScroll == null) {
            JList elementResidenceList = new UMLLinkedList(
                    elementResidenceListModel);
            elementResidenceScroll = new JScrollPane(elementResidenceList);
        }
        return elementResidenceScroll;
    }

    /**
     * @return a textfield for the name
     */
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
    protected static ImageIcon lookupIcon(String name) {
        return ResourceLoaderWrapper
                .lookupIconResource(name);
    }
}
