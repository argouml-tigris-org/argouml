// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import org.argouml.i18n.Translator;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.ActionAddMessage;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelAssociation;
import org.argouml.util.ConfigLoader;

/**
 * The properties panel for an AssociationRole.
 *
 */
public class PropPanelAssociationRole extends PropPanelAssociation {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = 7693759162647306494L;

    /**
     * Construct a property panel for an AssociationRole.
     */
    public PropPanelAssociationRole() {
        super("Association Role", ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.namespace"),
                getNamespaceSelector());

        JComboBox baseComboBox =
	    new UMLComboBox2(new UMLAssociationRoleBaseComboBoxModel(),
                new ActionSetAssociationRoleBase());
        addField(Translator.localize("label.base"), 
            new UMLComboBoxNavigator(
                this,
                Translator.localize("label.association.navigate.tooltip"), 
                baseComboBox));

        addSeparator();

        JList assocEndList = new UMLLinkedList(
                new UMLAssociationRoleAssociationEndRoleListModel());
	// only binary associationroles are allowed
        assocEndList.setVisibleRowCount(2);
        addField(Translator.localize("label.associationrole-ends"),
		 new JScrollPane(assocEndList));

        JList messageList =
	    new UMLLinkedList(new UMLAssociationRoleMessageListModel());
        addField(Translator.localize("label.messages"),
		 new JScrollPane(messageList));

        addAction(new ActionNavigateContainerElement());
        addAction(TargetManager.getInstance().getAddMessageAction());
        addAction(getDeleteAction());
    }

} /* end class PropPanelAssociationRole */
