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

package org.argouml.core.propertypanels.module;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.argouml.core.propertypanels.ui.XMLPropPanelFactory;
import org.argouml.core.propertypanels.ui.XmlPropertyPanel;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.ui.DetailsPane;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanelFactory;
import org.argouml.uml.ui.PropPanelFactoryManager;

/**
 * Defines the XMLPropertyPanels module
 *
 * @author penyaskito
 */
public class XmlPropertyPanelsModule 
    implements ModuleInterface {
    // TODO: Uncomment this and replace above line to see old and new panel
    // together
//  implements ModuleInterface, DetailsTabProvider {

    /**
     * The logger
     */
    private static final Logger LOG =
        Logger.getLogger(XmlPropertyPanel.class);
    
    private TempTabPage tempPanel;
    
    /**
     * It is useful during testing to view the current and new property panels
     * side by side. This can be switched by changing this flag to replace
     * the existing panels instead. Once XML Property Panels goes live all the
     * code dependent on this being false can be removed and the flag itself
     * removed.
     */
    private final static boolean REPLACE = false;
    
    public boolean enable() { 
        
        if (REPLACE) {
            /* Set up the property panels for UML elements: */
            try {
                PropPanelFactory elementFactory = XMLPropPanelFactory.getInstance();
                PropPanelFactoryManager.addPropPanelFactory(elementFactory);
                return true;
            } catch (Exception e) {
                LOG.error("Exception caught", e);
                return false;
            }
        } else {
            DetailsPane detailsPane = (DetailsPane) ProjectBrowser.getInstance().getDetailsPane();
            tempPanel = new TempTabPage();
            detailsPane.addTab(tempPanel, true);
            
            TempListener listener = new TempListener(tempPanel);
            TargetManager.getInstance().addTargetListener(listener);
            return true;
        }
    }

    public boolean disable() {
        if (REPLACE) {
            /* Set up the property panels for UML elements: */
            try {
                PropPanelFactory elementFactory = XMLPropPanelFactory.getInstance();
                PropPanelFactoryManager.removePropPanelFactory(elementFactory);
                return true;
            } catch (Exception e) {
                LOG.error("Exception caught", e);
                return false;
            }
        } else {
            DetailsPane detailsPane = (DetailsPane) ProjectBrowser.getInstance().getDetailsPane();
            detailsPane.removeTab(tempPanel);
            return true;
        }
    }

    public String getInfo(int type) {
        switch (type) {
        case AUTHOR:
            return "Christian Lopez Espinola";
        case DESCRIPTION:
            return "Module for adding property panels based on XML";
        case DOWNLOADSITE:
            return "Not released. Prototyping yet.";
        case VERSION:
            return "0.1";
        default:
            return null;
        }
    }

    public String getName() {        
        return "Xml Property Panels Module";
    }

}
