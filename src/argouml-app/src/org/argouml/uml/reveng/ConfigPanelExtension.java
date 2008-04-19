// $Id: FileImportSupport.java 11168 2006-09-14 20:35:24Z andrea_nironi $
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

package org.argouml.uml.reveng;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.i18n.Translator;

/**
 * Default extended configuration panel for file import.
 */
public class ConfigPanelExtension extends JPanel {

    /**
     * Key for RE extended settings: model attributes as:
     * 0: attibutes
     * 1: associations
     */
    public static final ConfigurationKey KEY_IMPORT_EXTENDED_MODEL_ATTR =
        Configuration
            .makeKey("import", "extended", "java", "model", "attributes");

    /**
     * Key for RE extended settings: model arrays as:
     * 0: datatype
     * 1: associations
     */
    public static final ConfigurationKey KEY_IMPORT_EXTENDED_MODEL_ARRAYS =
        Configuration.makeKey("import", "extended", "java", "model", "arrays");

    /**
     * Key for RE extended settings: flag for modelling of listed collections,
     * if to model them as associations with multiplicity *.
     */
    public static final ConfigurationKey KEY_IMPORT_EXTENDED_COLLECTIONS_FLAG =
        Configuration
            .makeKey("import", "extended", "java", "collections", "flag");

    /**
     * Key for RE extended settings: list of collections, that will be modelled
     * as associations with multiplicity *.
     */
    public static final ConfigurationKey KEY_IMPORT_EXTENDED_COLLECTIONS_LIST =
        Configuration
            .makeKey("import", "extended", "java", "collections", "list");

    /**
     * Key for RE extended settings: flag for modelling of listed collections,
     * if to model them as ordered associations with multiplicity *.
     */
    public static final ConfigurationKey KEY_IMPORT_EXTENDED_ORDEREDCOLLS_FLAG =
        Configuration
            .makeKey("import", "extended", "java", "orderedcolls", "flag");

    /**
     * Key for RE extended settings: list of collections, that will be modelled
     * as ordered associations with multiplicity *.
     */
    public static final ConfigurationKey KEY_IMPORT_EXTENDED_ORDEREDCOLLS_LIST =
        Configuration
            .makeKey("import", "extended", "java", "orderedcolls", "list");

    private JPanel configPanel;

    private JRadioButton attribute;

    private JRadioButton datatype;

    private JCheckBox modelcollections, modelorderedcollections;

    private JTextField collectionlist, orderedcollectionlist;

    /**
     * Construct a new default configuration extension panel.
     */
    public ConfigPanelExtension() {

        configPanel = this;
        configPanel.setLayout(new GridBagLayout());

        JLabel attributeLabel1 =
            new JLabel(
                    Translator.localize("action.import-java-attr-model"));
        configPanel.add(attributeLabel1,
                createGridBagConstraints(true, false, false));
        ButtonGroup group1 = new ButtonGroup();
        attribute =
            new JRadioButton(
                    Translator.localize("action.import-java-UML-attr"));
        group1.add(attribute);
        configPanel.add(attribute,
                createGridBagConstraints(false, false, false));
        JRadioButton association =
            new JRadioButton(
                    Translator.localize("action.import-java-UML-assoc"));
        group1.add(association);
        configPanel.add(association,
                createGridBagConstraints(false, true, false));
        String modelattr =
            Configuration.getString(KEY_IMPORT_EXTENDED_MODEL_ATTR);
        if ("1".equals(modelattr)) {
            association.setSelected(true);
        } else {
            attribute.setSelected(true);
        }
        
        JLabel attributeLabel2 =
            new JLabel(
                    Translator.localize("action.import-java-array-model"));
        configPanel.add(attributeLabel2,
                createGridBagConstraints(true, false, false));
        ButtonGroup group2 = new ButtonGroup();
        datatype =
            new JRadioButton(
                    Translator.localize(
                            "action.import-java-array-model-datatype"));
        group2.add(datatype);
        configPanel.add(datatype,
                createGridBagConstraints(false, false, false));
        JRadioButton multi =
            new JRadioButton(
                    Translator.localize(
                    "action.import-java-array-model-multi"));
        group2.add(multi);
        configPanel.add(multi,
                createGridBagConstraints(false, true, false));
        String modelarrays =
            Configuration.getString(KEY_IMPORT_EXTENDED_MODEL_ARRAYS);
        if ("1".equals(modelarrays)) {
            multi.setSelected(true);
        } else {
            datatype.setSelected(true);
        }

        String s = Configuration
                .getString(KEY_IMPORT_EXTENDED_COLLECTIONS_FLAG);
        boolean flag = ("true".equals(s));
        modelcollections =
            new JCheckBox(Translator.localize(
                    "action.import-option-model-collections"), flag);
        configPanel.add(modelcollections,
                createGridBagConstraints(true, false, false));

        s = Configuration.getString(KEY_IMPORT_EXTENDED_COLLECTIONS_LIST);
        collectionlist = new JTextField(s);
        configPanel.add(collectionlist,
                createGridBagConstraints(false, false, true));
        JLabel listLabel =
            new JLabel(
                    Translator.localize("action.import-comma-separated-names"));
        configPanel.add(listLabel,
                createGridBagConstraints(false, true, false));

        s = Configuration.getString(KEY_IMPORT_EXTENDED_ORDEREDCOLLS_FLAG);
        flag = ("true".equals(s));
        modelorderedcollections =
            new JCheckBox(Translator.localize(
                    "action.import-option-model-ordered-collections"), flag);
        configPanel.add(modelorderedcollections,
                createGridBagConstraints(true, false, false));

        s = Configuration.getString(KEY_IMPORT_EXTENDED_ORDEREDCOLLS_LIST);
        orderedcollectionlist = new JTextField(s);
        configPanel.add(orderedcollectionlist,
                createGridBagConstraints(false, false, true));
        listLabel =
            new JLabel(
                    Translator.localize("action.import-comma-separated-names"));
        configPanel.add(listLabel,
                createGridBagConstraintsFinal());

        // TODO: Get the list of extended settings from the current
        // language importer and add those too
    }


    /**
     * Create a GridBagConstraints object to use with the layout.
     * 
     * @param topInset true to use a top inset 
     * @param bottomInset true to use a bottom inset
     * @param fill true to fill (horizontally)
     * @return the grid bag constraints
     */
    private GridBagConstraints createGridBagConstraints(boolean topInset,
            boolean bottomInset, boolean fill) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = fill ? GridBagConstraints.HORIZONTAL
                : GridBagConstraints.NONE;
        gbc.insets = 
            new Insets(
                    topInset ? 5 : 0, 
                    5, 
                    bottomInset ? 5 : 0, 
                    5);
        gbc.ipadx = 0;
        gbc.ipady = 0;
        return gbc;
    }

    /**
     * A GridBagConstraints for the last item to take up the rest of the space.
     * @return the GridBagConstraints object
     */
    private GridBagConstraints createGridBagConstraintsFinal() {
        GridBagConstraints gbc = createGridBagConstraints(false, true, false);
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.weighty = 1.0;
        return gbc;
    }

    /**
     * @return the attribute radio button
     */
    public JRadioButton getAttribute() {
        return attribute;
    }

    /**
     * @return the datatype radio button
     */
    public JRadioButton getDatatype() {
        return datatype;
    }

    public void disposeDialog() {
        Configuration.setString(KEY_IMPORT_EXTENDED_MODEL_ATTR,
                String.valueOf(getAttribute().isSelected() ? "0" : "1"));
        Configuration.setString(KEY_IMPORT_EXTENDED_MODEL_ARRAYS,
                String.valueOf(getDatatype().isSelected() ? "0" : "1"));
        Configuration.setString(KEY_IMPORT_EXTENDED_COLLECTIONS_FLAG,
                String.valueOf(modelcollections.isSelected()));
        Configuration.setString(KEY_IMPORT_EXTENDED_COLLECTIONS_LIST,
                String.valueOf(collectionlist.getText()));
        Configuration.setString(KEY_IMPORT_EXTENDED_ORDEREDCOLLS_FLAG,
                String.valueOf(modelorderedcollections.isSelected()));
        Configuration.setString(KEY_IMPORT_EXTENDED_ORDEREDCOLLS_LIST,
                String.valueOf(orderedcollectionlist.getText()));
    }
}
