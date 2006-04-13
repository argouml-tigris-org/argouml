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

import org.argouml.model.Model;

/**
 * @author mkl
 */
public abstract class UMLExpressionModel2  {
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
        expression = null;
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
        return Model.getDataTypesHelper().getLanguage(expression);
    }

    /**
     * @return The body text of the expression.
     */
    public String getBody() {
        if (mustRefresh) {
            expression = getExpression();
        }
        if (expression == null) {
            return EMPTYSTRING;
        }
        return Model.getDataTypesHelper().getBody(expression);
    }

    /**
     * @param lang the language of the expression
     */
    public void setLanguage(String lang) {

        boolean mustChange = true;
        if (expression != null) {
            String oldValue =
                Model.getDataTypesHelper().getLanguage(expression);
            if (oldValue != null && oldValue.equals(lang)) {
                mustChange = false;
            }
        }
        if (mustChange) {
            String body = EMPTYSTRING;
            if (expression != null
                    && Model.getDataTypesHelper().getBody(expression) != null) {
                body = Model.getDataTypesHelper().getBody(expression);
            }

            setExpression(lang, body);
        }
    }

    /**
     * @param body the body text of the expression
     */
    public void setBody(String body) {
        boolean mustChange = true;
        if (expression != null) {
            Object oldValue = Model.getDataTypesHelper().getBody(expression);
            if (oldValue != null && oldValue.equals(body)) {
                mustChange = false;
            }
        }
        if (mustChange) {
            String lang = null;
            if (expression != null) {
                lang = Model.getDataTypesHelper().getLanguage(expression);
            }
            if (lang == null) {
                lang = EMPTYSTRING;
            }

            setExpression(lang, body);
        }
    }

    /**
     * @param lang the language of the expression
     * @param body the body text of the expression
     */
    private void setExpression(String lang, String body) {
        // Expressions are DataTypes, not independent model elements
        // be careful not to reuse them
        if (mustRefresh || expression == null) {
            expression = newExpression();
        }
        expression = Model.getDataTypesHelper().setLanguage(expression, lang);
        expression = Model.getDataTypesHelper().setBody(expression, body);
        setExpression(expression);
    }

    /**
     * @return the container
     */
    protected UMLUserInterfaceContainer getContainer() {
        return container;
    }

}
