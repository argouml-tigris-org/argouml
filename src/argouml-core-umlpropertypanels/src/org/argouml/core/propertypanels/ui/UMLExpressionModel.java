/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *    Thomas Neustupny
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

package org.argouml.core.propertypanels.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.argouml.model.Model;

/**
 * The model for Expressions.
 * The target is the UML element to which this Expression is attached.
 *
 * The ChangeEvent/ChangeListener handling is inspired by
 * javax.swing.DefaultBoundedRangeModel.
 * It listens to UML model changes not caused by us,
 * which need to trigger an update of the UI rendering.
 *
 * @author mkl, penyaskito, mvw
 */
abstract class UMLExpressionModel
	implements PropertyChangeListener {

    private static final Logger LOG =
        Logger.getLogger(UMLExpressionModel.class.getName());

    private Object target;
    private String propertyName;

    /** This member is only used when we set the expression ourselves.
     * In this case, we do not wish to receive UML model change events
     * for this self-inflicted change.
     * So, this member is used to detect this situation. */
    private Object rememberExpression;

//    private boolean mustRefresh;
    private static final String EMPTYSTRING = "";

    /** The listeners waiting for model changes. */
    protected EventListenerList listenerList = new EventListenerList();

    /**
     * Only one <code>ChangeEvent</code> is needed per model instance
     * since the event's only (read-only) state is the expression.  The source
     * of events generated here is always "this".
     */
    protected transient ChangeEvent changeEvent = null;

    /**
     * The constructor.
     *
     * @param target the UML element
     * @param name the name of the property
     */
    public UMLExpressionModel(Object target, String name) {
        this.target = target;
        propertyName = name;
//        mustRefresh = true;
        startListeningForModelChanges();
    }

    protected void startListeningForModelChanges() {
	if (Model.getFacade().isAUMLElement(target)) {
	    Model.getPump().addModelEventListener(this, target,
		    propertyName);
	}
        LOG.log(Level.FINE, ">>Start listening for UML changes...");
    }

    protected void stopListeningForModelChanges() {
	if (Model.getFacade().isAUMLElement(target)) {
	    Model.getPump().removeModelEventListener(this, target,
	                propertyName);
	}
        LOG.log(Level.FINE, ">>Stop listening for UML changes...");
    }

    public void propertyChange(PropertyChangeEvent e) {
	if (propertyName.equals(e.getPropertyName())) {
	    if (rememberExpression != e.getNewValue()) {
		fireStateChanged();
                LOG.log(Level.FINE, ">>UML expression changed.");
	    } else {
		/* This should not happen. */
                LOG.log(Level.FINE,
                        ">>Got an event for a modelchange that we "
                        + "inflicted ourselves...");
	    }
	}
    }

    protected Object getTarget() {
        return target;
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
     * @return a new expression with given language and body
     */
    public abstract Object newExpression(String language, String body);

    /**
     * @return the language of the expression
     */
    public String getLanguage() {
	Object expression = getExpression();
        if (expression == null) {
            return EMPTYSTRING;
        }
        String language = null;
        try {
            language = Model.getDataTypesHelper().getLanguage(expression);
        } catch (Exception ex) {
            // if it has no language, then we assume it is not an instance of
            // OpaqueExpression, so we leave it null and handle elsewhere
        }
        return language;
    }

    /**
     * @return The body text of the expression.
     */
    public String getBody() {
        Object expression = getExpression();
        if (expression == null) {
            return EMPTYSTRING;
        }
        return Model.getFacade().getBody(expression).toString();
    }

    /**
     * @param lang the language of the expression
     */
    public void setLanguage(String lang) {

	Object expression = getExpression();
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

	Object expression = getExpression();
        boolean mustChange = true;
        if (expression != null) {
            Object oldValue = Model.getFacade().getBody(expression).toString();
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
     * This is only called if we already know that the values differ.
     *
     * @param lang the language of the expression
     * @param body the body text of the expression
     */
    private void setExpression(String lang, String body) {
	assert lang != null;
	assert body != null;

        // Expressions are DataTypes, not independent model elements
        // be careful not to reuse them
	rememberExpression = getExpression();
	stopListeningForModelChanges();
	if (rememberExpression != null) {
	    Model.getUmlFactory().delete(rememberExpression);
	}
	if (lang.length() == 0 && body.length()==0) {
	    rememberExpression = null;
	} else {
	    rememberExpression = newExpression(lang, body);
	}
	setExpression(rememberExpression);
	startListeningForModelChanges();
    }

    /**
     * Adds a <code>ChangeListener</code>.
     * The change listeners are run each
     * time the expression changes.
     *
     * @param l the ChangeListener to add
     * @see #removeChangeListener
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
        LOG.log(Level.FINE, ">>Add listener");
    }

    /**
     * Removes a <code>ChangeListener</code>.
     *
     * @param l the <code>ChangeListener</code> to remove
     * @see #addChangeListener
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
        LOG.log(Level.FINE, ">>Remove listener");
    }

    /**
     * Runs each <code>ChangeListener</code>'s
     * <code>stateChanged</code> method.
     *
     * @see #setRangeProperties
     * @see EventListenerList
     */
    protected void fireStateChanged() {
        LOG.log(Level.FINE, ">>Fire state changed to listeners.");
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -=2 ) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
        }
    }
}
