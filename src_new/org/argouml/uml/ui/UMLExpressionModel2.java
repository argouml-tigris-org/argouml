// $Id$
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
import org.apache.log4j.Logger;
import org.argouml.model.ModelFacade;

import ru.novosoft.uml.MElementEvent;

/**
 * 
 * 
 * @author mkl
 *
 */
public abstract class UMLExpressionModel2  {
    protected static Logger LOG =
            Logger.getLogger(UMLExpressionModel2.class);

    protected UMLUserInterfaceContainer _container;
    private String _propertyName;
    private Object/*MExpression*/ _expression;
    private boolean _mustRefresh;
    private static final String _emptyStr = "";

    public UMLExpressionModel2(UMLUserInterfaceContainer container, String propertyName) {
        _container = container;
        _propertyName = propertyName;
        _mustRefresh = true;   
    }

    public void targetChanged() {
        _mustRefresh = true;
    }

    public boolean propertySet(MElementEvent event) {
        boolean isAffected = false;
        String eventName = event.getName();
        if (eventName != null && eventName.equals(_propertyName)) {
            isAffected = true;
            _mustRefresh = true;
        }
        return isAffected;
    }

    public abstract Object  getExpression();
    
    public abstract void setExpression(Object expression);
    
    public abstract Object newExpression();
    

    public String getLanguage() {
        if (_mustRefresh) {
            getExpression();
        }
        if (_expression == null) {
            return _emptyStr;
        }
        return ModelFacade.getLanguage(_expression);
    }

    public Object getBody() {
        if (_mustRefresh) {
            _expression=getExpression();
        }
        if (_expression == null) {
            return _emptyStr;
        }
        return ModelFacade.getBody(_expression);
    }

    public void setLanguage(String lang) {
      
        boolean mustChange = true;
        if (_expression != null) {
            String oldValue = ModelFacade.getLanguage(_expression);
            if (oldValue != null && oldValue.equals(lang)) {
                mustChange = false;
            }
        }
        if (mustChange) {
            Object body = null;
            if (_expression != null) {
                body = ModelFacade.getBody(_expression);
            }
            if (body == null) body = _emptyStr;

            setExpression(lang, body);
        }
    }

    public void setBody(Object body) {
        boolean mustChange = true;
        if (_expression != null) {
            Object oldValue = ModelFacade.getBody(_expression);
            if (oldValue != null && oldValue.equals(body)) {
                mustChange = false;
            }
        }
        if (mustChange) {
            String lang = null;
            if (_expression != null) {
                lang = ModelFacade.getLanguage(_expression);
            }
            if (lang == null) lang = _emptyStr;

            setExpression(lang, body);
        }
    }

    private void setExpression(String lang, Object body) {
        if (_expression == null) _expression = newExpression();
        ModelFacade.setLanguage(_expression, lang);
        ModelFacade.setBody(_expression, body);
        setExpression(_expression);
    }

}