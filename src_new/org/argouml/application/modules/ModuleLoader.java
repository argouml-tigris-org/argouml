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

package org.argouml.application.modules;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.api.ArgoModule;
import org.argouml.application.api.ArgoSingletonModule;
import org.argouml.application.api.Pluggable;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoModuleEvent;
import org.argouml.i18n.Translator;

/**
 * Handles loading of modules and plugins for ArgoUML.
 *
 * @author  Will Howery
 * @author  Thierry Lach
 * @since   0.9.4
 * @deprecated by Linus Tolke (0.21.1 March 2006).
 *         Use {@link org.argouml.moduleloader.ModuleLoader2}.
 */
public class ModuleLoader {
    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(ModuleLoader.class);

    /**
     * Class file suffix.
     */
    public static final String CLASS_SUFFIX = ".class";

    // String mModulePropertyFile=null;
    private static ModuleLoader singleton;

    private ArrayList moduleClasses;
    private static Hashtable singletons;
    private static String argoRoot;
    private static String argoHome;

    /**
     * Make sure the module loader cannot be instantiated from outside.
     */
    private ModuleLoader() {
        singletons = new Hashtable();
        moduleClasses = new ArrayList();

	// Use a little trick to find out where Argo is being loaded from.
        String extForm =
	    org.argouml.application.Main.class.getResource(Argo.ARGOINI)
	        .toExternalForm();
	argoRoot =
            extForm.substring(0,
                              extForm.length() - Argo.ARGOINI.length());

	// If it's a jar, clean it up and make it look like a file url
	if (argoRoot.startsWith("jar:")) {
	    argoRoot = argoRoot.substring(4);
	    if (argoRoot.endsWith("!")) {
	        argoRoot = argoRoot.substring(0, argoRoot.length() - 1);
	    }
	}
	if (argoRoot != null) {
	    LOG.info("argoRoot is " + argoRoot);
	    if (argoRoot.startsWith("file:")) {
	        argoHome =
                    new File(argoRoot.substring(5)).getAbsoluteFile()
                        .getParent();
	    } else {
	        argoHome = new File(argoRoot).getAbsoluteFile().getParent();
	    }
	    try {
	        /* JDK 1.2 URLDecoder.decode(String) throws Exception
		 * so we catch it here (and ignore it).
		 * JDK 1.3 and JDK 1.4 do not.
		 */
	        argoHome = java.net.URLDecoder.decode(argoHome);
	    } catch (Exception e) {
		LOG.warn(e);
            }

	    LOG.info("argoHome is " + argoHome);
	}
    }

    /**
     * Get the singleton instance.
     *
     * @return the module loader singleton
     */
    public static ModuleLoader getInstance() {
        if (singleton == null) {
            singleton = new ModuleLoader();
        }
        return singleton;
    }

    /**
     * Load the internal modules.
     */
    public void initialize() {
        // TODO: Move to specific registration.
        loadClassFromLoader(getClass().getClassLoader(),
                "module.language.java.generator",
                "org.argouml.language.java.generator.GeneratorJava",
                true);
        // TODO: Move to specific registration.
        loadClassFromLoader(getClass().getClassLoader(),
                "module.import.java-files",
                "org.argouml.uml.reveng.java.JavaImport",
                true);
        // TODO: Move to specific registration.
        loadClassFromLoader(getClass().getClassLoader(),
                "module.menu.file.export.xmi",
                "org.argouml.ui.ActionExportXMI",
                true);

	loadModulesFromExtensionDir();
	loadModulesFromClassPathJars();
	loadModulesFromPredefinedLists();
    }

    /**
     * Search for and load modules from predefined places.
     * Look in the following locations in the following order, using
     * System.getProperty() to retrieve the values.
     *
     * Property name
     * ${user.dir}
     * ${user.home}
     * ${java.home}/lib
     */
    public void loadModulesFromPredefinedLists() {
        String fs = System.getProperty("file.separator");

        String[] path =
        {
            System.getProperty("user.dir") + fs + ArgoModule.MODULEFILENAME,
            System.getProperty("user.dir") + fs
                + ArgoModule.MODULEFILENAME_ALTERNATE,
            System.getProperty("user.home") + fs + ArgoModule.MODULEFILENAME,
            System.getProperty("user.home") + fs
                + ArgoModule.MODULEFILENAME_ALTERNATE,
            System.getProperty("java.home") + fs + "lib" + fs
                + ArgoModule.MODULEFILENAME,
            System.getProperty("java.home") + fs + "lib" + fs
                + ArgoModule.MODULEFILENAME_ALTERNATE,
	};

	// Get all of the file paths.  Check if the file exists,
	// is a file (not a directory), and is readable.
	for (int i = 0; i < path.length; i++) {
	    try {
	        File file = new File(path[i]).getCanonicalFile();
	        if (file.exists() && file.isFile() && file.canRead()) {
	            LOG.info ("Loading modules from " + file);
		    loadModules(new FileInputStream(file), file.getPath());
		}
	    } catch (FileNotFoundException fnfe) {
	        // Ignore problem
	        LOG.error ("File not found " + path[i], fnfe);
	    } catch (IOException ioe) {
	        // Ignore problem
	        LOG.error ("IO Exception " + path[i], ioe);
	    }
	}
    }

    /**
     * Check the manifest of a jar file for an argo extension.
     */
    private void processJarFile(ClassLoader classloader, File file) {
	JarFile jarfile = null;
        Manifest manifest = null;
	LOG.info("Opening jar file " + file);
        // try {
	// File file = new File(jarName);
	try {
	    jarfile = new JarFile(file);
	} catch (Exception e) {
	    LOG.debug("Unable to open " + file, e);
	}

	if (jarfile != null) {
	    try {
	        manifest = jarfile.getManifest();
	        if (manifest == null) {
	            LOG.debug(file + " does not have a manifest");
	        }
	    } catch (Exception e) {
	        LOG.debug("Unable to read manifest of " + file, e);
		manifest = null;
	    }
	}

	if (manifest != null) {
	    // else {
	    Map entries = manifest.getEntries();
	    Iterator iMap = entries.keySet().iterator();
	    while (iMap.hasNext()) {
                // Look for our specification
                String cname = (String) iMap.next();
                Attributes atrs = manifest.getAttributes(cname);
                String s1 = atrs.getValue(Attributes.Name.SPECIFICATION_TITLE);
                String s2 = atrs.getValue(Attributes.Name.SPECIFICATION_VENDOR);

                // TODO:  If we are in jdk1.3 or above, check
                // EXTENSION_NAME.  Otherwise pass the class name.  It's not
                // as good of a check (we might get duplicate modules with
                // the same key), but it's better than nothing.

                // String key = atrs.getValue(Attributes.Name.EXTENSION_NAME);
                String key = cname;
                if (Pluggable.PLUGIN_TITLE.equals(s1)
                        && Pluggable.PLUGIN_VENDOR.equals(s2)
                        && key != null
                        && cname.endsWith(CLASS_SUFFIX)) {
                    int classNamelen = cname.length() - CLASS_SUFFIX.length();
                    String className = cname.substring(0, classNamelen);
                    className = className.replace('/', '.');
                    // This load is not secure.
                    loadClassFromLoader(classloader, key, className, false);
                }
	    }
	    // }
	}
	// }
	// catch (Exception e) {
	// // Ignore problem.
	// ArgoModule.cat.debug("Exception", e);
	// }
    }

    /**
     * Search for and load modules from classpath, and from
     * other places.
     */
    public void loadModulesFromExtensionDir() {
	if (argoHome != null) {
	    if (argoHome.startsWith("file:")) {
	        loadModulesFromNamedDirectory(argoHome.substring(5)
					      + File.separator + "ext");
	    } else {
	        loadModulesFromNamedDirectory(argoHome
					      + File.separator + "ext");
	    }

	}
        String extdir = System.getProperty("argo.ext.dir");
	if (extdir != null) {
	    loadModulesFromNamedDirectory(extdir);
	}
    }

    private void loadModulesFromNamedDirectory(String dirname) {
	File extensionDir = new File(dirname);
	if (extensionDir.isDirectory()) {
	    File[] file = extensionDir.listFiles(new JarFileFilter());
	    for (int i = 0; i < file.length; i++) {
		JarFile jarfile = null;
		// Try-catch only the JarFile instantiation so we
		// don't accidentally mask anything in processJarFile.
		try {
		    jarfile = new JarFile(file[i]);
		    if (jarfile != null) {
	                ClassLoader classloader =
			    new URLClassLoader(new URL[] {
				file[i].toURL(),
			    });
			Translator.addClassLoader(classloader);
	                processJarFile(classloader, file[i]);
		    }
		} catch (IOException ioe) { }
	    }
	}
    }

    /**
     * Load modules from a jar file.
     *
     * @param filename jar file name to load from
     */
    public void loadModulesFromJar(String filename) {
	if (filename.toLowerCase().endsWith(".jar")) {
	    processJarFile(getClass().getClassLoader(), new File(filename));
	}
    }

    /**
     * Load modules from jars in the class path.
     */
    public void loadModulesFromClassPathJars() {
	StringTokenizer st;
	st =
	    new StringTokenizer(System.getProperty("java.class.path"),
				System.getProperty("path.separator"));

	while (st.hasMoreTokens()) {
	    String component = st.nextToken();

	    if (component.toLowerCase().endsWith(".jar")) {
	        processJarFile(getClass().getClassLoader(),
			       new File(component));
	    }

	}

    }

    /**
     * Load modules from a property file.
     *
     * The load may be successful even if no modules are loaded.
     *
     * @param moduleFile name of file
     * @return false if the load succeeded
     */
    public boolean loadModulesFromFile(String moduleFile) {
	LOG.info("Loading modules from " + moduleFile);
        try {
	    return loadModules(new FileInputStream(moduleFile), moduleFile);
	} catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }

    private boolean keyAlreadyLoaded(String key) {
        ListIterator iterator = moduleClasses.listIterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj instanceof ArgoModule) {
                if (key.equals(((ArgoModule) obj).getModuleKey())) {
		    return true;
		}
	    }
	}
	return false;
    }

    public void loadClassFromLoader(ClassLoader classloader,
                                     String key,
				     String classname,
				     boolean secure) {

	LOG.info("Load key:" + key + " class:" + classname);
	if (keyAlreadyLoaded(key)) {
	    return;
	}

	Object obj = null;
	try {
            Class moduleClass = classloader.loadClass(classname);
	    //
	    // Allow us to load classes with private constructors
	    //
	    // First get the zero-argument constructor
	    Constructor c = moduleClass.getDeclaredConstructor(new Class[]{});
	    // Tell jre we can get at it even though it may not be public
	    c.setAccessible(true);
	    // Instantiate it
	    obj = c.newInstance(new Object[]{});
	} catch (Exception e) {
	    obj = null;
            LOG.error("Could not instantiate module" + classname, e);
	}
        if (obj != null && obj instanceof ArgoModule) {
            ArgoModule aModule = (ArgoModule) obj;
	    if (aModule.getModuleKey().equals(key) || (!secure)) {
                if (aModule.initializeModule()) {
                    LOG.info("Loaded Module: " + aModule.getModuleName());
                    moduleClasses.add(aModule);
		    fireEvent(ArgoEventTypes.MODULE_LOADED, aModule);
		    if (aModule instanceof ArgoSingletonModule) {
			ArgoSingletonModule sModule =
			    (ArgoSingletonModule) aModule;
		        try {
			    Class moduleType = sModule.getSingletonType();
		            if (!(singletons.containsKey(moduleType))) {
			        requestNewSingleton(moduleType, sModule);
		            }
		        } catch (Exception e) {
		            LOG.debug ("Exception", e);
		        }
		    }
                }
	    } else {
	        LOG.warn ("Key '" + key
			       + "' does not match module key '"
			       + aModule.getModuleKey() + "'");
	    }
        }
    }

    /**
     * Load modules from an input stream.
     *
     * The load may be successful even if no modules are loaded.
     *
     * @param is input stream in property file format
     * @param filename the input stream is from (for reporting purposes)
     *
     * @return false if the load succeeded
     */
    public boolean loadModules(InputStream is, String filename) {
        try {
	    LineNumberReader lnr =
		new LineNumberReader(new InputStreamReader(is));
	    while (true) {
	        String realLine = lnr.readLine();
		if (realLine == null) {
		    return true;
		}
		String line = realLine.trim();
		if (line.length() == 0) {
		    continue;
		}
		if (line.charAt(0) == '#') {
		    continue;
		}
		if (line.charAt(0) == '!') {
		    continue;
		}
		String sKey = "";
		String sClassName = "";
		try {
		    int equalPos = line.indexOf("=");
		    sKey = line.substring(0, equalPos).trim();
		    sClassName =
			line.substring(equalPos + 1).trim().replace('/', '.');
		} catch (Exception e) {
		    LOG.warn ("Unable to process " + filename
			      + " at line " + lnr.getLineNumber()
			      + " data = '" + realLine + "'");
		    continue;
		}

                try {
                    if (sKey.startsWith("module.")) {
			loadClassFromLoader(getClass().getClassLoader(),
			                    sKey,
					    sClassName,
					    true);
		    }
		} catch (Exception e) {
		    LOG.warn("Could not load Module: " + sKey);
		    LOG.debug("Could not load Module: " + sKey, e);
		}
		sKey = "";
	    }
	} catch (Exception e) {
            e.printStackTrace();
	    System.exit(1);
        }
        return false;
    }

    /**
     * Shut down all modules.
     */
    public void shutdown() {
        try {
            ListIterator iterator = moduleClasses.listIterator();
            while (iterator.hasNext()) {
                Object obj = iterator.next();
                if (obj instanceof ArgoModule) {
                    ArgoModule m = (ArgoModule) obj;
                    m.shutdownModule();
                }
            }
        } catch (Exception e) {
            LOG.warn("ModuleLoader.shutdown "
			  + "Error processing Module shutdown:",
			  e);
            e.printStackTrace();
        }

    }

    /**
     * Process all of the modules to add popup actions for the
     * given context.
     *
     * @param popUpActions vector of actions
     * @param context to filter by
     */
    public void addModuleAction(Vector popUpActions, Object context) {
        try {
            ListIterator iterator = moduleClasses.listIterator();
            while (iterator.hasNext()) {
                Object obj = iterator.next();
                if (obj instanceof ArgoModule) {
                    ArgoModule m = (ArgoModule) obj;
                    m.getModulePopUpActions(popUpActions, context);
                }
            }
        } catch (Exception e) {
            LOG.warn("ModuleLoader.addModuleAction "
			  + "Error processing Module popup actions:",
			  e);
            e.printStackTrace();
        }
    }

    /**
     * Get the list of modules.
     *
     * @return the list of modules.
     */
    public ArrayList getModules() {
        // TODO: change signature to return Collection
	return moduleClasses;
    }

    /**
     * Locates a module by key.
     *
     * @param key module identifier to find
     * @return a module object or null if not found.
     */
    public Object getModule(String key) {
        ListIterator iterator = moduleClasses.listIterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj instanceof ArgoModule) {
                if (key.equals(((ArgoModule) obj).getModuleKey())) {
		    return obj;
		}
	    }
	}
	return null;
    }

    /**
     * Activate a loaded module.
     *
     * @param module to activate
     * @return true if the module was activated,
     *         false if not or if it was already active.
     */
    public boolean activateModule(ArgoModule module) {
        return false;
    }

    /**
     * Gets the current singleton of the module type requested.
     *
     * @param moduleClass the class of the module singleton
     * @return null if there is some problem.
     */
    public static ArgoModule getCurrentSingleton(Class moduleClass) {
	try {
	    return (ArgoModule) singletons.get(moduleClass);
	} catch (Exception e) {
	    return null;
	}
    }

    /**
     * Requests the passed singleton to become the current singleton
     * of the module type requested.
     *
     * Singletons may refuse to be activated.  In this case,
     * requestNewSingleton returns false and does not deactivate the
     * current singleton.
     *
     * @param modClass class which identifies the singleton
     * @param moduleInstance the module to make the singleton
     * @return true if the singleton is activated
     */
    public static boolean requestNewSingleton(Class modClass,
					      ArgoSingletonModule
					              moduleInstance) {
	ArgoSingletonModule currentSingleton;
	if (!moduleInstance.canActivateSingleton()) {
	    return false;
	}
	try {
	    currentSingleton =
		(ArgoSingletonModule) getCurrentSingleton(modClass);
	    if (currentSingleton.canDeactivateSingleton()) {
		currentSingleton.deactivateSingleton();
		singletons.remove(modClass);
	    } else {
		// The current singleton refused to relinquish control.
		return false;
	    }
	} catch (Exception e) {
	    currentSingleton = moduleInstance;
	}
	singletons.put(modClass, currentSingleton);
	currentSingleton.activateSingleton();
	return true;
    }

    /**
     * Returns a plug-in of a given type.
     *
     *  The type of plug-in returned is determined by the class passed.
     *
     *  @param pluginType a Class which extends Pluggable and indicates
     *                    the type of plug-in to return.
     *  @param context   Additional information used to choose between
     *                    plugins.
     *
     *  @return A plug-in class which extends the type of class passed
     *          as the argument or null if there is some problem.
     */
    public Pluggable getPlugin (Class pluginType,
				Object[] context) {
	//  Make sure that we are only looking at real extensions
	if (!(pluginType.getName().startsWith(Pluggable.PLUGIN_PREFIX))) {
	    LOG.warn ("Class " + pluginType.getName()
		      + " is not a core Argo pluggable type.");
	    return null;
	}

	// Check to see that the class is not Pluggable itself
	if (pluginType.equals(Pluggable.class)) {
	    LOG.warn ("This is " + pluginType.getName()
		      + ", it cannot be used here.");
	    return null;
	}

	// TODO:  The vector should be populated from
	//                   the enumeration in FIFO sequence.

	ListIterator iterator = getModules().listIterator();
	while (iterator.hasNext()) {
	    Object module = iterator.next();
	    // if (module.getClass().isAssignableFrom(pluginType))
	    if (classImplements(module, pluginType)) {
		Pluggable pluggable = (Pluggable) module;
		// if (pluggable.isModuleType(pluginType))
		if (context == null) {
		    return pluggable;
		}
		if (pluggable.inContext(context)) {
		    return pluggable;
		}
	    }
	}
	return null;

    }

    /**
     * Indicates whether a requested plug-in is available.  This guarantees
     * not to instantiate the plug-in.
     *
     * @param pluginType a Class which extends Pluggable and indicates
     *                   the type of plug-in to return.
     *
     * @param context   Additional information used to choose between
     *                   plugins.
     *
     * @return A plug-in class which extends the type of class passed
     *         as the argument.
     */
    public boolean hasPlugin (Class pluginType,
			      Object[] context) {
	//  Make sure that we are only looking at real extensions
	if (!(pluginType.getName().startsWith(Pluggable.PLUGIN_PREFIX))) {
	    LOG.warn ("Class " + pluginType.getName()
		      + " is not a core Argo pluggable type.");
	    return false;
	}

	// Check to see that the class implements Pluggable
	if (!(pluginType.isAssignableFrom(Pluggable.class))) {
	    LOG.warn ("Class " + pluginType.getName()
		      + " does not extend Pluggable.");
	    return false;
	}

	// Check to see that the class is not Pluggable itself
	if (pluginType.equals(Pluggable.class)) {
	    LOG.warn ("Class " + pluginType.getName()
		      + " does not extend Pluggable.");
	    return false;
	}

	// TODO:  The vector should be populated from
	//                   the enumeration in FIFO sequence.

	ListIterator iterator = getModules().listIterator();
	while (iterator.hasNext()) {
	    Object element = iterator.next();
	    // if (element.getClass().isAssignableFrom(pluginType))
	    if (classImplements(element, pluginType)) {
		Pluggable module = (Pluggable) element;
		if (context == null) {
		    return true;
		}
		if (module.inContext(context)) {
		    return true;
		}
	    }
	}
	return false;
    }


    /**
     * Returns all plug-in of a given type.
     *
     * The type of plug-in returned is determined by the class passed.
     *
     * @param pluginType a Class which extends Pluggable and indicates
     *                   the type of plug-in to return.
     *
     * @param context An object (or null) which allows the plugin to
     *                determine if it should be included in a list.
     *
     * @return A Vector containing all the plugins of the type
     *         passed for the passed context, or null if none
     *         are available.
     */
    public ArrayList getPlugins (Class pluginType, Object[] context) {

	if (!(pluginType.getName().startsWith(Pluggable.PLUGIN_PREFIX))) {
	    LOG.warn ("Class " + pluginType.getName()
		      + " is not a core Argo pluggable type.");
	    return null;
	}

	// Check to see that the class is not Pluggable itself
	if (pluginType.equals(Pluggable.class)) {
	    LOG.warn ("This is " + pluginType.getName()
		      + ", it cannot be used here.");
	    return null;
	}

	// TODO:  The vector should be populated from
	//                   the enumeration in FIFO sequence.

	ArrayList results = new ArrayList();
	ListIterator iterator = getModules().listIterator();
	while (iterator.hasNext()) {
	    Object module = iterator.next();
	    try {
		Pluggable pluggable = (Pluggable) module;
		// if (pluggable.isModuleType(pluginType))
		// if (pluggable.getClass().isAssignableFrom(pluginType))
		if (classImplements(module, pluginType)) {
		    if (context == null) {
			results.add(module);
		    } else {
			if (pluggable.inContext(context)) {
			    results.add(module);
			}
		    }
		}
	    } catch (Exception ex) {
		LOG.warn("Exception for " + module, ex);
	    }
	}
	return results;
    }

    /**
     * Returns argo home.
     *
     * @return the argo home directory
     */
    public String getArgoHome() { return argoHome; }

    /**
     * Returns argo root.
     *
     * @return the argo root directory
     */
    public String getArgoRoot() { return argoRoot; }

    private boolean classImplements(Object implementor, Class implemented) {

	if (implemented.isInstance(implementor)) {
	    return true;
	}

	return false;
    }

    class JarFileFilter implements FileFilter {
	/**
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File pathname) {
	    return (pathname.canRead()
		    && pathname.isFile()
		    && pathname.getPath().toLowerCase().endsWith(".jar"));
	}
    }

    private void fireEvent(int eventType,  ArgoModule module) {
	ArgoEventPump.fireEvent(new ArgoModuleEvent(eventType, module));
    }

} /* end class ModuleLoader */

