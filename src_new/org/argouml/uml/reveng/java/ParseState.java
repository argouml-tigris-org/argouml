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

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;
import java.util.*;

import org.argouml.model.uml.UmlFactory;

/**
   The parse state keep control of data during parsing.
*/
class ParseState
{
    /** When the classifier parse is finished, these features will be
	removed from the model. */
    private Vector obsoleteFeatures;

    /** When the classifier parse is finished, these inner classes
        will be removed from the model. */
    private Vector obsoleteInnerClasses;

    /** This prefix is appended to inner classes, if any. */
    private String classnamePrefix;

    /** The available context for currentClassifier. */
    private Context context;
    
    /** The classifier that is parsed for the moment. */
    private MClassifier classifier;

    /** Counter for anonymous innner classes */
    private int anonymousClassCounter;
    
    /**
       Create a new parse state.

       @param model The model.
       @param javaLangPackage The default package java.lang.
     */
    public ParseState(MModel model,
                      MPackage javaLangPackage)
    {
	obsoleteInnerClasses = new Vector();
	classifier = null;
	context = 
	    new PackageContext(new PackageContext(null, model), 
				   javaLangPackage);
	anonymousClassCounter = 0;
    }

    /**
       Create a new parse state based on another parse state.

       @param previousState The base parse state.
       @param mClassifier The new classifier being parsed.
       @param currentPackage The current package being parsed.
     */
    public ParseState(ParseState previousState,
                      MClassifier mClassifier,
                      MPackage currentPackage)
    {
	classnamePrefix =
	    previousState.classnamePrefix + mClassifier.getName() + "$";
	obsoleteFeatures = new Vector(mClassifier.getFeatures());
	obsoleteInnerClasses = new Vector(mClassifier.getOwnedElements());
	context = new OuterClassifierContext(previousState.context,
						     mClassifier,
						     currentPackage,
						     classnamePrefix);
	classifier = mClassifier;
	anonymousClassCounter = previousState.anonymousClassCounter;
    }
   
    /**
       Add a package to the current context.

       @param mPackage The package to add.
     */
    public void addPackageContext(MPackage mPackage)
    {
	context = new PackageContext(context, mPackage);
    }
    
    /**
       Add a classifier to the current context.

       @param mClassifier The classifier to add.
     */
    public void addClassifierContext(MClassifier mClassifier)
    {
	context = new ClassifierContext(context, mClassifier);
    }
    
    /**
       Get the current context.

       @return The current context.
     */
    public Context getContext()
    {
	return context;
    }
    
    /**
       Get the current classifier.

       @return The current classifier.
     */
    public MClassifier getClassifier()
    {
	return classifier;
    }

    /**
       Tell the parse state that an anonymous class is being parsed.

       @return The name of the anonymous class.
     */
    public String anonymousClass()
    {
	classnamePrefix = 
	    classnamePrefix.substring(0, classnamePrefix.indexOf("$") + 1);
	anonymousClassCounter++;
	return (new Integer(anonymousClassCounter)).toString();
    }

    /**
       Tell the parse state that an outer class is being parsed.
     */
    public void outerClassifier()
    {
	classnamePrefix = "";
	anonymousClassCounter = 0;
    }

    /**
       Get the current classname prefix.

       @return The current classname prefix.
     */
    public String getClassnamePrefix()
    {
	return classnamePrefix;
    }
 
    /**
       Tell the parse state that a classifier is an inner classifier
       to the current parsed classifier.
       
       @param mClassifier The inner classifier.
    */
    public void innerClassifier(MClassifier mClassifier)
    {
	obsoleteInnerClasses.remove(mClassifier);
    }

    /**
       Remove features no longer in the source from the current
       classifier in the model.
    */
    public void removeObsoleteFeatures()
    {
	for(Iterator i = obsoleteFeatures.iterator(); i.hasNext(); ) {
	    classifier.removeFeature((MFeature)i.next());
	}
    }

    /**
       Remove inner classes no longer in the source from the current
       classifier in the model.
    */
    public void removeObsoleteInnerClasses()
    {
	for(Iterator i = obsoleteInnerClasses.iterator(); i.hasNext(); ) {
	    MModelElement element = (MModelElement)i.next();
	    if(element instanceof MClassifier) {
                UmlFactory.getFactory().delete(element);
	    }
	}	
    }

    /**
       Tell the parse state that a feature belongs to the current
       classifier.

       @param feature The feature.
    */
    public void feature(MFeature feature)
    {
	obsoleteFeatures.remove(feature);
    }

    /**
       Get a feature from the current classifier not yet modeled.

       @param name The name of the feature.
       @return The found feature, null if not found.
     */
    public MFeature getFeature(String name)
    {
	for(Iterator i = obsoleteFeatures.iterator(); i.hasNext(); ) {
	    MFeature mFeature = (MFeature)i.next();
	    if(name.equals(mFeature.getName())) {
		return mFeature;
	    }
	}
	return null;
    }

    /**
       Get a method from the current classifier not yet modeled.

       @param name The name of the method.
       @return The found method, null if not found.
     */
    public MMethod getMethod(String name)
    {
	for(Iterator i = obsoleteFeatures.iterator(); i.hasNext(); ) {
	    MFeature mFeature = (MFeature)i.next();
	    if(mFeature instanceof MMethod &&
	       name.equals(mFeature.getName())) {
		return (MMethod)mFeature;
	    }
	}
	return null;
    }

    /**
       Get a operation from the current classifier not yet modeled.

       @param name The name of the operation.
       @return The found operation, null if not found.
     */
    public MOperation getOperation(String name)
    {
	for(Iterator i = obsoleteFeatures.iterator(); i.hasNext(); ) {
	    MFeature mFeature = (MFeature)i.next();
	    if(mFeature instanceof MOperation &&
	       name.equals(mFeature.getName())) {
		return (MOperation)mFeature;
	    }
	}
	return null;
    }
}
	
