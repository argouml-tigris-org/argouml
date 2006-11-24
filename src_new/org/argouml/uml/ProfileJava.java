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

package org.argouml.uml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.xml.sax.InputSource;

/**
 * This class implements the abstract class Profile for use in modelling
 * Java language projects.  Eventually, this class may be replaced by
 * a configurable profile.
 *
 * TODO: (MVW) I see only little Java specific stuff here.
 * Most of this should be moved to a ProfileUML.java file, which
 * should be used by default.
 *
 * TODO: (MVW) Document the use of "argo.defaultModel" in
 * the argo.user.properties file.
 * 
 * TODO: This is an i18n nightmare.  Tons of hardcoded strings and
 * string concatentation.  Change to use message formatting. - tfm 20060226
 *
 * @author Curt Arnold
 */
public class ProfileJava extends Profile {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ProfileJava.class);

    private Object defaultModel;
    
    static final String DEFAULT_PROFILE = 
                            "/org/argouml/model/mdr/profiles/default-uml14.xmi";
    
    private String defaultModelFilename;

    /**
     * The constructor.
     */
    public ProfileJava() {
        // do nothing - profile loading deferred until needed
    }

    /*
     * @see org.argouml.uml.Profile#formatElement(java.lang.Object,
     * java.lang.Object)
     */
    public String formatElement(Object element, Object namespace) {
	String value = null;
	if (element == null) {
	    value = "";
	} else {
	    Object elementNs = Model.getFacade().getNamespace(element);
	    //
	    //   if element is an AssociationEnd use
	    //      the namespace of containing association
	    //
	    if (Model.getFacade().isAAssociationEnd(element)) {
		Object assoc = Model.getFacade().getAssociation(element);
		if (assoc != null) {
		    elementNs = Model.getFacade().getNamespace(assoc);
		}
	    }
	    if (elementNs == namespace) {
		value = Model.getFacade().getName(element);
		if (value == null || value.length() == 0) {
		    value = defaultName(element, namespace);
		}
	    } else {
		StringBuffer buffer = new StringBuffer();
		String pathSep = getPathSeparator();
		buildPath(buffer, element, pathSep);
		value = buffer.toString();
	    }
	}
	return value;
    }

    /**
     * @param assocEnd the given association end name
     * @param namespace the namespace
     * @return the default name for the given associationend
     */
    protected String defaultAssocEndName(Object assocEnd,
					 Object namespace) {
	String name = null;
	Object type = Model.getFacade().getType(assocEnd);
	if (type != null) {
	    name = formatElement(type, namespace);
	} else {
	    name = "unknown type";
	}
	Object mult = Model.getFacade().getMultiplicity(assocEnd);
	if (mult != null) {
	    StringBuffer buf = new StringBuffer(name);
	    buf.append("[");
	    buf.append(Integer.toString(Model.getFacade().getLower(mult)));
	    buf.append("..");
	    int upper = Model.getFacade().getUpper(mult);
	    if (upper >= 0) {
		buf.append(Integer.toString(upper));
	    } else {
		buf.append("*");
	    }
	    buf.append("]");
	    name = buf.toString();
	}
	return name;
    }

    /**
     * This function creates a default association name from its ends.
     *
     * @param assoc the given association
     * @param ns the namespace
     * @return the default association name
     */
    protected String defaultAssocName(Object assoc, Object ns) {
	StringBuffer buf = new StringBuffer();
	Iterator iter = Model.getFacade().getConnections(assoc).iterator();
	for (int i = 0; iter.hasNext(); i++) {
	    if (i != 0) {
		buf.append("-");
	    }
	    buf.append(defaultAssocEndName(iter.next(), ns));
	}
	return buf.toString();
    }

    /**
     * @param gen the given Generalization
     * @param namespace the namespace
     * @return the default generalization name
     */
    protected String defaultGeneralizationName(Object gen, Object namespace) {
	Object child = Model.getFacade().getChild(gen);
        Object parent = Model.getFacade().getParent(gen);
	StringBuffer buf = new StringBuffer();
	buf.append(formatElement(child, namespace));
	buf.append(" extends ");
	buf.append(formatElement(parent, namespace));
	return buf.toString();
    }

    /**
     * @param element the given modelelement
     * @param namespace the namespace
     * @return a default name for this modelelement
     */
    protected String defaultName(Object/*MModelElement*/ element,
				 Object namespace) {
	String name = null;
	if (Model.getFacade().isAAssociationEnd(element)) {
	    name = defaultAssocEndName(element, namespace);
	} else {
	    if (Model.getFacade().isAAssociation(element)) {
		name = defaultAssocName(element, namespace);
	    }
	    if (Model.getFacade().isAGeneralization(element)) {
		name = defaultGeneralizationName(element, namespace);
	    }
	}
	if (name == null) {
            name = "anon";
        }
	return name;
    }

    /**
     * @return the path separator (currently ".")
     */
    protected String getPathSeparator() {
	return ".";
    }

    /**
     * @param buffer (out) the buffer that will contain the path build
     * @param element the given modelelement
     * @param pathSep the path separator character(s)
     */
    private void buildPath(StringBuffer buffer, Object element, 
            String pathSep) {
        if (element != null) {
            Object parent = Model.getFacade().getNamespace(element);
	    if (parent != null && parent != element) {
		buildPath(buffer, parent, pathSep);
		buffer.append(pathSep);
	    }
	    String name = Model.getFacade().getName(element);
	    if (name == null || name.length() == 0) {
		name = defaultName(element, null);
	    }
	    buffer.append(name);
	}
    }

    /**
     * @return the string that separates elements
     */
    protected String getElementSeparator() {
	return ", ";
    }

    /**
     * @return the string that represents an empty collection
     */
    protected String getEmptyCollection() {
	return "[empty]";
    }

    /*
     * @see org.argouml.uml.Profile#formatCollection(java.util.Iterator,
     * java.lang.Object)
     */
    public String formatCollection(Iterator iter, Object namespace) {
	String value = null;
	if (iter.hasNext()) {
	    StringBuffer buffer = new StringBuffer();
	    String elementSep = getElementSeparator();
	    Object obj = null;
	    for (int i = 0; iter.hasNext(); i++) {
		if (i > 0) {
		    buffer.append(elementSep);
		}
		obj = iter.next();
		if (Model.getFacade().isAModelElement(obj)) {
		    buffer.append(formatElement(obj, namespace));
		} else {
		    buffer.append(obj.toString());
		}
	    }
	    value = buffer.toString();
	} else {
	    value = getEmptyCollection();
	}
	return value;
    }

    /*
     * @see org.argouml.uml.Profile#getProfileModel()
     */
    public Object getProfileModel() throws ProfileException {
        if (defaultModel == null) {
            defaultModel = loadProfileModel();
        }
        return defaultModel;
    }
    
    /*
     * @see org.argouml.uml.Profile#setProfileModelFilename(java.lang.String)
     */
    public void setProfileModelFilename(String name) throws ProfileException {
        if (loadProfile(name) != null) {
            Configuration.setString(Argo.KEY_DEFAULT_MODEL, name);
        } else {
            throw new ProfileException("Failed to load profile " + name);
        }
    }
    
    /*
     * @see org.argouml.uml.Profile#getProfileModelFilename()
     */
    public String getProfileModelFilename() {
        if (defaultModelFilename == null) {
            // TODO: Can we remove the use of this property
            // and just use the config file setting? - tfm 20060227
            defaultModelFilename  = System.getProperty("argo.defaultModel",
                    Configuration.getString(Argo.KEY_DEFAULT_MODEL,
                            DEFAULT_PROFILE));            
        }
        return defaultModelFilename;
    }

    /**
     * This function loads our profile or default Model and returns it as a
     * Model. Priority is the property "argo.defaultModel", followed by value
     * stored in the Configuration properties. If these are both null or the
     * profile fails to load, we fall back to our DEFAULT_PROFILE.
     * <p>
     * 
     * Because parts of ArgoUML will fail ungracefully otherwise, we always try
     * to return something, even if it's just an empty model.
     * 
     * @return the profile model object or null
     */
    public Object loadProfileModel() {
        Object profileModel = null;
        String modelFilename = getProfileModelFilename();

        if (modelFilename != null) {
            profileModel = loadProfile(modelFilename);
            if (profileModel != null) {
                return profileModel;
            }
        }

        if (!(DEFAULT_PROFILE.equals(modelFilename))) {
            LOG.warn("Falling back to default profile '" 
                    + DEFAULT_PROFILE + "'");
            profileModel = loadProfile(DEFAULT_PROFILE);
            if (profileModel != null) {
                defaultModelFilename = DEFAULT_PROFILE;
                return profileModel;
            }
        }

        LOG.error("Failed to load any profile - returning empty model");
        defaultModelFilename = "";
        return Model.getModelManagementFactory().createModel();
    }
    
    private Object loadProfile(String modelFilename) {
        LOG.info("Loading profile '" + modelFilename + "'");
        InputStream is = null;
        //
        //  try to find a file with that name
        //
        try {
            File modelFile = new File(modelFilename);
            // TODO: This is in the wrong place.  It's not profile specific.
            // It needs to be moved to main XMI reading code. - tfm 20060326
            if (modelFilename.endsWith("zip")) {
                String filename = modelFile.getName();
                String extension =
                    filename.substring(
                            filename.indexOf('.'), filename.lastIndexOf('.'));
                String path = modelFile.getParent();
                // Add the path of the model to the search path, so we can
                // read dependent models
                if (path != null) {
                    System.setProperty("org.argouml.model.modules_search_path",
                            path);
                }
                try {
                    is = openZipStreamAt(modelFile.toURL(), extension);
                } catch (MalformedURLException e) {
                    LOG.error("Exception while loading profile '"
                            + modelFilename + "'", e);
                    return null;
                } catch (IOException e) {
                    LOG.error("Exception while loading profile '"
                            + modelFilename + "'", e);
                    return null;
                }
            } else {
                is = new FileInputStream(modelFile);
            }
        } catch (FileNotFoundException ex) {
            //
            // No file found, try looking in the resources
            //
            // Note that the class that we run getClass() in needs to be
            // in the same ClassLoader as the profile XMI file.
            // If we run using Java Web Start then we have every ArgoUML
            // file in the same jar (i.e. the same ClassLoader).
            is = new Object().getClass().getResourceAsStream(modelFilename);
        }
        if (is != null) {

            try {
                XmiReader xmiReader = Model.getXmiReader();
                InputSource inputSource = new InputSource(is);
                LOG.info("Loaded profile '" + modelFilename + "'");
                Collection elements = xmiReader.parse(inputSource, true);
                if (elements.size() != 1) {
                    LOG.error("Error loading profile '" + modelFilename
                            + "' expected 1 top level element" + " found "
                            + elements.size());
                    return null;
                }
                return elements.iterator().next();
            } catch (UmlException e) {
                LOG.error("Exception while loading profile '" 
                        + modelFilename + "'", e);
                return null;
            }
        }
        LOG.warn("Profile '" + modelFilename + "' not found");
        return null;
    }

    /**
     * Open a ZipInputStream to the first file found with a given extension.
     *
     * TODO: Remove since this is a duplicate of ZipFilePersister method
     * when we have refactored the Persister subsystem.
     *
     * @param url
     *            The URL of the zip file.
     * @param ext
     *            The required extension.
     * @return the zip stream positioned at the required location.
     * @throws IOException
     *             if there is a problem opening the file.
     */
    private ZipInputStream openZipStreamAt(URL url, String ext)
        throws IOException {
        ZipInputStream zis = new ZipInputStream(url.openStream());
        ZipEntry entry = zis.getNextEntry();
        while (entry != null && !entry.getName().endsWith(ext)) {
            entry = zis.getNextEntry();
        }
        return zis;
    }
}
