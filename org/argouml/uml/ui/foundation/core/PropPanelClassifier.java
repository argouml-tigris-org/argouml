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

package org.argouml.uml.ui.foundation.core;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ScrollList;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewReception;
import org.tigris.swidgets.Orientation;

/**
 * The abstract properties panel for Classifiers.
 *
 */
public abstract class PropPanelClassifier extends PropPanelNamespace {

    private JPanel modifiersPanel;

    /**
     * The action used to add a reception to the classifier.
     */
    private ActionNewReception actionNewReception = new ActionNewReception();

    private JScrollPane generalizationScroll;
    private JScrollPane specializationScroll;
    private JScrollPane featureScroll;
    private JScrollPane participantScroll;
    private JScrollPane createActionScroll;
    private JScrollPane instanceScroll;
    private JScrollPane collaborationScroll;
    private JScrollPane classifierRoleScroll;
    private JScrollPane classifierInStateScroll;
    private JScrollPane objectFlowStateScroll;
    private JScrollPane powerTypeRangeScroll;
    private JScrollPane associationEndScroll;
    private JScrollPane parameterScroll;
    private JScrollPane structuralFeatureScroll;
    private JScrollPane attributeScroll;
    private JScrollPane operationScroll;

    // all GUI models that can be singletons and
    // that are being used in subclasses
    // implemented as static (singleton) instances so that only one model is
    // registered for some modelevent and not an instance per proppanel.

    private static UMLGeneralizableElementGeneralizationListModel
        generalizationListModel =
            new UMLGeneralizableElementGeneralizationListModel();
    private static UMLGeneralizableElementSpecializationListModel
        specializationListModel =
            new UMLGeneralizableElementSpecializationListModel();
    private static UMLClassifierFeatureListModel featureListModel =
        new UMLClassifierFeatureListModel();
    private static UMLClassifierParticipantListModel participantListModel =
        new UMLClassifierParticipantListModel();
    private static UMLClassifierCreateActionListModel createActionListModel =
        new UMLClassifierCreateActionListModel();
    private static UMLClassifierInstanceListModel instanceListModel =
        new UMLClassifierInstanceListModel();
    private static UMLClassifierCollaborationListModel collaborationListModel =
        new UMLClassifierCollaborationListModel();
    private static UMLClassifierClassifierRoleListModel
        classifierRoleListModel =
            new UMLClassifierClassifierRoleListModel();
    private static UMLClassifierClassifierInStateListModel
        classifierInStateListModel =
            new UMLClassifierClassifierInStateListModel();
    private static UMLClassifierObjectFlowStateListModel
        objectFlowStateListModel =
            new UMLClassifierObjectFlowStateListModel();
    private static UMLClassifierPowertypeRangeListModel
        powertypeRangeListModel =
            new UMLClassifierPowertypeRangeListModel();
    private static UMLClassifierAssociationEndListModel
        associationEndListModel =
            new UMLClassifierAssociationEndListModel();
    private static UMLClassifierParameterListModel parameterListModel =
        new UMLClassifierParameterListModel();
    private static UMLClassifierStructuralFeatureListModel
        structuralFeatureListModel =
            new UMLClassifierStructuralFeatureListModel();
    private static UMLClassAttributeListModel attributeListModel =
        new UMLClassAttributeListModel();
    private static UMLClassOperationListModel operationListModel =
        new UMLClassOperationListModel();

    /**
     * The constructor.
     *
     * @param title the title of the properties panel
     * @param orientation the orientation of the panel
     */
    public PropPanelClassifier(String title, Orientation orientation) {
        super(title, orientation);
        initialize();
    }

    /**
     * The constructor.
     *
     * @param name the title of the properties panel
     * @param orientation the orientation of the panel
     * @param icon the icon shown next to the name
     */
    public PropPanelClassifier(String name, ImageIcon icon,
            Orientation orientation) {
        super(name, icon, orientation);
        initialize();
    }

    /**
     * Initialize the panel with the common fields and stuff.
     */
    private void initialize() {

        modifiersPanel =createBorderPanel(Translator.localize("label.modifiers"));
        modifiersPanel.add(
            new UMLGeneralizableElementAbstractCheckBox());
        modifiersPanel.add(
            new UMLGeneralizableElementLeafCheckBox());
        modifiersPanel.add(
            new UMLGeneralizableElementRootCheckBox());

    }

    /**
     * Returns the associationEndScroll.
     * @return JScrollPane
     */
    public JScrollPane getAssociationEndScroll() {
        if (associationEndScroll == null) {
            associationEndScroll = new ScrollList(associationEndListModel);
        }
        return associationEndScroll;

    }

    /**
     * Returns the classifierInStateScroll.
     * @return JScrollPane
     */
    public JScrollPane getClassifierInStateScroll() {
        if (classifierInStateScroll == null) {
            classifierInStateScroll = 
                new ScrollList(classifierInStateListModel);
        }
        return classifierInStateScroll;
    }

    /**
     * Returns the classifierRoleScroll.
     * @return JScrollPane
     */
    public JScrollPane getClassifierRoleScroll() {
        if (classifierRoleScroll == null) {
            classifierRoleScroll = new ScrollList(classifierRoleListModel);
        }
        return classifierRoleScroll;
    }

    /**
     * Returns the collaborationScroll.
     * @return JScrollPane
     */
    public JScrollPane getCollaborationScroll() {
        if (collaborationScroll == null) {
            collaborationScroll = new ScrollList(collaborationListModel);
        }
        return collaborationScroll;
    }

    /**
     * Returns the createActionScroll.
     * @return JScrollPane
     */
    public JScrollPane getCreateActionScroll() {
        if (createActionScroll == null) {
            createActionScroll = new ScrollList(createActionListModel);
        }
        return createActionScroll;
    }

    /**
     * Returns the featureScroll.
     * @return JScrollPane
     */
    public JScrollPane getFeatureScroll() {
        if (featureScroll == null) {
            featureScroll = new ScrollList(featureListModel);
        }
        return featureScroll;
    }

    /**
     * Returns the generalizationScroll.
     * @return JScrollPane
     */
    public JScrollPane getGeneralizationScroll() {
        if (generalizationScroll == null) {
            generalizationScroll = new ScrollList(generalizationListModel);
        }
        return generalizationScroll;
    }

    /**
     * Returns the instanceScroll.
     * @return JScrollPane
     */
    public JScrollPane getInstanceScroll() {
        if (instanceScroll == null) {
            instanceScroll = new ScrollList(instanceListModel);
        }
        return instanceScroll;
    }

    /**
     * Returns the objectFlowStateScroll.
     * @return JScrollPane
     */
    public JScrollPane getObjectFlowStateScroll() {
        if (objectFlowStateScroll == null) {
            objectFlowStateScroll = new ScrollList(objectFlowStateListModel);
        }
        return objectFlowStateScroll;
    }

    /**
     * Returns the parameterScroll.
     * @return JScrollPane
     */
    public JScrollPane getParameterScroll() {
        if (parameterScroll == null) {
            parameterScroll = new ScrollList(parameterListModel);
        }
        return parameterScroll;
    }

    /**
     * Returns the participantScroll.
     * @return JScrollPane
     */
    public JScrollPane getParticipantScroll() {
        if (participantScroll == null) {
            participantScroll = new ScrollList(participantListModel);
        }
        return participantScroll;
    }

    /**
     * Returns the powerTypeRangeScroll.
     * @return JScrollPane
     */
    public JScrollPane getPowerTypeRangeScroll() {
        if (powerTypeRangeScroll == null) {
            powerTypeRangeScroll = new ScrollList(powertypeRangeListModel);
        }
        return powerTypeRangeScroll;
    }

    /**
     * Returns the specializationScroll.
     * @return JScrollPane
     */
    public JScrollPane getSpecializationScroll() {
        if (specializationScroll == null) {
            specializationScroll = new ScrollList(specializationListModel);
        }
        
        return specializationScroll;
    }

    /**
     * Returns the structuralFeatureScroll.
     * @return JScrollPane
     */
    public JScrollPane getStructuralFeatureScroll() {
        if (structuralFeatureScroll == null) {
            structuralFeatureScroll = 
                new ScrollList(structuralFeatureListModel);
        }
        return structuralFeatureScroll;
    }

    /**
     * Returns the attributeScroll.
     *
     * @return JScrollPane
     */
    public JScrollPane getAttributeScroll() {
        if (attributeScroll == null) {
            attributeScroll = new ScrollList(attributeListModel);
        }
        return attributeScroll;
    }

    /**
     * Returns the operationScroll.
     *
     * @return JScrollPane
     */
    public JScrollPane getOperationScroll() {
        if (operationScroll == null) {
            operationScroll = new ScrollList(operationListModel);
        }
        return operationScroll;
    }

    /**
     * @return the action for a new reception
     */
    protected ActionNewReception getActionNewReception() {
        return actionNewReception;
    }

    /**
     * @return Returns the modifiersPanel.
     */
    protected JPanel getModifiersPanel() {
        return modifiersPanel;
    }


} /* end class PropPanelClassifier */
