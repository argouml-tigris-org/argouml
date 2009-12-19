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

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.argouml.core.propertypanels.meta.CheckBoxMeta;
import org.argouml.core.propertypanels.meta.PanelMeta;
import org.argouml.core.propertypanels.meta.PropertyMeta;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLRadioButtonPanel;
import org.argouml.uml.ui.foundation.core.UMLAssociationEndAggregationRadioButtonPanel;
import org.argouml.uml.ui.foundation.core.UMLAssociationEndChangeabilityRadioButtonPanel;
import org.argouml.uml.ui.foundation.core.UMLModelElementVisibilityRadioButtonPanel;
import org.argouml.uml.ui.foundation.core.UMLOperationConcurrencyRadioButtonPanel;
import org.argouml.uml.ui.foundation.core.UMLParameterDirectionKindRadioButtonPanel;
import org.argouml.uml.ui.foundation.core.UMLStructuralFeatureChangeabilityRadioButtonPanel;
import org.tigris.swidgets.GridLayout2;

/**
 * Creates the XML Property panels
 */
class SwingUIFactory {
    
    public SwingUIFactory() {
        
    }
    
    /**
     * @param target The model element selected
     * @return A Panel to be added to the main panel
     * @throws Exception If something goes wrong
     * @see org.argouml.core.propertypanels.panel.UIFactory#createGUI(java.lang.Object)
     */
    public JPanel createGUI (Object target, JPanel panel) throws Exception {
        PanelMeta data = 
            XMLPropPanelFactory.getInstance().getPropertyPanelsData(
                    Model.getMetaTypes().getName(target));
        
        for (PropertyMeta prop : data.getProperties()) {
            if ("text".equals(prop.getType())) {
                buildTextboxPanel(panel, target, prop);
            } else if ("combo".equals(prop.getType())) {
                buildComboPanel(panel, target, prop);                
            } else if ("checkgroup".equals(prop.getType())) {
                buildCheckGroup(panel, target, prop);
            } else if ("optionbox".equals(prop.getType())) {
                buildOptionBox(panel, target, prop);
            } else if ("singlerow".equals(prop.getType())) {
                buildSingleRow(panel, target, prop);
            } else if ("list".equals(prop.getType())) {                    
                buildList(panel, target, prop);
            } else if ("textarea".equals(prop.getType())) {
                buildTextArea(panel, target, prop);
            } else if ("separator".equals(prop.getType())) {
                panel.add(LabelledLayout.getSeparator());
            }
        }
        return panel;
    }

    private void buildTextArea(
            final JPanel panel,
            final Object target, 
            final PropertyMeta prop) {
        
        // TODO: Why do we need this as well as control? Why is it
        // instantiated when its not always needed.
        JPanel p = new JPanel();
        
        final TitledBorder border = new TitledBorder(prop.getName());        
        p.setBorder(border);

        JComponent control = null;
        
        if ("initialValue".equals(prop.getName())) {        
            UMLExpressionModel model = 
                new UMLInitialValueExpressionModel(target);
            p  = new UMLExpressionPanel(model, prop.getName());
            control = p;
        } else if ("defaultValue".equals(prop.getName())) {        
            UMLExpressionModel model = 
                new UMLDefaultValueExpressionModel(target);
            p  = new UMLExpressionPanel(model, prop.getName());
            control = p;
        } else if ("specification".equals(prop.getName())) {
            UMLPlainTextDocument document = 
                new UMLOperationSpecificationDocument(prop.getName(), target);
            UMLTextArea osta = new UMLTextArea(document);
            osta.setRows(3);
            control = new JScrollPane(osta);
        } else if ("body".equals(prop.getName())) {
            UMLPlainTextDocument document = new UMLCommentBodyDocument(prop.getName(), target);
            UMLTextArea text = new UMLTextArea(document);
            text.setLineWrap(true);
            text.setRows(5);
            control = new JScrollPane(text);
        } else if ("condition".equals(prop.getName())) {
            UMLExpressionModel conditionModel =
                new UMLConditionExpressionModel(target);
            JTextArea conditionArea =
                new UMLExpressionBodyField(conditionModel, true);
            conditionArea.setRows(5);
            control = new JScrollPane(conditionArea);
        } else if ("script".equals(prop.getName())) {
            UMLExpressionModel scriptModel =
                new UMLScriptExpressionModel(target);            
            p  = new UMLExpressionPanel(scriptModel, prop.getName());
            control = p;
        } else if ("recurrence".equals(prop.getName())) {
            UMLExpressionModel recurrenceModel =
                new UMLRecurrenceExpressionModel(target);            
            p  = new UMLExpressionPanel(recurrenceModel, prop.getName());
            control = p;
        } else if ("expression".equals(prop.getName())) {
            UMLExpressionModel model = new UMLExpressionExpressionModel(target);
            p = new UMLExpressionPanel(model, prop.getName());
            control = p;
        } else if ("changeExpression".equals(prop.getName())) {
            UMLExpressionModel model = new UMLChangeExpressionModel(target);
            p = new UMLExpressionPanel(model, prop.getName());
            control = p;
        } else if ("when".equals(prop.getName())) {
            UMLExpressionModel model = new UMLTimeExpressionModel(target);
            p = new UMLExpressionPanel(model, prop.getName());
            control = p;
        }
        
        if (control != null) {
            if (control == p) {
                // if the control is a panel, add it
                addControl(panel, null, control);
            } else {
                // if not, it is a control and must be labeled...
                addControl(panel, prop.getName(), control);
            }
        }
    }

    private void buildSingleRow(JPanel panel, Object target,
            PropertyMeta prop) {
        
        final SingleListFactory factory = new SingleListFactory();
        final JComponent pane =
            factory.createComponent(target, prop.getName());
        
        if (pane != null) {           
            addControl(panel, prop.getName(), pane);
        }
    }

    private void buildList(
            final JPanel panel, Object target, 
            final PropertyMeta prop) {
        
        final ListFactory factory = new ListFactory();
        final JComponent list =
            factory.createComponent(target, prop.getName());
        
        if (list != null) {
            addControl(panel, prop.getName(), list);
        }
    }

    /**
     * @param target The target of the panel
     * @param prop The XML data that contains the information
     *        of the options.
     * @return a radio button panel with the options 
     */
    private void buildOptionBox(JPanel panel, Object target,
            PropertyMeta prop) {
        
        UMLRadioButtonPanel control = null;
        
        if ("visibility".equals(prop.getName())) {
            UMLRadioButtonPanel visibilityPanel =   
                new UMLModelElementVisibilityRadioButtonPanel(
                    Translator.localize("label.visibility"), 
                    true); 
            visibilityPanel.setTarget(target);
            control = visibilityPanel;
        } else if ("changeability".equals(prop.getName())) {
            UMLRadioButtonPanel cPanel = null;
            if (Model.getFacade().isAAssociationEnd(target)) {
                cPanel = 
                    new UMLAssociationEndChangeabilityRadioButtonPanel(
                            "label.changeability", true);
            } else {
                cPanel =   
                    new UMLStructuralFeatureChangeabilityRadioButtonPanel(
                            Translator.localize("label.changeability"), 
                            true);
            }
            cPanel.setTarget(target);
            control = cPanel;

        } else if ("concurrency".equals(prop.getName())) { 
            UMLRadioButtonPanel cPanel =   
                new UMLOperationConcurrencyRadioButtonPanel(
                        Translator.localize("label.concurrency"), true); 
            cPanel.setTarget(target);
            control = cPanel;
            
        } else if ("kind".equals(prop.getName())) {
            UMLRadioButtonPanel cPanel = 
                new UMLParameterDirectionKindRadioButtonPanel(
                    Translator.localize("label.parameter.kind"), true);
            cPanel.setTarget(target);
            control = cPanel;   
        } else if ("aggregation".equals(prop.getName())) {
            UMLRadioButtonPanel cPanel = 
                new UMLAssociationEndAggregationRadioButtonPanel(
                        "label.aggregation", true);
            cPanel.setTarget(target);
            control = cPanel;   
        }
        
        if (control != null) {
            addControl(panel, null, control);
        }
    }

    /**
     * @param target The target of the checkbox group
     * @param prop The XML data that contains the information
     *        of the checkboxes.
     * @return a panel that contains the checkboxes 
     */
    private void buildCheckGroup(JPanel panel, Object target,
            PropertyMeta prop) {
        JPanel p = new JPanel(new GridLayout2());
        TitledBorder border = new TitledBorder(prop.getName());        
        p.setBorder(border);
        
        if ("modifiers".equals(prop.getName())) {  
            for (CheckBoxMeta data : prop.getCheckboxes()) {
                buildCheckBox(p, target, data);
            }                            
        }
        addControl(panel, null, p);
    }

    private void buildCheckBox(
            final JPanel panel,
            final Object target,
            final CheckBoxMeta p) {
        UMLCheckBox checkbox = null;
        
        if ("isAbstract".equals(p.getName())) {
            checkbox = new UMLGeneralizableElementAbstractCheckBox();
        } else if ("isLeaf".equals(p.getName())) {
            checkbox = new UMLGeneralizableElementLeafCheckBox();
        } else if ("isRoot".equals(p.getName())) {
            checkbox = new UMLGeneralizableElementRootCheckBox(); 
        } else if ("derived".equals(p.getName())) {
            checkbox = new UMLDerivedCheckBox();
        } else if ("isActive".equals(p.getName())) {
            checkbox = new UMLClassActiveCheckBox();    
        } else if ("ownerScope".equals(p.getName())) {   
            checkbox = new UMLFeatureOwnerScopeCheckBox();
        } else if ("targetScope".equals(p.getName())) {
            checkbox = new UMLAssociationEndTargetScopeCheckbox();
        } else if ("isQuery".equals(p.getName())) {
            checkbox = new UMLBehavioralFeatureQueryCheckBox();
        } else if ("isNavigable".equals(p.getName())) {
            checkbox = new UMLAssociationEndNavigableCheckBox();
        } else if ("ordering".equals(p.getName())) {
            checkbox = new UMLAssociationEndOrderingCheckBox();
        } else if ("isAsynchronous".equals(p.getName())) {
            checkbox = new UMLActionAsynchronousCheckBox();
        } else if ("isSynch".equals(p.getName())) {
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
    private void buildComboPanel(
            final JPanel panel,
            final Object target,
            final PropertyMeta prop) {
        
        JComponent comp = null;
        
        final String propertyName = prop.getName();
        if ("namespace".equals(prop.getName())) {
            final UMLComboBoxModel model =
                new UMLModelElementNamespaceComboBoxModel(propertyName, target);
            final JComboBox combo = new UMLSearchableComboBox(
                    model,
                    model.getAction(), true);            
            comp = new UMLComboBoxNavigator(
                    Translator.localize(
                    "label.namespace.navigate.tooltip"),
                    combo);
        } else if ("type".equals(prop.getName())) {
            final UMLComboBoxModel model;
            if (Model.getFacade().isATemplateParameter(target)) {
                model = new UMLStructuralFeatureTypeComboBoxModel(
                        propertyName,
                        Model.getFacade().getParameter(target));
            } else {
                model = new UMLStructuralFeatureTypeComboBoxModel(
                        propertyName,
                        target);
            }
            comp = new UMLComboBox(model);
        } else if ("base".equals(prop.getName())) {
            if (Model.getFacade().isAAssociationRole(target)) {
                final UMLComboBoxModel model = 
                    new UMLAssociationRoleBaseComboBoxModel(propertyName, target);
                final JComboBox combo = new UMLSearchableComboBox(
                        model,
                        model.getAction(), true);
                comp = new UMLComboBoxNavigator(Translator.localize(
                        "label.association.navigate.tooltip"),
                        combo);
            } else {
                //
            }
        } else if ("powertype".equals(prop.getName())) {
            final UMLComboBoxModel model = 
                new UMLGeneralizationPowertypeComboBoxModel(propertyName, target);
            final JComboBox combo = new UMLComboBox(
                    model);
            comp = combo;
        } else if ("multiplicity".equals(prop.getName())) {            
            final UMLMultiplicityPanel mPanel = new UMLMultiplicityPanel(propertyName, target);
            comp = mPanel;
        } else if ("activator".equals(prop.getName())) {
            final UMLComboBoxModel model = 
                new UMLMessageActivatorComboBoxModel(propertyName, target);
            final JComboBox combo =
                new UMLMessageActivatorComboBox(model, model.getAction());
            comp = combo;
        } else if ("operation".equals(prop.getName())) {
            if (Model.getFacade().isACallEvent(target)) {
                UMLComboBoxModel model = 
                    new UMLCallEventOperationComboBoxModel(propertyName, target);
                JComboBox combo = new UMLCallEventOperationComboBox(model);
                comp = new UMLComboBoxNavigator(Translator.localize(
                        "label.operation.navigate.tooltip"),
                        combo);
            } else {
                final UMLComboBoxModel model = 
                    new UMLCallActionOperationComboBoxModel(propertyName, target);
                UMLComboBox operationComboBox =
                    new UMLCallActionOperationComboBox(model,
                            model.getAction());
                comp = new UMLComboBoxNavigator(
                        Translator.localize("label.operation.navigate.tooltip"),
                        operationComboBox);
            }
        } else if ("representedClassifier".equals(prop.getName())) {
            final UMLComboBoxModel model = 
                new UMLCollaborationRepresentedClassifierComboBoxModel(propertyName, target);
            UMLComboBox combo = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(Translator.localize(
                            "label.represented-classifier.navigate.tooltip"),
                            combo);
        } else if ("representedOperation".equals(prop.getName())) {
            final UMLComboBoxModel model = 
                new UMLCollaborationRepresentedOperationComboBoxModel(propertyName, target);
            UMLComboBox combo = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(Translator.localize(
                            "label.represented-operation.navigate.tooltip"),
                    combo);
        } else if ("context".equals(prop.getName())) {
            final UMLComboBoxModel model = 
                new UMLStateMachineContextComboBoxModel(propertyName, target);
            UMLComboBox combo = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(Translator.localize(
                            "label.context.navigate.tooltip"),
                    combo);

        } else if ("association".equals(prop.getName())) {
            final UMLComboBoxModel model = 
                new UMLLinkAssociationComboBoxModel(propertyName, target);
            comp =  new UMLComboBoxNavigator(Translator.localize(
                        "label.association.navigate.tooltip"),                
                    new UMLSearchableComboBox(model,
                            model.getAction(), true));
        } else if ("participant".equals(prop.getName())) {
            final UMLComboBoxModel model = 
                new UMLAssociationEndTypeComboBoxModel(propertyName, target);
            comp = new UMLComboBox(model,
                    true);
        } else if ("submachine".equals(prop.getName())) {
            final UMLComboBoxModel model =
                new UMLSubmachineStateComboBoxModel(propertyName, target);
            final JComboBox submachineBox = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(Translator.localize(
                            "tooltip.nav-submachine"), submachineBox);
        } else if ("referenceState".equals(prop.getName())) {
            final UMLComboBoxModel model = 
                new UMLStubStateComboBoxModel(propertyName, target);
            final JComboBox referencestateBox =
                new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(Translator.localize(
                    "tooltip.nav-stubstate"), referencestateBox);            
        } else if ("tagType".equals(prop.getName())) {
            UMLComboBoxModel model = new UMLMetaClassComboBoxModel(propertyName, target);
            final JComboBox typeComboBox = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(
                   Translator.localize("label.type.navigate.tooltip"),
                   typeComboBox);
            // TODO: Why is this disabled always?
            comp.setEnabled(false);
        } else if ("parameter".equals(prop.getName())) {
            final UMLComboBoxModel model =
                new UMLTemplateParameterParameterComboBoxModel(target);
            final JComboBox combo = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(
                    Translator.localize("label.type.navigate.tooltip"),
                    combo);
        } else if ("defaultElement".equals(prop.getName())) {
            final UMLComboBoxModel model =
                new UMLTemplateParameterDefaultElementComboBoxModel(propertyName, target);
            final JComboBox combo = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(
                    Translator.localize("label.type.navigate.tooltip"),
                    combo);
        }
        
        if (comp != null) {
            addControl(panel, prop.getName(), comp);
        }
    }

    /**
     * @param panel a panel with a labelled text field 
     * @param target The target of the panel
     * @param prop The XML data that contains the information
     *        of the options.
     */
    private void buildTextboxPanel(JPanel panel, Object target,
            PropertyMeta prop) {
       
        UMLPlainTextDocument document = null;
        if ("name".equals(prop.getName())) {
            if (Model.getFacade().isATemplateParameter(target)) {
                target = Model.getFacade().getParameter(target);
            } 
            document = new UMLModelElementNameDocument(prop.getName(), target);
        } else if ("discriminator".equals(prop.getName())) {
            document = new UMLDiscriminatorNameDocument(prop.getName(), target);
        } else if ("location".equals(prop.getName())) {
            document = new UMLExtensionPointLocationDocument(prop.getName(), target);
        } else if ("bound".equals(prop.getName())) {
            document = new UMLSynchStateBoundDocument(prop.getName(), target);
        }
        
        if (document != null) {
            JTextField tfield = new UMLTextField(document);
            addControl(panel, prop.getName(), tfield);
        }
    }
    
    private void addControl(JPanel panel, String text, JComponent component) {
        LabelledComponent lc = new LabelledComponent(text, component);
        panel.add(lc);
    }
}
