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

/**
   The context is the current available namespaces via import in the
   class that is currently parsed. It is non mutable and a new
   context can be based on the current context with an
   additional namespace.  
*/
abstract class Context
{
    /** The succeding context. May be null. */
    protected Context context;

    /**
       Create a new context.
       
       @param base Based on this context, may be null.
    */
    public Context(Context base)
    {
	context = base;
    }

    /**
       Get a classifier from the model. If it is not in the model, try
       to find it with the CLASSPATH. If found, in the classpath, the
       classifier is created and added to the model. If not found at
       all, a datatype is created and added to the model.

       @param className The name of the classifier to find.
       @return Found classifier.
    */
    abstract public MClassifier get(String name)
	throws ClassifierNotFoundException;

    abstract public MInterface getInterface(String name)
	throws ClassifierNotFoundException;

    /**
       Get the complete java name for a package.
       
       @param mPackage The package.
       @return Package name in java format
    */
    protected String getJavaName(MPackage mPackage)
    {
	MNamespace parent = mPackage.getNamespace();
	if(parent instanceof MModel) {
	    return mPackage.getName();
	}
	else if(parent != null) {
	    return getJavaName((MPackage)parent) + "." + mPackage.getName();
	}
	else {
	    return "";
	}
    }
}
	
