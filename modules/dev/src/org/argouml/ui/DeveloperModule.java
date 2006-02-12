// $Id$
// Copyright (c) 2004-2005 The Regents of the University of California. All
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
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import org.argouml.uml.ui.UMLAction;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.cmd.GenericArgoMenuBar;
import org.argouml.kernel.UndoEnabler;
import org.argouml.moduleloader.ModuleInterface;
import org.tigris.gef.undo.UndoLogPanel;
import org.tigris.gef.undo.UndoManager;
import org.tigris.gef.undo.UndoManagerWrapper;

/**
 * Module that registers itself to the Tools menu.<p>
 *
 * This is primarily designed to be able to test ModuleLoader2.<p>
 *
 * @author Linus Tolke
 * @since  0.17.1
 */
public final class DeveloperModule extends UMLAction
    implements ModuleInterface {
    
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(DeveloperModule.class);

    /**
     * The menu item.
     */
    private JMenuItem menuItem;
    JPanel undoLogPanel;

    UndoManagerWrapper um = new UndoManagerWrapper();
    
    /**
     * This is creatable from the module loader.
     */
    public DeveloperModule() {
	super("Test entry", false);

	menuItem = new JMenuItem("Testit");
	menuItem.addActionListener(this);
    }

    ////////////////////////////////////////////////////////////////
    // Main methods.

    /**
     * @see ModuleInterface#enable()
     */
    public boolean enable() {
        UndoManager.setInstance(um);
        // Hack into the edit menu and make the 
        GenericArgoMenuBar menubar =
            (GenericArgoMenuBar) ProjectBrowser.getInstance().getJMenuBar();
        menubar.getTools().add(menuItem);
        JMenu editMenu = ProjectBrowser.getInstance().getJMenuBar().getMenu(1);
        
        editMenu.getMenuComponent(0).setVisible(true);
        editMenu.getMenuComponent(1).setVisible(true);
        UndoManager.getInstance().setUndoMax(10);
        
        JComponent undoLogPanel = UndoLogPanel.getInstance();
        ProjectBrowser.getInstance().addPanel(undoLogPanel, BorderLayout.EAST);
        
        return true;
    }

    /**
     * @see ModuleInterface#disable()
     *
     * This removes us from the Tools menu. If we were not registered there
     * we don't care.
     */
    public boolean disable() {
        GenericArgoMenuBar menubar =
            (GenericArgoMenuBar) ProjectBrowser.getInstance().getJMenuBar();
        menubar.getTools().remove(menuItem);
        UndoEnabler.enabled = false;
        return true;
    }

    /**
     * @see ModuleInterface#getName()
     */
    public String getName() {
        return "DeveloperModule";
    }

    /**
     * @see ModuleInterface#getInfo(int)
     */
    public String getInfo(int type) {
        switch (type) {
        case DESCRIPTION:
            return "This is a module to provide test panels for ArgoUML developers";
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
