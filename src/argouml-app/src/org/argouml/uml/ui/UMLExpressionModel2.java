/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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
import java.beans.PropertyChangeListener;

import javax.swing.SwingUtilities;

import org.argouml.model.Model;
import org.argouml.ui.TabTarget;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.tigris.gef.presentation.Fig;

/**
 * The model for an expression. 
 * An expression consists of a body and a language.
 * 
 * @author mkl
 */
public abstract class UMLExpressionModel2  
    implements TargetListener, PropertyChangeListener {
    private UMLUserInterfaceContainer container;
    private String propertyName;
    private Object expression;
    private boolean mustRefresh;
    private static final String EMPTYSTRING = "";
    
    private Object target = null;

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
            expression = getExpression();
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
        Object oldExpression = null;
        if (mustRefresh || expression == null) {
            oldExpression = expression;
            expression = newExpression();
        }
        expression = Model.getDataTypesHelper().setLanguage(expression, lang);
        expression = Model.getDataTypesHelper().setBody(expression, body);
        setExpression(expression);
        if (oldExpression != null) {
            Model.getUmlFactory().delete(oldExpression);
        }
    }

    /**
     * @return the container
     */
    protected UMLUserInterfaceContainer getContainer() {
        return container;
    }

    /**
     * TODO: The next text was copied - to adapt.
     * 
     * Sets the target. If the old target is an UML Element, it also removes
     * the model from the element listener list of the target. If the new target
     * is an UML Element, the model is added as element listener to the
     * new target. <p>
     *
     * This function is called when the user changes the target. 
     * Hence, this shall not result in any UML model changes.<p>
     * 
     * This function looks a lot like the one in UMLComboBoxModel2.
     * <p>
     * As a possible future extension, we could allow listening to 
     * other model elements.
     * 
     * @param theNewTarget the new target
     */
    public void setTarget(Object theNewTarget) {
        theNewTarget = theNewTarget instanceof Fig
            ? ((Fig) theNewTarget).getOwner() : theNewTarget;
        if (Model.getFacade().isAUMLElement(target)) {
            Model.getPump().removeModelEventListener(this, target,
                    propertyName);
            // Allow listening to other elements:
            //                removeOtherModelEventListeners(listTarget);
        }

        if (Model.getFacade().isAUMLElement(theNewTarget)) {
            target = theNewTarget;
            Model.getPump().addModelEventListener(this, target,
                    propertyName);
            // Allow listening to other elements:
            //                addOtherModelEventListeners(listTarget);

            if (container instanceof TabTarget) {
                ((TabTarget) container).refresh();
            }
        } else {
            target = null;
        }
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        if (target != null && target == e.getSource()) {
            mustRefresh = true;
            expression = null;
            /* This works - we do get an event - and now 
             * refresh the UI: */
            if (container instanceof TabTarget) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ((TabTarget) container).refresh();
                        /* TODO: The above statement also refreshes when 
                         * we are not shown (to be verified) - hence 
                         * not entirely correct. */
                    }
                });
            }
        }
    }

    public void targetAdded(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

    public void targetRemoved(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

}
