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

import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;

/**
   This context is an outer class containing inner classes.
*/
class OuterClassifierContext extends Context
{
    /** The classifier this context represents. */
    private Object mClassifier;

    /** The package this classifier belongs to */
    private Object mPackage;

    /** This is appended to classname when searching in classpath. */
    private String namePrefix;

    /** The java style name of the package. */
    private String packageJavaName;

    /**
	Create a new context from a classifier.

	@param base Based on this context.
	@param mClassifier The classifier.
	@param mPackage The package the classifier belongs to.
	@param namePrefix Inner class prefix, like "OuterClassname$"
    */
    public OuterClassifierContext(Context base,
				  Object mClassifier,
				  Object mPackage,
				  String namePrefix)
    {
	super(base);
	this.mClassifier = mClassifier;
	this.mPackage = mPackage;
	this.namePrefix = namePrefix;
	packageJavaName = getJavaName(mPackage);
    }

    public Object getInterface(String name)
	throws ClassifierNotFoundException
    {
        // Search in classifier
        Object mInterface = ModelFacade.lookupIn(mClassifier,name);

	if(mInterface == null) {
	    // Try to find it via the classpath
	    try {
		Class classifier;

		// Special case for model
		if(ModelFacade.isAModel(mPackage)) {
		    classifier = Class.forName(namePrefix + name);
		}
		else {
		    classifier =
			Class.forName(packageJavaName + "." +
				      namePrefix + name);
		}
		if(classifier.isInterface()) {
		    mInterface = UmlFactory.getFactory().getCore().buildInterface(name,mClassifier);
		}
		else {
		    // Only interfaces will do
		    throw new Exception();
		}
	    }
	    catch(Throwable e) {
		// Continue the search through the rest of the model
		if(context != null) {
		    mInterface = context.getInterface(name);
		}
	    }
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
	// Search in classifier
	Object iClassifier = ModelFacade.lookupIn(mClassifier,name);

	if(iClassifier == null) {
	    // Try to find it via the classpath
	    try {
		Class classifier;

		// Special case for model
		if(ModelFacade.isAModel(mPackage)) {
		    classifier = Class.forName(namePrefix + name);
		}
		else {
		    classifier =
			Class.forName(packageJavaName + "." +
				      namePrefix + name);
		}
		if(classifier.isInterface()) {
		    iClassifier = UmlFactory.getFactory().getCore().buildInterface(name,mClassifier);
		}
		else {
		    iClassifier = UmlFactory.getFactory().getCore().buildClass(name,mClassifier);
		}
	    }
	    catch(Throwable e) {
		// Continue the search through the rest of the model
		if(context != null) {
		    iClassifier = context.get(name);
		}
	    }
	}
	return iClassifier;
    }
}

