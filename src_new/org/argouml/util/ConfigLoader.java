// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.ConfigurationKey;
import org.argouml.swingext.Orientation;
import org.argouml.ui.ProjectBrowser;

public class ConfigLoader {
    private static Logger _Log = Logger.getLogger("org.argouml.util.ConfigLoader"); 

    ////////////////////////////////////////////////////////////////
    // static utility functions

    public static String TabPath = "org.argouml";
    private static Orientation tabPropsOrientation;

    public static Orientation getTabPropsOrientation() {
        return tabPropsOrientation;
    }

    public static void loadTabs(Vector tabs, String panelName, Orientation orientation) {
        String position = null;
        if (
	    panelName.equals("north") || panelName.equals("south") || 
	    panelName.equals("west") || panelName.equals("east") ||
	    panelName.equals("northwest") || panelName.equals("northeast") || 
	    panelName.equals("southwest") || panelName.equals("southeast")) {
            position = panelName;
            panelName = "detail";
        }
        InputStream is = null;
	LineNumberReader lnr = null;
	String configFile = System.getProperty("argo.config");
        //
        //    if property specified
        //
        if (configFile != null) {
            //    try to load a file
            try {
                is = new FileInputStream(configFile);
            }
            catch (FileNotFoundException e) {
                is = ConfigLoader.class.getResourceAsStream(configFile);
                if (is != null) {
                    _Log.info("Value of argo.config (" + configFile + ") could not be loaded.\nLoading default configuration.");
                }
            }
        }
        if (is == null) {
            configFile = "/org/argouml/argo.ini";
            is = ConfigLoader.class.getResourceAsStream(configFile);
        }
        if (is != null) {
            lnr = new LineNumberReader(new InputStreamReader(is));

            if (lnr != null) {
                try {
                    String line = lnr.readLine();
                    while (line != null) {
                        Class tabClass =
                            parseConfigLine(line, panelName, lnr.getLineNumber(), configFile);
                        if (tabClass != null) {
                            try {
                                String className = tabClass.getName();
                                String shortClassName = className.substring(className.lastIndexOf('.') + 1).toLowerCase();
                                ConfigurationKey key = Configuration.makeKey("layout", shortClassName);
                                if (position == null || position.equalsIgnoreCase(Configuration.getString(key, "South"))) {
                                    if (className.equals("org.argouml.uml.ui.TabProps")) {
                                        tabPropsOrientation = orientation;
                                    }
                                    Object newTab = tabClass.newInstance();
                                    tabs.addElement(newTab);
                                }
                            }
                            catch (InstantiationException ex) {
                                _Log.error("Could not make instance of " +
					   tabClass.getName());
                            }
                            catch (IllegalAccessException ex) {
                                _Log.error("Could not make instance of " +
					   tabClass.getName());
                            }
                        }
                        line = lnr.readLine();                     
                    }
                }
                catch (IOException io) {
                    _Log.error(io);
                }
            }
            else {
                _Log.error("lnr is null");
            }
        }
    }

    public static Class parseConfigLine(String line, String panelName,
					int lineNum, String configFile) {
	if (line.startsWith("tabpath")) {
	    String newPath = stripBeforeColon(line).trim();
	    if (newPath.length() > 0) TabPath = newPath;
	    return null;
	}
	else if (line.startsWith(panelName)) {
	    String tabNames = stripBeforeColon(line).trim();
	    StringTokenizer tabAlternatives = new StringTokenizer(tabNames, "|");
	    Class res = null;
	    while (tabAlternatives.hasMoreElements()) {
		String tabSpec = tabAlternatives.nextToken().trim();
		String tabName = tabSpec;  //TODO: arguments
		String tabClassName;
                                
		if ( tabName.indexOf('.') > 0 )
		    tabClassName = tabName;
		else
		    tabClassName = TabPath + "." + tabName;

		try {
		    res = Class.forName(tabClassName);
		}
		catch (ClassNotFoundException cnfe) { }
		catch (Exception e) {
		    _Log.error("Unanticipated exception, skipping " + tabName);
		    _Log.error(e);
		}
		if (res != null) {
		    if (ProjectBrowser.getInstance().getSplashScreen() != null) {
			ProjectBrowser.getInstance().getSplashScreen().getStatusBar().showStatus("Making Project Browser: " + tabName);
			ProjectBrowser.getInstance().getSplashScreen().getStatusBar().incProgress(2);
		    }
		    return res;
		}
	    }
	    if (Boolean.getBoolean("dbg")) {
		_Log.warn("\nCould not find any of these classes:\n" +
			  "TabPath=" + TabPath + "\n" +
			  "Config file=" + configFile + "\n" +
			  "Config line #" + lineNum + ":" + line);
	    }
	}
	return null;
    }

    public static String stripBeforeColon(String s) {
	int colonPos = s.indexOf(":");
	return s.substring(colonPos  + 1);
    }

} /* end class ConfigLoader */
