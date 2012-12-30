/* $Id$
 *******************************************************************************
 * Copyright (c) 2011-2012 Contributors - see below
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.argouml.i18n.Translator;
import org.argouml.util.ArgoDialog;

/**
 * A dialog to edit OpaqueExpression (usefull for more than one language/Body).
 * 
 * TODO ? Review the layout
 * 
 */
class UMLValueSpecificationValueFieldOpaqueExpressionDialog extends
	ArgoDialog {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = -5429439639242117770L;

    /**
     * 
     */
    private UMLValueSpecificationModel model;

    /**
     * current index of body/language at call
     */
    private int currentIndexCall;

    /**
     * current index of body/language display
     */
    private int currentIndex;

    /**
     * The Language field
     */
    private JTextField curLanguage;

    /**
     * 
     */
    private JTextArea curBody;

    /**
     * 
     */
    private JList list;
    /**
     * Model for JList, containing all the language value for this Property
     */
    private DefaultListModel listModel;

    public UMLValueSpecificationValueFieldOpaqueExpressionDialog(
	    UMLValueSpecificationModel aModel, int currentIndex) {

	super("OpaqueExpression", true); //$NON-NLS-1$

	this.model = aModel;

	this.currentIndexCall = currentIndex;
	this.currentIndex = currentIndex;

	setSize(450, 300);
	buildFirstPanel();

	updateFields();
    }

    private void buildFirstPanel() {
	JPanel contentPanel = new JPanel(new GridBagLayout());
	add(contentPanel);

	// The 5 main component : 2 label, a panel for langage , a panel with
	// button, a textarea
	JLabel labelLangage = new JLabel(Translator
		.localize("label.language.tooltip"));
	JLabel labelBody = new JLabel(Translator.localize("label.body.tooltip"));

	curBody = new JTextArea();
	curBody.setToolTipText(Translator.localize("label.body.tooltip"));
	curBody.setRows(2); // make it stretch vertically
	// TODO: The curBody must notify modification

	JPanel languagePanel = buildLanguagePanel();

	JPanel paneBtn = buildBoutonPanel();

	// Layout
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.HORIZONTAL;
	c.insets = new Insets(1, 1, 1, 1);
	c.gridx = 0;
	c.gridy = 0;
	c.weightx = 0.5;
	c.weighty = 0;
	contentPanel.add(labelLangage, c);

	c = new GridBagConstraints();
	c.insets = new Insets(1, 1, 1, 1);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 2;
	c.gridy = 0;
	c.weightx = 0.5;
	c.weighty = 0;
	contentPanel.add(labelBody, c);

	c = new GridBagConstraints();
	c.insets = new Insets(1, 1, 1, 1);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 0;
	c.gridy = 1;
	c.weightx = 0.5;
	c.weighty = 0;
	c.anchor = GridBagConstraints.NORTHEAST;
	contentPanel.add(languagePanel, c);

	c = new GridBagConstraints();
	c.insets = new Insets(1, 1, 1, 1);
	c.fill = GridBagConstraints.BOTH;
	c.gridx = 2;
	c.gridy = 1;
	c.weightx = 0.5;
	c.weighty = 1;
	contentPanel.add(curBody, c);

	c = new GridBagConstraints();
	c.insets = new Insets(1, 1, 1, 1);
	c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 1;
	contentPanel.add(paneBtn, c);

    }

    private JPanel buildBoutonPanel() {
	JPanel paneBtn = new JPanel(new GridBagLayout());

	// Save the model (current)
	JButton btnApply = new JButton(Translator.localize("button.ok"));
	btnApply.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		String[] tabValues = (String[]) model.getValue();
		boolean bContinue = true;
		if (!curLanguage.getText().equals(
			tabValues[2 * currentIndex + 1])) {
		    // Language has changed
		    bContinue = isUniqueLanguage(tabValues, curLanguage
			    .getText());

		}
		if (bContinue) {
		    tabValues[2 * currentIndex] = curBody.getText();
		    tabValues[2 * currentIndex + 1] = curLanguage.getText();
		    model.setValue(tabValues);
		    updateFields();
		}
		// TODO: else : alert

	    }
	});

	// Add a new entry in the list and select it.
	// Translator.localize("button.add")
	JButton btnAdd = new JButton("+"); // +
	btnAdd.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		String[] tabValues = (String[]) model.getValue();

		int num = getEmptyLanguage(tabValues);
		if (num == -1) {
		    String[] newTabValues = new String[tabValues.length + 2];
		    System.arraycopy(tabValues, 0, newTabValues, 0,
			    tabValues.length);
		    newTabValues[tabValues.length] = "";
		    newTabValues[tabValues.length + 1] = "";
		    model.setValue(newTabValues);

		    currentIndex = newTabValues.length / 2 - 1;

		    updateFields();
		} else {
		    // A language must be unique
		    // So, select the current empty language.
		    // TODO ? Alert
		    currentIndex = num;
		    updateFields();

		}

	    }
	});

	// Translator.localize("button.delete")
	JButton btnDel = new JButton("-");
	btnDel.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		String[] tabValues = (String[]) model.getValue();
		if (tabValues.length > 2) {
		    String[] newTabValues = new String[tabValues.length - 2];

		    if (currentIndex > 0) {
			// Copy previous element
			System.arraycopy(tabValues, 0, newTabValues, 0,
				currentIndex * 2);
		    }
		    if (currentIndex < tabValues.length / 2) {
			// Copy next element
			System.arraycopy(tabValues, currentIndex * 2 + 2,
				newTabValues, currentIndex * 2,
				tabValues.length - (currentIndex * 2 + 2));
		    }

		    model.setValue(newTabValues);

		    if (currentIndex * 2 > newTabValues.length) {
			currentIndex = newTabValues.length / 2 - 1;
		    }

		    updateFields();

		} else {
		    String[] newTabValues = new String[2];
		    newTabValues[0] = "";
		    newTabValues[1] = "";
		    model.setValue(newTabValues);
		    updateFields();
		}

	    }
	});

	// Translator.localize("button.move-up")
	JButton btnUp = new JButton("/\\");
	btnUp.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		if (currentIndex > 0) {
		    String[] tabValues = (String[]) model.getValue();
		    String[] savTabValues = new String[2];
		    // Copy in memory the current
		    System.arraycopy(tabValues, currentIndex * 2, savTabValues,
			    0, 2);

		    // Copy the previous value in the current
		    tabValues[currentIndex * 2] = tabValues[currentIndex * 2 - 2];
		    tabValues[currentIndex * 2 + 1] = tabValues[currentIndex * 2 - 1];

		    // Copy the current in the previous
		    tabValues[(currentIndex - 1) * 2] = savTabValues[0];
		    tabValues[(currentIndex - 1) * 2 + 1] = savTabValues[1];

		    model.setValue(tabValues);

		    currentIndex = currentIndex - 1;
		    updateFields();

		}

	    }
	});

	// Translator.localize("button.move-down")
	JButton btnDown = new JButton("\\/");
	btnDown.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {

		if (currentIndex + 1 < listModel.size()) {
		    String[] tabValues = (String[]) model.getValue();
		    String[] savTabValues = new String[2];
		    // Copy in memory the current
		    System.arraycopy(tabValues, currentIndex * 2, savTabValues,
			    0, 2);

		    // Copy the next value in the current
		    tabValues[currentIndex * 2] = tabValues[(currentIndex + 1) * 2];
		    tabValues[currentIndex * 2 + 1] = tabValues[(currentIndex + 1) * 2 + 1];

		    // Copy the current in the next
		    tabValues[(currentIndex + 1) * 2] = savTabValues[0];
		    tabValues[(currentIndex + 1) * 2 + 1] = savTabValues[1];

		    model.setValue(tabValues);

		    currentIndex = currentIndex + 1;
		    updateFields();

		}

	    }
	});
	// Layout
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.NONE;
	c.gridx = 0;
	c.gridy = 0;
	paneBtn.add(btnApply, c);

	c = new GridBagConstraints();
	c.fill = GridBagConstraints.NONE;
	c.gridx = 0;
	c.gridy = 1;
	paneBtn.add(btnAdd, c);

	c = new GridBagConstraints();
	c.fill = GridBagConstraints.NONE;
	c.gridx = 0;
	c.gridy = 2;
	paneBtn.add(btnDel, c);

	c = new GridBagConstraints();
	c.fill = GridBagConstraints.NONE;
	c.gridx = 0;
	c.gridy = 3;
	paneBtn.add(btnUp, c);

	c = new GridBagConstraints();
	c.fill = GridBagConstraints.NONE;
	c.gridx = 0;
	c.gridy = 4;
	paneBtn.add(btnDown, c);

	return paneBtn;
    }

    private JPanel buildLanguagePanel() {
	JPanel panel = new JPanel(new GridBagLayout());

	// The 2 component: a textField and a list
	curLanguage = new JTextField();

	listModel = new DefaultListModel();
	list = new JList(listModel);
	list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	list.setSelectedIndex(currentIndex);

	list.addListSelectionListener(new ListSelectionListener() {

	    public void valueChanged(ListSelectionEvent arg0) {
		if (list.getSelectedIndex() != -1) {
		    if (list.getSelectedIndex() != currentIndex) {
			currentIndex = list.getSelectedIndex();
			updateFields();
		    }

		}

	    }
	});

	// Layout
	GridBagConstraints c = new GridBagConstraints();
	c.insets = new Insets(1, 1, 1, 1);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 0;
	c.gridy = 0;
	c.weightx = 1;
	panel.add(curLanguage, c);

	c = new GridBagConstraints();
	c.insets = new Insets(1, 1, 1, 1);
	c.fill = GridBagConstraints.BOTH;
	c.gridx = 0;
	c.gridy = 1;
	c.weightx = 1;
	c.weighty = 1;
	c.anchor = GridBagConstraints.NORTHEAST;
	panel.add(list, c);

	return panel;

    }

    private void updateFields() {
	String[] newTabText = (String[]) this.model.getValue();

	if (newTabText != null) {
	    listModel.removeAllElements();
	    for (int i = 1; i < newTabText.length; i += 2) {
		listModel.addElement(newTabText[i]);
	    }

	    if (currentIndex * 2 < newTabText.length) {

		list.setSelectedIndex(currentIndex);

		curBody.setText(newTabText[2 * currentIndex]);
		curLanguage.setText(newTabText[2 * currentIndex + 1]);

	    }
	}

    }

    private boolean isUniqueLanguage(String[] tabValues, String newLanguage) {
	// Language must be unique
	// TODO: do it in the model (if don't use the dialog, and
	// change the first)
	boolean bContinue = true;
	for (int i = 1; i < tabValues.length && bContinue; i += 2) {
	    if (i != currentIndex) {
		bContinue = !tabValues[i].equals(newLanguage);
	    }

	}
	return bContinue;
    }

    /**
     * Return the language which is empty
     * 
     * @param tabValues
     * @return -1 if it doesn't exist a such language
     * 
     */
    private int getEmptyLanguage(String[] tabValues) {
	int num = -1;
	for (int i = 1; i < tabValues.length && num == -1; i += 2) {
	    if (tabValues[i].equals("")) {
		num = (i - 1) / 2;
	    }
	}
	return num;
    }

}
