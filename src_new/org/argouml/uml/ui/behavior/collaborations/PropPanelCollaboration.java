// Copyright (c) 1996-2001 The Regents of the University of California. All
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

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.swingext.LabelledLayout;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLTextField;
import org.argouml.uml.ui.UMLTextProperty;
import org.argouml.uml.ui.foundation.core.PropPanelNamespace;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.behavior.collaborations.MCollaboration;

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
     * @param title
     * @param icon
     * @param panelCount
     */
    public PropPanelCollaboration() {
        super("Collaboration", _collaborationIcon, ConfigLoader.getTabPropsOrientation());
        
        addField(Argo.localize("UMLMenu", "label.name"),nameField);
        addField(Argo.localize("UMLMenu", "label.stereotype"), 
            new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox));
        addField(Argo.localize("UMLMenu", "label.namespace"),namespaceScroll);
        
        UMLLinkedList classifierList = new UMLLinkedList(this, new UMLCollaborationRepresentedClassifierListModel(this));
        classifierList.setVisibleRowCount(1);   
        addField(Argo.localize("UMLMenu", "label.representedClassifier"), 
            new JScrollPane(classifierList));
        
        UMLLinkedList operationList = new UMLLinkedList(this, new UMLCollaborationRepresentedOperationListModel(this));
        operationList.setVisibleRowCount(1);
        addField(Argo.localize("UMLMenu", "label.representedOperation"), 
            new JScrollPane(operationList));
         
        add(LabelledLayout.getSeperator());
        
        UMLLinkedList interactionList = new UMLLinkedList(this, new UMLCollaborationInteractionListModel(this));
        interactionList.setVisibleRowCount(1);
        addField(Argo.localize("UMLMenu", "label.interaction"), 
            new JScrollPane(interactionList));
            
        UMLLinkedList constrainingList = new UMLLinkedList(this, new UMLCollaborationConstraintListModel(this));
        addField(Argo.localize("UMLMenu", "label.constraining-elements"), 
            new JScrollPane(constrainingList));
            
        // we do not add the ownedelements since they are not of real interest
            
    }

    /**
     * Used to determine which stereotypes are legal with a collaboration. At 
     * the moment, only the stereotypes of namespace and generlizable elements
     * are shown.
     * @see org.argouml.uml.ui.PropPanel#isAcceptibleBaseMetaClass(String)
     */
    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Namespace") || 
            baseClass.equals("GeneralizableElement");
    }

}
