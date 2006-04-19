// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionDeleteSingleModelElement;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.ActionAddDataType;
import org.argouml.uml.ui.foundation.core.ActionAddEnumeration;
import org.argouml.uml.ui.foundation.core.PropPanelNamespace;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementAbstractCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementGeneralizationListModel;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementLeafCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementRootCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementSpecializationListModel;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewTagDefinition;
import org.argouml.util.ConfigLoader;
import org.tigris.swidgets.Orientation;


/**
 * PropPanelPackage defines the Property Panel for Package elements.
 */
public class PropPanelPackage extends PropPanelNamespace  {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = -699491324617952412L;
   
    private JPanel modifiersPanel;
    private JScrollPane generalizationScroll;
    private JScrollPane specializationScroll;

    private static UMLGeneralizableElementGeneralizationListModel
        generalizationListModel =
            new UMLGeneralizableElementGeneralizationListModel();
    private static UMLGeneralizableElementSpecializationListModel
        specializationListModel =
            new UMLGeneralizableElementSpecializationListModel();

    /**
     * Construct a default property panel for UML Package elements.
     */
    public PropPanelPackage() {
        this("Package", lookupIcon("Package"),
                ConfigLoader.getTabPropsOrientation());
    }

    /**
     * Construct a property panel for UML Packages with the given parameters.
     * 
     * @param title the title for this panel
     * @param orientation the orientation
     * @param icon the icon to show next to the title
     */
    public PropPanelPackage(String title, ImageIcon icon,
            Orientation orientation) {
        super(title, icon, orientation);
        placeElements();
    }

    /**
     * Via this method, the GUI elements are added to the proppanel. Subclasses
     * should override to place the elements the way they want.
     */
    protected void placeElements() {
        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.namespace"),
                getNamespaceSelector());

        add(getNamespaceVisibilityPanel());

        add(getModifiersPanel());
        
        addSeparator();
        
        addField(Translator.localize("label.generalizations"),
                getGeneralizationScroll());
        addField(Translator.localize("label.specializations"),
                getSpecializationScroll());
        
        addSeparator();
        
        addField(Translator.localize("label.owned-elements"),
                getOwnedElementsScroll());

        JList importList =
            new UMLMutableLinkedList(new UMLClassifierPackageImportsListModel(),
                new ActionAddPackageImport(),
                null,
                new ActionRemovePackageImport(),
                true);
        addField(Translator.localize("label.imported-elements"),
                new JScrollPane(importList));

        addAction(new ActionNavigateNamespace());
        addAction(new ActionAddPackage());
        addAction(new ActionAddDataType());
        addAction(new ActionAddEnumeration());
        addAction(new ActionNewStereotype());
        addAction(new ActionNewTagDefinition());
        addAction(new ActionDeleteSingleModelElement());
    }

    /**
     * Returns the Modifiers panel.
     * 
     * @return a panel with modifiers
     */
    public JPanel getModifiersPanel() {
        if (modifiersPanel == null) {
            modifiersPanel = createBorderPanel(Translator.localize(
                "label.modifiers"));
            modifiersPanel.add(
                    new UMLGeneralizableElementAbstractCheckBox());
            modifiersPanel.add(
                    new UMLGeneralizableElementLeafCheckBox());
            modifiersPanel.add(
                    new UMLGeneralizableElementRootCheckBox());
        }
        return modifiersPanel;
    }
    
    /**
     * Returns the generalizationScroll.
     * @return JScrollPane
     */
    public JScrollPane getGeneralizationScroll() {
        if (generalizationScroll == null) {
            JList list = new UMLLinkedList(generalizationListModel);
            generalizationScroll = new JScrollPane(list);
        }
        return generalizationScroll;
    }

    /**
     * Returns the specializationScroll.
     * @return JScrollPane
     */
    public JScrollPane getSpecializationScroll() {
        if (specializationScroll == null) {
            JList list = new UMLLinkedList(specializationListModel);
            specializationScroll = new JScrollPane(list);
        }
        return specializationScroll;
    }

} /* end class PropPanelPackage */
