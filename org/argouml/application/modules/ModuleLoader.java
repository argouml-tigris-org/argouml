// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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


/*
 * ModuleLoader.java
 *
 * Created on June 11, 2001, 6:26 AM
 */

package org.argouml.application.modules;
import org.argouml.application.api.*;
import org.argouml.application.events.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.jar.*;
import java.net.*;
import org.apache.log4j.*;

// Import the following classes fully qualified to ensure that
// no one can short-circuit our intended inheritance.

import org.argouml.application.security.ArgoJarClassLoader;

/**  Handles loading of modules and plugins for ArgoUML.
 *
 * @author  Will Howery
 * @author  Thierry Lach
 * @since   0.9.4
 */
public class ModuleLoader {

    public static final String CLASS_SUFFIX = ".class";

    // String mModulePropertyFile=null;
    private static ModuleLoader SINGLETON = null;

    private ArrayList mModuleClasses = null;
    private Vector mMenuActionList = null;
    private static Hashtable _singletons = null;
    private static String argoRoot = null;
    private static String argoHome = null;

    /** Make sure the module loader cannot be instantiated from outside.
     */
    private ModuleLoader() {
        _singletons = new Hashtable();
        mModuleClasses = new ArrayList();
        mMenuActionList = new Vector();

	// Use a little trick to find out where Argo is being loaded from.
        String extForm = 
	    org.argouml.application.Main.class.getResource(Argo.ARGOINI)
	    .toExternalForm();
	argoRoot = extForm.substring(0, 
				     extForm.length() - Argo.ARGOINI.length());

	// If it's a jar, clean it up and make it look like a file url
	if (argoRoot.startsWith("jar:")) {
	    argoRoot = argoRoot.substring(4);
	    if (argoRoot.endsWith("!")) {
	        argoRoot = argoRoot.substring(0, argoRoot.length() - 1);
	    }
	}
	if (argoRoot != null) {
	    ArgoModule.cat.info("argoRoot is " + argoRoot);
	    if (argoRoot.startsWith("file:")) {
	        argoHome = new File(argoRoot.substring(5)).getAbsoluteFile()
		    .getParent();
	    }
	    else {
	        argoHome = new File(argoRoot).getAbsoluteFile().getParent();
	    }
	    try {
	        /* JDK 1.2 URLDecoder.decode(String) throws Exception
		 * so we catch it here (and ignore it).
		 * JDK 1.3 and JDK 1.4 do not.
		 */
	        argoHome = java.net.URLDecoder.decode(argoHome);
	    }
	    catch (Exception e) { }

	    ArgoModule.cat.info("argoHome is " + argoHome);
	}
    }

    public static ModuleLoader getInstance() {
        if (SINGLETON == null) {
            SINGLETON = new ModuleLoader();
        }
        return SINGLETON;
    }

    /** Load the internal modules.
     */
    public void initialize() {
	loadInternalModules(getClass(), "standard.modules");
	loadModulesFromExtensionDir();
	loadModulesFromClassPathJars();
	loadModulesFromPredefinedLists();
    }

    /** Search for and load modules from predefined places.
     *  Look in the following locations in the following order, using
     *  System.getProperty() to retrieve the values.
     *
     *  Property name
     *  ${user.dir}
     *  ${user.home}
     *  ${java.home}/lib
     */
    public void loadModulesFromPredefinedLists() {
        String fs = System.getProperty("file.separator");

	String[] path = {
	    System.getProperty("user.dir") + fs + ArgoModule.MODULEFILENAME,
	    System.getProperty("user.dir") + fs
	    + ArgoModule.MODULEFILENAME_ALTERNATE,

	    System.getProperty("user.home") + fs + ArgoModule.MODULEFILENAME,
	    System.getProperty("user.home") + fs 
	    + ArgoModule.MODULEFILENAME_ALTERNATE,

	    System.getProperty("java.home") + fs + "lib" + fs
	    + ArgoModule.MODULEFILENAME,
	    System.getProperty("java.home") + fs + "lib" + fs
	    + ArgoModule.MODULEFILENAME_ALTERNATE
	};

	// Get all of the file paths.  Check if the file exists,
	// is a file (not a directory), and is readable.
	for (int i = 0; i < path.length; i++) {
	    try {
	        File file = new File(path[i]).getCanonicalFile();
	        if (file.exists() && file.isFile() && file.canRead()) {
	            Argo.log.info ("Loading modules from " + file);
		    loadModules(new FileInputStream(file), file.getPath());
		}
	    }
	    catch (FileNotFoundException fnfe) {
	        // Ignore problem
	        ArgoModule.cat.error ("File not found " + path[i], fnfe);
	    }
	    catch (IOException ioe) {
	        // Ignore problem
	        ArgoModule.cat.error ("IO Exception " + path[i], ioe);
	    }
	}
    }

    /** Check the manifest of a jar file for an argo extension.
     */
    private void processJarFile(ClassLoader classloader, File file) {
	JarFile jarfile = null;
        Manifest manifest = null;
	ArgoModule.cat.info("Opening jar file " + file);
        // try {
	// File file = new File(jarName);
	try {
	    jarfile = new JarFile(file);
	}
	catch (Exception e) {
	    ArgoModule.cat.debug("Unable to open " + file, e);
	}

	if (jarfile != null) {
	    try {
	        manifest = jarfile.getManifest();
	        if (manifest == null) {
	            ArgoModule.cat.debug(file + " does not have a manifest");
	        }
	    }
	    catch (Exception e) {
	        ArgoModule.cat.debug("Unable to read manifest of " + file, e);
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
		if (Pluggable.PLUGIN_TITLE.equals(s1) &&
		    Pluggable.PLUGIN_VENDOR.equals(s2) &&
		    key != null &&
		    cname.endsWith(CLASS_SUFFIX))
		{
		    int cslen = CLASS_SUFFIX.length();
		    // This load is not secure.
		    loadClassFromLoader(classloader, key,
					cname.substring(0, 
							cname.length()
							- cslen),
					false);
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

    /** Search for and load modules from classpath, and from
     *  other places.
     */
    public void loadModulesFromExtensionDir() {
	if (argoHome != null) {
	    if (argoHome.startsWith("file:")) {
	        loadModulesFromNamedDirectory(argoHome.substring(5) 
					      + File.separator + "ext");
	    }
	    else {
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
		// don't accidentally mask anything in ArgoJarClassLoader
		// or processJarFile.
		try {
		    jarfile = new JarFile(file[i]);
		    if (jarfile != null) {
	                ClassLoader classloader =
			    new ArgoJarClassLoader(file[i].toURL());
	                // ClassLoader classloader = 
			// getClass().getClassLoader();
	                processJarFile(classloader, file[i]);
	                // processJarFile(getClass().getClassLoader(), file[i]);
		    }
		}
		catch (IOException ioe) { }
	    }
	}
    }

    public void loadModulesFromJar(String filename) {
	if (filename.toLowerCase().endsWith(".jar")) {
	    processJarFile(getClass().getClassLoader(), new File(filename));
	}
    }

    public void loadModulesFromClassPathJars() {
	StringTokenizer st;
	st = new StringTokenizer(System.getProperty("java.class.path"),
	                         System.getProperty("path.separator"));

	while (st.hasMoreTokens()) {
	    String component = st.nextToken();

	    if (component.toLowerCase().endsWith(".jar")) {
	        processJarFile(getClass().getClassLoader(), 
			       new File(component));
	    }

	}

    }

    /** Load modules listed in Argo resources.
     */
    public boolean loadInternalModules(Class fromClass, String rsrcName) {
	ArgoModule.cat.info("Loading modules from " + rsrcName);
	// Load the internal modules
	InputStream is =
	    fromClass.getResourceAsStream(Argo.RESOURCEDIR + rsrcName);
	return (is == null) ? false : loadModules(is, rsrcName);
    }

    public boolean loadModulesFromFile(String moduleFile) {
	Argo.log.info("Loading modules from " + moduleFile);
        try {
	    return loadModules(new FileInputStream(moduleFile), moduleFile);
	}
	catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean keyAlreadyLoaded(String key) {
        ListIterator iterator = mModuleClasses.listIterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj instanceof ArgoModule)
                if (key.equals(((ArgoModule) obj).getModuleKey()))
		    return true;
	}
	return false;
    }

    private void loadClassFromLoader(ClassLoader classloader,
                                     String key,
				     String classname,
				     boolean secure) {

	ArgoModule.cat.debug("Load key:" + key + " class:" + classname);
	if (keyAlreadyLoaded(key)) return;

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
	}
	catch (Exception e) {
	    obj = null;
            Argo.log.warn("Could not instantiate module " + classname);
            ArgoModule.cat.debug("Could not instantiate " + classname, e);
	}
        if (obj != null && obj instanceof ArgoModule) {
            ArgoModule aModule = (ArgoModule) obj;
	    if (aModule.getModuleKey().equals(key) || (!secure)) {
                if (aModule.initializeModule()) {
                    Argo.log.info("Loaded Module: " +
				  aModule.getModuleName());
                    mModuleClasses.add(aModule);
		    fireEvent(ArgoModuleEvent.MODULE_LOADED, aModule);
		    if (aModule instanceof ArgoSingletonModule) {
			ArgoSingletonModule sModule =
			    (ArgoSingletonModule) aModule;
		        try {
			    Class moduleType = sModule.getSingletonType();
		            if (!(_singletons.containsKey(moduleType))) {
			        requestNewSingleton(moduleType, sModule);
		            }
		        }
		        catch (Exception e) {
		            ArgoModule.cat.debug ("Exception", e);
		        }
		    }
                }
	    }
	    else {
	        Argo.log.warn ("Key '" + key
			       + "' does not match module key '" 
			       + aModule.getModuleKey() + "'");
	    }
        }
    }

    public boolean loadModules(InputStream is, String filename) {
        try {
	    LineNumberReader lnr =
		new LineNumberReader(new InputStreamReader(is));
	    while (true) {
	        String realLine = lnr.readLine();
		if (realLine == null) return true;
		String line = realLine.trim();
		if (line.length() == 0) continue;
		if (line.charAt(0) == '#') continue;
		if (line.charAt(0) == '!') continue;
		String sKey = "";
		String sClassName = "";
		try {
		    int equalPos = line.indexOf("=");
		    sKey = line.substring(0, equalPos).trim();
		    sClassName = line.substring(equalPos + 1).trim();
		}
		catch (Exception e) {
		    System.err.println ("Unable to process " + filename +
		                        " at line " + lnr.getLineNumber() +
					" data = '" + realLine + "'");
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
		    Argo.log.warn("Could not load Module: " + sKey);
		    ArgoModule.cat.debug("Could not load Module: " + sKey, e);
		}
		sKey = "";
	    }
	}
	catch (Exception e) {
            e.printStackTrace();
	    System.exit(1);
        }
        return false;
    }

    public void shutdown() {
        try {
            ListIterator iterator = mModuleClasses.listIterator();
            while (iterator.hasNext()) {
                Object obj = iterator.next();
                if (obj instanceof ArgoModule) {
                    ArgoModule m = (ArgoModule) obj;
                    m.shutdownModule();
                }
            }
        } catch (Exception e) {
            Argo.log.warn("ModuleLoader.shutdown "
			  + "Error processing Module shutdown:",
			  e);
            e.printStackTrace();
        }

    }

    public void addModuleAction(Vector popUpActions, Object context) {
        try {
            ListIterator iterator = mModuleClasses.listIterator();
            while (iterator.hasNext()) {
                Object obj = iterator.next();
                if (obj instanceof ArgoModule) {
                    ArgoModule m = (ArgoModule) obj;
                    m.getModulePopUpActions(popUpActions, context);
                }
            }
        } catch (Exception e) {
            Argo.log.warn("ModuleLoader.addModuleAction "
			  + "Error processing Module popup actions:",
			  e);
            e.printStackTrace();
        }
    }

    public ArrayList getModules() {
	return mModuleClasses;
    }

    public Object getModule(String key) {
        ListIterator iterator = mModuleClasses.listIterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj instanceof ArgoModule)
                if (key.equals(((ArgoModule) obj).getModuleKey()))
		    return obj;
	}
	return null;
    }

    /** Activate a loaded module.
     *  @return true if the module was activated,
     *          false if not or if it was already active.
     */
    public boolean activateModule(ArgoModule module) {
        return false;
    }

    /** Gets the current singleton of the module type requested.
     *
     * @return null if there is some problem.
     */
    public static ArgoModule getCurrentSingleton(Class moduleClass) {
	try {
	    return (ArgoModule) _singletons.get(moduleClass);
	}
	catch (Exception e) {
	    return null;
	}
    }

    /** Requests the passed singleton to become the current singleton
     *  of the module type requested.
     *
     *  Singletons may refuse to be activated.  In this case,
     *  requestNewSingleton returns false and does not deactivate the
     *  current singleton.
     */
    public static boolean requestNewSingleton(Class modClass,
					      ArgoSingletonModule
					              moduleInstance)
    {
	boolean rc = moduleInstance.canActivateSingleton();
	ArgoSingletonModule currentSingleton;
	if (!moduleInstance.canActivateSingleton()) return false;
	try {
	    currentSingleton =
		(ArgoSingletonModule) getCurrentSingleton(modClass);
	    if (currentSingleton.canDeactivateSingleton()) {
		currentSingleton.deactivateSingleton();
		_singletons.remove(modClass);
	    }
	    else {
		// The current singleton refused to relinquish control.
		return false;
	    }
	}
	catch (Exception e) {
	    currentSingleton = moduleInstance;
	}
	_singletons.put(modClass, currentSingleton);
	currentSingleton.activateSingleton();
	return true;
    }

    /** Returns a plug-in of a given type.
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
	    Argo.log.warn ("Class " + pluginType.getName() +
			   " is not a core Argo pluggable type.");
	    return null;
	}

	// Check to see that the class is not Pluggable itself
	if (pluginType.equals(Pluggable.class)) {
	    Argo.log.warn ("This is " + pluginType.getName() +
			   ", it cannot be used here.");
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
		else {
		    if (pluggable.inContext(context)) {
			return pluggable;
		    }
		}
	    }
	}
	return null;

    }

    /** Indicates whether a requested plug-in is available.  This guarantees
     *  not to instantiate the plug-in.
     *
     *  @param pluginType a Class which extends Pluggable and indicates
     *                    the type of plug-in to return.
     *
     *  @param context   Additional information used to choose between
     *                    plugins.
     *
     *  @return A plug-in class which extends the type of class passed
     *          as the argument.
     *
     *
     */
    public boolean hasPlugin (Class pluginType,
			      Object[] context) {
	//  Make sure that we are only looking at real extensions
	if (!(pluginType.getName().startsWith(Pluggable.PLUGIN_PREFIX))) {
	    Argo.log.warn ("Class " + pluginType.getName() +
			   " is not a core Argo pluggable type.");
	    return false;
	}

	// Check to see that the class implements Pluggable
	if (!(pluginType.isAssignableFrom(Pluggable.class))) {
	    Argo.log.warn ("Class " + pluginType.getName() +
			   " does not extend Pluggable.");
	    return false;
	}

	// Check to see that the class is not Pluggable itself
	if (pluginType.equals(Pluggable.class)) {
	    Argo.log.warn ("Class " + pluginType.getName() +
			   " does not extend Pluggable.");
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
		else {
		    if (module.inContext(context)) {
			return true;
		    }
		}
	    }
	}
	return false;
    }


    /** Returns all plug-in of a given type.
     *
     *  The type of plug-in returned is determined by the class passed.
     *
     *  @param pluginType a Class which extends Pluggable and indicates
     *                    the type of plug-in to return.
     *
     *  @param context An object (or null) which allows the plugin to
     *                 determine if it should be included in a list.
     *
     *  @return A Vector containing all the plugins of the type
     *          passed for the passed context, or null if none
     *          are available.
     */
    public ArrayList getPlugins (Class pluginType, Object[] context) {

	if (!(pluginType.getName().startsWith(Pluggable.PLUGIN_PREFIX))) {
	    Argo.log.warn ("Class " + pluginType.getName() +
			   " is not a core Argo pluggable type.");
	    return null;
	}

	// Check to see that the class is not Pluggable itself
	if (pluginType.equals(Pluggable.class)) {
	    Argo.log.warn ("This is " + pluginType.getName() +
			   ", it cannot be used here.");
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
		    }
		    else {
			if (pluggable.inContext(context)) {
			    results.add(module);
			}
		    }
		}
	    }
	    catch (Exception ex) {
		ArgoModule.cat.warn("Exception for " + module, ex);
	    }
	}
	return results;
    }

    public String getArgoHome() { return argoHome; }

    public String getArgoRoot() { return argoRoot; }

    private boolean classImplements(Object implementor, Class implemented) {

	if (implemented.isInstance(implementor)) {
	    return true;
	}

	return false;
    }

    class JarFileFilter implements FileFilter {
	public boolean accept(File pathname) {
	    return (pathname.canRead() &&
		    pathname.isFile() &&
		    pathname.getPath().toLowerCase().endsWith(".jar"));
	}
    }

    private void fireEvent(int eventType,  ArgoModule module) {
	ArgoEventPump.getInstance().fireEvent(new ArgoModuleEvent(eventType,
								  module));
    }

} /* end class ModuleLoader */

