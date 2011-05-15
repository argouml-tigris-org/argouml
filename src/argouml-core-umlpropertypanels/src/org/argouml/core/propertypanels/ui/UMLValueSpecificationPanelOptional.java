/* $Id$
 *******************************************************************************
 * Copyright (c) 2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Laurent Braud (issue 6215)
 *******************************************************************************
 */

package org.argouml.core.propertypanels.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.tigris.swidgets.LabelledLayout;

/**
 * Checkbox creates or nulls the instance of valueSpecificationPanel
 * 
 * @author BRAUD (issue 6215)
 * 
 *         Do we need : ChangeListener
 */
public class UMLValueSpecificationPanelOptional extends JPanel implements
	ChangeListener {

    private static final Logger LOG = Logger
	    .getLogger(UMLValueSpecificationPanel.class);

    /**
     * 
     */
    private JCheckBox valueExists;

    /**
     * Is null if valueExists isn't check
     */
    private UMLValueSpecificationPanel uvsPanel;

    /**
     * 
     */
    private final UMLValueSpecificationModel model;

    public UMLValueSpecificationPanelOptional(
	    UMLValueSpecificationModel aModel, String title) {

	// super(new LabelledLayout());
	super(new GridBagLayout());
	
	LOG.debug(">>New Optional ValueSpecification panel created");

	TitledBorder border = new TitledBorder(title);
	this.setBorder(border);

	this.model = aModel;

	// TODO : ?? use an other CheckBox (defines in Argo and extends
	// JCheckBox )
	valueExists = new JCheckBox();

	//
	boolean withDefault = model.getExpression() != null;
	valueExists.setSelected(withDefault);

	valueExists.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		uvsPanel.setVisible(valueExists.isSelected());
		if (valueExists.isSelected()) {
		    // Create it [Here, OpaqueExpression which is the first
		    // choice]
		    // Note that if we edit the Initial value in the diagram
		    // (double clic)
		    // it was replaced by an OpaqueExpression
		    uvsPanel.selectDefaultVS();
		} else {
		    // Destroy it
		    model.setValue(null);
		}
	    }
	});

	// title = "" because already done for Optional
	uvsPanel = new UMLValueSpecificationPanel(model, "");
	uvsPanel.setVisible(valueExists.isSelected());

	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.NONE;
	c.gridx = 0;
	c.gridy = 0;
	c.weightx = 0;
	c.weighty = 0;
	add(valueExists, c);

	c.fill = GridBagConstraints.BOTH;
	c.gridx = 1;
	c.gridy = 0;
	c.weightx = 1;
	c.weighty = 1;
	add(uvsPanel, c);

	model.addChangeListener(this);

    }

    /**
     * TODO: Is is really used ?
     * 
     * @see javax.swing.JComponent#removeNotify()
     */
    @Override
    public void removeNotify() {
	model.removeChangeListener(this);
	super.removeNotify();
    }

    /**
     * TODO: Is is really used ?
     * 
     * @see javax.swing.JComponent#removeNotify()
     */
    public void stateChanged(ChangeEvent e) {
	LOG.debug(">>Values shown on panel are changed");

    }

}
