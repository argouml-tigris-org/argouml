// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.tigris.swidgets.Orientation;

/**
 * This class loads property panel tab classes specified by a configuration file.
 */
public class ConfigLoader {

    private static final Logger LOG = Logger.getLogger(ConfigLoader.class);

    private static final String CONFIG_FILE_PROPERTY = "argo.config";
    private static final String DEFAULT_CONFIG_FILE = "/org/argouml/argo.ini";
    private static String tabPath = "org.argouml";
    private static Orientation tabPropsOrientation;

    /**
     * @return the orientation
     * @deprecated for 0.25.4 by tfmorris.  Use this.getOrientation() on the
     * parent component who's orientation you want to match.
     */
    @Deprecated
    public static Orientation getTabPropsOrientation() {
        return tabPropsOrientation;
    }

    /**
     * Load the tab panels as defined in the configuration file. The system
     * property <code>argo.config</code> can be used to override the default
     * config file which is <code>org/argouml/argo.ini</code>
     * 
     * @param tabs List in which to return the tabs that were loaded.
     * @param panelName the panel name
     * @param orientation the orientation (Horizontal or Vertical)
     * @deprecated for 0.25.4 by tfmorris.  Callers should instantiate 
     * required classes directly rather than allowing dependencies to be 
     * hidden in argo.ini.
     */
    @Deprecated
    public static void loadTabs(List tabs, String panelName,
            final Orientation orientation) {

        String position = null;
        if (panelName.equals("north") || panelName.equals("south")
	    || panelName.equals("west") || panelName.equals("east")
	    || panelName.equals("northwest") || panelName.equals("northeast")
	    || panelName.equals("southwest") || panelName.equals("southeast")) {
            position = panelName;
            panelName = "detail";
        }

        List<Class<JPanel>> tabClasses = readConfigFile(panelName, orientation,
                position);

        if (tabClasses != null) {
            for (Class<JPanel> tabClass : tabClasses) {
                JPanel tab = createTab(tabClass, position);
                if (tab != null) {
                    tabs.add(tab);
                }
            }
            updateOrientation(tabs, orientation);
        }
    }

    
    /**
     * Read each line from the config file, parsing each line and adding
     * the relevant tabs.
     * 
     * @param tabs List of tabs which were added.
     * @param panelName
     * @param orientation
     * @param position
     */
    private static List<Class<JPanel>> readConfigFile(String panelName,
            Orientation orientation, String position) {

        InputStream is = null;
        LineNumberReader lnr = null;
        String configFile = System.getProperty(CONFIG_FILE_PROPERTY);
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
                if (is == null) {
                    LOG.info("File specified by argo.config ("
                            + configFile
                            + ") could not be loaded.\n" 
                            + "Loading default configuration.");
                }
            }
        }
        if (is == null) {
            configFile = DEFAULT_CONFIG_FILE;
            is = ConfigLoader.class.getResourceAsStream(configFile);
        }
        if (is == null) {
                LOG.error("Unable to instantiate a config file reader");
            return null;
        }
        try {
            lnr = new LineNumberReader(new InputStreamReader(is, 
                    Argo.getEncoding()));
        } catch (UnsupportedEncodingException ueExc) {
            lnr = new LineNumberReader(new InputStreamReader(is));
        }

        List<Class<JPanel>> tabs = new ArrayList<Class<JPanel>>();
        try {
            String line = lnr.readLine();
            while (line != null) {
                Class tabClass = parseConfigLine(line, panelName, 
                        lnr.getLineNumber(), configFile);
                if (tabClass != null) {
                    tabs.add(tabClass);
                }
                line = lnr.readLine();
            }
        }
        catch (IOException io) {
            LOG.error(io);
        }
        return tabs;
    }

    /**
     * Instantiates the class for a tab if its saved position matches the
     * requested position.  Also saves the orientation of the TabProps tab
     * when it is encountered.
     * @param tabClass the class to be instantiated as a new tab
     * @param position requested position (North, South, etc)
     */
    private static JPanel createTab(Class<JPanel> tabClass, String position) {
        try {
            String className = tabClass.getName();
            if (position == null
                    || position.equalsIgnoreCase(getSavedPosition(className))) {
                JPanel newTab = tabClass.newInstance();
                return newTab;
            }
        } catch (InstantiationException ex) {
            LOG.error("Could not make instance of "
                    + tabClass.getName());
        } catch (IllegalAccessException ex) {
            LOG.error("Could not make instance of "
                    + tabClass.getName());
        }
        return null;
    }
    
    /**
     * @return the saved position for the given class
     */
    private static String getSavedPosition(String className) {
        String shortClassName = className.substring(
                className.lastIndexOf('.') + 1).toLowerCase();
        ConfigurationKey key = Configuration.makeKey("layout", shortClassName);
        return Configuration.getString(key, "South");
    }

    /**
     * Parse a line in the text file containing the configuration of ArgoUML,
     * "/org/argouml/argo.ini". Also has the side effect of loading the named
     * class if one is found.
     * 
     * @param line the given line
     * @param panelName the name of the panel
     * @param lineNum the number of the current line
     * @param configFile the configuration file name
     * @return the resulting class of the tabpanel
     */
    public static Class parseConfigLine(final String line,
            final String panelName, final int lineNum, 
            final String configFile) {
        
        if (line.startsWith("tabpath:")) {
	    String newPath = stripBeforeColon(line).trim();
	    if (newPath.length() > 0) {
	        tabPath = newPath;
	    }
	    return null;
	}
	else if (line.startsWith(panelName + ":")) {
	    String tabNames = stripBeforeColon(line).trim();
	    StringTokenizer tabAlternatives =
	        new StringTokenizer(tabNames, "|");
	    Class res = null;
	    while (tabAlternatives.hasMoreElements()) {
		String tabSpec = tabAlternatives.nextToken().trim();
		String tabName = tabSpec;  //TODO: arguments
		String tabClassName;

		if (tabName.indexOf('.') > 0) {
		    tabClassName = tabName;
		} else {
		    tabClassName = tabPath + "." + tabName;
		}

		try {
		    res = Class.forName(tabClassName);
		}
		catch (ClassNotFoundException cnfe) { }
		catch (Exception e) {
		    LOG.error("Unanticipated exception, skipping " 
                            + tabName, e);
		}
		if (res != null) {
		    return res;
		}
	    }
	    if (LOG.isDebugEnabled()) {
		LOG.debug("\nCould not find any of these classes:\n"
			  + "TabPath=" + tabPath + "\n"
			  + "Config file=" + configFile + "\n"
			  + "Config line #" + lineNum + ":" + line);
	    }
	}
	return null;
    }

    /**
     * @param s input string
     * @return string with everything before (including) colon stripped
     */
    private static String stripBeforeColon(String s) {
	int colonPos = s.indexOf(":");
	return s.substring(colonPos  + 1);
    }

    private static void updateOrientation(List<JPanel> tabs,
            Orientation orientation) {
        for (JPanel tab : tabs) {
            if (tab instanceof org.argouml.uml.ui.TabProps) {
                tabPropsOrientation = orientation;
            }
        }
    }

}
