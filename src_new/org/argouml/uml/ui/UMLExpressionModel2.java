// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;

import ru.novosoft.uml.MElementEvent;

/**
 *
 *
 * @author mkl
 *
 */
public abstract class UMLExpressionModel2  {
    private static final Logger LOG =
            Logger.getLogger(UMLExpressionModel2.class);

    private UMLUserInterfaceContainer container;
    private String propertyName;
    private Object/*MExpression*/ expression;
    private boolean mustRefresh;
    private static final String EMPTYSTRING = "";

    /**
     * The constructor.
     *
     * @param c the container of UML user interface components
     * @param name the name of the property
     */
    public UMLExpressionModel2(UMLUserInterfaceContainer c, String name) {
        container = c;
        propertyName = name;
        mustRefresh = true;
    }

    /**
     * When the target is changed, we must refresh.
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
        if (eventName != null && eventName.equals(propertyName)) {
            isAffected = true;
            mustRefresh = true;
        }
        return isAffected;
    }

    /**
     * @return the expression
     */
    public abstract Object getExpression();

    /**
     * @param expr the expression
     */
    public abstract void setExpression(Object expr);

    /**
     * @return a new expression
     */
    public abstract Object newExpression();


    /**
     * @return the language of the expression
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
     * @return the body text of the expression
     */
    public Object getBody() {
        if (mustRefresh) {
            expression = getExpression();
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
     * @param body the body text of the expression
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
     * @param body the body text of the expression
     */
    private void setExpression(String lang, Object body) {
        if (expression == null) {
            expression = newExpression();
        }
        Model.getDataTypesHelper().setLanguage(expression, lang);
        Model.getCoreHelper().setBody(expression, body);
        setExpression(expression);
    }

    /**
     * @return the container
     */
    protected UMLUserInterfaceContainer getContainer() {
        return container;
    }

}
