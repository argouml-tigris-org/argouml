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

package org.argouml.tools.ui;

import org.argouml.application.api.*;
import org.argouml.uml.ui.*;
import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.uml.reveng.*;

import org.tigris.gef.base.*;

import ru.novosoft.uml.foundation.core.*;

import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import silk.*;

/* class ActionExecSilk */
public class ActionExecSilk extends UMLAction
    implements PluggableMenu 
{

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionExecSilk SINGLETON = new ActionExecSilk(); 

    private static JMenuItem _menuItem = null;

    public static final String separator = "/"; //System.getProperty("file.separator");

    private final static String SILK_CLASS = "silk.Scheme";

    ////////////////////////////////////////////////////////////////
    // constructors

    public ActionExecSilk() {
        super("Exec SILK script...", NO_ICON);
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent event) {
        ProjectBrowser pb = ProjectBrowser.getInstance();
	Object target = pb.getDetailsTarget();
	
	// if (!(target instanceof MClassifier)) return;

        try {
            String directory = Globals.getLastDirectory();
            JFileChooser chooser = new JFileChooser(directory);

            if (chooser == null) chooser = new JFileChooser();

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
			silk.Scheme.eval(new Pair(init, new Pair(this, Pair.EMPTY)));
		    }
                }
            }
        } catch (Exception exception) {
            Argo.log.error("Exception in ActionExecSilk", exception);
        }
    }



    public boolean initializeModule() {
        boolean initialized = false;
	try {
            Class c = Class.forName(SILK_CLASS);
	    // Make sure we can instantiate it also.
	    Object o = c.newInstance();
            Argo.log.info ("+----------------------------+");
            Argo.log.info ("| JScheme scripting enabled! |");
            Argo.log.info ("+----------------------------+");
	    initialized = true;
	}
	catch (Exception e) {
	    Argo.log.error("JScheme does not appear to be in the classpath.");
	    Argo.log.error("Unable to initialize JScheme scripting plugin.");
	}
        return initialized;
    }

    public Object[] buildContext(JMenuItem a, String b) {
        return new Object[] {
	    a, b
	};
    }

    public void setModuleEnabled(boolean enabled) { }
    public boolean isModuleEnabled() { return true; }
    public Vector getModulePopUpActions(Vector v, Object o) { return null; }
    public boolean shutdownModule() { return true; }

    public String getModuleName() { return "ActionExecSilk"; }
    public String getModuleDescription() { return "JScheme Scripting"; }
    public String getModuleAuthor() { return "Andreas Rueckert"; }
    public String getModuleVersion() { return "0.9.4"; }
    public String getModuleKey() { return "module.tools.scripting.jscheme"; }

    public boolean inContext(Object[] o) {
        if (o.length < 2) return false;
	// Allow ourselves on the "Tools" menu.
	if ((o[0] instanceof JMenuItem) &&
	    (PluggableMenu.KEY_TOOLS.equals(o[1]))) {
	    return true;
	}
        return false;
    }

    public JMenuItem getMenuItem(JMenuItem mi, String s) {
        return getMenuItem(buildContext(mi, s));
    }

    public JMenuItem getMenuItem(Object[]  context) {

        if (!inContext(context)) {
	    return null;
	}

        if (_menuItem == null) {
            _menuItem = new JMenuItem(Argo.localize(Argo.MENU_BUNDLE,
	                                            "Exec Silk Script..."));
	    _menuItem.addActionListener(this);
	}
        return _menuItem;
    }

}
/* end class ActionExecSilk */   













