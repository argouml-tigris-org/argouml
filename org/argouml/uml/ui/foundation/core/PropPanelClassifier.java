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

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.argouml.i18n.Translator;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.swingext.GridLayout2;
import org.argouml.swingext.Orientation;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewReception;

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
     * Add an operation to the classifier.
     */
    public void addOperation() {
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isAClassifier(target)) {
            Object newOper =
                UmlFactory.getFactory().getCore().buildOperation(
                    /*(MClassifier)*/ target);
            TargetManager.getInstance().setTarget(newOper);
        }
    }       

    /**
     * Add an attribute to the classifier.
     */
    public void addAttribute() {
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isAClassifier(target)) {
            Object cls = /*(MClassifier)*/ target;
            Object attr =
                UmlFactory.getFactory().getCore().buildAttribute(cls);
            TargetManager.getInstance().setTarget(attr);
        }
    }

    /**
     * Initialize the panel with the common fields and stuff.
     */
    private void initialize() { 

        modifiersPanel =
            new JPanel(new GridLayout2()); 
        modifiersPanel.setBorder(
                new TitledBorder(Translator.localize(
                        "UMLMenu", "label.modifiers")));
        modifiersPanel.add(
            new UMLGeneralizableElementAbstractCheckBox());
        modifiersPanel.add(
            new UMLGeneralizableElementLeafCheckBox());
        modifiersPanel.add(
            new UMLGeneralizableElementRootCheckBox());
       
    }

    /**
     * Add a datatype.
     */
    public void addDataType() {
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isANamespace(target)) {
            Object ns = /*(MNamespace)*/ target;
            Object ownedElem = CoreFactory.getFactory().createDataType();
            ModelFacade.addOwnedElement(ns, ownedElem);
            TargetManager.getInstance().setTarget(ownedElem);
        }
    }

    /**
     * Returns the associationEndScroll.
     * @return JScrollPane
     */
    public JScrollPane getAssociationEndScroll() {
        if (associationEndScroll == null) {
            JList list = new UMLLinkedList(associationEndListModel);
            associationEndScroll = new JScrollPane(list);
        }
        return associationEndScroll;

    }

    /**
     * Returns the classifierInStateScroll.
     * @return JScrollPane
     */
    public JScrollPane getClassifierInStateScroll() {
        if (classifierInStateScroll == null) {
            JList list = new UMLLinkedList(classifierInStateListModel);
            classifierInStateScroll = new JScrollPane(list);
        }
        return classifierInStateScroll;
    }

    /**
     * Returns the classifierRoleScroll.
     * @return JScrollPane
     */
    public JScrollPane getClassifierRoleScroll() {
        if (classifierRoleScroll == null) {
            JList list = new UMLLinkedList(classifierRoleListModel);
            classifierRoleScroll = new JScrollPane(list);
        }
        return classifierRoleScroll;
    }

    /**
     * Returns the collaborationScroll.
     * @return JScrollPane
     */
    public JScrollPane getCollaborationScroll() {
        if (collaborationScroll == null) {
            JList list = new UMLLinkedList(collaborationListModel);
            collaborationScroll = new JScrollPane(list);
        }
        return collaborationScroll;
    }

    /**
     * Returns the createActionScroll.
     * @return JScrollPane
     */
    public JScrollPane getCreateActionScroll() {
        if (createActionScroll == null) {
            JList list = new UMLLinkedList(createActionListModel);
            createActionScroll = new JScrollPane(list);
        }
        return createActionScroll;
    }

    /**
     * Returns the featureScroll.
     * @return JScrollPane
     */
    public JScrollPane getFeatureScroll() {
        if (featureScroll == null) {
            JList list = new UMLLinkedList(featureListModel);
            featureScroll = new JScrollPane(list);
        }
        return featureScroll;
    }

    /**
     * Returns the generalizationScroll.
     * @return JScrollPane
     */
    public JScrollPane getGeneralizationScroll() {
        if (generalizationScroll == null) {
            JList list = new UMLLinkedList(generalizationListModel);
            generalizationScroll = new JScrollPane(list);
        }
        return generalizationScroll;
    }

    /**
     * Returns the instanceScroll.
     * @return JScrollPane
     */
    public JScrollPane getInstanceScroll() {
        if (instanceScroll == null) {
            JList list = new UMLLinkedList(instanceListModel);
            instanceScroll = new JScrollPane(list);
        }
        return instanceScroll;
    }

    /**
     * Returns the objectFlowStateScroll.
     * @return JScrollPane
     */
    public JScrollPane getObjectFlowStateScroll() {
        if (objectFlowStateScroll == null) {
            JList list = new UMLLinkedList(objectFlowStateListModel);
            objectFlowStateScroll = new JScrollPane(list);
        }
        return objectFlowStateScroll;
    }

    /**
     * Returns the parameterScroll.
     * @return JScrollPane
     */
    public JScrollPane getParameterScroll() {
        if (parameterScroll == null) {
            JList list = new UMLLinkedList(parameterListModel);
            parameterScroll = new JScrollPane(list);
        }
        return parameterScroll;
    }

    /**
     * Returns the participantScroll.
     * @return JScrollPane
     */
    public JScrollPane getParticipantScroll() {
        if (participantScroll == null) {
            JList list = new UMLLinkedList(participantListModel);
            participantScroll = new JScrollPane(list);
        }
        return participantScroll;
    }

    /**
     * Returns the powerTypeRangeScroll.
     * @return JScrollPane
     */
    public JScrollPane getPowerTypeRangeScroll() {
        if (powerTypeRangeScroll == null) {
            JList list = new UMLLinkedList(powertypeRangeListModel);
            powerTypeRangeScroll = new JScrollPane(list);
        }
        return powerTypeRangeScroll;
    }

    /**
     * Returns the specializationScroll.
     * @return JScrollPane
     */
    public JScrollPane getSpecializationScroll() {
        if (specializationScroll == null) {
            JList list = new UMLLinkedList(specializationListModel);
            specializationScroll = new JScrollPane(list);
        }
        return specializationScroll;
    }
        
    /**
     * @return the action for a new reception
     */
    protected ActionNewReception getActionNewReception() {
        return actionNewReception;
    }

    /**
     * Returns the structuralFeatureScroll.
     * @return JScrollPane
     */
    public JScrollPane getStructuralFeatureScroll() {
        if (structuralFeatureScroll == null) {
            JList list = new UMLLinkedList(structuralFeatureListModel);
            structuralFeatureScroll = new JScrollPane(list);
        }
        return structuralFeatureScroll;
    }
    
    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {       
        super.targetSet(e);       
        Object target = TargetManager.getInstance().getModelTarget();
        getActionNewReception().putValue(ActionNewReception.CLASSIFIER, target);
    }

    /**
     * @return Returns the modifiersPanel.
     */
    protected JPanel getModifiersPanel() {
        return modifiersPanel;
    }
    

} /* end class PropPanelClassifier */