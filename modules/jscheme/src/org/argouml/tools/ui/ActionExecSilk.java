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

package org.argouml.tools.ui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;
import org.argouml.application.api.PluggableMenu;
import org.argouml.i18n.Translator;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.UMLAction;
import org.tigris.gef.base.Globals;

import silk.Pair;
import silk.Symbol;

/** class ActionExecSilk */
public class ActionExecSilk extends UMLAction
    implements PluggableMenu {

    ////////////////////////////////////////////////////////////////
    // static variables

    private static final Logger LOG =
        Logger.getLogger(ActionExecSilk.class);

    private static JMenuItem menuItem = null;

    private static final String SILK_CLASS = "silk.Scheme";

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor.
     */
    public ActionExecSilk() {
        super("Exec SILK script...", NO_ICON);
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent event) {
        ProjectBrowser pb = ProjectBrowser.getInstance();
	try {
            String directory = Globals.getLastDirectory();
            JFileChooser chooser = new JFileChooser(directory);

            if (chooser == null) {
                chooser = new JFileChooser();
            }

            //chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setDialogTitle("Exec SILK script");
            //      FileFilter filter = FileFilters.SilkFilter;
            //chooser.addChoosableFileFilter(filter);
            //chooser.setFileFilter(filter);

            int retval = chooser.showOpenDialog(pb);

            if (retval == 0) {
                File theFile = chooser.getSelectedFile();
                if (theFile != null) {
		    silk.Scheme.load(theFile.getCanonicalPath());

		    Symbol init = Symbol.intern("main");
		    if (init != null) {
			silk.Scheme.eval(
			        new Pair(init,
			                new Pair(this, Pair.EMPTY)));
		    }
                }
            }
        } catch (Exception exception) {
            LOG.error("Exception in ActionExecSilk", exception);
        }
    }



    /**
     * @see org.argouml.application.api.ArgoModule#initializeModule()
     */
    public boolean initializeModule() {
        boolean initialized = false;
	try {
            Class c = Class.forName(SILK_CLASS);
	    // Make sure we can instantiate it also.
	    c.newInstance();
            LOG.info("JScheme scripting enabled!");
	    initialized = true;
	} catch (Exception e) {
	    LOG.error("JScheme does not appear to be in the classpath.");
	    LOG.error("Unable to initialize JScheme scripting plugin.");
	}
        return initialized;
    }

    /**
     * @see org.argouml.application.api.PluggableMenu#buildContext(
     *          javax.swing.JMenuItem, java.lang.String)
     */
    public Object[] buildContext(JMenuItem a, String b) {
        return new Object[] {
	    a, b
	};
    }

    /**
     * @see org.argouml.application.api.ArgoModule#setModuleEnabled(boolean)
     */
    public void setModuleEnabled(boolean enabled) { }

    /**
     * @see org.argouml.application.api.ArgoModule#isModuleEnabled()
     */
    public boolean isModuleEnabled() { return true; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModulePopUpActions(
     *         java.util.Vector, java.lang.Object)
     */
    public Vector getModulePopUpActions(Vector v, Object o) { return null; }

    /**
     * @see org.argouml.application.api.ArgoModule#shutdownModule()
     */
    public boolean shutdownModule() { return true; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     */
    public String getModuleName() { return "ActionExecSilk"; }
    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() { return "JScheme Scripting"; }
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
    public String getModuleKey() { return "module.tools.scripting.jscheme"; }

    /**
     * @see org.argouml.application.api.Pluggable#inContext(java.lang.Object[])
     */
    public boolean inContext(Object[] o) {
        if (o.length < 2) {
            return false;
        }
	// Allow ourselves on the "Tools" menu.
	if ((o[0] instanceof JMenuItem)
	        && (PluggableMenu.KEY_TOOLS.equals(o[1]))) {
	    return true;
	}
        return false;
    }

    /**
     * @see org.argouml.application.api.PluggableMenu#getMenuItem(java.lang.Object[])
     */
    public JMenuItem getMenuItem(Object[]  context) {

        if (!inContext(context)) {
	    return null;
	}

        if (menuItem == null) {
            menuItem =
                new JMenuItem(Translator.localize("Exec Silk Script..."));
	    menuItem.addActionListener(this);
	}
        return menuItem;
    }

}
/* end class ActionExecSilk */













