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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.argouml.model.Model;

/**
 * The panel that shows a value specification for an other UML element.
 *
 *
 * TODO: Do we need to implements ChangeListener If yes => ok If no => Can't be
 * use without *Optional class, or the *Optional don't need to !
 *
 */
class UMLValueSpecificationPanel extends JPanel {

    /**
     * Generated UID.
     */
    private static final long serialVersionUID = 1494398250907085817L;

    private static final Logger LOG =
        Logger.getLogger(UMLValueSpecificationPanel.class.getName());

    /**
     *
     */
    private final UMLValueSpecificationModel model;

    /**
     * The component for view/change the type.
     */
    private JComboBox typeInstanceValueList;

    /**
     * This contains a panel to display (A checkbox for boolean, ...)
     */
    private UMLValueSpecificationValueField valueField;

    /**
     *
     * TODO: Try to use valueField.getComponent()
     */
    private Component scrollPane;

    /**
     *
     * @param model
     * @param title
     */
    public UMLValueSpecificationPanel(UMLValueSpecificationModel model,
	    String title) {

	//super(new LabelledLayout());
	super(new GridBagLayout());
        LOG.log(Level.FINE, ">>New ValueSpecification panel created");

	TitledBorder border = new TitledBorder(title);
	this.setBorder(border);

	this.model = model;

	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.HORIZONTAL;
	// c.insets = new Insets(1, 1, 1, 1);
	c.gridx = 0;
	c.gridy = 0;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.weightx = 1;
	c.weighty = 0;

	JComboBox combo = uiSelect();
	add(combo, c);

	this.valueField = createField((String) combo.getSelectedItem());



    }

    /**
     *
     * @param sType
     * @return
     */
    private UMLValueSpecificationValueField createField(String sType) {

	if (scrollPane != null) {
	    remove(scrollPane);
	}

	UMLValueSpecificationValueField ret = null;

	try {
	    // TODO: Bob says the reflective code following should be replaced
	    // with something more specific like this commented out code.
	    // This would mean we need sType changed to a meta type Object
	    // rather than a String.

//	    if (Model.getFacade().isALiteralBoolean(sType)) {
//		fieldControl = new UMLValueSpecificationValueFieldLiteralBoolean(model, true);
//	    } else if (Model.getFacade().isALiteralString(sType)) {
//		fieldControl = new UMLValueSpecificationValueFieldLiteralString(model, true);
//	    } else if (Model.getFacade().isAOpaqueExpression(sType)) {
//		fieldControl = new UMLValueSpecificationValueFieldLiteralString(model, true);
//	    }

	    Class<?> oClass= Class.forName("org.argouml.core.propertypanels.ui.UMLValueSpecificationValueField"+sType);
	    Constructor<?> constructeur = oClass.getConstructor (new Class [] {UMLValueSpecificationModel.class,boolean.class});
	    ret=(UMLValueSpecificationValueField) constructeur.newInstance (new Object [] {model, true});
	} catch (Exception e) {
            LOG.log(Level.SEVERE, "Unknow type "+sType+" : "+e, e);
	    return null;
	}

	scrollPane = ret.getComponent();

	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.BOTH;
	c.gridx = 0;
	c.gridy = 1;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.weightx = 1;
	c.weighty = 1;
	add(scrollPane, c);

	return ret;
    }

    /**
     * Create the combobox wich display available ValueSpecification (type)
     *
     * TODO LiteralNull,Expression, InstanceValue,... TODO ? Use something else
     * that a combobox. TODO ? If combobox, can we restrict list ? For instance,
     * can we create a Boolean for a Integer Value ?
     *
     * TODO
     *
     * @return
     */
    private JComboBox uiSelect() {

	// Get the list: OpaqueExpression, LiteralString,...
	Collection<String> listVS = Model.getDataTypesHelper()
		.getValueSpecifications();
	Object[] typeInstanceValue = listVS.toArray();

	typeInstanceValueList = new JComboBox(typeInstanceValue);

	int iSel = 0;// By default, the first value of the combobox is selected.

	if (model != null) {
	    // Get current InitialValue
	    Object expression = this.model.getExpression();

	    if (expression != null) {
		// Select the Current type in the combobox

		// if "expression" implements one of the combobox, select it
		// TODO ? Do it in eUML module project ?
		Class<?>[] interfaces = expression.getClass().getInterfaces();
		iSel = -1;
		for (int iInterf = 0; iInterf < interfaces.length && iSel == -1; iInterf++) {
		    for (int iVS = 0; iVS < typeInstanceValue.length
			    && iSel == -1; iVS++) {
			if (interfaces[iInterf].getSimpleName().equals(
				typeInstanceValue[iVS])) {
			    iSel = iVS;
			}
		    }
		}
	    }

	}

	typeInstanceValueList.setSelectedIndex(iSel);

	/**
	 * When we change the type, we need to create a new Initial Value. And
	 * to display the Panel
	 *
	 * TODO: if we select the same type that the current, do nothing.
	 */
	typeInstanceValueList.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent ae) {

		if (ae.getActionCommand().equals("comboBoxChanged")) {

		    JComboBox lst = (JComboBox) ae.getSource();
		    String sTypeVS = (String) lst.getSelectedItem();
		    model.createValueSpecification(sTypeVS);
		    createField(sTypeVS);
		    // TODO: When the attribute isn't in the diagram
		    // , for exemple: select attribut by Explorer
		    // Then, the refresh isn't auto: we had to click !!
		}

	    }
	});

	return typeInstanceValueList;
    }

    /**
     * Select element in a combo.
     */
    public void selectDefaultVS() {
	// default : 0 => OpaqueExpression
	// set to 1 for test
	typeInstanceValueList.setSelectedIndex(0);
    }

}
