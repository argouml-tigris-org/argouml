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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 * TODO: What is this replaced by?,
 * this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 * that used reflection a lot.
 */
public class UMLEnumerationBooleanProperty extends UMLBooleanProperty {
    private static final Logger LOG =
	Logger.getLogger(UMLEnumerationBooleanProperty.class);

    private Method theGetMethod;
    private Method theSetMethod;
    private static final Object[] NOARG = {};
    private Object[] trueArg = new Object[1];
    private Object[] falseArg = new Object[1];

    /**
     * Creates new BooleanChangeListener.
     *
     * @param propertyName the name of the property
     * @param elementClass the element
     * @param getMethod the get method
     * @param setMethod the set method
     * @param enumClass the enumeration
     * @param trueValue the value for true
     * @param falseValue the value for false
     */
    public UMLEnumerationBooleanProperty(String propertyName,
                                         Class elementClass,
                                         String getMethod,
                                         String setMethod,
                                         Object/*Class*/ enumClass,
                                         Object trueValue,
                                         Object falseValue) {
	super(propertyName);
	trueArg[0] = trueValue;
	falseArg[0] = falseValue;
	Class[] noClass = {};
	try {
	    theGetMethod = elementClass.getMethod(getMethod, noClass);
	} catch (NoSuchMethodException e) {
	    LOG.fatal(getMethod
			 + " not found in UMLEnumerationBooleanProperty(): ",
			 e);
	} catch (SecurityException e) {
	    LOG.fatal(getMethod
			 + " not found in UMLEnumerationBooleanProperty(): ",
			 e);
	}

	Class[] boolClass = {
	    (Class) enumClass,
	};

	try {
	    theSetMethod = elementClass.getMethod(setMethod, boolClass);
	} catch (NoSuchMethodException e) {
	    LOG.fatal(setMethod
	            + " not found in UMLEnumerationBooleanProperty(): ",
	            e);
	} catch (SecurityException e) {
	    LOG.fatal(setMethod
	            + " not found in UMLEnumerationBooleanProperty(): ",
	            e);
	}
    }


    /**
     * @see org.argouml.uml.ui.UMLBooleanProperty#setProperty(
     * java.lang.Object, boolean)
     */
    public void setProperty(Object element, boolean newState) {
        if (theSetMethod != null && element != null) {
            try {
                //
                //   this allows enumerations to work properly
                //      if newState is false, it won't override
                //      a different enumeration value
                boolean oldState = getProperty(element);
                if (newState != oldState) {
                    if (newState) {
                        theSetMethod.invoke(element, trueArg);
                    } else {
                        theSetMethod.invoke(element, falseArg);
                    }
                }
            } catch (IllegalArgumentException e) {
                LOG.fatal("Error in "
			     + "UMLEnumerationBooleanProperty.setProperty for "
			     + getPropertyName() + ": " + e.toString(),
			     e);
            } catch (IllegalAccessException e) {
                LOG.fatal("Error in "
			     + "UMLEnumerationBooleanProperty.setProperty for "
			     + getPropertyName() + ": " + e.toString(),
			     e);
            } catch (InvocationTargetException e) {
                LOG.fatal("Error in "
			     + "UMLEnumerationBooleanProperty.setProperty for "
			     + getPropertyName() + ": " + e.toString(),
			     e);
            }
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLBooleanProperty#getProperty(java.lang.Object)
     */
    public boolean getProperty(Object element) {
        boolean state = false;
        if (theGetMethod != null && element != null) {
            try {
                Object retval = theGetMethod.invoke(element, NOARG);
                if (retval != null
		    && (retval == trueArg[0] || retval.equals(trueArg[0]))) {
                    state = true;
                }
            } catch (IllegalArgumentException e) {
                LOG.fatal("Error in "
			     + "UMLEnumerationBooleanProperty.getProperty for "
			     + getPropertyName() + ": " + e.toString(),
			     e);
            } catch (IllegalAccessException e) {
                LOG.fatal("Error in "
			     + "UMLEnumerationBooleanProperty.getProperty for "
			     + getPropertyName() + ": " + e.toString(),
			     e);
            } catch (InvocationTargetException e) {
                LOG.fatal("Error in "
			     + "UMLEnumerationBooleanProperty.getProperty for "
			     + getPropertyName() + ": " + e.toString(),
			     e);
            }
        }
        return state;
    }
}
