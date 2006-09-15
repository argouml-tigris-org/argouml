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

package org.argouml.application.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * This class manages the resource locations needed within the application.
 * Already loaded resources are cached. The resources can be searched in
 * different locations.
 * <p>
 * Derived from org.tigris.gef.util.ResourceLoader with the following changes:
 * 1) invalid characters are no longer stripped from names.  The caller is
 * required to pass a valid name.  2) Formatting and variable naming changed
 * to conform to ArgoUML coding standard.
 * <p>
 * We use a local copy to reduce coupling to GEF and so that GEF isn't trying
 * to do uplevel accesses to the application resources (which won't work in
 * environments that enforce strict partitioning between projects eg Eclipse).
 * 
 * @author Original Author: Thorsten Sturm
 */

// NOTE: This is package scope to force callers to use ResourceLoaderWrapper
class ResourceLoader {
    private static HashMap resourceCache = new HashMap();
    private static List resourceLocations = new ArrayList();
    private static List resourceExtensions = new ArrayList();

    public static ImageIcon lookupIconResource(String resource) {
        return lookupIconResource(resource, resource);
    }

    public static ImageIcon lookupIconResource(String resource, String desc) {
        return lookupIconResource(resource, desc, null);
    }

    public static ImageIcon lookupIconResource(String resource,
            ClassLoader loader) {
        return lookupIconResource(resource, resource, loader);
    }

    /**
     * This method tries to find an ImageIcon for the given name in all known
     * locations. The file extension of the used image file can be any of the
     * known extensions.
     * 
     * @param resource
     *            Name of the image to be looked after.
     * @param desc
     *            A description for the ImageIcon.
     * @param loader
     *            The class loader that should be used for loading the resource.
     * @return ImageIcon for the given name, null if no image could be found.
     */
    public static ImageIcon lookupIconResource(String resource, String desc,
            ClassLoader loader) {
        if (isInCache(resource)) {
            return (ImageIcon) resourceCache.get(resource);
        }
    
        ImageIcon res = null;
        java.net.URL imgURL = null;
        try {
            for (Iterator extensions = resourceExtensions.iterator(); 
                    extensions.hasNext();) {
                String tmpExt = (String) extensions.next();
                for (Iterator locations = resourceLocations.iterator(); 
                        locations.hasNext();) {
                    String imageName =
                            (String) locations.next() + "/" + resource + "."
                                    + tmpExt;
                    // System.out.println("[ResourceLoader] try loading " +
                    // imageName);
                    if (loader == null) {
                        imgURL = ResourceLoader.class.getResource(imageName);
                    } else {
                        imgURL = loader.getResource(imageName);
                    }
                    if (imgURL != null) {
                        break;
                    }
                }
                if (imgURL != null) {
                    break;
                }
            }
            if (imgURL == null) {
                return null;
            }
            res = new ImageIcon(imgURL, desc);
            synchronized (resourceCache) {
                resourceCache.put(resource, res);
            }
            return res;
        } catch (Exception ex) {
            System.err.println("Exception in looking up IconResource");
            ex.printStackTrace();
            return new ImageIcon(resource);
        }
    }

    /**
     * This method adds a new location to the list of known locations.
     *
     * @param location String representation of the new location.
     */
    public static void addResourceLocation(String location) {
        if (!containsLocation(location)) {
            resourceLocations.add(location);
        }
    }

    /**
     * This method adds a new extension to the list of known extensions.
     * 
     * @param extension
     *            String representation of the new extension.
     */
    public static void addResourceExtension(String extension) {
        if (!containsExtension(extension)) {
            resourceExtensions.add(extension);
        }
    }

    /**
     * This method removes a location from the list of known locations.
     * 
     * @param location
     *            String representation of the location to be removed.
     */
    public static void removeResourceLocation(String location) {
        for (Iterator iter = resourceLocations.iterator(); iter.hasNext();) {
            String loc = (String) iter.next();
            if (loc.equals(location)) {
                resourceLocations.remove(loc);
                break;
            }
        }
    }

    /**
     * This method removes a extension from the list of known extensions.
     *
     * @param extension String representation of the extension to be removed.
     */
    public static void removeResourceExtension(String extension) {
        for (Iterator iter = resourceExtensions.iterator(); iter.hasNext();) {
            String ext = (String) iter.next();
            if (ext.equals(extension)) {
                resourceExtensions.remove(ext);
                break;
            }
        }
    }

    public static boolean containsExtension(String extension) {
        return resourceExtensions.contains(extension);
    }

    public static boolean containsLocation(String location) {
        return resourceLocations.contains(location);
    }

    public static boolean isInCache(String resource) {
        return resourceCache.containsKey(resource);
    }
} /* end class ResourceLoader */

