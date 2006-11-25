// $Id$
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

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.argouml.cognitive.ui.WizStepTextField;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;

/**
 * A non-modal wizard to help the user change the name of a
 * MModelElement to a better name.
 *
 * @author jrobbins
 */
public class WizMEName extends UMLWizard {
    private static final Logger LOG = Logger.getLogger(WizMEName.class);

    private String instructions = Translator.localize("critics.WizMEName-ins");
    private String label = Translator.localize("label.name");
    private boolean mustEdit = false;

    private WizStepTextField step1 = null;

    private String origSuggest;

    /**
     * The constructor.
     *
     */
    public WizMEName() { }

    /**
     * @param s the instructions
     */
    public void setInstructions(String s) { instructions = s; }

    /**
     * @param b if true, then the wizard step needs userinput,
     *          i.e. it must be edited
     */
    public void setMustEdit(boolean b) { mustEdit = b; }

    /**
     * Create a new panel for the given step.
     *
     * @see org.argouml.cognitive.ui.Wizard#makePanel(int)
     */
    public JPanel makePanel(int newStep) {
	switch (newStep) {
	case 1:
	    if (step1 == null) {
		step1 = new WizStepTextField(this, instructions,
					      label, offerSuggestion());
	    }
	    return step1;
	}
	return null;
    }

    /*
     * @see org.argouml.uml.cognitive.critics.UMLWizard#setSuggestion(java.lang.String)
     */
    public void setSuggestion(String s) {
        origSuggest = s;
        super.setSuggestion(s);
    }


    /**
     * Return false if the user has not edited the text and they were required
     * to.
     *
     * @see org.argouml.cognitive.ui.Wizard#canGoNext()
     */
    public boolean canGoNext() {
	if (!super.canGoNext()) return false;
	if (step1 != null) {
	    boolean changed = origSuggest.equals(step1.getText());
	    if (mustEdit && !changed) return false;
	}
	return true;
    }

    /*
     * @see org.argouml.cognitive.ui.Wizard#doAction(int)
     */
    public void doAction(int oldStep) {
	LOG.debug("doAction " + oldStep);
	switch (oldStep) {
	case 1:
	    String newName = getSuggestion();
	    if (step1 != null) {
	        newName = step1.getText();
	    }
	    try {
		Object me = getModelElement();
		Model.getCoreHelper().setName(me, newName);
	    }
	    catch (Exception pve) {
		LOG.error("could not set name", pve);
	    }
	}
    }

    /**
     * @return Returns the instructions.
     */
    protected String getInstructions() {
        return instructions;
    }
} /* end class WizMEName */
