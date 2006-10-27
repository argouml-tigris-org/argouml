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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.argouml.i18n.Translator;

/**
 * Default extended configuration panel for file import.
 */
public class ConfigPanelExtension extends JPanel {

    private JPanel configPanel;

    private JRadioButton attribute;

    private JRadioButton datatype;


    /**
     * Construct a new default configuration extension panel.
     */
    public ConfigPanelExtension() {

//        configPanel = new JPanel();
        configPanel = this;
        configPanel.setLayout(new GridBagLayout());

        JLabel attributeLabel1 =
            new JLabel(
                    Translator.localize("action.import-java-attr-model"));
        configPanel.add(attributeLabel1,
                new GridBagConstraints(GridBagConstraints.RELATIVE,
                        GridBagConstraints.RELATIVE,
                        GridBagConstraints.REMAINDER,
                        1,
                        1.0, 0.0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(5, 5, 0, 5),
                        0, 0));
        ButtonGroup group1 = new ButtonGroup();
        attribute =
            new JRadioButton(
                    Translator.localize("action.import-java-UML-attr"));
        attribute.setSelected(true);
        group1.add(attribute);
        configPanel.add(attribute,
                new GridBagConstraints(GridBagConstraints.RELATIVE,
                        GridBagConstraints.RELATIVE,
                        GridBagConstraints.REMAINDER,
                        1,
                        1.0, 0.0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(0, 5, 0, 5),
                        0, 0));
        JRadioButton association =
            new JRadioButton(
                    Translator.localize("action.import-java-UML-assoc"));
        group1.add(association);
        configPanel.add(association,
                new GridBagConstraints(GridBagConstraints.RELATIVE,
                        GridBagConstraints.RELATIVE,
                        GridBagConstraints.REMAINDER,
                        1,
                        1.0, 0.0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(0, 5, 5, 5),
                        0, 0));
        JLabel attributeLabel2 =
            new JLabel(
                    Translator.localize("action.import-java-array-model"));
        configPanel.add(attributeLabel2,
                new GridBagConstraints(GridBagConstraints.RELATIVE,
                        GridBagConstraints.RELATIVE,
                        GridBagConstraints.REMAINDER,
                        1,
                        1.0, 0.0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(5, 5, 0, 5),
                        0, 0));

        ButtonGroup group2 = new ButtonGroup();
        datatype =
            new JRadioButton(
                    Translator.localize(
                            "action.import-java-array-model-datatype"));
        datatype.setSelected(true);
        group2.add(datatype);
        configPanel.add(datatype,
                new GridBagConstraints(GridBagConstraints.RELATIVE,
                        GridBagConstraints.RELATIVE,
                        GridBagConstraints.REMAINDER,
                        1,
                        1.0, 0.0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(5, 5, 0, 5),
                        0, 0));
        JRadioButton multi =
            new JRadioButton(
                    Translator.localize(
                    "action.import-java-array-model-multi"));
        group2.add(multi);
        configPanel.add(multi,
                new GridBagConstraints(GridBagConstraints.RELATIVE,
                        GridBagConstraints.RELATIVE,
                        GridBagConstraints.REMAINDER,
                        GridBagConstraints.REMAINDER,
                        1.0, 1.0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(0, 5, 5, 5),
                        0, 0));
        
//        add(configPanel);
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

}
