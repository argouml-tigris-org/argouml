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
    
    private WizStepTextField step1 = null;
    private String label = Translator.localize("UMLMenu", "label.name");
    private String instructions =
	"Please change the name of the offending model element.";
    private String suggestion = "suggestion";
    private String origSuggest = "suggestion";
    private boolean mustEdit = false;
    
    /** Creates a new instance of WizAddOperation */
    public WizAddOperation() {
        super();
    }
    
    /**
     * @see org.argouml.kernel.Wizard#doAction(int)
     */
    public void doAction(int oldStep) {
        switch (oldStep) {
	case 1:
	    String newName = suggestion;
	    if (step1 != null) {
		newName = step1.getText();
	    }
	    Object me = getModelElement();
	    UmlFactory.getFactory().getCore().buildOperation(me, newName);
        }
    }
    
    /**
     * @see org.argouml.kernel.Wizard#getNumSteps()
     */
    public int getNumSteps() {
        return 1;
    }
    
    /**
     * @return the offending modelelement
     */
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
    
    /**
     * @return the suggestion string
     */
    public String getSuggestion() {
        if (suggestion != null) return suggestion;
        Object me = getModelElement();
        if (me != null) {
            String n = ModelFacade.getName(me);
            return n;
        }
        return "";
    }
    
    /**
     * @param s the new suggestion string
     */
    public void setSuggestion(String s) {
	suggestion = s;
	origSuggest = s;
    }
    
    /**
     * @param s the new instructions
     */
    public void setInstructions(String s) { instructions = s; }
    
    /**
     * @param b
     */
    public void setMustEdit(boolean b) { mustEdit = b; }
    
    /** 
     * Create a new panel for the given step.
     * 
     * @see org.argouml.kernel.Wizard#makePanel(int)
     */
    public JPanel makePanel(int newStep) {
        switch (newStep) {
	case 1:
	    if (step1 == null) {
		step1 = new WizStepTextField(this, instructions,
					      label, getSuggestion());
	    }
	    return step1;
        }
        return null;
    }
    
}
