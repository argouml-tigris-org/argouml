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

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.argouml.i18n.Translator;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.swingext.GridLayout2;
import org.argouml.swingext.Orientation;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelNamespace;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementAbstractCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementGeneralizationListModel;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementLeafCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementRootCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementSpecializationListModel;
import org.argouml.util.ConfigLoader;


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
        this("Package", _packageIcon, ConfigLoader.getTabPropsOrientation());
    }

    /**
     * Constructor for PropPanelPackage.
     * @param title
     * @param orientation
     */
    public PropPanelPackage(String title, ImageIcon icon, Orientation orientation) {
        super(title, icon, orientation);
        placeElements();
    }

    /**
     * Via this method, the GUI elements are added to the proppanel. Subclasses
     * should override to place the elements the way they want.
     */
    protected void placeElements() {
        addField(Translator.localize("UMLMenu", "label.name"), getNameTextField());
        // addField(Translator.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Translator.localize("UMLMenu", "tooltip.nav-stereo"), getStereotypeBox()));
        addField(Translator.localize("UMLMenu", "label.stereotype"), getStereotypeBox());
        addField(Translator.localize("UMLMenu", "label.namespace"), getNamespaceComboBox());

        add(getNamespaceVisibilityPanel());

        // TODO: facilitate importedElements.
        
        _modifiersPanel =
            new JPanel(new GridLayout2()); 
        _modifiersPanel.setBorder(
                new TitledBorder(Translator.localize("UMLMenu", "label.modifiers")));
        
        _modifiersPanel.add(
                            new UMLGeneralizableElementAbstractCheckBox());
        _modifiersPanel.add(
                            new UMLGeneralizableElementLeafCheckBox());
        _modifiersPanel.add(
                            new UMLGeneralizableElementRootCheckBox());
        
        add(_modifiersPanel);
        addSeperator();
        addField(Translator.localize("UMLMenu", "label.generalizations"), getGeneralizationScroll());
        addField(Translator.localize("UMLMenu", "label.specializations"), getSpecializationScroll());
        addSeperator();
        addField(Translator.localize("UMLMenu", "label.owned-elements"), getOwnedElementsScroll());

        buttonPanel.add(new PropPanelButton2(this, new ActionNavigateNamespace()));
        new PropPanelButton(this, buttonPanel, _packageIcon, Translator.localize("UMLMenu", "button.new-package"), "addPackage", null);
	buttonPanel.add(new PropPanelButton2(this, new ActionRemoveFromModel()));
    }


    /** add a package to the current package. */
    public void addPackage() {
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isAPackage(target)) {
            Object/*MPackage*/ newPackage =  UmlFactory.getFactory().
                getModelManagement().createPackage();
            Object/*MPackage*/ currentPackage = target;
            ModelFacade.addOwnedElement(currentPackage, newPackage);
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