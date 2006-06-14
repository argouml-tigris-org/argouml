// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
   The context is the current available namespaces via import in the
   class that is currently parsed. It is non mutable and a new
   context can be based on the current context with an
   additional namespace.
*/
abstract class Context
{
    /** The succeding context. May be null. */
    private Context context;

    /**
       Create a new context.

       @param base Based on this context, may be null.
    */
    public Context(Context base)
    {
	context = base;
    }

    /**
     * Get a classifier from the model. If it is not in the model, try
     * to find it with the CLASSPATH. If found, in the classpath, the
     * classifier is created and added to the model. If not found at
     * all, a datatype is created and added to the model.
     *
     * @param name The name of the classifier to find.
     * @return Found classifier.
     */
    public abstract Object get(String name)
	throws ClassifierNotFoundException;

    public abstract Object getInterface(String name)
	throws ClassifierNotFoundException;

    /**
       Get the complete java name for a package.

       @param mPackage The package.
       @return Package name in java format
    */
    protected String getJavaName(Object mPackage)
    {
	Object parent = Model.getFacade().getNamespace(mPackage);
	if (Model.getFacade().isAModel(parent)) {
	    return Model.getFacade().getName(mPackage);
	}
	else if (parent != null) {
	    return getJavaName(parent) + "."
	            + Model.getFacade().getName(mPackage);
	}
	else {
	    return "";
	}
    }

    /**
     * @param c The context to set.
     */
    protected void setContext(Context c) {
        this.context = c;
    }

    /**
     * @return Returns the context.
     */
    protected Context getContext() {
        return context;
    }
}

