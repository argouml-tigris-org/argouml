// $Id$

package org.argouml.uml.reveng.java;

/**
   This is thrown when a classifier can not be located in the model or
   via the classpath.
*/
class ClassifierNotFoundException extends Exception
{
    public ClassifierNotFoundException(String name)
    {
	super("classifier not found: " + name);
    }
}
	
