// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import org.apache.log4j.Logger;
import org.argouml.kernel.*;
import java.lang.reflect.*;
import ru.novosoft.uml.MElementEvent;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 * TODO: What is this replaced by?,
 * this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 * that used reflection a lot.
 */
public class UMLTextProperty  {

    /** logger */
    private static final Logger LOG = Logger.getLogger(UMLTextProperty.class);
           
    private Method theGetMethod;
    private Method theSetMethod;
    private String thePropertyName;
    private static Object[] noArg = {};    

    /**
     * The constructor.
     * 
     * @param propertyName the name of the property
     */
    public UMLTextProperty(String propertyName) {
        thePropertyName = propertyName;
    }
    
    /**
     * The constructor.
     * 
     * @param elementClass the element
     * @param propertyName the name of the property
     * @param getMethod the get method
     * @param setMethod the set method
     */
    public UMLTextProperty(Class elementClass, String propertyName,
			   String getMethod, String setMethod) {
        
        thePropertyName = propertyName;
        Class[] noClass = {};
        try {
            theGetMethod = elementClass.getMethod(getMethod, noClass);
        }
        catch (Exception e) {
            LOG.error(e.toString() + " in UMLTextProperty: " + getMethod, e);
            // 2002-07-20
            // Jaap Branderhorst
            // If it is illegal we should throw an exception
            throw new IllegalArgumentException("The method "
					       + getMethod
					       + " is not a legal method to "
					       + "get the property "
					       + propertyName);
        }
        Class[] stringClass = {
	    String.class
	};
        try {
            theSetMethod = elementClass.getMethod(setMethod, stringClass);
        }
        catch (Exception e) {
            LOG.error(e.toString() + " in UMLTextProperty: " + setMethod, e);
            // 2002-07-20
            // Jaap Branderhorst
            // If it is illegal we should throw an exception
            throw new IllegalArgumentException("The method "
					       + setMethod
					       + " is not a legal method "
					       + "to set the property "
					       + propertyName);
        }
    }
    
    /**
     * @param container the container of UML user interface components
     * @param newValue the new value 
     * @throws Exception @see setProperty(UMLUserInterfaceContainer container,
     *          String newValue, boolean vetoableCheck)
     */
    public void setProperty(UMLUserInterfaceContainer container,
			    String newValue)
	throws Exception {

    	setProperty(container, newValue, false);

    }

    /**
     * @param container the container of UML user interface components
     * @param newValue the new value
     * @param vetoableCheck
     * @throws Exception
     */
    public void setProperty(UMLUserInterfaceContainer container,
			    String newValue, boolean vetoableCheck)
	throws Exception {
	
        if (theSetMethod != null) {
            Object element = container.getTarget();
            if (element != null) {					
		String oldValue = getProperty(container);
		//
		//  if one or the other is null or they are not equal
		if (newValue == null
		    || oldValue == null
		    || !newValue.equals(oldValue)) {

		    //
		    //  as long as they aren't both null 
		    //   (or a really rare identical string pointer)
		    if (newValue != oldValue) {
			Object[] args = {
			    newValue 
			};
			try {
                            	
                            		
			    theSetMethod.invoke(element, args);
			    // Mark the project as having been changed 
			    Project p =
				ProjectManager.getManager().getCurrentProject();
			    if (p != null) p.setNeedsSave(true); 
			}
			catch (InvocationTargetException inv) {
			    Throwable targetException =
				inv.getTargetException();
			    LOG.error(inv);
			    LOG.error(targetException);
			    if (targetException instanceof Exception) {
				throw (Exception) targetException;
			    }
			    System.exit(-1); // we have a real error
			}
		    }
		}  
            }
        }
    }

    
    /**
     * @param container the container of UML user interface components
     * @return the property value
     */
    public String getProperty(UMLUserInterfaceContainer container) {
        String value = null;
        if (theGetMethod != null) {
            Object element = container.getTarget();
            if (element != null) {
                try {
                    Object obj =  theGetMethod.invoke(element, noArg);
                    if (obj != null) value = obj.toString();
                }
                catch (InvocationTargetException e) {
                    LOG.error(e.getTargetException().toString()
			      + " is invocationtargetexception "
			      + "in UMLTextProperty.getMethod()", 
			      e.getTargetException());
                }
                catch (Exception e) {
                    LOG.error(e.toString() + " in UMLTextProperty.getMethod()",
			      e);
                }
            }
        }
        return value;
    }
    
    boolean isAffected(MElementEvent event) {
        String sourceName = event.getName();
        if (thePropertyName == null
	    || sourceName == null
	    || sourceName.equals(thePropertyName)) {

            return true;

	}
        return false;
    }
    
    void targetChanged() {
    }
}
