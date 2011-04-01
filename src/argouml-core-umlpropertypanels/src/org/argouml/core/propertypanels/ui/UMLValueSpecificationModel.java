/* $Id$
 *******************************************************************************
 * Copyright (c) 2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Thomas Neustupny
 *******************************************************************************
 */

package org.argouml.core.propertypanels.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.argouml.model.Model;

/**
 * The model for value specifications.
 * The target is the UML element to which this ValueSpecification is attached.
 *
 * The ChangeEvent/ChangeListener handling is inspired by
 * javax.swing.DefaultBoundedRangeModel.
 * It listens to UML model changes not caused by us,
 * which need to trigger an update of the UI rendering.
 *
 * @author Thomas Neustupny
 */
 class UMLValueSpecificationModel
	implements PropertyChangeListener {

    private static final Logger LOG =
        Logger.getLogger(UMLValueSpecificationModel.class);

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
    public UMLValueSpecificationModel(Object target, String name) {
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
	LOG.debug(">>Start listening for UML changes...");
    }

    protected void stopListeningForModelChanges() {
	if (Model.getFacade().isAUMLElement(target)) {
	    Model.getPump().removeModelEventListener(this, target,
	                propertyName);
	}
	LOG.debug(">>Stop listening for UML changes...");
    }

    public void propertyChange(PropertyChangeEvent e) {
	if (propertyName.equals(e.getPropertyName())) {
	    if (rememberExpression != e.getNewValue()) {
		fireStateChanged();
		LOG.debug(">>UML expression changed.");
	    } else {
		/* This should not happen. */
		LOG.debug(">>Got an event for a modelchange that we inflicted ourselves...");
	    }
	}
    }

    protected Object getTarget() {
        return target;
    }

    /**
     * @return the expression
     */
    private Object getExpression() {
        return Model.getFacade().getInitialValue(getTarget());
    }

    /**
     * @param expr the expression
     */
    private void setExpression(Object expression) {
        Object target = getTarget();
        assert (expression == null) || Model.getFacade().isAExpression(expression);
        /* If we do not set it to null first, then we get a MDR DebugException: */
        Model.getCoreHelper().setInitialValue(target, null);
        Model.getCoreHelper().setInitialValue(target, expression);
    }

    /**
     * @return a new expression with given language and body
     */
    public Object newExpression(String lang, String body) {
        return Model.getDataTypesFactory().createExpression(lang, body);
    }

    /**
     * @return The value text of the value specification.
     */
    public String getText() {
        Object expression = getExpression();
        if (expression == null) {
            return EMPTYSTRING;
        }
        return Model.getFacade().getBody(expression).toString();
    }

    /**
     * @param text the value text of the value specification
     */
    public void setText(String text) {

	Object expression = getExpression();
        boolean mustChange = true;
        if (expression != null) {
            Object oldValue = Model.getFacade().getBody(expression).toString();
            if (oldValue != null && oldValue.equals(text)) {
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

            setExpression(lang, text);
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
        LOG.debug(">>Add listener");
    }

    /**
     * Removes a <code>ChangeListener</code>.
     *
     * @param l the <code>ChangeListener</code> to remove
     * @see #addChangeListener
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
        LOG.debug(">>Remove listener");
    }

    /**
     * Runs each <code>ChangeListener</code>'s
     * <code>stateChanged</code> method.
     *
     * @see #setRangeProperties
     * @see EventListenerList
     */
    protected void fireStateChanged() {
	LOG.debug(">>Fire state changed to listeners.");
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
