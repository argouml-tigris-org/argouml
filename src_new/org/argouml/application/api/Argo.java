// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.application.api;

import java.util.ArrayList;

import javax.swing.Icon;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.application.modules.ModuleLoader;

/**
 * The <code>Argo</code> class provides static methods and definitions
 * that can be used as helpers throughout the Argo code.<p>
 *
 * This class is a variation of the <em>Expert</em> design pattern
 * [Grand].  By incorporating a number of unrelated but commonly
 * used methods in a single class, it attempts to decrease the
 * complexity of the overall code while increasing its own complexity.<p>
 *
 * These include
 * <ul>
 * <li>definitions of configuration keys
 * <li>definitions of resource bundle identifier strings
 * <li>methods for localization using <code>gef</code>
 * <li>methods for environment manipulation
 * </ul>
 *
 */
public final class Argo {

    /**
     * Key for argo resource directory.
     */
    public static final String RESOURCEDIR = "/org/argouml/resource/";

    /**
     * argo.ini path.
     */
    public static final String ARGOINI = "/org/argouml/argo.ini";

    /**
     * Key for default startup directory.
     */
    public static final ConfigurationKey KEY_STARTUP_DIR =
	Configuration.makeKey("default", "user", "dir");

    /**
     * Key to show splash screen.
     */
    public static final ConfigurationKey KEY_SPLASH =
	Configuration.makeKey("init", "splash");

    /**
     * Key to preload classes.
     */
    public static final ConfigurationKey KEY_PRELOAD =
	Configuration.makeKey("init", "preload");

    /**
     * Key to report usage statistics.
     */
    public static final ConfigurationKey KEY_EDEM =
	Configuration.makeKey("init", "edem");

    /**
     * Key for last saved project URI.
     */
    public static final ConfigurationKey KEY_MOST_RECENT_PROJECT_FILE =
	Configuration.makeKey("project", "mostrecent", "file");

    /**
     * Key for last generated class/classes directory.
     */
    public static final ConfigurationKey KEY_MOST_RECENT_EXPORT_DIRECTORY =
        Configuration.makeKey("project", "mostrecent", "exportdirectory");

    /**
     * Key to reload last saved project on startup.
     */
    public static final ConfigurationKey KEY_RELOAD_RECENT_PROJECT =
	Configuration.makeKey("init", "project", "loadmostrecent");

    /**
     * Key for number of last recently used file entries in menu list.
     */
    public static final ConfigurationKey KEY_NUMBER_LAST_RECENT_USED =
	Configuration.makeKey("project", "mostrecent", "maxNumber");

    /**
     * Key for screen top.
     */
    public static final ConfigurationKey KEY_SCREEN_TOP_Y =
	Configuration.makeKey("screen", "top");

    /**
     * Key for screen left.
     */
    public static final ConfigurationKey KEY_SCREEN_LEFT_X =
	Configuration.makeKey("screen", "left");

    /**
     * Key for screen width.
     */
    public static final ConfigurationKey KEY_SCREEN_WIDTH =
	Configuration.makeKey("screen", "width");

    /**
     * Key for screen height.
     */
    public static final ConfigurationKey KEY_SCREEN_HEIGHT =
	Configuration.makeKey("screen", "height");

    /**
     * Key for southwest pane width.
     */
    public static final ConfigurationKey KEY_SCREEN_SOUTHWEST_WIDTH =
	Configuration.makeKey("screen", "southwest", "width");

    /**
     * Key for northwest pane width.
     */
    public static final ConfigurationKey KEY_SCREEN_NORTHWEST_WIDTH =
	Configuration.makeKey("screen", "northwest", "width");

    /**
     * Key for southeast pane width.
     */
    public static final ConfigurationKey KEY_SCREEN_SOUTHEAST_WIDTH =
	Configuration.makeKey("screen", "southeast", "width");

    /**
     * Key for northeast pane width.
     */
    public static final ConfigurationKey KEY_SCREEN_NORTHEAST_WIDTH =
	Configuration.makeKey("screen", "northeast", "width");

    /**
     * Key for west pane width.
     */
    public static final ConfigurationKey KEY_SCREEN_WEST_WIDTH =
	Configuration.makeKey("screen", "west", "width");

    /**
     * Key for east pane width.
     */
    public static final ConfigurationKey KEY_SCREEN_EAST_WIDTH =
	Configuration.makeKey("screen", "east", "width");

    /**
     * Key for south pane height.
     */
    public static final ConfigurationKey KEY_SCREEN_SOUTH_HEIGHT =
	Configuration.makeKey("screen", "south", "height");

    /**
     * Key for north pane height.
     */
    public static final ConfigurationKey KEY_SCREEN_NORTH_HEIGHT =
	Configuration.makeKey("screen", "north", "height");

    /**
     * Key for theme.
     */
    public static final ConfigurationKey KEY_SCREEN_THEME =
	Configuration.makeKey("screen", "theme");

    /**
     * Key for look and feel class name.
     */
    public static final ConfigurationKey KEY_LOOK_AND_FEEL_CLASS =
        Configuration.makeKey("screen", "lookAndFeelClass");

    /**
     * Key for theme class name.
     */
    public static final ConfigurationKey KEY_THEME_CLASS =
        Configuration.makeKey("screen", "themeClass");

    /**
     * Key to enable smooth edges of diagram text and lines (anti-aliasing).
     */
    public static final ConfigurationKey KEY_SMOOTH_EDGES =
        Configuration.makeKey("screen", "diagram-antialiasing");

    /**
     * Key for user email address.
     */
    public static final ConfigurationKey KEY_USER_EMAIL =
	Configuration.makeKey("user", "email");

    /**
     * Key for user full name.
     */
    public static final ConfigurationKey KEY_USER_FULLNAME =
	Configuration.makeKey("user", "fullname");

    /**
     * Key for user java reverse engineering classpath.
     */
    public static final ConfigurationKey KEY_USER_IMPORT_CLASSPATH =
	Configuration.makeKey("import", "clazzpath");

    /**
     * Key for input source file encoding used in RE.
     *  Will be used for generated file also.
     */
    public static final ConfigurationKey KEY_INPUT_SOURCE_ENCODING =
	Configuration.makeKey("import", "file", "encoding");
    
    /**
     * Key to store setting of stripping diagrams on XMI import.
     */
    public static final ConfigurationKey KEY_XMI_STRIP_DIAGRAMS =
        Configuration.makeKey("import", "xmi", "stripDiagrams");

    /**
     * Key to store profile/default model.
     */
    public static final ConfigurationKey KEY_DEFAULT_MODEL =
        Configuration.makeKey("defaultModel");

    /**
     * Key for user explorer perspectives.
     *<pre>
     * format:
     * perspective name,rule,rule,rule;perspective name, etc
     *</pre>
     */
    public static final ConfigurationKey KEY_USER_EXPLORER_PERSPECTIVES =
	Configuration.makeKey("explorer", "perspectives");

    /**
     * Key for selecting the locale.
     */
    public static final ConfigurationKey KEY_LOCALE =
        Configuration.makeKey("locale");

    /**
     * Standard definition of the logging category for the console.
     */
    public static final String CONSOLE_LOG = "argo.console.log";

    /**
     * Standard definition of the logging category for the console.
     */
    public static final String ARGO_CONSOLE_SUPPRESS = "argo.console.suppress";

    /**
     * Standard definition of system variable to add text prefix to
     * console log.
     */
    public static final String ARGO_CONSOLE_PREFIX = "argo.console.prefix";

    /**
     * Don't let this class be instantiated.
     */
    private Argo() {
    }

    /**
     * Change the default startup directory.
     *
     * @param dir the directory to save
     */
    public static void setDirectory(String dir) {
	// Store in the user configuration, and
	// let gef know also.
	org.tigris.gef.base.Globals.setLastDirectory(dir);

	// Configuration.setString(KEY_STARTUP_DIR, dir);
    }

    /**
     * Get the default startup directory.
     *
     * @return the startup directory
     */
    public static String getDirectory() {
	// Use the configuration if it exists, otherwise
	// use what gef thinks.
	return Configuration.getString(KEY_STARTUP_DIR,
				       org.tigris.gef.base.Globals
				           .getLastDirectory());
    }

    /**
     * Returns a vector of plugins of the class type passed
     * which satisfy both of the contexts required.
     *
     * If no plugins are available, returns null.
     *
     * @param pluginType class of the plugin to search for
     * @param context plugin-specific query parameters
     * @return a vector of plugins or null
     * @deprecated by Linus Tolke for 0.21.3. This is replaced by the
     *         new module loader.
     */
    public static ArrayList getPlugins(Class pluginType,
                                       Object[] context) {
	return ModuleLoader.getInstance().getPlugins(pluginType, context);
    }

    /**
     * Returns a vector of all plugins of the class type passed.
     *
     * If no plugins are available, returns null.
     *
     * @param pluginType class of the plugin to search for
     * @return a vector of plugins or null
     * @deprecated by Linus Tolke for 0.21.3. This is replaced by the
     *         new module loader.
     */
    public static ArrayList getPlugins(Class pluginType) {
	return ModuleLoader.getInstance().getPlugins(pluginType, null);
    }

    /**
     * Initializes the module loader.  Multiple calls are ignored.
     * @deprecated by Linus Tolke for 0.21.3. There is no reason to
     *         let this call go through the Argo class.
     */
    public static void initializeModules() {
	ModuleLoader.getInstance().initialize();
    }

    /**
     * Convenience helper to access the argo home directory.
     *
     * @return the argo home directory
     * @deprecated by Linus Tolke for 0.21.3. I don't think there is
     *         a reason to let this call go through the Argo class.
     */
    public static String getArgoHome() {
	return ModuleLoader.getInstance().getArgoHome();
    }

    /**
     * Convenience helper to access the argo root directory.
     *
     * @return the argo root directory
     * @deprecated by Linus Tolke for 0.21.3. I don't think there is
     *         a reason to let this call go through the Argo class.
     */
    public static String getArgoRoot() {
	return ModuleLoader.getInstance().getArgoRoot();
    }

    /**
     * Look up an icon resource.
     *
     * @param arg1 the name of the resource to find.
     * @return an Icon
     */
    public static Icon lookupIconResource(String arg1) {
	return ResourceLoaderWrapper.lookupIconResource(arg1);
    }

    /**
     * Look up an icon resource.
     *
     * @param arg1 the name of the resource to find.
     * @param arg2 the description of the resource
     * @return an Icon
     */
    public static Icon lookupIconResource(String arg1, String arg2) {
	return ResourceLoaderWrapper.lookupIconResource(arg1, arg2);
    }

    static {
	if (System.getProperty(ARGO_CONSOLE_SUPPRESS) != null) {
            Category.getRoot().getLoggerRepository().setThreshold(Level.OFF);
	}
    }
}
