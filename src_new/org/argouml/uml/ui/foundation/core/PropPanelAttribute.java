// Copyright (c) 1996-2003 The Regents of the University of California. All
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

// File: PropPanelAttribute.java
// Classes: PropPanelAttribute
// Original Author: jrobbins@ics.uci.edu
// Refactored by: jaap.branderhorst@xs4all.nl
// $Id$

package org.argouml.uml.ui.foundation.core;

import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;

import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.util.ConfigLoader;

public class PropPanelAttribute extends PropPanelStructuralFeature {

	public PropPanelAttribute() {
		super("Attribute", ConfigLoader.getTabPropsOrientation());

		addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
		addField(
			Argo.localize("UMLMenu", "label.stereotype"),
			new UMLComboBoxNavigator(
				this,
				Argo.localize("UMLMenu", "tooltip.nav-stereo"),
				getStereotypeBox()));
		addField(Argo.localize("UMLMenu", "label.owner"), getOwnerScroll());
		addField(
			Argo.localize("UMLMenu", "label.multiplicity"),
			getMultiplicityComboBox());

		addSeperator();

		addField(
			Argo.localize("UMLMenu", "label.type"),
			new UMLComboBoxNavigator(
				this,
				Argo.localize("UMLMenu", "tooltip.nav-class"),
				getTypeComboBox()));

		addField(Argo.localize("UMLMenu", "label.initial-value"), new JScrollPane(new UMLLinkedList(new UMLAttributeInitialValueListModel())));
        add(new UMLModelElementVisibilityRadioButtonPanel(Argo.localize("UMLMenu", "label.visibility"), true));
        add(getChangeabilityRadioButtonPanel());
        add(getOwnerScopeCheckbox());

		new PropPanelButton(
			this,
			buttonPanel,
			_navUpIcon,
			Argo.localize("UMLMenu", "button.go-up"),
			"navigateUp",
			null);
		new PropPanelButton(
			this,
			buttonPanel,
			_addAttrIcon,
			Argo.localize("UMLMenu", "button.add-attribute"),
			"newAttribute",
			null);
		new PropPanelButton(
			this,
			buttonPanel,
			_deleteIcon,
			Argo.localize("UMLMenu", "button.delete-attribute"),
			"removeElement",
			null);
	}

	public void newAttribute() {
        Object target = getTarget();
        if (ModelFacade.getInstance().isAStructuralFeature(target)) {
            Object owner = ModelFacade.getInstance().getOwner(target);
            TargetManager.getInstance().setTarget(UmlFactory.getFactory().getCore().buildAttribute(owner));
        }

	}

} /* end class PropPanelAttribute */
