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

package org.argouml.uml.ui.foundation.extension_mechanisms;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.argouml.i18n.Translator;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsFactory;
import org.argouml.swingext.GridLayout2;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementAbstractCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementGeneralizationListModel;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementLeafCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementRootCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementSpecializationListModel;
import org.argouml.util.ConfigLoader;

/**
 * TODO: this property panel needs refactoring to remove dependency on old gui
 * components.
 */
public class PropPanelStereotype extends PropPanelModelElement {

    private static UMLGeneralizableElementSpecializationListModel specializationListModel = new UMLGeneralizableElementSpecializationListModel();

    private static UMLGeneralizableElementGeneralizationListModel generalizationListModel = new UMLGeneralizableElementGeneralizationListModel();

    private JScrollPane generalizationScroll;

    private JScrollPane specializationScroll;

    /**
     * Construct new stereotype properties tab
     */
    public PropPanelStereotype() {
        super("Stereotype", _stereotypeIcon, ConfigLoader
                .getTabPropsOrientation());

        Class mclass = (Class) ModelFacade.STEREOTYPE;

        addField(Translator.localize("UMLMenu", "label.name"),
                getNameTextField());

        
        JComboBox baseClass = new UMLComboBox2(new UMLMetaClassComboBoxModel(),
                ActionSetMetaClass.SINGLETON, false);
        addField(Translator.localize("UMLMenu", "label.base-class"), baseClass);

        addField(Translator.localize("UMLMenu", "label.namespace"),
                getNamespaceComboBox());

        JPanel _modifiersPanel = new JPanel(new GridLayout2(0, 2,
                GridLayout2.ROWCOLPREFERRED));
        _modifiersPanel.setBorder(
                new TitledBorder(Translator.localize("UMLMenu", "label.modifiers")));
        _modifiersPanel.add(new UMLGeneralizableElementAbstractCheckBox());
        _modifiersPanel.add(new UMLGeneralizableElementLeafCheckBox());
        _modifiersPanel.add(new UMLGeneralizableElementRootCheckBox());

        add(_modifiersPanel);

        addSeperator();

        addField(Translator.localize("UMLMenu", "label.generalizations"),
                getGeneralizationScroll());
        addField(Translator.localize("UMLMenu", "label.specializations"),
                getSpecializationScroll());

        new PropPanelButton(this, buttonPanel, _navUpIcon, Translator.localize(
                "UMLMenu", "button.go-up"), "navigateNamespace", null);
        new PropPanelButton(this, buttonPanel, _stereotypeIcon, Translator
                .localize("UMLMenu", "button.new-stereotype"), "newStereotype",
                null);
        new PropPanelButton(this, buttonPanel, _deleteIcon, Translator
                .localize("UMLMenu", "button.delete-stereotype"),
                "removeElement", null);
    }

    public void newStereotype() {
        Object target = getTarget();
        Object newStereo = ExtensionMechanismsFactory.getFactory()
                .buildStereotype(/* (MModelElement) */(Object) null,
                        (String) null);
        TargetManager.getInstance().setTarget(newStereo);
        /*
         * if(target instanceof MStereotype) { MNamespace ns = ((MStereotype)
         * target).getNamespace(); if(ns != null) { MStereotype newStereo =
         * ExtensionMechanismsFactory.getFactory().createStereotype();
         * ns.addOwnedElement(newStereo); navigateTo(newStereo); } }
         */
    }

    public Object getBaseClass() {
        Object baseClass = "ModelElement";
        Object target = getTarget();
        if (ModelFacade.isAStereotype(target)) {
            baseClass = ModelFacade.getBaseClass(target);
        }
        return baseClass;
    }

    public void setBaseClass(String baseClass) {
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isAStereotype(target)) {
            ModelFacade.setBaseClass(target, baseClass);
        }
    }

    /**
     * Returns the generalizationScroll.
     * 
     * @return JScrollPane
     */
    protected JScrollPane getGeneralizationScroll() {
        if (generalizationScroll == null) {
            JList list = new UMLLinkedList(generalizationListModel);
            generalizationScroll = new JScrollPane(list);
        }
        return generalizationScroll;
    }

    /**
     * Returns the specializationScroll.
     * 
     * @return JScrollPane
     */
    protected JScrollPane getSpecializationScroll() {
        if (specializationScroll == null) {
            JList list = new UMLLinkedList(specializationListModel);
            specializationScroll = new JScrollPane(list);
        }
        return specializationScroll;
    }

} /* end class PropPanelStereotype */