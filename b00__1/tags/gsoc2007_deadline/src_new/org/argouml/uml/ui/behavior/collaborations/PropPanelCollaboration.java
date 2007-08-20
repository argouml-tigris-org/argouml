// $Id:PropPanelCollaboration.java 13223 2007-08-03 19:38:41Z mvw $
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

import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelNamespace;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;

/**
 * Property panel for collaborations. 
 *
 * @author jaap.branderhorst@xs4all.nl
 */
public class PropPanelCollaboration extends PropPanelNamespace {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = 5642815840272293391L;

    /**
     * Construct a property panel for a Collaboration.
     */
    public PropPanelCollaboration() {
        super("Collaboration", ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.namespace"),
                getNamespaceSelector());

        // the represented classifier
        UMLComboBox2 representedClassifierComboBox =
            new UMLComboBox2(
                     new UMLCollaborationRepresentedClassifierComboBoxModel(),
                     new ActionSetRepresentedClassifierCollaboration());
        addField(Translator.localize("label.represented-classifier"),
                new UMLComboBoxNavigator(
                        Translator.localize(
                                "label.represented-classifier."
                                + "navigate.tooltip"),
                        representedClassifierComboBox));

        // the represented operation
        UMLComboBox2 representedOperationComboBox =
            new UMLComboBox2(
                     new UMLCollaborationRepresentedOperationComboBoxModel(),
                     new ActionSetRepresentedOperationCollaboration());
        addField(Translator.localize("label.represented-operation"),
                new UMLComboBoxNavigator(
                        Translator.localize(
                                "label.represented-operation."
                                + "navigate.tooltip"),
                        representedOperationComboBox));

        addSeparator();

        addField(Translator.localize("label.interaction"),
                getSingleRowScroll(new UMLCollaborationInteractionListModel()));

        UMLLinkedList constrainingList =
	    new UMLLinkedList(
                new UMLCollaborationConstrainingElementListModel());
        addField(Translator.localize("label.constraining-elements"),
            new JScrollPane(constrainingList));

        addSeparator();

        /* Add the owned-elements field 
         * with ClassifierRoles and AssociationRoles:
         */
        addField(Translator.localize("label.owned-elements"),
                getOwnedElementsScroll());
        
        addAction(new ActionNavigateContainerElement());
        addAction(new ActionNewStereotype());
        addAction(getDeleteAction());
    }
}
