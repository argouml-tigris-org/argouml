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

package org.argouml.core.propertypanels.panel;

import java.awt.Dimension;
import java.io.InputStream;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.argouml.core.propertypanels.ui.ActionAddStereotypeBaseClass;
import org.argouml.core.propertypanels.ui.ActionDeleteStereotypeBaseClass;
import org.argouml.core.propertypanels.ui.EnumerationListModel;
import org.argouml.core.propertypanels.ui.UMLClassifierPackageImportsListModel;
import org.argouml.core.propertypanels.ui.UMLDefaultValueExpressionModel;
import org.argouml.core.propertypanels.ui.UMLExpressionModel3;
import org.argouml.core.propertypanels.ui.UMLExpressionPanel;
import org.argouml.core.propertypanels.ui.UMLExtendedElementsListModel;
import org.argouml.core.propertypanels.ui.UMLInitialValueExpressionModel;
import org.argouml.core.propertypanels.ui.UMLMultiplicityPanel;
import org.argouml.core.propertypanels.ui.UMLStereotypeBaseClassListModel;
import org.argouml.core.propertypanels.ui.UMLStereotypeTagDefinitionListModel;
import org.argouml.core.propertypanels.xml.XMLPropertyPanelsData;
import org.argouml.core.propertypanels.xml.XMLPropertyPanelsDataRecord;
import org.argouml.core.propertypanels.xml.XMLPropertyPanelsHandler;
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
import org.argouml.uml.ui.foundation.core.ActionAddClientDependencyAction;
import org.argouml.uml.ui.foundation.core.ActionAddSupplierDependencyAction;
import org.argouml.uml.ui.foundation.core.ActionSetGeneralizationPowertype;
import org.argouml.uml.ui.foundation.core.ActionSetModelElementNamespace;
import org.argouml.uml.ui.foundation.core.ActionSetStructuralFeatureType;
import org.argouml.uml.ui.foundation.core.UMLAssociationAssociationRoleListModel;
import org.argouml.uml.ui.foundation.core.UMLAssociationConnectionListModel;
import org.argouml.uml.ui.foundation.core.UMLAssociationLinkListModel;
import org.argouml.uml.ui.foundation.core.UMLBehavioralFeatureQueryCheckBox;
import org.argouml.uml.ui.foundation.core.UMLClassActiveCheckBox;
import org.argouml.uml.ui.foundation.core.UMLClassAttributeListModel;
import org.argouml.uml.ui.foundation.core.UMLClassOperationListModel;
import org.argouml.uml.ui.foundation.core.UMLClassifierAssociationEndListModel;
import org.argouml.uml.ui.foundation.core.UMLClassifierFeatureListModel;
import org.argouml.uml.ui.foundation.core.UMLClassifierParameterListModel;
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
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

// TODO: This class will be an interface or abstract class
// and will be implemented by SwingUIFactory and SwtUIFactory.
/**
 * Creates the XML Property panels
 * <<factory>><<singleton>>
 */
public class UIFactory {
    
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(UIFactory.class);
    
    private static UIFactory instance = new UIFactory();
    
    private JPanel panel;
    
    public UIFactory() {
        
    }
    
    public static UIFactory getInstance() {        
        return instance;
    }
    
    // TODO: This will be a template method, where there will be two
    // implementations, for Swing and for SWT.
    // TODO: This should take care of diagrams? or only model
    // elements?
    /**
     * Creates a panel based on the target, using a XML that
     * describes the UI of the panel.     * 
     * @param target The model element selected
     * @return A Panel to be added to the main panel
     * @throws Exception If something goes wrong
     */
    public JPanel createGUI (Object target) throws Exception {
        String filename = getXMLFileName(target);
        LOG.info("[XMLPP] filename is:" + filename);
        XMLPropertyPanelsData data = parseXML(filename);
        buildPanel(data, target);
        return panel;       
    }
    
    private JPanel buildPanel(XMLPropertyPanelsData data, Object target) {
        
        panel = new JPanel(new LabelledLayout());
        
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
        
        if ("initial_value".equals(prop.getName())) {        
            UMLExpressionModel3 model = new UMLInitialValueExpressionModel();
            // model.setTarget(target);

            p  = new UMLExpressionPanel(model, prop.getName());
            control = p;
        }
        if ("default_value".equals(prop.getName())) {        
            UMLExpressionModel3 model = new UMLDefaultValueExpressionModel();
            // model.setTarget(target);

            p  = new UMLExpressionPanel(model, prop.getName());
            control = p;
        }
        else if ("specification".equals(prop.getName())) {
            UMLPlainTextDocument document = 
                new UMLOperationSpecificationDocument();
            document.setTarget(target);
            UMLTextArea2 osta = new UMLTextArea2(
                    new UMLOperationSpecificationDocument());
            osta.setRows(3);
            control = new JScrollPane(osta);
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
        if ("client_dependency".equals(prop.getName())) {
            model = new UMLModelElementClientDependencyListModel();
            model.setTarget(target); 
            list = new ScrollList(new UMLMutableLinkedList(
                    model,
                    new ActionAddClientDependencyAction(),
                    null,
                    null,
                    true));
        }
        else if ("supplier_dependency".equals(prop.getName())) {
            model = new UMLModelElementSupplierDependencyListModel();
            model.setTarget(target);
            list = new ScrollList(new UMLMutableLinkedList(
                    model,
                    new ActionAddSupplierDependencyAction(),
                    null,
                    null,
                    true));
        }
        else if ("generalizations".equals(prop.getName())) {
            model = new UMLGeneralizableElementGeneralizationListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("specializations".equals(prop.getName())) {
            model = new UMLGeneralizableElementSpecializationListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("attributes".equals(prop.getName())) {
            model = new UMLClassAttributeListModel();
            model.setTarget(target);
            list = new ScrollList(model, true, false);
        }
        else if ("association_ends".equals(prop.getName())) {
            model = new UMLClassifierAssociationEndListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("features".equals(prop.getName())) {
            model = new UMLClassifierFeatureListModel();
            model.setTarget(target);
            list = new ScrollList(model, true, false);
        }
        else if ("operations".equals(prop.getName())) {
            model = new UMLClassOperationListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("owned".equals(prop.getName())) {
            model = new UMLNamespaceOwnedElementListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("imported_elements".equals(prop.getName())) {
            model = new UMLClassifierPackageImportsListModel();
            model.setTarget(target);
            list = new ScrollList(new UMLMutableLinkedList(model,
                    // TODO: It's OK to change the visibility of this actions?
                    null, // new ActionAddPackageImport(),
                    null,
                    null, //new ActionRemovePackageImport(),
                    true));
        }
        else if ("parameters".equals(prop.getName())) {
            model = new UMLClassifierParameterListModel();
            model.setTarget(target);
            list = new ScrollList(new UMLLinkedList(model, 
                    true, false));
        }
        else if ("raised_signals".equals(prop.getName())) {
            model = new UMLOperationRaisedSignalsListModel();
            model.setTarget(target);
            list = new ScrollList(new UMLLinkedList(model, 
                    true, false));
        }
        else if ("methods".equals(prop.getName())) {
            model = new UMLOperationMethodsListModel();
            model.setTarget(target);
            list = new ScrollList(new UMLLinkedList(model, 
                    true, false));
        }
        else if ("tag_definitions".equals(prop.getName())) {
            model = new UMLStereotypeTagDefinitionListModel();
            model.setTarget(target);
            list = new ScrollList(new UMLLinkedList(model,
                    true, false));
        }
        else if ("base_class".equals(prop.getName())) {
            model = new UMLStereotypeBaseClassListModel();
            model.setTarget(target);
            UMLMutableLinkedList l = new UMLMutableLinkedList(
                    new UMLStereotypeBaseClassListModel(), 
                    new ActionAddStereotypeBaseClass(),
                    null,
                    new ActionDeleteStereotypeBaseClass(),
                    true);
            l.setCellRenderer(new DefaultListCellRenderer());
            list = new ScrollList(l
                   );
        }
        else if ("extended_elements".equals(prop.getName())) {
            model = new UMLExtendedElementsListModel();
            model.setTarget(target);
            list = new ScrollList( new UMLLinkedList(model, 
                    true, true));
        }
        else if ("literals".equals(prop.getName())) {
            model = new UMLEnumerationLiteralsListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("suppliers".equals(prop.getName())) {
            model = new UMLDependencySupplierListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("clients".equals(prop.getName())) {
            model = new UMLDependencyClientListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("connections".equals(prop.getName())) {
            model = new UMLAssociationConnectionListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("association_roles".equals(prop.getName())) {
            model = new UMLAssociationAssociationRoleListModel();
            model.setTarget(target);
            list = new ScrollList(model);
        }
        else if ("links".equals(prop.getName())) {
            model = new UMLAssociationLinkListModel();
            model.setTarget(target);
            list = new ScrollList(model);
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
            UMLRadioButtonPanel cPanel =   
                new UMLStructuralFeatureChangeabilityRadioButtonPanel(
                    Translator.localize("label.changeability"), 
                    true); 
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
        else if ("direction_kind".equals(prop.getName())) {
            UMLRadioButtonPanel cPanel = 
                new UMLParameterDirectionKindRadioButtonPanel(
                    Translator.localize("label.parameter.kind"), true);
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
        
        if ("abstract".equals(p.getName())) {
            checkbox = new UMLGeneralizableElementAbstractCheckBox();
        }
        else if ("leaf".equals(p.getName())) {
            checkbox = new UMLGeneralizableElementLeafCheckBox();
        }
        else if ("root".equals(p.getName())) {
            checkbox = new UMLGeneralizableElementRootCheckBox(); 
        }
        else if ("derived".equals(p.getName())) {
            checkbox = new UMLDerivedCheckBox();
        }
        else if ("active".equals(p.getName())) {
            checkbox = new UMLClassActiveCheckBox();    
        }        
        else if ("static".equals(p.getName())) {
            checkbox = new UMLFeatureOwnerScopeCheckBox();    
        }
        else if ("query".equals(p.getName())) {
            checkbox = new UMLBehavioralFeatureQueryCheckBox();
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


    private String getXMLFileName(Object target) {
        return Model.getMetaTypes().getName(target);
    }

    private XMLPropertyPanelsData parseXML(String filename) 
        throws Exception {
        
        XMLPropertyPanelsData data = new XMLPropertyPanelsData();

        XMLReader parser = XMLReaderFactory.createXMLReader();
        parser.setContentHandler(new XMLPropertyPanelsHandler(data));

        String file = "org/argouml/core/propertypanels/xml/"
            + filename + ".xml";
        LOG.debug("UIFactory creates PropPanel with file " + file);
        InputStream stream = this.getClass().getClassLoader().
            getResourceAsStream(file);
        if (stream != null) {
            InputSource source = new InputSource(stream);
            parser.parse(source);        
        }
        return data;
    }
    

}
