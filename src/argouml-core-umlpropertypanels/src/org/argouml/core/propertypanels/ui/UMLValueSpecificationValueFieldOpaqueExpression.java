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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.argouml.i18n.Translator;
import org.argouml.ui.LookAndFeelMgr;

/**
 * An OpaqueExpression can have n body n language
 * 
 * When display to user, we only show one body.
 * 
 * TODO: How to add/delete/search a language for edit it TODO: Can we, by Import
 * XMI, have 0 language ?
 * 
 * @author Laurent Braud
 */
public class UMLValueSpecificationValueFieldOpaqueExpression extends
	UMLValueSpecificationValueField implements DocumentListener {

    /**
     * The body display at the moment
     */
    private JTextArea curBody;
    /**
     * The Language display at the moment
     */
    private JTextField curLanguage;

    /**
     * The number of the currentBody and currentLangage
     */
    private int currentText;

    /**
     * The constructor.
     */
    public UMLValueSpecificationValueFieldOpaqueExpression(
	    UMLValueSpecificationModel model, boolean notify) {
	super(model, notify);

    }

    /**
     * 
     * 
     * @see org.argouml.core.propertypanels.ui.UMLValueSpecificationValueField#buildPanel()
     */
    public void buildPanel() {
	currentText = 0;
	// Create the body and language Field
	curBody = new JTextArea();
	curBody.setToolTipText(Translator.localize("label.body.tooltip"));
	curBody.setRows(2); // make it stretch vertically
	curBody.getDocument().addDocumentListener(this);

	curLanguage = new JTextField();
	curLanguage.setToolTipText(Translator
		.localize("label.language.tooltip"));
	curLanguage.getDocument().addDocumentListener(this);

	// create Panel containing the previous field
	JPanel panel = new JPanel();
	panel.setFont(LookAndFeelMgr.getInstance().getStandardFont());

	panel.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.HORIZONTAL;
	c.insets = new Insets(1, 1, 1, 1);
	c.gridx = 0;
	c.gridy = 0;
	c.weightx = 100;
	panel.add(curLanguage, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.insets = new Insets(1, 1, 1, 1);
	c.ipady = 40;
	// c.weightx = 0.0;
	// c.gridwidth = 3;
	c.gridx = 0;
	c.gridy = 1;
	panel.add(curBody, c);

	//
	this.allField = panel;

    }

    /***
     * 
     * @see org.argouml.core.propertypanels.ui.UMLValueSpecificationValueField#updateFields()
     */
    public void updateFields() {
	String oldText = curBody.getText();
	String[] newTabText = (String[]) getModel().getValue();
	String newText = "";
	if (newTabText != null) {
	    newText = newTabText[2 * currentText];
	}

	if (oldText == null || newText == null || !oldText.equals(newText)) {
	    if (oldText != newText) {
		curBody.setText(newText);
	    }
	}

	oldText = curLanguage.getText();
	newText = "";
	if (newTabText != null) {
	    newText = newTabText[2 * currentText + 1];
	}

	if (oldText == null || newText == null || !oldText.equals(newText)) {
	    if (oldText != newText) {
		curLanguage.setText(newText);
	    }
	}

    }

    /*
     * TODO: Are the 3 methods uses ?
     */
    public void changedUpdate(DocumentEvent arg0) {
	updateModel();
    }

    public void insertUpdate(DocumentEvent arg0) {
	updateModel();
    }

    public void removeUpdate(DocumentEvent arg0) {
	updateModel();
    }

    protected void updateModel() {
	String[] tabValues = (String[]) getModel().getValue();
	tabValues[2 * currentText] = curBody.getText();
	tabValues[2 * currentText + 1] = curLanguage.getText();
	getModel().setValue(tabValues);
    }

}
