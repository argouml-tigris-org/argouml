// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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



// File: WzMEName.java
// Classes: WzMEName
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import javax.swing.JPanel;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.cognitive.ui.WizStepTextField;
import org.argouml.kernel.Wizard;
import org.tigris.gef.util.VectorSet;

import ru.novosoft.uml.foundation.core.MModelElement;

/** A non-modal wizard to help the user change the name of a
 *  MModelElement to a better name. */

public class WizMEName extends Wizard {
    protected static Category cat = Category.getInstance(WizMEName.class);
					   
    protected String _instructions =
	"Please change the name of the offending model element.";
    protected String _label = Argo.localize("UMLMenu", "label.name");
    protected String _suggestion = "suggestion";
    protected String _origSuggest = "suggestion";
    protected boolean _mustEdit = false;
							       
    protected WizStepTextField _step1 = null;
								   
    public WizMEName() { }
								       
    public int getNumSteps() { return 1; }
									   
    public MModelElement getModelElement() {
	if (_item != null) {
	    VectorSet offs = _item.getOffenders();
	    if (offs.size() >= 1) {
		MModelElement me = (MModelElement) offs.elementAt(0);
		return me;
	    }
	}
	return null;
    }
									       
    public String getSuggestion() {
	if (_suggestion != null) return _suggestion;
	MModelElement me = getModelElement();
	if (me != null) {
	    String n = me.getName();
	    return n;
	}
	return "";
    }
    public void setSuggestion(String s) { _origSuggest = _suggestion = s; }

    public void setInstructions(String s) { _instructions = s; }

    public void setMustEdit(boolean b) { _mustEdit = b; }

    /** Create a new panel for the given step.  */
    public JPanel makePanel(int newStep) {
	switch (newStep) {
	case 1:
	    if (_step1 == null) {
		_step1 = new WizStepTextField(this, _instructions,
					      _label, getSuggestion());
	    }
	    return _step1;
	}
	return null;
    }

    /** Return false iff the user has not edited the text and they were
     *  required to. */
    public boolean canGoNext() {
	if (!super.canGoNext()) return false;
	if (_step1 != null) {
	    boolean changed = _origSuggest.equals(_step1.getText());
	    if (_mustEdit && !changed) return false;
	}
	return true;
    }

    /** Take action at the completion of a step. For example, when the
     *  given step is 0, do nothing; and when the given step is 1, do
     *  the first action.  Argo non-modal wizards should take action as
     *  they do along, as soon as possible, they should not wait until
     *  the final step. */
    public void doAction(int oldStep) {
	cat.debug("doAction " + oldStep);
	switch (oldStep) {
	case 1:
	    String newName = _suggestion;
	    if (_step1 != null) newName = _step1.getText();
	    try {
		MModelElement me = getModelElement();
		me.setName(newName);
	    }
	    catch (Exception pve) {
		cat.error("could not set name", pve);
	    }
	}
    }
} /* end class WizMEName */
