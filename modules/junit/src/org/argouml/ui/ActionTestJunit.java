// $Id$
// Copyright (c) 1996-2003, 2005 The Regents of the University of California. All
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

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.application.api.PluggableMenu;
import org.argouml.uml.ui.*;

import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;

import javax.swing.*;


/** Action object for handling Argo tests
 *
 *  @author Andreas Rueckert
 *  @since  0.9.4
 */
public class ActionTestJunit extends UMLAction
    implements PluggableMenu
{


    ////////////////////////////////////////////////////////////////
    // Static variables

    /** logger */
    private static final Logger LOG = Logger.getLogger(ActionTestJunit.class);

    /**
     * A singleton as the only instance of this action.
     */
    private static ActionTestJunit singleton = new ActionTestJunit();

    private static final String JUNIT_CLASS = "junit.swingui.TestRunner";
    private static JMenuItem menuItem = null;

    /** Internal flag to avoid multiple reports of failure. */
    private boolean failed = false;

    ////////////////////////////////////////////////////////////////
    // Constructors.

    /**
     * Create a new ActionTestJunit instance (is not public, due to
     * singleton pattern).
     */
    protected ActionTestJunit() {
	super(Translator.localize("Test Panel..."), false);
	failed = false;
    }

    ////////////////////////////////////////////////////////////////
    // Main methods.

    /**
     * Get the instance.
     *
     * @return The ActionTestJunit instance
     */
    public static ActionTestJunit getInstance() {
        return singleton;
    }

    /**
     * Start the actual tests.
     *
     * The event, that triggered this action.
     *
     * Use reflection rather than directly executing class.main itself
     * This executes the equivalent of:
     *
     * <code>junit.swingui.TestRunner.main(args);</code>
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent event) {
	String[] args = {};
	try {
	    Class cls = Class.forName(JUNIT_CLASS);
	    Object instance = cls.newInstance();
	    Class[] parmClasses = new Class[] {
		args.getClass()
	    };
	    Method method = cls.getMethod("main", parmClasses);
	    Object[] passedArgs = new Object[] {
		args
	    };
	    method.invoke(instance, passedArgs);
	}
	catch (Exception e) {
	    if (!failed) {
	        LOG.error("Unable to launch Junit", e);
	    }
	    failed = true;
	    // TODO:  Disable the menu entry on failure.
	}
    }

    /**
     * @see org.argouml.application.api.ArgoModule#setModuleEnabled(boolean)
     */
    public void setModuleEnabled(boolean v) { }

    /**
     * @see org.argouml.application.api.ArgoModule#initializeModule()
     */
    public boolean initializeModule() {
        boolean initialized = false;
	try {
	    // Make sure we can find the class.
            Class c = Class.forName(JUNIT_CLASS);
	    // Make sure we can instantiate it also.
	    Object o = c.newInstance();

	    // Advertise a little
            LOG.info ("+-------------------------------+");
            LOG.info ("| JUnit plugin testing enabled! |");
            LOG.info ("+-------------------------------+");
	    initialized = true;
	}
	catch (Exception e) {
	    failed = true;
	    LOG.error("JUnit does not appear to be in the classpath.");
	    LOG.error("Unable to initialize JUnit testing plugin.");
	}
        return initialized;
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
	if ((o[0] instanceof JMenuItem)
            && (PluggableMenu.KEY_TOOLS.equals(o[1]))) {
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
    public String getModuleName() { return "ActionTestJunit"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() {
        return "Menu Item for JUnit Testing";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleAuthor()
     */
    public String getModuleAuthor() { return "Andreas Rueckert"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() { return "0.9.4"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() { return "module.menu.tools.junit"; }

    /**
     * @see org.argouml.application.api.PluggableMenu#getMenuItem(java.lang.Object[])
     */
    public JMenuItem getMenuItem(Object [] context) {
        if (!inContext(context)) {
	    return null;
	}

        if (menuItem == null) {
            menuItem = new JMenuItem(Translator.localize("Test Panel..."));
	    menuItem.addActionListener(this);
	}
        return menuItem;
    }
}
