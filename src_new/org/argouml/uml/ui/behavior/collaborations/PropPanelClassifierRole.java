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

// File: PropPanelClassifierRole.java
// Classes: PropPanelClassifierRole
// Original Author: agauthie@ics.uci.edu
// $Id$

package org.argouml.uml.ui.behavior.collaborations;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;

import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLList;
import org.argouml.uml.ui.UMLMultiplicityComboBox;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelClassifier;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.behavior.collaborations.MClassifierRole;

/**
 * TODO: this property panel needs refactoring to remove dependency on
 *       old gui components.
 */
public class PropPanelClassifierRole extends PropPanelClassifier {


    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelClassifierRole() {
	super("ClassifierRole", ConfigLoader.getTabPropsOrientation());

	Class mclass = MClassifierRole.class;

	addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
	addField(Argo.localize("UMLMenu", "label.stereotype"),
		 new UMLComboBoxNavigator(this,
					  Argo.localize("UMLMenu",
							"tooltip.nav-stereo"),
					  getStereotypeBox()));
	addField(Argo.localize("UMLMenu", "label.namespace"),
		 getNamespaceScroll());

	addField(Argo.localize("UMLMenu", "label.multiplicity"),
		 new UMLMultiplicityComboBox(this,
					     mclass));

	JList baseList =
	    new UMLMutableLinkedList(new UMLClassifierRoleBaseListModel(),
				     ActionAddClassifierRoleBase.SINGLETON,
				     null,
				     ActionRemoveClassifierRoleBase.SINGLETON,
				     false);
	addField(Argo.localize("UMLMenu", "label.base"),
		 new JScrollPane(baseList));


	addSeperator();

	addField(Argo.localize("UMLMenu", "label.generalizations"),
		 getGeneralizationScroll());
	addField(Argo.localize("UMLMenu", "label.specializations"),
		 getSpecializationScroll());

	JList connectList =
	    new UMLList(new UMLClassifierRoleAssociationRoleListModel(this,
								      null,
								      true),
			true);
	addField(Argo.localize("UMLMenu", "label.associationrole-ends"),
		 getAssociationEndScroll());

	addSeperator();

	JList availableContentsList =
	    new UMLLinkedList(new UMLClassifierRoleAvailableContentsListModel());
	addField(Argo.localize("UMLMenu", "label.available-contents"),
		 new JScrollPane(availableContentsList));

	JList availableFeaturesList =
	    new UMLLinkedList(new UMLClassifierRoleAvailableFeaturesListModel());
	addField(Argo.localize("UMLMenu", "label.available-features"),
		 new JScrollPane(availableFeaturesList));

	new PropPanelButton(this, buttonPanel, _navUpIcon,
			    Argo.localize("UMLMenu",
					  "button.go-up"),
			    "navigateUp",
			    null);
	new PropPanelButton(this, buttonPanel, _deleteIcon,
			    localize("Delete"),
			    "removeElement",
			    null);
    }




} /* end class PropPanelClassifierRole */

