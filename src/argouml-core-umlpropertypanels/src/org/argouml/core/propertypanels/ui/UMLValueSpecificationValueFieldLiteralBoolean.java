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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JScrollPane;

import org.argouml.ui.LookAndFeelMgr;

public class UMLValueSpecificationValueFieldLiteralBoolean extends
	UMLValueSpecificationValueField {

    /**
     * checkbox: Field
     */
    private JCheckBox checkbox;

    /**
     * 
     * @param model
     * @param notify
     */
    public UMLValueSpecificationValueFieldLiteralBoolean(
	    UMLValueSpecificationModel model, boolean notify) {

	super(model, notify);

    }

    /**
     * The panel consist of: One CheckBox
     * 
     * @see org.argouml.core.propertypanels.ui.UMLValueSpecificationValueField#buildPanel()
     */
    @Override
    public void buildPanel() {
	// ///////////////////////////////////////
	// Build the field
	// ///////////////////////////////////////

	checkbox = new JCheckBox();
	// TODO ? find a Tool tips, add a label
	// checkbox.setToolTipText(Translator.localize("label.body.tooltip"));
	checkbox.setFont(LookAndFeelMgr.getInstance().getStandardFont());

	/**
	 * on change : Change the value in the model
	 */
	checkbox.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		updateModel();
	    }
	});

	// ///////////////////////////////////////
	// Add field(s) to panel
	// ///////////////////////////////////////
	this.allField = new JScrollPane(checkbox);

    }

    /**
     * 
     * @see org.argouml.core.propertypanels.ui.UMLValueSpecificationValueField#updateModel()
     */
    protected void updateModel() {
	// The checkbox have only one information: the value (true or false)
	Boolean[] values = new Boolean[1];
	values[0] = checkbox.isSelected();

	// Update the model, and then notify
	getModel().setValue(values);

    }

    /**
     * 
     * @see org.argouml.core.propertypanels.ui.UMLValueSpecificationValueField#updateFields()
     */
    protected void updateFields() {
	boolean oldSelected = checkbox.isSelected();
	boolean newSelected = isToCheck();

	if (oldSelected != newSelected) {
	    checkbox.setSelected(isToCheck());
	}
    }

    /**
     * return if the value in model is "true" or not.
     * 
     * @return
     */
    public boolean isToCheck() {
	Boolean[] values = (Boolean[]) getModel().getValue();
	return values[0];
    }

}
