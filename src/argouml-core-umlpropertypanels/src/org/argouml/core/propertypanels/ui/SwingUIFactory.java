/* $Id$
 *******************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling - Post GSOC improvements
 *    Christian López Espínola
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.core.propertypanels.model.CheckBoxData;
import org.argouml.core.propertypanels.model.ControlData;
import org.argouml.core.propertypanels.model.GetterSetterManager;
import org.argouml.core.propertypanels.model.PanelData;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionDeleteModelElements;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.tigris.swidgets.GridLayout2;
import org.tigris.toolbar.ToolBarFactory;

/**
 * Creates the XML Property panels
 */
class SwingUIFactory {
	
    private static final Logger LOG = Logger.getLogger(SwingUIFactory.class);
    
    public SwingUIFactory() {
        
    }
    
    /**
     * @param target The model element selected
     * @return A Panel to be added to the main panel
     * @throws Exception If something goes wrong
     * @see org.argouml.core.propertypanels.panel.UIFactory#createGUI(java.lang.Object)
     */
    public void createGUI (
            final Object target,
            final JPanel panel) throws Exception {
        PanelData data = 
            XMLPropPanelFactory.getInstance().getPropertyPanelsData(
              	target.getClass());
            
        createLabel(target, panel);
            
        for (ControlData prop : data.getProperties()) {
            try {
                if ("text".equals(prop.getControlType())) {
                    buildTextboxPanel(panel, target, prop);
                } else if ("combo".equals(prop.getControlType())) {
                    buildComboPanel(panel, target, prop);                
                } else if ("checkgroup".equals(prop.getControlType())) {
                    buildCheckGroup(panel, target, prop);
                } else if ("optionbox".equals(prop.getControlType())) {
                    buildOptionBox(panel, target, prop);
                } else if ("singlerow".equals(prop.getControlType())) {
                    buildSingleRow(panel, target, prop);
                } else if ("list".equals(prop.getControlType())) {                    
                    buildList(panel, target, prop);
                } else if ("textarea".equals(prop.getControlType())) {
                    buildTextArea(panel, target, prop);
                } else if ("separator".equals(prop.getControlType())) {
                    panel.add(LabelledLayout.getSeparator());
                }
            } catch (Exception e) {
                throw new IllegalStateException(
            		"Exception caught building control " + prop.getControlType()
            		+ " for property " + prop.getPropertyName() + " on panel for "
            		+ target, e);
            }
        }
    }

    /**
     * Create the label with icon and description for the panel.
     * @param target
     * @param panel
     */
    private void createLabel (Object target, JPanel panel) {
        final String metaTypeName = Model.getMetaTypes().getName(target);
        final ToolBarFactory tbf = new ToolBarFactory(new Object[0]);
        tbf.setRollover(true);
        final JToolBar tb = tbf.createToolBar();
        final String label;
        
        if (Model.getFacade().isAPseudostate(target)) {
        	// TODO: We need some way of driving this from panel xml rather
        	// than hard coded test
        	Object pseudostateKind = Model.getFacade().getKind(target);
        	label = Model.getFacade().getName(pseudostateKind);
        } else {
            label = metaTypeName;
        }
    	tb.add(new JLabel(label, ResourceLoaderWrapper.lookupIconResource(label), JLabel.LEFT));
        if (!Model.getModelManagementHelper().isReadOnly(target)) {
            tb.add(new NavigateUpAction(target));
            
            // TODO: This should not be hard coded but should be driven from the panel xml
            if (Model.getFacade().isAAttribute(target)
                || Model.getFacade().isAOperation(target)
                || Model.getFacade().isAReception(target)
                || Model.getFacade().isAParameter(target)
                || Model.getFacade().isAAssociationEnd(target)) {
                tb.add(new NavigatePreviousAction(target));
                tb.add(new NavigateNextAction(target));
            }
            
            tb.add(new ActionDeleteModelElements());
            // We only have this here until we have stereotypes
            // list on property panel
            tb.add(new ActionNewStereotype());
        }
        panel.add(tb);
    }
    
    private void buildTextArea(
            final JPanel panel,
            final Object target, 
            final ControlData prop) {
        
        // TODO: Why do we need this as well as control? Why is it
        // instantiated when its not always needed.
        JPanel p = new JPanel();
        
        final TitledBorder border = new TitledBorder(prop.getPropertyName());        
        p.setBorder(border);

        JComponent control = null;
        
        final String propertyName = prop.getPropertyName();
        
        if ("initialValue".equals(prop.getPropertyName())) {        
            UMLExpressionModel model = 
                new UMLInitialValueExpressionModel(target);
            p  = new UMLExpressionPanel(model, prop.getPropertyName());
            control = p;
        } else if ("defaultValue".equals(prop.getPropertyName())) {
            UMLExpressionModel model = 
                new UMLDefaultValueExpressionModel(target);
            p  = new UMLExpressionPanel(model, prop.getPropertyName());
            control = p;
        } else if ("specification".equals(prop.getPropertyName())) {
            UMLPlainTextDocument document = 
                new UMLOperationSpecificationDocument(prop.getPropertyName(), target);
            UMLTextArea osta = new UMLTextArea(document);
            osta.setRows(3);
            control = new JScrollPane(osta);
        } else if ("body".equals(prop.getPropertyName()) && "String".equals(prop.getType())) {
            UMLPlainTextDocument document = new UMLCommentBodyDocument(prop.getPropertyName(), target);
            UMLTextArea text = new UMLTextArea(document);
            text.setLineWrap(true);
            text.setRows(5);
            control = new JScrollPane(text);
        } else if ("condition".equals(prop.getPropertyName())) {
            UMLExpressionModel conditionModel =
                new UMLConditionExpressionModel(target);
            JTextArea conditionArea =
                new UMLExpressionBodyField(conditionModel, true);
            conditionArea.setRows(5);
            control = new JScrollPane(conditionArea);
        } else if ("script".equals(prop.getPropertyName())) {
            UMLExpressionModel scriptModel =
                new UMLScriptExpressionModel(target);            
            p  = new UMLExpressionPanel(scriptModel, prop.getPropertyName());
            control = p;
        } else if ("recurrence".equals(prop.getPropertyName())) {
            UMLExpressionModel recurrenceModel =
                new UMLRecurrenceExpressionModel(target);            
            p  = new UMLExpressionPanel(recurrenceModel, prop.getPropertyName());
            control = p;
        } else if ("expression".equals(prop.getPropertyName())) {
            UMLExpressionModel model = new UMLExpressionExpressionModel(target);
            p = new UMLExpressionPanel(model, prop.getPropertyName());
            control = p;
        } else if ("changeExpression".equals(prop.getPropertyName())) {
            UMLExpressionModel model = new UMLChangeExpressionModel(target);
            p = new UMLExpressionPanel(model, prop.getPropertyName());
            control = p;
        } else if ("when".equals(prop.getPropertyName())) {
            UMLExpressionModel model = new UMLTimeExpressionModel(target);
            p = new UMLExpressionPanel(model, prop.getPropertyName());
            control = p;
        }
        
        if (control != null) {
            if (control == p) {
                // if the control is a panel, add it
                addControl(panel, null, control, target);
            } else {
                // if not, it is a control and must be labeled...
                addControl(panel, Translator.localize(prop.getLabel()),
                		control, target);
            }
        } else {
            final GetterSetterManager getterSetter = 
            	GetterSetterManager.getGetterSetter(prop.getType());

            if (getterSetter.contains(propertyName)) {
                ExpressionModel model =
                	new ExpressionModel(propertyName, prop.getTypes().get(0), target, getterSetter);
                final JTextField languageField = 
                    new ExpressionLanguageField(model);
                addControl(
                        panel,
                        Translator.localize("label.language"),
                        languageField, target);
                control = new JScrollPane(new ExpressionBodyField(model));
                addControl(panel, null, control, target);
            }
        }
    }

    private void buildSingleRow(JPanel panel, Object target,
            ControlData prop) {
        
        final SingleListFactory factory = new SingleListFactory();
        final JComponent pane =
            factory.createComponent(target, prop.getPropertyName(), prop.getTypes());
        
        if (pane != null) {
            addControl(panel, Translator.localize(prop.getLabel()), pane, target);
        }
    }

    private void buildList(
            final JPanel panel, Object target, 
            final ControlData prop) {
        
        final ListFactory factory = new ListFactory();
        final JComponent list =
            factory.createComponent(target, prop.getPropertyName(), prop.getTypes());

        if (list != null) {
            addControl(panel, Translator.localize(prop.getLabel()), list, target);
        }
    }

    /**
     * @param target The target of the panel
     * @param prop The XML data that contains the information
     *        of the options.
     * @return a radio button panel with the options 
     */
    private void buildOptionBox(JPanel panel, Object target,
            ControlData prop) {
        
        final String propertyName = prop.getPropertyName();

        final GetterSetterManager getterSetter = GetterSetterManager.getGetterSetter(prop.getType());

        if (getterSetter.contains(propertyName)) {
            JPanel control = new RadioButtonPanel(
                    target, 
                    propertyName, 
                    true, 
                    getterSetter);
            addControl(panel, null, control, target);
        }
    }

    /**
     * @param target The target of the checkbox group
     * @param prop The XML data that contains the information
     *        of the checkboxes.
     * @return a panel that contains the checkboxes 
     */
    private void buildCheckGroup(
	    final JPanel panel,
	    final Object target,
            final ControlData prop) {
        JPanel p = new JPanel(new GridLayout2());
        TitledBorder border = new TitledBorder(prop.getPropertyName());        
        p.setBorder(border);
        
        if ("modifiers".equals(prop.getPropertyName())) {  
            for (CheckBoxData data : prop.getCheckboxes()) {
                buildCheckBox(p, target, data);
            }                            
        }
        addControl(panel, null, p, target);
    }

    private void buildCheckBox(
            final JPanel panel,
            final Object target,
            final CheckBoxData prop) {
        
        final String propertyName = prop.getPropertyName();
        
        final GetterSetterManager getterSetter =
            GetterSetterManager.getGetterSetter(prop.getType());

        final String label = Translator.localize(prop.getLabel());
        
        if (getterSetter.contains(propertyName)) {
        	final CheckBox cb =
        		new CheckBox(label, target, propertyName, getterSetter);
        	if (Model.getModelManagementHelper().isReadOnly(target)) {
        		cb.setEnabled(false);
        	}
        	
            panel.add(cb);
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
            final ControlData prop) {
        
        JComponent comp = null;
        
        final String propertyName = prop.getPropertyName();
        if ("namespace".equals(prop.getPropertyName())) {
            final UMLComboBoxModel model =
                new UMLModelElementNamespaceComboBoxModel(propertyName, target);
            final UMLComboBox combo = new UMLSearchableComboBox(
                    model,
                    model.getAction(), true);            
            comp = new UMLComboBoxNavigator(
                    Translator.localize(
                    "label.namespace.navigate.tooltip"),
                    combo);
        } else if ("type".equals(prop.getPropertyName())) {
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
        } else if ("base".equals(prop.getPropertyName())) {
            if (Model.getFacade().isAAssociationRole(target)) {
                final UMLComboBoxModel model = 
                    new UMLAssociationRoleBaseComboBoxModel(propertyName, target);
                final UMLComboBox combo = new UMLSearchableComboBox(
                        model,
                        model.getAction(), true);
                comp = new UMLComboBoxNavigator(Translator.localize(
                        "label.association.navigate.tooltip"),
                        combo);
            } else {
                //
            }
        } else if ("powertype".equals(prop.getPropertyName())) {
            final UMLComboBoxModel model = 
                new UMLGeneralizationPowertypeComboBoxModel(propertyName, target);
            final UMLComboBox combo = new UMLComboBox(
                    model);
            comp = combo;
        } else if ("multiplicity".equals(prop.getPropertyName())) {            
            final UMLMultiplicityPanel mPanel = new UMLMultiplicityPanel(propertyName, target);
            comp = mPanel;
        } else if ("activator".equals(prop.getPropertyName())) {
            final UMLComboBoxModel model = 
                new UMLMessageActivatorComboBoxModel(propertyName, target);
            final JComboBox combo =
                new UMLMessageActivatorComboBox(model, model.getAction());
            comp = combo;
        } else if ("operation".equals(prop.getPropertyName())) {
            if (Model.getFacade().isACallEvent(target)) {
                UMLComboBoxModel model = 
                    new UMLCallEventOperationComboBoxModel(propertyName, target);
                UMLComboBox combo = new UMLComboBox(model);
                comp = new UMLComboBoxNavigator(Translator.localize(
                        "label.operation.navigate.tooltip"),
                        combo);
            } else {
                final UMLComboBoxModel model = 
                    new UMLCallActionOperationComboBoxModel(propertyName, target);
                UMLComboBox combo =
                    new UMLComboBox(model);
                comp = new UMLComboBoxNavigator(Translator.localize(
                	"label.operation.navigate.tooltip"),
                        combo);
            }
        } else if ("classifier".equals(prop.getPropertyName())) {
            final UMLComboBoxModel model = 
                new UMLComponentInstanceClassifierComboBoxModel(propertyName, target);
            UMLComboBox combo = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(Translator.localize(
                            "label.component-instance.navigate.tooltip"),
                            combo);
        } else if ("representedClassifier".equals(prop.getPropertyName())) {
            final UMLComboBoxModel model = 
                new UMLCollaborationRepresentedClassifierComboBoxModel(propertyName, target);
            UMLComboBox combo = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(Translator.localize(
                            "label.represented-classifier.navigate.tooltip"),
                            combo);
        } else if ("representedOperation".equals(prop.getPropertyName())) {
            final UMLComboBoxModel model = 
                new UMLCollaborationRepresentedOperationComboBoxModel(propertyName, target);
            UMLComboBox combo = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(Translator.localize(
                            "label.represented-operation.navigate.tooltip"),
                    combo);
        } else if ("context".equals(prop.getPropertyName())) {
            final UMLComboBoxModel model;
            if (Model.getFacade().isAActivityGraph(target)) {
                model = 
                    new UMLActivityGraphContextComboBoxModel(propertyName, target);
            } else {
                model = 
                    new UMLStateMachineContextComboBoxModel(propertyName, target);
            }
            UMLComboBox combo = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(Translator.localize(
                            "label.context.navigate.tooltip"),
                    combo);

        } else if ("association".equals(prop.getPropertyName())) {
            final UMLComboBoxModel model = 
                new UMLLinkAssociationComboBoxModel(propertyName, target);
            comp =  new UMLComboBoxNavigator(Translator.localize(
                        "label.association.navigate.tooltip"),                
                    new UMLSearchableComboBox(model,
                            model.getAction(), true));
        } else if ("participant".equals(prop.getPropertyName())) {
            final UMLComboBoxModel model = 
                new UMLAssociationEndTypeComboBoxModel(propertyName, target);
            comp = new UMLComboBox(model,
                    true);
        } else if ("submachine".equals(prop.getPropertyName())) {
            final UMLComboBoxModel model =
                new UMLSubmachineStateComboBoxModel(propertyName, target);
            final UMLComboBox submachineBox = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(Translator.localize(
                            "tooltip.nav-submachine"), submachineBox);
        } else if ("referenceState".equals(prop.getPropertyName())) {
            final UMLComboBoxModel model = 
                new UMLStubStateComboBoxModel(propertyName, target);
            final UMLComboBox referencestateBox =
                new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(Translator.localize(
                    "tooltip.nav-stubstate"), referencestateBox);            
        } else if ("tagType".equals(prop.getPropertyName())) {
            UMLComboBoxModel model = new UMLMetaClassComboBoxModel(propertyName, target);
            final UMLComboBox typeComboBox = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(
                   Translator.localize("label.type.navigate.tooltip"),
                   typeComboBox);
            // TODO: Why is this disabled always?
            comp.setEnabled(false);
        } else if ("parameter".equals(prop.getPropertyName())) {
            final UMLComboBoxModel model =
                new UMLTemplateParameterParameterComboBoxModel(target);
            final UMLComboBox combo = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(
                    Translator.localize("label.type.navigate.tooltip"),
                    combo);
        } else if ("defaultElement".equals(prop.getPropertyName())) {
            final UMLComboBoxModel model =
                new UMLTemplateParameterDefaultElementComboBoxModel(propertyName, target);
            final UMLComboBox combo = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(
                    Translator.localize("label.type.navigate.tooltip"),
                    combo);
        } else if ("signal".equals(prop.getPropertyName())) {
            final UMLComboBoxModel model =
                new UMLSignalComboBoxModel(propertyName, target);
            final UMLComboBox combo = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(
                    Translator.localize("label.type.navigate.tooltip"),
                    combo);
        } else if ("trigger".equals(prop.getPropertyName())) {
            final UMLComboBoxModel model =
                new UMLTransitionTriggerComboBoxModel(propertyName, target);
            final UMLComboBox combo = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(
                    Translator.localize("label.type.navigate.tooltip"),
                    combo);
        } else if ("specification".equals(prop.getPropertyName())) {
            final UMLComboBoxModel model =
                new UMLMethodSpecificationComboBoxModel(propertyName, target);
            final UMLComboBox combo = new UMLComboBox(model);
            comp = new UMLComboBoxNavigator(
                    Translator.localize("label.type.navigate.tooltip"),
                    combo);
        }
        
        if (comp != null) {
            addControl(panel, Translator.localize(prop.getLabel()),
            		comp, target);
        }
    }

    /**
     * @param panel a panel with a labelled text field 
     * @param target The target of the panel
     * @param prop The XML data that contains the information
     *        of the options.
     */
    private void buildTextboxPanel(JPanel panel, Object target,
            ControlData prop) {
       
        UMLPlainTextDocument document = null;
        if ("name".equals(prop.getPropertyName())) {
            if (Model.getFacade().isATemplateParameter(target)) {
                target = Model.getFacade().getParameter(target);
            }
            document = new UMLModelElementNameDocument(prop.getPropertyName(), target);
        } else if ("discriminator".equals(prop.getPropertyName())) {
            document = new UMLDiscriminatorNameDocument(prop.getPropertyName(), target);
        } else if ("location".equals(prop.getPropertyName())) {
            document = new UMLExtensionPointLocationDocument(prop.getPropertyName(), target);
        } else if ("bound".equals(prop.getPropertyName())) {
            document = new UMLSynchStateBoundDocument(prop.getPropertyName(), target);
        }
        
        if (document != null) {
            JTextField tfield = new UMLTextField(document);
            addControl(panel, Translator.localize(prop.getLabel()),
            		tfield, target);
        }
    }
    
    private void addControl(
    		final JPanel panel,
    		final String text,
    		final JComponent component,
    		final Object target) {
    	if (Model.getModelManagementHelper().isReadOnly(target)) {
    		component.setEnabled(false);
    	}

        LabelledComponent lc = new LabelledComponent(text, component);
        panel.add(lc);
    }
}
