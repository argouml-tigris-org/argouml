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

package org.argouml.uml.ui.behavior.collaborations;

import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionDeleteSingleModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelNamespace;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;

/**
 * Property panel for collaborations. This panel is not totally finished yet.
 * It is not possible at the moment to see any attributes or associations at the
 * panel except for name and stereotype. Since the other attributes are not
 * implemented correctly speaking in general terms, they are not implemented
 * in this class either.
 *
 * @author jaap.branderhorst@xs4all.nl
 */
public class PropPanelCollaboration extends PropPanelNamespace {

    /**
     * Constructor for PropPanelCollaboration.
     */
    public PropPanelCollaboration() {
        super("Collaboration", ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.stereotype"),
                getStereotypeSelector());
        addField(Translator.localize("label.namespace"),
                getNamespaceSelector());

        UMLLinkedList classifierList =
	    new UMLLinkedList(
                new UMLCollaborationRepresentedClassifierListModel());
        classifierList.setVisibleRowCount(1);
        addField(Translator.localize("label.represented-classifier"),
            new JScrollPane(classifierList));

        UMLLinkedList operationList =
	    new UMLLinkedList(
                new UMLCollaborationRepresentedOperationListModel());
        operationList.setVisibleRowCount(1);
        addField(Translator.localize("label.represented-operation"),
            new JScrollPane(operationList));

        addSeperator();

        UMLLinkedList interactionList =
	    new UMLLinkedList(new UMLCollaborationInteractionListModel());
        interactionList.setVisibleRowCount(1);
        addField(Translator.localize("label.interaction"),
            new JScrollPane(interactionList));

        UMLLinkedList constrainingList =
	    new UMLLinkedList(
                new UMLCollaborationConstrainingElementListModel());
        addField(Translator.localize("label.constraining-elements"),
            new JScrollPane(constrainingList));

        addSeperator();

        // add the owned-elements field with ClassifierRoles and AssociationRoles
        addField(Translator.localize("label.owned-elements"),
                getOwnedElementsScroll());
        
        addAction(new ActionNavigateContainerElement());
        addAction(new ActionNewStereotype());
        addAction(new ActionDeleteSingleModelElement());
    }
}
