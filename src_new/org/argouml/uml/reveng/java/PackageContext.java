// $Id$

/*
  JavaRE - Code generation and reverse engineering for UML and Java
  Copyright (C) 2000 Marcus Andersson andersson@users.sourceforge.net

  This library is free software; you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 2.1 of the
  License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
  USA

*/

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
        Object mInterface = ModelFacade.lookupNamespaceFor(mPackage,name);

        if(mInterface == null) {
	    // Try to find it via the classpath
	    try {
		Class classifier;

		// Special case for model
		if(ModelFacade.isAModel(mPackage)) {
		    classifier = Class.forName(name);
		}
		else {
		    classifier =
			Class.forName(javaName + "." + name);
		}
		if(classifier.isInterface()) {
		    mInterface = UmlFactory.getFactory().getCore().buildInterface(name,mPackage);
		    ModelFacade.setTaggedValue(mInterface,MMUtil.GENERATED_TAG,"yes");
		}
	    }
	    catch(Throwable e) {
		// We didn't find any interface
	    }
	}
	if(mInterface == null && context != null) {
	    // Continue the search through the rest of the model
	    mInterface = context.getInterface(name);
        }
	if(mInterface == null) {
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
	Object mClassifier = ModelFacade.lookupNamespaceFor(mPackage,name);

	if(mClassifier == null) {
	    // Try to find it via the classpath
	    try {
		Class classifier;

		// Special case for model
		if(ModelFacade.isAModel(mPackage)) {
		    classifier = Class.forName(name);
		}
		else {
		    classifier =
			Class.forName(javaName + "." + name);
		}
		if(classifier.isInterface()) {
		    mClassifier = UmlFactory.getFactory().getCore().buildInterface(name,mPackage);
		}
		else {
		    mClassifier = UmlFactory.getFactory().getCore().buildClass(name,mPackage);
		}
		ModelFacade.setTaggedValue(mClassifier,MMUtil.GENERATED_TAG,"yes");
	    }
	    catch(Throwable e) {
		// No class or interface found
	    }
	}
	if(mClassifier == null) {
	    // Continue the search through the rest of the model
	    if(context != null) {
		mClassifier = context.get(name);
	    }
	    else {
		// Check for java data types
		if(name.equals("int") ||
		   name.equals("long") ||
		   name.equals("short") ||
		   name.equals("byte") ||
		   name.equals("char") ||
		   name.equals("float") ||
		   name.equals("double") ||
		   name.equals("boolean") ||
		   name.equals("void") ||
		   // How do I represent arrays in UML?
		   name.indexOf("[]") != -1) {
		    mClassifier = UmlFactory.getFactory().getCore().buildDataType(name,mPackage);
		}
	    }
	}
	if(mClassifier == null) {
	    throw new ClassifierNotFoundException(name);
	}

	return mClassifier;
    }
}

