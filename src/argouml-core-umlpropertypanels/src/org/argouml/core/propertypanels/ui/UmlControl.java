/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 */

package org.argouml.core.propertypanels.ui;

import java.awt.LayoutManager;
import java.util.List;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.tigris.swidgets.FlexiGridLayout;
import org.tigris.toolbar.ToolBarFactory;
import org.tigris.toolbar.toolbutton.PopupToolBoxButton;
import org.tigris.toolbar.toolbutton.ToolButton;

/**
 * The base class for controls on a panel.
 *
 * @author Bob Tarling
 */
class UmlControl extends JPanel {
	
	/**
	 * The uid
	 */
	private static final long serialVersionUID = -826243573886719300L;

	public UmlControl(LayoutManager layout) {
		super(layout);
	}
	
	/**
	 * Create a JPanel containing a single button to access the given actions.
	 * If there if more than one button then a drop down button is created
	 * with the actions otherwise there is a simple button for the single
	 * action.
	 * @param actions the actions from which to generate the panel
	 * @return the panel containing the actions required.
	 */
	protected JPanel createSingleButtonPanel(List<Action> actions) {
        final JPanel buttonPanel =
            new JPanel(new FlexiGridLayout(2, 1, FlexiGridLayout.ROWCOLPREFERRED));
		final JToolBar toolbar;
    	if (actions.size() == 1) {
    		final ToolButton tb = new ToolButton(actions.get(0));
            final ToolBarFactory tbf = new ToolBarFactory(actions);
            toolbar = tbf.createToolBar();
            toolbar.setRollover(true);
    	} else {
    		final PopupToolBoxButton ptb =
    			new PopupToolBoxButton(actions.get(0), actions.size(), 1, true);
            for (Action action : actions) {
                ptb.add(action);
            }
            final ToolBarFactory tbf = new ToolBarFactory(new Object[] {});
            toolbar = tbf.createToolBar();
            toolbar.setRollover(true);
    		toolbar.add(ptb);
    	}
		buttonPanel.add(toolbar);
        return buttonPanel;
	}
}
