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

import org.apache.log4j.Category;
import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.data_types.*;

public final class UMLExpressionModel  {
    protected static Category cat = 
            Category.getInstance(UMLExpressionModel.class);

    private UMLUserInterfaceContainer _container;
    private Method _getMethod;
    private Method _setMethod;
    private String _propertyName;
    private MExpression _expression;
    private boolean _mustRefresh;
    private Constructor _constructor;
    private static final Object[] _noArgs = {};
    private static final Class[] _noClasses = {};
    private static final String _emptyStr = "";
    
    public UMLExpressionModel(UMLUserInterfaceContainer container,Class targetClass,String propertyName,Class expressionClass,String getMethodName,String setMethodName) {
        
        _container = container;
        _propertyName = propertyName;
        _mustRefresh = true;
        try {
            _getMethod = targetClass.getMethod(getMethodName,_noClasses);
        }
        catch(Exception e) {
            cat.error(e.toString() + " in UMLExpressionModel() for " + getMethodName, e);
        }
        try {
            _setMethod = targetClass.getMethod(setMethodName,new Class[] { expressionClass });
        }
        catch(Exception e) {
            cat.error(e.toString() + " in UMLExpressionModel() for " + setMethodName, e);
        }
        try {
            _constructor = expressionClass.getConstructor(new Class[] { String.class, String.class });
        }
        catch(Exception e) {
            cat.error(e.toString() + " in UMLExpressionModel() for " + expressionClass.getName(), e);
        }
    }

    public void targetChanged() {
        _mustRefresh = true;
    }
    
    public boolean propertySet(MElementEvent event) {
        boolean isAffected = false;
        String eventName = event.getName();
        if(eventName != null && eventName.equals(_propertyName)) {
            isAffected = true;
            _mustRefresh = true;
        }
        return isAffected;
    }

    public MExpression getExpression() {
        if(_mustRefresh) {
            _expression = null;
            Object target = _container.getTarget();
            if(_getMethod != null && target != null) {
                try {
                    _expression = (MExpression) _getMethod.invoke(target,_noArgs);
                }
                catch(Exception e) {
                    cat.error(e.toString() + " in UMLExpressionModel.getExpression()", e);
                }
            }
        }
        _mustRefresh = false;
            
        return _expression;
    }
    
    public String getLanguage() {
        if(_mustRefresh) {
            getExpression();
        }
        if(_expression == null) {
            return _emptyStr;
        }
        return _expression.getLanguage();
    }
    
    public String getBody() {
        if(_mustRefresh) {
            getExpression();
        }
        if(_expression == null) {
            return _emptyStr;
        }
        return _expression.getBody();
    }
    
    public void setLanguage(String lang) {
        boolean mustChange = true;
        if(_expression != null) {
            String oldValue = _expression.getLanguage();
            if(oldValue != null && oldValue.equals(lang)) {
                mustChange = false;
            }
        }
        if(mustChange) {
            String body = null;
            if(_expression != null) {
                body = _expression.getBody();
            }
            if(body == null) body = _emptyStr;
            
            setExpression(lang,body);
        }
    }
    
    public void setBody(String body) {
        boolean mustChange = true;
        if(_expression != null) {
            String oldValue = _expression.getBody();
            if(oldValue != null && oldValue.equals(body)) {
                mustChange = false;
            }
        }
        if(mustChange) {
            String lang = null;
            if(_expression != null) {
                lang = _expression.getLanguage();
            }
            if(lang == null) lang = _emptyStr;
            
            setExpression(lang,body);
        }
    }
    
    
    private void setExpression(String lang,String body) {
        try {
            //MExpression newExpression = (MExpression) _constructor.newInstance(new Object[] { lang,body });
            _expression = (MExpression) _constructor.newInstance(new Object[] { lang,body });
            
            Object target = _container.getTarget();
            if(target != null) {
                _setMethod.invoke(target,new Object[] { _expression});
            }
        }
        catch(Exception e) {
            cat.error(e.toString() + " in UMLExpressionModel.setExpression()", e);
        }
    }
        
}
