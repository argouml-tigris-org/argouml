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

// File: OuterClassifierContext.java
// Classes: OuterClassifierContext
// Original Author: Marcus Andersson andersson@users.sourceforge.net

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
        Object mInterface = ModelFacade.lookupIn(mClassifier, name);

	if (mInterface == null) {
	    // Try to find it via the classpath
	    try {
		Class classifier;

		// Special case for model
		if (ModelFacade.isAModel(mPackage)) {
		    classifier = Class.forName(namePrefix + name);
		}
		else {
		    classifier =
			Class.forName(packageJavaName + "." +
				      namePrefix + name);
		}
		if (classifier.isInterface()) {
		    mInterface =
			UmlFactory.getFactory().getCore()
			.buildInterface(name, mClassifier);
		}
		else {
		    // Only interfaces will do
		    throw new ClassNotFoundException();
		}
	    }
	    catch (ClassNotFoundException e) {
		// Continue the search through the rest of the model
		if (context != null) {
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
	Object iClassifier = ModelFacade.lookupIn(mClassifier, name);

	if (iClassifier == null) {
	    // Try to find it via the classpath
	    try {
		Class classifier;

		// Special case for model
		if (ModelFacade.isAModel(mPackage)) {
		    classifier = Class.forName(namePrefix + name);
		}
		else {
		    classifier =
			Class.forName(packageJavaName + "." +
				      namePrefix + name);
		}
		if (classifier.isInterface()) {
		    iClassifier =
			UmlFactory.getFactory().getCore()
			.buildInterface(name, mClassifier);
		}
		else {
		    iClassifier =
			UmlFactory.getFactory().getCore().buildClass(name, mClassifier);
		}
	    }
	    catch (ClassNotFoundException e) {
		// Continue the search through the rest of the model
		if (context != null) {
		    iClassifier = context.get(name);
		}
	    }
	}
	return iClassifier;
    }
}

