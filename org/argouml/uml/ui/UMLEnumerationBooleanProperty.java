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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.lang.reflect.*;

import org.apache.log4j.Category;
import org.argouml.model.uml.AbstractWellformednessRule;
import org.argouml.model.uml.UmlHelper;

import ru.novosoft.uml.*;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by ?,
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLEnumerationBooleanProperty extends UMLBooleanProperty {
    private Method _getMethod;
    private Method _setMethod;
    static final private Object[] _noArg = {};
    private Object[] _trueArg = new Object[1];
    private Object[] _falseArg = new Object[1];
    private Class _enumClass;
    
     /** Log4j logging category.
     */
    public static Category logger =
                  Category.getInstance("org.argouml.uml.ui.UMLEnumerationBooleanProperty");
                  
    /**
     * wellformednessRules are rules that should be hold true if the property is set
     * they are defined in the model helpers (CoreHelper etc) and conform to the
     * wellformednessRules defined in the UML 1.3 spec
     * They are of the form:
     * wellformednessRule(modelelement, newvalue)
     */
    private AbstractWellformednessRule[] _wellformednessRules = null;
    
    /** Creates new BooleanChangeListener */
    public UMLEnumerationBooleanProperty(String propertyName,Class elementClass,
                                         String getMethod,String setMethod,Class enumClass,
                                         Object trueValue,Object falseValue) {
        this(propertyName, elementClass, getMethod, setMethod, enumClass, trueValue, falseValue, null);
    }
    
     public UMLEnumerationBooleanProperty(String propertyName,Class elementClass,
                                         String getMethod,String setMethod,Class enumClass,
                                         Object trueValue,Object falseValue, AbstractWellformednessRule[] wellformednessRules) {
	        super(propertyName);
	        _enumClass = enumClass;
	        _trueArg[0] = trueValue;
	        _falseArg[0] = falseValue;
	        Class[] noClass = {};
	        _wellformednessRules = wellformednessRules;
	        try {
	            _getMethod = elementClass.getMethod(getMethod,noClass);
	        }
	        catch(Exception e) {
	            logger.fatal(getMethod + " not found in UMLEnumerationBooleanProperty(): " + e.toString());
	        }
	        Class[] boolClass = { enumClass };
	        try {
	            _setMethod = elementClass.getMethod(setMethod,boolClass);
	        }
	        catch(Exception e) {
	            logger.fatal(setMethod + " not found in UMLEnumerationBooleanProperty(): " + e.toString());
	        }
    }  
    
    
    public void setProperty(Object element,boolean newState) throws PropertyVetoException {
    
    	if (_wellformednessRules != null && element instanceof MBase) {
    		Object helper = UmlHelper.getHelper().getHelper(element);  
    		if (helper != null) {
	    		for (int i = 0; i < _wellformednessRules.length; i++) {
	    			Object arg = _falseArg[0];
	    			if (newState) {
	    				arg = _trueArg[0];
	    			}
	    			if (arg != null) {
	    				if (!_wellformednessRules[i].isWellformed((MBase)element, arg)) {
		    				throw new PropertyVetoException( _wellformednessRules[i].getUserMessage(),
		    					new PropertyChangeEvent(element, getPropertyName(), new Boolean(!newState), new Boolean(newState)));
		    					
	    				}
	    			}
	    		}
    		}
    	}
    	
    	
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
                logger.fatal("Error in UMLEnumerationBooleanProperty.setProperty for " + getPropertyName() + ": " + e.toString());
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
                logger.fatal("Error in UMLEnumerationBooleanProperty.getProperty for " + getPropertyName() + ": " + e.toString());
            }
        }
        return state;
    }
    
}


