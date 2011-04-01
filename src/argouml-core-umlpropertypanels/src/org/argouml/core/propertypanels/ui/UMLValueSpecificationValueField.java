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

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.ui.LookAndFeelMgr;

/**
 * This text field shows the value of a UML value specification.
 * 
 */
class UMLValueSpecificationValueField extends JTextArea implements
	DocumentListener {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger
	    .getLogger(UMLValueSpecificationValueField.class);

    private UMLValueSpecificationModel model;
    private boolean notifyModel;

    /**
     * The constructor.
     * 
     * @param model
     *            ValueSpecification model, should be shared between the fields
     * @param notify
     *            Set to true to forward events to model. Only one of Language
     *            and Body fields should have this set to true.
     */
    public UMLValueSpecificationValueField(UMLValueSpecificationModel model,
	    boolean notify) {
	this.model = model;
	this.notifyModel = notify;
	getDocument().addDocumentListener(this);
	setToolTipText(Translator.localize("label.body.tooltip"));
	setFont(LookAndFeelMgr.getInstance().getStandardFont());
	setRows(2); // make it stretch vertically

	update();
    }

    void update() {
	String oldText = getText();
	String newText = model.getText();

	if (oldText == null || newText == null || !oldText.equals(newText)) {
	    if (oldText != newText) {
		setText(newText);
	    }
	}
    }

    /*
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.
     * DocumentEvent)
     */
    public void changedUpdate(final DocumentEvent p1) {
	model.setText(getText());
    }

    /*
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.
     * DocumentEvent)
     */
    public void removeUpdate(final DocumentEvent p1) {
	model.setText(getText());
    }

    /*
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.
     * DocumentEvent)
     */
    public void insertUpdate(final DocumentEvent p1) {
	model.setText(getText());
    }
}
