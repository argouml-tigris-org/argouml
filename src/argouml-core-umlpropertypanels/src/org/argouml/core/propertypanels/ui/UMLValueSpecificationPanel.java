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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.tigris.swidgets.LabelledLayout;

/**
 * The panel that shows a value specification for an other UML element.
 */
class UMLValueSpecificationPanel extends JPanel implements ChangeListener {

    private static final Logger LOG = Logger
	    .getLogger(UMLValueSpecificationPanel.class);

    private final UMLValueSpecificationModel model;
    private final UMLValueSpecificationValueField valueField;

    public UMLValueSpecificationPanel(UMLValueSpecificationModel model,
	    String title) {

	super(new LabelledLayout());
	LOG.debug(">>New ValueSpecification panel created");

	TitledBorder border = new TitledBorder(title);
	this.setBorder(border);

	this.model = model;
	this.valueField = new UMLValueSpecificationValueField(model, true);

	add(new JScrollPane(valueField));

	model.addChangeListener(this);
    }

    @Override
    public void removeNotify() {
	model.removeChangeListener(this);
	super.removeNotify();
    }

    public void stateChanged(ChangeEvent e) {
	LOG.debug(">>Values shown on panel are changed");
	valueField.update();
    }
}
