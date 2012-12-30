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

import java.awt.Component;
import java.util.logging.Logger;

abstract class UMLValueSpecificationValueField {

    /**
     * TODO: see if protected or need to be present in each subclass
     */
    protected static final Logger LOG =
        Logger.getLogger(UMLValueSpecificationValueField.class.getName());

    /**
     *
     */
    private UMLValueSpecificationModel model;

    /**
     * TODO: Use it notify Set to true to forward events to model. Only one of
     * Language and Body fields should have this set to true.
     */
    private boolean notifyModel;

    protected Component allField;

    /**
     * Constructor which create the Component
     *
     * @param model
     * @param notify
     */
    public UMLValueSpecificationValueField(UMLValueSpecificationModel aModel,
	    boolean notify) {
	this.model = aModel;
	this.notifyModel = notify;

	buildPanel();
	updateFields();

    }

    /**
     * Update all the Field in the Component "allField", which are display, with
     * the value of the model
     *
     */
    protected abstract void updateFields();

    /**
     * Update the model with the field If field aren't display, need to get the
     * current value before update
     */
    protected abstract void updateModel();

    /**
     * build the panel for the ValueSpecification's type
     */
    public abstract void buildPanel();

    /**
     * Return a component with all field need to display and modify this
     * ValueSpecification
     *
     * @return
     */
    public Component getComponent() {
	return allField;
    }

    public boolean isNotifyModel() {
	return notifyModel;
    }

    public UMLValueSpecificationModel getModel() {
	return model;
    }

}
