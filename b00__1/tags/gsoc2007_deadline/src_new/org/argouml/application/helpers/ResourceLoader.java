// $Id:ResourceLoader.java 12945 2007-07-01 05:48:28Z tfmorris $
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

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * This class manages the resource locations needed within the application.
 * Already loaded resources are cached. The resources can be searched in
 * different locations.
 * <p>
 * Derived from org.tigris.gef.util.ResourceLoader with formatting and
 * variable naming changed to conform to ArgoUML coding standard.
 * <p>
 * We use a local copy to reduce coupling to GEF and so that GEF isn't trying
 * to do uplevel accesses to the application resources (which won't work in
 * environments that enforce strict partitioning between projects eg Eclipse).
 * 
 * @author Original Author: Thorsten Sturm
 */

// NOTE: This is package scope to force callers to use ResourceLoaderWrapper
class ResourceLoader {
    private static HashMap<String, Icon> resourceCache = 
        new HashMap<String, Icon>();
    private static List<String> resourceLocations = new ArrayList<String>();
    private static List<String> resourceExtensions = new ArrayList<String>();

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
        resource = toJavaIdentifier(resource);
        if (isInCache(resource)) {
            return (ImageIcon) resourceCache.get(resource);
        }
    
        ImageIcon res = null;
        java.net.URL imgURL = lookupIconUrl(resource, loader);

        if (imgURL != null) {
            res = new ImageIcon(imgURL, desc);
            synchronized (resourceCache) {
                resourceCache.put(resource, res);
            }
        }
        return res;
    }

    /**
     * Search for the given resource with one of the registered extensions, in
     * one of the registered locations. The URL of the first found is returned.
     * 
     * @param resource
     *            base name of resource to search for
     * @param loader
     *            class loader to use
     * @return URL representing first location the resource was found or null if
     *         it was not found in any of the registered locations.
     */
    static java.net.URL lookupIconUrl(String resource, 
            ClassLoader loader) {
        java.net.URL imgURL = null;
        for (Iterator extensions = resourceExtensions.iterator(); 
                extensions.hasNext();) {
            String tmpExt = (String) extensions.next();
            for (Iterator locations = resourceLocations.iterator(); 
                    locations.hasNext();) {
                String imageName =
                        locations.next() + "/" + resource + "." + tmpExt;
// System.out.println("[ResourceLoader] try loading " + imageName);
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
        return imgURL;
    }

    /**
     * Adds a location (path) to the list of known locations. Locations are
     * searched in the order they are added, so for best performance add the
     * most likely locations first.
     * 
     * @param location
     *            String representation of the new location.
     */
    public static void addResourceLocation(String location) {
        if (!containsLocation(location)) {
            resourceLocations.add(location);
        }
    }

    /**
     * Add an extension to the list of known extensions. Extensions are searched
     * in the order they are added, so for best performance add the most likely
     * extensions first.
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
    
    /*
     * Strip all characters out of <var>s</var> that could not be part of a
     * valid Java identifier. Return either the given string (if all characters
     * were valid), or a new string with all invalid characters stripped out.
     * This allows automatic conversion of strings containing punctuation and
     * spaces to a resource name that can be looked up.
     */
    public static final String toJavaIdentifier(String s) {
        int len = s.length();
        int pos = 0;
        for (int i = 0; i < len; i++, pos++) {
            if (!Character.isJavaIdentifierPart(s.charAt(i))) break;
        }
        if (pos == len) {
            return s;
        }

        StringBuffer buf = new StringBuffer(len);
        buf.append(s.substring(0, pos));

        // skip pos, we know it's not a valid char from above
        for (int i = pos + 1; i < len; i++) {
            char c = s.charAt(i);
            if (Character.isJavaIdentifierPart(c)) {
                buf.append(c);
            }
        }
        return buf.toString();
    }
} /* end class ResourceLoader */

