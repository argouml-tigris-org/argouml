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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.argouml.application.ArgoVersion;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;
import org.argouml.swingext.LabelledLayout;

/**
 *  Tab pane for setting layout options.
 *
 *  @author Linus Tolke
 */
public class SettingsTabLayout extends SettingsTabHelper implements SettingsTabPanel {

    private JComboBox	_todo;
    private JComboBox	_properties;
    private JComboBox	_documentation;
    private JComboBox	_style;
    private JComboBox	_source;
    private JComboBox	_constraints;
    private JComboBox	_taggedValues;

    public SettingsTabLayout() {
        super();

        String positions[] = {"North", "South", "East"};
        setLayout(new BorderLayout());

        int labelGap = 10;
        int componentGap = 10;
        JPanel top = new JPanel(new LabelledLayout(labelGap, componentGap));

        _todo = new JComboBox(positions);
        addPositionSelector(top, "label.todo-pane", _todo);

        _properties = new JComboBox(positions);
        addPositionSelector(top, "label.properties-pane", _properties);

        _documentation = new JComboBox(positions);
        addPositionSelector(top, "label.documentation-pane", _documentation);

        _style = new JComboBox(positions);
        addPositionSelector(top, "label.style-pane", _style);

        _source = new JComboBox(positions);
        addPositionSelector(top, "label.source-pane", _source);

        _constraints = new JComboBox(positions);
        addPositionSelector(top, "label.constraints-pane", _constraints);

        _taggedValues = new JComboBox(positions);
        addPositionSelector(top, "label.tagged-values-pane", _taggedValues);

        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));		
        add(top, BorderLayout.CENTER);

        JLabel restart = createLabel("label.restart-application");
        restart.setHorizontalAlignment(SwingConstants.CENTER);
        restart.setVerticalAlignment(SwingConstants.CENTER);
        restart.setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 2));		
        add(restart, BorderLayout.SOUTH);
    }

    private void addPositionSelector(JPanel panel, String text, JComboBox combo) {
        JLabel label = createLabel(text);
        label.setLabelFor(combo);
        panel.add(label);
        panel.add(combo);
    }
    
    public void handleSettingsTabRefresh() {
    }

    public void handleSettingsTabSave() {
    }

    public void handleSettingsTabCancel() {}
    public String getModuleName() { return "SettingsTabLayout"; }
    public String getModuleDescription() { return "Positioning of components"; }
    public String getModuleAuthor() { return "ArgoUML Core"; }
    public String getModuleVersion() { return ArgoVersion.getVersion(); }
    public String getModuleKey() { return "module.settings.layout"; }
    public String getTabKey() { return "tab.layout"; }
}
