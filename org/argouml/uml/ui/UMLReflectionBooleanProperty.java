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

// 3 May 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to mark the
// project as needing saving if a property is set.


package org.argouml.uml.ui;

import java.lang.reflect.Method;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 * TODO: What is this replaced by?,
 * this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 * that used reflection a lot.
 */
public class UMLReflectionBooleanProperty extends UMLBooleanProperty {
    private Method theGetMethod;
    private Method theSetMethod;
    private static final Object[] NOARG = {};
    private static final Object[] TRUEARG = {
	new Boolean(true) 
    };
    private static final Object[] FALSEARG = {
	new Boolean(false) 
    };
    
    /**
     * Creates new BooleanChangeListener.<p>
     *
     * @param propertyName the property name
     * @param elementClass the class
     * @param gm the getter method
     * @param sm the setter method
     */
    public UMLReflectionBooleanProperty(String propertyName,
					Class elementClass,
					String gm, String sm) {
        super(propertyName);

        Class[] noClass = {};
        try {
            theGetMethod = elementClass.getMethod(gm, noClass);
        }
        catch (Exception e) {
            cat.error(e.toString()
		      + " in UMLReflectionBooleanProperty(): "
		      + gm,
		      e);
            cat.error("Going to rethrow as RuntimeException");
	    // need to throw exception again for unit testing!
	    throw new RuntimeException(e.toString());
        }
        Class[] boolClass = {
	    boolean.class 
	};
        try {
            theSetMethod = elementClass.getMethod(sm, boolClass);
        }
        catch (Exception e) {
            cat.error(e.toString()
		      + " in UMLReflectionBooleanProperty(): "
		      + sm,
		      e);
	    cat.error("Going to rethrow as RuntimeException");
	    // need to throw exception again for unit testing!
	    throw new RuntimeException(e.toString());
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
                        theSetMethod.invoke(element, TRUEARG);
                    }
                    else {
                        theSetMethod.invoke(element, FALSEARG);
                    }
                    
                    // Having set a property, mark as needing saving

                    Project p = ProjectManager.getManager().getCurrentProject();
                    p.setNeedsSave(true);
                }
            }
            catch (Exception e) {
                cat.error(e.toString()
			  + " in UMLReflectionBooleanProperty.setMethod()",
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
                if (retval != null && retval instanceof Boolean) {
                    state = ((Boolean) retval).booleanValue();
                }
            }
            catch (Exception e) {
                cat.error(e.toString()
			  + " in UMLReflectionBooleanProperty.getMethod()",
			  e);
            }
        }
        return state;
    }
    
}


