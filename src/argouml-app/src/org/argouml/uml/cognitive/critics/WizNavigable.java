/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import org.argouml.cognitive.ui.WizStepChoice;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;

/**
 * A non-modal wizard to help the user change navigability
 * of an association.
 */
public class WizNavigable extends UMLWizard {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(WizNavigable.class.getName());

    private String instructions =
        Translator.localize("critics.WizNavigable-ins");
    private String option0 =
        Translator.localize("critics.WizNavigable-option1");
    private String option1 =
        Translator.localize("critics.WizNavigable-option2");
    private String option2 =
        Translator.localize("critics.WizNavigable-option3");

    private WizStepChoice step1 = null;

    /**
     * The constructor.
     *
     */
    public WizNavigable() { }

    /**
     * @return the options
     */
    public List<String> getOptions() {
        List<String> result = new ArrayList<String>();
	Object asc = getModelElement();
	Object ae0 =
            new ArrayList(Model.getFacade().getConnections(asc)).get(0);
	Object ae1 =
	    new ArrayList(Model.getFacade().getConnections(asc)).get(1);
	Object cls0 = Model.getFacade().getType(ae0);
	Object cls1 = Model.getFacade().getType(ae1);

	if (cls0 != null && !"".equals(Model.getFacade().getName(cls0))) {
	    option0 = Translator.localize("critics.WizNavigable-option4")
                + Model.getFacade().getName(cls0);
        }

	if (cls1 != null && !"".equals(Model.getFacade().getName(cls1))) {
	    option1 = Translator.localize("critics.WizNavigable-option5")
                + Model.getFacade().getName(cls1);
        }

	result.add(option0);
	result.add(option1);
	result.add(option2);
	return result;
    }

    /**
     * @param s the instructions
     */
    public void setInstructions(String s) { instructions = s; }

    /*
     * @see org.argouml.cognitive.ui.Wizard#makePanel(int)
     */
    public JPanel makePanel(int newStep) {
	switch (newStep) {
	case 1:
	    if (step1 == null) {
		step1 = new WizStepChoice(this, instructions, getOptions());
		step1.setTarget(getToDoItem());
	    }
	    return step1;
	}
	return null;
    }

    /*
     * @see org.argouml.cognitive.ui.Wizard#doAction(int)
     */
    public void doAction(int oldStep) {
        LOG.log(Level.FINE, "doAction {0}", oldStep);

	switch (oldStep) {
	case 1:
	    int choice = -1;
	    if (step1 != null) {
                choice = step1.getSelectedIndex();
            }
	    if (choice == -1) {
		throw new Error("nothing selected, should not get here");
	    }
	    try {
		Object asc = getModelElement();
		Object ae0 =
		    new ArrayList(Model.getFacade().getConnections(asc)).get(0);
		Object ae1 =
		    new ArrayList(Model.getFacade().getConnections(asc)).get(1);
		Model.getCoreHelper().setNavigable(ae0,
		        choice == 0 || choice == 2);
		Model.getCoreHelper().setNavigable(ae1,
		        choice == 1 || choice == 2);
	    } catch (Exception pve) {
                LOG.log(Level.SEVERE, "could not set navigablity", pve);
	    }
	}
    }

    /*
     * @see org.argouml.cognitive.ui.Wizard#canFinish()
     */
    @Override
    public boolean canFinish() {
	if (!super.canFinish()) {
            return false;
        }
	if (getStep() == 0) {
            return true;
        }
	if (getStep() == 1 && step1 != null && step1.getSelectedIndex() != -1) {
            return true;
        }
	return false;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 2571165058454693999L;
} /* end class WizNavigable */
