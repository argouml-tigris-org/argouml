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

package org.argouml.uml.ui;
import java.lang.reflect.*;
import ru.novosoft.uml.*;

public class UMLEnumerationBooleanProperty extends UMLBooleanProperty {
    private Method _getMethod;
    private Method _setMethod;
    static final private Object[] _noArg = {};
    private Object[] _trueArg = new Object[1];
    private Object[] _falseArg = new Object[1];
    private Class _enumClass;
    
    /** Creates new BooleanChangeListener */
    public UMLEnumerationBooleanProperty(String propertyName,Class elementClass,
                                         String getMethod,String setMethod,Class enumClass,
                                         Object trueValue,Object falseValue) {
        super(propertyName);

        _enumClass = enumClass;
        _trueArg[0] = trueValue;
        _falseArg[0] = falseValue;
        
        Class[] noClass = {};
        try {
            _getMethod = elementClass.getMethod(getMethod,noClass);
        }
        catch(Exception e) {
            System.out.println(getMethod + " not found in UMLEnumerationBooleanProperty(): " + e.toString());
        }
        Class[] boolClass = { enumClass };
        try {
            _setMethod = elementClass.getMethod(setMethod,boolClass);
        }
        catch(Exception e) {
            System.out.println(setMethod + " not found in UMLEnumerationBooleanProperty(): " + e.toString());
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
                System.out.println("Error in UMLEnumerationBooleanProperty.setProperty for " + getPropertyName() + ": " + e.toString());
            }
        }
    }
    
    public boolean getProperty(Object element) {
        boolean state = false;
        if(_getMethod != null && element != null) {
            try {
                Object retval = _getMethod.invoke(element,_noArg);
                if(retval != null && 
                    (retval == _trueArg[0] || retval.equals(_trueArg[0]))) {
                    state = true;
                }
            }
            catch(Exception e) {
                System.out.println("Error in UMLEnumerationBooleanProperty.getProperty for " + getPropertyName() + ": " + e.toString());
            }
        }
        return state;
    }
    
}


