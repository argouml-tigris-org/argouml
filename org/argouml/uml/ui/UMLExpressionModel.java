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
import java.lang.reflect.*;

import org.apache.log4j.Logger;
import org.argouml.model.ModelFacade;
import ru.novosoft.uml.MElementEvent;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by UMLExpressionModel2,
 *             this class is part of the 'old'(pre 0.13.*) 
 *             implementation of proppanels
 *             that used reflection a lot.
 */
public final class UMLExpressionModel  {
    private static final Logger LOG =
            Logger.getLogger(UMLExpressionModel.class);

    private UMLUserInterfaceContainer theContainer;
    private Method theGetMethod;
    private Method theSetMethod;
    private String thePropertyName;
    private Object/*MExpression*/ expression;
    private boolean mustRefresh;
    private Constructor constructor;
    private static final Object[] NOARGS = {};
    private static final Class[] NOCLASSES = {};
    private static final String EMPTYSTRING = "";

    /**
     * The constructor.
     * 
     * @param container the container of UML user interface components
     * @param targetClass the targhet class
     * @param propertyName the name of the property
     * @param expressionClass  the expression
     * @param getMethodName the name of the get method
     * @param setMethodName the name of the set method
     */
    public UMLExpressionModel(UMLUserInterfaceContainer container, 
            Class targetClass, String propertyName, 
            Class expressionClass, String getMethodName, String setMethodName) {

        theContainer = container;
        thePropertyName = propertyName;
        mustRefresh = true;
        try {
            theGetMethod = targetClass.getMethod(getMethodName, NOCLASSES);
        }
        catch (Exception e) {
            LOG.error(e.toString() + " in UMLExpressionModel() for " 
                    + getMethodName, e);
        }
        try {
            theSetMethod = targetClass.getMethod(setMethodName, new Class[] {
		expressionClass
	    });
        }
        catch (Exception e) {
            LOG.error(e.toString() + " in UMLExpressionModel() for " 
                    + setMethodName, e);
        }
        try {
            constructor = expressionClass.getConstructor(new Class[] {
		String.class,
		String.class
	    });
        }
        catch (Exception e) {
            LOG.error(e.toString() + " in UMLExpressionModel() for " 
                    + expressionClass.getName(), e);
        }
    }

    /**
     * When the target changes, we must refresh.
     */
    public void targetChanged() {
        mustRefresh = true;
    }

    /**
     * @param event the event
     * @return true if the property is affected
     */
    public boolean propertySet(MElementEvent event) {
        boolean isAffected = false;
        String eventName = event.getName();
        if (eventName != null && eventName.equals(thePropertyName)) {
            isAffected = true;
            mustRefresh = true;
        }
        return isAffected;
    }

    /**
     * @return the expression object
     */
    public Object/*MExpression*/ getExpression() {
        if (mustRefresh) {
            expression = null;
            Object target = theContainer.getTarget();
            if (theGetMethod != null && target != null) {
                try {
                    expression = theGetMethod.invoke(target, NOARGS);
                }
                catch (Exception e) {
                    LOG.error(e.toString() 
                        + " in UMLExpressionModel.getExpression()", e);
                }
            }
        }
        mustRefresh = false;

        return expression;
    }

    /**
     * @return the lannguage of the expression
     */
    public String getLanguage() {
        if (mustRefresh) {
            getExpression();
        }
        if (expression == null) {
            return EMPTYSTRING;
        }
        return ModelFacade.getLanguage(expression);
    }

    /**
     * @return the body of the expression
     */
    public Object getBody() {
        if (mustRefresh) {
            getExpression();
        }
        if (expression == null) {
            return EMPTYSTRING;
        }
        return ModelFacade.getBody(expression);
    }

    /**
     * @param lang the language of the expression
     */
    public void setLanguage(String lang) {
        boolean mustChange = true;
        if (expression != null) {
            String oldValue = ModelFacade.getLanguage(expression);
            if (oldValue != null && oldValue.equals(lang)) {
                mustChange = false;
            }
        }
        if (mustChange) {
            Object body = null;
            if (expression != null) {
                body = ModelFacade.getBody(expression);
            }
            if (body == null) body = EMPTYSTRING;

            setExpression(lang, body);
        }
    }

    /**
     * @param body the body of the expression
     */
    public void setBody(Object body) {
        boolean mustChange = true;
        if (expression != null) {
            Object oldValue = ModelFacade.getBody(expression);
            if (oldValue != null && oldValue.equals(body)) {
                mustChange = false;
            }
        }
        if (mustChange) {
            String lang = null;
            if (expression != null) {
                lang = ModelFacade.getLanguage(expression);
            }
            if (lang == null) lang = EMPTYSTRING;

            setExpression(lang, body);
        }
    }


    /**
     * @param lang the language of the expression
     * @param body the body of the expression
     */
    private void setExpression(String lang, Object body) {
        try {
            //MExpression newExpression = 
            //(MExpression) _constructor.newInstance(new Object[] { 
            //    lang,body });
            expression = constructor.newInstance(new Object[] {
		lang, body
	    });

            Object target = theContainer.getTarget();
            if (target != null) {
                theSetMethod.invoke(target, new Object[] {
		    expression
		});
            }
        }
        catch (Exception e) {
            LOG.error(e.toString() 
                    + " in UMLExpressionModel.setExpression()", e);
        }
    }

}