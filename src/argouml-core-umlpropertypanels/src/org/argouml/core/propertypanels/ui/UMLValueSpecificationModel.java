/* $Id$
 *******************************************************************************
 * Copyright (c) 2011-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Thomas Neustupny
 *    Laurent Braud
 *******************************************************************************
 */

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
        Logger.getLogger(UMLValueSpecificationModel.class.getName());

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
                LOG.log(Level.FINE, ">>Got an event for a modelchange that we inflicted ourselves...");
	    }
	}
    }

    protected Object getTarget() {
        return target;
    }

    /**
     * @return the expression
     */
    public Object getExpression() {
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
     * @deprecated I don't use it.
     */
    public String getText() {
        Object expression = getExpression();
        if (expression == null) {
            return EMPTYSTRING;
        }
        return Model.getFacade().getBody(expression).toString();
    }


    /**
     * Get the ValueSpecification
     * @return
     */
    public Object[] getValue(){
	return Model.getDataTypesHelper().getValueSpecificationValues(getExpression());
    }
    /**
     * Set ValueSpecification
     *
     * @param tabValues: A formated array depends on type of ValueSpecification.
     */
    public void setValue(Object tabValues[]) {
        // Expressions are DataTypes, not independent model elements
        // be careful not to reuse them
	rememberExpression = getExpression();

	//
	stopListeningForModelChanges();
	if (tabValues == null) {
	    if (rememberExpression != null) {
		    Model.getUmlFactory().delete(rememberExpression);
	    }
	    rememberExpression = null;

	    setExpression(rememberExpression);
	} else {
	    Model.getDataTypesHelper().modifyValueSpecification(rememberExpression, tabValues);
	    // We need to to this otherwise there is no notification
	    // and diagram isn't change
	    Model.getCoreHelper().setInitialValue(target, rememberExpression);


	}

	startListeningForModelChanges();
    }

    /**
    *
    * @param sType
    */
   public void createValueSpecification(String sType) {

	Object exp=Model.getDataTypesHelper().createValueSpecification(getTarget(),sType);
	// needed for notification
	Model.getCoreHelper().setInitialValue(target,exp);

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
