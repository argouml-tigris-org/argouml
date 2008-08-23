// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.core.propertypanels.ui;

import org.argouml.uml.ui.behavior.state_machines.*;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.argouml.core.propertypanels.panel.UIFactory;
import org.argouml.core.propertypanels.panel.XMLPropPanelFactory;
import org.argouml.core.propertypanels.xml.XMLPropertyPanelsData;
import org.argouml.core.propertypanels.xml.XMLPropertyPanelsDataRecord;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.ScrollList;
import org.argouml.uml.ui.UMLCheckBox2;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLDerivedCheckBox;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.UMLPlainTextDocument;
import org.argouml.uml.ui.UMLRadioButtonPanel;
import org.argouml.uml.ui.UMLSearchableComboBox;
import org.argouml.uml.ui.UMLSingleRowSelector;
import org.argouml.uml.ui.UMLTextArea2;
import org.argouml.uml.ui.UMLTextField2;
import org.argouml.uml.ui.behavior.activity_graphs.ActionSetObjectFlowStateClassifier;
import org.argouml.uml.ui.behavior.activity_graphs.UMLActionSynchCheckBox;
import org.argouml.uml.ui.behavior.activity_graphs.UMLObjectFlowStateClassifierComboBoxModel;
import org.argouml.uml.ui.behavior.collaborations.ActionAddClassifierRoleBase;
import org.argouml.uml.ui.behavior.collaborations.ActionAddMessagePredecessor;
import org.argouml.uml.ui.behavior.collaborations.ActionRemoveClassifierRoleBase;
import org.argouml.uml.ui.behavior.collaborations.UMLClassifierRoleAvailableContentsListModel;
import org.argouml.uml.ui.behavior.collaborations.UMLClassifierRoleAvailableFeaturesListModel;
import org.argouml.uml.ui.behavior.collaborations.UMLClassifierRoleBaseListModel;
import org.argouml.uml.ui.behavior.collaborations.UMLCollaborationConstrainingElementListModel;
import org.argouml.uml.ui.behavior.collaborations.UMLCollaborationInteractionListModel;
import org.argouml.uml.ui.behavior.collaborations.UMLInteractionContextListModel;
import org.argouml.uml.ui.behavior.collaborations.UMLInteractionMessagesListModel;
import org.argouml.uml.ui.behavior.collaborations.UMLMessageActionListModel;
import org.argouml.uml.ui.behavior.collaborations.UMLMessageInteractionListModel;
import org.argouml.uml.ui.behavior.collaborations.UMLMessagePredecessorListModel;
import org.argouml.uml.ui.behavior.collaborations.UMLMessageReceiverListModel;
import org.argouml.uml.ui.behavior.collaborations.UMLMessageSenderListModel;
import org.argouml.uml.ui.behavior.common_behavior.ActionAddContextSignal;
import org.argouml.uml.ui.behavior.common_behavior.ActionAddCreateActionInstantiation;
import org.argouml.uml.ui.behavior.common_behavior.ActionAddInstanceClassifier;
import org.argouml.uml.ui.behavior.common_behavior.UMLActionArgumentListModel;
import org.argouml.uml.ui.behavior.common_behavior.UMLActionAsynchronousCheckBox;
import org.argouml.uml.ui.behavior.common_behavior.UMLCreateActionClassifierListModel;
import org.argouml.uml.ui.behavior.common_behavior.UMLInstanceClassifierListModel;
import org.argouml.uml.ui.behavior.common_behavior.UMLInstanceReceiverStimulusListModel;
import org.argouml.uml.ui.behavior.common_behavior.UMLInstanceSenderStimulusListModel;
import org.argouml.uml.ui.behavior.common_behavior.UMLSignalContextListModel;
import org.argouml.uml.ui.behavior.state_machines.ActionNewTransition;
import org.argouml.uml.ui.behavior.state_machines.ActionSetContextStateMachine;
import org.argouml.uml.ui.behavior.state_machines.ActionSetStubStateReferenceState;
import org.argouml.uml.ui.behavior.state_machines.ActionSetSubmachineStateSubmachine;
import org.argouml.uml.ui.behavior.state_machines.UMLCompositeStateSubvertexList;
import org.argouml.uml.ui.behavior.state_machines.UMLCompositeStateSubvertexListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLGuardTransitionListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLStateDeferrableEventList;
import org.argouml.uml.ui.behavior.state_machines.UMLStateDeferrableEventListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLStateDoActivityList;
import org.argouml.uml.ui.behavior.state_machines.UMLStateDoActivityListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLStateEntryList;
import org.argouml.uml.ui.behavior.state_machines.UMLStateEntryListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLStateExitList;
import org.argouml.uml.ui.behavior.state_machines.UMLStateExitListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLStateInternalTransition;
import org.argouml.uml.ui.behavior.state_machines.UMLStateMachineContextComboBoxModel;
import org.argouml.uml.ui.behavior.state_machines.UMLStateMachineSubmachineStateListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLStateMachineTopListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLStateMachineTransitionListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLStateVertexContainerListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLStateVertexIncomingListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLStateVertexOutgoingListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLStubStateComboBoxModel;
import org.argouml.uml.ui.behavior.state_machines.UMLSubmachineStateComboBoxModel;
import org.argouml.uml.ui.behavior.state_machines.UMLSynchStateBoundDocument;
import org.argouml.uml.ui.behavior.state_machines.UMLTransitionEffectListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLTransitionGuardListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLTransitionSourceListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLTransitionStateListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLTransitionStatemachineListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLTransitionTargetListModel;
import org.argouml.uml.ui.behavior.state_machines.UMLTransitionTriggerListModel;
import org.argouml.uml.ui.behavior.use_cases.ActionAddExtendExtensionPoint;
import org.argouml.uml.ui.behavior.use_cases.ActionNewExtendExtensionPoint;
import org.argouml.uml.ui.behavior.use_cases.ActionNewUseCaseExtensionPoint;
import org.argouml.uml.ui.behavior.use_cases.UMLExtendBaseListModel;
import org.argouml.uml.ui.behavior.use_cases.UMLExtendExtensionListModel;
import org.argouml.uml.ui.behavior.use_cases.UMLExtendExtensionPointListModel;
import org.argouml.uml.ui.behavior.use_cases.UMLExtensionPointLocationDocument;
import org.argouml.uml.ui.behavior.use_cases.UMLExtensionPointUseCaseListModel;
import org.argouml.uml.ui.behavior.use_cases.UMLIncludeAdditionListModel;
import org.argouml.uml.ui.behavior.use_cases.UMLUseCaseExtendListModel;
import org.argouml.uml.ui.behavior.use_cases.UMLUseCaseIncludeListModel;
import org.argouml.uml.ui.foundation.core.ActionAddAssociationSpecification;
import org.argouml.uml.ui.foundation.core.ActionAddClientDependencyAction;
import org.argouml.uml.ui.foundation.core.ActionAddSupplierDependencyAction;
import org.argouml.uml.ui.foundation.core.ActionSetAssociationEndType;
import org.argouml.uml.ui.foundation.core.ActionSetGeneralizationPowertype;
import org.argouml.uml.ui.foundation.core.ActionSetModelElementNamespace;
import org.argouml.uml.ui.foundation.core.ActionSetStructuralFeatureType;
import org.argouml.uml.ui.foundation.core.UMLAssociationAssociationRoleListModel;
import org.argouml.uml.ui.foundation.core.UMLAssociationConnectionListModel;
import org.argouml.uml.ui.foundation.core.UMLAssociationEndAggregationRadioButtonPanel;
import org.argouml.uml.ui.foundation.core.UMLAssociationEndAssociationListModel;
import org.argouml.uml.ui.foundation.core.UMLAssociationEndChangeabilityRadioButtonPanel;
import org.argouml.uml.ui.foundation.core.UMLAssociationEndNavigableCheckBox;
import org.argouml.uml.ui.foundation.core.UMLAssociationEndOrderingCheckBox;
import org.argouml.uml.ui.foundation.core.UMLAssociationEndQualifiersListModel;
import org.argouml.uml.ui.foundation.core.UMLAssociationEndSpecificationListModel;
import org.argouml.uml.ui.foundation.core.UMLAssociationEndTargetScopeCheckbox;
import org.argouml.uml.ui.foundation.core.UMLAssociationEndTypeComboBoxModel;
import org.argouml.uml.ui.foundation.core.UMLAssociationLinkListModel;
import org.argouml.uml.ui.foundation.core.UMLBehavioralFeatureQueryCheckBox;
import org.argouml.uml.ui.foundation.core.UMLClassActiveCheckBox;
import org.argouml.uml.ui.foundation.core.UMLClassAttributeListModel;
import org.argouml.uml.ui.foundation.core.UMLClassOperationListModel;
import org.argouml.uml.ui.foundation.core.UMLClassifierAssociationEndListModel;
import org.argouml.uml.ui.foundation.core.UMLClassifierFeatureListModel;
import org.argouml.uml.ui.foundation.core.UMLClassifierParameterListModel;
import org.argouml.uml.ui.foundation.core.UMLCommentAnnotatedElementListModel;
import org.argouml.uml.ui.foundation.core.UMLComponentResidentListModel;
import org.argouml.uml.ui.foundation.core.UMLContainerResidentListModel;
import org.argouml.uml.ui.foundation.core.UMLDependencyClientListModel;
import org.argouml.uml.ui.foundation.core.UMLDependencySupplierListModel;
import org.argouml.uml.ui.foundation.core.UMLDiscriminatorNameDocument;
import org.argouml.uml.ui.foundation.core.UMLEnumerationLiteralsListModel;
import org.argouml.uml.ui.foundation.core.UMLFeatureOwnerListModel;
import org.argouml.uml.ui.foundation.core.UMLFeatureOwnerScopeCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementAbstractCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementGeneralizationListModel;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementLeafCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementRootCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementSpecializationListModel;
import org.argouml.uml.ui.foundation.core.UMLGeneralizationChildListModel;
import org.argouml.uml.ui.foundation.core.UMLGeneralizationParentListModel;
import org.argouml.uml.ui.foundation.core.UMLGeneralizationPowertypeComboBoxModel;
import org.argouml.uml.ui.foundation.core.UMLModelElementClientDependencyListModel;
import org.argouml.uml.ui.foundation.core.UMLModelElementNameDocument;
import org.argouml.uml.ui.foundation.core.UMLModelElementNamespaceComboBoxModel;
import org.argouml.uml.ui.foundation.core.UMLModelElementSupplierDependencyListModel;
import org.argouml.uml.ui.foundation.core.UMLModelElementVisibilityRadioButtonPanel;
import org.argouml.uml.ui.foundation.core.UMLNamespaceOwnedElementListModel;
import org.argouml.uml.ui.foundation.core.UMLOperationConcurrencyRadioButtonPanel;
import org.argouml.uml.ui.foundation.core.UMLOperationMethodsListModel;
import org.argouml.uml.ui.foundation.core.UMLOperationRaisedSignalsListModel;
import org.argouml.uml.ui.foundation.core.UMLOperationSpecificationDocument;
import org.argouml.uml.ui.foundation.core.UMLParameterBehavioralFeatListModel;
import org.argouml.uml.ui.foundation.core.UMLParameterDirectionKindRadioButtonPanel;
import org.argouml.uml.ui.foundation.core.UMLStructuralFeatureChangeabilityRadioButtonPanel;
import org.argouml.uml.ui.foundation.core.UMLStructuralFeatureTypeComboBoxModel;
import org.tigris.swidgets.GridLayout2;
import org.tigris.swidgets.LabelledLayout;

/**
 * Creates the XML Property panels
 */
public class SwingUIFactory implements UIFactory {
    
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(SwingUIFactory.class);
    
    private static UIFactory instance = new SwingUIFactory();
    
    public SwingUIFactory() {
        
    }
    
    public static UIFactory getInstance() {        
        return instance;
    }
    
    /**
     * @param target The model element selected
     * @return A Panel to be added to the main panel
     * @throws Exception If something goes wrong
     * @see org.argouml.core.propertypanels.panel.UIFactory#createGUI(java.lang.Object)
     */
    public JPanel createGUI (Object target) throws Exception {
        XMLPropertyPanelsData data = 
            XMLPropPanelFactory.getInstance().getPropertyPanelsData(
                    Model.getMetaTypes().getName(target));
        JPanel p = buildPanel(data, target);
        return p;       
    }
    
    private JPanel buildPanel(XMLPropertyPanelsData data, Object target) {
        
        JPanel panel = new JPanel(new LabelledLayout());
        
        for (XMLPropertyPanelsDataRecord prop : data.getProperties()) {        
            if ("text".equals(prop.getType())) {
                buildTextboxPanel(panel, target, prop);
            }
            else if ("combo".equals(prop.getType())) {
                buildComboPanel(panel, target, prop);                
            }
            else if ("checkgroup".equals(prop.getType())) {
                buildCheckGroup(panel, target, prop);
            }
            else if ("optionbox".equals(prop.getType())) {
                buildOptionBox(panel, target, prop);
            }
            else if ("singlerow".equals(prop.getType())) {
                buildSingleRow(panel, target, prop);
            }
            else if ("list".equals(prop.getType())) {                    
                buildList(panel, target, prop);
            }
            else if ("textarea".equals(prop.getType())) {
                buildTextArea(panel, target, prop);
            }
            else if ("separator".equals(prop.getType())) {
                panel.add(LabelledLayout.getSeperator());
            }
        }
        return panel;
    }

    private void buildTextArea(JPanel panel, Object target, 
            XMLPropertyPanelsDataRecord prop) {
        JPanel p = new JPanel();
        TitledBorder border = new TitledBorder(prop.getName());        
        p.setBorder(border);

        JComponent control = null;
        
        if ("initialValue".equals(prop.getName())) {        
            UMLExpressionModel model = 
                new UMLInitialValueExpressionModel(target);
            p  = new UMLExpressionPanel(model, prop.getName());
            control = p;
        }
        if ("defaultValue".equals(prop.getName())) {        
            UMLExpressionModel model = 
                new UMLDefaultValueExpressionModel(target);
            p  = new UMLExpressionPanel(model, prop.getName());
            control = p;
        }
        else if ("specification".equals(prop.getName())) {
            UMLPlainTextDocument document = 
                new UMLOperationSpecificationDocument();
            document.setTarget(target);
            UMLTextArea2 osta = new UMLTextArea2(document);
            osta.setRows(3);
            control = new JScrollPane(osta);
        }
        else if ("body".equals(prop.getName())) {
            UMLPlainTextDocument document = new UMLCommentBodyDocument();
            document.setTarget(target);
            UMLTextArea2 text = new UMLTextArea2(document);
            text.setLineWrap(true);
            text.setRows(5);
            control = new JScrollPane(text);
        }
        else if ("condition".equals(prop.getName())) {
            UMLExpressionModel conditionModel =
                new UMLConditionExpressionModel(target);
            JTextArea conditionArea =
                new UMLExpressionBodyField(conditionModel, true);
            conditionArea.setRows(5);
            control = new JScrollPane(conditionArea);
        }
        else if ("script".equals(prop.getName())) {
            UMLExpressionModel scriptModel =
                new UMLScriptExpressionModel(target);            
            p  = new UMLExpressionPanel(scriptModel, prop.getName());
            control = p;
        }
        else if ("recurrence".equals(prop.getName())) {
            UMLExpressionModel recurrenceModel =
                new UMLRecurrenceExpressionModel(target);            
            p  = new UMLExpressionPanel(recurrenceModel, prop.getName());
            control = p;
        }
        else if ("expression".equals(prop.getName())) {
            UMLExpressionModel model = new UMLExpressionExpressionModel(target);
            p = new UMLExpressionPanel(model, prop.getName());
            control = p;
        }
        else if ("changeExpression".equals(prop.getName())) {
            UMLExpressionModel model = new UMLChangeExpressionModel(target);
            p = new UMLExpressionPanel(model, prop.getName());
            control = p;
        }
        else if ("when".equals(prop.getName())) {
            UMLExpressionModel model = new UMLTimeExpressionModel(target);
            p = new UMLExpressionPanel(model, prop.getName());
            control = p;
        }
        if (control != null) {
            // if the control is a panel, add it
            if (control == p) {
                panel.add(control);
            }
            // if not, it is a control and must be labeled...
            else {
                JLabel label = new JLabel(prop.getName());
                label.setLabelFor(control);
                panel.add(label);
                panel.add(control);
            }
            
        }
    }

    private void buildSingleRow(JPanel panel, Object target,
            XMLPropertyPanelsDataRecord prop) {
        
        UMLModelElementListModel2 model = null;
        UMLSingleRowSelector pane = null;
        
        if ("owner".equals(prop.getName())) {
            model = new UMLFeatureOwnerListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("behavioralFeature".equals(prop.getName())) {
            model = new UMLParameterBehavioralFeatListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("parent".equals(prop.getName())) {
            model = new UMLGeneralizationParentListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("child".equals(prop.getName())) {
            model = new UMLGeneralizationChildListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("feature".equals(prop.getName())) {
            model = new UMLParameterBehavioralFeatListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("enumeration".equals(prop.getName())) {
            EnumerationListModel m = new EnumerationListModel();
            m.setTarget(target);   
            pane = new UMLSingleRowSelector(m);
        }
        else if ("association".equals(prop.getName())) {
            model = new UMLAssociationEndAssociationListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("base".equals(prop.getName())) {
            model = new UMLExtendBaseListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);            
        }
        else if ("extension".equals(prop.getName())) {
            model = new UMLExtendExtensionListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);            
        }
        else if ("addition".equals(prop.getName())) {
            model = new UMLIncludeAdditionListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("useCase".equals(prop.getName())) {
            model = new UMLExtensionPointUseCaseListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);            
        }
        else if ("interaction".equals(prop.getName())) {
            if (Model.getFacade().isAMessage(target)) {
                model = new UMLMessageInteractionListModel();
            }
            else {
                model = new UMLCollaborationInteractionListModel();                
            }
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);            
        }
        else if ("sender".equals(prop.getName())) {
            model = new UMLMessageSenderListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);            
        }
        else if ("receiver".equals(prop.getName())) {
            model = new UMLMessageReceiverListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);            
        }
        else if ("action".equals(prop.getName())) {
            model = new UMLMessageActionListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);            
        }
        else if ("context".equals(prop.getName())) {
            model = new UMLInteractionContextListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        /*
         * The XML generated is "stimulus", because the A_receiver_stimulus
         * association has "stimulus" and "receiver" as association ends.
         * The A_stimulus_sender has "sender" and "stimulus", so it is generated
         * once. So we have created them by hand with a more explicit name and
         * removed "stimulus".
         */ 
        else if ("sentStimulus".equals(prop.getName())) {
            model = new UMLInstanceSenderStimulusListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("receivedStimulus".equals(prop.getName())) {
            model = new UMLInstanceReceiverStimulusListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("stateMachine".equals(prop.getName())) {
            model = new UMLTransitionStatemachineListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("state".equals(prop.getName())) {
            model = new UMLTransitionStateListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("source".equals(prop.getName())) {
            model = new UMLTransitionSourceListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("target".equals(prop.getName())) {
            model = new UMLTransitionTargetListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("guard".equals(prop.getName())) {
            model = new UMLTransitionGuardListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("effect".equals(prop.getName())) {
            model = new UMLTransitionEffectListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("trigger".equals(prop.getName())) {
            model = new UMLTransitionTriggerListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("transition".equals(prop.getName())) {
            model = new UMLGuardTransitionListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("container".equals(prop.getName())) {
            model = new UMLStateVertexContainerListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        else if ("activityGraph".equals(prop.getName())) {
            model = new UMLPartitionActivityGraphListModel();
            model.setTarget(target);
            pane = new UMLSingleRowSelector(model);
        }
        if (pane != null) {           
            JLabel label = new JLabel(prop.getName());
            label.setLabelFor(pane);
            panel.add(label);
            panel.add(pane);
        }
    }

    private void buildList(JPanel panel, Object target, 
            XMLPropertyPanelsDataRecord prop) {
        
        ScrollList list = null;
        UMLModelElementListModel2 model = null;
        if ("clientDependency".equals(prop.getName())) {
            model = new UMLModelElementClientDependencyListModel();
            model.setTarget(target); 
            list = new ScrollList(new UMLMutableLinkedList(
                    model,
                    new ActionAddClientDependencyAction(),
                    null,
                    null,
                    true));
        }
        else if ("supplierDependency".equals(prop.getName())) {
            model = new UMLModelElementSupplierDependencyListModel();
            model.setTarget(target);
            list = new ScrollList(new UMLMutableLinkedList(
                    model,
                    new ActionAddSupplierDependencyAction(),
                    null,
                    null,
                    true));
        }
        else if ("generalization".equals(prop.getName())) {
            model = new UMLGeneralizableElementGeneralizationListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("specialization".equals(prop.getName())) {
            model = new UMLGeneralizableElementSpecializationListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("attribute".equals(prop.getName())) {
            model = new UMLClassAttributeListModel();
            model.setTarget(target);
            list = new ScrollList(model, true, false);
        }
        else if ("association".equals(prop.getName())) {
            model = new UMLClassifierAssociationEndListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("feature".equals(prop.getName())) {
            model = new UMLClassifierFeatureListModel();
            model.setTarget(target);
            list = new ScrollList(model, true, false);
        }
        else if ("operation".equals(prop.getName())) {
            model = new UMLClassOperationListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("ownedElement".equals(prop.getName())) {
            model = new UMLNamespaceOwnedElementListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("elementImport".equals(prop.getName())) {
            model = new UMLClassifierPackageImportsListModel();
            model.setTarget(target);
            list = new ScrollList(new UMLMutableLinkedList(model,
                    // TODO: It's OK to change the visibility of this actions?
                    null, // new ActionAddPackageImport(),
                    null,
                    null, //new ActionRemovePackageImport(),
                    true));
        }
        else if ("parameter".equals(prop.getName())) {
            if (Model.getFacade().isAObjectFlowState(target)) {
                model = new UMLObjectFlowStateParameterListModel();
                model.setTarget(target);
                JList l = new UMLMutableLinkedList(model,
                        new ActionAddOFSParameter(),
                        new ActionNewOFSParameter(),
                        new ActionRemoveOFSParameter(),
                        true); 
                list = new ScrollList(l);
                        
            }
            else {
                model = new UMLClassifierParameterListModel();
                model.setTarget(target);
                list = new ScrollList(new UMLLinkedList(model, 
                        true, false));
            }
        }
        else if ("raisedSignal".equals(prop.getName())) {
            model = new UMLOperationRaisedSignalsListModel();
            model.setTarget(target);
            list = new ScrollList(new UMLLinkedList(model, 
                    true, false));
        }
        else if ("method".equals(prop.getName())) {
            model = new UMLOperationMethodsListModel();
            model.setTarget(target);
            list = new ScrollList(new UMLLinkedList(model, 
                    true, false));
        }
        else if ("definedTag".equals(prop.getName())) {
            model = new UMLStereotypeTagDefinitionListModel();
            model.setTarget(target);
            list = new ScrollList(new UMLLinkedList(model,
                    true, false));
        }
        else if ("baseClass".equals(prop.getName())) {
            model = new UMLStereotypeBaseClassListModel();
            model.setTarget(target);
            UMLMutableLinkedList l = new UMLMutableLinkedList(
                    new UMLStereotypeBaseClassListModel(), 
                    new ActionAddStereotypeBaseClass(),
                    null,
                    new ActionDeleteStereotypeBaseClass(),
                    true);
            l.setCellRenderer(new DefaultListCellRenderer());
            list = new ScrollList(l);
        }
        else if ("extended_elements".equals(prop.getName())) {
            model = new UMLExtendedElementsListModel();
            model.setTarget(target);
            list = new ScrollList( new UMLLinkedList(model, 
                    true, true));
        }
        else if ("literal".equals(prop.getName())) {
            model = new UMLEnumerationLiteralsListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("supplier".equals(prop.getName())) {
            model = new UMLDependencySupplierListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("client".equals(prop.getName())) {
            model = new UMLDependencyClientListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("connection".equals(prop.getName())) {
            model = new UMLAssociationConnectionListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("associationRole".equals(prop.getName())) {
            model = new UMLAssociationAssociationRoleListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("link".equals(prop.getName())) {
            model = new UMLAssociationLinkListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("specification".equals(prop.getName())) {
            model = new UMLAssociationEndSpecificationListModel();
            model.setTarget(target);
            list = new ScrollList(new UMLMutableLinkedList(
                    model,
                    ActionAddAssociationSpecification.getInstance(),
                    null, null, true));
        }
        else if ("qualifier".equals(prop.getName())) {
            model = new UMLAssociationEndQualifiersListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("annotatedElement".equals(prop.getName())) {
            model = new UMLCommentAnnotatedElementListModel();
            model.setTarget(target);
            UMLMutableLinkedList l = new UMLMutableLinkedList(
                    model, null, null);            
            l.setDeleteAction(new ActionDeleteAnnotatedElement());
            list = new ScrollList(l);
        }
        else if ("context".equals(prop.getName())) {
            model = new UMLSignalContextListModel();
            model.setTarget(target);
            UMLMutableLinkedList l = new UMLMutableLinkedList(
                    model,
                    new ActionAddContextSignal(), null, 
                    new ActionRemoveContextSignal(), true);
            list = new ScrollList(l);
        }
        else if ("reception".equals(prop.getName())) {
            model = new UMLSignalReceptionListModel();
            model.setTarget(target);
            UMLMutableLinkedList l = new UMLMutableLinkedList(
                    model,
                    new ActionAddReceptionSignal(), null, 
                    new ActionRemoveReceptionSignal(), true);
            list = new ScrollList(l);
        }
        else if ("extend".equals(prop.getName())) {
            model = new UMLUseCaseExtendListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("include".equals(prop.getName())) {
            model = new UMLUseCaseIncludeListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("extensionPoint".equals(prop.getName())) {
            if (Model.getFacade().isAUseCase(target)) {
                model = new UMLUseCaseIncludeListModel();
                model.setTarget(target);
                UMLMutableLinkedList l = new UMLMutableLinkedList(
                        model, null,
                        ActionNewUseCaseExtensionPoint.SINGLETON);
                list = new ScrollList(l);
            }
            else {
                model = new UMLExtendExtensionPointListModel();
                model.setTarget(target);
                JList l =
                    new UMLMutableLinkedList(model, 
                        ActionAddExtendExtensionPoint.getInstance(),
                        ActionNewExtendExtensionPoint.SINGLETON);
                list = new ScrollList(l);
            }
        }
        else if ("base".equals(prop.getName())) {
            model = new UMLClassifierRoleBaseListModel();
            model.setTarget(target);
            JList l =
                new UMLMutableLinkedList(model,
                    ActionAddClassifierRoleBase.SINGLETON,
                    null,
                    ActionRemoveClassifierRoleBase.getInstance(),
                    true);
            list = new ScrollList(l);
        }
        else if ("availableFeature".equals(prop.getName())) {
            model = new UMLClassifierRoleAvailableFeaturesListModel();
            model.setTarget(target);
            JList l = new UMLLinkedList(model);
            list = new ScrollList(l);
        }
        else if ("availableContents".equals(prop.getName())) {
            model = new UMLClassifierRoleAvailableContentsListModel();
            model.setTarget(target);
            JList l = new UMLLinkedList(model);
            list = new ScrollList(l);
        }
        else if ("predecessor".equals(prop.getName())) {
            model = new UMLMessagePredecessorListModel();
            model.setTarget(target);
            JList l = new UMLMutableLinkedList(model,
                    ActionAddMessagePredecessor.getInstance(),
                    null);
            list = new ScrollList(l);
        }
        else if ("actualArgument".equals(prop.getName())) {
            model = new UMLActionArgumentListModel();
            model.setTarget(target);
            JList l = new UMLLinkedList(model);
            list = new ScrollList(l);
        }
        else if ("instantiation".equals(prop.getName())) {
            model = new UMLCreateActionClassifierListModel();
            model.setTarget(target);
            JList l = new UMLMutableLinkedList(model,
                    new ActionAddCreateActionInstantiation(), 
                    null, null, true);
            list = new ScrollList(l);
        }
        else if ("constrainingElement".equals(prop.getName())) {
            model = new UMLCollaborationConstrainingElementListModel();
            model.setTarget(target);
            JList l = new UMLLinkedList(model);
            list = new ScrollList(l);
        }
        else if ("top".equals(prop.getName())) {
            model = new UMLStateMachineTopListModel();
            model.setTarget(target);
            JList l = new UMLLinkedList(model);
            list = new ScrollList(l);
        }
        else if ("transitions".equals(prop.getName())) {
            model = new UMLStateMachineTransitionListModel();
            model.setTarget(target);
            JList l = new UMLLinkedList(model);
            list = new ScrollList(l);
        }
        else if ("transition".equals(prop.getName())) {
            model = new UMLEventTransitionListModel();
            model.setTarget(target);
            JList l = new UMLLinkedList(model);
            list = new ScrollList(l);
        }
        else if ("submachineState".equals(prop.getName())) {
            model = new UMLStateMachineSubmachineStateListModel();
            model.setTarget(target);
            JList l = new UMLLinkedList(model);
            list = new ScrollList(l);
        }
        else if ("message".equals(prop.getName())) {
            model = new UMLInteractionMessagesListModel();
            model.setTarget(target);
            JList l = new UMLLinkedList(model);
            list = new ScrollList(l);
        }
        else if ("deployedComponent".equals(prop.getName())) {
            model = new UMLNodeDeployedComponentListModel();
            model.setTarget(target);
            JList l = new UMLLinkedList(model);
            list = new ScrollList(l);
        }
        else if ("residentElement".equals(prop.getName())) {
            model = new UMLComponentResidentListModel();
            model.setTarget(target);
            JList l = new UMLLinkedList(model);
            list = new ScrollList(l);
        }
        else if ("classifier".equals(prop.getName())) {
            model = new UMLInstanceClassifierListModel();
            model.setTarget(target);
            JList l = new UMLMutableLinkedList(model,
                             new ActionAddInstanceClassifier(
                                     Model.getMetaTypes().getClassifier()), 
                                     null, null, true);
            list = new ScrollList(l);
        }
        else if ("resident".equals(prop.getName())) {
            model = new UMLContainerResidentListModel();
            model.setTarget(target);
            JList l = new UMLLinkedList(model);
            list = new ScrollList(l);
        }
        else if ("entry".equals(prop.getName())) {
            model = new UMLStateEntryListModel();
            model.setTarget(target);
            JList l = new UMLStateEntryList(model);
            list = new ScrollList(l);
        }
        else if ("exit".equals(prop.getName())) {
            model = new UMLStateExitListModel();
            model.setTarget(target);
            JList l = new UMLStateExitList(model);
            list = new ScrollList(l);
        }
        else if ("deferrableEvent".equals(prop.getName())) {
            model = new UMLStateDeferrableEventListModel();
            model.setTarget(target);
            JList l = new UMLStateDeferrableEventList(model);
            list = new ScrollList(l);                    
        }
        else if ("doActivity".equals(prop.getName())) {
            model = new UMLStateDoActivityListModel();
            model.setTarget(target);
            JList l = new UMLStateDoActivityList(model);
            list = new ScrollList(l);
        }
        else if ("outgoing".equals(prop.getName())) {
            model = new UMLStateVertexOutgoingListModel();
            model.setTarget(target);
            JList l = new UMLLinkedList(model);
            list = new ScrollList(l);                    
        }
        else if ("incoming".equals(prop.getName())) {
            model = new UMLStateVertexIncomingListModel();
            model.setTarget(target);
            JList l = new UMLLinkedList(model);
            list = new ScrollList(l);                    
        }
        else if ("classifierInState".equals(prop.getName())) {
            model = new UMLOFSStateListModel();
            model.setTarget(target);
            JList l = new UMLMutableLinkedList(model,
                    new ActionAddOFSState(),
                    null,
                    new ActionRemoveOFSState(),
                    true);
            list = new ScrollList(l);
        }
        else if ("internalTransition".equals(prop.getName())) {
            model = new UMLStateInternalTransition();
            model.setTarget(target);
            JList l = new UMLMutableLinkedList(model, null, 
                    new ActionNewTransition());
            list = new ScrollList(l);
        }
        else if ("subvertex".equals(prop.getName())) {
            model = new UMLCompositeStateSubvertexListModel();
            model.setTarget(target);
            JList l = new UMLCompositeStateSubvertexList(model);
            list = new ScrollList(l);
        }
        else if ("contents".equals(prop.getName())) {
            model = new UMLPartitionContentListModel();
            model.setTarget(target);
            JList l = new UMLMutableLinkedList(model,
                    new ActionAddPartitionContent(),
                    null);
            list = new ScrollList(l);
        }
        else if ("partition".equals(prop.getName())) {
            model = new UMLActivityGraphPartitionListModel();
            model.setTarget(target);
            list = new ScrollList(new UMLLinkedList(model));
        }
        else if ("signal".equals(prop.getName())) {
            model = new UMLSignalEventSignalListModel();
            model.setTarget(target);
            list = new ScrollList(new UMLSignalEventSignalList(model));                    
        }
        else if ("action".equals(prop.getName())) {
            // TODO: we need to set up the target. 
            // This creates a model without it.
            list = new ScrollList(new UMLActionSequenceActionList());
        }
        if (list != null) {
            String name = prop.getName();

            JLabel label = new JLabel(name);
            label.setLabelFor(list);
            panel.add(label);
            panel.add(list);
        }
    }

    /**
     * @param target The target of the panel
     * @param prop The XML data that contains the information
     *        of the options.
     * @return a radio button panel with the options 
     */
    private void buildOptionBox(JPanel panel, Object target,
            XMLPropertyPanelsDataRecord prop) {
        
        UMLRadioButtonPanel control = null;
        
        if ("visibility".equals(prop.getName())) {
            UMLRadioButtonPanel visibilityPanel =   
                new UMLModelElementVisibilityRadioButtonPanel(
                    Translator.localize("label.visibility"), 
                    true); 
            visibilityPanel.setTarget(target);
            control = visibilityPanel;
        }
        else if ("changeability".equals(prop.getName())) {
            UMLRadioButtonPanel cPanel = null;
            if (Model.getFacade().isAAssociationEnd(target)) {
                cPanel = 
                    new UMLAssociationEndChangeabilityRadioButtonPanel(
                            "label.changeability", true);
            }
            else {
                cPanel =   
                    new UMLStructuralFeatureChangeabilityRadioButtonPanel(
                            Translator.localize("label.changeability"), 
                            true);
            }
            cPanel.setTarget(target);
            control = cPanel;

        }
        else if ("concurrency".equals(prop.getName())) { 
            UMLRadioButtonPanel cPanel =   
                new UMLOperationConcurrencyRadioButtonPanel(
                        Translator.localize("label.concurrency"), true); 
            cPanel.setTarget(target);
            control = cPanel;
            
        }
        else if ("kind".equals(prop.getName())) {
            UMLRadioButtonPanel cPanel = 
                new UMLParameterDirectionKindRadioButtonPanel(
                    Translator.localize("label.parameter.kind"), true);
            cPanel.setTarget(target);
            control = cPanel;   
        }
        else if ("aggregation".equals(prop.getName())) {
            UMLRadioButtonPanel cPanel = 
                new UMLAssociationEndAggregationRadioButtonPanel(
                        "label.aggregation", true);
            cPanel.setTarget(target);
            control = cPanel;   
        }
        if (control != null) {
            panel.add(control);
        }
    }

    /**
     * @param target The target of the checkbox group
     * @param prop The XML data that contains the information
     *        of the checkboxes.
     * @return a panel that contains the checkboxes 
     */
    private void buildCheckGroup(JPanel panel, Object target,
            XMLPropertyPanelsDataRecord prop) {
        JPanel p = new JPanel(new GridLayout2());
        TitledBorder border = new TitledBorder(prop.getName());        
        p.setBorder(border);
        
        if ("modifiers".equals(prop.getName())) {  
            for (XMLPropertyPanelsDataRecord data : prop.getChildren()) {
                buildCheckBox(p, target, data);
            }                            
        }
        panel.add(p);
    }

    private void buildCheckBox(JPanel panel, Object target,
            XMLPropertyPanelsDataRecord p) {
        UMLCheckBox2 checkbox = null;
        
        if ("isAbstract".equals(p.getName())) {
            checkbox = new UMLGeneralizableElementAbstractCheckBox();
        }
        else if ("isLeaf".equals(p.getName())) {
            checkbox = new UMLGeneralizableElementLeafCheckBox();
        }
        else if ("isRoot".equals(p.getName())) {
            checkbox = new UMLGeneralizableElementRootCheckBox(); 
        }
        else if ("derived".equals(p.getName())) {
            checkbox = new UMLDerivedCheckBox();
        }
        else if ("isActive".equals(p.getName())) {
            checkbox = new UMLClassActiveCheckBox();    
        }        
        else if ("ownerScope".equals(p.getName())) {   
            checkbox = new UMLFeatureOwnerScopeCheckBox();
        }
        else if ("targetScope".equals(p.getName())) {
            checkbox = new UMLAssociationEndTargetScopeCheckbox();
        }
        else if ("isQuery".equals(p.getName())) {
            checkbox = new UMLBehavioralFeatureQueryCheckBox();
        }
        else if ("isNavigable".equals(p.getName())) {
            checkbox = new UMLAssociationEndNavigableCheckBox();
        }
        else if ("ordering".equals(p.getName())) {
            checkbox = new UMLAssociationEndOrderingCheckBox();
        }
        else if ("isAsynchronous".equals(p.getName())) {
            checkbox = new UMLActionAsynchronousCheckBox();
        }
        else if ("isSynch".equals(p.getName())) {
            checkbox = new UMLActionSynchCheckBox();
        }
        if (checkbox != null) {
            checkbox.setTarget(target);
            panel.add(checkbox);
        }
    }

    /**
     * @param panel The panel where the controls will be added.
     * @param target The target of the panel
     * @param prop The XML data that contains the information
     *        of the combo.
     * @return a combo panel 
     */
    private void buildComboPanel(JPanel panel, Object target,
            XMLPropertyPanelsDataRecord prop) {        
        JComponent comp = null;
        if ("namespace".equals(prop.getName())) {
            final UMLComboBoxModel2 model =
                new UMLModelElementNamespaceComboBoxModel();
            model.setTarget(target);
            final JComboBox combo = new UMLSearchableComboBox(
                    model,
                    new ActionSetModelElementNamespace(), true);            
            comp = new UMLComboBoxNavigator(
                    Translator.localize(
                    "label.namespace.navigate.tooltip"),
                    combo);
        }
        else if ("type".equals(prop.getName())) {
            if (Model.getFacade().isAObjectFlowState(target)) {
                final UMLComboBoxModel2 model = 
                    new UMLObjectFlowStateClassifierComboBoxModel();
                model.setTarget(target);
                final JComboBox combo = new UMLSearchableComboBox(
                        model,
                        new ActionSetObjectFlowStateClassifier(), true);
                comp = new UMLComboBoxNavigator(Translator.localize(
                        "label.classifierinstate.navigate.tooltip"),
                        combo);
            }
            else {
                final UMLComboBoxModel2 model =
                    new UMLStructuralFeatureTypeComboBoxModel();
                model.setTarget(target);           
                final JComboBox combo = new UMLComboBox2(
                        model,
                        // TODO: The action is different for
                        // attributes and parameters. Issue #5222
                        ActionSetStructuralFeatureType.getInstance());
                comp = combo;
            }
        }
        else if ("powertype".equals(prop.getName())) {
            final UMLComboBoxModel2 model = 
                new UMLGeneralizationPowertypeComboBoxModel();
            model.setTarget(target);
            final JComboBox combo = new UMLComboBox2(
                    model,
                    ActionSetGeneralizationPowertype.getInstance());
            comp = combo;
        }
        else if ("multiplicity".equals(prop.getName())) {            
            final UMLMultiplicityPanel mPanel = new UMLMultiplicityPanel();
            mPanel.setTarget(target);
            comp = mPanel;
        }
        else if ("activator".equals(prop.getName())) {
            final UMLComboBoxModel3 model = 
                new UMLMessageActivatorComboBoxModel();
            model.setTarget(target); 
            final JComboBox combo = new UMLMessageActivatorComboBox(model);
            comp = combo;
        }
        else if ("operation".equals(prop.getName())) {
            if (Model.getFacade().isACallEvent(target)) {
                UMLComboBoxModel2 model = 
                    new UMLCallEventOperationComboBoxModel();
                model.setTarget(target);
                JComboBox combo = new UMLCallEventOperationComboBox2(model);
                comp = new UMLComboBoxNavigator(Translator.localize(
                        "label.operation.navigate.tooltip"),
                        combo);
            }
            else {
                final UMLComboBoxModel3 model = 
                    new UMLCallActionOperationComboBoxModel();
                model.setTarget(target); 
                UMLComboBox3 operationComboBox =
                    new UMLCallActionOperationComboBox2(model);
                comp = new UMLComboBoxNavigator(
                        Translator.localize("label.operation.navigate.tooltip"),
                        operationComboBox);
            }
        }
        else if ("representedClassifier".equals(prop.getName())) {
            final UMLComboBoxModel2 model = 
                new UMLCollaborationRepresentedClassifierComboBoxModel();
            model.setTarget(target);
            UMLComboBox2 combo =
                new UMLComboBox2(model,
                        new ActionSetRepresentedClassifierCollaboration());
            comp = new UMLComboBoxNavigator(Translator.localize(
                            "label.represented-classifier.navigate.tooltip"),
                            combo);
        }
        else if ("representedOperation".equals(prop.getName())) {
            final UMLComboBoxModel2 model = 
                new UMLCollaborationRepresentedOperationComboBoxModel();
            model.setTarget(target);
            UMLComboBox2 combo = new UMLComboBox2(model,
                        new ActionSetRepresentedOperationCollaboration());
            comp = new UMLComboBoxNavigator(Translator.localize(
                            "label.represented-operation.navigate.tooltip"),
                    combo);
        }
        else if ("context".equals(prop.getName())) {
            final UMLComboBoxModel2 model = 
                new UMLStateMachineContextComboBoxModel();
            model.setTarget(target);
            UMLComboBox2 combo = new UMLComboBox2(model,
                    ActionSetContextStateMachine.getInstance());
            comp = new UMLComboBoxNavigator(Translator.localize(
                            "label.context.navigate.tooltip"),
                    combo);

        }
        else if ("association".equals(prop.getName())) {
            final UMLComboBoxModel2 model = 
                new UMLLinkAssociationComboBoxModel();
            model.setTarget(target);
            comp =  new UMLComboBoxNavigator(Translator.localize(
                        "label.association.navigate.tooltip"),                
                    new UMLSearchableComboBox(model,
                            new ActionSetLinkAssociation(), true));
        }
        else if ("participant".equals(prop.getName())) {
            final UMLComboBoxModel2 model = 
                new UMLAssociationEndTypeComboBoxModel();
            model.setTarget(target);
            comp = new UMLComboBox2(model,
                    ActionSetAssociationEndType.getInstance(), true);
        }
        else if ("submachine".equals(prop.getName())) {
            final UMLComboBoxModel2 model = 
                new UMLSubmachineStateComboBoxModel();
            model.setTarget(target);            
            final JComboBox submachineBox = new UMLComboBox2(model,
                    ActionSetSubmachineStateSubmachine.getInstance());
            comp = new UMLComboBoxNavigator(Translator.localize(
                            "tooltip.nav-submachine"), submachineBox);
        }
        else if ("referenceState".equals(prop.getName())) {
            final UMLComboBoxModel2 model = 
                new UMLStubStateComboBoxModel();
            model.setTarget(target);            
            final JComboBox referencestateBox =
                new UMLComboBox2(model,
                        ActionSetStubStateReferenceState.getInstance());
            comp = new UMLComboBoxNavigator(Translator.localize(
                    "tooltip.nav-stubstate"), referencestateBox);            
        }
        if (comp != null) {
            String name = prop.getName();

            JLabel label = new JLabel(name);
            label.setLabelFor(comp);
            panel.add(label);
            panel.add(comp);
        }
    }

    /**
     * @param target The target of the panel
     * @param prop The XML data that contains the information
     *        of the options.
     * @return a panel with a labelled text field 
     */
    private void buildTextboxPanel(JPanel panel, Object target,
            XMLPropertyPanelsDataRecord prop) {
       
        JTextField tfield = null;
        UMLPlainTextDocument document = null;
        if ("name".equals(prop.getName())) {
            document = new UMLModelElementNameDocument();            
        }
        else if ("discriminator".equals(prop.getName())) {
            document = new UMLDiscriminatorNameDocument();            
        }
        else if ("location".equals(prop.getName())) {
            document = new UMLExtensionPointLocationDocument();
        }
        else if ("bound".equals(prop.getName())) {
            document = new UMLSynchStateBoundDocument();
        }
        if (document != null) {
            document.setTarget(target);
            tfield = new UMLTextField2(document);
        }        
        if (tfield != null) {
            String name = prop.getName();
            JLabel label = new JLabel(name);
            label.setLabelFor(tfield);
            panel.add(label);
            panel.add(tfield);
        }        
    }


    


}
