// Copyright (c) 1996-99 The Regents of the University of California. All
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

import java.lang.reflect.*;

import ru.novosoft.uml.*;

import org.argouml.ui.*;
import org.apache.log4j.Category;
import org.argouml.kernel.*;


public class UMLReflectionBooleanProperty extends UMLBooleanProperty {
    protected static Category cat = Category.getInstance(UMLReflectionBooleanProperty.class);

    private Method _getMethod;
    private Method _setMethod;
    static final private Object[] _noArg = {};
    static final private Object[] _trueArg = { new Boolean(true) };
    static final private Object[] _falseArg = { new Boolean(false) };
    
    /** Creates new BooleanChangeListener */
    public UMLReflectionBooleanProperty(String propertyName,Class elementClass,String getMethod,String setMethod) {
        super(propertyName);

        Class[] noClass = {};
        try {
            _getMethod = elementClass.getMethod(getMethod,noClass);
        }
        catch(Exception e) {
            cat.error(e.toString() + " in UMLReflectionBooleanProperty(): " + getMethod, e);
        }
        Class[] boolClass = { boolean.class };
        try {
            _setMethod = elementClass.getMethod(setMethod,boolClass);
        }
        catch(Exception e) {
            cat.error(e.toString() + " in UMLReflectionBooleanProperty(): "  + setMethod, e);
        }
    }
    
    
    public void setProperty(Object element,boolean newState) {
        if(_setMethod != null && element != null) {
            try {
                //
                //   this allows enumerations to work properly
                //      if newState is false, it won't override
                //      a different enumeration value
                boolean oldState = getProperty(element);
                if(newState != oldState) {
                    if(newState) {
                        _setMethod.invoke(element,_trueArg);
                    }
                    else {
                        _setMethod.invoke(element,_falseArg);
                    }
                }
            }
            catch(Exception e) {
                cat.error(e.toString() + " in UMLReflectionBooleanProperty.setMethod()", e);
            }

            // Having set a property, mark as needing saving

            Project p = ProjectManager.getManager().getCurrentProject();
            p.setNeedsSave(true);
        }
    }
    
    public boolean getProperty(Object element) {
        boolean state = false;
        if(_getMethod != null && element != null) {
            try {
                Object retval = _getMethod.invoke(element,_noArg);
                if(retval != null && retval instanceof Boolean) {
                    state = ((Boolean) retval).booleanValue();
                }
            }
            catch(Exception e) {
                cat.error(e.toString() + " in UMLReflectionBooleanProperty.getMethod()", e);
            }
        }
        return state;
    }
    
}


