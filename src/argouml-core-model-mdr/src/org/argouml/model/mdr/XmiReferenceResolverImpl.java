/* $Id$
 *****************************************************************************
 * Copyright (c) 2005-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris
 *    euluis
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2008 The Regents of the University of California. All
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;

import org.argouml.model.UmlException;
import org.argouml.model.XmiReferenceRuntimeException;
import org.netbeans.api.xmi.XMIInputConfig;
import org.netbeans.lib.jmi.util.DebugException;
import org.netbeans.lib.jmi.xmi.XmiContext;
import org.xml.sax.InputSource;

/**
 * Custom resolver to use with XMI reader.
 * <p>
 *
 * This provides two functions:
 * <nl>
 * <li>Records the mapping of <code>xmi.id</code>'s to MDR objects as they
 * are resolved so that the map can be used to lookup objects by xmi.id later
 * (used by diagram subsystem to associate GEF/PGML objects with model
 * elements).  This map is also used to resolve cross references (HREFs) to
 * other files when reading multiple files linked together.
 * <li>Keeps an inverse map of objects to the xmi.id that they were read in from
 * which can be used to maintain stable xmi.id values on output.
 * <li>Handles search special processing for profiles including the search list
 * of directories which an be used to look them up.
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
        Logger.getLogger(XmiReferenceResolverImpl.class.getName());

    /**
     * Map of href/id to object.  IDs for top level document will have no
     * leading URL piece while others will be in <url>#<id> form
     */
    private Map<String, Map<String, Object>> idToObject =
        Collections.synchronizedMap(new HashMap<String, Map<String, Object>>());

    /**
     * Map indexed by MOF ID containing XmiReference objects.
     */
    private Map<String, XmiReference> mofidToXmiref;

    /**
     * System ID of top level document
     */
    private String topSystemId;

    /**
     * Most recent system ID (public ID in our context) translated by toURL
     */
    private Map<String, URL> pendingProfiles = new HashMap<String, URL>();

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
     * Mapping from absolute resolved URL to the original SystemID
     * that was read from the input file.  We'll preserve this mapping when
     * we write things back out again.
     */
    private Map<String, String> reverseUrlMap = new HashMap<String, String>();

    /**
     * True if top level file is a profile/readonly
     */
    private boolean profile;

    /**
     * Mapping from public ID to system ID for files which have been read.
     */
    private Map<String, String> public2SystemIds;

    private String modelPublicId;

    private MDRModelImplementation modelImpl;

    /**
     * Constructor.
     * @param systemId
     * @see org.netbeans.lib.jmi.xmi.XmiContext#XmiContext(javax.jmi.reflect.RefPackage[], org.netbeans.api.xmi.XMIInputConfig)
     * (see also {link org.netbeans.api.xmi.XMIReferenceResolver})
     */
    // CHECKSTYLE:OFF - ignore too many parameters since API is fixed by MDR
    XmiReferenceResolverImpl(RefPackage[] extents, XMIInputConfig config,
            Map<String, XmiReference> objectToXmiref,
            Map<String, String> publicIds,
            Map<String, Map<String, Object>> idToObject,
            List<String> searchDirs,
            boolean isProfile, String publicId, String systemId,
            MDRModelImplementation modelImplementation) {
    // CHECKSTYLE:ON
        super(extents, config);
        modelImpl = modelImplementation;
        mofidToXmiref = objectToXmiref;
        modulesPath = searchDirs;
        profile = isProfile;
        public2SystemIds = publicIds;
        this.idToObject = idToObject;
        modelPublicId = publicId;
        if (isProfile) {
            if (publicId == null) {
                LOG.log(Level.WARNING, "Profile load with null public ID.  Using system ID - "
                        + systemId);
                modelPublicId = publicId = systemId;
            }
            if (public2SystemIds.containsKey(modelPublicId)) {
                if (systemId.equals(public2SystemIds.get(publicId))) {
                    LOG.log(Level.WARNING, "Loaded profile is being re-read "
                            + "publicId = \"" + publicId + "\";  systemId = \""
                            + systemId + "\".");
                } else {
                    LOG.log(Level.WARNING, "Profile with the duplicate publicId "
                            + "is being loaded! publicId = \"" + publicId
                            + "\"; existing systemId = \""
                            + public2SystemIds.get(publicId)
                            + "\"; new systemId = \"" + systemId + "\".");
                }
            }
            public2SystemIds.put(publicId, systemId);
        }
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
    public void register(final String systemId, final String xmiId,
            final RefObject object) {

        LOG.log(Level.FINE,
                "Registering XMI ID {0} in system ID {1} to object with MOF ID {2}",
                new Object[]{xmiId, systemId, object.refMofId()});

        if (topSystemId == null) {
            topSystemId = systemId;
            try {
                baseUri = new URI(
                        systemId.substring(0, systemId.lastIndexOf('/') + 1));
            } catch (URISyntaxException e) {
                LOG.log(Level.WARNING, "Bad URI syntax for base URI from XMI document "
                        + systemId, e);
                baseUri = null;
            }
            LOG.log(Level.FINE, "Top system ID set to {0}", topSystemId);
        }

        String resolvedSystemId = systemId;
        if (profile && systemId.equals(topSystemId)) {
            resolvedSystemId = modelPublicId;
        } else if (reverseUrlMap.get(systemId) != null) {
            resolvedSystemId = reverseUrlMap.get(systemId);
        } else {
            LOG.log(Level.FINE, "Unable to map systemId - {0}", systemId);
        }

        RefObject o = getReferenceInt(resolvedSystemId, xmiId);
        if (o == null) {
            if (mofidToXmiref.containsKey(object.refMofId())) {
                XmiReference ref = mofidToXmiref.get(object.refMofId());
                // For now just skip registering this and ignore the request,
                // but the real issue is that MagicDraw serializes the same
                // object in two different composition associations, first in
                // the referencing file and second in the referenced file
                LOG.log(Level.FINE, "register called twice for the same object "
                        + "- ignoring second");
                LOG.log(Level.FINE, " - first reference = {0}#{1}", new Object[]{ref.getSystemId(), ref.getXmiId()});
                LOG.log(Level.FINE, " - 2nd reference   = {0}#{1}", new Object[]{systemId, xmiId});
                LOG.log(Level.FINE, " -   resolved system id = {0}", resolvedSystemId );
            } else {
                registerInt(resolvedSystemId, xmiId, object);
                super.register(resolvedSystemId, xmiId, object);
            }
        } else {
            if (o.equals(object)) {
                // Object from a different file, register with superclass so it
                // can resolve all references
                super.register(resolvedSystemId, xmiId, object);
            } else {
               LOG.log(Level.SEVERE, "Collision - multiple elements with same xmi.id : "
                        + xmiId);
                throw new IllegalStateException(
                        "Multiple elements with same xmi.id");
            }
        }
    }

    private RefObject getReferenceInt(String docId, String xmiId)  {
        Map<String, Object> map = idToObject.get(docId);
        if (map != null) {
            RefObject result = (RefObject) map.get(xmiId);
            if (result == null ) {
                LOG.log(Level.FINE, "No internal reference for - {0}#{1}", new Object[]{docId, xmiId});
            }
            return result;
        }
        return null;
    }

    private void registerInt(String docId, String xmiId, RefObject object) {
        Map<String, Object> map = idToObject.get(docId);
        if (map == null) {
            map = new HashMap<String, Object>();
            idToObject.put(docId,map);
        }
        map.put(xmiId, object);
        mofidToXmiref.put(object.refMofId(), new XmiReference(docId, xmiId));
    }

    /*
     * @see org.netbeans.lib.jmi.xmi.XmiContext#getReference(java.lang.String, java.lang.String)
     */
    public RefObject getReference (String docId, String xmiId) {
        RefObject ro = getReferenceInt(docId, xmiId);
        if (ro == null && !idToObject.containsKey(docId)) {
            ro = super.getReference(docId, xmiId);
        }
        if (ro == null) {
            // TODO: Distinguish between deferred resolution and things which
            // are unresolved at end of load and should be reported to user.
            LOG.log(Level.SEVERE, "Failed to resolve " + docId + "#" + xmiId );
        }
        // TODO: Count/report unresolved references
        return ro;
    }

    /**
     * Return map of all registered objects for top level document.
     *
     * @return map of xmi.id to RefObject correspondences
     */
    Map<String, Object> getIdToObjectMap() {
        return getIdToObjectMaps().get(topSystemId);
    }

    /**
     * @return map of maps from xmi ID to object
     */
    Map<String, Map<String, Object>> getIdToObjectMaps() {
        return idToObject;
    }

    /**
     * Reinitialize the object id maps to the empty state.
     */
    void clearIdMaps() {
        getIdToObjectMap().clear();
        mofidToXmiref.clear();
        topSystemId = null;
    }


    /////////////////////////////////////////////////////
    ////////// Begin AndroMDA Code //////////////////////
    /////////////////////////////////////////////////////

    /**
     * Convert a System ID from an HREF which may be relative or otherwise in
     * need of resolution to an absolute URL.
     *
     * Copied from AndroMDA 3.1 by Ludo (rastaman)
     * see @link org.andromda.repositories.mdr.MDRXmiReferenceResolverContext
     * @see org.netbeans.lib.jmi.xmi.XmiContext#toURL(java.lang.String)
     */
    @Override
    public URL toURL(String systemId) {
        LOG.log(Level.FINE,
                "attempting to resolve Xmi Href --> {0}", systemId);

        // TODO: Using just the last piece of the ID leaves the potential for
        // name collisions if two linked files have the same name in different
        // directories
        final String suffix = getSuffix(systemId);

        // if the model URL has a suffix of '.zip' or '.jar', get
        // the suffix without it and store it in the urlMap
        String exts = "\\.jar|\\.zip";
        String suffixWithExt = suffix.replaceAll(exts, "");
        URL modelUrl = urlMap.get(suffixWithExt);

        // Several tries to construct a URL that really exists.
        if (modelUrl == null) {
            if (public2SystemIds.containsKey(systemId)) {
                // If systemId is publicId previously mapped from a systemId,
                // try to use the systemId.
                modelUrl = getValidURL(public2SystemIds.get(systemId));
            }
            if (modelUrl == null) {
                // If systemId is a valid URL, simply use it.
                // TODO: This causes a network connection attempt for profiles
                modelUrl = getValidURL(fixupURL(systemId));
            }
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
                LOG.log(Level.INFO, "Referenced model --> {0}", modelUrl);

                urlMap.put(suffixWithExt, modelUrl);
                pendingProfiles.put(systemId, modelUrl);
                String relativeUri = systemId;
                try {
                    if (baseUri != null) {
                        relativeUri = baseUri.relativize(modelUrl.toURI())
                                .toString();
                        LOG.log(Level.FINE, "  system ID {0} modelUrl {1}\n  relativized as {2}",
                                new Object[]{systemId, modelUrl, relativeUri});
                    } else {
                        relativeUri = systemId;
                    }
                } catch (URISyntaxException e) {
                    LOG.log(Level.SEVERE, "Error relativizing system ID " + systemId, e);
                    relativeUri = systemId;
                }
                // TODO: Check whether this is really needed.  I think it's
                // left over from an incomplete understanding of the MagicDraw
                // composition error problem - tfm
                reverseUrlMap.put(modelUrl.toString(), relativeUri);
                reverseUrlMap.put(systemId, relativeUri);
            } else {
                // TODO: We failed to resolve URL - signal error
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

        LOG.log(Level.FINE, "findModuleURL: modulesPath.size() = {0}", modulesPath.size());

        for (String moduleDirectory : modulesPath) {
            File candidate = new File(moduleDirectory, moduleName);

            LOG.log(Level.FINE,
                    "candidate {0} exists={1}",
                    new Object[]{candidate, candidate.exists()});

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
        if (public2SystemIds.containsKey(moduleName)) {
            LOG.log(Level.FINE,
                    "Couldn't find user model ({0}) in modulesPath, attempt to use a model stored within the zargo file.",
                    moduleName);

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
        if (public2SystemIds.containsKey(systemId)) {
            modelName = public2SystemIds.get(systemId);
        } else {
            int filenameIndex = systemId.lastIndexOf("/");
            if (filenameIndex > 0) {
                modelName = systemId.substring(filenameIndex + 1, systemId
                        .length());
            } else {
                LOG.log(Level.WARNING, "Received systemId with no '/'" + systemId);
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

                    LOG.log(Level.FINE,
                            "searching for model reference --> {0}", modelUrl);

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

    @Override
    public void readExternalDocument(String arg0) {
        // We've got a profile read pending - handle it ourselves now
        URL url = pendingProfiles.remove(arg0);
        if (url != null) {
            InputSource is = new InputSource(url.toExternalForm());
            is.setPublicId(arg0);
            XmiReaderImpl reader = new XmiReaderImpl(modelImpl);
            try {
                reader.parse(is, true);
            } catch (UmlException e) {
                LOG.log(Level.SEVERE, "Error reading referenced profile " + arg0);
                throw new XmiReferenceRuntimeException(arg0, e);
            }
        } else if (!(public2SystemIds.containsKey(arg0))) {
            // Otherwise if it's not something we've already read, just
            // punt to the super class.
            try {
                super.readExternalDocument(arg0);
            } catch (DebugException e) {
                // Unfortunately the MDR super implementation throws
                // DebugException with just the message from the causing
                // exception rather than nesting the exception itself, so
                // we don't have all the information we'd like
                LOG.log(Level.SEVERE, "Error reading external document " + arg0);
                throw new XmiReferenceRuntimeException(arg0, e);
            }
        }
    }
}
