/* $Id$
 *******************************************************************************
 * Copyright (c) 2011-2012 Contributors - see below
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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Checkbox creates or nulls the instance of valueSpecificationPanel
 *
 * @author BRAUD (issue 6215)
 *
 *         Do we need : ChangeListener
 */
class UMLValueSpecificationPanelOptional extends JPanel implements
	ChangeListener {

    private static final Logger LOG =
        Logger.getLogger(UMLValueSpecificationPanel.class.getName());

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

	super(new BorderLayout());

        LOG.log(Level.FINE, ">>New Optional ValueSpecification panel created");

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

	add(valueExists, BorderLayout.WEST);
	add(uvsPanel, BorderLayout.CENTER);

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
        LOG.log(Level.FINE, ">>Values shown on panel are changed");

    }

}
