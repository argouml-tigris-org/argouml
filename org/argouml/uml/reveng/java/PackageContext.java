// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

// File: PackageContext.java
// Classes: PackageContext
// Original Author: Marcus Andersson andersson@users.sourceforge.net

package org.argouml.uml.reveng.java;

import org.argouml.uml.*;

import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;

/**
   This context is a package.
*/
class PackageContext extends Context
{
    /** The package this context represents. */
    private Object mPackage;

    /** The java style name of the package. */
    private String javaName;

    /**
       Create a new context from a package.

       @param base Based on this context.
       @param mPackage Represents this package.
    */
    public PackageContext(Context base, Object mPackage)
    {
	super(base);
	this.mPackage = mPackage;
	javaName = getJavaName(mPackage);
    }

    public Object getInterface(String name)
	throws ClassifierNotFoundException
    {
        // Search in model
        Object mInterface = ModelFacade.lookupIn(mPackage, name);

        if (mInterface == null) {
	    // Try to find it via the classpath
	    try {
		Class classifier;

		// Special case for model
		if (ModelFacade.isAModel(mPackage)) {
		    classifier = Class.forName(name);
		}
		else {
		    classifier =
			Class.forName(javaName + "." + name);
		}
		if (classifier.isInterface()) {
		    mInterface =
			UmlFactory.getFactory().getCore().buildInterface(name, mPackage);
		    ModelFacade.setTaggedValue(mInterface, MMUtil.GENERATED_TAG,
					       "yes");
		}
	    }
	    catch (ClassNotFoundException e) {
		// We didn't find any interface
	    }
	}
	if (mInterface == null && context != null) {
	    // Continue the search through the rest of the model
	    mInterface = context.getInterface(name);
        }
	if (mInterface == null) {
	    throw new ClassifierNotFoundException(name);
	}

        return mInterface;
    }

    /**
       Get a classifier from the model. If it is not in the model, try
       to find it with the CLASSPATH. If found, in the classpath, the
       classifier is created and added to the model. If not found at
       all, a datatype is created and added to the model.

       @param classifierName The name of the classifier to find.
       @return Found classifier.
    */
    public Object get(String name)
	throws ClassifierNotFoundException
    {
	// Search in model
	Object mClassifier = ModelFacade.lookupIn(mPackage, name);

	if (mClassifier == null) {
	    // Try to find it via the classpath
	    try {
		Class classifier;

		// Special case for model
		if (ModelFacade.isAModel(mPackage)) {
		    classifier = Class.forName(name);
		}
		else {
		    classifier =
			Class.forName(javaName + "." + name);
		}
		if (classifier.isInterface()) {
		    mClassifier =
			UmlFactory.getFactory().getCore()
			.buildInterface(name, mPackage);
		}
		else {
		    mClassifier =
			UmlFactory.getFactory().getCore()
			.buildClass(name, mPackage);
		}
		ModelFacade.setTaggedValue(mClassifier,
					   MMUtil.GENERATED_TAG,
					   "yes");
	    }
	    catch (ClassNotFoundException e) {
		// No class or interface found
	    }
	}
	if (mClassifier == null) {
	    // Continue the search through the rest of the model
	    if (context != null) {
		mClassifier = context.get(name);
	    }
	    else {
		// Check for java data types
		if (name.equals("int") ||
		    name.equals("long") ||
		    name.equals("short") ||
		    name.equals("byte") ||
		    name.equals("char") ||
		    name.equals("float") ||
		    name.equals("double") ||
		    name.equals("boolean") ||
		    name.equals("void") ||
		    // How do I represent arrays in UML?
		    name.indexOf("[]") != -1)
		{
		    mClassifier =
			UmlFactory.getFactory().getCore()
			.buildDataType(name, mPackage);
		}
	    }
	}
	if (mClassifier == null) {
	    throw new ClassifierNotFoundException(name);
	}

	return mClassifier;
    }
}

