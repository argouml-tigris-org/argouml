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
import java.util.*;
import java.util.jar.*;
import java.net.*;
import org.apache.log4j.*;

// Import the following classes fully qualified to ensure that
// no one can short-circuit our intended inheritance.

import org.argouml.application.security.ArgoClassLoader;

/**
 *
 * @author  Will Howery
 * @author  Thierry Lach
 * @since   0.9.4
 */
public class ModuleLoader {

    public static final String CLASS_SUFFIX = ".class";

    // String mModulePropertyFile=null;
    private static ModuleLoader SINGLETON = null;
    
    private Hashtable mModuleClasses = null;
    private Vector mMenuActionList = null;
    private static Hashtable _singletons = null;
    private static String argoRoot = null; 
    private static String argoHome = null;

    /** Make sure the module loader cannot be instantiated from outside.
     */
    private ModuleLoader() {
        _singletons = new Hashtable();
        mModuleClasses= new Hashtable();
        mMenuActionList=new Vector(); 

	// Use a little trick to find out where Argo is being loaded from.
        String extForm = getClass().getResource(Argo.ARGOINI).toExternalForm();
	argoRoot = extForm.substring(0, extForm.length()-Argo.ARGOINI.length());

	// If it's a jar, clean it up and make it look like a file url
	if (argoRoot.startsWith("jar:")) {
	    argoRoot = argoRoot.substring(4);
	    if (argoRoot.endsWith("!")) {
	        argoRoot = argoRoot.substring(0, argoRoot.length()-1);
	    }
	}
	if (argoRoot != null) {
	    ArgoModule.cat.info("argoRoot is " + argoRoot);
	    if (argoRoot.startsWith("file:")) {
	        argoHome = new File(argoRoot.substring(5)).getAbsoluteFile().getParent();
	    }
	    else {
	        argoHome = new File(argoRoot).getAbsoluteFile().getParent();
	    }
	    ArgoModule.cat.info("argoHome is " + argoHome);
	}
    }
    
    public static ModuleLoader getInstance(){
        if (SINGLETON == null){
            SINGLETON = new ModuleLoader();
        }
        return SINGLETON ;
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
	    System.getProperty("user.home") + fs + ArgoModule.MODULEFILENAME,
	    System.getProperty("java.home") + fs + "lib" +
	                                        fs + ArgoModule.MODULEFILENAME
	};

	// Get all of the file paths.  Check if the file exists,
	// is a file (not a directory), and is readable.
	for (int i = 0; i < path.length; i++) {
	    try {
	        File file = new File(path[i]).getCanonicalFile();
	        if (file.exists() && file.isFile() && file.canRead()) {
	            ArgoModule.cat.debug ("Loading modules from " + file);
		    loadModules(new FileInputStream(file));
		}
	    }
	    catch (FileNotFoundException fnfe) {
	        // Ignore problem
	    }
	    catch (IOException ioe) {
	        // Ignore problem.
	    }
	}
    }

    /** Check the manifest of a jar file for an argo extension.
     */
    private void processJarFile(ClassLoader classloader, File file) {
        try {
	    // File file = new File(jarName);
	    JarFile jarfile = new JarFile(file);
	    Manifest manifest = jarfile.getManifest();
	    Map entries = manifest.getEntries();
	    Iterator iMap = entries.keySet().iterator();
	    while (iMap.hasNext()) {
	        // Look for our specification
		String cname = (String)iMap.next();
		Attributes atrs = manifest.getAttributes(cname);
		String s1 = atrs.getValue(Attributes.Name.SPECIFICATION_TITLE);
		String s2 = atrs.getValue(Attributes.Name.SPECIFICATION_VENDOR);

		// needs-more-work:  If we are in jdk1.3 or above, check
		// EXTENSION_NAME.  Otherwise pass the class name.  It's not
		// as good of a check (we might get duplicate modules with
		// the same key), but it's better than nothing.

		// String key = atrs.getValue(Attributes.Name.EXTENSION_NAME);
		String key = cname;
	        if (s1.equals(Pluggable.PLUGIN_TITLE) &&
		    s2.equals(Pluggable.PLUGIN_VENDOR) &&
		    key != null &&
		    cname.endsWith(CLASS_SUFFIX)) {
		    // This load is not secure.
                    loadClassFromLoader(classloader, key, 
	    cname.substring(0, cname.length()-CLASS_SUFFIX.length()), false);
	        }
	    }
	}
	catch (FileNotFoundException fnfe) {
	    // Ignore problem
	}
	catch (IOException ioe) {
	    // Ignore problem.
	} 
    }

    /** Search for and load modules from classpath, and from
     *  other places.
     */ 
    public void loadModulesFromExtensionDir() {
	if (argoHome != null) {
	    if (argoHome.startsWith("file:")) {
	        loadModulesFromNamedDirectory(argoHome.substring(5) + File.separator + "ext");
	    }
	    else {
	        loadModulesFromNamedDirectory(argoHome + File.separator + "ext");
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
		// don't accidentally mask anything in ArgoClassLoader
		// or processJarFile.
		try {
		    jarfile = new JarFile(file[i]);
		}
		catch (IOException ioe) { }
		if (jarfile != null) {
	            ClassLoader classloader = new ArgoClassLoader(jarfile);
	            // ClassLoader classloader = getClass().getClassLoader();
	            processJarFile(classloader, file[i]); 
	            // processJarFile(getClass().getClassLoader(), file[i]); 
		}
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

	while(st.hasMoreTokens()) {
	    String component = st.nextToken();

	    if (component.toLowerCase().endsWith(".jar")) {
	        processJarFile(getClass().getClassLoader(), new File(component));
	    }

	}

    }

    /** Load modules listed in Argo resources.
     */ 
    public boolean loadInternalModules(Class fromClass, String rsrcName) {
	// Load the internal modules
	InputStream is = fromClass.getResourceAsStream(Argo.RESOURCEDIR + rsrcName);
	return (is == null) ? false : loadModules(is);
    }

    public boolean loadModulesFromFile(String moduleFile) {
        try {
	    return loadModules(new FileInputStream(moduleFile));
	}
	catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private void loadClassFromLoader(ClassLoader classloader,
                                     String key,
				     String classname,
				     boolean secure) {

	if (mModuleClasses.containsKey(key)) return;

	Object obj = null;
	try {
            Class moduleClass = classloader.loadClass(classname);
            obj = moduleClass.newInstance();
	}
	catch (Exception e) {
	    obj = null;
            Argo.log.warn("Could not instantiate " + classname);
	    // System.out.println (e);
	    // e.printStackTrace();
	}
        if (obj!= null && obj instanceof ArgoModule) {
            ArgoModule aModule = (ArgoModule) obj;
	    if (aModule.getModuleKey().equals(key) || (! secure)) {
                if (aModule.initializeModule()){
                    ArgoModule.cat.debug("Loaded Module: " +
			                   aModule.getModuleName());
                    mModuleClasses.put(key, aModule);
		    try {
		        if (aModule instanceof ArgoSingletonModule) {
			    ArgoSingletonModule sModule = (ArgoSingletonModule)aModule;
			    Class moduleType = sModule.getSingletonType();
		            if (! (_singletons.containsKey(moduleType))) {
			        requestNewSingleton(moduleType, sModule);
		            }
		        }
		    }
		    catch (Exception e) { ArgoModule.cat.debug (e); }
                }
	    }
	    else {
	        Argo.log.warn ("Key '" + key + "' does not match module key '" + aModule.getModuleKey() + "'");
	    }
        }
    }

    public boolean loadModules(InputStream is) {
        try {
            PropertyResourceBundle rb = new PropertyResourceBundle(is);
            // mPropertyResourceBundle = rb;
            Enumeration enum = rb.getKeys();
            while (enum.hasMoreElements()){
                String sModule = (String) enum.nextElement();
                try {
                    if (sModule.startsWith("module")){
                        String sClassName = rb.getString(sModule);
			loadClassFromLoader(getClass().getClassLoader(),
			                    sModule,
					    sClassName,
					    true);
		    }
               } catch (Exception e){
                    Argo.log.warn("Could not load Module: " + sModule);
                }
                
                sModule ="";
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
		    System.exit(1);
        }
        return false;
    }

    public void shutdown(){
        try{
            Enumeration enum = mModuleClasses.elements();
            while (enum.hasMoreElements()){
                Object obj = enum.nextElement();
                if (obj instanceof ArgoModule){
                    ArgoModule m = (ArgoModule) obj;
                    m.shutdownModule();
                }
            }
        } catch (Exception e){
            Argo.log.warn("ModuleLoader.shutdown Error processing Module shutdown:"+e);
            e.printStackTrace();
        }
        
    }

    public void addModuleAction(Vector popUpActions, Object context){
        try{
            Enumeration enum = mModuleClasses.elements();
            while (enum.hasMoreElements()){
                Object obj = enum.nextElement();
                if (obj instanceof ArgoModule){
                    ArgoModule m = (ArgoModule) obj;
                    m.getModulePopUpActions(popUpActions,context);
                }
            }
        } catch (Exception e){
            Argo.log.warn("ModuleLoader.addModuleAction Error processing Module popup actions:"+e);
            e.printStackTrace();
        }
    }

    public Hashtable getModules() {
       return mModuleClasses;
    }

    public Object getModule(String key) {
        return mModuleClasses.get(key);
    }

    /** Activate a loaded module.
     *  @return true if the module was activated,
     *          false if not or if it was already active.
     */
    public boolean activateModule(ArgoModule module) {
        return false;
    }

  /** Gets the current singleton of the module type requested.
   */
  public static ArgoModule getCurrentSingleton(Class moduleClass) {
      try {
	  return (ArgoModule)_singletons.get(moduleClass);
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
				    ArgoSingletonModule moduleInstance) {
      boolean rc = moduleInstance.canActivateSingleton();
      ArgoSingletonModule currentSingleton;
      if (! moduleInstance.canActivateSingleton()) return false;
      try {
	  currentSingleton = (ArgoSingletonModule)getCurrentSingleton(modClass);
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
   *          as the argument.
   */
  public Pluggable getPlugin (Class pluginType,
                                    Object context1,
				    Object context2) {
      //  Make sure that we are only looking at real extensions
      if (! (pluginType.getName().startsWith(Pluggable.PLUGIN_PREFIX))) {
          Argo.log.warn ("Class " + pluginType.getName() +
	                      " is not a core Argo pluggable type."); 
          return null;
      }

      // // Check to see that the class implements Pluggable
      // if (! (pluginType.isAssignableFrom(Pluggable.class))) {
          // System.out.println ("Class " + pluginType.getName() +
	                      // " does not extend Pluggable."); 
          // return null;
      // }

      // Check to see that the class is not Pluggable itself
      if (pluginType.equals(Pluggable.class)) {
          Argo.log.warn ("This is " + pluginType.getName() +
	                      ", it cannot be used here."); 
          return null;
      }

      Enumeration e = getModules().elements();
      while (e.hasMoreElements()) {
          Object module = e.nextElement();
	  // if (module.getClass().isAssignableFrom(pluginType))
	  if (classImplements(module, pluginType)) {
	      Pluggable pluggable = (Pluggable)module;
	  // if (pluggable.isModuleType(pluginType)) 
	      if (context1 == null && context2 == null) {
	          return pluggable;
	      }
	      else {
	          if (pluggable.inContext(1, context1)) {
	              if (pluggable.inContext(2, context2)) {
		          return pluggable;
		      }
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
                                  Object context1,
				  Object context2) {
      //  Make sure that we are only looking at real extensions
      if (! (pluginType.getName().startsWith(Pluggable.PLUGIN_PREFIX))) {
          Argo.log.warn ("Class " + pluginType.getName() +
	                      " is not a core Argo pluggable type."); 
          return false;
      }

      // Check to see that the class implements Pluggable
      if (! (pluginType.isAssignableFrom(Pluggable.class))) {
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

      Enumeration e = getModules().elements();
      while (e.hasMoreElements()) {
          Object element = e.nextElement();
	  // if (element.getClass().isAssignableFrom(pluginType))
	  if (classImplements(element, pluginType)) {
              Pluggable module = (Pluggable)element;
	      if (context1 == null && context2 == null) {
	          return true;
	      }
	      else {
		  if (module.inContext(1, context1)) {
		      if (module.inContext(2, context2)) {
		          return true;
		      }
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
  public Vector getPlugins (Class pluginType,
                                  Object context1,
				  Object context2) {

      if (! (pluginType.getName().startsWith(Pluggable.PLUGIN_PREFIX))) {
          Argo.log.warn ("Class " + pluginType.getName() +
	                      " is not a core Argo pluggable type."); 
          return null;
      }

      // // Check to see that the class implements Pluggable
      // if (! (pluginType.isAssignableFrom(Pluggable.class))) {
          // System.out.println ("Class " + pluginType.getName() +
	                      // " does not extend Pluggable."); 
          // return null;
      // }

      // Check to see that the class is not Pluggable itself
      if (pluginType.equals(Pluggable.class)) {
          Argo.log.warn ("This is " + pluginType.getName() +
	                      ", it cannot be used here."); 
          return null;
      }

      Vector results = new Vector();
      Enumeration e = getModules().elements();
      while (e.hasMoreElements()) {
          Object module = e.nextElement();
	  try {
	      Pluggable pluggable = (Pluggable)module;
	      // if (pluggable.isModuleType(pluginType)) 
	      // if (pluggable.getClass().isAssignableFrom(pluginType))
	      if (classImplements(module, pluginType)) {
	           if (context1 == null && context2 == null) {
		       results.add(module);
		   }
		   else {
	               if (pluggable.inContext(1, context1)) {
	                   if (pluggable.inContext(2, context2)) {
		               results.add(module);
		           }
		       }
		   }
	      }
	  }
	  catch (Exception ex) {
                  ArgoModule.cat.debug("Exception for " + module);
		  ArgoModule.cat.debug(ex); 
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

} /* end class ModuleLoader */ 


