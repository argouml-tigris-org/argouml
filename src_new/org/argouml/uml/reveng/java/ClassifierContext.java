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

import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;

/**
   This context is a specific classifier.
*/
class ClassifierContext extends Context
{
    /** The classifier this context represents. */
    private MClassifier mClassifier;

    /**
       Create a new context from a classifier.
       
       @param base Based on this context.
       @param mClassifier Represents this classifier.
    */
    public ClassifierContext(Context base,
			     MClassifier mClassifier)
    {
	super(base);
	this.mClassifier = mClassifier;
    }

    public MInterface getInterface(String name)
	throws ClassifierNotFoundException
    {
	// Check if it is this interface
	if(name.equals(mClassifier.getName()) &&
	   mClassifier instanceof MInterface) {
	    return (MInterface)mClassifier;
	}
	else {
	    // Continue the search through the rest of the model
	    if(context != null) {
		return context.getInterface(name);
	    }
	    else {
		return null;
	    }
	}	    
    }

    /**
     * Get the classifier for a given name
     *
     * @param classifierName The name of the classifier to retrieve.
     * @return A classifier for the name.
     */
    public MClassifier get(String classifierName)
	throws ClassifierNotFoundException
    {
	// Check if it is this classifier
	if(classifierName.equals(mClassifier.getName())) {
	    return mClassifier;
	}
	else {
	    // Continue the search through the rest of the model
	    if(context != null) {
		return context.get(classifierName);
	    }
	    else {
		return null;
	    }
	}	    
    }
}

