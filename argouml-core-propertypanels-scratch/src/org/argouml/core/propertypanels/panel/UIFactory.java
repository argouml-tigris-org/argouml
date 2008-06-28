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

import java.io.InputStream;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.argouml.core.propertypanels.xml.XMLPropertyPanelsData;
import org.argouml.core.propertypanels.xml.XMLPropertyPanelsDataRecord;
import org.argouml.core.propertypanels.xml.XMLPropertyPanelsHandler;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.UMLCheckBox2;
import org.argouml.uml.ui.UMLDerivedCheckBox;
import org.argouml.uml.ui.UMLRadioButtonPanel;
import org.argouml.uml.ui.UMLSearchableComboBox;
import org.argouml.uml.ui.UMLTextField2;
import org.argouml.uml.ui.foundation.core.ActionSetModelElementNamespace;
import org.argouml.uml.ui.foundation.core.UMLClassActiveCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementAbstractCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementLeafCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementRootCheckBox;
import org.argouml.uml.ui.foundation.core.UMLModelElementNamespaceComboBoxModel;
import org.argouml.uml.ui.foundation.core.UMLModelElementVisibilityRadioButtonPanel;
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
                JPanel p = buildTextboxPanel(target, prop);
                panel.add(p);
            }
            else if ("combo".equals(prop.getType())) {
                JPanel p = buildComboPanel(target, prop);
                panel.add(p);
            }
            else if ("checkgroup".equals(prop.getType())) {
                JPanel p = buildCheckGroup(target, prop);
                panel.add(p);
            }
            else if ("optionbox".equals(prop.getType())) {
                JPanel p = buildOptionBox(target, prop);
                panel.add(p);
            }
        }
        return panel;
    }

    /**
     * @param target
     * @param prop
     * @return 
     */
    protected JPanel buildOptionBox(Object target,
            XMLPropertyPanelsDataRecord prop) {
        if ("visibility".equals(prop.getName())) {
            UMLRadioButtonPanel visibilityPanel =   
                new UMLModelElementVisibilityRadioButtonPanel(
                    Translator.localize("label.visibility"), 
                    true); 
            visibilityPanel.setTarget(target);
            return visibilityPanel;
        }
        // log something? Invalid XML!
        return null;
    }

    /**
     * @param target
     * @param prop
     * @return 
     */
    protected JPanel buildCheckGroup(Object target,
            XMLPropertyPanelsDataRecord prop) {
        JPanel panel = new JPanel();
        if ("modifiers".equals(prop.getName())) {  
            // TODO: The checkboxes must be explicitly said at the
            // XML. Interface and UmlClass differ on "derived"
            // we must build the checkboxes
            for (XMLPropertyPanelsDataRecord p : prop.getChildren()) {
                UMLCheckBox2 checkbox = buildCheckBox(target, p);
                panel.add(checkbox);
            }                            
        }
        return panel;
    }

    protected UMLCheckBox2 buildCheckBox(Object target,
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
        if (checkbox != null) {
            checkbox.setTarget(target);
        }
        return checkbox;
    }

    /**
     * @param target
     * @param prop
     * @return
     */
    protected JPanel buildComboPanel(Object target,
            XMLPropertyPanelsDataRecord prop) {
        JPanel p = new JPanel();
        String name = prop.getName();
        JLabel label = new JLabel(name);
        p.add(label);

        if ("namespace".equals(prop.getName())) {
            UMLModelElementNamespaceComboBoxModel namespaceModel =
                new UMLModelElementNamespaceComboBoxModel();                    
            namespaceModel.setTarget(target);
            JComboBox namespaceSelector = new UMLSearchableComboBox(
                    namespaceModel,
                    new ActionSetModelElementNamespace(), true);
            
            p.add(namespaceSelector);                    
//                  p.add(new UMLComboBoxNavigator(
//                  Translator.localize(
//                  "label.namespace.navigate.tooltip"),
//                  namespaceSelector));
        }
        return p;
    }

    /**
     * @param target
     * @param prop
     * @return
     */
    protected JPanel buildTextboxPanel(Object target,
            XMLPropertyPanelsDataRecord prop) {
        JPanel p = new JPanel();
        String name = prop.getName();
        JLabel label = new JLabel(name);
        p.add(label);

        GenericUMLPlainTextDocument document = 
            new GenericUMLPlainTextDocument(name);
        document.setTarget(target);
        JTextField tfield = 
            new UMLTextField2(document);
        tfield.setColumns(40);
        p.add(tfield);
        return p;
    }


    private String getXMLFileName(Object target) {
        String classname = target.getClass().getSimpleName();
        // TODO: I don't like this hack, it may exist a better way.
        return classname.replace("$Impl", "");
    }

    private XMLPropertyPanelsData parseXML(String filename) 
        throws Exception {
        
        XMLPropertyPanelsData data = new XMLPropertyPanelsData();

        XMLReader parser = XMLReaderFactory.createXMLReader(
                "org.apache.xerces.parsers.SAXParser"              
        );
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
