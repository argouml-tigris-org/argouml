// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.model.mdr;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;

import org.apache.log4j.Logger;
import org.netbeans.api.xmi.XMIInputConfig;
import org.netbeans.lib.jmi.xmi.XmiContext;

/**
 * Custom resolver to use with XMI reader.
 * <p>
 * 
 * This provides two functions:
 * <nl>
 * <li>Records the mapping of <code>xmi.id</code>'s to MDR's object as they
 * are resolved so that the map can be used to lookup objects by xmi.id later
 * (used by diagram subsystem to associated GEF/PGML objects with model
 * elements).
 * <li>Resolves a System ID to a fully specified URL which can be used by MDR
 * to open and read the referenced content. The standard MDR resolver is
 * extended to support that "jar:" protocol for URLs, allowing it to handle
 * multi-file Zip/jar archives contained a set of models. The method
 * <code>toUrl</code> and supporting methods and fields was copied from the
 * AndroMDA 3.1 implementation
 * (org.andromda.repositories.mdr.MDRXmiReferenceResolverContext) by Ludo
 * (rastaman).
 * </nl>
 * <p>
 * NOTE: This is not a standalone implementation of the reference resolver since
 * it depends on extending the specific MDR implementation.
 * 
 * @author Tom Morris
 * 
 */
class XmiReferenceResolverImpl extends XmiContext {

    private Map idToObjects = Collections.synchronizedMap(new HashMap());

    private Map objectsToId;

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(XmiReferenceResolverImpl.class);

    /**
     * The array of paths in which the models references in other models will be
     * searched.
     * Copied from AndroMDA 3.1 by Ludo (rastaman).
     * see org.andromda.repositories.mdr.MDRXmiReferenceResolverContext
     */
    private static List modulesPath = new Vector();

    /**
     * Module to URL map to cache things we've already found.
     * Copied from AndroMDA 3.1 by Ludo (rastaman).
     * 
     * see org.andromda.repositories.mdr.MDRXmiReferenceResolverContext
     */
    private static Map urlMap = new HashMap();
    
    /**
     * Constructor.
     * @see org.netbeans.lib.jmi.xmi.XmiContext#XmiContext(javax.jmi.reflect.RefPackage[], org.netbeans.api.xmi.XMIInputConfig)
     * (see also {link org.netbeans.api.xmi.XMIReferenceResolver})
     */
    XmiReferenceResolverImpl(RefPackage[] extents, 
            XMIInputConfig config, Map objectToIdMap) {
        super(extents, config);
        registerSearchPath();
        objectsToId = objectToIdMap;
    }

    /**
     * Save registered ID in our object map.
     * 
     * @param systemId
     *            URL of XMI field
     * @param xmiId
     *            xmi.id string for current object
     * @param object
     *            referenced object
     */
    public void register(String systemId, String xmiId, RefObject object) {
        super.register(systemId, xmiId, object);
        if (!idToObjects.containsKey(xmiId)) {
            // TODO: This needs to include the SystemID as well - tfm
            idToObjects.put(xmiId, object);
            objectsToId.put(object.refMofId(),
                    new XmiReference(systemId, xmiId));
        } else {
            LOG.error("Collision - multiple elements with same xmi.id : " 
                    + xmiId);
        }
    }

    /**
     * Return complete map of all registered objects.
     * 
     * @return map of xmi.id to RefObject correspondances
     */
    public Map getIdToObjectMap() {
        return idToObjects;
    }

    /**
     * Reinitialize the object id maps to the empty state.
     */
    public void clearIdMaps() {
        idToObjects.clear();
        objectsToId.clear();
    }
    
    /*
     * Set up module search path to be used by AndroMDA URL resolver.
     * The path is retrieved from shared state (a system property) which
     * is set up externally (currently by 
     * @link org.argouml.uml.ProfileJava#loadProfile() which is probably
     * the wrong place for it)
     */
    private void registerSearchPath() {
        //TODO: Replace by something elegant (i.e in the Model, or anything 
        //accessible by the components of ArgoUML base and this class).
        String path = 
            System.getProperty("org.argouml.model.modules_search_path");
        if (path != null) {
            String[] paths = path.split(",");
            for (int i = 0; i < paths.length; i++) {
                addModuleSearchPath(paths[i]);
            }
        }
    }
    
    /////////////////////////////////////////////////////
    ////////// Begin AndroMDA Code //////////////////////
    /////////////////////////////////////////////////////

    /**
     * Return the module search paths as a String array.
     * @return String[] An array with all the module search paths
     */
    public static String[] getModuleSearchPath() {
        return (String[]) modulesPath.toArray(new String[modulesPath.size()]);
    }
    
    /**
     * Add a path to module search path. Can be used by modules to register new
     * paths to metamodels facades / profiles.
     * 
     * @param path
     *            The path to add to the module search paths
     */
    public static void addModuleSearchPath(String path) {
        if (!modulesPath.contains(path)) {
            modulesPath.add(path);
        }
    }
    
    /**
     * Remove a path from the list of modules search paths.
     * 
     * @param path The path to remove
     */
    public static void removeModuleSearchPath(String path) {
        modulesPath.remove(path);
    }

    /**
     * Convert a System ID from an HREF (typically filespec-like) to a URL.
     * Copied from AndroMDA 3.1 by Ludo (rastaman)
     * see @link org.andromda.repositories.mdr.MDRXmiReferenceResolverContext
     * @see org.netbeans.lib.jmi.xmi.XmiContext#toURL(java.lang.String)
     */
    public URL toURL(String systemId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("attempting to resolve Xmi Href --> '" + systemId + "'");
        }

        final String suffix = getSuffix(systemId);

        // if the model URL has a suffix of '.zip' or '.jar', get
        // the suffix without it and store it in the urlMap
        String exts = "\\.jar|\\.zip";
        String suffixWithExt = suffix.replaceAll(exts, "");
        URL modelUrl = (URL) urlMap.get(suffixWithExt);

        // Several tries to construct a URL that really exists.
        if (modelUrl == null) {
            // If systemId is a valid URL, simply use it
            modelUrl = this.getValidURL(systemId);
            if (modelUrl == null) {
                // Try to find suffix in module list.
                String modelUrlAsString = findModuleURL(suffix);
                if (!(modelUrlAsString == null 
                        || "".equals(modelUrlAsString))) {
                    modelUrl = getValidURL(modelUrlAsString);
                }
                if (modelUrl == null) {
                    // search the classpath
                    modelUrl = this.findModelUrlOnClasspath(systemId);
                }
                if (modelUrl == null) {
                    // Give up and let superclass deal with it.
                    modelUrl = super.toURL(systemId);
                }
            }
            // if we've found the module model, log it
            // and place it in the map so we don't have to
            // find it if we need it again.
            if (modelUrl != null) {
                LOG.info("Referenced model --> '" + modelUrl + "'");
                urlMap.put(suffixWithExt, modelUrl);
            }
        }
        return modelUrl;
    }

    /**
     * Finds a module in the module search path. 
     * <p>
     * Copied from AndroMDA 3.1 by Ludo (rastaman).
     * 
     * see org.andromda.repositories.mdr.MDRXmiReferenceResolverContext
     * @param moduleName
     *            the name of the module without any path
     * @return the complete URL string of the module if found (null if not
     *         found)
     */
    private String findModuleURL(String moduleName) {
        
        String[] moduleSearchPath = getModuleSearchPath();
        
        if (moduleSearchPath == null || moduleSearchPath.length == 0) {
            return null;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("findModuleURL: moduleSearchPath.length="
                    + moduleSearchPath.length);
        }
        for (int i = 0; i < moduleSearchPath.length; i++) {
            File candidate = new File(moduleSearchPath[i], moduleName);
            if (LOG.isDebugEnabled())
                LOG.debug("candidate '" + candidate.toString() + "' exists="
                        + candidate.exists());
            if (candidate.exists()) {
                String urlString;
                try {
                    urlString = candidate.toURL().toExternalForm();
                } catch (MalformedURLException e) {
                    return null;
                }

                if (moduleName.endsWith(".zip") 
                        || moduleName.endsWith(".jar")) {
                    // typical case for MagicDraw
                    urlString = "jar:" + urlString + "!/"
                            + moduleName.substring(0, moduleName.length() - 4);
                }
                return urlString;
            }
        }
        return null;
    }

    /**
     * Gets the suffix of the <code>systemId</code>.<p>
     * Copied from AndroMDA 3.1 by Ludo (rastaman).
     * see org.andromda.repositories.mdr.MDRXmiReferenceResolverContext
     * @param systemId
     *            the system identifier.
     * @return the suffix as a String.
     */
    private String getSuffix(String systemId) {
        int lastSlash = systemId.lastIndexOf("/");
        if (lastSlash > 0) {
            String suffix = systemId.substring(lastSlash + 1);
            return suffix;
        }
        return systemId;
    }

    /**
     * The suffixes to use when searching for referenced models on the
     * classpath.
     * <p>
     * Copied from AndroMDA 3.1 by Ludo (rastaman).
     * see org.andromda.repositories.mdr.MDRXmiReferenceResolverContext
     */
    protected static final String[] CLASSPATH_MODEL_SUFFIXES = 
        new String[] {"xml", "xmi", };

    /**
     * Searches for the model URL on the classpath.
     * <p>
     * Copied from AndroMDA 3.1 by Ludo (rastaman).
     * 
     * see org.andromda.repositories.mdr.MDRXmiReferenceResolverContext
     * 
     * @param systemId
     *            the system identifier.
     * @return the suffix as a String.
     */
    private URL findModelUrlOnClasspath(String systemId) {
        String modelName = systemId.substring(systemId.lastIndexOf("/") + 1,
                systemId.length());
        String dot = ".";
        // remove the first prefix because it may be an archive
        // (like magicdraw)
        modelName = modelName.substring(0, modelName.lastIndexOf(dot));

        URL modelUrl = Thread.currentThread().getContextClassLoader()
                .getResource(modelName);
        if (modelUrl == null) {
            if (CLASSPATH_MODEL_SUFFIXES != null
                    && CLASSPATH_MODEL_SUFFIXES.length > 0) {
                int suffixNum = CLASSPATH_MODEL_SUFFIXES.length;
                for (int ctr = 0; ctr < suffixNum; ctr++) {
                    if (LOG.isDebugEnabled())
                        LOG.debug("searching for model reference --> '"
                                + modelUrl + "'");
                    String suffix = CLASSPATH_MODEL_SUFFIXES[ctr];
                    modelUrl = Thread.currentThread().getContextClassLoader()
                            .getResource(modelName + dot + suffix);
                    if (modelUrl != null) {
                        break;
                    }
                }
            }
        }
        return modelUrl;
    }

    /**
     * Returns a URL if the systemId is valid. Returns null otherwise. Catches
     * exceptions as necessary.
     * <p>
     * Copied from AndroMDA 3.1 by Ludo (rastaman). See
     * org.andromda.repositories.mdr.MDRXmiReferenceResolverContext
     * 
     * @param systemId
     *            the system id
     * @return the URL (if valid) or null
     */
    private URL getValidURL(String systemId) {
        InputStream stream = null;
        URL url = null;
        try {
            url = new URL(systemId);
            stream = url.openStream();
            stream.close();
        } catch (Exception e) {
            // TODO: What kinds of exceptions are expected here? - tfm
            url = null;
        } finally {
            stream = null;
        }
        return url;
    }    

    /////////////////////////////////////////////////////
    ////////// End AndroMDA Code //////////////////////
    /////////////////////////////////////////////////////

}
