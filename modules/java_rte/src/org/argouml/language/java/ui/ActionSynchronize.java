// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.language.java.ui;

//import org.argouml.language.java.rte.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JMenuItem;

import org.argouml.application.api.PluggableMenu;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.UMLAction;

/** Plugin for synchronizing Java sources (RTE).
 *
 *  @author Thomas Neustupny
 *  @since  0.11.3
 */
public class ActionSynchronize extends UMLAction implements PluggableMenu
{
    /**
     * Create a new ActionSynchronize (this is not public)
     */
    protected ActionSynchronize() {
	super("Synchronize model/code...", false);
    }

    /**
     * Opens the synchronization dialog and fills it with 'differences'
     */
    public void actionPerformed(ActionEvent event) {
	//Argo.log.info("User clicked on '" + event.getActionCommand() + "'");
	SynchronizeDialog syndia = new SynchronizeDialog(ProjectBrowser.getInstance(), true);
	syndia.show();
    }

    public void setModuleEnabled(boolean v) { }

    /**
     * Initialize module
     */
    public boolean initializeModule() {
	//Argo.log.info ("+--------------------------+");
	//Argo.log.info ("| Plugin Java RTE enabled! |");
	//Argo.log.info ("+--------------------------+");
	return true;
    }

    public Object[] buildContext(JMenuItem a, String b) {
	return new Object[] {
	    a, b
	};
    }

    public boolean inContext(Object[] o) {
	if (o.length < 2) return false;
	// We are in context for any JMenuItem.
	if (o[0] instanceof JMenuItem && o[1].equals(PluggableMenu.KEY_TOOLS)) {
	    return true;
	}
	return false;
    }

    public boolean isModuleEnabled() { return true; }
    public Vector getModulePopUpActions(Vector v, Object o) { return null; }
    public boolean shutdownModule() { return true; }
    public String getModuleName() { return "ActionSynchronize"; }
    public String getModuleDescription() { return "Menu Item for Synchronizing Model and Java Code"; }
    public String getModuleAuthor() { return "Thomas Neustupny"; }
    public String getModuleVersion() { return "0.11.3"; }
    public String getModuleKey() { return "module.language.java.rte"; }

    public JMenuItem getMenuItem(JMenuItem mi, String s) {
	return getMenuItem(buildContext(mi, s));
    }

    public JMenuItem getMenuItem(Object [] context) {
	if (!inContext(context)) {
	    return null;
	}
	JMenuItem _menuItem = new JMenuItem("Synchronize model/code...");
	_menuItem.addActionListener(this);
	return _menuItem;
    }
}
