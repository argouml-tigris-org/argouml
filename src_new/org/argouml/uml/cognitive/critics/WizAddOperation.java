// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import org.tigris.gef.util.VectorSet;
import javax.swing.JPanel;

import org.argouml.cognitive.ui.WizStepTextField;
import org.argouml.i18n.Translator;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.kernel.Wizard;

/**
 * A wizard to add operations to a classifier.
 *
 * @author  mkl
 * @since Created on December 4, 2003, 1:55 PM
 */
public class WizAddOperation extends Wizard {
    
    protected WizStepTextField _step1 = null;
    protected String _label = Translator.localize("UMLMenu", "label.name");
    protected String _instructions =
	"Please change the name of the offending model element.";
    protected String _suggestion = "suggestion";
    protected String _origSuggest = "suggestion";
    protected boolean _mustEdit = false;
    
    /** Creates a new instance of WizAddOperation */
    public WizAddOperation() {
        super();
    }
    
    public void doAction(int oldStep) {
        switch (oldStep) {
	case 1:
	    String newName = _suggestion;
	    if (_step1 != null) {
		newName = _step1.getText();
	    }
	    Object me = getModelElement();
	    UmlFactory.getFactory().getCore().buildOperation(me, newName);
        }
    }
    
    public int getNumSteps() {
        return 1;
    }
    
    public Object getModelElement() {
        if (getToDoItem() != null) {
            VectorSet offs = _item.getOffenders();
            if (offs.size() >= 1) {
                Object me = /*(MModelElement)*/ offs.elementAt(0);
                return me;
            }
        }
        return null;
    }
    
    public String getSuggestion() {
        if (_suggestion != null) return _suggestion;
        Object me = getModelElement();
        if (me != null) {
            String n = ModelFacade.getName(me);
            return n;
        }
        return "";
    }
    public void setSuggestion(String s) {
	_suggestion = s;
	_origSuggest = s;
    }
    
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
    
}
