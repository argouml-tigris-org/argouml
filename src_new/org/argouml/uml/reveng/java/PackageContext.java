// $Id$
// Copyright (c) 2003-2007 The Regents of the University of California. All
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

package org.argouml.uml.reveng.java;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.argouml.model.Facade;
import org.argouml.model.Model;
import org.argouml.uml.reveng.ImportClassLoader;

/**
 * This context is a package.
 *
 * @author Marcus Andersson
 */
class PackageContext extends Context {
    
    private static final Logger LOG = Logger.getLogger(PackageContext.class);
    
    /** The package this context represents. */
    private Object mPackage;

    /** The java style name of the package. */
    private String javaName;

    /**
       Create a new context from a package.

       @param base Based on this context.
       @param thePackage Represents this package.
    */
    public PackageContext(Context base, Object thePackage) {
	super(base);
	this.mPackage = thePackage;
	javaName = getJavaName(thePackage);
    }

    public Object getInterface(String name)
	throws ClassifierNotFoundException {
        return get(name, true);
    }

    /**
     * Get a classifier from the model. If it is not in the model, try
     * to find it with the CLASSPATH. If found, in the classpath, the
     * classifier is created and added to the model. If not found at
     * all, a datatype is created and added to the model.
     *
     * @param name The name of the classifier to find.
     * @return Found classifier.
     * @throws ClassifierNotFoundException if classifier couldn't be located
     */
    public Object get(String name)
        throws ClassifierNotFoundException {
        return get(name, false);
    }
    
    /**
     * Get a classifier from the model. If it is not in the model, try
     * to find it with the CLASSPATH. If found, in the classpath, the
     * classifier is created and added to the model. If not found at
     * all, a datatype is created and added to the model.
     *
     * @param name The name of the classifier to find.
     * @return Found classifier.
     * @throws ClassifierNotFoundException if classifier couldn't be located
     */
    public Object get(String name, boolean interfacesOnly)
	throws ClassifierNotFoundException {
	// Search in model
	Object mClassifier = Model.getFacade().lookupIn(mPackage, name);

	if (mClassifier == null) {
	    Class classifier;
	    // Try to find it via the classpath
	    try {

		// Special case for model
		if (Model.getFacade().isAModel(mPackage)) {
		    classifier = Class.forName(name);
		}
		else {
                    String clazzName = javaName + "." + name;
                    classifier = Class.forName(clazzName);
		}
		if (classifier.isInterface()) {
		    mClassifier =
                            Model.getCoreFactory().buildInterface(
                                    name, mPackage);
		} else {
                    if (!interfacesOnly) {
                        mClassifier =
                                Model.getCoreFactory().buildClass(
                                        name, mPackage);
                    }
		}
                if (mClassifier != null) {
                    setGeneratedTag(mClassifier);
                }
	    }
	    catch (ClassNotFoundException e) {
		// No class or interface found
                // try USER classpath

                try {
                    // Special case for model
                    if (Model.getFacade().isAModel(mPackage)) {
                        classifier =
			    ImportClassLoader.getInstance().loadClass(name);
                    }
                    else {
                        String clazzName = javaName + "." + name;
                        classifier =
			    ImportClassLoader.getInstance()
			        .loadClass(clazzName);
                    }
		    if (classifier.isInterface()) {
			mClassifier =
                                Model.getCoreFactory().buildInterface(
                                        name, mPackage);
		    } else {
		        if (!interfacesOnly) {
                            mClassifier =
                                    Model.getCoreFactory().buildClass(
                                            name, mPackage);
                        }
		    }
                    if (mClassifier != null) {
                        setGeneratedTag(mClassifier);
                    }
                }
                catch (ClassNotFoundException e1) {
                    // Ignore - we'll deal with this later by checking to see
                    // if we found anything.
                } catch (MalformedURLException e1) {
                    LOG.warn("Classpath configuration error", e1);
                }
            }
	}
	if (mClassifier == null) {
	    // Continue the search through the rest of the model
	    if (getContext() != null) {
		mClassifier = getContext().get(name, interfacesOnly);
	    } else {
		// Check for java data types
	        if (!interfacesOnly 
	                && name.equals("int")
	                || name.equals("long")
	                || name.equals("short")
	                || name.equals("byte")
	                || name.equals("char")
	                || name.equals("float")
	                || name.equals("double")
	                || name.equals("boolean")
	                || name.equals("void")
	                // How do I represent arrays in UML?
	                || name.indexOf("[]") != -1) {
		    mClassifier =
			Model.getCoreFactory()
			    .buildDataType(name, mPackage);
		}
	    }
	}
	if (mClassifier == null) {
	    throw new ClassifierNotFoundException(name);
	}

	return mClassifier;
    }

    // Historically this used the value "yes", but all existing
    // code only checks for the presence of the tag, not its value
    private static final String GENERATED_TAG_VALUE = "true";
    
    /**
     * Set the tagged value which indicates this element was 
     * generated as a result of reverse engineering.
     * 
     * @param element the ModelElement to set the tag on
     */
    private void setGeneratedTag(Object element) {
        Object tv =
                Model.getFacade().getTaggedValue(element, Facade.GENERATED_TAG);
        if (tv == null) {
            Model.getExtensionMechanismsHelper().addTaggedValue(
                    element,
                    Model.getExtensionMechanismsFactory().buildTaggedValue(
                            Facade.GENERATED_TAG, GENERATED_TAG_VALUE));
        } else {
            Model.getExtensionMechanismsHelper().setValueOfTag(
                    tv, GENERATED_TAG_VALUE);
        }
    }
}

