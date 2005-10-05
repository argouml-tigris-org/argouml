// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.argouml.kernel.NsumlEnabler;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.xml.sax.InputSource;

/**
 *   This class implements the abstract class Profile for use in modelling
 *   Java language projects.  Eventually, this class may be replaced by
 *   a configurable profile.
 *
 * TODO: (MVW) I see only little Java specific stuff here.
 * Most of this should be moved to a ProfileUML.java file, which
 * should be used by default.
 *
 * TODO: (MVW) Document the use of "argo.defaultModel" in
 * the argo.user.properties file.
 *
 *   @author Curt Arnold
 */
public class ProfileJava extends Profile {

    private static final Logger LOG = Logger.getLogger(ProfileJava.class);

    private Object/*MModel*/ defaultModel;

    /**
     * The constructor.
     */
    public ProfileJava() {
        try {
            getProfileModel();
        } catch (ProfileException e) {
            // TODO: How are we going to handle exception here.
            // I think the profiles need some rethinking - Bob.
            LOG.error("Exception in ProfileJava constructor", e);
        }
    }

    /**
     * @see org.argouml.uml.Profile#formatElement(java.lang.Object,
     * java.lang.Object)
     */
    public String formatElement(Object/*MModelElement*/ element,
				Object namespace) {
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
    protected String defaultAssocEndName(Object/*MAssociationEnd*/ assocEnd,
					 Object namespace) {
	String name = null;
	Object/*MClassifier*/ type = Model.getFacade().getType(assocEnd);
	if (type != null) {
	    name = formatElement(type, namespace);
	} else {
	    name = "unknown type";
	}
	Object/*MMultiplicity*/ mult = 
	    Model.getFacade().getMultiplicity(assocEnd);
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
    protected String defaultAssocName(Object/*MAssociation*/ assoc,
				      Object ns) {
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
    protected String defaultGeneralizationName(Object/*MGeneralization*/ gen,
					       Object namespace) {
	Object/*MGeneralizableElement*/ child = 
	    Model.getFacade().getChild(gen);
	Object/*MGeneralizableElement*/ parent = 
	    Model.getFacade().getParent(gen);
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
	if (name == null)
	    name = "anon";
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
    private void buildPath(StringBuffer buffer,
			   Object/*MModelElement*/ element,
			   String pathSep) {
	if (element != null) {
	    Object/*MNamespace*/ parent = 
	        Model.getFacade().getNamespace(element);
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

    /**
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

    /**
     * @see org.argouml.uml.Profile#getProfileModel()
     */
    public Object/*MModel*/ getProfileModel() throws ProfileException {
        if (defaultModel == null) {
            defaultModel = loadProfileModel();
        }
        return defaultModel;
    }

    /**
     * This function loads the model object containing the default model from
     * either property "argo.defaultModel", or "/org/argouml/default.xmi".
     * May result in null, if the files are not found.
     *
     * @return the model object
     * @throws ProfileException if failed to load profile
     */
    public Object loadProfileModel() throws ProfileException {
        //
        //    get a file name for the default model
        //
        String modelFileName = System.getProperty("argo.defaultModel");
        
        if (modelFileName == null) {
            if (NsumlEnabler.isNsuml()) {
                modelFileName = "/org/argouml/default.xmi";           		
            } else {
                modelFileName = "/org/argouml/model/mdr/mof/default-uml14.xmi";
            }
        }
        //
        //   if there is a default model
        //
        if (modelFileName != null) {
            InputStream is = null;
            //
            //  try to find a file with that name
            //
            try {
                is = new FileInputStream(modelFileName);
            } catch (FileNotFoundException ex) {
                //
                // No file found, try looking in the resources
                //
                // Notice that the class that we run getClass() in needs to be
                // in the same ClassLoader that the default.xmi.
                // If we run using Java Web Start then we have every ArgoUML
                // file in the same jar (i.e. the same ClassLoader).
                is = new Object().getClass().getResourceAsStream(
                        modelFileName);
            }
            if (is != null) {
                try {
                    XmiReader xmiReader = Model.getXmiReader();
                    InputSource inputSource = new InputSource(is);
                    return xmiReader.parseToModel(inputSource);
                } catch (UmlException e) {
                    throw new ProfileException(e);
                }
            } 
            LOG.error("Value of property argo.defaultModel ("
                    + modelFileName
                    + ") did not correspond to an available file.\n");
        }
        return Model.getModelManagementFactory().createModel();
    }
    
}
