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

package org.argouml.uml.ui.behavior.collaborations;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;

import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelAssociation;
import org.argouml.util.ConfigLoader;

public class PropPanelAssociationRole extends PropPanelAssociation {

    ////////////////////////////////////////////////////////////////
    // attributes
    protected JComboBox _baseField;

    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelAssociationRole() {
        super("Association Role", ConfigLoader.getTabPropsOrientation());

        addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
        addField(Argo.localize("UMLMenu", "label.stereotype"),
		 getStereotypeBox());
        addField(Argo.localize("UMLMenu", "label.namespace"),
		 getNamespaceScroll());

        JComboBox baseComboBox =
	    new UMLComboBox2(new UMLAssociationRoleBaseComboBoxModel(), ActionSetAssociationRoleBase.SINGLETON);
        addField(Argo.localize("UMLMenu", "label.base"), baseComboBox);

        addSeperator();

        JList assocEndList =
	    new UMLLinkedList(new UMLAssociationRoleAssociationEndRoleListModel());
	// only binary associationroles are allowed
        assocEndList.setVisibleRowCount(2);
        addField(Argo.localize("UMLMenu", "label.associationrole-ends"),
		 new JScrollPane(assocEndList));

        JList messageList =
	    new UMLLinkedList(new UMLAssociationRoleMessageListModel());
        addField(Argo.localize("UMLMenu", "label.messages"),
		 new JScrollPane(messageList));

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

} /* end class PropPanelAssociationRole */
