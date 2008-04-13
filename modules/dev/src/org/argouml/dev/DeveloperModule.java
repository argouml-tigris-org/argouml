// $Id$
// Copyright (c) 2006-2007 The Regents of the University of California. All
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

package org.argouml.dev;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JTabbedPane;

import org.apache.log4j.Logger;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.dev.figinspector.FigInspectorPanel;
import org.argouml.dev.test.TestPanel;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.ui.DetailsPane;
import org.argouml.ui.ProjectBrowser;
import org.tigris.gef.undo.UndoManager;

/**
 * A module to provide debug windows for developers of ArgoUML.
 *
 * @author Bob Tarling
 */
public final class DeveloperModule implements ModuleInterface {

    private static final Logger LOG = Logger.getLogger(DeveloperModule.class);
    
    /**
     * Wrapper.
     */
//    private UndoManagerWrapper um = new UndoManagerWrapper();

    /**
     * This is creatable from the module loader.
     */
    public DeveloperModule() {
    }

    ////////////////////////////////////////////////////////////////
    // Main methods.

    /*
     * @see ModuleInterface#enable()
     */
    public boolean enable() {
        // TODO: Add a checkbox menu item to hide/show undo panel
	LOG.info("Enabling developer module");
        JMenu editMenu = ProjectBrowser.getInstance().getJMenuBar().getMenu(1);
        editMenu.getMenuComponent(0).setVisible(true);
        editMenu.getMenuComponent(1).setVisible(true);
        UndoManager.getInstance().setUndoMax(10);

        JTabbedPane devPanel = new JTabbedPane();

        JComponent undoLogPanel = UndoLogPanel.getInstance();
        devPanel.addTab("Undo Stack", undoLogPanel);

        JComponent inspectorPanel = FigInspectorPanel.getInstance();
        devPanel.add("Fig Inspector", inspectorPanel);

        JComponent targetManagerPanel = TargetManagerPanel.getInstance();
        devPanel.add("TargetManager", targetManagerPanel);

        JComponent eventPumpPanel = EventPumpInspectorPanel.getInstance();
        devPanel.add("Model Listeners", eventPumpPanel);

        ProjectBrowser.getInstance().addPanel(devPanel, 
                ProjectBrowser.Position.East);
        
        AbstractArgoJPanel testPanel = TestPanel.getInstance();
        DetailsPane dp = (DetailsPane) ProjectBrowser.getInstance().getDetailsPane();
        dp.addTab(testPanel, true);

        return true;
    }

    /*
     * @see ModuleInterface#disable()
     */
    public boolean disable() {
        JMenu editMenu = ProjectBrowser.getInstance().getJMenuBar().getMenu(1);

        editMenu.getMenuComponent(0).setVisible(false);
        editMenu.getMenuComponent(1).setVisible(false);
        UndoManager.getInstance().empty();
        UndoManager.getInstance().setUndoMax(0);

        JComponent undoLogPanel = UndoLogPanel.getInstance();
        ProjectBrowser.getInstance().removePanel(undoLogPanel);
        return true;
    }

    /*
     * @see ModuleInterface#getName()
     */
    public String getName() {
        return "DeveloperModule";
    }

    /*
     * @see ModuleInterface#getInfo(int)
     */
    public String getInfo(int type) {
        switch (type) {
        case DESCRIPTION:
            return "This is a module to provide test panels "
                + "for ArgoUML developers";
        case AUTHOR:
            return "Bob Tarling";
        case VERSION:
            return "1.0";
        default:
            return null;
        }
    }

    /**
     * The version uid.
     */
    private static final long serialVersionUID = -2570516012301142091L;
}
