// $Id$
// Copyright (c) 2005-2007 The Regents of the University of California. All
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
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * <li>Records the mapping of <code>xmi.id</code>'s to MDR objects as they
 * are resolved so that the map can be used to lookup objects by xmi.id later
 * (used by diagram subsystem to associate GEF/PGML objects with model
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

    private static final Logger LOG =
        Logger.getLogger(XmiReferenceResolverImpl.class);
    
    private static final String PROFILE_RESOURCE_PATH = 
        "/org/argouml/profile/profiles/";
    private static final String PROFILE_BASE_URL = 
        "http://argouml.org/profiles/uml14";

    private static final String USER_PROFILE_BASE_URL = 
        "http://argouml.org/user-profiles/";
    
    private Map<String, Object> idToObjects = 
        Collections.synchronizedMap(new HashMap<String, Object>());

    /**
     * Map indexed by MOF ID.
     */
    private Map<String, XmiReference> objectsToId;

    /**
     * System ID of top level document
     */
    private String topSystemId;

    /**
     * URI form of topSystemID for use in relativization.
     */
    private URI baseUri;

    /**
     * The array of paths in which the models references in other models will be
     * searched.
     * Copied from AndroMDA 3.1 by Ludo (rastaman).
     * see org.andromda.repositories.mdr.MDRXmiReferenceResolverContext
     */
    private List<String> modulesPath = new ArrayList<String>();

    /**
     * Module to URL map to cache things we've already found.
     * Copied from AndroMDA 3.1 by Ludo (rastaman).
     * 
     * see org.andromda.repositories.mdr.MDRXmiReferenceResolverContext
     */
    private Map<String, URL> urlMap = new HashMap<String, URL>();
    
    /**
     * Mapping from URL or absolute reference back to the original SystemID
     * that was read from the input file.  We'll preserve this mapping when
     * we write things back out again.
     */
    private Map<String, String> reverseUrlMap = new HashMap<String, String>();
    
    private boolean profile;

    private Set<String> modelsPublicIds;

    private String modelPublicId;
    
    /**
     * Constructor.
     * @see org.netbeans.lib.jmi.xmi.XmiContext#XmiContext(javax.jmi.reflect.RefPackage[], org.netbeans.api.xmi.XMIInputConfig)
     * (see also {link org.netbeans.api.xmi.XMIReferenceResolver})
     */
    XmiReferenceResolverImpl(RefPackage[] extents, XMIInputConfig config,
            Map<String, XmiReference> objectToIdMap, Set<String> publicIds, 
            List<String> searchDirs, boolean isProfile, String publicId) {
        super(extents, config);
        objectsToId = objectToIdMap;
        modulesPath = searchDirs;
        profile = isProfile;
        modelsPublicIds = publicIds;
        modelPublicId = publicId;
        if (isProfile) modelsPublicIds.add(publicId);
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
    @Override
    public void register(String systemId, String xmiId, RefObject object) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Registering XMI ID '" + xmiId 
                    + "' in system ID '" + systemId 
                    + "' to object with MOF ID '" + object.refMofId() + "'");
        }

        if (topSystemId == null) {
            topSystemId = systemId;
            try {
                baseUri = new URI(
                        systemId.substring(0, systemId.lastIndexOf('/') + 1));
            } catch (URISyntaxException e) {
                LOG.warn("Bad URI syntax for base URI from XMI document "
                        + systemId, e);
                baseUri = null;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Top system ID set to " + topSystemId);
            }
        }

        if (profile) {
            if (systemId.contains(PROFILE_RESOURCE_PATH))
                systemId = PROFILE_BASE_URL + getSuffix(systemId);
            else if (systemId.contains("file:/")) {
                // user defined profile - replace path with corresponding label
                systemId = USER_PROFILE_BASE_URL + modelPublicId;
            } else 
                systemId = PROFILE_BASE_URL + modelPublicId;
        } else if (systemId == topSystemId) {
            systemId = null;
        } else if (reverseUrlMap.get(systemId) != null) {
            systemId = reverseUrlMap.get(systemId);
        } else {
            LOG.debug("Unable to map systemId - " + systemId);
        }
        
        String key;
        if (systemId == null || "".equals(systemId)) {
            // No # here because PGML parser needs bare UUID/xmi.id
            key = xmiId;            
        } else {
            key = systemId + "#" + xmiId;                
        }

        if (!idToObjects.containsKey(key) 
                && !objectsToId.containsKey(object.refMofId())) {
            super.register(systemId, xmiId, object);
            idToObjects.put(key, object);
            objectsToId.put(object.refMofId(),
                    new XmiReference(systemId, xmiId));
        } else {
            if (idToObjects.containsKey(key) 
                    && idToObjects.get(key) != object) {
                LOG.error("Collision - multiple elements with same xmi.id : "
                        + xmiId);
            }
            if (objectsToId.containsKey(object.refMofId())) {
                // For now just skip registering this and ignore the request, 
                // but the real issue is that MagicDraw serializes the same 
                // object in two different composition associations, first in
                // the referencing file and second in the referenced file
                LOG.debug("register called twice for the same object "
                        + "- ignoring second");
                XmiReference ref = objectsToId.get(object.refMofId());
                LOG.debug(" - first reference = " + ref.getSystemId() + "#"
                        + ref.getXmiId());
                LOG.debug(" - 2nd reference   = " + systemId + "#" + xmiId);
            }
        }
    }

    /**
     * Return complete map of all registered objects.
     * 
     * @return map of xmi.id to RefObject correspondences
     */
    public Map<String, Object> getIdToObjectMap() {
        return idToObjects;
    }

    /**
     * Reinitialize the object id maps to the empty state.
     */
    public void clearIdMaps() {
        idToObjects.clear();
        objectsToId.clear();
    }
    
    
    /////////////////////////////////////////////////////
    ////////// Begin AndroMDA Code //////////////////////
    /////////////////////////////////////////////////////

    /**
     * Convert a System ID from an HREF (typically filespec-like) to a URL.
     * Copied from AndroMDA 3.1 by Ludo (rastaman)
     * see @link org.andromda.repositories.mdr.MDRXmiReferenceResolverContext
     * @see org.netbeans.lib.jmi.xmi.XmiContext#toURL(java.lang.String)
     */
    @Override
    public URL toURL(String systemId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("attempting to resolve Xmi Href --> '" + systemId + "'");
        }

        final String suffix = getSuffix(systemId);

        // if the model URL has a suffix of '.zip' or '.jar', get
        // the suffix without it and store it in the urlMap
        String exts = "\\.jar|\\.zip";
        String suffixWithExt = suffix.replaceAll(exts, "");
        URL modelUrl = urlMap.get(suffixWithExt);

        // Several tries to construct a URL that really exists.
        if (modelUrl == null) {
            // If systemId is a valid URL, simply use it
            modelUrl = getValidURL(fixupURL(systemId));
            if (modelUrl == null) {
                // Try to find suffix in module list.
                String modelUrlAsString = findModuleURL(suffix);
                if (!(modelUrlAsString == null 
                        || "".equals(modelUrlAsString))) {
                    modelUrl = getValidURL(modelUrlAsString);
                }
                if (modelUrl == null) {
                    // search the classpath
                    modelUrl = findModelUrlOnClasspath(systemId);
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
                String relativeUri = systemId;
                try {
                    if (baseUri != null) {
                        relativeUri = baseUri.relativize(new URI(systemId))
                                .toString();
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("       system ID " + systemId
                                    + "\n  relativized as " + relativeUri);
                        }
                    } else {
                        relativeUri = systemId;
                    }
                } catch (URISyntaxException e) {
                    LOG.error("Error relativizing system ID " + systemId, e);
                    relativeUri = systemId;
                }
                // TODO: Check whether this is really needed.  I think it's 
                // left over from an incomplete understanding of the MagicDraw
                // composition error problem - tfm
                reverseUrlMap.put(modelUrl.toString(), relativeUri);
                reverseUrlMap.put(systemId, relativeUri);
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
        if (modulesPath == null) {
            return null;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("findModuleURL: modulesPath.size() = " 
                    + modulesPath.size());
        }
        for (String moduleDirectory : modulesPath) {
            File candidate = new File(moduleDirectory, moduleName);
            if (LOG.isDebugEnabled())
                LOG.debug("candidate '" + candidate.toString() + "' exists="
                        + candidate.exists());
            if (candidate.exists()) {
                String urlString;
                try {
                    urlString = candidate.toURI().toURL().toExternalForm();
                } catch (MalformedURLException e) {
                    return null;
                }

                return fixupURL(urlString);
            }
        }
        if (modelsPublicIds.contains(moduleName)) {
            if (LOG.isDebugEnabled())
                LOG.debug("Couldn't find user model (\"" + moduleName 
                    + "\") in modulesPath, attempt " 
                    + "to use a model stored within the zargo file.");
            return moduleName;
        }
        return null;
    }


    
    /**
     * Gets the suffix of the <code>systemId</code>.
     * <p>
     * Copied from AndroMDA 3.1 by Ludo (rastaman). see
     * org.andromda.repositories.mdr.MDRXmiReferenceResolverContext
     * 
     * @param systemId the system identifier.
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
        final String dot = ".";
        String modelName = systemId;
        if (systemId.startsWith(PROFILE_BASE_URL)) {
            String publicId = systemId.substring(PROFILE_BASE_URL.length());
            if (modelsPublicIds.contains(publicId)) {
                modelName = publicId;
            } else { 
                modelName = PROFILE_RESOURCE_PATH + publicId;
            }
        } else {
            int filenameIndex = systemId.lastIndexOf("/");
            if (filenameIndex > 0) {
                modelName = systemId.substring(filenameIndex + 1, systemId
                        .length());
            } else {
                LOG.warn("Received systemId with no '/'" + systemId);
            }


            // remove the first prefix because it may be an archive
            // (like magicdraw)
            if (modelName.lastIndexOf(dot) > 0) {
                modelName = modelName.substring(0, modelName.lastIndexOf(dot));
            }
        }

        URL modelUrl = Thread.currentThread().getContextClassLoader()
                .getResource(modelName);
        // TODO: Not sure whether the above is better in some cases, but
        // the code below is better for both Java Web Start and Eclipse.
        if (modelUrl == null) {
            modelUrl = this.getClass().getResource(modelName);
        }
        // TODO: Is this adequate for finding profiles in Java WebStart jars? 
        //       - tfm
        if (modelUrl == null) {
            if (CLASSPATH_MODEL_SUFFIXES != null
                    && CLASSPATH_MODEL_SUFFIXES.length > 0) {
                for (String suffix : CLASSPATH_MODEL_SUFFIXES) {
                    if (LOG.isDebugEnabled())
                        LOG.debug("searching for model reference --> '"
                                + modelUrl + "'");
                    modelUrl = Thread.currentThread().getContextClassLoader()
                            .getResource(modelName + dot + suffix);
                    if (modelUrl != null) {
                        break;
                    }
                    modelUrl = this.getClass().getResource(modelName);
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
        } catch (MalformedURLException e) {
            url = null;
        } catch (IOException e) {
            url = null;
        } finally {
            stream = null;
        }
        return url;
    }

    /////////////////////////////////////////////////////
    ////////// End AndroMDA Code //////////////////////
    /////////////////////////////////////////////////////

    /**
     * Fix up a file URL for a Zip file or Jar.  Assume it is a single
     * file archive with the entry name the same as the base name.
     */
    private String fixupURL(String url) {
        final String suffix = getSuffix(url);
        if (suffix.endsWith(".zargo")) {
            url = "jar:" + url + "!/"
                    + suffix.substring(0, suffix.length() - 6) + ".xmi";
        } else if (suffix.endsWith(".zip") || suffix.endsWith(".jar")) {
            url = "jar:" + url + "!/"
                    + suffix.substring(0, suffix.length() - 4);
        }
        return url;
    }
}
