/* $Id$
 *******************************************************************************
 * Copyright (c) 2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Laurent Braud
 *******************************************************************************
 */

package org.argouml.core.propertypanels.ui;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.argouml.ui.LookAndFeelMgr;

class UMLValueSpecificationValueFieldLiteralString extends
	UMLValueSpecificationValueField implements DocumentListener {

    /**
     * Field TODO: TextArea or TextField
     */
    private JTextArea stringField;

    /**
     * The constructor.
     * 
     * @param model
     *            ValueSpecification model, should be shared between the fields
     * @param notify
     *            Set to true to forward events to model. Only one of Language
     *            and Body fields should have this set to true.
     */
    public UMLValueSpecificationValueFieldLiteralString(
	    UMLValueSpecificationModel model, boolean notify) {
	super(model, notify);

    }

    /**
     * @see org.argouml.core.propertypanels.ui.UMLValueSpecificationValueField#buildPanel()
     */
    @Override
    public void buildPanel() {
	stringField = new JTextArea();
	stringField.getDocument().addDocumentListener(this);
	// TODO: review the tool tips ?
	// stringField.setToolTipText(Translator.localize("label.body.tooltip"));
	stringField.setFont(LookAndFeelMgr.getInstance().getStandardFont());
	stringField.setRows(2); // make it stretch vertically

	allField = new JScrollPane(stringField);

    }

    /**
     * 
     * @see org.argouml.core.propertypanels.ui.UMLValueSpecificationValueField#updateFields()
     */
    public void updateFields() {
	String oldText = stringField.getText();
	String[] tabNewText = (String[]) getModel().getValue();
	String newText = tabNewText[0];
	if (oldText == null || newText == null || !oldText.equals(newText)) {
	    if (oldText != newText) {
		stringField.setText(newText);
	    }
	}
    }

    @Override
    protected void updateModel() {
	String[] tabValues = new String[] { stringField.getText() };
	getModel().setValue(tabValues);
    }

    /*
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.
     * DocumentEvent) TODO: Are the 3 methods uses ?
     */
    public void changedUpdate(final DocumentEvent p1) {
	updateModel();
    }

    /*
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.
     * DocumentEvent)
     */
    public void removeUpdate(final DocumentEvent p1) {
	updateModel();
    }

    /*
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.
     * DocumentEvent)
     */
    public void insertUpdate(final DocumentEvent p1) {
	updateModel();
    }
}
