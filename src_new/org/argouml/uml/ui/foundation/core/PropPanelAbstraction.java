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

package org.argouml.uml.ui.foundation.core;

import org.argouml.application.api.Argo;
import org.argouml.swingext.LabelledLayout;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLTextField2;
import org.argouml.util.ConfigLoader;

public class PropPanelAbstraction extends PropPanelDependency {

  ////////////////////////////////////////////////////////////////
  // constructors

    public PropPanelAbstraction() {
        super("Abstraction", ConfigLoader.getTabPropsOrientation());
        
        addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
        addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"), getStereotypeBox()));
        addField(Argo.localize("UMLMenu", "label.namespace"), getNamespaceComboBox());

        add(LabelledLayout.getSeperator());

        addField(Argo.localize("UMLMenu", "label.suppliers"), _supplierScroll);
        addField(Argo.localize("UMLMenu", "label.clients"), _clientScroll);

        // TODO: add Mapping
        new PropPanelButton(this, buttonPanel, _navUpIcon, Argo.localize("UMLMenu", "button.go-up"), "navigateNamespace", null);
        new PropPanelButton(this, buttonPanel, _navBackIcon, Argo.localize("UMLMenu", "button.go-back"), "navigateBackAction", "isNavigateBackEnabled");
        new PropPanelButton(this, buttonPanel, _navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"), "navigateForwardAction", "isNavigateForwardEnabled");
        new PropPanelButton(this, buttonPanel, _deleteIcon, Argo.localize("UMLMenu", "button.delete-association"), "removeElement", null);

    }

} /* end class PropPanelAbstraction */

