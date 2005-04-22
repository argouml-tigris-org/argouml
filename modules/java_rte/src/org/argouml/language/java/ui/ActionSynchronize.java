// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

/**
 * Plugin for synchronizing Java sources (RTE).
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
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent event) {
	//Argo.log.info("User clicked on '" + event.getActionCommand() + "'");
	SynchronizeDialog syndia =
	    new SynchronizeDialog(ProjectBrowser.getInstance(), true);
	syndia.setVisible(true);
    }

    /**
     * @see org.argouml.application.api.ArgoModule#setModuleEnabled(boolean)
     */
    public void setModuleEnabled(boolean v) { }

    /**
     * Initialize module
     *
     * @see org.argouml.application.api.ArgoModule#initializeModule()
     */
    public boolean initializeModule() {
	//Argo.log.info ("+--------------------------+");
	//Argo.log.info ("| Plugin Java RTE enabled! |");
	//Argo.log.info ("+--------------------------+");
	return true;
    }

    /**
     * @see org.argouml.application.api.PluggableMenu#buildContext(
     * javax.swing.JMenuItem, java.lang.String)
     */
    public Object[] buildContext(JMenuItem a, String b) {
	return new Object[] {
	    a, b
	};
    }

    /**
     * @see org.argouml.application.api.Pluggable#inContext(java.lang.Object[])
     */
    public boolean inContext(Object[] o) {
	if (o.length < 2) return false;
	// We are in context for any JMenuItem.
	if (o[0] instanceof JMenuItem && o[1].equals(PluggableMenu.KEY_TOOLS)) {
	    return true;
	}
	return false;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#isModuleEnabled()
     */
    public boolean isModuleEnabled() { return true; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModulePopUpActions(
     * java.util.Vector, java.lang.Object)
     */
    public Vector getModulePopUpActions(Vector v, Object o) { return null; }

    /**
     * @see org.argouml.application.api.ArgoModule#shutdownModule()
     */
    public boolean shutdownModule() { return true; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     */
    public String getModuleName() { return "ActionSynchronize"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() {
        return "Menu Item for Synchronizing Model and Java Code";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleAuthor()
     */
    public String getModuleAuthor() { return "Thomas Neustupny"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() { return "0.11.3"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() { return "module.language.java.rte"; }

    /**
     * @param mi parent menu item
     * @param s menu item name
     * @return the new menu item
     */
    public JMenuItem getMenuItem(JMenuItem mi, String s) {
	return getMenuItem(buildContext(mi, s));
    }

    /**
     * @see org.argouml.application.api.PluggableMenu#getMenuItem(java.lang.Object[])
     */
    public JMenuItem getMenuItem(Object [] context) {
	if (!inContext(context)) {
	    return null;
	}
	JMenuItem item = new JMenuItem("Synchronize model/code...");
	item.addActionListener(this);
	return item;
    }
}
