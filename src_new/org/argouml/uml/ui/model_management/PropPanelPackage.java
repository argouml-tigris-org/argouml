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

import org.argouml.application.ArgoVersion;
import org.argouml.application.api.Argo;
import org.argouml.application.api.PluggablePropertyPanel;

import org.argouml.swingext.Orientation;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.foundation.core.PropPanelNamespace;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.model_management.MPackageImpl;

/** PropPanelPackage defines the Property Panel for MPackage elements.
 */
public class PropPanelPackage extends PropPanelNamespace implements PluggablePropertyPanel {

    protected PropPanelButton _stereotypeButton;

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

    public Class getClassForPanel() {
        return MPackageImpl.class;
    }

    public String getModuleName() {
        return "PropPanelPackage";
    }
    public String getModuleDescription() {
        return "Property Panel for Package";
    }
    public String getModuleAuthor() {
        return "ArgoUML Core";
    }
    public String getModuleVersion() {
        return ArgoVersion.getVersion();
    }
    public String getModuleKey() {
        return "module.propertypanel.package";
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
        // TODO: facilitate the fact that Package is a generalizable element.
        addSeperator();

        addField(Argo.localize("UMLMenu", "label.owned-elements"), getOwnedElementsScroll());

        new PropPanelButton(this, buttonPanel, _navUpIcon, Argo.localize("UMLMenu", "button.go-up"), "navigateNamespace", null);
        new PropPanelButton(this, buttonPanel, _deleteIcon, Argo.localize("UMLMenu", "button.delete-package"), "removeElement", "isRemovableElement");
    }

} /* end class PropPanelPackage */
