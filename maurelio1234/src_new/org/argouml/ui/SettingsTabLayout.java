// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.i18n.Translator;
import org.tigris.swidgets.Property;

/**
 * Settings dialog tab panel for layout options.<p>
 *
 * TODO: This class contains a lot of references to org.argouml.uml.ui.
 * It would probably be better to move it there.
 * 
 * TODO: This class breaks too many dependency limitations. 
 * And it is not used anywhere. So I (MVW) disabled its functionality. 
 *
 * @author Linus Tolke
 */
class SettingsTabLayout extends JPanel
    implements GUISettingsTabInterface {

//    private Property	prpTodo;
//    private Property	prpProperties;
//    private Property	prpDocumentation;
//    private Property	prpStyle;
//    private Property	prpSource;
//    private Property	prpConstraints;
//    private Property	prpTaggedValues;

    /**
     * The constructor.
     *
     */
    SettingsTabLayout() {
        super();
        setLayout(new BorderLayout());

        // TODO: Localize these
        final String[] positions = {"North", "South", "East"};
        final String paneColumnHeader = "Pane";
        final String positionColumnHeader = "Position";

        JPanel top = new JPanel(new BorderLayout());

//        prpTodo = createProperty("label.todo-pane", positions, TabToDo.class);
//        prpProperties =
//            createProperty("label.properties-pane",
//                    positions, TabProps.class);
//        prpDocumentation =
//            createProperty("label.documentation-pane",
//                    positions, TabDocumentation.class);
//        prpStyle =
//            createProperty("label.style-pane",
//                    positions, TabStyle.class);
//        prpSource =
//            createProperty("label.source-pane",
//                    positions, TabSrc.class);
//        prpConstraints =
//            createProperty("label.constraints-pane",
//                    positions, TabConstraints.class);
//        prpTaggedValues =
//            createProperty("label.tagged-values-pane",
//                    positions, TabTaggedValues.class);
//
//        Property[] propertyList = new Property[] {
//            prpTodo, prpProperties, prpDocumentation, prpStyle,
//	    prpSource, prpConstraints, prpTaggedValues,
//        };
//        Arrays.sort(propertyList);
//
//        top.add(new JScrollPane(new PropertyTable(
//						  propertyList,
//						  paneColumnHeader,
//						  positionColumnHeader)),
//		BorderLayout.CENTER);

        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(top, BorderLayout.CENTER);

        JLabel restart =
            new JLabel(Translator.localize("label.restart-application"));
        restart.setHorizontalAlignment(SwingConstants.CENTER);
        restart.setVerticalAlignment(SwingConstants.CENTER);
        restart.setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 2));
        add(restart, BorderLayout.SOUTH);
    }

    /**
     * Create a Property for the position of the given tab pane, selecting
     * the current display value from the user properties file.
     */
    private Property createProperty(String text, String[] positions,
				    Class tab) {
        ConfigurationKey key = makeKey(tab);
        String currentValue = Configuration.getString(key, "South");
        return new Property(Translator.localize(text), String.class,
			    currentValue, positions);
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
        String shortClassName =
	    className.substring(className.lastIndexOf('.') + 1).toLowerCase();
        ConfigurationKey key = Configuration.makeKey("layout", shortClassName);
        return key;
    }

    /**
     * When the setting values should be reloaded.
     */
    public void handleSettingsTabRefresh() {
//        loadPosition(prpTodo, TabToDo.class);
//        loadPosition(prpProperties, TabProps.class);
//        loadPosition(prpDocumentation, TabDocumentation.class);
//        loadPosition(prpStyle, TabStyle.class);
//        loadPosition(prpSource, TabSrc.class);
//        loadPosition(prpConstraints, TabConstraints.class);
//        loadPosition(prpTaggedValues, TabTaggedValues.class);
    }

    /**
     * When the ok or apply button is pressed.
     */
    public void handleSettingsTabSave() {
//        savePosition(prpTodo, TabToDo.class);
//        savePosition(prpProperties, TabProps.class);
//        savePosition(prpDocumentation, TabDocumentation.class);
//        savePosition(prpStyle, TabStyle.class);
//        savePosition(prpSource, TabSrc.class);
//        savePosition(prpConstraints, TabConstraints.class);
//        savePosition(prpTaggedValues, TabTaggedValues.class);
    }

    /*
     * @see GUISettingsTabInterface#handleSettingsTabCancel()
     */
    public void handleSettingsTabCancel() { }

    /*
     * @see org.argouml.ui.GUISettingsTabInterface#handleResetToDefault()
     */
    public void handleResetToDefault() {
        // Do nothing - these buttons are not shown.
    }

    /*
     * @see GUISettingsTabInterface#getTabKey()
     */
    public String getTabKey() { return "tab.layout"; }

    /*
     * @see GUISettingsTabInterface#getTabPanel()
     */
    public JPanel getTabPanel() { return this; }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 739259705815092510L;
}
