// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.util.Vector;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.argouml.cognitive.ui.WizStepManyTextFields;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;

/**
 * A non-modal wizard to help the user change the name of a
 * MModelElement to a better name.
 *
 * @author jrobbins
 */

public class WizManyNames extends UMLWizard {
    private static final Logger LOG = Logger.getLogger(WizManyNames.class);

    private String instructions =
	"Please change the name of the offending model element.";
    private Vector mes = null;

    private WizStepManyTextFields step1 = null;

    /**
     * The constructor.
     *
     */
    public WizManyNames() { }


    /**
     * @param m the offenders
     */
    public void setMEs(Vector m) { mes = m; }

    /**
     * @param s set the new instructions
     */
    public void setInstructions(String s) { instructions = s; }

    /**
     * Create a new panel for the given step.
     *
     * @see org.argouml.cognitive.ui.Wizard#makePanel(int)
     */
    public JPanel makePanel(int newStep) {
	switch (newStep) {
	case 1:
	    if (step1 == null) {
		Vector names = new Vector();
		int size = mes.size();
		for (int i = 0; i < size; i++) {
		    Object me = /*(MModelElement)*/ mes.elementAt(i);
		    names.addElement(ModelFacade.getName(me));
		}
		step1 = new WizStepManyTextFields(this, instructions, names);
	    }
	    return step1;
	}
	return null;
    }

    /**
     * Take action at the completion of a step. For example, when the
     * given step is 0, do nothing; and when the given step is 1, do
     * the first action.  Argo non-modal wizards should take action as
     * they do along, as soon as possible, they should not wait until
     * the final step.
     *
     * @see org.argouml.cognitive.ui.Wizard#doAction(int)
     */
    public void doAction(int oldStep) {
	LOG.debug("doAction " + oldStep);
	switch (oldStep) {
	case 1:
	    Vector newNames = null;
	    if (step1 != null) newNames = step1.getStrings();
	    try {
		int size = mes.size();
		for (int i = 0; i < size; i++) {
		    Object me = /*(MModelElement)*/ mes.elementAt(i);
		    Model.getCoreHelper().setName(me, (String) newNames.elementAt(i));
		}
	    }
	    catch (Exception pve) {
		LOG.error("could not set name", pve);
	    }
	}
    }
} /* end class WizManyNames */
