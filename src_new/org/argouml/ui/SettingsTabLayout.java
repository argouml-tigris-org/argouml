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
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.argouml.application.ArgoVersion;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.ConfigurationKey;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;
import org.argouml.cognitive.ui.TabToDo;
import org.argouml.swingext.Property;
import org.argouml.swingext.PropertyTable;
import org.argouml.uml.ui.TabConstraints;
import org.argouml.uml.ui.TabDocumentation;
import org.argouml.uml.ui.TabProps;
import org.argouml.uml.ui.TabSrc;
import org.argouml.uml.ui.TabStyle;
import org.argouml.uml.ui.TabTaggedValues;

/**
 *  Tab pane for setting layout options.
 *
 *  @author Linus Tolke
 */
public class SettingsTabLayout extends SettingsTabHelper implements SettingsTabPanel {

    private Property	_todo;
    private Property	_properties;
    private Property	_documentation;
    private Property	_style;
    private Property	_source;
    private Property	_constraints;
    private Property	_taggedValues;

    public SettingsTabLayout() {
        super();
        setLayout(new BorderLayout());

        // TODO Localize these
        final String positions[] = {"North", "South", "East"};        
        final String paneColumnHeader = "Pane";
        final String positionColumnHeader = "Position";

        JPanel top = new JPanel(new BorderLayout());

        _todo = createProperty("label.todo-pane", positions, TabToDo.class);
        _properties = createProperty("label.properties-pane", positions, TabProps.class);
        _documentation = createProperty("label.documentation-pane", positions, TabDocumentation.class);
        _style = createProperty("label.style-pane", positions, TabStyle.class);
        _source = createProperty("label.source-pane", positions, TabSrc.class);
        _constraints = createProperty("label.constraints-pane", positions, TabConstraints.class);
        _taggedValues = createProperty("label.tagged-values-pane", positions, TabTaggedValues.class);
        
        Property[] properties = new Property[] {
            _todo, _properties, _documentation, _style, _source, _constraints, _taggedValues
        };
        Arrays.sort(properties);
        
        top.add(new JScrollPane(new PropertyTable(
            properties, paneColumnHeader, positionColumnHeader)), BorderLayout.CENTER);
       
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));		
        add(top, BorderLayout.CENTER);

        JLabel restart = createLabel("label.restart-application");
        restart.setHorizontalAlignment(SwingConstants.CENTER);
        restart.setVerticalAlignment(SwingConstants.CENTER);
        restart.setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 2));		
        add(restart, BorderLayout.SOUTH);
    }

    /**
     * Create a Property for the position of the given tab pane, selecting
     * the current display value from the user properties file.
     */
    private Property createProperty(String text, String[] positions, Class tab) {     
        ConfigurationKey key = makeKey(tab);
        String currentValue = Configuration.getString(key, "South");        
        return new Property(localize(text), String.class, currentValue, positions);
    }

    private void loadPosition(Property position, Class tab) {     
        ConfigurationKey key = makeKey(tab);
        position.setCurrentValue(Configuration.getString(key, "South"));
    }
        
    private void savePosition(Property position, Class tab) {
        ConfigurationKey key = makeKey(tab);
        Configuration.setString(key, position.getCurrentValue().toString());
    }

    private ConfigurationKey makeKey(Class tab) {
        String className = tab.getName();
        String shortClassName = className.substring(className.lastIndexOf('.')+1).toLowerCase();
        ConfigurationKey key = Configuration.makeKey("layout", shortClassName);
        return key;
    }
    
    /**
     * When the setting values should be reloaded
     */
    public void handleSettingsTabRefresh() {
        loadPosition(_todo, TabToDo.class);
        loadPosition(_properties, TabProps.class);
        loadPosition(_documentation, TabDocumentation.class);
        loadPosition(_style, TabStyle.class);
        loadPosition(_source, TabSrc.class);
        loadPosition(_constraints, TabConstraints.class);
        loadPosition(_taggedValues, TabTaggedValues.class);
    }

    /**
     * When the ok or apply button is pressed
     */
    public void handleSettingsTabSave() {
        savePosition(_todo, TabToDo.class);
        savePosition(_properties, TabProps.class);
        savePosition(_documentation, TabDocumentation.class);
        savePosition(_style, TabStyle.class);
        savePosition(_source, TabSrc.class);
        savePosition(_constraints, TabConstraints.class);
        savePosition(_taggedValues, TabTaggedValues.class);
    }

    public void handleSettingsTabCancel() {}
    public String getModuleName() { return "SettingsTabLayout"; }
    public String getModuleDescription() { return "Positioning of components"; }
    public String getModuleAuthor() { return "ArgoUML Core"; }
    public String getModuleVersion() { return ArgoVersion.getVersion(); }
    public String getModuleKey() { return "module.settings.layout"; }
    public String getTabKey() { return "tab.layout"; }
}
