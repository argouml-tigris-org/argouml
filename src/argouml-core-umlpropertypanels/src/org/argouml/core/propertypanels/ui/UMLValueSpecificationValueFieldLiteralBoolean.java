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

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.ui.LookAndFeelMgr;

class UMLValueSpecificationValueFieldLiteralBoolean extends
	UMLValueSpecificationValueField {

    private JRadioButton trueButton;

    private JRadioButton falseButton;

    private ButtonGroup trueFalseGroup;
    
    /**
     * true if we can call UpdateModele
     * false : We have read the model, so we have to update field. Don't update Modele a new.
     */
    private boolean activeUpdateModele;

    /**
     * 
     * @param model
     * @param notify
     */
    public UMLValueSpecificationValueFieldLiteralBoolean(
	    UMLValueSpecificationModel model, boolean notify) {

	super(model, notify);
	activeUpdateModele = true;
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

	trueButton = new JRadioButton(Translator.localize("misc.boolean.true"));
	falseButton = new JRadioButton(Translator
		.localize("misc.boolean.false"));

	trueFalseGroup = new ButtonGroup();
	trueFalseGroup.add(trueButton);
	trueFalseGroup.add(falseButton);

	trueButton.setFont(LookAndFeelMgr.getInstance().getStandardFont());
	falseButton.setFont(LookAndFeelMgr.getInstance().getStandardFont());

	/**
	 * on change : Change the value in the model
	 */
	trueButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		updateModel();
	    }
	});

	falseButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		updateModel();
	    }
	});

	// ///////////////////////////////////////
	// Add field(s) to panel
	// ///////////////////////////////////////
	JPanel panel = new JPanel();
	panel.add(trueButton);
	panel.add(falseButton);
	this.allField = new JScrollPane(panel);

    }

    /**
     * 
     * @see org.argouml.core.propertypanels.ui.UMLValueSpecificationValueField#updateModel()
     */
    protected void updateModel() {
	if (activeUpdateModele) {
	    boolean oldSelected = trueButton.isSelected();
	    boolean newSelected = isToCheck();

	    // click on the already selected value must not call setValue
	    if (oldSelected != newSelected) {
		// The 2 Radios have only one information.
		Boolean[] values = new Boolean[1];
		values[0] = trueButton.isSelected();

		// Update the model, and then notify
		getModel().setValue(values);
	    }
	}
    }

    /**
     * 
     * @see org.argouml.core.propertypanels.ui.UMLValueSpecificationValueField#updateFields()
     */
    protected void updateFields() {
	boolean oldSelected = trueButton.isSelected();
	boolean newSelected = isToCheck();

	if (oldSelected != newSelected) {
	    boolean oldActiveUpdateModele = activeUpdateModele;
	    activeUpdateModele = false;
	    trueButton.setSelected(newSelected);
	    falseButton.setSelected(!newSelected);
	    activeUpdateModele = oldActiveUpdateModele;
	} else {
	    // When call by contructor : no one is selected
	    oldSelected = falseButton.isSelected();
	    if (oldSelected == false && newSelected == false) {
		boolean oldActiveUpdateModele = activeUpdateModele;
		activeUpdateModele = false;
		falseButton.setSelected(true);
		activeUpdateModele = oldActiveUpdateModele;
	    }
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
