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

package org.argouml.ui.test;

import org.argouml.application.api.*;
import org.argouml.uml.ui.*;

import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;

import javax.swing.*;


/** Plugin that exposes itself to all JMenuItem contexts and provides
 *  a text message based upon the string argument of the context.
 *
 *  This is primarily designed to be able to test PluggableMenu
 *  requestors.
 *
 *  @author Thierry Lach
 *  @since  0.9.4
 */
public class ActionTestMenuPlugins extends UMLAction
    implements PluggableMenu 
{
    /**
     * This is not publicly creatable.
     */
    protected ActionTestMenuPlugins() {
	super("Plugin menu test entry", false);
    }

    ////////////////////////////////////////////////////////////////
    // Main methods.

    /**
     * Just let the tester know that we got executed.
     */
    public void actionPerformed(ActionEvent event) {
        Argo.log.info("User clicked on '" + event.getActionCommand() + "'");
    }

    public void setModuleEnabled(boolean v) { }
    public boolean initializeModule() {
        Argo.log.info ("+-----------------------------+");
        Argo.log.info ("| Plugin Menu tester enabled! |");
        Argo.log.info ("+-----------------------------+");
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
	if (o[0] instanceof JMenuItem) {
	    return true;
	}
        return false;
    }
    public boolean isModuleEnabled() { return true; }
    public Vector getModulePopUpActions(Vector v, Object o) { return null; }
    public boolean shutdownModule() { return true; }

    public String getModuleName() { return "ActionTestMenuPlugins"; }
    public String getModuleDescription() { return "Menu Item for JUnit Testing"; }
    public String getModuleAuthor() { return "Thierry Lach"; }
    public String getModuleVersion() { return "0.9.4"; }
    public String getModuleKey() { return "module.test.menu.plugins"; }

    public JMenuItem getMenuItem(JMenuItem mi, String s) {
        return getMenuItem(buildContext(mi, s));
    }

    public JMenuItem getMenuItem(Object [] context) {
        if (!inContext(context)) {
	    return null;
	}

        JMenuItem _menuItem = new JMenuItem("Plugin menu for " + context[1]);
	_menuItem.addActionListener(this);
        return _menuItem;
    }
}
