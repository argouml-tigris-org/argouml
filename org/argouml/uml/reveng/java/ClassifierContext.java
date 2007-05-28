// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

/*
  JavaRE - Code generation and reverse engineering for UML and Java
  Copyright (C) 2000 Marcus Andersson andersson@users.sourceforge.net
*/

package org.argouml.uml.reveng.java;

import org.argouml.model.Model;

/**
   This context is a specific classifier.
*/
class ClassifierContext extends Context
{
    /** The classifier this context represents. */
    private Object mClassifier;

    /**
       Create a new context from a classifier.

       @param base Based on this context.
       @param classifier Represents this classifier.
    */
    public ClassifierContext(Context base, Object classifier)
    {
	super(base);
	this.mClassifier = classifier;
    }

    public Object getInterface(String name)
	throws ClassifierNotFoundException
    {
        return get(name, true);
    }

    /**
     * Get the classifier for a given name
     *
     * @param classifierName The name of the classifier to retrieve.
     * @return A classifier for the name.
     */
    public Object get(String classifierName) 
    throws ClassifierNotFoundException {
        return get(classifierName, false);
    }
    
    /**
     * Get the classifier for a given name
     * 
     * @param classifierName
     *            The name of the classifier to retrieve.
     * @return A classifier for the name.
     */
    public Object get(String classifierName, boolean interfacesOnly)
	throws ClassifierNotFoundException
    {
	// Check if it is this classifier
	if (classifierName.equals(Model.getFacade().getName(mClassifier))
                && (!interfacesOnly || Model.getFacade().isAInterface(
                        mClassifier))) {
	    return mClassifier;
	}
	else {
	    // Continue the search through the rest of the model
	    if (getContext() != null) {
		return getContext().get(classifierName, interfacesOnly);
	    }
	    else {
		return null;
	    }
	}
    }
}

