// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.uml.ui.model_management;

import org.argouml.application.api.Argo;

import org.argouml.swingext.Orientation;
import org.argouml.model.uml.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.foundation.core.*;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.model_management.MPackage;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


/** PropPanelPackage defines the Property Panel for MPackage elements.
 */
public class PropPanelPackage extends PropPanelNamespace  {

    protected JPanel _modifiersPanel = new JPanel();
    protected PropPanelButton _stereotypeButton;
    protected JScrollPane _generalizationScroll;
    protected JScrollPane _specializationScroll;

    private static UMLGeneralizableElementGeneralizationListModel generalizationListModel =
        new UMLGeneralizableElementGeneralizationListModel();
    private static UMLGeneralizableElementSpecializationListModel specializationListModel =
        new UMLGeneralizableElementSpecializationListModel();

    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelPackage() {
        this("Package", ConfigLoader.getTabPropsOrientation());
    }

    /**
     * Constructor for PropPanelPackage.
     * @param title
     * @param icon
     * @param orientation
     */
    public PropPanelPackage(String title, Orientation orientation) {
        super(title, orientation);
        placeElements();
    }

    /**
     * Via this method, the GUI elements are added to the proppanel. Subclasses
     * should override to place the elements the way they want.
     */
    protected void placeElements() {
        addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
        addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"), getStereotypeBox()));
        addField(Argo.localize("UMLMenu", "label.namespace"), getNamespaceComboBox());

        addField(Argo.localize("UMLMenu", "label.visibility"), getNamespaceVisibilityPanel());

        // TODO: facilitate importedElements.
        
        _modifiersPanel.add(
                            new UMLGeneralizableElementAbstractCheckBox());
        _modifiersPanel.add(
                            new UMLGeneralizableElementLeafCheckBox());
        _modifiersPanel.add(
                            new UMLGeneralizableElementRootCheckBox());
        
        addField(Argo.localize("UMLMenu", "label.modifiers"), _modifiersPanel);
        addSeperator();
        addField(Argo.localize("UMLMenu", "label.generalizations"), getGeneralizationScroll());
        addField(Argo.localize("UMLMenu", "label.specializations"), getSpecializationScroll());
        addSeperator();
        addField(Argo.localize("UMLMenu", "label.owned-elements"), getOwnedElementsScroll());

        new PropPanelButton(this, buttonPanel, _navUpIcon, Argo.localize("UMLMenu", "button.go-up"), "navigateNamespace", null);
        new PropPanelButton(this, buttonPanel, _packageIcon, Argo.localize("UMLMenu", "button.add-package"), "addPackage", null);
        new PropPanelButton(this, buttonPanel, _deleteIcon, Argo.localize("UMLMenu", "button.delete-package"), "removeElement", "isRemovableElement");
    }


    /** add a package to the current package. */
    public void addPackage() {
        Object target = getTarget();
        if (target instanceof MPackage) {
            MPackage newPackage =  UmlFactory.getFactory().
                getModelManagement().createPackage();
            MPackage currentPackage = (MPackage) target;
            currentPackage.addOwnedElement(newPackage);
            TargetManager.getInstance().setTarget(newPackage);
        }
    }

    /**
     * Returns the generalizationScroll.
     * @return JScrollPane
     */
    public JScrollPane getGeneralizationScroll() {
        if (_generalizationScroll == null) {
            JList list = new UMLLinkedList(generalizationListModel);
            _generalizationScroll = new JScrollPane(list);
        }
        return _generalizationScroll;
    }

    /**
     * Returns the specializationScroll.
     * @return JScrollPane
     */
    public JScrollPane getSpecializationScroll() {
        if (_specializationScroll == null) {
            JList list = new UMLLinkedList(specializationListModel);
            _specializationScroll = new JScrollPane(list);
        }
        return _specializationScroll;
    }


} /* end class PropPanelPackage */
